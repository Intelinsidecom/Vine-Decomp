package co.vine.android.share.activities;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;
import co.vine.android.BaseControllerActionBarActivity;
import co.vine.android.HomeTabActivity;
import co.vine.android.R;
import co.vine.android.SonyLaunchActivity;
import co.vine.android.api.VineChannel;
import co.vine.android.api.VineEntity;
import co.vine.android.api.VinePost;
import co.vine.android.api.VineRecipient;
import co.vine.android.api.VineSource;
import co.vine.android.api.VineUser;
import co.vine.android.cache.image.ImageKey;
import co.vine.android.cache.image.UrlImage;
import co.vine.android.cache.video.UrlVideo;
import co.vine.android.cache.video.VideoKey;
import co.vine.android.client.AppSessionListener;
import co.vine.android.client.Session;
import co.vine.android.embed.player.VideoViewInterface;
import co.vine.android.network.HttpResult;
import co.vine.android.recorder.BasicVineRecorder;
import co.vine.android.recorder.RecordConfigUtils;
import co.vine.android.recorder.RecordController;
import co.vine.android.recorder.RecordSessionManager;
import co.vine.android.recorder.RecordSessionVersion;
import co.vine.android.scribe.AppNavigationProviderSingleton;
import co.vine.android.scribe.model.AppNavigation;
import co.vine.android.service.VineUploadService;
import co.vine.android.service.components.Components;
import co.vine.android.service.components.share.ShareActionListener;
import co.vine.android.service.components.suggestions.SuggestionsActionListener;
import co.vine.android.share.adapters.ContactsAdapter;
import co.vine.android.share.adapters.RecipientsAdapter;
import co.vine.android.share.providers.ChannelProvider;
import co.vine.android.share.providers.RecipientProvider;
import co.vine.android.share.screens.ChannelPickerScreen;
import co.vine.android.share.screens.CommentScreen;
import co.vine.android.share.screens.ScreenManager;
import co.vine.android.share.screens.ShareScreen;
import co.vine.android.share.screens.ShareScreenHelper;
import co.vine.android.share.screens.VmScreen;
import co.vine.android.share.widgets.ShareVideoView;
import co.vine.android.social.FacebookHelper;
import co.vine.android.util.CommonUtil;
import co.vine.android.util.MediaUtil;
import co.vine.android.util.SystemUtil;
import co.vine.android.util.ViewUtil;
import co.vine.android.util.analytics.FlurryUtils;
import co.vine.android.widget.LoopSdkVideoView;
import co.vine.android.widget.ObservableSet;
import co.vine.android.widget.SectionAdapter;
import com.edisonwang.android.slog.SLog;
import com.googlecode.javacv.cpp.avformat;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.io.FilenameUtils;
import org.parceler.Parcels;

/* loaded from: classes.dex */
public class PostShareActivity extends BaseControllerActionBarActivity {
    private ChannelProvider mChannelProvider;
    private boolean mIsFollowing;
    private boolean mIsPostMine;
    private boolean mIsRevinedByMe;
    private VideoKey mLowVideoKey;
    private long mMyRepostId;
    private long mPostId;
    private List<RecipientsAdapter> mRecipientsAdapters;
    private RecipientProvider mRecipientsProvider;
    private long mRepostId;
    private ScreenManager mScreenManager;
    private ShareActionListener mShareActionListener;
    private ShareScreenHelper mShareHelper;
    private SuggestionsActionListener mSuggestionsActionListener;
    private ShareVideoView mVideoContainer;
    private VideoKey mVideoKey;
    private ArrayList<VineSource> sources;

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
        this.sources = intent.getParcelableArrayListExtra("sources");
        FragmentManager fragmentManager = getSupportFragmentManager();
        ObservableSet<VineRecipient> selectedRecipientsRepository = new ObservableSet<>();
        RecipientsAdapter userSearchAdapter = new RecipientsAdapter(this, getString(R.string.search), selectedRecipientsRepository);
        RecipientsAdapter recentsAdapter = new RecipientsAdapter(this, getString(R.string.recent), selectedRecipientsRepository);
        RecipientsAdapter friendsAdapter = new RecipientsAdapter(this, getString(R.string.friends), selectedRecipientsRepository);
        RecipientsAdapter followingAdapter = new RecipientsAdapter(this, getString(R.string.following), selectedRecipientsRepository);
        ContactsAdapter defaultRecipientContactsAdapter = new ContactsAdapter(this, getString(R.string.contacts), fragmentManager, selectedRecipientsRepository);
        ContactsAdapter searchContactsAdapter = new ContactsAdapter(this, getString(R.string.contacts), fragmentManager, selectedRecipientsRepository);
        this.mRecipientsAdapters = new ArrayList();
        this.mRecipientsAdapters.add(userSearchAdapter);
        this.mRecipientsAdapters.add(recentsAdapter);
        this.mRecipientsAdapters.add(friendsAdapter);
        this.mRecipientsAdapters.add(followingAdapter);
        this.mAppSessionListener = createAppSessionListener();
        this.mShareActionListener = new ShareListener();
        this.mSuggestionsActionListener = new SuggestedFriendsListener();
        BaseAdapter[] defaultRecipientBaseAdapters = {recentsAdapter, friendsAdapter, followingAdapter, defaultRecipientContactsAdapter};
        SectionAdapter defaultRecipientsMultiDataSetAdapter = new SectionAdapter(defaultRecipientBaseAdapters);
        SectionAdapter searchMultiDataSetAdapter = new SectionAdapter(userSearchAdapter, searchContactsAdapter);
        this.mRecipientsProvider = new RecipientProvider(this, getLoaderManager(), this.mAppController);
        this.mRecipientsProvider.requestContacts(null);
        int videoHeight = SystemUtil.getDisplaySize(this).x;
        if (this.mPostId > 0) {
            this.mVideoKey = (VideoKey) intent.getParcelableExtra("video_key");
            this.mLowVideoKey = (VideoKey) intent.getParcelableExtra("low_video_key");
            String remoteVideoUrl = intent.getStringExtra("remote_video_url");
            String videoThumbnailUrl = intent.getStringExtra("video_thumbnail_url");
            String username = intent.getStringExtra("username");
            String shareUrl = intent.getStringExtra("share_url");
            String localVideoUriPath = this.mAppController.getVideoFilePath(this.mVideoKey);
            this.mRepostId = intent.getLongExtra("repost_id", 0L);
            this.mIsPostMine = intent.getBooleanExtra("is_post_mine", false);
            this.mIsRevinedByMe = intent.getBooleanExtra("revined_by_me", false);
            this.mIsFollowing = intent.getBooleanExtra("is_following", false);
            this.mMyRepostId = intent.getLongExtra("my_repost_id", 0L);
            Uri localVideoUri = null;
            if (!TextUtils.isEmpty(localVideoUriPath)) {
                localVideoUri = Uri.parse(localVideoUriPath);
            }
            ImageKey videoThumbnailKey = new ImageKey(videoThumbnailUrl);
            Bitmap videoThumbnail = this.mAppController.getPhotoBitmap(videoThumbnailKey);
            this.mScreenManager = (ScreenManager) findViewById(R.id.content_container);
            this.mVideoContainer = (ShareVideoView) findViewById(R.id.video_container);
            boolean hasLongform = intent.getBooleanExtra("longform", false);
            initializeTimelineShare(videoHeight, videoThumbnail, localVideoUri, defaultRecipientsMultiDataSetAdapter, searchMultiDataSetAdapter, this.mRecipientsProvider, selectedRecipientsRepository, remoteVideoUrl, videoThumbnailUrl, username, shareUrl, hasLongform);
        } else {
            String uploadVideoUriPath = intent.getStringExtra("upload_video_uri_path");
            String previewVideoUriPath = intent.getStringExtra("preview_video_uri_path");
            String videoThumbnailUrl2 = intent.getStringExtra("video_thumbnail_url");
            boolean isRetry = intent.getBooleanExtra("is_retry", false);
            boolean isFromSonyWidget = intent.getBooleanExtra("is_from_sony_widget", false);
            Bitmap videoThumbnail2 = BitmapFactory.decodeFile(videoThumbnailUrl2);
            this.mChannelProvider = new ChannelProvider(this.mAppController);
            this.mScreenManager = (ScreenManager) findViewById(R.id.content_container);
            this.mVideoContainer = (ShareVideoView) findViewById(R.id.video_container);
            PostShareParameters inputParameters = (PostShareParameters) Parcels.unwrap(intent.getParcelableExtra("share_screen_parameters"));
            initializeNewPostShare(videoHeight, videoThumbnail2, previewVideoUriPath, uploadVideoUriPath, selectedRecipientsRepository, defaultRecipientsMultiDataSetAdapter, searchMultiDataSetAdapter, this.mRecipientsProvider, isRetry, isFromSonyWidget, inputParameters);
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
        if (this.mChannelProvider != null) {
            this.mChannelProvider.onResume();
            this.mChannelProvider.requestChannels();
        }
        this.mScreenManager.onResume();
        this.mRecipientsProvider.requestRecipientsGroupedBySection();
        Components.shareComponent().addListener(this.mShareActionListener);
        Components.suggestionsComponent().addListener(this.mSuggestionsActionListener);
        AppNavigationProviderSingleton.getInstance().setViewAndSubview(AppNavigation.Views.SHARE, null);
    }

    @Override // co.vine.android.BaseControllerActionBarActivity, co.vine.android.BaseActionBarActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onPause() {
        Components.shareComponent().removeListener(this.mShareActionListener);
        Components.suggestionsComponent().removeListener(this.mSuggestionsActionListener);
        if (this.mChannelProvider != null) {
            this.mChannelProvider.onPause();
        }
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
            if (this.mShareHelper == null || this.mPostId > 0) {
                super.onBackPressed();
                return;
            }
            PostShareParameters params = (PostShareParameters) Parcels.unwrap(getIntent().getParcelableExtra("share_screen_parameters"));
            Intent backData = new Intent();
            PostShareParameters newParams = new PostShareParameters(params.caption, params.captionEntities, params.venueName, params.venueId, this.mShareHelper.getSelectedChannel(), this.mShareHelper.getSelectedRecipients(), Boolean.valueOf(this.mShareHelper.isVineSelected()), Boolean.valueOf(this.mShareHelper.isTwitterSelected()), Boolean.valueOf(this.mShareHelper.isFacebookSelected()), Boolean.valueOf(this.mShareHelper.isTumblrSelected()));
            backData.putExtra("share_screen_parameters", Parcels.wrap(newParams));
            setResult(0, backData);
            finish();
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

    private void initializeTimelineShare(int videoHeight, Bitmap videoThumbnail, Uri videoUri, SectionAdapter defaultRecipientsMultiDataSetAdapter, SectionAdapter searchMultiDataSetAdapter, RecipientProvider recipientProvider, ObservableSet<VineRecipient> selectedRecipientsRepository, String remoteVideoUrl, String videoThumbnailUrl, String username, String shareUrl, boolean hasLongform) {
        this.mScreenManager.initialize(this.mAppController);
        LayoutInflater layoutInflater = getLayoutInflater();
        ShareScreen shareScreen = ShareScreen.inflateInitialized(layoutInflater, true, false, selectedRecipientsRepository);
        shareScreen.setState(this.mIsPostMine, this.mIsRevinedByMe, hasLongform);
        shareScreen.setOnActionListener(createShareViewOnActionListener(username, remoteVideoUrl, videoThumbnailUrl));
        CommentScreen commentScreen = (CommentScreen) layoutInflater.inflate(R.layout.comment_screen_standalone, (ViewGroup) null, false);
        Bundle timelineShareScreenData = new Bundle();
        timelineShareScreenData.putString("username", username);
        timelineShareScreenData.putString("shareUrl", shareUrl);
        this.mScreenManager.addScreen("main", shareScreen, timelineShareScreenData);
        if (!hasLongform) {
            Bundle timelineVmScreenData = new Bundle();
            timelineVmScreenData.putLong("postId", this.mPostId);
            timelineVmScreenData.putString("remoteVideoUrl", remoteVideoUrl);
            timelineVmScreenData.putString("videoThumbnailUrl", videoThumbnailUrl);
            VmScreen vmScreen = VmScreen.inflateInitialized(layoutInflater, defaultRecipientsMultiDataSetAdapter, searchMultiDataSetAdapter, recipientProvider, selectedRecipientsRepository, true);
            this.mScreenManager.addScreen("vm", vmScreen, timelineVmScreenData);
        }
        this.mScreenManager.addScreen("comment", commentScreen, null);
        this.mVideoContainer.setHeight(videoHeight);
        setVideoAndThumbnail(videoUri, videoThumbnail);
        this.mVideoContainer.dim(128);
        this.mScreenManager.showScreen("main");
    }

    private void initializeNewPostShare(int videoHeight, Bitmap videoThumbnail, String previewVideoUriPath, String uploadVideoUriPath, ObservableSet<VineRecipient> selectedRecipientsRepository, SectionAdapter defaultRecipientsMultiDataSetAdapter, SectionAdapter searchMultiDataSetAdapter, RecipientProvider recipientProvider, boolean isRetry, boolean isFromSonyWidget, PostShareParameters state) {
        Uri previewVideoUri = Uri.parse(previewVideoUriPath);
        VineChannel channel = null;
        if (state != null) {
            List<VineRecipient> recipients = state.recipients;
            channel = state.channel;
            if (recipients != null) {
                for (VineRecipient recipient : recipients) {
                    selectedRecipientsRepository.add(recipient);
                }
            }
        }
        this.mScreenManager.initialize(this.mAppController);
        LayoutInflater layoutInflater = getLayoutInflater();
        ShareScreen postShareScreen = ShareScreen.inflateInitialized(layoutInflater, false, false, selectedRecipientsRepository, channel, state);
        postShareScreen.setOnActionListener(createMainScreenOnActionListener(state.caption, state.captionEntities, previewVideoUriPath, uploadVideoUriPath, isRetry, isFromSonyWidget, state.venueId));
        VmScreen postVmScreen = VmScreen.inflateInitialized(layoutInflater, defaultRecipientsMultiDataSetAdapter, searchMultiDataSetAdapter, recipientProvider, selectedRecipientsRepository, false);
        ChannelPickerScreen channelPickerScreen = (ChannelPickerScreen) layoutInflater.inflate(R.layout.channel_picker_screen_standalone, (ViewGroup) null, false);
        channelPickerScreen.setChannelProvider(this.mChannelProvider);
        this.mShareHelper = postShareScreen.getHelper();
        this.mScreenManager.addScreen("main", postShareScreen, null);
        this.mScreenManager.addScreen("vm", postVmScreen, null);
        this.mScreenManager.addScreen("channel_picker", channelPickerScreen, null);
        this.mVideoContainer.setHeight(videoHeight);
        this.mVideoContainer.setVideoAndThumbnailImage(previewVideoUri, videoThumbnail);
        this.mVideoContainer.dim(128);
        this.mScreenManager.showScreen("main");
    }

    private ShareScreenHelper.OnActionListener createMainScreenOnActionListener(final String caption, final ArrayList<VineEntity> captionEntities, final String previewVideoUriPath, final String uploadVideoUriPath, final boolean isRetry, final boolean isFromSonyWidget, final String foursquareVenueId) {
        return new ShareScreenHelper.OnActionListener() { // from class: co.vine.android.share.activities.PostShareActivity.1
            @Override // co.vine.android.share.screens.ShareScreenHelper.OnActionListener
            public void onSubmit(boolean isVineSelected, boolean isTwitterSelected, boolean isFacebookSelected, boolean isTumblrSelected, long selectedChannelId, ArrayList<VineRecipient> vmRecipients, String comment, boolean hidePost) throws Throwable {
                final Activity activity = PostShareActivity.this;
                String filePathToSave = PostShareActivity.this.getFilePathToSave(previewVideoUriPath, uploadVideoUriPath);
                PostShareActivity.this.saveFileToCameraRoll(activity, filePathToSave);
                if (isVineSelected) {
                    Intent performPost = VineUploadService.getPostIntent(activity, uploadVideoUriPath, caption, isTwitterSelected, isFacebookSelected, isTumblrSelected, foursquareVenueId, selectedChannelId, isRetry, captionEntities, vmRecipients, null, -1L, hidePost, PostShareActivity.this.sources);
                    activity.startService(performPost);
                    activity.startService(VineUploadService.getShowProgressIntent(activity));
                } else if (vmRecipients != null && !vmRecipients.isEmpty()) {
                    Intent performVmPost = VineUploadService.getVMPostIntent(activity, uploadVideoUriPath, false, -1L, vmRecipients, caption);
                    activity.startService(performVmPost);
                    activity.startService(VineUploadService.getShowProgressIntent(activity));
                } else {
                    Intent discardPost = VineUploadService.getDiscardIntent(activity, uploadVideoUriPath);
                    activity.startService(discardPost);
                }
                activity.setResult(-1);
                FlurryUtils.trackPost(isVineSelected);
                FlurryUtils.trackPostTier(String.valueOf((int) (SystemUtil.getMemoryRatio(activity, true) * 2.0d)), BasicVineRecorder.sTimeTaken, RecordController.sMaxKnownStopTime);
                PostShareActivity.this.cleanUpRecordingSession(activity, previewVideoUriPath, uploadVideoUriPath);
                if (isFromSonyWidget) {
                    String postingMessage = PostShareActivity.this.getResources().getString(R.string.share_posting);
                    PostShareActivity.this.showProgressDialog(postingMessage);
                    new Handler().postDelayed(new Runnable() { // from class: co.vine.android.share.activities.PostShareActivity.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            Intent sonyRecording = new Intent(activity, (Class<?>) SonyLaunchActivity.class);
                            activity.startActivity(sonyRecording);
                            activity.setResult(-1);
                            activity.finish();
                        }
                    }, 750L);
                } else {
                    Intent home = new Intent(activity, (Class<?>) HomeTabActivity.class).setFlags(avformat.AVFMT_SEEK_TO_PTS);
                    activity.startActivity(home);
                    activity.finish();
                }
            }

            @Override // co.vine.android.share.screens.ShareScreenHelper.OnActionListener
            public void onUndoRevine() {
            }
        };
    }

    private final class ShareListener implements ShareActionListener {
        private ShareListener() {
        }

        @Override // co.vine.android.service.components.share.ShareActionListener
        public void onVMShared(String reqId, int statusCode, String reasonPhrase, long postId, String message) {
            PostShareActivity.this.finish();
        }

        @Override // co.vine.android.service.components.share.ShareActionListener
        public void onNetworkShared(String reqId, int statusCode, String reasonPhrase, long postId, String network, String message) {
            if (statusCode == 400 && ShareScreenHelper.ShareTarget.FACEBOOK.name().toLowerCase().equals(network)) {
                FacebookHelper.clearFacebookToken(PostShareActivity.this);
                FacebookHelper.connectToFacebookProfile(PostShareActivity.this);
                Toast.makeText(PostShareActivity.this, R.string.facebook_auth_failed_retry, 1).show();
                return;
            }
            PostShareActivity.this.finish();
        }
    }

    private final class SuggestedFriendsListener extends SuggestionsActionListener {
        private SuggestedFriendsListener() {
        }

        @Override // co.vine.android.service.components.suggestions.SuggestionsActionListener
        public void onGetFriendsTypeAheadComplete(String reqId, int statusCode, String reasonPhrase, String query, ArrayList<VineUser> users) {
            PostShareActivity.this.mRecipientsProvider.onGetFriendsTypeAheadComplete(users);
        }
    }

    private AppSessionListener createAppSessionListener() {
        return new AppSessionListener() { // from class: co.vine.android.share.activities.PostShareActivity.2
            @Override // co.vine.android.client.AppSessionListener
            public void onPhotoImageLoaded(HashMap<ImageKey, UrlImage> images) {
                for (RecipientsAdapter recipientsAdapter : PostShareActivity.this.mRecipientsAdapters) {
                    recipientsAdapter.updateAvatarImages(images);
                }
            }

            @Override // co.vine.android.client.AppSessionListener
            public void onGetUsersComplete(Session session, String reqId, int errorCode, String reasonPhrase, int count, ArrayList<VineUser> users, int nextPage, int prevPage, String anchor) {
                PostShareActivity.this.mRecipientsProvider.onGetUsersComplete();
            }

            @Override // co.vine.android.client.AppSessionListener
            public void onVideoPathObtained(HashMap<VideoKey, UrlVideo> videos) {
                UrlVideo video = videos.get(PostShareActivity.this.mVideoKey);
                if (video != null && video.isValid()) {
                    Uri localVideoUri = Uri.parse(video.getAbsolutePath());
                    PostShareActivity.this.mVideoContainer.setVideo(localVideoUri);
                }
            }

            @Override // co.vine.android.client.AppSessionListener
            public void onVideoPathError(VideoKey key, HttpResult result) {
                SLog.e("Download of video failed: " + result.reasonPhrase + " key: " + key.url);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getFilePathToSave(String previewVideoUriPath, String uploadVideoUriPath) {
        return previewVideoUriPath != null ? previewVideoUriPath : uploadVideoUriPath;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveFileToCameraRoll(Context context, String filePath) throws Throwable {
        SLog.d("Saving file at path {} to the camera roll.", filePath);
        File sourceFile = new File(filePath);
        File destinationFile = RecordConfigUtils.getCameraRollFile(context, System.currentTimeMillis(), FilenameUtils.getExtension(filePath));
        if (sourceFile.exists() && destinationFile != null) {
            boolean didCopyToFile = RecordConfigUtils.copySilently(sourceFile, destinationFile);
            if (didCopyToFile) {
                MediaUtil.scanFile(context, destinationFile, null);
                SLog.d("Copied the file at path {} to {}.", filePath, destinationFile.getAbsolutePath());
            } else {
                SLog.d("Failed to copy file at path {}.", filePath);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cleanUpRecordingSession(Context context, String previewVideoUriPath, String uploadVideoUriPath) {
        try {
            if (previewVideoUriPath != null) {
                File previewVideoDirectory = new File(previewVideoUriPath).getParentFile();
                RecordSessionManager.deleteSession(previewVideoDirectory, "Session Completed.");
                SLog.i("Session folder deleted: {}.", previewVideoUriPath);
            } else {
                RecordSessionVersion.deleteSessionWithName(context, new File(uploadVideoUriPath).getName());
            }
        } catch (IOException e) {
            SLog.e("Failed to delete session folder.", (Throwable) e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showProgressDialog(String message) {
        this.mProgressDialog = new ProgressDialog(this);
        this.mProgressDialog.setMessage(message);
        try {
            this.mProgressDialog.show();
        } catch (Exception e) {
            SLog.e("Failed to show the progress dialog with message {}.", (Object) message, (Throwable) e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean sendPostAsVm(long postId, ArrayList<VineRecipient> recipients, String remoteVideoUrl, String videoThumbnailUrl, String comment) {
        if (postId <= 0 || recipients == null || recipients.isEmpty()) {
            return false;
        }
        Components.shareComponent().shareVM(this.mAppController, recipients, postId, remoteVideoUrl, videoThumbnailUrl, "");
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean sendCommentAsVm(ArrayList<VineRecipient> recipients, String comment) {
        if (recipients == null || recipients.isEmpty() || CommonUtil.isNullOrWhitespace(comment)) {
            return false;
        }
        Components.shareComponent().shareVM(this.mAppController, recipients, -1L, null, null, comment);
        return true;
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
            view.setOnPreparedListener(new VideoViewInterface.OnPreparedListener() { // from class: co.vine.android.share.activities.PostShareActivity.3
                @Override // co.vine.android.embed.player.VideoViewInterface.OnPreparedListener
                public void onPrepared(VideoViewInterface videoView) {
                    videoView.setMute(true);
                    videoView.start();
                    ViewUtil.disableAndHide(thumbnail);
                }
            });
            view.setOnErrorListener(new VideoViewInterface.OnErrorListener() { // from class: co.vine.android.share.activities.PostShareActivity.4
                @Override // co.vine.android.embed.player.VideoViewInterface.OnErrorListener
                public boolean onError(VideoViewInterface videoView, int what, int extra) {
                    if (!view.retryOpenVideo(false)) {
                        PostShareActivity.this.mAppController.getVideoFilePath(PostShareActivity.this.mLowVideoKey);
                        return true;
                    }
                    return true;
                }
            });
        }
    }

    private ShareScreenHelper.OnActionListener createShareViewOnActionListener(final String username, final String remoteVideoUrl, final String videoThumbnailUrl) {
        return new ShareScreenHelper.OnActionListener() { // from class: co.vine.android.share.activities.PostShareActivity.5
            @Override // co.vine.android.share.screens.ShareScreenHelper.OnActionListener
            public void onSubmit(boolean isVineSelected, boolean isTwitterSelected, boolean isFacebookSelected, boolean isTumblrSelected, long selectedChannelId, ArrayList<VineRecipient> vmRecipients, String comment, boolean hidden) {
                if (isTwitterSelected) {
                    Components.shareComponent().sharePost(PostShareActivity.this, PostShareActivity.this.mAppController, ShareScreenHelper.ShareTarget.TWITTER.name().toLowerCase(), comment, PostShareActivity.this.mPostId);
                }
                if (isFacebookSelected) {
                    Components.shareComponent().sharePost(PostShareActivity.this, PostShareActivity.this.mAppController, ShareScreenHelper.ShareTarget.FACEBOOK.name().toLowerCase(), comment, PostShareActivity.this.mPostId);
                }
                if (isTumblrSelected) {
                    Components.shareComponent().sharePost(PostShareActivity.this, PostShareActivity.this.mAppController, ShareScreenHelper.ShareTarget.TUMBLR.name().toLowerCase(), comment, PostShareActivity.this.mPostId);
                }
                if (!vmRecipients.isEmpty()) {
                    PostShareActivity.this.sendPostAsVm(PostShareActivity.this.mPostId, vmRecipients, remoteVideoUrl, videoThumbnailUrl, comment);
                    PostShareActivity.this.sendCommentAsVm(vmRecipients, comment);
                }
                if (isVineSelected && !PostShareActivity.this.mIsPostMine) {
                    Intent data = new Intent();
                    data.putExtra("post_id", PostShareActivity.this.mPostId);
                    data.putExtra("repost_id", PostShareActivity.this.mRepostId);
                    data.putExtra("following", PostShareActivity.this.mIsFollowing);
                    data.putExtra("username", username);
                    data.putExtra("revine", isVineSelected);
                    PostShareActivity.this.setResult(-1, data);
                } else {
                    PostShareActivity.this.setResult(-1, null);
                }
                PostShareActivity.this.finish();
            }

            @Override // co.vine.android.share.screens.ShareScreenHelper.OnActionListener
            public void onUndoRevine() {
                Intent intent = new Intent();
                intent.putExtra("post_id", PostShareActivity.this.mPostId);
                intent.putExtra("repost_id", PostShareActivity.this.mRepostId);
                intent.putExtra("revine", false);
                intent.putExtra("following", PostShareActivity.this.mIsFollowing);
                intent.putExtra("my_repost_id", PostShareActivity.this.mMyRepostId);
                PostShareActivity.this.setResult(-1, intent);
                PostShareActivity.this.finish();
            }
        };
    }

    public static Intent getPostShareIntent(Context context, VinePost post, boolean isPostMine) {
        return getIntent(context, post.postId, post.repost == null ? 0L : post.repost.repostId, post.myRepostId, post.shareUrl, post.username, post.thumbnailUrl, post.following, isPostMine, post.videoUrl, new VideoKey(post.videoUrl), new VideoKey(post.videoLowUrl), post.longform != null);
    }

    private static Intent getIntent(Context context, long postId, long repostId, long myRepostId, String shareUrl, String username, String videoThumbnailUrl, boolean following, boolean isPostMine, String remoteVideoUrl, VideoKey videoKey, VideoKey lowVideoKey, boolean hasLongform) {
        Intent intent = new Intent(context, (Class<?>) PostShareActivity.class);
        intent.putExtra("post_id", postId);
        intent.putExtra("repost_id", repostId);
        intent.putExtra("remote_video_url", remoteVideoUrl);
        intent.putExtra("video_thumbnail_url", videoThumbnailUrl);
        intent.putExtra("username", username);
        intent.putExtra("share_url", shareUrl);
        intent.putExtra("is_post_mine", isPostMine);
        intent.putExtra("revined_by_me", myRepostId > 0);
        intent.putExtra("my_repost_id", myRepostId);
        intent.putExtra("is_following", following);
        intent.putExtra("video_key", videoKey);
        intent.putExtra("low_video_key", lowVideoKey);
        intent.putExtra("longform", hasLongform);
        return intent;
    }

    public static Intent getPostCreationShareIntent(Context context, String uploadVideoUriPath, String previewVideoUriPath, String videoThumbnailUrl, boolean isRetry, boolean isFromSonyWidget, PostShareParameters shareParameters, ArrayList<VineSource> sources) {
        Intent intent = new Intent(context, (Class<?>) PostShareActivity.class);
        intent.putExtra("post_id", -1L);
        intent.putExtra("upload_video_uri_path", uploadVideoUriPath);
        intent.putExtra("preview_video_uri_path", previewVideoUriPath);
        intent.putExtra("video_thumbnail_url", videoThumbnailUrl);
        intent.putExtra("is_retry", isRetry);
        intent.putExtra("is_from_sony_widget", isFromSonyWidget);
        intent.putExtra("share_screen_parameters", Parcels.wrap(shareParameters));
        intent.putExtra("sources", sources);
        return intent;
    }
}
