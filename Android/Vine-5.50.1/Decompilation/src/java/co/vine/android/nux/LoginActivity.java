package co.vine.android.nux;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import co.vine.android.BaseControllerActionBarActivity;
import co.vine.android.R;
import co.vine.android.ResetPasswordActivity;
import co.vine.android.StartActivity;
import co.vine.android.api.VineLogin;
import co.vine.android.client.Session;
import co.vine.android.scribe.AppNavigationProviderSingleton;
import co.vine.android.scribe.model.AppNavigation;
import co.vine.android.service.components.Components;
import co.vine.android.service.components.authentication.AuthenticationActionListener;
import co.vine.android.util.AuthenticationUtils;
import co.vine.android.util.DialogUtils;
import co.vine.android.util.Util;
import co.vine.android.widgets.PromptDialogSupportFragment;

/* loaded from: classes.dex */
public class LoginActivity extends BaseControllerActionBarActivity {
    private EditText mEmailAddress;
    private boolean mFinish;
    private EditText mPassword;
    private final AuthenticationActionListener mAuthListener = new LoginSessionListener();
    private final PromptDialogSupportFragment.OnDialogDoneListener mActivateAccountDialogDoneListener = new PromptDialogSupportFragment.OnDialogDoneListener() { // from class: co.vine.android.nux.LoginActivity.3
        @Override // co.vine.android.widgets.PromptDialogFragment.OnDialogDoneListener
        public void onDialogDone(DialogInterface dialog, int id, int which) {
            if (which == -1) {
                LoginActivity.this.login(true);
            }
        }
    };

    @Override // co.vine.android.BaseActionBarActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityDonut, android.app.Activity
    @SuppressLint({"MissingSuperCall"})
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.login, false);
        setupActionBar((Boolean) true, (Boolean) true, R.string.login_sign_in, (Boolean) true, (Boolean) true);
        this.mEmailAddress = (EditText) findViewById(R.id.login_username);
        this.mPassword = (EditText) findViewById(R.id.login_password);
        this.mPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: co.vine.android.nux.LoginActivity.1
            @Override // android.widget.TextView.OnEditorActionListener
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && event.getKeyCode() == 66) || (actionId == 6 && LoginActivity.this.validateAndDisplayToastIfCredentialsInvalid())) {
                    InputMethodManager imm = (InputMethodManager) LoginActivity.this.getSystemService("input_method");
                    imm.hideSoftInputFromWindow(LoginActivity.this.mPassword.getWindowToken(), 0);
                    LoginActivity.this.login(false);
                    return true;
                }
                return true;
            }
        });
        StyleSpan[] styleSpanBoldSingle = {new StyleSpan(1)};
        Spanned resetPasswordSpan = Util.getSpannedText(styleSpanBoldSingle, getString(R.string.login_forgot_password), '\"');
        TextView resetPassword = (TextView) findViewById(R.id.login_reset_password);
        resetPassword.setText(resetPasswordSpan, TextView.BufferType.SPANNABLE);
        resetPassword.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.nux.LoginActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (view.getId() == R.id.login_reset_password) {
                    Intent intent = new Intent(LoginActivity.this, (Class<?>) ResetPasswordActivity.class);
                    String email = LoginActivity.this.mEmailAddress.getText().toString();
                    if (!TextUtils.isEmpty(email)) {
                        intent.putExtra("email", email);
                    }
                    LoginActivity.this.startActivity(intent);
                }
            }
        });
        Intent intent = getIntent();
        if (intent != null) {
            this.mFinish = intent.getBooleanExtra("finish", false);
        }
    }

    @Override // co.vine.android.BaseControllerActionBarActivity, co.vine.android.BaseActionBarActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        AppNavigationProviderSingleton.getInstance().setViewAndSubview(AppNavigation.Views.SIGNIN, null);
        Components.authComponent().addListener(this.mAuthListener);
    }

    @Override // co.vine.android.BaseControllerActionBarActivity, co.vine.android.BaseActionBarActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onPause() {
        super.onPause();
        Components.authComponent().removeListener(this.mAuthListener);
    }

    @Override // co.vine.android.BaseActionBarActivity, android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override // co.vine.android.BaseActionBarActivity, android.app.Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != R.id.sign_in) {
            return super.onOptionsItemSelected(item);
        }
        if (validateAndDisplayToastIfCredentialsInvalid()) {
            login(false);
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean validateAndDisplayToastIfCredentialsInvalid() {
        String emailAddress = this.mEmailAddress.getText().toString();
        if (emailAddress.isEmpty()) {
            Util.showCenteredToast(this, R.string.login_empty_email);
            return false;
        }
        if (!AuthenticationUtils.isEmailAddressValid(emailAddress)) {
            Util.showCenteredToast(this, R.string.login_invalid_email);
            return false;
        }
        AuthenticationUtils.Result passwordValidationResult = AuthenticationUtils.validatePassword(this.mPassword.getText().toString());
        switch (passwordValidationResult) {
            case EMPTY:
                Util.showCenteredToast(this, R.string.login_empty_password);
                break;
            case TOO_SHORT:
                Util.showCenteredToast(this, R.string.login_password_too_short);
                break;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void login(boolean reactivate) {
        String emailAddress = this.mEmailAddress.getText().toString();
        String password = this.mPassword.getText().toString();
        ProgressDialog dialog = new ProgressDialog(this, R.style.ProgressDialogTheme);
        dialog.setProgressStyle(0);
        dialog.setMessage(getString(R.string.login_logging_in));
        DialogUtils.showDialogUnsafe(dialog);
        this.mProgressDialog = dialog;
        Session session = this.mAppController.getActiveSession();
        VineLogin login = new VineLogin(null, emailAddress, 0L, null);
        Components.authComponent().login(this.mAppController, session, login, password, reactivate);
    }

    public static Intent getIntent(Context context) {
        return new Intent(context, (Class<?>) LoginActivity.class);
    }

    public static Intent getIntentWithFinish(Context context) {
        Intent intent = new Intent(context, (Class<?>) LoginActivity.class);
        intent.putExtra("finish", true);
        return intent;
    }

    private class LoginSessionListener extends AuthenticationActionListener {
        private LoginSessionListener() {
        }

        @Override // co.vine.android.service.components.authentication.AuthenticationActionListener
        public void onLoginSuccess() {
            LoginActivity.this.dismissDialog();
            LoginActivity.this.mAppController.getEditions();
            if (!LoginActivity.this.mFinish) {
                StartActivity.toStart(LoginActivity.this.getBaseContext());
            }
            LoginActivity.this.finish();
        }

        @Override // co.vine.android.service.components.authentication.AuthenticationActionListener
        public void onLoginDeactivatedAccount() {
            LoginActivity.this.dismissDialog();
            PromptDialogSupportFragment dialogFragment = PromptDialogSupportFragment.newInstance(1);
            dialogFragment.setMessage(R.string.settings_activate_account_dialog);
            dialogFragment.setTitle(R.string.settings_activate_account_title);
            dialogFragment.setNegativeButton(R.string.cancel);
            dialogFragment.setPositiveButton(R.string.settings_activate_account_confirm);
            dialogFragment.setListener(LoginActivity.this.mActivateAccountDialogDoneListener);
            dialogFragment.show(LoginActivity.this.getSupportFragmentManager());
        }

        @Override // co.vine.android.service.components.authentication.AuthenticationActionListener
        public void onLoginFailed(Context context, String reasonPhrase) {
            super.onLoginFailed(context, reasonPhrase);
            LoginActivity.this.dismissDialog();
        }
    }
}
