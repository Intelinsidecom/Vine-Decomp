package co.vine.android.player;

import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import co.vine.android.player.SdkVideoView;

/* loaded from: classes.dex */
public final class SinglePlayerState extends SdkVideoView.SdkVideoViewState {
    private static SinglePlayerState sInstance;
    private Uri mCurrentUri;
    private View mCurrentView;

    public static synchronized SinglePlayerState getInstance() {
        if (sInstance == null) {
            sInstance = new SinglePlayerState();
        }
        return sInstance;
    }

    private SinglePlayerState() {
    }

    @Override // co.vine.android.player.SdkVideoView.SdkVideoViewState
    public void onMediaOpRelease(MediaPlayer player) {
    }

    @Override // co.vine.android.player.SdkVideoView.SdkVideoViewState
    public boolean hasControlOfPlayer(View view, Uri uri) {
        return this.mCurrentView == view && this.mCurrentUri == uri;
    }

    @Override // co.vine.android.player.SdkVideoView.SdkVideoViewState
    public void setUri(Uri uri) {
        this.mCurrentUri = uri;
    }

    @Override // co.vine.android.player.SdkVideoView.SdkVideoViewState
    public boolean requiresLockOnStopPlayback() {
        return true;
    }

    @Override // co.vine.android.player.SdkVideoView.SdkVideoViewState
    public void setCurrentView(View view) {
        this.mCurrentView = view;
    }

    public Uri getCurrentUri() {
        return this.mCurrentUri;
    }

    public void release() {
        try {
            MediaPlayer player = getPlayer();
            if (player != null) {
                player.release();
                setPlayer(null);
            }
            this.mCurrentUri = null;
            this.mCurrentView = null;
        } catch (Exception e) {
            Log.e(SinglePlayerState.class.getSimpleName(), "Failed to release static player.", e);
        }
    }
}
