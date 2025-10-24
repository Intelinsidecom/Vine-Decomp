package co.vine.android;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import co.vine.android.api.VineParsers;
import co.vine.android.api.VineRTCConversation;
import co.vine.android.api.VineRTCParticipant;
import co.vine.android.client.AppSessionListener;
import co.vine.android.client.TwitterVineApp;
import co.vine.android.client.VineAPI;
import co.vine.android.recorder.RecordConfigUtils;
import co.vine.android.recorder.RecordSessionManager;
import co.vine.android.recorder.RecordSessionVersion;
import co.vine.android.recorder.RecordingActivityHelper;
import co.vine.android.scribe.AppNavigationProviderSingleton;
import co.vine.android.scribe.FollowScribeActionsLogger;
import co.vine.android.scribe.FollowScribeActionsLoggerSingleton;
import co.vine.android.scribe.ScribeLoggerSingleton;
import co.vine.android.service.GCMNotificationService;
import co.vine.android.service.ResourceService;
import co.vine.android.service.components.Components;
import co.vine.android.service.components.authentication.AuthenticationActionListener;
import co.vine.android.service.components.userinteraction.UserInteractionsListener;
import co.vine.android.util.AppTrackingUtil;
import co.vine.android.util.CrashUtil;
import co.vine.android.util.CrossConstants;
import co.vine.android.util.IntentionalObjectCounter;
import co.vine.android.util.MuteUtil;
import co.vine.android.util.PhoneConfirmationUtil;
import co.vine.android.util.Util;
import co.vine.android.widgets.PromptDialogFragment;
import co.vine.android.widgets.PromptDialogSupportFragment;
import com.codebutler.android_websockets.WebSocketClient;
import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.edisonwang.android.slog.SLog;
import com.fasterxml.jackson.core.JsonParser;
import com.googlecode.javacv.cpp.avformat;
import com.twitter.sdk.android.core.AuthToken;
import com.twitter.sdk.android.core.SessionManager;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;

/* loaded from: classes.dex */
public class ConversationActivity extends BaseControllerActionBarActivity {
    private static final String EVENT_SOURCE_TITLE = ConversationActivity.class.getName();
    public boolean keyBoardUpOnStart;
    private boolean mAmFollowingRecipient;
    private RealTimeChatReceiver mChatReceiver;
    private VineWebSocketClient mClient;
    private long mConversationId;
    private long mConversationRowId;
    private long mDirectUserId;
    private FollowScribeActionsLogger mFollowScribeActionsLogger;
    private boolean mIsDirectUserExternal;
    private boolean mLastIsConnected;
    private long mLastMessageId;
    private boolean mLastTyping;
    private Handler mMainHandler;
    private long mMessageIdToDelete;
    private SharedPreferences mPref;
    private String mRecipientUserName;
    private long mReconnectDelay;
    private Runnable mRetrySuccessRunnable;
    private RecordSessionVersion mVersion;
    private RecordingActivityHelper mHelper = new RecordingActivityHelper();
    private final IntentionalObjectCounter mIntentionalObjectCounter = new IntentionalObjectCounter("recorder", this);
    private final AuthenticationActionListener mAuthListener = new AuthenticationActionListener() { // from class: co.vine.android.ConversationActivity.1
        @Override // co.vine.android.service.components.authentication.AuthenticationActionListener
        public void digitVerificationSuccess() {
            ConversationActivity.this.successPhoneVerification();
        }

        @Override // co.vine.android.service.components.authentication.AuthenticationActionListener
        public void digitVerificationFailure() {
            ConversationActivity.this.failurePhoneVerification();
        }
    };
    private final AuthCallback mCallback = new AuthCallback() { // from class: co.vine.android.ConversationActivity.2
        @Override // com.digits.sdk.android.AuthCallback
        public void success(DigitsSession digitsSession, String s) {
            ConversationActivity.this.checkDigitSuccess();
        }

        @Override // com.digits.sdk.android.AuthCallback
        public void failure(DigitsException e) {
            ConversationActivity.this.failurePhoneVerification();
        }
    };
    private final PromptDialogSupportFragment.OnDialogDoneListener mListener = new PromptDialogSupportFragment.OnDialogDoneListener() { // from class: co.vine.android.ConversationActivity.3
        @Override // co.vine.android.widgets.PromptDialogFragment.OnDialogDoneListener
        public void onDialogDone(DialogInterface dialog, int id, int which) {
            switch (id) {
                case 0:
                    switch (which) {
                        case -1:
                            ConversationActivity.this.mAppController.deleteConversation(ConversationActivity.this.mConversationRowId);
                            ConversationActivity.this.finish();
                            break;
                    }
                case 1:
                    switch (which) {
                        case -1:
                            ConversationActivity.this.mAppController.ignoreConversation(ConversationActivity.this.mConversationRowId);
                            ConversationActivity.this.finish();
                            break;
                    }
                case 2:
                    switch (which) {
                        case -1:
                            ConversationActivity.this.mAppController.deleteMessage(ConversationActivity.this.mMessageIdToDelete);
                            break;
                    }
            }
        }
    };
    private BroadcastReceiver mConnectivityReceiver = new BroadcastReceiver() { // from class: co.vine.android.ConversationActivity.4
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            try {
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService("connectivity");
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null && activeNetwork.isConnected();
                if (isConnected && ConversationActivity.this.mLastIsConnected != isConnected) {
                    SLog.dWithTag("ConvActivity;RTC", "Connectivity change received, reconnecting");
                    ConversationActivity.this.mMainHandler.removeCallbacks(ConversationActivity.this.mReconnectRunnable);
                    ConversationActivity.this.reconnectWithBackoff();
                }
                ConversationActivity.this.mLastIsConnected = isConnected;
            } catch (SecurityException e) {
                SLog.e("We are not gangsta enough to acess the connectivity state.", (Throwable) e);
            }
        }
    };
    private WebSocketClient.Listener mWebSocketListener = new WebSocketClient.Listener() { // from class: co.vine.android.ConversationActivity.5
        @Override // com.codebutler.android_websockets.WebSocketClient.Listener
        public void onConnect() {
            SLog.dWithTag("ConvActivity;RTC", "Websocket connected");
            ConversationActivity.this.mMainHandler.removeCallbacks(ConversationActivity.this.mReconnectRunnable);
            ConversationActivity.this.mAppSessionListener.onWebSocketConnectComplete();
        }

        @Override // com.codebutler.android_websockets.WebSocketClient.Listener
        public void onMessage(String message) throws NumberFormatException {
            SLog.dWithTag("ConvActivity;RTC", String.format("Got string message: %s", message));
            try {
                JsonParser p = VineParsers.createParser(message);
                ArrayList<VineRTCConversation> data = VineParsers.parseRTCEvent(p);
                if (data != null) {
                    ConversationActivity.this.mAppSessionListener.onReceiveRTCMessage(data);
                }
            } catch (IOException e) {
                SLog.e("Failed to parse message.", (Throwable) e);
            }
        }

        @Override // com.codebutler.android_websockets.WebSocketClient.Listener
        public void onMessage(byte[] data) {
        }

        @Override // com.codebutler.android_websockets.WebSocketClient.Listener
        public void onDisconnect(int code, String reason) {
            SLog.dWithTag("ConvActivity;RTC", "Websocket disconnected, reason=" + reason);
            ConversationActivity.this.mAppSessionListener.onWebSocketDisconnected();
        }

        @Override // com.codebutler.android_websockets.WebSocketClient.Listener
        public void onError(Exception error) {
            SLog.dWithTag("ConvActivity;RTC", "Websocket error=" + error.getMessage());
            ConversationActivity.this.mAppSessionListener.onWebSocketError();
        }
    };
    private final Runnable mReconnectRunnable = new Runnable() { // from class: co.vine.android.ConversationActivity.6
        @Override // java.lang.Runnable
        public void run() {
            if (ConversationActivity.this.mClient == null) {
                ConversationActivity.this.prepareClient();
            }
            VineWebSocketClient client = ConversationActivity.this.mClient;
            if (client != null && !client.isConnecting() && !client.isConnected()) {
                client.connect();
                ConversationActivity.this.mReconnectDelay = ConversationActivity.this.mReconnectDelay < 16000 ? ConversationActivity.this.mReconnectDelay * 2 : ConversationActivity.this.mReconnectDelay;
                ConversationActivity.this.mMainHandler.postDelayed(ConversationActivity.this.mReconnectRunnable, ConversationActivity.this.mReconnectDelay);
                SLog.dWithTag("ConvActivity;RTC", "Posted reconnect with delay=" + ConversationActivity.this.mReconnectDelay + "ms");
            }
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    public void checkDigitSuccess() {
        SessionManager<DigitsSession> dsm = Digits.getSessionManager();
        DigitsSession das = (DigitsSession) dsm.getActiveSession();
        if (das != null) {
            AuthToken token = das.getAuthToken();
            if (token instanceof TwitterAuthToken) {
                SLog.d("Digit success...now verify it.");
                Components.authComponent().verifyDigits(this.mAppController, this.mAppController.getActiveSession(), (TwitterAuthToken) token, new TwitterAuthConfig(TwitterVineApp.API_KEY, TwitterVineApp.API_SECRET));
                dsm.clearActiveSession();
            }
        }
    }

    public static Intent getIntent(Context context, long conversationObjectId, String recipientUsername, long recipientId, boolean isRecipientExternal, boolean followingThem, boolean keyboardUpOnStart) {
        Class conversationActivity = RecordConfigUtils.isCapableOfInline(context) ? ConversationActivity.class : ConversationActivityMain.class;
        Intent intent = new Intent(context, (Class<?>) conversationActivity);
        intent.setFlags(avformat.AVFMT_SEEK_TO_PTS);
        intent.putExtra("conversation_row_id", conversationObjectId);
        intent.putExtra("recipient_id", recipientId);
        intent.putExtra("is_recipient_external", isRecipientExternal);
        intent.putExtra("am_following_recipient", followingThem);
        intent.putExtra("keyboard_up", keyboardUpOnStart);
        return intent;
    }

    @Override // co.vine.android.BaseControllerActionBarActivity
    public boolean isConversationSideMenuEnabled() {
        return true;
    }

    public void setRecipientUsername(String recipientUsername) {
        this.mRecipientUserName = recipientUsername;
        setupActionBar((Boolean) true, Boolean.valueOf(TextUtils.isEmpty(this.mRecipientUserName) ? false : true), this.mRecipientUserName, (Boolean) null, (Boolean) false);
    }

    @Override // co.vine.android.BaseActionBarActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityDonut, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.fragment_layout, true, true);
        Intent intent = getIntent();
        this.mIntentionalObjectCounter.add();
        this.mConversationRowId = intent.getLongExtra("conversation_row_id", -1L);
        this.mDirectUserId = intent.getLongExtra("recipient_id", -1L);
        this.mIsDirectUserExternal = intent.getBooleanExtra("is_recipient_external", false);
        this.mRecipientUserName = intent.getStringExtra("recipient_username");
        this.mAmFollowingRecipient = intent.getBooleanExtra("am_following_recipient", false);
        this.keyBoardUpOnStart = intent.getBooleanExtra("keyboard_up", false);
        this.mPref = Util.getDefaultSharedPrefs(this);
        if ("co.vine.android.MESSAGE_NOTIFICATION_PRESSED".equals(intent.getAction())) {
            this.mAppController.clearPushNotifications(2);
        }
        if (this.mConversationRowId < 0 && this.mDirectUserId < 0) {
            restoreActivityState();
        }
        setupCallbackListeners();
        if (this.mConversationRowId >= 0 || this.mDirectUserId > 0) {
        }
        this.mHelper.bindCameraService(this);
        this.mVersion = RecordSessionManager.getCurrentVersion(this);
        if (savedInstanceState == null) {
            ConversationFragment fragment = new ConversationFragment();
            Bundle b = ConversationFragment.prepareArguments(intent, false);
            b.putSerializable("arg_encoder_version", this.mVersion);
            b.putLong("conversation_row_id", this.mConversationRowId);
            fragment.setArguments(b);
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment, "conversationFragment").commit();
        }
        this.mMainHandler = new Handler(Looper.getMainLooper());
        this.mAppController.getConversationRemoteId(this.mConversationRowId);
        this.mChatReceiver = new RealTimeChatReceiver();
        this.mLastTyping = false;
        this.mFollowScribeActionsLogger = FollowScribeActionsLoggerSingleton.getInstance(ScribeLoggerSingleton.getInstance(getApplicationContext()), AppStateProviderSingleton.getInstance(this), AppNavigationProviderSingleton.getInstance());
    }

    private class RealTimeChatReceiver extends BroadcastReceiver {
        private RealTimeChatReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if ("co.vine.android.service.mergeSelfNewMessage".equals(intent.getAction())) {
                long messageId = intent.getLongExtra("message_id", 0L);
                long conversationId = intent.getLongExtra("conversation_id", 0L);
                if (messageId > 0 && conversationId > 0 && ConversationActivity.this.mClient != null) {
                    ConversationActivity.this.mClient.alertNewPrivateMessage(conversationId, messageId);
                }
            }
        }
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.mConversationRowId = intent.getLongExtra("conversation_row_id", -1L);
        this.mAmFollowingRecipient = intent.getBooleanExtra("am_following_recipient", false);
        if (this.mConversationRowId >= 0 || this.mDirectUserId > 0) {
        }
        this.mDirectUserId = intent.getLongExtra("recipient_id", -1L);
        this.mRecipientUserName = intent.getStringExtra("recipient_username");
        ConversationFragment fragment = new ConversationFragment();
        Bundle b = ConversationFragment.prepareArguments(intent, false);
        b.putSerializable("arg_encoder_version", this.mVersion);
        b.putLong("conversation_row_id", this.mConversationRowId);
        fragment.setArguments(b);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment, "conversationFragment").commit();
    }

    @Override // co.vine.android.BaseActionBarActivity, android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.conversation, menu);
        if (this.mAmFollowingRecipient || this.mDirectUserId <= 0 || TextUtils.isEmpty(this.mRecipientUserName) || this.mIsDirectUserExternal) {
            menu.removeItem(R.id.follow_user);
        } else {
            MenuItem item = menu.findItem(R.id.follow_user);
            item.setTitle(String.format(getString(R.string.follow_user_conversation), this.mRecipientUserName));
        }
        if (this.mDirectUserId > 0 && !TextUtils.isEmpty(this.mRecipientUserName) && !this.mIsDirectUserExternal) {
            MenuItem item2 = menu.findItem(R.id.ignore);
            item2.setTitle(String.format(getString(R.string.ignore_user), this.mRecipientUserName));
            MenuItem item3 = menu.findItem(R.id.go_to_user_profile);
            item3.setTitle(String.format(getString(R.string.go_to_profile_conversation), this.mRecipientUserName));
        } else {
            menu.removeItem(R.id.ignore);
            menu.removeItem(R.id.go_to_user_profile);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override // co.vine.android.BaseActionBarActivity, android.app.Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.ignore) {
            showIgnoreDialog();
            return true;
        }
        if (id == R.id.delete_conversation) {
            showDeleteDialog();
            return true;
        }
        if (id == 16908332) {
            onBackPressed();
            return true;
        }
        if (id == R.id.go_to_user_profile) {
            ChannelActivity.startProfile(this, this.mDirectUserId, EVENT_SOURCE_TITLE);
            return true;
        }
        if (id == R.id.follow_user) {
            Components.userInteractionsComponent().followUser(this.mAppController, this.mDirectUserId, true, this.mFollowScribeActionsLogger);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showDeleteDialog() {
        PromptDialogFragment p = PromptDialogFragment.newInstance(0);
        p.setListener(this.mListener);
        p.setMessage(R.string.delete_conversation_prompt);
        p.setPositiveButton(R.string.delete_yes);
        p.setNegativeButton(R.string.cancel);
        p.show(getFragmentManager());
    }

    public void showIgnoreDialog() {
        PromptDialogFragment p = PromptDialogFragment.newInstance(1);
        p.setListener(this.mListener);
        p.setMessage(R.string.ignore_conversation_prompt);
        p.setPositiveButton(R.string.ignore_yes);
        p.setNegativeButton(R.string.cancel);
        p.show(getFragmentManager());
    }

    @Override // co.vine.android.BaseActionBarActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        try {
            this.mIntentionalObjectCounter.release();
            int count = this.mIntentionalObjectCounter.getCount();
            if (count == 0) {
                CrashUtil.log("Clean up folders because we are the last one.");
                this.mVersion.getManager(this).cleanUnusedFolders();
            } else if (count > 1) {
                CrashUtil.logException(new VineLoggingException("Double instance violation, but it's ok."));
            }
        } catch (IOException e) {
            CrashUtil.logException(e);
        }
        this.mHelper.unBindCameraService(this);
    }

    @Override // co.vine.android.BaseActionBarActivity, com.jeremyfeinstein.slidingmenu.lib.SlidingActivityHelper.MenuStateHandler.MenuStateListener
    public void onMenuClose() {
        if (!MuteUtil.isMuted(this)) {
            sendBroadcast(new Intent(MuteUtil.ACTION_CHANGED_TO_UNMUTE), CrossConstants.BROADCAST_PERMISSION);
        }
    }

    @Override // co.vine.android.BaseActionBarActivity, com.jeremyfeinstein.slidingmenu.lib.SlidingActivityHelper.MenuStateHandler.MenuStateListener
    public void onMenuOpened() {
        if (!MuteUtil.isMuted(this)) {
            sendBroadcast(new Intent(MuteUtil.ACTION_CHANGED_TO_MUTE), CrossConstants.BROADCAST_PERMISSION);
        }
    }

    @Override // co.vine.android.BaseActionBarActivity
    protected void restoreActivityState() {
        super.restoreActivityState();
        Intent intent = getIntent();
        if (intent != null) {
            String action = intent.getAction();
            if ("android.intent.action.MAIN".equals(action)) {
                long[] states = getConversationActivityStates(this);
                this.mConversationRowId = states[0];
                this.mDirectUserId = states[1];
            }
        }
    }

    public static long[] getConversationActivityStates(Activity activity) {
        String data = Util.getDefaultSharedPrefs(activity).getString("pref_saved_state_data", null);
        long[] state = {-1, -1};
        if (data != null) {
            int separator = data.indexOf(":");
            try {
                long conversationId = Long.valueOf(data.substring(0, separator)).longValue();
                if (conversationId <= 0) {
                    conversationId = -1;
                }
                state[0] = conversationId;
            } catch (NumberFormatException e) {
                CrashUtil.logException(e, "Attempted to restore activity state with an invalid conversation id", new Object[0]);
            }
            try {
                long directUserId = Long.valueOf(data.substring(separator + 1)).longValue();
                if (directUserId <= 0) {
                    directUserId = -1;
                }
                state[1] = directUserId;
            } catch (NumberFormatException e2) {
                CrashUtil.logException(e2, "Attempted to restore activity state with an invalid direct user id", new Object[0]);
            }
        }
        return state;
    }

    @Override // co.vine.android.BaseActionBarActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onStart() {
        super.onStart();
        ResourceService.isConversationActive = true;
    }

    @Override // co.vine.android.BaseControllerActionBarActivity, co.vine.android.BaseActionBarActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        AppTrackingUtil.sendAppOpenedMessage(this);
        ResourceService.lastActiveconversationRowId = this.mConversationRowId;
        IntentFilter chatFilter = new IntentFilter();
        chatFilter.addAction("co.vine.android.rtc.WEBSOCKET_EVENT");
        chatFilter.addAction("co.vine.android.service.mergeSelfNewMessage");
        registerReceiver(this.mChatReceiver, chatFilter, CrossConstants.BROADCAST_PERMISSION, null);
        startService(GCMNotificationService.getUpdateNotificationIntent(this, 2, this.mConversationRowId));
        IntentFilter connFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(this.mConnectivityReceiver, connFilter, CrossConstants.BROADCAST_PERMISSION, null);
        prepareClient();
        VineWebSocketClient client = this.mClient;
        if (client != null) {
            client.connect();
        }
        Components.authComponent().addListener(this.mAuthListener);
        checkDigitSuccess();
    }

    public void onBackPressed(View v) {
        try {
            onBackPressed();
        } catch (IllegalStateException e) {
        }
    }

    @Override // co.vine.android.BaseControllerActionBarActivity, co.vine.android.BaseActionBarActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onPause() {
        super.onPause();
        try {
            unregisterReceiver(this.mChatReceiver);
        } catch (IllegalArgumentException e) {
        }
        try {
            unregisterReceiver(this.mConnectivityReceiver);
        } catch (IllegalArgumentException e2) {
        }
        onTypingStatusChanged(false, false);
        disconnectClient();
        Components.authComponent().removeListener(this.mAuthListener);
    }

    @Override // co.vine.android.BaseActionBarActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onStop() {
        super.onStop();
        ResourceService.isConversationActive = false;
    }

    public void onTypingStatusChanged(boolean isTyping, boolean isSending) {
        if (this.mConversationId > 0 && clientIsActive() && this.mLastTyping != isTyping) {
            if (isTyping) {
                this.mClient.updateTypingState(this.mConversationId, true);
            } else if (!isSending) {
                this.mClient.updateTypingState(this.mConversationId, false);
            }
        }
        this.mLastTyping = isTyping;
    }

    public void showDeleteMessageDialog(long messageId) {
        this.mMessageIdToDelete = messageId;
        PromptDialogFragment p = PromptDialogFragment.newInstance(2);
        p.setListener(this.mListener);
        p.setMessage(R.string.delete_message_prompt);
        p.setPositiveButton(R.string.delete_yes);
        p.setNegativeButton(R.string.cancel);
        try {
            p.show(getFragmentManager());
        } catch (IllegalStateException e) {
            CrashUtil.logException(e);
        }
    }

    @Override // co.vine.android.BaseActionBarActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 101) {
            if (resultCode == 1527) {
                successPhoneVerification();
            }
        } else {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag("conversationFragment");
            if (fragment instanceof ConversationFragment) {
                fragment.onActivityResult(requestCode - 65536, resultCode, data);
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void successPhoneVerification() {
        this.mPref.edit().putBoolean("profile_phone_verified", true).apply();
        if (this.mRetrySuccessRunnable != null) {
            this.mRetrySuccessRunnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void failurePhoneVerification() {
        this.mPref.edit().putBoolean("profile_phone_verified", false).apply();
    }

    public void startPhoneConfirmation(Runnable retrySuccessRunnable) {
        if (this.mPref.getBoolean("profile_phone_verified", false)) {
            retrySuccessRunnable.run();
            return;
        }
        this.mRetrySuccessRunnable = retrySuccessRunnable;
        String phone = this.mPref.getString("settings_profile_phone", "");
        PhoneConfirmationUtil.confirmPhoneNumber(this, this.mCallback, phone, 101);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:10:0x000e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public synchronized boolean clientIsActive() {
        /*
            r2 = this;
            monitor-enter(r2)
            co.vine.android.VineWebSocketClient r0 = r2.mClient     // Catch: java.lang.Throwable -> L10
            if (r0 == 0) goto Le
            boolean r1 = r0.isConnected()     // Catch: java.lang.Throwable -> L10
            if (r1 == 0) goto Le
            r1 = 1
        Lc:
            monitor-exit(r2)
            return r1
        Le:
            r1 = 0
            goto Lc
        L10:
            r1 = move-exception
            monitor-exit(r2)
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: co.vine.android.ConversationActivity.clientIsActive():boolean");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void prepareClient() {
        VineWebSocketClient client = this.mClient;
        if (client == null || (!client.isConnecting() && !client.isConnected())) {
            SLog.d("ConvActivity;RTC", "Preparing client now");
            VineAPI api = VineAPI.getInstance(this);
            URI uri = URI.create(getResources().getString(CrossConstants.RES_RTC_URL));
            String sessionKey = this.mAppController.getActiveSessionReadOnly().getSessionKey();
            List<BasicNameValuePair> extraHeaders = Arrays.asList(new BasicNameValuePair("vine-session-id", sessionKey), new BasicNameValuePair("X-Vine-Client", api.getVineClientHeader()));
            SLog.dWithTag("ConvActivity;RTC", "Creating client: sessionKey=" + sessionKey + ", uri=" + uri);
            this.mClient = new VineWebSocketClient(uri, this.mWebSocketListener, extraHeaders);
        }
    }

    private void disconnectClient() {
        this.mMainHandler.removeCallbacks(this.mReconnectRunnable);
        WebSocketClient client = this.mClient;
        if (client != null) {
            client.disconnect();
            this.mClient = null;
        }
    }

    public FollowScribeActionsLogger getFollowScribeLogger() {
        return this.mFollowScribeActionsLogger;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reconnectWithBackoff() {
        this.mReconnectDelay = 1000L;
        this.mMainHandler.postDelayed(this.mReconnectRunnable, this.mReconnectDelay);
        SLog.dWithTag("ConvActivity;RTC", "Posting reconnect runnable with delay=" + this.mReconnectDelay);
    }

    private void setupCallbackListeners() {
        this.mAppSessionListener = new AppSessionListener() { // from class: co.vine.android.ConversationActivity.7
            @Override // co.vine.android.client.AppSessionListener
            public void onGetConversationRemoteIdComplete(long conversationId) {
                ConversationActivity.this.mConversationId = conversationId;
                if (ConversationActivity.this.clientIsActive()) {
                    SLog.dWithTag("ConvActivity;RTC", "Subscribing to conversation=" + ConversationActivity.this.mConversationId);
                    ConversationActivity.this.mClient.subscribeConversation(ConversationActivity.this.mConversationId);
                }
            }

            @Override // co.vine.android.client.AppSessionListener
            public void onWebSocketConnectComplete() {
                if (ConversationActivity.this.mConversationId > 0) {
                    SLog.dWithTag("ConvActivity;RTC", "Subscribing to conversation=" + ConversationActivity.this.mConversationId);
                    ConversationActivity.this.mClient.subscribeConversation(ConversationActivity.this.mConversationId);
                }
            }

            @Override // co.vine.android.client.AppSessionListener
            public void onWebSocketDisconnected() {
                showTypingIndicator(false, -1L);
                ConversationActivity.this.reconnectWithBackoff();
            }

            @Override // co.vine.android.client.AppSessionListener
            public void onReceiveRTCMessage(ArrayList<VineRTCConversation> data) {
                Iterator<VineRTCConversation> it = data.iterator();
                while (it.hasNext()) {
                    VineRTCConversation conversation = it.next();
                    if (conversation.conversationId == ConversationActivity.this.mConversationId) {
                        ArrayList<VineRTCParticipant> participants = conversation.participants;
                        Iterator<VineRTCParticipant> it2 = participants.iterator();
                        while (it2.hasNext()) {
                            VineRTCParticipant participant = it2.next();
                            long userId = participant.userId;
                            if (participant.isTyping) {
                                showTypingIndicator(true, userId);
                            } else {
                                showTypingIndicator(false, userId);
                            }
                            long lastMessageId = participant.lastMessageId;
                            if (lastMessageId > 0) {
                                if (lastMessageId > ConversationActivity.this.mLastMessageId) {
                                    ConversationActivity.this.mAppController.fetchConversation(1, false, 0L, ConversationActivity.this.mConversationRowId, false);
                                    Fragment fragment = ConversationActivity.this.getSupportFragmentManager().findFragmentByTag("conversationFragment");
                                    if (fragment instanceof ConversationFragment) {
                                        ((ConversationFragment) fragment).onNewMessage();
                                    }
                                }
                                ConversationActivity.this.mLastMessageId = lastMessageId;
                            }
                        }
                    }
                }
            }

            private void showTypingIndicator(final boolean show, final long userId) {
                ConversationActivity.this.runOnUiThread(new Runnable() { // from class: co.vine.android.ConversationActivity.7.1
                    @Override // java.lang.Runnable
                    public void run() {
                        Fragment fragment = ConversationActivity.this.getSupportFragmentManager().findFragmentByTag("conversationFragment");
                        if (fragment instanceof ConversationFragment) {
                            ((ConversationFragment) fragment).showTypingIndicator(show, userId);
                        }
                    }
                });
            }
        };
        Components.userInteractionsComponent().addListener(new UserInteractionsListener() { // from class: co.vine.android.ConversationActivity.8
            @Override // co.vine.android.service.components.userinteraction.UserInteractionsListener
            public void onFollowComplete(String reqId, int statusCode, String reasonPhrase, long userId) {
                if (statusCode == 200 && ConversationActivity.this.mDirectUserId == userId && ConversationActivity.this.mMenu != null) {
                    ConversationActivity.this.mMenu.removeItem(R.id.follow_user);
                }
            }
        });
    }
}
