package co.vine.android.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.widget.Button;
import co.vine.android.R;

/* loaded from: classes.dex */
public class FollowStateButton extends Button {
    private int mColor;
    private int mFollowRequestedDrawableRes;
    private int mFollowRequestedText;
    private int mFollowRequestedTextColor;
    private int mFollowingDrawableRes;
    private int mFollowingText;
    private int mFollowingTextColor;
    private int mNotFollowingDrawableRes;
    private int mNotFollowingText;
    private int mNotFollowingTextColor;
    private int mSelfDrawableRes;
    private int mSelfText;
    private int mSelfTextColor;
    private int mState;

    public FollowStateButton(Context context) {
        this(context, null);
    }

    public FollowStateButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FollowStateButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FollowStateButton, defStyle, 0);
        this.mSelfDrawableRes = a.getResourceId(0, R.drawable.btn_profile_settings);
        this.mNotFollowingDrawableRes = a.getResourceId(3, R.drawable.btn_profile_follow);
        this.mFollowingDrawableRes = a.getResourceId(6, R.drawable.btn_follow);
        this.mFollowRequestedDrawableRes = a.getResourceId(9, R.drawable.btn_profile_pending);
        this.mSelfText = a.getResourceId(1, R.string.settings);
        this.mNotFollowingText = a.getResourceId(4, R.string.follow);
        this.mFollowingText = a.getResourceId(7, R.string.following);
        this.mFollowRequestedText = a.getResourceId(10, R.string.follow_requested);
        Resources res = getResources();
        this.mSelfTextColor = a.getColor(2, res.getColor(R.color.settings_btn_text));
        this.mNotFollowingTextColor = a.getColor(5, res.getColor(R.color.not_following_btn_text));
        this.mFollowingTextColor = a.getColor(5, res.getColor(R.color.following_btn_text));
        this.mFollowRequestedTextColor = a.getColor(11, res.getColor(R.color.not_following_btn_text));
        this.mColor = res.getColor(R.color.vine_green);
        a.recycle();
        setPadding(getResources().getDimensionPixelSize(R.dimen.follow_request_left_padding), 0, 0, 0);
        invalidate();
    }

    private void setColors() {
        int color = this.mColor;
        this.mFollowingTextColor = -1;
        this.mNotFollowingTextColor = color;
        this.mFollowRequestedTextColor = color;
        if (getBackground() != null && this.mState != 1) {
            getBackground().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        }
    }

    public int getState() {
        return this.mState;
    }

    private void setState(int state) {
        switch (state) {
            case 1:
                setBackgroundResource(this.mSelfDrawableRes);
                setText(this.mSelfText);
                setTextColor(this.mSelfTextColor);
                this.mState = 1;
                break;
            case 2:
                setBackgroundResource(this.mNotFollowingDrawableRes);
                setText(this.mNotFollowingText);
                setTextColor(this.mNotFollowingTextColor);
                this.mState = 2;
                break;
            case 3:
                setBackgroundResource(this.mFollowingDrawableRes);
                setText(this.mFollowingText);
                setTextColor(this.mFollowingTextColor);
                this.mState = 3;
                break;
            case 4:
                setBackgroundResource(this.mFollowRequestedDrawableRes);
                setText(this.mFollowRequestedText);
                setTextColor(this.mFollowRequestedTextColor);
                break;
        }
        setColors();
    }
}
