package com.google.android.gms.internal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.CalendarContract;
import android.text.TextUtils;
import com.google.android.gms.R;
import java.util.Map;

@zzha
/* loaded from: classes.dex */
public class zzfl extends zzfr {
    private final Context mContext;
    private String zzBU;
    private long zzBV;
    private long zzBW;
    private String zzBX;
    private String zzBY;
    private final Map<String, String> zzxc;

    public zzfl(zzjn zzjnVar, Map<String, String> map) {
        super(zzjnVar, "createCalendarEvent");
        this.zzxc = map;
        this.mContext = zzjnVar.zzhx();
        zzez();
    }

    private String zzai(String str) {
        return TextUtils.isEmpty(this.zzxc.get(str)) ? "" : this.zzxc.get(str);
    }

    private long zzaj(String str) {
        String str2 = this.zzxc.get(str);
        if (str2 == null) {
            return -1L;
        }
        try {
            return Long.parseLong(str2);
        } catch (NumberFormatException e) {
            return -1L;
        }
    }

    private void zzez() {
        this.zzBU = zzai("description");
        this.zzBX = zzai("summary");
        this.zzBV = zzaj("start_ticks");
        this.zzBW = zzaj("end_ticks");
        this.zzBY = zzai("location");
    }

    Intent createIntent() {
        Intent data = new Intent("android.intent.action.EDIT").setData(CalendarContract.Events.CONTENT_URI);
        data.putExtra("title", this.zzBU);
        data.putExtra("eventLocation", this.zzBY);
        data.putExtra("description", this.zzBX);
        if (this.zzBV > -1) {
            data.putExtra("beginTime", this.zzBV);
        }
        if (this.zzBW > -1) {
            data.putExtra("endTime", this.zzBW);
        }
        data.setFlags(268435456);
        return data;
    }

    public void execute() {
        if (this.mContext == null) {
            zzal("Activity context is not available.");
            return;
        }
        if (!com.google.android.gms.ads.internal.zzp.zzbx().zzN(this.mContext).zzdi()) {
            zzal("This feature is not available on the device.");
            return;
        }
        AlertDialog.Builder builderZzM = com.google.android.gms.ads.internal.zzp.zzbx().zzM(this.mContext);
        builderZzM.setTitle(com.google.android.gms.ads.internal.zzp.zzbA().zzf(R.string.create_calendar_title, "Create calendar event"));
        builderZzM.setMessage(com.google.android.gms.ads.internal.zzp.zzbA().zzf(R.string.create_calendar_message, "Allow Ad to create a calendar event?"));
        builderZzM.setPositiveButton(com.google.android.gms.ads.internal.zzp.zzbA().zzf(R.string.accept, "Accept"), new DialogInterface.OnClickListener() { // from class: com.google.android.gms.internal.zzfl.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                com.google.android.gms.ads.internal.zzp.zzbx().zzb(zzfl.this.mContext, zzfl.this.createIntent());
            }
        });
        builderZzM.setNegativeButton(com.google.android.gms.ads.internal.zzp.zzbA().zzf(R.string.decline, "Decline"), new DialogInterface.OnClickListener() { // from class: com.google.android.gms.internal.zzfl.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                zzfl.this.zzal("Operation denied by user.");
            }
        });
        builderZzM.create().show();
    }
}
