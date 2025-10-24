package co.vine.android.api;

import android.os.Parcelable;
import co.vine.android.model.VineTag;

/* loaded from: classes.dex */
public abstract class SearchResult implements Parcelable {
    public static final SearchResult EMPTY_RESULT = create(null, null, null, null, null, false);

    public abstract ListSection<VineChannel> getChannels();

    public abstract boolean getHasResults();

    public abstract ListSection<VinePost> getPosts();

    public abstract ListSection<VineSearchSuggestion> getSuggestions();

    public abstract ListSection<VineTag> getTags();

    public abstract ListSection<VineUser> getUsers();

    public static SearchResult create(ListSection<VineSearchSuggestion> suggestions, ListSection<VineUser> users, ListSection<VineTag> tags, ListSection<VinePost> posts, ListSection<VineChannel> channels, boolean hasResults) {
        return new AutoParcel_SearchResult(suggestions, users, tags, posts, channels, hasResults);
    }
}
