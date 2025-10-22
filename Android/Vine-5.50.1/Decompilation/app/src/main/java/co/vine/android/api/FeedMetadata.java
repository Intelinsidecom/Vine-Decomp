package co.vine.android.api;

import android.os.Parcel;
import android.os.Parcelable;
import co.vine.android.api.response.ParsingTypeConverter;
import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.fasterxml.jackson.core.JsonParser;
import java.io.IOException;

@JsonObject
/* loaded from: classes.dex */
public class FeedMetadata implements Parcelable {
    public static final Parcelable.Creator<FeedMetadata> CREATOR = new Parcelable.Creator<FeedMetadata>() { // from class: co.vine.android.api.FeedMetadata.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FeedMetadata createFromParcel(Parcel in) {
            return new FeedMetadata(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FeedMetadata[] newArray(int size) {
            return new FeedMetadata[size];
        }
    };

    @JsonField(name = {"channel"})
    public VineChannel channel;

    @JsonField(name = {"feedType"}, typeConverter = FeedTypeConverter.class)
    public FeedType feedType;

    @JsonField(name = {"profileUserId"})
    public long profileUserId;

    @JsonField(name = {"userProfile"})
    public VineUser userProfile;

    public enum FeedType {
        PROFILE,
        CHANNEL
    }

    public FeedMetadata() {
    }

    private FeedMetadata(Parcel in) {
        ClassLoader cl = getClass().getClassLoader();
        this.userProfile = (VineUser) in.readParcelable(cl);
        this.profileUserId = in.readLong();
        this.feedType = (FeedType) in.readSerializable();
        this.channel = (VineChannel) in.readParcelable(cl);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.userProfile, flags);
        dest.writeLong(this.profileUserId);
        dest.writeSerializable(this.feedType);
        dest.writeParcelable(this.channel, flags);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public static final class FeedTypeConverter extends ParsingTypeConverter<FeedType> {
        @Override // com.bluelinelabs.logansquare.typeconverters.TypeConverter
        public FeedType parse(JsonParser jsonParser) throws IOException {
            String feedType = jsonParser.getValueAsString();
            if (feedType.toLowerCase().equals("profile")) {
                return FeedType.PROFILE;
            }
            if (feedType.toLowerCase().equals("channel")) {
                return FeedType.CHANNEL;
            }
            return FeedType.PROFILE;
        }
    }
}
