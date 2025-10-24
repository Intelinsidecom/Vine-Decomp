package co.vine.android.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import co.vine.android.R;
import co.vine.android.Settings;
import co.vine.android.VineLoggingException;
import co.vine.android.api.VineEntity;
import co.vine.android.api.VinePost;
import co.vine.android.api.VineSingleNotification;
import co.vine.android.api.VineVideoUrlTier;
import co.vine.android.cache.video.VideoKey;
import co.vine.android.client.AppController;
import co.vine.android.provider.VineDatabaseHelper;
import co.vine.android.recorder.RecordConfigUtils;
import co.vine.android.service.components.Components;
import co.vine.android.widget.Typefaces;
import java.io.File;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import org.apache.commons.io.FileUtils;

/* loaded from: classes.dex */
public class Util extends CommonUtil {
    private static SimpleDateFormat sDateFormatNoYear;
    private static SimpleDateFormat sDateFormatWithYear;
    private static final int[] DATE_FORMAT_LOCK = new int[0];
    public static final IntentFilter COLOR_CHANGED_INTENT_FILTER = new IntentFilter();

    public enum ProfileImageSize {
        SMALL,
        MEDIUM,
        LARGE
    }

    static {
        COLOR_CHANGED_INTENT_FILTER.addAction("co.vine.android.profileColor");
    }

    public static void startActionOnRecordingAvailable(Activity activity, Intent intent, int requestId) {
        if (RecordConfigUtils.isCapableOfRecording(activity)) {
            activity.startActivityForResult(intent, requestId);
        } else {
            showNoCameraToast(activity);
        }
    }

    public static boolean recorder2EnabledAndSupported(Context context) {
        if (Build.VERSION.SDK_INT < 18) {
            return false;
        }
        if (ClientFlagsHelper.isRecorder2Enabled(context) || (ClientFlagsHelper.isRecorder2EnabledMarshmallow(context) && Build.VERSION.SDK_INT >= 23)) {
            return ((Build.BRAND.equalsIgnoreCase("HUAWEI") && Build.MODEL.startsWith("HUAWEI P6")) || Build.MODEL.startsWith("HUAWEI G615") || Build.MODEL.startsWith("HUAWEI D2")) ? false : true;
        }
        return false;
    }

    public static void showNoCameraToast(Context context) {
        Toast.makeText(context, R.string.no_camera_found_toast, 0).show();
    }

    public static void fetchClientFlags(Context context, boolean abortOnHostChange, boolean force) {
        if (!force) {
            try {
                if (ClientFlagsHelper.isClientFlagRateLimited(context)) {
                    return;
                }
            } catch (Exception e) {
                CrashUtil.logException(e);
                return;
            }
        }
        Components.clientConfigUpdateComponent().fetchClientFlags(AppController.getInstance(context), abortOnHostChange);
    }

    public static long getCacheSize(Context context) throws VineLoggingException {
        try {
            File cacheDir = getExternalCacheDir(context);
            long size = cacheDir != null ? 0 + FileUtils.sizeOfDirectory(cacheDir) : 0L;
            if (cacheDir != null) {
                cacheDir = getExternalFilesDir(context);
            }
            if (cacheDir != null) {
                size += FileUtils.sizeOfDirectory(cacheDir);
            }
            File f = context.getDatabasePath(VineDatabaseHelper.getDatabaseName(1));
            if (f != null) {
                size += f.length();
            }
            if (!BuildUtil.isLogsOn() && size <= 500000) {
                return 0L;
            }
            return size;
        } catch (Throwable e) {
            throw new VineLoggingException(e);
        }
    }

    public static String formatFileSize(Resources res, long bytes) throws NumberFormatException {
        DECIMAL_FORMAT_SYMBOLS.setGroupingSeparator(res.getString(R.string.number_format_separator).charAt(0));
        int groupingSize = 3;
        try {
            groupingSize = Integer.parseInt(res.getString(R.string.number_format_grouping_size));
        } catch (NumberFormatException e) {
        }
        DECIMAL_FORMAT.setGroupingSize(groupingSize);
        DECIMAL_FORMAT.setGroupingUsed(true);
        DECIMAL_FORMAT.setDecimalFormatSymbols(DECIMAL_FORMAT_SYMBOLS);
        if (bytes / 1000000000 >= 1) {
            return res.getString(R.string.number_format_gigabytes, DECIMAL_FORMAT.format(bytes / 1000000000));
        }
        if (bytes / 1000000 >= 1) {
            return res.getString(R.string.number_format_megabytes, DECIMAL_FORMAT.format(bytes / 1000000));
        }
        if (bytes / 1000 >= 1) {
            return res.getString(R.string.number_format_kilobytes, DECIMAL_FORMAT.format(bytes / 1000));
        }
        return res.getString(R.string.number_format_bytes, DECIMAL_FORMAT.format(bytes));
    }

    public static void setTextWithSpan(Object span, String text, TextView view) {
        SpannableStringBuilder contentSb = new SpannableStringBuilder(text);
        safeSetSpan(contentSb, span, 0, contentSb.length(), 33);
        view.setText(contentSb);
    }

    public static void safeSetSpan(Spannable spannable, Object span, int start, int end, int flags) {
        if (BuildUtil.isLogsOn()) {
            spannable.setSpan(span, start, end, flags);
            return;
        }
        if (start >= 0 && start < spannable.length() && end >= start && end < spannable.length()) {
            spannable.setSpan(span, start, end, flags);
            return;
        }
        try {
            spannable.setSpan(span, start, end, flags);
        } catch (IndexOutOfBoundsException e) {
            CrashUtil.logException(e, "We got an IOOB while setting a span. {} {} {}", spannable.toString(), Integer.valueOf(start), Integer.valueOf(end));
        }
    }

    public static boolean isPopularTimeline(int groupType) {
        return groupType == 5 || groupType == 8 || groupType == 4 || groupType == 11 || groupType == 14 || groupType == 15 || groupType == 30 || groupType == 36;
    }

    public static String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        StringBuilder formatBuilder = new StringBuilder();
        Formatter formatter = new Formatter(formatBuilder, Locale.getDefault());
        formatBuilder.setLength(0);
        return hours > 0 ? formatter.format("%d:%02d:%02d", Integer.valueOf(hours), Integer.valueOf(minutes), Integer.valueOf(seconds)).toString() : formatter.format("%d:%02d", Integer.valueOf(minutes), Integer.valueOf(seconds)).toString();
    }

    public static Spanned getSpannedText(Object[] span, String text, char marker) {
        int start = text.indexOf(marker);
        if (start == -1) {
            return new SpannableString(text);
        }
        int end = text.indexOf(marker, start + 1);
        if (end == -1) {
            return new SpannableString(text);
        }
        int maxSpans = span.length;
        int spanIndex = 0;
        int next = 0;
        int offset = 0;
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        do {
            ssb.append((CharSequence) text.substring(next, start));
            ssb.append((CharSequence) text.substring(start + 1, end));
            safeSetSpan(ssb, span[spanIndex], start - offset, (end - offset) - 1, 33);
            spanIndex++;
            next = end + 1;
            if (spanIndex >= maxSpans) {
                break;
            }
            start = text.indexOf(marker, next);
            if (start != -1) {
                end = text.indexOf(marker, start + 1);
                offset += 2;
            }
            if (start == -1) {
                break;
            }
        } while (end != -1);
        ssb.append((CharSequence) text.substring(next));
        return ssb;
    }

    public static String getRelativeTimeString(Context context, long timestamp, boolean verbose) {
        String quantityString;
        synchronized (DATE_FORMAT_LOCK) {
            Resources res = context.getResources();
            if (sDateFormatWithYear == null) {
                char[] order = null;
                try {
                    order = DateFormat.getDateFormatOrder(context);
                } catch (Exception e) {
                }
                Locale locale = Locale.getDefault();
                if (order == null || (locale != null && (locale.equals(Locale.KOREA) || locale.equals(Locale.KOREAN)))) {
                    sDateFormatNoYear = new SimpleDateFormat(res.getString(R.string.date_format_short));
                    sDateFormatWithYear = new SimpleDateFormat(res.getString(R.string.date_format_long));
                } else {
                    StringBuilder noYear = new StringBuilder();
                    StringBuilder withYear = new StringBuilder();
                    int i = 0;
                    for (char c : order) {
                        switch (c) {
                            case 'M':
                                noYear.append(c).append(c).append(c);
                                withYear.append(c).append(c).append(c);
                                if (i != order.length) {
                                    noYear.append(' ');
                                    withYear.append(' ');
                                    break;
                                } else {
                                    break;
                                }
                            case 'd':
                                noYear.append(c).append(c);
                                withYear.append(c).append(c);
                                if (i != order.length) {
                                    noYear.append(' ');
                                    withYear.append(' ');
                                    break;
                                } else {
                                    break;
                                }
                            case 'y':
                                withYear.append(c).append(c).append(c);
                                if (i != order.length) {
                                    withYear.append(' ');
                                    break;
                                } else {
                                    break;
                                }
                        }
                        i++;
                        sDateFormatNoYear = new SimpleDateFormat(noYear.toString());
                        sDateFormatWithYear = new SimpleDateFormat(withYear.toString());
                    }
                }
            }
            long diff = System.currentTimeMillis() - timestamp;
            if (diff < 0) {
                quantityString = sDateFormatWithYear.format(new Date(timestamp));
            } else if (diff < 60000) {
                int secs = (int) (diff / 1000);
                quantityString = res.getQuantityString(verbose ? R.plurals.time_secs_verbose_ago : R.plurals.time_secs, secs, Integer.valueOf(secs));
            } else if (diff < 3600000) {
                int mins = (int) (diff / 60000);
                quantityString = res.getQuantityString(verbose ? R.plurals.time_mins_verbose_ago : R.plurals.time_mins, mins, Integer.valueOf(mins));
            } else if (diff < 86400000) {
                int hours = (int) (diff / 3600000);
                quantityString = res.getQuantityString(verbose ? R.plurals.time_hours_verbose_ago : R.plurals.time_hours, hours, Integer.valueOf(hours));
            } else if (diff < 604800000) {
                int days = (int) (diff / 86400000);
                quantityString = res.getQuantityString(verbose ? R.plurals.time_days_verbose_ago : R.plurals.time_days, days, Integer.valueOf(days));
            } else {
                Calendar now = Calendar.getInstance();
                Calendar c2 = Calendar.getInstance();
                Date d = new Date(timestamp);
                c2.setTime(d);
                if (now.get(1) == c2.get(1)) {
                    quantityString = sDateFormatNoYear.format(d);
                } else {
                    quantityString = sDateFormatWithYear.format(d);
                }
            }
        }
        return quantityString;
    }

    public static String numberFormat(Resources res, long number, boolean shorten) {
        DECIMAL_FORMAT.setMaximumFractionDigits(1);
        if (shorten && !Locale.KOREA.getLanguage().equals(Locale.getDefault().getLanguage())) {
            if (number >= 1000000000) {
                float n = number / 1.0E9f;
                return String.format(res.getString(R.string.number_format_billions), DECIMAL_FORMAT.format(n));
            }
            if (number >= 1000000) {
                float n2 = number / 1000000.0f;
                return String.format(res.getString(R.string.number_format_millions), DECIMAL_FORMAT.format(n2));
            }
            if (number >= 10000) {
                float n3 = number / 1000;
                return String.format(res.getString(R.string.number_format_thousands), DECIMAL_FORMAT.format(n3));
            }
        }
        return NumberFormat.getInstance().format(number);
    }

    public static String numberFormat(Resources res, long number) {
        return numberFormat(res, number, true);
    }

    public static void styleSectionHeader(Context context, TextView sectionTitle, TextView sectionSort) {
        Typeface font = Typefaces.get(context).mediumContent;
        sectionTitle.setTypeface(font);
        sectionSort.setTypeface(font);
    }

    public static void validateAndFixEntities(ArrayList<VineEntity> entities) {
        Collections.sort(entities);
    }

    public static void adjustEntities(List<VineEntity> entities, Editable contentSb, int offset, boolean xmlEntity) {
        int directionalityOffset;
        for (int i = 0; i < contentSb.length(); i++) {
            char c = contentSb.charAt(i);
            if (Character.isHighSurrogate(c) || c == 8206 || c == 8207) {
                if (c == 8206 || c == 8207) {
                    directionalityOffset = 1;
                } else {
                    directionalityOffset = 0;
                }
                for (VineEntity entity : entities) {
                    if (!entity.adjusted) {
                        int startWithOffset = entity.start + offset + directionalityOffset;
                        int endWithOffset = entity.end + offset;
                        if (startWithOffset > i) {
                            if (startWithOffset < contentSb.length() - 1) {
                                entity.start++;
                            }
                            if (endWithOffset < contentSb.length()) {
                                entity.end++;
                            }
                        }
                    }
                }
            }
        }
        for (VineEntity entity2 : entities) {
            if (!entity2.adjusted) {
                if (entity2.title == null) {
                    entity2.adjusted = true;
                } else {
                    if (entity2.isUserType()) {
                        entity2.start += offset;
                        entity2.end += offset;
                        if (BuildUtil.isLogsOn()) {
                            contentSb.replace(entity2.start, entity2.end, entity2.title);
                        } else {
                            try {
                                contentSb.replace(entity2.start, entity2.end, entity2.title);
                            } catch (IndexOutOfBoundsException e) {
                                CrashUtil.logException(e, "Supressing IOOBE with replace", new Object[0]);
                            }
                        }
                        offset -= (entity2.end - entity2.start) - entity2.title.length();
                        entity2.end = entity2.start + entity2.title.length();
                    } else if (entity2.isTagType()) {
                        if (!entity2.title.startsWith("#")) {
                            entity2.title = "#" + entity2.title;
                        }
                        entity2.start += offset;
                        entity2.end += offset;
                        contentSb.replace(entity2.start, entity2.end, entity2.title);
                        offset = (offset - (entity2.end - entity2.start)) + entity2.title.length();
                        entity2.end = entity2.start + entity2.title.length();
                    } else if (entity2.isUserListType() || entity2.isCommentListType()) {
                        entity2.start += offset;
                        entity2.end += offset;
                        contentSb.replace(entity2.start, entity2.end, entity2.title);
                        offset = (offset - (entity2.end - entity2.start)) + entity2.title.length();
                        entity2.end = entity2.start + entity2.title.length();
                    }
                    entity2.adjusted = true;
                }
            }
        }
    }

    public static void safeSetDefaultAvatar(ImageView view, ProfileImageSize size, int color) {
        int res = -1;
        switch (size) {
            case SMALL:
                res = R.drawable.avatar_small;
                break;
            case MEDIUM:
                res = R.drawable.avatar_medium;
                break;
            case LARGE:
                res = R.drawable.avatar_large;
                break;
        }
        if (color == 0) {
            color = Settings.DEFAULT_PROFILE_COLOR;
        }
        int color2 = color | ViewCompat.MEASURED_STATE_MASK;
        try {
            view.setImageResource(res);
            view.setColorFilter(color2, PorterDuff.Mode.SRC_IN);
        } catch (OutOfMemoryError e) {
            CrashUtil.log("OOM has happened.");
        }
    }

    public static int getUserImageSize(Resources res) {
        return res.getDimensionPixelOffset(R.dimen.user_image_size);
    }

    public static VideoKey getVideoRequestKey(VinePost post, boolean forceLowKey, int avgSpeed) {
        List<VineVideoUrlTier> tieredUrls = post.getUrls();
        if (forceLowKey && post.videoLowUrl != null) {
            return new VideoKey(post.videoLowUrl);
        }
        if (tieredUrls != null && tieredUrls.size() > 0) {
            VineVideoUrlTier selectedTier = tieredUrls.get(0);
            for (VineVideoUrlTier tier : tieredUrls) {
                if (tier.rate > selectedTier.rate && avgSpeed >= tier.rate) {
                    selectedTier = tier;
                }
            }
            return new VideoKey(selectedTier.url);
        }
        return null;
    }

    public static String toPrettyComment(VineSingleNotification notification) {
        if (notification.prettyComment == null && !TextUtils.isEmpty(notification.getComment())) {
            SpannableStringBuilder ssb = new SpannableStringBuilder(notification.getComment());
            if (notification.getEntities() != null) {
                adjustEntities(notification.getEntities(), ssb, 0, true);
            }
            notification.prettyComment = ssb.toString();
        }
        return notification.prettyComment;
    }
}
