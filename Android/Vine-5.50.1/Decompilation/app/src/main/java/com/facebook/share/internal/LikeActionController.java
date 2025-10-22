package com.facebook.share.internal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.FacebookRequestError;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestBatch;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.LoggingBehavior;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.internal.AppCall;
import com.facebook.internal.BundleJSONConverter;
import com.facebook.internal.CallbackManagerImpl;
import com.facebook.internal.FileLruCache;
import com.facebook.internal.Logger;
import com.facebook.internal.NativeProtocol;
import com.facebook.internal.PlatformServiceClient;
import com.facebook.internal.Utility;
import com.facebook.internal.WorkQueue;
import com.facebook.share.internal.LikeContent;
import com.facebook.share.widget.LikeView;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class LikeActionController {
    private static AccessTokenTracker accessTokenTracker;
    private static FileLruCache controllerDiskCache;
    private static Handler handler;
    private static boolean isInitialized;
    private static String objectIdForPendingController;
    private static volatile int objectSuffix;
    private AppEventsLogger appEventsLogger;
    private Bundle facebookDialogAnalyticsBundle;
    private boolean isObjectLiked;
    private boolean isObjectLikedOnServer;
    private boolean isPendingLikeOrUnlike;
    private String likeCountStringWithLike;
    private String likeCountStringWithoutLike;
    private String objectId;
    private boolean objectIsPage;
    private LikeView.ObjectType objectType;
    private String socialSentenceWithLike;
    private String socialSentenceWithoutLike;
    private String unlikeToken;
    private String verifiedObjectId;
    private static final String TAG = LikeActionController.class.getSimpleName();
    private static final ConcurrentHashMap<String, LikeActionController> cache = new ConcurrentHashMap<>();
    private static WorkQueue mruCacheWorkQueue = new WorkQueue(1);
    private static WorkQueue diskIOWorkQueue = new WorkQueue(1);

    public interface CreationCallback {
        void onComplete(LikeActionController likeActionController, FacebookException facebookException);
    }

    private interface LikeRequestWrapper extends RequestWrapper {
        String getUnlikeToken();

        boolean isObjectLiked();
    }

    private interface RequestCompletionCallback {
        void onComplete();
    }

    private interface RequestWrapper {
        void addToBatch(GraphRequestBatch graphRequestBatch);

        FacebookRequestError getError();
    }

    public static boolean handleOnActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (Utility.isNullOrEmpty(objectIdForPendingController)) {
            Context appContext = FacebookSdk.getApplicationContext();
            SharedPreferences sharedPreferences = appContext.getSharedPreferences("com.facebook.LikeActionController.CONTROLLER_STORE_KEY", 0);
            objectIdForPendingController = sharedPreferences.getString("PENDING_CONTROLLER_KEY", null);
        }
        if (Utility.isNullOrEmpty(objectIdForPendingController)) {
            return false;
        }
        getControllerForObjectId(objectIdForPendingController, LikeView.ObjectType.UNKNOWN, new CreationCallback() { // from class: com.facebook.share.internal.LikeActionController.1
            @Override // com.facebook.share.internal.LikeActionController.CreationCallback
            public void onComplete(LikeActionController likeActionController, FacebookException error) {
                if (error == null) {
                    likeActionController.onActivityResult(requestCode, resultCode, data);
                } else {
                    Utility.logd(LikeActionController.TAG, error);
                }
            }
        });
        return true;
    }

    public static void getControllerForObjectId(String objectId, LikeView.ObjectType objectType, CreationCallback callback) {
        if (!isInitialized) {
            performFirstInitialize();
        }
        LikeActionController controllerForObject = getControllerFromInMemoryCache(objectId);
        if (controllerForObject != null) {
            verifyControllerAndInvokeCallback(controllerForObject, objectType, callback);
        } else {
            diskIOWorkQueue.addActiveWorkItem(new CreateLikeActionControllerWorkItem(objectId, objectType, callback));
        }
    }

    private static void verifyControllerAndInvokeCallback(LikeActionController likeActionController, LikeView.ObjectType objectType, CreationCallback callback) {
        LikeView.ObjectType bestObjectType = ShareInternalUtility.getMostSpecificObjectType(objectType, likeActionController.objectType);
        FacebookException error = null;
        if (bestObjectType == null) {
            error = new FacebookException("Object with id:\"%s\" is already marked as type:\"%s\". Cannot change the type to:\"%s\"", likeActionController.objectId, likeActionController.objectType.toString(), objectType.toString());
            likeActionController = null;
        } else {
            likeActionController.objectType = bestObjectType;
        }
        invokeCallbackWithController(callback, likeActionController, error);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void createControllerForObjectIdAndType(String objectId, LikeView.ObjectType objectType, CreationCallback callback) throws JSONException, IOException {
        LikeActionController controllerForObject = getControllerFromInMemoryCache(objectId);
        if (controllerForObject != null) {
            verifyControllerAndInvokeCallback(controllerForObject, objectType, callback);
            return;
        }
        LikeActionController controllerForObject2 = deserializeFromDiskSynchronously(objectId);
        if (controllerForObject2 == null) {
            controllerForObject2 = new LikeActionController(objectId, objectType);
            serializeToDiskAsync(controllerForObject2);
        }
        putControllerInMemoryCache(objectId, controllerForObject2);
        LikeActionController controllerToRefresh = controllerForObject2;
        handler.post(new Runnable() { // from class: com.facebook.share.internal.LikeActionController.2
            @Override // java.lang.Runnable
            public void run() {
                LikeActionController.this.refreshStatusAsync();
            }
        });
        invokeCallbackWithController(callback, controllerToRefresh, null);
    }

    private static synchronized void performFirstInitialize() {
        if (!isInitialized) {
            handler = new Handler(Looper.getMainLooper());
            Context appContext = FacebookSdk.getApplicationContext();
            SharedPreferences sharedPreferences = appContext.getSharedPreferences("com.facebook.LikeActionController.CONTROLLER_STORE_KEY", 0);
            objectSuffix = sharedPreferences.getInt("OBJECT_SUFFIX", 1);
            controllerDiskCache = new FileLruCache(TAG, new FileLruCache.Limits());
            registerAccessTokenTracker();
            CallbackManagerImpl.registerStaticCallback(CallbackManagerImpl.RequestCodeOffset.Like.toRequestCode(), new CallbackManagerImpl.Callback() { // from class: com.facebook.share.internal.LikeActionController.3
                @Override // com.facebook.internal.CallbackManagerImpl.Callback
                public boolean onActivityResult(int resultCode, Intent data) {
                    return LikeActionController.handleOnActivityResult(CallbackManagerImpl.RequestCodeOffset.Like.toRequestCode(), resultCode, data);
                }
            });
            isInitialized = true;
        }
    }

    private static void invokeCallbackWithController(final CreationCallback callback, final LikeActionController controller, final FacebookException error) {
        if (callback != null) {
            handler.post(new Runnable() { // from class: com.facebook.share.internal.LikeActionController.4
                @Override // java.lang.Runnable
                public void run() {
                    callback.onComplete(controller, error);
                }
            });
        }
    }

    private static void registerAccessTokenTracker() {
        accessTokenTracker = new AccessTokenTracker() { // from class: com.facebook.share.internal.LikeActionController.5
            @Override // com.facebook.AccessTokenTracker
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                Context appContext = FacebookSdk.getApplicationContext();
                if (currentAccessToken == null) {
                    int unused = LikeActionController.objectSuffix = (LikeActionController.objectSuffix + 1) % 1000;
                    appContext.getSharedPreferences("com.facebook.LikeActionController.CONTROLLER_STORE_KEY", 0).edit().putInt("OBJECT_SUFFIX", LikeActionController.objectSuffix).apply();
                    LikeActionController.cache.clear();
                    LikeActionController.controllerDiskCache.clearCache();
                }
                LikeActionController.broadcastAction(null, "com.facebook.sdk.LikeActionController.DID_RESET");
            }
        };
    }

    private static void putControllerInMemoryCache(String objectId, LikeActionController controllerForObject) {
        String cacheKey = getCacheKeyForObjectId(objectId);
        mruCacheWorkQueue.addActiveWorkItem(new MRUCacheWorkItem(cacheKey, true));
        cache.put(cacheKey, controllerForObject);
    }

    private static LikeActionController getControllerFromInMemoryCache(String objectId) {
        String cacheKey = getCacheKeyForObjectId(objectId);
        LikeActionController controller = cache.get(cacheKey);
        if (controller != null) {
            mruCacheWorkQueue.addActiveWorkItem(new MRUCacheWorkItem(cacheKey, false));
        }
        return controller;
    }

    private static void serializeToDiskAsync(LikeActionController controller) throws JSONException {
        String controllerJson = serializeToJson(controller);
        String cacheKey = getCacheKeyForObjectId(controller.objectId);
        if (!Utility.isNullOrEmpty(controllerJson) && !Utility.isNullOrEmpty(cacheKey)) {
            diskIOWorkQueue.addActiveWorkItem(new SerializeToDiskWorkItem(cacheKey, controllerJson));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void serializeToDiskSynchronously(String cacheKey, String controllerJson) throws IOException {
        OutputStream outputStream = null;
        try {
            try {
                outputStream = controllerDiskCache.openPutStream(cacheKey);
                outputStream.write(controllerJson.getBytes());
                if (outputStream != null) {
                    Utility.closeQuietly(outputStream);
                }
            } catch (IOException e) {
                Log.e(TAG, "Unable to serialize controller to disk", e);
                if (outputStream != null) {
                    Utility.closeQuietly(outputStream);
                }
            }
        } catch (Throwable th) {
            if (outputStream != null) {
                Utility.closeQuietly(outputStream);
            }
            throw th;
        }
    }

    private static LikeActionController deserializeFromDiskSynchronously(String objectId) throws IOException {
        LikeActionController controller = null;
        InputStream inputStream = null;
        try {
            try {
                String cacheKey = getCacheKeyForObjectId(objectId);
                inputStream = controllerDiskCache.get(cacheKey);
                if (inputStream != null) {
                    String controllerJsonString = Utility.readStreamToString(inputStream);
                    if (!Utility.isNullOrEmpty(controllerJsonString)) {
                        controller = deserializeFromJson(controllerJsonString);
                    }
                }
            } catch (IOException e) {
                Log.e(TAG, "Unable to deserialize controller from disk", e);
                controller = null;
                if (inputStream != null) {
                    Utility.closeQuietly(inputStream);
                }
            }
            return controller;
        } finally {
            if (inputStream != null) {
                Utility.closeQuietly(inputStream);
            }
        }
    }

    private static LikeActionController deserializeFromJson(String controllerJsonString) throws JSONException {
        try {
            JSONObject controllerJson = new JSONObject(controllerJsonString);
            int version = controllerJson.optInt("com.facebook.share.internal.LikeActionController.version", -1);
            if (version != 3) {
                return null;
            }
            String objectId = controllerJson.getString("object_id");
            int objectTypeInt = controllerJson.optInt("object_type", LikeView.ObjectType.UNKNOWN.getValue());
            LikeActionController controller = new LikeActionController(objectId, LikeView.ObjectType.fromInt(objectTypeInt));
            controller.likeCountStringWithLike = controllerJson.optString("like_count_string_with_like", null);
            controller.likeCountStringWithoutLike = controllerJson.optString("like_count_string_without_like", null);
            controller.socialSentenceWithLike = controllerJson.optString("social_sentence_with_like", null);
            controller.socialSentenceWithoutLike = controllerJson.optString("social_sentence_without_like", null);
            controller.isObjectLiked = controllerJson.optBoolean("is_object_liked");
            controller.unlikeToken = controllerJson.optString("unlike_token", null);
            JSONObject analyticsJSON = controllerJson.optJSONObject("facebook_dialog_analytics_bundle");
            if (analyticsJSON != null) {
                controller.facebookDialogAnalyticsBundle = BundleJSONConverter.convertToBundle(analyticsJSON);
                return controller;
            }
            return controller;
        } catch (JSONException e) {
            Log.e(TAG, "Unable to deserialize controller from JSON", e);
            return null;
        }
    }

    private static String serializeToJson(LikeActionController controller) throws JSONException {
        JSONObject analyticsJSON;
        JSONObject controllerJson = new JSONObject();
        try {
            controllerJson.put("com.facebook.share.internal.LikeActionController.version", 3);
            controllerJson.put("object_id", controller.objectId);
            controllerJson.put("object_type", controller.objectType.getValue());
            controllerJson.put("like_count_string_with_like", controller.likeCountStringWithLike);
            controllerJson.put("like_count_string_without_like", controller.likeCountStringWithoutLike);
            controllerJson.put("social_sentence_with_like", controller.socialSentenceWithLike);
            controllerJson.put("social_sentence_without_like", controller.socialSentenceWithoutLike);
            controllerJson.put("is_object_liked", controller.isObjectLiked);
            controllerJson.put("unlike_token", controller.unlikeToken);
            if (controller.facebookDialogAnalyticsBundle != null && (analyticsJSON = BundleJSONConverter.convertToJSON(controller.facebookDialogAnalyticsBundle)) != null) {
                controllerJson.put("facebook_dialog_analytics_bundle", analyticsJSON);
            }
            return controllerJson.toString();
        } catch (JSONException e) {
            Log.e(TAG, "Unable to serialize controller to JSON", e);
            return null;
        }
    }

    private static String getCacheKeyForObjectId(String objectId) {
        String accessTokenPortion = null;
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            accessTokenPortion = accessToken.getToken();
        }
        if (accessTokenPortion != null) {
            accessTokenPortion = Utility.md5hash(accessTokenPortion);
        }
        return String.format(Locale.ROOT, "%s|%s|com.fb.sdk.like|%d", objectId, Utility.coerceValueIfNullOrEmpty(accessTokenPortion, ""), Integer.valueOf(objectSuffix));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void broadcastAction(LikeActionController controller, String action) {
        broadcastAction(controller, action, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void broadcastAction(LikeActionController controller, String action, Bundle data) {
        Intent broadcastIntent = new Intent(action);
        if (controller != null) {
            if (data == null) {
                data = new Bundle();
            }
            data.putString("com.facebook.sdk.LikeActionController.OBJECT_ID", controller.getObjectId());
        }
        if (data != null) {
            broadcastIntent.putExtras(data);
        }
        LocalBroadcastManager.getInstance(FacebookSdk.getApplicationContext()).sendBroadcast(broadcastIntent);
    }

    private LikeActionController(String objectId, LikeView.ObjectType objectType) {
        this.objectId = objectId;
        this.objectType = objectType;
    }

    public String getObjectId() {
        return this.objectId;
    }

    public String getLikeCountString() {
        return this.isObjectLiked ? this.likeCountStringWithLike : this.likeCountStringWithoutLike;
    }

    public String getSocialSentence() {
        return this.isObjectLiked ? this.socialSentenceWithLike : this.socialSentenceWithoutLike;
    }

    public boolean isObjectLiked() {
        return this.isObjectLiked;
    }

    public boolean shouldEnableView() {
        if (LikeDialog.canShowNativeDialog() || LikeDialog.canShowWebFallback()) {
            return true;
        }
        if (this.objectIsPage || this.objectType == LikeView.ObjectType.PAGE) {
            return false;
        }
        AccessToken token = AccessToken.getCurrentAccessToken();
        return (token == null || token.getPermissions() == null || !token.getPermissions().contains("publish_actions")) ? false : true;
    }

    public void toggleLike(Activity activity, Fragment fragment, Bundle analyticsParameters) {
        boolean shouldLikeObject = !this.isObjectLiked;
        if (canUseOGPublish()) {
            updateLikeState(shouldLikeObject);
            if (this.isPendingLikeOrUnlike) {
                getAppEventsLogger().logSdkEvent("fb_like_control_did_undo_quickly", null, analyticsParameters);
                return;
            } else {
                if (!publishLikeOrUnlikeAsync(shouldLikeObject, analyticsParameters)) {
                    updateLikeState(shouldLikeObject ? false : true);
                    presentLikeDialog(activity, fragment, analyticsParameters);
                    return;
                }
                return;
            }
        }
        presentLikeDialog(activity, fragment, analyticsParameters);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public AppEventsLogger getAppEventsLogger() {
        if (this.appEventsLogger == null) {
            this.appEventsLogger = AppEventsLogger.newLogger(FacebookSdk.getApplicationContext());
        }
        return this.appEventsLogger;
    }

    private boolean publishLikeOrUnlikeAsync(boolean shouldLikeObject, Bundle analyticsParameters) {
        if (!canUseOGPublish()) {
            return false;
        }
        if (shouldLikeObject) {
            publishLikeAsync(analyticsParameters);
            return true;
        }
        if (Utility.isNullOrEmpty(this.unlikeToken)) {
            return false;
        }
        publishUnlikeAsync(analyticsParameters);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void publishDidError(boolean oldLikeState) throws JSONException {
        updateLikeState(oldLikeState);
        Bundle errorBundle = new Bundle();
        errorBundle.putString("com.facebook.platform.status.ERROR_DESCRIPTION", "Unable to publish the like/unlike action");
        broadcastAction(this, "com.facebook.sdk.LikeActionController.DID_ERROR", errorBundle);
    }

    private void updateLikeState(boolean isObjectLiked) throws JSONException {
        updateState(isObjectLiked, this.likeCountStringWithLike, this.likeCountStringWithoutLike, this.socialSentenceWithLike, this.socialSentenceWithoutLike, this.unlikeToken);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateState(boolean isObjectLiked, String likeCountStringWithLike, String likeCountStringWithoutLike, String socialSentenceWithLike, String socialSentenceWithoutLike, String unlikeToken) throws JSONException {
        String likeCountStringWithLike2 = Utility.coerceValueIfNullOrEmpty(likeCountStringWithLike, null);
        String likeCountStringWithoutLike2 = Utility.coerceValueIfNullOrEmpty(likeCountStringWithoutLike, null);
        String socialSentenceWithLike2 = Utility.coerceValueIfNullOrEmpty(socialSentenceWithLike, null);
        String socialSentenceWithoutLike2 = Utility.coerceValueIfNullOrEmpty(socialSentenceWithoutLike, null);
        String unlikeToken2 = Utility.coerceValueIfNullOrEmpty(unlikeToken, null);
        boolean stateChanged = (isObjectLiked == this.isObjectLiked && Utility.areObjectsEqual(likeCountStringWithLike2, this.likeCountStringWithLike) && Utility.areObjectsEqual(likeCountStringWithoutLike2, this.likeCountStringWithoutLike) && Utility.areObjectsEqual(socialSentenceWithLike2, this.socialSentenceWithLike) && Utility.areObjectsEqual(socialSentenceWithoutLike2, this.socialSentenceWithoutLike) && Utility.areObjectsEqual(unlikeToken2, this.unlikeToken)) ? false : true;
        if (stateChanged) {
            this.isObjectLiked = isObjectLiked;
            this.likeCountStringWithLike = likeCountStringWithLike2;
            this.likeCountStringWithoutLike = likeCountStringWithoutLike2;
            this.socialSentenceWithLike = socialSentenceWithLike2;
            this.socialSentenceWithoutLike = socialSentenceWithoutLike2;
            this.unlikeToken = unlikeToken2;
            serializeToDiskAsync(this);
            broadcastAction(this, "com.facebook.sdk.LikeActionController.UPDATED");
        }
    }

    private void presentLikeDialog(Activity activity, Fragment fragment, Bundle analyticsParameters) throws JSONException {
        String objectTypeString;
        String analyticsEvent = null;
        if (LikeDialog.canShowNativeDialog()) {
            analyticsEvent = "fb_like_control_did_present_dialog";
        } else if (LikeDialog.canShowWebFallback()) {
            analyticsEvent = "fb_like_control_did_present_fallback_dialog";
        } else {
            logAppEventForError("present_dialog", analyticsParameters);
            Utility.logd(TAG, "Cannot show the Like Dialog on this device.");
            broadcastAction(null, "com.facebook.sdk.LikeActionController.UPDATED");
        }
        if (analyticsEvent != null) {
            if (this.objectType != null) {
                objectTypeString = this.objectType.toString();
            } else {
                objectTypeString = LikeView.ObjectType.UNKNOWN.toString();
            }
            LikeContent likeContent = new LikeContent.Builder().setObjectId(this.objectId).setObjectType(objectTypeString).build();
            if (fragment != null) {
                new LikeDialog(fragment).show(likeContent);
            } else {
                new LikeDialog(activity).show(likeContent);
            }
            saveState(analyticsParameters);
            getAppEventsLogger().logSdkEvent("fb_like_control_did_present_dialog", null, analyticsParameters);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ShareInternalUtility.handleActivityResult(requestCode, resultCode, data, getResultProcessor(this.facebookDialogAnalyticsBundle));
        clearState();
    }

    private ResultProcessor getResultProcessor(final Bundle analyticsParameters) {
        return new ResultProcessor(null) { // from class: com.facebook.share.internal.LikeActionController.6
            @Override // com.facebook.share.internal.ResultProcessor
            public void onSuccess(AppCall appCall, Bundle data) throws JSONException {
                String unlikeToken;
                if (data != null && data.containsKey("object_is_liked")) {
                    boolean isObjectLiked = data.getBoolean("object_is_liked");
                    String likeCountStringWithLike = LikeActionController.this.likeCountStringWithLike;
                    String likeCountStringWithoutLike = LikeActionController.this.likeCountStringWithoutLike;
                    if (data.containsKey("like_count_string")) {
                        likeCountStringWithLike = data.getString("like_count_string");
                        likeCountStringWithoutLike = likeCountStringWithLike;
                    }
                    String socialSentenceWithLike = LikeActionController.this.socialSentenceWithLike;
                    String socialSentenceWithoutWithoutLike = LikeActionController.this.socialSentenceWithoutLike;
                    if (data.containsKey("social_sentence")) {
                        socialSentenceWithLike = data.getString("social_sentence");
                        socialSentenceWithoutWithoutLike = socialSentenceWithLike;
                    }
                    if (!data.containsKey("object_is_liked")) {
                        unlikeToken = LikeActionController.this.unlikeToken;
                    } else {
                        unlikeToken = data.getString("unlike_token");
                    }
                    Bundle logParams = analyticsParameters == null ? new Bundle() : analyticsParameters;
                    logParams.putString("call_id", appCall.getCallId().toString());
                    LikeActionController.this.getAppEventsLogger().logSdkEvent("fb_like_control_dialog_did_succeed", null, logParams);
                    LikeActionController.this.updateState(isObjectLiked, likeCountStringWithLike, likeCountStringWithoutLike, socialSentenceWithLike, socialSentenceWithoutWithoutLike, unlikeToken);
                }
            }

            @Override // com.facebook.share.internal.ResultProcessor
            public void onError(AppCall appCall, FacebookException error) {
                Logger.log(LoggingBehavior.REQUESTS, LikeActionController.TAG, "Like Dialog failed with error : %s", error);
                Bundle logParams = analyticsParameters == null ? new Bundle() : analyticsParameters;
                logParams.putString("call_id", appCall.getCallId().toString());
                LikeActionController.this.logAppEventForError("present_dialog", logParams);
                LikeActionController.broadcastAction(LikeActionController.this, "com.facebook.sdk.LikeActionController.DID_ERROR", NativeProtocol.createBundleForException(error));
            }

            @Override // com.facebook.share.internal.ResultProcessor
            public void onCancel(AppCall appCall) {
                onError(appCall, new FacebookOperationCanceledException());
            }
        };
    }

    private void saveState(Bundle analyticsParameters) throws JSONException {
        storeObjectIdForPendingController(this.objectId);
        this.facebookDialogAnalyticsBundle = analyticsParameters;
        serializeToDiskAsync(this);
    }

    private void clearState() {
        this.facebookDialogAnalyticsBundle = null;
        storeObjectIdForPendingController(null);
    }

    private static void storeObjectIdForPendingController(String objectId) {
        objectIdForPendingController = objectId;
        Context appContext = FacebookSdk.getApplicationContext();
        appContext.getSharedPreferences("com.facebook.LikeActionController.CONTROLLER_STORE_KEY", 0).edit().putString("PENDING_CONTROLLER_KEY", objectIdForPendingController).apply();
    }

    private boolean canUseOGPublish() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return (this.objectIsPage || this.verifiedObjectId == null || accessToken == null || accessToken.getPermissions() == null || !accessToken.getPermissions().contains("publish_actions")) ? false : true;
    }

    private void publishLikeAsync(final Bundle analyticsParameters) {
        this.isPendingLikeOrUnlike = true;
        fetchVerifiedObjectId(new RequestCompletionCallback() { // from class: com.facebook.share.internal.LikeActionController.7
            @Override // com.facebook.share.internal.LikeActionController.RequestCompletionCallback
            public void onComplete() {
                if (Utility.isNullOrEmpty(LikeActionController.this.verifiedObjectId)) {
                    Bundle errorBundle = new Bundle();
                    errorBundle.putString("com.facebook.platform.status.ERROR_DESCRIPTION", "Invalid Object Id");
                    LikeActionController.broadcastAction(LikeActionController.this, "com.facebook.sdk.LikeActionController.DID_ERROR", errorBundle);
                } else {
                    GraphRequestBatch requestBatch = new GraphRequestBatch();
                    final PublishLikeRequestWrapper likeRequest = LikeActionController.this.new PublishLikeRequestWrapper(LikeActionController.this.verifiedObjectId, LikeActionController.this.objectType);
                    likeRequest.addToBatch(requestBatch);
                    requestBatch.addCallback(new GraphRequestBatch.Callback() { // from class: com.facebook.share.internal.LikeActionController.7.1
                        @Override // com.facebook.GraphRequestBatch.Callback
                        public void onBatchCompleted(GraphRequestBatch batch) throws JSONException {
                            LikeActionController.this.isPendingLikeOrUnlike = false;
                            if (likeRequest.getError() != null) {
                                LikeActionController.this.publishDidError(false);
                                return;
                            }
                            LikeActionController.this.unlikeToken = Utility.coerceValueIfNullOrEmpty(likeRequest.unlikeToken, null);
                            LikeActionController.this.isObjectLikedOnServer = true;
                            LikeActionController.this.getAppEventsLogger().logSdkEvent("fb_like_control_did_like", null, analyticsParameters);
                            LikeActionController.this.publishAgainIfNeeded(analyticsParameters);
                        }
                    });
                    requestBatch.executeAsync();
                }
            }
        });
    }

    private void publishUnlikeAsync(final Bundle analyticsParameters) {
        this.isPendingLikeOrUnlike = true;
        GraphRequestBatch requestBatch = new GraphRequestBatch();
        final PublishUnlikeRequestWrapper unlikeRequest = new PublishUnlikeRequestWrapper(this.unlikeToken);
        unlikeRequest.addToBatch(requestBatch);
        requestBatch.addCallback(new GraphRequestBatch.Callback() { // from class: com.facebook.share.internal.LikeActionController.8
            @Override // com.facebook.GraphRequestBatch.Callback
            public void onBatchCompleted(GraphRequestBatch batch) throws JSONException {
                LikeActionController.this.isPendingLikeOrUnlike = false;
                if (unlikeRequest.getError() != null) {
                    LikeActionController.this.publishDidError(true);
                    return;
                }
                LikeActionController.this.unlikeToken = null;
                LikeActionController.this.isObjectLikedOnServer = false;
                LikeActionController.this.getAppEventsLogger().logSdkEvent("fb_like_control_did_unlike", null, analyticsParameters);
                LikeActionController.this.publishAgainIfNeeded(analyticsParameters);
            }
        });
        requestBatch.executeAsync();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshStatusAsync() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken == null) {
            refreshStatusViaService();
        } else {
            fetchVerifiedObjectId(new RequestCompletionCallback() { // from class: com.facebook.share.internal.LikeActionController.9
                @Override // com.facebook.share.internal.LikeActionController.RequestCompletionCallback
                public void onComplete() {
                    final LikeRequestWrapper likeRequestWrapper;
                    switch (LikeActionController.this.objectType) {
                        case PAGE:
                            likeRequestWrapper = LikeActionController.this.new GetPageLikesRequestWrapper(LikeActionController.this.verifiedObjectId);
                            break;
                        default:
                            likeRequestWrapper = LikeActionController.this.new GetOGObjectLikesRequestWrapper(LikeActionController.this.verifiedObjectId, LikeActionController.this.objectType);
                            break;
                    }
                    final GetEngagementRequestWrapper engagementRequest = LikeActionController.this.new GetEngagementRequestWrapper(LikeActionController.this.verifiedObjectId, LikeActionController.this.objectType);
                    GraphRequestBatch requestBatch = new GraphRequestBatch();
                    likeRequestWrapper.addToBatch(requestBatch);
                    engagementRequest.addToBatch(requestBatch);
                    requestBatch.addCallback(new GraphRequestBatch.Callback() { // from class: com.facebook.share.internal.LikeActionController.9.1
                        @Override // com.facebook.GraphRequestBatch.Callback
                        public void onBatchCompleted(GraphRequestBatch batch) throws JSONException {
                            if (likeRequestWrapper.getError() == null && engagementRequest.getError() == null) {
                                LikeActionController.this.updateState(likeRequestWrapper.isObjectLiked(), engagementRequest.likeCountStringWithLike, engagementRequest.likeCountStringWithoutLike, engagementRequest.socialSentenceStringWithLike, engagementRequest.socialSentenceStringWithoutLike, likeRequestWrapper.getUnlikeToken());
                            } else {
                                Logger.log(LoggingBehavior.REQUESTS, LikeActionController.TAG, "Unable to refresh like state for id: '%s'", LikeActionController.this.objectId);
                            }
                        }
                    });
                    requestBatch.executeAsync();
                }
            });
        }
    }

    private void refreshStatusViaService() {
        LikeStatusClient likeStatusClient = new LikeStatusClient(FacebookSdk.getApplicationContext(), FacebookSdk.getApplicationId(), this.objectId);
        if (likeStatusClient.start()) {
            PlatformServiceClient.CompletedListener callback = new PlatformServiceClient.CompletedListener() { // from class: com.facebook.share.internal.LikeActionController.10
                @Override // com.facebook.internal.PlatformServiceClient.CompletedListener
                public void completed(Bundle result) throws JSONException {
                    String likeCountWithLike;
                    String likeCountWithoutLike;
                    String socialSentenceWithLike;
                    String socialSentenceWithoutLike;
                    String unlikeToken;
                    if (result != null && result.containsKey("com.facebook.platform.extra.OBJECT_IS_LIKED")) {
                        boolean objectIsLiked = result.getBoolean("com.facebook.platform.extra.OBJECT_IS_LIKED");
                        if (!result.containsKey("com.facebook.platform.extra.LIKE_COUNT_STRING_WITH_LIKE")) {
                            likeCountWithLike = LikeActionController.this.likeCountStringWithLike;
                        } else {
                            likeCountWithLike = result.getString("com.facebook.platform.extra.LIKE_COUNT_STRING_WITH_LIKE");
                        }
                        if (!result.containsKey("com.facebook.platform.extra.LIKE_COUNT_STRING_WITHOUT_LIKE")) {
                            likeCountWithoutLike = LikeActionController.this.likeCountStringWithoutLike;
                        } else {
                            likeCountWithoutLike = result.getString("com.facebook.platform.extra.LIKE_COUNT_STRING_WITHOUT_LIKE");
                        }
                        if (!result.containsKey("com.facebook.platform.extra.SOCIAL_SENTENCE_WITH_LIKE")) {
                            socialSentenceWithLike = LikeActionController.this.socialSentenceWithLike;
                        } else {
                            socialSentenceWithLike = result.getString("com.facebook.platform.extra.SOCIAL_SENTENCE_WITH_LIKE");
                        }
                        if (!result.containsKey("com.facebook.platform.extra.SOCIAL_SENTENCE_WITHOUT_LIKE")) {
                            socialSentenceWithoutLike = LikeActionController.this.socialSentenceWithoutLike;
                        } else {
                            socialSentenceWithoutLike = result.getString("com.facebook.platform.extra.SOCIAL_SENTENCE_WITHOUT_LIKE");
                        }
                        if (!result.containsKey("com.facebook.platform.extra.UNLIKE_TOKEN")) {
                            unlikeToken = LikeActionController.this.unlikeToken;
                        } else {
                            unlikeToken = result.getString("com.facebook.platform.extra.UNLIKE_TOKEN");
                        }
                        LikeActionController.this.updateState(objectIsLiked, likeCountWithLike, likeCountWithoutLike, socialSentenceWithLike, socialSentenceWithoutLike, unlikeToken);
                    }
                }
            };
            likeStatusClient.setCompletedListener(callback);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void publishAgainIfNeeded(Bundle analyticsParameters) throws JSONException {
        if (this.isObjectLiked != this.isObjectLikedOnServer && !publishLikeOrUnlikeAsync(this.isObjectLiked, analyticsParameters)) {
            publishDidError(!this.isObjectLiked);
        }
    }

    private void fetchVerifiedObjectId(final RequestCompletionCallback completionHandler) {
        if (!Utility.isNullOrEmpty(this.verifiedObjectId)) {
            if (completionHandler != null) {
                completionHandler.onComplete();
                return;
            }
            return;
        }
        final GetOGObjectIdRequestWrapper objectIdRequest = new GetOGObjectIdRequestWrapper(this.objectId, this.objectType);
        final GetPageIdRequestWrapper pageIdRequest = new GetPageIdRequestWrapper(this.objectId, this.objectType);
        GraphRequestBatch requestBatch = new GraphRequestBatch();
        objectIdRequest.addToBatch(requestBatch);
        pageIdRequest.addToBatch(requestBatch);
        requestBatch.addCallback(new GraphRequestBatch.Callback() { // from class: com.facebook.share.internal.LikeActionController.11
            @Override // com.facebook.GraphRequestBatch.Callback
            public void onBatchCompleted(GraphRequestBatch batch) {
                FacebookRequestError error;
                LikeActionController.this.verifiedObjectId = objectIdRequest.verifiedObjectId;
                if (Utility.isNullOrEmpty(LikeActionController.this.verifiedObjectId)) {
                    LikeActionController.this.verifiedObjectId = pageIdRequest.verifiedObjectId;
                    LikeActionController.this.objectIsPage = pageIdRequest.objectIsPage;
                }
                if (Utility.isNullOrEmpty(LikeActionController.this.verifiedObjectId)) {
                    Logger.log(LoggingBehavior.DEVELOPER_ERRORS, LikeActionController.TAG, "Unable to verify the FB id for '%s'. Verify that it is a valid FB object or page", LikeActionController.this.objectId);
                    LikeActionController likeActionController = LikeActionController.this;
                    if (pageIdRequest.getError() != null) {
                        error = pageIdRequest.getError();
                    } else {
                        error = objectIdRequest.getError();
                    }
                    likeActionController.logAppEventForError("get_verified_id", error);
                }
                if (completionHandler != null) {
                    completionHandler.onComplete();
                }
            }
        });
        requestBatch.executeAsync();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logAppEventForError(String action, Bundle parameters) {
        Bundle logParams = new Bundle(parameters);
        logParams.putString("object_id", this.objectId);
        logParams.putString("object_type", this.objectType.toString());
        logParams.putString("current_action", action);
        getAppEventsLogger().logSdkEvent("fb_like_control_error", null, logParams);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logAppEventForError(String action, FacebookRequestError error) {
        JSONObject requestResult;
        Bundle logParams = new Bundle();
        if (error != null && (requestResult = error.getRequestResult()) != null) {
            logParams.putString("error", requestResult.toString());
        }
        logAppEventForError(action, logParams);
    }

    private class GetOGObjectIdRequestWrapper extends AbstractRequestWrapper {
        String verifiedObjectId;

        GetOGObjectIdRequestWrapper(String objectId, LikeView.ObjectType objectType) {
            super(objectId, objectType);
            Bundle objectIdRequestParams = new Bundle();
            objectIdRequestParams.putString("fields", "og_object.fields(id)");
            objectIdRequestParams.putString("ids", objectId);
            setRequest(new GraphRequest(AccessToken.getCurrentAccessToken(), "", objectIdRequestParams, HttpMethod.GET));
        }

        @Override // com.facebook.share.internal.LikeActionController.AbstractRequestWrapper
        protected void processError(FacebookRequestError error) {
            if (error.getErrorMessage().contains("og_object")) {
                this.error = null;
            } else {
                Logger.log(LoggingBehavior.REQUESTS, LikeActionController.TAG, "Error getting the FB id for object '%s' with type '%s' : %s", this.objectId, this.objectType, error);
            }
        }

        @Override // com.facebook.share.internal.LikeActionController.AbstractRequestWrapper
        protected void processSuccess(GraphResponse response) {
            JSONObject ogObject;
            JSONObject results = Utility.tryGetJSONObjectFromResponse(response.getJSONObject(), this.objectId);
            if (results != null && (ogObject = results.optJSONObject("og_object")) != null) {
                this.verifiedObjectId = ogObject.optString("id");
            }
        }
    }

    private class GetPageIdRequestWrapper extends AbstractRequestWrapper {
        boolean objectIsPage;
        String verifiedObjectId;

        GetPageIdRequestWrapper(String objectId, LikeView.ObjectType objectType) {
            super(objectId, objectType);
            Bundle pageIdRequestParams = new Bundle();
            pageIdRequestParams.putString("fields", "id");
            pageIdRequestParams.putString("ids", objectId);
            setRequest(new GraphRequest(AccessToken.getCurrentAccessToken(), "", pageIdRequestParams, HttpMethod.GET));
        }

        @Override // com.facebook.share.internal.LikeActionController.AbstractRequestWrapper
        protected void processSuccess(GraphResponse response) {
            JSONObject results = Utility.tryGetJSONObjectFromResponse(response.getJSONObject(), this.objectId);
            if (results != null) {
                this.verifiedObjectId = results.optString("id");
                this.objectIsPage = !Utility.isNullOrEmpty(this.verifiedObjectId);
            }
        }

        @Override // com.facebook.share.internal.LikeActionController.AbstractRequestWrapper
        protected void processError(FacebookRequestError error) {
            Logger.log(LoggingBehavior.REQUESTS, LikeActionController.TAG, "Error getting the FB id for object '%s' with type '%s' : %s", this.objectId, this.objectType, error);
        }
    }

    private class PublishLikeRequestWrapper extends AbstractRequestWrapper {
        String unlikeToken;

        PublishLikeRequestWrapper(String objectId, LikeView.ObjectType objectType) {
            super(objectId, objectType);
            Bundle likeRequestParams = new Bundle();
            likeRequestParams.putString("object", objectId);
            setRequest(new GraphRequest(AccessToken.getCurrentAccessToken(), "me/og.likes", likeRequestParams, HttpMethod.POST));
        }

        @Override // com.facebook.share.internal.LikeActionController.AbstractRequestWrapper
        protected void processSuccess(GraphResponse response) {
            this.unlikeToken = Utility.safeGetStringFromResponse(response.getJSONObject(), "id");
        }

        @Override // com.facebook.share.internal.LikeActionController.AbstractRequestWrapper
        protected void processError(FacebookRequestError error) {
            int errorCode = error.getErrorCode();
            if (errorCode == 3501) {
                this.error = null;
            } else {
                Logger.log(LoggingBehavior.REQUESTS, LikeActionController.TAG, "Error liking object '%s' with type '%s' : %s", this.objectId, this.objectType, error);
                LikeActionController.this.logAppEventForError("publish_like", error);
            }
        }
    }

    private class PublishUnlikeRequestWrapper extends AbstractRequestWrapper {
        private String unlikeToken;

        PublishUnlikeRequestWrapper(String unlikeToken) {
            super(null, null);
            this.unlikeToken = unlikeToken;
            setRequest(new GraphRequest(AccessToken.getCurrentAccessToken(), unlikeToken, null, HttpMethod.DELETE));
        }

        @Override // com.facebook.share.internal.LikeActionController.AbstractRequestWrapper
        protected void processSuccess(GraphResponse response) {
        }

        @Override // com.facebook.share.internal.LikeActionController.AbstractRequestWrapper
        protected void processError(FacebookRequestError error) {
            Logger.log(LoggingBehavior.REQUESTS, LikeActionController.TAG, "Error unliking object with unlike token '%s' : %s", this.unlikeToken, error);
            LikeActionController.this.logAppEventForError("publish_unlike", error);
        }
    }

    private class GetPageLikesRequestWrapper extends AbstractRequestWrapper implements LikeRequestWrapper {
        private boolean objectIsLiked;
        private String pageId;

        GetPageLikesRequestWrapper(String pageId) {
            super(pageId, LikeView.ObjectType.PAGE);
            this.objectIsLiked = LikeActionController.this.isObjectLiked;
            this.pageId = pageId;
            Bundle requestParams = new Bundle();
            requestParams.putString("fields", "id");
            setRequest(new GraphRequest(AccessToken.getCurrentAccessToken(), "me/likes/" + pageId, requestParams, HttpMethod.GET));
        }

        @Override // com.facebook.share.internal.LikeActionController.AbstractRequestWrapper
        protected void processSuccess(GraphResponse response) {
            JSONArray dataSet = Utility.tryGetJSONArrayFromResponse(response.getJSONObject(), "data");
            if (dataSet != null && dataSet.length() > 0) {
                this.objectIsLiked = true;
            }
        }

        @Override // com.facebook.share.internal.LikeActionController.AbstractRequestWrapper
        protected void processError(FacebookRequestError error) {
            Logger.log(LoggingBehavior.REQUESTS, LikeActionController.TAG, "Error fetching like status for page id '%s': %s", this.pageId, error);
            LikeActionController.this.logAppEventForError("get_page_like", error);
        }

        @Override // com.facebook.share.internal.LikeActionController.LikeRequestWrapper
        public boolean isObjectLiked() {
            return this.objectIsLiked;
        }

        @Override // com.facebook.share.internal.LikeActionController.LikeRequestWrapper
        public String getUnlikeToken() {
            return null;
        }
    }

    private class GetOGObjectLikesRequestWrapper extends AbstractRequestWrapper implements LikeRequestWrapper {
        private final String objectId;
        private boolean objectIsLiked;
        private final LikeView.ObjectType objectType;
        private String unlikeToken;

        GetOGObjectLikesRequestWrapper(String objectId, LikeView.ObjectType objectType) {
            super(objectId, objectType);
            this.objectIsLiked = LikeActionController.this.isObjectLiked;
            this.objectId = objectId;
            this.objectType = objectType;
            Bundle requestParams = new Bundle();
            requestParams.putString("fields", "id,application");
            requestParams.putString("object", this.objectId);
            setRequest(new GraphRequest(AccessToken.getCurrentAccessToken(), "me/og.likes", requestParams, HttpMethod.GET));
        }

        @Override // com.facebook.share.internal.LikeActionController.AbstractRequestWrapper
        protected void processSuccess(GraphResponse response) {
            JSONArray dataSet = Utility.tryGetJSONArrayFromResponse(response.getJSONObject(), "data");
            if (dataSet != null) {
                for (int i = 0; i < dataSet.length(); i++) {
                    JSONObject data = dataSet.optJSONObject(i);
                    if (data != null) {
                        this.objectIsLiked = true;
                        JSONObject appData = data.optJSONObject("application");
                        AccessToken accessToken = AccessToken.getCurrentAccessToken();
                        if (appData != null && accessToken != null && Utility.areObjectsEqual(accessToken.getApplicationId(), appData.optString("id"))) {
                            this.unlikeToken = data.optString("id");
                        }
                    }
                }
            }
        }

        @Override // com.facebook.share.internal.LikeActionController.AbstractRequestWrapper
        protected void processError(FacebookRequestError error) {
            Logger.log(LoggingBehavior.REQUESTS, LikeActionController.TAG, "Error fetching like status for object '%s' with type '%s' : %s", this.objectId, this.objectType, error);
            LikeActionController.this.logAppEventForError("get_og_object_like", error);
        }

        @Override // com.facebook.share.internal.LikeActionController.LikeRequestWrapper
        public boolean isObjectLiked() {
            return this.objectIsLiked;
        }

        @Override // com.facebook.share.internal.LikeActionController.LikeRequestWrapper
        public String getUnlikeToken() {
            return this.unlikeToken;
        }
    }

    private class GetEngagementRequestWrapper extends AbstractRequestWrapper {
        String likeCountStringWithLike;
        String likeCountStringWithoutLike;
        String socialSentenceStringWithLike;
        String socialSentenceStringWithoutLike;

        GetEngagementRequestWrapper(String objectId, LikeView.ObjectType objectType) {
            super(objectId, objectType);
            this.likeCountStringWithLike = LikeActionController.this.likeCountStringWithLike;
            this.likeCountStringWithoutLike = LikeActionController.this.likeCountStringWithoutLike;
            this.socialSentenceStringWithLike = LikeActionController.this.socialSentenceWithLike;
            this.socialSentenceStringWithoutLike = LikeActionController.this.socialSentenceWithoutLike;
            Bundle requestParams = new Bundle();
            requestParams.putString("fields", "engagement.fields(count_string_with_like,count_string_without_like,social_sentence_with_like,social_sentence_without_like)");
            setRequest(new GraphRequest(AccessToken.getCurrentAccessToken(), objectId, requestParams, HttpMethod.GET));
        }

        @Override // com.facebook.share.internal.LikeActionController.AbstractRequestWrapper
        protected void processSuccess(GraphResponse response) {
            JSONObject engagementResults = Utility.tryGetJSONObjectFromResponse(response.getJSONObject(), "engagement");
            if (engagementResults != null) {
                this.likeCountStringWithLike = engagementResults.optString("count_string_with_like", this.likeCountStringWithLike);
                this.likeCountStringWithoutLike = engagementResults.optString("count_string_without_like", this.likeCountStringWithoutLike);
                this.socialSentenceStringWithLike = engagementResults.optString("social_sentence_with_like", this.socialSentenceStringWithLike);
                this.socialSentenceStringWithoutLike = engagementResults.optString("social_sentence_without_like", this.socialSentenceStringWithoutLike);
            }
        }

        @Override // com.facebook.share.internal.LikeActionController.AbstractRequestWrapper
        protected void processError(FacebookRequestError error) {
            Logger.log(LoggingBehavior.REQUESTS, LikeActionController.TAG, "Error fetching engagement for object '%s' with type '%s' : %s", this.objectId, this.objectType, error);
            LikeActionController.this.logAppEventForError("get_engagement", error);
        }
    }

    private abstract class AbstractRequestWrapper implements RequestWrapper {
        protected FacebookRequestError error;
        protected String objectId;
        protected LikeView.ObjectType objectType;
        private GraphRequest request;

        protected abstract void processSuccess(GraphResponse graphResponse);

        protected AbstractRequestWrapper(String objectId, LikeView.ObjectType objectType) {
            this.objectId = objectId;
            this.objectType = objectType;
        }

        @Override // com.facebook.share.internal.LikeActionController.RequestWrapper
        public void addToBatch(GraphRequestBatch batch) {
            batch.add(this.request);
        }

        @Override // com.facebook.share.internal.LikeActionController.RequestWrapper
        public FacebookRequestError getError() {
            return this.error;
        }

        protected void setRequest(GraphRequest request) {
            this.request = request;
            request.setVersion("v2.4");
            request.setCallback(new GraphRequest.Callback() { // from class: com.facebook.share.internal.LikeActionController.AbstractRequestWrapper.1
                @Override // com.facebook.GraphRequest.Callback
                public void onCompleted(GraphResponse response) {
                    AbstractRequestWrapper.this.error = response.getError();
                    if (AbstractRequestWrapper.this.error != null) {
                        AbstractRequestWrapper.this.processError(AbstractRequestWrapper.this.error);
                    } else {
                        AbstractRequestWrapper.this.processSuccess(response);
                    }
                }
            });
        }

        protected void processError(FacebookRequestError error) {
            Logger.log(LoggingBehavior.REQUESTS, LikeActionController.TAG, "Error running request for object '%s' with type '%s' : %s", this.objectId, this.objectType, error);
        }
    }

    private static class MRUCacheWorkItem implements Runnable {
        private static ArrayList<String> mruCachedItems = new ArrayList<>();
        private String cacheItem;
        private boolean shouldTrim;

        MRUCacheWorkItem(String cacheItem, boolean shouldTrim) {
            this.cacheItem = cacheItem;
            this.shouldTrim = shouldTrim;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (this.cacheItem != null) {
                mruCachedItems.remove(this.cacheItem);
                mruCachedItems.add(0, this.cacheItem);
            }
            if (this.shouldTrim && mruCachedItems.size() >= 128) {
                while (64 < mruCachedItems.size()) {
                    String cacheKey = mruCachedItems.remove(mruCachedItems.size() - 1);
                    LikeActionController.cache.remove(cacheKey);
                }
            }
        }
    }

    private static class SerializeToDiskWorkItem implements Runnable {
        private String cacheKey;
        private String controllerJson;

        SerializeToDiskWorkItem(String cacheKey, String controllerJson) {
            this.cacheKey = cacheKey;
            this.controllerJson = controllerJson;
        }

        @Override // java.lang.Runnable
        public void run() throws IOException {
            LikeActionController.serializeToDiskSynchronously(this.cacheKey, this.controllerJson);
        }
    }

    private static class CreateLikeActionControllerWorkItem implements Runnable {
        private CreationCallback callback;
        private String objectId;
        private LikeView.ObjectType objectType;

        CreateLikeActionControllerWorkItem(String objectId, LikeView.ObjectType objectType, CreationCallback callback) {
            this.objectId = objectId;
            this.objectType = objectType;
            this.callback = callback;
        }

        @Override // java.lang.Runnable
        public void run() throws JSONException, IOException {
            LikeActionController.createControllerForObjectIdAndType(this.objectId, this.objectType, this.callback);
        }
    }
}
