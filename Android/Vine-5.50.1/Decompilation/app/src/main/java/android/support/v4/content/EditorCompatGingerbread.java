package android.support.v4.content;

import android.content.SharedPreferences;

/* loaded from: classes2.dex */
class EditorCompatGingerbread {
    EditorCompatGingerbread() {
    }

    public static void apply(SharedPreferences.Editor editor) {
        try {
            editor.apply();
        } catch (AbstractMethodError e) {
            editor.commit();
        }
    }
}
