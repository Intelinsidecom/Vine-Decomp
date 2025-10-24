package co.vine.android.provider;

/* loaded from: classes.dex */
public class DbResponse {
    public final int code;
    public final int numInserted;
    public final int numUpdated;

    public DbResponse(int numInserted, int numUpdated, int code) {
        this.numInserted = numInserted;
        this.numUpdated = numUpdated;
        this.code = code;
    }

    public String toString() {
        return "<DbResponse> inserted: " + this.numInserted + " updated: " + this.numUpdated + " responseCode: " + this.code;
    }
}
