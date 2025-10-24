package com.google.android.gms.ads.internal.overlay;

import android.content.Context;
import android.support.v4.view.InputDeviceCompat;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.google.android.gms.internal.zzcf;
import com.google.android.gms.internal.zzch;
import com.google.android.gms.internal.zzha;
import com.google.android.gms.internal.zzjn;
import java.util.HashMap;

@zzha
/* loaded from: classes.dex */
public class zzk extends FrameLayout implements zzh {
    private final FrameLayout zzDM;
    private final zzq zzDN;
    private zzi zzDO;
    private boolean zzDP;
    private boolean zzDQ;
    private TextView zzDR;
    private long zzDS;
    private long zzDT;
    private String zzDU;
    private final zzjn zzps;
    private String zzzk;

    public zzk(Context context, zzjn zzjnVar, int i, zzch zzchVar, zzcf zzcfVar) {
        super(context);
        this.zzps = zzjnVar;
        this.zzDM = new FrameLayout(context);
        addView(this.zzDM, new FrameLayout.LayoutParams(-1, -1));
        com.google.android.gms.common.internal.zzb.zzu(zzjnVar.zzhz());
        this.zzDO = zzjnVar.zzhz().zzpn.zza(context, zzjnVar, i, zzchVar, zzcfVar);
        if (this.zzDO != null) {
            this.zzDM.addView(this.zzDO, new FrameLayout.LayoutParams(-1, -1, 17));
        }
        this.zzDR = new TextView(context);
        this.zzDR.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
        zzfv();
        this.zzDN = new zzq(this);
        this.zzDN.zzfD();
        if (this.zzDO != null) {
            this.zzDO.zza(this);
        }
        if (this.zzDO == null) {
            zzg("AdVideoUnderlay Error", "Allocating player failed.");
        }
    }

    private void zza(String str, String... strArr) {
        HashMap map = new HashMap();
        map.put("event", str);
        int length = strArr.length;
        int i = 0;
        String str2 = null;
        while (i < length) {
            String str3 = strArr[i];
            if (str2 != null) {
                map.put(str2, str3);
                str3 = null;
            }
            i++;
            str2 = str3;
        }
        this.zzps.zzb("onVideoEvent", map);
    }

    public static void zzd(zzjn zzjnVar) {
        HashMap map = new HashMap();
        map.put("event", "no_video_view");
        zzjnVar.zzb("onVideoEvent", map);
    }

    private void zzfv() {
        if (zzfx()) {
            return;
        }
        this.zzDM.addView(this.zzDR, new FrameLayout.LayoutParams(-1, -1));
        this.zzDM.bringChildToFront(this.zzDR);
    }

    private void zzfw() {
        if (zzfx()) {
            this.zzDM.removeView(this.zzDR);
        }
    }

    private boolean zzfx() {
        return this.zzDR.getParent() != null;
    }

    private void zzfy() {
        if (this.zzps.zzhx() == null || this.zzDP) {
            return;
        }
        this.zzDQ = (this.zzps.zzhx().getWindow().getAttributes().flags & 128) != 0;
        if (this.zzDQ) {
            return;
        }
        this.zzps.zzhx().getWindow().addFlags(128);
        this.zzDP = true;
    }

    private void zzfz() {
        if (this.zzps.zzhx() == null || !this.zzDP || this.zzDQ) {
            return;
        }
        this.zzps.zzhx().getWindow().clearFlags(128);
        this.zzDP = false;
    }

    public void destroy() {
        this.zzDN.cancel();
        if (this.zzDO != null) {
            this.zzDO.stop();
        }
        zzfz();
    }

    @Override // com.google.android.gms.ads.internal.overlay.zzh
    public void onPaused() {
        zza("pause", new String[0]);
        zzfz();
    }

    public void pause() {
        if (this.zzDO == null) {
            return;
        }
        this.zzDO.pause();
    }

    public void play() {
        if (this.zzDO == null) {
            return;
        }
        this.zzDO.play();
    }

    public void seekTo(int millis) {
        if (this.zzDO == null) {
            return;
        }
        this.zzDO.seekTo(millis);
    }

    public void setMimeType(String mimeType) {
        this.zzDU = mimeType;
    }

    public void zza(float f) {
        if (this.zzDO == null) {
            return;
        }
        this.zzDO.zza(f);
    }

    public void zzao(String str) {
        this.zzzk = str;
    }

    public void zzd(int i, int i2, int i3, int i4) {
        if (i3 == 0 || i4 == 0) {
            return;
        }
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(i3 + 2, i4 + 2);
        layoutParams.setMargins(i - 1, i2 - 1, 0, 0);
        this.zzDM.setLayoutParams(layoutParams);
        requestLayout();
    }

    public void zzd(MotionEvent motionEvent) {
        if (this.zzDO == null) {
            return;
        }
        this.zzDO.dispatchTouchEvent(motionEvent);
    }

    public void zzeU() {
        if (this.zzDO == null) {
            return;
        }
        this.zzDO.zzeU();
    }

    public void zzeV() {
        if (this.zzDO == null) {
            return;
        }
        this.zzDO.zzeV();
    }

    @Override // com.google.android.gms.ads.internal.overlay.zzh
    public void zzfn() {
    }

    @Override // com.google.android.gms.ads.internal.overlay.zzh
    public void zzfo() {
        if (this.zzDO != null && this.zzDT == 0) {
            zza("canplaythrough", "duration", String.valueOf(this.zzDO.getDuration() / 1000.0f), "videoWidth", String.valueOf(this.zzDO.getVideoWidth()), "videoHeight", String.valueOf(this.zzDO.getVideoHeight()));
        }
    }

    @Override // com.google.android.gms.ads.internal.overlay.zzh
    public void zzfp() {
        zzfy();
    }

    @Override // com.google.android.gms.ads.internal.overlay.zzh
    public void zzfq() {
        zza("ended", new String[0]);
        zzfz();
    }

    @Override // com.google.android.gms.ads.internal.overlay.zzh
    public void zzfr() {
        zzfv();
        this.zzDT = this.zzDS;
    }

    public void zzfs() {
        if (this.zzDO == null) {
            return;
        }
        if (TextUtils.isEmpty(this.zzzk)) {
            zza("no_src", new String[0]);
        } else {
            this.zzDO.setMimeType(this.zzDU);
            this.zzDO.setVideoPath(this.zzzk);
        }
    }

    public void zzft() {
        if (this.zzDO == null) {
            return;
        }
        TextView textView = new TextView(this.zzDO.getContext());
        textView.setText("AdMob - " + this.zzDO.zzeO());
        textView.setTextColor(-65536);
        textView.setBackgroundColor(InputDeviceCompat.SOURCE_ANY);
        this.zzDM.addView(textView, new FrameLayout.LayoutParams(-2, -2, 17));
        this.zzDM.bringChildToFront(textView);
    }

    void zzfu() {
        if (this.zzDO == null) {
            return;
        }
        long currentPosition = this.zzDO.getCurrentPosition();
        if (this.zzDS == currentPosition || currentPosition <= 0) {
            return;
        }
        zzfw();
        zza("timeupdate", "time", String.valueOf(currentPosition / 1000.0f));
        this.zzDS = currentPosition;
    }

    @Override // com.google.android.gms.ads.internal.overlay.zzh
    public void zzg(String str, String str2) {
        zza("error", "what", str, "extra", str2);
    }
}
