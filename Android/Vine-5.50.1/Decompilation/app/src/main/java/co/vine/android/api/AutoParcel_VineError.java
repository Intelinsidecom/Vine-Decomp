package co.vine.android.api;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
final class AutoParcel_VineError extends VineError {
    private final String data;
    private final int errorCode;
    private final String message;
    public static final Parcelable.Creator<AutoParcel_VineError> CREATOR = new Parcelable.Creator<AutoParcel_VineError>() { // from class: co.vine.android.api.AutoParcel_VineError.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AutoParcel_VineError createFromParcel(Parcel in) {
            return new AutoParcel_VineError(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AutoParcel_VineError[] newArray(int size) {
            return new AutoParcel_VineError[size];
        }
    };
    private static final ClassLoader CL = AutoParcel_VineError.class.getClassLoader();

    AutoParcel_VineError(int errorCode, String message, String data) {
        this.errorCode = errorCode;
        this.message = message;
        this.data = data;
    }

    @Override // co.vine.android.api.VineError
    public int getErrorCode() {
        return this.errorCode;
    }

    @Override // co.vine.android.api.VineError
    public String getMessage() {
        return this.message;
    }

    @Override // co.vine.android.api.VineError
    public String getData() {
        return this.data;
    }

    public String toString() {
        return "VineError{errorCode=" + this.errorCode + ", message=" + this.message + ", data=" + this.data + "}";
    }

    public int hashCode() {
        int h = 1 * 1000003;
        return ((((h ^ this.errorCode) * 1000003) ^ (this.message == null ? 0 : this.message.hashCode())) * 1000003) ^ (this.data != null ? this.data.hashCode() : 0);
    }

    private AutoParcel_VineError(Parcel in) {
        this(((Integer) in.readValue(CL)).intValue(), (String) in.readValue(CL), (String) in.readValue(CL));
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(Integer.valueOf(this.errorCode));
        dest.writeValue(this.message);
        dest.writeValue(this.data);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }
}
