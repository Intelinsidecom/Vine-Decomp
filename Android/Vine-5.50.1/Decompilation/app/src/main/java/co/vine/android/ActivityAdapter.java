package co.vine.android;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorFilter;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.support.v4.view.ViewCompat;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SectionIndexer;
import co.vine.android.api.VineEntity;
import co.vine.android.api.VineEverydayNotification;
import co.vine.android.api.VineNotification;
import co.vine.android.api.VineSingleNotification;
import co.vine.android.api.VineUser;
import co.vine.android.cache.image.ImageKey;
import co.vine.android.cache.image.UrlImage;
import co.vine.android.client.AppController;
import co.vine.android.drawable.RecyclableBitmapDrawable;
import co.vine.android.util.LinkDispatcher;
import co.vine.android.util.Util;
import co.vine.android.widget.ActivityViewHolder;
import co.vine.android.widget.PinnedHeader;
import co.vine.android.widget.PinnedHeaderListView;
import co.vine.android.widget.Typefaces;
import co.vine.android.widget.TypefacesSpan;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import twitter4j.conf.PropertyConfiguration;

/* loaded from: classes.dex */
public class ActivityAdapter extends BaseAdapter implements SectionIndexer, PinnedHeader {
    private AppController mAppController;
    private final Context mContext;
    private final String mFollowRequestsString;
    private final ListView mListView;
    private final String mNewActivityString;
    private final String mOlderActivityString;
    private final int mPinnedHeaderHeight;
    private final int mProfileImageSize;
    private View.OnClickListener mViewClickListener;
    private final int mVineGreen;
    private Friendships mFriendships = new Friendships();
    private ArrayList<VineEverydayNotification> mNotifications = new ArrayList<>();
    private ArrayList<VineSingleNotification> mFollowRequests = new ArrayList<>();
    private int mLastNewIndex = -1;
    private ArrayList<String> mSections = new ArrayList<>();
    private int mPinnedHeaderSection = -1;
    private boolean mShouldReloadHeaderBecauseDataChanged = false;
    private View.OnClickListener mPostClicker = new View.OnClickListener() { // from class: co.vine.android.ActivityAdapter.1
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            long postId = ((Long) v.getTag()).longValue();
            SingleActivity.start(ActivityAdapter.this.mContext, postId);
        }
    };
    private View.OnClickListener mAvatarClicker = new View.OnClickListener() { // from class: co.vine.android.ActivityAdapter.2
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            long userId = ((Long) v.getTag()).longValue();
            ChannelActivity.startProfile(ActivityAdapter.this.mContext, userId, null);
        }
    };
    private ArrayList<WeakReference<ActivityViewHolder>> mViewHolders = new ArrayList<>();

    public ActivityAdapter(Context context, ListView listView, AppController appController, View.OnClickListener viewClickListener) {
        this.mListView = listView;
        this.mAppController = appController;
        this.mViewClickListener = viewClickListener;
        this.mContext = context;
        Resources res = context.getResources();
        this.mFollowRequestsString = res.getString(R.string.follow_header);
        this.mNewActivityString = res.getString(R.string.new_activity);
        this.mOlderActivityString = res.getString(R.string.older_activity);
        this.mVineGreen = res.getColor(R.color.vine_green);
        this.mPinnedHeaderHeight = res.getDimensionPixelSize(R.dimen.activity_header_height);
        this.mProfileImageSize = context.getResources().getDimensionPixelOffset(R.dimen.user_image_size);
    }

    public View newView(int position, ViewGroup root) {
        View view;
        int itemViewType = getItemViewType(position);
        switch (itemViewType) {
            case 1:
                view = LayoutInflater.from(this.mContext).inflate(R.layout.activity_row_view_milestone, root, false);
                break;
            case 2:
            default:
                view = LayoutInflater.from(this.mContext).inflate(R.layout.activity_row_view, root, false);
                break;
            case 3:
                view = LayoutInflater.from(this.mContext).inflate(R.layout.activity_row_view_count, root, false);
                break;
        }
        ActivityViewHolder holder = new ActivityViewHolder(view);
        if (itemViewType != 1) {
            holder.contentLine.setMovementMethod(LinkMovementMethod.getInstance());
        }
        view.setTag(holder);
        this.mViewHolders.add(new WeakReference<>(holder));
        return view;
    }

    public void bindView(View view, int position) throws Resources.NotFoundException {
        ActivityViewHolder h = (ActivityViewHolder) view.getTag();
        if (h == null) {
            throw new RuntimeException("View tag is null.");
        }
        bindHeader(h, position);
        switch (getItemViewType(position)) {
            case 0:
                bindGroupNotification(h, (VineEverydayNotification) getItem(position));
                return;
            case 1:
                bindMilestone(h, (VineEverydayNotification) getItem(position));
                return;
            case 2:
                bindFollowRequest(h, (VineSingleNotification) getItem(position));
                return;
            case 3:
                final VineEverydayNotification notif = (VineEverydayNotification) getItem(position);
                h.notification = new ActivityViewHolder.ActivityNotification(notif.post != null ? notif.post.postId : 0L, notif.link);
                h.contentLine.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.ActivityAdapter.3
                    @Override // android.view.View.OnClickListener
                    public void onClick(View v) {
                        LinkDispatcher.dispatch(notif.link, ActivityAdapter.this.mContext);
                    }
                });
                h.contentLine.setText(notif.getComment());
                h.contentLine.setTag(notif);
                return;
            default:
                return;
        }
    }

    private void bindHeader(ActivityViewHolder h, int position) {
        if (h.headerView != null) {
            Object[] sections = getSections();
            if (sections.length > 0) {
                int section = getSectionForPosition(position);
                int sectionPosition = getPositionForSection(section);
                if (position == sectionPosition) {
                    if (h.extraSpacer != null) {
                        h.extraSpacer.setVisibility(sectionPosition != 0 ? 0 : 8);
                    }
                    h.headerView.setVisibility(0);
                    h.headerText.setText((CharSequence) sections[section]);
                    if (h.topSpacer != null) {
                        h.topSpacer.setVisibility(0);
                        return;
                    }
                    return;
                }
                h.headerView.setVisibility(8);
                if (h.topSpacer != null) {
                    h.topSpacer.setVisibility(8);
                    return;
                }
                return;
            }
            h.headerView.setVisibility(8);
            if (h.topSpacer != null) {
                h.topSpacer.setVisibility(position != 0 ? 8 : 0);
            }
        }
    }

    private boolean nextViewHasHeader(int position) {
        return (getSections().length == 0 || getSectionForPosition(position) == getSectionForPosition(position + 1)) ? false : true;
    }

    private void bindFollowRequest(ActivityViewHolder h, VineSingleNotification notif) throws Resources.NotFoundException {
        if (h == null) {
            throw new RuntimeException("View tag is null.");
        }
        final ActivityViewHolder.ActivityNotification notification = new ActivityViewHolder.ActivityNotification(notif.postId, VineEntity.generateUserLink(notif.userId));
        h.notification = notification;
        setAvatar(h, h.avatar, notif.avatarUrl, notif.userId);
        h.avatar.setTag(Long.valueOf(notif.userId));
        h.avatar.setOnClickListener(this.mAvatarClicker);
        h.typeIcon.setVisibility(0);
        h.typeIcon.setImageResource(R.drawable.ic_activity_follow);
        h.thumbnail.setVisibility(8);
        h.username.setVisibility(8);
        h.twitterScreenname.setVisibility(8);
        h.verified.setVisibility(8);
        h.contentLine.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.ActivityAdapter.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                LinkDispatcher.dispatch(notification.getLink(), ActivityAdapter.this.mContext);
            }
        });
        bindBodyText(h, notif);
    }

    private void bindMilestone(ActivityViewHolder h, VineEverydayNotification notif) {
        if (notif.milestone != null) {
            h.notification = new ActivityViewHolder.ActivityNotification(notif.post != null ? notif.post.postId : 0L, notif.link);
            boolean isUserType = false;
            if (!TextUtils.isEmpty(notif.milestone.milestoneUrl)) {
                Uri milestoneUri = Uri.parse(notif.milestone.milestoneUrl);
                String host = milestoneUri.getHost();
                if ("user-id".equals(host)) {
                    isUserType = true;
                } else if ("post".equals(host)) {
                    isUserType = false;
                }
            }
            ImageKey key = new ImageKey(notif.milestone.backgroundImageUrl, true, notif.milestone.blurRadius * 2, true);
            h.milestoneBackgroundImageKey = key;
            setImage(h.milestoneBackground, this.mAppController.getPhotoBitmap(key), false);
            ImageKey key2 = new ImageKey(notif.milestone.backgroundImageUrl, this.mProfileImageSize, this.mProfileImageSize, isUserType);
            h.milestoneImageImageKey = key2;
            setImage(h.milestoneImage, this.mAppController.getPhotoBitmap(key2), false);
            h.milestoneImageFrame.setImageResource(isUserType ? R.drawable.milestone_avatar_frame : R.drawable.milestone_thumb_frame);
            ImageKey key3 = new ImageKey(notif.milestone.iconUrl);
            h.milestoneIconImageKey = key3;
            setImage(h.milestoneIcon, this.mAppController.getPhotoBitmap(key3), false);
            h.milestoneTitle.setText(notif.milestone.title);
            h.milestoneDescription.setText(Util.getRelativeTimeString(this.mContext, notif.getCreatedAt(), true));
            GradientDrawable overlay = (GradientDrawable) h.milestoneOverlay.getBackground();
            overlay.setColor((-16777216) | notif.milestone.overlayColor);
            h.milestoneOverlay.setAlpha(notif.milestone.overlayAlpha + 0.1f);
        }
    }

    private void bindGroupNotification(ActivityViewHolder h, final VineEverydayNotification notif) throws Resources.NotFoundException {
        h.notification = new ActivityViewHolder.ActivityNotification(notif.post != null ? notif.post.postId : 0L, notif.link);
        VineUser user = notif.user;
        if (user != null) {
            setAvatar(h, h.avatar, user.avatarUrl, user.userId);
            h.avatar.setTag(Long.valueOf(user.userId));
            h.avatar.setOnClickListener(this.mAvatarClicker);
        }
        if (notif.post != null) {
            h.thumbnail.setVisibility(0);
            h.thumbnail.setTag(Long.valueOf(h.notification.getPostId()));
            h.thumbnail.setOnClickListener(this.mPostClicker);
            if (notif.post.thumbnailUrl != null) {
                ImageKey key = new ImageKey(notif.post.thumbnailUrl, false);
                h.thumbnailImageKey = key;
                setImage(h.thumbnail, this.mAppController.getPhotoBitmap(key));
            } else {
                setImage(h.thumbnail, BitmapFactory.decodeResource(this.mContext.getResources(), R.drawable.ic_launcher));
            }
        } else if ("follow approved".equals(notif.type)) {
            h.thumbnail.setVisibility(8);
        } else {
            h.thumbnail.setVisibility(4);
        }
        if ("followed".equals(notif.type) || "follow approved".equals(notif.type) || "address book friend joined".equals(notif.type)) {
            h.typeIcon.setVisibility(0);
            h.typeIcon.setImageResource(R.drawable.ic_activity_follow);
        } else if ("mentioned comment".equals(notif.type) || "commented".equals(notif.type) || "repost comment".equals(notif.type) || "grouped comment".equals(notif.type) || "mentioned post".equals(notif.type) || "mentioned".equals(notif.type)) {
            h.typeIcon.setVisibility(0);
            h.typeIcon.setImageResource(R.drawable.ic_activity_comment);
        } else if ("liked".equals(notif.type) || "repost like".equals(notif.type)) {
            h.typeIcon.setVisibility(0);
            h.typeIcon.setImageResource(R.drawable.ic_activity_like);
        } else if ("reposted".equals(notif.type) || "repost repost".equals(notif.type)) {
            h.typeIcon.setVisibility(0);
            h.typeIcon.setImageResource(R.drawable.ic_activity_revine);
        } else if ("twitter friend joined".equals(notif.type)) {
            h.typeIcon.setVisibility(0);
            h.typeIcon.setImageResource(R.drawable.ic_activity_twitter);
        } else if ("recommendation".equals(notif.type)) {
            h.typeIcon.setVisibility(0);
            h.typeIcon.setImageResource(R.drawable.ic_activity_recommendation);
        } else if ("friend post".equals(notif.type)) {
            h.typeIcon.setVisibility(0);
            h.typeIcon.setImageResource(R.drawable.ic_activity_favorite);
        } else {
            h.typeIcon.setVisibility(8);
        }
        if ("followed".equals(notif.type) || "twitter friend joined".equals(notif.type) || "address book friend joined".equals(notif.type)) {
            h.followButton.setVisibility(0);
            h.followButton.setOnClickListener(this.mViewClickListener);
            if (user != null) {
                if (!this.mFriendships.contains(user.userId) && user.isFollowing()) {
                    this.mFriendships.addFollowing(user.userId);
                }
                h.followButton.setTag(Long.valueOf(user.userId));
                if (isFollowing(user.userId)) {
                    h.followButton.setBackgroundResource(R.drawable.btn_following_default);
                } else {
                    h.followButton.setBackgroundResource(R.drawable.btn_follow_default);
                }
            }
        } else {
            h.followButton.setVisibility(8);
        }
        h.contentLine.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.ActivityAdapter.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                LinkDispatcher.dispatch(notif.link, ActivityAdapter.this.mContext);
            }
        });
        bindBodyText(h, notif);
    }

    public boolean isFollowing(long userId) {
        return this.mFriendships.isFollowing(userId);
    }

    private void bindBodyText(ActivityViewHolder h, VineNotification notif) throws Resources.NotFoundException {
        VineEverydayNotification everydayNotif;
        VineUser user;
        String contentLine = notif.getComment();
        String commentText = null;
        boolean usingShortBody = false;
        if ((notif instanceof VineEverydayNotification) && (user = (everydayNotif = (VineEverydayNotification) notif).user) != null) {
            String username = user.username;
            h.username.setText(username);
            if (user.verified == 1) {
                h.verified.setVisibility(0);
            } else {
                h.verified.setVisibility(8);
            }
            if (everydayNotif.shortBody != null) {
                contentLine = everydayNotif.shortBody;
                usingShortBody = true;
            }
            if (everydayNotif.commentText != null) {
                commentText = everydayNotif.commentText;
            }
            String twitterScreenname = user.twitterScreenname;
            if (!user.hiddenTwitter && twitterScreenname != null) {
                h.twitterScreenname.setText("@" + twitterScreenname);
                h.twitterScreenname.setVisibility(0);
            } else {
                h.twitterScreenname.setVisibility(8);
            }
        }
        SpannableStringBuilder contentSb = new SpannableStringBuilder(contentLine);
        ArrayList<VineEntity> entities = null;
        if (notif.getEntities() != null) {
            entities = new ArrayList<>(notif.getEntities().size());
            for (int i = 0; i < notif.getEntities().size(); i++) {
                VineEntity entity = notif.getEntities().get(i);
                if (!usingShortBody) {
                    entities.add(new VineEntity(entity));
                }
            }
        }
        if (entities != null && !TextUtils.isEmpty(contentLine)) {
            Util.adjustEntities(entities, contentSb, 0, true);
            Iterator<VineEntity> it = entities.iterator();
            while (it.hasNext()) {
                VineEntity entity2 = it.next();
                if (!PropertyConfiguration.USER.equals(entity2.type)) {
                    TypefacesSpan span = new TypefacesSpan(null, Typefaces.get(this.mContext).getContentTypeface(1, 4));
                    try {
                        Util.safeSetSpan(contentSb, span, entity2.start, entity2.end, 33);
                    } catch (IndexOutOfBoundsException e) {
                    }
                }
            }
        }
        if (commentText != null) {
            int start = contentSb.length();
            contentSb.append((CharSequence) "\n").append((CharSequence) commentText);
            int end = contentSb.length();
            ColorStateList color = this.mContext.getResources().getColorStateList(R.color.timestamp_text);
            TextAppearanceSpan timestampSpan = new TextAppearanceSpan(null, 0, this.mContext.getResources().getDimensionPixelSize(R.dimen.font_size_middle), color, null);
            Util.safeSetSpan(contentSb, timestampSpan, start, end, 33);
        }
        int start2 = contentSb.length();
        contentSb.append((CharSequence) "  ").append((CharSequence) Util.getRelativeTimeString(this.mContext, notif.getCreatedAt(), false));
        int end2 = contentSb.length();
        ColorStateList color2 = this.mContext.getResources().getColorStateList(R.color.timestamp_text);
        TextAppearanceSpan timestampSpan2 = new TextAppearanceSpan(null, 0, this.mContext.getResources().getDimensionPixelSize(R.dimen.font_size_mini), color2, null);
        Util.safeSetSpan(contentSb, timestampSpan2, start2, end2, 33);
        h.contentLine.setText(contentSb);
        if (h.dateLine != null) {
            h.dateLine.setText(Util.getRelativeTimeString(this.mContext, notif.getCreatedAt(), true));
        }
    }

    private void setAvatar(ActivityViewHolder h, ImageView avatarImageView, String avatarUrl, long userId) {
        if (Util.isDefaultAvatarUrl(avatarUrl)) {
            Util.safeSetDefaultAvatar(avatarImageView, Util.ProfileImageSize.MEDIUM, (-16777216) | this.mVineGreen);
        } else {
            ImageKey key = new ImageKey(avatarUrl, this.mProfileImageSize, this.mProfileImageSize, true);
            h.avatarImageKey = key;
            setUserImage(avatarImageView, this.mAppController.getPhotoBitmap(key));
        }
        avatarImageView.setTag(Long.valueOf(userId));
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.mFollowRequests.size() + this.mNotifications.size();
    }

    @Override // android.widget.Adapter
    public Object getItem(int position) {
        return position < this.mFollowRequests.size() ? this.mFollowRequests.get(position) : this.mNotifications.get(position - this.mFollowRequests.size());
    }

    @Override // android.widget.Adapter
    public long getItemId(int position) {
        return ((VineNotification) getItem(position)).getNotificationId();
    }

    @Override // android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) throws Resources.NotFoundException {
        View view;
        if (convertView == null) {
            view = newView(position, parent);
        } else {
            view = convertView;
        }
        bindView(view, position);
        return view;
    }

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public int getItemViewType(int position) {
        if (position < this.mFollowRequests.size()) {
            return 2;
        }
        VineEverydayNotification notif = (VineEverydayNotification) getItem(position);
        if (notif.milestone != null) {
            return 1;
        }
        if ("count".equals(notif.type)) {
            return 3;
        }
        return 0;
    }

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public int getViewTypeCount() {
        return 4;
    }

    public void setImages(HashMap<ImageKey, UrlImage> images) {
        UrlImage image;
        UrlImage image2;
        UrlImage targetImage;
        UrlImage targetImage2;
        ArrayList<WeakReference<ActivityViewHolder>> toRemove = new ArrayList<>();
        Iterator<WeakReference<ActivityViewHolder>> it = this.mViewHolders.iterator();
        while (it.hasNext()) {
            WeakReference<ActivityViewHolder> ref = it.next();
            ActivityViewHolder holder = ref.get();
            if (holder == null) {
                toRemove.add(ref);
            } else {
                if (holder.avatarImageKey != null) {
                    ImageKey key = holder.avatarImageKey;
                    UrlImage image3 = images.get(key);
                    if (image3 != null && image3.isValid()) {
                        setUserImage(holder.avatar, image3.bitmap);
                    }
                }
                if (holder.thumbnailImageKey != null && (targetImage2 = images.get(holder.thumbnailImageKey)) != null && targetImage2.isValid()) {
                    setImage(holder.thumbnail, targetImage2.bitmap);
                }
                if (holder.milestoneImageImageKey != null && (targetImage = images.get(holder.milestoneImageImageKey)) != null && targetImage.isValid()) {
                    setImage(holder.milestoneImage, targetImage.bitmap);
                }
                if (holder.milestoneBackgroundImageKey != null && (image2 = images.get(holder.milestoneBackgroundImageKey)) != null && image2.isValid()) {
                    setImage(holder.milestoneBackground, image2.bitmap);
                }
                if (holder.milestoneIconImageKey != null && (image = images.get(holder.milestoneIconImageKey)) != null && image.isValid()) {
                    setImage(holder.milestoneIcon, image.bitmap);
                }
            }
        }
        Iterator<WeakReference<ActivityViewHolder>> it2 = toRemove.iterator();
        while (it2.hasNext()) {
            WeakReference<ActivityViewHolder> r = it2.next();
            this.mViewHolders.remove(r);
        }
    }

    private void setUserImage(ImageView v, Bitmap bmp) {
        if (v != null) {
            v.setColorFilter((ColorFilter) null);
            if (bmp == null) {
                v.setImageResource(R.drawable.circle_shape_light);
            } else {
                v.setBackgroundColor(0);
                v.setImageDrawable(new RecyclableBitmapDrawable(this.mContext.getResources(), bmp));
            }
        }
    }

    private void setImage(ImageView v, Bitmap bmp) {
        setImage(v, bmp, true);
    }

    private void setImage(ImageView v, Bitmap bmp, boolean showGray) {
        if (v != null) {
            if (bmp == null) {
                if (showGray) {
                    v.setBackgroundColor(this.mContext.getResources().getColor(R.color.solid_light_gray));
                } else {
                    v.setBackgroundColor(ViewCompat.MEASURED_SIZE_MASK);
                }
                v.setImageBitmap(null);
                return;
            }
            v.setBackgroundColor(0);
            v.setImageDrawable(new RecyclableBitmapDrawable(this.mContext.getResources(), bmp));
        }
    }

    @Override // android.widget.BaseAdapter
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        this.mShouldReloadHeaderBecauseDataChanged = true;
    }

    public void mergeData(ArrayList<VineEverydayNotification> notifications, ArrayList<VineSingleNotification> followRequests, boolean mergeAfter) {
        if (!mergeAfter) {
            this.mNotifications = new ArrayList<>();
            this.mFriendships.clear();
            this.mLastNewIndex = -1;
        }
        this.mFollowRequests = new ArrayList<>();
        if (notifications != null) {
            Iterator<VineEverydayNotification> it = notifications.iterator();
            while (it.hasNext()) {
                VineEverydayNotification notif = it.next();
                if (notif.isNew) {
                    this.mLastNewIndex++;
                }
                this.mNotifications.add(notif);
            }
        }
        if (followRequests != null) {
            Iterator<VineSingleNotification> it2 = followRequests.iterator();
            while (it2.hasNext()) {
                this.mFollowRequests.add(it2.next());
            }
        }
        ((PinnedHeaderListView) this.mListView).untrackScrollawayChild();
        if (notifications != null || followRequests != null) {
            notifyDataSetChanged();
        }
    }

    public void follow(long userId) {
        this.mFriendships.addFollowing(userId);
        notifyDataSetChanged();
    }

    public void unfollow(long userId) {
        this.mFriendships.removeFollowing(userId);
        notifyDataSetChanged();
    }

    @Override // android.widget.SectionIndexer
    public Object[] getSections() {
        this.mSections.clear();
        if (this.mFollowRequests.size() > 0) {
            this.mSections.add(this.mFollowRequestsString);
        }
        if (this.mLastNewIndex >= 0) {
            this.mSections.add(this.mNewActivityString);
        }
        if (this.mLastNewIndex >= 0 || this.mFollowRequests.size() > 0) {
            this.mSections.add(this.mOlderActivityString);
        }
        return this.mSections.toArray();
    }

    @Override // android.widget.SectionIndexer
    public int getPositionForSection(int sectionIndex) {
        if (getSections().length <= 0 || sectionIndex == 0) {
            return 0;
        }
        if (sectionIndex == 1) {
            if (this.mFollowRequests.size() > 0) {
                return this.mFollowRequests.size();
            }
            if (this.mLastNewIndex >= 0) {
                return this.mLastNewIndex + 1;
            }
        }
        if (sectionIndex == 2) {
            return this.mFollowRequests.size() + this.mLastNewIndex + 1;
        }
        return 0;
    }

    @Override // android.widget.SectionIndexer
    public int getSectionForPosition(int position) {
        if (getSections().length <= 0 || position < this.mFollowRequests.size()) {
            return 0;
        }
        if (position <= this.mFollowRequests.size() + this.mLastNewIndex) {
            return this.mFollowRequests.size() <= 0 ? 0 : 1;
        }
        return (this.mFollowRequests.size() > 0 ? 1 : 0) + (this.mLastNewIndex < 0 ? 0 : 1);
    }

    @Override // co.vine.android.widget.PinnedHeader
    public PinnedHeader.PinnedHeaderStatus getPinnedHeaderStatus(int navBottom) {
        View nextView;
        int pinnedHeaderSection = this.mPinnedHeaderSection;
        int childIndex = ((PinnedHeaderListView) this.mListView).getPositionForPixelLocation(navBottom);
        int position = (this.mListView.getFirstVisiblePosition() - this.mListView.getHeaderViewsCount()) + childIndex;
        this.mPinnedHeaderSection = getSectionForPosition(position);
        int offset = 0;
        if (nextViewHasHeader(position) && (nextView = this.mListView.getChildAt(childIndex + 1)) != null && nextView.getTop() < this.mPinnedHeaderHeight + navBottom) {
            offset = (nextView.getTop() - this.mPinnedHeaderHeight) - navBottom;
        }
        boolean shouldReloadHeader = pinnedHeaderSection != this.mPinnedHeaderSection || this.mShouldReloadHeaderBecauseDataChanged;
        this.mShouldReloadHeaderBecauseDataChanged = false;
        return new PinnedHeader.PinnedHeaderStatus(offset, shouldReloadHeader, true);
    }

    @Override // co.vine.android.widget.PinnedHeader
    public View getPinnedHeaderView(View convertView) {
        if (getSections().length == 0) {
            return null;
        }
        if (convertView == null) {
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.activity_header, (ViewGroup) null);
            convertView.layout(0, 0, this.mListView.getWidth(), this.mPinnedHeaderHeight);
            ActivityViewHolder h = new ActivityViewHolder(convertView);
            convertView.setTag(h);
        }
        ActivityViewHolder h2 = (ActivityViewHolder) convertView.getTag();
        bindHeader(h2, getPositionForSection(this.mPinnedHeaderSection));
        return convertView;
    }

    @Override // co.vine.android.widget.PinnedHeader
    public void layoutPinnedHeader(View header, int l, int t, int r, int b) {
        header.layout(l, t, r, b);
    }

    @Override // co.vine.android.widget.PinnedHeader
    public int getPinnedHeaderHeight() {
        return this.mPinnedHeaderHeight;
    }
}
