package co.vine.android.player;

import android.view.View;
import co.vine.android.embed.player.VideoViewInterface;
import com.edisonwang.android.slog.SLog;
import java.lang.ref.WeakReference;

/* loaded from: classes.dex */
public class OnSingleVideoClickedListener implements View.OnClickListener {
    private final WeakReference<VideoViewInterface> mViewPlayer;

    public OnSingleVideoClickedListener(VideoViewInterface videoPlayer) {
        this.mViewPlayer = new WeakReference<>(videoPlayer);
    }

    public void onClick() {
        VideoViewInterface player = this.mViewPlayer.get();
        if (player != null && player.hasStarted()) {
            SLog.d("Change player state.");
            if (player.isPaused()) {
                player.resume();
                return;
            } else {
                player.pause();
                return;
            }
        }
        SLog.d("Ignore because it is not in playing state.");
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View v) {
        onClick();
    }
}
