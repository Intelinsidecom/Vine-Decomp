package com.google.android.gms.internal;

import android.text.TextUtils;
import java.util.Map;

@zzha
/* loaded from: classes.dex */
public abstract class zzce {

    @zzha
    public static final zzce zzwU = new zzce() { // from class: com.google.android.gms.internal.zzce.1
        @Override // com.google.android.gms.internal.zzce
        public String zzc(String str, String str2) {
            return str2;
        }
    };

    @zzha
    public static final zzce zzwV = new zzce() { // from class: com.google.android.gms.internal.zzce.2
        @Override // com.google.android.gms.internal.zzce
        public String zzc(String str, String str2) {
            return str != null ? str : str2;
        }
    };

    @zzha
    public static final zzce zzwW = new zzce() { // from class: com.google.android.gms.internal.zzce.3
        private String zzQ(String str) {
            if (TextUtils.isEmpty(str)) {
                return str;
            }
            int i = 0;
            int length = str.length();
            while (i < str.length() && str.charAt(i) == ',') {
                i++;
            }
            while (length > 0 && str.charAt(length - 1) == ',') {
                length--;
            }
            return (i == 0 && length == str.length()) ? str : str.substring(i, length);
        }

        @Override // com.google.android.gms.internal.zzce
        public String zzc(String str, String str2) {
            String strZzQ = zzQ(str);
            String strZzQ2 = zzQ(str2);
            return TextUtils.isEmpty(strZzQ) ? strZzQ2 : TextUtils.isEmpty(strZzQ2) ? strZzQ : strZzQ + "," + strZzQ2;
        }
    };

    public final void zza(Map<String, String> map, String str, String str2) {
        map.put(str, zzc(map.get(str), str2));
    }

    public abstract String zzc(String str, String str2);
}
