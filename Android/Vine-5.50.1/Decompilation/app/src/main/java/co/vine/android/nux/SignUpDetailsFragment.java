package co.vine.android.nux;

import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.StyleSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import co.vine.android.BaseFragment;
import co.vine.android.R;
import co.vine.android.scribe.model.AppNavigation;
import co.vine.android.util.AuthenticationUtils;
import co.vine.android.util.ContactsHelper;
import co.vine.android.util.Util;

/* loaded from: classes.dex */
public class SignUpDetailsFragment extends BaseFragment implements ContactsHelper.ContactHelperListener {
    private EditText mEmailAddress;
    private Handler mHandler;
    private MenuItem mNextButton;
    private EditText mPassword;
    private EditText mPhone;

    @Override // android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        this.mHandler = new Handler();
        ((SignUpPagerActivity) getActivity()).setBarTitle(R.string.login_sign_up);
    }

    @Override // android.support.v4.app.Fragment
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mEmailAddress = (EditText) view.findViewById(R.id.signup_email);
        this.mPassword = (EditText) view.findViewById(R.id.signup_password);
        this.mPhone = (EditText) view.findViewById(R.id.signup_phone);
        this.mPhone.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: co.vine.android.nux.SignUpDetailsFragment.1
            @Override // android.widget.TextView.OnEditorActionListener
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                if (actionId == 6 && SignUpDetailsFragment.this.validateAndDisplayToastIfCredentialsInvalid() && SignUpDetailsFragment.this.getActivity() != null) {
                    InputMethodManager imm = (InputMethodManager) SignUpDetailsFragment.this.getActivity().getSystemService("input_method");
                    imm.hideSoftInputFromWindow(SignUpDetailsFragment.this.mPhone.getWindowToken(), 0);
                    SignUpDetailsFragment.this.onNextClicked();
                    return true;
                }
                return true;
            }
        });
        TextView termsOfServiceView = (TextView) view.findViewById(R.id.tos_line_2);
        StyleSpan[] boldStyle = {new StyleSpan(1), new StyleSpan(1)};
        Spanned spanned = Util.getSpannedText(boldStyle, getString(R.string.sign_up_tos_line_2), '\"');
        termsOfServiceView.setText(spanned);
        termsOfServiceView.setMovementMethod(LinkMovementMethod.getInstance());
        Spannable clickSpannable = (Spannable) termsOfServiceView.getText();
        StyleSpan[] spans = (StyleSpan[]) spanned.getSpans(0, spanned.length(), StyleSpan.class);
        NuxClickableSpanFactory clickableSpanFactory = new NuxClickableSpanFactory(getResources().getColor(R.color.text_fineprint));
        int start = spanned.getSpanStart(spans[0]);
        int end = spanned.getSpanEnd(spans[0]);
        Util.safeSetSpan(clickSpannable, clickableSpanFactory.newVinePrivacyPolicyClickableSpan(), start, end, 33);
        int start2 = spanned.getSpanStart(spans[1]);
        int end2 = spanned.getSpanEnd(spans[1]);
        Util.safeSetSpan(clickSpannable, clickableSpanFactory.newVineTermsOfServiceClickableSpan(), start2, end2, 33);
        ContactsHelper.loadContacts(this, this);
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup root, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sign_up_flow_details, root, false);
    }

    @Override // android.support.v4.app.Fragment
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.next, menu);
        this.mNextButton = menu.findItem(R.id.next);
        if (this.mNextButton != null) {
            this.mNextButton.setEnabled(true);
        }
    }

    @Override // android.support.v4.app.Fragment
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.next && validateAndDisplayToastIfCredentialsInvalid()) {
            if (validateAndDisplayToastIfCredentialsInvalid()) {
                return onNextClicked();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean onNextClicked() {
        Util.setSoftKeyboardVisibility(getActivity(), this.mEmailAddress, false);
        SignUpPagerActivity activity = (SignUpPagerActivity) getActivity();
        if (activity == null) {
            return false;
        }
        activity.setLogin(this.mEmailAddress.getText().toString());
        activity.setPassword(this.mPassword.getText().toString());
        activity.setPhone(this.mPhone.getText().toString());
        activity.toNextStep();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean validateAndDisplayToastIfCredentialsInvalid() {
        String emailAddress = this.mEmailAddress.getText().toString();
        if (emailAddress.isEmpty()) {
            Util.showCenteredToast(getActivity(), R.string.login_empty_email);
            return false;
        }
        if (!AuthenticationUtils.isEmailAddressValid(emailAddress)) {
            Util.showCenteredToast(getActivity(), R.string.login_invalid_email);
            return false;
        }
        AuthenticationUtils.Result passwordValidationResult = AuthenticationUtils.validatePassword(this.mPassword.getText().toString());
        switch (passwordValidationResult) {
            case EMPTY:
                Util.showCenteredToast(getActivity(), R.string.login_empty_password);
                break;
            case TOO_SHORT:
                Util.showCenteredToast(getActivity(), R.string.login_password_too_short);
                break;
        }
        return false;
    }

    @Override // co.vine.android.util.ContactsHelper.ContactHelperListener
    public void onNameLoaded(String name) {
    }

    @Override // co.vine.android.util.ContactsHelper.ContactHelperListener
    public void onPhoneNumberLoaded(String phoneNumber) {
        if (TextUtils.isEmpty(this.mPhone.getText()) && !TextUtils.isEmpty(phoneNumber)) {
            this.mPhone.setText(phoneNumber);
        }
    }

    @Override // co.vine.android.util.ContactsHelper.ContactHelperListener
    public void onEmailLoaded(String emailText) {
        EditText email = this.mEmailAddress;
        if (TextUtils.isEmpty(email.getText()) && !TextUtils.isEmpty(emailText)) {
            email.setText(emailText);
            email.setSelection(emailText.length());
        }
    }

    @Override // co.vine.android.BaseFragment, android.support.v4.app.Fragment
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            updateAppNavigationProvider();
        }
    }

    @Override // co.vine.android.BaseFragment
    protected AppNavigation.Views getAppNavigationView() {
        return AppNavigation.Views.SIGN_UP_EMAIL_2;
    }
}
