package co.vine.android.share.widgets;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import co.vine.android.ContactEntry;
import co.vine.android.R;
import co.vine.android.widget.TypefacesTextView;

/* loaded from: classes.dex */
public final class VineContactRow extends RelativeLayout {
    private int mColorSelectedIndicator;
    private int mColorUnselectedIndicator;
    private final ImageView mEmailIndicator;
    private final TypefacesTextView mLabel;
    private final ImageView mPhoneIndicator;

    public VineContactRow(Context context) {
        this(context, null, 0);
    }

    public VineContactRow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VineContactRow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.contact_row, this);
        this.mLabel = (TypefacesTextView) findViewById(R.id.contact_name);
        this.mEmailIndicator = (ImageView) findViewById(R.id.email_indicator);
        this.mPhoneIndicator = (ImageView) findViewById(R.id.phone_indicator);
        Resources resources = getResources();
        this.mColorUnselectedIndicator = resources.getColor(R.color.black_thirty_five_percent);
        this.mColorSelectedIndicator = resources.getColor(R.color.vine_green);
    }

    public void bind(ContactEntry contactEntry, int selectedMimeTypes) {
        this.mLabel.setText(contactEntry.displayName);
        boolean showEmail = (contactEntry.typeFlag & 1) != 0;
        boolean showPhone = (contactEntry.typeFlag & 2) != 0;
        int emailIndicatorColorFilter = (selectedMimeTypes & 1) > 0 ? this.mColorSelectedIndicator : this.mColorUnselectedIndicator;
        int phoneIndicatorColorFilter = (selectedMimeTypes & 2) > 0 ? this.mColorSelectedIndicator : this.mColorUnselectedIndicator;
        this.mEmailIndicator.setColorFilter(emailIndicatorColorFilter, PorterDuff.Mode.SRC_IN);
        this.mPhoneIndicator.setColorFilter(phoneIndicatorColorFilter, PorterDuff.Mode.SRC_IN);
        setIndicators(showPhone, showEmail);
    }

    private void setIndicators(boolean showPhone, boolean showEmail) {
        int phoneVisibility = showPhone ? 0 : 8;
        int emailVisibility = showEmail ? 0 : 8;
        this.mPhoneIndicator.setVisibility(phoneVisibility);
        this.mEmailIndicator.setVisibility(emailVisibility);
    }
}
