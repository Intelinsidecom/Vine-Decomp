package com.google.android.gms.ads.internal.request;

import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import com.google.android.gms.ads.internal.zzp;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.zzha;
import com.google.android.gms.internal.zznt;
import java.io.DataInputStream;
import java.io.IOException;

@zzha
/* loaded from: classes.dex */
public final class LargeParcelTeleporter implements SafeParcelable {
    public static final Parcelable.Creator<LargeParcelTeleporter> CREATOR = new zzl();
    final int mVersionCode;
    ParcelFileDescriptor zzHf;
    private Parcelable zzHg;
    private boolean zzHh;

    LargeParcelTeleporter(int versionCode, ParcelFileDescriptor parcelFileDescriptor) {
        this.mVersionCode = versionCode;
        this.zzHf = parcelFileDescriptor;
        this.zzHg = null;
        this.zzHh = true;
    }

    public LargeParcelTeleporter(SafeParcelable teleportee) {
        this.mVersionCode = 1;
        this.zzHf = null;
        this.zzHg = teleportee;
        this.zzHh = false;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        if (this.zzHf == null) {
            Parcel parcelObtain = Parcel.obtain();
            try {
                this.zzHg.writeToParcel(parcelObtain, 0);
                byte[] bArrMarshall = parcelObtain.marshall();
                parcelObtain.recycle();
                this.zzHf = zzf(bArrMarshall);
            } catch (Throwable th) {
                parcelObtain.recycle();
                throw th;
            }
        }
        zzl.zza(this, dest, flags);
    }

    public <T extends SafeParcelable> T zza(Parcelable.Creator<T> creator) throws IOException {
        if (this.zzHh) {
            if (this.zzHf == null) {
                com.google.android.gms.ads.internal.util.client.zzb.e("File descriptor is empty, returning null.");
                return null;
            }
            DataInputStream dataInputStream = new DataInputStream(new ParcelFileDescriptor.AutoCloseInputStream(this.zzHf));
            try {
                try {
                    byte[] bArr = new byte[dataInputStream.readInt()];
                    dataInputStream.readFully(bArr, 0, bArr.length);
                    zznt.zzb(dataInputStream);
                    Parcel parcelObtain = Parcel.obtain();
                    try {
                        parcelObtain.unmarshall(bArr, 0, bArr.length);
                        parcelObtain.setDataPosition(0);
                        this.zzHg = creator.createFromParcel(parcelObtain);
                        parcelObtain.recycle();
                        this.zzHh = false;
                    } catch (Throwable th) {
                        parcelObtain.recycle();
                        throw th;
                    }
                } catch (IOException e) {
                    throw new IllegalStateException("Could not read from parcel file descriptor", e);
                }
            } catch (Throwable th2) {
                zznt.zzb(dataInputStream);
                throw th2;
            }
        }
        return (T) this.zzHg;
    }

    protected <T> ParcelFileDescriptor zzf(final byte[] bArr) throws IOException {
        final ParcelFileDescriptor.AutoCloseOutputStream autoCloseOutputStream;
        ParcelFileDescriptor[] parcelFileDescriptorArrCreatePipe;
        try {
            parcelFileDescriptorArrCreatePipe = ParcelFileDescriptor.createPipe();
            autoCloseOutputStream = new ParcelFileDescriptor.AutoCloseOutputStream(parcelFileDescriptorArrCreatePipe[1]);
        } catch (IOException e) {
            e = e;
            autoCloseOutputStream = null;
        }
        try {
            new Thread(new Runnable() { // from class: com.google.android.gms.ads.internal.request.LargeParcelTeleporter.1
                /* JADX WARN: Removed duplicated region for block: B:18:0x003e  */
                /* JADX WARN: Removed duplicated region for block: B:20:0x0044  */
                @Override // java.lang.Runnable
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                    To view partially-correct code enable 'Show inconsistent code' option in preferences
                */
                public void run() throws java.lang.Throwable {
                    /*
                        r4 = this;
                        r2 = 0
                        java.io.DataOutputStream r1 = new java.io.DataOutputStream     // Catch: java.io.IOException -> L1f java.lang.Throwable -> L3a
                        java.io.OutputStream r0 = r2     // Catch: java.io.IOException -> L1f java.lang.Throwable -> L3a
                        r1.<init>(r0)     // Catch: java.io.IOException -> L1f java.lang.Throwable -> L3a
                        byte[] r0 = r3     // Catch: java.lang.Throwable -> L48 java.io.IOException -> L4a
                        int r0 = r0.length     // Catch: java.lang.Throwable -> L48 java.io.IOException -> L4a
                        r1.writeInt(r0)     // Catch: java.lang.Throwable -> L48 java.io.IOException -> L4a
                        byte[] r0 = r3     // Catch: java.lang.Throwable -> L48 java.io.IOException -> L4a
                        r1.write(r0)     // Catch: java.lang.Throwable -> L48 java.io.IOException -> L4a
                        if (r1 != 0) goto L1b
                        java.io.OutputStream r0 = r2
                        com.google.android.gms.internal.zznt.zzb(r0)
                    L1a:
                        return
                    L1b:
                        com.google.android.gms.internal.zznt.zzb(r1)
                        goto L1a
                    L1f:
                        r0 = move-exception
                        r1 = r2
                    L21:
                        java.lang.String r2 = "Error transporting the ad response"
                        com.google.android.gms.ads.internal.util.client.zzb.zzb(r2, r0)     // Catch: java.lang.Throwable -> L48
                        com.google.android.gms.internal.zzig r2 = com.google.android.gms.ads.internal.zzp.zzbA()     // Catch: java.lang.Throwable -> L48
                        r3 = 1
                        r2.zzb(r0, r3)     // Catch: java.lang.Throwable -> L48
                        if (r1 != 0) goto L36
                        java.io.OutputStream r0 = r2
                        com.google.android.gms.internal.zznt.zzb(r0)
                        goto L1a
                    L36:
                        com.google.android.gms.internal.zznt.zzb(r1)
                        goto L1a
                    L3a:
                        r0 = move-exception
                        r1 = r2
                    L3c:
                        if (r1 != 0) goto L44
                        java.io.OutputStream r1 = r2
                        com.google.android.gms.internal.zznt.zzb(r1)
                    L43:
                        throw r0
                    L44:
                        com.google.android.gms.internal.zznt.zzb(r1)
                        goto L43
                    L48:
                        r0 = move-exception
                        goto L3c
                    L4a:
                        r0 = move-exception
                        goto L21
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.ads.internal.request.LargeParcelTeleporter.AnonymousClass1.run():void");
                }
            }).start();
            return parcelFileDescriptorArrCreatePipe[0];
        } catch (IOException e2) {
            e = e2;
            com.google.android.gms.ads.internal.util.client.zzb.zzb("Error transporting the ad response", e);
            zzp.zzbA().zzb((Throwable) e, true);
            zznt.zzb(autoCloseOutputStream);
            return null;
        }
    }
}
