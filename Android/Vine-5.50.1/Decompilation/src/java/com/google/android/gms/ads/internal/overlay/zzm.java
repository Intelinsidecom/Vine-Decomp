package com.google.android.gms.ads.internal.overlay;

import android.R;
import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import com.google.android.gms.internal.zzha;

@zzha
/* loaded from: classes.dex */
public class zzm extends FrameLayout implements View.OnClickListener {
    private final ImageButton zzDV;
    private final zzo zzDW;

    public zzm(Context context, int i, zzo zzoVar) {
        super(context);
        this.zzDW = zzoVar;
        setOnClickListener(this);
        this.zzDV = new ImageButton(context);
        this.zzDV.setImageResource(R.drawable.btn_dialog);
        this.zzDV.setBackgroundColor(0);
        this.zzDV.setOnClickListener(this);
        this.zzDV.setPadding(0, 0, 0, 0);
        this.zzDV.setContentDescription("Interstitial close button");
        int iZzb = com.google.android.gms.ads.internal.client.zzl.zzcN().zzb(context, i);
        addView(this.zzDV, new FrameLayout.LayoutParams(iZzb, iZzb, 17));
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (this.zzDW != null) {
            this.zzDW.zzfb();
        }
    }

    public void zza(boolean z, boolean z2) {
        if (!z2) {
            this.zzDV.setVisibility(0);
        } else if (z) {
            this.zzDV.setVisibility(4);
        } else {
            this.zzDV.setVisibility(8);
        }
    }
}
