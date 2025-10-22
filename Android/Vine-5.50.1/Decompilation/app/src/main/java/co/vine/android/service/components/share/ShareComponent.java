package co.vine.android.service.components.share;

import android.content.Context;
import android.os.Bundle;
import co.vine.android.api.PostInfo;
import co.vine.android.api.VineRecipient;
import co.vine.android.api.VineUpload;
import co.vine.android.client.AppController;
import co.vine.android.service.VineServiceActionMapProvider;
import co.vine.android.service.components.NotifiableComponent;
import co.vine.android.social.TumblrHelper;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class ShareComponent extends NotifiableComponent<Actions, ShareActionListener> {

    protected enum Actions {
        SHARE_VM,
        SHARE_NETWORK
    }

    @Override // co.vine.android.service.components.VineServiceComponent
    public void registerActions(VineServiceActionMapProvider.Builder builder) {
        registerAsActionCode(builder, Actions.SHARE_VM, new VMShareAction(), new VMShareActionNotifier(this.mListeners));
        registerAsActionCode(builder, Actions.SHARE_NETWORK, new NetworkShareAction(), new NetworkShareActionNotifier(this.mListeners));
    }

    public String shareVM(AppController appController, ArrayList<VineRecipient> recipients, long postId, String remoteVideoUrl, String videoThumbnailUrl, String message) {
        VineUpload upload = new VineUpload();
        upload.mergedMessageId = -1L;
        upload.isPrivate = true;
        upload.status = 1;
        upload.conversationRowId = -1L;
        upload.videoUrl = remoteVideoUrl;
        PostInfo postInfo = new PostInfo("", false, false, false, "", -1L, null, message, postId, remoteVideoUrl, videoThumbnailUrl, System.currentTimeMillis(), recipients);
        upload.postInfo = postInfo.toString();
        Bundle b = appController.createServiceBundle();
        b.putLong("post_id", postId);
        b.putString("message", message);
        b.putParcelable("upload", upload);
        return executeServiceAction(appController, Actions.SHARE_VM, b);
    }

    public String sharePost(Context context, AppController appController, String network, String message, long postId) {
        Bundle b = appController.createServiceBundle();
        b.putString("network", network);
        b.putString("message", message);
        b.putLong("post_id", postId);
        if ("tumblr".equalsIgnoreCase(network)) {
            b.putString("token", TumblrHelper.getTumblrToken(context));
            b.putString("secret", TumblrHelper.getTumblrSecret(context));
        }
        return executeServiceAction(appController, Actions.SHARE_NETWORK, b);
    }
}
