package com.google.android.gms.internal;

import android.content.Context;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

@zzha
/* loaded from: classes.dex */
public final class zzbz {
    public static final zzbv<String> zzvf = zzbv.zzc(0, "gads:sdk_core_experiment_id");
    public static final zzbv<String> zzvg = zzbv.zza(0, "gads:sdk_core_location", "https://googleads.g.doubleclick.net/mads/static/mad/sdk/native/sdk-core-v40.html");
    public static final zzbv<Boolean> zzvh = zzbv.zza(0, "gads:request_builder:singleton_webview", (Boolean) false);
    public static final zzbv<String> zzvi = zzbv.zzc(0, "gads:request_builder:singleton_webview_experiment_id");
    public static final zzbv<Boolean> zzvj = zzbv.zza(0, "gads:sdk_use_dynamic_module", (Boolean) false);
    public static final zzbv<String> zzvk = zzbv.zzc(0, "gads:sdk_use_dynamic_module_experiment_id");
    public static final zzbv<Boolean> zzvl = zzbv.zza(0, "gads:sdk_crash_report_enabled", (Boolean) false);
    public static final zzbv<Boolean> zzvm = zzbv.zza(0, "gads:sdk_crash_report_full_stacktrace", (Boolean) false);
    public static final zzbv<Boolean> zzvn = zzbv.zza(0, "gads:block_autoclicks", (Boolean) false);
    public static final zzbv<String> zzvo = zzbv.zzc(0, "gads:block_autoclicks_experiment_id");
    public static final zzbv<String> zzvp = zzbv.zzd(0, "gads:prefetch:experiment_id");
    public static final zzbv<String> zzvq = zzbv.zzc(0, "gads:spam_app_context:experiment_id");
    public static final zzbv<Boolean> zzvr = zzbv.zza(0, "gads:spam_app_context:enabled", (Boolean) false);
    public static final zzbv<String> zzvs = zzbv.zzc(0, "gads:video_stream_cache:experiment_id");
    public static final zzbv<Integer> zzvt = zzbv.zza(0, "gads:video_stream_cache:limit_count", 5);
    public static final zzbv<Integer> zzvu = zzbv.zza(0, "gads:video_stream_cache:limit_space", 8388608);
    public static final zzbv<Integer> zzvv = zzbv.zza(0, "gads:video_stream_exo_cache:buffer_size", 8388608);
    public static final zzbv<Long> zzvw = zzbv.zza(0, "gads:video_stream_cache:limit_time_sec", 300L);
    public static final zzbv<Long> zzvx = zzbv.zza(0, "gads:video_stream_cache:notify_interval_millis", 1000L);
    public static final zzbv<Integer> zzvy = zzbv.zza(0, "gads:video_stream_cache:connect_timeout_millis", 10000);
    public static final zzbv<Boolean> zzvz = zzbv.zza(0, "gads:video:metric_reporting_enabled", (Boolean) false);
    public static final zzbv<String> zzvA = zzbv.zza(0, "gads:video:metric_frame_hash_times", "");
    public static final zzbv<Long> zzvB = zzbv.zza(0, "gads:video:metric_frame_hash_time_leniency", 500L);
    public static final zzbv<String> zzvC = zzbv.zzd(0, "gads:spam_ad_id_decorator:experiment_id");
    public static final zzbv<Boolean> zzvD = zzbv.zza(0, "gads:spam_ad_id_decorator:enabled", (Boolean) false);
    public static final zzbv<String> zzvE = zzbv.zzd(0, "gads:looper_for_gms_client:experiment_id");
    public static final zzbv<Boolean> zzvF = zzbv.zza(0, "gads:looper_for_gms_client:enabled", (Boolean) true);
    public static final zzbv<Boolean> zzvG = zzbv.zza(0, "gads:sw_ad_request_service:enabled", (Boolean) true);
    public static final zzbv<Boolean> zzvH = zzbv.zza(0, "gads:sw_dynamite:enabled", (Boolean) true);
    public static final zzbv<String> zzvI = zzbv.zza(0, "gad:mraid:url_banner", "https://googleads.g.doubleclick.net/mads/static/mad/sdk/native/mraid/v2/mraid_app_banner.js");
    public static final zzbv<String> zzvJ = zzbv.zza(0, "gad:mraid:url_expanded_banner", "https://googleads.g.doubleclick.net/mads/static/mad/sdk/native/mraid/v2/mraid_app_expanded_banner.js");
    public static final zzbv<String> zzvK = zzbv.zza(0, "gad:mraid:url_interstitial", "https://googleads.g.doubleclick.net/mads/static/mad/sdk/native/mraid/v2/mraid_app_interstitial.js");
    public static final zzbv<Boolean> zzvL = zzbv.zza(0, "gads:enabled_sdk_csi", (Boolean) false);
    public static final zzbv<String> zzvM = zzbv.zza(0, "gads:sdk_csi_server", "https://csi.gstatic.com/csi");
    public static final zzbv<Boolean> zzvN = zzbv.zza(0, "gads:sdk_csi_write_to_file", (Boolean) false);
    public static final zzbv<Boolean> zzvO = zzbv.zza(0, "gads:enable_content_fetching", (Boolean) true);
    public static final zzbv<Integer> zzvP = zzbv.zza(0, "gads:content_length_weight", 1);
    public static final zzbv<Integer> zzvQ = zzbv.zza(0, "gads:content_age_weight", 1);
    public static final zzbv<Integer> zzvR = zzbv.zza(0, "gads:min_content_len", 11);
    public static final zzbv<Integer> zzvS = zzbv.zza(0, "gads:fingerprint_number", 10);
    public static final zzbv<Integer> zzvT = zzbv.zza(0, "gads:sleep_sec", 10);
    public static final zzbv<Boolean> zzvU = zzbv.zza(0, "gad:app_index_enabled", (Boolean) true);
    public static final zzbv<Boolean> zzvV = zzbv.zza(0, "gads:app_index:without_content_info_present:enabled", (Boolean) true);
    public static final zzbv<Long> zzvW = zzbv.zza(0, "gads:app_index:timeout_ms", 1000L);
    public static final zzbv<String> zzvX = zzbv.zzc(0, "gads:app_index:experiment_id");
    public static final zzbv<String> zzvY = zzbv.zzc(0, "gads:kitkat_interstitial_workaround:experiment_id");
    public static final zzbv<Boolean> zzvZ = zzbv.zza(0, "gads:kitkat_interstitial_workaround:enabled", (Boolean) true);
    public static final zzbv<Boolean> zzwa = zzbv.zza(0, "gads:interstitial_follow_url", (Boolean) true);
    public static final zzbv<Boolean> zzwb = zzbv.zza(0, "gads:interstitial_follow_url:register_click", (Boolean) true);
    public static final zzbv<String> zzwc = zzbv.zzc(0, "gads:interstitial_follow_url:experiment_id");
    public static final zzbv<Boolean> zzwd = zzbv.zza(0, "gads:analytics_enabled", (Boolean) true);
    public static final zzbv<Boolean> zzwe = zzbv.zza(0, "gads:ad_key_enabled", (Boolean) false);
    public static final zzbv<Integer> zzwf = zzbv.zza(0, "gads:webview_cache_version", 0);
    public static final zzbv<String> zzwg = zzbv.zzd(0, "gads:pan:experiment_id");
    public static final zzbv<String> zzwh = zzbv.zza(0, "gads:native:engine_url", "//googleads.g.doubleclick.net/mads/static/mad/sdk/native/native_ads.html");
    public static final zzbv<Boolean> zzwi = zzbv.zza(0, "gads:ad_manager_creator:enabled", (Boolean) true);
    public static final zzbv<Boolean> zzwj = zzbv.zza(1, "gads:interstitial_ad_pool:enabled", (Boolean) false);
    public static final zzbv<String> zzwk = zzbv.zza(1, "gads:interstitial_ad_pool:schema", "customTargeting");
    public static final zzbv<Integer> zzwl = zzbv.zza(1, "gads:interstitial_ad_pool:max_pools", 3);
    public static final zzbv<Integer> zzwm = zzbv.zza(1, "gads:interstitial_ad_pool:max_pool_depth", 2);
    public static final zzbv<Integer> zzwn = zzbv.zza(1, "gads:interstitial_ad_pool:time_limit_sec", 1200);
    public static final zzbv<String> zzwo = zzbv.zzc(1, "gads:interstitial_ad_pool:experiment_id");
    public static final zzbv<Boolean> zzwp = zzbv.zza(0, "gads:log:verbose_enabled", (Boolean) false);
    public static final zzbv<Boolean> zzwq = zzbv.zza(0, "gads:device_info_caching:enabled", (Boolean) true);
    public static final zzbv<Long> zzwr = zzbv.zza(0, "gads:device_info_caching_expiry_ms:expiry", 300000L);
    public static final zzbv<Boolean> zzws = zzbv.zza(0, "gads:gen204_signals:enabled", (Boolean) false);
    public static final zzbv<Boolean> zzwt = zzbv.zza(0, "gads:webview:error_reporting_enabled", (Boolean) false);
    public static final zzbv<Boolean> zzwu = zzbv.zza(0, "gads:adid_reporting:enabled", (Boolean) false);
    public static final zzbv<Boolean> zzwv = zzbv.zza(0, "gads:ad_settings_page_reporting:enabled", (Boolean) false);
    public static final zzbv<Boolean> zzww = zzbv.zza(0, "gads:adid_info_gmscore_upgrade_reporting:enabled", (Boolean) false);
    public static final zzbv<Boolean> zzwx = zzbv.zza(0, "gads:request_pkg:enabled", (Boolean) true);
    public static final zzbv<Boolean> zzwy = zzbv.zza(0, "gads:gmsg:disable_back_button:enabled", (Boolean) false);
    public static final zzbv<Long> zzwz = zzbv.zza(0, "gads:network:cache_prediction_duration_s", 300L);
    public static final zzbv<Boolean> zzwA = zzbv.zza(0, "gads:mediation:dynamite_first", (Boolean) true);
    public static final zzbv<Long> zzwB = zzbv.zza(0, "gads:ad_loader:timeout_ms", 60000L);
    public static final zzbv<Long> zzwC = zzbv.zza(0, "gads:rendering:timeout_ms", 60000L);
    public static final zzbv<Boolean> zzwD = zzbv.zza(0, "gads:adshield:enable_adshield_instrumentation", (Boolean) false);
    public static final zzbv<Boolean> zzwE = zzbv.zza(0, "gass:enabled", (Boolean) false);
    public static final zzbv<Boolean> zzwF = zzbv.zza(0, "gass:enable_int_signal", (Boolean) true);
    public static final zzbv<Boolean> zzwG = zzbv.zza(0, "gads:adid_notification:first_party_check:enabled", (Boolean) true);
    public static final zzbv<Boolean> zzwH = zzbv.zza(0, "gads:edu_device_helper:enabled", (Boolean) true);
    public static final zzbv<Boolean> zzwI = zzbv.zza(0, "gads:support_screen_shot", (Boolean) true);
    public static final zzbv<Long> zzwJ = zzbv.zza(0, "gads:js_flags:update_interval", TimeUnit.HOURS.toMillis(12));

    public static void initialize(final Context context) {
        zziz.zzb(new Callable<Void>() { // from class: com.google.android.gms.internal.zzbz.1
            @Override // java.util.concurrent.Callable
            /* renamed from: zzdm, reason: merged with bridge method [inline-methods] */
            public Void call() {
                com.google.android.gms.ads.internal.zzp.zzbG().initialize(context);
                return null;
            }
        });
    }

    public static List<String> zzdl() {
        return com.google.android.gms.ads.internal.zzp.zzbF().zzdl();
    }
}
