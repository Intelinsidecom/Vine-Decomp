package com.google.android.gms.measurement.internal;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.internal.zznl;
import java.io.ByteArrayInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import javax.security.auth.x500.X500Principal;

/* loaded from: classes.dex */
public class zzm extends zzw {
    private static final X500Principal zzaTf = new X500Principal("CN=Android Debug,O=Android,C=US");
    private String zzRk;
    private String zzRl;
    private String zzaRd;
    private String zzaSf;
    private String zzaSj;
    private long zzaTg;

    zzm(zzt zztVar) {
        super(zztVar);
    }

    static long zzp(byte[] bArr) {
        int i = 0;
        com.google.android.gms.common.internal.zzx.zzy(bArr);
        com.google.android.gms.common.internal.zzx.zzaa(bArr.length > 0);
        long j = 0;
        for (int length = bArr.length - 1; length >= 0 && length >= bArr.length - 8; length--) {
            j += (bArr[length] & 255) << i;
            i += 8;
        }
        return j;
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

    String zzBi() {
        zzje();
        if (zzAX().zzka()) {
            return "";
        }
        if (this.zzaSf == null) {
            Status statusZzaS = com.google.android.gms.measurement.zza.zzaS(getContext());
            if (statusZzaS == null || !statusZzaS.isSuccess()) {
                this.zzaSf = "";
                zzzz().zzBl().zzj("getGoogleAppId failed with status", Integer.valueOf(statusZzaS == null ? 0 : statusZzaS.getStatusCode()));
                if (statusZzaS != null && statusZzaS.getStatusMessage() != null) {
                    zzzz().zzBq().zzez(statusZzaS.getStatusMessage());
                }
            } else {
                try {
                    if (com.google.android.gms.measurement.zza.zzzC()) {
                        String strZzzA = com.google.android.gms.measurement.zza.zzzA();
                        if (TextUtils.isEmpty(strZzzA)) {
                            strZzzA = "";
                        }
                        this.zzaSf = strZzzA;
                    } else {
                        this.zzaSf = "";
                    }
                } catch (IllegalStateException e) {
                    this.zzaSf = "";
                    zzzz().zzBl().zzj("getGoogleAppId or isMeasurementEnabled failed with exception", e);
                }
            }
        }
        return this.zzaSf;
    }

    long zzBj() {
        zzje();
        return this.zzaTg;
    }

    boolean zzBk() throws PackageManager.NameNotFoundException {
        try {
            PackageInfo packageInfo = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 64);
            if (packageInfo != null && packageInfo.signatures != null && packageInfo.signatures.length > 0) {
                return ((X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(packageInfo.signatures[0].toByteArray()))).getSubjectX500Principal().equals(zzaTf);
            }
        } catch (PackageManager.NameNotFoundException e) {
            zzzz().zzBl().zzj("Package name not found", e);
        } catch (CertificateException e2) {
            zzzz().zzBl().zzj("Error obtaining certificate", e2);
        }
        return true;
    }

    AppMetadata zzex(String str) {
        return new AppMetadata(this.zzaRd, zzBi(), this.zzRl, this.zzaSj, zzAX().zzAE(), zzBj(), str, zzAW().zzzC());
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

    @Override // com.google.android.gms.measurement.internal.zzw
    protected void zzir() throws PackageManager.NameNotFoundException, NoSuchAlgorithmException {
        String string;
        String str = "Unknown";
        string = "Unknown";
        PackageManager packageManager = getContext().getPackageManager();
        String packageName = getContext().getPackageName();
        String installerPackageName = packageManager.getInstallerPackageName(packageName);
        if (installerPackageName == null) {
            installerPackageName = "manual_install";
        } else if (GooglePlayServicesUtil.GOOGLE_PLAY_STORE_PACKAGE.equals(installerPackageName)) {
            installerPackageName = "";
        }
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getContext().getPackageName(), 0);
            if (packageInfo != null) {
                CharSequence applicationLabel = packageManager.getApplicationLabel(packageInfo.applicationInfo);
                string = TextUtils.isEmpty(applicationLabel) ? "Unknown" : applicationLabel.toString();
                str = packageInfo.versionName;
            }
        } catch (PackageManager.NameNotFoundException e) {
            zzzz().zzBl().zzj("Error retrieving package info: appName", string);
        }
        this.zzaRd = packageName;
        this.zzaSj = installerPackageName;
        this.zzRl = str;
        this.zzRk = string;
        long jZzp = 0;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            if (!zzBk()) {
                PackageInfo packageInfo2 = packageManager.getPackageInfo(getContext().getPackageName(), 64);
                if (messageDigest != null && packageInfo2.signatures != null && packageInfo2.signatures.length > 0) {
                    jZzp = zzp(messageDigest.digest(packageInfo2.signatures[0].toByteArray()));
                }
            }
        } catch (PackageManager.NameNotFoundException e2) {
            zzzz().zzBl().zzj("Package name not found", e2);
        } catch (NoSuchAlgorithmException e3) {
            zzzz().zzBl().zzj("Could not get MD5 instance", e3);
        }
        this.zzaTg = jZzp;
    }

    @Override // com.google.android.gms.measurement.internal.zzv
    public /* bridge */ /* synthetic */ zzo zzzz() {
        return super.zzzz();
    }
}
