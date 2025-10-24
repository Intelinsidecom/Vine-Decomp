package co.vine.android.widget.notifier;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import co.vine.android.R;
import co.vine.android.recorder.ViewGoneAnimationListener;
import co.vine.android.util.ViewUtil;

/* loaded from: classes.dex */
public class NotifyableRelativeLayout extends RelativeLayout {
    private NotifierClickListener mListener;
    private int mNotificationMargin;
    private int mVineGreen;

    public interface NotifierClickListener {
        void onNotifyClick();
    }

    public NotifyableRelativeLayout(Context context) throws Resources.NotFoundException {
        super(context);
        init(context);
    }

    public NotifyableRelativeLayout(Context context, AttributeSet attrs) throws Resources.NotFoundException {
        super(context, attrs);
        init(context);
    }

    public NotifyableRelativeLayout(Context context, AttributeSet attrs, int defStyle) throws Resources.NotFoundException {
        super(context, attrs, defStyle);
        init(context);
    }

    public void init(Context context) throws Resources.NotFoundException {
        Resources res = context.getResources();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService("layout_inflater");
        View v = inflater.inflate(R.layout.floating_notification, (ViewGroup) null);
        TextView t = (TextView) v.findViewById(R.id.floating_view);
        Drawable d = res.getDrawable(R.drawable.vm_notification);
        this.mVineGreen = res.getColor(R.color.vine_green);
        d.setColorFilter(this.mVineGreen, PorterDuff.Mode.SRC_ATOP);
        ViewUtil.setBackground(t, d);
        addView(v);
        this.mNotificationMargin = res.getDimensionPixelOffset(R.dimen.tabbar_height) + res.getDimensionPixelOffset(R.dimen.notification_margin);
        t.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.widget.notifier.NotifyableRelativeLayout.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v2) {
                if (NotifyableRelativeLayout.this.mListener != null) {
                    NotifyableRelativeLayout.this.mListener.onNotifyClick();
                }
                NotifyableRelativeLayout.this.hideNotification();
            }
        });
    }

    @Override // android.widget.RelativeLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getId() == R.id.floating_notification) {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) child.getLayoutParams();
                params.addRule(10);
                params.topMargin = this.mNotificationMargin;
                bringChildToFront(child);
            }
        }
        super.onLayout(changed, l, t, r, b);
    }

    public void setNotifierListener(NotifierClickListener listener) {
        this.mListener = listener;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideNotification() {
        View v = findViewById(R.id.floating_view);
        v.animate().alpha(0.0f).setDuration(1000L).setListener(new ViewGoneAnimationListener(v));
        v.setVisibility(8);
    }
}
