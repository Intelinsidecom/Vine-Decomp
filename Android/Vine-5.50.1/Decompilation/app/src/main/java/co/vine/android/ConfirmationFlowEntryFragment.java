package co.vine.android;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import co.vine.android.util.Util;
import co.vine.android.widgets.PromptDialogSupportFragment;

/* loaded from: classes.dex */
public class ConfirmationFlowEntryFragment extends BaseControllerFragment {
    private MenuItem mNext;
    private boolean mNextEnabled;
    private String mPhone;
    private EditText mPhoneNumberBox;

    @Override // co.vine.android.BaseControllerFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Bundle args = getArguments();
        if (args != null) {
            this.mPhone = args.getString("phone_number");
        } else if (savedInstanceState != null) {
            this.mPhone = savedInstanceState.getString("phone_number");
        }
    }

    @Override // co.vine.android.BaseControllerFragment, android.support.v4.app.Fragment
    public void onPause() {
        super.onPause();
        Util.setSoftKeyboardVisibility(getActivity(), this.mPhoneNumberBox, false);
    }

    @Override // android.support.v4.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("phone_number", this.mPhone);
        super.onSaveInstanceState(outState);
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.confirm_phone_enter_number, container, false);
        final EditText phoneNumberBox = (EditText) v.findViewById(R.id.phone_number_box);
        TextView confirmation = (TextView) v.findViewById(R.id.enter_number_message);
        confirmation.setText(getString(R.string.confirm_phone_start));
        String phone = this.mPhone;
        if (!TextUtils.isEmpty(phone)) {
            phoneNumberBox.setText(phone);
            phoneNumberBox.setSelection(phone.length());
        }
        phoneNumberBox.addTextChangedListener(new TextWatcher() { // from class: co.vine.android.ConfirmationFlowEntryFragment.1
            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(phoneNumberBox.getText())) {
                    ConfirmationFlowEntryFragment.this.toggleNextAction(true);
                } else {
                    ConfirmationFlowEntryFragment.this.toggleNextAction(false);
                }
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable s) {
            }
        });
        this.mPhoneNumberBox = phoneNumberBox;
        return v;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void toggleNextAction(boolean enabled) {
        this.mNextEnabled = enabled;
        if (this.mNext != null) {
            this.mNext.setEnabled(enabled);
        }
    }

    @Override // android.support.v4.app.Fragment
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.next, menu);
        this.mNext = menu.findItem(R.id.next);
        this.mNext.setEnabled(this.mNextEnabled);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override // co.vine.android.BaseControllerFragment, co.vine.android.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(this.mPhone)) {
            toggleNextAction(true);
        }
    }

    @Override // android.support.v4.app.Fragment
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.next) {
            Editable e = this.mPhoneNumberBox.getText();
            if (e == null) {
                return false;
            }
            this.mPhone = e.toString();
            PromptDialogSupportFragment p = PromptDialogSupportFragment.newInstance(1, 3);
            p.setListener(new PromptDialogSupportFragment.OnDialogDoneListener() { // from class: co.vine.android.ConfirmationFlowEntryFragment.2
                @Override // co.vine.android.widgets.PromptDialogFragment.OnDialogDoneListener
                public void onDialogDone(DialogInterface dialog, int id, int which) {
                    switch (which) {
                        case -1:
                            FragmentActivity activity = ConfirmationFlowEntryFragment.this.getActivity();
                            if (activity != null) {
                                Fragment frag = new ConfirmationFlowVerificationFragment();
                                Bundle args = new Bundle();
                                args.putString("phone_number", ConfirmationFlowEntryFragment.this.mPhone);
                                args.putBoolean("from_sign_up", false);
                                frag.setArguments(args);
                                activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left).replace(R.id.fragment_container, frag).commit();
                                break;
                            }
                            break;
                    }
                }
            });
            p.setTitle(R.string.verify_your_number).setMessage(getString(R.string.is_this_your_number, this.mPhone)).setPositiveButton(R.string.ok).setNeutralButton(R.string.cancel).show(getActivity().getSupportFragmentManager());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
