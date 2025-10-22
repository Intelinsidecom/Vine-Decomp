package com.google.android.gms.measurement.internal;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.os.Bundle;
import android.text.TextUtils;
import com.google.android.gms.internal.zznl;
import com.google.android.gms.internal.zzqq;
import com.google.android.gms.internal.zztd;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/* loaded from: classes.dex */
public class zzae extends zzv {
    zzae(zzt zztVar) {
        super(zztVar);
    }

    private Object zza(int i, Object obj, boolean z) {
        if (obj == null) {
            return null;
        }
        if ((obj instanceof Long) || (obj instanceof Float)) {
            return obj;
        }
        if (obj instanceof Integer) {
            return Long.valueOf(((Integer) obj).intValue());
        }
        if (obj instanceof Byte) {
            return Long.valueOf(((Byte) obj).byteValue());
        }
        if (obj instanceof Short) {
            return Long.valueOf(((Short) obj).shortValue());
        }
        if (obj instanceof Boolean) {
            return Long.valueOf(((Boolean) obj).booleanValue() ? 1L : 0L);
        }
        if (obj instanceof Double) {
            return Float.valueOf((float) ((Double) obj).doubleValue());
        }
        if (!(obj instanceof String) && !(obj instanceof Character) && !(obj instanceof CharSequence)) {
            return null;
        }
        String strValueOf = String.valueOf(obj);
        if (strValueOf.length() <= i) {
            return strValueOf;
        }
        if (z) {
            return strValueOf.substring(0, i);
        }
        return null;
    }

    private void zza(String str, String str2, int i, Object obj) {
        if (obj == null) {
            zzzz().zzBo().zzj(str + " value can't be null. Ignoring " + str, str2);
            return;
        }
        if ((obj instanceof Long) || (obj instanceof Float) || (obj instanceof Integer) || (obj instanceof Byte) || (obj instanceof Short) || (obj instanceof Boolean) || (obj instanceof Double)) {
            return;
        }
        if ((obj instanceof String) || (obj instanceof Character) || (obj instanceof CharSequence)) {
            String strValueOf = String.valueOf(obj);
            if (strValueOf.length() > i) {
                zzzz().zzBo().zze("Ignoring " + str + ". Value is too long. name, value length", str2, Integer.valueOf(strValueOf.length()));
            }
        }
    }

    public static boolean zza(Context context, Class<? extends Service> cls) throws PackageManager.NameNotFoundException {
        try {
            ServiceInfo serviceInfo = context.getPackageManager().getServiceInfo(new ComponentName(context, cls), 4);
            if (serviceInfo != null) {
                if (serviceInfo.enabled) {
                    return true;
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
        }
        return false;
    }

    public static boolean zza(Context context, Class<? extends BroadcastReceiver> cls, boolean z) throws PackageManager.NameNotFoundException {
        try {
            ActivityInfo receiverInfo = context.getPackageManager().getReceiverInfo(new ComponentName(context, cls), 2);
            if (receiverInfo != null && receiverInfo.enabled) {
                if (z) {
                    if (receiverInfo.exported) {
                    }
                }
                return true;
            }
        } catch (PackageManager.NameNotFoundException e) {
        }
        return false;
    }

    private int zzeH(String str) {
        return "_ldl".equals(str) ? zzAX().zzAy() : zzAX().zzAx();
    }

    @Override // com.google.android.gms.measurement.internal.zzv
    public /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    @Override // com.google.android.gms.measurement.internal.zzv
    public /* bridge */ /* synthetic */ void zzAR() {
        super.zzAR();
    }

    @Override // com.google.android.gms.measurement.internal.zzv
    public /* bridge */ /* synthetic */ zzm zzAS() {
        return super.zzAS();
    }

    @Override // com.google.android.gms.measurement.internal.zzv
    public /* bridge */ /* synthetic */ zzz zzAT() {
        return super.zzAT();
    }

    @Override // com.google.android.gms.measurement.internal.zzv
    public /* bridge */ /* synthetic */ zzae zzAU() {
        return super.zzAU();
    }

    @Override // com.google.android.gms.measurement.internal.zzv
    public /* bridge */ /* synthetic */ zzs zzAV() {
        return super.zzAV();
    }

    @Override // com.google.android.gms.measurement.internal.zzv
    public /* bridge */ /* synthetic */ zzr zzAW() {
        return super.zzAW();
    }

    @Override // com.google.android.gms.measurement.internal.zzv
    public /* bridge */ /* synthetic */ zzc zzAX() {
        return super.zzAX();
    }

    public void zza(Bundle bundle, String str, Object obj) {
        if (obj instanceof Long) {
            bundle.putLong(str, ((Long) obj).longValue());
            return;
        }
        if (obj instanceof Float) {
            bundle.putFloat(str, ((Float) obj).floatValue());
        } else if (obj instanceof String) {
            bundle.putString(str, String.valueOf(obj));
        } else if (str != null) {
            zzzz().zzBo().zze("Not putting event parameter. Invalid value type. name, type", str, obj.getClass().getSimpleName());
        }
    }

    public void zza(zzqq.zzb zzbVar, Object obj) {
        com.google.android.gms.common.internal.zzx.zzy(obj);
        zzbVar.zzakS = null;
        zzbVar.zzaVo = null;
        zzbVar.zzaVi = null;
        if (obj instanceof String) {
            zzbVar.zzakS = (String) obj;
            return;
        }
        if (obj instanceof Long) {
            zzbVar.zzaVo = (Long) obj;
        } else if (obj instanceof Float) {
            zzbVar.zzaVi = (Float) obj;
        } else {
            zzzz().zzBl().zzj("Ignoring invalid (type) event param value", obj);
        }
    }

    public void zza(zzqq.zze zzeVar, Object obj) {
        com.google.android.gms.common.internal.zzx.zzy(obj);
        zzeVar.zzakS = null;
        zzeVar.zzaVo = null;
        zzeVar.zzaVi = null;
        if (obj instanceof String) {
            zzeVar.zzakS = (String) obj;
            return;
        }
        if (obj instanceof Long) {
            zzeVar.zzaVo = (Long) obj;
        } else if (obj instanceof Float) {
            zzeVar.zzaVi = (Float) obj;
        } else {
            zzzz().zzBl().zzj("Ignoring invalid (type) user attribute value", obj);
        }
    }

    public byte[] zza(zzqq.zzc zzcVar) {
        try {
            byte[] bArr = new byte[zzcVar.getSerializedSize()];
            zztd zztdVarZzD = zztd.zzD(bArr);
            zzcVar.writeTo(zztdVarZzD);
            zztdVarZzD.zzHy();
            return bArr;
        } catch (IOException e) {
            zzzz().zzBl().zzj("Data loss. Failed to serialize batch", e);
            return null;
        }
    }

    public boolean zzbh(String str) {
        zziS();
        return getContext().checkCallingOrSelfPermission(str) == 0;
    }

    void zzc(String str, int i, String str2) {
        if (str2 == null) {
            throw new IllegalArgumentException(str + " name is required and can't be null");
        }
        if (str2.length() == 0) {
            throw new IllegalArgumentException(str + " name is required and can't be empty");
        }
        char cCharAt = str2.charAt(0);
        if (!Character.isLetter(cCharAt) && cCharAt != '_') {
            throw new IllegalArgumentException(str + " name must start with a letter or _");
        }
        for (int i2 = 1; i2 < str2.length(); i2++) {
            char cCharAt2 = str2.charAt(i2);
            if (cCharAt2 != '_' && !Character.isLetterOrDigit(cCharAt2)) {
                throw new IllegalArgumentException(str + " name must consist of letters, digits or _ (underscores)");
            }
        }
        if (str2.length() > i) {
            throw new IllegalArgumentException(str + " name is too long. The maximum supported length is " + i);
        }
    }

    public boolean zzc(long j, long j2) {
        return j == 0 || j2 <= 0 || Math.abs(zziT().currentTimeMillis() - j) > j2;
    }

    public void zzeF(String str) {
        zzc("user attribute", zzAX().zzAu(), str);
    }

    public byte[] zzg(byte[] bArr) throws IOException {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            GZIPOutputStream gZIPOutputStream = new GZIPOutputStream(byteArrayOutputStream);
            gZIPOutputStream.write(bArr);
            gZIPOutputStream.close();
            byteArrayOutputStream.close();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            zzzz().zzBl().zzj("Failed to gzip content", e);
            throw e;
        }
    }

    @Override // com.google.android.gms.measurement.internal.zzv
    public /* bridge */ /* synthetic */ void zziR() {
        super.zziR();
    }

    @Override // com.google.android.gms.measurement.internal.zzv
    public /* bridge */ /* synthetic */ void zziS() {
        super.zziS();
    }

    @Override // com.google.android.gms.measurement.internal.zzv
    public /* bridge */ /* synthetic */ zznl zziT() {
        return super.zziT();
    }

    public Object zzk(String str, Object obj) {
        return zza((TextUtils.isEmpty(str) || !str.startsWith("_")) ? zzAX().zzAv() : zzAX().zzAw(), obj, false);
    }

    public void zzl(String str, Object obj) {
        if ("_ldl".equals(str)) {
            zza("user attribute referrer", str, zzeH(str), obj);
        } else {
            zza("user attribute", str, zzeH(str), obj);
        }
    }

    public Object zzm(String str, Object obj) {
        return "_ldl".equals(str) ? zza(zzeH(str), obj, true) : zza(zzeH(str), obj, false);
    }

    public byte[] zzq(byte[] bArr) throws IOException {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
            GZIPInputStream gZIPInputStream = new GZIPInputStream(byteArrayInputStream);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] bArr2 = new byte[1024];
            while (true) {
                int i = gZIPInputStream.read(bArr2);
                if (i <= 0) {
                    gZIPInputStream.close();
                    byteArrayInputStream.close();
                    return byteArrayOutputStream.toByteArray();
                }
                byteArrayOutputStream.write(bArr2, 0, i);
            }
        } catch (IOException e) {
            zzzz().zzBl().zzj("Failed to ungzip content", e);
            throw e;
        }
    }

    @Override // com.google.android.gms.measurement.internal.zzv
    public /* bridge */ /* synthetic */ zzo zzzz() {
        return super.zzzz();
    }
}
