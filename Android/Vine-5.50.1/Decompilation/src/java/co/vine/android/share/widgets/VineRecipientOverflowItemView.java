package co.vine.android.share.widgets;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import co.vine.android.R;
import co.vine.android.api.VineRecipient;
import co.vine.android.util.ViewUtil;
import co.vine.android.widget.TypefacesTextView;
import java.util.LinkedList;

/* loaded from: classes.dex */
public final class VineRecipientOverflowItemView extends RelativeLayout {
    private final TypefacesTextView mLabel;
    private final LinkedList<VineRecipient> mRecipients;
    private final String mSingleRecipientStringResource;

    public VineRecipientOverflowItemView(Context context) {
        this(context, null);
    }

    public VineRecipientOverflowItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VineRecipientOverflowItemView(Context context, AttributeSet attrs, int defStyleAttr) throws Resources.NotFoundException {
        super(context, attrs, defStyleAttr);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService("layout_inflater");
        inflater.inflate(R.layout.recipient_picker_overflow_item, this);
        this.mLabel = (TypefacesTextView) findViewById(R.id.label);
        this.mSingleRecipientStringResource = getResources().getString(R.string.and_x_others_single);
        this.mRecipients = new LinkedList<>();
        Resources resources = getResources();
        int colorSuperLightGray = resources.getColor(R.color.solid_super_light_gray);
        int colorBlackFiftyPercent = resources.getColor(R.color.black_fifty_percent);
        float strokeWidth = resources.getDimension(R.dimen.vm_recipient_view_stoke_width);
        GradientDrawable background = (GradientDrawable) resources.getDrawable(R.drawable.bg_recipient);
        background.setColor(colorSuperLightGray);
        background.setStroke((int) strokeWidth, colorSuperLightGray);
        this.mLabel.setBackground(background);
        this.mLabel.setTextColor(colorBlackFiftyPercent);
        updateTextAndVisibility();
    }

    public void addRecipient(VineRecipient recipient) {
        boolean added = this.mRecipients.add(recipient);
        if (added) {
            updateTextAndVisibility();
        }
    }

    public void removeRecipient(VineRecipient recipient) {
        boolean removed = this.mRecipients.remove(recipient);
        if (removed) {
            updateTextAndVisibility();
        }
    }

    public VineRecipient removeFirstRecipient() {
        if (this.mRecipients.isEmpty()) {
            return null;
        }
        VineRecipient recipient = this.mRecipients.remove(0);
        VineRecipient recipient2 = recipient;
        updateTextAndVisibility();
        return recipient2;
    }

    public boolean containsRecipient(VineRecipient recipient) {
        return this.mRecipients.contains(recipient);
    }

    private String getMultipleRecipientString(int numberOfRecipients) {
        return getResources().getString(R.string.and_x_others_plural, Integer.valueOf(numberOfRecipients));
    }

    private void updateTextAndVisibility() {
        int numberOfRecipients = this.mRecipients.size();
        if (this.mRecipients.isEmpty()) {
            ViewUtil.disableAndHide(this);
            return;
        }
        if (numberOfRecipients == 1) {
            this.mLabel.setText(this.mSingleRecipientStringResource);
            ViewUtil.enableAndShow(this);
        } else {
            String labelText = getMultipleRecipientString(numberOfRecipients);
            this.mLabel.setText(labelText);
            ViewUtil.enableAndShow(this);
        }
    }
}
