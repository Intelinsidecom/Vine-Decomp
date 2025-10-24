package co.vine.android;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import co.vine.android.api.VineFeed;
import co.vine.android.api.VinePost;
import co.vine.android.cache.video.UrlVideo;
import co.vine.android.cache.video.VideoKey;
import co.vine.android.client.AppController;
import co.vine.android.client.AppSessionListener;
import co.vine.android.recordingui.CameraCaptureActivity;
import co.vine.android.scribe.FollowScribeActionsLogger;
import co.vine.android.service.components.Components;
import co.vine.android.util.ClientFlagsHelper;
import co.vine.android.util.CommonUtil;
import co.vine.android.util.CrashUtil;
import co.vine.android.util.Util;
import co.vine.android.widgets.PromptDialogSupportFragment;
import com.googlecode.javacv.cpp.opencv_core;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/* loaded from: classes.dex */
public class PostOptionsDialogActivity extends SupportListActivity implements View.OnClickListener {
    private AppController mAppController;
    private ArrayAdapter<Option> mArrayAdapter;
    private boolean mDelete;
    private boolean mDeleteFeedItem;
    private boolean mEditCaption;
    private VineFeed mFeed;
    private boolean mHide;
    private ArrayList<Option> mItems2;
    private String mName;
    private boolean mOriginalPost;
    private VinePost mPost;
    private String mPostCachedLocalPath;
    private boolean mPrivate;
    private boolean mReport;
    private String mShareUrl;
    private boolean mShowLessUser;
    private boolean mUnfollow;
    private boolean mUnhide;
    private String mVideoPath;
    private final AppSessionListener mVideoPathListener = new AppSessionListener() { // from class: co.vine.android.PostOptionsDialogActivity.1
        @Override // co.vine.android.client.AppSessionListener
        public void onVideoPathObtained(HashMap<VideoKey, UrlVideo> videos) {
            UrlVideo video;
            if (PostOptionsDialogActivity.this.mPost != null && (video = videos.get(new VideoKey(PostOptionsDialogActivity.this.mPost.videoUrl))) != null && video.isValid()) {
                PostOptionsDialogActivity.this.mPostCachedLocalPath = video.getAbsolutePath();
                PostOptionsDialogActivity.this.mAppController.removeListener(this);
            }
        }
    };
    private final PromptDialogSupportFragment.OnDialogDoneListener mReportDeleteDialogDoneListener = new PromptDialogSupportFragment.OnDialogDoneListener() { // from class: co.vine.android.PostOptionsDialogActivity.2
        @Override // co.vine.android.widgets.PromptDialogFragment.OnDialogDoneListener
        public void onDialogDone(DialogInterface dialog, int id, int which) {
            Intent intent = new Intent();
            intent.putExtra("post_id", PostOptionsDialogActivity.this.mPost == null ? 0L : PostOptionsDialogActivity.this.mPost.postId);
            switch (id) {
                case 1:
                    switch (which) {
                        case -1:
                            intent.setAction("action_report");
                            PostOptionsDialogActivity.this.setResult(-1, intent);
                            PostOptionsDialogActivity.this.finish();
                            break;
                    }
                case 2:
                    switch (which) {
                        case opencv_core.CV_StsInternal /* -3 */:
                            intent.setAction("action_hide");
                            PostOptionsDialogActivity.this.setResult(-1, intent);
                            PostOptionsDialogActivity.this.finish();
                            break;
                        case -1:
                            intent.setAction("action_delete");
                            PostOptionsDialogActivity.this.setResult(-1, intent);
                            PostOptionsDialogActivity.this.finish();
                            break;
                    }
                case 3:
                    switch (which) {
                        case -1:
                            intent.putExtra("feed", PostOptionsDialogActivity.this.mFeed);
                            intent.setAction("action_delete_feed_item");
                            PostOptionsDialogActivity.this.setResult(-1, intent);
                            PostOptionsDialogActivity.this.finish();
                            break;
                    }
            }
        }
    };

    @Override // android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityDonut, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.option_list);
        this.mAppController = AppController.getInstance(this);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            this.mReport = b.getBoolean("report", true);
            this.mUnfollow = b.getBoolean("unfollow");
            this.mDelete = b.getBoolean("delete", false);
            this.mPost = (VinePost) b.getParcelable("post");
            this.mVideoPath = b.getString("video_path");
            this.mName = b.getString("name");
            this.mOriginalPost = b.getBoolean("original_post", false);
            this.mPrivate = b.getBoolean("private");
            this.mShareUrl = b.getString("share_url");
            this.mEditCaption = b.getBoolean("allow_edit_caption");
            this.mDeleteFeedItem = b.getBoolean("delete_feed_item", false);
            this.mShowLessUser = b.getBoolean("show_less_user", false);
            this.mFeed = (VineFeed) b.getParcelable("feed");
            if (this.mFeed != null) {
                this.mPost = this.mFeed.coverPost;
            }
            if (ClientFlagsHelper.isCanHidePostsEnabled(this)) {
                this.mHide = b.getBoolean("hide", false);
                this.mUnhide = b.getBoolean("unhide", false);
            } else {
                this.mHide = false;
                this.mUnhide = false;
            }
        }
        Resources res = getResources();
        this.mArrayAdapter = new OptionArrayAdapter(this, R.layout.post_more_row);
        ArrayList<Option> items = new ArrayList<>();
        if (this.mOriginalPost) {
            items.add(new Option(5, res.getString(R.string.original_post)));
        }
        ArrayList<Option> items2 = new ArrayList<>();
        if (this.mPost != null && this.mPost.isShareable() && this.mPost.longform == null) {
            items.add(new Option(7, res.getString(R.string.copy_link)));
            if (ClientFlagsHelper.enableVideoRemix(this) && ClientFlagsHelper.isRecorder2Enabled(this)) {
                this.mAppController.addListener(this.mVideoPathListener);
                this.mPostCachedLocalPath = this.mAppController.getVideoFilePath(new VideoKey(this.mPost.videoUrl));
                items.add(new Option(13, res.getString(R.string.make_video_remix)));
            }
        }
        if (this.mEditCaption) {
            items.add(new Option(8, res.getString(R.string.edit_caption)));
        }
        if (this.mUnfollow) {
            items.add(new Option(1, res.getString(R.string.unfollow)));
        }
        if (this.mShowLessUser) {
            items.add(new Option(12, res.getString(R.string.show_less_user_option)));
        }
        if (this.mReport) {
            items.add(new Option(2, res.getString(R.string.block_or_report)));
        }
        if (this.mHide) {
            items.add(new Option(9, res.getString(R.string.hide_post)));
        }
        if (this.mUnhide) {
            items.add(new Option(10, res.getString(R.string.unhide_post)));
        }
        if (this.mDelete) {
            items.add(new Option(3, res.getString(R.string.delete_post)));
        }
        if (this.mDeleteFeedItem) {
            items.add(new Option(11, res.getString(R.string.delete_feed_item)));
        }
        if (items2.size() > 0) {
            items.add(new Option(6, res.getString(R.string.more_options)));
        }
        this.mItems2 = items2;
        invalidateOptions(items);
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        this.mAppController.removeListener(this.mVideoPathListener);
    }

    private void invalidateOptions(ArrayList<Option> items) {
        this.mArrayAdapter.clear();
        Iterator<Option> it = items.iterator();
        while (it.hasNext()) {
            Option item = it.next();
            this.mArrayAdapter.add(item);
        }
        setListAdapter(this.mArrayAdapter);
        getListView().setChoiceMode(0);
        getListView().setAdapter(getListAdapter());
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
    }

    @Override // co.vine.android.SupportListActivity
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = getIntent();
        int optionId = ((Integer) v.getTag()).intValue();
        switch (optionId) {
            case 1:
                intent.setAction("unfollow");
                setResult(-1, intent);
                finish();
                break;
            case 2:
                intent.putExtra("post_id", this.mPost.postId);
                intent.putExtra("post_user_id", this.mPost.userId);
                intent.putExtra("name", this.mName);
                intent.setAction("block_or_report");
                setResult(-1, intent);
                finish();
                break;
            case 3:
                PromptDialogSupportFragment promptDialogFragment = PromptDialogSupportFragment.newInstance(2);
                promptDialogFragment.setListener(this.mReportDeleteDialogDoneListener);
                if (ClientFlagsHelper.isCanHidePostsEnabled(this)) {
                    promptDialogFragment.setMessage(R.string.delete_confirm_hidden_post);
                } else {
                    promptDialogFragment.setMessage(R.string.delete_confirm);
                }
                promptDialogFragment.setPositiveButton(R.string.delete_post);
                if (this.mHide) {
                    promptDialogFragment.setMessage(R.string.delete_comfirm_hide_option);
                    promptDialogFragment.setPositiveButton(R.string.delete);
                    promptDialogFragment.setNeutralButton(R.string.hide);
                }
                promptDialogFragment.setNegativeButton(R.string.cancel);
                promptDialogFragment.show(getSupportFragmentManager());
                break;
            case 5:
                intent.setAction("action_original_post");
                setResult(-1, intent);
                finish();
                break;
            case 6:
                invalidateOptions(this.mItems2);
                break;
            case 7:
                copyLinkToClipboard();
                finish();
                break;
            case 8:
                intent.putExtra("post", this.mPost);
                intent.putExtra("video_path", this.mVideoPath);
                intent.setAction("edit_caption");
                setResult(-1, intent);
                finish();
                break;
            case 9:
                intent.setAction("action_hide");
                setResult(-1, intent);
                finish();
                break;
            case 10:
                intent.setAction("action_unhide");
                setResult(-1, intent);
                finish();
                break;
            case 11:
                PromptDialogSupportFragment promptDialogFragment2 = PromptDialogSupportFragment.newInstance(3);
                promptDialogFragment2.setListener(this.mReportDeleteDialogDoneListener);
                promptDialogFragment2.setMessage(R.string.delete_feed_confirm);
                promptDialogFragment2.setPositiveButton(R.string.delete_feed_item);
                promptDialogFragment2.setNegativeButton(R.string.cancel);
                promptDialogFragment2.show(getSupportFragmentManager());
                break;
            case 12:
                intent.setAction("action_show_less_user");
                setResult(-1, intent);
                finish();
                break;
            case 13:
                if (this.mPostCachedLocalPath != null) {
                    Intent remixIntent = CameraCaptureActivity.getRemixVideoIntent(this, this.mPostCachedLocalPath, "" + this.mPost.postId);
                    Util.startActionOnRecordingAvailable(this, remixIntent, 0);
                    break;
                }
                break;
        }
    }

    private void copyLinkToClipboard() {
        if (TextUtils.isEmpty(this.mShareUrl)) {
            CrashUtil.logOrThrowInDebug(new RuntimeException("Empty share url for post"));
            return;
        }
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService("clipboard");
        ClipData link = ClipData.newPlainText(this.mShareUrl, this.mShareUrl);
        clipboardManager.setPrimaryClip(link);
        CommonUtil.showCenteredToast(this, R.string.copy_link_confirmation, 0);
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(0, intent);
        finish();
    }

    private static class Option {
        public int id;
        public String text;

        public Option(int id, String text) {
            this.id = id;
            this.text = text;
        }
    }

    public static Intent getMoreIntentForPost(VinePost post, String videoPath, Context context, long myUserId, boolean isFollowing, boolean showOriginalPost) {
        Uri data;
        boolean isMyPost = post.userId == myUserId;
        boolean isForYouFeed = false;
        if ((context instanceof ExploreVideoListActivity) && (data = ((ExploreVideoListActivity) context).getIntent().getData()) != null && data.getPathSegments().contains("suggested")) {
            isForYouFeed = true;
        }
        long currentTime = System.currentTimeMillis();
        return new Intent(context, (Class<?>) PostOptionsDialogActivity.class).putExtra("show_less_user", isForYouFeed).putExtra("private", post.isPrivate()).putExtra("share_url", post.shareUrl).putExtra("post_id", post.postId).putExtra("post", post).putExtra("video_path", videoPath).putExtra("delete", isMyPost).putExtra("hide", isMyPost && !post.hidden).putExtra("unhide", isMyPost && post.hidden).putExtra("unfollow", !isMyPost && isFollowing).putExtra("report", !isMyPost).putExtra("original_post", showOriginalPost).putExtra("post_user_id", post.userId).putExtra("name", post.username).putExtra("allow_edit_caption", currentTime < post.descriptionEditableTill && videoPath != null);
    }

    public static Intent getMoreIntentForFeed(VineFeed feed, Context context, long myUserId) {
        boolean isMySharedFeed = feed.userId == myUserId;
        boolean isPostMine = feed.feedMetadata != null && feed.feedMetadata.profileUserId == myUserId;
        return new Intent(context, (Class<?>) PostOptionsDialogActivity.class).putExtra("feed", feed).putExtra("report", isPostMine ? false : true).putExtra("delete_feed_item", isMySharedFeed);
    }

    public static class Result {
        public final Intent intent;
        public final String request;

        public Result(String request, Intent intent) {
            this.request = request;
            this.intent = intent;
        }
    }

    public static Result processActivityResult(AppController appController, Activity activity, int resultCode, Intent data, FollowScribeActionsLogger logger) {
        String request = null;
        Intent intent = null;
        if (resultCode == -1 && data != null) {
            String action = data.getAction();
            if ("action_report".equals(action)) {
                long postId = data.getLongExtra("post_id", 0L);
                if (postId > 0) {
                    request = appController.reportPost(appController.getActiveSession(), postId);
                }
            } else if ("action_delete".equals(action)) {
                long postId2 = data.getLongExtra("post_id", 0L);
                if (postId2 > 0) {
                    request = appController.deletePost(appController.getActiveSession(), postId2);
                }
            } else if ("action_hide".equals(action)) {
                long postId3 = data.getLongExtra("post_id", 0L);
                if (postId3 > 0) {
                    request = appController.hidePost(appController.getActiveSession(), postId3);
                }
            } else if ("action_unhide".equals(action)) {
                long postId4 = data.getLongExtra("post_id", 0L);
                if (postId4 > 0) {
                    request = appController.unhidePost(appController.getActiveSession(), postId4);
                }
            } else if ("action_original_post".equals(action)) {
                long postId5 = data.getLongExtra("post_id", 0L);
                if (postId5 > 0) {
                    intent = SingleActivity.getIntent(activity, postId5);
                }
            } else if ("unfollow".equals(action)) {
                long postUserId = data.getLongExtra("post_user_id", 0L);
                Components.userInteractionsComponent().unfollowUser(appController, postUserId, false, logger);
            } else if ("block_or_report".equals(action)) {
                long postId6 = data.getLongExtra("post_id", 0L);
                long userId = data.getLongExtra("post_user_id", 0L);
                String username = data.getStringExtra("name");
                Intent i = ReportingActivity.getReportPostIntent(activity, postId6, userId, username);
                activity.startActivityForResult(i, 30);
            } else if ("action_show_less_user".equals(action)) {
                Components.recommendationsComponent().userFeedback(appController, data.getLongExtra("post_user_id", 0L), "negative");
                intent = new Intent();
                intent.putExtra("show_less_user", true);
            } else if ("edit_caption".equals(action)) {
                VinePost post = (VinePost) data.getParcelableExtra("post");
                String videoPath = data.getStringExtra("video_path");
                Intent i2 = VideoEditTextActivity.getIntent(activity, videoPath, post, 0);
                activity.startActivityForResult(i2, 40);
                activity.overridePendingTransition(R.anim.slide_in_from_bottom, R.anim.slide_out_to_bottom);
            } else if ("action_delete_feed_item".equals(action)) {
                VineFeed feed = (VineFeed) data.getParcelableExtra("feed");
                Components.feedActionsComponent().deleteFeed(appController, feed.feedId);
            }
        }
        return new Result(request, intent);
    }

    private class OptionArrayAdapter extends ArrayAdapter<Option> {
        public OptionArrayAdapter(Context context, int layout) {
            super(context, layout);
        }

        @Override // android.widget.ArrayAdapter, android.widget.Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = super.getView(position, convertView, parent);
            Option option = getItem(position);
            TextView title = (TextView) v.findViewById(R.id.item);
            title.setTextColor(PostOptionsDialogActivity.this.getResources().getColor(R.color.soft_black));
            title.setText(option.text);
            v.setTag(Integer.valueOf(option.id));
            return v;
        }
    }
}
