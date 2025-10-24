package com.facebook.internal;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcel;
import android.os.StatFs;
import android.support.v4.os.EnvironmentCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import com.facebook.AccessToken;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/* loaded from: classes2.dex */
public final class Utility {
    private static final String[] APP_SETTING_FIELDS = {"supports_implicit_sdk_logging", "gdpv4_nux_content", "gdpv4_nux_enabled", "android_dialog_configs", "android_sdk_error_categories"};
    private static Map<String, FetchedAppSettings> fetchedAppSettings = new ConcurrentHashMap();
    private static AtomicBoolean loadingSettings = new AtomicBoolean(false);
    private static int numCPUCores = 0;
    private static long timestampOfLastCheck = -1;
    private static long totalExternalStorageGB = -1;
    private static long availableExternalStorageGB = -1;
    private static String deviceTimezone = "";
    private static String carrierName = "NoCarrier";

    public interface GraphMeRequestWithCacheCallback {
        void onFailure(FacebookException facebookException);

        void onSuccess(JSONObject jSONObject);
    }

    public interface Mapper<T, K> {
        K apply(T t);
    }

    public static class FetchedAppSettings {
        private Map<String, Map<String, DialogFeatureConfig>> dialogConfigMap;
        private FacebookRequestErrorClassification errorClassification;
        private String nuxContent;
        private boolean nuxEnabled;
        private boolean supportsImplicitLogging;

        private FetchedAppSettings(boolean supportsImplicitLogging, String nuxContent, boolean nuxEnabled, Map<String, Map<String, DialogFeatureConfig>> dialogConfigMap, FacebookRequestErrorClassification errorClassification) {
            this.supportsImplicitLogging = supportsImplicitLogging;
            this.nuxContent = nuxContent;
            this.nuxEnabled = nuxEnabled;
            this.dialogConfigMap = dialogConfigMap;
            this.errorClassification = errorClassification;
        }

        public boolean supportsImplicitLogging() {
            return this.supportsImplicitLogging;
        }

        public String getNuxContent() {
            return this.nuxContent;
        }

        public boolean getNuxEnabled() {
            return this.nuxEnabled;
        }

        public Map<String, Map<String, DialogFeatureConfig>> getDialogConfigurations() {
            return this.dialogConfigMap;
        }

        public FacebookRequestErrorClassification getErrorClassification() {
            return this.errorClassification;
        }
    }

    public static class DialogFeatureConfig {
        private String dialogName;
        private Uri fallbackUrl;
        private String featureName;
        private int[] featureVersionSpec;

        /* JADX INFO: Access modifiers changed from: private */
        public static DialogFeatureConfig parseDialogConfig(JSONObject dialogConfigJSON) throws NumberFormatException {
            String dialogNameWithFeature = dialogConfigJSON.optString("name");
            if (Utility.isNullOrEmpty(dialogNameWithFeature)) {
                return null;
            }
            String[] components = dialogNameWithFeature.split("\\|");
            if (components.length != 2) {
                return null;
            }
            String dialogName = components[0];
            String featureName = components[1];
            if (Utility.isNullOrEmpty(dialogName) || Utility.isNullOrEmpty(featureName)) {
                return null;
            }
            String urlString = dialogConfigJSON.optString("url");
            Uri fallbackUri = null;
            if (!Utility.isNullOrEmpty(urlString)) {
                fallbackUri = Uri.parse(urlString);
            }
            JSONArray versionsJSON = dialogConfigJSON.optJSONArray("versions");
            int[] featureVersionSpec = parseVersionSpec(versionsJSON);
            return new DialogFeatureConfig(dialogName, featureName, fallbackUri, featureVersionSpec);
        }

        private static int[] parseVersionSpec(JSONArray versionsJSON) throws NumberFormatException {
            int[] versionSpec = null;
            if (versionsJSON != null) {
                int numVersions = versionsJSON.length();
                versionSpec = new int[numVersions];
                for (int i = 0; i < numVersions; i++) {
                    int version = versionsJSON.optInt(i, -1);
                    if (version == -1) {
                        String versionString = versionsJSON.optString(i);
                        if (!Utility.isNullOrEmpty(versionString)) {
                            try {
                                version = Integer.parseInt(versionString);
                            } catch (NumberFormatException nfe) {
                                Utility.logd("FacebookSDK", nfe);
                                version = -1;
                            }
                        }
                    }
                    versionSpec[i] = version;
                }
            }
            return versionSpec;
        }

        private DialogFeatureConfig(String dialogName, String featureName, Uri fallbackUrl, int[] featureVersionSpec) {
            this.dialogName = dialogName;
            this.featureName = featureName;
            this.fallbackUrl = fallbackUrl;
            this.featureVersionSpec = featureVersionSpec;
        }

        public String getDialogName() {
            return this.dialogName;
        }

        public String getFeatureName() {
            return this.featureName;
        }

        public Uri getFallbackUrl() {
            return this.fallbackUrl;
        }

        public int[] getVersionSpec() {
            return this.featureVersionSpec;
        }
    }

    public static <T> boolean isNullOrEmpty(Collection<T> c) {
        return c == null || c.size() == 0;
    }

    public static boolean isNullOrEmpty(String s) {
        return s == null || s.length() == 0;
    }

    public static String coerceValueIfNullOrEmpty(String s, String valueIfNullOrEmpty) {
        return isNullOrEmpty(s) ? valueIfNullOrEmpty : s;
    }

    public static <T> Collection<T> unmodifiableCollection(T... ts) {
        return Collections.unmodifiableCollection(Arrays.asList(ts));
    }

    public static String md5hash(String key) {
        return hashWithAlgorithm("MD5", key);
    }

    public static String sha1hash(byte[] bytes) {
        return hashWithAlgorithm("SHA-1", bytes);
    }

    private static String hashWithAlgorithm(String algorithm, String key) {
        return hashWithAlgorithm(algorithm, key.getBytes());
    }

    private static String hashWithAlgorithm(String algorithm, byte[] bytes) throws NoSuchAlgorithmException {
        try {
            MessageDigest hash = MessageDigest.getInstance(algorithm);
            return hashBytes(hash, bytes);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    private static String hashBytes(MessageDigest hash, byte[] bytes) {
        hash.update(bytes);
        byte[] digest = hash.digest();
        StringBuilder builder = new StringBuilder();
        for (int b : digest) {
            builder.append(Integer.toHexString((b >> 4) & 15));
            builder.append(Integer.toHexString((b >> 0) & 15));
        }
        return builder.toString();
    }

    public static Uri buildUri(String authority, String path, Bundle parameters) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https");
        builder.authority(authority);
        builder.path(path);
        if (parameters != null) {
            for (String key : parameters.keySet()) {
                Object parameter = parameters.get(key);
                if (parameter instanceof String) {
                    builder.appendQueryParameter(key, (String) parameter);
                }
            }
        }
        return builder.build();
    }

    public static Bundle parseUrlQueryString(String queryString) {
        Bundle params = new Bundle();
        if (!isNullOrEmpty(queryString)) {
            String[] array = queryString.split("&");
            for (String parameter : array) {
                String[] keyValuePair = parameter.split("=");
                try {
                    if (keyValuePair.length == 2) {
                        params.putString(URLDecoder.decode(keyValuePair[0], "UTF-8"), URLDecoder.decode(keyValuePair[1], "UTF-8"));
                    } else if (keyValuePair.length == 1) {
                        params.putString(URLDecoder.decode(keyValuePair[0], "UTF-8"), "");
                    }
                } catch (UnsupportedEncodingException e) {
                    logd("FacebookSDK", e);
                }
            }
        }
        return params;
    }

    public static void putNonEmptyString(Bundle b, String key, String value) {
        if (!isNullOrEmpty(value)) {
            b.putString(key, value);
        }
    }

    public static void putUri(Bundle b, String key, Uri uri) {
        if (uri != null) {
            putNonEmptyString(b, key, uri.toString());
        }
    }

    public static void closeQuietly(Closeable closeable) throws IOException {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
            }
        }
    }

    public static void disconnectQuietly(URLConnection connection) {
        if (connection instanceof HttpURLConnection) {
            ((HttpURLConnection) connection).disconnect();
        }
    }

    public static String getMetadataApplicationId(Context context) {
        Validate.notNull(context, "context");
        FacebookSdk.sdkInitialize(context);
        return FacebookSdk.getApplicationId();
    }

    public static Object getStringPropertyAsJSON(JSONObject jsonObject, String key, String nonJSONPropertyKey) throws JSONException {
        Object value = jsonObject.opt(key);
        if (value != null && (value instanceof String)) {
            JSONTokener tokener = new JSONTokener((String) value);
            value = tokener.nextValue();
        }
        if (value == null || (value instanceof JSONObject) || (value instanceof JSONArray)) {
            return value;
        }
        if (nonJSONPropertyKey != null) {
            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.putOpt(nonJSONPropertyKey, value);
            return jsonObject2;
        }
        throw new FacebookException("Got an unexpected non-JSON object.");
    }

    public static String readStreamToString(InputStream inputStream) throws Throwable {
        BufferedInputStream bufferedInputStream;
        InputStreamReader reader;
        BufferedInputStream bufferedInputStream2 = null;
        InputStreamReader reader2 = null;
        try {
            bufferedInputStream = new BufferedInputStream(inputStream);
            try {
                reader = new InputStreamReader(bufferedInputStream);
            } catch (Throwable th) {
                th = th;
                bufferedInputStream2 = bufferedInputStream;
            }
        } catch (Throwable th2) {
            th = th2;
        }
        try {
            StringBuilder stringBuilder = new StringBuilder();
            char[] buffer = new char[2048];
            while (true) {
                int n = reader.read(buffer);
                if (n != -1) {
                    stringBuilder.append(buffer, 0, n);
                } else {
                    String string = stringBuilder.toString();
                    closeQuietly(bufferedInputStream);
                    closeQuietly(reader);
                    return string;
                }
            }
        } catch (Throwable th3) {
            th = th3;
            reader2 = reader;
            bufferedInputStream2 = bufferedInputStream;
            closeQuietly(bufferedInputStream2);
            closeQuietly(reader2);
            throw th;
        }
    }

    public static int copyAndCloseInputStream(InputStream inputStream, OutputStream outputStream) throws Throwable {
        BufferedInputStream bufferedInputStream = null;
        int totalBytes = 0;
        try {
            BufferedInputStream bufferedInputStream2 = new BufferedInputStream(inputStream);
            try {
                byte[] buffer = new byte[8192];
                while (true) {
                    int bytesRead = bufferedInputStream2.read(buffer);
                    if (bytesRead == -1) {
                        break;
                    }
                    outputStream.write(buffer, 0, bytesRead);
                    totalBytes += bytesRead;
                }
                if (bufferedInputStream2 != null) {
                    bufferedInputStream2.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                return totalBytes;
            } catch (Throwable th) {
                th = th;
                bufferedInputStream = bufferedInputStream2;
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    private static void clearCookiesForDomain(Context context, String domain) {
        CookieSyncManager syncManager = CookieSyncManager.createInstance(context);
        syncManager.sync();
        CookieManager cookieManager = CookieManager.getInstance();
        String cookies = cookieManager.getCookie(domain);
        if (cookies != null) {
            String[] splitCookies = cookies.split(";");
            for (String cookie : splitCookies) {
                String[] cookieParts = cookie.split("=");
                if (cookieParts.length > 0) {
                    String newCookie = cookieParts[0].trim() + "=;expires=Sat, 1 Jan 2000 00:00:01 UTC;";
                    cookieManager.setCookie(domain, newCookie);
                }
            }
            cookieManager.removeExpiredCookie();
        }
    }

    public static void clearFacebookCookies(Context context) {
        clearCookiesForDomain(context, "facebook.com");
        clearCookiesForDomain(context, ".facebook.com");
        clearCookiesForDomain(context, "https://facebook.com");
        clearCookiesForDomain(context, "https://.facebook.com");
    }

    public static void logd(String tag, Exception e) {
        if (FacebookSdk.isDebugEnabled() && tag != null && e != null) {
            Log.d(tag, e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    public static void logd(String tag, String msg) {
        if (FacebookSdk.isDebugEnabled() && tag != null && msg != null) {
            Log.d(tag, msg);
        }
    }

    public static void logd(String tag, String msg, Throwable t) {
        if (FacebookSdk.isDebugEnabled() && !isNullOrEmpty(tag)) {
            Log.d(tag, msg, t);
        }
    }

    public static <T> boolean areObjectsEqual(T a, T b) {
        if (a == null) {
            return b == null;
        }
        return a.equals(b);
    }

    public static void loadAppSettingsAsync(final Context context, final String applicationId) {
        boolean canStartLoading = loadingSettings.compareAndSet(false, true);
        if (!isNullOrEmpty(applicationId) && !fetchedAppSettings.containsKey(applicationId) && canStartLoading) {
            final String settingsKey = String.format("com.facebook.internal.APP_SETTINGS.%s", applicationId);
            FacebookSdk.getExecutor().execute(new Runnable() { // from class: com.facebook.internal.Utility.1
                @Override // java.lang.Runnable
                public void run() {
                    SharedPreferences sharedPrefs = context.getSharedPreferences("com.facebook.internal.preferences.APP_SETTINGS", 0);
                    String settingsJSONString = sharedPrefs.getString(settingsKey, null);
                    if (!Utility.isNullOrEmpty(settingsJSONString)) {
                        JSONObject settingsJSON = null;
                        try {
                            JSONObject settingsJSON2 = new JSONObject(settingsJSONString);
                            settingsJSON = settingsJSON2;
                        } catch (JSONException je) {
                            Utility.logd("FacebookSDK", je);
                        }
                        if (settingsJSON != null) {
                            Utility.parseAppSettingsFromJSON(applicationId, settingsJSON);
                        }
                    }
                    JSONObject resultJSON = Utility.getAppSettingsQueryResponse(applicationId);
                    if (resultJSON != null) {
                        Utility.parseAppSettingsFromJSON(applicationId, resultJSON);
                        sharedPrefs.edit().putString(settingsKey, resultJSON.toString()).apply();
                    }
                    Utility.loadingSettings.set(false);
                }
            });
        }
    }

    public static FetchedAppSettings getAppSettingsWithoutQuery(String applicationId) {
        if (applicationId != null) {
            return fetchedAppSettings.get(applicationId);
        }
        return null;
    }

    public static FetchedAppSettings queryAppSettings(String applicationId, boolean forceRequery) {
        if (!forceRequery && fetchedAppSettings.containsKey(applicationId)) {
            return fetchedAppSettings.get(applicationId);
        }
        JSONObject response = getAppSettingsQueryResponse(applicationId);
        if (response == null) {
            return null;
        }
        return parseAppSettingsFromJSON(applicationId, response);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static FetchedAppSettings parseAppSettingsFromJSON(String applicationId, JSONObject settingsJSON) {
        FacebookRequestErrorClassification errorClassification;
        JSONArray errorClassificationJSON = settingsJSON.optJSONArray("android_sdk_error_categories");
        if (errorClassificationJSON == null) {
            errorClassification = FacebookRequestErrorClassification.getDefaultErrorClassification();
        } else {
            errorClassification = FacebookRequestErrorClassification.createFromJSON(errorClassificationJSON);
        }
        FetchedAppSettings result = new FetchedAppSettings(settingsJSON.optBoolean("supports_implicit_sdk_logging", false), settingsJSON.optString("gdpv4_nux_content", ""), settingsJSON.optBoolean("gdpv4_nux_enabled", false), parseDialogConfigurations(settingsJSON.optJSONObject("android_dialog_configs")), errorClassification);
        fetchedAppSettings.put(applicationId, result);
        return result;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static JSONObject getAppSettingsQueryResponse(String applicationId) {
        Bundle appSettingsParams = new Bundle();
        appSettingsParams.putString("fields", TextUtils.join(",", APP_SETTING_FIELDS));
        GraphRequest request = GraphRequest.newGraphPathRequest(null, applicationId, null);
        request.setSkipClientToken(true);
        request.setParameters(appSettingsParams);
        return request.executeAndWait().getJSONObject();
    }

    public static DialogFeatureConfig getDialogFeatureConfig(String applicationId, String actionName, String featureName) {
        FetchedAppSettings settings;
        Map<String, DialogFeatureConfig> featureMap;
        if (isNullOrEmpty(actionName) || isNullOrEmpty(featureName) || (settings = fetchedAppSettings.get(applicationId)) == null || (featureMap = settings.getDialogConfigurations().get(actionName)) == null) {
            return null;
        }
        return featureMap.get(featureName);
    }

    private static Map<String, Map<String, DialogFeatureConfig>> parseDialogConfigurations(JSONObject dialogConfigResponse) throws NumberFormatException {
        JSONArray dialogConfigData;
        HashMap<String, Map<String, DialogFeatureConfig>> dialogConfigMap = new HashMap<>();
        if (dialogConfigResponse != null && (dialogConfigData = dialogConfigResponse.optJSONArray("data")) != null) {
            for (int i = 0; i < dialogConfigData.length(); i++) {
                DialogFeatureConfig dialogConfig = DialogFeatureConfig.parseDialogConfig(dialogConfigData.optJSONObject(i));
                if (dialogConfig != null) {
                    String dialogName = dialogConfig.getDialogName();
                    Map<String, DialogFeatureConfig> featureMap = dialogConfigMap.get(dialogName);
                    if (featureMap == null) {
                        featureMap = new HashMap<>();
                        dialogConfigMap.put(dialogName, featureMap);
                    }
                    featureMap.put(dialogConfig.getFeatureName(), dialogConfig);
                }
            }
        }
        return dialogConfigMap;
    }

    public static String safeGetStringFromResponse(JSONObject response, String propertyName) {
        return response != null ? response.optString(propertyName, "") : "";
    }

    public static JSONObject tryGetJSONObjectFromResponse(JSONObject response, String propertyKey) {
        if (response != null) {
            return response.optJSONObject(propertyKey);
        }
        return null;
    }

    public static JSONArray tryGetJSONArrayFromResponse(JSONObject response, String propertyKey) {
        if (response != null) {
            return response.optJSONArray(propertyKey);
        }
        return null;
    }

    public static void deleteDirectory(File directoryOrFile) {
        if (directoryOrFile.exists()) {
            if (directoryOrFile.isDirectory()) {
                for (File child : directoryOrFile.listFiles()) {
                    deleteDirectory(child);
                }
            }
            directoryOrFile.delete();
        }
    }

    public static <T> List<T> asListNoNulls(T... array) {
        ArrayList<T> result = new ArrayList<>();
        for (T t : array) {
            if (t != null) {
                result.add(t);
            }
        }
        return result;
    }

    public static List<String> jsonArrayToStringList(JSONArray jsonArray) throws JSONException {
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            result.add(jsonArray.getString(i));
        }
        return result;
    }

    public static Set<String> jsonArrayToSet(JSONArray jsonArray) throws JSONException {
        Set<String> result = new HashSet<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            result.add(jsonArray.getString(i));
        }
        return result;
    }

    public static void setAppEventAttributionParameters(JSONObject params, AttributionIdentifiers attributionIdentifiers, String anonymousAppDeviceGUID, boolean limitEventUsage) throws JSONException {
        if (attributionIdentifiers != null && attributionIdentifiers.getAttributionId() != null) {
            params.put("attribution", attributionIdentifiers.getAttributionId());
        }
        if (attributionIdentifiers != null && attributionIdentifiers.getAndroidAdvertiserId() != null) {
            params.put("advertiser_id", attributionIdentifiers.getAndroidAdvertiserId());
            params.put("advertiser_tracking_enabled", !attributionIdentifiers.isTrackingLimited());
        }
        if (attributionIdentifiers != null && attributionIdentifiers.getAndroidInstallerPackage() != null) {
            params.put("installer_package", attributionIdentifiers.getAndroidInstallerPackage());
        }
        params.put("anon_id", anonymousAppDeviceGUID);
        params.put("application_tracking_enabled", limitEventUsage ? false : true);
    }

    public static void setAppEventExtendedDeviceInfoParameters(JSONObject params, Context appContext) throws JSONException, PackageManager.NameNotFoundException {
        Locale locale;
        JSONArray extraInfoArray = new JSONArray();
        extraInfoArray.put("a2");
        refreshPeriodicExtendedDeviceInfo(appContext);
        String pkgName = appContext.getPackageName();
        int versionCode = -1;
        String versionName = "";
        try {
            PackageInfo pi = appContext.getPackageManager().getPackageInfo(pkgName, 0);
            versionCode = pi.versionCode;
            versionName = pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
        }
        extraInfoArray.put(pkgName);
        extraInfoArray.put(versionCode);
        extraInfoArray.put(versionName);
        extraInfoArray.put(Build.VERSION.RELEASE);
        extraInfoArray.put(Build.MODEL);
        try {
            locale = appContext.getResources().getConfiguration().locale;
        } catch (Exception e2) {
            locale = Locale.getDefault();
        }
        extraInfoArray.put(locale.getLanguage() + "_" + locale.getCountry());
        extraInfoArray.put(deviceTimezone);
        extraInfoArray.put(carrierName);
        int width = 0;
        int height = 0;
        double density = 0.0d;
        try {
            WindowManager wm = (WindowManager) appContext.getSystemService("window");
            if (wm != null) {
                Display display = wm.getDefaultDisplay();
                DisplayMetrics displayMetrics = new DisplayMetrics();
                display.getMetrics(displayMetrics);
                width = displayMetrics.widthPixels;
                height = displayMetrics.heightPixels;
                density = displayMetrics.density;
            }
        } catch (Exception e3) {
        }
        extraInfoArray.put(width);
        extraInfoArray.put(height);
        extraInfoArray.put(String.format("%.2f", Double.valueOf(density)));
        extraInfoArray.put(refreshBestGuessNumberOfCPUCores());
        extraInfoArray.put(totalExternalStorageGB);
        extraInfoArray.put(availableExternalStorageGB);
        params.put("extinfo", extraInfoArray.toString());
    }

    public static Method getMethodQuietly(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        try {
            return clazz.getMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    public static Method getMethodQuietly(String className, String methodName, Class<?>... parameterTypes) throws ClassNotFoundException {
        try {
            Class<?> clazz = Class.forName(className);
            return getMethodQuietly(clazz, methodName, parameterTypes);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public static Object invokeMethodQuietly(Object receiver, Method method, Object... args) {
        try {
            return method.invoke(receiver, args);
        } catch (IllegalAccessException e) {
            return null;
        } catch (InvocationTargetException e2) {
            return null;
        }
    }

    public static String getActivityName(Context context) {
        if (context == null) {
            return "null";
        }
        if (context == context.getApplicationContext()) {
            return EnvironmentCompat.MEDIA_UNKNOWN;
        }
        return context.getClass().getSimpleName();
    }

    public static <T, K> List<K> map(List<T> target, Mapper<T, K> mapper) {
        if (target == null) {
            return null;
        }
        List<K> list = new ArrayList<>();
        for (T item : target) {
            K mappedItem = mapper.apply(item);
            if (mappedItem != null) {
                list.add(mappedItem);
            }
        }
        if (list.size() == 0) {
            list = null;
        }
        return list;
    }

    public static String getUriString(Uri uri) {
        if (uri == null) {
            return null;
        }
        return uri.toString();
    }

    public static boolean isWebUri(Uri uri) {
        return uri != null && ("http".equalsIgnoreCase(uri.getScheme()) || "https".equalsIgnoreCase(uri.getScheme()));
    }

    public static boolean isContentUri(Uri uri) {
        return uri != null && "content".equalsIgnoreCase(uri.getScheme());
    }

    public static boolean isFileUri(Uri uri) {
        return uri != null && "file".equalsIgnoreCase(uri.getScheme());
    }

    public static long getContentSize(Uri contentUri) {
        Cursor cursor = null;
        try {
            cursor = FacebookSdk.getApplicationContext().getContentResolver().query(contentUri, null, null, null, null);
            int sizeIndex = cursor.getColumnIndex("_size");
            cursor.moveToFirst();
            return cursor.getLong(sizeIndex);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static Date getBundleLongAsDate(Bundle bundle, String key, Date dateBase) throws NumberFormatException {
        long secondsFromBase;
        if (bundle == null) {
            return null;
        }
        Object secondsObject = bundle.get(key);
        if (secondsObject instanceof Long) {
            secondsFromBase = ((Long) secondsObject).longValue();
        } else {
            if (!(secondsObject instanceof String)) {
                return null;
            }
            try {
                secondsFromBase = Long.parseLong((String) secondsObject);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        if (secondsFromBase == 0) {
            return new Date(Long.MAX_VALUE);
        }
        return new Date(dateBase.getTime() + (1000 * secondsFromBase));
    }

    public static void writeStringMapToParcel(Parcel parcel, Map<String, String> map) {
        if (map == null) {
            parcel.writeInt(-1);
            return;
        }
        parcel.writeInt(map.size());
        for (Map.Entry<String, String> entry : map.entrySet()) {
            parcel.writeString(entry.getKey());
            parcel.writeString(entry.getValue());
        }
    }

    public static Map<String, String> readStringMapFromParcel(Parcel parcel) {
        int size = parcel.readInt();
        if (size < 0) {
            return null;
        }
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < size; i++) {
            map.put(parcel.readString(), parcel.readString());
        }
        return map;
    }

    public static boolean isCurrentAccessToken(AccessToken token) {
        if (token != null) {
            return token.equals(AccessToken.getCurrentAccessToken());
        }
        return false;
    }

    public static void getGraphMeRequestWithCacheAsync(final String accessToken, final GraphMeRequestWithCacheCallback callback) {
        JSONObject cachedValue = ProfileInformationCache.getProfileInformation(accessToken);
        if (cachedValue != null) {
            callback.onSuccess(cachedValue);
            return;
        }
        GraphRequest.Callback graphCallback = new GraphRequest.Callback() { // from class: com.facebook.internal.Utility.2
            @Override // com.facebook.GraphRequest.Callback
            public void onCompleted(GraphResponse response) {
                if (response.getError() != null) {
                    callback.onFailure(response.getError().getException());
                } else {
                    ProfileInformationCache.putProfileInformation(accessToken, response.getJSONObject());
                    callback.onSuccess(response.getJSONObject());
                }
            }
        };
        GraphRequest graphRequest = getGraphMeRequestWithCache(accessToken);
        graphRequest.setCallback(graphCallback);
        graphRequest.executeAsync();
    }

    public static JSONObject awaitGetGraphMeRequestWithCache(String accessToken) {
        JSONObject cachedValue = ProfileInformationCache.getProfileInformation(accessToken);
        if (cachedValue == null) {
            GraphRequest graphRequest = getGraphMeRequestWithCache(accessToken);
            GraphResponse response = graphRequest.executeAndWait();
            if (response.getError() != null) {
                return null;
            }
            return response.getJSONObject();
        }
        return cachedValue;
    }

    private static GraphRequest getGraphMeRequestWithCache(String accessToken) {
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,first_name,middle_name,last_name,link");
        parameters.putString("access_token", accessToken);
        GraphRequest graphRequest = new GraphRequest(null, "me", parameters, HttpMethod.GET, null);
        return graphRequest;
    }

    private static int refreshBestGuessNumberOfCPUCores() {
        if (numCPUCores > 0) {
            return numCPUCores;
        }
        try {
            File cpuDir = new File("/sys/devices/system/cpu/");
            File[] cpuFiles = cpuDir.listFiles(new FilenameFilter() { // from class: com.facebook.internal.Utility.3
                @Override // java.io.FilenameFilter
                public boolean accept(File dir, String fileName) {
                    return Pattern.matches("cpu[0-9]+", fileName);
                }
            });
            numCPUCores = cpuFiles.length;
        } catch (Exception e) {
        }
        if (numCPUCores <= 0) {
            numCPUCores = Math.max(Runtime.getRuntime().availableProcessors(), 1);
        }
        return numCPUCores;
    }

    private static void refreshPeriodicExtendedDeviceInfo(Context appContext) {
        if (timestampOfLastCheck == -1 || System.currentTimeMillis() - timestampOfLastCheck >= 1800000) {
            timestampOfLastCheck = System.currentTimeMillis();
            refreshTimezone();
            refreshCarrierName(appContext);
            refreshTotalExternalStorage();
            refreshAvailableExternalStorage();
        }
    }

    private static void refreshTimezone() {
        try {
            TimeZone tz = TimeZone.getDefault();
            deviceTimezone = tz.getDisplayName(tz.inDaylightTime(new Date()), 0);
        } catch (Exception e) {
        }
    }

    private static void refreshCarrierName(Context appContext) {
        if (carrierName.equals("NoCarrier")) {
            try {
                TelephonyManager telephonyManager = (TelephonyManager) appContext.getSystemService("phone");
                carrierName = telephonyManager.getNetworkOperatorName();
            } catch (Exception e) {
            }
        }
    }

    private static boolean externalStorageExists() {
        return "mounted".equals(Environment.getExternalStorageState());
    }

    private static void refreshAvailableExternalStorage() {
        try {
            if (externalStorageExists()) {
                File path = Environment.getExternalStorageDirectory();
                StatFs stat = new StatFs(path.getPath());
                availableExternalStorageGB = stat.getAvailableBlocks() * stat.getBlockSize();
            }
            availableExternalStorageGB = convertBytesToGB(availableExternalStorageGB);
        } catch (Exception e) {
        }
    }

    private static void refreshTotalExternalStorage() {
        try {
            if (externalStorageExists()) {
                File path = Environment.getExternalStorageDirectory();
                StatFs stat = new StatFs(path.getPath());
                totalExternalStorageGB = stat.getBlockCount() * stat.getBlockSize();
            }
            totalExternalStorageGB = convertBytesToGB(totalExternalStorageGB);
        } catch (Exception e) {
        }
    }

    private static long convertBytesToGB(double bytes) {
        return Math.round(bytes / 1.073741824E9d);
    }
}
