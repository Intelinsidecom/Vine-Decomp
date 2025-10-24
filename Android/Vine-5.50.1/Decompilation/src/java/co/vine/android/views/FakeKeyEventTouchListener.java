package co.vine.android.views;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

/* loaded from: classes.dex */
public class FakeKeyEventTouchListener implements View.OnTouchListener {
    private final Activity mActivity;
    private final int mCode;

    public FakeKeyEventTouchListener(Activity activity, int code) {
        this.mActivity = activity;
        this.mCode = code;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case 0:
                this.mActivity.onKeyDown(this.mCode, new KeyEvent(0, this.mCode));
                return true;
            case 1:
                this.mActivity.onKeyUp(this.mCode, new KeyEvent(1, this.mCode));
                return true;
            default:
                return true;
        }
    }
}
