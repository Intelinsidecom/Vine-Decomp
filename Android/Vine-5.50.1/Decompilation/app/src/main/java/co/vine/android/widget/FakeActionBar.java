package co.vine.android.widget;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import co.vine.android.R;
import co.vine.android.util.AppCompatProxy;
import co.vine.android.util.ViewUtil;

/* loaded from: classes.dex */
public class FakeActionBar implements View.OnClickListener {
    private RelativeLayout mActionBar;
    private RelativeLayout mActionBarContainer;
    private RelativeLayout mActionBarLeft;
    private LinearLayout mActionBarRight;
    private final AppCompatActivity mActivity;
    private ImageView mBackIcon;
    private View mContentView;
    private boolean mDisplayHomeAsUp;
    private boolean mDisplayLogo;
    private Boolean mDisplayShowTitle;
    private View mDistanceMarker;
    private boolean mHomeButtonEnabled;
    private ImageView mHomeIcon;
    private View mSpacer;
    private Integer mTextColor;
    public Theme mTheme = Theme.LIGHT;
    private String mTitle;
    private TextView mTitleView;

    public enum Theme {
        DARK,
        LIGHT
    }

    public void setActionBarColor(int color) {
        this.mActionBar.setBackgroundColor(color);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View v) {
        if (v == this.mBackIcon || v == this.mHomeIcon) {
            this.mActivity.finish();
        }
    }

    public RelativeLayout getActionBarLeft() {
        return this.mActionBarLeft;
    }

    public LinearLayout getActionBarRight() {
        return this.mActionBarRight;
    }

    public FakeActionBar(AppCompatActivity activity) {
        this.mActivity = activity;
    }

    public void onCreate() {
        this.mActionBarContainer = (RelativeLayout) LayoutInflater.from(this.mActivity).inflate(R.layout.fake_action_bar, (ViewGroup) null);
        this.mActionBar = (RelativeLayout) this.mActionBarContainer.findViewById(R.id.fake_action_bar);
        ViewUtil.setActionBarHeight(this.mActivity, this.mActionBar);
        this.mDistanceMarker = this.mActionBarContainer.findViewById(R.id.distance_marker);
        ViewUtil.setActionBarHeight(this.mActivity, this.mDistanceMarker);
        this.mActionBarLeft = (RelativeLayout) this.mActionBarContainer.findViewById(R.id.fake_action_bar_left);
        this.mActionBarRight = (LinearLayout) this.mActionBarContainer.findViewById(R.id.fake_action_bar_right);
        this.mBackIcon = (ImageView) this.mActionBar.findViewById(R.id.ab_back_button);
        this.mHomeIcon = (ImageView) this.mActionBar.findViewById(R.id.ab_home_button);
        this.mSpacer = this.mActionBar.findViewById(R.id.spacer);
        this.mTitleView = (TextView) this.mActionBar.findViewById(R.id.ab_title_view);
    }

    public ImageView getBackIcon() {
        return this.mBackIcon;
    }

    public void onPostCreate() throws Resources.NotFoundException {
        if (this.mContentView == null || this.mActionBarContainer == null) {
            throw new IllegalStateException("custom setContentView is not called.");
        }
        if (this.mDisplayHomeAsUp || this.mHomeButtonEnabled) {
            if (this.mDisplayHomeAsUp) {
                this.mBackIcon.setImageResource(AppCompatProxy.getBackIcon());
                this.mBackIcon.setClickable(true);
                this.mBackIcon.setVisibility(0);
                this.mBackIcon.setOnClickListener(this);
                this.mSpacer.setVisibility(8);
            } else {
                this.mBackIcon.setVisibility(8);
            }
            Drawable icon = null;
            Resources.Theme theme = this.mActivity.getTheme();
            if (theme != null) {
                TypedArray att = theme.obtainStyledAttributes(new int[]{android.R.attr.actionBarStyle});
                TypedValue typedValue = new TypedValue();
                if (att.getValue(0, typedValue)) {
                    TypedArray att2 = theme.obtainStyledAttributes(typedValue.resourceId, new int[]{android.R.attr.icon});
                    int iconRes = att2.getResourceId(0, -1);
                    att2.recycle();
                    if (iconRes > 0) {
                        icon = this.mActivity.getResources().getDrawable(iconRes);
                    }
                }
                att.recycle();
            }
            if (icon == null) {
                icon = this.mActivity.getApplicationInfo().loadLogo(this.mActivity.getPackageManager());
            }
            if (this.mHomeButtonEnabled && this.mDisplayLogo) {
                this.mHomeIcon.setClickable(true);
                this.mHomeIcon.setOnClickListener(this);
                this.mHomeIcon.setImageDrawable(icon);
                this.mHomeIcon.setVisibility(0);
                this.mSpacer.setVisibility(8);
            } else {
                this.mHomeIcon.setVisibility(8);
            }
        } else {
            this.mBackIcon.setVisibility(8);
            this.mHomeIcon.setVisibility(8);
            this.mSpacer.setVisibility(4);
        }
        if (this.mDisplayShowTitle.booleanValue() && this.mTitle != null) {
            this.mTitleView.setVisibility(0);
            this.mTitleView.setText(this.mTitle);
            if (this.mTextColor != null) {
                this.mTitleView.setTextColor(this.mTextColor.intValue());
                return;
            }
            return;
        }
        this.mTitleView.setVisibility(8);
    }

    public void setContentView(View v, ViewGroup.LayoutParams params) {
        this.mContentView = v;
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(params);
        v.setLayoutParams(p);
        p.addRule(3, this.mDistanceMarker.getId());
        this.mActionBarContainer.addView(this.mContentView);
    }

    public RelativeLayout getActionBar() {
        return this.mActionBar;
    }

    public TextView getTitleView() {
        return this.mTitleView;
    }

    public RelativeLayout getRoot() {
        return this.mActionBarContainer;
    }

    public void setHomeButtonEnabled(Boolean setHomeButtonEnabled) {
        this.mHomeButtonEnabled = setHomeButtonEnabled.booleanValue();
    }

    public void setDisplayShowTitleEnabled(Boolean setDisplayShowTitleEnabled) {
        this.mDisplayShowTitle = setDisplayShowTitleEnabled;
    }

    public void setDisplayHomeAsUpEnabled(Boolean setDisplayHomeAsUpEnabled) {
        this.mDisplayHomeAsUp = setDisplayHomeAsUpEnabled.booleanValue();
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public void setTitle(Integer titleRes) {
        this.mTitle = this.mActivity.getString(titleRes.intValue());
    }

    public void setDisplayLogoEnabled(Boolean setDisplayLogoEnabled) {
        this.mDisplayLogo = setDisplayLogoEnabled.booleanValue();
    }

    public void onCreateOptionsMenu(Menu menu, boolean show) {
        if (show) {
            int size = menu.size();
            for (int i = 0; i < size; i++) {
                final MenuItem item = menu.getItem(i);
                if (item.isVisible()) {
                    View v = null;
                    Drawable icon = item.getIcon();
                    CharSequence title = item.getTitle();
                    if (icon != null) {
                        v = new ImageView(this.mActivity);
                        ((ImageView) v).setImageDrawable(icon);
                    }
                    if (title != null) {
                        v = new TextView(this.mActivity);
                        ((TextView) v).setText(title);
                        ((TextView) v).setTextAppearance(this.mActivity, R.style.MenuItemActionBar);
                    }
                    if (v != null) {
                        v.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.widget.FakeActionBar.1
                            @Override // android.view.View.OnClickListener
                            public void onClick(View v2) {
                                FakeActionBar.this.mActivity.onOptionsItemSelected(item);
                            }
                        });
                        this.mActionBarRight.addView(v);
                        this.mActionBarRight.setVisibility(0);
                    }
                }
            }
        }
    }
}
