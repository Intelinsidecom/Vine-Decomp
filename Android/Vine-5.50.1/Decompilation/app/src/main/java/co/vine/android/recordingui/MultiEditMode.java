package co.vine.android.recordingui;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Point;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import co.vine.android.R;
import co.vine.android.RecordingNavigationController;
import co.vine.android.recorder2.InvalidateGhostListener;
import co.vine.android.recorder2.RecordController;
import co.vine.android.recorder2.model.Segment;
import co.vine.android.util.SystemUtil;
import co.vine.android.views.HorizontalListView;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class MultiEditMode {
    private Activity mActivity;
    private MultiTrimPagerAdapter mAdapter;
    private RecordController mController;
    private InvalidateGhostListener mGhostListener;
    private HorizontalListView mListView;
    private RecordingNavigationController mNavigationController;
    private FrameLayout mPagerContainer;
    private ViewGroup mRoot;
    private ArrayList<Segment> mSegments;
    private Point mSize;
    private Toolbar mToolbar;
    private MultiImportTrimmerManager mTrimmerManager;
    private LinearLayout mUtilityTray;
    private ViewPager mViewPager;

    public MultiEditMode(RecordController controller, RecordingNavigationController viewController, Activity activity, InvalidateGhostListener ghostListener) throws Resources.NotFoundException {
        this.mActivity = activity;
        this.mSize = SystemUtil.getDisplaySize(this.mActivity);
        this.mRoot = (ViewGroup) this.mActivity.findViewById(R.id.trim);
        this.mListView = (HorizontalListView) this.mActivity.findViewById(android.R.id.list);
        this.mToolbar = (Toolbar) this.mActivity.findViewById(R.id.toolbar);
        this.mUtilityTray = (LinearLayout) this.mActivity.findViewById(R.id.utility_tray);
        this.mViewPager = (ViewPager) this.mActivity.findViewById(R.id.preview_pager);
        this.mPagerContainer = (FrameLayout) this.mActivity.findViewById(R.id.pager_container);
        this.mController = controller;
        this.mNavigationController = viewController;
        this.mGhostListener = ghostListener;
        setUpTrimmer(this.mRoot);
    }

    private void setUpViewPager() throws IllegalAccessException, Resources.NotFoundException, IllegalArgumentException, InvocationTargetException {
        this.mPagerContainer.getLayoutParams().height = this.mSize.x;
        this.mPagerContainer.requestLayout();
        this.mViewPager.setVisibility(0);
        this.mViewPager.setClipToPadding(false);
        this.mViewPager.setPageMargin((int) (this.mActivity.getResources().getDisplayMetrics().widthPixels * (-0.37f)));
        this.mViewPager.setOffscreenPageLimit(this.mSegments.size());
        this.mViewPager.setPageTransformer(false, new ViewPager.PageTransformer() { // from class: co.vine.android.recordingui.MultiEditMode.1
            @Override // android.support.v4.view.ViewPager.PageTransformer
            public void transformPage(View page, float position) {
                page.setScaleX(0.8f - Math.abs(0.6f * position));
                page.setScaleY(0.8f - Math.abs(0.3f * position));
                page.setAlpha(1.0f - Math.abs(0.5f * position));
            }
        });
        this.mViewPager.setAdapter(this.mAdapter);
        this.mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() { // from class: co.vine.android.recordingui.MultiEditMode.2
            @Override // android.support.v4.view.ViewPager.OnPageChangeListener
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override // android.support.v4.view.ViewPager.OnPageChangeListener
            public void onPageSelected(int position) {
                MultiEditMode.this.mAdapter.show(position);
            }

            @Override // android.support.v4.view.ViewPager.OnPageChangeListener
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    public void show() throws IllegalAccessException, Resources.NotFoundException, IllegalArgumentException, InvocationTargetException {
        this.mListView.setVisibility(0);
        this.mPagerContainer.setVisibility(0);
        this.mUtilityTray.setVisibility(0);
        this.mSegments = this.mController.getDraft().getSegments();
        this.mAdapter = new MultiTrimPagerAdapter(this.mActivity, this.mSegments, this.mTrimmerManager);
        setUpViewPager();
        this.mUtilityTray.findViewById(R.id.edit_crop).setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.recordingui.MultiEditMode.3
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                MultiEditMode.this.mAdapter.toggleCrop();
            }
        });
        this.mController.startEditing();
        this.mRoot.setVisibility(0);
    }

    public void hide() {
        this.mListView.setVisibility(4);
        this.mPagerContainer.setVisibility(4);
        this.mUtilityTray.setVisibility(4);
        this.mRoot.setVisibility(4);
        if (this.mAdapter != null) {
            this.mAdapter.release();
        }
    }

    private void setUpTrimmer(ViewGroup container) throws Resources.NotFoundException {
        if (this.mTrimmerManager == null) {
            MultiImportTrimmerHolder multiImportTrimmerHolder = new MultiImportTrimmerHolder(container, this.mActivity);
            this.mTrimmerManager = new MultiImportTrimmerManager(multiImportTrimmerHolder);
            this.mTrimmerManager.bind(multiImportTrimmerHolder);
        }
    }

    public void onPause() {
        this.mAdapter.release();
    }
}
