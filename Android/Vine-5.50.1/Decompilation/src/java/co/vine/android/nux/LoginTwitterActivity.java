package co.vine.android.nux;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.StyleSpan;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import co.vine.android.BaseControllerActionBarActivity;
import co.vine.android.R;
import co.vine.android.StartActivity;
import co.vine.android.api.VineLogin;
import co.vine.android.client.ServiceNotification;
import co.vine.android.scribe.AppNavigationProviderSingleton;
import co.vine.android.scribe.model.AppNavigation;
import co.vine.android.service.components.Components;
import co.vine.android.service.components.authentication.AuthenticationActionListener;
import co.vine.android.util.DialogUtils;
import co.vine.android.util.Util;
import co.vine.android.util.analytics.FlurryUtils;
import co.vine.android.widget.Typefaces;
import co.vine.android.widgets.PromptDialogSupportFragment;
import twitter4j.conf.PropertyConfiguration;

/* loaded from: classes.dex */
public class LoginTwitterActivity extends BaseControllerActionBarActivity {
    private boolean mFinish;
    private EditText mPassword;
    private EditText mUsername;
    private VineLogin mVineLogin;
    private AuthenticationActionListener mAuthListener = new LoginTwitterListener();
    private final PromptDialogSupportFragment.OnDialogDoneListener mActivateAccountDialogDoneListener = new PromptDialogSupportFragment.OnDialogDoneListener() { // from class: co.vine.android.nux.LoginTwitterActivity.2
        @Override // co.vine.android.widgets.PromptDialogFragment.OnDialogDoneListener
        public void onDialogDone(DialogInterface dialog, int id, int which) {
            if (which == -1) {
                LoginTwitterActivity.this.login(true);
            }
        }
    };

    @Override // co.vine.android.BaseActionBarActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityDonut, android.app.Activity
    @SuppressLint({"MissingSuperCall"})
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.xauth, false);
        setupActionBar((Boolean) true, (Boolean) true, R.string.login_xauth_title, (Boolean) true, (Boolean) true);
        this.mUsername = (EditText) findViewById(R.id.login_username);
        this.mUsername.setTypeface(Typefaces.get(this).getContentTypeface(0, 2));
        this.mPassword = (EditText) findViewById(R.id.login_password);
        this.mPassword.setTypeface(Typefaces.get(this).getContentTypeface(0, 2));
        this.mPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: co.vine.android.nux.LoginTwitterActivity.1
            @Override // android.widget.TextView.OnEditorActionListener
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && event.getKeyCode() == 66) || (actionId == 6 && LoginTwitterActivity.this.validateAndDisplayToastIfCredentialsInvalid())) {
                    LoginTwitterActivity.this.login(false);
                }
                return false;
            }
        });
        this.mFinish = getIntent().getBooleanExtra("finish", false);
        int loginMode = getIntent().getIntExtra("login_mode", 2);
        if (loginMode == 2 || loginMode == 4) {
            int actionbarTitle = loginMode == 4 ? R.string.connect_twitter : R.string.signup_with_twitter;
            setupActionBar((Boolean) true, (Boolean) true, actionbarTitle, (Boolean) true, (Boolean) true);
            TextView mTos = (TextView) findViewById(R.id.tos_line_2);
            StyleSpan[] boldStyle = {new StyleSpan(1), new StyleSpan(1)};
            Spanned spanned = Util.getSpannedText(boldStyle, getString(R.string.login_xauth_tos_line_2), '\"');
            mTos.setText(spanned);
            mTos.setMovementMethod(LinkMovementMethod.getInstance());
            Spannable clickSpannable = (Spannable) mTos.getText();
            StyleSpan[] spans = (StyleSpan[]) spanned.getSpans(0, spanned.length(), StyleSpan.class);
            Resources res = getResources();
            NuxClickableSpanFactory clickableSpanFactory = new NuxClickableSpanFactory(res.getColor(R.color.text_fineprint));
            int start = spanned.getSpanStart(spans[0]);
            int end = spanned.getSpanEnd(spans[0]);
            Util.safeSetSpan(clickSpannable, clickableSpanFactory.newVineTermsOfServiceClickableSpan(), start, end, 33);
            int start2 = spanned.getSpanStart(spans[1]);
            int end2 = spanned.getSpanEnd(spans[1]);
            Util.safeSetSpan(clickSpannable, clickableSpanFactory.newVinePrivacyPolicyClickableSpan(), start2, end2, 33);
            FlurryUtils.trackNuxStarted("twitter-xauth");
            AppNavigationProviderSingleton.getInstance().setViewAndSubview(AppNavigation.Views.SIGNUP_TWITTER_XAUTH, null);
        } else {
            findViewById(R.id.tos_line).setVisibility(8);
        }
        if (savedInstanceState != null && savedInstanceState.containsKey("vine_login")) {
            this.mVineLogin = (VineLogin) savedInstanceState.getParcelable("vine_login");
        }
    }

    @Override // co.vine.android.BaseActionBarActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (this.mVineLogin != null) {
            outState.putParcelable("vine_login", this.mVineLogin);
        }
    }

    @Override // co.vine.android.BaseActionBarActivity, android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override // co.vine.android.BaseActionBarActivity, android.app.Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != R.id.sign_in || !validateAndDisplayToastIfCredentialsInvalid()) {
            return super.onOptionsItemSelected(item);
        }
        login(false);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean validateAndDisplayToastIfCredentialsInvalid() {
        if (this.mUsername.getText().toString().isEmpty()) {
            Util.showCenteredToast(this, R.string.login_twitter_empty_username_email);
            return false;
        }
        if (this.mPassword.getText().toString().isEmpty()) {
            Util.showCenteredToast(this, R.string.login_empty_password);
            return false;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void login(boolean reactivate) {
        ProgressDialog dialog = new ProgressDialog(this, R.style.ProgressDialogTheme);
        dialog.setMessage(getString(R.string.sign_up_authorizing));
        dialog.setProgress(0);
        DialogUtils.showDialogUnsafe(dialog);
        this.mProgressDialog = dialog;
        if (!reactivate) {
            Components.authComponent().loginWithTwitterXAuth(this.mAppController, this.mUsername.getText().toString(), this.mPassword.getText().toString());
        } else if (this.mVineLogin != null) {
            VineLogin login = this.mVineLogin;
            Components.authComponent().loginCheckTwitter(this.mAppController, login, true);
        } else {
            Util.showCenteredToast(this, R.string.error_xauth);
        }
    }

    @Override // co.vine.android.BaseControllerActionBarActivity, co.vine.android.BaseActionBarActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        Components.authComponent().addListener(this.mAuthListener);
    }

    @Override // co.vine.android.BaseControllerActionBarActivity, co.vine.android.BaseActionBarActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onPause() {
        super.onPause();
        Components.authComponent().removeListener(this.mAuthListener);
    }

    private class LoginTwitterListener extends AuthenticationActionListener {
        private LoginTwitterListener() {
        }

        @Override // co.vine.android.service.components.authentication.AuthenticationActionListener
        public void onCheckTwitterLoginSuccess(VineLogin login) {
            LoginTwitterActivity.this.dismissDialog();
            if (LoginTwitterActivity.this.mFinish) {
                LoginTwitterActivity.this.finish();
            } else {
                StartActivity.toStart(LoginTwitterActivity.this);
            }
        }

        @Override // co.vine.android.service.components.authentication.AuthenticationActionListener
        public void onCheckTwitterLoginBadCredentials(VineLogin login) {
            LoginTwitterActivity.this.dismissDialog();
            Bundle extras = new Bundle();
            extras.putParcelable(PropertyConfiguration.USER, login);
            LoginTwitterActivity.this.startActivity(SignUpPagerActivity.getIntent(LoginTwitterActivity.this, extras));
        }

        @Override // co.vine.android.service.components.authentication.AuthenticationActionListener
        public void onCheckTwitterLoginDeactivated(VineLogin login) {
            LoginTwitterActivity.this.dismissDialog();
            PromptDialogSupportFragment dialogFragment = PromptDialogSupportFragment.newInstance(1);
            dialogFragment.setMessage(R.string.settings_activate_account_dialog);
            dialogFragment.setTitle(R.string.settings_activate_account_title);
            dialogFragment.setNegativeButton(R.string.cancel);
            dialogFragment.setPositiveButton(R.string.settings_activate_account_confirm);
            dialogFragment.setListener(LoginTwitterActivity.this.mActivateAccountDialogDoneListener);
            dialogFragment.show(LoginTwitterActivity.this.getSupportFragmentManager());
        }

        @Override // co.vine.android.service.components.authentication.AuthenticationActionListener
        public void onCheckTwitterLoginUnknownError(ServiceNotification notification) {
            LoginTwitterActivity.this.dismissDialog();
            super.onCheckTwitterLoginUnknownError(notification);
        }

        @Override // co.vine.android.service.components.authentication.AuthenticationActionListener
        public void onCheckTwitterLoginFailedToCreateLocalAccount(Context context) {
            LoginTwitterActivity.this.dismissDialog();
            super.onCheckTwitterLoginFailedToCreateLocalAccount(context);
            LoginTwitterActivity.this.finish();
        }

        @Override // co.vine.android.service.components.authentication.AuthenticationActionListener
        public void onTwitterxAuthComplete(String reqId, int statusCode, String reasonPhrase, VineLogin login) {
            if (LoginTwitterActivity.this.mFinish) {
                if (statusCode == 200) {
                    if (!LoginTwitterActivity.this.mAppController.isLoggedIn()) {
                        Components.authComponent().loginCheckTwitter(LoginTwitterActivity.this.mAppController, login, false);
                        return;
                    }
                    Intent intent = new Intent();
                    intent.putExtra("token", login.twitterToken);
                    intent.putExtra("secret", login.twitterSecret);
                    intent.putExtra("user_id", login.twitterUserId);
                    intent.putExtra("screen_name", login.twitterUsername);
                    LoginTwitterActivity.this.setResult(-1, intent);
                    LoginTwitterActivity.this.finish();
                    return;
                }
                LoginTwitterActivity.this.dismissDialog();
                Util.showCenteredToast(LoginTwitterActivity.this, R.string.find_friends_twitter_xauth_error);
                return;
            }
            if (statusCode != 200 || login == null) {
                LoginTwitterActivity.this.dismissDialog();
                if (!TextUtils.isEmpty(reasonPhrase)) {
                    Util.showCenteredToast(LoginTwitterActivity.this, reasonPhrase);
                    return;
                } else {
                    Util.showCenteredToast(LoginTwitterActivity.this, R.string.error_xauth);
                    return;
                }
            }
            LoginTwitterActivity.this.mVineLogin = login;
            Components.authComponent().loginCheckTwitter(LoginTwitterActivity.this.mAppController, login, false);
        }
    }

    public static Intent getIntent(Context context) {
        return new Intent(context, (Class<?>) LoginTwitterActivity.class);
    }

    public static Intent getIntentWithFinish(Context context) {
        Intent intent = new Intent(context, (Class<?>) LoginTwitterActivity.class);
        intent.putExtra("finish", true);
        return intent;
    }

    public static Intent getIntentWithFinishAndMode(Context context, int mode) {
        Intent intent = new Intent(context, (Class<?>) LoginTwitterActivity.class);
        intent.putExtra("finish", true);
        intent.putExtra("login_mode", mode);
        return intent;
    }
}
