package co.vine.android.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.Process;
import android.os.RemoteException;
import co.vine.android.Conversation;
import co.vine.android.VineWebSocketClient;
import co.vine.android.api.VineParsers;
import co.vine.android.api.VineRTCConversation;
import co.vine.android.client.VineAPI;
import co.vine.android.util.CrashUtil;
import co.vine.android.util.CrossConstants;
import com.codebutler.android_websockets.WebSocketClient;
import com.edisonwang.android.slog.SLog;
import com.fasterxml.jackson.core.JsonParser;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.http.message.BasicNameValuePair;

/* loaded from: classes.dex */
public class RealTimeChatService extends Service {
    private WebSocketClient mClient;
    private ExecutorService mExecutor;
    private Handler mMainHandler;
    private Messenger mMessenger;
    private long mReconnectDelay;
    private String mRtcUrl;
    private WebSocketClient.Listener mWebSocketListener = new WebSocketClient.Listener() { // from class: co.vine.android.service.RealTimeChatService.1
        @Override // com.codebutler.android_websockets.WebSocketClient.Listener
        public void onConnect() {
            SLog.dWithTag("RTCService", "Connected - now ready to subscribe to conversations.");
            RealTimeChatService.this.removeReconnectCallback();
            RealTimeChatService.this.broadcastEvent(1, null);
        }

        @Override // com.codebutler.android_websockets.WebSocketClient.Listener
        public void onMessage(String message) throws NumberFormatException {
            SLog.dWithTag("RTCService", String.format("Got string message: %s", message));
            try {
                JsonParser p = VineParsers.createParser(message);
                ArrayList<VineRTCConversation> data = VineParsers.parseRTCEvent(p);
                Bundle b = new Bundle();
                b.putParcelableArrayList("data", data);
                RealTimeChatService.this.broadcastEvent(9, b);
            } catch (IOException e) {
                SLog.e("Failed to parse message.", (Throwable) e);
            }
        }

        @Override // com.codebutler.android_websockets.WebSocketClient.Listener
        public void onDisconnect(int code, String reason) {
            SLog.dWithTag("RTCService", String.format("Disconnected with code=%d, reason=%s, delay=%dms", Integer.valueOf(code), reason, Long.valueOf(RealTimeChatService.this.mReconnectDelay)));
            Bundle b = new Bundle();
            b.putInt("code", code);
            b.putString("reason", reason);
            RealTimeChatService.this.broadcastEvent(5, b);
        }

        @Override // com.codebutler.android_websockets.WebSocketClient.Listener
        public void onError(Exception error) {
            SLog.dWithTag("RTCService", "Error: " + error.getMessage());
        }

        @Override // com.codebutler.android_websockets.WebSocketClient.Listener
        public void onMessage(byte[] data) {
        }
    };
    private final Runnable mReconnectRunnable = new Runnable() { // from class: co.vine.android.service.RealTimeChatService.2
        @Override // java.lang.Runnable
        public void run() {
            if (RealTimeChatService.this.mClient != null && !RealTimeChatService.this.clientIsActive()) {
                RealTimeChatService.this.mClient.connect();
                RealTimeChatService.this.mReconnectDelay = RealTimeChatService.this.mReconnectDelay < 16000 ? RealTimeChatService.this.mReconnectDelay * 2 : RealTimeChatService.this.mReconnectDelay;
                RealTimeChatService.this.mMainHandler.postDelayed(RealTimeChatService.this.mReconnectRunnable, RealTimeChatService.this.mReconnectDelay);
                SLog.dWithTag("RTCService", "Posted reconnect with delay=" + RealTimeChatService.this.mReconnectDelay + "ms");
            }
        }
    };

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        this.mMainHandler = new Handler(Looper.getMainLooper());
        this.mExecutor = Executors.newCachedThreadPool();
        this.mMessenger = new Messenger(new VineServiceHandler());
        this.mRtcUrl = VineAPI.getInstance(this).getRtcUrl();
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int flags, int startId) {
        return 2;
    }

    private synchronized void disconnect() {
        WebSocketClient client = this.mClient;
        if (client != null) {
            client.disconnect();
            this.mClient = null;
        }
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        SLog.dWithTag("RTCService", "rtc service was bound");
        return this.mMessenger.getBinder();
    }

    public synchronized void prepareClient(String sessionKey) {
        if (!clientIsActive()) {
            SLog.d("RTCService", "preparing client now");
            VineAPI api = VineAPI.getInstance(this);
            URI uri = URI.create(this.mRtcUrl);
            List<BasicNameValuePair> extraHeaders = Arrays.asList(new BasicNameValuePair("vine-session-id", sessionKey), new BasicNameValuePair("X-Vine-Client", api.getVineClientHeader()));
            SLog.dWithTag("RTCService", "Creating client: sessionKey=" + sessionKey + ", uri=" + uri.toString());
            WebSocketClient client = new WebSocketClient(uri, this.mWebSocketListener, extraHeaders);
            this.mClient = client;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void broadcastEvent(int actionCode, Bundle extras) {
        Intent intent = new Intent("co.vine.android.rtc.WEBSOCKET_EVENT");
        if (extras != null) {
            intent.putExtras(extras);
        }
        intent.putExtra("action_code", actionCode);
        sendBroadcast(intent, CrossConstants.BROADCAST_PERMISSION);
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
            com.codebutler.android_websockets.WebSocketClient r0 = r2.mClient     // Catch: java.lang.Throwable -> L10
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
        throw new UnsupportedOperationException("Method not decompiled: co.vine.android.service.RealTimeChatService.clientIsActive():boolean");
    }

    private synchronized String getPayload(Long conversationId, Conversation conversation) {
        return VineWebSocketClient.getPayload(conversationId, conversation);
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
        removeReconnectCallback();
        disconnect();
    }

    class VineServiceHandler extends Handler {
        VineServiceHandler() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            msg.getData().setClassLoader(RealTimeChatService.this.getClassLoader());
            RealTimeChatService.this.mExecutor.execute(RealTimeChatService.this.new ExecutionRunnable(msg.arg1, msg.getData(), msg.replyTo));
        }
    }

    private class ExecutionRunnable implements Runnable {
        private final int mActionCode;
        private final Bundle mBundle;
        private final Messenger mResponder;

        public ExecutionRunnable(int actionCode, Bundle bundle, Messenger messenger) {
            this.mActionCode = actionCode;
            this.mBundle = bundle;
            this.mResponder = messenger;
        }

        @Override // java.lang.Runnable
        public void run() throws SecurityException, RemoteException, IllegalArgumentException {
            Process.setThreadPriority(10);
            RealTimeChatService.this.executeAction(this.mActionCode, this.mBundle);
            if (this.mBundle.getBoolean("respond", true)) {
                Message msg = Message.obtain();
                msg.what = this.mActionCode;
                msg.setData(this.mBundle);
                try {
                    this.mResponder.send(msg);
                } catch (Exception e) {
                    CrashUtil.logException(e, "Error sending service response", new Object[0]);
                }
            }
        }
    }

    private void send(String payload) {
        try {
            WebSocketClient client = this.mClient;
            if (client != null && clientIsActive()) {
                client.send(payload);
            }
        } catch (IllegalStateException e) {
            CrashUtil.log(e.getMessage());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeReconnectCallback() {
        this.mMainHandler.removeCallbacks(this.mReconnectRunnable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void executeAction(int actionCode, Bundle b) {
        switch (actionCode) {
            case 1:
                String sessionKey = b.getString("s_key");
                removeReconnectCallback();
                prepareClient(sessionKey);
                if (!this.mClient.isConnected()) {
                    this.mClient.connect();
                    break;
                }
                break;
            case 2:
                String sessionKey2 = b.getString("s_key");
                prepareClient(sessionKey2);
                removeReconnectCallback();
                this.mReconnectDelay = 1000L;
                this.mMainHandler.postDelayed(this.mReconnectRunnable, this.mReconnectDelay);
                break;
            case 3:
                Long conversationId = Long.valueOf(b.getLong("conversation_id"));
                Conversation conversation = new Conversation.Builder().setConnected().build();
                send(getPayload(conversationId, conversation));
                break;
            case 4:
                Long conversationId2 = Long.valueOf(b.getLong("conversation_id"));
                Conversation conversation2 = new Conversation.Builder().build();
                send(getPayload(conversationId2, conversation2));
                break;
            case 5:
                disconnect();
                break;
            case 7:
                boolean isTyping = b.getBoolean("is_typing");
                Long conversationId3 = Long.valueOf(b.getLong("conversation_id"));
                Conversation conversation3 = new Conversation.Builder().setConnected().setTyping(isTyping).build();
                send(getPayload(conversationId3, conversation3));
                break;
            case 8:
                long conversationId4 = b.getLong("conversation_id");
                long lastMessageId = b.getLong("last_message_id");
                Conversation conversation4 = new Conversation.Builder().setConnected().setLastMessageId(lastMessageId).build();
                send(getPayload(Long.valueOf(conversationId4), conversation4));
                break;
        }
    }
}
