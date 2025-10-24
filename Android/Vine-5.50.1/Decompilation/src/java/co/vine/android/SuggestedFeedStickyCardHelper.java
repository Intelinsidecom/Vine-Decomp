package co.vine.android;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import co.vine.android.api.TimelineItem;
import co.vine.android.api.TimelineItemType;
import co.vine.android.api.VineMosaic;
import co.vine.android.api.VinePost;
import co.vine.android.client.AppController;
import co.vine.android.feedadapter.viewholder.PostMosaicViewHolder;
import co.vine.android.util.LinkDispatcher;
import co.vine.android.util.ResourceLoader;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public class SuggestedFeedStickyCardHelper {
    public void initDiscoverStickyHeader(final View stickyHeader, final View stickyPlaceholder, final Context context, final AppController appController, Handler handler, final VineMosaic stickyCardItem) {
        stickyPlaceholder.setVisibility(0);
        stickyHeader.setVisibility(0);
        int[] location = {0, 0};
        stickyPlaceholder.getLocationOnScreen(location);
        stickyHeader.setTop(location[1]);
        ((TextView) stickyHeader.findViewById(R.id.title)).setText(stickyCardItem.title);
        ((TextView) stickyHeader.findViewById(R.id.description)).setText(stickyCardItem.description);
        handler.postDelayed(new Runnable() { // from class: co.vine.android.SuggestedFeedStickyCardHelper.1
            @Override // java.lang.Runnable
            public void run() {
                ArrayList<String> thumbnailUrls = new ArrayList<>(stickyCardItem.mosaicItems.size());
                Iterator<TimelineItem> it = stickyCardItem.mosaicItems.iterator();
                while (it.hasNext()) {
                    TimelineItem item = it.next();
                    if (item.getType() == TimelineItemType.POST) {
                        thumbnailUrls.add(((VinePost) item).thumbnailUrl);
                    }
                }
                PostMosaicViewHolder mvh = new PostMosaicViewHolder(stickyHeader);
                ResourceLoader bitmapLoader = new ResourceLoader(context, appController);
                for (int i = 0; i < mvh.getThumbnailCount(); i++) {
                    ImageView thumbnail = mvh.thumbnails.get(i);
                    if (thumbnail != null && thumbnailUrls.size() > i) {
                        bitmapLoader.setImageWhenLoaded(new ResourceLoader.ImageViewImageSetter(thumbnail), thumbnailUrls.get(i));
                    }
                }
            }
        }, 300L);
        View mCloseButton = stickyHeader.findViewById(R.id.close_button);
        mCloseButton.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.SuggestedFeedStickyCardHelper.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                stickyPlaceholder.setVisibility(8);
                stickyHeader.setVisibility(8);
            }
        });
        stickyHeader.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.SuggestedFeedStickyCardHelper.3
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                String url = stickyCardItem.link;
                if (url != null && !"".equals(url)) {
                    LinkDispatcher.dispatch(url, context);
                }
            }
        });
    }
}
