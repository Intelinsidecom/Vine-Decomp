package co.vine.android;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import co.vine.android.api.VineEntity;
import co.vine.android.api.VinePost;
import co.vine.android.api.VineRepost;
import co.vine.android.cache.image.ImageKey;
import co.vine.android.embed.player.VideoViewInterface;
import co.vine.android.model.impl.VineModelFactory;
import co.vine.android.player.OnSingleVideoClickedListener;
import co.vine.android.player.SdkVideoView;
import co.vine.android.service.components.Components;
import co.vine.android.service.components.postactions.PostActionsListener;
import co.vine.android.share.widgets.FakeActionBar;
import co.vine.android.util.CommonUtil;
import co.vine.android.util.SystemUtil;
import co.vine.android.util.Util;
import co.vine.android.util.ViewUtil;
import co.vine.android.views.SimpleTextWatcher;
import co.vine.android.widget.PopupEditText;
import co.vine.android.widget.Typefaces;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public final class VideoEditTextFragment extends BaseAdapterFragment {
    private int mActionButtonTextColor;
    private int mActionButtonTextTooLongColor;
    private PopupEditText mEdit;
    private PostDescriptionUpdateListener mEditTextListener;
    EntityUpdateListener mEntityUpdateListener;
    private FakeActionBar mFakeActionBar;
    private Button mFakeActionBarActionButton;
    private ViewGroup mFakeActionBarActionView;
    private boolean mFakeActionBarAdded = false;
    private ImageButton mFakeActionBarBackArrow;
    private int mMode;
    private VinePost mPost;
    private TextView mRemainingCharacterCount;
    private int mRemainingCharacterCountTextColor;
    private int mRemainingCharacterCountTextTooLongColor;
    private String mStartingText;
    private ImageView mThumbnail;
    private String mVideoPath;
    private SdkVideoView mVideoPlayer;
    private View mVideoPlayerDimOverlay;

    @Override // co.vine.android.BaseAdapterFragment, co.vine.android.BaseControllerFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        this.mPost = (VinePost) args.getParcelable("post");
        this.mVideoPath = args.getString("video_path");
        this.mMode = args.getInt("mode");
        this.mStartingText = this.mPost.description;
        this.mEditTextListener = new PostDescriptionUpdateListener();
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) throws IllegalStateException, Resources.NotFoundException {
        View view = inflater.inflate(R.layout.video_edit_text, container, false);
        initializeFakeActionBar(inflater, view);
        this.mEdit = (PopupEditText) view.findViewById(R.id.preview_text);
        this.mEntityUpdateListener = new EntityUpdateListener(getActivity(), this.mAppController, this, this.mEdit);
        this.mEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() { // from class: co.vine.android.VideoEditTextFragment.1
            @Override // android.view.View.OnFocusChangeListener
            public void onFocusChange(View view2, boolean hasFocus) {
                if (!hasFocus) {
                    CommonUtil.setSoftKeyboardVisibility(VideoEditTextFragment.this.getActivity(), VideoEditTextFragment.this.mEdit, false);
                    VideoEditTextFragment.this.mVideoPlayerDimOverlay.setVisibility(8);
                    VideoEditTextFragment.this.ensureActionBarState();
                } else {
                    VideoEditTextFragment.this.mVideoPlayerDimOverlay.setVisibility(0);
                    VideoEditTextFragment.this.ensureActionBarState();
                }
            }
        });
        this.mEdit.addTextChangedListener(new SimpleTextWatcher() { // from class: co.vine.android.VideoEditTextFragment.2
            @Override // co.vine.android.views.SimpleTextWatcher, android.text.TextWatcher
            public void afterTextChanged(Editable s) {
                VideoEditTextFragment.this.ensureActionBarState();
            }
        });
        this.mVideoPlayer = (SdkVideoView) view.findViewById(R.id.sdkVideoView);
        this.mVideoPlayer.setVisibility(0);
        this.mVideoPlayer.setAutoPlayOnPrepared(true);
        this.mVideoPlayer.setKeepScreenOn(true);
        this.mVideoPlayer.setMute(true);
        this.mVideoPlayerDimOverlay = view.findViewById(R.id.dim_overlay);
        this.mVideoPlayerDimOverlay.setVisibility(8);
        this.mEdit.requestFocus();
        Util.setSoftKeyboardVisibility(getActivity(), this.mEdit, true);
        this.mThumbnail = (ImageView) view.findViewById(R.id.thumb);
        Point size = SystemUtil.getDisplaySize(getActivity());
        View videoPlayerContainer = view.findViewById(R.id.videoViewContainer);
        this.mVideoPlayer.setSize(size.x, size.x);
        this.mVideoPlayerDimOverlay.getLayoutParams().width = size.x;
        this.mVideoPlayerDimOverlay.getLayoutParams().height = size.x;
        this.mThumbnail.getLayoutParams().width = size.x;
        this.mThumbnail.getLayoutParams().height = size.x;
        videoPlayerContainer.setOnClickListener(new OnSingleVideoClickedListener(this.mVideoPlayer));
        this.mVideoPlayer.setSurfaceUpdatedListener(new VideoViewInterface.SurfaceUpdatedListener() { // from class: co.vine.android.VideoEditTextFragment.3
            @Override // co.vine.android.embed.player.VideoViewInterface.SurfaceUpdatedListener
            public void onSurfaceUpdated() {
                VideoEditTextFragment.this.mThumbnail.setVisibility(4);
            }
        });
        this.mVideoPlayer.setOnCompletionListener(new VideoViewInterface.OnCompletionListener() { // from class: co.vine.android.VideoEditTextFragment.4
            @Override // co.vine.android.embed.player.VideoViewInterface.OnCompletionListener
            public void onCompletion(VideoViewInterface view2) throws IllegalStateException {
                VideoEditTextFragment.this.mVideoPlayer.setVideoPath(VideoEditTextFragment.this.mVideoPath);
                VideoEditTextFragment.this.mVideoPlayer.setLooping(true);
            }
        });
        if (this.mMode == 0) {
            this.mEdit.setTextWithEntities(this.mPost.description, this.mPost.entities);
        } else if (this.mMode == 1) {
            this.mEdit.setHint(getActivity().getString(R.string.comments_hint));
        }
        addFakeActionBar((ViewGroup) view);
        ImageKey imageKey = new ImageKey(this.mPost.thumbnailUrl);
        Bitmap bmp = this.mAppController.getPhotoBitmap(imageKey);
        if (bmp != null) {
            this.mThumbnail.setImageBitmap(bmp);
        }
        return view;
    }

    private ArrayList<VineEntity> getModifiedEntitiesForModel(ArrayList<VineEntity> entities) {
        if (entities == null) {
            return new ArrayList<>();
        }
        Iterator<VineEntity> it = entities.iterator();
        while (it.hasNext()) {
            VineEntity e = it.next();
            if ("tag".equalsIgnoreCase(e.type) && !TextUtils.isEmpty(e.title) && e.title.charAt(0) == '#') {
                e.title = e.title.substring(1, e.title.length());
            }
        }
        return entities;
    }

    private void addFakeActionBar(ViewGroup view) {
        if (!this.mFakeActionBarAdded) {
            view.addView(this.mFakeActionBar, new ViewGroup.LayoutParams(-1, -2));
            this.mFakeActionBarAdded = true;
        }
    }

    @Override // android.support.v4.app.Fragment
    public void onActivityCreated(Bundle savedInstanceState) throws IllegalStateException {
        super.onActivityCreated(savedInstanceState);
        this.mVideoPlayer.setVideoPath(this.mVideoPath);
        this.mVideoPlayer.setLooping(true);
    }

    @Override // co.vine.android.BaseAdapterFragment, co.vine.android.BaseControllerFragment, co.vine.android.BaseFragment, android.support.v4.app.Fragment
    public void onResume() throws IllegalStateException {
        super.onResume();
        if (this.mVideoPlayer != null) {
            this.mVideoPlayer.resume();
        }
        Components.suggestionsComponent().addListener(this.mEntityUpdateListener.getSuggestionsActionListener());
        Components.postActionsComponent().addListener(this.mEditTextListener);
    }

    @Override // co.vine.android.BaseAdapterFragment, co.vine.android.BaseControllerFragment, android.support.v4.app.Fragment
    public void onPause() throws IllegalStateException {
        super.onPause();
        this.mVideoPlayer.pause();
        Components.suggestionsComponent().removeListener(this.mEntityUpdateListener.getSuggestionsActionListener());
        Components.postActionsComponent().removeListener(this.mEditTextListener);
    }

    @Override // co.vine.android.BaseAdapterFragment
    protected void hideProgress(int fetchType) {
    }

    @Override // co.vine.android.BaseAdapterFragment
    protected void showProgress(int fetchType) {
    }

    public static VideoEditTextFragment newInstance(String videoPath, VinePost post, int mode) {
        Bundle b = new Bundle();
        b.putParcelable("post", post);
        b.putString("video_path", videoPath);
        b.putInt("mode", mode);
        VideoEditTextFragment fragment = new VideoEditTextFragment();
        fragment.setArguments(b);
        return fragment;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void ensureActionBarState() {
        int i = R.string.done;
        Resources resources = getResources();
        if (this.mEdit.hasFocus()) {
            if (this.mMode == 0) {
                this.mFakeActionBar.setLabelText(resources.getString(R.string.caption_label));
                Button button = this.mFakeActionBarActionButton;
                if (TextUtils.isEmpty(this.mPost.description)) {
                    i = R.string.add;
                }
                button.setText(i);
            } else if (this.mMode == 1) {
                this.mFakeActionBar.setLabelText(resources.getString(R.string.comment));
                this.mFakeActionBarActionButton.setText(R.string.done);
            }
            String text = this.mEdit.getText().toString();
            this.mRemainingCharacterCount.setText(String.valueOf(140 - text.length()));
            if (text.length() <= 140) {
                this.mFakeActionBarActionButton.setEnabled(true);
                this.mFakeActionBarActionButton.setTextColor(this.mActionButtonTextColor);
                this.mRemainingCharacterCount.setTextColor(this.mRemainingCharacterCountTextColor);
                return;
            } else {
                this.mFakeActionBarActionButton.setEnabled(false);
                this.mFakeActionBarActionButton.setTextColor(this.mActionButtonTextTooLongColor);
                this.mRemainingCharacterCount.setTextColor(this.mRemainingCharacterCountTextTooLongColor);
                return;
            }
        }
        this.mFakeActionBar.setLabelText(resources.getString(R.string.preview_title));
        ViewUtil.enableAndShow(this.mFakeActionBarActionView);
        this.mRemainingCharacterCount.setText("");
        this.mFakeActionBarActionButton.setText(getResources().getString(R.string.next));
        this.mFakeActionBarActionButton.setTextColor(this.mActionButtonTextColor);
        this.mFakeActionBarActionButton.setEnabled(true);
    }

    private void initializeFakeActionBar(LayoutInflater inflater, View contentView) throws Resources.NotFoundException {
        Activity activity = getActivity();
        this.mFakeActionBar = (FakeActionBar) inflater.inflate(R.layout.screen_fake_action_bar_standalone, (ViewGroup) null);
        this.mFakeActionBarBackArrow = (ImageButton) this.mFakeActionBar.inflateBackView(R.layout.fake_action_bar_down_arrow);
        this.mFakeActionBarActionView = (ViewGroup) this.mFakeActionBar.inflateActionView(R.layout.recording_preview_fake_action_bar_next);
        this.mFakeActionBar.setBackView(this.mFakeActionBarBackArrow);
        this.mFakeActionBar.setActionView(this.mFakeActionBarActionView);
        this.mFakeActionBar.setOnActionListener(new FakeActionBar.OnActionListener() { // from class: co.vine.android.VideoEditTextFragment.5
            @Override // co.vine.android.share.widgets.FakeActionBar.OnActionListener
            public void onBackPressed() {
                Activity activity2 = VideoEditTextFragment.this.getActivity();
                activity2.finish();
                activity2.overridePendingTransition(R.anim.slide_in_from_top, R.anim.slide_out_to_bottom);
            }
        });
        this.mRemainingCharacterCount = (TextView) this.mFakeActionBarActionView.findViewById(R.id.characters_remaining);
        this.mFakeActionBarActionButton = (Button) this.mFakeActionBarActionView.findViewById(R.id.action);
        this.mFakeActionBarActionButton.setTypeface(Typefaces.get(activity).getContentTypeface(Typefaces.get(activity).mediumContentBold.getStyle(), 3));
        setActionBarActionViewBackgroundColor(this.mFakeActionBarActionButton, getResources().getColor(R.color.vine_green));
        this.mFakeActionBarActionButton.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.VideoEditTextFragment.6
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (VideoEditTextFragment.this.mMode == 0) {
                    VideoEditTextFragment.this.saveCaptionAndFinish();
                } else if (VideoEditTextFragment.this.mMode == 1) {
                    VideoEditTextFragment.this.postCommentAndFinish();
                }
            }
        });
        this.mFakeActionBar.setTopOfWindowLayoutParams();
        this.mActionButtonTextColor = -1;
        this.mActionButtonTextTooLongColor = getResources().getColor(R.color.white_fifty_percent);
        this.mRemainingCharacterCountTextColor = getResources().getColor(R.color.white_fifty_percent);
        this.mRemainingCharacterCountTextTooLongColor = -1;
    }

    private void setActionBarActionViewBackgroundColor(View actionView, int color) throws Resources.NotFoundException {
        Drawable backgroundDrawable = actionView.getBackground();
        if (backgroundDrawable instanceof GradientDrawable) {
            GradientDrawable actionBarActionViewDrawable = (GradientDrawable) actionView.getBackground();
            actionBarActionViewDrawable.setColor(color);
            float strokeWidth = getResources().getDimension(R.dimen.vm_recipient_view_stoke_width);
            actionBarActionViewDrawable.setStroke((int) strokeWidth, color);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void postCommentAndFinish() {
        this.mPost.description = this.mEdit.getText().toString();
        this.mPost.entities = getModifiedEntitiesForModel(this.mEdit.getEntities());
        postComment();
        getActivity().finish();
    }

    private void postComment() {
        if (this.mEdit != null && this.mEdit.length() > 0 && this.mEdit.length() <= 140) {
            ProgressDialog dialog = new ProgressDialog(getActivity(), R.style.ProgressDialogTheme);
            dialog.setMessage(getString(R.string.comments_posting));
            dialog.setProgressStyle(0);
            dialog.show();
            this.mProgressDialog = dialog;
            addRequest(this.mAppController.postComment(this.mPost.getId(), this.mPost.getVineRepostRepostId(), this.mAppController.getActiveSession(), this.mEdit.getText().toString(), this.mEdit.getEntities()));
            this.mEdit.getText().clear();
            Util.setSoftKeyboardVisibility(getActivity(), this.mEdit, false);
            this.mEdit.clearFocus();
            return;
        }
        if (this.mEdit != null && this.mEdit.length() > 140) {
            Util.showDefaultToast(getActivity(), R.string.comment_length_exceeded);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveCaptionAndFinish() {
        this.mPost.entities = this.mEdit.getEntities();
        this.mPost.description = this.mEdit.getText().toString();
        if (!this.mPost.description.equals(this.mStartingText)) {
            Components.postActionsComponent().editCaption(this.mAppController, this.mPost.postId, this.mPost.description, this.mPost.entities);
        }
    }

    private class PostDescriptionUpdateListener implements PostActionsListener {
        private PostDescriptionUpdateListener() {
        }

        @Override // co.vine.android.service.components.postactions.PostActionsListener
        public void onPostEditCaption(String reqId, int statusCode, String reasonPhrase) {
            Activity activity = VideoEditTextFragment.this.getActivity();
            if (activity != null) {
                if (statusCode == 200) {
                    VineModelFactory.getMutableModelInstance().getMutableTimelineItemModel().updateTimelineItem(VideoEditTextFragment.this.mPost.getId(), VideoEditTextFragment.this.mPost);
                    Intent i = new Intent();
                    i.putExtra("post", VideoEditTextFragment.this.mPost);
                    activity.setResult(-1, i);
                    activity.finish();
                    activity.overridePendingTransition(0, R.anim.slide_out_to_bottom);
                    return;
                }
                String errorMessage = activity.getResources().getString(R.string.edit_caption_error) + reasonPhrase;
                Toast.makeText(activity, errorMessage, 0).show();
            }
        }

        @Override // co.vine.android.service.components.postactions.PostActionsListener
        public void onLikePost(String reqId, int statusCode, String reasonPhrase, long postId) {
        }

        @Override // co.vine.android.service.components.postactions.PostActionsListener
        public void onUnlikePost(String reqId, int statusCode, String reasonPhrase, long postId) {
        }

        @Override // co.vine.android.service.components.postactions.PostActionsListener
        public void onRevine(String reqId, int statusCode, String reasonPhrase, long postId, VineRepost repost) {
        }

        @Override // co.vine.android.service.components.postactions.PostActionsListener
        public void onUnrevine(String reqId, int statusCode, String reasonPhrase, long postId) {
        }
    }
}
