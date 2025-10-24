package co.vine.android.share.widgets;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import co.vine.android.R;
import co.vine.android.widget.TypefacesTextView;

/* loaded from: classes.dex */
public final class VineCommentRow extends RelativeLayout {
    private String mComment;
    private int mCommentLabelTextColor;
    private String mEmptyCommentLabelText;
    private int mEmptyCommentLabelTextColor;
    private final ImageView mIcon;
    private int mIconColorFilterColor;
    private int mIconColorFilterEmptyColor;
    private final TypefacesTextView mLabel;

    public VineCommentRow(Context context) {
        this(context, null);
    }

    public VineCommentRow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VineCommentRow(Context context, AttributeSet attrs, int defStyleAttr) throws Resources.NotFoundException {
        super(context, attrs, defStyleAttr);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService("layout_inflater");
        inflater.inflate(R.layout.comment_row, this);
        this.mLabel = (TypefacesTextView) findViewById(R.id.comment_text);
        this.mIcon = (ImageView) findViewById(R.id.comment_icon);
        if (attrs != null) {
            applyStyles(context, attrs);
        }
        this.mComment = "";
        ensureIconColorFilter();
    }

    public String getComment() {
        return this.mComment;
    }

    public void setComment(String text) {
        if (TextUtils.isEmpty(text)) {
            clearComment();
        } else {
            this.mComment = text;
            this.mLabel.setText(text);
            this.mLabel.setTextColor(this.mCommentLabelTextColor);
        }
        ensureIconColorFilter();
    }

    private void applyStyles(Context context, AttributeSet attrs) throws Resources.NotFoundException {
        int defaultColor = getResources().getColor(android.R.color.black);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.VineCommentRow);
        Drawable icon = attributes.getDrawable(0);
        this.mIconColorFilterColor = attributes.getColor(1, defaultColor);
        this.mIconColorFilterEmptyColor = attributes.getColor(2, defaultColor);
        this.mEmptyCommentLabelText = attributes.getString(3);
        this.mEmptyCommentLabelTextColor = attributes.getColor(4, defaultColor);
        this.mCommentLabelTextColor = attributes.getColor(5, defaultColor);
        attributes.recycle();
        this.mIcon.setImageDrawable(icon);
        clearComment();
    }

    private void clearComment() {
        this.mComment = "";
        this.mLabel.setText(this.mEmptyCommentLabelText);
        this.mLabel.setTextColor(this.mEmptyCommentLabelTextColor);
    }

    private void ensureIconColorFilter() {
        this.mIcon.setColorFilter(this.mComment.isEmpty() ? this.mIconColorFilterEmptyColor : this.mIconColorFilterColor, PorterDuff.Mode.SRC_IN);
    }

    public void setEmptyCommentLabelText(String text) {
        this.mEmptyCommentLabelText = text;
        clearComment();
    }
}
