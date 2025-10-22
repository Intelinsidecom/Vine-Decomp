package com.facebook.internal;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.FacebookSdk;
import com.facebook.login.DefaultAudience;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

/* loaded from: classes.dex */
public final class NativeProtocol {
    private static final NativeAppInfo FACEBOOK_APP_INFO = new KatanaAppInfo();
    private static List<NativeAppInfo> facebookAppInfoList = buildFacebookAppList();
    private static Map<String, List<NativeAppInfo>> actionToAppInfoMap = buildActionToAppInfoMap();
    private static AtomicBoolean protocolVersionsAsyncUpdating = new AtomicBoolean(false);
    private static final List<Integer> KNOWN_PROTOCOL_VERSIONS = Arrays.asList(20141218, 20141107, 20141028, 20141001, 20140701, 20140324, 20140204, 20131107, 20130618, 20130502, 20121101);

    /* loaded from: classes2.dex */
    private static abstract class NativeAppInfo {
        private static final HashSet<String> validAppSignatureHashes = buildAppSignatureHashes();
        private TreeSet<Integer> availableVersions;

        protected abstract String getPackage();

        private NativeAppInfo() {
        }

        private static HashSet<String> buildAppSignatureHashes() {
            HashSet<String> set = new HashSet<>();
            set.add("8a3c4b262d721acd49a4bf97d5213199c86fa2b9");
            set.add("a4b7452e2ed8f5f191058ca7bbfd26b0d3214bfc");
            set.add("5e8f16062ea3cd2c4a0d547876baa6f38cabf625");
            return set;
        }

        public boolean validateSignature(Context context, String packageName) throws PackageManager.NameNotFoundException {
            String brand = Build.BRAND;
            int applicationFlags = context.getApplicationInfo().flags;
            if (brand.startsWith("generic") && (applicationFlags & 2) != 0) {
                return true;
            }
            try {
                PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, 64);
                for (Signature signature : packageInfo.signatures) {
                    String hashedSignature = Utility.sha1hash(signature.toByteArray());
                    if (validAppSignatureHashes.contains(hashedSignature)) {
                        return true;
                    }
                }
                return false;
            } catch (PackageManager.NameNotFoundException e) {
                return false;
            }
        }

        public TreeSet<Integer> getAvailableVersions() {
            if (this.availableVersions == null) {
                fetchAvailableVersions(false);
            }
            return this.availableVersions;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void fetchAvailableVersions(boolean force) {
            if (!force) {
                if (this.availableVersions == null) {
                }
            }
            this.availableVersions = NativeProtocol.fetchAllAvailableProtocolVersionsForAppInfo(this);
        }
    }

    /* loaded from: classes2.dex */
    private static class KatanaAppInfo extends NativeAppInfo {
        private KatanaAppInfo() {
            super();
        }

        @Override // com.facebook.internal.NativeProtocol.NativeAppInfo
        protected String getPackage() {
            return "com.facebook.katana";
        }
    }

    /* loaded from: classes2.dex */
    private static class MessengerAppInfo extends NativeAppInfo {
        private MessengerAppInfo() {
            super();
        }

        @Override // com.facebook.internal.NativeProtocol.NativeAppInfo
        protected String getPackage() {
            return "com.facebook.orca";
        }
    }

    /* loaded from: classes2.dex */
    private static class WakizashiAppInfo extends NativeAppInfo {
        private WakizashiAppInfo() {
            super();
        }

        @Override // com.facebook.internal.NativeProtocol.NativeAppInfo
        protected String getPackage() {
            return "com.facebook.wakizashi";
        }
    }

    private static List<NativeAppInfo> buildFacebookAppList() {
        List<NativeAppInfo> list = new ArrayList<>();
        list.add(FACEBOOK_APP_INFO);
        list.add(new WakizashiAppInfo());
        return list;
    }

    private static Map<String, List<NativeAppInfo>> buildActionToAppInfoMap() {
        Map<String, List<NativeAppInfo>> map = new HashMap<>();
        ArrayList<NativeAppInfo> messengerAppInfoList = new ArrayList<>();
        messengerAppInfoList.add(new MessengerAppInfo());
        map.put("com.facebook.platform.action.request.OGACTIONPUBLISH_DIALOG", facebookAppInfoList);
        map.put("com.facebook.platform.action.request.FEED_DIALOG", facebookAppInfoList);
        map.put("com.facebook.platform.action.request.LIKE_DIALOG", facebookAppInfoList);
        map.put("com.facebook.platform.action.request.APPINVITES_DIALOG", facebookAppInfoList);
        map.put("com.facebook.platform.action.request.MESSAGE_DIALOG", messengerAppInfoList);
        map.put("com.facebook.platform.action.request.OGMESSAGEPUBLISH_DIALOG", messengerAppInfoList);
        return map;
    }

    static Intent validateActivityIntent(Context context, Intent intent, NativeAppInfo appInfo) {
        ResolveInfo resolveInfo;
        if (intent == null || (resolveInfo = context.getPackageManager().resolveActivity(intent, 0)) == null || !appInfo.validateSignature(context, resolveInfo.activityInfo.packageName)) {
            return null;
        }
        return intent;
    }

    static Intent validateServiceIntent(Context context, Intent intent, NativeAppInfo appInfo) {
        ResolveInfo resolveInfo;
        if (intent == null || (resolveInfo = context.getPackageManager().resolveService(intent, 0)) == null || !appInfo.validateSignature(context, resolveInfo.serviceInfo.packageName)) {
            return null;
        }
        return intent;
    }

    public static Intent createProxyAuthIntent(Context context, String applicationId, Collection<String> permissions, String e2e, boolean isRerequest, boolean isForPublish, DefaultAudience defaultAudience) {
        for (NativeAppInfo appInfo : facebookAppInfoList) {
            Intent intent = new Intent().setClassName(appInfo.getPackage(), "com.facebook.katana.ProxyAuth").putExtra("client_id", applicationId);
            if (!Utility.isNullOrEmpty(permissions)) {
                intent.putExtra("scope", TextUtils.join(",", permissions));
            }
            if (!Utility.isNullOrEmpty(e2e)) {
                intent.putExtra("e2e", e2e);
            }
            intent.putExtra("response_type", "token,signed_request");
            intent.putExtra("return_scopes", "true");
            if (isForPublish) {
                intent.putExtra("default_audience", defaultAudience.getNativeProtocolAudience());
            }
            intent.putExtra("legacy_override", "v2.4");
            if (isRerequest) {
                intent.putExtra("auth_type", "rerequest");
            }
            Intent intent2 = validateActivityIntent(context, intent, appInfo);
            if (intent2 != null) {
                return intent2;
            }
        }
        return null;
    }

    public static final int getLatestKnownVersion() {
        return KNOWN_PROTOCOL_VERSIONS.get(0).intValue();
    }

    private static Intent findActivityIntent(Context context, String activityAction, String internalAction) {
        List<NativeAppInfo> list = actionToAppInfoMap.get(internalAction);
        if (list == null) {
            return null;
        }
        Intent intent = null;
        for (NativeAppInfo appInfo : list) {
            Intent intent2 = new Intent().setAction(activityAction).setPackage(appInfo.getPackage()).addCategory("android.intent.category.DEFAULT");
            intent = validateActivityIntent(context, intent2, appInfo);
            if (intent != null) {
                return intent;
            }
        }
        return intent;
    }

    public static boolean isVersionCompatibleWithBucketedIntent(int version) {
        return KNOWN_PROTOCOL_VERSIONS.contains(Integer.valueOf(version)) && version >= 20140701;
    }

    public static Intent createPlatformActivityIntent(Context context, String callId, String action, int version, Bundle extras) {
        Intent intent = findActivityIntent(context, "com.facebook.platform.PLATFORM_ACTIVITY", action);
        if (intent == null) {
            return null;
        }
        setupProtocolRequestIntent(intent, callId, action, version, extras);
        return intent;
    }

    public static void setupProtocolRequestIntent(Intent intent, String callId, String action, int version, Bundle params) {
        String applicationId = FacebookSdk.getApplicationId();
        String applicationName = FacebookSdk.getApplicationName();
        intent.putExtra("com.facebook.platform.protocol.PROTOCOL_VERSION", version).putExtra("com.facebook.platform.protocol.PROTOCOL_ACTION", action).putExtra("com.facebook.platform.extra.APPLICATION_ID", applicationId);
        if (isVersionCompatibleWithBucketedIntent(version)) {
            Bundle bridgeArguments = new Bundle();
            bridgeArguments.putString("action_id", callId);
            Utility.putNonEmptyString(bridgeArguments, "app_name", applicationName);
            intent.putExtra("com.facebook.platform.protocol.BRIDGE_ARGS", bridgeArguments);
            Bundle methodArguments = params == null ? new Bundle() : params;
            intent.putExtra("com.facebook.platform.protocol.METHOD_ARGS", methodArguments);
            return;
        }
        intent.putExtra("com.facebook.platform.protocol.CALL_ID", callId);
        if (!Utility.isNullOrEmpty(applicationName)) {
            intent.putExtra("com.facebook.platform.extra.APPLICATION_NAME", applicationName);
        }
        intent.putExtras(params);
    }

    public static Intent createProtocolResultIntent(Intent requestIntent, Bundle results, FacebookException error) {
        UUID callId = getCallIdFromIntent(requestIntent);
        if (callId == null) {
            return null;
        }
        Intent resultIntent = new Intent();
        resultIntent.putExtra("com.facebook.platform.protocol.PROTOCOL_VERSION", getProtocolVersionFromIntent(requestIntent));
        Bundle bridgeArguments = new Bundle();
        bridgeArguments.putString("action_id", callId.toString());
        if (error != null) {
            bridgeArguments.putBundle("error", createBundleForException(error));
        }
        resultIntent.putExtra("com.facebook.platform.protocol.BRIDGE_ARGS", bridgeArguments);
        if (results != null) {
            resultIntent.putExtra("com.facebook.platform.protocol.RESULT_ARGS", results);
            return resultIntent;
        }
        return resultIntent;
    }

    public static Intent createPlatformServiceIntent(Context context) {
        for (NativeAppInfo appInfo : facebookAppInfoList) {
            Intent intent = validateServiceIntent(context, new Intent("com.facebook.platform.PLATFORM_SERVICE").setPackage(appInfo.getPackage()).addCategory("android.intent.category.DEFAULT"), appInfo);
            if (intent != null) {
                return intent;
            }
        }
        return null;
    }

    public static int getProtocolVersionFromIntent(Intent intent) {
        return intent.getIntExtra("com.facebook.platform.protocol.PROTOCOL_VERSION", 0);
    }

    public static UUID getCallIdFromIntent(Intent intent) {
        if (intent == null) {
            return null;
        }
        int version = getProtocolVersionFromIntent(intent);
        String callIdString = null;
        if (isVersionCompatibleWithBucketedIntent(version)) {
            Bundle bridgeArgs = intent.getBundleExtra("com.facebook.platform.protocol.BRIDGE_ARGS");
            if (bridgeArgs != null) {
                callIdString = bridgeArgs.getString("action_id");
            }
        } else {
            callIdString = intent.getStringExtra("com.facebook.platform.protocol.CALL_ID");
        }
        if (callIdString == null) {
            return null;
        }
        try {
            UUID callId = UUID.fromString(callIdString);
            return callId;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static Bundle getBridgeArgumentsFromIntent(Intent intent) {
        int version = getProtocolVersionFromIntent(intent);
        if (isVersionCompatibleWithBucketedIntent(version)) {
            return intent.getBundleExtra("com.facebook.platform.protocol.BRIDGE_ARGS");
        }
        return null;
    }

    public static Bundle getMethodArgumentsFromIntent(Intent intent) {
        int version = getProtocolVersionFromIntent(intent);
        return !isVersionCompatibleWithBucketedIntent(version) ? intent.getExtras() : intent.getBundleExtra("com.facebook.platform.protocol.METHOD_ARGS");
    }

    public static Bundle getSuccessResultsFromIntent(Intent resultIntent) {
        int version = getProtocolVersionFromIntent(resultIntent);
        Bundle extras = resultIntent.getExtras();
        return (!isVersionCompatibleWithBucketedIntent(version) || extras == null) ? extras : extras.getBundle("com.facebook.platform.protocol.RESULT_ARGS");
    }

    public static boolean isErrorResult(Intent resultIntent) {
        Bundle bridgeArgs = getBridgeArgumentsFromIntent(resultIntent);
        return bridgeArgs != null ? bridgeArgs.containsKey("error") : resultIntent.hasExtra("com.facebook.platform.status.ERROR_TYPE");
    }

    public static Bundle getErrorDataFromResultIntent(Intent resultIntent) {
        if (!isErrorResult(resultIntent)) {
            return null;
        }
        Bundle bridgeArgs = getBridgeArgumentsFromIntent(resultIntent);
        if (bridgeArgs != null) {
            return bridgeArgs.getBundle("error");
        }
        return resultIntent.getExtras();
    }

    public static FacebookException getExceptionFromErrorData(Bundle errorData) {
        if (errorData == null) {
            return null;
        }
        String type = errorData.getString("error_type");
        if (type == null) {
            type = errorData.getString("com.facebook.platform.status.ERROR_TYPE");
        }
        String description = errorData.getString("error_description");
        if (description == null) {
            description = errorData.getString("com.facebook.platform.status.ERROR_DESCRIPTION");
        }
        if (type != null && type.equalsIgnoreCase("UserCanceled")) {
            return new FacebookOperationCanceledException(description);
        }
        return new FacebookException(description);
    }

    public static Bundle createBundleForException(FacebookException e) {
        if (e == null) {
            return null;
        }
        Bundle errorBundle = new Bundle();
        errorBundle.putString("error_description", e.toString());
        if (e instanceof FacebookOperationCanceledException) {
            errorBundle.putString("error_type", "UserCanceled");
            return errorBundle;
        }
        return errorBundle;
    }

    public static int getLatestAvailableProtocolVersionForService(int minimumVersion) {
        return getLatestAvailableProtocolVersionForAppInfoList(facebookAppInfoList, new int[]{minimumVersion});
    }

    public static int getLatestAvailableProtocolVersionForAction(String action, int[] versionSpec) {
        List<NativeAppInfo> appInfoList = actionToAppInfoMap.get(action);
        return getLatestAvailableProtocolVersionForAppInfoList(appInfoList, versionSpec);
    }

    private static int getLatestAvailableProtocolVersionForAppInfoList(List<NativeAppInfo> appInfoList, int[] versionSpec) {
        updateAllAvailableProtocolVersionsAsync();
        if (appInfoList == null) {
            return -1;
        }
        for (NativeAppInfo appInfo : appInfoList) {
            int protocolVersion = computeLatestAvailableVersionFromVersionSpec(appInfo.getAvailableVersions(), getLatestKnownVersion(), versionSpec);
            if (protocolVersion != -1) {
                return protocolVersion;
            }
        }
        return -1;
    }

    public static void updateAllAvailableProtocolVersionsAsync() {
        if (protocolVersionsAsyncUpdating.compareAndSet(false, true)) {
            FacebookSdk.getExecutor().execute(new Runnable() { // from class: com.facebook.internal.NativeProtocol.1
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        for (NativeAppInfo appInfo : NativeProtocol.facebookAppInfoList) {
                            appInfo.fetchAvailableVersions(true);
                        }
                    } finally {
                        NativeProtocol.protocolVersionsAsyncUpdating.set(false);
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static TreeSet<Integer> fetchAllAvailableProtocolVersionsForAppInfo(NativeAppInfo appInfo) {
        TreeSet<Integer> allAvailableVersions = new TreeSet<>();
        Context appContext = FacebookSdk.getApplicationContext();
        ContentResolver contentResolver = appContext.getContentResolver();
        String[] projection = {"version"};
        Uri uri = buildPlatformProviderVersionURI(appInfo);
        Cursor c = null;
        try {
            PackageManager pm = FacebookSdk.getApplicationContext().getPackageManager();
            String contentProviderName = appInfo.getPackage() + ".provider.PlatformProvider";
            ProviderInfo pInfo = pm.resolveContentProvider(contentProviderName, 0);
            if (pInfo != null && (c = contentResolver.query(uri, projection, null, null, null)) != null) {
                while (c.moveToNext()) {
                    int version = c.getInt(c.getColumnIndex("version"));
                    allAvailableVersions.add(Integer.valueOf(version));
                }
            }
            return allAvailableVersions;
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    /* JADX WARN: Failed to analyze thrown exceptions
    java.util.ConcurrentModificationException
    	at java.base/java.util.ArrayList$Itr.checkForComodification(Unknown Source)
    	at java.base/java.util.ArrayList$Itr.next(Unknown Source)
    	at jadx.core.dex.visitors.MethodThrowsVisitor.processInstructions(MethodThrowsVisitor.java:118)
    	at jadx.core.dex.visitors.MethodThrowsVisitor.visit(MethodThrowsVisitor.java:69)
    	at jadx.core.dex.visitors.MethodThrowsVisitor.checkInsn(MethodThrowsVisitor.java:179)
    	at jadx.core.dex.visitors.MethodThrowsVisitor.processInstructions(MethodThrowsVisitor.java:132)
    	at jadx.core.dex.visitors.MethodThrowsVisitor.visit(MethodThrowsVisitor.java:69)
    	at jadx.core.dex.visitors.MethodThrowsVisitor.checkInsn(MethodThrowsVisitor.java:179)
    	at jadx.core.dex.visitors.MethodThrowsVisitor.processInstructions(MethodThrowsVisitor.java:132)
    	at jadx.core.dex.visitors.MethodThrowsVisitor.visit(MethodThrowsVisitor.java:69)
    	at jadx.core.dex.visitors.MethodThrowsVisitor.checkInsn(MethodThrowsVisitor.java:179)
    	at jadx.core.dex.visitors.MethodThrowsVisitor.processInstructions(MethodThrowsVisitor.java:132)
    	at jadx.core.dex.visitors.MethodThrowsVisitor.visit(MethodThrowsVisitor.java:69)
     */
    public static int computeLatestAvailableVersionFromVersionSpec(TreeSet<Integer> allAvailableFacebookAppVersions, int latestSdkVersion, int[] versionSpec) {
        int versionSpecIndex = versionSpec.length - 1;
        Iterator<Integer> fbAppVersionsIterator = allAvailableFacebookAppVersions.descendingIterator();
        int latestFacebookAppVersion = -1;
        while (fbAppVersionsIterator.hasNext()) {
            int fbAppVersion = fbAppVersionsIterator.next().intValue();
            latestFacebookAppVersion = Math.max(latestFacebookAppVersion, fbAppVersion);
            while (versionSpecIndex >= 0 && versionSpec[versionSpecIndex] > fbAppVersion) {
                versionSpecIndex--;
            }
            if (versionSpecIndex < 0) {
                return -1;
            }
            if (versionSpec[versionSpecIndex] == fbAppVersion) {
                return versionSpecIndex % 2 == 0 ? Math.min(latestFacebookAppVersion, latestSdkVersion) : -1;
            }
        }
        return -1;
    }

    private static Uri buildPlatformProviderVersionURI(NativeAppInfo appInfo) {
        return Uri.parse("content://" + appInfo.getPackage() + ".provider.PlatformProvider/versions");
    }
}
