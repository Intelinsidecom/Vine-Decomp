package com.google.android.gms.ads.internal.formats;

import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import com.google.android.gms.ads.internal.client.zzl;
import com.google.android.gms.internal.zzcp;
import com.google.android.gms.internal.zzha;
import com.google.android.gms.internal.zzip;
import com.google.android.gms.internal.zzji;
import com.google.android.gms.internal.zzjn;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

@zzha
/* loaded from: classes.dex */
public class zzj extends zzcp.zza implements View.OnClickListener, View.OnTouchListener, ViewTreeObserver.OnGlobalLayoutListener, ViewTreeObserver.OnScrollChangedListener {
    private FrameLayout zzoF;
    private zzh zzxH;
    private final FrameLayout zzyf;
    private zzb zzyh;
    int zzyj;
    int zzyk;
    private final Object zzpK = new Object();
    private Map<String, WeakReference<View>> zzyg = new HashMap();
    boolean zzyi = false;

    public zzj(FrameLayout frameLayout, FrameLayout frameLayout2) {
        this.zzyf = frameLayout;
        this.zzoF = frameLayout2;
        zzji.zza((View) this.zzyf, (ViewTreeObserver.OnGlobalLayoutListener) this);
        zzji.zza((View) this.zzyf, (ViewTreeObserver.OnScrollChangedListener) this);
        this.zzyf.setOnTouchListener(this);
    }

    @Override // com.google.android.gms.internal.zzcp
    public void destroy() {
        this.zzoF.removeAllViews();
        this.zzoF = null;
        this.zzyg = null;
        this.zzyh = null;
        this.zzxH = null;
    }

    int getMeasuredHeight() {
        return this.zzyf.getMeasuredHeight();
    }

    int getMeasuredWidth() {
        return this.zzyf.getMeasuredWidth();
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        synchronized (this.zzpK) {
            if (this.zzxH == null) {
                return;
            }
            JSONObject jSONObject = new JSONObject();
            for (Map.Entry<String, WeakReference<View>> entry : this.zzyg.entrySet()) {
                View view2 = entry.getValue().get();
                Point pointZzk = zzk(view2);
                JSONObject jSONObject2 = new JSONObject();
                try {
                    jSONObject2.put("width", zzq(view2.getWidth()));
                    jSONObject2.put("height", zzq(view2.getHeight()));
                    jSONObject2.put("x", zzq(pointZzk.x));
                    jSONObject2.put("y", zzq(pointZzk.y));
                    jSONObject.put(entry.getKey(), jSONObject2);
                } catch (JSONException e) {
                    com.google.android.gms.ads.internal.util.client.zzb.zzaH("Unable to get view rectangle for view " + entry.getKey());
                }
            }
            JSONObject jSONObject3 = new JSONObject();
            try {
                jSONObject3.put("x", zzq(this.zzyj));
                jSONObject3.put("y", zzq(this.zzyk));
            } catch (JSONException e2) {
                com.google.android.gms.ads.internal.util.client.zzb.zzaH("Unable to get click location");
            }
            JSONObject jSONObject4 = new JSONObject();
            try {
                jSONObject4.put("width", zzq(getMeasuredWidth()));
                jSONObject4.put("height", zzq(getMeasuredHeight()));
            } catch (JSONException e3) {
                com.google.android.gms.ads.internal.util.client.zzb.zzaH("Unable to get native ad view bounding box");
            }
            if (this.zzyh == null || !this.zzyh.zzdB().equals(view)) {
                this.zzxH.zza(view, this.zzyg, jSONObject, jSONObject3, jSONObject4);
            } else {
                this.zzxH.zza("1007", jSONObject, jSONObject3, jSONObject4);
            }
        }
    }

    @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
    public void onGlobalLayout() {
        synchronized (this.zzpK) {
            if (this.zzyi) {
                int measuredWidth = getMeasuredWidth();
                int measuredHeight = getMeasuredHeight();
                if (measuredWidth != 0 && measuredHeight != 0) {
                    this.zzoF.setLayoutParams(new FrameLayout.LayoutParams(measuredWidth, measuredHeight));
                    this.zzyi = false;
                }
            }
            if (this.zzxH != null) {
                this.zzxH.zzi(this.zzyf);
            }
        }
    }

    @Override // android.view.ViewTreeObserver.OnScrollChangedListener
    public void onScrollChanged() {
        synchronized (this.zzpK) {
            if (this.zzxH != null) {
                this.zzxH.zzi(this.zzyf);
            }
        }
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View view, MotionEvent motionEvent) {
        synchronized (this.zzpK) {
            if (this.zzxH != null) {
                Point pointZzc = zzc(motionEvent);
                this.zzyj = pointZzc.x;
                this.zzyk = pointZzc.y;
                MotionEvent motionEventObtain = MotionEvent.obtain(motionEvent);
                motionEventObtain.setLocation(pointZzc.x, pointZzc.y);
                this.zzxH.zzb(motionEventObtain);
                motionEventObtain.recycle();
            }
        }
        return false;
    }

    @Override // com.google.android.gms.internal.zzcp
    public com.google.android.gms.dynamic.zzd zzU(String str) {
        com.google.android.gms.dynamic.zzd zzdVarZzB;
        synchronized (this.zzpK) {
            WeakReference<View> weakReference = this.zzyg.get(str);
            zzdVarZzB = com.google.android.gms.dynamic.zze.zzB(weakReference == null ? null : weakReference.get());
        }
        return zzdVarZzB;
    }

    @Override // com.google.android.gms.internal.zzcp
    public void zza(String str, com.google.android.gms.dynamic.zzd zzdVar) {
        View view = (View) com.google.android.gms.dynamic.zze.zzp(zzdVar);
        synchronized (this.zzpK) {
            if (view == null) {
                this.zzyg.remove(str);
            } else {
                this.zzyg.put(str, new WeakReference<>(view));
                view.setOnTouchListener(this);
                view.setOnClickListener(this);
            }
        }
    }

    @Override // com.google.android.gms.internal.zzcp
    public void zzb(com.google.android.gms.dynamic.zzd zzdVar) {
        synchronized (this.zzpK) {
            this.zzyi = true;
            zzj(null);
            final zzh zzhVar = (zzh) com.google.android.gms.dynamic.zze.zzp(zzdVar);
            if ((this.zzxH instanceof zzg) && ((zzg) this.zzxH).zzdI()) {
                ((zzg) this.zzxH).zzc(zzhVar);
            } else {
                this.zzxH = zzhVar;
                if (this.zzxH instanceof zzg) {
                    ((zzg) this.zzxH).zzc(null);
                }
            }
            this.zzoF.removeAllViews();
            this.zzyh = zzg(zzhVar);
            if (this.zzyh != null) {
                this.zzyg.put("1007", new WeakReference<>(this.zzyh.zzdB()));
                this.zzoF.addView(this.zzyh);
            }
            zzip.zzKO.post(new Runnable() { // from class: com.google.android.gms.ads.internal.formats.zzj.1
                @Override // java.lang.Runnable
                public void run() {
                    zzjn zzjnVarZzdK = zzhVar.zzdK();
                    if (zzjnVarZzdK != null) {
                        zzj.this.zzoF.addView(zzjnVarZzdK.getView());
                    }
                }
            });
            zzhVar.zzh(this.zzyf);
            zzj(this.zzyf);
        }
    }

    Point zzc(MotionEvent motionEvent) {
        this.zzyf.getLocationOnScreen(new int[2]);
        return new Point((int) (motionEvent.getRawX() - r0[0]), (int) (motionEvent.getRawY() - r0[1]));
    }

    zzb zzg(zzh zzhVar) {
        return zzhVar.zza(this);
    }

    void zzj(View view) {
        if (this.zzxH != null) {
            zzh zzhVarZzdJ = this.zzxH instanceof zzg ? ((zzg) this.zzxH).zzdJ() : this.zzxH;
            if (zzhVarZzdJ != null) {
                zzhVarZzdJ.zzj(view);
            }
        }
    }

    Point zzk(View view) {
        if (this.zzyh == null || !this.zzyh.zzdB().equals(view)) {
            Point point = new Point();
            view.getGlobalVisibleRect(new Rect(), point);
            return point;
        }
        Point point2 = new Point();
        this.zzyf.getGlobalVisibleRect(new Rect(), point2);
        Point point3 = new Point();
        view.getGlobalVisibleRect(new Rect(), point3);
        return new Point(point3.x - point2.x, point3.y - point2.y);
    }

    int zzq(int i) {
        return zzl.zzcN().zzc(this.zzxH.getContext(), i);
    }
}
