package co.vine.android.share.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import co.vine.android.R;
import co.vine.android.widget.TypefacesTextView;

/* loaded from: classes.dex */
public final class SectionHeaderRow extends LinearLayout {
    private final TypefacesTextView mText;

    public SectionHeaderRow(Context context) {
        this(context, null, 0);
    }

    public SectionHeaderRow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SectionHeaderRow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService("layout_inflater");
        inflater.inflate(R.layout.vm_recipient_section, this);
        this.mText = (TypefacesTextView) findViewById(R.id.text);
    }

    public void bind(String text) {
        this.mText.setText(text);
    }
}
