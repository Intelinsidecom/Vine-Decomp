package com.google.android.gms.ads.internal.overlay;

import android.content.Context;
import android.view.TextureView;

/* loaded from: classes.dex */
public abstract class zzi extends TextureView {
    public zzi(Context context) {
        super(context);
    }

    public abstract int getCurrentPosition();

    public abstract int getDuration();

    public abstract int getVideoHeight();

    public abstract int getVideoWidth();

    public abstract void pause();

    public abstract void play();

    public abstract void seekTo(int i);

    public abstract void setMimeType(String str);

    public abstract void setVideoPath(String str);

    public abstract void stop();

    public abstract void zza(float f);

    public abstract void zza(zzh zzhVar);

    public abstract String zzeO();

    public abstract void zzeU();

    public abstract void zzeV();
}
