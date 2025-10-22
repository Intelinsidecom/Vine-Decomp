package com.google.android.gms.internal;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import com.google.android.gms.internal.zzgq;
import com.google.android.gms.internal.zzie;

@zzha
/* loaded from: classes.dex */
public class zzgs extends zzgr {
    private Object zzFu;
    private PopupWindow zzFv;
    private boolean zzFw;

    zzgs(Context context, zzie.zza zzaVar, zzjn zzjnVar, zzgq.zza zzaVar2) {
        super(context, zzaVar, zzjnVar, zzaVar2);
        this.zzFu = new Object();
        this.zzFw = false;
    }

    private void zzfX() {
        synchronized (this.zzFu) {
            this.zzFw = true;
            if ((this.mContext instanceof Activity) && ((Activity) this.mContext).isDestroyed()) {
                this.zzFv = null;
            }
            if (this.zzFv != null) {
                if (this.zzFv.isShowing()) {
                    this.zzFv.dismiss();
                }
                this.zzFv = null;
            }
        }
    }

    @Override // com.google.android.gms.internal.zzgm, com.google.android.gms.internal.zzir
    public void cancel() {
        zzfX();
        super.cancel();
    }

    @Override // com.google.android.gms.internal.zzgm
    protected void zzC(int i) {
        zzfX();
        super.zzC(i);
    }

    @Override // com.google.android.gms.internal.zzgr
    protected void zzfW() {
        Window window = this.mContext instanceof Activity ? ((Activity) this.mContext).getWindow() : null;
        if (window == null || window.getDecorView() == null || ((Activity) this.mContext).isDestroyed()) {
            return;
        }
        FrameLayout frameLayout = new FrameLayout(this.mContext);
        frameLayout.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        frameLayout.addView(this.zzps.getView(), -1, -1);
        synchronized (this.zzFu) {
            if (this.zzFw) {
                return;
            }
            this.zzFv = new PopupWindow((View) frameLayout, 1, 1, false);
            this.zzFv.setOutsideTouchable(true);
            this.zzFv.setClippingEnabled(false);
            com.google.android.gms.ads.internal.util.client.zzb.zzaF("Displaying the 1x1 popup off the screen.");
            try {
                this.zzFv.showAtLocation(window.getDecorView(), 0, -1, -1);
            } catch (Exception e) {
                this.zzFv = null;
            }
        }
    }
}
