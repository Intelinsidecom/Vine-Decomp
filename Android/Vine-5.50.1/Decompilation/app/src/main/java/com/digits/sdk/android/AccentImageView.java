package com.digits.sdk.android;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.widget.ImageView;

/* loaded from: classes.dex */
public class AccentImageView extends ImageView {
    public AccentImageView(Context context) throws NoSuchFieldException {
        super(context);
        init(context);
    }

    public AccentImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AccentImageView(Context context, AttributeSet attrs, int defStyleAttr) throws NoSuchFieldException {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) throws NoSuchFieldException {
        int accentColor = ThemeUtils.getAccentColor(getResources(), context.getTheme());
        setColorFilter(accentColor, PorterDuff.Mode.SRC_IN);
    }
}
