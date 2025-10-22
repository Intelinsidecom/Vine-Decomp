package com.google.android.gms.internal;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import com.google.android.gms.common.api.GoogleApiClient;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.UUID;

/* loaded from: classes.dex */
public abstract class zzak implements zzaj {
    protected static GoogleApiClient zznr;
    private static zzaq zzns;
    protected MotionEvent zzno;
    protected DisplayMetrics zznp;
    protected zzap zznq;

    protected zzak(Context context, zzap zzapVar, zzaq zzaqVar) {
        this.zznq = zzapVar;
        zzns = zzaqVar;
        try {
            this.zznp = context.getResources().getDisplayMetrics();
        } catch (UnsupportedOperationException e) {
            this.zznp = new DisplayMetrics();
            this.zznp.density = 1.0f;
        }
    }

    private void zzS() {
        zzns.reset();
    }

    private byte[] zzT() throws IOException {
        return zzns.zzad();
    }

    private String zza(Context context, String str, boolean z) {
        byte[] bArrZzT;
        try {
            synchronized (this) {
                zzS();
                if (z) {
                    zzd(context);
                } else {
                    zzc(context);
                }
                bArrZzT = zzT();
            }
            return bArrZzT.length == 0 ? Integer.toString(5) : zza(bArrZzT, str);
        } catch (UnsupportedEncodingException e) {
            return Integer.toString(7);
        } catch (IOException e2) {
            return Integer.toString(3);
        } catch (NoSuchAlgorithmException e3) {
            return Integer.toString(7);
        }
    }

    String zza(byte[] bArr, String str) throws NoSuchAlgorithmException, IOException {
        byte[] bArrArray;
        if (bArr.length > 239) {
            zzS();
            zza(20, 1L);
            bArr = zzT();
        }
        if (bArr.length < 239) {
            byte[] bArr2 = new byte[239 - bArr.length];
            new SecureRandom().nextBytes(bArr2);
            bArrArray = ByteBuffer.allocate(240).put((byte) bArr.length).put(bArr).put(bArr2).array();
        } else {
            bArrArray = ByteBuffer.allocate(240).put((byte) bArr.length).put(bArr).array();
        }
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(bArrArray);
        byte[] bArrArray2 = ByteBuffer.allocate(256).put(messageDigest.digest()).put(bArrArray).array();
        byte[] bArr3 = new byte[256];
        new zzai().zzb(bArrArray2, bArr3);
        if (str != null && str.length() > 0) {
            zza(str, bArr3);
        }
        return this.zznq.zza(bArr3, true);
    }

    @Override // com.google.android.gms.internal.zzaj
    public void zza(int i, int i2, int i3) {
        if (this.zzno != null) {
            this.zzno.recycle();
        }
        this.zzno = MotionEvent.obtain(0L, i3, 1, i * this.zznp.density, i2 * this.zznp.density, 0.0f, 0.0f, 0, 0.0f, 0.0f, 0, 0);
    }

    protected void zza(int i, long j) throws IOException {
        zzns.zzb(i, j);
    }

    protected void zza(int i, String str) throws IOException {
        zzns.zzb(i, str);
    }

    @Override // com.google.android.gms.internal.zzaj
    public void zza(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 1) {
            if (this.zzno != null) {
                this.zzno.recycle();
            }
            this.zzno = MotionEvent.obtain(motionEvent);
        }
    }

    void zza(String str, byte[] bArr) throws UnsupportedEncodingException {
        if (str.length() > 32) {
            str = str.substring(0, 32);
        }
        new zztb(str.getBytes("UTF-8")).zzB(bArr);
    }

    @Override // com.google.android.gms.internal.zzaj
    public String zzb(Context context) {
        return zza(context, (String) null, false);
    }

    @Override // com.google.android.gms.internal.zzaj
    public String zzb(Context context, String str) {
        return zza(context, str, true);
    }

    protected abstract void zzc(Context context);

    protected abstract void zzd(Context context);

    protected String zzk(String str) {
        if (str == null || !str.matches("^[a-fA-F0-9]{8}-([a-fA-F0-9]{4}-){3}[a-fA-F0-9]{12}$")) {
            return str;
        }
        UUID uuidFromString = UUID.fromString(str);
        byte[] bArr = new byte[16];
        ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr);
        byteBufferWrap.putLong(uuidFromString.getMostSignificantBits());
        byteBufferWrap.putLong(uuidFromString.getLeastSignificantBits());
        return this.zznq.zza(bArr, true);
    }
}
