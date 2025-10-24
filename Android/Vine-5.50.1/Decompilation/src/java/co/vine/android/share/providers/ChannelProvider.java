package co.vine.android.share.providers;

import co.vine.android.PendingRequest;
import co.vine.android.PendingRequestHelper;
import co.vine.android.api.VineChannel;
import co.vine.android.cache.image.ImageKey;
import co.vine.android.cache.image.UrlImage;
import co.vine.android.client.AppController;
import co.vine.android.client.AppSessionListener;
import com.edisonwang.android.slog.SLog;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class ChannelProvider {
    private final AppController mAppController;
    private OnDataFetchedListener mOnDataFetchedListener;
    private final PendingRequestHelper mPendingRequestHelper = new PendingRequestHelper();
    private final List<VineChannel> mChannels = new ArrayList();
    private final AppSessionListener mAppSessionListener = new AppSessionListener() { // from class: co.vine.android.share.providers.ChannelProvider.1
        @Override // co.vine.android.client.AppSessionListener
        public void onGetChannelsComplete(String requestId, int statusCode, String reasonPhrase, ArrayList<VineChannel> channels) {
            PendingRequest request = ChannelProvider.this.mPendingRequestHelper.removeRequest(requestId);
            if (request != null) {
                if (statusCode == 200) {
                    ChannelProvider.this.mChannels.addAll(channels);
                } else {
                    SLog.i("Unable to fetch channels. Reason: {}.", reasonPhrase);
                }
                ChannelProvider.this.fireOnChannelsFetched();
            }
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onPhotoImageLoaded(HashMap<ImageKey, UrlImage> images) {
            ChannelProvider.this.fireOnChannelImagesFetched(images);
        }
    };

    public interface OnDataFetchedListener {
        void onChannelImagesFetched(Map<ImageKey, UrlImage> map);

        void onChannelsFetched(List<VineChannel> list);
    }

    public ChannelProvider(AppController appController) {
        this.mAppController = appController;
        this.mPendingRequestHelper.onCreate(appController, null);
    }

    public void onResume() {
        this.mAppController.addListener(this.mAppSessionListener);
    }

    public void onPause() {
        this.mAppController.removeListener(this.mAppSessionListener);
    }

    public void setOnDataFetchedListener(OnDataFetchedListener onDataFetchedListener) {
        this.mOnDataFetchedListener = onDataFetchedListener;
    }

    public void requestChannels() {
        if (!this.mChannels.isEmpty()) {
            fireOnChannelsFetched();
        } else if (!this.mPendingRequestHelper.hasPendingRequest()) {
            this.mPendingRequestHelper.addRequest(this.mAppController.fetchChannels(1));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fireOnChannelsFetched() {
        if (this.mOnDataFetchedListener != null) {
            this.mOnDataFetchedListener.onChannelsFetched(this.mChannels);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fireOnChannelImagesFetched(HashMap<ImageKey, UrlImage> images) {
        if (this.mOnDataFetchedListener != null) {
            this.mOnDataFetchedListener.onChannelImagesFetched(images);
        }
    }
}
