package com.google.android.exoplayer.util;

import android.net.Uri;
import android.os.Build;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import com.google.android.exoplayer.upstream.DataSpec;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
public final class Util {
    public static final String DEVICE;
    public static final String MANUFACTURER;
    public static final String MODEL;
    public static final int SDK_INT;
    private static final Pattern XS_DATE_TIME_PATTERN;
    private static final Pattern XS_DURATION_PATTERN;

    static {
        SDK_INT = (Build.VERSION.SDK_INT == 23 && Build.VERSION.CODENAME.charAt(0) == 'N') ? 24 : Build.VERSION.SDK_INT;
        DEVICE = Build.DEVICE;
        MANUFACTURER = Build.MANUFACTURER;
        MODEL = Build.MODEL;
        XS_DATE_TIME_PATTERN = Pattern.compile("(\\d\\d\\d\\d)\\-(\\d\\d)\\-(\\d\\d)[Tt](\\d\\d):(\\d\\d):(\\d\\d)(\\.(\\d+))?([Zz]|((\\+|\\-)(\\d\\d):(\\d\\d)))?");
        XS_DURATION_PATTERN = Pattern.compile("^(-)?P(([0-9]*)Y)?(([0-9]*)M)?(([0-9]*)D)?(T(([0-9]*)H)?(([0-9]*)M)?(([0-9.]*)S)?)?$");
    }

    public static boolean isLocalFileUri(Uri uri) {
        String scheme = uri.getScheme();
        return TextUtils.isEmpty(scheme) || scheme.equals("file");
    }

    public static boolean areEqual(Object o1, Object o2) {
        return o1 == null ? o2 == null : o1.equals(o2);
    }

    public static boolean contains(Object[] items, Object item) {
        for (Object obj : items) {
            if (areEqual(obj, item)) {
                return true;
            }
        }
        return false;
    }

    public static ExecutorService newSingleThreadExecutor(final String threadName) {
        return Executors.newSingleThreadExecutor(new ThreadFactory() { // from class: com.google.android.exoplayer.util.Util.1
            @Override // java.util.concurrent.ThreadFactory
            public Thread newThread(Runnable r) {
                return new Thread(r, threadName);
            }
        });
    }

    public static String toLowerInvariant(String text) {
        if (text == null) {
            return null;
        }
        return text.toLowerCase(Locale.US);
    }

    public static int ceilDivide(int numerator, int denominator) {
        return ((numerator + denominator) - 1) / denominator;
    }

    public static int binarySearchFloor(long[] a, long key, boolean inclusive, boolean stayInBounds) {
        int index = Arrays.binarySearch(a, key);
        if (index < 0) {
            index = -(index + 2);
        } else if (!inclusive) {
            index--;
        }
        return stayInBounds ? Math.max(0, index) : index;
    }

    public static int binarySearchCeil(long[] a, long key, boolean inclusive, boolean stayInBounds) {
        int index = Arrays.binarySearch(a, key);
        if (index < 0) {
            index ^= -1;
        } else if (!inclusive) {
            index++;
        }
        return stayInBounds ? Math.min(a.length - 1, index) : index;
    }

    public static <T> int binarySearchFloor(List<? extends Comparable<? super T>> list, T key, boolean inclusive, boolean stayInBounds) {
        int index = Collections.binarySearch(list, key);
        if (index < 0) {
            index = -(index + 2);
        } else if (!inclusive) {
            index--;
        }
        return stayInBounds ? Math.max(0, index) : index;
    }

    public static long scaleLargeTimestamp(long timestamp, long multiplier, long divisor) {
        if (divisor >= multiplier && divisor % multiplier == 0) {
            long divisionFactor = divisor / multiplier;
            return timestamp / divisionFactor;
        }
        if (divisor < multiplier && multiplier % divisor == 0) {
            long multiplicationFactor = multiplier / divisor;
            return timestamp * multiplicationFactor;
        }
        double multiplicationFactor2 = multiplier / divisor;
        return (long) (timestamp * multiplicationFactor2);
    }

    public static void scaleLargeTimestampsInPlace(long[] timestamps, long multiplier, long divisor) {
        if (divisor >= multiplier && divisor % multiplier == 0) {
            long divisionFactor = divisor / multiplier;
            for (int i = 0; i < timestamps.length; i++) {
                timestamps[i] = timestamps[i] / divisionFactor;
            }
            return;
        }
        if (divisor < multiplier && multiplier % divisor == 0) {
            long multiplicationFactor = multiplier / divisor;
            for (int i2 = 0; i2 < timestamps.length; i2++) {
                timestamps[i2] = timestamps[i2] * multiplicationFactor;
            }
            return;
        }
        double multiplicationFactor2 = multiplier / divisor;
        for (int i3 = 0; i3 < timestamps.length; i3++) {
            timestamps[i3] = (long) (timestamps[i3] * multiplicationFactor2);
        }
    }

    public static int[] toArray(List<Integer> list) {
        if (list == null) {
            return null;
        }
        int length = list.size();
        int[] intArray = new int[length];
        for (int i = 0; i < length; i++) {
            intArray[i] = list.get(i).intValue();
        }
        return intArray;
    }

    public static void maybeTerminateInputStream(HttpURLConnection connection, long bytesRemaining) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        if (SDK_INT == 19 || SDK_INT == 20) {
            try {
                InputStream inputStream = connection.getInputStream();
                if (bytesRemaining == -1) {
                    if (inputStream.read() == -1) {
                        return;
                    }
                } else if (bytesRemaining <= PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH) {
                    return;
                }
                String className = inputStream.getClass().getName();
                if (className.equals("com.android.okhttp.internal.http.HttpTransport$ChunkedInputStream") || className.equals("com.android.okhttp.internal.http.HttpTransport$FixedLengthInputStream")) {
                    Class<?> superclass = inputStream.getClass().getSuperclass();
                    Method unexpectedEndOfInput = superclass.getDeclaredMethod("unexpectedEndOfInput", new Class[0]);
                    unexpectedEndOfInput.setAccessible(true);
                    unexpectedEndOfInput.invoke(inputStream, new Object[0]);
                }
            } catch (IOException e) {
            } catch (Exception e2) {
            }
        }
    }

    public static DataSpec getRemainderDataSpec(DataSpec dataSpec, int bytesLoaded) {
        if (bytesLoaded != 0) {
            long remainingLength = dataSpec.length != -1 ? dataSpec.length - bytesLoaded : -1L;
            return new DataSpec(dataSpec.uri, dataSpec.position + bytesLoaded, remainingLength, dataSpec.key, dataSpec.flags);
        }
        return dataSpec;
    }

    public static int getIntegerCodeForString(String string) {
        int length = string.length();
        Assertions.checkArgument(length <= 4);
        int result = 0;
        for (int i = 0; i < length; i++) {
            result = (result << 8) | string.charAt(i);
        }
        return result;
    }

    public static int getTopInt(long value) {
        return (int) (value >>> 32);
    }

    public static int getBottomInt(long value) {
        return (int) value;
    }

    public static long getLong(int topInteger, int bottomInteger) {
        return (topInteger << 32) | (bottomInteger & 4294967295L);
    }

    public static <T> String getCommaDelimitedSimpleClassNames(T[] objects) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < objects.length; i++) {
            stringBuilder.append(objects[i].getClass().getSimpleName());
            if (i < objects.length - 1) {
                stringBuilder.append(", ");
            }
        }
        return stringBuilder.toString();
    }
}
