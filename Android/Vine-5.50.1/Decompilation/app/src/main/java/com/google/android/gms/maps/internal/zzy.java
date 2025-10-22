package com.google.android.gms.maps.internal;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.internal.zzc;
import com.google.android.gms.maps.model.RuntimeRemoteException;

/* loaded from: classes.dex */
public class zzy {
    private static Context zzaPW;
    private static zzc zzaPX;

    private static Context getRemoteContext(Context context) {
        if (zzaPW == null) {
            if (zzzr()) {
                zzaPW = context.getApplicationContext();
            } else {
                zzaPW = GooglePlayServicesUtil.getRemoteContext(context);
            }
        }
        return zzaPW;
    }

    private static <T> T zza(ClassLoader classLoader, String str) {
        try {
            return (T) zzc(((ClassLoader) com.google.android.gms.common.internal.zzx.zzy(classLoader)).loadClass(str));
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Unable to find dynamic class " + str);
        }
    }

    public static zzc zzaP(Context context) throws GooglePlayServicesNotAvailableException, PackageManager.NameNotFoundException {
        com.google.android.gms.common.internal.zzx.zzy(context);
        if (zzaPX != null) {
            return zzaPX;
        }
        zzaQ(context);
        zzaPX = zzaR(context);
        try {
            zzaPX.zzd(com.google.android.gms.dynamic.zze.zzB(getRemoteContext(context).getResources()), GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE);
            return zzaPX;
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    private static void zzaQ(Context context) throws GooglePlayServicesNotAvailableException, PackageManager.NameNotFoundException {
        int iIsGooglePlayServicesAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        switch (iIsGooglePlayServicesAvailable) {
            case 0:
                return;
            default:
                throw new GooglePlayServicesNotAvailableException(iIsGooglePlayServicesAvailable);
        }
    }

    private static zzc zzaR(Context context) {
        if (zzzr()) {
            Log.i(zzy.class.getSimpleName(), "Making Creator statically");
            return (zzc) zzc(zzzs());
        }
        Log.i(zzy.class.getSimpleName(), "Making Creator dynamically");
        return zzc.zza.zzcu((IBinder) zza(getRemoteContext(context).getClassLoader(), "com.google.android.gms.maps.internal.CreatorImpl"));
    }

    private static <T> T zzc(Class<?> cls) {
        try {
            return (T) cls.newInstance();
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Unable to call the default constructor of " + cls.getName());
        } catch (InstantiationException e2) {
            throw new IllegalStateException("Unable to instantiate the dynamic class " + cls.getName());
        }
    }

    public static boolean zzzr() {
        return false;
    }

    private static Class<?> zzzs() {
        try {
            return Class.forName("com.google.android.gms.maps.internal.CreatorImpl");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
