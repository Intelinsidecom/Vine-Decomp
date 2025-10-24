package co.vine.android.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.v8.renderscript.RenderScript;
import co.vine.android.recorder.PictureConverter;
import co.vine.android.recorder.RecordConfigUtils;
import co.vine.android.recorder.SwVineFrameRecorder;
import co.vine.android.recorder.audio.AudioArray;
import co.vine.android.recorder.audio.AudioArrays;
import co.vine.android.util.CrashUtil;
import co.vine.android.util.CrossConstants;
import co.vine.android.util.SystemUtil;
import com.edisonwang.android.slog.SLog;
import com.googlecode.javacpp.BytePointer;
import com.googlecode.javacv.cpp.opencv_core;
import java.nio.ByteBuffer;

/* loaded from: classes.dex */
public class ResourceService extends Service {
    public static long lastActiveconversationRowId;
    private static AudioArray sAudioDataBufferByte;
    private static AudioArray sAudioDataBufferShort;
    private static int sBufferUserCountByte;
    private static int sBufferUserCountShort;
    private static ByteBuffer sFrameBuffer;
    private static opencv_core.IplImage sIplImage;
    private static PictureConverter sPictureConverter;
    private static Bitmap sPreviewBitmap;
    private static Canvas sPreviewCanvas;
    private static RenderScript sRS;
    private static Bitmap sThumbnailBitmap;
    private static Canvas sThumbnailBitmapCanvas;
    private static Matrix sThumbnailMatrix;
    private static SwVineFrameRecorder sVineRecorder;
    private final Messenger messenger = new Messenger(new CameraServiceHandler());
    private static final int[] sLOCK = new int[0];
    public static boolean IS_READY = false;
    public static boolean isConversationActive = false;

    public static void initVineRecorder(Context context) {
        synchronized (sLOCK) {
            if (sVineRecorder == null) {
                try {
                    if (!SwVineFrameRecorder.hasEverSuccessfullyLoaded) {
                        SwVineFrameRecorder.tryLoad(context);
                        RecordConfigUtils.setLoadWasEverSuccessful(context, true);
                    }
                } catch (Throwable e) {
                    try {
                        CrashUtil.logException(e, "Failed to load", new Object[0]);
                        RecordConfigUtils.setLoadWasEverSuccessful(context, false);
                    } catch (Throwable throwable) {
                        CrashUtil.logException(throwable);
                    }
                }
                sVineRecorder = new SwVineFrameRecorder("", 0, 0, 1, null, null);
            }
        }
    }

    public static Bitmap getThumbnailBitmap(Context context) {
        Bitmap bitmap;
        synchronized (sLOCK) {
            if (sThumbnailBitmap == null) {
                Point size = SystemUtil.getDisplaySize(context);
                sThumbnailBitmap = generateThumbnailBitmap(size);
            }
            bitmap = sThumbnailBitmap;
        }
        return bitmap;
    }

    public static RenderScript getRenderScript(Context context) {
        RenderScript renderScript;
        synchronized (sLOCK) {
            if (sRS == null) {
                sRS = RenderScript.create(context.getApplicationContext());
            }
            renderScript = sRS;
        }
        return renderScript;
    }

    public static Bitmap getPreviewBitmap() {
        Bitmap bitmap;
        synchronized (sLOCK) {
            if (sPreviewBitmap == null) {
                sPreviewBitmap = RecordConfigUtils.createDefaultSizeBitmap();
            }
            bitmap = sPreviewBitmap;
        }
        return bitmap;
    }

    public static opencv_core.IplImage getFrameImage() {
        opencv_core.IplImage iplImage;
        synchronized (sLOCK) {
            if (sIplImage == null) {
                sIplImage = opencv_core.IplImage.create(480, 480, 8, 4);
            }
            iplImage = sIplImage;
        }
        return iplImage;
    }

    public static Matrix getThumbnailMatrix(Context context) {
        Matrix matrix;
        synchronized (sLOCK) {
            if (sThumbnailMatrix == null) {
                sThumbnailMatrix = generateThumbnailMatrix(getThumbnailBitmap(context), getPreviewBitmap());
            }
            matrix = sThumbnailMatrix;
        }
        return matrix;
    }

    public static Canvas getThumbnailCanvas(Context context) {
        Canvas canvas;
        synchronized (sLOCK) {
            if (sThumbnailBitmapCanvas == null) {
                sThumbnailBitmapCanvas = new Canvas(getThumbnailBitmap(context));
            }
            canvas = sThumbnailBitmapCanvas;
        }
        return canvas;
    }

    public static Canvas getPreviewCanvas() {
        Canvas canvas;
        synchronized (sLOCK) {
            if (sPreviewCanvas == null) {
                sPreviewCanvas = new Canvas(getPreviewBitmap());
            }
            canvas = sPreviewCanvas;
        }
        return canvas;
    }

    public static AudioArray getAudioDataBuffer(AudioArrays.AudioArrayType type) {
        AudioArray audioArrayCreateAudioArray;
        synchronized (sLOCK) {
            if (type == AudioArrays.AudioArrayType.BYTE) {
                sBufferUserCountByte++;
                if (sAudioDataBufferByte == null) {
                    sAudioDataBufferByte = RecordConfigUtils.createAudioArray(type);
                }
                if (sBufferUserCountByte > 1) {
                    audioArrayCreateAudioArray = RecordConfigUtils.createAudioArray(type);
                } else {
                    audioArrayCreateAudioArray = sAudioDataBufferByte;
                }
            } else {
                sBufferUserCountShort++;
                if (sAudioDataBufferShort == null) {
                    sAudioDataBufferShort = RecordConfigUtils.createAudioArray(type);
                }
                if (sBufferUserCountShort > 1) {
                    audioArrayCreateAudioArray = RecordConfigUtils.createAudioArray(type);
                } else {
                    audioArrayCreateAudioArray = sAudioDataBufferShort;
                }
            }
        }
        return audioArrayCreateAudioArray;
    }

    public static ByteBuffer getFrameBuffer() {
        ByteBuffer byteBuffer;
        synchronized (sLOCK) {
            if (sFrameBuffer == null) {
                sFrameBuffer = new BytePointer(921600).asByteBuffer();
            }
            byteBuffer = sFrameBuffer;
        }
        return byteBuffer;
    }

    public static void releaseAudioBuffer(AudioArrays.AudioArrayType type) {
        synchronized (sLOCK) {
            if (type == AudioArrays.AudioArrayType.BYTE) {
                sBufferUserCountByte--;
                if (sBufferUserCountByte < 0) {
                    sBufferUserCountByte = 0;
                }
            } else {
                sBufferUserCountShort--;
                if (sBufferUserCountShort < 0) {
                    sBufferUserCountShort = 0;
                }
            }
        }
    }

    public static PictureConverter getPictureConverter() {
        return sPictureConverter;
    }

    public static void setPictureConverter(PictureConverter pictureConverter) {
        sPictureConverter = pictureConverter;
    }

    public class CameraServiceHandler extends Handler {
        public CameraServiceHandler() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 101:
                    Intent intent = new Intent(ResourceService.isConversationActive ? "co.vine.android.camera.request.conversation.state.on" : "co.vine.android.camera.request.conversation.state.off");
                    intent.putExtra("co.vine.android.camera.request.conversation.state.id", ResourceService.lastActiveconversationRowId);
                    ResourceService.this.sendBroadcast(intent, CrossConstants.BROADCAST_PERMISSION);
                    break;
                default:
                    SLog.e("Invalid request: " + msg.what);
                    break;
            }
        }
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return this.messenger.getBinder();
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
        if (sVineRecorder != null) {
            sVineRecorder.release();
            sVineRecorder = null;
        }
        SLog.d("Camera Service onDestroy..");
        IS_READY = false;
    }

    @Override // android.app.Service
    public void onCreate() {
        SLog.d("Camera Service onCreate start.");
        long start = System.currentTimeMillis();
        try {
            getThumbnailBitmap(this);
            getRenderScript(this);
            if (RecordConfigUtils.HW_ENABLED) {
                getFrameBuffer();
            }
            if (RecordConfigUtils.SW_ENABLED) {
                initVineRecorder(this);
                getFrameImage();
            }
            getPreviewBitmap();
            getThumbnailMatrix(this);
            getThumbnailCanvas(this);
            getPreviewCanvas();
        } catch (Throwable e) {
            CrashUtil.log("Failed to init camera service, it is ok.", e);
        }
        IS_READY = true;
        SLog.d("Camera Service took {}ms.", Long.valueOf(System.currentTimeMillis() - start));
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int flags, int startId) {
        return 1;
    }

    public static Matrix generateThumbnailMatrix(Bitmap thumbnailBitmap, Bitmap previewBitmap) {
        Matrix thumbnailMatrix = new Matrix();
        float thumbnailScale = (1.0f * thumbnailBitmap.getHeight()) / previewBitmap.getHeight();
        thumbnailMatrix.setScale(thumbnailScale, thumbnailScale);
        return thumbnailMatrix;
    }

    public static Bitmap generateThumbnailBitmap(Point size) {
        int thumbnailSize = size.x / 2;
        return Bitmap.createBitmap(thumbnailSize, thumbnailSize, RecordConfigUtils.DEFAULT_BITMAP_CONFIG);
    }
}
