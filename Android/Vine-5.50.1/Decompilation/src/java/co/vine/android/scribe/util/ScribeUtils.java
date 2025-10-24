package co.vine.android.scribe.util;

import co.vine.android.api.VineMosaic;
import co.vine.android.api.VinePost;
import co.vine.android.api.VineSolicitor;
import co.vine.android.api.VineUrlAction;
import co.vine.android.api.response.VineShortPost;
import co.vine.android.scribe.model.Byline;
import co.vine.android.scribe.model.Item;
import co.vine.android.scribe.model.MosaicDetails;
import co.vine.android.scribe.model.PostOrRepostDetails;
import co.vine.android.scribe.model.UserDetails;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public final class ScribeUtils {
    public static Item getItemFromUserId(long userId) {
        Item item = new Item();
        item.itemType = Item.ItemType.USER.name();
        item.user = new UserDetails();
        item.user.userId = Long.valueOf(userId);
        return item;
    }

    public static Item getItemFromPost(VinePost post) {
        Item item = new Item();
        item.itemType = Item.ItemType.POST.name();
        item.postOrRepost = new PostOrRepostDetails();
        item.postOrRepost.postId = Long.valueOf(post.postId);
        item.postOrRepost.postAuthorId = Long.valueOf(post.userId);
        item.postOrRepost.liked = Boolean.valueOf(post.isLiked());
        item.postOrRepost.reposted = Boolean.valueOf(post.isRevined());
        if (post.repost != null) {
            item.postOrRepost.repostAuthorId = Long.valueOf(post.repost.userId);
            item.postOrRepost.repostId = Long.valueOf(post.repost.repostId);
        }
        parseActionableByline(post, item);
        if (post.longform != null) {
            item.postOrRepost.longformId = post.longform.longformId;
        }
        return item;
    }

    private static void parseActionableByline(VinePost post, Item item) {
        if (post.byline != null && post.byline.bylineAction != null) {
            item.postOrRepost.byline = new Byline();
            item.postOrRepost.byline.body = post.byline.body;
            item.postOrRepost.byline.iconUrl = post.byline.iconUrl;
            item.postOrRepost.byline.actionIconUrl = post.byline.bylineAction.actionIconUrl;
            item.postOrRepost.byline.actionTitle = post.byline.bylineAction.actionTitle;
            item.postOrRepost.byline.description = post.byline.bylineAction.description;
            item.postOrRepost.byline.detailedDescription = post.byline.bylineAction.detailedDescription;
            if (post.byline.bylineAction.records != null && post.byline.bylineAction.records.size() != 0) {
                item.postOrRepost.byline.postIds = new ArrayList<>();
                Iterator<VineShortPost> it = post.byline.bylineAction.records.iterator();
                while (it.hasNext()) {
                    VineShortPost p = it.next();
                    item.postOrRepost.byline.postIds.add(p.postId);
                }
            }
        }
    }

    public static Item getItemFromPostMosaic(VineMosaic mosaic) {
        Item item = new Item();
        item.itemType = Item.ItemType.POST_MOSAIC.name();
        item.reference = mosaic.reference;
        item.postMosaic = new MosaicDetails();
        item.postMosaic.mosaicType = mosaic.mosaicType;
        item.postMosaic.link = mosaic.link;
        return item;
    }

    public static Item getItemFromUserMosaic(VineMosaic mosaic) {
        Item item = new Item();
        item.itemType = Item.ItemType.USER_MOSAIC.name();
        item.reference = mosaic.reference;
        item.userMosaic = new MosaicDetails();
        item.userMosaic.mosaicType = mosaic.mosaicType;
        item.userMosaic.link = mosaic.link;
        return item;
    }

    public static Item getItemFromUrlAction(VineUrlAction urlAction) {
        Item item = new Item();
        item.itemType = Item.ItemType.URL_ACTION.name();
        item.reference = urlAction.reference;
        return item;
    }

    public static Item getItemFromSolicitor(VineSolicitor solicitor) {
        Item item = new Item();
        item.itemType = Item.ItemType.SOLICITOR.name();
        item.reference = solicitor.reference;
        return item;
    }
}
