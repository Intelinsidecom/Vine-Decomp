package co.vine.android.share.widgets;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import co.vine.android.R;
import co.vine.android.widget.Typefaces;
import co.vine.android.widget.TypefacesTextView;
import co.vine.android.widget.VineToggleButton;

/* loaded from: classes.dex */
public class VineToggleRow extends RelativeLayout {
    private OnCheckedStateChangedListener mCheckedStateChangedListener;
    protected final ImageView mIcon;
    protected final TypefacesTextView mLabel;
    private int mLabelDisabledTextColor;
    private int mLabelSelectedTextColor;
    private int mLabelTextColor;
    protected final VineToggleButton mToggleButton;
    private int mToggleButtonColor;
    private int mToggleButtonDisabledColor;

    public interface OnCheckedStateChangedListener {
        void onCheckedStateChanged(boolean z, boolean z2);
    }

    public VineToggleRow(Context context) {
        this(context, null, 0);
    }

    public VineToggleRow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VineToggleRow(Context context, AttributeSet attrs, int defStyleAttr) throws Resources.NotFoundException {
        super(context, attrs, defStyleAttr);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService("layout_inflater");
        inflater.inflate(R.layout.toggle_row, this);
        this.mIcon = (ImageView) findViewById(R.id.toggle_icon);
        this.mLabel = (TypefacesTextView) findViewById(R.id.toggle_label);
        this.mToggleButton = (VineToggleButton) findViewById(R.id.toggle_button);
        int defaultLabelTextColor = getResources().getColor(android.R.color.black);
        int defaultToggleButtonColor = getResources().getColor(R.color.vine_green);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.VineToggleRow);
        Drawable icon = attributes.getDrawable(0);
        String labelText = attributes.getString(1);
        this.mLabelTextColor = attributes.getColor(2, defaultLabelTextColor);
        this.mLabelSelectedTextColor = attributes.getColor(3, defaultLabelTextColor);
        this.mLabelDisabledTextColor = attributes.getColor(4, defaultLabelTextColor);
        this.mToggleButtonColor = attributes.getColor(5, defaultToggleButtonColor);
        this.mToggleButtonDisabledColor = attributes.getColor(6, defaultToggleButtonColor);
        attributes.recycle();
        this.mIcon.setImageDrawable(icon);
        this.mIcon.setColorFilter(this.mToggleButtonColor, PorterDuff.Mode.SRC_IN);
        this.mLabel.setText(labelText);
        setTextStyle(isEnabled(), isChecked());
        this.mToggleButton.setCheckedColorStyle(Integer.valueOf(this.mToggleButtonColor));
        setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.share.widgets.VineToggleRow.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                VineToggleRow.this.mToggleButton.toggle();
            }
        });
        this.mToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: co.vine.android.share.widgets.VineToggleRow.2
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                VineToggleRow.this.setTextStyle(VineToggleRow.this.isEnabled(), isChecked);
                VineToggleRow.this.fireCheckedStateChangedListener(true);
            }
        });
    }

    public void setOnCheckedStateChangedListener(OnCheckedStateChangedListener listener) {
        this.mCheckedStateChangedListener = listener;
    }

    public void setChecked(boolean checked) {
        boolean oldCheckState = isChecked();
        if (checked != oldCheckState) {
            this.mToggleButton.setChecked(checked);
            fireCheckedStateChangedListener(false);
        }
    }

    @Override // android.view.View
    public void setEnabled(boolean enabled) {
        this.mToggleButton.setEnabled(enabled);
        super.setEnabled(enabled);
        setTextStyle(enabled, isChecked());
        ensureIconStyle();
        ensureToggleButtonStyle();
    }

    public void setCheckedWithoutEvent(boolean checked) {
        this.mToggleButton.setCheckedWithoutEvent(checked);
        setTextStyle(isEnabled(), isChecked());
    }

    public void setLabelTextColors(int unselectedLabelTextColor, int selectedLabelTextColor) {
        this.mLabelTextColor = unselectedLabelTextColor;
        this.mLabelSelectedTextColor = selectedLabelTextColor;
    }

    public void setToggleButtonColor(int toggleButtonColor) {
        this.mToggleButtonColor = toggleButtonColor;
        this.mToggleButton.setCheckedColorStyle(Integer.valueOf(this.mToggleButtonColor));
    }

    public boolean isChecked() {
        return this.mToggleButton.isChecked();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fireCheckedStateChangedListener(boolean fromUserInput) {
        if (this.mCheckedStateChangedListener != null) {
            boolean checked = this.mToggleButton.isChecked();
            this.mCheckedStateChangedListener.onCheckedStateChanged(checked, fromUserInput);
        }
    }

    private void ensureIconStyle() {
        if (isEnabled()) {
            this.mIcon.setColorFilter(this.mToggleButtonColor, PorterDuff.Mode.SRC_IN);
        } else {
            this.mIcon.setColorFilter(this.mToggleButtonDisabledColor, PorterDuff.Mode.SRC_IN);
        }
    }

    private void ensureToggleButtonStyle() {
        if (isEnabled()) {
            this.mToggleButton.setCheckedColorStyle(Integer.valueOf(this.mToggleButtonColor));
        } else {
            this.mToggleButton.setCheckedColorStyle(Integer.valueOf(this.mToggleButtonDisabledColor));
        }
    }

    protected void setTextStyle(boolean enabled, boolean checked) {
        if (!enabled) {
            this.mLabel.setTextColor(this.mLabelDisabledTextColor);
        } else if (checked) {
            this.mLabel.setTypeface(Typefaces.get(getContext()).getContentTypeface(1, 3), 1);
            this.mLabel.setTextColor(this.mLabelSelectedTextColor);
        } else {
            this.mLabel.setTypeface(Typefaces.get(getContext()).getContentTypeface(0, 3), 0);
            this.mLabel.setTextColor(this.mLabelTextColor);
        }
    }
}
