package co.vine.android.storage.model;

import io.realm.RealmObject;

/* loaded from: classes.dex */
public class LongformData extends RealmObject {
    private long cursorTime;
    private String longformId;
    private boolean reachedEnd;
    private boolean watched;

    public String getLongformId() {
        return this.longformId;
    }

    public void setLongformId(String longformId) {
        this.longformId = longformId;
    }

    public long getCursorTime() {
        return this.cursorTime;
    }

    public void setCursorTime(long cursorTime) {
        this.cursorTime = cursorTime;
    }

    public boolean isWatched() {
        return this.watched;
    }

    public void setWatched(boolean watched) {
        this.watched = watched;
    }

    public boolean isReachedEnd() {
        return this.reachedEnd;
    }

    public void setReachedEnd(boolean reachedEnd) {
        this.reachedEnd = reachedEnd;
    }
}
