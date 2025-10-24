package com.google.android.gms.common.images;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.os.ResultReceiver;
import android.os.SystemClock;
import android.support.v4.util.LruCache;
import android.util.Log;
import com.google.android.gms.common.images.zza;
import com.google.android.gms.internal.zzmx;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

/* loaded from: classes2.dex */
public final class ImageManager {
    private static final Object zzahR = new Object();
    private static HashSet<Uri> zzahS = new HashSet<>();
    private final Context mContext;
    private final Handler mHandler;
    private final ExecutorService zzahV;
    private final zzb zzahW;
    private final zzmx zzahX;
    private final Map<zza, ImageReceiver> zzahY;
    private final Map<Uri, ImageReceiver> zzahZ;
    private final Map<Uri, Long> zzaia;

    private final class ImageReceiver extends ResultReceiver {
        private final Uri mUri;
        private final ArrayList<zza> zzaib;
        final /* synthetic */ ImageManager zzaic;

        @Override // android.os.ResultReceiver
        public void onReceiveResult(int resultCode, Bundle resultData) {
            this.zzaic.zzahV.execute(this.zzaic.new zzc(this.mUri, (ParcelFileDescriptor) resultData.getParcelable("com.google.android.gms.extra.fileDescriptor")));
        }
    }

    public interface OnImageLoadedListener {
        void onImageLoaded(Uri uri, Drawable drawable, boolean z);
    }

    private static final class zzb extends LruCache<zza.C0033zza, Bitmap> {
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.support.v4.util.LruCache
        /* renamed from: zza, reason: merged with bridge method [inline-methods] */
        public int sizeOf(zza.C0033zza c0033zza, Bitmap bitmap) {
            return bitmap.getHeight() * bitmap.getRowBytes();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.support.v4.util.LruCache
        /* renamed from: zza, reason: merged with bridge method [inline-methods] */
        public void entryRemoved(boolean z, zza.C0033zza c0033zza, Bitmap bitmap, Bitmap bitmap2) {
            super.entryRemoved(z, c0033zza, bitmap, bitmap2);
        }
    }

    private final class zzc implements Runnable {
        private final Uri mUri;
        private final ParcelFileDescriptor zzaid;

        public zzc(Uri uri, ParcelFileDescriptor parcelFileDescriptor) {
            this.mUri = uri;
            this.zzaid = parcelFileDescriptor;
        }

        @Override // java.lang.Runnable
        public void run() throws InterruptedException, IOException {
            com.google.android.gms.common.internal.zzb.zzcy("LoadBitmapFromDiskRunnable can't be executed in the main thread");
            boolean z = false;
            Bitmap bitmapDecodeFileDescriptor = null;
            if (this.zzaid != null) {
                try {
                    bitmapDecodeFileDescriptor = BitmapFactory.decodeFileDescriptor(this.zzaid.getFileDescriptor());
                } catch (OutOfMemoryError e) {
                    Log.e("ImageManager", "OOM while loading bitmap for uri: " + this.mUri, e);
                    z = true;
                }
                try {
                    this.zzaid.close();
                } catch (IOException e2) {
                    Log.e("ImageManager", "closed failed", e2);
                }
            }
            CountDownLatch countDownLatch = new CountDownLatch(1);
            ImageManager.this.mHandler.post(ImageManager.this.new zzf(this.mUri, bitmapDecodeFileDescriptor, z, countDownLatch));
            try {
                countDownLatch.await();
            } catch (InterruptedException e3) {
                Log.w("ImageManager", "Latch interrupted while posting " + this.mUri);
            }
        }
    }

    private final class zzf implements Runnable {
        private final Bitmap mBitmap;
        private final Uri mUri;
        private boolean zzaif;
        private final CountDownLatch zzpy;

        public zzf(Uri uri, Bitmap bitmap, boolean z, CountDownLatch countDownLatch) {
            this.mUri = uri;
            this.mBitmap = bitmap;
            this.zzaif = z;
            this.zzpy = countDownLatch;
        }

        private void zza(ImageReceiver imageReceiver, boolean z) {
            ArrayList arrayList = imageReceiver.zzaib;
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                zza zzaVar = (zza) arrayList.get(i);
                if (z) {
                    zzaVar.zza(ImageManager.this.mContext, this.mBitmap, false);
                } else {
                    ImageManager.this.zzaia.put(this.mUri, Long.valueOf(SystemClock.elapsedRealtime()));
                    zzaVar.zza(ImageManager.this.mContext, ImageManager.this.zzahX, false);
                }
                if (!(zzaVar instanceof zza.zzc)) {
                    ImageManager.this.zzahY.remove(zzaVar);
                }
            }
        }

        @Override // java.lang.Runnable
        public void run() {
            com.google.android.gms.common.internal.zzb.zzcx("OnBitmapLoadedRunnable must be executed in the main thread");
            boolean z = this.mBitmap != null;
            if (ImageManager.this.zzahW != null) {
                if (this.zzaif) {
                    ImageManager.this.zzahW.evictAll();
                    System.gc();
                    this.zzaif = false;
                    ImageManager.this.mHandler.post(this);
                    return;
                }
                if (z) {
                    ImageManager.this.zzahW.put(new zza.C0033zza(this.mUri), this.mBitmap);
                }
            }
            ImageReceiver imageReceiver = (ImageReceiver) ImageManager.this.zzahZ.remove(this.mUri);
            if (imageReceiver != null) {
                zza(imageReceiver, z);
            }
            this.zzpy.countDown();
            synchronized (ImageManager.zzahR) {
                ImageManager.zzahS.remove(this.mUri);
            }
        }
    }
}
