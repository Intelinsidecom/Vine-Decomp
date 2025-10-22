package com.google.android.gms.internal;

import android.os.Parcel;
import android.util.Base64;
import com.google.android.gms.ads.internal.client.AdRequestParcel;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@zzha
/* loaded from: classes.dex */
class zzeh {
    final String zzpH;
    final AdRequestParcel zzqo;

    zzeh(AdRequestParcel adRequestParcel, String str) {
        this.zzqo = adRequestParcel;
        this.zzpH = str;
    }

    zzeh(String str) throws IOException {
        String[] strArrSplit = str.split("\u0000");
        if (strArrSplit.length != 2) {
            throw new IOException("Incorrect field count for QueueSeed.");
        }
        Parcel parcelObtain = Parcel.obtain();
        try {
            try {
                this.zzpH = new String(Base64.decode(strArrSplit[0], 0), "UTF-8");
                byte[] bArrDecode = Base64.decode(strArrSplit[1], 0);
                parcelObtain.unmarshall(bArrDecode, 0, bArrDecode.length);
                parcelObtain.setDataPosition(0);
                this.zzqo = AdRequestParcel.CREATOR.createFromParcel(parcelObtain);
            } catch (IllegalArgumentException e) {
                throw new IOException("Malformed QueueSeed encoding.");
            }
        } finally {
            parcelObtain.recycle();
        }
    }

    String zzef() {
        String str;
        Parcel parcelObtain = Parcel.obtain();
        try {
            try {
                String strEncodeToString = Base64.encodeToString(this.zzpH.getBytes("UTF-8"), 0);
                this.zzqo.writeToParcel(parcelObtain, 0);
                str = strEncodeToString + "\u0000" + Base64.encodeToString(parcelObtain.marshall(), 0);
            } catch (UnsupportedEncodingException e) {
                com.google.android.gms.ads.internal.util.client.zzb.e("QueueSeed encode failed because UTF-8 is not available.");
                parcelObtain.recycle();
                str = "";
            }
            return str;
        } finally {
            parcelObtain.recycle();
        }
    }
}
