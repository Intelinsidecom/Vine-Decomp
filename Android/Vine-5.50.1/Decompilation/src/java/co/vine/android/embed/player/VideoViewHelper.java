package co.vine.android.embed.player;

import android.os.Build;
import android.view.View;
import co.vine.android.embed.player.VideoViewInterface;
import co.vine.android.player.SdkVideoView;

/* loaded from: classes.dex */
public final class VideoViewHelper {
    private static VideoViewInterface.SilentExceptionHandler sDefaultCrashHandler;
    private static OnUseVinePlayerFlagChangedListener sFlagChangedListener;
    private static boolean sGlobalUseVineVideoView;

    public interface OnUseVinePlayerFlagChangedListener {
        void onUseVinePlayerFlagChanged(boolean z);
    }

    public static void setOnUseVinePlayerFlagChangedListener(OnUseVinePlayerFlagChangedListener listener) {
        sFlagChangedListener = listener;
    }

    public static void setDefaultCrashHandler(VideoViewInterface.SilentExceptionHandler crashHandler) {
        sDefaultCrashHandler = crashHandler;
    }

    public static void setGlobalUseVineVideoView(boolean useVineVideoView) {
        if (useVineVideoView && Build.VERSION.SDK_INT < 18) {
            throw new UnsupportedOperationException("Sdk version is too low.");
        }
        sGlobalUseVineVideoView = useVineVideoView;
        OnUseVinePlayerFlagChangedListener listener = sFlagChangedListener;
        if (listener != null) {
            listener.onUseVinePlayerFlagChanged(useVineVideoView);
        }
    }

    public static boolean getGlobalUseVineVideoView() {
        return sGlobalUseVineVideoView;
    }

    public static boolean isDeviceWhiteListed() {
        return WhitelistedDevices.isCurrentDeviceWhiteListed(Build.MODEL, Build.VERSION.SDK_INT);
    }

    public static VideoViewInterface useDefaultVideoView(View view, int sdkVideoView, int vineVideoView) {
        if (getGlobalUseVineVideoView()) {
            return useVineVideoView(view, sdkVideoView, vineVideoView);
        }
        return useSdkVideoView(view, sdkVideoView, sdkVideoView);
    }

    public static VineVideoView useVineVideoView(View view, int sdkVideoViewRes, int vineVideoViewRes) {
        View sdkVideoView = view.findViewById(sdkVideoViewRes);
        if (sdkVideoView != null) {
            sdkVideoView.setVisibility(8);
        }
        VineVideoView vineVideoView = (VineVideoView) view.findViewById(vineVideoViewRes);
        if (vineVideoView.getVisibility() != 0) {
            vineVideoView.setVisibility(0);
        }
        vineVideoView.setSilentExceptionHandler(sDefaultCrashHandler);
        return vineVideoView;
    }

    public static SdkVideoView useSdkVideoView(View view, int sdkVideoViewRes, int videoViewRes) {
        View vineVideoView = view.findViewById(videoViewRes);
        if (vineVideoView != null) {
            vineVideoView.setVisibility(8);
        }
        SdkVideoView sdkVideoView = (SdkVideoView) view.findViewById(sdkVideoViewRes);
        if (sdkVideoView.getVisibility() != 0) {
            sdkVideoView.setVisibility(0);
        }
        sdkVideoView.setSilentExceptionHandler(sDefaultCrashHandler);
        return sdkVideoView;
    }
}
