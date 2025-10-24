package co.vine.android;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import co.vine.android.util.CommonUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Locale;

/* loaded from: classes.dex */
public class LocaleDialog extends DialogFragment implements AdapterView.OnItemSelectedListener {
    private boolean mChanges;
    private int mLastSelected;
    private RadioGroup mLocaleOptions;
    private Spinner mLocales;
    private ArrayAdapter<String> mLocalesAdapter;
    private ArrayList<Locale> mLocalesCodes;

    static LocaleDialog newInstance() {
        return new LocaleDialog();
    }

    private static Boolean isValidLocale(Locale locale) {
        return Boolean.valueOf((TextUtils.isEmpty(locale.getLanguage()) || TextUtils.isEmpty(locale.getCountry()) || !TextUtils.isEmpty(locale.getVariant())) ? false : true);
    }

    @Override // android.support.v4.app.DialogFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<String> localesNames = new ArrayList<>();
        ArrayList<Locale> localesCodes = new ArrayList<>();
        Locale[] availableLocales = Locale.getAvailableLocales();
        if (availableLocales != null) {
            Arrays.sort(availableLocales, new LocaleComparator());
            for (Locale l : availableLocales) {
                if (isValidLocale(l).booleanValue()) {
                    String code = l.getDisplayName(Locale.ENGLISH);
                    localesCodes.add(l);
                    localesNames.add(l.getLanguage() + " - " + code);
                }
            }
        }
        Locale dummy = new Locale("en", "ss");
        localesNames.add("DEBUG - " + dummy.getLanguage() + " - " + dummy.getDisplayName(Locale.ENGLISH));
        localesCodes.add(dummy);
        this.mLocalesCodes = localesCodes;
        ArrayAdapter<String> localesAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, localesNames);
        localesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.mLocalesAdapter = localesAdapter;
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.locale_preference, container, false);
        RadioButton system = (RadioButton) v.findViewById(R.id.system_locale);
        RadioButton custom = (RadioButton) v.findViewById(R.id.custom_locale);
        SharedPreferences prefs = CommonUtil.getDefaultSharedPrefs(getActivity());
        boolean customEnabled = prefs.getBoolean("pref_custom_locale_enabled", false);
        system.setChecked(!customEnabled);
        custom.setChecked(customEnabled);
        Spinner locales = (Spinner) v.findViewById(R.id.locales_spinner);
        locales.setAdapter((SpinnerAdapter) this.mLocalesAdapter);
        if (prefs.contains("pref_custom_locale")) {
            String customLocaleCode = prefs.getString("pref_custom_locale", "");
            String customCountry = prefs.getString("pref_custom_locale_country", "");
            if (!TextUtils.isEmpty(customLocaleCode)) {
                int index = 0;
                Iterator<Locale> it = this.mLocalesCodes.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    Locale l = it.next();
                    if (customLocaleCode.equals(l.getLanguage()) && customCountry.equals(l.getCountry())) {
                        locales.setSelection(index);
                        this.mLastSelected = index;
                        break;
                    }
                    index++;
                }
            }
        }
        RadioGroup localeOptions = (RadioGroup) v.findViewById(R.id.locale_group);
        if (customEnabled) {
            localeOptions.check(R.id.custom_locale);
        } else {
            localeOptions.check(R.id.system_locale);
        }
        localeOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { // from class: co.vine.android.LocaleDialog.1
            @Override // android.widget.RadioGroup.OnCheckedChangeListener
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                LocaleDialog.this.mLocales.setEnabled(checkedId == R.id.custom_locale);
                LocaleDialog.this.mChanges = true;
            }
        });
        locales.setEnabled(customEnabled);
        locales.setOnItemSelectedListener(this);
        this.mLocales = locales;
        this.mLocaleOptions = localeOptions;
        getDialog().setTitle("Custom locales");
        return v;
    }

    @Override // android.support.v4.app.DialogFragment, android.content.DialogInterface.OnCancelListener
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        getDialog().dismiss();
    }

    @Override // android.support.v4.app.DialogFragment, android.content.DialogInterface.OnDismissListener
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (this.mChanges) {
            boolean customLocale = this.mLocaleOptions.getCheckedRadioButtonId() == R.id.custom_locale;
            int position = this.mLocales.getSelectedItemPosition();
            int index = 0;
            Locale selectedLocale = null;
            Iterator<Locale> it = this.mLocalesCodes.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Locale l = it.next();
                if (index == position) {
                    selectedLocale = l;
                    break;
                }
                index++;
            }
            if (selectedLocale != null) {
                String customLocaleCode = selectedLocale.getLanguage();
                String customCountry = selectedLocale.getCountry();
                SharedPreferences prefs = CommonUtil.getDefaultSharedPrefs(getActivity());
                prefs.edit().putBoolean("pref_custom_locale_enabled", customLocale).putString("pref_custom_locale", customLocaleCode).putString("pref_custom_locale_country", customCountry).apply();
                if (index != this.mLastSelected) {
                    CommonUtil.restartApp(getActivity());
                }
            }
        }
    }

    @Override // android.widget.AdapterView.OnItemSelectedListener
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        this.mChanges = true;
    }

    @Override // android.widget.AdapterView.OnItemSelectedListener
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private class LocaleComparator implements Comparator<Locale> {
        private LocaleComparator() {
        }

        @Override // java.util.Comparator
        public int compare(Locale l1, Locale l2) {
            String s1 = l1.getLanguage() + "-" + l1.getCountry();
            String s2 = l2.getLanguage() + "-" + l2.getCountry();
            return s1.compareTo(s2);
        }
    }
}
