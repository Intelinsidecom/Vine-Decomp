package co.vine.android.player;

import android.view.View;
import co.vine.android.embed.player.VideoViewInterface;
import co.vine.android.util.CommonUtil;
import com.edisonwang.android.slog.SLog;

/* loaded from: classes.dex */
public class OnListVideoClickListener implements View.OnClickListener {
    private final HasVideoPlayerAdapter mAdapter;
    protected int mPosition;
    private final SaveVideoClicker mSaveVideoClicker = new SaveVideoClicker();

    public OnListVideoClickListener(HasVideoPlayerAdapter adapter) {
        this.mAdapter = adapter;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View v) {
        VideoViewInterface player = this.mAdapter.getPlayer(this.mPosition, false);
        if (this.mAdapter.getLastPlayer() != player) {
            this.mAdapter.pauseCurrentPlayer();
        }
        if (player != null && player.hasStarted()) {
            if (SLog.sLogsOn && CommonUtil.DEBUG_VERBOSE) {
                this.mSaveVideoClicker.onClick(player);
            }
            if (this.mPosition == player.getPlayingPosition()) {
                if (player.isPaused() || !player.isPlaying()) {
                    this.mAdapter.resumePlayer(player);
                    this.mAdapter.hideAttributions();
                    return;
                } else {
                    this.mAdapter.pausePlayer(player);
                    this.mAdapter.showAttributions();
                    return;
                }
            }
            return;
        }
        this.mAdapter.getVideoPathAndPlayForPosition(this.mPosition, false);
    }

    public void setPosition(int position) {
        this.mPosition = position;
    }

    public int getPosition() {
        return this.mPosition;
    }
}
