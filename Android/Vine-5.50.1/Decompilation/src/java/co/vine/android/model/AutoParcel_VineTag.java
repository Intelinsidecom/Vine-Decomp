package co.vine.android.model;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
final class AutoParcel_VineTag extends VineTag {
    private final long postCount;
    private final long tagId;
    private final String tagName;
    public static final Parcelable.Creator<AutoParcel_VineTag> CREATOR = new Parcelable.Creator<AutoParcel_VineTag>() { // from class: co.vine.android.model.AutoParcel_VineTag.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AutoParcel_VineTag createFromParcel(Parcel in) {
            return new AutoParcel_VineTag(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AutoParcel_VineTag[] newArray(int size) {
            return new AutoParcel_VineTag[size];
        }
    };
    private static final ClassLoader CL = AutoParcel_VineTag.class.getClassLoader();

    AutoParcel_VineTag(String tagName, long tagId, long postCount) {
        if (tagName == null) {
            throw new NullPointerException("Null tagName");
        }
        this.tagName = tagName;
        this.tagId = tagId;
        this.postCount = postCount;
    }

    @Override // co.vine.android.model.VineTag
    public String getTagName() {
        return this.tagName;
    }

    @Override // co.vine.android.model.VineTag
    public long getTagId() {
        return this.tagId;
    }

    @Override // co.vine.android.model.VineTag
    public long getPostCount() {
        return this.postCount;
    }

    public String toString() {
        return "VineTag{tagName=" + this.tagName + ", tagId=" + this.tagId + ", postCount=" + this.postCount + "}";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof VineTag)) {
            return false;
        }
        VineTag that = (VineTag) o;
        return this.tagName.equals(that.getTagName()) && this.tagId == that.getTagId() && this.postCount == that.getPostCount();
    }

    public int hashCode() {
        int h = 1 * 1000003;
        return (int) ((((int) (((h ^ this.tagName.hashCode()) * 1000003) ^ ((this.tagId >>> 32) ^ this.tagId))) * 1000003) ^ ((this.postCount >>> 32) ^ this.postCount));
    }

    private AutoParcel_VineTag(Parcel in) {
        this((String) in.readValue(CL), ((Long) in.readValue(CL)).longValue(), ((Long) in.readValue(CL)).longValue());
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.tagName);
        dest.writeValue(Long.valueOf(this.tagId));
        dest.writeValue(Long.valueOf(this.postCount));
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }
}
