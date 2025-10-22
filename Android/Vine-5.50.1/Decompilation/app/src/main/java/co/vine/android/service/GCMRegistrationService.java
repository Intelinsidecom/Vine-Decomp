package co.vine.android.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.IBinder;
import co.vine.android.client.AppController;
import co.vine.android.client.AppSessionListener;
import co.vine.android.util.BuildUtil;
import com.edisonwang.android.slog.SLog;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public class GCMRegistrationService extends Service {
    private static SharedPreferences sPrefs;
    private AppController mAppController;
    private GoogleCloudMessaging mGcm;
    private AppSessionListener mGcmRegistrationListener;
    private String mSenderId;
    private ArrayList<Integer> mStartIds;

    public static Intent getRegisterIntent(Context context, long userId) {
        Intent intent = new Intent(context, (Class<?>) GCMRegistrationService.class);
        intent.setAction("co.vine.android.gcm.REGISTER");
        intent.putExtra("user_id", userId);
        return intent;
    }

    public static Intent getUnregisterIntent(Context context, long userId, String key) {
        Intent intent = new Intent(context, (Class<?>) GCMRegistrationService.class);
        intent.setAction("co.vine.android.gcm.UNREGISTER");
        intent.putExtra("user_id", userId);
        intent.putExtra("s_key", key);
        return intent;
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        this.mGcm = GoogleCloudMessaging.getInstance(this);
        this.mAppController = AppController.getInstance(this);
        this.mGcmRegistrationListener = new GCMRegistrationListener();
        if (this.mGcmRegistrationListener != null) {
            this.mAppController.addListener(this.mGcmRegistrationListener);
        }
        this.mSenderId = BuildUtil.getSenderId(this);
        this.mStartIds = new ArrayList<>();
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int flags, int startId) throws PackageManager.NameNotFoundException {
        SLog.dWithTag("GCMRegService", "onStartCommand, startId=" + startId + ", startIds=" + this.mStartIds.toString());
        this.mStartIds.add(Integer.valueOf(startId));
        if (intent != null) {
            String regId = getRegistrationId();
            SLog.dWithTag("GCMRegService", "Starting GCM registration service with regId=" + regId);
            long userId = intent.getLongExtra("user_id", 0L);
            String action = intent.getAction();
            if ("co.vine.android.gcm.REGISTER".equals(action)) {
                if (regId == null) {
                    new GCMRegisterTask(userId).execute(new Void[0]);
                } else if (!getGCMPreferences(this).getBoolean("registrationIdSent", false)) {
                    this.mAppController.sendGcmRegId(regId, userId);
                } else {
                    stopService();
                }
            } else if ("co.vine.android.gcm.UNREGISTER".equals(action)) {
                String key = intent.getStringExtra("s_key");
                new GCMUnregisterTask(regId, userId, key).execute(new Void[0]);
            } else {
                stopService();
            }
        }
        return 2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopService() {
        Iterator<Integer> it = this.mStartIds.iterator();
        while (it.hasNext()) {
            int startId = it.next().intValue();
            SLog.dWithTag("GCMRegService", "Stopping service for startId=" + startId);
            stopSelf(startId);
        }
    }

    private String getRegistrationId() throws PackageManager.NameNotFoundException {
        SharedPreferences prefs = getGCMPreferences(this);
        String registrationId = prefs.getString("registrationId", null);
        if (registrationId == null) {
            SLog.dWithTag("GCMRegService", "Registration not found.");
            return null;
        }
        int registeredVersion = prefs.getInt("registrationVersion", Integer.MIN_VALUE);
        int currentVersion = getAppVersion();
        if (registeredVersion != currentVersion || isRegistrationExpired()) {
            SLog.dWithTag("GCMRegService", "App version changed or registration expired.");
            return null;
        }
        return registrationId;
    }

    private int getAppVersion() throws PackageManager.NameNotFoundException {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return -1;
        }
    }

    private boolean isRegistrationExpired() {
        SharedPreferences prefs = getGCMPreferences(this);
        long expirationTime = prefs.getLong("registrationExpiryTime", -1L);
        return System.currentTimeMillis() > expirationTime;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setRegistrationId(String regId) throws PackageManager.NameNotFoundException {
        SharedPreferences prefs = getGCMPreferences(this);
        int appVersion = getAppVersion();
        long expirationTime = System.currentTimeMillis() + 604800000;
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("registrationIdSent", false);
        editor.putInt("registrationVersion", appVersion);
        editor.putLong("registrationExpiryTime", expirationTime);
        editor.putString("registrationId", regId);
        editor.apply();
    }

    public static SharedPreferences getGCMPreferences(Context context) {
        if (sPrefs == null) {
            sPrefs = context.getSharedPreferences("gcmPrefs", 0);
        }
        return sPrefs;
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
        if (this.mGcmRegistrationListener != null) {
            this.mAppController.removeListener(this.mGcmRegistrationListener);
        }
        this.mGcm.close();
    }

    private class GCMRegisterTask extends AsyncTask<Void, Void, Void> {
        private long mUserId;

        public GCMRegisterTask(long userId) {
            this.mUserId = userId;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public Void doInBackground(Void... params) throws PackageManager.NameNotFoundException {
            if (GCMRegistrationService.this.mGcm == null) {
                GCMRegistrationService.this.mGcm = GoogleCloudMessaging.getInstance(getContext());
            }
            try {
                SLog.dWithTag("GCMRegService", "Registering with senderId=" + GCMRegistrationService.this.mSenderId);
                String regId = GCMRegistrationService.this.mGcm.register(GCMRegistrationService.this.mSenderId);
                SLog.d("GCM: Registration complete with regId={}", regId);
                GCMRegistrationService.this.setRegistrationId(regId);
                GCMRegistrationService.this.mAppController.sendGcmRegId(regId, this.mUserId);
                return null;
            } catch (IOException e) {
                SLog.dWithTag("GCMRegService", "Failed to register with GCM service.");
                return null;
            } catch (SecurityException e2) {
                return null;
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(Void aVoid) {
            GCMRegistrationService.this.stopService();
        }

        private Context getContext() {
            return GCMRegistrationService.this;
        }
    }

    private class GCMUnregisterTask extends AsyncTask<Void, Void, Void> {
        private String mRegId;
        private String mSessionKey;
        private long mUserId;

        public GCMUnregisterTask(String regId, long userId, String key) {
            this.mRegId = regId;
            this.mUserId = userId;
            this.mSessionKey = key;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public Void doInBackground(Void... voids) {
            if (GCMRegistrationService.this.mGcm == null) {
                GCMRegistrationService.this.mGcm = GoogleCloudMessaging.getInstance(getContext());
            }
            SLog.dWithTag("GCMRegService", "Unregistering...");
            GCMRegistrationService.this.mAppController.clearGcmRegId(this.mRegId, this.mUserId, this.mSessionKey);
            GCMRegistrationService.getGCMPreferences(getContext()).edit().clear().commit();
            return null;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(Void aVoid) {
            GCMRegistrationService.this.startService(GCMNotificationService.getClearNotificationIntent(getContext(), 1));
            GCMRegistrationService.this.startService(GCMNotificationService.getClearNotificationIntent(getContext(), 2));
            GCMRegistrationService.this.stopService();
        }

        private Context getContext() {
            return GCMRegistrationService.this;
        }
    }

    private class GCMRegistrationListener extends AppSessionListener {
        private GCMRegistrationListener() {
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onGcmRegistrationComplete(String reqId, int statusCode, String reasonPhrase, long userId) {
            if (statusCode == 200 && userId > 0) {
                SLog.dWithTag("GCMRegService", "GCM registration completed successfully; saving regId and stopping service now.");
                SharedPreferences.Editor editor = GCMRegistrationService.getGCMPreferences(GCMRegistrationService.this).edit();
                editor.putBoolean("registrationIdSent", true);
                editor.apply();
            }
            GCMRegistrationService.this.stopService();
        }
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }
}
