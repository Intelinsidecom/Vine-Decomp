package co.vine.android;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.ColorFilter;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import co.vine.android.AbstractRecordingActivity;
import co.vine.android.ComposeTokenizer;
import co.vine.android.api.VineEntity;
import co.vine.android.api.VineRecipient;
import co.vine.android.api.VineSource;
import co.vine.android.api.VineTypeAhead;
import co.vine.android.api.VineUser;
import co.vine.android.cache.image.ImageKey;
import co.vine.android.cache.image.UrlImage;
import co.vine.android.client.AppController;
import co.vine.android.client.AppSessionListener;
import co.vine.android.drawable.RecyclableBitmapDrawable;
import co.vine.android.embed.player.VideoViewInterface;
import co.vine.android.model.ModelEvents;
import co.vine.android.model.TagModel;
import co.vine.android.model.TimelineModel;
import co.vine.android.model.VineTag;
import co.vine.android.model.impl.TimelineDetails;
import co.vine.android.model.impl.VineModelFactory;
import co.vine.android.player.OnSingleVideoClickedListener;
import co.vine.android.player.SdkVideoView;
import co.vine.android.player.StaticSizeVideoView;
import co.vine.android.provider.VineProviderHelper;
import co.vine.android.recorder.RecordConfigUtils;
import co.vine.android.recorder.RecordingFile;
import co.vine.android.recordingui.CameraCaptureActivity;
import co.vine.android.recordingui.RecordStateHolder;
import co.vine.android.service.VineUploadService;
import co.vine.android.service.components.Components;
import co.vine.android.service.components.suggestions.SuggestionsActionListener;
import co.vine.android.service.components.suggestions.SuggestionsComponent;
import co.vine.android.share.activities.PostShareActivity;
import co.vine.android.share.activities.PostShareParameters;
import co.vine.android.share.widgets.FakeActionBar;
import co.vine.android.util.BuildUtil;
import co.vine.android.util.CommonUtil;
import co.vine.android.util.CrashUtil;
import co.vine.android.util.SystemUtil;
import co.vine.android.util.UploadManager;
import co.vine.android.util.Util;
import co.vine.android.util.ViewUtil;
import co.vine.android.util.analytics.FlurryUtils;
import co.vine.android.views.SimpleTextWatcher;
import co.vine.android.widget.PopupEditText;
import co.vine.android.widget.Typefaces;
import co.vine.android.widget.TypefacesEditText;
import co.vine.android.widget.TypefacesTextView;
import co.vine.android.widgets.PromptDialogSupportFragment;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.parceler.Parcels;

/* loaded from: classes.dex */
public class RecordingPreviewFragment extends BaseControllerFragment {
    private int mActionButtonTextColor;
    private int mActionButtonTextTooLongColor;
    private ViewGroup mBelowVideoContainer;
    private String mCachedCaption;
    private ArrayList<VineEntity> mCachedCaptionEntities;
    private int mColor;
    private long mConversationRowId;
    private long mDirectUserId;
    private FakeActionBar mFakeActionBar;
    private Button mFakeActionBarActionButton;
    private ViewGroup mFakeActionBarActionView;
    private ImageButton mFakeActionBarBackArrow;
    private View mFakeActionBarPlaceholder;
    private RecordingFile mFinalFile;
    private boolean mIsFromSony;
    private boolean mIsMessaging;
    private ViewGroup mLocationContainer;
    private TypefacesTextView mLocationLabel;
    private ViewGroup mMessagingDetailsContainer;
    private TypefacesEditText mMessagingEditText;
    private LifetimeSafeModelListener mModelListener;
    private ViewGroup mPreviewActionsContainer;
    private PopupEditText mPreviewCaption;
    private ViewGroup mPreviewDetailsContainer;
    private String mRecipientUsername;
    private TextView mRemainingCharacterCount;
    private int mRemainingCharacterCountTextColor;
    private int mRemainingCharacterCountTextTooLongColor;
    private RecordingPreviewSuggestionsListener mSuggestionsListener;
    private SimpleTagAdapter mTagsAdapter;
    private String mThumbnailPath;
    private BaseAdapter mTypeaheadAdapter;
    private String mUploadPath;
    private SimpleUserAdapter mUsersAdapter;
    private String mVenueId;
    private String mVenueName;
    private String mVideoPath;
    private SdkVideoView mVideoPlayer;
    private View mVideoPlayerDimOverlay;
    private ArrayList<VineSource> sources;
    private boolean mFakeActionBarAdded = false;
    private final PromptDialogSupportFragment.OnDialogDoneListener mLoginRequiredDialogDoneListener = new PromptDialogSupportFragment.OnDialogDoneListener() { // from class: co.vine.android.RecordingPreviewFragment.1
        @Override // co.vine.android.widgets.PromptDialogFragment.OnDialogDoneListener
        public void onDialogDone(DialogInterface dialog, int id, int which) {
            switch (which) {
                case -1:
                    Activity activity = RecordingPreviewFragment.this.getActivity();
                    Intent i = new Intent(activity, (Class<?>) SonyStartActivity.class);
                    i.putExtra("login_request_start_activity", true);
                    activity.startActivityForResult(i, 12345);
                    break;
            }
        }
    };

    @Override // co.vine.android.BaseControllerFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        this.mVideoPath = args.getString("video_path");
        this.mThumbnailPath = args.getString("pic_path");
        this.mUploadPath = args.getString("upload_path");
        this.mIsMessaging = args.getBoolean("messaging");
        this.mConversationRowId = args.getLong("conversation_row_id", -1L);
        this.mRecipientUsername = args.getString("recipient_username");
        this.mDirectUserId = args.getLong("direct_user_id", -1L);
        this.mIsFromSony = args.getBoolean("f_s");
        this.mColor = args.getInt("color", -1);
        this.sources = args.getParcelableArrayList("sources");
        if (this.mColor == Settings.DEFAULT_PROFILE_COLOR || this.mColor <= 0) {
            this.mColor = 16777215 & getResources().getColor(R.color.vine_green);
        }
        this.mColor |= ViewCompat.MEASURED_STATE_MASK;
        setAppSessionListener(new RecordingPreviewSessionListener());
        this.mUsersAdapter = new SimpleUserAdapter(this.mAppController);
        this.mTagsAdapter = new SimpleTagAdapter();
        this.mTypeaheadAdapter = this.mUsersAdapter;
        this.mSuggestionsListener = new RecordingPreviewSuggestionsListener();
        this.mModelListener = new LifetimeSafeModelListener(this, new ModelListener());
        VineModelFactory.getModelInstance().getModelEvents().addListener(this.mModelListener);
    }

    @Override // android.support.v4.app.Fragment
    public void onActivityCreated(Bundle savedInstanceState) throws IllegalStateException {
        super.onActivityCreated(savedInstanceState);
        RecordConfigUtils.deletePreProcess(getActivity().getFilesDir());
        this.mVideoPlayer.setVideoPath(this.mVideoPath);
        CrashUtil.log("Preview: {}", this.mVideoPath);
        PackageManager pm = getActivity().getPackageManager();
        boolean hasLocation = pm.hasSystemFeature("android.hardware.location");
        if (!hasLocation) {
            this.mLocationContainer.setVisibility(8);
        }
    }

    @Override // android.support.v4.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("caption", this.mPreviewCaption.getText().toString());
        outState.putParcelableArrayList("caption_entities", this.mPreviewCaption.getEntities());
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) throws Resources.NotFoundException {
        View view = inflater.inflate(R.layout.preview, container, false);
        View rootView = view.findViewById(R.id.root);
        PostShareParameters state = null;
        if (getActivity() instanceof RecordStateHolder) {
            state = ((RecordStateHolder) getActivity()).getPostShareParameters();
        }
        initializeFakeActionBar(inflater, view);
        this.mBelowVideoContainer = (ViewGroup) view.findViewById(R.id.below_video_container);
        this.mMessagingDetailsContainer = (ViewGroup) inflater.inflate(R.layout.messaging_preview_details, this.mBelowVideoContainer, false);
        this.mMessagingEditText = (TypefacesEditText) this.mMessagingDetailsContainer.findViewById(R.id.messaging_preview_caption);
        this.mPreviewDetailsContainer = (ViewGroup) inflater.inflate(R.layout.post_preview_details, this.mBelowVideoContainer, false);
        this.mLocationLabel = (TypefacesTextView) this.mPreviewDetailsContainer.findViewById(R.id.selected_location);
        if (state != null && state.venueName != null && state.venueId != null) {
            this.mVenueId = state.venueId;
            this.mVenueName = state.venueName;
            this.mLocationLabel.setText(this.mVenueName);
        }
        this.mPreviewCaption = (PopupEditText) this.mPreviewDetailsContainer.findViewById(R.id.preview_text);
        this.mPreviewCaption.setHorizontallyScrolling(false);
        this.mPreviewCaption.setLines(3);
        this.mMessagingEditText.setHorizontallyScrolling(false);
        this.mMessagingEditText.setLines(3);
        this.mLocationContainer = (ViewGroup) this.mPreviewDetailsContainer.findViewById(R.id.location_container);
        this.mLocationContainer.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.RecordingPreviewFragment.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                Activity activity = RecordingPreviewFragment.this.getActivity();
                Intent locationSearchIntent = new Intent(activity, (Class<?>) LocationSearchActivity.class);
                activity.startActivityForResult(locationSearchIntent, 1995);
            }
        });
        this.mLocationContainer.setOnLongClickListener(new View.OnLongClickListener() { // from class: co.vine.android.RecordingPreviewFragment.3
            @Override // android.view.View.OnLongClickListener
            public boolean onLongClick(View view2) {
                RecordingPreviewFragment.this.mVenueId = null;
                RecordingPreviewFragment.this.mLocationLabel.setText((CharSequence) null);
                return true;
            }
        });
        MultiAutoCompleteTextView.Tokenizer tokenizer = new ComposeTokenizer(new TokenListener());
        this.mPreviewActionsContainer = (ViewGroup) view.findViewById(R.id.preview_actions_container);
        this.mPreviewCaption.setAdapter(this.mUsersAdapter);
        this.mPreviewCaption.setTokenizer(tokenizer, null, SuggestionsComponent.getTypeaheadThrottle());
        this.mPreviewCaption.setPopupEditTextListener(new EditTextListener());
        this.mPreviewCaption.setOnBackPressedListener(new PopupEditText.OnBackPressedListener() { // from class: co.vine.android.RecordingPreviewFragment.4
            @Override // co.vine.android.widget.PopupEditText.OnBackPressedListener
            public boolean onBackPressed() {
                RecordingPreviewFragment.this.mPreviewCaption.setText(RecordingPreviewFragment.this.mCachedCaption, RecordingPreviewFragment.this.mCachedCaptionEntities);
                RecordingPreviewFragment.this.mPreviewCaption.clearFocus();
                return false;
            }
        });
        this.mPreviewCaption.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: co.vine.android.RecordingPreviewFragment.5
            @Override // android.widget.TextView.OnEditorActionListener
            public boolean onEditorAction(TextView view2, int actionId, KeyEvent event) {
                if (actionId != 6) {
                    return false;
                }
                String previewCaption = RecordingPreviewFragment.this.mPreviewCaption.getText().toString();
                ArrayList<VineEntity> previewCaptionEntities = RecordingPreviewFragment.this.mPreviewCaption.getEntities();
                if (previewCaption.length() <= 110) {
                    RecordingPreviewFragment.this.mCachedCaption = previewCaption;
                    RecordingPreviewFragment.this.mCachedCaptionEntities = previewCaptionEntities;
                    RecordingPreviewFragment.this.mPreviewCaption.clearFocus();
                }
                return true;
            }
        });
        this.mPreviewCaption.setOnFocusChangeListener(new View.OnFocusChangeListener() { // from class: co.vine.android.RecordingPreviewFragment.6
            @Override // android.view.View.OnFocusChangeListener
            public void onFocusChange(View view2, boolean hasFocus) {
                if (!hasFocus) {
                    CommonUtil.setSoftKeyboardVisibility(RecordingPreviewFragment.this.getActivity(), RecordingPreviewFragment.this.mPreviewCaption, false);
                    RecordingPreviewFragment.this.mVideoPlayerDimOverlay.setVisibility(8);
                    ViewUtil.enableAndShow(RecordingPreviewFragment.this.mPreviewActionsContainer, RecordingPreviewFragment.this.mLocationContainer, RecordingPreviewFragment.this.mFakeActionBarPlaceholder);
                    RecordingPreviewFragment.this.ensureActionBarState();
                    return;
                }
                RecordingPreviewFragment.this.mVideoPlayerDimOverlay.setVisibility(0);
                ViewUtil.disableAndHide(RecordingPreviewFragment.this.mPreviewActionsContainer, RecordingPreviewFragment.this.mLocationContainer, RecordingPreviewFragment.this.mFakeActionBarPlaceholder);
                RecordingPreviewFragment.this.ensureActionBarState();
            }
        });
        this.mPreviewCaption.addTextChangedListener(new SimpleTextWatcher() { // from class: co.vine.android.RecordingPreviewFragment.7
            @Override // co.vine.android.views.SimpleTextWatcher, android.text.TextWatcher
            public void afterTextChanged(Editable s) {
                RecordingPreviewFragment.this.ensureActionBarState();
            }
        });
        if (state != null && state.caption != null) {
            this.mCachedCaption = state.caption;
            this.mCachedCaptionEntities = state.captionEntities;
            this.mPreviewCaption.setText(this.mCachedCaption, this.mCachedCaptionEntities);
        }
        if (savedInstanceState != null) {
            this.mCachedCaption = savedInstanceState.getString("caption", "");
            this.mCachedCaptionEntities = savedInstanceState.getParcelableArrayList("caption_entities");
            this.mPreviewCaption.setText(this.mCachedCaption, this.mCachedCaptionEntities);
        }
        ensureActionBarState();
        RelativeLayout previewEditButton = (RelativeLayout) view.findViewById(R.id.preview_edit);
        if (this.mIsMessaging) {
            ViewUtil.addToViewGroup(this.mMessagingDetailsContainer, this.mBelowVideoContainer);
            this.mPreviewDetailsContainer.setVisibility(8);
            this.mMessagingDetailsContainer.setVisibility(0);
            this.mFakeActionBar.setLabelStyle(R.style.VineRecordingPreviewMessagingActionBarLabel, 0, 3);
            int profileBackgroundColor = getProfileBackgroundColor();
            int actionButtonColor = profileBackgroundColor;
            if (profileBackgroundColor == Settings.DEFAULT_PROFILE_COLOR || profileBackgroundColor == (Settings.DEFAULT_PROFILE_COLOR | ViewCompat.MEASURED_STATE_MASK)) {
                actionButtonColor = getResources().getColor(R.color.vine_green);
            }
            setActionBarActionViewBackgroundColor(this.mFakeActionBarActionButton, actionButtonColor);
            if (!TextUtils.isEmpty(this.mRecipientUsername)) {
                this.mFakeActionBar.setLabelText(this.mRecipientUsername);
            }
            previewEditButton.setVisibility(8);
        } else {
            rootView.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
            ViewUtil.addToViewGroup(this.mPreviewDetailsContainer, this.mBelowVideoContainer);
            this.mPreviewDetailsContainer.setVisibility(0);
            this.mMessagingDetailsContainer.setVisibility(8);
            previewEditButton.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.RecordingPreviewFragment.8
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    RecordingPreviewFragment.this.previewToRecord(RecordingPreviewFragment.this.getActivity(), true);
                }
            });
            ViewUtil.enableAndShow(this.mPreviewActionsContainer);
            this.mFakeActionBar.setLabelStyle(R.style.VineRecordingPreviewPostingActionBarLabel, 0, 3);
            setActionBarActionViewBackgroundColor(this.mFakeActionBarActionButton, getResources().getColor(R.color.vine_green));
            this.mFakeActionBarBackArrow.clearColorFilter();
            this.mFakeActionBar.setLabelText(getString(R.string.preview_title));
        }
        this.mVideoPlayer = (SdkVideoView) view.findViewById(R.id.sdkVideoView);
        this.mVideoPlayer.setVisibility(0);
        this.mVideoPlayer.setAutoPlayOnPrepared(true);
        this.mVideoPlayer.setKeepScreenOn(true);
        this.mVideoPlayerDimOverlay = view.findViewById(R.id.dim_overlay);
        this.mVideoPlayerDimOverlay.setVisibility(8);
        Point size = SystemUtil.getDisplaySize(getActivity());
        View videoPlayerContainer = view.findViewById(R.id.videoViewContainer);
        ((StaticSizeVideoView) this.mVideoPlayer).setSize(size.x, size.x);
        this.mVideoPlayerDimOverlay.getLayoutParams().width = size.x;
        this.mVideoPlayerDimOverlay.getLayoutParams().height = size.x;
        videoPlayerContainer.setOnClickListener(new OnSingleVideoClickedListener(this.mVideoPlayer));
        this.mVideoPlayer.setOnCompletionListener(new VideoViewInterface.OnCompletionListener() { // from class: co.vine.android.RecordingPreviewFragment.9
            @Override // co.vine.android.embed.player.VideoViewInterface.OnCompletionListener
            public void onCompletion(VideoViewInterface view2) throws IllegalStateException {
                RecordingPreviewFragment.this.mVideoPlayer.setVideoPath(RecordingPreviewFragment.this.mVideoPath);
            }
        });
        addFakeActionBar((ViewGroup) view);
        return view;
    }

    private void addFakeActionBar(ViewGroup view) {
        if (!this.mFakeActionBarAdded) {
            view.addView(this.mFakeActionBar, new ViewGroup.LayoutParams(-1, -2));
            this.mFakeActionBarAdded = true;
        }
    }

    @Override // co.vine.android.BaseControllerFragment, co.vine.android.BaseFragment, android.support.v4.app.Fragment
    public void onResume() throws IllegalStateException {
        super.onResume();
        if (this.mVideoPlayer.isPaused()) {
            this.mVideoPlayer.start();
        }
        Components.suggestionsComponent().addListener(this.mSuggestionsListener);
    }

    @Override // co.vine.android.BaseControllerFragment, android.support.v4.app.Fragment
    public void onPause() throws IllegalStateException {
        super.onPause();
        if (this.mVideoPlayer.canPause()) {
            this.mVideoPlayer.pause();
        }
        Components.suggestionsComponent().removeListener(this.mSuggestionsListener);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void previewToRecord(Activity activity, boolean startWithEdit) {
        PostShareParameters newState;
        if (activity != 0 && (activity instanceof RecordStateHolder)) {
            PostShareParameters state = ((RecordStateHolder) activity).getPostShareParameters();
            if (state != null) {
                newState = new PostShareParameters(this.mCachedCaption, this.mCachedCaptionEntities, this.mVenueName, this.mVenueId, state.channel, state.recipients, state.shareToVine, state.shareToTwitter, state.shareToFacebook, state.shareToTumblr);
            } else {
                newState = new PostShareParameters(this.mCachedCaption, this.mCachedCaptionEntities, this.mVenueName, this.mVenueId);
            }
            ((RecordStateHolder) activity).setPostShareParameters(newState);
            if (activity instanceof AbstractRecordingActivity) {
                try {
                    ((AbstractRecordingActivity) activity).toRecord(false, startWithEdit, this.mFinalFile);
                } catch (AbstractRecordingActivity.InvalidStateException e) {
                }
            } else if (activity instanceof CameraCaptureActivity) {
                ((CameraCaptureActivity) activity).returnFromPreview(startWithEdit);
            }
        }
    }

    @Override // android.support.v4.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        this.mFinalFile = null;
        VineModelFactory.getModelInstance().getModelEvents().removeListener(this.mModelListener);
    }

    public void setFinalFile(RecordingFile finalFile) {
        this.mFinalFile = finalFile;
    }

    @Override // android.support.v4.app.Fragment
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1990:
                enableViewsForEnterAnimation();
                this.mPreviewCaption.setVisibility(0);
                switch (resultCode) {
                    case -1:
                        getActivity().finish();
                        break;
                    case 0:
                        if (data != null) {
                            ((RecordStateHolder) getActivity()).setPostShareParameters((PostShareParameters) Parcels.unwrap(data.getParcelableExtra("share_screen_parameters")));
                            break;
                        }
                        break;
                }
            case 1995:
                switch (resultCode) {
                    case -1:
                        this.mVenueName = data.getStringExtra("foursquare_venue");
                        this.mVenueId = data.getStringExtra("foursquare_venue_id");
                        if (this.mVenueName != null) {
                            this.mLocationLabel.setText(this.mVenueName);
                            break;
                        }
                        break;
                }
            case 12345:
                switch (resultCode) {
                    case -1:
                        uploadAndToPost();
                        break;
                }
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    public void uploadAndToPost() {
        try {
            AbstractRecordingActivity activity = (AbstractRecordingActivity) getActivity();
            if (activity != null) {
                if (BuildUtil.isIsHwEncodingEnabled()) {
                    activity.makeSureUploadIsReady();
                }
                this.mUploadPath = UploadManager.addToUploadQueue(activity, this.mFinalFile.version.name(), this.mFinalFile.getVideoPath(), this.mThumbnailPath, this.mFinalFile.folder.getName(), this.mFinalFile.getSession().getMetaData().toString(), true, this.mConversationRowId, null);
                toPost();
            }
        } catch (Exception e) {
            CrashUtil.logException(e);
        }
    }

    public void toPost() {
        PostShareParameters outParams = null;
        KeyEvent.Callback activity = getActivity();
        if (activity != null) {
            if (activity instanceof RecordStateHolder) {
                if ((activity instanceof AbstractRecordingActivity) && BuildUtil.isIsHwEncodingEnabled()) {
                    this.mUploadPath = ((AbstractRecordingActivity) activity).makeSureUploadIsReady();
                }
                PostShareParameters inParams = ((RecordStateHolder) activity).getPostShareParameters();
                if (inParams != null) {
                    outParams = new PostShareParameters(this.mCachedCaption, this.mCachedCaptionEntities, this.mVenueName, this.mVenueId, inParams.channel, inParams.recipients, inParams.shareToVine, inParams.shareToTwitter, inParams.shareToFacebook, inParams.shareToTumblr);
                }
                disableViewsForExitAnimation();
            }
            if (outParams == null) {
                outParams = new PostShareParameters(this.mCachedCaption, this.mCachedCaptionEntities, this.mVenueName, this.mVenueId);
            }
            Intent intent = PostShareActivity.getPostCreationShareIntent(getActivity(), this.mUploadPath, this.mVideoPath, this.mThumbnailPath, false, this.mIsFromSony, outParams, this.sources);
            getActivity().startActivityForResult(intent, 1990);
        }
    }

    public void toRecipientPicker(Activity activity) {
        ArrayList<VineRecipient> recipients;
        Editable text = this.mMessagingEditText.getText();
        String message = text != null ? text.toString() : "";
        if (this.mDirectUserId < 0 && this.mConversationRowId < 0) {
            activity.startService(VineUploadService.getShowProgressIntent(getActivity()));
            return;
        }
        if (this.mDirectUserId >= 0) {
            recipients = new ArrayList<>();
            recipients.add(VineRecipient.fromUser(this.mDirectUserId));
        } else {
            recipients = VineProviderHelper.getConversationRecipients(activity.getContentResolver(), this.mConversationRowId);
        }
        activity.startService(VineUploadService.getVMPostIntent(activity, this.mUploadPath, false, this.mConversationRowId, recipients, message));
        activity.startService(VineUploadService.getShowProgressIntent(activity));
        activity.finish();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addTags(TagModel tagModel, String query) {
        List<VineTag> tags = tagModel.getTagsForQuery(query);
        if (tags != null && !tags.isEmpty()) {
            this.mTagsAdapter.setItems(tags);
            this.mPreviewCaption.onItemsFetched();
        }
    }

    private final class ModelListener implements ModelEvents.ModelListener {
        private ModelListener() {
        }

        @Override // co.vine.android.model.ModelEvents.ModelListener
        public void onTagsAdded(TagModel tagModel, String query) {
            RecordingPreviewFragment.this.addTags(tagModel, query);
        }

        @Override // co.vine.android.model.ModelEvents.ModelListener
        public void onTimelineUpdated(TimelineModel timelineModel, TimelineDetails timelineDetails) {
        }
    }

    private final class RecordingPreviewSuggestionsListener extends SuggestionsActionListener {
        private RecordingPreviewSuggestionsListener() {
        }

        @Override // co.vine.android.service.components.suggestions.SuggestionsActionListener
        public void onGetUserTypeAheadComplete(String reqId, int statusCode, String reasonPhrase, String query, ArrayList<VineUser> users) {
            if (statusCode == 200 && !users.isEmpty()) {
                RecordingPreviewFragment.this.mUsersAdapter.setItems(users);
                RecordingPreviewFragment.this.mPreviewCaption.onItemsFetched();
            }
        }
    }

    private final class RecordingPreviewSessionListener extends AppSessionListener {
        private RecordingPreviewSessionListener() {
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onPhotoImageLoaded(HashMap<ImageKey, UrlImage> images) {
            super.onPhotoImageLoaded(images);
            RecordingPreviewFragment.this.mUsersAdapter.setUserImages(images);
        }
    }

    private void initializeFakeActionBar(LayoutInflater inflater, View contentView) {
        Activity activity = getActivity();
        this.mFakeActionBar = (FakeActionBar) inflater.inflate(R.layout.screen_fake_action_bar_standalone, (ViewGroup) null);
        this.mFakeActionBarPlaceholder = contentView.findViewById(R.id.fake_action_bar_placeholder);
        this.mFakeActionBarBackArrow = (ImageButton) this.mFakeActionBar.inflateBackView(R.layout.fake_action_bar_back_arrow);
        this.mFakeActionBarActionView = (ViewGroup) this.mFakeActionBar.inflateActionView(R.layout.recording_preview_fake_action_bar_next);
        this.mFakeActionBar.setBackView(this.mFakeActionBarBackArrow);
        this.mFakeActionBar.setActionView(this.mFakeActionBarActionView);
        this.mFakeActionBar.setOnActionListener(new FakeActionBar.OnActionListener() { // from class: co.vine.android.RecordingPreviewFragment.10
            @Override // co.vine.android.share.widgets.FakeActionBar.OnActionListener
            public void onBackPressed() {
                if (!RecordingPreviewFragment.this.mIsMessaging && RecordingPreviewFragment.this.mPreviewCaption.hasFocus()) {
                    RecordingPreviewFragment.this.mPreviewCaption.setText(RecordingPreviewFragment.this.mCachedCaption, RecordingPreviewFragment.this.mCachedCaptionEntities);
                    RecordingPreviewFragment.this.mPreviewCaption.clearFocus();
                } else {
                    Activity activity2 = RecordingPreviewFragment.this.getActivity();
                    RecordingPreviewFragment.this.previewToRecord(activity2, false);
                }
            }
        });
        this.mRemainingCharacterCount = (TextView) this.mFakeActionBarActionView.findViewById(R.id.characters_remaining);
        this.mFakeActionBarActionButton = (Button) this.mFakeActionBarActionView.findViewById(R.id.action);
        this.mFakeActionBarActionButton.setTypeface(Typefaces.get(activity).getContentTypeface(Typefaces.get(activity).mediumContentBold.getStyle(), 3));
        this.mFakeActionBarActionButton.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.RecordingPreviewFragment.11
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                Activity activity2 = RecordingPreviewFragment.this.getActivity();
                if (activity2 != null) {
                    if (RecordingPreviewFragment.this.mUploadPath == null && (activity2 instanceof AbstractRecordingActivity)) {
                        AbstractRecordingActivity abstractRecordingActivity = (AbstractRecordingActivity) activity2;
                        RecordingPreviewFragment.this.mUploadPath = abstractRecordingActivity.makeSureUploadIsReady();
                    }
                    if (!RecordingPreviewFragment.this.mPreviewCaption.hasFocus()) {
                        if (RecordingPreviewFragment.this.mIsMessaging) {
                            RecordingPreviewFragment.this.toRecipientPicker(activity2);
                            return;
                        }
                        if (!AppController.getInstance(activity2).isLoggedIn()) {
                            PromptDialogSupportFragment fragment = PromptDialogSupportFragment.newInstance(1);
                            fragment.setMessage(R.string.share_login_to_post);
                            fragment.setPositiveButton(android.R.string.ok);
                            fragment.setNeutralButton(android.R.string.cancel);
                            fragment.setListener(RecordingPreviewFragment.this.mLoginRequiredDialogDoneListener);
                            fragment.show(RecordingPreviewFragment.this.getFragmentManager());
                            return;
                        }
                        RecordingPreviewFragment.this.toPost();
                        FlurryUtils.trackPreviewAction("post");
                        return;
                    }
                    RecordingPreviewFragment.this.mCachedCaption = RecordingPreviewFragment.this.mPreviewCaption.getText().toString();
                    RecordingPreviewFragment.this.mCachedCaptionEntities = RecordingPreviewFragment.this.mPreviewCaption.getEntities();
                    RecordingPreviewFragment.this.mPreviewCaption.clearFocus();
                }
            }
        });
        this.mFakeActionBar.setTopOfWindowLayoutParams();
        this.mActionButtonTextColor = -1;
        this.mActionButtonTextTooLongColor = getResources().getColor(R.color.white_fifty_percent);
        this.mRemainingCharacterCountTextColor = getResources().getColor(R.color.white_fifty_percent);
        this.mRemainingCharacterCountTextTooLongColor = -1;
    }

    private void disableViewsForExitAnimation() {
        ViewUtil.disableAndHide(this.mPreviewCaption, this.mMessagingDetailsContainer, this.mPreviewDetailsContainer, this.mPreviewActionsContainer);
    }

    private void enableViewsForEnterAnimation() {
        ViewUtil.enableAndShow(this.mPreviewCaption, this.mMessagingDetailsContainer, this.mPreviewDetailsContainer, this.mPreviewActionsContainer);
    }

    private int getProfileBackgroundColor() {
        SharedPreferences sharedPreferences = Util.getDefaultSharedPrefs(getActivity());
        int profileColor = sharedPreferences.getInt("profile_background", Settings.DEFAULT_PROFILE_COLOR);
        return (-16777216) | profileColor;
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
    public void ensureActionBarState() {
        Resources resources = getResources();
        if (this.mPreviewCaption.hasFocus()) {
            this.mFakeActionBar.setLabelText(resources.getString(R.string.caption_label));
            String text = this.mPreviewCaption.getText().toString();
            this.mRemainingCharacterCount.setText(String.valueOf(110 - text.length()));
            this.mFakeActionBarActionButton.setText(TextUtils.isEmpty(this.mCachedCaption) ? R.string.add : R.string.done);
            if (text.length() <= 110) {
                this.mFakeActionBarActionButton.setEnabled(true);
                this.mFakeActionBarActionButton.setTextColor(this.mActionButtonTextColor);
                this.mRemainingCharacterCount.setTextColor(this.mRemainingCharacterCountTextColor);
            } else {
                this.mFakeActionBarActionButton.setEnabled(false);
                this.mFakeActionBarActionButton.setTextColor(this.mActionButtonTextTooLongColor);
                this.mRemainingCharacterCount.setTextColor(this.mRemainingCharacterCountTextTooLongColor);
            }
            if (TextUtils.isEmpty(text) && TextUtils.isEmpty(this.mCachedCaption)) {
                ViewUtil.disableAndHide(this.mFakeActionBarActionView);
                return;
            } else {
                ViewUtil.enableAndShow(this.mFakeActionBarActionView);
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

    private final class TokenListener implements ComposeTokenizer.TokenListener {
        private TokenListener() {
        }

        @Override // co.vine.android.ComposeTokenizer.TokenListener
        public void onUserTokenFound() {
            if (RecordingPreviewFragment.this.mTypeaheadAdapter != RecordingPreviewFragment.this.mUsersAdapter) {
                RecordingPreviewFragment.this.mTypeaheadAdapter = RecordingPreviewFragment.this.mUsersAdapter;
                RecordingPreviewFragment.this.mPreviewCaption.setAdapter(RecordingPreviewFragment.this.mTypeaheadAdapter);
            }
        }

        @Override // co.vine.android.ComposeTokenizer.TokenListener
        public void onTagTokenFound() {
            if (RecordingPreviewFragment.this.mTypeaheadAdapter != RecordingPreviewFragment.this.mTagsAdapter) {
                RecordingPreviewFragment.this.mTypeaheadAdapter = RecordingPreviewFragment.this.mTagsAdapter;
                RecordingPreviewFragment.this.mPreviewCaption.setAdapter(RecordingPreviewFragment.this.mTypeaheadAdapter);
            }
        }
    }

    public static RecordingPreviewFragment newInstance(String videoPath, String uploadPath, String thumbnailPath, boolean isMessaging, long conversationObjectId, long directUserId, boolean isFromSony, int color, String recipientUsername, ArrayList<VineSource> sources) {
        Bundle bundle = new Bundle();
        bundle.putString("video_path", videoPath);
        bundle.putString("pic_path", thumbnailPath);
        bundle.putString("upload_path", uploadPath);
        bundle.putBoolean("messaging", isMessaging);
        bundle.putLong("conversation_row_id", conversationObjectId);
        bundle.putLong("direct_user_id", directUserId);
        bundle.putBoolean("f_s", isFromSony);
        bundle.putInt("color", color);
        bundle.putString("recipient_username", recipientUsername);
        if (sources != null && !sources.isEmpty()) {
            bundle.putParcelableArrayList("sources", sources);
        }
        RecordingPreviewFragment fragment = new RecordingPreviewFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private class SimpleUserAdapter extends BaseAdapter {
        private AppController mAppController;
        private int mImageSize;
        private ArrayList<VineUser> mItems;
        private final ArrayList<WeakReference<UserViewHolder>> mViewHolders = new ArrayList<>();

        public SimpleUserAdapter(AppController appController) {
            this.mAppController = appController;
            this.mImageSize = RecordingPreviewFragment.this.getResources().getDimensionPixelSize(R.dimen.user_image_size);
        }

        public void setItems(ArrayList<VineUser> items) {
            this.mItems = items;
        }

        @Override // android.widget.Adapter
        public int getCount() {
            if (this.mItems != null) {
                return this.mItems.size();
            }
            return 0;
        }

        @Override // android.widget.Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            View ret;
            UserViewHolder holder;
            if (convertView == null) {
                ret = LayoutInflater.from(RecordingPreviewFragment.this.getActivity()).inflate(R.layout.user_dropdown_row_view, parent, false);
                holder = new UserViewHolder(ret);
                this.mViewHolders.add(new WeakReference<>(holder));
                ret.setTag(holder);
            } else {
                ret = convertView;
                holder = (UserViewHolder) ret.getTag();
            }
            holder.username.setText(this.mItems.get(position).username);
            String avatarUrl = this.mItems.get(position).avatarUrl;
            if (!TextUtils.isEmpty(avatarUrl)) {
                ImageKey key = new ImageKey(avatarUrl, this.mImageSize, this.mImageSize, true);
                if (Util.isDefaultAvatarUrl(avatarUrl)) {
                    Util.safeSetDefaultAvatar(holder.image, Util.ProfileImageSize.MEDIUM, (-16777216) | RecordingPreviewFragment.this.getResources().getColor(R.color.vine_green));
                } else {
                    holder.imageKey = key;
                    setUserImage(holder, this.mAppController.getPhotoBitmap(key));
                }
            } else {
                setUserImage(holder, null);
            }
            return ret;
        }

        @Override // android.widget.Adapter
        public long getItemId(int position) {
            return 0L;
        }

        @Override // android.widget.Adapter
        public Object getItem(int position) {
            if (this.mItems.size() <= position) {
                return new VineTypeAhead("mention", "", 0L);
            }
            VineUser user = this.mItems.get(position);
            return new VineTypeAhead("mention", user.username, user.userId);
        }

        private class UserViewHolder {
            public ImageView image;
            public ImageKey imageKey;
            public TextView username;

            public UserViewHolder(View view) {
                this.image = (ImageView) view.findViewById(R.id.user_image);
                this.username = (TextView) view.findViewById(R.id.username);
            }
        }

        public void setUserImages(HashMap<ImageKey, UrlImage> images) {
            ArrayList<WeakReference<UserViewHolder>> toRemove = new ArrayList<>();
            Iterator<WeakReference<UserViewHolder>> it = this.mViewHolders.iterator();
            while (it.hasNext()) {
                WeakReference<UserViewHolder> ref = it.next();
                UserViewHolder holder = ref.get();
                if (holder == null) {
                    toRemove.add(ref);
                } else {
                    UrlImage image = images.get(holder.imageKey);
                    if (image != null && image.isValid()) {
                        setUserImage(holder, image.bitmap);
                    }
                }
            }
            Iterator<WeakReference<UserViewHolder>> it2 = toRemove.iterator();
            while (it2.hasNext()) {
                WeakReference<UserViewHolder> r = it2.next();
                this.mViewHolders.remove(r);
            }
        }

        private void setUserImage(UserViewHolder holder, Bitmap bm) {
            holder.image.setColorFilter((ColorFilter) null);
            if (bm != null) {
                holder.image.setImageDrawable(new RecyclableBitmapDrawable(RecordingPreviewFragment.this.getResources(), bm));
            } else {
                holder.image.setImageResource(R.drawable.circle_shape_light);
            }
        }
    }

    private class SimpleTagAdapter extends BaseAdapter {
        private List<VineTag> mItems;

        private SimpleTagAdapter() {
        }

        public void setItems(List<VineTag> items) {
            this.mItems = items;
        }

        @Override // android.widget.Adapter
        public int getCount() {
            if (this.mItems != null) {
                return this.mItems.size();
            }
            return 0;
        }

        @Override // android.widget.Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            View ret;
            if (convertView == null) {
                ret = LayoutInflater.from(RecordingPreviewFragment.this.getActivity()).inflate(R.layout.tag_row_view, parent, false);
            } else {
                ret = convertView;
            }
            ((TextView) ret.findViewById(R.id.tag_name)).setText("#" + this.mItems.get(position).getTagName());
            return ret;
        }

        @Override // android.widget.Adapter
        public long getItemId(int position) {
            return 0L;
        }

        @Override // android.widget.Adapter
        public Object getItem(int position) {
            if (this.mItems.size() <= position) {
                return new VineTypeAhead("tag", "", 0L);
            }
            VineTag tag = this.mItems.get(position);
            return new VineTypeAhead("tag", "#" + tag.getTagName(), tag.getTagId());
        }
    }

    private class EditTextListener extends TypeAheadEditTextListener {
        public EditTextListener() {
            super(RecordingPreviewFragment.this.mAppController);
        }

        @Override // co.vine.android.TypeAheadEditTextListener
        void onTagsAvailable(TagModel tagModel, String query) {
            RecordingPreviewFragment.this.addTags(tagModel, query);
        }
    }
}
