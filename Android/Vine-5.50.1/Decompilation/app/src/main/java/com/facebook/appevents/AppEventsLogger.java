package com.facebook.appevents;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.facebook.AccessToken;
import com.facebook.FacebookException;
import com.facebook.FacebookRequestError;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.LoggingBehavior;
import com.facebook.internal.AppEventsLoggerUtility;
import com.facebook.internal.AttributionIdentifiers;
import com.facebook.internal.Logger;
import com.facebook.internal.Utility;
import com.facebook.internal.Validate;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class AppEventsLogger {
    private static String anonymousAppDeviceGUID;
    private static Context applicationContext;
    private static ScheduledThreadPoolExecutor backgroundExecutor;
    private static boolean requestInFlight;
    private final AccessTokenAppIdPair accessTokenAppId;
    private final String contextName;
    private static final String TAG = AppEventsLogger.class.getCanonicalName();
    private static Map<AccessTokenAppIdPair, SessionEventsState> stateMap = new ConcurrentHashMap();
    private static FlushBehavior flushBehavior = FlushBehavior.AUTO;
    private static Object staticLock = new Object();

    public enum FlushBehavior {
        AUTO,
        EXPLICIT_ONLY
    }

    private enum FlushReason {
        EXPLICIT,
        TIMER,
        SESSION_CHANGE,
        PERSISTED_EVENTS,
        EVENT_THRESHOLD,
        EAGER_FLUSHING_EVENT
    }

    private enum FlushResult {
        SUCCESS,
        SERVER_ERROR,
        NO_CONNECTIVITY,
        UNKNOWN_ERROR
    }

    private static class AccessTokenAppIdPair implements Serializable {
        private static final long serialVersionUID = 1;
        private final String accessTokenString;
        private final String applicationId;

        AccessTokenAppIdPair(AccessToken accessToken) {
            this(accessToken.getToken(), FacebookSdk.getApplicationId());
        }

        AccessTokenAppIdPair(String accessTokenString, String applicationId) {
            this.accessTokenString = Utility.isNullOrEmpty(accessTokenString) ? null : accessTokenString;
            this.applicationId = applicationId;
        }

        String getAccessTokenString() {
            return this.accessTokenString;
        }

        String getApplicationId() {
            return this.applicationId;
        }

        public int hashCode() {
            return (this.accessTokenString == null ? 0 : this.accessTokenString.hashCode()) ^ (this.applicationId != null ? this.applicationId.hashCode() : 0);
        }

        public boolean equals(Object o) {
            if (!(o instanceof AccessTokenAppIdPair)) {
                return false;
            }
            AccessTokenAppIdPair p = (AccessTokenAppIdPair) o;
            return Utility.areObjectsEqual(p.accessTokenString, this.accessTokenString) && Utility.areObjectsEqual(p.applicationId, this.applicationId);
        }

        private static class SerializationProxyV1 implements Serializable {
            private static final long serialVersionUID = -2488473066578201069L;
            private final String accessTokenString;
            private final String appId;

            /* synthetic */ SerializationProxyV1(String x0, String x1, AnonymousClass1 x2) {
                this(x0, x1);
            }

            private SerializationProxyV1(String accessTokenString, String appId) {
                this.accessTokenString = accessTokenString;
                this.appId = appId;
            }

            private Object readResolve() {
                return new AccessTokenAppIdPair(this.accessTokenString, this.appId);
            }
        }

        private Object writeReplace() {
            return new SerializationProxyV1(this.accessTokenString, this.applicationId, null);
        }
    }

    /* renamed from: com.facebook.appevents.AppEventsLogger$1, reason: invalid class name */
    static class AnonymousClass1 implements Runnable {
        final /* synthetic */ long val$eventTime;
        final /* synthetic */ AppEventsLogger val$logger;
        final /* synthetic */ String val$sourceApplicationInfo;

        @Override // java.lang.Runnable
        public void run() {
            this.val$logger.logAppSessionResumeEvent(this.val$eventTime, this.val$sourceApplicationInfo);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logAppSessionResumeEvent(long eventTime, String sourceApplicationInfo) {
        PersistedAppSessionInfo.onResume(applicationContext, this.accessTokenAppId, this, eventTime, sourceApplicationInfo);
    }

    public static AppEventsLogger newLogger(Context context) {
        return new AppEventsLogger(context, null, null);
    }

    public static AppEventsLogger newLogger(Context context, String applicationId) {
        return new AppEventsLogger(context, applicationId, null);
    }

    public static FlushBehavior getFlushBehavior() {
        FlushBehavior flushBehavior2;
        synchronized (staticLock) {
            flushBehavior2 = flushBehavior;
        }
        return flushBehavior2;
    }

    public void logEvent(String eventName, Bundle parameters) {
        logEvent(eventName, null, parameters, false);
    }

    public void logEvent(String eventName, double valueToSum, Bundle parameters) {
        logEvent(eventName, Double.valueOf(valueToSum), parameters, false);
    }

    public void logSdkEvent(String eventName, Double valueToSum, Bundle parameters) {
        logEvent(eventName, valueToSum, parameters, true);
    }

    private AppEventsLogger(Context context, String applicationId, AccessToken accessToken) {
        Validate.notNull(context, "context");
        this.contextName = Utility.getActivityName(context);
        accessToken = accessToken == null ? AccessToken.getCurrentAccessToken() : accessToken;
        if (accessToken != null && (applicationId == null || applicationId.equals(accessToken.getApplicationId()))) {
            this.accessTokenAppId = new AccessTokenAppIdPair(accessToken);
        } else {
            this.accessTokenAppId = new AccessTokenAppIdPair(null, applicationId == null ? Utility.getMetadataApplicationId(context) : applicationId);
        }
        synchronized (staticLock) {
            if (applicationContext == null) {
                applicationContext = context.getApplicationContext();
            }
        }
        initializeTimersIfNeeded();
    }

    private static void initializeTimersIfNeeded() {
        synchronized (staticLock) {
            if (backgroundExecutor == null) {
                backgroundExecutor = new ScheduledThreadPoolExecutor(1);
                Runnable flushRunnable = new Runnable() { // from class: com.facebook.appevents.AppEventsLogger.3
                    @Override // java.lang.Runnable
                    public void run() throws Throwable {
                        if (AppEventsLogger.getFlushBehavior() != FlushBehavior.EXPLICIT_ONLY) {
                            AppEventsLogger.flushAndWait(FlushReason.TIMER);
                        }
                    }
                };
                backgroundExecutor.scheduleAtFixedRate(flushRunnable, 0L, 15L, TimeUnit.SECONDS);
                Runnable attributionRecheckRunnable = new Runnable() { // from class: com.facebook.appevents.AppEventsLogger.4
                    @Override // java.lang.Runnable
                    public void run() {
                        Set<String> applicationIds = new HashSet<>();
                        synchronized (AppEventsLogger.staticLock) {
                            for (AccessTokenAppIdPair accessTokenAppId : AppEventsLogger.stateMap.keySet()) {
                                applicationIds.add(accessTokenAppId.getApplicationId());
                            }
                        }
                        for (String applicationId : applicationIds) {
                            Utility.queryAppSettings(applicationId, true);
                        }
                    }
                };
                backgroundExecutor.scheduleAtFixedRate(attributionRecheckRunnable, 0L, 86400L, TimeUnit.SECONDS);
            }
        }
    }

    private void logEvent(String eventName, Double valueToSum, Bundle parameters, boolean isImplicitlyLogged) {
        AppEvent event = new AppEvent(this.contextName, eventName, valueToSum, parameters, isImplicitlyLogged);
        logEvent(applicationContext, event, this.accessTokenAppId);
    }

    private static void logEvent(final Context context, final AppEvent event, final AccessTokenAppIdPair accessTokenAppId) {
        FacebookSdk.getExecutor().execute(new Runnable() { // from class: com.facebook.appevents.AppEventsLogger.5
            @Override // java.lang.Runnable
            public void run() throws Throwable {
                SessionEventsState state = AppEventsLogger.getSessionEventsState(context, accessTokenAppId);
                state.addEvent(event);
                AppEventsLogger.flushIfNecessary();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void flushIfNecessary() {
        synchronized (staticLock) {
            if (getFlushBehavior() != FlushBehavior.EXPLICIT_ONLY && getAccumulatedEventCount() > 100) {
                flush(FlushReason.EVENT_THRESHOLD);
            }
        }
    }

    private static int getAccumulatedEventCount() {
        int result;
        synchronized (staticLock) {
            result = 0;
            for (SessionEventsState state : stateMap.values()) {
                result += state.getAccumulatedEventCount();
            }
        }
        return result;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static SessionEventsState getSessionEventsState(Context context, AccessTokenAppIdPair accessTokenAppId) throws Throwable {
        AttributionIdentifiers attributionIdentifiers = null;
        if (stateMap.get(accessTokenAppId) == null) {
            attributionIdentifiers = AttributionIdentifiers.getAttributionIdentifiers(context);
        }
        synchronized (staticLock) {
            try {
                SessionEventsState state = stateMap.get(accessTokenAppId);
                if (state == null) {
                    SessionEventsState state2 = new SessionEventsState(attributionIdentifiers, context.getPackageName(), getAnonymousAppDeviceGUID(context));
                    try {
                        stateMap.put(accessTokenAppId, state2);
                        state = state2;
                    } catch (Throwable th) {
                        th = th;
                        throw th;
                    }
                }
                return state;
            } catch (Throwable th2) {
                th = th2;
            }
        }
    }

    private static SessionEventsState getSessionEventsState(AccessTokenAppIdPair accessTokenAppId) {
        SessionEventsState sessionEventsState;
        synchronized (staticLock) {
            sessionEventsState = stateMap.get(accessTokenAppId);
        }
        return sessionEventsState;
    }

    private static void flush(final FlushReason reason) {
        FacebookSdk.getExecutor().execute(new Runnable() { // from class: com.facebook.appevents.AppEventsLogger.6
            @Override // java.lang.Runnable
            public void run() throws Throwable {
                AppEventsLogger.flushAndWait(reason);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void flushAndWait(FlushReason reason) throws Throwable {
        synchronized (staticLock) {
            if (!requestInFlight) {
                requestInFlight = true;
                Set<AccessTokenAppIdPair> keysToFlush = new HashSet<>(stateMap.keySet());
                accumulatePersistedEvents();
                FlushStatistics flushResults = null;
                try {
                    flushResults = buildAndExecuteRequests(reason, keysToFlush);
                } catch (Exception e) {
                    Utility.logd(TAG, "Caught unexpected exception while flushing: ", e);
                }
                synchronized (staticLock) {
                    requestInFlight = false;
                }
                if (flushResults != null) {
                    Intent intent = new Intent("com.facebook.sdk.APP_EVENTS_FLUSHED");
                    intent.putExtra("com.facebook.sdk.APP_EVENTS_NUM_EVENTS_FLUSHED", flushResults.numEvents);
                    intent.putExtra("com.facebook.sdk.APP_EVENTS_FLUSH_RESULT", flushResults.result);
                    LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent);
                }
            }
        }
    }

    private static FlushStatistics buildAndExecuteRequests(FlushReason reason, Set<AccessTokenAppIdPair> keysToFlush) {
        GraphRequest request;
        FlushStatistics flushResults = new FlushStatistics(null);
        boolean limitEventUsage = FacebookSdk.getLimitEventAndDataUsage(applicationContext);
        List<GraphRequest> requestsToExecute = new ArrayList<>();
        for (AccessTokenAppIdPair accessTokenAppId : keysToFlush) {
            SessionEventsState sessionEventsState = getSessionEventsState(accessTokenAppId);
            if (sessionEventsState != null && (request = buildRequestForSession(accessTokenAppId, sessionEventsState, limitEventUsage, flushResults)) != null) {
                requestsToExecute.add(request);
            }
        }
        if (requestsToExecute.size() <= 0) {
            return null;
        }
        Logger.log(LoggingBehavior.APP_EVENTS, TAG, "Flushing %d events due to %s.", Integer.valueOf(flushResults.numEvents), reason.toString());
        Iterator<GraphRequest> it = requestsToExecute.iterator();
        while (it.hasNext()) {
            it.next().executeAndWait();
        }
        return flushResults;
    }

    private static class FlushStatistics {
        public int numEvents;
        public FlushResult result;

        private FlushStatistics() {
            this.numEvents = 0;
            this.result = FlushResult.SUCCESS;
        }

        /* synthetic */ FlushStatistics(AnonymousClass1 x0) {
            this();
        }
    }

    private static GraphRequest buildRequestForSession(final AccessTokenAppIdPair accessTokenAppId, final SessionEventsState sessionEventsState, boolean limitEventUsage, final FlushStatistics flushState) {
        int numEvents;
        String applicationId = accessTokenAppId.getApplicationId();
        Utility.FetchedAppSettings fetchedAppSettings = Utility.queryAppSettings(applicationId, false);
        final GraphRequest postRequest = GraphRequest.newPostRequest(null, String.format("%s/activities", applicationId), null, null);
        Bundle requestParameters = postRequest.getParameters();
        if (requestParameters == null) {
            requestParameters = new Bundle();
        }
        requestParameters.putString("access_token", accessTokenAppId.getAccessTokenString());
        postRequest.setParameters(requestParameters);
        if (fetchedAppSettings != null && (numEvents = sessionEventsState.populateRequest(postRequest, fetchedAppSettings.supportsImplicitLogging(), limitEventUsage)) != 0) {
            flushState.numEvents += numEvents;
            postRequest.setCallback(new GraphRequest.Callback() { // from class: com.facebook.appevents.AppEventsLogger.7
                @Override // com.facebook.GraphRequest.Callback
                public void onCompleted(GraphResponse response) throws JSONException {
                    AppEventsLogger.handleResponse(accessTokenAppId, postRequest, response, sessionEventsState, flushState);
                }
            });
            return postRequest;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void handleResponse(AccessTokenAppIdPair accessTokenAppId, GraphRequest request, GraphResponse response, SessionEventsState sessionEventsState, FlushStatistics flushState) throws JSONException {
        String prettyPrintedEvents;
        FacebookRequestError error = response.getError();
        String resultDescription = "Success";
        FlushResult flushResult = FlushResult.SUCCESS;
        if (error != null) {
            if (error.getErrorCode() == -1) {
                resultDescription = "Failed: No Connectivity";
                flushResult = FlushResult.NO_CONNECTIVITY;
            } else {
                resultDescription = String.format("Failed:\n  Response: %s\n  Error %s", response.toString(), error.toString());
                flushResult = FlushResult.SERVER_ERROR;
            }
        }
        if (FacebookSdk.isLoggingBehaviorEnabled(LoggingBehavior.APP_EVENTS)) {
            String eventsJsonString = (String) request.getTag();
            try {
                JSONArray jsonArray = new JSONArray(eventsJsonString);
                prettyPrintedEvents = jsonArray.toString(2);
            } catch (JSONException e) {
                prettyPrintedEvents = "<Can't encode events for debug logging>";
            }
            Logger.log(LoggingBehavior.APP_EVENTS, TAG, "Flush completed\nParams: %s\n  Result: %s\n  Events JSON: %s", request.getGraphObject().toString(), resultDescription, prettyPrintedEvents);
        }
        sessionEventsState.clearInFlightAndStats(error != null);
        if (flushResult == FlushResult.NO_CONNECTIVITY) {
            PersistedEvents.persistEvents(applicationContext, accessTokenAppId, sessionEventsState);
        }
        if (flushResult != FlushResult.SUCCESS && flushState.result != FlushResult.NO_CONNECTIVITY) {
            flushState.result = flushResult;
        }
    }

    private static int accumulatePersistedEvents() throws Throwable {
        PersistedEvents persistedEvents = PersistedEvents.readAndClearStore(applicationContext);
        int result = 0;
        for (AccessTokenAppIdPair accessTokenAppId : persistedEvents.keySet()) {
            SessionEventsState sessionEventsState = getSessionEventsState(applicationContext, accessTokenAppId);
            List<AppEvent> events = persistedEvents.getEvents(accessTokenAppId);
            sessionEventsState.accumulatePersistedEvents(events);
            result += events.size();
        }
        return result;
    }

    public static String getAnonymousAppDeviceGUID(Context context) {
        if (anonymousAppDeviceGUID == null) {
            synchronized (staticLock) {
                if (anonymousAppDeviceGUID == null) {
                    SharedPreferences preferences = context.getSharedPreferences("com.facebook.sdk.appEventPreferences", 0);
                    anonymousAppDeviceGUID = preferences.getString("anonymousAppDeviceGUID", null);
                    if (anonymousAppDeviceGUID == null) {
                        anonymousAppDeviceGUID = "XZ" + UUID.randomUUID().toString();
                        context.getSharedPreferences("com.facebook.sdk.appEventPreferences", 0).edit().putString("anonymousAppDeviceGUID", anonymousAppDeviceGUID).apply();
                    }
                }
            }
        }
        return anonymousAppDeviceGUID;
    }

    static class SessionEventsState {
        private String anonymousAppDeviceGUID;
        private AttributionIdentifiers attributionIdentifiers;
        private int numSkippedEventsDueToFullBuffer;
        private String packageName;
        private List<AppEvent> accumulatedEvents = new ArrayList();
        private List<AppEvent> inFlightEvents = new ArrayList();
        private final int MAX_ACCUMULATED_LOG_EVENTS = 1000;

        public SessionEventsState(AttributionIdentifiers identifiers, String packageName, String anonymousGUID) {
            this.attributionIdentifiers = identifiers;
            this.packageName = packageName;
            this.anonymousAppDeviceGUID = anonymousGUID;
        }

        public synchronized void addEvent(AppEvent event) {
            if (this.accumulatedEvents.size() + this.inFlightEvents.size() >= 1000) {
                this.numSkippedEventsDueToFullBuffer++;
            } else {
                this.accumulatedEvents.add(event);
            }
        }

        public synchronized int getAccumulatedEventCount() {
            return this.accumulatedEvents.size();
        }

        public synchronized void clearInFlightAndStats(boolean moveToAccumulated) {
            if (moveToAccumulated) {
                this.accumulatedEvents.addAll(this.inFlightEvents);
                this.inFlightEvents.clear();
                this.numSkippedEventsDueToFullBuffer = 0;
            } else {
                this.inFlightEvents.clear();
                this.numSkippedEventsDueToFullBuffer = 0;
            }
        }

        public int populateRequest(GraphRequest request, boolean includeImplicitEvents, boolean limitEventUsage) throws JSONException {
            synchronized (this) {
                int numSkipped = this.numSkippedEventsDueToFullBuffer;
                this.inFlightEvents.addAll(this.accumulatedEvents);
                this.accumulatedEvents.clear();
                JSONArray jsonArray = new JSONArray();
                for (AppEvent event : this.inFlightEvents) {
                    if (includeImplicitEvents || !event.getIsImplicit()) {
                        jsonArray.put(event.getJSONObject());
                    }
                }
                if (jsonArray.length() == 0) {
                    return 0;
                }
                populateRequest(request, numSkipped, jsonArray, limitEventUsage);
                return jsonArray.length();
            }
        }

        public synchronized List<AppEvent> getEventsToPersist() {
            List<AppEvent> result;
            result = this.accumulatedEvents;
            this.accumulatedEvents = new ArrayList();
            return result;
        }

        public synchronized void accumulatePersistedEvents(List<AppEvent> events) {
            this.accumulatedEvents.addAll(events);
        }

        private void populateRequest(GraphRequest request, int numSkipped, JSONArray events, boolean limitEventUsage) throws JSONException {
            JSONObject publishParams;
            try {
                publishParams = AppEventsLoggerUtility.getJSONObjectForGraphAPICall(AppEventsLoggerUtility.GraphAPIActivityType.CUSTOM_APP_EVENTS, this.attributionIdentifiers, this.anonymousAppDeviceGUID, limitEventUsage, AppEventsLogger.applicationContext);
                if (this.numSkippedEventsDueToFullBuffer > 0) {
                    publishParams.put("num_skipped_events", numSkipped);
                }
            } catch (JSONException e) {
                publishParams = new JSONObject();
            }
            request.setGraphObject(publishParams);
            Bundle requestParameters = request.getParameters();
            if (requestParameters == null) {
                requestParameters = new Bundle();
            }
            String jsonString = events.toString();
            if (jsonString != null) {
                requestParameters.putByteArray("custom_events_file", getStringAsByteArray(jsonString));
                request.setTag(jsonString);
            }
            request.setParameters(requestParameters);
        }

        private byte[] getStringAsByteArray(String jsonString) throws UnsupportedEncodingException {
            try {
                byte[] jsonUtf8 = jsonString.getBytes("UTF-8");
                return jsonUtf8;
            } catch (UnsupportedEncodingException e) {
                Utility.logd("Encoding exception: ", e);
                return null;
            }
        }
    }

    static class AppEvent implements Serializable {
        private static final long serialVersionUID = 1;
        private static final HashSet<String> validatedIdentifiers = new HashSet<>();
        private boolean isImplicit;
        private JSONObject jsonObject;
        private String name;

        /* synthetic */ AppEvent(String x0, boolean x1, AnonymousClass1 x2) throws JSONException {
            this(x0, x1);
        }

        public AppEvent(String contextName, String eventName, Double valueToSum, Bundle parameters, boolean isImplicitlyLogged) throws JSONException {
            try {
                validateIdentifier(eventName);
                this.name = eventName;
                this.isImplicit = isImplicitlyLogged;
                this.jsonObject = new JSONObject();
                this.jsonObject.put("_eventName", eventName);
                this.jsonObject.put("_logTime", System.currentTimeMillis() / 1000);
                this.jsonObject.put("_ui", contextName);
                if (valueToSum != null) {
                    this.jsonObject.put("_valueToSum", valueToSum.doubleValue());
                }
                if (this.isImplicit) {
                    this.jsonObject.put("_implicitlyLogged", "1");
                }
                if (parameters != null) {
                    for (String key : parameters.keySet()) {
                        validateIdentifier(key);
                        Object value = parameters.get(key);
                        if (!(value instanceof String) && !(value instanceof Number)) {
                            throw new FacebookException(String.format("Parameter value '%s' for key '%s' should be a string or a numeric type.", value, key));
                        }
                        this.jsonObject.put(key, value.toString());
                    }
                }
                if (!this.isImplicit) {
                    Logger.log(LoggingBehavior.APP_EVENTS, "AppEvents", "Created app event '%s'", this.jsonObject.toString());
                }
            } catch (FacebookException e) {
                Logger.log(LoggingBehavior.APP_EVENTS, "AppEvents", "Invalid app event name or parameter:", e.toString());
                this.jsonObject = null;
            } catch (JSONException jsonException) {
                Logger.log(LoggingBehavior.APP_EVENTS, "AppEvents", "JSON encoding for app event failed: '%s'", jsonException.toString());
                this.jsonObject = null;
            }
        }

        private AppEvent(String jsonString, boolean isImplicit) throws JSONException {
            this.jsonObject = new JSONObject(jsonString);
            this.isImplicit = isImplicit;
        }

        public boolean getIsImplicit() {
            return this.isImplicit;
        }

        public JSONObject getJSONObject() {
            return this.jsonObject;
        }

        private void validateIdentifier(String identifier) throws FacebookException {
            boolean alreadyValidated;
            if (identifier == null || identifier.length() == 0 || identifier.length() > 40) {
                if (identifier == null) {
                    identifier = "<None Provided>";
                }
                throw new FacebookException(String.format(Locale.ROOT, "Identifier '%s' must be less than %d characters", identifier, 40));
            }
            synchronized (validatedIdentifiers) {
                alreadyValidated = validatedIdentifiers.contains(identifier);
            }
            if (!alreadyValidated) {
                if (identifier.matches("^[0-9a-zA-Z_]+[0-9a-zA-Z _-]*$")) {
                    synchronized (validatedIdentifiers) {
                        validatedIdentifiers.add(identifier);
                    }
                    return;
                }
                throw new FacebookException(String.format("Skipping event named '%s' due to illegal name - must be under 40 chars and alphanumeric, _, - or space, and not start with a space or hyphen.", identifier));
            }
        }

        private static class SerializationProxyV1 implements Serializable {
            private static final long serialVersionUID = -2488473066578201069L;
            private final boolean isImplicit;
            private final String jsonString;

            /* synthetic */ SerializationProxyV1(String x0, boolean x1, AnonymousClass1 x2) {
                this(x0, x1);
            }

            private SerializationProxyV1(String jsonString, boolean isImplicit) {
                this.jsonString = jsonString;
                this.isImplicit = isImplicit;
            }

            private Object readResolve() throws JSONException {
                return new AppEvent(this.jsonString, this.isImplicit, null);
            }
        }

        private Object writeReplace() {
            return new SerializationProxyV1(this.jsonObject.toString(), this.isImplicit, null);
        }

        public String toString() {
            return String.format("\"%s\", implicit: %b, json: %s", this.jsonObject.optString("_eventName"), Boolean.valueOf(this.isImplicit), this.jsonObject.toString());
        }
    }

    static class PersistedAppSessionInfo {
        private static Map<AccessTokenAppIdPair, FacebookTimeSpentData> appSessionInfoMap;
        private static final Object staticLock = new Object();
        private static boolean hasChanges = false;
        private static boolean isLoaded = false;
        private static final Runnable appSessionInfoFlushRunnable = new Runnable() { // from class: com.facebook.appevents.AppEventsLogger.PersistedAppSessionInfo.1
            @Override // java.lang.Runnable
            public void run() throws Throwable {
                PersistedAppSessionInfo.saveAppSessionInformation(AppEventsLogger.applicationContext);
            }
        };

        private static void restoreAppSessionInformation(Context context) throws Throwable {
            ObjectInputStream ois;
            ObjectInputStream ois2 = null;
            synchronized (staticLock) {
                try {
                    try {
                        if (!isLoaded) {
                            try {
                                ois = new ObjectInputStream(context.openFileInput("AppEventsLogger.persistedsessioninfo"));
                                try {
                                    appSessionInfoMap = (HashMap) ois.readObject();
                                    Logger.log(LoggingBehavior.APP_EVENTS, "AppEvents", "App session info loaded");
                                } catch (FileNotFoundException e) {
                                    ois2 = ois;
                                    Utility.closeQuietly(ois2);
                                    context.deleteFile("AppEventsLogger.persistedsessioninfo");
                                    if (appSessionInfoMap == null) {
                                        appSessionInfoMap = new HashMap();
                                    }
                                    isLoaded = true;
                                    hasChanges = false;
                                } catch (Exception e2) {
                                    e = e2;
                                    ois2 = ois;
                                    Log.d(AppEventsLogger.TAG, "Got unexpected exception: " + e.toString());
                                    Utility.closeQuietly(ois2);
                                    context.deleteFile("AppEventsLogger.persistedsessioninfo");
                                    if (appSessionInfoMap == null) {
                                        appSessionInfoMap = new HashMap();
                                    }
                                    isLoaded = true;
                                    hasChanges = false;
                                } catch (Throwable th) {
                                    th = th;
                                    ois2 = ois;
                                    Utility.closeQuietly(ois2);
                                    context.deleteFile("AppEventsLogger.persistedsessioninfo");
                                    if (appSessionInfoMap == null) {
                                        appSessionInfoMap = new HashMap();
                                    }
                                    isLoaded = true;
                                    hasChanges = false;
                                    throw th;
                                }
                            } catch (FileNotFoundException e3) {
                            } catch (Exception e4) {
                                e = e4;
                            }
                            try {
                                Utility.closeQuietly(ois);
                                context.deleteFile("AppEventsLogger.persistedsessioninfo");
                                if (appSessionInfoMap == null) {
                                    appSessionInfoMap = new HashMap();
                                }
                                isLoaded = true;
                                hasChanges = false;
                                ois2 = ois;
                            } catch (Throwable th2) {
                                th = th2;
                                throw th;
                            }
                        }
                    } catch (Throwable th3) {
                        th = th3;
                    }
                } catch (Throwable th4) {
                    th = th4;
                }
            }
        }

        static void saveAppSessionInformation(Context context) throws Throwable {
            ObjectOutputStream oos = null;
            synchronized (staticLock) {
                try {
                    if (hasChanges) {
                        try {
                            try {
                                ObjectOutputStream oos2 = new ObjectOutputStream(new BufferedOutputStream(context.openFileOutput("AppEventsLogger.persistedsessioninfo", 0)));
                                try {
                                    oos2.writeObject(appSessionInfoMap);
                                    hasChanges = false;
                                    Logger.log(LoggingBehavior.APP_EVENTS, "AppEvents", "App session info saved");
                                    try {
                                        Utility.closeQuietly(oos2);
                                    } catch (Throwable th) {
                                        th = th;
                                        throw th;
                                    }
                                } catch (Exception e) {
                                    e = e;
                                    oos = oos2;
                                    Log.d(AppEventsLogger.TAG, "Got unexpected exception: " + e.toString());
                                    Utility.closeQuietly(oos);
                                } catch (Throwable th2) {
                                    th = th2;
                                    oos = oos2;
                                    Utility.closeQuietly(oos);
                                    throw th;
                                }
                            } catch (Throwable th3) {
                                th = th3;
                            }
                        } catch (Exception e2) {
                            e = e2;
                        }
                    }
                } catch (Throwable th4) {
                    th = th4;
                }
            }
        }

        static void onResume(Context context, AccessTokenAppIdPair accessTokenAppId, AppEventsLogger logger, long eventTime, String sourceApplicationInfo) {
            synchronized (staticLock) {
                FacebookTimeSpentData timeSpentData = getTimeSpentData(context, accessTokenAppId);
                timeSpentData.onResume(logger, eventTime, sourceApplicationInfo);
                onTimeSpentDataUpdate();
            }
        }

        private static FacebookTimeSpentData getTimeSpentData(Context context, AccessTokenAppIdPair accessTokenAppId) throws Throwable {
            restoreAppSessionInformation(context);
            FacebookTimeSpentData result = appSessionInfoMap.get(accessTokenAppId);
            if (result == null) {
                FacebookTimeSpentData result2 = new FacebookTimeSpentData();
                appSessionInfoMap.put(accessTokenAppId, result2);
                return result2;
            }
            return result;
        }

        private static void onTimeSpentDataUpdate() {
            if (!hasChanges) {
                hasChanges = true;
                AppEventsLogger.backgroundExecutor.schedule(appSessionInfoFlushRunnable, 30L, TimeUnit.SECONDS);
            }
        }
    }

    static class PersistedEvents {
        private static Object staticLock = new Object();
        private Context context;
        private HashMap<AccessTokenAppIdPair, List<AppEvent>> persistedEvents = new HashMap<>();

        private PersistedEvents(Context context) {
            this.context = context;
        }

        public static PersistedEvents readAndClearStore(Context context) {
            PersistedEvents persistedEvents;
            synchronized (staticLock) {
                persistedEvents = new PersistedEvents(context);
                persistedEvents.readAndClearStore();
            }
            return persistedEvents;
        }

        public static void persistEvents(Context context, AccessTokenAppIdPair accessTokenAppId, SessionEventsState eventsToPersist) {
            Map<AccessTokenAppIdPair, SessionEventsState> map = new HashMap<>();
            map.put(accessTokenAppId, eventsToPersist);
            persistEvents(context, map);
        }

        public static void persistEvents(Context context, Map<AccessTokenAppIdPair, SessionEventsState> eventsToPersist) {
            synchronized (staticLock) {
                PersistedEvents persistedEvents = readAndClearStore(context);
                for (Map.Entry<AccessTokenAppIdPair, SessionEventsState> entry : eventsToPersist.entrySet()) {
                    List<AppEvent> events = entry.getValue().getEventsToPersist();
                    if (events.size() != 0) {
                        persistedEvents.addEvents(entry.getKey(), events);
                    }
                }
                persistedEvents.write();
            }
        }

        public Set<AccessTokenAppIdPair> keySet() {
            return this.persistedEvents.keySet();
        }

        public List<AppEvent> getEvents(AccessTokenAppIdPair accessTokenAppId) {
            return this.persistedEvents.get(accessTokenAppId);
        }

        private void write() throws Throwable {
            ObjectOutputStream oos = null;
            try {
                try {
                    ObjectOutputStream oos2 = new ObjectOutputStream(new BufferedOutputStream(this.context.openFileOutput("AppEventsLogger.persistedevents", 0)));
                    try {
                        oos2.writeObject(this.persistedEvents);
                        Utility.closeQuietly(oos2);
                        oos = oos2;
                    } catch (Exception e) {
                        e = e;
                        oos = oos2;
                        Log.d(AppEventsLogger.TAG, "Got unexpected exception: " + e.toString());
                        Utility.closeQuietly(oos);
                    } catch (Throwable th) {
                        th = th;
                        oos = oos2;
                        Utility.closeQuietly(oos);
                        throw th;
                    }
                } catch (Exception e2) {
                    e = e2;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        }

        private void readAndClearStore() throws Throwable {
            ObjectInputStream ois = null;
            try {
                try {
                    ObjectInputStream ois2 = new ObjectInputStream(new BufferedInputStream(this.context.openFileInput("AppEventsLogger.persistedevents")));
                    try {
                        HashMap<AccessTokenAppIdPair, List<AppEvent>> obj = (HashMap) ois2.readObject();
                        this.context.getFileStreamPath("AppEventsLogger.persistedevents").delete();
                        this.persistedEvents = obj;
                        Utility.closeQuietly(ois2);
                        ois = ois2;
                    } catch (FileNotFoundException e) {
                        ois = ois2;
                        Utility.closeQuietly(ois);
                    } catch (Exception e2) {
                        e = e2;
                        ois = ois2;
                        Log.d(AppEventsLogger.TAG, "Got unexpected exception: " + e.toString());
                        Utility.closeQuietly(ois);
                    } catch (Throwable th) {
                        th = th;
                        ois = ois2;
                        Utility.closeQuietly(ois);
                        throw th;
                    }
                } catch (FileNotFoundException e3) {
                } catch (Exception e4) {
                    e = e4;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        }

        public void addEvents(AccessTokenAppIdPair accessTokenAppId, List<AppEvent> eventsToPersist) {
            if (!this.persistedEvents.containsKey(accessTokenAppId)) {
                this.persistedEvents.put(accessTokenAppId, new ArrayList());
            }
            this.persistedEvents.get(accessTokenAppId).addAll(eventsToPersist);
        }
    }
}
