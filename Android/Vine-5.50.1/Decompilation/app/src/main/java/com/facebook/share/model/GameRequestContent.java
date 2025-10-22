package com.facebook.share.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public final class GameRequestContent implements ShareModel {
    public static final Parcelable.Creator<GameRequestContent> CREATOR = new Parcelable.Creator<GameRequestContent>() { // from class: com.facebook.share.model.GameRequestContent.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GameRequestContent createFromParcel(Parcel in) {
            return new GameRequestContent(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GameRequestContent[] newArray(int size) {
            return new GameRequestContent[size];
        }
    };
    private final ActionType actionType;
    private final String data;
    private final Filters filters;
    private final String message;
    private final String objectId;
    private final ArrayList<String> suggestions = new ArrayList<>();
    private final String title;
    private final String to;

    public enum ActionType {
        SEND,
        ASKFOR,
        TURN
    }

    public enum Filters {
        APP_USERS,
        APP_NON_USERS
    }

    GameRequestContent(Parcel in) {
        this.message = in.readString();
        this.to = in.readString();
        this.title = in.readString();
        this.data = in.readString();
        this.actionType = ActionType.valueOf(in.readString());
        this.objectId = in.readString();
        this.filters = Filters.valueOf(in.readString());
        in.readStringList(this.suggestions);
    }

    public ActionType getActionType() {
        return this.actionType;
    }

    public String getObjectId() {
        return this.objectId;
    }

    public Filters getFilters() {
        return this.filters;
    }

    public ArrayList<String> getSuggestions() {
        return this.suggestions;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.message);
        out.writeString(this.to);
        out.writeString(this.title);
        out.writeString(this.data);
        out.writeString(getActionType().toString());
        out.writeString(getObjectId());
        out.writeString(getFilters().toString());
        out.writeStringList(getSuggestions());
    }
}
