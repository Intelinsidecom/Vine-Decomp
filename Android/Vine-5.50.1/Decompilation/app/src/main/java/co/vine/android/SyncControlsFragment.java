package co.vine.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import co.vine.android.prefetch.PrefetchManager;
import co.vine.android.util.Util;
import co.vine.android.views.SwitchInterface;
import co.vine.android.widgets.PromptDialogFragment;
import com.edisonwang.android.slog.SLog;

/* loaded from: classes.dex */
public class SyncControlsFragment extends BaseControllerFragment {
    private static final long[] INTERVALS = PrefetchManager.INTERVALS;
    private SwitchInterface mChargingRequiredSwitcher;
    private TextView mIntervalDescription;
    private String[] mIntervalStrings;
    private PrefetchManager mPrefetchManager;
    private SwitchInterface mRoamingAllowedSwitcher;
    private TextView mSyncDescription;
    private SwitchInterface mSyncOnOffSwitcher;
    private SwitchInterface mWifiOnlySwitcher;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private final PrefetchManager.PrefetchFilter mFilter = PrefetchManager.getStateChangeFilter();
    private int mNext = -2;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: co.vine.android.SyncControlsFragment.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) throws Resources.NotFoundException {
            int next = SyncControlsFragment.this.mFilter.getScheduledTimeInSeconds(intent.getExtras());
            if (next >= -1) {
                SyncControlsFragment.this.mNext = next;
            }
            if (SyncControlsFragment.this.mFilter.isEnd(intent.getAction())) {
                SyncControlsFragment.this.mHandler.postDelayed(new Runnable() { // from class: co.vine.android.SyncControlsFragment.1.1
                    @Override // java.lang.Runnable
                    public void run() throws Resources.NotFoundException {
                        SyncControlsFragment.this.updateSyncDescription();
                    }
                }, 2000L);
            } else {
                SyncControlsFragment.this.updateSyncDescription();
            }
        }
    };

    @Override // co.vine.android.BaseControllerFragment, co.vine.android.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        try {
            getActivity().registerReceiver(this.mReceiver, this.mFilter);
        } catch (Exception e) {
            SLog.e("Receiver may have already been registered.", (Throwable) e);
        }
    }

    @Override // co.vine.android.BaseControllerFragment, android.support.v4.app.Fragment
    public void onPause() {
        super.onPause();
        try {
            getActivity().unregisterReceiver(this.mReceiver);
        } catch (Exception e) {
        }
    }

    @Override // android.support.v4.app.Fragment
    public void onActivityCreated(Bundle savedInstanceState) throws Resources.NotFoundException {
        super.onActivityCreated(savedInstanceState);
        initPrefetchManager();
        initIntervalArray(getActivity());
        this.mSyncOnOffSwitcher.setChecked(isEnabled());
        this.mChargingRequiredSwitcher.setChecked(isChargingRequired());
        this.mWifiOnlySwitcher.setChecked(isWifiOnly());
        this.mRoamingAllowedSwitcher.setChecked(isRoamingAllowed());
        updateSyncDescription();
        updateIntervalText();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateIntervalText() throws Resources.NotFoundException {
        String text;
        FragmentActivity activity = getActivity();
        if (activity != null) {
            Resources res = activity.getResources();
            int minutes = (int) (getInterval() / 60000);
            if (minutes >= 60) {
                text = res.getQuantityString(R.plurals.time_hours_verbose, minutes / 60, Integer.valueOf(minutes / 60));
            } else {
                text = res.getQuantityString(R.plurals.time_mins_verbose, minutes, Integer.valueOf(minutes));
            }
            this.mIntervalDescription.setText(res.getString(R.string.sync_interval_desc, text));
        }
    }

    private void initIntervalArray(FragmentActivity activity) {
        if (this.mIntervalStrings == null) {
            this.mIntervalStrings = new String[INTERVALS.length];
            Resources res = activity.getResources();
            for (int i = 0; i < INTERVALS.length; i++) {
                int minutes = (int) (INTERVALS[i] / 60000);
                if (minutes >= 60) {
                    this.mIntervalStrings[i] = res.getString(R.string.sync_interval_desc, res.getQuantityString(R.plurals.time_hours_verbose, minutes / 60, Integer.valueOf(minutes / 60)));
                } else {
                    this.mIntervalStrings[i] = res.getString(R.string.sync_interval_desc, res.getQuantityString(R.plurals.time_mins_verbose, minutes, Integer.valueOf(minutes)));
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSyncDescription() throws Resources.NotFoundException {
        FragmentActivity activity = getActivity();
        if (this.mSyncDescription != null && this.mPrefetchManager != null && activity != null) {
            if (this.mPrefetchManager.isSyncPending()) {
                this.mSyncDescription.setText(activity.getString(R.string.sync_now_pending));
                return;
            }
            long startTime = this.mPrefetchManager.getCurrentSyncStartTime();
            if (startTime > 0) {
                this.mSyncDescription.setText(activity.getString(R.string.sync_now_in_progress));
                return;
            }
            long lastSync = this.mPrefetchManager.getLastSync();
            String text = null;
            if (lastSync > 0) {
                text = activity.getResources().getString(R.string.sync_now_desc, Util.getRelativeTimeString(activity, lastSync, true));
            }
            if (SLog.sLogsOn) {
                long next = this.mPrefetchManager.getNextSync();
                if (next > -1) {
                    this.mNext = (int) (next - System.currentTimeMillis());
                } else {
                    this.mNext = (int) next;
                }
                if (this.mNext >= 0) {
                    if (text == null) {
                        text = "";
                    }
                    text = text + " , next sync: " + (this.mNext / 60000) + "m later";
                }
            }
            this.mSyncDescription.setText(text);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean initPrefetchManager() {
        if (this.mPrefetchManager == null) {
            VineApplication va = VineApplication.getInstance();
            if (va != null) {
                this.mPrefetchManager = PrefetchManager.getInstance(va.getApplicationContext());
            } else {
                FragmentActivity activity = getActivity();
                if (activity != null) {
                    this.mPrefetchManager = PrefetchManager.getInstance(activity);
                }
            }
        }
        return this.mPrefetchManager != null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isEnabled() {
        return this.mPrefetchManager != null && this.mPrefetchManager.isEnabled();
    }

    private long getInterval() {
        if (this.mPrefetchManager != null) {
            return this.mPrefetchManager.getInterval();
        }
        return -1L;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isWifiOnly() {
        return this.mPrefetchManager == null || this.mPrefetchManager.isWifiOnly();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isRoamingAllowed() {
        return this.mPrefetchManager == null || this.mPrefetchManager.isRoamingAllowed();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isChargingRequired() {
        return this.mPrefetchManager == null || this.mPrefetchManager.isChargingRequired();
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup vg = (ViewGroup) inflater.inflate(R.layout.settings_sub, container, false);
        this.mSyncOnOffSwitcher = addSwitcher(inflater, vg, R.string.sync_enabled, R.string.sync_enabled_desc, new CompoundButton.OnCheckedChangeListener() { // from class: co.vine.android.SyncControlsFragment.2
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) throws Resources.NotFoundException {
                SyncControlsFragment.this.initPrefetchManager();
                if (isChecked != SyncControlsFragment.this.isEnabled() && SyncControlsFragment.this.mPrefetchManager != null) {
                    SyncControlsFragment.this.mPrefetchManager.setEnabled(isChecked);
                    SyncControlsFragment.this.updateSyncDescription();
                }
            }
        });
        View intervalView = addText(inflater, vg, R.string.sync_interval, R.string.sync_interval_desc);
        this.mIntervalDescription = (TextView) intervalView.findViewById(R.id.description);
        intervalView.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.SyncControlsFragment.3
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                FragmentActivity activity = SyncControlsFragment.this.getActivity();
                if (activity != null && SyncControlsFragment.this.mIntervalStrings != null) {
                    PromptDialogFragment p = PromptDialogFragment.newInstance(1);
                    p.setItems(SyncControlsFragment.this.mIntervalStrings);
                    p.setListener(new PromptDialogFragment.OnDialogDoneListener() { // from class: co.vine.android.SyncControlsFragment.3.1
                        @Override // co.vine.android.widgets.PromptDialogFragment.OnDialogDoneListener
                        public void onDialogDone(DialogInterface dialog, int id, int which) throws Resources.NotFoundException {
                            if (which >= 0 && id == 1 && which < SyncControlsFragment.INTERVALS.length) {
                                if (SyncControlsFragment.this.mPrefetchManager != null) {
                                    SyncControlsFragment.this.mPrefetchManager.setInterval(SyncControlsFragment.INTERVALS[which]);
                                    SyncControlsFragment.this.mPrefetchManager.cancelNextPrefetch();
                                    SyncControlsFragment.this.mPrefetchManager.scheduleNextPrefetch(true);
                                }
                                SyncControlsFragment.this.updateIntervalText();
                            }
                        }
                    });
                    p.show(activity.getFragmentManager());
                }
            }
        });
        this.mChargingRequiredSwitcher = addSwitcher(inflater, vg, R.string.while_charging_only, R.string.while_charging_only_desc, new CompoundButton.OnCheckedChangeListener() { // from class: co.vine.android.SyncControlsFragment.4
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SyncControlsFragment.this.initPrefetchManager();
                if (isChecked != SyncControlsFragment.this.isChargingRequired()) {
                    if (SyncControlsFragment.this.mPrefetchManager != null) {
                        SyncControlsFragment.this.mPrefetchManager.setChargingRequired(isChecked);
                    } else {
                        buttonView.setChecked(SyncControlsFragment.this.isChargingRequired());
                    }
                }
            }
        });
        this.mWifiOnlySwitcher = addSwitcher(inflater, vg, R.string.while_wifi_only, R.string.while_wifi_only_desc, new CompoundButton.OnCheckedChangeListener() { // from class: co.vine.android.SyncControlsFragment.5
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SyncControlsFragment.this.initPrefetchManager();
                if (isChecked != SyncControlsFragment.this.isWifiOnly()) {
                    if (SyncControlsFragment.this.mPrefetchManager != null) {
                        SyncControlsFragment.this.mPrefetchManager.setWifiOnly(isChecked);
                    } else {
                        buttonView.setChecked(SyncControlsFragment.this.isWifiOnly());
                    }
                }
            }
        });
        this.mRoamingAllowedSwitcher = addSwitcher(inflater, vg, R.string.roaming, R.string.roaming_desc, new CompoundButton.OnCheckedChangeListener() { // from class: co.vine.android.SyncControlsFragment.6
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SyncControlsFragment.this.initPrefetchManager();
                if (isChecked != SyncControlsFragment.this.isRoamingAllowed()) {
                    if (SyncControlsFragment.this.mPrefetchManager != null) {
                        SyncControlsFragment.this.mPrefetchManager.setRoamingAllowed(isChecked);
                    } else {
                        buttonView.setChecked(SyncControlsFragment.this.isRoamingAllowed());
                    }
                }
            }
        });
        ViewGroup doitNow = (ViewGroup) addText(inflater, vg, R.string.sync_now, R.string.sync_now_never);
        doitNow.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.SyncControlsFragment.7
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SyncControlsFragment.this.initPrefetchManager();
                if (SyncControlsFragment.this.mPrefetchManager != null) {
                    SyncControlsFragment.this.mPrefetchManager.run(0L);
                    SyncControlsFragment.this.mHandler.postDelayed(new Runnable() { // from class: co.vine.android.SyncControlsFragment.7.1
                        @Override // java.lang.Runnable
                        public void run() throws Resources.NotFoundException {
                            SyncControlsFragment.this.updateSyncDescription();
                        }
                    }, 100L);
                }
            }
        });
        this.mSyncDescription = (TextView) doitNow.findViewById(R.id.description);
        return vg;
    }

    private View addText(LayoutInflater inflater, ViewGroup parent, int labelString, int descString) {
        View view = inflater.inflate(R.layout.settings_text_row, parent, false);
        ((TextView) view.findViewById(R.id.label)).setText(labelString);
        ((TextView) view.findViewById(R.id.description)).setText(descString);
        parent.addView(view);
        inflater.inflate(R.layout.settings_sub_divider, parent, true);
        return view;
    }

    private SwitchInterface addSwitcher(LayoutInflater inflater, ViewGroup parent, int labelString, int descString, CompoundButton.OnCheckedChangeListener onCheckedChangeListener) {
        View view = inflater.inflate(R.layout.settings_switch_row, parent, false);
        SwitchInterface switcher = (SwitchInterface) view.findViewById(R.id.switcher);
        switcher.setOnCheckedChangeListener(onCheckedChangeListener);
        ((TextView) view.findViewById(R.id.label)).setText(labelString);
        ((TextView) view.findViewById(R.id.description)).setText(descString);
        parent.addView(view);
        inflater.inflate(R.layout.settings_sub_divider, parent, true);
        return switcher;
    }
}
