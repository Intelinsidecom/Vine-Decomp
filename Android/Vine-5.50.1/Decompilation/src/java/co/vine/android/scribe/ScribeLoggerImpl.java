package co.vine.android.scribe;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import co.vine.android.client.VineAPI;
import co.vine.android.scribe.model.ClientEvent;
import co.vine.android.scribe.model.EventDetails;
import co.vine.android.util.ClientFlagsHelper;
import com.edisonwang.android.slog.SLog;
import org.parceler.Parcels;

/* loaded from: classes.dex */
final class ScribeLoggerImpl implements ScribeLogger {
    private final Context mContext;
    private Messenger mScribeService;

    ScribeLoggerImpl(Context context) {
        this.mContext = context;
    }

    public void initScribeService() {
        this.mContext.bindService(new Intent(this.mContext, (Class<?>) ScribeService.class), new ServiceConnection() { // from class: co.vine.android.scribe.ScribeLoggerImpl.1
            @Override // android.content.ServiceConnection
            public void onServiceConnected(ComponentName name, IBinder service) {
                ScribeLoggerImpl.this.mScribeService = new Messenger(service);
            }

            @Override // android.content.ServiceConnection
            public void onServiceDisconnected(ComponentName name) throws RemoteException {
                if (ScribeLoggerImpl.this.mScribeService != null) {
                    try {
                        ScribeLoggerImpl.this.mScribeService.send(Message.obtain(null, 0, null));
                    } catch (RemoteException ex) {
                        SLog.e("Unable to send stop message to scribe service", (Throwable) ex);
                    }
                }
                ScribeLoggerImpl.this.mScribeService = null;
            }
        }, 1);
    }

    @Override // co.vine.android.scribe.ScribeLogger
    public void logClientEvent(ClientEvent event) throws RemoteException {
        if (ClientFlagsHelper.scribeEnabled(this.mContext)) {
            try {
                if (this.mScribeService != null) {
                    Message message = Message.obtain(null, 1, null);
                    message.getData().putParcelable("event", Parcels.wrap(event));
                    this.mScribeService.send(message);
                }
            } catch (RemoteException ex) {
                SLog.e("Unable to send event message to scribe service", (Throwable) ex);
            }
        }
    }

    @Override // co.vine.android.scribe.ScribeLogger
    public ClientEvent getDefaultClientEvent() {
        ClientEvent event = new ClientEvent();
        event.eventDetails = new EventDetails();
        event.eventDetails.timestamp = Double.valueOf(System.currentTimeMillis() / 1000.0d);
        event.clientId = VineAPI.getInstance(this.mContext).getVineClientHeader();
        event.deviceData = DeviceDataUtil.getDeviceData(this.mContext);
        return event;
    }
}
