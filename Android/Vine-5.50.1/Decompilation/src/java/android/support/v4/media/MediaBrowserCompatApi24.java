package android.support.v4.media;

import android.media.browse.MediaBrowser;
import android.os.Bundle;
import android.os.Parcel;
import android.support.v4.media.MediaBrowserCompatApi21;
import java.util.List;

/* loaded from: classes2.dex */
class MediaBrowserCompatApi24 {

    interface SubscriptionCallback extends MediaBrowserCompatApi21.SubscriptionCallback {
        void onChildrenLoaded(String str, List<Parcel> list, Bundle bundle);

        void onError(String str, Bundle bundle);
    }

    MediaBrowserCompatApi24() {
    }

    public static Object createSubscriptionCallback(SubscriptionCallback callback) {
        return new SubscriptionCallbackProxy(callback);
    }

    public static void subscribe(Object browserObj, String parentId, Bundle options, Object subscriptionCallbackObj) {
        ((MediaBrowser) browserObj).subscribe(parentId, options, (MediaBrowser.SubscriptionCallback) subscriptionCallbackObj);
    }

    public static void unsubscribe(Object browserObj, String parentId, Object subscriptionCallbackObj) {
        ((MediaBrowser) browserObj).unsubscribe(parentId, (MediaBrowser.SubscriptionCallback) subscriptionCallbackObj);
    }

    static class SubscriptionCallbackProxy<T extends SubscriptionCallback> extends MediaBrowserCompatApi21.SubscriptionCallbackProxy<T> {
        public SubscriptionCallbackProxy(T callback) {
            super(callback);
        }

        @Override // android.media.browse.MediaBrowser.SubscriptionCallback
        public void onChildrenLoaded(String parentId, List<MediaBrowser.MediaItem> children, Bundle options) {
            ((SubscriptionCallback) this.mSubscriptionCallback).onChildrenLoaded(parentId, itemListToParcelList(children), options);
        }

        @Override // android.media.browse.MediaBrowser.SubscriptionCallback
        public void onError(String parentId, Bundle options) {
            ((SubscriptionCallback) this.mSubscriptionCallback).onError(parentId, options);
        }
    }
}
