package co.vine.android.recorder;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.AudioRecord;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import co.vine.android.recorder.audio.AudioArray;
import co.vine.android.recorder.audio.AudioArrays;
import co.vine.android.recorder.camera.CameraManager;
import co.vine.android.util.CrashUtil;
import co.vine.android.util.SystemUtil;
import com.edisonwang.android.slog.SLog;
import com.googlecode.javacv.cpp.avcodec;
import com.googlecode.javacv.cpp.avutil;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import org.apache.commons.io.FileUtils;

/* loaded from: classes.dex */
public class RecordConfigUtils {
    public static final int AUDIO_BIT_RATE;
    public static final int AUDIO_BUFFER_SIZE;
    public static final int AUDIO_RUNNABLE_SAMPLE_SIZE;
    public static final int AUDIO_WAIT_THRESHOLD_NS;
    private static String DCIM;
    public static final Bitmap.Config DEFAULT_BITMAP_CONFIG;
    public static final File FOLDER_ROOT_DIRECT_UPLOAD;
    private static final File FOLDER_ROOT_PROCESS_DEBUG;
    private static final String FOLDER_ROOT_SAVE;
    public static final boolean HW_ENABLED = "sw".toLowerCase().contains("hw");
    public static final boolean SW_ENABLED = "sw".toLowerCase().contains("sw");
    public static final int VIDEO_BIT_RATE;
    public static final String VIDEO_CONTAINER_EXT;
    private static RecordConfig sGenericConfig;

    static {
        VIDEO_BIT_RATE = RecordSessionManager.DEFAULT_VERSION.willEventuallyTranscoded ? 3500000 : avutil.AV_TIME_BASE;
        AUDIO_BIT_RATE = RecordSessionManager.DEFAULT_VERSION.willEventuallyTranscoded ? 128000 : 64000;
        AUDIO_BUFFER_SIZE = AudioRecord.getMinBufferSize(44100, 16, 2);
        AUDIO_RUNNABLE_SAMPLE_SIZE = AUDIO_BUFFER_SIZE > 0 ? AUDIO_BUFFER_SIZE : 7680;
        AUDIO_WAIT_THRESHOLD_NS = (AUDIO_RUNNABLE_SAMPLE_SIZE * 2000000000) / 44100;
        DEFAULT_BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
        VIDEO_CONTAINER_EXT = RecordSessionManager.DEFAULT_VERSION.videoOutputExtension;
        try {
            DCIM = Environment.DIRECTORY_DCIM;
        } catch (Exception e) {
            DCIM = "DCIM";
        }
        FOLDER_ROOT_SAVE = new File(Environment.getExternalStoragePublicDirectory(DCIM), "Vine").getPath();
        FOLDER_ROOT_PROCESS_DEBUG = new File(Environment.getExternalStoragePublicDirectory(DCIM), "vine_capture");
        FOLDER_ROOT_DIRECT_UPLOAD = new File(Environment.getExternalStoragePublicDirectory(DCIM), "vine_upload");
        if (SLog.sLogsOn) {
            FOLDER_ROOT_DIRECT_UPLOAD.mkdirs();
        }
    }

    public static RecordConfig getGenericConfig(Context context) {
        if (sGenericConfig == null) {
            sGenericConfig = new RecordConfig(context);
        }
        return sGenericConfig;
    }

    public static AudioArray createAudioArray(AudioArrays.AudioArrayType type) {
        return AudioArrays.newInstance(AUDIO_RUNNABLE_SAMPLE_SIZE, type);
    }

    public static boolean isFastEncoding(boolean hardwareEncoding) {
        return hardwareEncoding && Build.VERSION.SDK_INT >= 16;
    }

    public static class RecordConfig {
        public static final boolean DISABLE_FACE_RECOGNITION;
        public static final boolean FORCE_AUTO_FOCUS;
        public static final boolean IS_ZERO_INDEX_BASED_ZOOM;
        private static final boolean PROCESS_ON_SD_CARD;
        public static final boolean SHOULD_DISABLE_BACKFACING_SET_RECORDING_HINT;
        public static final boolean SHOULD_DISABLE_FRONTFACING_SET_RECORDING_HINT;
        public static final boolean SHOULD_FORCE_SMOOTH_ZOOM;
        public static final boolean SHOULD_SHOW_ZOOM_SLIDER;
        public final float backFacingAspectRatioOverride;
        public final boolean backFacingRecordingHint;
        public final int bufferCount;
        public final boolean cameraSwitchEnabled;
        public final boolean flashSwitchEnabled;
        public final float frontFacingAspectRatioOverride;
        public final boolean frontFacingRecordingHint;
        public final boolean highQuality;
        public final boolean isZoomButtonEnabled;
        public final int maxDuration;
        public final double memRatio;
        public final double preAllocRatio;
        public final boolean preAllocateBuffer;
        public final int previewHeight;
        public final int previewWidth;
        public final File processDir;
        public final boolean processOnSd;
        public final boolean recordingEnabled;
        public final int targetFrameRate;
        public final int targetSize;
        public final boolean zoomEnabled;

        static {
            SHOULD_FORCE_SMOOTH_ZOOM = Build.MODEL.length() == "SM-C115".length() && Build.MODEL.contains("SM-C11");
            DISABLE_FACE_RECOGNITION = Build.MODEL.length() == "SM-C115".length() && Build.MODEL.contains("SM-C11");
            SHOULD_DISABLE_FRONTFACING_SET_RECORDING_HINT = Build.MODEL.contains("Nexus 5") || Build.MODEL.contains("SM-N900");
            SHOULD_DISABLE_BACKFACING_SET_RECORDING_HINT = Build.MODEL.contains("SGH-I747") || Build.MODEL.contains("SGH-T999") || Build.MODEL.contains("SGH-N064") || Build.MODEL.contains("SC-06D") || Build.MODEL.contains("SCH-J021") || Build.MODEL.contains("SCH-R530") || Build.MODEL.contains("SCH-I535") || Build.MODEL.contains("SCL21") || Build.MODEL.contains("SCH-S960L");
            SHOULD_SHOW_ZOOM_SLIDER = Build.MODEL.startsWith("SM-C11");
            IS_ZERO_INDEX_BASED_ZOOM = Build.MODEL.length() == "SM-C115".length() && Build.MODEL.contains("SM-C11");
            FORCE_AUTO_FOCUS = Build.MODEL.contains("GT-I9505") || Build.MODEL.contains("GT-I9506") || Build.MODEL.contains("GT-I9500") || Build.MODEL.contains("SGH-I337") || Build.MODEL.contains("SGH-M919") || Build.MODEL.contains("SCH-I545") || Build.MODEL.contains("SPH-L720") || Build.MODEL.contains("GT-I9508") || Build.MODEL.contains("SHV-E300") || Build.MODEL.contains("SCH-R970") || Build.MODEL.contains("SM-N900") || Build.MODEL.contains("LG-D801");
            PROCESS_ON_SD_CARD = SLog.sLogsOn;
        }

        public RecordConfig(Context context) {
            File filesDir;
            this.memRatio = SystemUtil.getMemoryRatio(context, true);
            boolean defaultRecordingEnabled = this.memRatio >= 1.0d;
            this.highQuality = true;
            SharedPreferences sp = context.getSharedPreferences("RecordConfig", 0);
            this.preAllocRatio = this.memRatio >= 2.0d ? sp.getFloat("pre_ratio", 1.5f) : 0.8d;
            this.preAllocateBuffer = sp.getBoolean("preAllocateBuffer", true);
            this.recordingEnabled = sp.getBoolean("recordingEnabled", defaultRecordingEnabled);
            this.zoomEnabled = sp.getBoolean("zoomEnabled", true);
            this.processOnSd = (PROCESS_ON_SD_CARD || sp.getBoolean("processOnSD", PROCESS_ON_SD_CARD)) && "mounted".equals(Environment.getExternalStorageState());
            if (this.processOnSd) {
                filesDir = RecordConfigUtils.FOLDER_ROOT_PROCESS_DEBUG;
            } else {
                filesDir = context.getFilesDir();
            }
            this.processDir = filesDir;
            this.cameraSwitchEnabled = sp.getBoolean("cameraSwitchEnabled", true) && CameraManager.hasFrontFacingCamera() && CameraManager.hasBackFacingCamera();
            this.flashSwitchEnabled = sp.getBoolean("flashSwitchEnabled", false);
            this.targetFrameRate = sp.getInt("frameRate", 30);
            this.targetSize = sp.getInt("targetSize", 480);
            this.previewWidth = sp.getInt("previewWidth", 640) / (this.highQuality ? 1 : 2);
            this.previewHeight = sp.getInt("previewHeight", 480) / (this.highQuality ? 1 : 2);
            this.maxDuration = sp.getInt("maxDuration", 6000);
            int initialBufferCount = (int) (sp.getInt("bufferSize", avcodec.AV_CODEC_ID_AIC) * (this.maxDuration / 6000.0f));
            this.bufferCount = this.memRatio <= 1.0d ? (int) (initialBufferCount * 0.8d) : initialBufferCount;
            this.frontFacingAspectRatioOverride = sp.getFloat("aspect_ratio_or_ff", 0.0f);
            this.backFacingAspectRatioOverride = sp.getFloat("aspect_ratio_or_bf", 0.0f);
            this.frontFacingRecordingHint = SHOULD_DISABLE_FRONTFACING_SET_RECORDING_HINT ? false : sp.getBoolean("set_recording_hint_ff", true);
            this.backFacingRecordingHint = SHOULD_DISABLE_BACKFACING_SET_RECORDING_HINT ? false : sp.getBoolean("set_recording_hint_bf", true);
            this.isZoomButtonEnabled = SHOULD_SHOW_ZOOM_SLIDER && this.zoomEnabled;
        }
    }

    public static int getMaxAudioBufferSize(RecordConfig config) {
        return getMaxAudioBufferSize(config.maxDuration);
    }

    public static int getMaxAudioBufferSize(int duration) {
        return (int) (duration * 48 * 1.1d);
    }

    public static int getTimeStampInMsFromSampleCounted(int currentCount) {
        return (int) (currentCount / 44.1d);
    }

    public static int getVideoBufferMax(int duration) {
        return (int) (((VIDEO_BIT_RATE * 1.1d) * duration) / 1000.0d);
    }

    public static int getVideoBufferMax(RecordConfig config) {
        return getVideoBufferMax(config.maxDuration);
    }

    public static int getGopFromKps(double kps, int fps) {
        return (int) (fps / kps);
    }

    public static SwVineFrameRecorder newVideoRecorder(String output, int frameRate, int targetSize, avcodec.AVPacket videoPacket, avcodec.AVPacket audioPacket, boolean useMp4) {
        SwVineFrameRecorder recorder = new SwVineFrameRecorder(output, targetSize, targetSize, 1, videoPacket, audioPacket);
        recorder.setAudioCodecName("libvorbis");
        recorder.setVideoCodecName("libvpx");
        recorder.setFormat("webm");
        recorder.setSampleRate(44100);
        recorder.setFps(frameRate);
        return recorder;
    }

    public static SwVineFrameRecorder newVideoRecorder(String output, int frameRate, int targetSize, boolean useMp4) {
        return newVideoRecorder(output, frameRate, targetSize, null, null, useMp4);
    }

    public static void deletePreProcess(File root) {
        deleteNonFolders(new File(root, "process"));
    }

    public static void deleteNonFolders(File f) {
        if (f.isDirectory()) {
            for (File c : f.listFiles()) {
                deleteNonFolders(c);
            }
        }
    }

    public static boolean isDefaultFrontFacing() {
        return !CameraManager.hasBackFacingCamera();
    }

    public static boolean copySilently(File from, File to) throws Throwable {
        try {
            FileUtils.copyFile(from, to);
            return true;
        } catch (Exception e) {
            SLog.e("Failed to copy", (Throwable) e);
            return false;
        }
    }

    public static boolean loadWasEverSuccessful(Context context) {
        String name = SwVineFrameRecorder.class.getName();
        SharedPreferences prefs = context.getSharedPreferences(name, 0);
        return prefs.getBoolean("canLoad", false);
    }

    public static void setLoadWasEverSuccessful(Context context, boolean canLoad) {
        if (context != null) {
            context.getSharedPreferences(SwVineFrameRecorder.class.getName(), 0).edit().putBoolean("canLoad", canLoad).commit();
        }
    }

    public static String getThumbnailPath(String path) {
        return path + "_image";
    }

    public static String getMetadataPath(String path) {
        return path + "_meta";
    }

    public static File copyForUpload(File dir, String path, String fileName) throws Throwable {
        File actualFile = new File(path);
        File finalName = getUploadFile(dir, fileName);
        FileUtils.copyFile(actualFile, finalName);
        return finalName;
    }

    public static String readForUpload(File finalName) throws IOException {
        FileInputStream fileStream = new FileInputStream(finalName);
        DataInputStream stream = new DataInputStream(new BufferedInputStream(fileStream));
        try {
            int size = stream.readInt();
            byte[] data = new byte[size];
            int r = stream.read(data);
            SLog.i("Data read, is there more? {}.", Boolean.valueOf(r != -1));
            return new String(data);
        } catch (IOException e) {
            SLog.e("Failed to read data: {}.", finalName);
            throw e;
        }
    }

    public static File writeForUpload(File dir, String fileName, String data) throws Throwable {
        File finalName = getUploadFile(dir, fileName);
        FileOutputStream fileStream = new FileOutputStream(finalName);
        DataOutputStream stream = null;
        try {
            try {
                DataOutputStream stream2 = new DataOutputStream(new BufferedOutputStream(fileStream));
                try {
                    byte[] bytes = data.getBytes();
                    stream2.writeInt(bytes.length);
                    stream2.write(bytes);
                    stream2.flush();
                    if (stream2 != null) {
                        stream2.close();
                    }
                    return finalName;
                } catch (IOException e) {
                    e = e;
                    stream = stream2;
                    SLog.e("Failed to write data: {}.", finalName);
                    throw e;
                } catch (Throwable th) {
                    th = th;
                    stream = stream2;
                    if (stream != null) {
                        stream.close();
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (IOException e2) {
            e = e2;
        }
    }

    private static synchronized File getUploadFile(File root, String fileName) {
        File mediaStorageDir;
        mediaStorageDir = new File(root, "upload");
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            SLog.d("Failed to make dirs: {},", fileName);
            throw new RuntimeException("Failed to make directories. You will be doomed.");
        }
        return new File(mediaStorageDir, fileName);
    }

    public static synchronized File getCameraRollFile(Context context, long startTime, String extension) {
        File file;
        File mediaStorageDir = null;
        if ("mounted".equals(Environment.getExternalStorageState())) {
            mediaStorageDir = new File(FOLDER_ROOT_SAVE);
        } else {
            try {
                String[] projection = {"_id", "_data", "datetaken"};
                Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.INTERNAL_CONTENT_URI, projection, null, null, "datetaken DESC");
                if (cursor != null && cursor.moveToFirst()) {
                    mediaStorageDir = new File(new File(cursor.getString(cursor.getColumnIndexOrThrow("_data"))).getParentFile(), "Vine");
                }
            } catch (Exception e) {
                SLog.e("Failed to get internal path", (Throwable) e);
            }
        }
        if (mediaStorageDir == null) {
            file = null;
        } else if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            SLog.d("Failed to make dirs.");
            CrashUtil.logException(new RuntimeException("Failed to make directories. You will be doomed."));
            file = null;
        } else {
            String fileName = new SimpleDateFormat("yyyy_MM_dd_HH_mm_sss", Locale.US).format(Long.valueOf(startTime));
            file = new File(mediaStorageDir.getPath() + File.separator + fileName + "." + extension);
        }
        return file;
    }

    public static Bitmap createDefaultSizeBitmap() {
        return Bitmap.createBitmap(480, 480, DEFAULT_BITMAP_CONFIG);
    }

    public static boolean isCapableOfRecording(Context context) {
        return CameraManager.hasCamera() && getGenericConfig(context).recordingEnabled;
    }

    public static boolean isCapableOfInline(Context context) {
        return SystemUtil.getMemoryRatio(context, true) > 1.2d;
    }
}
