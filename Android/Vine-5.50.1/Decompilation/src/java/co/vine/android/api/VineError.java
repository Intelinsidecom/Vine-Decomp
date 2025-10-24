package co.vine.android.api;

import android.os.Parcelable;

/* loaded from: classes.dex */
public abstract class VineError implements Parcelable {
    public abstract String getData();

    public abstract int getErrorCode();

    public abstract String getMessage();

    public static VineError create(int errorCode, String message) {
        return create(errorCode, message, null);
    }

    public static VineError create(int errorCode, String message, String data) {
        return new AutoParcel_VineError(errorCode, message, data);
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o.getClass() == getClass()) {
            return getErrorCode() == ((VineError) o).getErrorCode();
        }
        if (o instanceof VineKnownErrors) {
            return getErrorCode() == ((VineKnownErrors) o).code;
        }
        return false;
    }
}
