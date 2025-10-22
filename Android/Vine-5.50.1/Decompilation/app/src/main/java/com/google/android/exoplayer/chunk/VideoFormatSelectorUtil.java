package com.google.android.exoplayer.chunk;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;
import com.google.android.exoplayer.MediaCodecUtil;
import com.google.android.exoplayer.util.MimeTypes;
import com.google.android.exoplayer.util.Util;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public final class VideoFormatSelectorUtil {
    public static int[] selectVideoFormatsForDefaultDisplay(Context context, List<? extends FormatWrapper> formatWrappers, String[] allowedContainerMimeTypes, boolean filterHdFormats) throws MediaCodecUtil.DecoderQueryException {
        Point viewportSize = getViewportSize(context);
        return selectVideoFormats(formatWrappers, allowedContainerMimeTypes, filterHdFormats, true, viewportSize.x, viewportSize.y);
    }

    public static int[] selectVideoFormats(List<? extends FormatWrapper> formatWrappers, String[] allowedContainerMimeTypes, boolean filterHdFormats, boolean orientationMayChange, int viewportWidth, int viewportHeight) throws MediaCodecUtil.DecoderQueryException {
        int maxVideoPixelsToRetain = Integer.MAX_VALUE;
        ArrayList<Integer> selectedIndexList = new ArrayList<>();
        int maxDecodableFrameSize = MediaCodecUtil.maxH264DecodableFrameSize();
        int formatWrapperCount = formatWrappers.size();
        for (int i = 0; i < formatWrapperCount; i++) {
            Format format = formatWrappers.get(i).getFormat();
            if (isFormatPlayable(format, allowedContainerMimeTypes, filterHdFormats, maxDecodableFrameSize)) {
                selectedIndexList.add(Integer.valueOf(i));
                if (format.width > 0 && format.height > 0 && viewportWidth > 0 && viewportHeight > 0) {
                    Point maxVideoSizeInViewport = getMaxVideoSizeInViewport(orientationMayChange, viewportWidth, viewportHeight, format.width, format.height);
                    int videoPixels = format.width * format.height;
                    if (format.width >= ((int) (maxVideoSizeInViewport.x * 0.98f)) && format.height >= ((int) (maxVideoSizeInViewport.y * 0.98f)) && videoPixels < maxVideoPixelsToRetain) {
                        maxVideoPixelsToRetain = videoPixels;
                    }
                }
            }
        }
        if (maxVideoPixelsToRetain != Integer.MAX_VALUE) {
            for (int i2 = selectedIndexList.size() - 1; i2 >= 0; i2--) {
                Format format2 = formatWrappers.get(selectedIndexList.get(i2).intValue()).getFormat();
                if (format2.width > 0 && format2.height > 0 && format2.width * format2.height > maxVideoPixelsToRetain) {
                    selectedIndexList.remove(i2);
                }
            }
        }
        return Util.toArray(selectedIndexList);
    }

    private static boolean isFormatPlayable(Format format, String[] allowedContainerMimeTypes, boolean filterHdFormats, int maxDecodableFrameSize) throws MediaCodecUtil.DecoderQueryException {
        if (allowedContainerMimeTypes != null && !Util.contains(allowedContainerMimeTypes, format.mimeType)) {
            return false;
        }
        if (filterHdFormats && (format.width >= 1280 || format.height >= 720)) {
            return false;
        }
        if (format.width > 0 && format.height > 0) {
            if (Util.SDK_INT >= 21) {
                String videoMediaMimeType = MimeTypes.getVideoMediaMimeType(format.codecs);
                if ("video/x-unknown".equals(videoMediaMimeType)) {
                    videoMediaMimeType = "video/avc";
                }
                if (format.frameRate > 0.0f) {
                    return MediaCodecUtil.isSizeAndRateSupportedV21(videoMediaMimeType, false, format.width, format.height, format.frameRate);
                }
                return MediaCodecUtil.isSizeSupportedV21(videoMediaMimeType, false, format.width, format.height);
            }
            if (format.width * format.height > maxDecodableFrameSize) {
                return false;
            }
        }
        return true;
    }

    private static Point getMaxVideoSizeInViewport(boolean orientationMayChange, int viewportWidth, int viewportHeight, int videoWidth, int videoHeight) {
        if (orientationMayChange) {
            if ((videoWidth > videoHeight) != (viewportWidth > viewportHeight)) {
                viewportWidth = viewportHeight;
                viewportHeight = viewportWidth;
            }
        }
        if (videoWidth * viewportHeight >= videoHeight * viewportWidth) {
            return new Point(viewportWidth, Util.ceilDivide(viewportWidth * videoHeight, videoWidth));
        }
        return new Point(Util.ceilDivide(viewportHeight * videoWidth, videoHeight), viewportHeight);
    }

    private static Point getViewportSize(Context context) {
        if (Util.SDK_INT < 23 && Util.MODEL != null && Util.MODEL.startsWith("BRAVIA") && context.getPackageManager().hasSystemFeature("com.sony.dtv.hardware.panel.qfhd")) {
            return new Point(3840, 2160);
        }
        WindowManager windowManager = (WindowManager) context.getSystemService("window");
        return getDisplaySize(windowManager.getDefaultDisplay());
    }

    private static Point getDisplaySize(Display display) {
        Point displaySize = new Point();
        if (Util.SDK_INT >= 23) {
            getDisplaySizeV23(display, displaySize);
        } else if (Util.SDK_INT >= 17) {
            getDisplaySizeV17(display, displaySize);
        } else if (Util.SDK_INT >= 16) {
            getDisplaySizeV16(display, displaySize);
        } else {
            getDisplaySizeV9(display, displaySize);
        }
        return displaySize;
    }

    @TargetApi(23)
    private static void getDisplaySizeV23(Display display, Point outSize) {
        Display.Mode mode = display.getMode();
        outSize.x = mode.getPhysicalWidth();
        outSize.y = mode.getPhysicalHeight();
    }

    @TargetApi(17)
    private static void getDisplaySizeV17(Display display, Point outSize) {
        display.getRealSize(outSize);
    }

    @TargetApi(16)
    private static void getDisplaySizeV16(Display display, Point outSize) {
        display.getSize(outSize);
    }

    private static void getDisplaySizeV9(Display display, Point outSize) {
        outSize.x = display.getWidth();
        outSize.y = display.getHeight();
    }
}
