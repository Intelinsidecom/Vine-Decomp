package co.vine.android.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import co.vine.android.R;
import co.vine.android.cache.image.ImageKey;
import co.vine.android.cache.image.UrlImage;
import co.vine.android.drawable.RecyclableBitmapDrawable;
import co.vine.android.feedadapter.v2.ViewType;
import co.vine.android.feedadapter.viewholder.CardViewHolder;
import co.vine.android.feedadapter.viewholder.PostViewHolder;
import co.vine.android.feedadapter.viewholder.TimelineItemVideoViewHolder;
import co.vine.android.feedadapter.viewholder.ViewHolder;
import co.vine.android.feedadapter.viewmanager.CardViewManager;
import com.edisonwang.android.slog.SLogger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/* loaded from: classes.dex */
public final class FeedImageViewUtils {
    public static void setFeedImages(HashMap<ImageKey, UrlImage> images, ArrayList<CardViewHolder> holders, ArrayList<CardViewManager> managers, Context context, SLogger logger) {
        UrlImage image;
        Iterator<CardViewHolder> it = holders.iterator();
        while (it.hasNext()) {
            ViewHolder viewHolder = (CardViewHolder) it.next();
            if (viewHolder.getType() == ViewType.POST) {
                TimelineItemVideoViewHolder videoHolder = ((PostViewHolder) viewHolder).getVideoHolder();
                if (videoHolder.thumbnailKey != null && (image = images.get(videoHolder.thumbnailKey)) != null && image.isValid()) {
                    logger.d("found for video: {}", image.url);
                    setImage(videoHolder.bottomThumbnail, image.bitmap, context);
                    Iterator<CardViewManager> it2 = managers.iterator();
                    while (it2.hasNext()) {
                        CardViewManager manager = it2.next();
                        manager.onVideoImageObtained();
                    }
                }
            }
        }
    }

    private static boolean setImage(View imageView, Bitmap bitmap, Context context) {
        if (bitmap == null) {
            imageView.setBackgroundColor(context.getResources().getColor(R.color.solid_light_gray));
            return false;
        }
        ViewUtil.setBackground(imageView, new RecyclableBitmapDrawable(context.getResources(), bitmap));
        return true;
    }
}
