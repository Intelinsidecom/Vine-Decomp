package co.vine.android.api;

import android.os.Parcel;
import android.os.Parcelable;
import co.vine.android.model.VineTag;

/* loaded from: classes.dex */
final class AutoParcel_SearchResult extends SearchResult {
    private final ListSection<VineChannel> channels;
    private final boolean hasResults;
    private final ListSection<VinePost> posts;
    private final ListSection<VineSearchSuggestion> suggestions;
    private final ListSection<VineTag> tags;
    private final ListSection<VineUser> users;
    public static final Parcelable.Creator<AutoParcel_SearchResult> CREATOR = new Parcelable.Creator<AutoParcel_SearchResult>() { // from class: co.vine.android.api.AutoParcel_SearchResult.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AutoParcel_SearchResult createFromParcel(Parcel in) {
            return new AutoParcel_SearchResult(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AutoParcel_SearchResult[] newArray(int size) {
            return new AutoParcel_SearchResult[size];
        }
    };
    private static final ClassLoader CL = AutoParcel_SearchResult.class.getClassLoader();

    AutoParcel_SearchResult(ListSection<VineSearchSuggestion> suggestions, ListSection<VineUser> users, ListSection<VineTag> tags, ListSection<VinePost> posts, ListSection<VineChannel> channels, boolean hasResults) {
        this.suggestions = suggestions;
        this.users = users;
        this.tags = tags;
        this.posts = posts;
        this.channels = channels;
        this.hasResults = hasResults;
    }

    @Override // co.vine.android.api.SearchResult
    public ListSection<VineSearchSuggestion> getSuggestions() {
        return this.suggestions;
    }

    @Override // co.vine.android.api.SearchResult
    public ListSection<VineUser> getUsers() {
        return this.users;
    }

    @Override // co.vine.android.api.SearchResult
    public ListSection<VineTag> getTags() {
        return this.tags;
    }

    @Override // co.vine.android.api.SearchResult
    public ListSection<VinePost> getPosts() {
        return this.posts;
    }

    @Override // co.vine.android.api.SearchResult
    public ListSection<VineChannel> getChannels() {
        return this.channels;
    }

    @Override // co.vine.android.api.SearchResult
    public boolean getHasResults() {
        return this.hasResults;
    }

    public String toString() {
        return "SearchResult{suggestions=" + this.suggestions + ", users=" + this.users + ", tags=" + this.tags + ", posts=" + this.posts + ", channels=" + this.channels + ", hasResults=" + this.hasResults + "}";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SearchResult)) {
            return false;
        }
        SearchResult that = (SearchResult) o;
        if (this.suggestions != null ? this.suggestions.equals(that.getSuggestions()) : that.getSuggestions() == null) {
            if (this.users != null ? this.users.equals(that.getUsers()) : that.getUsers() == null) {
                if (this.tags != null ? this.tags.equals(that.getTags()) : that.getTags() == null) {
                    if (this.posts != null ? this.posts.equals(that.getPosts()) : that.getPosts() == null) {
                        if (this.channels != null ? this.channels.equals(that.getChannels()) : that.getChannels() == null) {
                            if (this.hasResults == that.getHasResults()) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public int hashCode() {
        int h = 1 * 1000003;
        return ((((((((((h ^ (this.suggestions == null ? 0 : this.suggestions.hashCode())) * 1000003) ^ (this.users == null ? 0 : this.users.hashCode())) * 1000003) ^ (this.tags == null ? 0 : this.tags.hashCode())) * 1000003) ^ (this.posts == null ? 0 : this.posts.hashCode())) * 1000003) ^ (this.channels != null ? this.channels.hashCode() : 0)) * 1000003) ^ (this.hasResults ? 1231 : 1237);
    }

    private AutoParcel_SearchResult(Parcel in) {
        this((ListSection) in.readValue(CL), (ListSection) in.readValue(CL), (ListSection) in.readValue(CL), (ListSection) in.readValue(CL), (ListSection) in.readValue(CL), ((Boolean) in.readValue(CL)).booleanValue());
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.suggestions);
        dest.writeValue(this.users);
        dest.writeValue(this.tags);
        dest.writeValue(this.posts);
        dest.writeValue(this.channels);
        dest.writeValue(Boolean.valueOf(this.hasResults));
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }
}
