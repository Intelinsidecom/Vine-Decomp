package co.vine.android;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.view.View;
import co.vine.android.util.ViewUtil;

/* loaded from: classes.dex */
public class SwitchUtils {
    public static void setSelected(Context context, View selectionIndicator, boolean selected, int color) throws Resources.NotFoundException {
        selectionIndicator.setSelected(selected);
        Resources res = context.getResources();
        if (selected) {
            Drawable bg = res.getDrawable(R.drawable.ic_circle_selected).mutate();
            bg.setColorFilter(new PorterDuffColorFilter((-16777216) | color, PorterDuff.Mode.SRC_IN));
            ViewUtil.setBackground(selectionIndicator, bg);
        } else {
            Drawable bg2 = res.getDrawable(R.drawable.ic_circle_default);
            bg2.setColorFilter(new PorterDuffColorFilter(res.getColor(R.color.btn_circle_stroke), PorterDuff.Mode.SRC_IN));
            ViewUtil.setBackground(selectionIndicator, bg2);
        }
    }
}
