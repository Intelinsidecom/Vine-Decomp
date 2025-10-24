package android.support.v4.widget;

import android.os.Build;
import android.widget.ListView;

/* loaded from: classes.dex */
public final class ListViewCompat {
    public static void scrollListBy(ListView listView, int y) {
        if (Build.VERSION.SDK_INT >= 19) {
            ListViewCompatKitKat.scrollListBy(listView, y);
        } else {
            ListViewCompatDonut.scrollListBy(listView, y);
        }
    }

    private ListViewCompat() {
    }
}
