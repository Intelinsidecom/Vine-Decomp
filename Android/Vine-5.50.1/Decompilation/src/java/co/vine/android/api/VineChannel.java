package co.vine.android.api;

import android.os.Parcel;
import android.os.Parcelable;
import co.vine.android.api.response.DateStringToMilliseconds;
import co.vine.android.api.response.ParsingTypeConverter;
import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.fasterxml.jackson.core.JsonParser;
import java.io.IOException;
import java.util.ArrayList;

@JsonObject
/* loaded from: classes.dex */
public class VineChannel implements Parcelable, Comparable<VineChannel> {
    public static final Parcelable.Creator<VineChannel> CREATOR = new Parcelable.Creator<VineChannel>() { // from class: co.vine.android.api.VineChannel.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VineChannel createFromParcel(Parcel in) {
            return new VineChannel(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VineChannel[] newArray(int size) {
            return new VineChannel[size];
        }
    };

    @JsonField(name = {"backgroundColor"}, typeConverter = ColorTypeConverter.class)
    public String backgroundColor;

    @JsonField(name = {"channel"})
    public String channel;

    @JsonField(name = {"channelId"})
    public long channelId;
    public int colorHex;

    @JsonField(name = {"created"}, typeConverter = DateStringToMilliseconds.class)
    public long created;

    @JsonField(name = {"description"})
    public String description;

    @JsonField(name = {"exploreRetinaIconFullUrl"})
    public String exploreRetinaIconFullUrl;

    @JsonField(name = {"following"})
    public boolean following;

    @JsonField(name = {"fontColor"}, typeConverter = ColorTypeConverter.class)
    public String fontColor;

    @JsonField(name = {"iconFullUrl"})
    public String iconFullUrl;

    @JsonField(name = {"retinaIconFullUrl"})
    public String retinaIconFullUrl;

    @JsonField(name = {"secondaryColor"}, typeConverter = ColorTypeConverter.class)
    public String secondaryColor;

    @JsonField(name = {"showRecent"})
    public Boolean showRecent;

    @JsonField(name = {"timeline"})
    public TimeLine timeline;

    @JsonObject
    public static class TimeLine {

        @JsonField(name = {"records"})
        public ArrayList<TimelineItem> items;
    }

    public static final class ColorTypeConverter extends ParsingTypeConverter<String> {
        @Override // com.bluelinelabs.logansquare.typeconverters.TypeConverter
        public String parse(JsonParser jsonParser) throws IOException {
            String color = jsonParser.getValueAsString();
            if (color == null) {
                return null;
            }
            return !color.startsWith("#") ? "#" + color : color;
        }
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VineChannel that = (VineChannel) o;
        if (this.following == that.following && this.channelId == that.channelId && this.created == that.created) {
            if (this.channel == null ? that.channel != null : !this.channel.equals(that.channel)) {
                return false;
            }
            if (this.iconFullUrl == null ? that.iconFullUrl != null : !this.iconFullUrl.equals(that.iconFullUrl)) {
                return false;
            }
            if (this.retinaIconFullUrl == null ? that.retinaIconFullUrl != null : !this.retinaIconFullUrl.equals(that.retinaIconFullUrl)) {
                return false;
            }
            if (this.description == null ? that.description != null : !this.description.equals(that.description)) {
                return false;
            }
            if (this.backgroundColor == null ? that.backgroundColor != null : !this.backgroundColor.equals(that.backgroundColor)) {
                return false;
            }
            if (this.secondaryColor == null ? that.secondaryColor != null : !this.secondaryColor.equals(that.secondaryColor)) {
                return false;
            }
            if (this.fontColor == null ? that.fontColor != null : !this.fontColor.equals(that.fontColor)) {
                return false;
            }
            if (this.exploreRetinaIconFullUrl == null ? that.exploreRetinaIconFullUrl != null : !this.exploreRetinaIconFullUrl.equals(that.exploreRetinaIconFullUrl)) {
                return false;
            }
            return this.colorHex == that.colorHex && this.showRecent == that.showRecent;
        }
        return false;
    }

    public int hashCode() {
        int result = (int) (this.channelId ^ (this.channelId >>> 32));
        return (((((((((((((((((((((((result * 31) + ((int) (this.created ^ (this.created >>> 32)))) * 31) + (this.following ? 1 : 0)) * 31) + (this.channel != null ? this.channel.hashCode() : 0)) * 31) + (this.iconFullUrl != null ? this.iconFullUrl.hashCode() : 0)) * 31) + (this.retinaIconFullUrl != null ? this.retinaIconFullUrl.hashCode() : 0)) * 31) + (this.description != null ? this.description.hashCode() : 0)) * 31) + (this.backgroundColor != null ? this.backgroundColor.hashCode() : 0)) * 31) + (this.secondaryColor != null ? this.secondaryColor.hashCode() : 0)) * 31) + (this.fontColor != null ? this.fontColor.hashCode() : 0)) * 31) + (this.exploreRetinaIconFullUrl != null ? this.exploreRetinaIconFullUrl.hashCode() : 0)) * 31) + this.colorHex) * 31) + (this.showRecent.booleanValue() ? 1 : 0);
    }

    @Override // java.lang.Comparable
    public int compareTo(VineChannel vineChannel) {
        return Long.valueOf(vineChannel.channelId).compareTo(Long.valueOf(this.channelId));
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.following ? 1 : 0);
        dest.writeLong(this.channelId);
        dest.writeLong(this.created);
        dest.writeString(this.channel);
        dest.writeString(this.description);
        dest.writeString(this.backgroundColor);
        dest.writeString(this.secondaryColor);
        dest.writeString(this.fontColor);
        dest.writeString(this.iconFullUrl);
        dest.writeString(this.retinaIconFullUrl);
        dest.writeString(this.exploreRetinaIconFullUrl);
        dest.writeInt(this.showRecent.booleanValue() ? 1 : 0);
    }

    public VineChannel() {
    }

    public VineChannel(Parcel in) {
        this.following = in.readInt() == 1;
        this.channelId = in.readLong();
        this.created = in.readLong();
        this.channel = in.readString();
        this.description = in.readString();
        this.backgroundColor = in.readString();
        this.secondaryColor = in.readString();
        this.fontColor = in.readString();
        this.iconFullUrl = in.readString();
        this.retinaIconFullUrl = in.readString();
        this.exploreRetinaIconFullUrl = in.readString();
        this.showRecent = Boolean.valueOf(in.readInt() == 1);
    }
}
