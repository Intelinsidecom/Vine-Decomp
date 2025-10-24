package co.vine.android.util.analytics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import co.vine.android.prefetch.PrefetchManager;
import co.vine.android.util.SystemUtil;
import com.edisonwang.android.slog.MessageFormatter;

/* loaded from: classes.dex */
public class BehaviorUsageView extends View {
    private static final String[] DAYS = {"SUN", "MON", "TUE", "WED", "THR", "FRI", "SAT"};
    private float mDeltaY;
    private final BehaviorManager mManager;
    private final Paint mPaint;
    private Point mScreenSize;
    private final String[] mStats;
    private int[] mUsagesByDayOfWeek;
    private long[] mUsagesByDayOfWeekOriginal;
    private int[] mUsagesByHourOfDay;
    private long[] mUsagesByHourOfDayOriginal;
    private String[] mUsagesByTimeline;

    public BehaviorUsageView(Context context) {
        super(context);
        this.mManager = BehaviorManager.getInstance(context);
        this.mStats = PrefetchManager.getInstance(context).getStats();
        this.mPaint = initialize();
    }

    private Paint initialize() {
        this.mUsagesByHourOfDayOriginal = this.mManager.getUsagesByHourOfDay();
        this.mUsagesByHourOfDay = normalize(this.mUsagesByHourOfDayOriginal);
        this.mUsagesByDayOfWeekOriginal = this.mManager.getUsagesByDateOfWeek();
        this.mUsagesByDayOfWeek = normalize(this.mUsagesByDayOfWeekOriginal);
        this.mUsagesByTimeline = this.mManager.getUsagesStringsByTimelineForStatView();
        Paint paint = new Paint(1);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(30.0f);
        this.mDeltaY = paint.measureText("%") * 1.5f;
        this.mScreenSize = SystemUtil.getDisplaySize(getContext());
        return paint;
    }

    private int[] normalize(long[] usages) {
        long sum = 0;
        for (long usage : usages) {
            sum += usage;
        }
        int[] percentages = new int[usages.length];
        for (int i = 0; i < usages.length; i++) {
            if (sum != 0) {
                percentages[i] = (int) ((usages[i] * 100) / sum);
            }
        }
        return percentages;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        getHandler().post(new Runnable() { // from class: co.vine.android.util.analytics.BehaviorUsageView.1
            @Override // java.lang.Runnable
            public void run() {
                ((ViewGroup) BehaviorUsageView.this.getParent()).removeView(BehaviorUsageView.this);
            }
        });
        return super.onTouchEvent(event);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(-1);
        canvas.drawText("Usages by hour of day: ", 10.0f, 50, this.mPaint);
        int y = (int) (50 + this.mDeltaY);
        for (int i = 0; i < 23; i++) {
            String text = MessageFormatter.toStringMessage("{}:00: {}% ({})", Integer.valueOf(i), Integer.valueOf(this.mUsagesByHourOfDay[i]), Long.valueOf(this.mUsagesByHourOfDayOriginal[i]));
            canvas.drawText(text, 10.0f, y, this.mPaint);
            y = (int) (y + this.mDeltaY);
        }
        int lastY = y;
        canvas.drawText("Usages by day of week: ", this.mScreenSize.x / 2, 50, this.mPaint);
        int y2 = (int) (50 + this.mDeltaY);
        for (int i2 = 0; i2 < 7; i2++) {
            String text2 = MessageFormatter.toStringMessage("{}: {}% ({})", DAYS[i2], Integer.valueOf(this.mUsagesByDayOfWeek[i2]), Long.valueOf(this.mUsagesByDayOfWeekOriginal[i2]));
            canvas.drawText(text2, this.mScreenSize.x / 2, y2, this.mPaint);
            y2 = (int) (y2 + this.mDeltaY);
        }
        int y3 = (int) (y2 + (this.mDeltaY * 5.0f));
        for (String stat : this.mStats) {
            canvas.drawText(stat, this.mScreenSize.x / 2, y3, this.mPaint);
            y3 = (int) (y3 + this.mDeltaY);
        }
        int lastY2 = (int) (Math.max(lastY, y3) + (this.mDeltaY * 5.0f));
        for (String s : this.mUsagesByTimeline) {
            canvas.drawText(s, 0.0f, lastY2, this.mPaint);
            lastY2 = (int) (lastY2 + this.mDeltaY);
        }
    }
}
