package co.vine.android.share.activities;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import co.vine.android.BaseControllerActionBarActivity;
import co.vine.android.R;
import co.vine.android.api.FeedMetadata;
import co.vine.android.api.VineRecipient;
import co.vine.android.api.VineUser;
import co.vine.android.cache.image.ImageKey;
import co.vine.android.cache.image.UrlImage;
import co.vine.android.cache.video.UrlVideo;
import co.vine.android.cache.video.VideoKey;
import co.vine.android.client.AppSessionListener;
import co.vine.android.client.Session;
import co.vine.android.embed.player.VideoViewInterface;
import co.vine.android.network.HttpResult;
import co.vine.android.scribe.AppNavigationProviderSingleton;
import co.vine.android.scribe.model.AppNavigation;
import co.vine.android.share.screens.CommentScreen;
import co.vine.android.share.screens.ScreenManager;
import co.vine.android.share.screens.ShareScreen;
import co.vine.android.share.screens.ShareScreenHelper;
import co.vine.android.share.widgets.ShareVideoView;
import co.vine.android.social.TumblrHelper;
import co.vine.android.util.SystemUtil;
import co.vine.android.util.ViewUtil;
import co.vine.android.widget.LoopSdkVideoView;
import com.edisonwang.android.slog.SLog;
import java.util.ArrayList;
import java.util.HashMap;

/* loaded from: classes.dex */
public class FeedShareActivity extends BaseControllerActionBarActivity {
    private long mChannelId;
    private FeedMetadata.FeedType mFeedType;
    private VideoKey mLowVideoKey;
    private long mPostId;
    private ScreenManager mScreenManager;
    private ShareVideoView mVideoContainer;
    private VideoKey mVideoKey;

    @Override // co.vine.android.BaseActionBarActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityDonut, android.app.Activity
    @SuppressLint({"MissingSuperCall"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_share, true);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.hide();
        }
        Intent intent = getIntent();
        this.mPostId = intent.getLongExtra("post_id", -1L);
        this.mChannelId = intent.getLongExtra("channel_id", -1L);
        this.mFeedType = (FeedMetadata.FeedType) intent.getSerializableExtra("feed_type");
        this.mAppSessionListener = createAppSessionListener();
        int videoHeight = SystemUtil.getDisplaySize(this).x;
        if (this.mPostId > 0) {
            this.mVideoKey = (VideoKey) intent.getParcelableExtra("video_key");
            this.mLowVideoKey = (VideoKey) intent.getParcelableExtra("low_video_key");
            String remoteVideoUrl = intent.getStringExtra("remote_video_url");
            String videoThumbnailUrl = intent.getStringExtra("video_thumbnail_url");
            String username = intent.getStringExtra("username");
            String shareUrl = intent.getStringExtra("share_url");
            String localVideoUriPath = this.mAppController.getVideoFilePath(this.mVideoKey);
            Uri localVideoUri = null;
            if (!TextUtils.isEmpty(localVideoUriPath)) {
                localVideoUri = Uri.parse(localVideoUriPath);
            }
            ImageKey videoThumbnailKey = new ImageKey(videoThumbnailUrl);
            Bitmap videoThumbnail = this.mAppController.getPhotoBitmap(videoThumbnailKey);
            this.mScreenManager = (ScreenManager) findViewById(R.id.content_container);
            this.mVideoContainer = (ShareVideoView) findViewById(R.id.video_container);
            initializeTimelineFeedShare(videoHeight, videoThumbnail, localVideoUri, remoteVideoUrl, videoThumbnailUrl, username, shareUrl);
        }
        ObjectAnimator videoFader = ObjectAnimator.ofFloat(this, "alpha", 0.5f, 1.0f);
        videoFader.setDuration(100L);
        videoFader.setTarget(this.mVideoContainer);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(videoFader);
        animatorSet.start();
    }

    @Override // co.vine.android.BaseControllerActionBarActivity, co.vine.android.BaseActionBarActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        this.mScreenManager.onResume();
        AppNavigationProviderSingleton.getInstance().setViewAndSubview(AppNavigation.Views.SHARE, null);
    }

    @Override // co.vine.android.BaseControllerActionBarActivity, co.vine.android.BaseActionBarActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onPause() {
        this.mScreenManager.onPause();
        super.onPause();
    }

    @Override // android.app.Activity
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_from_top, R.anim.slide_out_to_bottom);
    }

    @Override // co.vine.android.BaseActionBarActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onBackPressed() {
        boolean handled = this.mScreenManager.onBackPressed();
        if (!handled) {
            super.onBackPressed();
        }
    }

    @Override // co.vine.android.BaseActionBarActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        boolean handled = false;
        if (this.mScreenManager != null) {
            handled = this.mScreenManager.onActivityResult(this, requestCode, resultCode, data);
        }
        if (!handled) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override // android.app.Activity
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.mScreenManager.onRestoreInstanceState(savedInstanceState);
    }

    @Override // co.vine.android.BaseActionBarActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onSaveInstanceState(Bundle bundle) {
        this.mScreenManager.onSaveInstanceState(bundle);
        super.onSaveInstanceState(bundle);
    }

    private void initializeTimelineFeedShare(int videoHeight, Bitmap videoThumbnail, Uri videoUri, String remoteVideoUrl, String videoThumbnailUrl, String username, String shareUrl) {
        this.mScreenManager.initialize(this.mAppController);
        LayoutInflater layoutInflater = getLayoutInflater();
        ShareScreen shareScreen = ShareScreen.inflateInitialized(layoutInflater, false, true);
        shareScreen.setState(false, false, false);
        shareScreen.setOnActionListener(createShareViewOnActionListener(username, remoteVideoUrl, videoThumbnailUrl));
        CommentScreen commentScreen = (CommentScreen) layoutInflater.inflate(R.layout.comment_screen_standalone, (ViewGroup) null, false);
        Bundle timelineShareScreenData = new Bundle();
        timelineShareScreenData.putString("username", username);
        timelineShareScreenData.putString("shareUrl", shareUrl);
        this.mScreenManager.addScreen("main", shareScreen, timelineShareScreenData);
        this.mScreenManager.addScreen("comment", commentScreen, null);
        this.mVideoContainer.setHeight(videoHeight);
        setVideoAndThumbnail(videoUri, videoThumbnail);
        this.mVideoContainer.dim(128);
        this.mScreenManager.showScreen("main");
    }

    private AppSessionListener createAppSessionListener() {
        return new AppSessionListener() { // from class: co.vine.android.share.activities.FeedShareActivity.1
            @Override // co.vine.android.client.AppSessionListener
            public void onPhotoImageLoaded(HashMap<ImageKey, UrlImage> images) {
            }

            @Override // co.vine.android.client.AppSessionListener
            public void onGetUsersComplete(Session session, String reqId, int errorCode, String reasonPhrase, int count, ArrayList<VineUser> users, int nextPage, int prevPage, String anchor) {
            }

            @Override // co.vine.android.client.AppSessionListener
            public void onVideoPathObtained(HashMap<VideoKey, UrlVideo> videos) {
                UrlVideo video = videos.get(FeedShareActivity.this.mVideoKey);
                if (video != null && video.isValid()) {
                    Uri localVideoUri = Uri.parse(video.getAbsolutePath());
                    FeedShareActivity.this.mVideoContainer.setVideo(localVideoUri);
                }
            }

            @Override // co.vine.android.client.AppSessionListener
            public void onVideoPathError(VideoKey key, HttpResult result) {
                SLog.e("Download of video failed: " + result.reasonPhrase + " key: " + key.url);
            }
        };
    }

    private void setVideoAndThumbnail(Uri videoUri, Bitmap bitmap) {
        this.mVideoContainer.setThumbnailImage(bitmap);
        setVideo(videoUri);
    }

    private void setVideo(Uri videoUri) {
        if (videoUri != null) {
            final ImageView thumbnail = this.mVideoContainer.getVideoThumbnail();
            final LoopSdkVideoView view = this.mVideoContainer.getVideoView();
            view.setVideoURI(videoUri);
            view.setOnPreparedListener(new VideoViewInterface.OnPreparedListener() { // from class: co.vine.android.share.activities.FeedShareActivity.2
                @Override // co.vine.android.embed.player.VideoViewInterface.OnPreparedListener
                public void onPrepared(VideoViewInterface videoView) {
                    videoView.setMute(true);
                    videoView.start();
                    ViewUtil.disableAndHide(thumbnail);
                }
            });
            view.setOnErrorListener(new VideoViewInterface.OnErrorListener() { // from class: co.vine.android.share.activities.FeedShareActivity.3
                @Override // co.vine.android.embed.player.VideoViewInterface.OnErrorListener
                public boolean onError(VideoViewInterface videoView, int what, int extra) {
                    if (!view.retryOpenVideo(false)) {
                        FeedShareActivity.this.mAppController.getVideoFilePath(FeedShareActivity.this.mLowVideoKey);
                        return true;
                    }
                    return true;
                }
            });
        }
    }

    private ShareScreenHelper.OnActionListener createShareViewOnActionListener(String username, String remoteVideoUrl, String videoThumbnailUrl) {
        return new ShareScreenHelper.OnActionListener() { // from class: co.vine.android.share.activities.FeedShareActivity.4
            @Override // co.vine.android.share.screens.ShareScreenHelper.OnActionListener
            public void onSubmit(boolean isVineSelected, boolean isTwitterSelected, boolean isFacebookSelected, boolean isTumblrSelected, long selectedChannelId, ArrayList<VineRecipient> vmRecipients, String comment, boolean hidden) {
                Intent data = new Intent();
                data.putExtra("comment", comment);
                data.putExtra("postToVine", isVineSelected);
                data.putExtra("postToTwitter", isTwitterSelected);
                data.putExtra("postToFacebook", isFacebookSelected);
                data.putExtra("postToTumblr", isTumblrSelected);
                data.putExtra("channelId", FeedShareActivity.this.mChannelId);
                data.putExtra("feedType", FeedShareActivity.this.mFeedType);
                data.putExtra("coverPostId", FeedShareActivity.this.mPostId);
                if (isTumblrSelected) {
                    String tumblrOAuthToken = TumblrHelper.getTumblrToken(FeedShareActivity.this);
                    String tumblrOAuthSecret = TumblrHelper.getTumblrSecret(FeedShareActivity.this);
                    data.putExtra("tumblrOauthToken", tumblrOAuthToken);
                    data.putExtra("tumblrOauthSecret", tumblrOAuthSecret);
                }
                FeedShareActivity.this.setResult(-1, data);
                FeedShareActivity.this.finish();
            }

            @Override // co.vine.android.share.screens.ShareScreenHelper.OnActionListener
            public void onUndoRevine() {
            }
        };
    }

    public static Intent getIntent(Context context, long postId, String shareUrl, String username, String videoThumbnailUrl, String remoteVideoUrl, VideoKey videoKey, VideoKey lowVideoKey, FeedMetadata.FeedType feedType, long channelId) {
        Intent intent = new Intent(context, (Class<?>) FeedShareActivity.class);
        intent.putExtra("post_id", postId);
        intent.putExtra("remote_video_url", remoteVideoUrl);
        intent.putExtra("video_thumbnail_url", videoThumbnailUrl);
        intent.putExtra("username", username);
        intent.putExtra("share_url", shareUrl);
        intent.putExtra("video_key", videoKey);
        intent.putExtra("low_video_key", lowVideoKey);
        intent.putExtra("feed_type", feedType);
        intent.putExtra("channel_id", channelId);
        return intent;
    }
}
