package co.vine.android.util;

import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.shapes.RoundRectShape;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.RemoteException;
import android.os.StatFs;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes.dex */
public class ImageUtils {
    private static final Paint sInStrokePaint;
    private static final Paint sSolidStrokePaint;
    public static final PorterDuffXfermode sSrcXferMode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
    private static final Paint sResizePaint = new Paint(2);
    private static final Paint sOutStrokePaint = new Paint();

    static {
        sOutStrokePaint.setStrokeWidth(1.0f);
        sOutStrokePaint.setStyle(Paint.Style.STROKE);
        sOutStrokePaint.setColor(-2039584);
        sInStrokePaint = new Paint();
        sInStrokePaint.setStrokeWidth(1.0f);
        sInStrokePaint.setStyle(Paint.Style.STROKE);
        sInStrokePaint.setColor(-986896);
        sSolidStrokePaint = new Paint();
        sSolidStrokePaint.setStrokeWidth(3.0f);
        sSolidStrokePaint.setStyle(Paint.Style.STROKE);
        sSolidStrokePaint.setColor(-986896);
    }

    public static BitmapInfo safeDecode(Context context, byte[] imageBytes, int viewWidth, int viewHeight) {
        BitmapFactory.Options dbo = new BitmapFactory.Options();
        dbo.inJustDecodeBounds = true;
        decodeByteArray(context, imageBytes, 0, imageBytes.length, dbo);
        int origWidth = dbo.outWidth;
        int origHeight = dbo.outHeight;
        setBitmapOptionsScale(dbo, viewWidth, viewHeight, false);
        BitmapInfo bitmapInfo = decodeByteArray(context, imageBytes, 0, imageBytes.length, dbo);
        if (bitmapInfo != null) {
            return new BitmapInfo(bitmapInfo.bitmap, dbo.inSampleSize, origWidth, origHeight);
        }
        return null;
    }

    public static BitmapInfo safeDecode(Context context, InputStream is, int viewWidth, int viewHeight) throws Throwable {
        BitmapInfo bitmapInfo;
        FileOutputStream out;
        int bytesLen;
        File f = CommonUtil.getTempFile(context);
        FileOutputStream out2 = null;
        try {
            try {
                out = new FileOutputStream(f);
            } catch (IOException e) {
            } catch (Throwable th) {
                th = th;
            }
            try {
                bytesLen = CommonUtil.readFullyWriteTo(is, out, 4096);
            } catch (IOException e2) {
                out2 = out;
                bitmapInfo = null;
                CommonUtil.closeSilently(out2);
                f.delete();
                return bitmapInfo;
            } catch (Throwable th2) {
                th = th2;
                out2 = out;
                CommonUtil.closeSilently(out2);
                throw th;
            }
        } catch (Throwable th3) {
            th = th3;
        }
        try {
            if (bytesLen == 0) {
                bitmapInfo = null;
                CommonUtil.closeSilently(out);
                f.delete();
            } else {
                out.flush();
                CommonUtil.closeSilently(out);
                BufferedInputStream optionsInput = null;
                BufferedInputStream imageInput = null;
                try {
                    try {
                        BufferedInputStream optionsInput2 = new BufferedInputStream(new FileInputStream(f));
                        try {
                            BitmapFactory.Options dbo = new BitmapFactory.Options();
                            dbo.inJustDecodeBounds = true;
                            BitmapFactory.decodeStream(optionsInput2, null, dbo);
                            BufferedInputStream imageInput2 = new BufferedInputStream(new FileInputStream(f));
                            try {
                                int origWidth = dbo.outWidth;
                                int origHeight = dbo.outHeight;
                                setBitmapOptionsScale(dbo, viewWidth, viewHeight, false);
                                Bitmap bitmap = BitmapFactory.decodeStream(imageInput2, null, dbo);
                                if (bitmap != null) {
                                    bitmapInfo = new BitmapInfo(bitmap, dbo.inSampleSize, origWidth, origHeight);
                                    CommonUtil.closeSilently(optionsInput2);
                                    CommonUtil.closeSilently(imageInput2);
                                    f.delete();
                                } else {
                                    bitmapInfo = null;
                                    CommonUtil.closeSilently(optionsInput2);
                                    CommonUtil.closeSilently(imageInput2);
                                    f.delete();
                                }
                            } catch (IOException e3) {
                                imageInput = imageInput2;
                                optionsInput = optionsInput2;
                                bitmapInfo = null;
                                CommonUtil.closeSilently(optionsInput);
                                CommonUtil.closeSilently(imageInput);
                                f.delete();
                                return bitmapInfo;
                            } catch (OutOfMemoryError e4) {
                                e = e4;
                                throw new ImageMemoryException(e);
                            } catch (Throwable th4) {
                                th = th4;
                                imageInput = imageInput2;
                                optionsInput = optionsInput2;
                                CommonUtil.closeSilently(optionsInput);
                                CommonUtil.closeSilently(imageInput);
                                throw th;
                            }
                        } catch (IOException e5) {
                            optionsInput = optionsInput2;
                        } catch (OutOfMemoryError e6) {
                            e = e6;
                        } catch (Throwable th5) {
                            th = th5;
                            optionsInput = optionsInput2;
                        }
                    } catch (IOException e7) {
                    } catch (OutOfMemoryError e8) {
                        e = e8;
                    }
                } catch (Throwable th6) {
                    th = th6;
                }
            }
            return bitmapInfo;
        } catch (Throwable th7) {
            th = th7;
            f.delete();
            throw th;
        }
    }

    private static boolean isPortrait(int width, int height) {
        return height > width;
    }

    public static class ImageMemoryException extends Exception {
        public ImageMemoryException(OutOfMemoryError e) {
            super(e);
        }
    }

    public static BitmapInfo safeDecode(Context context, Uri uri, int width, int height, boolean conservative) throws IOException {
        try {
            ContentResolver resolver = context.getContentResolver();
            if (resolver == null || uri == null) {
                CommonUtil.closeSilently(null);
                CommonUtil.closeSilently(null);
                return null;
            }
            try {
                InputStream optionsStream = resolver.openInputStream(uri);
                BitmapFactory.Options dbo = new BitmapFactory.Options();
                dbo.inJustDecodeBounds = true;
                decodeStream(context, optionsStream, null, dbo);
                int bitmapWidth = dbo.outWidth;
                int bitmapHeight = dbo.outHeight;
                setBitmapOptionsScale(dbo, width, height, conservative);
                InputStream inputStream = resolver.openInputStream(uri);
                Bitmap bmp = decodeStream(context, inputStream, null, dbo);
                if (bmp == null) {
                    CommonUtil.closeSilently(optionsStream);
                    CommonUtil.closeSilently(inputStream);
                    return null;
                }
                Bitmap result = adjustRotation(context, uri, bmp);
                if (result == null) {
                    result = bmp;
                } else if (isPortrait(bitmapWidth, bitmapHeight) != isPortrait(result.getWidth(), result.getHeight())) {
                    bitmapHeight = bitmapWidth;
                    bitmapWidth = bitmapHeight;
                }
                BitmapInfo bitmapInfo = new BitmapInfo(result, dbo.inSampleSize, bitmapWidth, bitmapHeight);
                CommonUtil.closeSilently(optionsStream);
                CommonUtil.closeSilently(inputStream);
                return bitmapInfo;
            } catch (NullPointerException e) {
                throw new FileNotFoundException();
            }
        } catch (FileNotFoundException e2) {
            CommonUtil.closeSilently(null);
            CommonUtil.closeSilently(null);
            return null;
        } catch (SecurityException e3) {
            CommonUtil.closeSilently(null);
            CommonUtil.closeSilently(null);
            return null;
        } catch (Throwable th) {
            CommonUtil.closeSilently(null);
            CommonUtil.closeSilently(null);
            throw th;
        }
    }

    public static void setBitmapOptionsScale(BitmapFactory.Options dbo, int desiredWidth, int desiredHeight, boolean conservative) {
        int initialScale;
        int width = dbo.outWidth;
        int height = dbo.outHeight;
        int scale = 1;
        int size = Math.max(width, height);
        int maxSize = Math.max(desiredWidth, desiredHeight);
        if (conservative) {
            initialScale = 1;
        } else {
            initialScale = 2;
        }
        while (size / initialScale >= maxSize) {
            size /= 2;
            scale *= 2;
        }
        dbo.inJustDecodeBounds = false;
        dbo.inDither = true;
        dbo.inPreferQualityOverSpeed = true;
        dbo.inSampleSize = scale;
    }

    public static Bitmap roundBitmapCorners(Context context, Bitmap bm, int width, int height, float cornerRadius) {
        Bitmap bitmap = createBitmap(context, width, height, Bitmap.Config.ARGB_8888);
        if (bitmap == null) {
            return null;
        }
        Canvas canvas = new Canvas(bitmap);
        Paint p = new Paint(3);
        RoundRectShape rrs = new RoundRectShape(new float[]{cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius}, null, null);
        rrs.resize(width, height);
        rrs.draw(canvas, p);
        p.setXfermode(sSrcXferMode);
        if (bm.getWidth() != width || bm.getHeight() != height) {
            Rect src = new Rect(0, 0, bm.getWidth(), bm.getHeight());
            Rect dest = new Rect(0, 0, width, height);
            canvas.drawBitmap(bm, src, dest, p);
            return bitmap;
        }
        canvas.drawBitmap(bm, 0.0f, 0.0f, p);
        return bitmap;
    }

    public static BitmapInfo resizeBitmap(Context context, byte[] imageBytes, int desiredWidth, int desiredHeight) {
        return resizeBitmap(context, safeDecode(context, imageBytes, desiredWidth, desiredHeight), desiredWidth, desiredHeight);
    }

    public static BitmapInfo resizeBitmap(Context context, InputStream is, int desiredWidth, int desiredHeight) throws ImageMemoryException {
        return resizeBitmap(context, safeDecode(context, is, desiredWidth, desiredHeight), desiredWidth, desiredHeight);
    }

    private static BitmapInfo resizeBitmap(Context context, BitmapInfo src, int desiredWidth, int desiredHeight) {
        if (src == null) {
            return null;
        }
        Bitmap bm = src.bitmap;
        int width = bm.getWidth();
        int height = bm.getHeight();
        int desiredSize = Math.max(desiredWidth, desiredHeight);
        int size = Math.max(width, height);
        if (size > desiredSize) {
            Matrix matrix = new Matrix();
            float scale = desiredSize / size;
            matrix.postScale(scale, scale);
            Bitmap bitmap = createBitmap(context, bm, 0, 0, width, height, matrix, true);
            if (bitmap != null) {
                bm.recycle();
                return new BitmapInfo(bitmap, (int) scale, width, height);
            }
            return null;
        }
        return src;
    }

    public static Bitmap resizeBitmap(Context context, Uri uri, float viewWidth, float viewHeight, int border) throws IOException {
        float bitmapWidth;
        float bitmapHeight;
        Bitmap srcBitmap;
        ContentResolver cr = context.getContentResolver();
        InputStream inputStream = null;
        InputStream optionStream = null;
        try {
            optionStream = cr.openInputStream(uri);
            BitmapFactory.Options dbo = new BitmapFactory.Options();
            dbo.inJustDecodeBounds = true;
            decodeStream(context, optionStream, null, dbo);
            float nativeWidth = dbo.outWidth;
            float nativeHeight = dbo.outHeight;
            float viewWidth2 = viewWidth - border;
            float viewHeight2 = viewHeight - border;
            inputStream = cr.openInputStream(uri);
            if (nativeWidth > viewWidth2 || nativeHeight > viewHeight2) {
                float dx = nativeWidth / viewWidth2;
                float dy = nativeHeight / viewHeight2;
                if (dx > dy) {
                    bitmapWidth = viewWidth2;
                    bitmapHeight = nativeHeight / dx;
                } else {
                    bitmapWidth = nativeWidth / dy;
                    bitmapHeight = viewHeight2;
                }
                if (nativeWidth / bitmapWidth > 1.0f) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = (int) (nativeWidth / bitmapWidth);
                    srcBitmap = decodeStream(context, inputStream, null, options);
                } else {
                    srcBitmap = decodeStream(context, inputStream);
                }
            } else {
                bitmapWidth = viewWidth2;
                bitmapHeight = viewHeight2;
                srcBitmap = decodeStream(context, inputStream);
            }
            if (srcBitmap == null) {
                return null;
            }
            Bitmap bitmap = scaleBitmap(context, srcBitmap, bitmapWidth, bitmapHeight, border);
            srcBitmap.recycle();
            return bitmap;
        } catch (IOException e) {
            return null;
        } finally {
            CommonUtil.closeSilently(optionStream);
            CommonUtil.closeSilently(inputStream);
        }
    }

    public static Bitmap scaleBitmap(Context context, Bitmap src, float width, float height, int border) {
        Bitmap bitmap = createBitmap(context, ((int) width) + border, ((int) height) + border, Bitmap.Config.ARGB_8888);
        if (bitmap == null) {
            return null;
        }
        Canvas canvas = new Canvas(bitmap);
        if (border > 0) {
            canvas.drawColor(-1);
            canvas.drawBitmap(src, new Rect(0, 0, src.getWidth(), src.getHeight()), new Rect(border, border, ((int) width) + 1, (int) height), sResizePaint);
            canvas.drawRect(0.0f, 0.0f, (border + width) - 1.0f, (border + height) - 1.0f, sOutStrokePaint);
            canvas.drawRect(border - 1, border - 1, width, height, sInStrokePaint);
            return bitmap;
        }
        canvas.drawBitmap(src, new Rect(0, 0, src.getWidth(), src.getHeight()), new Rect(border, border, ((int) width) + 1, (int) height), sResizePaint);
        return bitmap;
    }

    public static File getTempPic(Context context, boolean resized, long ownerId) {
        File dir = CommonUtil.getFilesDir(context);
        if (dir == null) {
            return null;
        }
        String prefix = resized ? "pic-r-" : "pic-" + ownerId + "-";
        return new File(dir, prefix + System.currentTimeMillis() + ".jpg");
    }

    public static boolean isTempPic(Uri uri) {
        return (uri == null || uri.getLastPathSegment() == null || !uri.getLastPathSegment().startsWith("pic-")) ? false : true;
    }

    public static boolean deleteTempPic(Uri uri) {
        if (!isTempPic(uri)) {
            return false;
        }
        File f = new File(uri.getPath());
        return f.delete();
    }

    public static Bitmap decodeStream(Context context, InputStream is) {
        try {
            return BitmapFactory.decodeStream(is);
        } catch (OutOfMemoryError e) {
            return null;
        }
    }

    public static Bitmap decodeStream(Context context, InputStream is, Rect outPadding, BitmapFactory.Options opts) {
        try {
            return BitmapFactory.decodeStream(is, outPadding, opts);
        } catch (OutOfMemoryError e) {
            return null;
        }
    }

    public static BitmapInfo decodeByteArray(Context context, byte[] data, int offset, int length, BitmapFactory.Options opts) {
        try {
            BitmapFactory.Options dbo = new BitmapFactory.Options();
            dbo.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(data, 0, data.length, dbo);
            int origWidth = dbo.outWidth;
            int origHeight = dbo.outHeight;
            int scale = opts.inSampleSize;
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, offset, length, opts);
            if (bitmap != null) {
                return new BitmapInfo(bitmap, scale, origWidth, origHeight);
            }
            return null;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }

    public static Bitmap createBitmap(Context context, Bitmap bmp, int left, int top, int width, int height) {
        try {
            return Bitmap.createBitmap(bmp, left, top, width, height);
        } catch (OutOfMemoryError e) {
            return null;
        }
    }

    public static Bitmap createBitmap(Context context, Bitmap bmp, int left, int top, int width, int height, Matrix matrix, boolean filter) {
        try {
            return Bitmap.createBitmap(bmp, left, top, width, height, matrix, filter);
        } catch (OutOfMemoryError e) {
            return null;
        }
    }

    public static Bitmap createBitmap(Context context, int width, int height, Bitmap.Config config) {
        try {
            return Bitmap.createBitmap(width, height, config);
        } catch (OutOfMemoryError e) {
            return null;
        }
    }

    public static void removeFiles(Context context) {
        File[] files;
        File dir = CommonUtil.getFilesDir(context);
        if (dir != null && (files = dir.listFiles()) != null) {
            for (File f : files) {
                if (!f.isDirectory()) {
                    f.delete();
                }
            }
        }
    }

    public static Uri writePicToFile(Context context, Bitmap pic, long ownerId) throws Throwable {
        Uri uriFromFile = null;
        FileOutputStream fs = null;
        File f = getTempPic(context, true, ownerId);
        if (f != null) {
            try {
                FileOutputStream fs2 = new FileOutputStream(f);
                try {
                    pic.compress(Bitmap.CompressFormat.JPEG, 85, fs2);
                    pic.recycle();
                    uriFromFile = Uri.fromFile(f);
                    CommonUtil.closeSilently(fs2);
                } catch (IOException e) {
                    fs = fs2;
                    CommonUtil.closeSilently(fs);
                    return uriFromFile;
                } catch (OutOfMemoryError e2) {
                    fs = fs2;
                    CommonUtil.closeSilently(fs);
                    return uriFromFile;
                } catch (Throwable th) {
                    th = th;
                    fs = fs2;
                    CommonUtil.closeSilently(fs);
                    throw th;
                }
            } catch (IOException e3) {
            } catch (OutOfMemoryError e4) {
            } catch (Throwable th2) {
                th = th2;
            }
        }
        return uriFromFile;
    }

    public static boolean hasEnoughExternalStorageForNewPhoto(Context context) {
        File dir = CommonUtil.getFilesDir(context);
        if (dir == null) {
            return false;
        }
        StatFs stat = new StatFs(dir.getAbsolutePath());
        long bytesAvailable = stat.getBlockSize() * stat.getAvailableBlocks();
        return bytesAvailable > 1048576;
    }

    public static class BitmapInfo {
        public final Bitmap bitmap;
        public final int origHeight;
        public final int origWidth;
        public final int scale;

        public BitmapInfo(Bitmap bitmap, int scale, int origWidth, int origHeight) {
            this.bitmap = bitmap;
            this.scale = scale;
            this.origWidth = origWidth;
            this.origHeight = origHeight;
        }
    }

    public static int getExifOrientation(String filepath) {
        if (filepath == null) {
            return 0;
        }
        try {
            ExifInterface exif = new ExifInterface(filepath);
            return exif.getAttributeInt("Orientation", 0);
        } catch (IOException e) {
            return 0;
        }
    }

    public static String getRealPathFromImageUri(Context context, Uri uri) {
        String result;
        String scheme = uri.getScheme();
        if (scheme == null || "file".equals(scheme)) {
            return uri.getPath();
        }
        if ("content".equals(scheme)) {
            String[] proj = {"_data"};
            Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
            if (cursor != null) {
                try {
                    if (cursor.moveToFirst()) {
                        result = cursor.getString(0);
                        if (result == null) {
                            result = uri.getPath();
                            cursor.close();
                        }
                    } else {
                        result = uri.getPath();
                        cursor.close();
                    }
                    return result;
                } finally {
                    cursor.close();
                }
            }
        }
        return uri.getPath();
    }

    public static int getExifOrientation(Context context, Uri uri) throws RemoteException {
        ContentProviderClient provider;
        int rotationFromFile;
        String scheme = uri.getScheme();
        if (scheme == null || "file".equals(scheme)) {
            return getExifOrientation(uri.getPath());
        }
        if ("content".equals(scheme) && (provider = context.getContentResolver().acquireContentProviderClient(uri)) != null) {
            try {
                Cursor result = provider.query(uri, new String[]{"orientation", "_data"}, null, null, null);
                if (result == null) {
                    return 0;
                }
                try {
                    if (result.moveToFirst()) {
                        String path = result.getString(1);
                        if (path != null && (rotationFromFile = getExifOrientation(path)) != 0) {
                            return rotationFromFile;
                        }
                        int rotationFromCursor = result.getInt(0);
                        if (rotationFromCursor != 0) {
                            return rotationFromCursor;
                        }
                        return 0;
                    }
                } finally {
                    result.close();
                }
            } catch (RemoteException | RuntimeException e) {
                return 0;
            }
        }
        return 0;
    }

    public static Bitmap adjustRotation(Context context, Uri uri, Bitmap bitmap) throws RemoteException {
        float rotation;
        int degrees = getExifOrientation(context, uri);
        switch (degrees) {
            case 3:
                rotation = 180.0f;
                break;
            case 4:
            case 5:
            case 7:
            default:
                return bitmap;
            case 6:
                rotation = 90.0f;
                break;
            case 8:
                rotation = 270.0f;
                break;
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(rotation);
        Bitmap result = createBitmap(context, bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (result != null && result != bitmap) {
            bitmap.recycle();
        }
        return result;
    }

    public static Bitmap getCroppedBitmap(Bitmap bmp, int radius) {
        Bitmap sbmp = Bitmap.createScaledBitmap(bmp, radius, radius, false);
        Bitmap output = Bitmap.createBitmap(sbmp.getWidth(), sbmp.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(sbmp.getWidth() / 2, sbmp.getHeight() / 2, sbmp.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(sbmp, rect, rect, paint);
        return output;
    }

    public static Bitmap getMutableBitmap(Bitmap bm) {
        if (!bm.isMutable()) {
            return bm.copy(bm.getConfig(), true);
        }
        return bm;
    }

    public static Bitmap addStroke(int color, float width, Bitmap source) {
        Bitmap bm = getMutableBitmap(source);
        Canvas canvas = new Canvas(bm);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(color);
        paint.setStrokeWidth(width);
        paint.setAntiAlias(true);
        float halfStroke = width / 2.0f;
        canvas.drawOval(new RectF(halfStroke, halfStroke, bm.getWidth() - halfStroke, bm.getHeight() - halfStroke), paint);
        return bm;
    }

    public static Bitmap desaturateWithColorFilter(Bitmap source, float sat, int color) {
        Canvas canvas = new Canvas(source);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(sat);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        paint.setColorFilter(filter);
        canvas.drawBitmap(source, 0.0f, 0.0f, paint);
        if (color != 0) {
            paint.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
            canvas.drawOval(new RectF(0.0f, 0.0f, source.getWidth(), source.getHeight()), paint);
        }
        return source;
    }
}
