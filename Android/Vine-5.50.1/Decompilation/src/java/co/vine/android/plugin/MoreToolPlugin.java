package co.vine.android.plugin;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import co.vine.android.CameraOnboardHelper;
import co.vine.android.R;
import co.vine.android.recorder.VineRecorder;
import co.vine.android.widget.ToolDrawerLinearLayout;
import co.vine.android.widget.TooltipView;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public class MoreToolPlugin extends RecorderPluginSubManager<LinearLayout, VineRecorder> implements View.OnClickListener, TooltipView.Listener {
    protected Activity mActivity;
    private ImageButton mButton;
    private ToolDrawerLinearLayout mDrawer;
    private Runnable mDrawerClosedFinishedRunnable;
    private Fragment mFragment;
    private View mNib;
    private View.OnClickListener mOnClickListener;
    private ViewGroup mParent;
    private final Runnable mShowToolTipRunnable;
    private TooltipView mTooltip;
    private int mViewSpec;
    private boolean mWasToolTipHidden;

    public static MoreToolPlugin newInstance() {
        ArrayList<RecorderPlugin<?, VineRecorder>> plugins = new ArrayList<>();
        plugins.add(new GridPlugin());
        plugins.add(new FocusPlugin());
        plugins.add(new GhostPlugin());
        plugins.add(new FlashPlugin());
        return new MoreToolPlugin(plugins);
    }

    private MoreToolPlugin(ArrayList<RecorderPlugin<?, VineRecorder>> plugins) {
        super("More tools", plugins);
        this.mDrawerClosedFinishedRunnable = new Runnable() { // from class: co.vine.android.plugin.MoreToolPlugin.1
            @Override // java.lang.Runnable
            public void run() {
                MoreToolPlugin.this.mNib.setVisibility(8);
                MoreToolPlugin.this.mDrawer.setVisibility(8);
            }
        };
        this.mShowToolTipRunnable = new Runnable() { // from class: co.vine.android.plugin.MoreToolPlugin.2
            @Override // java.lang.Runnable
            public void run() {
                if (MoreToolPlugin.this.mParent != null && MoreToolPlugin.this.mTooltip != null) {
                    MoreToolPlugin.this.mWasToolTipHidden = false;
                    MoreToolPlugin.this.mTooltip.updatePosition(MoreToolPlugin.this.mButton);
                    MoreToolPlugin.this.mTooltip.show();
                }
            }
        };
    }

    @Override // co.vine.android.plugin.RecorderPluginSubManager
    public LinearLayout onLayout(ViewGroup parent, LayoutInflater inflater, Fragment fragment) {
        LinearLayout view = (LinearLayout) inflater.inflate(R.layout.plugin_base_tool, parent, false);
        view.setOnClickListener(this);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.plugin_option_image_button_with_nib, parent, false);
        this.mNib = layout.findViewById(R.id.nib);
        ImageButton button = (ImageButton) layout.findViewById(R.id.option_image_button);
        button.setOnClickListener(this);
        button.setImageResource(R.drawable.ic_tools);
        button.setAlpha(0.35f);
        view.addView(layout);
        ViewGroup viewGroup = (ViewGroup) fragment.getView().findViewById(R.id.recording_sub_options_bar);
        Iterator it = this.mPlugins.iterator();
        while (it.hasNext()) {
            RecorderPlugin<?, VineRecorder> plugin = (RecorderPlugin) it.next();
            plugin.createLayout(viewGroup, inflater, fragment);
        }
        this.mDrawer = (ToolDrawerLinearLayout) fragment.getView().findViewById(R.id.recording_sub_options);
        this.mButton = button;
        this.mFragment = fragment;
        this.mParent = parent;
        return view;
    }

    @Override // co.vine.android.plugin.BaseRecorderPluginManager, co.vine.android.plugin.RecorderPlugin
    public boolean onSetEditMode(boolean editing, boolean hasData) {
        closeDrawer();
        if (editing) {
            if (hideToolTip()) {
                this.mWasToolTipHidden = true;
                return false;
            }
            return false;
        }
        if (this.mWasToolTipHidden) {
            showToolTip();
            return false;
        }
        return false;
    }

    @Override // co.vine.android.plugin.BasePluginManager, co.vine.android.plugin.Plugin
    public void onActivityCreated(Activity activity) {
        this.mActivity = activity;
        super.onActivityCreated(activity);
    }

    @Override // co.vine.android.plugin.BasePluginManager, co.vine.android.plugin.Plugin
    public void onResume(Activity activity) {
        this.mActivity = activity;
        super.onResume(activity);
    }

    @Override // co.vine.android.plugin.BasePluginManager, co.vine.android.plugin.Plugin
    public void onPause() {
        this.mActivity = null;
        super.onPause();
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View v) {
        if (this.mOnClickListener != null) {
            this.mOnClickListener.onClick(v);
        }
        if (this.mActivity != null) {
            if (this.mViewSpec == 0) {
                ViewGroup parent = (ViewGroup) this.mDrawer.getParent();
                this.mViewSpec = View.MeasureSpec.makeMeasureSpec(parent.getMeasuredHeight() - parent.findViewById(R.id.recording_options).getMeasuredHeight(), 0);
                int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, 0);
                DisplayMetrics metrics = new DisplayMetrics();
                this.mActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
                int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(metrics.widthPixels, 1073741824);
                this.mDrawer.measure(widthMeasureSpec, heightMeasureSpec);
                this.mViewSpec = this.mDrawer.getMeasuredHeight();
            }
            if (!this.mDrawer.isOpen()) {
                this.mButton.setAlpha(1.0f);
                this.mDrawer.setVisibility(0);
                this.mNib.setVisibility(0);
                this.mDrawer.toggle(null);
            } else {
                this.mButton.setAlpha(0.35f);
                this.mDrawer.toggle(this.mDrawerClosedFinishedRunnable);
            }
        }
        if (hideToolTip()) {
            CameraOnboardHelper.setLastCompletedStep(this.mActivity, "more_tools");
        }
    }

    private boolean hideToolTip() {
        if (this.mTooltip == null || this.mTooltip.hasBeenHidden()) {
            return false;
        }
        this.mTooltip.hide();
        return true;
    }

    @Override // co.vine.android.plugin.BaseRecorderPluginManager, co.vine.android.plugin.RecorderPlugin
    public void onOnboardingStepFinished() {
        if (this.mActivity != null) {
            String lastStep = CameraOnboardHelper.getLastCompletedStep(this.mActivity);
            boolean shouldOnboard = "delete".equals(lastStep) || "delete_2".equals(lastStep);
            if (shouldOnboard && this.mFragment.getView() != null) {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-2, -2);
                params.addRule(3, R.id.recording_options);
                if (this.mTooltip == null) {
                    TooltipView tooltip = new TooltipView(this.mActivity);
                    tooltip.setText(R.string.camera_onboarding_more_camera_tools);
                    tooltip.setListener(this);
                    this.mTooltip = tooltip;
                    RelativeLayout bottomMask = (RelativeLayout) this.mFragment.getView().findViewById(R.id.bottom_mask);
                    bottomMask.addView(this.mTooltip, params);
                }
                showToolTip();
            }
        }
    }

    private void showToolTip() {
        if (this.mParent != null) {
            this.mParent.removeCallbacks(this.mShowToolTipRunnable);
            this.mParent.postDelayed(this.mShowToolTipRunnable, 500L);
        }
    }

    @Override // co.vine.android.plugin.BaseRecorderPluginManager, co.vine.android.plugin.RecorderPlugin
    public void onShowDrafts() {
        closeDrawer();
    }

    public void closeDrawer() {
        if (this.mDrawer.isOpen()) {
            this.mButton.setAlpha(0.35f);
            this.mDrawer.toggle(this.mDrawerClosedFinishedRunnable);
        }
    }

    @Override // co.vine.android.widget.TooltipView.Listener
    public void onTooltipTapped() {
        CameraOnboardHelper.setLastCompletedStep(this.mActivity, "more_tools");
    }
}
