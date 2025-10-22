package com.digits.sdk.android;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import java.lang.reflect.Field;

/* loaded from: classes.dex */
class ActivityClassManagerFactory {
    ActivityClassManagerFactory() {
    }

    ActivityClassManager createActivityClassManager(Context context, int themeResId) throws ClassNotFoundException {
        ActivityClassManager activityClassManagerImp;
        try {
            Class.forName("android.support.v7.app.ActionBarActivity");
            ThemeAttributes attributes = new ThemeAttributes();
            if (isAppCompatTheme(context, themeResId, attributes)) {
                activityClassManagerImp = new AppCompatClassManagerImp();
            } else {
                activityClassManagerImp = new ActivityClassManagerImp();
            }
            return activityClassManagerImp;
        } catch (Exception e) {
            return new ActivityClassManagerImp();
        }
    }

    private boolean isAppCompatTheme(Context context, int themeResId, ThemeAttributes attributes) throws Resources.NotFoundException {
        TypedArray a = context.obtainStyledAttributes(themeResId, attributes.styleableTheme);
        boolean result = a.hasValue(attributes.styleableThemeWindowActionBar);
        a.recycle();
        return result;
    }

    static class ThemeAttributes {
        private final int[] styleableTheme;
        private final int styleableThemeWindowActionBar;

        public ThemeAttributes() throws Exception {
            Class<?> clazz = Class.forName("android.support.v7.appcompat.R$styleable");
            Field field = clazz.getField("Theme");
            this.styleableTheme = (int[]) field.get(field.getType());
            Field field2 = clazz.getField("Theme_windowActionBar");
            this.styleableThemeWindowActionBar = ((Integer) field2.get(field2.getType())).intValue();
        }
    }
}
