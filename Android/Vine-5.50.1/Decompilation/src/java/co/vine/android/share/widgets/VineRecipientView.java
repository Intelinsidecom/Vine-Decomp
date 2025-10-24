package co.vine.android.share.widgets;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import co.vine.android.R;
import co.vine.android.api.VineRecipient;
import co.vine.android.util.ColorUtils;
import co.vine.android.widget.TypefacesTextView;

/* loaded from: classes.dex */
public final class VineRecipientView extends RelativeLayout {
    private final ImageView mCancelButton;
    private int mColorVineGreen;
    private final ViewGroup mContainer;
    private final TypefacesTextView mLabel;
    private VineRecipient mRecipient;
    private int mRecipientColor;
    private float mStrokeWidth;

    public VineRecipientView(Context context) {
        this(context, null);
    }

    public VineRecipientView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VineRecipientView(Context context, AttributeSet attrs, int defStyleAttr) throws Resources.NotFoundException {
        super(context, attrs, defStyleAttr);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService("layout_inflater");
        inflater.inflate(R.layout.vine_recipient, this);
        this.mContainer = (ViewGroup) findViewById(R.id.content_container);
        this.mLabel = (TypefacesTextView) findViewById(R.id.label);
        this.mCancelButton = (ImageView) findViewById(R.id.cancel);
        Resources resources = getResources();
        this.mColorVineGreen = resources.getColor(R.color.vine_green);
        this.mStrokeWidth = resources.getDimension(R.dimen.vm_recipient_view_stoke_width);
        Drawable background = resources.getDrawable(R.drawable.bg_recipient);
        this.mContainer.setBackground(background);
    }

    public void bind(VineRecipient recipient) {
        this.mRecipient = recipient;
        if (recipient.color < 0) {
            recipient.color = this.mColorVineGreen;
        }
        this.mRecipientColor = ColorUtils.ensureNotBlack((-16777216) | recipient.color, this.mColorVineGreen);
        this.mLabel.setText(recipient.getDisplay());
        this.mCancelButton.setColorFilter(this.mRecipientColor, PorterDuff.Mode.SRC_IN);
        updateView();
    }

    @Override // android.view.View
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        updateView();
    }

    public VineRecipient getRecipient() {
        return this.mRecipient;
    }

    private void updateView() {
        GradientDrawable drawable = (GradientDrawable) this.mContainer.getBackground();
        drawable.setStroke((int) this.mStrokeWidth, this.mRecipientColor);
        boolean selected = isSelected();
        drawable.setColor(selected ? -1 : this.mRecipientColor);
        this.mLabel.setTextColor(selected ? this.mRecipientColor : -1);
        this.mLabel.setEnabled(!selected);
        this.mLabel.setVisibility(selected ? 4 : 0);
        this.mCancelButton.setEnabled(selected);
        this.mCancelButton.setVisibility(selected ? 0 : 8);
    }
}
