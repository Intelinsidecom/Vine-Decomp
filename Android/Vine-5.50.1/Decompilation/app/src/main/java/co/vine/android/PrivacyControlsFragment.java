package co.vine.android;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import co.vine.android.client.AppSessionListener;
import co.vine.android.util.Util;
import co.vine.android.views.SwitchInterface;

/* loaded from: classes.dex */
public class PrivacyControlsFragment extends BaseControllerFragment {
    private boolean mAcceptOon;
    private boolean mAllowAddressBook;
    private boolean mEmailDiscoverable;
    private boolean mPhoneDiscoverable;
    private boolean mTwitterDiscoverable;

    @Override // co.vine.android.BaseControllerFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAppSessionListener(new ContentControlsListener());
        if (savedInstanceState != null) {
            this.mAcceptOon = savedInstanceState.getBoolean("state_accept_oon", false);
            this.mAllowAddressBook = savedInstanceState.getBoolean("state_address_book", false);
            this.mEmailDiscoverable = savedInstanceState.getBoolean("state_email_discoverable", true);
            this.mPhoneDiscoverable = savedInstanceState.getBoolean("state_phone_discoverable", true);
            this.mTwitterDiscoverable = savedInstanceState.getBoolean("state_twitter_discoverable", true);
            return;
        }
        SharedPreferences prefs = Util.getDefaultSharedPrefs(getActivity());
        this.mAcceptOon = prefs.getBoolean("accept_out_of_network_messages", true);
        this.mAllowAddressBook = prefs.getBoolean("enable_address_book", false);
        this.mEmailDiscoverable = prefs.getBoolean("email_discoverable", true);
        this.mPhoneDiscoverable = prefs.getBoolean("phone_discoverable", true);
        this.mTwitterDiscoverable = prefs.getBoolean("twitter_discoverable", true);
    }

    @Override // android.support.v4.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("state_accept_oon", this.mAcceptOon);
        outState.putBoolean("state_address_book", this.mAllowAddressBook);
        outState.putBoolean("state_email_discoverable", this.mEmailDiscoverable);
        outState.putBoolean("state_phone_discoverable", this.mPhoneDiscoverable);
        outState.putBoolean("state_twitter_discoverable", this.mTwitterDiscoverable);
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup vg = (ViewGroup) inflater.inflate(R.layout.settings_sub, container, false);
        addSwitcher(inflater, vg, R.string.settings_accepts_oon, R.string.settings_accepts_oon_desc, this.mAcceptOon, 3);
        addSwitcher(inflater, vg, R.string.settings_address_book, R.string.settings_address_book_desc, this.mAllowAddressBook, 4);
        addSwitcher(inflater, vg, R.string.email_discoverability, R.string.email_discoverability_desc, this.mEmailDiscoverable, 0);
        addSwitcher(inflater, vg, R.string.phone_discoverability, R.string.phone_discoverability_desc, this.mPhoneDiscoverable, 1);
        addSwitcher(inflater, vg, R.string.show_twitter_handle, R.string.show_twitter_handle_desc, this.mTwitterDiscoverable, 2);
        return vg;
    }

    private void addSwitcher(LayoutInflater inflater, ViewGroup parent, int labelString, int descString, boolean initialCondition, int type) {
        View view = inflater.inflate(R.layout.settings_switch_row, parent, false);
        SwitchInterface switcher = (SwitchInterface) view.findViewById(R.id.switcher);
        if (initialCondition) {
            switcher.setChecked(true);
        }
        switcher.setOnCheckedChangeListener(new PrivacyControlOnCheckChangedListener(type));
        ((TextView) view.findViewById(R.id.label)).setText(labelString);
        ((TextView) view.findViewById(R.id.description)).setText(descString);
        parent.addView(view);
        inflater.inflate(R.layout.settings_sub_divider, parent, true);
    }

    class PrivacyControlOnCheckChangedListener implements CompoundButton.OnCheckedChangeListener {
        private final int mActionCode;

        private PrivacyControlOnCheckChangedListener(int actionCode) {
            this.mActionCode = actionCode;
        }

        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (this.mActionCode == 3) {
                PrivacyControlsFragment.this.mAcceptOon = isChecked;
                PrivacyControlsFragment.this.mAppController.updateAcceptOon(PrivacyControlsFragment.this.mAcceptOon);
                return;
            }
            if (this.mActionCode == 4) {
                PrivacyControlsFragment.this.mAllowAddressBook = isChecked;
                PrivacyControlsFragment.this.mAppController.updateEnableAddressBook(PrivacyControlsFragment.this.mAllowAddressBook);
                return;
            }
            if (this.mActionCode == 0) {
                PrivacyControlsFragment.this.mEmailDiscoverable = isChecked;
                PrivacyControlsFragment.this.mAppController.updateDiscoverability(0, PrivacyControlsFragment.this.mEmailDiscoverable);
            } else if (this.mActionCode == 1) {
                PrivacyControlsFragment.this.mPhoneDiscoverable = isChecked;
                PrivacyControlsFragment.this.mAppController.updateDiscoverability(1, PrivacyControlsFragment.this.mPhoneDiscoverable);
            } else if (this.mActionCode == 2) {
                PrivacyControlsFragment.this.mTwitterDiscoverable = isChecked;
                PrivacyControlsFragment.this.mAppController.updateDiscoverability(2, PrivacyControlsFragment.this.mTwitterDiscoverable);
            }
        }
    }

    class ContentControlsListener extends AppSessionListener {
        ContentControlsListener() {
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onUpdateAcceptOonComplete(String reqId, int statusCode, String reasonPhrase, boolean acceptOon) {
            Activity activity = PrivacyControlsFragment.this.getActivity();
            if (statusCode == 200) {
                SharedPreferences.Editor editor = Util.getDefaultSharedPrefs(activity).edit();
                editor.putBoolean("accept_out_of_network_messages", acceptOon);
                editor.apply();
                return;
            }
            Util.showCenteredToast(activity, R.string.privacy_controls_update_fail);
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onUpdateEnableAddressBookComplete(String reqId, int statusCode, String reasonPhrase, boolean enableAddressBook) {
            Activity activity = PrivacyControlsFragment.this.getActivity();
            if (statusCode == 200) {
                SharedPreferences.Editor editor = Util.getDefaultSharedPrefs(PrivacyControlsFragment.this.getActivity()).edit();
                editor.putBoolean("enable_address_book", enableAddressBook);
                editor.apply();
                if (enableAddressBook) {
                    PrivacyControlsFragment.this.mAppController.sendAddressBook();
                    return;
                }
                return;
            }
            Util.showCenteredToast(activity, R.string.privacy_controls_update_fail);
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onUpdateDiscoverability(String reqId, int statusCode, String reasonPhrase, int type, boolean enable) {
            String pref;
            Activity activity = PrivacyControlsFragment.this.getActivity();
            if (statusCode == 200) {
                if (type == 0) {
                    pref = "email_discoverable";
                } else if (type == 1) {
                    pref = "phone_discoverable";
                } else {
                    pref = "twitter_discoverable";
                }
                Util.getDefaultSharedPrefs(PrivacyControlsFragment.this.getActivity()).edit().putBoolean(pref, enable).apply();
                return;
            }
            Util.showCenteredToast(activity, R.string.privacy_controls_update_fail);
        }
    }
}
