package co.vine.android.scribe;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.text.TextUtils;
import co.vine.android.VineLoggingException;
import co.vine.android.api.VineError;
import co.vine.android.api.VineParserReader;
import co.vine.android.client.VineAPI;
import co.vine.android.network.NetworkOperation;
import co.vine.android.network.NetworkOperationFactory;
import co.vine.android.network.NetworkOperationReader;
import co.vine.android.network.VineNetworkUtils;
import co.vine.android.scribe.model.ClientEvent;
import co.vine.android.scribe.model.ClientEvents;
import co.vine.android.util.CrashUtil;
import com.bluelinelabs.logansquare.LoganSquare;
import java.io.IOException;
import java.util.ArrayList;
import org.parceler.Parcels;

/* loaded from: classes.dex */
public final class ScribeService extends Service {
    private Messenger mMessenger;
    private volatile Handler mScribeServiceHandler;
    private volatile ScribeServiceHandlerCallback mScribeServiceHandlerCallback;
    private volatile Looper mServiceLooper;
    private int mStartId;
    private Runnable mTimedRunnable;

    private final class ScribeServiceHandlerCallback implements Handler.Callback {
        private long mLastFlushTimeMs = System.currentTimeMillis();
        private ArrayList<ClientEvent> mEvents = new ArrayList<>();

        public ScribeServiceHandlerCallback() {
        }

        @Override // android.os.Handler.Callback
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    ScribeService.this.stopSelf(ScribeService.this.mStartId);
                    break;
                case 1:
                    Bundle data = msg.getData();
                    data.setClassLoader(ScribeService.this.getClassLoader());
                    ClientEvent event = (ClientEvent) Parcels.unwrap(msg.getData().getParcelable("event"));
                    this.mEvents.add(event);
                    if (this.mEvents.size() >= 10 && System.currentTimeMillis() - this.mLastFlushTimeMs >= 30000) {
                        flush();
                        break;
                    }
                    break;
            }
            return true;
        }

        public void flush() {
            this.mLastFlushTimeMs = System.currentTimeMillis();
            if (!this.mEvents.isEmpty()) {
                ScribeService.this.onHandleEvents(this.mEvents);
                this.mEvents.clear();
            }
        }
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        HandlerThread thread = new HandlerThread("ScribeService");
        thread.start();
        this.mServiceLooper = thread.getLooper();
        this.mScribeServiceHandlerCallback = new ScribeServiceHandlerCallback();
        this.mScribeServiceHandler = new Handler(this.mServiceLooper, this.mScribeServiceHandlerCallback);
        this.mMessenger = new Messenger(this.mScribeServiceHandler);
        this.mTimedRunnable = new Runnable() { // from class: co.vine.android.scribe.ScribeService.1
            @Override // java.lang.Runnable
            public void run() {
                ScribeService.this.mScribeServiceHandlerCallback.flush();
                ScribeService.this.mScribeServiceHandler.postDelayed(this, 60000L);
            }
        };
        this.mScribeServiceHandler.postDelayed(this.mTimedRunnable, 60000L);
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.mStartId = startId;
        return 2;
    }

    @Override // android.app.Service
    public void onDestroy() {
        this.mServiceLooper.quit();
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return this.mMessenger.getBinder();
    }

    protected void onHandleEvents(ArrayList<ClientEvent> clientEvents) {
        try {
            postClientEvents(clientEvents);
        } catch (IOException e) {
            CrashUtil.logOrThrowInDebug(e);
        }
    }

    private NetworkOperation postClientEvents(ArrayList<ClientEvent> clientEvents) throws IOException {
        ClientEvents events = new ClientEvents();
        events.events = clientEvents;
        VineAPI api = VineAPI.getInstance(this);
        StringBuilder url = VineAPI.buildUponUrl(api.getBaseUrl(), "jot");
        NetworkOperationFactory<VineAPI> networkOperationFactory = VineNetworkUtils.getDefaultNetworkOperationFactory();
        String serializedEvents = LoganSquare.serialize(events);
        VineParserReader vp = VineParserReader.createParserReader(1);
        NetworkOperation op = networkOperationFactory.createBasicAuthJsonPostRequest((Context) this, url, (StringBuilder) api, serializedEvents, (NetworkOperationReader) vp).execute();
        if (!op.isOK()) {
            VineError error = (VineError) vp.getParsedObject();
            if (error != null) {
                StringBuilder errorSb = new StringBuilder();
                errorSb.append(!TextUtils.isEmpty(error.getMessage()) ? error.getMessage() : "Unable to write to scribe due to API error!");
                errorSb.append(" Status code: " + op.statusCode);
                errorSb.append(" Events JSON: ");
                errorSb.append(serializedEvents);
                CrashUtil.logOrThrowInDebug(new VineLoggingException(errorSb.toString()));
            } else if (op.statusCode != 0) {
                CrashUtil.logOrThrowInDebug(new VineLoggingException("Unable to write scribe event due to unknown error. Code: " + Integer.toString(op.statusCode)));
            } else {
                CrashUtil.log("Unknown error while writing to Scribe! Possibly a bad network connection.");
            }
        }
        return op;
    }
}
