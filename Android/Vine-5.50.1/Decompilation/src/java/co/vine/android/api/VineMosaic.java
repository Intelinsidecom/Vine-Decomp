package co.vine.android.api;

import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public class VineMosaic implements TimelineItem {
    public String avatarUrl;
    public String description;
    public String link;
    public ArrayList<TimelineItem> mosaicItems;
    public String mosaicType;
    public String originUrl;
    public Boolean pinnable = false;
    public String reference;
    public TimelineItemType timelineItemType;
    public String title;
    public String type;

    VineMosaic() {
    }

    @Override // co.vine.android.api.TimelineItem
    public long getId() {
        long resultId = 0;
        Iterator<TimelineItem> it = this.mosaicItems.iterator();
        while (it.hasNext()) {
            TimelineItem item = it.next();
            resultId += item.getId();
        }
        long resultId2 = (resultId / this.mosaicItems.size()) + this.title.hashCode() + this.link.hashCode() + this.reference.hashCode() + this.avatarUrl.hashCode() + this.description.hashCode() + this.type.hashCode() + this.mosaicType.hashCode();
        return resultId2 < 0 ? resultId2 : resultId2 * (-1);
    }

    @Override // co.vine.android.api.TimelineItem
    public TimelineItemType getType() {
        return this.timelineItemType;
    }
}
