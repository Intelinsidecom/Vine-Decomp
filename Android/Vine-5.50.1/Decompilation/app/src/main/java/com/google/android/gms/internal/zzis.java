package com.google.android.gms.internal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.MotionEvent;
import java.util.Map;

@zzha
/* loaded from: classes.dex */
public class zzis {
    private final Context mContext;
    private int mState;
    private final float zzCB;
    private String zzKT;
    private float zzKU;
    private float zzKV;
    private float zzKW;

    public zzis(Context context) {
        this.mState = 0;
        this.mContext = context;
        this.zzCB = context.getResources().getDisplayMetrics().density;
    }

    public zzis(Context context, String str) {
        this(context);
        this.zzKT = str;
    }

    private void showDialog() {
        if (!(this.mContext instanceof Activity)) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaG("Can not create dialog without Activity Context");
            return;
        }
        final String strZzaD = zzaD(this.zzKT);
        AlertDialog.Builder builder = new AlertDialog.Builder(this.mContext);
        builder.setMessage(strZzaD);
        builder.setTitle("Ad Information");
        builder.setPositiveButton("Share", new DialogInterface.OnClickListener() { // from class: com.google.android.gms.internal.zzis.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                com.google.android.gms.ads.internal.zzp.zzbx().zzb(zzis.this.mContext, Intent.createChooser(new Intent("android.intent.action.SEND").setType("text/plain").putExtra("android.intent.extra.TEXT", strZzaD), "Share via"));
            }
        });
        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() { // from class: com.google.android.gms.internal.zzis.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create().show();
    }

    static String zzaD(String str) {
        if (TextUtils.isEmpty(str)) {
            return "No debug information";
        }
        Uri uriBuild = new Uri.Builder().encodedQuery(str.replaceAll("\\+", "%20")).build();
        StringBuilder sb = new StringBuilder();
        Map<String, String> mapZze = com.google.android.gms.ads.internal.zzp.zzbx().zze(uriBuild);
        for (String str2 : mapZze.keySet()) {
            sb.append(str2).append(" = ").append(mapZze.get(str2)).append("\n\n");
        }
        String strTrim = sb.toString().trim();
        return TextUtils.isEmpty(strTrim) ? "No debug information" : strTrim;
    }

    void zza(int i, float f, float f2) {
        if (i == 0) {
            this.mState = 0;
            this.zzKU = f;
            this.zzKV = f2;
            this.zzKW = f2;
            return;
        }
        if (this.mState != -1) {
            if (i != 2) {
                if (i == 1 && this.mState == 4) {
                    showDialog();
                    return;
                }
                return;
            }
            if (f2 > this.zzKV) {
                this.zzKV = f2;
            } else if (f2 < this.zzKW) {
                this.zzKW = f2;
            }
            if (this.zzKV - this.zzKW > 30.0f * this.zzCB) {
                this.mState = -1;
                return;
            }
            if (this.mState == 0 || this.mState == 2) {
                if (f - this.zzKU >= 50.0f * this.zzCB) {
                    this.zzKU = f;
                    this.mState++;
                }
            } else if ((this.mState == 1 || this.mState == 3) && f - this.zzKU <= (-50.0f) * this.zzCB) {
                this.zzKU = f;
                this.mState++;
            }
            if (this.mState == 1 || this.mState == 3) {
                if (f > this.zzKU) {
                    this.zzKU = f;
                }
            } else {
                if (this.mState != 2 || f >= this.zzKU) {
                    return;
                }
                this.zzKU = f;
            }
        }
    }

    public void zzaC(String str) {
        this.zzKT = str;
    }

    public void zze(MotionEvent motionEvent) {
        int historySize = motionEvent.getHistorySize();
        for (int i = 0; i < historySize; i++) {
            zza(motionEvent.getActionMasked(), motionEvent.getHistoricalX(0, i), motionEvent.getHistoricalY(0, i));
        }
        zza(motionEvent.getActionMasked(), motionEvent.getX(), motionEvent.getY());
    }
}
