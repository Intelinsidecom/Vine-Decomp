package com.google.android.gms.internal;

import android.R;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.PopupWindow;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.search.SearchAdView;
import com.google.android.gms.common.GooglePlayServicesUtil;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.UUID;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@zzha
/* loaded from: classes.dex */
public class zzip {
    private static final String zzKI = AdView.class.getName();
    private static final String zzKJ = InterstitialAd.class.getName();
    private static final String zzKK = PublisherAdView.class.getName();
    private static final String zzKL = PublisherInterstitialAd.class.getName();
    private static final String zzKM = SearchAdView.class.getName();
    private static final String zzKN = AdLoader.class.getName();
    public static final Handler zzKO = new zzim(Looper.getMainLooper());
    private String zzKi;
    private final Object zzpK = new Object();
    private boolean zzKP = true;
    private boolean zzKQ = false;

    private final class zza extends BroadcastReceiver {
        private zza() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if ("android.intent.action.USER_PRESENT".equals(intent.getAction())) {
                zzip.this.zzKP = true;
            } else if ("android.intent.action.SCREEN_OFF".equals(intent.getAction())) {
                zzip.this.zzKP = false;
            }
        }
    }

    public static void runOnUiThread(Runnable runnable) {
        if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
            runnable.run();
        } else {
            zzKO.post(runnable);
        }
    }

    private JSONArray zza(Collection<?> collection) throws JSONException {
        JSONArray jSONArray = new JSONArray();
        Iterator<?> it = collection.iterator();
        while (it.hasNext()) {
            zza(jSONArray, it.next());
        }
        return jSONArray;
    }

    private void zza(JSONArray jSONArray, Object obj) throws JSONException {
        if (obj instanceof Bundle) {
            jSONArray.put(zzf((Bundle) obj));
            return;
        }
        if (obj instanceof Map) {
            jSONArray.put(zzz((Map) obj));
            return;
        }
        if (obj instanceof Collection) {
            jSONArray.put(zza((Collection<?>) obj));
        } else if (obj instanceof Object[]) {
            jSONArray.put(zza((Object[]) obj));
        } else {
            jSONArray.put(obj);
        }
    }

    private void zza(JSONObject jSONObject, String str, Object obj) throws JSONException {
        if (obj instanceof Bundle) {
            jSONObject.put(str, zzf((Bundle) obj));
            return;
        }
        if (obj instanceof Map) {
            jSONObject.put(str, zzz((Map) obj));
            return;
        }
        if (obj instanceof Collection) {
            if (str == null) {
                str = "null";
            }
            jSONObject.put(str, zza((Collection<?>) obj));
        } else if (obj instanceof Object[]) {
            jSONObject.put(str, zza(Arrays.asList((Object[]) obj)));
        } else {
            jSONObject.put(str, obj);
        }
    }

    static String zzb(String str, String str2, int i) {
        StringTokenizer stringTokenizer = new StringTokenizer(str, str2);
        StringBuilder sb = new StringBuilder();
        int i2 = i - 1;
        if (i <= 0 || !stringTokenizer.hasMoreElements()) {
            return str;
        }
        sb.append(stringTokenizer.nextToken());
        while (true) {
            int i3 = i2 - 1;
            if (i2 <= 0 || !stringTokenizer.hasMoreElements()) {
                break;
            }
            sb.append(".").append(stringTokenizer.nextToken());
            i2 = i3;
        }
        return sb.toString();
    }

    private JSONObject zzf(Bundle bundle) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        for (String str : bundle.keySet()) {
            zza(jSONObject, str, bundle.get(str));
        }
        return jSONObject;
    }

    private boolean zzs(Context context) {
        PowerManager powerManager = (PowerManager) context.getSystemService("power");
        if (powerManager == null) {
            return false;
        }
        return powerManager.isScreenOn();
    }

    public boolean zzJ(Context context) {
        boolean z;
        Intent intent = new Intent();
        intent.setClassName(context, "com.google.android.gms.ads.AdActivity");
        ResolveInfo resolveInfoResolveActivity = context.getPackageManager().resolveActivity(intent, 65536);
        if (resolveInfoResolveActivity == null || resolveInfoResolveActivity.activityInfo == null) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("Could not find com.google.android.gms.ads.AdActivity, please make sure it is declared in AndroidManifest.xml.");
            return false;
        }
        if ((resolveInfoResolveActivity.activityInfo.configChanges & 16) == 0) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH(String.format("com.google.android.gms.ads.AdActivity requires the android:configChanges value to contain \"%s\".", "keyboard"));
            z = false;
        } else {
            z = true;
        }
        if ((resolveInfoResolveActivity.activityInfo.configChanges & 32) == 0) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH(String.format("com.google.android.gms.ads.AdActivity requires the android:configChanges value to contain \"%s\".", "keyboardHidden"));
            z = false;
        }
        if ((resolveInfoResolveActivity.activityInfo.configChanges & 128) == 0) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH(String.format("com.google.android.gms.ads.AdActivity requires the android:configChanges value to contain \"%s\".", "orientation"));
            z = false;
        }
        if ((resolveInfoResolveActivity.activityInfo.configChanges & 256) == 0) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH(String.format("com.google.android.gms.ads.AdActivity requires the android:configChanges value to contain \"%s\".", "screenLayout"));
            z = false;
        }
        if ((resolveInfoResolveActivity.activityInfo.configChanges & 512) == 0) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH(String.format("com.google.android.gms.ads.AdActivity requires the android:configChanges value to contain \"%s\".", "uiMode"));
            z = false;
        }
        if ((resolveInfoResolveActivity.activityInfo.configChanges & 1024) == 0) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH(String.format("com.google.android.gms.ads.AdActivity requires the android:configChanges value to contain \"%s\".", "screenSize"));
            z = false;
        }
        if ((resolveInfoResolveActivity.activityInfo.configChanges & 2048) != 0) {
            return z;
        }
        com.google.android.gms.ads.internal.util.client.zzb.zzaH(String.format("com.google.android.gms.ads.AdActivity requires the android:configChanges value to contain \"%s\".", "smallestScreenSize"));
        return false;
    }

    public boolean zzK(Context context) {
        if (this.zzKQ) {
            return false;
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.USER_PRESENT");
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        context.getApplicationContext().registerReceiver(new zza(), intentFilter);
        this.zzKQ = true;
        return true;
    }

    protected String zzL(Context context) {
        return new WebView(context).getSettings().getUserAgentString();
    }

    public AlertDialog.Builder zzM(Context context) {
        return new AlertDialog.Builder(context);
    }

    public zzbr zzN(Context context) {
        return new zzbr(context);
    }

    public String zzO(Context context) throws SecurityException {
        ActivityManager activityManager;
        ActivityManager.RunningTaskInfo runningTaskInfo;
        try {
            activityManager = (ActivityManager) context.getSystemService("activity");
        } catch (Exception e) {
        }
        if (activityManager == null) {
            return null;
        }
        List<ActivityManager.RunningTaskInfo> runningTasks = activityManager.getRunningTasks(1);
        if (runningTasks != null && !runningTasks.isEmpty() && (runningTaskInfo = runningTasks.get(0)) != null && runningTaskInfo.topActivity != null) {
            return runningTaskInfo.topActivity.getClassName();
        }
        return null;
    }

    /* JADX WARN: Code restructure failed: missing block: B:17:0x003b, code lost:
    
        if (r0.importance != 100) goto L23;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0041, code lost:
    
        if (r1.inKeyguardRestrictedInputMode() != false) goto L23;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x0047, code lost:
    
        if (zzs(r7) == false) goto L23;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x0049, code lost:
    
        return true;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean zzP(android.content.Context r7) {
        /*
            r6 = this;
            r2 = 0
            java.lang.String r0 = "activity"
            java.lang.Object r0 = r7.getSystemService(r0)     // Catch: java.lang.Throwable -> L4d
            android.app.ActivityManager r0 = (android.app.ActivityManager) r0     // Catch: java.lang.Throwable -> L4d
            java.lang.String r1 = "keyguard"
            java.lang.Object r1 = r7.getSystemService(r1)     // Catch: java.lang.Throwable -> L4d
            android.app.KeyguardManager r1 = (android.app.KeyguardManager) r1     // Catch: java.lang.Throwable -> L4d
            if (r0 == 0) goto L15
            if (r1 != 0) goto L17
        L15:
            r0 = r2
        L16:
            return r0
        L17:
            java.util.List r0 = r0.getRunningAppProcesses()     // Catch: java.lang.Throwable -> L4d
            if (r0 != 0) goto L1f
            r0 = r2
            goto L16
        L1f:
            java.util.Iterator r3 = r0.iterator()     // Catch: java.lang.Throwable -> L4d
        L23:
            boolean r0 = r3.hasNext()     // Catch: java.lang.Throwable -> L4d
            if (r0 == 0) goto L4b
            java.lang.Object r0 = r3.next()     // Catch: java.lang.Throwable -> L4d
            android.app.ActivityManager$RunningAppProcessInfo r0 = (android.app.ActivityManager.RunningAppProcessInfo) r0     // Catch: java.lang.Throwable -> L4d
            int r4 = android.os.Process.myPid()     // Catch: java.lang.Throwable -> L4d
            int r5 = r0.pid     // Catch: java.lang.Throwable -> L4d
            if (r4 != r5) goto L23
            int r0 = r0.importance     // Catch: java.lang.Throwable -> L4d
            r3 = 100
            if (r0 != r3) goto L4b
            boolean r0 = r1.inKeyguardRestrictedInputMode()     // Catch: java.lang.Throwable -> L4d
            if (r0 != 0) goto L4b
            boolean r0 = r6.zzs(r7)     // Catch: java.lang.Throwable -> L4d
            if (r0 == 0) goto L4b
            r0 = 1
            goto L16
        L4b:
            r0 = r2
            goto L16
        L4d:
            r0 = move-exception
            r0 = r2
            goto L16
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzip.zzP(android.content.Context):boolean");
    }

    public Bitmap zzQ(Context context) {
        if (!(context instanceof Activity)) {
            return null;
        }
        try {
            View decorView = ((Activity) context).getWindow().getDecorView();
            Bitmap bitmapCreateBitmap = Bitmap.createBitmap(decorView.getWidth(), decorView.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmapCreateBitmap);
            decorView.layout(0, 0, decorView.getWidth(), decorView.getHeight());
            decorView.draw(canvas);
            return bitmapCreateBitmap;
        } catch (RuntimeException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzb("Fail to capture screen shot", e);
            return null;
        }
    }

    public DisplayMetrics zza(WindowManager windowManager) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }

    public PopupWindow zza(View view, int i, int i2, boolean z) {
        return new PopupWindow(view, i, i2, z);
    }

    public String zza(Context context, View view, AdSizeParcel adSizeParcel) throws JSONException {
        if (!zzbz.zzwe.get().booleanValue()) {
            return null;
        }
        try {
            JSONObject jSONObject = new JSONObject();
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("width", adSizeParcel.width);
            jSONObject2.put("height", adSizeParcel.height);
            jSONObject.put("size", jSONObject2);
            jSONObject.put("activity", zzO(context));
            if (!adSizeParcel.zztW) {
                JSONArray jSONArray = new JSONArray();
                while (view != null) {
                    Object parent = view.getParent();
                    if (parent != null) {
                        int iIndexOfChild = parent instanceof ViewGroup ? ((ViewGroup) parent).indexOfChild(view) : -1;
                        JSONObject jSONObject3 = new JSONObject();
                        jSONObject3.put("type", parent.getClass().getName());
                        jSONObject3.put("index_of_child", iIndexOfChild);
                        jSONArray.put(jSONObject3);
                    }
                    view = (parent == null || !(parent instanceof View)) ? null : (View) parent;
                }
                if (jSONArray.length() > 0) {
                    jSONObject.put("parents", jSONArray);
                }
            }
            return jSONObject.toString();
        } catch (JSONException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Fail to get view hierarchy json", e);
            return null;
        }
    }

    public String zza(Context context, zzan zzanVar, String str) {
        if (zzanVar == null) {
            return str;
        }
        try {
            Uri uriZza = Uri.parse(str);
            if (zzanVar.zzc(uriZza)) {
                uriZza = zzanVar.zza(uriZza, context);
            }
            return uriZza.toString();
        } catch (Exception e) {
            return str;
        }
    }

    public String zza(zzjn zzjnVar, String str) {
        return zza(zzjnVar.getContext(), zzjnVar.zzhE(), str);
    }

    public String zza(InputStreamReader inputStreamReader) throws IOException {
        StringBuilder sb = new StringBuilder(8192);
        char[] cArr = new char[2048];
        while (true) {
            int i = inputStreamReader.read(cArr);
            if (i == -1) {
                return sb.toString();
            }
            sb.append(cArr, 0, i);
        }
    }

    public String zza(StackTraceElement[] stackTraceElementArr, String str) {
        String className;
        if (zzbz.zzwx.get().booleanValue()) {
            for (int i = 0; i + 1 < stackTraceElementArr.length; i++) {
                StackTraceElement stackTraceElement = stackTraceElementArr[i];
                String className2 = stackTraceElement.getClassName();
                if ("loadAd".equalsIgnoreCase(stackTraceElement.getMethodName()) && (zzKI.equalsIgnoreCase(className2) || zzKJ.equalsIgnoreCase(className2) || zzKK.equalsIgnoreCase(className2) || zzKL.equalsIgnoreCase(className2) || zzKM.equalsIgnoreCase(className2) || zzKN.equalsIgnoreCase(className2))) {
                    className = stackTraceElementArr[i + 1].getClassName();
                    break;
                }
            }
            className = null;
            if (str != null) {
                String strZzb = zzb(str, ".", 3);
                if (className != null && !className.contains(strZzb)) {
                    return className;
                }
            }
        }
        return null;
    }

    JSONArray zza(Object[] objArr) throws JSONException {
        JSONArray jSONArray = new JSONArray();
        for (Object obj : objArr) {
            zza(jSONArray, obj);
        }
        return jSONArray;
    }

    public void zza(Activity activity, ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener) {
        Window window = activity.getWindow();
        if (window == null || window.getDecorView() == null || window.getDecorView().getViewTreeObserver() == null) {
            return;
        }
        window.getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
    }

    public void zza(Activity activity, ViewTreeObserver.OnScrollChangedListener onScrollChangedListener) {
        Window window = activity.getWindow();
        if (window == null || window.getDecorView() == null || window.getDecorView().getViewTreeObserver() == null) {
            return;
        }
        window.getDecorView().getViewTreeObserver().addOnScrollChangedListener(onScrollChangedListener);
    }

    public void zza(Context context, String str, WebSettings webSettings) {
        webSettings.setUserAgentString(zzd(context, str));
    }

    public void zza(Context context, String str, String str2, Bundle bundle, boolean z) {
        if (z) {
            Context applicationContext = context.getApplicationContext();
            if (applicationContext == null) {
                applicationContext = context;
            }
            bundle.putString("os", Build.VERSION.RELEASE);
            bundle.putString("api", String.valueOf(Build.VERSION.SDK_INT));
            bundle.putString("device", com.google.android.gms.ads.internal.zzp.zzbx().zzhb());
            bundle.putString("appid", applicationContext.getPackageName());
            bundle.putString("eids", TextUtils.join(",", zzbz.zzdl()));
            if (str != null) {
                bundle.putString("js", str);
            } else {
                bundle.putString("gmscore_version", Integer.toString(GooglePlayServicesUtil.zzan(context)));
            }
        }
        Uri.Builder builderAppendQueryParameter = new Uri.Builder().scheme("https").path("//pagead2.googlesyndication.com/pagead/gen_204").appendQueryParameter("id", str2);
        for (String str3 : bundle.keySet()) {
            builderAppendQueryParameter.appendQueryParameter(str3, bundle.getString(str3));
        }
        com.google.android.gms.ads.internal.zzp.zzbx().zzc(context, str, builderAppendQueryParameter.toString());
    }

    public void zza(Context context, String str, List<String> list) {
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            new zziw(context, str, it.next()).zzfR();
        }
    }

    public void zza(Context context, String str, List<String> list, String str2) {
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            new zziw(context, str, it.next(), str2).zzfR();
        }
    }

    public void zza(Context context, String str, boolean z, HttpURLConnection httpURLConnection) {
        zza(context, str, z, httpURLConnection, false);
    }

    public void zza(Context context, String str, boolean z, HttpURLConnection httpURLConnection, String str2) {
        httpURLConnection.setConnectTimeout(60000);
        httpURLConnection.setInstanceFollowRedirects(z);
        httpURLConnection.setReadTimeout(60000);
        httpURLConnection.setRequestProperty("User-Agent", str2);
        httpURLConnection.setUseCaches(false);
    }

    public void zza(Context context, String str, boolean z, HttpURLConnection httpURLConnection, boolean z2) {
        httpURLConnection.setConnectTimeout(60000);
        httpURLConnection.setInstanceFollowRedirects(z);
        httpURLConnection.setReadTimeout(60000);
        httpURLConnection.setRequestProperty("User-Agent", zzd(context, str));
        httpURLConnection.setUseCaches(z2);
    }

    public boolean zza(Context context, Bitmap bitmap, String str) throws IOException {
        com.google.android.gms.common.internal.zzx.zzcy("saveImageToFile must not be called on the main UI thread.");
        try {
            FileOutputStream fileOutputStreamOpenFileOutput = context.openFileOutput(str, 0);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStreamOpenFileOutput);
            fileOutputStreamOpenFileOutput.close();
            bitmap.recycle();
            return true;
        } catch (Exception e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzb("Fail to save file", e);
            return false;
        }
    }

    public boolean zza(PackageManager packageManager, String str, String str2) {
        return packageManager.checkPermission(str2, str) == 0;
    }

    public boolean zza(ClassLoader classLoader, Class<?> cls, String str) {
        try {
            return cls.isAssignableFrom(Class.forName(str, false, classLoader));
        } catch (Throwable th) {
            return false;
        }
    }

    public int zzaA(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("Could not parse value:" + e);
            return 0;
        }
    }

    public boolean zzaB(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return str.matches("([^\\s]+(\\.(?i)(jpg|png|gif|bmp|webp))$)");
    }

    public String zzaz(String str) {
        return Uri.parse(str).buildUpon().query(null).build().toString();
    }

    public void zzb(Activity activity, ViewTreeObserver.OnScrollChangedListener onScrollChangedListener) {
        Window window = activity.getWindow();
        if (window == null || window.getDecorView() == null || window.getDecorView().getViewTreeObserver() == null) {
            return;
        }
        window.getDecorView().getViewTreeObserver().removeOnScrollChangedListener(onScrollChangedListener);
    }

    public void zzb(Context context, Intent intent) {
        try {
            context.startActivity(intent);
        } catch (Throwable th) {
            intent.addFlags(268435456);
            context.startActivity(intent);
        }
    }

    public void zzb(Context context, String str, String str2, Bundle bundle, boolean z) {
        if (zzbz.zzws.get().booleanValue()) {
            zza(context, str, str2, bundle, z);
        }
    }

    public void zzc(Context context, String str, String str2) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(str2);
        zza(context, str, arrayList);
    }

    public String zzd(final Context context, String str) {
        String str2;
        synchronized (this.zzpK) {
            if (this.zzKi != null) {
                str2 = this.zzKi;
            } else {
                try {
                    this.zzKi = com.google.android.gms.ads.internal.zzp.zzbz().getDefaultUserAgent(context);
                } catch (Exception e) {
                }
                if (TextUtils.isEmpty(this.zzKi)) {
                    if (com.google.android.gms.ads.internal.client.zzl.zzcN().zzhr()) {
                        try {
                            this.zzKi = zzL(context);
                        } catch (Exception e2) {
                            this.zzKi = zzgZ();
                        }
                    } else {
                        this.zzKi = null;
                        zzKO.post(new Runnable() { // from class: com.google.android.gms.internal.zzip.1
                            @Override // java.lang.Runnable
                            public void run() {
                                synchronized (zzip.this.zzpK) {
                                    zzip.this.zzKi = zzip.this.zzL(context);
                                    zzip.this.zzpK.notifyAll();
                                }
                            }
                        });
                        while (this.zzKi == null) {
                            try {
                                this.zzpK.wait();
                            } catch (InterruptedException e3) {
                                this.zzKi = zzgZ();
                                com.google.android.gms.ads.internal.util.client.zzb.zzaH("Interrupted, use default user agent: " + this.zzKi);
                            }
                        }
                    }
                }
                this.zzKi += " (Mobile; " + str + ")";
                str2 = this.zzKi;
            }
        }
        return str2;
    }

    public Bitmap zze(Context context, String str) throws IOException {
        com.google.android.gms.common.internal.zzx.zzcy("getBackgroundImage must not be called on the main UI thread.");
        try {
            FileInputStream fileInputStreamOpenFileInput = context.openFileInput(str);
            Bitmap bitmapDecodeStream = BitmapFactory.decodeStream(fileInputStreamOpenFileInput);
            fileInputStreamOpenFileInput.close();
            return bitmapDecodeStream;
        } catch (Exception e) {
            com.google.android.gms.ads.internal.util.client.zzb.e("Fail to get background image");
            return null;
        }
    }

    public Map<String, String> zze(Uri uri) {
        if (uri == null) {
            return null;
        }
        HashMap map = new HashMap();
        for (String str : com.google.android.gms.ads.internal.zzp.zzbz().zzf(uri)) {
            map.put(str, uri.getQueryParameter(str));
        }
        return map;
    }

    public void zzf(Context context, String str) {
        com.google.android.gms.common.internal.zzx.zzcy("deleteFile must not be called on the main UI thread.");
        context.deleteFile(str);
    }

    public int[] zzg(Activity activity) {
        View viewFindViewById;
        Window window = activity.getWindow();
        return (window == null || (viewFindViewById = window.findViewById(R.id.content)) == null) ? zzhc() : new int[]{viewFindViewById.getWidth(), viewFindViewById.getHeight()};
    }

    public boolean zzgY() {
        return this.zzKP;
    }

    String zzgZ() {
        StringBuffer stringBuffer = new StringBuffer(256);
        stringBuffer.append("Mozilla/5.0 (Linux; U; Android");
        if (Build.VERSION.RELEASE != null) {
            stringBuffer.append(" ").append(Build.VERSION.RELEASE);
        }
        stringBuffer.append("; ").append(Locale.getDefault());
        if (Build.DEVICE != null) {
            stringBuffer.append("; ").append(Build.DEVICE);
            if (Build.DISPLAY != null) {
                stringBuffer.append(" Build/").append(Build.DISPLAY);
            }
        }
        stringBuffer.append(") AppleWebKit/533 Version/4.0 Safari/533");
        return stringBuffer.toString();
    }

    public int[] zzh(Activity activity) {
        int[] iArrZzg = zzg(activity);
        return new int[]{com.google.android.gms.ads.internal.client.zzl.zzcN().zzc(activity, iArrZzg[0]), com.google.android.gms.ads.internal.client.zzl.zzcN().zzc(activity, iArrZzg[1])};
    }

    public String zzha() throws NoSuchAlgorithmException {
        UUID uuidRandomUUID = UUID.randomUUID();
        byte[] byteArray = BigInteger.valueOf(uuidRandomUUID.getLeastSignificantBits()).toByteArray();
        byte[] byteArray2 = BigInteger.valueOf(uuidRandomUUID.getMostSignificantBits()).toByteArray();
        String string = new BigInteger(1, byteArray).toString();
        for (int i = 0; i < 2; i++) {
            try {
                MessageDigest messageDigest = MessageDigest.getInstance("MD5");
                messageDigest.update(byteArray);
                messageDigest.update(byteArray2);
                byte[] bArr = new byte[8];
                System.arraycopy(messageDigest.digest(), 0, bArr, 0, 8);
                string = new BigInteger(1, bArr).toString();
            } catch (NoSuchAlgorithmException e) {
            }
        }
        return string;
    }

    public String zzhb() {
        String str = Build.MANUFACTURER;
        String str2 = Build.MODEL;
        return str2.startsWith(str) ? str2 : str + " " + str2;
    }

    protected int[] zzhc() {
        return new int[]{0, 0};
    }

    public int[] zzi(Activity activity) {
        View viewFindViewById;
        Window window = activity.getWindow();
        return (window == null || (viewFindViewById = window.findViewById(R.id.content)) == null) ? zzhc() : new int[]{viewFindViewById.getTop(), viewFindViewById.getBottom()};
    }

    public int[] zzj(Activity activity) {
        int[] iArrZzi = zzi(activity);
        return new int[]{com.google.android.gms.ads.internal.client.zzl.zzcN().zzc(activity, iArrZzi[0]), com.google.android.gms.ads.internal.client.zzl.zzcN().zzc(activity, iArrZzi[1])};
    }

    public Bitmap zzl(View view) {
        view.setDrawingCacheEnabled(true);
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        return bitmapCreateBitmap;
    }

    public JSONObject zzz(Map<String, ?> map) throws JSONException {
        try {
            JSONObject jSONObject = new JSONObject();
            for (String str : map.keySet()) {
                zza(jSONObject, str, map.get(str));
            }
            return jSONObject;
        } catch (ClassCastException e) {
            throw new JSONException("Could not convert map to JSON: " + e.getMessage());
        }
    }
}
