package co.vine.android;

import android.app.Activity;
import android.view.OrientationEventListener;

/* loaded from: classes.dex */
public class FullScreenVideoOrientationManager extends OrientationEventListener {
    private final Activity mActivity;

    public FullScreenVideoOrientationManager(Activity activity) {
        super(activity);
        this.mActivity = activity;
    }

    @Override // android.view.OrientationEventListener
    public void onOrientationChanged(int degrees) {
        if (this.mActivity.getRequestedOrientation() == 0 && (degreesWithinAllowance(degrees, 90, 10) || degreesWithinAllowance(degrees, 270, 10))) {
            this.mActivity.setRequestedOrientation(-1);
        } else if (this.mActivity.getRequestedOrientation() == 1) {
            if (degrees > 350 || degrees < 10) {
                this.mActivity.setRequestedOrientation(-1);
            }
        }
    }

    private static boolean degreesWithinAllowance(int orientation, int desired, int allowance) {
        return Math.abs(desired - orientation) < allowance;
    }
}
