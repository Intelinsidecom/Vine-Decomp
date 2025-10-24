package com.google.android.gms.ads.internal.formats;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.android.gms.ads.internal.client.zzl;
import com.google.android.gms.ads.internal.zzp;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzha;
import java.util.Iterator;
import java.util.List;

@zzha
/* loaded from: classes.dex */
class zzb extends RelativeLayout {
    private static final float[] zzxt = {5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f};
    private final RelativeLayout zzxu;
    private AnimationDrawable zzxv;

    public zzb(Context context, zza zzaVar) {
        super(context);
        zzx.zzy(zzaVar);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
        layoutParams.addRule(10);
        layoutParams.addRule(11);
        ShapeDrawable shapeDrawable = new ShapeDrawable(new RoundRectShape(zzxt, null, null));
        shapeDrawable.getPaint().setColor(zzaVar.getBackgroundColor());
        this.zzxu = new RelativeLayout(context);
        this.zzxu.setLayoutParams(layoutParams);
        zzp.zzbz().zza(this.zzxu, shapeDrawable);
        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(-2, -2);
        if (!TextUtils.isEmpty(zzaVar.getText())) {
            RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(-2, -2);
            TextView textView = new TextView(context);
            textView.setLayoutParams(layoutParams3);
            textView.setId(1195835393);
            textView.setTypeface(Typeface.DEFAULT);
            textView.setText(zzaVar.getText());
            textView.setTextColor(zzaVar.getTextColor());
            textView.setTextSize(zzaVar.getTextSize());
            textView.setPadding(zzl.zzcN().zzb(context, 4), 0, zzl.zzcN().zzb(context, 4), 0);
            this.zzxu.addView(textView);
            layoutParams2.addRule(1, textView.getId());
        }
        ImageView imageView = new ImageView(context);
        imageView.setLayoutParams(layoutParams2);
        imageView.setId(1195835394);
        List<Drawable> listZzdz = zzaVar.zzdz();
        if (listZzdz.size() > 1) {
            this.zzxv = new AnimationDrawable();
            Iterator<Drawable> it = listZzdz.iterator();
            while (it.hasNext()) {
                this.zzxv.addFrame(it.next(), zzaVar.zzdA());
            }
            zzp.zzbz().zza(imageView, this.zzxv);
        } else if (listZzdz.size() == 1) {
            imageView.setImageDrawable(listZzdz.get(0));
        }
        this.zzxu.addView(imageView);
        addView(this.zzxu);
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        if (this.zzxv != null) {
            this.zzxv.start();
        }
        super.onAttachedToWindow();
    }

    public ViewGroup zzdB() {
        return this.zzxu;
    }
}
