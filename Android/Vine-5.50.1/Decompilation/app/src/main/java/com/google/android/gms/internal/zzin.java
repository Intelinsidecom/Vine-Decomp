package com.google.android.gms.internal;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import java.util.concurrent.Future;

@zzha
/* loaded from: classes.dex */
public final class zzin {

    private static abstract class zza extends zzil {
        private zza() {
        }

        @Override // com.google.android.gms.internal.zzil
        public void onStop() {
        }
    }

    public interface zzb {
        void zze(Bundle bundle);
    }

    public static Future zza(final Context context, final int i) {
        return new zza() { // from class: com.google.android.gms.internal.zzin.3
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super();
            }

            @Override // com.google.android.gms.internal.zzil
            public void zzbp() {
                SharedPreferences.Editor editorEdit = zzin.zzw(context).edit();
                editorEdit.putInt("webview_cache_version", i);
                editorEdit.apply();
            }
        }.zzfR();
    }

    public static Future zza(final Context context, final zzb zzbVar) {
        return new zza() { // from class: com.google.android.gms.internal.zzin.2
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super();
            }

            @Override // com.google.android.gms.internal.zzil
            public void zzbp() {
                SharedPreferences sharedPreferencesZzw = zzin.zzw(context);
                Bundle bundle = new Bundle();
                bundle.putBoolean("use_https", sharedPreferencesZzw.getBoolean("use_https", true));
                if (zzbVar != null) {
                    zzbVar.zze(bundle);
                }
            }
        }.zzfR();
    }

    public static Future zza(final Context context, final boolean z) {
        return new zza() { // from class: com.google.android.gms.internal.zzin.1
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super();
            }

            @Override // com.google.android.gms.internal.zzil
            public void zzbp() {
                SharedPreferences.Editor editorEdit = zzin.zzw(context).edit();
                editorEdit.putBoolean("use_https", z);
                editorEdit.apply();
            }
        }.zzfR();
    }

    public static Future zzb(final Context context, final zzb zzbVar) {
        return new zza() { // from class: com.google.android.gms.internal.zzin.4
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super();
            }

            @Override // com.google.android.gms.internal.zzil
            public void zzbp() {
                SharedPreferences sharedPreferencesZzw = zzin.zzw(context);
                Bundle bundle = new Bundle();
                bundle.putInt("webview_cache_version", sharedPreferencesZzw.getInt("webview_cache_version", 0));
                if (zzbVar != null) {
                    zzbVar.zze(bundle);
                }
            }
        }.zzfR();
    }

    public static Future zzb(final Context context, final boolean z) {
        return new zza() { // from class: com.google.android.gms.internal.zzin.5
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super();
            }

            @Override // com.google.android.gms.internal.zzil
            public void zzbp() {
                SharedPreferences.Editor editorEdit = zzin.zzw(context).edit();
                editorEdit.putBoolean("content_url_opted_out", z);
                editorEdit.apply();
            }
        }.zzfR();
    }

    public static Future zzc(final Context context, final zzb zzbVar) {
        return new zza() { // from class: com.google.android.gms.internal.zzin.6
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super();
            }

            @Override // com.google.android.gms.internal.zzil
            public void zzbp() {
                SharedPreferences sharedPreferencesZzw = zzin.zzw(context);
                Bundle bundle = new Bundle();
                bundle.putBoolean("content_url_opted_out", sharedPreferencesZzw.getBoolean("content_url_opted_out", true));
                if (zzbVar != null) {
                    zzbVar.zze(bundle);
                }
            }
        }.zzfR();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static SharedPreferences zzw(Context context) {
        return context.getSharedPreferences("admob", 0);
    }
}
