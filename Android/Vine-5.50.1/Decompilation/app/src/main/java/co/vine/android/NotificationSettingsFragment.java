package co.vine.android;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import co.vine.android.api.VineNotificationSetting;
import co.vine.android.client.AppSessionListener;
import co.vine.android.util.ViewUtil;
import com.twitter.android.widget.RefreshableListView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/* loaded from: classes.dex */
public class NotificationSettingsFragment extends BaseArrayListFragment {
    private int mBackgroundColor;
    private boolean mFetched = false;
    private boolean mSucceeded = false;

    @Override // co.vine.android.BaseArrayListFragment, co.vine.android.BaseAdapterFragment, co.vine.android.BaseControllerFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mRefreshable = false;
        setAppSessionListener(new NotificationSettingsListener());
        setHasOptionsMenu(true);
        this.mBackgroundColor = getArguments().getInt("color");
    }

    @Override // co.vine.android.BaseArrayListFragment, android.support.v4.app.Fragment
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View alertsRowHeader = LayoutInflater.from(getActivity()).inflate(R.layout.notification_alerts_row, (ViewGroup) null);
        alertsRowHeader.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.NotificationSettingsFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                Intent i = new Intent(NotificationSettingsFragment.this.getActivity(), (Class<?>) NotificationAlertsSettingsActivity.class);
                i.putExtra("color", NotificationSettingsFragment.this.mBackgroundColor);
                NotificationSettingsFragment.this.startActivity(i);
            }
        });
        this.mListView.addHeaderView(alertsRowHeader, null, false);
    }

    @Override // android.support.v4.app.Fragment
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (this.mSucceeded) {
            inflater.inflate(R.menu.save, menu);
            MenuItem menuItem = menu.findItem(R.id.done);
            menuItem.setEnabled(true);
            menu.findItem(R.id.done).setEnabled(true);
        }
    }

    @Override // co.vine.android.BaseArrayListFragment, co.vine.android.BaseAdapterFragment, co.vine.android.BaseControllerFragment, co.vine.android.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        this.mListView.setDividerHeight(0);
        ((RefreshableListView) this.mListView).disablePTR();
        if (!this.mFetched) {
            addRequest(this.mAppController.fetchNotificationSettings());
        }
    }

    private class NotificationSettingsListener extends AppSessionListener {
        private NotificationSettingsListener() {
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onGetNotificationSettingsComplete(String reqId, ArrayList<VineNotificationSetting> settings) {
            NotificationSettingsFragment.this.mFetched = true;
            PendingRequest request = NotificationSettingsFragment.this.removeRequest(reqId);
            if (request != null) {
                if (settings != null) {
                    NotificationSettingsFragment.this.mAdapter = NotificationSettingsFragment.this.new NotificationSettingsAdapter(NotificationSettingsFragment.this.getActivity(), settings);
                    NotificationSettingsFragment.this.mListView.setAdapter((ListAdapter) NotificationSettingsFragment.this.mAdapter);
                    NotificationSettingsFragment.this.mSucceeded = true;
                    NotificationSettingsFragment.this.getActivity().invalidateOptionsMenu();
                    return;
                }
                NotificationSettingsFragment.this.showSadface(true);
                NotificationSettingsFragment.this.mSucceeded = false;
            }
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onSaveNotificationSettingsComplete(String reqId, int statusCode, String reasonPhrase) {
            if (statusCode != 200) {
                if (TextUtils.isEmpty(reasonPhrase)) {
                    Toast.makeText(NotificationSettingsFragment.this.getActivity(), R.string.error_server, 1).show();
                    return;
                } else {
                    Toast.makeText(NotificationSettingsFragment.this.getActivity(), reasonPhrase, 1).show();
                    return;
                }
            }
            NotificationSettingsFragment.this.getActivity().finish();
        }
    }

    private class NotificationSettingsAdapter extends BaseAdapter {
        private Context mContext;
        private ArrayList<VineNotificationSetting> mSettings;

        public NotificationSettingsAdapter(Context context, ArrayList<VineNotificationSetting> settings) {
            this.mSettings = new ArrayList<>();
            this.mContext = context;
            this.mSettings = settings;
        }

        @Override // android.widget.Adapter
        public int getCount() {
            return this.mSettings.size();
        }

        @Override // android.widget.BaseAdapter, android.widget.Adapter
        public int getViewTypeCount() {
            return 2;
        }

        @Override // android.widget.BaseAdapter, android.widget.Adapter
        public int getItemViewType(int position) {
            return this.mSettings.get(position).isBooleanSetting ? 0 : 1;
        }

        @Override // android.widget.Adapter
        public Object getItem(int position) {
            return this.mSettings.get(position);
        }

        @Override // android.widget.Adapter
        public long getItemId(int position) {
            return position;
        }

        @Override // android.widget.Adapter
        public View getView(int position, View convertView, ViewGroup parent) throws Resources.NotFoundException {
            int layout = R.layout.notification_setting_check;
            int type = getItemViewType(position);
            if (convertView == null) {
                if (type == 1) {
                    layout = R.layout.notification_settings_spinner;
                }
                convertView = LayoutInflater.from(this.mContext).inflate(layout, (ViewGroup) null);
            }
            bindView(convertView, position);
            return convertView;
        }

        private void bindView(View view, int position) throws Resources.NotFoundException {
            final VineNotificationSetting setting = this.mSettings.get(position);
            final NotificationSettingsViewHolder h = new NotificationSettingsViewHolder(view);
            h.title.setText(setting.title);
            if (setting.isBooleanSetting) {
                setSelected(NotificationSettingsFragment.this.getResources(), "on".equals(setting.value), h.selectionIndicator);
                View.OnClickListener listener = new View.OnClickListener() { // from class: co.vine.android.NotificationSettingsFragment.NotificationSettingsAdapter.1
                    @Override // android.view.View.OnClickListener
                    public void onClick(View v) throws Resources.NotFoundException {
                        NotificationSettingsAdapter.this.setSelected(NotificationSettingsFragment.this.getResources(), !v.isSelected(), h.selectionIndicator);
                        setting.value = v.isSelected() ? "on" : "off";
                    }
                };
                h.selectionIndicator.setOnClickListener(listener);
                view.setOnClickListener(listener);
            } else {
                NotificationSettingsSpinnerAdapter spinnerAdapter = NotificationSettingsFragment.this.new NotificationSettingsSpinnerAdapter(this.mContext, setting);
                h.spinner.setAdapter((SpinnerAdapter) spinnerAdapter);
                h.spinner.setOnItemSelectedListener(spinnerAdapter);
                int i = 0;
                while (true) {
                    if (i >= setting.choices.size()) {
                        break;
                    }
                    if (!setting.choices.get(i).value.equals(setting.value)) {
                        i++;
                    } else {
                        h.spinner.setSelection(i);
                        break;
                    }
                }
            }
            if (shouldShowHeader(position)) {
                h.header.setVisibility(0);
                h.headerText.setText(setting.section);
            } else {
                h.header.setVisibility(8);
            }
        }

        private boolean shouldShowHeader(int position) {
            if (position == 0) {
                return true;
            }
            VineNotificationSetting currentSetting = this.mSettings.get(position);
            VineNotificationSetting previousSetting = this.mSettings.get(position - 1);
            return (TextUtils.isEmpty(currentSetting.section) || currentSetting.section.equals(previousSetting.section)) ? false : true;
        }

        public void setSelected(Resources res, boolean selected, View view) throws Resources.NotFoundException {
            if (selected) {
                Drawable bg = res.getDrawable(R.drawable.ic_circle_selected).mutate();
                int color = res.getColor(R.color.vine_green);
                bg.setColorFilter(new PorterDuffColorFilter((-16777216) | color, PorterDuff.Mode.SRC_IN));
                ViewUtil.setBackground(view, bg);
            } else {
                Drawable bg2 = res.getDrawable(R.drawable.ic_circle_default);
                bg2.setColorFilter(new PorterDuffColorFilter(res.getColor(R.color.btn_circle_stroke), PorterDuff.Mode.SRC_IN));
                ViewUtil.setBackground(view, bg2);
            }
            view.setSelected(selected);
        }

        public HashMap<String, String> getMap() {
            HashMap<String, String> map = new HashMap<>();
            Iterator<VineNotificationSetting> it = this.mSettings.iterator();
            while (it.hasNext()) {
                VineNotificationSetting setting = it.next();
                map.put(setting.name, setting.value);
            }
            return map;
        }

        class NotificationSettingsViewHolder {
            View header;
            TextView headerText;
            View selectionIndicator;
            Spinner spinner;
            TextView title;

            public NotificationSettingsViewHolder(View view) {
                this.header = view.findViewById(R.id.header);
                this.headerText = (TextView) view.findViewById(R.id.header_text);
                this.title = (TextView) view.findViewById(R.id.title);
                this.spinner = (Spinner) view.findViewById(R.id.spinner);
                this.selectionIndicator = view.findViewById(R.id.selection_indicator);
            }
        }
    }

    private class NotificationSettingsSpinnerAdapter extends BaseAdapter implements AdapterView.OnItemSelectedListener, SpinnerAdapter {
        private Context mContext;
        private VineNotificationSetting mSetting;

        public NotificationSettingsSpinnerAdapter(Context context, VineNotificationSetting setting) {
            this.mSetting = setting;
            this.mContext = context;
        }

        @Override // android.widget.Adapter
        public int getCount() {
            return this.mSetting.choices.size();
        }

        @Override // android.widget.Adapter
        public Object getItem(int position) {
            return this.mSetting.choices.get(position);
        }

        @Override // android.widget.Adapter
        public long getItemId(int position) {
            return position;
        }

        @Override // android.widget.Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            View res;
            VineNotificationSetting.Choice choice = this.mSetting.choices.get(position);
            if (convertView != null) {
                res = convertView;
            } else {
                res = LayoutInflater.from(this.mContext).inflate(R.layout.settings_spinner_row_item, (ViewGroup) null);
            }
            TextView t = (TextView) res.findViewById(R.id.title);
            t.setText((!TextUtils.isEmpty(choice.title) || TextUtils.isEmpty(choice.value)) ? choice.title : choice.value);
            t.setSingleLine(false);
            res.invalidate();
            return res;
        }

        @Override // android.widget.AdapterView.OnItemSelectedListener
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            this.mSetting.value = this.mSetting.choices.get(position).value;
            parent.setSelection(position);
        }

        @Override // android.widget.AdapterView.OnItemSelectedListener
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }

    @Override // android.support.v4.app.Fragment
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != R.id.done) {
            return super.onOptionsItemSelected(item);
        }
        if (this.mAdapter != null) {
            HashMap<String, String> map = ((NotificationSettingsAdapter) this.mAdapter).getMap();
            addRequest(this.mAppController.saveNotificationSettings(map));
        }
        return true;
    }
}
