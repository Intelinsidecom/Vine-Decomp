package co.vine.android.widget;

import java.util.HashSet;

/* loaded from: classes.dex */
public final class SensitiveAcknowledgments {
    private final HashSet<Long> mAcknowledgedSensitiveIds;

    public interface SensitiveItem {
        long getId();

        boolean isExplicit();
    }

    public SensitiveAcknowledgments(int maxSizeToKeepTrack) {
        this.mAcknowledgedSensitiveIds = new HashSet<>(maxSizeToKeepTrack);
    }

    public void acknowledge(long id) {
        this.mAcknowledgedSensitiveIds.add(Long.valueOf(id));
    }

    public boolean isExplicit(SensitiveItem item) {
        return (item == null || !item.isExplicit() || this.mAcknowledgedSensitiveIds.contains(Long.valueOf(item.getId()))) ? false : true;
    }
}
