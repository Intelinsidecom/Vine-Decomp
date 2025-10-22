package co.vine.android.util;

import android.content.res.Resources;
import android.text.SpannableStringBuilder;
import android.text.style.TypefaceSpan;
import co.vine.android.R;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;

/* loaded from: classes.dex */
public class VineDateFormatter {
    private final SimpleDateFormat mDateFormatLong;
    private final SimpleDateFormat mDateFormatShort;
    private final HashSet<TypefaceSpan> mDateSectionSpans;
    private final String mDateTimePlacerPart1;
    private final boolean mIsDateBeforeTime;
    private Calendar mStartOfToday;
    private long mStartOfYesterday;
    private final SimpleDateFormat mTimeFormat;
    private final HashSet<TypefaceSpan> mTimeSectionSpans;
    private final String mYesterday;

    public VineDateFormatter(Resources res) throws Resources.NotFoundException {
        this.mTimeFormat = new SimpleDateFormat(res.getString(R.string.vm_time_format));
        String dateTimePlacer = res.getString(R.string.vm_date_format);
        int dateTimePlacerStart1 = dateTimePlacer.indexOf("%1$s");
        int dateTimePlacerStart2 = dateTimePlacer.indexOf("%2$s");
        this.mIsDateBeforeTime = dateTimePlacerStart1 < dateTimePlacerStart2;
        if (this.mIsDateBeforeTime) {
            this.mDateTimePlacerPart1 = dateTimePlacer.substring(dateTimePlacerStart1, dateTimePlacerStart2);
        } else {
            this.mDateTimePlacerPart1 = dateTimePlacer.substring(dateTimePlacerStart1);
        }
        this.mDateFormatLong = new SimpleDateFormat(res.getString(R.string.date_format_long));
        this.mDateFormatShort = new SimpleDateFormat(res.getString(R.string.date_format_short));
        this.mDateSectionSpans = new HashSet<>();
        this.mTimeSectionSpans = new HashSet<>();
        this.mYesterday = res.getString(R.string.yesterday);
    }

    public void refreshDates() {
        this.mStartOfToday = DateTimeUtil.getStartOfDay();
        this.mStartOfYesterday = this.mStartOfToday.getTimeInMillis() - 86400000;
    }

    public CharSequence format(long time) {
        Date createdDate = new Date(time);
        Calendar createdCal = Calendar.getInstance();
        createdCal.setTimeInMillis(time);
        if (time > this.mStartOfToday.getTimeInMillis()) {
            return this.mTimeFormat.format(createdDate);
        }
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        if (this.mIsDateBeforeTime) {
            if (time > this.mStartOfYesterday) {
                ssb.append((CharSequence) this.mDateTimePlacerPart1.replace("%1$s", this.mYesterday));
            } else if (createdCal.get(1) == this.mStartOfToday.get(1)) {
                ssb.append((CharSequence) this.mDateTimePlacerPart1.replace("%1$s", this.mDateFormatShort.format(createdDate)));
            } else {
                ssb.append((CharSequence) this.mDateTimePlacerPart1.replace("%1$s", this.mDateFormatLong.format(createdDate)));
            }
            int end1 = ssb.length();
            ssb.append((CharSequence) this.mTimeFormat.format(createdDate));
            int end2 = ssb.length();
            Iterator<TypefaceSpan> it = this.mDateSectionSpans.iterator();
            while (it.hasNext()) {
                TypefaceSpan span = it.next();
                Util.safeSetSpan(ssb, span, 0, end1, 33);
            }
            Iterator<TypefaceSpan> it2 = this.mTimeSectionSpans.iterator();
            while (it2.hasNext()) {
                TypefaceSpan span2 = it2.next();
                Util.safeSetSpan(ssb, span2, end1, end2, 33);
            }
            return ssb;
        }
        ssb.append((CharSequence) this.mTimeFormat.format(createdDate));
        int start = ssb.length();
        if (time > this.mStartOfYesterday) {
            ssb.append((CharSequence) this.mDateTimePlacerPart1.replace("%1$s", this.mYesterday));
        } else if (createdCal.get(1) == this.mStartOfToday.get(1)) {
            ssb.append((CharSequence) this.mDateTimePlacerPart1.replace("%1$s", this.mDateFormatShort.format(createdDate)));
        } else {
            ssb.append((CharSequence) this.mDateTimePlacerPart1.replace("%1$s", this.mDateFormatLong.format(createdDate)));
        }
        int end = ssb.length();
        Iterator<TypefaceSpan> it3 = this.mDateSectionSpans.iterator();
        while (it3.hasNext()) {
            TypefaceSpan span3 = it3.next();
            Util.safeSetSpan(ssb, span3, start, end, 33);
        }
        Iterator<TypefaceSpan> it4 = this.mTimeSectionSpans.iterator();
        while (it4.hasNext()) {
            TypefaceSpan span4 = it4.next();
            Util.safeSetSpan(ssb, span4, 0, start, 33);
        }
        return ssb;
    }

    public void addSpanForDateSection(TypefaceSpan span) {
        this.mDateSectionSpans.add(span);
    }
}
