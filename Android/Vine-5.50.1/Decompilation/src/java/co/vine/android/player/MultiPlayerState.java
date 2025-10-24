package co.vine.android.player;

import android.media.MediaPlayer;
import android.net.Uri;
import android.view.View;
import co.vine.android.player.SdkVideoView;

/* loaded from: classes.dex */
public final class MultiPlayerState extends SdkVideoView.SdkVideoViewState {
    @Override // co.vine.android.player.SdkVideoView.SdkVideoViewState
    public void onMediaOpRelease(MediaPlayer player) {
        player.release();
        setPlayer(null);
    }

    @Override // co.vine.android.player.SdkVideoView.SdkVideoViewState
    public boolean requiresLockOnStopPlayback() {
        return false;
    }

    @Override // co.vine.android.player.SdkVideoView.SdkVideoViewState
    public boolean hasControlOfPlayer(View View, Uri uri) {
        return true;
    }

    @Override // co.vine.android.player.SdkVideoView.SdkVideoViewState
    public void setCurrentView(View view) {
    }

    @Override // co.vine.android.player.SdkVideoView.SdkVideoViewState
    public void setUri(Uri uri) {
    }
}
