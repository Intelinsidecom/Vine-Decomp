package com.google.android.gms.ads.internal.formats;

import android.content.Context;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.ads.internal.zzn;
import com.google.android.gms.ads.internal.zzp;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzan;
import com.google.android.gms.internal.zzbb;
import com.google.android.gms.internal.zzdl;
import com.google.android.gms.internal.zzha;
import com.google.android.gms.internal.zzjn;
import com.google.android.gms.internal.zzjo;
import java.lang.ref.WeakReference;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

@zzha
/* loaded from: classes.dex */
public class zzh {
    private final Context mContext;
    private final VersionInfoParcel zzpI;
    private zzjn zzps;
    private final zzn zzxP;
    private final JSONObject zzxS;
    private final zzbb zzxT;
    private final zza zzxU;
    private final zzan zzxV;
    private boolean zzxW;
    private String zzxX;
    private final Object zzpK = new Object();
    private WeakReference<View> zzxY = null;

    public interface zza {
        String getCustomTemplateId();

        void zzb(zzh zzhVar);

        String zzdF();

        com.google.android.gms.ads.internal.formats.zza zzdG();
    }

    public zzh(Context context, zzn zznVar, zzbb zzbbVar, zzan zzanVar, JSONObject jSONObject, zza zzaVar, VersionInfoParcel versionInfoParcel) {
        this.mContext = context;
        this.zzxP = zznVar;
        this.zzxT = zzbbVar;
        this.zzxV = zzanVar;
        this.zzxS = jSONObject;
        this.zzxU = zzaVar;
        this.zzpI = versionInfoParcel;
    }

    public Context getContext() {
        return this.mContext;
    }

    public void recordImpression() throws JSONException {
        zzx.zzcx("recordImpression must be called on the main UI thread.");
        zzn(true);
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("ad", this.zzxS);
            this.zzxT.zza("google.afma.nativeAds.handleImpressionPing", jSONObject);
        } catch (JSONException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzb("Unable to create impression JSON.", e);
        }
        this.zzxP.zza(this);
    }

    public zzb zza(View.OnClickListener onClickListener) {
        com.google.android.gms.ads.internal.formats.zza zzaVarZzdG = this.zzxU.zzdG();
        if (zzaVarZzdG == null) {
            return null;
        }
        zzb zzbVar = new zzb(this.mContext, zzaVarZzdG);
        zzbVar.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
        zzbVar.zzdB().setOnClickListener(onClickListener);
        zzbVar.zzdB().setContentDescription("Ad attribution icon");
        return zzbVar;
    }

    public void zza(View view, Map<String, WeakReference<View>> map, JSONObject jSONObject, JSONObject jSONObject2, JSONObject jSONObject3) throws JSONException {
        zzx.zzcx("performClick must be called on the main UI thread.");
        for (Map.Entry<String, WeakReference<View>> entry : map.entrySet()) {
            if (view.equals(entry.getValue().get())) {
                zza(entry.getKey(), jSONObject, jSONObject2, jSONObject3);
                return;
            }
        }
    }

    public void zza(String str, JSONObject jSONObject, JSONObject jSONObject2, JSONObject jSONObject3) throws JSONException {
        zzx.zzcx("performClick must be called on the main UI thread.");
        try {
            JSONObject jSONObject4 = new JSONObject();
            jSONObject4.put("asset", str);
            jSONObject4.put("template", this.zzxU.zzdF());
            JSONObject jSONObject5 = new JSONObject();
            jSONObject5.put("ad", this.zzxS);
            jSONObject5.put("click", jSONObject4);
            jSONObject5.put("has_custom_click_handler", this.zzxP.zzr(this.zzxU.getCustomTemplateId()) != null);
            if (jSONObject != null) {
                jSONObject5.put("view_rectangles", jSONObject);
            }
            if (jSONObject2 != null) {
                jSONObject5.put("click_point", jSONObject2);
            }
            if (jSONObject3 != null) {
                jSONObject5.put("native_view_rectangle", jSONObject3);
            }
            this.zzxT.zza("google.afma.nativeAds.handleClickGmsg", jSONObject5);
        } catch (JSONException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzb("Unable to create click JSON.", e);
        }
    }

    public void zzb(MotionEvent motionEvent) {
        this.zzxV.zza(motionEvent);
    }

    public zzjn zzdK() {
        this.zzps = zzdM();
        this.zzps.getView().setVisibility(8);
        this.zzxT.zza("/loadHtml", new zzdl() { // from class: com.google.android.gms.ads.internal.formats.zzh.1
            @Override // com.google.android.gms.internal.zzdl
            public void zza(zzjn zzjnVar, final Map<String, String> map) {
                zzh.this.zzps.zzhC().zza(new zzjo.zza() { // from class: com.google.android.gms.ads.internal.formats.zzh.1.1
                    @Override // com.google.android.gms.internal.zzjo.zza
                    public void zza(zzjn zzjnVar2, boolean z) throws JSONException {
                        zzh.this.zzxX = (String) map.get("id");
                        JSONObject jSONObject = new JSONObject();
                        try {
                            jSONObject.put("messageType", "htmlLoaded");
                            jSONObject.put("id", zzh.this.zzxX);
                            zzh.this.zzxT.zzb("sendMessageToNativeJs", jSONObject);
                        } catch (JSONException e) {
                            com.google.android.gms.ads.internal.util.client.zzb.zzb("Unable to dispatch sendMessageToNativeJsevent", e);
                        }
                    }
                });
                String str = map.get("overlayHtml");
                String str2 = map.get("baseUrl");
                if (TextUtils.isEmpty(str2)) {
                    zzh.this.zzps.loadData(str, "text/html", "UTF-8");
                } else {
                    zzh.this.zzps.loadDataWithBaseURL(str2, str, "text/html", "UTF-8", null);
                }
            }
        });
        this.zzxT.zza("/showOverlay", new zzdl() { // from class: com.google.android.gms.ads.internal.formats.zzh.2
            @Override // com.google.android.gms.internal.zzdl
            public void zza(zzjn zzjnVar, Map<String, String> map) {
                zzh.this.zzps.getView().setVisibility(0);
            }
        });
        this.zzxT.zza("/hideOverlay", new zzdl() { // from class: com.google.android.gms.ads.internal.formats.zzh.3
            @Override // com.google.android.gms.internal.zzdl
            public void zza(zzjn zzjnVar, Map<String, String> map) {
                zzh.this.zzps.getView().setVisibility(8);
            }
        });
        this.zzps.zzhC().zza("/hideOverlay", new zzdl() { // from class: com.google.android.gms.ads.internal.formats.zzh.4
            @Override // com.google.android.gms.internal.zzdl
            public void zza(zzjn zzjnVar, Map<String, String> map) {
                zzh.this.zzps.getView().setVisibility(8);
            }
        });
        this.zzps.zzhC().zza("/sendMessageToSdk", new zzdl() { // from class: com.google.android.gms.ads.internal.formats.zzh.5
            @Override // com.google.android.gms.internal.zzdl
            public void zza(zzjn zzjnVar, Map<String, String> map) throws JSONException {
                JSONObject jSONObject = new JSONObject();
                try {
                    for (String str : map.keySet()) {
                        jSONObject.put(str, map.get(str));
                    }
                    jSONObject.put("id", zzh.this.zzxX);
                    zzh.this.zzxT.zzb("sendMessageToNativeJs", jSONObject);
                } catch (JSONException e) {
                    com.google.android.gms.ads.internal.util.client.zzb.zzb("Unable to dispatch sendMessageToNativeJs event", e);
                }
            }
        });
        return this.zzps;
    }

    public View zzdL() {
        if (this.zzxY != null) {
            return this.zzxY.get();
        }
        return null;
    }

    zzjn zzdM() {
        return zzp.zzby().zza(this.mContext, AdSizeParcel.zzt(this.mContext), false, false, this.zzxV, this.zzpI);
    }

    public void zzh(View view) {
    }

    public void zzi(View view) {
        synchronized (this.zzpK) {
            if (this.zzxW) {
                return;
            }
            if (view.isShown()) {
                if (view.getGlobalVisibleRect(new Rect(), null)) {
                    recordImpression();
                }
            }
        }
    }

    public void zzj(View view) {
        this.zzxY = new WeakReference<>(view);
    }

    protected void zzn(boolean z) {
        this.zzxW = z;
    }
}
