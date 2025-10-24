package co.vine.android.util;

import android.content.Context;
import android.content.SharedPreferences;
import co.vine.android.StandalonePreference;

/* loaded from: classes.dex */
public class StringAnchorManager {
    private final SharedPreferences mPrefs;
    private final String mType;

    public StringAnchorManager(Context context, String type) {
        this.mPrefs = StandalonePreference.STRING_ANCHORS.getPref(context);
        this.mType = type;
    }

    public String getStringAnchor() {
        return this.mPrefs.getString("anchor_" + this.mType, null);
    }

    public long getRefreshTime() {
        return this.mPrefs.getLong("refresh_time_" + this.mType, -1L);
    }

    public long getIndex() {
        return this.mPrefs.getLong("index_" + this.mType, -1L);
    }

    public long getLastId() {
        return this.mPrefs.getLong("lastId_" + this.mType, -1L);
    }

    public boolean haveMore() {
        return getLastId() != 0;
    }

    public static class Editor {
        private final SharedPreferences.Editor mEditor;
        private final String mType;

        public Editor(SharedPreferences.Editor edit, String type) {
            this.mEditor = edit;
            this.mType = type;
        }

        public void setRefreshTime(long refreshTime) {
            this.mEditor.putLong("refresh_time_" + this.mType, refreshTime);
        }

        public void setAnchor(String anchor) {
            this.mEditor.putString("anchor_" + this.mType, anchor);
        }

        public void setIndex(long index) {
            this.mEditor.putLong("index_" + this.mType, index);
        }

        public void setLastId(long lastId) {
            this.mEditor.putLong("lastId_" + this.mType, lastId);
        }

        public void commit() {
            this.mEditor.commit();
        }
    }

    public Editor edit() {
        return new Editor(this.mPrefs.edit(), this.mType);
    }
}
