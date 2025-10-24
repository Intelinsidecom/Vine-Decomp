package com.google.android.gms.internal;

import android.content.Context;
import android.net.Uri;
import android.view.MotionEvent;

/* loaded from: classes.dex */
public class zzan {
    private static final String[] zzoh = {"/aclk", "/pcs/click"};
    private String zzod = "googleads.g.doubleclick.net";
    private String zzoe = "/pagead/ads";
    private String zzof = "ad.doubleclick.net";
    private String[] zzog = {".doubleclick.net", ".googleadservices.com", ".googlesyndication.com"};
    private zzaj zzoi;

    public zzan(zzaj zzajVar) {
        this.zzoi = zzajVar;
    }

    private Uri zza(Uri uri, Context context, String str, boolean z) throws zzao {
        try {
            boolean zZza = zza(uri);
            if (zZza) {
                if (uri.toString().contains("dc_ms=")) {
                    throw new zzao("Parameter already exists: dc_ms");
                }
            } else if (uri.getQueryParameter("ms") != null) {
                throw new zzao("Query parameter already exists: ms");
            }
            String strZzb = z ? this.zzoi.zzb(context, str) : this.zzoi.zzb(context);
            return zZza ? zzb(uri, "dc_ms", strZzb) : zza(uri, "ms", strZzb);
        } catch (UnsupportedOperationException e) {
            throw new zzao("Provided Uri is not in a valid state");
        }
    }

    private Uri zza(Uri uri, String str, String str2) throws UnsupportedOperationException {
        String string = uri.toString();
        int iIndexOf = string.indexOf("&adurl");
        if (iIndexOf == -1) {
            iIndexOf = string.indexOf("?adurl");
        }
        return iIndexOf != -1 ? Uri.parse(string.substring(0, iIndexOf + 1) + str + "=" + str2 + "&" + string.substring(iIndexOf + 1)) : uri.buildUpon().appendQueryParameter(str, str2).build();
    }

    private Uri zzb(Uri uri, String str, String str2) {
        String string = uri.toString();
        int iIndexOf = string.indexOf(";adurl");
        if (iIndexOf != -1) {
            return Uri.parse(string.substring(0, iIndexOf + 1) + str + "=" + str2 + ";" + string.substring(iIndexOf + 1));
        }
        String encodedPath = uri.getEncodedPath();
        int iIndexOf2 = string.indexOf(encodedPath);
        return Uri.parse(string.substring(0, encodedPath.length() + iIndexOf2) + ";" + str + "=" + str2 + ";" + string.substring(encodedPath.length() + iIndexOf2));
    }

    public Uri zza(Uri uri, Context context) throws zzao {
        try {
            return zza(uri, context, uri.getQueryParameter("ai"), true);
        } catch (UnsupportedOperationException e) {
            throw new zzao("Provided Uri is not in a valid state");
        }
    }

    public void zza(MotionEvent motionEvent) {
        this.zzoi.zza(motionEvent);
    }

    public boolean zza(Uri uri) {
        if (uri == null) {
            throw new NullPointerException();
        }
        try {
            return uri.getHost().equals(this.zzof);
        } catch (NullPointerException e) {
            return false;
        }
    }

    public zzaj zzac() {
        return this.zzoi;
    }

    public boolean zzb(Uri uri) {
        if (uri == null) {
            throw new NullPointerException();
        }
        try {
            String host = uri.getHost();
            for (String str : this.zzog) {
                if (host.endsWith(str)) {
                    return true;
                }
            }
            return false;
        } catch (NullPointerException e) {
            return false;
        }
    }

    public boolean zzc(Uri uri) {
        if (!zzb(uri)) {
            return false;
        }
        for (String str : zzoh) {
            if (uri.getPath().endsWith(str)) {
                return true;
            }
        }
        return false;
    }
}
