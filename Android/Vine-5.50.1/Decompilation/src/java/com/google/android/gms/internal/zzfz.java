package com.google.android.gms.internal;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import org.json.JSONObject;

@zzha
/* loaded from: classes.dex */
public class zzfz extends Handler {
    private final zzfy zzEp;

    public zzfz(Context context) {
        this(new zzga(context.getApplicationContext() != null ? context.getApplicationContext() : context));
    }

    public zzfz(zzfy zzfyVar) {
        this.zzEp = zzfyVar;
    }

    private void zzc(JSONObject jSONObject) {
        try {
            this.zzEp.zza(jSONObject.getString("request_id"), jSONObject.getString("base_url"), jSONObject.getString("html"));
        } catch (Exception e) {
        }
    }

    @Override // android.os.Handler
    public void handleMessage(Message msg) {
        try {
            Bundle data = msg.getData();
            if (data == null) {
                return;
            }
            JSONObject jSONObject = new JSONObject(data.getString("data"));
            if ("fetch_html".equals(jSONObject.getString("message_name"))) {
                zzc(jSONObject);
            }
        } catch (Exception e) {
        }
    }
}
