package com.facebook.share.internal;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Pair;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.internal.AppCall;
import com.facebook.internal.CallbackManagerImpl;
import com.facebook.internal.NativeAppCallAttachmentStore;
import com.facebook.internal.NativeProtocol;
import com.facebook.internal.Utility;
import com.facebook.share.Sharer;
import com.facebook.share.internal.OpenGraphJSONUtility;
import com.facebook.share.model.ShareOpenGraphAction;
import com.facebook.share.model.ShareOpenGraphContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.model.ShareVideoContent;
import com.facebook.share.widget.LikeView;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public final class ShareInternalUtility {
    public static String getNativeDialogCompletionGesture(Bundle result) {
        return result.containsKey("completionGesture") ? result.getString("completionGesture") : result.getString("com.facebook.platform.extra.COMPLETION_GESTURE");
    }

    public static String getShareDialogPostId(Bundle result) {
        if (result.containsKey("postId")) {
            return result.getString("postId");
        }
        if (result.containsKey("com.facebook.platform.extra.POST_ID")) {
            return result.getString("com.facebook.platform.extra.POST_ID");
        }
        return result.getString("post_id");
    }

    public static boolean handleActivityResult(int requestCode, int resultCode, Intent data, ResultProcessor resultProcessor) {
        AppCall appCall = getAppCallFromActivityResult(requestCode, resultCode, data);
        if (appCall == null) {
            return false;
        }
        NativeAppCallAttachmentStore.cleanupAttachmentsForCall(appCall.getCallId());
        if (resultProcessor == null) {
            return true;
        }
        FacebookException exception = NativeProtocol.getExceptionFromErrorData(NativeProtocol.getErrorDataFromResultIntent(data));
        if (exception != null) {
            if (exception instanceof FacebookOperationCanceledException) {
                resultProcessor.onCancel(appCall);
                return true;
            }
            resultProcessor.onError(appCall, exception);
            return true;
        }
        Bundle results = NativeProtocol.getSuccessResultsFromIntent(data);
        resultProcessor.onSuccess(appCall, results);
        return true;
    }

    public static ResultProcessor getShareResultProcessor(final FacebookCallback<Sharer.Result> callback) {
        return new ResultProcessor(callback) { // from class: com.facebook.share.internal.ShareInternalUtility.1
            @Override // com.facebook.share.internal.ResultProcessor
            public void onSuccess(AppCall appCall, Bundle results) {
                if (results != null) {
                    String gesture = ShareInternalUtility.getNativeDialogCompletionGesture(results);
                    if (gesture == null || "post".equalsIgnoreCase(gesture)) {
                        String postId = ShareInternalUtility.getShareDialogPostId(results);
                        ShareInternalUtility.invokeOnSuccessCallback(callback, postId);
                    } else if ("cancel".equalsIgnoreCase(gesture)) {
                        ShareInternalUtility.invokeOnCancelCallback(callback);
                    } else {
                        ShareInternalUtility.invokeOnErrorCallback(callback, new FacebookException("UnknownError"));
                    }
                }
            }

            @Override // com.facebook.share.internal.ResultProcessor
            public void onCancel(AppCall appCall) {
                ShareInternalUtility.invokeOnCancelCallback(callback);
            }

            @Override // com.facebook.share.internal.ResultProcessor
            public void onError(AppCall appCall, FacebookException error) {
                ShareInternalUtility.invokeOnErrorCallback(callback, error);
            }
        };
    }

    private static AppCall getAppCallFromActivityResult(int requestCode, int resultCode, Intent data) {
        UUID callId = NativeProtocol.getCallIdFromIntent(data);
        if (callId == null) {
            return null;
        }
        return AppCall.finishPendingCall(callId, requestCode);
    }

    public static void registerStaticShareCallback(final int requestCode) {
        CallbackManagerImpl.registerStaticCallback(requestCode, new CallbackManagerImpl.Callback() { // from class: com.facebook.share.internal.ShareInternalUtility.2
            @Override // com.facebook.internal.CallbackManagerImpl.Callback
            public boolean onActivityResult(int resultCode, Intent data) {
                return ShareInternalUtility.handleActivityResult(requestCode, resultCode, data, ShareInternalUtility.getShareResultProcessor(null));
            }
        });
    }

    public static List<String> getPhotoUrls(SharePhotoContent photoContent, final UUID appCallId) {
        List<SharePhoto> photos;
        if (photoContent == null || (photos = photoContent.getPhotos()) == null) {
            return null;
        }
        List<NativeAppCallAttachmentStore.Attachment> attachments = Utility.map(photos, new Utility.Mapper<SharePhoto, NativeAppCallAttachmentStore.Attachment>() { // from class: com.facebook.share.internal.ShareInternalUtility.4
            @Override // com.facebook.internal.Utility.Mapper
            public NativeAppCallAttachmentStore.Attachment apply(SharePhoto item) {
                return ShareInternalUtility.getAttachment(appCallId, item);
            }
        });
        List<String> map = Utility.map(attachments, new Utility.Mapper<NativeAppCallAttachmentStore.Attachment, String>() { // from class: com.facebook.share.internal.ShareInternalUtility.5
            @Override // com.facebook.internal.Utility.Mapper
            public String apply(NativeAppCallAttachmentStore.Attachment item) {
                return item.getAttachmentUrl();
            }
        });
        NativeAppCallAttachmentStore.addAttachments(attachments);
        return map;
    }

    public static String getVideoUrl(ShareVideoContent videoContent, UUID appCallId) {
        if (videoContent == null || videoContent.getVideo() == null) {
            return null;
        }
        NativeAppCallAttachmentStore.Attachment attachment = NativeAppCallAttachmentStore.createAttachment(appCallId, videoContent.getVideo().getLocalUrl());
        ArrayList<NativeAppCallAttachmentStore.Attachment> attachments = new ArrayList<>(1);
        attachments.add(attachment);
        NativeAppCallAttachmentStore.addAttachments(attachments);
        return attachment.getAttachmentUrl();
    }

    public static JSONObject toJSONObjectForCall(final UUID callId, ShareOpenGraphContent content) throws JSONException {
        ShareOpenGraphAction action = content.getAction();
        final ArrayList<NativeAppCallAttachmentStore.Attachment> attachments = new ArrayList<>();
        JSONObject actionJSON = OpenGraphJSONUtility.toJSONObject(action, new OpenGraphJSONUtility.PhotoJSONProcessor() { // from class: com.facebook.share.internal.ShareInternalUtility.6
            @Override // com.facebook.share.internal.OpenGraphJSONUtility.PhotoJSONProcessor
            public JSONObject toJSONObject(SharePhoto photo) throws JSONException {
                NativeAppCallAttachmentStore.Attachment attachment = ShareInternalUtility.getAttachment(callId, photo);
                if (attachment == null) {
                    return null;
                }
                attachments.add(attachment);
                JSONObject photoJSONObject = new JSONObject();
                try {
                    photoJSONObject.put("url", attachment.getAttachmentUrl());
                    if (photo.getUserGenerated()) {
                        photoJSONObject.put("user_generated", true);
                        return photoJSONObject;
                    }
                    return photoJSONObject;
                } catch (JSONException e) {
                    throw new FacebookException("Unable to attach images", e);
                }
            }
        });
        NativeAppCallAttachmentStore.addAttachments(attachments);
        if (content.getPlaceId() != null) {
            String placeTag = actionJSON.optString("place");
            if (Utility.isNullOrEmpty(placeTag)) {
                actionJSON.put("place", content.getPlaceId());
            }
        }
        if (content.getPeopleIds() != null) {
            JSONArray peopleTags = actionJSON.optJSONArray("tags");
            Set<String> peopleIdSet = peopleTags == null ? new HashSet<>() : Utility.jsonArrayToSet(peopleTags);
            for (String peopleId : content.getPeopleIds()) {
                peopleIdSet.add(peopleId);
            }
            actionJSON.put("tags", new ArrayList(peopleIdSet));
        }
        return actionJSON;
    }

    public static JSONObject toJSONObjectForWeb(ShareOpenGraphContent shareOpenGraphContent) throws JSONException {
        ShareOpenGraphAction action = shareOpenGraphContent.getAction();
        return OpenGraphJSONUtility.toJSONObject(action, new OpenGraphJSONUtility.PhotoJSONProcessor() { // from class: com.facebook.share.internal.ShareInternalUtility.7
            @Override // com.facebook.share.internal.OpenGraphJSONUtility.PhotoJSONProcessor
            public JSONObject toJSONObject(SharePhoto photo) throws JSONException {
                Uri photoUri = photo.getImageUrl();
                JSONObject photoJSONObject = new JSONObject();
                try {
                    photoJSONObject.put("url", photoUri.toString());
                    return photoJSONObject;
                } catch (JSONException e) {
                    throw new FacebookException("Unable to attach images", e);
                }
            }
        });
    }

    public static JSONArray removeNamespacesFromOGJsonArray(JSONArray jsonArray, boolean requireNamespace) throws JSONException {
        JSONArray newArray = new JSONArray();
        for (int i = 0; i < jsonArray.length(); i++) {
            Object value = jsonArray.get(i);
            if (value instanceof JSONArray) {
                value = removeNamespacesFromOGJsonArray((JSONArray) value, requireNamespace);
            } else if (value instanceof JSONObject) {
                value = removeNamespacesFromOGJsonObject((JSONObject) value, requireNamespace);
            }
            newArray.put(value);
        }
        return newArray;
    }

    public static JSONObject removeNamespacesFromOGJsonObject(JSONObject jsonObject, boolean requireNamespace) throws JSONException {
        if (jsonObject == null) {
            return null;
        }
        try {
            JSONObject newJsonObject = new JSONObject();
            JSONObject data = new JSONObject();
            JSONArray names = jsonObject.names();
            for (int i = 0; i < names.length(); i++) {
                String key = names.getString(i);
                Object value = jsonObject.get(key);
                if (value instanceof JSONObject) {
                    value = removeNamespacesFromOGJsonObject((JSONObject) value, true);
                } else if (value instanceof JSONArray) {
                    value = removeNamespacesFromOGJsonArray((JSONArray) value, true);
                }
                Pair<String, String> fieldNameAndNamespace = getFieldNameAndNamespaceFromFullName(key);
                String namespace = (String) fieldNameAndNamespace.first;
                String fieldName = (String) fieldNameAndNamespace.second;
                if (requireNamespace) {
                    if (namespace != null && namespace.equals("fbsdk")) {
                        newJsonObject.put(key, value);
                    } else if (namespace == null || namespace.equals("og")) {
                        newJsonObject.put(fieldName, value);
                    } else {
                        data.put(fieldName, value);
                    }
                } else if (namespace != null && namespace.equals("fb")) {
                    newJsonObject.put(key, value);
                } else {
                    newJsonObject.put(fieldName, value);
                }
            }
            if (data.length() > 0) {
                newJsonObject.put("data", data);
                return newJsonObject;
            }
            return newJsonObject;
        } catch (JSONException e) {
            throw new FacebookException("Failed to create json object from share content");
        }
    }

    public static Pair<String, String> getFieldNameAndNamespaceFromFullName(String fullName) {
        String fieldName;
        String namespace = null;
        int index = fullName.indexOf(58);
        if (index != -1 && fullName.length() > index + 1) {
            namespace = fullName.substring(0, index);
            fieldName = fullName.substring(index + 1);
        } else {
            fieldName = fullName;
        }
        return new Pair<>(namespace, fieldName);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static NativeAppCallAttachmentStore.Attachment getAttachment(UUID callId, SharePhoto photo) {
        Bitmap bitmap = photo.getBitmap();
        Uri photoUri = photo.getImageUrl();
        if (bitmap != null) {
            NativeAppCallAttachmentStore.Attachment attachment = NativeAppCallAttachmentStore.createAttachment(callId, bitmap);
            return attachment;
        }
        if (photoUri == null) {
            return null;
        }
        NativeAppCallAttachmentStore.Attachment attachment2 = NativeAppCallAttachmentStore.createAttachment(callId, photoUri);
        return attachment2;
    }

    static void invokeOnCancelCallback(FacebookCallback<Sharer.Result> callback) {
        logShareResult("cancelled", null);
        if (callback != null) {
            callback.onCancel();
        }
    }

    static void invokeOnSuccessCallback(FacebookCallback<Sharer.Result> callback, String postId) {
        logShareResult("succeeded", null);
        if (callback != null) {
            callback.onSuccess(new Sharer.Result(postId));
        }
    }

    static void invokeOnErrorCallback(FacebookCallback<Sharer.Result> callback, FacebookException ex) {
        logShareResult("error", ex.getMessage());
        if (callback != null) {
            callback.onError(ex);
        }
    }

    private static void logShareResult(String shareOutcome, String errorMessage) {
        Context context = FacebookSdk.getApplicationContext();
        AppEventsLogger logger = AppEventsLogger.newLogger(context);
        Bundle parameters = new Bundle();
        parameters.putString("fb_share_dialog_outcome", shareOutcome);
        if (errorMessage != null) {
            parameters.putString("error_message", errorMessage);
        }
        logger.logSdkEvent("fb_share_dialog_result", null, parameters);
    }

    public static LikeView.ObjectType getMostSpecificObjectType(LikeView.ObjectType objectType1, LikeView.ObjectType objectType2) {
        if (objectType1 != objectType2) {
            if (objectType1 == LikeView.ObjectType.UNKNOWN) {
                return objectType2;
            }
            if (objectType2 != LikeView.ObjectType.UNKNOWN) {
                return null;
            }
            return objectType1;
        }
        return objectType1;
    }
}
