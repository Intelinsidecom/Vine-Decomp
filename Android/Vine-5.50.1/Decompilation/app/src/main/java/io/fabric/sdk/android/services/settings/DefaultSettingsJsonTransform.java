package io.fabric.sdk.android.services.settings;

import io.fabric.sdk.android.services.common.CurrentTimeProvider;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
class DefaultSettingsJsonTransform implements SettingsJsonTransform {
    DefaultSettingsJsonTransform() {
    }

    @Override // io.fabric.sdk.android.services.settings.SettingsJsonTransform
    public SettingsData buildFromJson(CurrentTimeProvider currentTimeProvider, JSONObject json) throws JSONException {
        int settingsVersion = json.optInt("settings_version", 0);
        int cacheDuration = json.optInt("cache_duration", 3600);
        AppSettingsData appData = buildAppDataFrom(json.getJSONObject("app"));
        SessionSettingsData settingsData = buildSessionDataFrom(json.getJSONObject("session"));
        PromptSettingsData promptData = buildPromptDataFrom(json.getJSONObject("prompt"));
        FeaturesSettingsData featureData = buildFeaturesSessionDataFrom(json.getJSONObject("features"));
        AnalyticsSettingsData analyticsData = buildAnalyticsSessionDataFrom(json.getJSONObject("analytics"));
        BetaSettingsData betaData = buildBetaSettingsDataFrom(json.getJSONObject("beta"));
        long expiresAtMillis = getExpiresAtFrom(currentTimeProvider, cacheDuration, json);
        return new SettingsData(expiresAtMillis, appData, settingsData, promptData, featureData, analyticsData, betaData, settingsVersion, cacheDuration);
    }

    private AppSettingsData buildAppDataFrom(JSONObject json) throws JSONException {
        String identifier = json.getString("identifier");
        String status = json.getString("status");
        String url = json.getString("url");
        String reportsUrl = json.getString("reports_url");
        boolean updateRequired = json.optBoolean("update_required", false);
        AppIconSettingsData icon = null;
        if (json.has("icon") && json.getJSONObject("icon").has("hash")) {
            icon = buildIconDataFrom(json.getJSONObject("icon"));
        }
        return new AppSettingsData(identifier, status, url, reportsUrl, updateRequired, icon);
    }

    private AppIconSettingsData buildIconDataFrom(JSONObject iconJson) throws JSONException {
        String hash = iconJson.getString("hash");
        int width = iconJson.getInt("width");
        int height = iconJson.getInt("height");
        return new AppIconSettingsData(hash, width, height);
    }

    private FeaturesSettingsData buildFeaturesSessionDataFrom(JSONObject json) {
        boolean promptEnabled = json.optBoolean("prompt_enabled", false);
        boolean collectLoggedExceptions = json.optBoolean("collect_logged_exceptions", true);
        boolean collectReports = json.optBoolean("collect_reports", true);
        boolean collectAnalytics = json.optBoolean("collect_analytics", false);
        return new FeaturesSettingsData(promptEnabled, collectLoggedExceptions, collectReports, collectAnalytics);
    }

    private AnalyticsSettingsData buildAnalyticsSessionDataFrom(JSONObject json) {
        String url = json.optString("url", "https://e.crashlytics.com/spi/v2/events");
        int flushSecs = json.optInt("flush_interval_secs", 600);
        int maxByteSizePerFile = json.optInt("max_byte_size_per_file", 8000);
        int maxFileCountPerSend = json.optInt("max_file_count_per_send", 1);
        int maxPendingSendFileCount = json.optInt("max_pending_send_file_count", 100);
        boolean trackCustomEvents = json.optBoolean("track_custom_events", true);
        boolean trackPredefinedEvents = json.optBoolean("track_predefined_events", true);
        int samplingRate = json.optInt("sampling_rate", 1);
        boolean flushOnBackground = json.optBoolean("flush_on_background", true);
        return new AnalyticsSettingsData(url, flushSecs, maxByteSizePerFile, maxFileCountPerSend, maxPendingSendFileCount, trackCustomEvents, trackPredefinedEvents, samplingRate, flushOnBackground);
    }

    private SessionSettingsData buildSessionDataFrom(JSONObject json) throws JSONException {
        int logBufferSize = json.optInt("log_buffer_size", 64000);
        int maxChainedExceptionDepth = json.optInt("max_chained_exception_depth", 8);
        int maxCustomExceptionEvents = json.optInt("max_custom_exception_events", 64);
        int maxCustomKeyValuePairs = json.optInt("max_custom_key_value_pairs", 64);
        int identifierMask = json.optInt("identifier_mask", 255);
        boolean sendSessionWithoutCrash = json.optBoolean("send_session_without_crash", false);
        return new SessionSettingsData(logBufferSize, maxChainedExceptionDepth, maxCustomExceptionEvents, maxCustomKeyValuePairs, identifierMask, sendSessionWithoutCrash);
    }

    private PromptSettingsData buildPromptDataFrom(JSONObject json) throws JSONException {
        String title = json.optString("title", "Send Crash Report?");
        String message = json.optString("message", "Looks like we crashed! Please help us fix the problem by sending a crash report.");
        String sendButtonTitle = json.optString("send_button_title", "Send");
        boolean showCancelButton = json.optBoolean("show_cancel_button", true);
        String cancelButtonTitle = json.optString("cancel_button_title", "Don't Send");
        boolean showAlwaysSendButton = json.optBoolean("show_always_send_button", true);
        String alwaysSendButtonTitle = json.optString("always_send_button_title", "Always Send");
        return new PromptSettingsData(title, message, sendButtonTitle, showCancelButton, cancelButtonTitle, showAlwaysSendButton, alwaysSendButtonTitle);
    }

    private BetaSettingsData buildBetaSettingsDataFrom(JSONObject json) throws JSONException {
        String updateUrl = json.optString("update_endpoint", SettingsJsonConstants.BETA_UPDATE_ENDPOINT_DEFAULT);
        int updateSuspendDurationSeconds = json.optInt("update_suspend_duration", 3600);
        return new BetaSettingsData(updateUrl, updateSuspendDurationSeconds);
    }

    private long getExpiresAtFrom(CurrentTimeProvider currentTimeProvider, long cacheDurationSeconds, JSONObject json) throws JSONException {
        if (json.has("expires_at")) {
            long expiresAtMillis = json.getLong("expires_at");
            return expiresAtMillis;
        }
        long currentTimeMillis = currentTimeProvider.getCurrentTimeMillis();
        long expiresAtMillis2 = currentTimeMillis + (1000 * cacheDurationSeconds);
        return expiresAtMillis2;
    }
}
