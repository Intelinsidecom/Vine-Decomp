package co.vine.android.plugin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import co.vine.android.recorder.VineRecorder;

/* loaded from: classes.dex */
public class TimeLapsePlugin extends BaseRecorderPlugin<View, VineRecorder> {
    private double mTimeLapseDelay;
    private final Runnable mTimeLapseRunnable;
    private boolean mTimelapsing;

    public TimeLapsePlugin() {
        super("TimeLapse");
        this.mTimelapsing = false;
        this.mTimeLapseRunnable = new Runnable() { // from class: co.vine.android.plugin.TimeLapsePlugin.1
            @Override // java.lang.Runnable
            public void run() {
                Handler handler = TimeLapsePlugin.this.getHandler();
                VineRecorder recorder = TimeLapsePlugin.this.getRecorder();
                if (handler != null && recorder != null && recorder.isWithinDurationPercentageOf(0.95d)) {
                    recorder.doOneFrame();
                    handler.postDelayed(this, (long) TimeLapsePlugin.this.mTimeLapseDelay);
                }
            }
        };
    }

    public void startTimeLapse(double sec) {
        this.mTimeLapseDelay = 1000.0d * sec;
        this.mTimelapsing = true;
        this.mTimeLapseRunnable.run();
    }

    @Override // co.vine.android.plugin.BaseRecorderPlugin
    public View onLayout(ViewGroup parent, LayoutInflater inflater, Fragment fragment) {
        return null;
    }

    @Override // co.vine.android.plugin.BaseRecorderPlugin, co.vine.android.plugin.RecorderPlugin
    public boolean onSetEditMode(boolean editing, boolean hasData) {
        Handler handler = getHandler();
        if (this.mTimelapsing && handler != null) {
            this.mTimelapsing = false;
            handler.removeCallbacks(this.mTimeLapseRunnable);
            return true;
        }
        if (hasData) {
            return false;
        }
        showTimeLapseDialog();
        return false;
    }

    public void showTimeLapseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.mActivity);
        builder.setTitle("Time Lapse Mode");
        LinearLayout ll = new LinearLayout(this.mActivity);
        final EditText et = new EditText(this.mActivity);
        et.setInputType(8194);
        et.setHint("time in seconds, decimals OK.");
        ll.addView(et);
        builder.setView(ll);
        builder.setPositiveButton("Start", new DialogInterface.OnClickListener() { // from class: co.vine.android.plugin.TimeLapsePlugin.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                VineRecorder recorder = TimeLapsePlugin.this.getRecorder();
                if (recorder != null && recorder.setAudioTrim(true)) {
                    TimeLapsePlugin.this.startTimeLapse(Double.parseDouble(et.getText().toString()));
                }
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() { // from class: co.vine.android.plugin.TimeLapsePlugin.3
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
