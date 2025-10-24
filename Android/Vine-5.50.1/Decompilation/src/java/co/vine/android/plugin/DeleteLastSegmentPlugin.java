package co.vine.android.plugin;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import co.vine.android.CameraOnboardHelper;
import co.vine.android.R;
import co.vine.android.recorder.RecordSegment;
import co.vine.android.recorder.VineRecorder;
import co.vine.android.widget.TooltipView;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public class DeleteLastSegmentPlugin extends BaseToolPlugin<ImageButton> implements View.OnClickListener, TooltipView.Listener {
    private Fragment mFragment;
    private boolean mHasData;
    private boolean mIsSelected;
    private ViewGroup mParent;
    private Rect mRect;
    private TooltipView mTooltip;
    private View mTouchView;

    public DeleteLastSegmentPlugin() {
        super("Delete Last Segment");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // co.vine.android.plugin.BaseToolPlugin
    public ImageButton onLayoutInflated(LinearLayout parent, LayoutInflater inflater, Fragment fragment) {
        this.mFragment = fragment;
        this.mParent = parent;
        ImageButton button = (ImageButton) inflater.inflate(R.layout.plugin_option_image_button, (ViewGroup) parent, false);
        button.setOnClickListener(this);
        button.setImageResource(R.drawable.ic_backspace_default);
        button.setAlpha(0.35f);
        return button;
    }

    @Override // co.vine.android.plugin.BaseRecorderPlugin, co.vine.android.plugin.BasePlugin, co.vine.android.plugin.Plugin
    public void onResume(Activity activity) {
        super.onResume(activity);
        invalidateButtonState();
    }

    @Override // co.vine.android.plugin.BaseRecorderPlugin, co.vine.android.plugin.RecorderPlugin
    public void onOnboardingStepFinished() {
        super.onOnboardingStepFinished();
        if (this.mTooltip != null) {
            this.mTooltip.hide();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showOnboardTooltip(int stringResId) {
        if (this.mFragment.getView() != null) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-2, -2);
            params.addRule(3, R.id.recording_options);
            final TooltipView tooltip = new TooltipView(this.mActivity);
            tooltip.setText(stringResId);
            tooltip.setListener(this);
            RelativeLayout bottomMask = (RelativeLayout) this.mFragment.getView().findViewById(R.id.bottom_mask);
            bottomMask.addView(tooltip, params);
            bottomMask.post(new Runnable() { // from class: co.vine.android.plugin.DeleteLastSegmentPlugin.1
                @Override // java.lang.Runnable
                public void run() {
                    if (DeleteLastSegmentPlugin.this.mParent != null) {
                        tooltip.updatePosition(DeleteLastSegmentPlugin.this.mParent);
                        ViewGroup.MarginLayoutParams params2 = (ViewGroup.MarginLayoutParams) tooltip.getLayoutParams();
                        params2.topMargin = tooltip.getResources().getDimensionPixelOffset(R.dimen.tooltipNegativeMargin);
                        tooltip.setLayoutParams(params2);
                        tooltip.show();
                    }
                }
            });
            this.mTooltip = tooltip;
        }
    }

    private void invalidateButtonState() {
        Handler handler = getHandler();
        if (handler != null) {
            handler.post(new Runnable() { // from class: co.vine.android.plugin.DeleteLastSegmentPlugin.2
                @Override // java.lang.Runnable
                public void run() {
                    float f;
                    ImageButton view = DeleteLastSegmentPlugin.this.getInflatedChild();
                    if (view != null) {
                        if (DeleteLastSegmentPlugin.this.mIsSelected) {
                            f = 1.0f;
                        } else {
                            f = DeleteLastSegmentPlugin.this.mHasData ? 0.35f : 0.175f;
                        }
                        view.setAlpha(f);
                    }
                }
            });
        }
    }

    @Override // co.vine.android.plugin.BaseRecorderPlugin, co.vine.android.plugin.RecorderPlugin
    public boolean onBackButtonPressed(boolean isEditing) {
        return this.mIsSelected;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View v) {
        final VineRecorder r = getRecorder();
        Activity activity = this.mActivity;
        if (activity != null && r != null) {
            final ViewGroup rootView = (ViewGroup) activity.findViewById(R.id.thumbnail_list);
            ArrayList<RecordSegment> segments = r.getEditedSegments();
            int segmentSize = segments.size();
            if (segmentSize > 0 && !this.mIsSelected) {
                int lastIndex = 0;
                for (int i = 0; i < segmentSize; i++) {
                    if (!segments.get(i).removed) {
                        lastIndex = i;
                    }
                }
                if (r.highlightSegment(lastIndex, activity.getResources().getColor(R.color.plugin_delete_selected_segment))) {
                    this.mIsSelected = true;
                    if (this.mTouchView == null) {
                        this.mTouchView = new View(this.mActivity);
                        this.mTouchView.setAlpha(0.0f);
                        this.mTouchView.setOnTouchListener(new View.OnTouchListener() { // from class: co.vine.android.plugin.DeleteLastSegmentPlugin.3
                            @Override // android.view.View.OnTouchListener
                            public boolean onTouch(View v2, MotionEvent event) throws IllegalStateException, InterruptedException {
                                if (DeleteLastSegmentPlugin.this.mRect != null && event.getAction() == 1) {
                                    float x = event.getX();
                                    float y = event.getY();
                                    if (x <= DeleteLastSegmentPlugin.this.mRect.left || x >= DeleteLastSegmentPlugin.this.mRect.right || y <= DeleteLastSegmentPlugin.this.mRect.top || y >= DeleteLastSegmentPlugin.this.mRect.bottom) {
                                        if (DeleteLastSegmentPlugin.this.mTooltip != null) {
                                            DeleteLastSegmentPlugin.this.mTooltip.hide();
                                            CameraOnboardHelper.setLastCompletedStep(DeleteLastSegmentPlugin.this.mActivity, "delete");
                                        }
                                    } else {
                                        r.deleteLastSegment();
                                        String lastStep = CameraOnboardHelper.getLastCompletedStep(DeleteLastSegmentPlugin.this.mActivity);
                                        if ("delete".equals(lastStep)) {
                                            CameraOnboardHelper.setLastCompletedStep(DeleteLastSegmentPlugin.this.mActivity, "delete_2");
                                        }
                                        if (DeleteLastSegmentPlugin.this.mManager instanceof BaseRecorderPluginManager) {
                                            ((BaseRecorderPluginManager) DeleteLastSegmentPlugin.this.mManager).onOnboardingStepFinished();
                                        }
                                    }
                                    DeleteLastSegmentPlugin.this.mIsSelected = false;
                                    r.unhighlightSegments();
                                    DeleteLastSegmentPlugin.this.invalidateView();
                                    if (DeleteLastSegmentPlugin.this.mTouchView != null) {
                                        rootView.removeView(DeleteLastSegmentPlugin.this.mTouchView);
                                    }
                                }
                                return true;
                            }
                        });
                    }
                    rootView.addView(this.mTouchView, new ViewGroup.LayoutParams(-1, -1));
                    invalidateView();
                    if (this.mTooltip != null) {
                        this.mTooltip.hide();
                        String lastStep = CameraOnboardHelper.getLastCompletedStep(this.mActivity);
                        if ("delete".equals(lastStep)) {
                            showOnboardTooltip(R.string.camera_onboarding_undo_step_two);
                        }
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void invalidateView() {
        ImageButton view = getInflatedChild();
        if (view != null) {
            view.setImageResource(this.mIsSelected ? R.drawable.ic_backspace_pressed : R.drawable.ic_backspace_default);
            invalidateButtonState();
            int[] loc = new int[2];
            ((View) view.getParent()).getLocationInWindow(loc);
            this.mRect = new Rect(loc[0], loc[1], loc[0] + view.getMeasuredWidth(), loc[1] + view.getMeasuredHeight());
        }
    }

    @Override // co.vine.android.plugin.BaseRecorderPlugin, co.vine.android.plugin.RecorderPlugin
    public boolean onSetEditMode(boolean editing, boolean hasData) {
        if (this.mTooltip != null) {
            this.mTooltip.hide();
            return false;
        }
        return false;
    }

    @Override // co.vine.android.plugin.BaseRecorderPlugin, co.vine.android.plugin.RecorderPlugin
    public void onSegmentDataChanged(ArrayList<RecordSegment> editedSegments) {
        this.mHasData = false;
        Iterator<RecordSegment> it = editedSegments.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            RecordSegment segment = it.next();
            if (!segment.removed) {
                this.mHasData = true;
                break;
            }
        }
        invalidateButtonState();
        boolean shouldOnboard = "grab".equals(CameraOnboardHelper.getLastCompletedStep(this.mActivity)) && editedSegments.size() > 0;
        if (shouldOnboard && this.mActivity != null) {
            CameraOnboardHelper.setLastCompletedStep(this.mActivity, "delete");
            this.mActivity.runOnUiThread(new Runnable() { // from class: co.vine.android.plugin.DeleteLastSegmentPlugin.4
                @Override // java.lang.Runnable
                public void run() {
                    DeleteLastSegmentPlugin.this.showOnboardTooltip(R.string.camera_onboarding_undo_step_one);
                }
            });
        }
    }

    @Override // co.vine.android.widget.TooltipView.Listener
    public void onTooltipTapped() {
        String lastStep = CameraOnboardHelper.getLastCompletedStep(this.mActivity);
        CameraOnboardHelper.setLastCompletedStep(this.mActivity, "delete".equals(lastStep) ? "delete_2" : "delete");
    }
}
