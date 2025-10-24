package co.vine.android;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;
import co.vine.android.recorder2.model.ImportVideoInfo;
import co.vine.android.widget.Typefaces;
import co.vine.android.widget.tabs.IconTabHost;
import co.vine.android.widget.tabs.TabIndicator;
import co.vine.android.widget.tabs.TabsAdapter;
import co.vine.android.widget.tabs.ViewPagerScrollBar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/* loaded from: classes.dex */
public class ImportVideoTabActivity extends BaseControllerActionBarActivity implements TabHost.OnTabChangeListener, IconTabHost.OnTabClickedListener {
    private Button mAddButton;
    private int mDisabledTabColor;
    private int mEnabledTabColor;
    private int mLastItem;
    private ViewPagerScrollBar mScrollBar;
    private LinkedList<ImportVideoInfo> mSelected;
    private IconTabHost mTabHost;
    private int mTabOnOpen = 0;
    private TabsAdapter mTabsAdapter;
    private HashMap<String, Integer> mVideoPositions;
    private TextView mVideoSelectedIndicator;
    private ViewPager mViewPager;

    @Override // co.vine.android.BaseActionBarActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityDonut, android.app.Activity
    @SuppressLint({"MissingSuperCall"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_import_video_tab, true);
        setupTabs();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.mAddButton = (Button) findViewById(R.id.import_add);
        this.mVideoSelectedIndicator = (TextView) findViewById(R.id.video_selected);
        this.mAddButton.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.ImportVideoTabActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                Iterator it = ImportVideoTabActivity.this.mSelected.iterator();
                while (it.hasNext()) {
                    ImportVideoInfo video = (ImportVideoInfo) it.next();
                    if (video.getLocalPath() == null) {
                        return;
                    }
                }
                Intent selectedVideos = new Intent();
                selectedVideos.putExtra("extra_selected_videos", ImportVideoTabActivity.this.mSelected);
                ImportVideoTabActivity.this.setResult(-1, selectedVideos);
                ImportVideoTabActivity.this.finish();
            }
        });
        IconTabHost tabHost = this.mTabHost;
        Resources res = getResources();
        this.mDisabledTabColor = res.getColor(R.color.solid_dark_gray);
        this.mEnabledTabColor = res.getColor(R.color.solid_white);
        this.mScrollBar = (ViewPagerScrollBar) findViewById(R.id.scrollbar);
        this.mScrollBar.setRange(2);
        this.mTabsAdapter = new TabsAdapter(this, tabHost, this.mViewPager, this.mScrollBar);
        this.mTabsAdapter.enableSetActionBarColorOnPageSelected(false);
        this.mVideoPositions = new HashMap<>();
        this.mSelected = new LinkedList<>();
        tabHost.setOnTabChangedListener(this);
        tabHost.setOnTabClickedListener(this);
        addCameraTab();
        addLikesTab();
        this.mTabHost.setCurrentTab(this.mTabOnOpen);
        setCurrentTabByTag("camera");
    }

    @Override // co.vine.android.widget.tabs.IconTabHost.OnTabClickedListener
    public void onCurrentTabClicked() {
    }

    @Override // android.app.Activity
    public boolean onPrepareOptionsMenu(Menu menu) {
        return false;
    }

    @Override // android.widget.TabHost.OnTabChangeListener
    public void onTabChanged(String tag) throws Resources.NotFoundException {
        changeTabColor(this.mLastItem);
        int tab = this.mTabHost.getCurrentTab();
        this.mTabHost.setCurrentTabByTag(tag);
        this.mViewPager.setCurrentItem(tab);
        if (tab == 0) {
            this.mScrollBar.setScrollBarWidth(55);
        } else if (tab == 1) {
            this.mScrollBar.setScrollBarWidth(40);
        }
        this.mScrollBar.setPosition(this.mViewPager.getCurrentItem());
        Fragment currentFrag = this.mTabsAdapter.getCurrentFragment();
        if (currentFrag != null) {
            ((BaseGridViewFragment) currentFrag).updateAdapter();
        }
        this.mLastItem = tab;
    }

    private void setCurrentTabByTag(String tag) {
        TabHost tabHost = this.mTabHost;
        if (!tag.equals(tabHost.getCurrentTabTag())) {
            tabHost.setCurrentTabByTag(tag);
        }
        this.mScrollBar.setPosition(this.mViewPager.getCurrentItem());
    }

    private void setupTabs() {
        this.mTabHost = (IconTabHost) findViewById(android.R.id.tabhost);
        if (this.mTabHost == null) {
            throw new RuntimeException("Your content must have a TabHost whose id attribute is 'android.R.id.tabhost'");
        }
        this.mTabHost.setup();
        this.mViewPager = (ViewPager) findViewById(R.id.pager);
    }

    private void changeTabColor(int lastItem) {
        if (lastItem >= 0) {
            ((TabIndicator) this.mTabHost.getTabWidget().getChildTabViewAt(lastItem)).setTextColor(this.mDisabledTabColor);
        }
        int newItem = this.mTabHost.getCurrentTab();
        ((TabIndicator) this.mTabHost.getTabWidget().getChildTabViewAt(newItem)).setTextColor(this.mEnabledTabColor);
    }

    private void addCameraTab() {
        Bundle cameraRollBundle = new Bundle();
        cameraRollBundle.putInt("columns", 3);
        TabHost.TabSpec tabSpecCamera = this.mTabHost.newTabSpec("camera");
        tabSpecCamera.setIndicator(getTabIndicator(R.string.videos_tab, R.color.solid_white));
        this.mTabsAdapter.addTab(tabSpecCamera, ImportGalleryFragment.class, cameraRollBundle);
    }

    private void addLikesTab() {
        Bundle likedVinesBundle = new Bundle();
        likedVinesBundle.putInt("columns", 3);
        TabHost.TabSpec tabSpecLikes = this.mTabHost.newTabSpec("likes");
        tabSpecLikes.setIndicator(getTabIndicator(R.string.likes_tab, R.color.solid_white));
        this.mTabsAdapter.addTab(tabSpecLikes, ImportLikedVineFragment.class, likedVinesBundle);
    }

    private TabIndicator getTabIndicator(int tabTextId, int colorResId) throws Resources.NotFoundException {
        TabIndicator indicator = TabIndicator.newTextIndicator(LayoutInflater.from(this), R.layout.splash_tab_indicator, this.mTabHost, tabTextId, false);
        ColorStateList indicatorStateList = TabIndicator.createTextColorList(getResources().getColor(colorResId), getResources().getColor(R.color.white_fifty_percent));
        TextView indicatorText = indicator.getIndicatorText();
        indicatorText.setTextColor(indicatorStateList);
        indicatorText.setTypeface(Typefaces.get(this).lightContentBold);
        return indicator;
    }

    public void updateSelection(String path, String localPath, String sourcePostId) {
        if (this.mVideoPositions.containsKey(path)) {
            int deletedIndex = this.mVideoPositions.get(path).intValue() - 1;
            this.mVideoPositions.remove(path);
            this.mSelected.remove(deletedIndex);
            for (int i = deletedIndex; i < this.mSelected.size(); i++) {
                this.mVideoPositions.put(this.mSelected.get(i).getVideoUrl(), Integer.valueOf(i + 1));
            }
        } else {
            ImportVideoInfo video = new ImportVideoInfo(path, localPath, sourcePostId);
            this.mSelected.add(video);
            this.mVideoPositions.put(path, Integer.valueOf(this.mSelected.size()));
        }
        this.mVideoSelectedIndicator.setText(getString(R.string.amount_of_vid_selected, new Object[]{Integer.valueOf(this.mSelected.size())}));
    }

    public int getVideoOrder(String path) {
        if (this.mVideoPositions.containsKey(path)) {
            return this.mVideoPositions.get(path).intValue();
        }
        return -1;
    }

    public void setLocalPath(String path, String localPath) {
        int index = this.mVideoPositions.get(path).intValue() - 1;
        if (index > -1 && index < this.mSelected.size()) {
            ImportVideoInfo video = this.mSelected.get(index);
            video.setLocalPath(localPath);
        }
    }

    public int getPreviewContainerID() {
        return R.id.fragment_container;
    }
}
