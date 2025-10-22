package co.vine.android.service.components.suggestions;

import android.os.Bundle;
import android.text.TextUtils;
import co.vine.android.client.AppController;
import co.vine.android.service.VineServiceActionMapProvider;
import co.vine.android.service.components.NotifiableComponent;

/* loaded from: classes.dex */
public final class SuggestionsComponent extends NotifiableComponent<Actions, SuggestionsActionListener> {

    protected enum Actions {
        FETCH_FRIENDS_TYPEAHEAD,
        FETCH_USERS_TYPEAHEAD,
        FETCH_TAGS_TYPEAHEAD,
        FETCH_SUGGESTED_USERS
    }

    public static long getTypeaheadThrottle() {
        return 300L;
    }

    public static boolean shouldFetchTagsTypeahead(CharSequence query) {
        return !TextUtils.isEmpty(query) && query.length() >= 3 && query.charAt(0) == '#';
    }

    public static boolean shouldFetchUsersTypeahead(CharSequence query) {
        return !TextUtils.isEmpty(query) && query.length() >= 2 && query.charAt(0) == '@';
    }

    @Override // co.vine.android.service.components.VineServiceComponent
    public void registerActions(VineServiceActionMapProvider.Builder builder) {
        registerAsActionCode(builder, Actions.FETCH_FRIENDS_TYPEAHEAD, new FetchFriendsTypeaheadAction(), new FetchFriendsTypeaheadActionNotifier(this.mListeners));
        registerAsActionCode(builder, Actions.FETCH_USERS_TYPEAHEAD, new FetchUsersTypeaheadAction(), new FetchUsersTypeaheadActionNotifier(this.mListeners));
        registerAsActionCode(builder, Actions.FETCH_TAGS_TYPEAHEAD, new FetchTagsTypeaheadAction(), new FetchTagsTypeaheadActionNotifier());
        registerAsActionCode(builder, Actions.FETCH_SUGGESTED_USERS, new FetchSuggestedUsersAction(), new FetchSuggestedUsersActionNotifier(this.mListeners));
    }

    public String fetchFriendsTypeAhead(AppController appController, String query) {
        if (TextUtils.isEmpty(query)) {
            return null;
        }
        Bundle b = appController.createServiceBundle();
        b.putString("q", query);
        return executeServiceAction(appController, Actions.FETCH_FRIENDS_TYPEAHEAD, b);
    }

    public String fetchUserTypeahead(AppController appController, String query) {
        if (TextUtils.isEmpty(query)) {
            return null;
        }
        Bundle bundle = appController.createServiceBundle();
        bundle.putString("q", query);
        return executeServiceAction(appController, Actions.FETCH_USERS_TYPEAHEAD, bundle);
    }

    public void fetchTags(AppController appController, String query) {
        if (!TextUtils.isEmpty(query)) {
            Bundle bundle = appController.createServiceBundle();
            bundle.putString("q", query);
            executeServiceAction(appController, Actions.FETCH_TAGS_TYPEAHEAD, bundle);
        }
    }

    public String fetchSuggestedUsers(AppController appController) {
        return executeServiceAction(appController, Actions.FETCH_SUGGESTED_USERS, appController.createServiceBundle());
    }
}
