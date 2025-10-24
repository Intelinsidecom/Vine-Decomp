package com.google.android.gms.common.server.response;

import android.os.Bundle;
import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.common.server.response.FastJsonResponse;
import com.google.android.gms.internal.zznh;
import com.google.android.gms.internal.zzni;
import com.google.android.gms.internal.zznu;
import com.google.android.gms.internal.zznv;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/* loaded from: classes2.dex */
public class SafeParcelResponse extends FastJsonResponse implements SafeParcelable {
    public static final zze CREATOR = new zze();
    private final String mClassName;
    private final int mVersionCode;
    private final FieldMappingDictionary zzalc;
    private final Parcel zzalj;
    private final int zzalk = 2;
    private int zzall;
    private int zzalm;

    SafeParcelResponse(int versionCode, Parcel parcel, FieldMappingDictionary fieldMappingDictionary) {
        this.mVersionCode = versionCode;
        this.zzalj = (Parcel) zzx.zzy(parcel);
        this.zzalc = fieldMappingDictionary;
        if (this.zzalc == null) {
            this.mClassName = null;
        } else {
            this.mClassName = this.zzalc.zzrl();
        }
        this.zzall = 2;
    }

    private static HashMap<Integer, Map.Entry<String, FastJsonResponse.Field<?, ?>>> zzG(Map<String, FastJsonResponse.Field<?, ?>> map) {
        HashMap<Integer, Map.Entry<String, FastJsonResponse.Field<?, ?>>> map2 = new HashMap<>();
        for (Map.Entry<String, FastJsonResponse.Field<?, ?>> entry : map.entrySet()) {
            map2.put(Integer.valueOf(entry.getValue().zzrc()), entry);
        }
        return map2;
    }

    private void zza(StringBuilder sb, int i, Object obj) {
        switch (i) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                sb.append(obj);
                return;
            case 7:
                sb.append("\"").append(zznu.zzcO(obj.toString())).append("\"");
                return;
            case 8:
                sb.append("\"").append(zzni.zzj((byte[]) obj)).append("\"");
                return;
            case 9:
                sb.append("\"").append(zzni.zzk((byte[]) obj));
                sb.append("\"");
                return;
            case 10:
                zznv.zza(sb, (HashMap) obj);
                return;
            case 11:
                throw new IllegalArgumentException("Method does not accept concrete type.");
            default:
                throw new IllegalArgumentException("Unknown type = " + i);
        }
    }

    private void zza(StringBuilder sb, FastJsonResponse.Field<?, ?> field, Parcel parcel, int i) {
        switch (field.zzqU()) {
            case 0:
                zzb(sb, field, zza(field, Integer.valueOf(com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, i))));
                return;
            case 1:
                zzb(sb, field, zza(field, com.google.android.gms.common.internal.safeparcel.zza.zzk(parcel, i)));
                return;
            case 2:
                zzb(sb, field, zza(field, Long.valueOf(com.google.android.gms.common.internal.safeparcel.zza.zzi(parcel, i))));
                return;
            case 3:
                zzb(sb, field, zza(field, Float.valueOf(com.google.android.gms.common.internal.safeparcel.zza.zzl(parcel, i))));
                return;
            case 4:
                zzb(sb, field, zza(field, Double.valueOf(com.google.android.gms.common.internal.safeparcel.zza.zzn(parcel, i))));
                return;
            case 5:
                zzb(sb, field, zza(field, com.google.android.gms.common.internal.safeparcel.zza.zzo(parcel, i)));
                return;
            case 6:
                zzb(sb, field, zza(field, Boolean.valueOf(com.google.android.gms.common.internal.safeparcel.zza.zzc(parcel, i))));
                return;
            case 7:
                zzb(sb, field, zza(field, com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, i)));
                return;
            case 8:
            case 9:
                zzb(sb, field, zza(field, com.google.android.gms.common.internal.safeparcel.zza.zzs(parcel, i)));
                return;
            case 10:
                zzb(sb, field, zza(field, zzl(com.google.android.gms.common.internal.safeparcel.zza.zzr(parcel, i))));
                return;
            case 11:
                throw new IllegalArgumentException("Method does not accept concrete type.");
            default:
                throw new IllegalArgumentException("Unknown field out type = " + field.zzqU());
        }
    }

    private void zza(StringBuilder sb, String str, FastJsonResponse.Field<?, ?> field, Parcel parcel, int i) {
        sb.append("\"").append(str).append("\":");
        if (field.zzrf()) {
            zza(sb, field, parcel, i);
        } else {
            zzb(sb, field, parcel, i);
        }
    }

    private void zza(StringBuilder sb, Map<String, FastJsonResponse.Field<?, ?>> map, Parcel parcel) {
        HashMap<Integer, Map.Entry<String, FastJsonResponse.Field<?, ?>>> mapZzG = zzG(map);
        sb.append('{');
        int iZzau = com.google.android.gms.common.internal.safeparcel.zza.zzau(parcel);
        boolean z = false;
        while (parcel.dataPosition() < iZzau) {
            int iZzat = com.google.android.gms.common.internal.safeparcel.zza.zzat(parcel);
            Map.Entry<String, FastJsonResponse.Field<?, ?>> entry = mapZzG.get(Integer.valueOf(com.google.android.gms.common.internal.safeparcel.zza.zzcc(iZzat)));
            if (entry != null) {
                if (z) {
                    sb.append(",");
                }
                zza(sb, entry.getKey(), entry.getValue(), parcel, iZzat);
                z = true;
            }
        }
        if (parcel.dataPosition() != iZzau) {
            throw new zza.C0034zza("Overread allowed size end=" + iZzau, parcel);
        }
        sb.append('}');
    }

    private void zzb(StringBuilder sb, FastJsonResponse.Field<?, ?> field, Parcel parcel, int i) {
        if (field.zzra()) {
            sb.append("[");
            switch (field.zzqU()) {
                case 0:
                    zznh.zza(sb, com.google.android.gms.common.internal.safeparcel.zza.zzv(parcel, i));
                    break;
                case 1:
                    zznh.zza(sb, com.google.android.gms.common.internal.safeparcel.zza.zzx(parcel, i));
                    break;
                case 2:
                    zznh.zza(sb, com.google.android.gms.common.internal.safeparcel.zza.zzw(parcel, i));
                    break;
                case 3:
                    zznh.zza(sb, com.google.android.gms.common.internal.safeparcel.zza.zzy(parcel, i));
                    break;
                case 4:
                    zznh.zza(sb, com.google.android.gms.common.internal.safeparcel.zza.zzz(parcel, i));
                    break;
                case 5:
                    zznh.zza(sb, com.google.android.gms.common.internal.safeparcel.zza.zzA(parcel, i));
                    break;
                case 6:
                    zznh.zza(sb, com.google.android.gms.common.internal.safeparcel.zza.zzu(parcel, i));
                    break;
                case 7:
                    zznh.zza(sb, com.google.android.gms.common.internal.safeparcel.zza.zzB(parcel, i));
                    break;
                case 8:
                case 9:
                case 10:
                    throw new UnsupportedOperationException("List of type BASE64, BASE64_URL_SAFE, or STRING_MAP is not supported");
                case 11:
                    Parcel[] parcelArrZzF = com.google.android.gms.common.internal.safeparcel.zza.zzF(parcel, i);
                    int length = parcelArrZzF.length;
                    for (int i2 = 0; i2 < length; i2++) {
                        if (i2 > 0) {
                            sb.append(",");
                        }
                        parcelArrZzF[i2].setDataPosition(0);
                        zza(sb, field.zzrh(), parcelArrZzF[i2]);
                    }
                    break;
                default:
                    throw new IllegalStateException("Unknown field type out.");
            }
            sb.append("]");
            return;
        }
        switch (field.zzqU()) {
            case 0:
                sb.append(com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, i));
                return;
            case 1:
                sb.append(com.google.android.gms.common.internal.safeparcel.zza.zzk(parcel, i));
                return;
            case 2:
                sb.append(com.google.android.gms.common.internal.safeparcel.zza.zzi(parcel, i));
                return;
            case 3:
                sb.append(com.google.android.gms.common.internal.safeparcel.zza.zzl(parcel, i));
                return;
            case 4:
                sb.append(com.google.android.gms.common.internal.safeparcel.zza.zzn(parcel, i));
                return;
            case 5:
                sb.append(com.google.android.gms.common.internal.safeparcel.zza.zzo(parcel, i));
                return;
            case 6:
                sb.append(com.google.android.gms.common.internal.safeparcel.zza.zzc(parcel, i));
                return;
            case 7:
                sb.append("\"").append(zznu.zzcO(com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, i))).append("\"");
                return;
            case 8:
                sb.append("\"").append(zzni.zzj(com.google.android.gms.common.internal.safeparcel.zza.zzs(parcel, i))).append("\"");
                return;
            case 9:
                sb.append("\"").append(zzni.zzk(com.google.android.gms.common.internal.safeparcel.zza.zzs(parcel, i)));
                sb.append("\"");
                return;
            case 10:
                Bundle bundleZzr = com.google.android.gms.common.internal.safeparcel.zza.zzr(parcel, i);
                Set<String> setKeySet = bundleZzr.keySet();
                setKeySet.size();
                sb.append("{");
                boolean z = true;
                for (String str : setKeySet) {
                    if (!z) {
                        sb.append(",");
                    }
                    sb.append("\"").append(str).append("\"");
                    sb.append(":");
                    sb.append("\"").append(zznu.zzcO(bundleZzr.getString(str))).append("\"");
                    z = false;
                }
                sb.append("}");
                return;
            case 11:
                Parcel parcelZzE = com.google.android.gms.common.internal.safeparcel.zza.zzE(parcel, i);
                parcelZzE.setDataPosition(0);
                zza(sb, field.zzrh(), parcelZzE);
                return;
            default:
                throw new IllegalStateException("Unknown field type out");
        }
    }

    private void zzb(StringBuilder sb, FastJsonResponse.Field<?, ?> field, Object obj) {
        if (field.zzqZ()) {
            zzb(sb, field, (ArrayList<?>) obj);
        } else {
            zza(sb, field.zzqT(), obj);
        }
    }

    private void zzb(StringBuilder sb, FastJsonResponse.Field<?, ?> field, ArrayList<?> arrayList) {
        sb.append("[");
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            if (i != 0) {
                sb.append(",");
            }
            zza(sb, field.zzqT(), arrayList.get(i));
        }
        sb.append("]");
    }

    public static HashMap<String, String> zzl(Bundle bundle) {
        HashMap<String, String> map = new HashMap<>();
        for (String str : bundle.keySet()) {
            map.put(str, bundle.getString(str));
        }
        return map;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        zze zzeVar = CREATOR;
        return 0;
    }

    public int getVersionCode() {
        return this.mVersionCode;
    }

    @Override // com.google.android.gms.common.server.response.FastJsonResponse
    public String toString() {
        zzx.zzb(this.zzalc, "Cannot convert to JSON on client side.");
        Parcel parcelZzrn = zzrn();
        parcelZzrn.setDataPosition(0);
        StringBuilder sb = new StringBuilder(100);
        zza(sb, this.zzalc.zzcL(this.mClassName), parcelZzrn);
        return sb.toString();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        zze zzeVar = CREATOR;
        zze.zza(this, out, flags);
    }

    @Override // com.google.android.gms.common.server.response.FastJsonResponse
    protected Object zzcH(String str) {
        throw new UnsupportedOperationException("Converting to JSON does not require this method.");
    }

    @Override // com.google.android.gms.common.server.response.FastJsonResponse
    protected boolean zzcI(String str) {
        throw new UnsupportedOperationException("Converting to JSON does not require this method.");
    }

    @Override // com.google.android.gms.common.server.response.FastJsonResponse
    public Map<String, FastJsonResponse.Field<?, ?>> zzqV() {
        if (this.zzalc == null) {
            return null;
        }
        return this.zzalc.zzcL(this.mClassName);
    }

    public Parcel zzrn() {
        switch (this.zzall) {
            case 0:
                this.zzalm = com.google.android.gms.common.internal.safeparcel.zzb.zzav(this.zzalj);
                com.google.android.gms.common.internal.safeparcel.zzb.zzI(this.zzalj, this.zzalm);
                this.zzall = 2;
                break;
            case 1:
                com.google.android.gms.common.internal.safeparcel.zzb.zzI(this.zzalj, this.zzalm);
                this.zzall = 2;
                break;
        }
        return this.zzalj;
    }

    FieldMappingDictionary zzro() {
        switch (this.zzalk) {
            case 0:
                return null;
            case 1:
                return this.zzalc;
            case 2:
                return this.zzalc;
            default:
                throw new IllegalStateException("Invalid creation type: " + this.zzalk);
        }
    }
}
