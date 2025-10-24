package co.vine.android.share.activities;

import co.vine.android.api.VineChannel;
import co.vine.android.api.VineEntity;
import co.vine.android.api.VineRecipient;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class PostShareParameters {
    public String caption;
    public ArrayList<VineEntity> captionEntities;
    public VineChannel channel;
    public List<VineRecipient> recipients;
    public Boolean shareToFacebook;
    public Boolean shareToTumblr;
    public Boolean shareToTwitter;
    public Boolean shareToVine;
    public String venueId;
    public String venueName;

    PostShareParameters() {
    }

    public PostShareParameters(String caption, ArrayList<VineEntity> captionEntities, String venueName, String venueId) {
        this(caption, captionEntities, venueName, venueId, null, null, null, null, null, null);
    }

    public PostShareParameters(String caption, ArrayList<VineEntity> captionEntities, String venueName, String venueId, VineChannel channel, List<VineRecipient> recipients, Boolean shareToVine, Boolean shareToTwitter, Boolean shareToFacebook, Boolean shareToTumblr) {
        this.caption = caption;
        this.captionEntities = captionEntities;
        this.venueName = venueName;
        this.venueId = venueId;
        this.channel = channel;
        this.recipients = recipients;
        this.shareToVine = shareToVine;
        this.shareToTwitter = shareToTwitter;
        this.shareToFacebook = shareToFacebook;
        this.shareToTumblr = shareToTumblr;
    }
}
