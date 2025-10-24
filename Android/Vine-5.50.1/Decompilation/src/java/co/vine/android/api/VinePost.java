package co.vine.android.api;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.SpannableStringBuilder;
import co.vine.android.ActionableFeedComponent;
import co.vine.android.client.Session;
import co.vine.android.model.VineTag;
import co.vine.android.widget.SensitiveAcknowledgments;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class VinePost extends VinePostData implements Parcelable, TimelineItem, SensitiveAcknowledgments.SensitiveItem {
    public static final Parcelable.Creator<VinePost> CREATOR = new Parcelable.Creator<VinePost>() { // from class: co.vine.android.api.VinePost.2
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VinePost createFromParcel(Parcel in) {
            return new VinePost(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VinePost[] newArray(int size) {
            return new VinePost[size];
        }
    };
    public ArrayList<CharSequence> cachedComments;
    public CharSequence cachedDescription;
    public String customLikeIconUrl;
    public String dateString;
    public SpannableStringBuilder descriptionSb;
    public Boolean isRTL;
    private ActionableFeedComponent mActionableComponent;
    public String originUrl;
    public SpannableStringBuilder sharedVmSb;
    public SpannableStringBuilder styledUserName;
    public ArrayList<VineEntity> transientEntities;

    public VinePost() {
    }

    public VinePost(Parcel in) {
        super(in);
    }

    public void registerActionableComponent(ActionableFeedComponent component) {
        this.mActionableComponent = component;
    }

    public void deregisterActionableComponent() {
        this.mActionableComponent = null;
    }

    public void addMeLike(Session session) {
        VineLike meLike = VineLike.getMeLike(this.postId, session.getUserId(), session.getScreenName());
        addLike(meLike);
        if (this.mActionableComponent != null) {
            this.mActionableComponent.updateLikedIcon(this, true);
        }
    }

    public void addLike(VineLike like) {
        if (this.likes == null) {
            this.likes = new VinePagedData<>();
        }
        if (this.likes.items == null) {
            this.likes.items = new ArrayList<>();
        }
        if (!this.likes.items.contains(like)) {
            this.likes.items.add(like);
            if (!isLiked()) {
                this.likesCount++;
            }
        }
        setFlagLiked(true);
    }

    public void removeLike(long likeUserId) {
        if (this.likes != null && this.likes.items != null) {
            Iterator<VineLike> it = this.likes.items.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                VineLike like = it.next();
                if (like.userId == likeUserId) {
                    this.likes.items.remove(like);
                    if (this.likesCount > 0 && isLiked()) {
                        this.likesCount--;
                    }
                }
            }
        }
        setFlagLiked(false);
    }

    public void removeMeLike(long activeSessionId) {
        removeLike(activeSessionId);
        if (this.mActionableComponent != null) {
            this.mActionableComponent.updateLikedIcon(this, false);
        }
    }

    public void updateRevined(VineRepost repost, boolean revined) {
        if (revined) {
            this.revinersCount++;
            this.myRepostId = repost.repostId;
            setFlagRevined(true);
        } else {
            if (this.revinersCount > 0) {
                this.revinersCount--;
            }
            this.myRepostId = 0L;
            setFlagRevined(false);
        }
        if (this.mActionableComponent != null) {
            this.mActionableComponent.updateRevinedIcon(this, revined);
        }
    }

    public void adjustLoopCount(int newCount) {
        if (this.mActionableComponent != null) {
            this.mActionableComponent.adjustLoopCount(this, newCount);
        }
    }

    public static byte[] getBytesFromTags(VinePost post) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(bos);
        if (post.tags != null) {
            Iterator<VineTag> it = post.tags.iterator();
            while (it.hasNext()) {
                VineTag tag = it.next();
                out.writeLong(tag.getTagId());
                out.writeObject(tag.getTagName());
            }
        }
        out.flush();
        out.close();
        bos.close();
        return bos.toByteArray();
    }

    public void setUpUrls(List<VineVideoUrlTier> urls) {
        this.tieredUrls = urls;
        if (this.tieredUrls != null && this.tieredUrls.size() > 0) {
            Collections.sort(this.tieredUrls, new Comparator<VineVideoUrlTier>() { // from class: co.vine.android.api.VinePost.1
                @Override // java.util.Comparator
                public int compare(VineVideoUrlTier lhs, VineVideoUrlTier rhs) {
                    return (int) (lhs.rate - rhs.rate);
                }
            });
            for (VineVideoUrlTier tier : this.tieredUrls) {
                if ("h264c".equals(tier.format)) {
                    this.videoLowUrl = tier.url;
                } else if ("h264-preview".equals(tier.format)) {
                    this.videoPreview = tier.url;
                }
            }
            if (this.videoLowUrl == null) {
                this.videoLowUrl = this.tieredUrls.get(0).url;
            }
            this.videoUrl = this.tieredUrls.get(this.tieredUrls.size() - 1).url;
        }
    }

    public void setUpThumbnails(List<ThumbnailData> urls) {
        for (ThumbnailData thumb : urls) {
            if (thumb.width == 480) {
                this.thumbnailUrl = thumb.url;
            } else if (thumb.width == 240) {
                this.thumbnailMedUrl = thumb.url;
            } else if (thumb.width == 120) {
                this.thumbnailLowUrl = thumb.url;
            }
        }
    }

    public List<VineVideoUrlTier> getUrls() {
        return this.tieredUrls;
    }

    public void setFlagVerified(boolean verified) {
        if (verified) {
            this.metadataFlags |= 1;
        } else {
            this.metadataFlags &= -2;
        }
    }

    public void setFlagPromoted(boolean promoted) {
        if (promoted) {
            this.metadataFlags |= 2;
        } else {
            this.metadataFlags &= -3;
        }
    }

    public void setFlagExplicit(boolean explicit) {
        if (explicit) {
            this.metadataFlags |= 4;
        } else {
            this.metadataFlags &= -5;
        }
    }

    public void setFlagLiked(boolean liked) {
        if (liked) {
            this.metadataFlags |= 8;
        } else {
            this.metadataFlags &= -9;
        }
    }

    public void setFlagRevined(boolean revined) {
        if (revined) {
            this.metadataFlags |= 16;
        } else {
            this.metadataFlags &= -17;
        }
    }

    public void setFlagPostVerified(boolean postVerified) {
        if (postVerified) {
            this.metadataFlags |= 32;
        } else {
            this.metadataFlags &= -33;
        }
    }

    public void setFlagPrivate(boolean priv) {
        if (priv) {
            this.metadataFlags |= 64;
        } else {
            this.metadataFlags &= -65;
        }
    }

    @Override // co.vine.android.widget.SensitiveAcknowledgments.SensitiveItem
    public boolean isExplicit() {
        return (this.metadataFlags & 4) != 0;
    }

    @Override // co.vine.android.api.TimelineItem
    public long getId() {
        return this.postId;
    }

    public boolean isLiked() {
        return (this.metadataFlags & 8) != 0;
    }

    public boolean isRevined() {
        return (this.metadataFlags & 16) != 0;
    }

    public boolean isPrivate() {
        return (this.metadataFlags & 64) != 0;
    }

    public boolean isShareable() {
        return this.shareUrl != null;
    }

    public long getVineRepostRepostId() {
        if (this.repost != null) {
            return this.repost.repostId;
        }
        return 0L;
    }

    @Override // co.vine.android.api.TimelineItem
    public TimelineItemType getType() {
        return TimelineItemType.POST;
    }

    @Override // co.vine.android.api.VinePostData, android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
    }
}
