package com.google.android.gms.ads.internal.overlay;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import com.google.android.gms.internal.zzha;
import com.google.android.gms.internal.zzip;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@zzha
/* loaded from: classes.dex */
public class zzc extends zzi implements AudioManager.OnAudioFocusChangeListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnInfoListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnVideoSizeChangedListener, TextureView.SurfaceTextureListener {
    private static final Map<Integer, String> zzCN = new HashMap();
    private final zzp zzCO;
    private int zzCP;
    private int zzCQ;
    private MediaPlayer zzCR;
    private Uri zzCS;
    private int zzCT;
    private int zzCU;
    private int zzCV;
    private int zzCW;
    private int zzCX;
    private float zzCY;
    private boolean zzCZ;
    private boolean zzDa;
    private int zzDb;
    private zzh zzDc;

    static {
        zzCN.put(-1004, "MEDIA_ERROR_IO");
        zzCN.put(-1007, "MEDIA_ERROR_MALFORMED");
        zzCN.put(-1010, "MEDIA_ERROR_UNSUPPORTED");
        zzCN.put(-110, "MEDIA_ERROR_TIMED_OUT");
        zzCN.put(100, "MEDIA_ERROR_SERVER_DIED");
        zzCN.put(1, "MEDIA_ERROR_UNKNOWN");
        zzCN.put(1, "MEDIA_INFO_UNKNOWN");
        zzCN.put(700, "MEDIA_INFO_VIDEO_TRACK_LAGGING");
        zzCN.put(3, "MEDIA_INFO_VIDEO_RENDERING_START");
        zzCN.put(701, "MEDIA_INFO_BUFFERING_START");
        zzCN.put(702, "MEDIA_INFO_BUFFERING_END");
        zzCN.put(800, "MEDIA_INFO_BAD_INTERLEAVING");
        zzCN.put(801, "MEDIA_INFO_NOT_SEEKABLE");
        zzCN.put(802, "MEDIA_INFO_METADATA_UPDATE");
        zzCN.put(901, "MEDIA_INFO_UNSUPPORTED_SUBTITLE");
        zzCN.put(902, "MEDIA_INFO_SUBTITLE_TIMED_OUT");
    }

    public zzc(Context context, zzp zzpVar) {
        super(context);
        this.zzCP = 0;
        this.zzCQ = 0;
        this.zzCY = 1.0f;
        setSurfaceTextureListener(this);
        this.zzCO = zzpVar;
        this.zzCO.zza((zzi) this);
    }

    private void zzb(float f) {
        if (this.zzCR == null) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("AdMediaPlayerView setMediaPlayerVolume() called before onPrepared().");
        } else {
            try {
                this.zzCR.setVolume(f, f);
            } catch (IllegalStateException e) {
            }
        }
    }

    private void zzeP() throws IllegalStateException, IOException, SecurityException, IllegalArgumentException {
        com.google.android.gms.ads.internal.util.client.zzb.v("AdMediaPlayerView init MediaPlayer");
        SurfaceTexture surfaceTexture = getSurfaceTexture();
        if (this.zzCS == null || surfaceTexture == null) {
            return;
        }
        zzv(false);
        try {
            this.zzCR = new MediaPlayer();
            this.zzCR.setOnBufferingUpdateListener(this);
            this.zzCR.setOnCompletionListener(this);
            this.zzCR.setOnErrorListener(this);
            this.zzCR.setOnInfoListener(this);
            this.zzCR.setOnPreparedListener(this);
            this.zzCR.setOnVideoSizeChangedListener(this);
            this.zzCV = 0;
            this.zzCR.setDataSource(getContext(), this.zzCS);
            this.zzCR.setSurface(new Surface(surfaceTexture));
            this.zzCR.setAudioStreamType(3);
            this.zzCR.setScreenOnWhilePlaying(true);
            this.zzCR.prepareAsync();
            zzw(1);
        } catch (IOException | IllegalArgumentException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Failed to initialize MediaPlayer at " + this.zzCS, e);
            onError(this.zzCR, 1, 0);
        }
    }

    private void zzeQ() throws IllegalStateException {
        if (!zzeT() || this.zzCR.getCurrentPosition() <= 0 || this.zzCQ == 3) {
            return;
        }
        com.google.android.gms.ads.internal.util.client.zzb.v("AdMediaPlayerView nudging MediaPlayer");
        zzb(0.0f);
        this.zzCR.start();
        int currentPosition = this.zzCR.getCurrentPosition();
        long jCurrentTimeMillis = com.google.android.gms.ads.internal.zzp.zzbB().currentTimeMillis();
        while (zzeT() && this.zzCR.getCurrentPosition() == currentPosition && com.google.android.gms.ads.internal.zzp.zzbB().currentTimeMillis() - jCurrentTimeMillis <= 250) {
        }
        this.zzCR.pause();
        zzeY();
    }

    private void zzeR() {
        AudioManager audioManagerZzeZ = zzeZ();
        if (audioManagerZzeZ == null || this.zzDa) {
            return;
        }
        if (audioManagerZzeZ.requestAudioFocus(this, 3, 2) == 1) {
            zzeW();
        } else {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("AdMediaPlayerView audio focus request failed");
        }
    }

    private void zzeS() {
        com.google.android.gms.ads.internal.util.client.zzb.v("AdMediaPlayerView abandon audio focus");
        AudioManager audioManagerZzeZ = zzeZ();
        if (audioManagerZzeZ == null || !this.zzDa) {
            return;
        }
        if (audioManagerZzeZ.abandonAudioFocus(this) == 1) {
            this.zzDa = false;
        } else {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("AdMediaPlayerView abandon audio focus failed");
        }
    }

    private boolean zzeT() {
        return (this.zzCR == null || this.zzCP == -1 || this.zzCP == 0 || this.zzCP == 1) ? false : true;
    }

    private void zzeW() {
        com.google.android.gms.ads.internal.util.client.zzb.v("AdMediaPlayerView audio focus gained");
        this.zzDa = true;
        zzeY();
    }

    private void zzeX() {
        com.google.android.gms.ads.internal.util.client.zzb.v("AdMediaPlayerView audio focus lost");
        this.zzDa = false;
        zzeY();
    }

    private void zzeY() {
        if (this.zzCZ || !this.zzDa) {
            zzb(0.0f);
        } else {
            zzb(this.zzCY);
        }
    }

    private AudioManager zzeZ() {
        return (AudioManager) getContext().getSystemService("audio");
    }

    private void zzv(boolean z) {
        com.google.android.gms.ads.internal.util.client.zzb.v("AdMediaPlayerView release");
        if (this.zzCR != null) {
            this.zzCR.reset();
            this.zzCR.release();
            this.zzCR = null;
            zzw(0);
            if (z) {
                this.zzCQ = 0;
                zzx(0);
            }
            zzeS();
        }
    }

    private void zzw(int i) {
        if (i == 3) {
            this.zzCO.zzfB();
        } else if (this.zzCP == 3 && i != 3) {
            this.zzCO.zzfC();
        }
        this.zzCP = i;
    }

    private void zzx(int i) {
        this.zzCQ = i;
    }

    @Override // com.google.android.gms.ads.internal.overlay.zzi
    public int getCurrentPosition() {
        if (zzeT()) {
            return this.zzCR.getCurrentPosition();
        }
        return 0;
    }

    @Override // com.google.android.gms.ads.internal.overlay.zzi
    public int getDuration() {
        if (zzeT()) {
            return this.zzCR.getDuration();
        }
        return -1;
    }

    @Override // com.google.android.gms.ads.internal.overlay.zzi
    public int getVideoHeight() {
        if (this.zzCR != null) {
            return this.zzCR.getVideoHeight();
        }
        return 0;
    }

    @Override // com.google.android.gms.ads.internal.overlay.zzi
    public int getVideoWidth() {
        if (this.zzCR != null) {
            return this.zzCR.getVideoWidth();
        }
        return 0;
    }

    @Override // android.media.AudioManager.OnAudioFocusChangeListener
    public void onAudioFocusChange(int focusChange) {
        if (focusChange > 0) {
            zzeW();
        } else if (focusChange < 0) {
            zzeX();
        }
    }

    @Override // android.media.MediaPlayer.OnBufferingUpdateListener
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        this.zzCV = percent;
    }

    @Override // android.media.MediaPlayer.OnCompletionListener
    public void onCompletion(MediaPlayer mp) {
        com.google.android.gms.ads.internal.util.client.zzb.v("AdMediaPlayerView completion");
        zzw(5);
        zzx(5);
        zzip.zzKO.post(new Runnable() { // from class: com.google.android.gms.ads.internal.overlay.zzc.2
            @Override // java.lang.Runnable
            public void run() {
                if (zzc.this.zzDc != null) {
                    zzc.this.zzDc.zzfq();
                }
            }
        });
    }

    @Override // android.media.MediaPlayer.OnErrorListener
    public boolean onError(MediaPlayer mp, int what, int extra) {
        final String str = zzCN.get(Integer.valueOf(what));
        final String str2 = zzCN.get(Integer.valueOf(extra));
        com.google.android.gms.ads.internal.util.client.zzb.zzaH("AdMediaPlayerView MediaPlayer error: " + str + ":" + str2);
        zzw(-1);
        zzx(-1);
        zzip.zzKO.post(new Runnable() { // from class: com.google.android.gms.ads.internal.overlay.zzc.3
            @Override // java.lang.Runnable
            public void run() {
                if (zzc.this.zzDc != null) {
                    zzc.this.zzDc.zzg(str, str2);
                }
            }
        });
        return true;
    }

    @Override // android.media.MediaPlayer.OnInfoListener
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        com.google.android.gms.ads.internal.util.client.zzb.v("AdMediaPlayerView MediaPlayer info: " + zzCN.get(Integer.valueOf(what)) + ":" + zzCN.get(Integer.valueOf(extra)));
        return true;
    }

    @Override // android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) throws IllegalStateException {
        int defaultSize = getDefaultSize(this.zzCT, widthMeasureSpec);
        int defaultSize2 = getDefaultSize(this.zzCU, heightMeasureSpec);
        if (this.zzCT > 0 && this.zzCU > 0) {
            int mode = View.MeasureSpec.getMode(widthMeasureSpec);
            int size = View.MeasureSpec.getSize(widthMeasureSpec);
            int mode2 = View.MeasureSpec.getMode(heightMeasureSpec);
            defaultSize2 = View.MeasureSpec.getSize(heightMeasureSpec);
            if (mode == 1073741824 && mode2 == 1073741824) {
                if (this.zzCT * defaultSize2 < this.zzCU * size) {
                    defaultSize = (this.zzCT * defaultSize2) / this.zzCU;
                } else if (this.zzCT * defaultSize2 > this.zzCU * size) {
                    defaultSize2 = (this.zzCU * size) / this.zzCT;
                    defaultSize = size;
                } else {
                    defaultSize = size;
                }
            } else if (mode == 1073741824) {
                int i = (this.zzCU * size) / this.zzCT;
                if (mode2 != Integer.MIN_VALUE || i <= defaultSize2) {
                    defaultSize2 = i;
                    defaultSize = size;
                } else {
                    defaultSize = size;
                }
            } else if (mode2 == 1073741824) {
                defaultSize = (this.zzCT * defaultSize2) / this.zzCU;
                if (mode == Integer.MIN_VALUE && defaultSize > size) {
                    defaultSize = size;
                }
            } else {
                int i2 = this.zzCT;
                int i3 = this.zzCU;
                if (mode2 != Integer.MIN_VALUE || i3 <= defaultSize2) {
                    defaultSize2 = i3;
                    defaultSize = i2;
                } else {
                    defaultSize = (this.zzCT * defaultSize2) / this.zzCU;
                }
                if (mode == Integer.MIN_VALUE && defaultSize > size) {
                    defaultSize2 = (this.zzCU * size) / this.zzCT;
                    defaultSize = size;
                }
            }
        }
        setMeasuredDimension(defaultSize, defaultSize2);
        if (Build.VERSION.SDK_INT == 16) {
            if ((this.zzCW > 0 && this.zzCW != defaultSize) || (this.zzCX > 0 && this.zzCX != defaultSize2)) {
                zzeQ();
            }
            this.zzCW = defaultSize;
            this.zzCX = defaultSize2;
        }
    }

    @Override // android.media.MediaPlayer.OnPreparedListener
    public void onPrepared(MediaPlayer mediaPlayer) throws IllegalStateException {
        com.google.android.gms.ads.internal.util.client.zzb.v("AdMediaPlayerView prepared");
        zzw(2);
        this.zzCO.zzfo();
        zzip.zzKO.post(new Runnable() { // from class: com.google.android.gms.ads.internal.overlay.zzc.1
            @Override // java.lang.Runnable
            public void run() {
                if (zzc.this.zzDc != null) {
                    zzc.this.zzDc.zzfo();
                }
            }
        });
        this.zzCT = mediaPlayer.getVideoWidth();
        this.zzCU = mediaPlayer.getVideoHeight();
        if (this.zzDb != 0) {
            seekTo(this.zzDb);
        }
        zzeQ();
        com.google.android.gms.ads.internal.util.client.zzb.zzaG("AdMediaPlayerView stream dimensions: " + this.zzCT + " x " + this.zzCU);
        if (this.zzCQ == 3) {
            play();
        }
        zzeR();
        zzeY();
    }

    @Override // android.view.TextureView.SurfaceTextureListener
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) throws IllegalStateException, IOException, SecurityException, IllegalArgumentException {
        com.google.android.gms.ads.internal.util.client.zzb.v("AdMediaPlayerView surface created");
        zzeP();
        zzip.zzKO.post(new Runnable() { // from class: com.google.android.gms.ads.internal.overlay.zzc.4
            @Override // java.lang.Runnable
            public void run() {
                if (zzc.this.zzDc != null) {
                    zzc.this.zzDc.zzfn();
                }
            }
        });
    }

    @Override // android.view.TextureView.SurfaceTextureListener
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        com.google.android.gms.ads.internal.util.client.zzb.v("AdMediaPlayerView surface destroyed");
        if (this.zzCR != null && this.zzDb == 0) {
            this.zzDb = this.zzCR.getCurrentPosition();
        }
        zzip.zzKO.post(new Runnable() { // from class: com.google.android.gms.ads.internal.overlay.zzc.5
            @Override // java.lang.Runnable
            public void run() {
                if (zzc.this.zzDc != null) {
                    zzc.this.zzDc.onPaused();
                    zzc.this.zzDc.zzfr();
                }
            }
        });
        zzv(true);
        return true;
    }

    @Override // android.view.TextureView.SurfaceTextureListener
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int w, int h) throws IllegalStateException {
        com.google.android.gms.ads.internal.util.client.zzb.v("AdMediaPlayerView surface changed");
        boolean z = this.zzCQ == 3;
        boolean z2 = this.zzCT == w && this.zzCU == h;
        if (this.zzCR != null && z && z2) {
            if (this.zzDb != 0) {
                seekTo(this.zzDb);
            }
            play();
        }
    }

    @Override // android.view.TextureView.SurfaceTextureListener
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        this.zzCO.zzb(this);
    }

    @Override // android.media.MediaPlayer.OnVideoSizeChangedListener
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        com.google.android.gms.ads.internal.util.client.zzb.v("AdMediaPlayerView size changed: " + width + " x " + height);
        this.zzCT = mp.getVideoWidth();
        this.zzCU = mp.getVideoHeight();
        if (this.zzCT == 0 || this.zzCU == 0) {
            return;
        }
        requestLayout();
    }

    @Override // com.google.android.gms.ads.internal.overlay.zzi
    public void pause() throws IllegalStateException {
        com.google.android.gms.ads.internal.util.client.zzb.v("AdMediaPlayerView pause");
        if (zzeT() && this.zzCR.isPlaying()) {
            this.zzCR.pause();
            zzw(4);
            zzip.zzKO.post(new Runnable() { // from class: com.google.android.gms.ads.internal.overlay.zzc.7
                @Override // java.lang.Runnable
                public void run() {
                    if (zzc.this.zzDc != null) {
                        zzc.this.zzDc.onPaused();
                    }
                }
            });
        }
        zzx(4);
    }

    @Override // com.google.android.gms.ads.internal.overlay.zzi
    public void play() throws IllegalStateException {
        com.google.android.gms.ads.internal.util.client.zzb.v("AdMediaPlayerView play");
        if (zzeT()) {
            this.zzCR.start();
            zzw(3);
            zzip.zzKO.post(new Runnable() { // from class: com.google.android.gms.ads.internal.overlay.zzc.6
                @Override // java.lang.Runnable
                public void run() {
                    if (zzc.this.zzDc != null) {
                        zzc.this.zzDc.zzfp();
                    }
                }
            });
        }
        zzx(3);
    }

    @Override // com.google.android.gms.ads.internal.overlay.zzi
    public void seekTo(int millis) throws IllegalStateException {
        com.google.android.gms.ads.internal.util.client.zzb.v("AdMediaPlayerView seek " + millis);
        if (!zzeT()) {
            this.zzDb = millis;
        } else {
            this.zzCR.seekTo(millis);
            this.zzDb = 0;
        }
    }

    @Override // com.google.android.gms.ads.internal.overlay.zzi
    public void setMimeType(String mimeType) {
    }

    @Override // com.google.android.gms.ads.internal.overlay.zzi
    public void setVideoPath(String path) throws IllegalStateException, IOException, SecurityException, IllegalArgumentException {
        setVideoURI(Uri.parse(path));
    }

    public void setVideoURI(Uri uri) throws IllegalStateException, IOException, SecurityException, IllegalArgumentException {
        this.zzCS = uri;
        this.zzDb = 0;
        zzeP();
        requestLayout();
        invalidate();
    }

    @Override // com.google.android.gms.ads.internal.overlay.zzi
    public void stop() throws IllegalStateException {
        com.google.android.gms.ads.internal.util.client.zzb.v("AdMediaPlayerView stop");
        if (this.zzCR != null) {
            this.zzCR.stop();
            this.zzCR.release();
            this.zzCR = null;
            zzw(0);
            zzx(0);
            zzeS();
        }
        this.zzCO.onStop();
    }

    @Override // android.view.View
    public String toString() {
        return getClass().getName() + "@" + Integer.toHexString(hashCode());
    }

    @Override // com.google.android.gms.ads.internal.overlay.zzi
    public void zza(float f) {
        this.zzCY = f;
        zzeY();
    }

    @Override // com.google.android.gms.ads.internal.overlay.zzi
    public void zza(zzh zzhVar) {
        this.zzDc = zzhVar;
    }

    @Override // com.google.android.gms.ads.internal.overlay.zzi
    public String zzeO() {
        return "MediaPlayer";
    }

    @Override // com.google.android.gms.ads.internal.overlay.zzi
    public void zzeU() {
        this.zzCZ = true;
        zzeY();
    }

    @Override // com.google.android.gms.ads.internal.overlay.zzi
    public void zzeV() {
        this.zzCZ = false;
        zzeY();
    }
}
