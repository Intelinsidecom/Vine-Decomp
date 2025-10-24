package co.vine.android;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import co.vine.android.account.AccountSwitchAdapter;
import co.vine.android.animation.HeightAnimationUtil;
import co.vine.android.api.VineUser;
import co.vine.android.cache.image.ImageKey;
import co.vine.android.cache.image.UrlImage;
import co.vine.android.client.AppController;
import co.vine.android.drawable.RecyclableBitmapDrawable;
import co.vine.android.scribe.AppNavigationProvider;
import co.vine.android.scribe.AppNavigationProviderSingleton;
import co.vine.android.scribe.AppStateProvider;
import co.vine.android.scribe.FollowRecScribeActionsLogger;
import co.vine.android.scribe.FollowScribeActionsLogger;
import co.vine.android.scribe.FollowScribeActionsLoggerSingleton;
import co.vine.android.scribe.ScribeLogger;
import co.vine.android.scribe.ScribeLoggerSingleton;
import co.vine.android.scribe.UIEventScribeLogger;
import co.vine.android.service.components.Components;
import co.vine.android.util.BuildUtil;
import co.vine.android.util.ClientFlagsHelper;
import co.vine.android.util.MediaUtil;
import co.vine.android.util.ResourceLoader;
import co.vine.android.util.SmartOnGestureListener;
import co.vine.android.util.SystemUtil;
import co.vine.android.util.Util;
import co.vine.android.util.analytics.FlurryUtils;
import co.vine.android.widget.DotIndicators;
import co.vine.android.widget.RoundedCornerBitmapImageView;
import co.vine.android.widget.RowLimitedLinearLayout;
import co.vine.android.widget.Typefaces;
import co.vine.android.widget.TypefacesSpan;
import co.vine.android.widget.TypefacesTextView;
import com.twitter.android.sdk.Twitter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/* loaded from: classes.dex */
public class ProfileHeaderAdapter extends BaseAdapter implements ViewPager.OnPageChangeListener, View.OnClickListener, View.OnLongClickListener {
    private AccountSwitchAdapter.AccountSwitchCallback mAccountSwitchCallback;
    private int mActionButtonWidth;
    private final AppController mAppController;
    private AppNavigationProvider mAppNavProvider;
    private AppStateProvider mAppStateProvider;
    private boolean mBlocked;
    private final TypefacesSpan mBold;
    private Context mContext;
    private int mCurrentTab;
    private int mDetailPageHeight;
    final GestureDetectorCompat mDetector;
    private int mFavBackgroundColor;
    private FollowRecScribeActionsLogger mFollowRecLogger;
    private boolean mHideProfileReposts;
    private final boolean mHideUserName;
    private ProfileViewHolder mHolder;
    private PagerDetailsViewHolder mHolderPagerDetails;
    private PagerMainViewHolder mHolderPagerMain;
    private String mLikesSortOrder;
    private SlowScrollLinearLayoutManager mLinearLayoutManager;
    private final ProfileHeaderListener mListener;
    private boolean mLocked;
    private int mMainPageHeight;
    private final int mNumberUnSelectedColor;
    private String mPostsSortOrder;
    private RecommendedUserAdapter mRecommendedUserAdapter;
    private final TypefacesSpan mRegular;
    private ScribeLogger mScribeLogger;
    private View mSelectedSwitch;
    private SharedPreferences mSharedPreferences;
    private boolean mShowActionsContainer;
    private TextView mSortSelectionView;
    private int mTabSelectedColor;
    private VineUser mUser;
    private FollowableUsersMemoryAdapter mUserRecsAdapter;
    private final int mVineGreen;
    private List<VineUser> mUserRecs = new ArrayList();
    private Animation.AnimationListener mDrawerListener = new Animation.AnimationListener() { // from class: co.vine.android.ProfileHeaderAdapter.1
        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationStart(Animation animation) {
            ProfileHeaderAdapter.this.mListener.onDrawerAnimationStart();
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationEnd(Animation animation) {
            ProfileHeaderAdapter.this.mListener.onDrawerAnimationEnd();
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationRepeat(Animation animation) {
        }
    };

    public interface ProfileHeaderListener {
        void onAcceptFollowRequestClicked();

        void onDownloadVideosClicked();

        void onDrawerAnimationEnd();

        void onDrawerAnimationStart();

        void onDropdownActionClicked(int i);

        void onFavUserClicked();

        void onFollowUserClicked();

        void onLikesTabClicked();

        void onOpenTwitterClicked();

        void onPostsTabClicked();

        void onRejectFollowRequestClicked();

        void onShowAccountSwitchingMenu();

        void onTheaterModeClicked();

        void onUnfollowUserClicked();

        void onViewFollowersClicked();

        void onViewFollowingClicked();
    }

    public ProfileHeaderAdapter(Context context, AppController appController, ProfileHeaderListener listener, boolean hideProfileReposts, String postsSortOrder, String likesSortOrder, boolean locked, boolean blocked, AccountSwitchAdapter.AccountSwitchCallback accountSwitchCallback, String followEventSource, boolean showActionsContainer, FollowScribeActionsLogger followActionsLogger, boolean hideUserName) {
        this.mContext = context;
        this.mLocked = locked;
        this.mBlocked = blocked;
        this.mAppController = appController;
        this.mSharedPreferences = Util.getDefaultSharedPrefs(this.mContext);
        this.mListener = listener;
        this.mHideUserName = hideUserName;
        this.mHideProfileReposts = hideProfileReposts;
        Resources res = context.getResources();
        this.mNumberUnSelectedColor = res.getColor(R.color.black_thirty_five_percent);
        this.mBold = new TypefacesSpan(null, Typefaces.get(this.mContext).getContentTypeface(1, 4));
        this.mRegular = new TypefacesSpan(null, Typefaces.get(this.mContext).getContentTypeface(0, 2));
        this.mAccountSwitchCallback = accountSwitchCallback;
        this.mUserRecsAdapter = new FollowableUsersMemoryAdapter(this.mContext, AppController.getInstance(this.mContext), true, false, this, null);
        this.mRecommendedUserAdapter = new RecommendedUserAdapter(this.mContext, this.mUserRecs, this);
        this.mVineGreen = context.getResources().getColor(R.color.vine_green);
        this.mShowActionsContainer = showActionsContainer;
        this.mCurrentTab = 0;
        this.mFollowRecLogger = FollowScribeActionsLoggerSingleton.getRecLoggerInstance(followActionsLogger);
        this.mScribeLogger = ScribeLoggerSingleton.getInstance(this.mContext);
        this.mAppStateProvider = AppStateProviderSingleton.getInstance(this.mContext.getApplicationContext());
        this.mAppNavProvider = AppNavigationProviderSingleton.getInstance();
        this.mPostsSortOrder = postsSortOrder;
        this.mLikesSortOrder = likesSortOrder;
        this.mDetector = new GestureDetectorCompat(this.mContext, new RecCarouselSwipeListener());
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return 1;
    }

    @Override // android.widget.Adapter
    public Object getItem(int i) {
        return null;
    }

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return 0L;
    }

    @Override // android.widget.Adapter
    public View getView(int i, View convertView, ViewGroup root) throws Resources.NotFoundException {
        if (convertView == null) {
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.profile_header, root, false);
            this.mHolder = new ProfileViewHolder(convertView);
            this.mLinearLayoutManager = new SlowScrollLinearLayoutManager(this.mContext, 0, false);
            this.mHolder.recommendedUsers.setLayoutManager(this.mLinearLayoutManager);
            this.mHolder.recommendedUsers.addItemDecoration(new UserRecsSpacesItemDecoration(MediaUtil.convertDpToPixel(4, this.mContext)));
            this.mHolder.recommendedUsers.setOnTouchListener(new View.OnTouchListener() { // from class: co.vine.android.ProfileHeaderAdapter.2
                @Override // android.view.View.OnTouchListener
                public boolean onTouch(View v, MotionEvent event) {
                    return ProfileHeaderAdapter.this.mDetector.onTouchEvent(event);
                }
            });
            if (ClientFlagsHelper.isPlaylistEnabled(this.mContext)) {
                this.mHolder.playlistHolder.relativeLayout.setVisibility(0);
            }
            if (ClientFlagsHelper.profileSortingEnabled(this.mContext)) {
                this.mHolder.postsDropdownHolder.topPostsView.setVisibility(0);
                this.mHolder.postsDropdownHolder.recentPostsView.setVisibility(0);
                this.mHolder.postsDropdownHolder.oldestPostsView.setVisibility(0);
                this.mHolder.likesDropdownHolder.newestLikesView.setVisibility(0);
                this.mHolder.likesDropdownHolder.oldestPostsView.setVisibility(0);
            }
            this.mHolder.pager.setAdapter(new ProfileViewPagerAdapter());
            this.mHolder.pager.setOnPageChangeListener(this);
            this.mHolder.twitterTooltip.setOnClickListener(this);
            this.mHolder.progress.setVisibility(0);
            this.mHolder.dots.setFinalIcon(false);
            this.mHolder.dots.setNumberOfDots(2);
            this.mHolder.dots.setVisibility(4);
            this.mHolder.postsDropdownHolder.revineParent.setOnClickListener(this);
            this.mHolder.postsDropdownHolder.topPostsView.setOnClickListener(this);
            this.mHolder.postsDropdownHolder.recentPostsView.setOnClickListener(this);
            this.mHolder.postsDropdownHolder.oldestPostsView.setOnClickListener(this);
            this.mHolder.likesDropdownHolder.newestLikesView.setOnClickListener(this);
            this.mHolder.likesDropdownHolder.oldestPostsView.setOnClickListener(this);
            setSelectedView();
            if (this.mCurrentTab == 0) {
                if (this.mPostsSortOrder.equalsIgnoreCase("oldest")) {
                    this.mSortSelectionView = this.mHolder.postsDropdownHolder.oldestLabel;
                } else if (this.mPostsSortOrder.equalsIgnoreCase("top")) {
                    this.mSortSelectionView = this.mHolder.postsDropdownHolder.topLabel;
                } else {
                    this.mSortSelectionView = this.mHolder.postsDropdownHolder.recentLabel;
                }
            } else {
                this.mSortSelectionView = this.mLikesSortOrder.equalsIgnoreCase("recent") ? this.mHolder.likesDropdownHolder.newestLabel : this.mHolder.likesDropdownHolder.oldestLabel;
            }
            this.mSortSelectionView.setTextColor(this.mContext.getResources().getColor(R.color.vine_green));
            this.mHolder.buttons.setVisibility(this.mShowActionsContainer ? 0 : 8);
            if (this.mUser != null) {
                bindUser(this.mUser);
            }
        }
        return convertView;
    }

    private class RecCarouselSwipeListener extends SmartOnGestureListener {
        private RecCarouselSwipeListener() {
        }

        @Override // co.vine.android.util.SmartOnGestureListener
        public boolean onSwipe(SmartOnGestureListener.Direction direction) {
            if (direction == SmartOnGestureListener.Direction.RIGHT) {
                UIEventScribeLogger.onRecommendationCarouselSwipe(ProfileHeaderAdapter.this.mScribeLogger, ProfileHeaderAdapter.this.mAppStateProvider, ProfileHeaderAdapter.this.mAppNavProvider, UIEventScribeLogger.SwipeDirection.RIGHT);
                return false;
            }
            if (direction == SmartOnGestureListener.Direction.LEFT) {
                UIEventScribeLogger.onRecommendationCarouselSwipe(ProfileHeaderAdapter.this.mScribeLogger, ProfileHeaderAdapter.this.mAppStateProvider, ProfileHeaderAdapter.this.mAppNavProvider, UIEventScribeLogger.SwipeDirection.LEFT);
                return false;
            }
            return false;
        }
    }

    private void setSelectedView() throws Resources.NotFoundException {
        SwitchUtils.setSelected(this.mContext, this.mHolder.postsDropdownHolder.revineSelector, false, -1);
        SwitchUtils.setSelected(this.mContext, this.mHolder.postsDropdownHolder.recentSelector, false, -1);
        SwitchUtils.setSelected(this.mContext, this.mHolder.postsDropdownHolder.topSelector, false, -1);
        SwitchUtils.setSelected(this.mContext, this.mHolder.postsDropdownHolder.oldestSelector, false, -1);
        SwitchUtils.setSelected(this.mContext, this.mHolder.likesDropdownHolder.newestSelector, false, -1);
        SwitchUtils.setSelected(this.mContext, this.mHolder.likesDropdownHolder.oldestSelector, false, -1);
        if (this.mCurrentTab == 0) {
            if (this.mPostsSortOrder.equalsIgnoreCase("recent")) {
                this.mSelectedSwitch = this.mHolder.postsDropdownHolder.recentSelector;
            } else if (this.mPostsSortOrder.equalsIgnoreCase("top")) {
                this.mSelectedSwitch = this.mHolder.postsDropdownHolder.topSelector;
            } else {
                this.mSelectedSwitch = this.mHolder.postsDropdownHolder.oldestSelector;
            }
        } else {
            this.mSelectedSwitch = this.mLikesSortOrder.equalsIgnoreCase("recent") ? this.mHolder.likesDropdownHolder.newestSelector : this.mHolder.likesDropdownHolder.oldestSelector;
        }
        SwitchUtils.setSelected(this.mContext, this.mSelectedSwitch, true, 16734567);
    }

    public void bindUser(final VineUser profileUser) {
        if (this.mHolder != null && this.mHolderPagerMain != null && this.mHolderPagerDetails != null) {
            this.mUser = profileUser;
            final ProfileViewHolder holder = this.mHolder;
            PagerMainViewHolder holderMain = this.mHolderPagerMain;
            PagerDetailsViewHolder holderDetails = this.mHolderPagerDetails;
            Resources res = this.mContext.getResources();
            if (TextUtils.isEmpty(profileUser.avatarUrl) || Util.isDefaultAvatarUrl(profileUser.avatarUrl)) {
                setImage(null);
            } else {
                holderMain.userImageKey = new ImageKey(profileUser.avatarUrl, true);
                setImage(this.mAppController.getPhotoBitmap(holderMain.userImageKey));
            }
            holderMain.imageView.setOnClickListener(this);
            holderMain.imageView.setOnLongClickListener(this);
            holderMain.usernameContainer.setOnClickListener(this);
            if (profileUser.profileBackground > 0) {
                holder.profileBackground.setBackgroundColor(profileUser.profileBackground | ViewCompat.MEASURED_STATE_MASK);
            }
            int tabSelectedColor = profileUser.profileBackground;
            int favBackgroundColor = profileUser.profileBackground;
            if (tabSelectedColor == Settings.DEFAULT_PROFILE_COLOR || tabSelectedColor <= 0) {
                tabSelectedColor = this.mContext.getResources().getColor(R.color.vine_green);
            }
            int tabSelectedColor2 = tabSelectedColor | ViewCompat.MEASURED_STATE_MASK;
            int favBackgroundColor2 = favBackgroundColor | ViewCompat.MEASURED_STATE_MASK;
            this.mTabSelectedColor = tabSelectedColor2;
            this.mFavBackgroundColor = favBackgroundColor2;
            holder.postsTabArrowImage.setColorFilter(new PorterDuffColorFilter(1275068416, PorterDuff.Mode.SRC_IN));
            holder.likesTabArrowImage.setColorFilter(new PorterDuffColorFilter(1275068416, PorterDuff.Mode.SRC_IN));
            boolean isMe = this.mAppController.getActiveId() == profileUser.userId;
            if (!TextUtils.isEmpty(profileUser.description)) {
                holderDetails.description.setText(profileUser.description);
                holderDetails.description.setVisibility(0);
            } else {
                holderDetails.description.setVisibility(8);
            }
            if (!TextUtils.isEmpty(profileUser.location)) {
                holderDetails.location.setText(profileUser.location);
                holderDetails.location.setVisibility(0);
            } else {
                holderDetails.location.setVisibility(8);
            }
            if (TextUtils.isEmpty(profileUser.description) && TextUtils.isEmpty(profileUser.location)) {
                this.mHolder.dots.setVisibility(4);
            } else {
                this.mHolder.dots.setVisibility(0);
            }
            holder.userId = profileUser.userId;
            if (profileUser.isVerified()) {
                holderMain.verified.setVisibility(0);
            } else {
                holderMain.verified.setVisibility(8);
            }
            this.mActionButtonWidth = getMaxButtonWidth(holder, isMe);
            if (this.mBlocked) {
                holder.buttons.setVisibility(8);
            } else if (isMe) {
                holder.followButton.setVisibility(8);
                holder.favoriteIcon.setVisibility(8);
                holder.favIconPadding.setVisibility(8);
            } else {
                holder.followButton.setWidth(this.mActionButtonWidth);
                holder.followButton.setVisibility(0);
                holder.favoriteIcon.setVisibility(0);
                holder.favIconPadding.setVisibility(0);
                holder.followButton.setOnClickListener(this);
                holder.favoriteIcon.setOnClickListener(this);
                holder.favIconPadding.setOnClickListener(this);
                setupFollowButton(holder.followButton, holder.favoriteIcon, holder.favIconPadding, profileUser);
            }
            Button settings = holder.settings;
            if (isMe) {
                settings.setWidth(this.mActionButtonWidth);
                settings.setVisibility(0);
                settings.setOnClickListener(this);
                int height = res.getDimensionPixelSize(R.dimen.profile_header_view_pager_height);
                this.mHolder.pager.getLayoutParams().height = height;
                this.mMainPageHeight = height;
                this.mHolderPagerMain.topSpacer.setVisibility(0);
                this.mHolderPagerMain.spacer.setVisibility(0);
                holderMain.username.setText(profileUser.username);
                holderMain.username.setTypeface(Typefaces.get(this.mContext).mediumContent);
                holderMain.username.setVisibility(0);
                if (!profileUser.hiddenTwitter && profileUser.twitterScreenname != null) {
                    holderMain.twitterScreenname.setText("@" + profileUser.twitterScreenname);
                    holderMain.twitterScreenname.setVisibility(0);
                } else {
                    holderMain.twitterScreenname.setVisibility(8);
                }
                if (ClientFlagsHelper.isAccountSwitchingEnabled(this.mContext)) {
                    this.mHolderPagerMain.accountSwitchingCaret.setVisibility(0);
                    RecyclerView recyclerView = (RecyclerView) this.mHolderPagerMain.accountSwitchingMenu.findViewById(R.id.recycler_view);
                    recyclerView.setLayoutManager(new LinearLayoutManager(this.mContext));
                    recyclerView.setAdapter(new AccountSwitchAdapter(this.mContext, this.mAppController, this.mAccountSwitchCallback));
                }
            } else {
                if (!profileUser.hiddenTwitter && profileUser.twitterScreenname != null) {
                    String handle = "@" + profileUser.twitterScreenname;
                    holder.twitter.setVisibility(0);
                    ResourceLoader loader = new ResourceLoader(this.mContext, this.mAppController);
                    loader.setImageWhenLoaded(new ResourceLoader.ImageViewImageSetter(holder.twitterAvatar), profileUser.avatarUrl, Util.getUserImageSize(this.mContext.getResources()), true);
                    String followText = this.mContext.getString(R.string.follow_on_twitter, handle);
                    SpannableStringBuilder sb = new SpannableStringBuilder(followText);
                    int start = followText.indexOf(handle);
                    int end = start + handle.length();
                    sb.setSpan(new StyleSpan(1), start, end, 17);
                    holder.twitterText.setText(sb);
                    holder.twitterFollow.setSelected(profileUser.followingOnTwitter);
                    holder.twitterFollow.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.ProfileHeaderAdapter.3
                        @Override // android.view.View.OnClickListener
                        public void onClick(View v) {
                            if (!profileUser.followingOnTwitter) {
                                if (!ProfileHeaderAdapter.this.mSharedPreferences.getBoolean("settings_twitter_connected", false)) {
                                    Twitter twitter = ProfileHeaderAdapter.this.mAppController.getTwitter();
                                    AppController.startTwitterAuthWithFinish(twitter, (Activity) ProfileHeaderAdapter.this.mContext, 4);
                                    return;
                                } else {
                                    holder.twitterFollow.setSelected(true);
                                    ProfileHeaderAdapter.this.followOnTwitter(true);
                                    return;
                                }
                            }
                            holder.twitterFollow.setSelected(false);
                            ProfileHeaderAdapter.this.followOnTwitter(false);
                        }
                    });
                } else {
                    holder.twitter.setVisibility(8);
                }
                settings.setVisibility(8);
            }
            holder.downloadButton.setVisibility(8);
            holder.watchButton.setVisibility(8);
            if (isMe && ClientFlagsHelper.enableDownloader(this.mContext)) {
                holder.downloadButton.setWidth(this.mActionButtonWidth);
                holder.downloadButton.setVisibility(0);
                holder.downloadButton.setOnClickListener(this);
            } else if (!BuildUtil.isAmazon() && ClientFlagsHelper.isTheaterModeEnabled(this.mContext)) {
                holder.watchButton.setWidth(this.mActionButtonWidth);
                holder.watchButton.setVisibility(0);
                holder.watchButton.setOnClickListener(this);
            } else if (!isMe) {
                holder.downloadButton.setWidth(this.mActionButtonWidth);
                holder.downloadButton.setVisibility(0);
                holder.downloadButton.setOnClickListener(this);
            } else {
                holder.rightButtonContainer.setVisibility(8);
            }
            if (this.mLocked) {
                holder.postsParent.setVisibility(8);
                holder.likesParent.setVisibility(8);
                holderMain.userLoopCount.setVisibility(8);
                holder.postsTabArrow.setVisibility(8);
                holder.likesTabArrow.setVisibility(8);
            } else {
                holder.postsParent.setVisibility(0);
                holder.likesParent.setVisibility(0);
                holderMain.userLoopCount.setVisibility(0);
                holder.postsTabArrow.setVisibility(this.mCurrentTab == 0 ? 0 : 8);
                holder.likesTabArrow.setVisibility(this.mCurrentTab == 1 ? 0 : 8);
                holder.likesParent.setOnClickListener(this);
                holder.postsParent.setOnClickListener(this);
                holderMain.userLoopCount.setText(res.getQuantityString(R.plurals.profile_loops, (int) profileUser.loopCount, Util.numberFormat(res, profileUser.loopCount, false)));
                invalidateCountAndTabColor(1, profileUser.likeCount, res, R.plurals.profile_likes, holder.likesLabel);
                invalidateCountAndTabColor(0, profileUser.postCount, res, R.plurals.profile_posts, holder.postsLabel);
            }
            holderMain.imageFrame.setVisibility(0);
            holder.progress.setVisibility(8);
            holder.postsTabArrow.setOnClickListener(this);
            holder.likesTabArrow.setOnClickListener(this);
            invalidateCount(profileUser.followerCount, res, R.plurals.profile_followers, holderMain.followers);
            invalidateCount(profileUser.followingCount, res, R.plurals.profile_following, holderMain.following);
            holderMain.followers.setOnClickListener(this);
            holderMain.following.setOnClickListener(this);
            if (this.mHideUserName) {
                this.mHolderPagerMain.topSpacer.setVisibility(8);
                this.mHolderPagerMain.spacer.setVisibility(8);
                this.mHolderPagerMain.username.setVisibility(8);
                this.mHolderPagerMain.twitterScreenname.setVisibility(8);
                int height2 = this.mContext.getResources().getDimensionPixelSize(R.dimen.profile_header_view_pager_height_short);
                this.mHolder.pager.getLayoutParams().height = height2;
                this.mMainPageHeight = height2;
            }
        }
        invalidateDetailHeight();
        this.mUser = profileUser;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void invalidateDetailHeight() {
        View parent;
        if (this.mHolderPagerDetails != null && (parent = this.mHolderPagerDetails.parent) != null) {
            int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, 0);
            int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(SystemUtil.getDisplaySize(this.mContext).x, 1073741824);
            parent.measure(widthMeasureSpec, heightMeasureSpec);
            this.mDetailPageHeight = parent.getMeasuredHeight();
            if (this.mMainPageHeight <= 0) {
                this.mMainPageHeight = this.mHolder.pager.getHeight();
            }
        }
    }

    private void invalidateCountAndTabColor(int tab, int count, Resources res, int resId, TypefacesTextView labelView) throws Resources.NotFoundException {
        invalidateCount(count, res, resId, labelView);
        invalidateTabColor(tab, labelView);
    }

    private void invalidateCount(int count, Resources res, int resId, TypefacesTextView labelView) throws Resources.NotFoundException {
        String text = res.getQuantityString(resId, count, Util.numberFormat(res, count));
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        int start = text.indexOf(34);
        int end = text.indexOf(34, start + 1);
        Util.safeSetSpan(builder, this.mBold, start, end, 33);
        Util.safeSetSpan(builder, this.mRegular, end, text.length(), 33);
        builder.delete(start, start + 1);
        builder.delete(end - 1, end);
        labelView.setText(builder);
    }

    private void invalidateTabColor(int tab, TextView numberView) {
        if (this.mCurrentTab == tab) {
            numberView.setTextColor(this.mTabSelectedColor);
        } else {
            numberView.setTextColor(this.mNumberUnSelectedColor);
        }
    }

    public void updatePostCount(int count, int tabToUpdate) {
        ProfileViewHolder holder = this.mHolder;
        Resources res = this.mContext.getResources();
        if (res != null && holder != null) {
            if (tabToUpdate == 0) {
                invalidateCountAndTabColor(0, count, res, R.plurals.profile_posts, holder.postsLabel);
                invalidateTabColor(1, holder.likesLabel);
            } else {
                invalidateCountAndTabColor(1, count, res, R.plurals.profile_likes, holder.likesLabel);
                invalidateTabColor(0, holder.postsLabel);
            }
        }
    }

    private class UserRecsSpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public UserRecsSpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override // android.support.v7.widget.RecyclerView.ItemDecoration
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left = this.space;
            outRect.right = this.space;
            int viewPosition = parent.getChildLayoutPosition(view);
            if (viewPosition == 0) {
                outRect.left = this.space * 4;
            }
            if (ProfileHeaderAdapter.this.mRecommendedUserAdapter.getItemViewType(viewPosition) == 0) {
                outRect.right = this.space * 3;
            } else {
                outRect.right = this.space;
            }
        }
    }

    public void hideUserRecommendations() {
        HeightAnimationUtil.collapse(this.mHolder.userRecs);
        HeightAnimationUtil.collapse(this.mHolder.recommendedUsersLinear);
    }

    private void setupFollowButton(Button followButton, RoundedCornerBitmapImageView favIcon, View favIconPadding, VineUser user) {
        boolean isFollowing = !user.hasFollowRequested() && user.isFollowing();
        boolean isFavoriteUser = user.notifyPosts;
        followButton.setSelected(isFollowing);
        if (isFollowing && isFavoriteUser) {
            favIcon.setVisibility(0);
            favIconPadding.setVisibility(0);
            favIcon.setImageResource(R.drawable.ic_fav_active);
        } else if (isFollowing) {
            favIcon.setVisibility(0);
            favIconPadding.setVisibility(0);
            favIcon.setImageResource(R.drawable.ic_fav_inactive);
            favIcon.setBackgroundColor(this.mFavBackgroundColor);
        } else {
            favIcon.setVisibility(8);
            favIconPadding.setVisibility(8);
        }
        Resources res = this.mContext.getResources();
        if (isFollowing) {
            this.mHolder.followButton.setText(res.getString(R.string.profile_following));
        } else if (!user.hasFollowRequested() && !user.isFollowing() && this.mAppController.getActiveId() != user.userId) {
            this.mHolder.followButton.setText(res.getString(R.string.profile_follow));
        }
        if (user.hasFollowRequested()) {
            this.mHolder.followButton.setText(res.getString(R.string.profile_requested));
        }
    }

    private int getMaxButtonWidth(ProfileViewHolder holder, boolean isMe) {
        Set<Integer> candidates = new HashSet<>();
        if (isMe) {
            candidates.add(Integer.valueOf(measureButtonWithTextAndDrawable(holder.measuringButton, R.string.profile_settings, 0)));
        } else {
            candidates.add(Integer.valueOf(measureButtonWithTextAndDrawable(holder.measuringButton, R.string.profile_follow, 0)));
            candidates.add(Integer.valueOf(measureButtonWithTextAndDrawable(holder.measuringButton, R.string.profile_following, 0)));
            if (this.mUser.isPrivate()) {
                candidates.add(Integer.valueOf(measureButtonWithTextAndDrawable(holder.measuringButton, R.string.profile_requested, 0)));
            }
        }
        if (isMe && ClientFlagsHelper.enableDownloader(this.mContext)) {
            candidates.add(Integer.valueOf(measureButtonWithTextAndDrawable(holder.measuringButton, R.string.download_button, R.drawable.ic_watch_theater)));
        } else if (!BuildUtil.isAmazon() && ClientFlagsHelper.isTheaterModeEnabled(this.mContext)) {
            candidates.add(Integer.valueOf(measureButtonWithTextAndDrawable(holder.measuringButton, R.string.profile_watch, R.drawable.ic_watch_theater)));
        } else if (!isMe) {
            candidates.add(Integer.valueOf(measureButtonWithTextAndDrawable(holder.measuringButton, R.string.profile_message, 0)));
        }
        return ((Integer) Collections.max(candidates)).intValue();
    }

    private int measureButtonWithTextAndDrawable(Button button, int strResId, int drawResId) {
        button.setText(strResId);
        button.setCompoundDrawablesWithIntrinsicBounds(drawResId, 0, 0, 0);
        button.measure(0, 0);
        return button.getMeasuredWidth();
    }

    @Override // android.view.View.OnLongClickListener
    public boolean onLongClick(View view) {
        if (view.getId() != R.id.user_image || this.mUser == null || this.mUser.userId != this.mAppController.getActiveId()) {
            return false;
        }
        FlurryUtils.trackProfileImageClick(true);
        view.performHapticFeedback(0);
        ImageActivity.start(this.mContext, this.mUser.avatarUrl, R.string.sign_up_profile_photo);
        return true;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) throws Resources.NotFoundException {
        int id = view.getId();
        if (id == R.id.settings) {
            FlurryUtils.trackVisitSettings("Profile");
            Intent intent = new Intent(this.mContext, (Class<?>) SettingsActivity.class);
            this.mContext.startActivity(intent);
            return;
        }
        if (id == R.id.profile_follow) {
            handleProfileFollowButtonClick();
            return;
        }
        if (id == R.id.user_row_btn_follow) {
            handleUserRecommendationsFollowClick(view);
            return;
        }
        if (id == R.id.recommended_user_follow) {
            handleUserRecommendationsFollowClick(view);
            return;
        }
        if (id == R.id.clickable_region) {
            handleRecommendedUserClick(view);
            return;
        }
        if (id == R.id.likesParent || id == R.id.postsParent) {
            changeSelectedTab(view.getId());
            return;
        }
        if (id == R.id.user_image) {
            handleUserImageClick();
            return;
        }
        if (id == R.id.watch_button) {
            this.mListener.onTheaterModeClicked();
            return;
        }
        if (id == R.id.download_button) {
            this.mListener.onDownloadVideosClicked();
            return;
        }
        if (id == R.id.follow_request_accept_container) {
            this.mListener.onAcceptFollowRequestClicked();
            return;
        }
        if (id == R.id.follow_request_reject_container) {
            this.mListener.onRejectFollowRequestClicked();
            return;
        }
        if (id == R.id.postsDropDownHideRevine) {
            this.mHideProfileReposts = this.mHideProfileReposts ? false : true;
            this.mListener.onDropdownActionClicked(0);
            togglePostsDropdown();
            return;
        }
        if (id == R.id.postsDropDownTop) {
            setSortSelection(this.mHolder.postsDropdownHolder.topLabel);
            SwitchUtils.setSelected(this.mContext, this.mSelectedSwitch, false, -1);
            this.mSelectedSwitch = this.mHolder.postsDropdownHolder.topSelector;
            SwitchUtils.setSelected(this.mContext, this.mSelectedSwitch, true, 16734567);
            this.mListener.onDropdownActionClicked(1);
            togglePostsDropdown();
            return;
        }
        if (id == R.id.postsDropDownRecent) {
            setSortSelection(this.mHolder.postsDropdownHolder.recentLabel);
            SwitchUtils.setSelected(this.mContext, this.mSelectedSwitch, false, -1);
            this.mSelectedSwitch = this.mHolder.postsDropdownHolder.recentSelector;
            SwitchUtils.setSelected(this.mContext, this.mSelectedSwitch, true, 16734567);
            this.mListener.onDropdownActionClicked(2);
            togglePostsDropdown();
            return;
        }
        if (id == R.id.postsDropDownOldest) {
            setSortSelection(this.mHolder.postsDropdownHolder.oldestLabel);
            SwitchUtils.setSelected(this.mContext, this.mSelectedSwitch, false, -1);
            this.mSelectedSwitch = this.mHolder.postsDropdownHolder.oldestSelector;
            SwitchUtils.setSelected(this.mContext, this.mSelectedSwitch, true, 16734567);
            this.mListener.onDropdownActionClicked(3);
            togglePostsDropdown();
            return;
        }
        if (id == R.id.posts_dropdown) {
            togglePostsDropdown();
            return;
        }
        if (id == R.id.likesDropDownNewest) {
            setSortSelection(this.mHolder.likesDropdownHolder.newestLabel);
            SwitchUtils.setSelected(this.mContext, this.mSelectedSwitch, false, -1);
            this.mSelectedSwitch = this.mHolder.likesDropdownHolder.newestSelector;
            SwitchUtils.setSelected(this.mContext, this.mSelectedSwitch, true, 16734567);
            this.mListener.onDropdownActionClicked(4);
            toggleLikesDropdown();
            return;
        }
        if (id == R.id.likesDropDownOldest) {
            setSortSelection(this.mHolder.likesDropdownHolder.oldestLabel);
            SwitchUtils.setSelected(this.mContext, this.mSelectedSwitch, false, -1);
            this.mSelectedSwitch = this.mHolder.likesDropdownHolder.oldestSelector;
            SwitchUtils.setSelected(this.mContext, this.mSelectedSwitch, true, 16734567);
            this.mListener.onDropdownActionClicked(5);
            toggleLikesDropdown();
            return;
        }
        if (id == R.id.likes_dropdown) {
            toggleLikesDropdown();
            return;
        }
        if (id == R.id.followers) {
            this.mListener.onViewFollowersClicked();
            return;
        }
        if (id == R.id.following) {
            this.mListener.onViewFollowingClicked();
            return;
        }
        if (id == R.id.twitterTooltip) {
            this.mListener.onOpenTwitterClicked();
            return;
        }
        if (id == R.id.username_container) {
            this.mListener.onShowAccountSwitchingMenu();
        } else if (id == R.id.favorite_user) {
            this.mListener.onFavUserClicked();
        } else if (id == R.id.favorite_user_padding) {
            this.mListener.onFavUserClicked();
        }
    }

    private void setSortSelection(TextView view) {
        this.mSortSelectionView.setTextColor(this.mContext.getResources().getColor(android.R.color.black));
        this.mSortSelectionView = view;
        this.mSortSelectionView.setTextColor(this.mContext.getResources().getColor(R.color.vine_green));
    }

    private void handleUserRecommendationsFollowClick(View view) throws Resources.NotFoundException {
        FollowButtonViewHolder tag = (FollowButtonViewHolder) view.getTag();
        if (tag.user != null && (tag.user instanceof FollowableUser) && ((FollowableUser) tag.user).isTwitterFollow) {
            handleRecommendedTwitterFollow(tag);
            return;
        }
        boolean showTwitterFollow = ClientFlagsHelper.showTwitterFollowCard(this.mContext);
        handleRecommendedVineFollow(tag);
        int index = 0;
        while (true) {
            if (index >= this.mUserRecs.size()) {
                break;
            }
            VineUser user = this.mUserRecs.get(index);
            if (user.userId != tag.userId) {
                index++;
            } else if (!showTwitterFollow) {
                this.mUserRecs.remove(index);
            }
        }
        if (showTwitterFollow) {
            final int nextIndex = index + 1;
            if (tag.following) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() { // from class: co.vine.android.ProfileHeaderAdapter.5
                    @Override // java.lang.Runnable
                    public void run() {
                        ProfileHeaderAdapter.this.mHolder.recommendedUsers.smoothScrollToPosition(nextIndex + 1);
                    }
                }, 600L);
                return;
            }
            return;
        }
        this.mUserRecsAdapter.mergeData(this.mUserRecs, false);
        this.mUserRecsAdapter.notifyDataSetChanged();
        if (this.mUserRecs.isEmpty()) {
            hideUserRecommendations();
        }
    }

    private void handleRecommendedVineFollow(FollowButtonViewHolder holder) throws Resources.NotFoundException {
        int color;
        if (holder.user == null) {
            Components.userInteractionsComponent().followUser(this.mAppController, holder.userId, false, this.mFollowRecLogger);
            holder.following = true;
            return;
        }
        if (holder.following) {
            Components.userInteractionsComponent().unfollowUser(this.mAppController, holder.user.userId, false, this.mFollowRecLogger);
        } else {
            Components.userInteractionsComponent().followUser(this.mAppController, holder.user.userId, false, this.mFollowRecLogger);
        }
        holder.following = !holder.following;
        holder.user.following = holder.following ? 1 : 0;
        holder.followButton.setSelected(holder.following);
        Button button = holder.followButton;
        if (holder.following) {
            color = this.mContext.getResources().getColor(R.color.solid_white);
        } else {
            color = this.mContext.getResources().getColor(R.color.black_eighty_percent);
        }
        button.setTextColor(color);
        holder.followButton.setText(holder.following ? R.string.profile_following : R.string.profile_follow);
    }

    private void handleRecommendedTwitterFollow(FollowButtonViewHolder holder) {
        VineUser user = holder.user;
        if (user.followingOnTwitter) {
            followOnTwitter(false);
        } else if (this.mSharedPreferences.getBoolean("settings_twitter_connected", false)) {
            followOnTwitter(true);
        } else {
            Twitter twitter = this.mAppController.getTwitter();
            AppController.startTwitterAuthWithFinish(twitter, (Activity) this.mContext, 4);
        }
    }

    public void followOnTwitter(boolean follow) {
        if (follow) {
            Components.userInteractionsComponent().followUserOnTwitter(this.mAppController, this.mUser.userId, false, this.mFollowRecLogger);
        } else {
            Components.userInteractionsComponent().unfollowUserOnTwitter(this.mAppController, this.mUser.userId, false, this.mFollowRecLogger);
        }
        this.mUser.followingOnTwitter = this.mUser.followingOnTwitter ? false : true;
    }

    private void handleRecommendedUserClick(View view) {
        if (view.getTag() instanceof String) {
            UIEventScribeLogger.onFollowOnTwitterCardClick(this.mScribeLogger, this.mAppStateProvider, this.mAppNavProvider);
            new AlertDialog.Builder(this.mContext).setMessage(this.mContext.getString(R.string.go_to_twitter, '@' + this.mUser.twitterScreenname)).setPositiveButton(R.string.twitter_tooltip, new DialogInterface.OnClickListener() { // from class: co.vine.android.ProfileHeaderAdapter.7
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    UIEventScribeLogger.onLaunchTwitterFromFollowCard(ProfileHeaderAdapter.this.mScribeLogger, ProfileHeaderAdapter.this.mAppStateProvider, ProfileHeaderAdapter.this.mAppNavProvider);
                    ProfileHeaderAdapter.this.openTwitter(ProfileHeaderAdapter.this.mUser.twitterScreenname);
                }
            }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() { // from class: co.vine.android.ProfileHeaderAdapter.6
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            }).show();
        } else {
            long userid = ((Long) view.getTag()).longValue();
            ChannelActivity.startProfile(this.mContext, userid, "User Recs");
        }
    }

    public void openTwitter(String twitterScreenname) {
        Intent intent;
        try {
            this.mContext.getPackageManager().getPackageInfo("com.twitter.android", 0);
            Intent intent2 = new Intent("android.intent.action.VIEW", Uri.parse("twitter://user?screen_name=" + twitterScreenname));
            try {
                intent2.addFlags(268435456);
                intent = intent2;
            } catch (Exception e) {
                intent = new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=com.twitter.android"));
                this.mContext.startActivity(intent);
            }
        } catch (Exception e2) {
        }
        this.mContext.startActivity(intent);
    }

    private void handleUserImageClick() {
        if (this.mUser != null) {
            if (this.mUser.userId == this.mAppController.getActiveId()) {
                FlurryUtils.trackVisitSettings("Profile");
                this.mContext.startActivity(new Intent(this.mContext, (Class<?>) SettingsActivity.class));
            } else {
                FlurryUtils.trackProfileImageClick(false);
                ImageActivity.start(this.mContext, this.mUser.avatarUrl, R.string.sign_up_profile_photo);
            }
        }
    }

    private void togglePostsDropdown() throws Resources.NotFoundException {
        if (this.mUser != null) {
            View postsButton = this.mHolder.postsParent;
            if (this.mHolder.postsDropdownHolder.dropdown.getVisibility() == 0) {
                HeightAnimationUtil.collapse(this.mHolder.postsDropdownHolder.dropdown);
                this.mHolder.postsTabArrowImage.setColorFilter(new PorterDuffColorFilter(1275068416, PorterDuff.Mode.SRC_IN));
                return;
            }
            if (postsButton != null) {
                String showRevines = this.mContext.getString(R.string.show_revines);
                this.mHolder.postsDropdownHolder.revineParent.setVisibility(0);
                Util.setTextWithSpan(this.mBold, showRevines, this.mHolder.postsDropdownHolder.revineLabel);
                SpannableStringBuilder contentSb = new SpannableStringBuilder(showRevines);
                Util.safeSetSpan(contentSb, this.mBold, 0, contentSb.length(), 33);
                this.mHolder.postsDropdownHolder.revineLabel.setText(contentSb);
                SwitchUtils.setSelected(this.mContext, this.mHolder.postsDropdownHolder.revineSelector, this.mHideProfileReposts ? false : true, this.mHideProfileReposts ? -1 : this.mContext.getResources().getColor(R.color.vine_green));
                HeightAnimationUtil.expand(this.mHolder.postsDropdownHolder.dropdown);
                if (this.mUser.profileBackground == Settings.DEFAULT_PROFILE_COLOR) {
                    this.mHolder.postsTabArrowImage.setColorFilter(new PorterDuffColorFilter(this.mVineGreen | ViewCompat.MEASURED_STATE_MASK, PorterDuff.Mode.SRC_IN));
                } else {
                    this.mHolder.postsTabArrowImage.setColorFilter(new PorterDuffColorFilter(this.mUser.profileBackground | ViewCompat.MEASURED_STATE_MASK, PorterDuff.Mode.SRC_IN));
                }
            }
        }
    }

    private void toggleLikesDropdown() {
        if (this.mUser != null) {
            View likesButton = this.mHolder.likesParent;
            if (this.mHolder.likesDropdownHolder.dropdown.getVisibility() == 0) {
                HeightAnimationUtil.collapse(this.mHolder.likesDropdownHolder.dropdown);
                this.mHolder.likesTabArrowImage.setColorFilter(new PorterDuffColorFilter(1275068416, PorterDuff.Mode.SRC_IN));
            } else if (likesButton != null) {
                HeightAnimationUtil.expand(this.mHolder.likesDropdownHolder.dropdown);
                if (this.mUser.profileBackground == Settings.DEFAULT_PROFILE_COLOR) {
                    this.mHolder.likesTabArrowImage.setColorFilter(new PorterDuffColorFilter(this.mVineGreen | ViewCompat.MEASURED_STATE_MASK, PorterDuff.Mode.SRC_IN));
                } else {
                    this.mHolder.likesTabArrowImage.setColorFilter(new PorterDuffColorFilter(this.mUser.profileBackground | ViewCompat.MEASURED_STATE_MASK, PorterDuff.Mode.SRC_IN));
                }
            }
        }
    }

    private void handleProfileFollowButtonClick() {
        if (!this.mAppController.isLoggedIn()) {
            StartActivity.toStart(this.mContext);
            return;
        }
        if (!this.mUser.hasFollowRequested()) {
            if (this.mUser.isFollowing()) {
                this.mListener.onUnfollowUserClicked();
                this.mUser.following = 0;
                this.mUser.notifyPosts = false;
            } else if (!this.mUser.isFollowing()) {
                if (this.mUser.isPrivate()) {
                    this.mUser.setFollowRequested();
                } else {
                    this.mUser.following = 1;
                    this.mUser.repostsEnabled = 1;
                }
                this.mListener.onFollowUserClicked();
            }
            setupFollowButton(this.mHolder.followButton, this.mHolder.favoriteIcon, this.mHolder.favIconPadding, this.mUser);
        }
    }

    public void changeSelectedTab(int tabId) {
        if (this.mHolder != null) {
            if (tabId == R.id.postsParent) {
                this.mCurrentTab = 0;
                this.mListener.onPostsTabClicked();
            } else if (tabId == R.id.likesParent) {
                this.mCurrentTab = 1;
                this.mListener.onLikesTabClicked();
            }
            invalidateTabColor(0, this.mHolder.postsLabel);
            invalidateTabColor(1, this.mHolder.likesLabel);
            this.mHolder.postsTabArrow.setVisibility(this.mCurrentTab == 0 ? 0 : 8);
            this.mHolder.likesTabArrow.setVisibility(this.mCurrentTab != 1 ? 8 : 0);
        }
    }

    public void onImageLoaded(HashMap<ImageKey, UrlImage> images) {
        UrlImage urlImage;
        if (this.mHolderPagerMain != null) {
            if (this.mHolderPagerMain.userImageKey != null && (urlImage = images.get(this.mHolderPagerMain.userImageKey)) != null) {
                setImage(urlImage.bitmap);
            }
            this.mUserRecsAdapter.setUserImages(images);
        }
    }

    private void setImage(Bitmap bmp) {
        if (bmp == null) {
            this.mHolderPagerMain.imageView.setImageResource(R.drawable.avatar_large);
            if (this.mUser != null && this.mUser.userId == this.mAppController.getActiveId()) {
                this.mHolderPagerMain.imageAction.setVisibility(0);
                return;
            } else {
                this.mHolderPagerMain.imageAction.setVisibility(8);
                return;
            }
        }
        this.mHolderPagerMain.imageAction.setVisibility(8);
        this.mHolderPagerMain.imageView.setImageDrawable(new RecyclableBitmapDrawable(this.mHolderPagerMain.imageView.getResources(), bmp));
    }

    @Override // android.support.v4.view.ViewPager.OnPageChangeListener
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) throws Resources.NotFoundException {
        if (positionOffset > 0.0f && this.mMainPageHeight > 0 && this.mDetailPageHeight > this.mMainPageHeight) {
            int delta = this.mDetailPageHeight - this.mMainPageHeight;
            ViewGroup.LayoutParams param = this.mHolder.pager.getLayoutParams();
            param.height = (int) (this.mMainPageHeight + (delta * positionOffset));
            this.mHolder.pager.setLayoutParams(param);
        }
        if (this.mUser != null && TextUtils.isEmpty(this.mUser.description) && TextUtils.isEmpty(this.mUser.location)) {
            this.mHolder.pager.setCurrentItem(0, true);
        }
    }

    @Override // android.support.v4.view.ViewPager.OnPageChangeListener
    public void onPageSelected(int i) {
        if (this.mHolder != null && this.mHolder.dots != null) {
            this.mHolder.dots.setActiveDot(i);
        }
    }

    @Override // android.support.v4.view.ViewPager.OnPageChangeListener
    public void onPageScrollStateChanged(int i) {
    }

    private class ProfileViewPagerAdapter extends PagerAdapter {
        private ProfileViewPagerAdapter() {
        }

        @Override // android.support.v4.view.PagerAdapter
        public int getCount() {
            return 2;
        }

        @Override // android.support.v4.view.PagerAdapter
        public Object instantiateItem(ViewGroup container, int position) {
            View v;
            LayoutInflater inflater = (LayoutInflater) container.getContext().getSystemService("layout_inflater");
            switch (position) {
                case 0:
                    v = inflater.inflate(R.layout.profile_header_pager_main, container, false);
                    ProfileHeaderAdapter.this.mHolderPagerMain = new PagerMainViewHolder(v);
                    container.addView(v);
                    if (ProfileHeaderAdapter.this.mUser != null) {
                        ProfileHeaderAdapter.this.bindUser(ProfileHeaderAdapter.this.mUser);
                        break;
                    }
                    break;
                case 1:
                    View v2 = inflater.inflate(R.layout.profile_header_pager_details, container, false);
                    ProfileHeaderAdapter.this.mHolderPagerDetails = new PagerDetailsViewHolder(v2);
                    container.addView(v2);
                    if (ProfileHeaderAdapter.this.mUser != null) {
                        ProfileHeaderAdapter.this.bindUser(ProfileHeaderAdapter.this.mUser);
                    }
                    ProfileHeaderAdapter.this.invalidateDetailHeight();
                    break;
            }
            return v;
        }

        @Override // android.support.v4.view.PagerAdapter
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override // android.support.v4.view.PagerAdapter
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
        }
    }

    static class PagerMainViewHolder {
        public View accountSwitchingCaret;
        public View accountSwitchingMenu;
        public final TypefacesTextView followers;
        public final TypefacesTextView following;
        public ImageView imageAction;
        public ImageView imageFrame;
        public ImageView imageView;
        public View spacer;
        public View topSpacer;
        public TextView twitterScreenname;
        public ImageKey userImageKey;
        public TextView userLoopCount;
        public TextView username;
        public View usernameContainer;
        public ImageView verified;

        public PagerMainViewHolder(View v) {
            this.imageFrame = (ImageView) v.findViewById(R.id.user_image_frame);
            this.verified = (ImageView) v.findViewById(R.id.profile_verified);
            this.imageView = (ImageView) v.findViewById(R.id.user_image);
            this.imageAction = (ImageView) v.findViewById(R.id.user_image_action);
            this.userLoopCount = (TextView) v.findViewById(R.id.userLoopCount);
            this.username = (TextView) v.findViewById(R.id.username);
            this.twitterScreenname = (TextView) v.findViewById(R.id.twitterScreenname);
            this.topSpacer = v.findViewById(R.id.spacer_top);
            this.spacer = v.findViewById(R.id.spacer);
            this.accountSwitchingCaret = v.findViewById(R.id.caret);
            this.accountSwitchingMenu = v.findViewById(R.id.account_switching);
            this.usernameContainer = v.findViewById(R.id.username_container);
            this.followers = (TypefacesTextView) v.findViewById(R.id.followers);
            this.following = (TypefacesTextView) v.findViewById(R.id.following);
            View nibUp = v.findViewById(R.id.nib_up);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) nibUp.getLayoutParams();
            layoutParams.leftMargin = 0;
            layoutParams.gravity = 1;
            nibUp.setLayoutParams(layoutParams);
        }
    }

    static class PagerDetailsViewHolder {
        public TextView description;
        public TextView location;
        public View parent;

        public PagerDetailsViewHolder(View v) {
            this.parent = v.findViewById(R.id.measurableParent);
            this.location = (TextView) v.findViewById(R.id.location);
            this.description = (TextView) v.findViewById(R.id.description);
        }
    }

    static class ProfileViewHolder {
        public final ViewGroup buttons;
        public final DotIndicators dots;
        public final Button downloadButton;
        public final View favIconPadding;
        public final RoundedCornerBitmapImageView favoriteIcon;
        public final Button followButton;
        public final LikesDropdownHolder likesDropdownHolder;
        public final TypefacesTextView likesLabel;
        public final View likesParent;
        public final ViewGroup likesTabArrow;
        public final ImageView likesTabArrowImage;
        public final Button measuringButton;
        public final ViewPager pager;
        public final PlaylistHolder playlistHolder;
        public final PostsDropdownHolder postsDropdownHolder;
        public final TypefacesTextView postsLabel;
        public final View postsParent;
        public final ViewGroup postsTabArrow;
        public final ImageView postsTabArrowImage;
        public final RelativeLayout profileBackground;
        public final ProgressBar progress;
        public final RecyclerView recommendedUsers;
        public final View recommendedUsersLinear;
        public final View rightButtonContainer;
        public final Button settings;
        public final View twitter;
        public final ImageView twitterAvatar;
        public final ImageButton twitterFollow;
        public final TextView twitterText;
        public final LinearLayout twitterTooltip;
        public long userId;
        public final RowLimitedLinearLayout userRecs;
        public final Button watchButton;

        public ProfileViewHolder(View v) {
            this.profileBackground = (RelativeLayout) v.findViewById(R.id.profile_content_outer);
            this.twitterTooltip = (LinearLayout) v.findViewById(R.id.twitterTooltip);
            this.progress = (ProgressBar) v.findViewById(R.id.profile_progress);
            this.pager = (ViewPager) v.findViewById(R.id.pager);
            this.buttons = (ViewGroup) v.findViewById(R.id.profile_actions_container);
            this.measuringButton = (Button) v.findViewById(R.id.measuring_button);
            this.settings = (Button) v.findViewById(R.id.settings);
            this.followButton = (Button) v.findViewById(R.id.profile_follow);
            this.favIconPadding = v.findViewById(R.id.favorite_user_padding);
            this.favoriteIcon = (RoundedCornerBitmapImageView) v.findViewById(R.id.favorite_user);
            this.rightButtonContainer = v.findViewById(R.id.right_button);
            this.watchButton = (Button) v.findViewById(R.id.watch_button);
            this.downloadButton = (Button) v.findViewById(R.id.download_button);
            this.dots = (DotIndicators) v.findViewById(R.id.dots);
            this.postsParent = v.findViewById(R.id.postsParent);
            this.postsLabel = (TypefacesTextView) v.findViewById(R.id.postsLabel);
            this.postsTabArrow = (ViewGroup) v.findViewById(R.id.posts_dropdown);
            this.postsTabArrowImage = (ImageView) v.findViewById(R.id.posts_arrow_image);
            this.likesParent = v.findViewById(R.id.likesParent);
            this.likesLabel = (TypefacesTextView) v.findViewById(R.id.likesLabel);
            this.likesTabArrow = (ViewGroup) v.findViewById(R.id.likes_dropdown);
            this.likesTabArrowImage = (ImageView) v.findViewById(R.id.likes_arrow_image);
            this.userRecs = (RowLimitedLinearLayout) v.findViewById(R.id.recommended_users_list);
            this.recommendedUsers = (RecyclerView) v.findViewById(R.id.recommended_users_list_recyclerview);
            this.recommendedUsersLinear = v.findViewById(R.id.recommended_users_list_linear);
            this.postsDropdownHolder = new PostsDropdownHolder(v);
            this.likesDropdownHolder = new LikesDropdownHolder(v);
            this.playlistHolder = new PlaylistHolder(v);
            this.twitter = v.findViewById(R.id.twitter);
            this.twitterAvatar = (ImageView) v.findViewById(R.id.twitter_avatar);
            this.twitterText = (TextView) v.findViewById(R.id.twitter_follow_text);
            this.twitterFollow = (ImageButton) v.findViewById(R.id.twitter_follow);
        }
    }

    public static class PostsDropdownHolder {
        public final View dropdown;
        public final TextView oldestLabel;
        public final View oldestPostsView;
        public final View oldestSelector;
        public final TextView recentLabel;
        public final View recentPostsView;
        public final View recentSelector;
        public final TextView revineLabel;
        public final View revineParent;
        public final View revineSelector;
        public final TextView topLabel;
        public final View topPostsView;
        public final View topSelector;

        public PostsDropdownHolder(View v) {
            this.dropdown = v.findViewById(R.id.postsLikeDropDown);
            this.revineParent = v.findViewById(R.id.postsDropDownHideRevine);
            this.revineLabel = (TextView) v.findViewById(R.id.postsDropDownHideRevineLabel);
            this.topPostsView = v.findViewById(R.id.postsDropDownTop);
            this.recentPostsView = v.findViewById(R.id.postsDropDownRecent);
            this.oldestPostsView = v.findViewById(R.id.postsDropDownOldest);
            this.topLabel = (TextView) v.findViewById(R.id.postsDropDownTopLabel);
            this.recentLabel = (TextView) v.findViewById(R.id.postsDropDownRecentLabel);
            this.oldestLabel = (TextView) v.findViewById(R.id.postsDropDownOldestLabel);
            this.topSelector = v.findViewById(R.id.posts_top_switch);
            this.recentSelector = v.findViewById(R.id.recent_switch);
            this.oldestSelector = v.findViewById(R.id.posts_oldest_switch);
            this.revineSelector = v.findViewById(R.id.revines_switch);
        }
    }

    public static class LikesDropdownHolder {
        public final View dropdown;
        public final TextView newestLabel;
        public final View newestLikesView;
        public final View newestSelector;
        public final TextView oldestLabel;
        public final View oldestPostsView;
        public final View oldestSelector;

        public LikesDropdownHolder(View v) {
            this.dropdown = v.findViewById(R.id.likesSortDropDown);
            this.newestLikesView = v.findViewById(R.id.likesDropDownNewest);
            this.oldestPostsView = v.findViewById(R.id.likesDropDownOldest);
            this.newestLabel = (TextView) v.findViewById(R.id.likesDropDownNewestLabel);
            this.oldestLabel = (TextView) v.findViewById(R.id.likesDropDownOldestLabel);
            this.newestSelector = v.findViewById(R.id.likes_newest_switch);
            this.oldestSelector = v.findViewById(R.id.likes_oldest_switch);
        }
    }

    public static class PlaylistHolder {
        public final RelativeLayout relativeLayout;

        public PlaylistHolder(View v) {
            this.relativeLayout = (RelativeLayout) v.findViewById(R.id.playlist_header);
        }
    }

    public void updateFollowButtonState(VineUser user) {
        if (this.mHolder != null) {
            setupFollowButton(this.mHolder.followButton, this.mHolder.favoriteIcon, this.mHolder.favIconPadding, user);
        }
    }

    public void toggleTwitterTooltip() {
        if (this.mHolder.twitterTooltip.getVisibility() == 0) {
            this.mHolder.twitterTooltip.setVisibility(8);
        } else {
            UIEventScribeLogger.twitterHandleTap(this.mScribeLogger, this.mAppStateProvider, this.mAppNavProvider);
            this.mHolder.twitterTooltip.setVisibility(0);
        }
    }

    public void hideTwitterTooltip() {
        this.mHolder.twitterTooltip.setVisibility(8);
    }

    public void toggleAccountSwitchingMenu() {
        if (this.mHolderPagerMain.accountSwitchingMenu.getVisibility() == 0) {
            this.mHolderPagerMain.accountSwitchingCaret.animate().rotation(180.0f).setDuration(200L);
            this.mHolderPagerMain.accountSwitchingMenu.animate().alpha(0.0f).setDuration(300L).setListener(new Animator.AnimatorListener() { // from class: co.vine.android.ProfileHeaderAdapter.8
                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animation) {
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animation) {
                    ProfileHeaderAdapter.this.mHolderPagerMain.accountSwitchingMenu.setVisibility(8);
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animation) {
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationRepeat(Animator animation) {
                }
            });
        } else {
            this.mHolderPagerMain.accountSwitchingCaret.animate().rotation(0.0f).setDuration(200L);
            this.mHolderPagerMain.accountSwitchingMenu.animate().alpha(1.0f).setDuration(300L).setListener(new Animator.AnimatorListener() { // from class: co.vine.android.ProfileHeaderAdapter.9
                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animation) {
                    ProfileHeaderAdapter.this.mHolderPagerMain.accountSwitchingMenu.setVisibility(0);
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animation) {
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animation) {
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationRepeat(Animator animation) {
                }
            });
        }
    }

    public void setPostsSortOrder(String sortOrder) {
        this.mPostsSortOrder = sortOrder;
    }

    public void setLikesSortOrder(String sortOrder) {
        this.mLikesSortOrder = sortOrder;
    }

    public class SlowScrollLinearLayoutManager extends LinearLayoutManager {
        public SlowScrollLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        @Override // android.support.v7.widget.LinearLayoutManager, android.support.v7.widget.RecyclerView.LayoutManager
        public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
            LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(recyclerView.getContext()) { // from class: co.vine.android.ProfileHeaderAdapter.SlowScrollLinearLayoutManager.1
                @Override // android.support.v7.widget.LinearSmoothScroller
                public PointF computeScrollVectorForPosition(int targetPosition) {
                    return SlowScrollLinearLayoutManager.this.computeScrollVectorForPosition(targetPosition);
                }

                @Override // android.support.v7.widget.LinearSmoothScroller
                protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                    return 100.0f / displayMetrics.densityDpi;
                }
            };
            linearSmoothScroller.setTargetPosition(position);
            startSmoothScroll(linearSmoothScroller);
        }
    }
}
