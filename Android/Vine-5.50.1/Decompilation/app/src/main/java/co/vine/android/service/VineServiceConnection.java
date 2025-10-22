package co.vine.android.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import co.vine.android.util.CrashUtil;
import co.vine.android.util.Util;
import java.util.HashMap;

/* loaded from: classes.dex */
public class VineServiceConnection implements ServiceConnection {
    private Context mContext;
    private boolean mIgnoreBundlesInResponse;
    private Messenger mResponder;
    private ServiceResponseHandler mResponseHandler;
    private Messenger mService;
    private final HashMap<String, PendingAction> mPendingQueue = new HashMap<>();
    private final HashMap<String, PendingAction> mRequestQueue = new HashMap<>();
    private final int[] mLock = new int[0];

    public interface ServiceResponseHandler {
        void handleServiceResponse(int i, int i2, String str, Bundle bundle);
    }

    public VineServiceConnection(Context context, ServiceResponseHandler handler) {
        this.mContext = context;
        this.mResponseHandler = handler;
    }

    public String queueAndExecute(int actionCode, Bundle bundle) {
        String reqId = generateRequestId();
        PendingAction action = new PendingAction(actionCode, bundle);
        bundle.putString("rid", reqId);
        synchronized (this.mLock) {
            Messenger service = this.mService;
            if (service != null) {
                this.mRequestQueue.put(reqId, action);
                sendMessage(service, getMessage(action));
            } else {
                this.mPendingQueue.put(reqId, action);
            }
        }
        return reqId;
    }

    public void cancelAll() {
        synchronized (this.mLock) {
            this.mRequestQueue.clear();
            this.mPendingQueue.clear();
        }
    }

    private Message getMessage(PendingAction action) {
        Message msg = Message.obtain();
        msg.arg1 = action.actionCode;
        msg.setData(action.bundle);
        msg.replyTo = getServiceResponder();
        return msg;
    }

    private synchronized Messenger getServiceResponder() {
        if (this.mResponder == null) {
            this.mResponder = new Messenger(new Handler(Looper.getMainLooper()) { // from class: co.vine.android.service.VineServiceConnection.1
                @Override // android.os.Handler
                public void handleMessage(Message msg) {
                    if (VineServiceConnection.this.mIgnoreBundlesInResponse) {
                        VineServiceConnection.this.mResponseHandler.handleServiceResponse(msg.what, msg.arg1, null, null);
                        return;
                    }
                    Bundle b = msg.getData();
                    b.setClassLoader(VineServiceConnection.this.mContext.getClassLoader());
                    VineServiceConnection.this.mResponseHandler.handleServiceResponse(msg.what, msg.arg1, b.getString("reason_phrase"), b);
                }
            });
        }
        return this.mResponder;
    }

    private void sendMessage(Messenger service, Message msg) {
        try {
            service.send(msg);
        } catch (Throwable e) {
            CrashUtil.logException(e, "Unable to send message to service", new Object[0]);
        }
    }

    @Override // android.content.ServiceConnection
    public void onServiceConnected(ComponentName name, IBinder binder) {
        onServiceAvailable(new Messenger(binder));
    }

    public void onServiceAvailable(Messenger service) {
        synchronized (this.mLock) {
            this.mService = service;
            this.mRequestQueue.putAll(this.mPendingQueue);
            for (PendingAction action : this.mPendingQueue.values()) {
                sendMessage(service, getMessage(action));
            }
            this.mPendingQueue.clear();
        }
    }

    @Override // android.content.ServiceConnection
    public void onServiceDisconnected(ComponentName name) {
        synchronized (this.mLock) {
            this.mService = null;
        }
    }

    public boolean isPending(String reqId) {
        boolean zContainsKey;
        synchronized (this.mLock) {
            zContainsKey = this.mRequestQueue.containsKey(reqId);
        }
        return zContainsKey;
    }

    public PendingAction remove(String reqId) {
        PendingAction action;
        synchronized (this.mLock) {
            action = this.mRequestQueue.get(reqId);
            this.mRequestQueue.remove(reqId);
        }
        return action;
    }

    public String generateRequestId() {
        return Util.randomString(6);
    }
}
