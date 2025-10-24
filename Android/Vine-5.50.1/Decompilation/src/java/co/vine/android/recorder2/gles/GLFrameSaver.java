package co.vine.android.recorder2.gles;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.opengl.GLES20;
import com.edisonwang.android.slog.SLog;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;

/* loaded from: classes.dex */
public class GLFrameSaver {
    public static void cacheFrame(ByteBuffer buffer, int width, int height) {
        long nanos = System.nanoTime();
        buffer.rewind();
        GLES20.glReadPixels(0, 0, width, height, 6408, 5121, buffer);
        SLog.d("ryango cache {}", Long.valueOf((System.nanoTime() - nanos) / 1000000));
    }

    public static void saveFrame(ByteBuffer buffer, int width, int height, String outputFilePath) throws FileNotFoundException {
        long nanos = System.nanoTime();
        buffer.rewind();
        Bitmap copyBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Bitmap outputBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        SLog.d("ryango save alloc {}", Long.valueOf((System.nanoTime() - nanos) / 1000000));
        copyBitmap.copyPixelsFromBuffer(buffer);
        Canvas canvas = new Canvas(outputBitmap);
        Matrix matrix = new Matrix();
        matrix.postScale(1.0f, -1.0f, width / 2, height / 2);
        canvas.drawBitmap(copyBitmap, matrix, null);
        outputBitmap.compress(Bitmap.CompressFormat.JPEG, 80, new FileOutputStream(outputFilePath));
        SLog.d("ryango save {}", Long.valueOf((System.nanoTime() - nanos) / 1000000));
    }
}
