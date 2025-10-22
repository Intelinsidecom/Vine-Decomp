package com.google.android.gms.common.images;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import com.google.android.gms.common.images.ImageManager;
import com.google.android.gms.common.internal.zzw;
import com.google.android.gms.internal.zzmv;
import com.google.android.gms.internal.zzmx;
import java.lang.ref.WeakReference;

/* loaded from: classes2.dex */
public abstract class zza {
    final C0033zza zzaig;
    protected int zzaii;
    protected ImageManager.OnImageLoadedListener zzaik;
    protected int zzaio;

    /* renamed from: com.google.android.gms.common.images.zza$zza, reason: collision with other inner class name */
    static final class C0033zza {
        public final Uri uri;

        public C0033zza(Uri uri) {
            this.uri = uri;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof C0033zza)) {
                return false;
            }
            if (this == obj) {
                return true;
            }
            return zzw.equal(((C0033zza) obj).uri, this.uri);
        }

        public int hashCode() {
            return zzw.hashCode(this.uri);
        }
    }

    public static final class zzc extends zza {
        private WeakReference<ImageManager.OnImageLoadedListener> zzaiq;

        public boolean equals(Object obj) {
            if (!(obj instanceof zzc)) {
                return false;
            }
            if (this == obj) {
                return true;
            }
            zzc zzcVar = (zzc) obj;
            ImageManager.OnImageLoadedListener onImageLoadedListener = this.zzaiq.get();
            ImageManager.OnImageLoadedListener onImageLoadedListener2 = zzcVar.zzaiq.get();
            return onImageLoadedListener2 != null && onImageLoadedListener != null && zzw.equal(onImageLoadedListener2, onImageLoadedListener) && zzw.equal(zzcVar.zzaig, this.zzaig);
        }

        public int hashCode() {
            return zzw.hashCode(this.zzaig);
        }

        @Override // com.google.android.gms.common.images.zza
        protected void zza(Drawable drawable, boolean z, boolean z2, boolean z3) {
            ImageManager.OnImageLoadedListener onImageLoadedListener;
            if (z2 || (onImageLoadedListener = this.zzaiq.get()) == null) {
                return;
            }
            onImageLoadedListener.onImageLoaded(this.zzaig.uri, drawable, z3);
        }
    }

    private Drawable zza(Context context, zzmx zzmxVar, int i) throws Resources.NotFoundException {
        Resources resources = context.getResources();
        if (this.zzaio <= 0) {
            return resources.getDrawable(i);
        }
        zzmx.zza zzaVar = new zzmx.zza(i, this.zzaio);
        Drawable drawable = zzmxVar.get(zzaVar);
        if (drawable != null) {
            return drawable;
        }
        Drawable drawable2 = resources.getDrawable(i);
        if ((this.zzaio & 1) != 0) {
            drawable2 = zza(resources, drawable2);
        }
        zzmxVar.put(zzaVar, drawable2);
        return drawable2;
    }

    protected Drawable zza(Resources resources, Drawable drawable) {
        return zzmv.zza(resources, drawable);
    }

    void zza(Context context, Bitmap bitmap, boolean z) {
        com.google.android.gms.common.internal.zzb.zzu(bitmap);
        if ((this.zzaio & 1) != 0) {
            bitmap = zzmv.zza(bitmap);
        }
        BitmapDrawable bitmapDrawable = new BitmapDrawable(context.getResources(), bitmap);
        if (this.zzaik != null) {
            this.zzaik.onImageLoaded(this.zzaig.uri, bitmapDrawable, true);
        }
        zza(bitmapDrawable, z, false, true);
    }

    void zza(Context context, zzmx zzmxVar, boolean z) {
        Drawable drawableZza = this.zzaii != 0 ? zza(context, zzmxVar, this.zzaii) : null;
        if (this.zzaik != null) {
            this.zzaik.onImageLoaded(this.zzaig.uri, drawableZza, false);
        }
        zza(drawableZza, z, false, false);
    }

    protected abstract void zza(Drawable drawable, boolean z, boolean z2, boolean z3);
}
