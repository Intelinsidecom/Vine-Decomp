package co.vine.android.network;

/* loaded from: classes.dex */
public class TransferProgressEvent {
    private long bytesTransfered;

    public TransferProgressEvent(int bytesTransfered) {
        this.bytesTransfered = bytesTransfered;
    }

    public void setBytesTransfered(long bytesTransfered) {
        this.bytesTransfered = bytesTransfered;
    }

    public long getBytesTransfered() {
        return this.bytesTransfered;
    }
}
