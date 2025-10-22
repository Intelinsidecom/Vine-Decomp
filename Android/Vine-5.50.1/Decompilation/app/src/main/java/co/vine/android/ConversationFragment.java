package co.vine.android;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.util.LongSparseArray;
import android.support.v4.view.ViewCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import co.vine.android.api.VineRecipient;
import co.vine.android.cache.image.ImageKey;
import co.vine.android.cache.image.UrlImage;
import co.vine.android.cache.video.UrlVideo;
import co.vine.android.cache.video.VideoKey;
import co.vine.android.client.AppSessionListener;
import co.vine.android.provider.Vine;
import co.vine.android.provider.VineDatabaseSQL;
import co.vine.android.provider.VineProviderHelper;
import co.vine.android.recorder.InlineVineRecorder;
import co.vine.android.recorder.NotEnoughSpaceException;
import co.vine.android.recorder.ProgressView;
import co.vine.android.recorder.RecordConfigUtils;
import co.vine.android.recorder.RecordSessionManager;
import co.vine.android.recorder.RecordSessionVersion;
import co.vine.android.scribe.FollowScribeActionsLogger;
import co.vine.android.service.VineUploadService;
import co.vine.android.util.BuildUtil;
import co.vine.android.util.CrashUtil;
import co.vine.android.util.CrossConstants;
import co.vine.android.util.MuteUtil;
import co.vine.android.util.UploadManager;
import co.vine.android.util.Util;
import co.vine.android.util.ViewUtil;
import co.vine.android.widget.ConversationList;
import co.vine.android.widget.TypefacesEditText;
import com.edisonwang.android.slog.SLog;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/* loaded from: classes.dex */
public class ConversationFragment extends BaseCursorListFragment implements TextWatcher, View.OnClickListener, TextView.OnEditorActionListener, TypefacesEditText.KeyboardListener {
    private boolean mBound;
    private ConversationActivity mCallback;
    private ViewGroup mChatPresenceContainer;
    private ImageView mChatPresenceEllipsis;
    private LongSparseArray<Integer> mColorMap;
    private View mComposeContainer;
    private ServiceConnection mConnection;
    private ContentResolver mContentResolver;
    private ConversationAdapter mConversationAdapter;
    private long mConversationRowId;
    private TypefacesEditText mEdit;
    private FollowScribeActionsLogger mFollowScribeActionsLogger;
    private Intent mFullRecordIntent;
    private Handler mIncomingHandler;
    private boolean mIsSending;
    private long mLastTopItemId;
    private int mLastTopItemPixelOffset;
    private View mLoadMoreHeaderContent;
    private View mLoadMoreProgress;
    private int mLocalUnreadCount;
    private View mNewMessageBarContainer;
    private TextView mNewMessageBarText;
    private ProgressView mProgressView;
    private String mRecipientUsername;
    private ImageView mRecordButton;
    private InlineRecorderManager mRecorderManager;
    private boolean mRecordingEnabled;
    private ImageView mSendButton;
    private RelativeLayout mTextInputContainer;
    private View mTextInputToggle;
    private ViewGroup mUploadProgressContainer;
    private Messenger mUploadServiceMessenger;
    private ImageView mUploadThumbnail;
    private RecordSessionVersion mVersion;
    private boolean mFetched = false;
    private boolean mFetchWasLoadMore = false;
    private int mProgressViewWidth = 0;
    private int mMyColor = 0;
    private int mRecipientColor = 0;
    private boolean mFetchWasInitial = false;
    private final BroadcastReceiver mMuteChangeReceiver = new BroadcastReceiver() { // from class: co.vine.android.ConversationFragment.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent == null || intent.getAction() == null) {
                CrashUtil.log("Something wrong has happened");
            } else {
                boolean mute = intent.getAction().equals(MuteUtil.ACTION_CHANGED_TO_MUTE);
                ((ConversationAdapter) ConversationFragment.this.mCursorAdapter).toggleMute(mute);
            }
        }
    };
    private final View.OnClickListener mLaunchRecorderListener = new View.OnClickListener() { // from class: co.vine.android.ConversationFragment.6
        @Override // android.view.View.OnClickListener
        public void onClick(View v) throws RemoteException {
            ConversationFragment.this.launchFullRecord();
        }
    };
    private final Runnable mLaunchFullRecordRunnable = new Runnable() { // from class: co.vine.android.ConversationFragment.7
        @Override // java.lang.Runnable
        public void run() {
            FragmentActivity activity = ConversationFragment.this.getActivity();
            if (activity != null) {
                Util.startActionOnRecordingAvailable(activity, ConversationFragment.this.mFullRecordIntent, 1);
            }
        }
    };

    @Override // android.support.v4.app.Fragment
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mCallback = (ConversationActivity) activity;
    }

    @Override // co.vine.android.BaseCursorListFragment, co.vine.android.BaseAdapterFragment, co.vine.android.BaseControllerFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey("fetched")) {
            this.mFetched = savedInstanceState.getBoolean("fetched");
        }
        SlidingMenu m = ((BaseActionBarActivity) getActivity()).getSlidingMenu();
        if (m != null) {
            m.setTouchModeAbove(0);
        }
        Bundle args = getArguments();
        this.mConversationRowId = args.getLong("conversation_row_id", -1L);
        setHasOptionsMenu(true);
        this.mVersion = (RecordSessionVersion) args.getSerializable("arg_encoder_version");
        if (this.mVersion == null) {
            this.mVersion = RecordSessionManager.getCurrentVersion(getActivity());
        }
        this.mIncomingHandler = new UploadProgressHandler();
        this.mBound = false;
        this.mConnection = new ServiceConnection() { // from class: co.vine.android.ConversationFragment.2
            @Override // android.content.ServiceConnection
            public void onServiceConnected(ComponentName name, IBinder service) throws RemoteException {
                SLog.dWithTag("ConversationFrag", "Bound to VineUploadService");
                ConversationFragment.this.mUploadServiceMessenger = new Messenger(service);
                ConversationFragment.this.mBound = true;
                ConversationFragment.this.subscribe(ConversationFragment.this.mIncomingHandler);
            }

            @Override // android.content.ServiceConnection
            public void onServiceDisconnected(ComponentName name) {
                SLog.dWithTag("ConversationFrag", "Connection to VineUploadService lost unexpectedly!");
                ConversationFragment.this.mUploadServiceMessenger = null;
                ConversationFragment.this.mBound = false;
            }
        };
        if (this.mColorMap == null) {
            this.mColorMap = new LongSparseArray<>();
        }
        long start = System.currentTimeMillis();
        this.mContentResolver = getActivity().getContentResolver();
        Cursor cursor = VineProviderHelper.queryConversationRecipientsUsersView(this.mContentResolver, this.mConversationRowId);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                long id = cursor.getLong(6);
                int color = cursor.getInt(7);
                if (color == Settings.DEFAULT_PROFILE_COLOR || color <= 0) {
                    color = 16777215 & getResources().getColor(R.color.vine_green);
                }
                int color2 = color | ViewCompat.MEASURED_STATE_MASK;
                if (this.mRecipientColor == 0) {
                    this.mRecipientColor = color2;
                }
                this.mColorMap.put(id, Integer.valueOf(color2));
                this.mRecipientUsername = cursor.getString(3);
            }
            cursor.close();
        }
        ((ConversationActivity) getActivity()).setRecipientUsername(this.mRecipientUsername);
        SLog.d("onCreate queries took {}ms.", Long.valueOf(System.currentTimeMillis() - start));
        this.mLocalUnreadCount = 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void subscribe(Handler replyHandler) throws RemoteException {
        Messenger replyTo = new Messenger(replyHandler);
        Message subscribe = Message.obtain((Handler) null, 1);
        subscribe.replyTo = replyTo;
        sendMessageToUploadService(subscribe);
    }

    @Override // co.vine.android.BaseCursorListFragment, co.vine.android.BaseAdapterFragment, android.support.v4.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("fetched", this.mFetched);
    }

    @Override // co.vine.android.BaseCursorListFragment, android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = createView(inflater, R.layout.conversation, container);
        TypefacesEditText edit = (TypefacesEditText) v.findViewById(R.id.edit_reply);
        edit.setOnEditorActionListener(this);
        edit.addTextChangedListener(this);
        edit.setKeyboardListener(this);
        this.mEdit = edit;
        this.mTextInputContainer = (RelativeLayout) v.findViewById(R.id.text_input_container);
        this.mTextInputToggle = v.findViewById(R.id.text_input_toggle);
        this.mTextInputToggle.setOnClickListener(this);
        this.mNewMessageBarContainer = v.findViewById(R.id.new_message_bar_container);
        this.mNewMessageBarContainer.setOnClickListener(this);
        this.mNewMessageBarText = (TextView) this.mNewMessageBarContainer.findViewById(R.id.new_message_bar);
        this.mRecordButton = (ImageView) v.findViewById(R.id.record_button);
        this.mSendButton = (ImageView) v.findViewById(R.id.send_reply_button);
        return v;
    }

    @Override // android.support.v4.app.Fragment
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ConversationActivity activity = (ConversationActivity) getActivity();
        SharedPreferences prefs = Util.getDefaultSharedPrefs(activity);
        this.mRecordingEnabled = RecordConfigUtils.isCapableOfRecording(activity);
        if (this.mRecordingEnabled) {
            try {
                this.mVersion.getManager(activity);
            } catch (IOException e) {
                this.mRecordingEnabled = false;
                if (e instanceof NotEnoughSpaceException) {
                    Util.showCenteredToast(activity, R.string.failed_to_start_recording_space);
                } else {
                    Util.showCenteredToast(activity, R.string.failed_to_start_recording_storage_not_ready);
                }
                CrashUtil.log("Exception has happened: {}", e);
            }
        }
        if (this.mCursorAdapter == null) {
            this.mConversationAdapter = new ConversationAdapter(activity, this.mAppController, this.mListView, prefs.getLong("pref_user_row_id", 0L), 0);
            this.mCursorAdapter = this.mConversationAdapter;
        }
        ListView listView = this.mListView;
        listView.setOnScrollListener(this);
        listView.setDivider(null);
        ((ConversationList) listView).deactivateRefresh(true);
        setAppSessionListener(new ConversationSessionListener());
        ViewGroup loadMoreHeader = (ViewGroup) LayoutInflater.from(activity).inflate(R.layout.conversation_list_load_more_header, (ViewGroup) null);
        this.mLoadMoreHeaderContent = loadMoreHeader.findViewById(R.id.header_content);
        this.mLoadMoreHeaderContent.setVisibility(8);
        this.mLoadMoreProgress = loadMoreHeader.findViewById(R.id.progress);
        listView.addHeaderView(loadMoreHeader);
        this.mLoadMoreHeaderContent.setOnClickListener(this);
        listView.setAdapter(this.mCursorAdapter);
        ViewGroup uploadProgressContainer = (ViewGroup) LayoutInflater.from(activity).inflate(R.layout.conversation_reply_progress, (ViewGroup) this.mListView, false);
        this.mUploadProgressContainer = (ViewGroup) uploadProgressContainer.findViewById(R.id.progress_container);
        this.mProgressView = (ProgressView) uploadProgressContainer.findViewById(R.id.progress_view);
        this.mUploadThumbnail = (ImageView) uploadProgressContainer.findViewById(R.id.thumbnail);
        ViewGroup footer = (ViewGroup) LayoutInflater.from(activity).inflate(R.layout.conversation_chat_presence, (ViewGroup) this.mListView, false);
        this.mChatPresenceContainer = (ViewGroup) footer.findViewById(R.id.container);
        this.mChatPresenceEllipsis = (ImageView) footer.findViewById(R.id.ellipsis);
        this.mListView.addFooterView(footer);
        View rootView = getView();
        if (this.mRecordingEnabled) {
            this.mRecorderManager = (InlineRecorderManager) rootView.findViewById(R.id.inline_recorder_manager);
            this.mRecorderManager.init(this, activity, this.mVersion);
        } else {
            ((InlineRecorderManager) rootView.findViewById(R.id.inline_recorder_manager)).initDisabled(this);
        }
        activity.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(this.mRecipientColor));
        this.mComposeContainer = rootView.findViewById(R.id.compose_container);
        int rawColor = prefs.getInt("profile_background", Settings.DEFAULT_PROFILE_COLOR);
        if (rawColor == Settings.DEFAULT_PROFILE_COLOR || rawColor <= 0) {
            rawColor = 16777215 & getResources().getColor(R.color.vine_green);
        }
        this.mMyColor = (-16777216) | rawColor;
        ((ProgressView) rootView.findViewById(R.id.progress_inline)).setColor(this.mMyColor);
        ((ProgressView) rootView.findViewById(R.id.finishing_progress)).setColor(this.mMyColor);
        ViewUtil.setBackground(this.mComposeContainer, new ColorDrawable(getMyColor()));
        GradientDrawable dr = (GradientDrawable) activity.getResources().getDrawable(R.drawable.chat_cursor_drawable);
        dr.setColor((-16777216) | this.mMyColor);
        this.mNewMessageBarContainer.setBackgroundDrawable(new ColorDrawable(this.mRecipientColor));
        this.mSendButton.setOnClickListener(this.mLaunchRecorderListener);
        this.mSendButton.setColorFilter(new PorterDuffColorFilter(getMyColor(), PorterDuff.Mode.SRC_IN));
        this.mFollowScribeActionsLogger = activity.getFollowScribeLogger();
    }

    protected void prepareUpload() {
        InlineVineRecorder recorder = this.mRecorderManager.getRecorder();
        if (recorder != null && recorder.finalFile != null) {
            try {
                String uploadPath = UploadManager.addToUploadQueue(getActivity(), recorder.finalFile.version.name(), recorder.finalFile.getVideoPath(), recorder.finalFile.getThumbnailPath(), recorder.finalFile.folder.getName(), recorder.finalFile.getSession().getMetaData().toString(), true, this.mConversationRowId, null);
                sendMessage(uploadPath);
            } catch (Exception e) {
                if (BuildUtil.isLogsOn()) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override // co.vine.android.BaseCursorListFragment, android.widget.AbsListView.OnScrollListener
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        super.onScrollStateChanged(view, scrollState);
        if (this.mCursorAdapter != null && scrollState == 0) {
            ((ConversationAdapter) this.mCursorAdapter).postNewPlayCurrentPositionRunnable();
        }
    }

    @Override // co.vine.android.BaseCursorListFragment, co.vine.android.BaseAdapterFragment, co.vine.android.BaseControllerFragment, co.vine.android.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        GradientDrawable dr = (GradientDrawable) getResources().getDrawable(R.drawable.chat_cursor_drawable);
        dr.setColor((-16777216) | this.mMyColor);
        ConversationActivity activity = (ConversationActivity) getActivity();
        if (activity.keyBoardUpOnStart) {
            activity.keyBoardUpOnStart = false;
            this.mHandler.post(new Runnable() { // from class: co.vine.android.ConversationFragment.3
                @Override // java.lang.Runnable
                public void run() {
                    ConversationFragment.this.toggleInput();
                }
            });
        }
        this.mConversationAdapter.onResume();
        activity.registerReceiver(this.mMuteChangeReceiver, MuteUtil.MUTE_INTENT_FILTER, CrossConstants.BROADCAST_PERMISSION, null);
        if (this.mCursorAdapter.getCursor() == null) {
            initLoader();
        }
        if (!this.mBound) {
            SLog.dWithTag("ConversationFrag", "Will bind to VineUploadService now");
            Intent intent = new Intent(activity, (Class<?>) VineUploadService.class);
            activity.bindService(intent, this.mConnection, 1);
        }
        this.mUploadProgressContainer.setVisibility(8);
        this.mChatPresenceContainer.setVisibility(8);
        if (this.mRecorderManager != null) {
            try {
                this.mRecorderManager.onResume();
            } catch (IOException e) {
                this.mRecordingEnabled = false;
                CrashUtil.log("Failed to create folder.");
                Util.showCenteredToast(getActivity(), R.string.failed_to_start_recording_storage_not_ready);
            }
        }
    }

    @Override // co.vine.android.BaseCursorListFragment, android.support.v4.app.Fragment
    public void onActivityResult(int requestCode, int resultCode, Intent data) throws RemoteException {
        if (requestCode == 2) {
            processPostOptionsResult(PostOptionsDialogActivity.processActivityResult(this.mAppController, getActivity(), resultCode, data, this.mFollowScribeActionsLogger));
            return;
        }
        if (requestCode == 1 && data != null) {
            SLog.dWithTag("ConversationFrag", "resultCode=" + resultCode + ", data=" + data);
            if (!BuildUtil.isIsHwEncodingEnabled()) {
                sendMessage(data.getStringExtra("upload_path"));
                return;
            }
            return;
        }
        if (resultCode == 100) {
            sendMessageToUploadService(Message.obtain((Handler) null, 2));
        }
    }

    private void sendMessage(String uploadPath) throws RemoteException {
        Intent intent = VineUploadService.getVMPostIntent(getActivity(), uploadPath, false, this.mConversationRowId, VineProviderHelper.getConversationRecipients(this.mContentResolver, this.mConversationRowId), null);
        SLog.d("intent={}, extras={}", intent, intent.getExtras());
        getActivity().startService(intent);
        this.mIncomingHandler = new UploadProgressHandler();
        subscribe(this.mIncomingHandler);
    }

    public void onRecorderShown() {
        this.mTextInputToggle.setVisibility(8);
        ((ConversationAdapter) this.mCursorAdapter).stopCurrentPlayer();
    }

    public void onRecorderHidden() {
        this.mTextInputToggle.setVisibility(0);
        ((ConversationAdapter) this.mCursorAdapter).postNewPlayCurrentPositionRunnable();
    }

    @Override // co.vine.android.widget.TypefacesEditText.KeyboardListener
    public void onKeyboardDismissed() {
        this.mCallback.onTypingStatusChanged(false, this.mIsSending);
        hideTypingContainer();
    }

    @Override // co.vine.android.widget.TypefacesEditText.KeyboardListener
    public boolean sendKeyEvent(KeyEvent event) {
        return false;
    }

    public int getMyColor() {
        return this.mMyColor;
    }

    private class UploadProgressHandler extends Handler {
        private UploadProgressHandler() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            int what = msg.what;
            Bundle data = msg.obj == null ? new Bundle() : (Bundle) msg.obj;
            SLog.dWithTag("ConversationFrag", "Message received, what=" + what);
            switch (what) {
                case 3:
                    boolean isActive = data.getBoolean("is_active");
                    long conversationRowId = data.getLong("conversation_row_id");
                    String thumbnailPath = data.getString("thumbnail");
                    SLog.dWithTag("ConversationFrag", "Result receiver was set in VineUploadService, conversationId=" + conversationRowId);
                    if (isActive && ConversationFragment.this.mConversationRowId == conversationRowId) {
                        ConversationFragment.this.mUploadProgressContainer.setVisibility(0);
                        ConversationFragment.this.mProgressView = (ProgressView) ConversationFragment.this.mUploadProgressContainer.findViewById(R.id.progress_view);
                        if (TextUtils.isEmpty(thumbnailPath)) {
                            ConversationFragment.this.mUploadThumbnail.setVisibility(8);
                            break;
                        } else {
                            Bitmap thumbnailBitmap = BitmapFactory.decodeFile(thumbnailPath);
                            ConversationFragment.this.mUploadThumbnail.setImageBitmap(thumbnailBitmap);
                            break;
                        }
                    }
                    break;
                case 4:
                    SLog.dWithTag("ConversationFrag", "Video transcoding started");
                    break;
                case 5:
                    double p = data.getDouble("transcode_progress");
                    SLog.dWithTag("ConversationFrag", "Transcode progress changed to p=" + p);
                    if (ConversationFragment.this.mProgressViewWidth == 0) {
                        ConversationFragment.this.mProgressViewWidth = ConversationFragment.this.mProgressView.getMeasuredWidth();
                    }
                    ConversationFragment.this.mProgressView.setProgressRatio((float) ((0.65d * p) / 100.0d));
                    break;
                case 6:
                    double p2 = data.getDouble("upload_progress");
                    SLog.dWithTag("ConversationFrag", "Upload progress changed to p=" + p2);
                    if (ConversationFragment.this.mProgressViewWidth == 0) {
                        ConversationFragment.this.mProgressViewWidth = ConversationFragment.this.mProgressView.getMeasuredWidth();
                    }
                    ConversationFragment.this.mProgressView.setProgressRatio((float) (0.65d + ((0.33d * p2) / 100.0d)));
                    break;
                case 7:
                    boolean success = data.getBoolean("success");
                    SLog.dWithTag("ConversationFrag", "Post completed, success=" + success);
                    ConversationFragment.this.mUploadProgressContainer.setVisibility(8);
                    sendMessage(Message.obtain((Handler) null, 2));
                    break;
            }
        }
    }

    private void sendMessageToUploadService(Message msg) throws RemoteException {
        try {
            if (this.mUploadServiceMessenger != null) {
                this.mUploadServiceMessenger.send(msg);
            }
        } catch (RemoteException e) {
            SLog.d("Failed to send message to upload service");
            if (BuildUtil.isLogsOn()) {
                e.printStackTrace();
            }
        }
    }

    private void scrollToBottom(boolean respectCurrentPos, final boolean smooth) {
        final ListView listView = this.mListView;
        final int count = this.mCursorAdapter.getCount() + listView.getHeaderViewsCount();
        if (!respectCurrentPos || count - listView.getLastVisiblePosition() < 2) {
            listView.post(new Runnable() { // from class: co.vine.android.ConversationFragment.4
                @Override // java.lang.Runnable
                public void run() {
                    if (smooth) {
                        listView.smoothScrollToPosition(count - 1);
                    } else {
                        listView.setSelectionFromTop(count - 1, 0);
                    }
                }
            });
        }
    }

    private void fetchContent(int fetchType) {
        if (this.mConversationRowId > 0 && !hasPendingRequest()) {
            switch (fetchType) {
                case 1:
                    this.mFetchWasLoadMore = true;
                    showProgress(1);
                    addRequest(this.mAppController.fetchConversation(3, true, 0L, this.mConversationRowId, false));
                    break;
                case 3:
                    this.mFetched = true;
                    showProgress(3);
                    addRequest(this.mAppController.fetchConversation(1, false, 0L, this.mConversationRowId, false));
                    break;
            }
        }
    }

    @Override // co.vine.android.BaseCursorListFragment, co.vine.android.BaseAdapterFragment
    public void showProgress(int type) {
        switch (type) {
            case 1:
                this.mLoadMoreHeaderContent.setVisibility(8);
                this.mLoadMoreProgress.setVisibility(0);
                break;
        }
    }

    @Override // co.vine.android.BaseCursorListFragment, co.vine.android.BaseAdapterFragment
    public void hideProgress(int type) {
        this.mLoadMoreProgress.setVisibility(8);
    }

    public void showTypingIndicator(boolean show, long userId) {
        final ViewGroup container = this.mChatPresenceContainer;
        container.clearAnimation();
        if (show) {
            ImageView ellipsis = this.mChatPresenceEllipsis;
            Integer color = null;
            if (this.mColorMap != null) {
                Integer color2 = this.mColorMap.get(userId);
                color = color2;
            }
            if (color == null) {
                color = Integer.valueOf(this.mRecipientColor);
            }
            container.setVisibility(0);
            container.getLayoutParams().height = getResources().getDimensionPixelSize(R.dimen.chat_presence_height);
            ellipsis.setColorFilter(1509949440 | (color.intValue() & ViewCompat.MEASURED_SIZE_MASK));
            scrollToBottom(true, false);
            return;
        }
        HeightAnimation collapse = new HeightAnimation(container, new Runnable() { // from class: co.vine.android.ConversationFragment.5
            @Override // java.lang.Runnable
            public void run() {
                container.setVisibility(8);
            }
        });
        container.startAnimation(collapse);
    }

    private class HeightAnimation extends Animation {
        private View mView;

        public HeightAnimation(View view, final Runnable onAnimationEnd) {
            setFillAfter(true);
            setDuration(300L);
            this.mView = view;
            setAnimationListener(new Animation.AnimationListener() { // from class: co.vine.android.ConversationFragment.HeightAnimation.1
                @Override // android.view.animation.Animation.AnimationListener
                public void onAnimationRepeat(Animation animation) {
                }

                @Override // android.view.animation.Animation.AnimationListener
                public void onAnimationStart(Animation animation) {
                }

                @Override // android.view.animation.Animation.AnimationListener
                public void onAnimationEnd(Animation animation) {
                    onAnimationEnd.run();
                }
            });
        }

        @Override // android.view.animation.Animation
        protected void applyTransformation(float interpolatedTime, Transformation t) throws Resources.NotFoundException {
            int height = ConversationFragment.this.getResources().getDimensionPixelSize(R.dimen.chat_presence_height);
            this.mView.getLayoutParams().height = (int) (height * (1.0f - interpolatedTime));
            this.mView.requestLayout();
        }

        @Override // android.view.animation.Animation
        public boolean willChangeBounds() {
            return true;
        }
    }

    @Override // android.text.TextWatcher
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (this.mEdit.getText().length() == 0) {
            this.mSendButton.setImageResource(R.drawable.ic_vm_capture_keyboard);
            this.mSendButton.setOnClickListener(this.mLaunchRecorderListener);
        } else {
            this.mSendButton.setImageResource(R.drawable.ic_action_send);
            this.mSendButton.setOnClickListener(this);
        }
    }

    @Override // android.text.TextWatcher
    public void afterTextChanged(Editable editable) {
        if (!TextUtils.isEmpty(editable)) {
            this.mCallback.onTypingStatusChanged(true, this.mIsSending);
        } else {
            this.mCallback.onTypingStatusChanged(false, this.mIsSending);
        }
    }

    @Override // android.text.TextWatcher
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    @Override // android.widget.TextView.OnEditorActionListener
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (v.getId() != R.id.edit_reply) {
            return false;
        }
        if (actionId == 4) {
            sendTextMessage();
        }
        return true;
    }

    @Override // co.vine.android.BaseCursorAdapterFragment, android.support.v4.app.LoaderManager.LoaderCallbacks
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String conversationId = String.valueOf(this.mConversationRowId);
        Uri contentUri = Vine.ConversationMessageUsersView.CONTENT_URI_CONVERSATION.buildUpon().appendQueryParameter("conversation_row_id", conversationId).build();
        return new CursorLoader(getActivity(), contentUri, VineDatabaseSQL.ConversationMessageUsersQuery.PROJECTION, null, null, "message_id ASC");
    }

    @Override // co.vine.android.BaseCursorListFragment, co.vine.android.BaseAdapterFragment, co.vine.android.BaseControllerFragment, android.support.v4.app.Fragment
    public void onPause() throws RemoteException {
        super.onPause();
        this.mConversationAdapter.onPause();
        getActivity().unregisterReceiver(this.mMuteChangeReceiver);
        if (this.mBound) {
            SLog.dWithTag("ConversationFrag", "Will unbind from VineUploadService now");
            sendMessageToUploadService(Message.obtain((Handler) null, 2));
            getActivity().unbindService(this.mConnection);
            this.mBound = false;
        }
        if (this.mRecorderManager != null) {
            this.mRecorderManager.onPaused();
        }
    }

    @Override // co.vine.android.BaseCursorAdapterFragment, android.support.v4.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        if (this.mRecorderManager != null) {
            this.mRecorderManager.release();
        }
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // co.vine.android.BaseCursorAdapterFragment, android.support.v4.app.LoaderManager.LoaderCallbacks
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        switch (loader.getId()) {
            case 0:
                saveTopItemInfo();
                super.onLoadFinished(loader, cursor);
                hideProgress(3);
                if (!this.mFetched) {
                    fetchContent(3);
                    this.mFetchWasInitial = true;
                    break;
                } else {
                    if (this.mCursorAdapter.isEmpty()) {
                        showSadface(true, false);
                    } else {
                        showSadface(false);
                        if (this.mFetchWasLoadMore) {
                            this.mFetchWasLoadMore = false;
                            this.mLoadMoreHeaderContent.setVisibility(0);
                            hideProgress(3);
                            this.mListView.setSelectionFromTop(((ConversationAdapter) this.mCursorAdapter).getPositionForId(this.mLastTopItemId) + this.mListView.getHeaderViewsCount(), this.mLastTopItemPixelOffset);
                        } else if (this.mFetchWasInitial) {
                            scrollToBottom(false, false);
                        } else if (this.mListView.getLastVisiblePosition() >= cursor.getCount() - 1) {
                            scrollToBottom(false, true);
                        }
                    }
                    this.mFetchWasInitial = false;
                    if (((ConversationAdapter) this.mCursorAdapter).atLastPage()) {
                        this.mLoadMoreHeaderContent.setVisibility(8);
                    } else {
                        this.mLoadMoreHeaderContent.setVisibility(0);
                    }
                    if (!this.mCursorAdapter.isEmpty()) {
                        ((ConversationAdapter) this.mCursorAdapter).postNewPlayCurrentPositionRunnable();
                        break;
                    }
                }
                break;
        }
    }

    private void saveTopItemInfo() {
        if (this.mCursorAdapter.getCount() != 0) {
            this.mLastTopItemId = this.mCursorAdapter.getItemId(this.mListView.getFirstVisiblePosition());
            View topChild = this.mListView.getChildAt(this.mListView.getFirstVisiblePosition() + this.mListView.getHeaderViewsCount());
            if (topChild != null) {
                this.mLastTopItemPixelOffset = topChild.getTop();
            }
        }
    }

    private void sendTextMessage() {
        EditText editText = this.mEdit;
        if (editText.getText().length() > 420) {
            Util.showTopToast(getActivity(), R.string.message_length_exceeded);
            return;
        }
        if (validate(this.mEdit)) {
            String message = editText.getText().toString();
            ArrayList<VineRecipient> recipients = VineProviderHelper.getConversationRecipients(this.mContentResolver, this.mConversationRowId);
            Intent i = VineUploadService.getVMPostIntent(getActivity(), null, false, this.mConversationRowId, recipients, message);
            getActivity().startService(i);
            this.mIsSending = true;
            editText.getText().clear();
            this.mIsSending = false;
            return;
        }
        Util.setSoftKeyboardVisibility(getActivity(), this.mEdit, false);
        hideTypingContainer();
    }

    private void hideTypingContainer() {
        this.mEdit.clearFocus();
        if (this.mLocalUnreadCount > 0) {
            toggleNewMessageBar(true);
        }
        this.mTextInputContainer.setVisibility(8);
        ViewUtil.setBackground(this.mComposeContainer, new ColorDrawable(getMyColor()));
        this.mRecordButton.setVisibility(0);
        this.mTextInputToggle.setVisibility(0);
    }

    private boolean validate(EditText editText) {
        return editText != null && editText.length() > 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void toggleInput() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            EditText edit = this.mEdit;
            edit.requestFocus();
            Util.setSoftKeyboardVisibility(activity, edit, true);
            this.mTextInputContainer.setVisibility(0);
            ViewUtil.setBackground(this.mComposeContainer.findViewById(R.id.compose_container), new ColorDrawable(activity.getResources().getColor(R.color.solid_white)));
            this.mRecordButton.setVisibility(8);
            this.mTextInputToggle.setVisibility(8);
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.send_reply_button) {
            sendTextMessage();
            return;
        }
        if (id == R.id.text_input_toggle) {
            toggleInput();
            return;
        }
        if (id == R.id.header_content) {
            this.mLoadMoreHeaderContent.setVisibility(8);
            this.mLoadMoreProgress.setVisibility(0);
            fetchContent(1);
        } else if (id == R.id.new_message_bar_container) {
            scrollToBottom(false, false);
            toggleNewMessageBar(false);
            this.mLocalUnreadCount = 0;
        }
    }

    public void onNewMessage() {
        if (this.mListView.getLastVisiblePosition() < this.mCursorAdapter.getCount() && this.mTextInputContainer.getVisibility() != 0) {
            this.mLocalUnreadCount++;
            this.mNewMessageBarText.setText(getResources().getQuantityString(R.plurals.new_messages, this.mLocalUnreadCount, Integer.valueOf(this.mLocalUnreadCount)));
            toggleNewMessageBar(true);
        }
    }

    @Override // co.vine.android.BaseCursorListFragment
    protected void onScrollLastItem(Cursor cursor) {
        this.mLocalUnreadCount = 0;
        toggleNewMessageBar(false);
    }

    private void toggleNewMessageBar(boolean show) {
        this.mNewMessageBarContainer.setVisibility(show ? 0 : 8);
    }

    public boolean isInTextInputMode() {
        return this.mRecordButton.getVisibility() == 8;
    }

    void launchFullRecord() throws RemoteException {
        this.mFullRecordIntent = AbstractRecordingActivity.getIntentForConversation(getActivity(), 0, "ConversationFullRecord", this.mConversationRowId, this.mRecipientUsername);
        this.mIncomingHandler = new UploadProgressHandler();
        subscribe(this.mIncomingHandler);
        this.mHandler.post(this.mLaunchFullRecordRunnable);
    }

    private class ConversationSessionListener extends AppSessionListener {
        private ConversationSessionListener() {
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onVideoPathObtained(HashMap<VideoKey, UrlVideo> videos) {
            ((ConversationAdapter) ConversationFragment.this.mCursorAdapter).onVideoPathObtained(videos);
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onPhotoImageLoaded(HashMap<ImageKey, UrlImage> images) {
            ((ConversationAdapter) ConversationFragment.this.mCursorAdapter).setImages(images);
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onGetConversationComplete(String reqId, int statusCode, String reasonPhrase, long conversationRowId, int responseCode, boolean empty) {
            PendingRequest req = ConversationFragment.this.removeRequest(reqId);
            if (req != null && conversationRowId == ConversationFragment.this.mConversationRowId) {
                ConversationFragment.this.hideProgress(3);
                if (empty) {
                    ConversationFragment.this.showSadface(true, false);
                } else {
                    ConversationFragment.this.showSadface(false);
                }
                ConversationAdapter adapter = (ConversationAdapter) ConversationFragment.this.mCursorAdapter;
                if (ConversationFragment.this.mFetched && (adapter.atLastPage() || empty || adapter.isEmpty())) {
                    ConversationFragment.this.mLoadMoreHeaderContent.setVisibility(8);
                }
                if (responseCode == 4) {
                    ConversationFragment.this.mLoadMoreHeaderContent.setVisibility(0);
                }
            }
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onGetConversationRemoteIdComplete(long conversationId) {
            if (conversationId <= 0) {
                ConversationFragment.this.mLoadMoreHeaderContent.setVisibility(8);
            }
        }
    }
}
