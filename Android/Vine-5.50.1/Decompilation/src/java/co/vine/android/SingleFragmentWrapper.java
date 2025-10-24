package co.vine.android;

import android.os.Bundle;
import co.vine.android.client.AppController;
import co.vine.android.client.AppSessionListener;
import co.vine.android.network.UrlCachePolicy;

/* loaded from: classes.dex */
public class SingleFragmentWrapper {
    private AppController mAppController;
    private BaseControllerFragment mFragment;
    private long mPostId;
    private String mShareId;
    private AppSessionListener mSinglePostListener;

    public boolean onCreate(BaseControllerFragment fragment, AppSessionListener listener) {
        this.mFragment = fragment;
        this.mAppController = this.mFragment.getAppController();
        Bundle args = fragment.getArguments();
        this.mPostId = args.getLong("post_id");
        this.mShareId = args.getString("post_share_id");
        boolean takeFocus = args.getBoolean("take_focus", false);
        this.mSinglePostListener = listener;
        return takeFocus;
    }

    public void onResume() {
        if (this.mSinglePostListener != null) {
            this.mAppController.addListener(this.mSinglePostListener);
        }
    }

    public String fetchPostContent(UrlCachePolicy cachePolicy) {
        return this.mAppController.fetchPost(this.mAppController.getActiveSession(), this.mPostId, cachePolicy);
    }

    public String fetchPostId() {
        return this.mAppController.fetchPostId(this.mAppController.getActiveSession(), this.mShareId);
    }

    public void onGetPostIdComplete(long postId) {
        this.mPostId = postId;
    }

    public void onPause() {
        if (this.mSinglePostListener != null) {
            this.mAppController.removeListener(this.mSinglePostListener);
        }
    }

    public long getPostId() {
        return this.mPostId;
    }

    public String getSharedId() {
        return this.mShareId;
    }
}
