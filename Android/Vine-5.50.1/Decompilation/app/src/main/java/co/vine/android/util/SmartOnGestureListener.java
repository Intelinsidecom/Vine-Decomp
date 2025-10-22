package co.vine.android.util;

import android.view.GestureDetector;
import android.view.MotionEvent;

/* loaded from: classes.dex */
public class SmartOnGestureListener extends GestureDetector.SimpleOnGestureListener {
    @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1 == null || e2 == null) {
            return false;
        }
        float x1 = e1.getX();
        float y1 = e1.getY();
        float x2 = e2.getX();
        float y2 = e2.getY();
        Direction direction = getDirection(x1, y1, x2, y2);
        return onSwipe(direction);
    }

    public boolean onSwipe(Direction direction) {
        return false;
    }

    public Direction getDirection(float x1, float y1, float x2, float y2) {
        double angle = getAngle(x1, y1, x2, y2);
        return Direction.get(angle);
    }

    public double getAngle(float x1, float y1, float x2, float y2) {
        double rad = Math.atan2(y1 - y2, x2 - x1) + 3.141592653589793d;
        return (((rad * 180.0d) / 3.141592653589793d) + 180.0d) % 360.0d;
    }

    public enum Direction {
        NONE,
        UP,
        DOWN,
        LEFT,
        RIGHT;

        public static Direction get(double angle) {
            if (inRange(angle, 45.0f, 135.0f)) {
                return UP;
            }
            if (inRange(angle, 0.0f, 45.0f) || inRange(angle, 315.0f, 360.0f)) {
                return RIGHT;
            }
            if (inRange(angle, 225.0f, 315.0f)) {
                return DOWN;
            }
            return LEFT;
        }

        private static boolean inRange(double angle, float init, float end) {
            return angle >= ((double) init) && angle < ((double) end);
        }
    }
}
