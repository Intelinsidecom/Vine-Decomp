package co.vine.android.service.components.suggestions;

import co.vine.android.api.VineUser;
import java.util.ArrayList;

/* loaded from: classes.dex */
public abstract class SuggestionsActionListener {
    public void onGetUserTypeAheadComplete(String reqId, int statusCode, String reasonPhrase, String query, ArrayList<VineUser> users) {
    }

    public void onGetFriendsTypeAheadComplete(String reqId, int statusCode, String reasonPhrase, String query, ArrayList<VineUser> users) {
    }

    public void onFetchSuggestedUsersComplete(String reqId, int statusCode, String reasonPhrase, ArrayList<VineUser> users) {
    }
}
