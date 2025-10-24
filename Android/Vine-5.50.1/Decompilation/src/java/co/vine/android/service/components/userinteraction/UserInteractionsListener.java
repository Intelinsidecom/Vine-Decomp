package co.vine.android.service.components.userinteraction;

import co.vine.android.api.VineUser;
import java.util.ArrayList;

/* loaded from: classes.dex */
public abstract class UserInteractionsListener {
    public void onFollowComplete(String reqId, int statusCode, String reasonPhrase, long userId) {
    }

    public void onUnFollowComplete(String reqId, int statusCode, String reasonPhrase, long userId) {
    }

    public void onBulkFollowComplete(String reqId, int statusCode, String reasonPhrase) {
    }

    public void onBulkFollowTwitterComplete(String reqId, int statusCode, String reasonPhrase) {
    }

    public void onGetUserFollowRecommendationsComplete(String reqId, int statusCode, String reasonPhrase, ArrayList<VineUser> users) {
    }

    public void onBulkFollowChannelsComplete(String reqId, int statusCode, String reasonPhrase) {
    }

    public void onUnsubscribeComplete(String reqId, int statusCode, String reasonPhrase, long userId) {
    }

    public void onResubscribeComplete(String reqId, int statusCode, String reasonPhrase, long userId) {
    }
}
