package com.google.android.gms.internal;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import com.google.ads.afma.nano.NanoAdshieldEvent;
import com.google.android.gms.clearcut.zza;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.internal.zzar;
import dalvik.system.DexClassLoader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import javax.crypto.NoSuchPaddingException;

/* loaded from: classes.dex */
public abstract class zzal extends zzak {
    private static Method zznA;
    private static Method zznB;
    private static Method zznC;
    private static Method zznD;
    private static Method zznE;
    private static Method zznF;
    private static String zznG;
    private static String zznH;
    private static String zznI;
    private static zzar zznJ;
    protected static NanoAdshieldEvent.AdShieldEvent zznM;
    protected static int zznN;
    private static boolean zznQ;
    private static Method zznt;
    private static Method zznu;
    private static Method zznv;
    private static Method zznw;
    private static Method zznx;
    private static Method zzny;
    private static Method zznz;
    private static long startTime = 0;
    static boolean zznK = false;
    protected static com.google.android.gms.clearcut.zza zznL = null;
    private static Random zznO = new Random();
    private static GoogleApiAvailability zznP = GoogleApiAvailability.getInstance();
    protected static boolean zznR = false;
    protected static boolean zznS = false;
    protected static boolean zznT = false;
    protected static boolean zznU = false;

    static class zza extends Exception {
        public zza() {
        }

        public zza(Throwable th) {
            super(th);
        }
    }

    protected zzal(Context context, zzap zzapVar, zzaq zzaqVar) {
        super(context, zzapVar, zzaqVar);
        zznM = new NanoAdshieldEvent.AdShieldEvent();
        zznM.appId = context.getPackageName();
    }

    private void zzU() {
        if (!zznU || zznL == null) {
            return;
        }
        zznL.zza(zznr, 100L, TimeUnit.MILLISECONDS);
        zznr.disconnect();
    }

    static String zzV() throws zza {
        if (zznG == null) {
            throw new zza();
        }
        return zznG;
    }

    static Long zzW() throws zza {
        if (zznt == null) {
            throw new zza();
        }
        try {
            return (Long) zznt.invoke(null, new Object[0]);
        } catch (IllegalAccessException e) {
            throw new zza(e);
        } catch (InvocationTargetException e2) {
            throw new zza(e2);
        }
    }

    static String zzX() throws zza {
        if (zznv == null) {
            throw new zza();
        }
        try {
            return (String) zznv.invoke(null, new Object[0]);
        } catch (IllegalAccessException e) {
            throw new zza(e);
        } catch (InvocationTargetException e2) {
            throw new zza(e2);
        }
    }

    static Long zzY() throws zza {
        if (zznu == null) {
            throw new zza();
        }
        try {
            return (Long) zznu.invoke(null, new Object[0]);
        } catch (IllegalAccessException e) {
            throw new zza(e);
        } catch (InvocationTargetException e2) {
            throw new zza(e2);
        }
    }

    static String zza(Context context, zzap zzapVar) throws zza {
        if (zznH != null) {
            return zznH;
        }
        if (zznw == null) {
            throw new zza();
        }
        try {
            ByteBuffer byteBuffer = (ByteBuffer) zznw.invoke(null, context);
            if (byteBuffer == null) {
                throw new zza();
            }
            zznH = zzapVar.zza(byteBuffer.array(), true);
            return zznH;
        } catch (IllegalAccessException e) {
            throw new zza(e);
        } catch (InvocationTargetException e2) {
            throw new zza(e2);
        }
    }

    static ArrayList<Long> zza(MotionEvent motionEvent, DisplayMetrics displayMetrics) throws zza {
        if (zznx == null || motionEvent == null) {
            throw new zza();
        }
        try {
            return (ArrayList) zznx.invoke(null, motionEvent, displayMetrics);
        } catch (IllegalAccessException e) {
            throw new zza(e);
        } catch (InvocationTargetException e2) {
            throw new zza(e2);
        }
    }

    protected static void zza(int i, int i2) throws IOException {
        if (zznU && zznR && zznL != null) {
            zza.C0032zza c0032zzaZzi = zznL.zzi(zztk.toByteArray(zznM));
            c0032zzaZzi.zzbr(i2);
            c0032zzaZzi.zzbq(i);
            c0032zzaZzi.zzc(zznr);
        }
    }

    protected static synchronized void zza(String str, Context context, zzap zzapVar) {
        synchronized (zzal.class) {
            if (!zznK) {
                try {
                    zznJ = new zzar(zzapVar, null);
                    zznG = str;
                    zzbz.initialize(context);
                    zzm(context);
                    startTime = zzW().longValue();
                    zznO = new Random();
                    try {
                        zznr = new GoogleApiClient.Builder(context).addApi(com.google.android.gms.clearcut.zza.API).build();
                        zznP = GoogleApiAvailability.getInstance();
                        zznQ = zznP.isGooglePlayServicesAvailable(context) == 0;
                        zzbz.initialize(context);
                        zznR = zzbz.zzwD.get().booleanValue();
                        zznL = new com.google.android.gms.clearcut.zza(context, "ADSHIELD", null, null);
                    } catch (NoClassDefFoundError e) {
                    }
                    zznK = true;
                } catch (zza e2) {
                } catch (UnsupportedOperationException e3) {
                }
            }
        }
    }

    static String zzb(Context context, zzap zzapVar) throws zza {
        if (zznI != null) {
            return zznI;
        }
        if (zznz == null) {
            throw new zza();
        }
        try {
            ByteBuffer byteBuffer = (ByteBuffer) zznz.invoke(null, context);
            if (byteBuffer == null) {
                throw new zza();
            }
            zznI = zzapVar.zza(byteBuffer.array(), true);
            return zznI;
        } catch (IllegalAccessException e) {
            throw new zza(e);
        } catch (InvocationTargetException e2) {
            throw new zza(e2);
        }
    }

    private static String zzb(byte[] bArr, String str) throws zza {
        try {
            return new String(zznJ.zzc(bArr, str), "UTF-8");
        } catch (zzar.zza e) {
            throw new zza(e);
        } catch (UnsupportedEncodingException e2) {
            throw new zza(e2);
        }
    }

    private void zze(Context context) {
        if (!zznQ) {
            zznU = false;
        } else {
            zznr.connect();
            zznU = true;
        }
    }

    static String zzf(Context context) throws zza {
        if (zzny == null) {
            throw new zza();
        }
        try {
            String str = (String) zzny.invoke(null, context);
            if (str == null) {
                throw new zza();
            }
            return str;
        } catch (IllegalAccessException e) {
            throw new zza(e);
        } catch (InvocationTargetException e2) {
            throw new zza(e2);
        }
    }

    static String zzg(Context context) throws zza {
        if (zznC == null) {
            throw new zza();
        }
        try {
            return (String) zznC.invoke(null, context);
        } catch (IllegalAccessException e) {
            throw new zza(e);
        } catch (InvocationTargetException e2) {
            throw new zza(e2);
        }
    }

    static Long zzh(Context context) throws zza {
        if (zznD == null) {
            throw new zza();
        }
        try {
            return (Long) zznD.invoke(null, context);
        } catch (IllegalAccessException e) {
            throw new zza(e);
        } catch (InvocationTargetException e2) {
            throw new zza(e2);
        }
    }

    static ArrayList<Long> zzi(Context context) throws zza {
        if (zznA == null) {
            throw new zza();
        }
        try {
            ArrayList<Long> arrayList = (ArrayList) zznA.invoke(null, context);
            if (arrayList == null || arrayList.size() != 2) {
                throw new zza();
            }
            return arrayList;
        } catch (IllegalAccessException e) {
            throw new zza(e);
        } catch (InvocationTargetException e2) {
            throw new zza(e2);
        }
    }

    static int[] zzj(Context context) throws zza {
        if (zznB == null) {
            throw new zza();
        }
        try {
            return (int[]) zznB.invoke(null, context);
        } catch (IllegalAccessException e) {
            throw new zza(e);
        } catch (InvocationTargetException e2) {
            throw new zza(e2);
        }
    }

    static int zzk(Context context) throws zza {
        if (zznE == null) {
            throw new zza();
        }
        try {
            return ((Integer) zznE.invoke(null, context)).intValue();
        } catch (IllegalAccessException e) {
            throw new zza(e);
        } catch (InvocationTargetException e2) {
            throw new zza(e2);
        }
    }

    static int zzl(Context context) throws zza {
        if (zznF == null) {
            throw new zza();
        }
        try {
            return ((Integer) zznF.invoke(null, context)).intValue();
        } catch (IllegalAccessException e) {
            throw new zza(e);
        } catch (InvocationTargetException e2) {
            throw new zza(e2);
        }
    }

    private static void zzm(Context context) throws NoSuchPaddingException, zza, NoSuchAlgorithmException, InvalidKeyException, IOException, InvalidAlgorithmParameterException {
        try {
            byte[] bArrZzl = zznJ.zzl(zzat.getKey());
            byte[] bArrZzc = zznJ.zzc(bArrZzl, zzat.zzae());
            File cacheDir = context.getCacheDir();
            if (cacheDir == null && (cacheDir = context.getDir("dex", 0)) == null) {
                throw new zza();
            }
            File file = cacheDir;
            File fileCreateTempFile = File.createTempFile("ads", ".jar", file);
            FileOutputStream fileOutputStream = new FileOutputStream(fileCreateTempFile);
            fileOutputStream.write(bArrZzc, 0, bArrZzc.length);
            fileOutputStream.close();
            try {
                DexClassLoader dexClassLoader = new DexClassLoader(fileCreateTempFile.getAbsolutePath(), file.getAbsolutePath(), null, context.getClassLoader());
                Class clsLoadClass = dexClassLoader.loadClass(zzb(bArrZzl, zzat.zzan()));
                Class clsLoadClass2 = dexClassLoader.loadClass(zzb(bArrZzl, zzat.zzaB()));
                Class clsLoadClass3 = dexClassLoader.loadClass(zzb(bArrZzl, zzat.zzav()));
                Class clsLoadClass4 = dexClassLoader.loadClass(zzb(bArrZzl, zzat.zzar()));
                Class clsLoadClass5 = dexClassLoader.loadClass(zzb(bArrZzl, zzat.zzaD()));
                Class clsLoadClass6 = dexClassLoader.loadClass(zzb(bArrZzl, zzat.zzap()));
                Class clsLoadClass7 = dexClassLoader.loadClass(zzb(bArrZzl, zzat.zzaz()));
                Class clsLoadClass8 = dexClassLoader.loadClass(zzb(bArrZzl, zzat.zzax()));
                Class clsLoadClass9 = dexClassLoader.loadClass(zzb(bArrZzl, zzat.zzal()));
                Class clsLoadClass10 = dexClassLoader.loadClass(zzb(bArrZzl, zzat.zzaj()));
                Class clsLoadClass11 = dexClassLoader.loadClass(zzb(bArrZzl, zzat.zzah()));
                Class clsLoadClass12 = dexClassLoader.loadClass(zzb(bArrZzl, zzat.zzat()));
                Class clsLoadClass13 = dexClassLoader.loadClass(zzb(bArrZzl, zzat.zzaf()));
                zznt = clsLoadClass.getMethod(zzb(bArrZzl, zzat.zzao()), new Class[0]);
                zznu = clsLoadClass2.getMethod(zzb(bArrZzl, zzat.zzaC()), new Class[0]);
                zznv = clsLoadClass3.getMethod(zzb(bArrZzl, zzat.zzaw()), new Class[0]);
                zznw = clsLoadClass4.getMethod(zzb(bArrZzl, zzat.zzas()), Context.class);
                zznx = clsLoadClass5.getMethod(zzb(bArrZzl, zzat.zzaE()), MotionEvent.class, DisplayMetrics.class);
                zzny = clsLoadClass6.getMethod(zzb(bArrZzl, zzat.zzaq()), Context.class);
                zznz = clsLoadClass7.getMethod(zzb(bArrZzl, zzat.zzaA()), Context.class);
                zznA = clsLoadClass8.getMethod(zzb(bArrZzl, zzat.zzay()), Context.class);
                zznB = clsLoadClass9.getMethod(zzb(bArrZzl, zzat.zzam()), Context.class);
                zznC = clsLoadClass10.getMethod(zzb(bArrZzl, zzat.zzak()), Context.class);
                zznD = clsLoadClass11.getMethod(zzb(bArrZzl, zzat.zzai()), Context.class);
                zznE = clsLoadClass12.getMethod(zzb(bArrZzl, zzat.zzau()), Context.class);
                zznF = clsLoadClass13.getMethod(zzb(bArrZzl, zzat.zzag()), Context.class);
            } finally {
                String name = fileCreateTempFile.getName();
                fileCreateTempFile.delete();
                new File(file, name.replace(".jar", ".dex")).delete();
            }
        } catch (zzar.zza e) {
            throw new zza(e);
        } catch (FileNotFoundException e2) {
            throw new zza(e2);
        } catch (IOException e3) {
            throw new zza(e3);
        } catch (ClassNotFoundException e4) {
            throw new zza(e4);
        } catch (NoSuchMethodException e5) {
            throw new zza(e5);
        } catch (NullPointerException e6) {
            throw new zza(e6);
        }
    }

    @Override // com.google.android.gms.internal.zzak
    protected void zzc(Context context) {
        try {
            zze(context);
            zznN = zznO.nextInt();
            zza(0, zznN);
            try {
                zza(1, zzX());
                zza(1, zznN);
            } catch (zza e) {
            }
            try {
                zza(2, zzV());
                zza(2, zznN);
            } catch (zza e2) {
            }
            try {
                long jLongValue = zzW().longValue();
                zza(25, jLongValue);
                if (startTime != 0) {
                    zza(17, jLongValue - startTime);
                    zza(23, startTime);
                }
                zza(25, zznN);
            } catch (zza e3) {
            }
            try {
                ArrayList<Long> arrayListZzi = zzi(context);
                zza(31, arrayListZzi.get(0).longValue());
                zza(32, arrayListZzi.get(1).longValue());
                zza(31, zznN);
            } catch (zza e4) {
            }
            try {
                zza(33, zzY().longValue());
                zza(33, zznN);
            } catch (zza e5) {
            }
            try {
                if (!zznS || !zznT) {
                    zza(27, zza(context, this.zznq));
                }
                zza(27, zznN);
            } catch (zza e6) {
            }
            try {
                zza(29, zzb(context, this.zznq));
                zza(29, zznN);
            } catch (zza e7) {
            }
            try {
                int[] iArrZzj = zzj(context);
                zza(5, iArrZzj[0]);
                zza(6, iArrZzj[1]);
                zza(5, zznN);
            } catch (zza e8) {
            }
            try {
                zza(12, zzk(context));
                zza(12, zznN);
            } catch (zza e9) {
            }
            try {
                zza(3, zzl(context));
                zza(3, zznN);
            } catch (zza e10) {
            }
            try {
                zza(34, zzg(context));
                zza(34, zznN);
            } catch (zza e11) {
            }
            try {
                zza(35, zzh(context).longValue());
                zza(35, zznN);
            } catch (zza e12) {
            }
            zzU();
        } catch (IOException e13) {
        }
    }

    @Override // com.google.android.gms.internal.zzak
    protected void zzd(Context context) {
        try {
            zze(context);
            zznN = zznO.nextInt();
            try {
                zza(2, zzV());
            } catch (zza e) {
            }
            try {
                zza(1, zzX());
            } catch (zza e2) {
            }
            try {
                zza(25, zzW().longValue());
            } catch (zza e3) {
            }
            zza(0, zznN);
            try {
                ArrayList<Long> arrayListZza = zza(this.zzno, this.zznp);
                zza(14, arrayListZza.get(0).longValue());
                zza(15, arrayListZza.get(1).longValue());
                if (arrayListZza.size() >= 3) {
                    zza(16, arrayListZza.get(2).longValue());
                }
                zza(14, zznN);
            } catch (zza e4) {
            }
            try {
                zza(34, zzg(context));
            } catch (zza e5) {
            }
            try {
                zza(35, zzh(context).longValue());
            } catch (zza e6) {
            }
            zzU();
        } catch (IOException e7) {
        }
    }
}
