package android.support.v4.widget;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.CompoundButton;
import java.lang.reflect.Field;

/* loaded from: classes2.dex */
class CompoundButtonCompatDonut {
    private static final String TAG = "CompoundButtonCompatDonut";
    private static Field sButtonDrawableField;
    private static boolean sButtonDrawableFieldFetched;

    CompoundButtonCompatDonut() {
    }

    /* JADX WARN: Multi-variable type inference failed */
    static void setButtonTintList(CompoundButton compoundButton, ColorStateList tint) {
        if (compoundButton instanceof TintableCompoundButton) {
            ((TintableCompoundButton) compoundButton).setSupportButtonTintList(tint);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    static ColorStateList getButtonTintList(CompoundButton compoundButton) {
        if (compoundButton instanceof TintableCompoundButton) {
            return ((TintableCompoundButton) compoundButton).getSupportButtonTintList();
        }
        return null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    static void setButtonTintMode(CompoundButton compoundButton, PorterDuff.Mode tintMode) {
        if (compoundButton instanceof TintableCompoundButton) {
            ((TintableCompoundButton) compoundButton).setSupportButtonTintMode(tintMode);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    static PorterDuff.Mode getButtonTintMode(CompoundButton compoundButton) {
        if (compoundButton instanceof TintableCompoundButton) {
            return ((TintableCompoundButton) compoundButton).getSupportButtonTintMode();
        }
        return null;
    }

    static Drawable getButtonDrawable(CompoundButton button) {
        if (!sButtonDrawableFieldFetched) {
            try {
                sButtonDrawableField = CompoundButton.class.getDeclaredField("mButtonDrawable");
                sButtonDrawableField.setAccessible(true);
            } catch (NoSuchFieldException e) {
                Log.i(TAG, "Failed to retrieve mButtonDrawable field", e);
            }
            sButtonDrawableFieldFetched = true;
        }
        if (sButtonDrawableField != null) {
            try {
                return (Drawable) sButtonDrawableField.get(button);
            } catch (IllegalAccessException e2) {
                Log.i(TAG, "Failed to get button drawable via reflection", e2);
                sButtonDrawableField = null;
            }
        }
        return null;
    }
}
