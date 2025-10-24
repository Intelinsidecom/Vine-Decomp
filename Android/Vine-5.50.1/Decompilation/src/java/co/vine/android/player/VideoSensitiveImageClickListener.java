package co.vine.android.player;

import android.view.View;
import co.vine.android.embed.player.VideoViewInterface;
import co.vine.android.widget.SensitiveAcknowledgments;

/* loaded from: classes.dex */
public class VideoSensitiveImageClickListener implements View.OnClickListener {
    private HasVideoPlayerAdapter mAdapter;
    protected int mPosition;
    protected long mPostId;
    private final SensitiveAcknowledgments mSensitiveAcknowledgments;

    public VideoSensitiveImageClickListener(HasVideoPlayerAdapter adapter, SensitiveAcknowledgments sensitiveAcknowledgments) {
        this.mAdapter = adapter;
        this.mSensitiveAcknowledgments = sensitiveAcknowledgments;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        VideoViewInterface player = this.mAdapter.getPlayer(this.mPosition, false);
        if (this.mAdapter.getLastPlayer() != player) {
            this.mAdapter.pauseCurrentPlayer();
        }
        if (player != null && player.hasStarted()) {
            if (this.mPosition == player.getPlayingPosition()) {
                if (player.isPaused()) {
                    player.resume();
                    return;
                } else {
                    player.pause();
                    return;
                }
            }
            this.mSensitiveAcknowledgments.acknowledge(this.mPostId);
            this.mAdapter.playFileAtPosition(this.mPosition, false);
            return;
        }
        this.mSensitiveAcknowledgments.acknowledge(this.mPostId);
        this.mAdapter.getVideoPathAndPlayForPosition(this.mPosition, false);
    }

    public void setPosition(int position) {
        this.mPosition = position;
    }

    public void setPostId(long postId) {
        this.mPostId = postId;
    }
}
