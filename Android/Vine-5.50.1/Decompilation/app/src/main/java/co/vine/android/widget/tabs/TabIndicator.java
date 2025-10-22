package co.vine.android.widget.tabs;

import android.R;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

/* loaded from: classes.dex */
public class TabIndicator extends RelativeLayout {
    private ImageView mIcon;
    private int mIconNewResId;
    private int mIconResId;
    private boolean mNew;
    private ImageView mNewIndicator;
    private View mStripIndicator;
    private TextView mTabText;

    public TabIndicator(Context context) {
        super(context);
    }

    public static ColorStateList createTextColorList(int on, int off) {
        return new ColorStateList(new int[][]{new int[]{R.attr.state_selected}, new int[]{R.attr.state_pressed}, new int[0]}, new int[]{on, on, off});
    }

    public TabIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TabIndicator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public static TabIndicator newTextIndicator(LayoutInflater inflater, int layout, TabHost tabHost, int tabTextId, boolean caps) throws Resources.NotFoundException {
        TabIndicator indicator = (TabIndicator) inflater.inflate(layout, (ViewGroup) tabHost.getTabWidget(), false);
        if (tabTextId > 0) {
            indicator.setTabText(tabTextId, caps);
        }
        return indicator;
    }

    public static TabIndicator newIconIndicator(LayoutInflater inflater, int layout, TabHost tabHost, int iconResId, int newResId) {
        TabIndicator indicator = (TabIndicator) inflater.inflate(layout, (ViewGroup) tabHost.getTabWidget(), false);
        if (iconResId > 0) {
            indicator.setTabIcon(iconResId, newResId);
        }
        return indicator;
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mTabText = (TextView) findViewById(co.vine.android.R.id.tab_text);
        this.mNewIndicator = (ImageView) findViewById(co.vine.android.R.id.new_indicator);
        this.mIcon = (ImageView) findViewById(co.vine.android.R.id.icon);
    }

    public ImageView getNewIndicator() {
        return this.mNewIndicator;
    }

    public void setNew(boolean isNew) {
        if (isNew != this.mNew) {
            if (this.mNewIndicator != null) {
                if (isNew) {
                    this.mNewIndicator.setVisibility(0);
                } else {
                    this.mNewIndicator.setVisibility(8);
                }
            } else if (this.mIconNewResId > 0) {
                if (isNew) {
                    this.mIcon.setImageResource(this.mIconNewResId);
                } else {
                    this.mIcon.setImageResource(this.mIconResId);
                }
            }
            this.mNew = isNew;
        }
    }

    public void setColor(int color) {
        this.mIcon.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        this.mIcon.invalidate();
    }

    public void setTextColor(int color) {
        this.mTabText.setTextColor(color);
        this.mTabText.invalidate();
    }

    public void setTabText(int resId, boolean caps) throws Resources.NotFoundException {
        if (this.mTabText != null) {
            String text = getResources().getString(resId);
            if (caps) {
                text = text.toUpperCase();
            }
            if (resId > 0) {
                this.mTabText.setVisibility(0);
                this.mTabText.setText(text);
            } else {
                this.mTabText.setVisibility(8);
            }
        }
    }

    public void setTabIcon(int resId, int newResId) {
        if (this.mIcon != null) {
            if (resId > 0) {
                this.mIcon.setImageResource(resId);
                this.mIcon.setVisibility(0);
            } else {
                this.mIcon.setVisibility(8);
            }
        }
        this.mIconResId = resId;
        this.mIconNewResId = newResId;
    }

    public void setStripVisible(int color) {
        if (this.mStripIndicator != null) {
            this.mStripIndicator.setBackgroundColor(color);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (this.mTabText != null) {
        }
    }

    public TextView getIndicatorText() {
        return this.mTabText;
    }
}
