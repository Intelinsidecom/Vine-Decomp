package co.vine.android.search;

import android.content.Context;
import android.content.SharedPreferences;
import co.vine.android.StandalonePreference;
import java.util.LinkedList;

/* loaded from: classes.dex */
public class RecentSearchesManager {
    private SharedPreferences mPrefs;
    private LinkedList<String> mRecentSearches;

    public RecentSearchesManager(Context context) {
        this.mPrefs = StandalonePreference.RECENT_SEARCH.getPref(context);
        loadRecentSearches();
    }

    public LinkedList<String> getRecentSearches() {
        return this.mRecentSearches;
    }

    public LinkedList<String> loadRecentSearches() {
        this.mRecentSearches = new LinkedList<>();
        this.mRecentSearches.addLast(this.mPrefs.getString("recent_search_1", null));
        this.mRecentSearches.addLast(this.mPrefs.getString("recent_search_2", null));
        this.mRecentSearches.addLast(this.mPrefs.getString("recent_search_3", null));
        return this.mRecentSearches;
    }

    public void addRecentSearch(String query) {
        if (this.mRecentSearches.contains(query)) {
            this.mRecentSearches.remove(query);
            this.mRecentSearches.addFirst(query);
        } else {
            if (this.mRecentSearches.size() >= 3) {
                this.mRecentSearches.removeLast();
            }
            this.mRecentSearches.addFirst(query);
            save();
        }
    }

    public void removeRecentSearch(String query) {
        this.mRecentSearches.remove(query);
        save();
    }

    private void save() {
        SharedPreferences.Editor editor = this.mPrefs.edit();
        LinkedList<String> toSave = new LinkedList<>(this.mRecentSearches);
        editor.putString("recent_search_1", toSave.poll());
        editor.putString("recent_search_2", toSave.poll());
        editor.putString("recent_search_3", toSave.poll());
        editor.apply();
    }
}
