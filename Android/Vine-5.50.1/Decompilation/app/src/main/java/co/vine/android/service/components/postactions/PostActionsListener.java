package co.vine.android.service.components.postactions;

import co.vine.android.api.VineRepost;

/* loaded from: classes.dex */
public interface PostActionsListener {
    void onLikePost(String str, int i, String str2, long j);

    void onPostEditCaption(String str, int i, String str2);

    void onRevine(String str, int i, String str2, long j, VineRepost vineRepost);

    void onUnlikePost(String str, int i, String str2, long j);

    void onUnrevine(String str, int i, String str2, long j);
}
