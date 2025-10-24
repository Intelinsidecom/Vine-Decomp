package co.vine.android;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import co.vine.android.api.VineChannel;
import co.vine.android.client.AppController;
import co.vine.android.util.BuildUtil;
import co.vine.android.util.ClientFlagsHelper;
import co.vine.android.util.ResourceLoader;
import co.vine.android.widget.DotIndicators;
import co.vine.android.widget.TypefacesTextView;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/* loaded from: classes.dex */
public class ChannelHeaderAdapter extends BaseAdapter implements ViewPager.OnPageChangeListener, View.OnClickListener {
    private final AppController mAppController;
    private VineChannel mChannel;
    private Context mContext;
    private int mCurrentTab = 0;
    private ChannelViewHolder mHolder;
    private PagerDetailsViewHolder mHolderPagerDetails;
    private PagerMainViewHolder mHolderPagerMain;
    private final ChannelHeaderListener mListener;
    private int mMainRGB;
    private final ResourceLoader mResourceLoader;
    private final boolean mShowRecent;

    public interface ChannelHeaderListener {
        void onFollowChannelClicked();

        void onForYouTabClicked();

        void onPopularTabClicked();

        void onRecentTabClicked();

        void onTheaterModeClicked();
    }

    public ChannelHeaderAdapter(Context context, AppController appController, ChannelHeaderListener listener, VineChannel channel, int mainRGB, boolean showRecent) {
        this.mContext = context;
        this.mAppController = appController;
        this.mResourceLoader = new ResourceLoader(this.mContext, this.mAppController);
        this.mListener = listener;
        this.mChannel = channel;
        this.mMainRGB = mainRGB;
        this.mShowRecent = showRecent;
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
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.channel_header, root, false);
            this.mHolder = new ChannelViewHolder(convertView);
            this.mHolder.pager.setAdapter(new ChannelViewPagerAdapter());
            this.mHolder.pager.setOnPageChangeListener(this);
            this.mHolder.dots.setFinalIcon(false);
            this.mHolder.dots.setNumberOfDots(2);
            if (this.mHolderPagerDetails != null && !TextUtils.isEmpty(this.mHolderPagerDetails.description.getText())) {
                this.mHolder.dots.setVisibility(0);
            } else {
                this.mHolder.dots.setVisibility(4);
            }
            this.mHolder.buttons.setVisibility(0);
        }
        return convertView;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void bindChannel() {
        if (this.mChannel != null && this.mHolder != null && this.mHolderPagerMain != null && this.mHolderPagerDetails != null) {
            if (!this.mShowRecent) {
                this.mHolder.channelTabs.setVisibility(8);
            }
            if (!TextUtils.isEmpty(this.mChannel.exploreRetinaIconFullUrl)) {
                this.mResourceLoader.setImageWhenLoaded(new ResourceLoader.ImageViewImageSetter(this.mHolderPagerMain.icon), this.mChannel.exploreRetinaIconFullUrl);
            }
            this.mHolder.channelBackground.setBackgroundColor(this.mMainRGB | ViewCompat.MEASURED_STATE_MASK);
            if (!TextUtils.isEmpty(this.mChannel.description)) {
                this.mHolderPagerDetails.description.setText(this.mChannel.description);
                this.mHolderPagerDetails.description.setVisibility(0);
            } else {
                this.mHolderPagerDetails.description.setVisibility(8);
            }
            this.mHolder.buttons.setVisibility(0);
            boolean showTheaterMode = !BuildUtil.isAmazon() && ClientFlagsHelper.isTheaterModeEnabled(this.mContext);
            int actionButtonWidth = getMaxButtonWidth(this.mHolder, showTheaterMode);
            this.mHolder.followButton.setWidth(actionButtonWidth);
            this.mHolder.followButton.setVisibility(0);
            this.mHolder.followButton.setOnClickListener(this);
            setupFollowButton(this.mHolder.followButton);
            if (showTheaterMode) {
                this.mHolder.watchButton.setWidth(actionButtonWidth);
                this.mHolder.watchButton.setVisibility(0);
                this.mHolder.watchButton.setOnClickListener(this);
            } else {
                this.mHolder.rightButtonContainer.setVisibility(8);
            }
            this.mHolder.popularParent.setOnClickListener(this);
            this.mHolder.recentParent.setOnClickListener(this);
            this.mHolder.forYouParent.setOnClickListener(this);
            if (ClientFlagsHelper.isChannelForYouTabEnabled(this.mContext)) {
                this.mHolder.secondVerticalDivider.setVisibility(0);
                this.mHolder.forYouParent.setVisibility(0);
            }
            invalidateTabColor(0, this.mHolder.popularLabel);
            invalidateTabColor(1, this.mHolder.recentLabel);
            invalidateTabColor(2, this.mHolder.forYouLabel);
        }
    }

    private void invalidateTabColor(int tab, TextView numberView) {
        if (this.mCurrentTab == tab) {
            numberView.setTextColor(this.mMainRGB);
        } else {
            numberView.setTextColor(this.mContext.getResources().getColor(R.color.black_thirty_five_percent));
        }
    }

    private void setupFollowButton(Button followButton) {
        followButton.setSelected(this.mChannel.following);
        Resources res = this.mContext.getResources();
        if (this.mChannel.following) {
            this.mHolder.followButton.setText(res.getString(R.string.profile_following));
        } else {
            this.mHolder.followButton.setText(res.getString(R.string.profile_follow));
        }
    }

    public void setFollowing(boolean following) {
        this.mChannel.following = following;
        setupFollowButton(this.mHolder.followButton);
    }

    private int getMaxButtonWidth(ChannelViewHolder holder, boolean showTheaterMode) {
        Set<Integer> candidates = new HashSet<>();
        candidates.add(Integer.valueOf(measureButtonWithTextAndDrawable(holder.measuringButton, R.string.profile_follow, 0)));
        candidates.add(Integer.valueOf(measureButtonWithTextAndDrawable(holder.measuringButton, R.string.profile_following, 0)));
        if (showTheaterMode) {
            candidates.add(Integer.valueOf(measureButtonWithTextAndDrawable(holder.measuringButton, R.string.profile_watch, R.drawable.ic_watch_theater)));
        }
        return ((Integer) Collections.max(candidates)).intValue();
    }

    private int measureButtonWithTextAndDrawable(Button button, int strResId, int drawResId) {
        button.setText(strResId);
        button.setCompoundDrawablesWithIntrinsicBounds(drawResId, 0, 0, 0);
        button.measure(0, 0);
        return button.getMeasuredWidth();
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.channel_follow) {
            handleChannelFollowButtonClick();
            return;
        }
        if (id == R.id.watch_button) {
            this.mListener.onTheaterModeClicked();
        } else if (id == R.id.popular_parent || id == R.id.recent_parent || id == R.id.for_you_parent) {
            changeSelectedTab(id);
        }
    }

    private void handleChannelFollowButtonClick() {
        if (!this.mAppController.isLoggedIn()) {
            StartActivity.toStart(this.mContext);
            return;
        }
        this.mListener.onFollowChannelClicked();
        this.mChannel.following = !this.mChannel.following;
        setupFollowButton(this.mHolder.followButton);
    }

    public void changeSelectedTab(int tabId) {
        if (this.mHolder != null) {
            if (tabId == R.id.popular_parent) {
                this.mCurrentTab = 0;
                this.mListener.onPopularTabClicked();
            } else if (tabId == R.id.recent_parent) {
                this.mCurrentTab = 1;
                this.mListener.onRecentTabClicked();
            } else if (tabId == R.id.for_you_parent) {
                this.mCurrentTab = 2;
                this.mListener.onForYouTabClicked();
            }
            invalidateTabColor(0, this.mHolder.popularLabel);
            invalidateTabColor(1, this.mHolder.recentLabel);
            invalidateTabColor(2, this.mHolder.forYouLabel);
        }
    }

    @Override // android.support.v4.view.ViewPager.OnPageChangeListener
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) throws Resources.NotFoundException {
        if (TextUtils.isEmpty(this.mHolderPagerDetails.description.getText())) {
            this.mHolder.pager.setCurrentItem(0);
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

    private class ChannelViewPagerAdapter extends PagerAdapter {
        private ChannelViewPagerAdapter() {
        }

        @Override // android.support.v4.view.PagerAdapter
        public int getCount() {
            return 2;
        }

        @Override // android.support.v4.view.PagerAdapter
        public Object instantiateItem(ViewGroup container, int position) {
            LayoutInflater inflater = (LayoutInflater) container.getContext().getSystemService("layout_inflater");
            switch (position) {
                case 0:
                    View v = inflater.inflate(R.layout.channel_header_pager_main, container, false);
                    ChannelHeaderAdapter.this.mHolderPagerMain = new PagerMainViewHolder(v);
                    container.addView(v);
                    ChannelHeaderAdapter.this.bindChannel();
                    return v;
                case 1:
                    View v2 = inflater.inflate(R.layout.channel_header_pager_details, container, false);
                    ChannelHeaderAdapter.this.mHolderPagerDetails = new PagerDetailsViewHolder(v2);
                    container.addView(v2);
                    ChannelHeaderAdapter.this.bindChannel();
                    return v2;
                default:
                    return null;
            }
        }

        @Override // android.support.v4.view.PagerAdapter
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }
    }

    static class PagerMainViewHolder {
        public ImageView icon;

        public PagerMainViewHolder(View v) {
            this.icon = (ImageView) v.findViewById(R.id.channel_icon);
        }
    }

    static class PagerDetailsViewHolder {
        public TextView description;

        public PagerDetailsViewHolder(View v) {
            this.description = (TextView) v.findViewById(R.id.channel_description);
        }
    }

    static class ChannelViewHolder {
        public final ViewGroup buttons;
        public final RelativeLayout channelBackground;
        public final View channelTabs;
        public final DotIndicators dots;
        public final Button followButton;
        public final TypefacesTextView forYouLabel;
        public final View forYouParent;
        public final Button measuringButton;
        public final ViewPager pager;
        public final TypefacesTextView popularLabel;
        public final View popularParent;
        public final TypefacesTextView recentLabel;
        public final View recentParent;
        public final View rightButtonContainer;
        public final View secondVerticalDivider;
        public final Button watchButton;

        public ChannelViewHolder(View v) {
            this.channelBackground = (RelativeLayout) v.findViewById(R.id.channel_content_outer);
            this.pager = (ViewPager) v.findViewById(R.id.header_pager);
            this.dots = (DotIndicators) v.findViewById(R.id.dots);
            this.buttons = (ViewGroup) v.findViewById(R.id.channel_actions_container);
            this.measuringButton = (Button) v.findViewById(R.id.measuring_button);
            this.followButton = (Button) v.findViewById(R.id.channel_follow);
            this.rightButtonContainer = v.findViewById(R.id.right_button);
            this.watchButton = (Button) v.findViewById(R.id.watch_button);
            this.popularParent = v.findViewById(R.id.popular_parent);
            this.popularLabel = (TypefacesTextView) v.findViewById(R.id.popular_label);
            this.recentParent = v.findViewById(R.id.recent_parent);
            this.recentLabel = (TypefacesTextView) v.findViewById(R.id.recent_label);
            this.forYouParent = v.findViewById(R.id.for_you_parent);
            this.forYouLabel = (TypefacesTextView) v.findViewById(R.id.for_you_label);
            this.secondVerticalDivider = v.findViewById(R.id.second_vertical_divider);
            this.channelTabs = v.findViewById(R.id.channel_tabs);
        }
    }
}
