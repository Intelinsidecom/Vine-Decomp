package co.vine.android;

import android.content.Intent;
import co.vine.android.client.AppController;
import co.vine.android.service.components.Components;

/* loaded from: classes.dex */
public class ShareRequestHandler {
    public static String handlePostShareResult(Intent data, AppController appController) {
        if (data == null) {
            return null;
        }
        long postId = data.getLongExtra("post_id", 0L);
        long repostId = data.getLongExtra("repost_id", 0L);
        boolean following = data.getBooleanExtra("following", false);
        String username = data.getStringExtra("username");
        long myRepostId = data.getLongExtra("my_repost_id", 0L);
        if (data.getBooleanExtra("revine", false)) {
            String request = Components.postActionsComponent().revine(appController, null, postId, repostId, username);
            return request;
        }
        String request2 = Components.postActionsComponent().unRevine(appController, null, postId, myRepostId, repostId, following, true);
        return request2;
    }
}
