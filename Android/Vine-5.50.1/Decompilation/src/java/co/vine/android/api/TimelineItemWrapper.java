package co.vine.android.api;

import android.os.Bundle;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.Iterator;
import org.parceler.Parcels;

/* loaded from: classes.dex */
public class TimelineItemWrapper {
    public VineFeed feed;
    public VineMosaic mosaic;
    public VinePost post;
    public VineSolicitor solicitor;
    public VineUrlAction urlAction;

    public static void bundleTimelineItemList(Bundle b, ArrayList<TimelineItem> items, String key) {
        ArrayList<Parcelable> parcelablesList = new ArrayList<>();
        Iterator<TimelineItem> it = items.iterator();
        while (it.hasNext()) {
            TimelineItem i = it.next();
            TimelineItemWrapper itemWrapper = new TimelineItemWrapper();
            if (i.getType() == TimelineItemType.POST) {
                itemWrapper.post = (VinePost) i;
            } else if (i.getType() == TimelineItemType.POST_MOSAIC || i.getType() == TimelineItemType.USER_MOSAIC) {
                itemWrapper.mosaic = (VineMosaic) i;
            } else if (i.getType() == TimelineItemType.URL_ACTION) {
                itemWrapper.urlAction = (VineUrlAction) i;
            } else if (i.getType() == TimelineItemType.SOLICITOR) {
                itemWrapper.solicitor = (VineSolicitor) i;
            } else if (i.getType() == TimelineItemType.FEED) {
                itemWrapper.feed = (VineFeed) i;
            }
            parcelablesList.add(Parcels.wrap(itemWrapper));
        }
        b.putParcelableArrayList(key, parcelablesList);
    }

    public static ArrayList<TimelineItem> unbundleTimelineItemList(Bundle b, String key) {
        ArrayList<Parcelable> list = b.getParcelableArrayList(key);
        ArrayList<TimelineItem> resultList = new ArrayList<>();
        if (list != null) {
            Iterator<Parcelable> it = list.iterator();
            while (it.hasNext()) {
                Parcelable p = it.next();
                TimelineItemWrapper itemWrapper = (TimelineItemWrapper) Parcels.unwrap(p);
                if (itemWrapper.post != null) {
                    resultList.add(itemWrapper.post);
                } else if (itemWrapper.mosaic != null) {
                    resultList.add(itemWrapper.mosaic);
                } else if (itemWrapper.urlAction != null) {
                    resultList.add(itemWrapper.urlAction);
                } else if (itemWrapper.solicitor != null) {
                    resultList.add(itemWrapper.solicitor);
                } else if (itemWrapper.feed != null) {
                    resultList.add(itemWrapper.feed);
                }
            }
        }
        return resultList;
    }
}
