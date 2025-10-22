package co.vine.android.api.response;

import android.os.Parcel;
import android.os.Parcelable;
import org.parceler.ParcelWrapper;

/* loaded from: classes.dex */
public class ServerStatus$$Parcelable implements Parcelable, ParcelWrapper<ServerStatus> {
    public static final ServerStatus$$Parcelable$Creator$$0 CREATOR = new ServerStatus$$Parcelable$Creator$$0();
    private ServerStatus serverStatus$$0;

    public ServerStatus$$Parcelable(Parcel parcel$$0) {
        ServerStatus serverStatus$$2;
        if (parcel$$0.readInt() == -1) {
            serverStatus$$2 = null;
        } else {
            serverStatus$$2 = readco_vine_android_api_response_ServerStatus(parcel$$0);
        }
        this.serverStatus$$0 = serverStatus$$2;
    }

    public ServerStatus$$Parcelable(ServerStatus serverStatus$$4) {
        this.serverStatus$$0 = serverStatus$$4;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel$$1, int flags) {
        if (this.serverStatus$$0 == null) {
            parcel$$1.writeInt(-1);
        } else {
            parcel$$1.writeInt(1);
            writeco_vine_android_api_response_ServerStatus(this.serverStatus$$0, parcel$$1, flags);
        }
    }

    private ServerStatus readco_vine_android_api_response_ServerStatus(Parcel parcel$$2) {
        ServerStatus serverStatus$$1 = new ServerStatus();
        serverStatus$$1.uploadType = parcel$$2.readString();
        serverStatus$$1.staticTimelineUrl = parcel$$2.readString();
        serverStatus$$1.message = parcel$$2.readString();
        serverStatus$$1.status = parcel$$2.readString();
        return serverStatus$$1;
    }

    private void writeco_vine_android_api_response_ServerStatus(ServerStatus serverStatus$$3, Parcel parcel$$3, int flags$$0) {
        parcel$$3.writeString(serverStatus$$3.uploadType);
        parcel$$3.writeString(serverStatus$$3.staticTimelineUrl);
        parcel$$3.writeString(serverStatus$$3.message);
        parcel$$3.writeString(serverStatus$$3.status);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.parceler.ParcelWrapper
    public ServerStatus getParcel() {
        return this.serverStatus$$0;
    }
}
