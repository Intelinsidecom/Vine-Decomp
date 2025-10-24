package co.vine.android.util;

/* loaded from: classes.dex */
public abstract class LinkSuppressor {
    public boolean shouldSuppressUserLink(long id) {
        return false;
    }

    public boolean shouldSuppressVenueLink(String fourSquareVenueId) {
        return false;
    }

    public boolean shouldSuppressTagLink(String tag) {
        return false;
    }
}
