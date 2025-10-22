package co.vine.android;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import co.vine.android.client.AppController;
import co.vine.android.client.VineAccountHelper;
import co.vine.android.service.VineService;
import co.vine.android.service.VineServiceCallback;
import co.vine.android.service.VineServiceResponder;

/* loaded from: classes.dex */
public class AuthenticatorActivity extends AccountAuthenticatorActivity {
    private AccountManager mAccountManager;
    Boolean mConfirmCredentials;
    private String mLoginEmail;
    private EditText mPasswordEditView;

    @Override // android.accounts.AccountAuthenticatorActivity, android.app.Activity
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        requestWindowFeature(3);
        setContentView(R.layout.login);
        getWindow().setFeatureDrawableResource(3, android.R.drawable.ic_dialog_alert);
        this.mAccountManager = AccountManager.get(this);
        Intent intent = getIntent();
        this.mLoginEmail = intent.getStringExtra("username");
        this.mConfirmCredentials = Boolean.valueOf(intent.getBooleanExtra("confirm_credentials", false));
        TextView usernameView = (TextView) findViewById(R.id.login_username);
        usernameView.setText(this.mLoginEmail);
        this.mPasswordEditView = (EditText) findViewById(R.id.login_password);
    }

    public void onClickHandler(View view) {
        String password = this.mPasswordEditView.getText().toString();
        if (!TextUtils.isEmpty(password)) {
            showDialog(1);
            VineServiceResponder stub = new VineServiceResponder() { // from class: co.vine.android.AuthenticatorActivity.1
                @Override // co.vine.android.service.VineServiceResponder
                public void onServiceResponse(int actionCode, int responseCode, String reasonPhrase, Bundle bundle) {
                    AuthenticatorActivity.this.removeDialog(1);
                    if (responseCode == 200) {
                        String key = bundle.getString("s_key");
                        if (!AuthenticatorActivity.this.mConfirmCredentials.booleanValue()) {
                            AuthenticatorActivity.this.finishLogin(key);
                            return;
                        } else {
                            AuthenticatorActivity.this.finishConfirmCredentials(key, true);
                            return;
                        }
                    }
                    throw new UnsupportedOperationException("handle bad password scenario");
                }
            };
            Intent intent = new Intent(this, (Class<?>) VineService.class);
            intent.setAction("co.vine.android.session.login");
            intent.putExtra("email", this.mLoginEmail);
            intent.putExtra("pass", password);
            intent.putExtra("ibinder", new VineServiceCallback(stub));
            startService(intent);
        }
    }

    @Override // android.app.Activity
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 1:
                ProgressDialog dialog = new ProgressDialog(this, R.style.ProgressDialogTheme);
                dialog.setMessage(getText(R.string.login_logging_in));
                dialog.setIndeterminate(true);
                dialog.setCancelable(true);
                return dialog;
            default:
                return null;
        }
    }

    void finishConfirmCredentials(String key, boolean result) {
        saveAuth(key);
        Intent intent = new Intent();
        intent.putExtra("booleanResult", result);
        setAccountAuthenticatorResult(intent.getExtras());
        setResult(-1, intent);
        finish();
    }

    void finishLogin(String key) {
        saveAuth(key);
        Intent intent = new Intent();
        intent.putExtra("authAccount", this.mLoginEmail);
        intent.putExtra("accountType", VineAccountHelper.ACCOUNT_TYPE);
        intent.putExtra("authtoken", key);
        setAccountAuthenticatorResult(intent.getExtras());
        setResult(-1, intent);
        AppController appController = AppController.getInstance(this);
        if (appController.isLoggedIn()) {
            appController.updateCredentials(key);
        }
        finish();
    }

    private void saveAuth(String key) {
        Account account = new Account(this.mLoginEmail, VineAccountHelper.ACCOUNT_TYPE);
        AccountManager am = this.mAccountManager;
        am.setAuthToken(account, "co.vine.android.basic_auth.token.secret", key);
    }
}
