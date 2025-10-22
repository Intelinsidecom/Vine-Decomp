package com.google.android.gms.ads.internal.client;

import android.os.Parcel;
import com.google.android.gms.ads.search.SearchAdRequest;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.zzha;

@zzha
/* loaded from: classes.dex */
public final class SearchAdRequestParcel implements SafeParcelable {
    public static final zzae CREATOR = new zzae();
    public final int backgroundColor;
    public final int versionCode;
    public final int zzuI;
    public final int zzuJ;
    public final int zzuK;
    public final int zzuL;
    public final int zzuM;
    public final int zzuN;
    public final int zzuO;
    public final String zzuP;
    public final int zzuQ;
    public final String zzuR;
    public final int zzuS;
    public final int zzuT;
    public final String zzuU;

    SearchAdRequestParcel(int versionCode, int anchorTextColor, int backgroundColor, int backgroundGradientBottom, int backgroundGradientTop, int borderColor, int borderThickness, int borderType, int callButtonColor, String channels, int descriptionTextColor, String fontFace, int headerTextColor, int headerTextSize, String query) {
        this.versionCode = versionCode;
        this.zzuI = anchorTextColor;
        this.backgroundColor = backgroundColor;
        this.zzuJ = backgroundGradientBottom;
        this.zzuK = backgroundGradientTop;
        this.zzuL = borderColor;
        this.zzuM = borderThickness;
        this.zzuN = borderType;
        this.zzuO = callButtonColor;
        this.zzuP = channels;
        this.zzuQ = descriptionTextColor;
        this.zzuR = fontFace;
        this.zzuS = headerTextColor;
        this.zzuT = headerTextSize;
        this.zzuU = query;
    }

    public SearchAdRequestParcel(SearchAdRequest searchAdRequest) {
        this.versionCode = 1;
        this.zzuI = searchAdRequest.getAnchorTextColor();
        this.backgroundColor = searchAdRequest.getBackgroundColor();
        this.zzuJ = searchAdRequest.getBackgroundGradientBottom();
        this.zzuK = searchAdRequest.getBackgroundGradientTop();
        this.zzuL = searchAdRequest.getBorderColor();
        this.zzuM = searchAdRequest.getBorderThickness();
        this.zzuN = searchAdRequest.getBorderType();
        this.zzuO = searchAdRequest.getCallButtonColor();
        this.zzuP = searchAdRequest.getCustomChannels();
        this.zzuQ = searchAdRequest.getDescriptionTextColor();
        this.zzuR = searchAdRequest.getFontFace();
        this.zzuS = searchAdRequest.getHeaderTextColor();
        this.zzuT = searchAdRequest.getHeaderTextSize();
        this.zzuU = searchAdRequest.getQuery();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        zzae.zza(this, out, flags);
    }
}
