package co.vine.android;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import co.vine.android.client.AppSessionListener;
import co.vine.android.scribe.AppNavigationProviderSingleton;
import co.vine.android.scribe.model.AppNavigation;
import co.vine.android.util.AuthenticationUtils;
import co.vine.android.util.Util;
import co.vine.android.widget.Typefaces;
import co.vine.android.widgets.PromptDialogSupportFragment;
import twitter4j.internal.http.HttpResponseCode;

/* loaded from: classes.dex */
public class ResetPasswordActivity extends BaseControllerActionBarActivity {
    private EditText mEmailAddress;

    @Override // co.vine.android.BaseActionBarActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityDonut, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.reset_password, false);
        InputMethodManager imm = (InputMethodManager) getSystemService("input_method");
        imm.showSoftInputFromInputMethod(getWindow().getDecorView().getWindowToken(), 0);
        setupActionBar((Boolean) true, (Boolean) true, R.string.password_reset_title, (Boolean) true, (Boolean) true);
        this.mAppSessionListener = new ResetPasswordListener();
        this.mEmailAddress = (EditText) findViewById(R.id.email);
        this.mEmailAddress.setTypeface(Typefaces.get(this).getContentTypeface(0, 4));
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String emailAddress = bundle.getString("email");
            if (!TextUtils.isEmpty(emailAddress)) {
                this.mEmailAddress.setText(emailAddress);
                this.mEmailAddress.setSelection(this.mEmailAddress.length());
            }
            int backgroundColor = bundle.getInt("color");
            if (backgroundColor != 0) {
                setActionBarColor(backgroundColor);
            }
        }
    }

    @Override // co.vine.android.BaseActionBarActivity, android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.reset_password, menu);
        return true;
    }

    @Override // co.vine.android.BaseActionBarActivity, android.app.Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.send) {
            String emailAddress = this.mEmailAddress.getText().toString();
            if (emailAddress.isEmpty()) {
                Util.showCenteredToast(this, R.string.login_empty_email);
                return false;
            }
            if (!AuthenticationUtils.isEmailAddressValid(emailAddress)) {
                Util.showCenteredToast(this, R.string.login_invalid_email);
                return false;
            }
            ProgressDialog dialog = new ProgressDialog(this, R.style.ProgressDialogTheme);
            dialog.setProgress(0);
            dialog.setMessage(getString(R.string.dialog_sending));
            dialog.show();
            this.mProgressDialog = dialog;
            this.mAppController.resetPassword(this.mEmailAddress.getText().toString());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override // co.vine.android.BaseControllerActionBarActivity, co.vine.android.BaseActionBarActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onPause() {
        super.onPause();
        Util.setSoftKeyboardVisibility(this, this.mEmailAddress, false);
    }

    @Override // co.vine.android.BaseControllerActionBarActivity, co.vine.android.BaseActionBarActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        Util.setSoftKeyboardVisibility(this, this.mEmailAddress, true);
        AppNavigationProviderSingleton.getInstance().setViewAndSubview(AppNavigation.Views.RESET_PASSWORD, null);
    }

    private class ResetPasswordListener extends AppSessionListener {
        private ResetPasswordListener() {
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onResetPasswordComplete(String reqId, int statusCode, String reasonPhrase) {
            int error;
            ResetPasswordActivity.this.dismissDialog();
            switch (statusCode) {
                case HttpResponseCode.OK /* 200 */:
                    error = R.string.password_reset_confirm;
                    break;
                case HttpResponseCode.NOT_FOUND /* 404 */:
                    error = R.string.password_reset_not_found;
                    break;
                default:
                    error = R.string.password_reset_error;
                    break;
            }
            PromptDialogSupportFragment fragment = PromptDialogSupportFragment.newInstance(1);
            fragment.setMessage(error);
            fragment.setPositiveButton(R.string.ok);
            fragment.show(ResetPasswordActivity.this.getSupportFragmentManager());
        }
    }
}
