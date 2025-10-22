package co.vine.android;

import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/* loaded from: classes.dex */
public class DraftCameraPreviewFragment extends BaseFragment implements View.OnClickListener {
    private int mDimen;
    private ImageView mIcon;
    private boolean mMasksAdjusted;
    private View mPreviewView;
    private Resources mRes;
    private final View.OnTouchListener onMaskTouchListener = new View.OnTouchListener() { // from class: co.vine.android.DraftCameraPreviewFragment.1
        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View v, MotionEvent event) {
            return true;
        }
    };

    public void setDimen(int dimen) {
        this.mDimen = dimen;
    }

    @Override // android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mRes = getResources();
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup root, Bundle savedInstanceState) throws Resources.NotFoundException {
        View rootView = inflater.inflate(R.layout.draft_camera, root, false);
        if (rootView == null) {
            return null;
        }
        int draftProgressDimen = this.mRes.getDimensionPixelOffset(R.dimen.draft_progress_view_height);
        int progressDimen = this.mRes.getDimensionPixelOffset(R.dimen.progress_view_height);
        RelativeLayout container = (RelativeLayout) rootView.findViewById(R.id.preview_container);
        container.setOnClickListener(this);
        RelativeLayout icon = (RelativeLayout) container.findViewById(R.id.icon_container);
        RelativeLayout.LayoutParams iconParams = (RelativeLayout.LayoutParams) icon.getLayoutParams();
        iconParams.width = this.mDimen;
        iconParams.height = this.mDimen;
        iconParams.topMargin = (draftProgressDimen * 2) + progressDimen;
        icon.setLayoutParams(iconParams);
        this.mPreviewView = icon;
        this.mIcon = (ImageView) icon.findViewById(R.id.preview_icon);
        Point screenSize = ((AbstractRecordingActivity) getActivity()).getScreenSize();
        int dotsHeight = this.mRes.getDimensionPixelOffset(R.dimen.draft_dots_height);
        int actualHeight = screenSize.x + progressDimen + dotsHeight;
        initMasks(rootView, screenSize.x, actualHeight);
        return rootView;
    }

    public synchronized void initMasks(View rootView, int actualWidth, int actualHeight) {
        if (!this.mMasksAdjusted) {
            this.mMasksAdjusted = true;
            View topMask = rootView.findViewById(R.id.draft_camera_top_mask);
            View bottomMask = rootView.findViewById(R.id.draft_camera_bottom_mask);
            topMask.setOnTouchListener(this.onMaskTouchListener);
            bottomMask.setOnTouchListener(this.onMaskTouchListener);
            int desiredHeight = this.mDimen;
            if (actualHeight > desiredHeight) {
                int draftProgress = this.mRes.getDimensionPixelOffset(R.dimen.draft_progress_view_height);
                int progress = this.mRes.getDimensionPixelOffset(R.dimen.progress_view_height);
                int topHeight = (draftProgress * 3) + progress;
                int bottomHeight = (actualHeight - topHeight) - this.mDimen;
                if (bottomHeight > 0) {
                    ViewGroup.LayoutParams topMaskParams = topMask.getLayoutParams();
                    ViewGroup.LayoutParams bottomMaskParams = bottomMask.getLayoutParams();
                    topMaskParams.height = topHeight;
                    bottomMaskParams.height = bottomHeight;
                    topMask.setLayoutParams(topMaskParams);
                    bottomMask.setLayoutParams(bottomMaskParams);
                }
            }
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View v) throws Resources.NotFoundException {
        if (v.getId() == R.id.preview_container) {
            AbstractRecordingActivity activity = (AbstractRecordingActivity) getActivity();
            activity.cameraPreviewToRecorder(this.mPreviewView, this.mIcon);
        }
    }
}
