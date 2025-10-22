package com.google.android.gms.common.server.response;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.common.server.converter.ConverterWrapper;
import com.google.android.gms.internal.zzni;
import com.google.android.gms.internal.zznu;
import com.google.android.gms.internal.zznv;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes2.dex */
public abstract class FastJsonResponse {

    public static class Field<I, O> implements SafeParcelable {
        public static final com.google.android.gms.common.server.response.zza CREATOR = new com.google.android.gms.common.server.response.zza();
        private final int mVersionCode;
        protected final int zzakU;
        protected final boolean zzakV;
        protected final int zzakW;
        protected final boolean zzakX;
        protected final String zzakY;
        protected final int zzakZ;
        protected final Class<? extends FastJsonResponse> zzala;
        protected final String zzalb;
        private FieldMappingDictionary zzalc;
        private zza<I, O> zzald;

        Field(int i, int i2, boolean z, int i3, boolean z2, String str, int i4, String str2, ConverterWrapper converterWrapper) {
            this.mVersionCode = i;
            this.zzakU = i2;
            this.zzakV = z;
            this.zzakW = i3;
            this.zzakX = z2;
            this.zzakY = str;
            this.zzakZ = i4;
            if (str2 == null) {
                this.zzala = null;
                this.zzalb = null;
            } else {
                this.zzala = SafeParcelResponse.class;
                this.zzalb = str2;
            }
            if (converterWrapper == null) {
                this.zzald = null;
            } else {
                this.zzald = (zza<I, O>) converterWrapper.zzqR();
            }
        }

        public I convertBack(O output) {
            return this.zzald.convertBack(output);
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            com.google.android.gms.common.server.response.zza zzaVar = CREATOR;
            return 0;
        }

        public int getVersionCode() {
            return this.mVersionCode;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Field\n");
            sb.append("            versionCode=").append(this.mVersionCode).append('\n');
            sb.append("                 typeIn=").append(this.zzakU).append('\n');
            sb.append("            typeInArray=").append(this.zzakV).append('\n');
            sb.append("                typeOut=").append(this.zzakW).append('\n');
            sb.append("           typeOutArray=").append(this.zzakX).append('\n');
            sb.append("        outputFieldName=").append(this.zzakY).append('\n');
            sb.append("      safeParcelFieldId=").append(this.zzakZ).append('\n');
            sb.append("       concreteTypeName=").append(zzre()).append('\n');
            if (zzrd() != null) {
                sb.append("     concreteType.class=").append(zzrd().getCanonicalName()).append('\n');
            }
            sb.append("          converterName=").append(this.zzald == null ? "null" : this.zzald.getClass().getCanonicalName()).append('\n');
            return sb.toString();
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel out, int flags) {
            com.google.android.gms.common.server.response.zza zzaVar = CREATOR;
            com.google.android.gms.common.server.response.zza.zza(this, out, flags);
        }

        public void zza(FieldMappingDictionary fieldMappingDictionary) {
            this.zzalc = fieldMappingDictionary;
        }

        public int zzqT() {
            return this.zzakU;
        }

        public int zzqU() {
            return this.zzakW;
        }

        public boolean zzqZ() {
            return this.zzakV;
        }

        public boolean zzra() {
            return this.zzakX;
        }

        public String zzrb() {
            return this.zzakY;
        }

        public int zzrc() {
            return this.zzakZ;
        }

        public Class<? extends FastJsonResponse> zzrd() {
            return this.zzala;
        }

        String zzre() {
            if (this.zzalb == null) {
                return null;
            }
            return this.zzalb;
        }

        public boolean zzrf() {
            return this.zzald != null;
        }

        ConverterWrapper zzrg() {
            if (this.zzald == null) {
                return null;
            }
            return ConverterWrapper.zza(this.zzald);
        }

        public Map<String, Field<?, ?>> zzrh() {
            zzx.zzy(this.zzalb);
            zzx.zzy(this.zzalc);
            return this.zzalc.zzcL(this.zzalb);
        }
    }

    public interface zza<I, O> {
        I convertBack(O o);
    }

    private void zza(StringBuilder sb, Field field, Object obj) {
        if (field.zzqT() == 11) {
            sb.append(field.zzrd().cast(obj).toString());
        } else {
            if (field.zzqT() != 7) {
                sb.append(obj);
                return;
            }
            sb.append("\"");
            sb.append(zznu.zzcO((String) obj));
            sb.append("\"");
        }
    }

    private void zza(StringBuilder sb, Field field, ArrayList<Object> arrayList) {
        sb.append("[");
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                sb.append(",");
            }
            Object obj = arrayList.get(i);
            if (obj != null) {
                zza(sb, field, obj);
            }
        }
        sb.append("]");
    }

    public String toString() {
        Map<String, Field<?, ?>> mapZzqV = zzqV();
        StringBuilder sb = new StringBuilder(100);
        for (String str : mapZzqV.keySet()) {
            Field<?, ?> field = mapZzqV.get(str);
            if (zza(field)) {
                Object objZza = zza(field, zzb(field));
                if (sb.length() == 0) {
                    sb.append("{");
                } else {
                    sb.append(",");
                }
                sb.append("\"").append(str).append("\":");
                if (objZza != null) {
                    switch (field.zzqU()) {
                        case 8:
                            sb.append("\"").append(zzni.zzj((byte[]) objZza)).append("\"");
                            break;
                        case 9:
                            sb.append("\"").append(zzni.zzk((byte[]) objZza)).append("\"");
                            break;
                        case 10:
                            zznv.zza(sb, (HashMap) objZza);
                            break;
                        default:
                            if (field.zzqZ()) {
                                zza(sb, (Field) field, (ArrayList<Object>) objZza);
                                break;
                            } else {
                                zza(sb, field, objZza);
                                break;
                            }
                    }
                } else {
                    sb.append("null");
                }
            }
        }
        if (sb.length() > 0) {
            sb.append("}");
        } else {
            sb.append("{}");
        }
        return sb.toString();
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected <O, I> I zza(Field<I, O> field, Object obj) {
        return ((Field) field).zzald != null ? field.convertBack(obj) : obj;
    }

    protected boolean zza(Field field) {
        return field.zzqU() == 11 ? field.zzra() ? zzcK(field.zzrb()) : zzcJ(field.zzrb()) : zzcI(field.zzrb());
    }

    protected Object zzb(Field field) {
        String strZzrb = field.zzrb();
        if (field.zzrd() == null) {
            return zzcH(field.zzrb());
        }
        zzx.zza(zzcH(field.zzrb()) == null, "Concrete field shouldn't be value object: %s", field.zzrb());
        HashMap<String, Object> mapZzqX = field.zzra() ? zzqX() : zzqW();
        if (mapZzqX != null) {
            return mapZzqX.get(strZzrb);
        }
        try {
            return getClass().getMethod("get" + Character.toUpperCase(strZzrb.charAt(0)) + strZzrb.substring(1), new Class[0]).invoke(this, new Object[0]);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract Object zzcH(String str);

    protected abstract boolean zzcI(String str);

    protected boolean zzcJ(String str) {
        throw new UnsupportedOperationException("Concrete types not supported");
    }

    protected boolean zzcK(String str) {
        throw new UnsupportedOperationException("Concrete type arrays not supported");
    }

    public abstract Map<String, Field<?, ?>> zzqV();

    public HashMap<String, Object> zzqW() {
        return null;
    }

    public HashMap<String, Object> zzqX() {
        return null;
    }
}
