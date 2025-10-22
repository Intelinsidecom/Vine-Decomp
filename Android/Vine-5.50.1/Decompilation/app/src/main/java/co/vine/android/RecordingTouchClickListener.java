package co.vine.android;

import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

/* loaded from: classes.dex */
public abstract class RecordingTouchClickListener implements View.OnTouchListener {
    protected abstract void onClick();

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case 0:
                updateButtonDisplay(view, true);
                return true;
            case 1:
                updateButtonDisplay(view, false);
                boolean contains = new Rect(0, 0, view.getWidth(), view.getHeight()).contains((int) motionEvent.getX(), (int) motionEvent.getY());
                if (!contains) {
                    return true;
                }
                onClick();
                return true;
            case 2:
                boolean contains2 = new Rect(0, 0, view.getWidth(), view.getHeight()).contains((int) motionEvent.getX(), (int) motionEvent.getY());
                updateButtonDisplay(view, contains2);
                return true;
            default:
                return false;
        }
    }

    protected void updateButtonDisplay(View view, boolean on) {
        view.setAlpha(on ? 1.0f : 0.5f);
    }
}
