package com.twitter.android.widget;

/* loaded from: classes.dex */
public class ItemPosition {
    public final long itemId;
    public final int offset;
    public final int position;

    public ItemPosition(int position, long itemId, int offset) {
        this.position = position;
        this.itemId = itemId;
        this.offset = offset;
    }
}
