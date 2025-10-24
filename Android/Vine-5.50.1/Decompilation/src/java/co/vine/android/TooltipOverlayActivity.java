package co.vine.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import co.vine.android.util.BuildUtil;
import co.vine.android.util.ViewUtil;
import co.vine.android.widget.TooltipOverlayView;

/* loaded from: classes.dex */
public final class TooltipOverlayActivity extends AppCompatActivity {
    private TooltipOverlayView mTooltipOverlayView;

    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityDonut, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        int anchorViewTopAdjusted;
        super.onCreate(savedInstanceState);
        if (getIntent() == null || getIntent().getExtras() == null) {
            finish();
            return;
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        Bundle extras = getIntent().getExtras();
        int[] anchorViewLoc = extras.getIntArray("anchor_view_loc");
        int anchorViewWidth = extras.getInt("anchor_view_width");
        int anchorViewHeight = extras.getInt("anchor_view_height");
        String message = extras.getString("message");
        int actionDrawableResId = extras.getInt("drawable_res_id");
        if (BuildUtil.isApi21Lollipop()) {
            anchorViewTopAdjusted = anchorViewLoc[1];
        } else {
            anchorViewTopAdjusted = anchorViewLoc[1] - ViewUtil.getStatusBarHeightPx(getResources());
        }
        TooltipOverlayView view = new TooltipOverlayView(this, message, new TooltipOverlayView.Listener() { // from class: co.vine.android.TooltipOverlayActivity.1
            @Override // co.vine.android.widget.TooltipOverlayView.Listener
            public void onDismissed() {
                TooltipOverlayActivity.this.finish();
            }
        });
        setContentView(view, new ViewGroup.LayoutParams(-1, -1));
        if (actionDrawableResId > 0) {
            ImageView action = new ImageView(this);
            action.setScaleType(ImageView.ScaleType.CENTER);
            action.setImageDrawable(getResources().getDrawable(actionDrawableResId));
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(anchorViewWidth, anchorViewHeight);
            params.setMargins(anchorViewLoc[0], anchorViewTopAdjusted, 0, 0);
            view.addView(action, params);
            action.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.TooltipOverlayActivity.2
                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    TooltipOverlayActivity.this.setResult(-1);
                    TooltipOverlayActivity.this.mTooltipOverlayView.dismiss();
                }
            });
        }
        view.updatePosition(anchorViewLoc[0], anchorViewWidth, anchorViewHeight + anchorViewTopAdjusted);
        view.startShowAnimation();
        this.mTooltipOverlayView = view;
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onBackPressed() {
        if (this.mTooltipOverlayView.getAnimation() == null) {
            this.mTooltipOverlayView.dismiss();
        } else {
            super.onBackPressed();
        }
    }

    public static void start(Activity context, View anchorView, String message, int drawableResId, int requestId) {
        if (anchorView != null && !TextUtils.isEmpty(message)) {
            int[] anchorViewLoc = new int[2];
            anchorView.getLocationOnScreen(anchorViewLoc);
            Intent intent = new Intent(context, (Class<?>) TooltipOverlayActivity.class);
            intent.putExtra("anchor_view_loc", anchorViewLoc);
            intent.putExtra("anchor_view_width", anchorView.getMeasuredWidth());
            intent.putExtra("anchor_view_height", anchorView.getMeasuredHeight());
            intent.putExtra("message", message);
            if (drawableResId > 0) {
                intent.putExtra("drawable_res_id", drawableResId);
            }
            context.startActivityForResult(intent, requestId);
        }
    }
}
