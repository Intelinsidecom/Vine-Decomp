package co.vine.android.recorder;

import android.graphics.Canvas;
import java.util.ArrayList;

/* loaded from: classes.dex */
public abstract class AbstractEncodingRunnable implements Runnable {
    protected ArrayList<Drawer> mAdditionalDrawers = new ArrayList<>();

    public interface Drawer {
        void draw(Canvas canvas, boolean z);
    }

    public abstract void setAsyncTask(BaseFinishProcessTask baseFinishProcessTask);

    public abstract void terminate();

    public void addAdditionalDrawer(Drawer additionalDrawer) {
        this.mAdditionalDrawers.add(additionalDrawer);
    }
}
