package co.vine.android.recordingui;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import co.vine.android.R;
import co.vine.android.RecordingNavigationController;
import co.vine.android.embed.player.VideoViewInterface;
import co.vine.android.player.SdkVideoView;
import co.vine.android.recorder.ProgressView;
import co.vine.android.recorder2.RecordController;
import co.vine.android.recorder2.model.Draft;
import co.vine.android.recorder2.model.DraftsManager;
import co.vine.android.widget.DotIndicators;
import java.io.IOException;
import java.util.HashMap;

/* loaded from: classes.dex */
public class DraftsMode extends PagerAdapter implements ViewPager.OnPageChangeListener {
    private final Activity mActivity;
    private DraftViewHolder mCurrentHolder;
    private final DotIndicators mDots;
    private final int mDraftMargin;
    private boolean mFull;
    private final RecordController mRecordController;
    private final RecordingNavigationController mRecordingNavigationController;
    private final View mRootView;
    private final int mVideoSize;
    private HashMap<Integer, DraftViewHolder> mViewHolders;
    private final ViewPager mViewPager;
    private final Point mWindowSize;

    public DraftsMode(Activity activity, RecordController controller, RecordingNavigationController navigationController) {
        this.mActivity = activity;
        this.mRecordController = controller;
        this.mRecordingNavigationController = navigationController;
        this.mRootView = activity.findViewById(R.id.drafts);
        this.mViewPager = (ViewPager) activity.findViewById(R.id.drafts_pager);
        this.mViewPager.setClipToPadding(false);
        this.mViewPager.setOnPageChangeListener(this);
        this.mDots = (DotIndicators) activity.findViewById(R.id.dots);
        this.mViewHolders = new HashMap<>();
        Resources res = activity.getResources();
        this.mVideoSize = res.getDimensionPixelSize(R.dimen.draft_video_size);
        this.mDraftMargin = res.getDimensionPixelSize(R.dimen.draft_margin);
        this.mWindowSize = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(this.mWindowSize);
        this.mFull = false;
    }

    public void show(Draft draft) throws Resources.NotFoundException {
        int draftCount;
        try {
            DraftsManager.loadDrafts(this.mActivity);
        } catch (IOException e) {
        } catch (ClassNotFoundException e2) {
        }
        this.mViewPager.setAdapter(this);
        this.mViewPager.setOffscreenPageLimit(DraftsManager.getDraftsCount() + 1);
        int draftIndex = -1;
        if (draft != null && (draftCount = DraftsManager.getDraftsCount()) > 0) {
            int i = 0;
            while (true) {
                if (i >= draftCount) {
                    break;
                }
                if (!DraftsManager.getDraftAtPosition(i).equals(draft)) {
                    i++;
                } else {
                    draftIndex = i;
                    break;
                }
            }
        }
        if (draftIndex == -1) {
            int lastDraft = DraftsManager.getDraftsCount() - 1;
            draftIndex = Math.max(0, lastDraft);
        }
        this.mViewPager.setCurrentItem(draftIndex);
        this.mDots.setNumberOfDots(DraftsManager.getDraftsCount());
        this.mDots.setActiveDot(draftIndex);
        this.mDots.invalidate();
        this.mRootView.setVisibility(0);
    }

    public void hide() throws IllegalStateException {
        if (this.mCurrentHolder != null) {
            this.mCurrentHolder.videoView.stopPlayback();
        }
        this.mRootView.setVisibility(8);
    }

    public void onBackPressed() {
        Draft draft;
        int currentPosition = this.mViewPager.getCurrentItem();
        int draftCount = DraftsManager.getDraftsCount();
        if (currentPosition < draftCount) {
            draft = DraftsManager.getDraftAtPosition(currentPosition);
        } else {
            draft = DraftsManager.getDraftAtPosition(draftCount - 1);
        }
        this.mRecordingNavigationController.goToCaptureFromDrafts(draft);
    }

    @Override // android.support.v4.view.PagerAdapter
    public int getCount() {
        int draftCount = DraftsManager.getDraftsCount();
        this.mFull = draftCount >= 9;
        return draftCount + 1;
    }

    @Override // android.support.v4.view.PagerAdapter
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override // android.support.v4.view.PagerAdapter
    public Object instantiateItem(ViewGroup collection, int position) {
        return position == getCount() + (-1) ? inflateCameraPage(collection) : inflateDraftPage(collection, position);
    }

    @Override // android.support.v4.view.PagerAdapter
    public void destroyItem(ViewGroup pager, int position, Object o) {
        pager.removeView((View) o);
    }

    private Object inflateDraftPage(ViewGroup collection, int position) throws IllegalStateException {
        View draftViewContainer = LayoutInflater.from(this.mActivity).inflate(R.layout.drafts_viewpager_page, (ViewGroup) null);
        final Draft draft = DraftsManager.getDraftAtPosition(position);
        final DraftViewHolder viewHolder = new DraftViewHolder(draftViewContainer, draft);
        viewHolder.videoView.setLooping(true);
        viewHolder.videoView.setAutoPlayOnPrepared(false);
        viewHolder.thumbnail.setVisibility(0);
        viewHolder.videoView.setVisibility(8);
        viewHolder.thumbnail.setImageURI(Uri.parse(draft.getThumbnailPath()));
        float ratio = this.mRecordController.getProgressRatioFromDuration(draft.getDuration());
        viewHolder.progressView.setProgressRatio(ratio);
        viewHolder.trash.setAlpha(0.5f);
        viewHolder.trash.setOnTouchListener(new View.OnTouchListener() { // from class: co.vine.android.recordingui.DraftsMode.1
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case 0:
                        view.setAlpha(1.0f);
                        return true;
                    case 1:
                        if (!new Rect(0, 0, view.getWidth(), view.getHeight()).contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
                            return true;
                        }
                        try {
                            viewHolder.draft.delete();
                            DraftsManager.loadDrafts(DraftsMode.this.mActivity);
                            DraftsMode.this.mDots.setNumberOfDots(DraftsManager.getDraftsCount());
                            DraftsMode.this.mDots.invalidate();
                            DraftsMode.this.notifyDataSetChanged();
                            return true;
                        } catch (IOException e) {
                            return true;
                        } catch (ClassNotFoundException e2) {
                            return true;
                        }
                    case 2:
                        if (new Rect(0, 0, view.getWidth(), view.getHeight()).contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
                            view.setAlpha(1.0f);
                            return true;
                        }
                        view.setAlpha(0.5f);
                        return true;
                    default:
                        return false;
                }
            }
        });
        draftViewContainer.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.recordingui.DraftsMode.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                DraftsMode.this.mRecordingNavigationController.goToCaptureFromDrafts(draft);
            }
        });
        collection.addView(draftViewContainer, 0);
        this.mViewHolders.put(Integer.valueOf(position), viewHolder);
        if (position == this.mViewPager.getCurrentItem()) {
            setupAndPlayDraftPreview(viewHolder);
        }
        return draftViewContainer;
    }

    @Override // android.support.v4.view.PagerAdapter
    public int getItemPosition(Object object) {
        return -2;
    }

    @Override // android.support.v4.view.PagerAdapter
    public float getPageWidth(int position) {
        if (getCount() == 1) {
            return 1.0f;
        }
        return (this.mVideoSize + (this.mDraftMargin * 2)) / this.mWindowSize.x;
    }

    private Object inflateCameraPage(ViewGroup collection) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(this.mActivity);
        if (!this.mFull) {
            view = inflater.inflate(R.layout.draft_viewpager_camera_page, (ViewGroup) null);
            view.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.recordingui.DraftsMode.3
                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    if (!DraftsMode.this.mFull) {
                        DraftsMode.this.mRecordingNavigationController.goToCaptureFromDrafts(null);
                    }
                }
            });
        } else {
            view = inflater.inflate(R.layout.draft_viewpager_full_page, (ViewGroup) null);
        }
        collection.addView(view, 0);
        return view;
    }

    @Override // android.support.v4.view.ViewPager.OnPageChangeListener
    public void onPageScrolled(int i, float v, int i2) {
    }

    @Override // android.support.v4.view.ViewPager.OnPageChangeListener
    public void onPageSelected(int pageIndex) throws IllegalStateException {
        if (this.mCurrentHolder != null) {
            this.mCurrentHolder.thumbnail.setVisibility(0);
            this.mCurrentHolder.videoView.stopPlayback();
            this.mCurrentHolder.videoView.setVisibility(8);
        }
        DraftViewHolder holder = this.mViewHolders.get(Integer.valueOf(pageIndex));
        if (holder != null) {
            setupAndPlayDraftPreview(holder);
        }
        this.mDots.setActiveDot(pageIndex);
    }

    private void setupAndPlayDraftPreview(final DraftViewHolder holder) throws IllegalStateException {
        SdkVideoView videoView = holder.videoView;
        videoView.setVideoPath(holder.draft.getVideoPath());
        videoView.setAutoPlayOnPrepared(true);
        videoView.setOnPreparedListener(new VideoViewInterface.OnPreparedListener() { // from class: co.vine.android.recordingui.DraftsMode.4
            @Override // co.vine.android.embed.player.VideoViewInterface.OnPreparedListener
            public void onPrepared(VideoViewInterface view) {
                holder.thumbnail.setVisibility(8);
            }
        });
        videoView.setVisibility(0);
        videoView.start();
        this.mCurrentHolder = holder;
    }

    @Override // android.support.v4.view.ViewPager.OnPageChangeListener
    public void onPageScrollStateChanged(int i) {
    }

    private class DraftViewHolder {
        public Draft draft;
        public ProgressView progressView;
        public View root;
        public ImageView thumbnail;
        public View trash;
        public SdkVideoView videoView;

        public DraftViewHolder(View inRoot, Draft inDraft) {
            this.draft = inDraft;
            this.root = inRoot;
            this.thumbnail = (ImageView) inRoot.findViewById(R.id.thumb);
            this.videoView = (SdkVideoView) inRoot.findViewById(R.id.videoView);
            this.progressView = (ProgressView) inRoot.findViewById(R.id.progress);
            this.trash = inRoot.findViewById(R.id.trash);
        }
    }

    @Override // android.support.v4.view.PagerAdapter
    public void notifyDataSetChanged() {
        this.mViewHolders = new HashMap<>();
        super.notifyDataSetChanged();
    }
}
