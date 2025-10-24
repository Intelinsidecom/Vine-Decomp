package co.vine.android.feedadapter.viewmanager;

import android.view.View;
import android.view.ViewGroup;
import co.vine.android.api.TimelineItem;
import co.vine.android.cache.video.UrlVideo;
import co.vine.android.cache.video.VideoKey;
import co.vine.android.feedadapter.viewholder.CardViewHolder;
import java.util.HashMap;

/* loaded from: classes.dex */
public abstract class CardViewManager implements ViewManager {
    public abstract View newView(int i, View view, ViewGroup viewGroup);

    public void onPause(boolean focused) {
    }

    public void onFocusChanged(boolean focused) {
    }

    public void onResume(boolean focused) {
    }

    public void onDestroy() {
    }

    public void onDestroyView() {
    }

    public void onScrollIdle() {
    }

    public void onRemoveItem(TimelineItem item) {
    }

    public void onVideoImageObtained() {
    }

    public void onVideoPathObtained(HashMap<VideoKey, UrlVideo> videos) {
    }

    public boolean isPlaying() {
        return false;
    }

    public CardViewHolder getValidViewHolder(int position) {
        return null;
    }

    public void pausePlayer() {
    }

    public void resetStates(boolean hasDataSetChanged) {
    }

    public void setProfileColor(int color) {
    }

    public void toggleMute(boolean mute) {
    }
}
