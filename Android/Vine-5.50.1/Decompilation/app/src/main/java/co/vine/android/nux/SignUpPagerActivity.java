package co.vine.android.nux;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.text.TextUtils;
import android.view.MenuItem;
import co.vine.android.BaseControllerActionBarActivity;
import co.vine.android.R;
import co.vine.android.api.TwitterUser;
import co.vine.android.api.VineLogin;
import co.vine.android.client.ServiceNotification;
import co.vine.android.client.Session;
import co.vine.android.client.TwitterVineApp;
import co.vine.android.service.components.Components;
import co.vine.android.service.components.authentication.AuthenticationActionListener;
import co.vine.android.util.AppTrackingUtil;
import co.vine.android.util.DialogUtils;
import co.vine.android.util.PhoneConfirmationUtil;
import co.vine.android.util.Util;
import co.vine.android.util.analytics.FlurryUtils;
import co.vine.android.widget.ConfigurableViewPager;
import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.twitter.sdk.android.core.AuthToken;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import java.lang.ref.WeakReference;
import twitter4j.conf.PropertyConfiguration;

/* loaded from: classes.dex */
public class SignUpPagerActivity extends BaseControllerActionBarActivity {
    private final AuthenticationActionListener mAuthListener = new SignUpListener();
    private WeakReference<Fragment> mAvatarFrag;
    private WeakReference<Fragment> mDetailsFrag;
    private boolean mFinish;
    private boolean mFollowVineOnTwitter;
    private String mLogin;
    private String mName;
    private ConfigurableViewPager mPager;
    private String mPassword;
    private String mPhone;
    private Uri mProfile;
    private TwitterUser mTwitterUser;
    private VineLogin mVineLogin;

    @Override // co.vine.android.BaseActionBarActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityDonut, android.app.Activity
    @SuppressLint({"MissingSuperCall"})
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.sign_up_flow, false);
        Intent intent = getIntent();
        if (intent != null) {
            this.mFinish = intent.getBooleanExtra("finish", false);
        }
        this.mPager = (ConfigurableViewPager) findViewById(R.id.pager);
        this.mPager.setSwipingEnabled(false);
        this.mPager.setAdapter(new SignUpPagerAdapter(getSupportFragmentManager()));
        this.mVineLogin = (VineLogin) getIntent().getParcelableExtra(PropertyConfiguration.USER);
        if (this.mVineLogin != null && this.mVineLogin.loginType == 2) {
            this.mPager.setCurrentItem(1);
        }
        if (savedInstanceState != null) {
            this.mLogin = savedInstanceState.getString("s_login");
            this.mName = savedInstanceState.getString("s_name");
            this.mPassword = savedInstanceState.getString("s_password");
            this.mPhone = savedInstanceState.getString("s_phone");
            this.mProfile = (Uri) savedInstanceState.getParcelable("s_profile");
        }
        setupActionBar((Boolean) true, (Boolean) true, (String) null, (Boolean) true, (Boolean) true);
        FlurryUtils.trackNuxScreenDisplayed("sign_up_username");
    }

    @Override // co.vine.android.BaseActionBarActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("s_login", this.mLogin);
        outState.putString("s_password", this.mPassword);
        outState.putString("s_name", this.mName);
        outState.putString("s_phone", this.mPhone);
        outState.putParcelable("s_profile", this.mProfile);
    }

    @Override // co.vine.android.BaseControllerActionBarActivity, co.vine.android.BaseActionBarActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        Components.authComponent().addListener(this.mAuthListener);
        if (!verifyDigitsSuccess(null) && this.mAppController.isLoggedIn() && this.mLogin != null && this.mLogin.equalsIgnoreCase(this.mAppController.getActiveSession().getUsername())) {
            toNuxFromSignUp();
        }
    }

    @Override // co.vine.android.BaseControllerActionBarActivity, co.vine.android.BaseActionBarActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onPause() {
        super.onPause();
        Components.authComponent().removeListener(this.mAuthListener);
    }

    @Override // co.vine.android.BaseActionBarActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onBackPressed() {
        if ((this.mVineLogin != null && this.mVineLogin.loginType == 2) || this.mPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            this.mPager.setCurrentItem(this.mPager.getCurrentItem() - 1);
        }
    }

    @Override // co.vine.android.BaseActionBarActivity, android.app.Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void toNextStep() {
        switch (this.mPager.getCurrentItem()) {
            case 0:
                this.mPager.setCurrentItem(1);
                FlurryUtils.trackNuxScreenDisplayed("sign_up_email");
                if (this.mAvatarFrag != null) {
                    Fragment avatarFrag = this.mAvatarFrag.get();
                    if (avatarFrag instanceof SignUpNameAvatarFragment) {
                        ((SignUpNameAvatarFragment) avatarFrag).onMoveTo();
                        break;
                    }
                }
                break;
            case 1:
                if (this.mTwitterUser != null) {
                    ProgressDialog dialog = new ProgressDialog(this, R.style.ProgressDialogTheme);
                    dialog.setMessage(getString(R.string.login_validating));
                    dialog.setProgress(0);
                    DialogUtils.showDialogUnsafe(dialog);
                    this.mProgressDialog = dialog;
                    Components.authComponent().signUp(this.mAppController, null, null, this.mName, this.mPhone, this.mProfile, this.mTwitterUser, this.mVineLogin);
                    if (this.mFollowVineOnTwitter) {
                        this.mAppController.followVineOnTwitter();
                    }
                    FlurryUtils.trackNuxScreenDisplayed("sign_up_twitter");
                    break;
                } else {
                    ProgressDialog dialog2 = new ProgressDialog(this, R.style.ProgressDialogTheme);
                    this.mProgressDialog = dialog2;
                    dialog2.setProgressStyle(0);
                    dialog2.setMessage(getString(R.string.signing_up));
                    DialogUtils.showDialogUnsafe(dialog2);
                    Components.authComponent().signUp(this.mAppController, this.mLogin, this.mPassword, this.mName, this.mPhone, this.mProfile, null, null);
                    break;
                }
        }
    }

    protected void setBarTitle(int resId) {
        setupActionBar((Boolean) null, (Boolean) true, resId, (Boolean) null, (Boolean) true);
    }

    public void setLogin(String login) {
        this.mLogin = login;
    }

    public void setPassword(String password) {
        this.mPassword = password;
    }

    public void setPhone(String phone) {
        this.mPhone = phone;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public void setProfile(Uri uri) {
        this.mProfile = uri;
    }

    public void setTwitterUser(TwitterUser tUser) {
        this.mTwitterUser = tUser;
    }

    public void setFollowVineOnTwitter(boolean followVineOnTwitter) {
        this.mFollowVineOnTwitter = followVineOnTwitter;
    }

    @Override // co.vine.android.BaseActionBarActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Fragment frag;
        Fragment frag2;
        switch (requestCode) {
            case 1653:
                toNuxFromSignUp();
                break;
            default:
                switch (this.mPager.getCurrentItem()) {
                    case 0:
                        if (this.mDetailsFrag != null && (frag = this.mDetailsFrag.get()) != null) {
                            frag.onActivityResult(requestCode, resultCode, data);
                            break;
                        }
                        break;
                    case 1:
                        if (this.mAvatarFrag != null && (frag2 = this.mAvatarFrag.get()) != null) {
                            frag2.onActivityResult(requestCode, resultCode, data);
                            break;
                        }
                        break;
                }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void toNuxFromSignUp() {
        Session session = this.mAppController.getActiveSessionReadOnly();
        if (session != null) {
            AppTrackingUtil.logUserRegistration(this, session);
        }
        NuxResolver.toNuxFromSignup(this);
    }

    private class SignUpPagerAdapter extends FragmentStatePagerAdapter {
        public SignUpPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override // android.support.v4.app.FragmentStatePagerAdapter
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    SignUpDetailsFragment detailsFragment = new SignUpDetailsFragment();
                    SignUpPagerActivity.this.mDetailsFrag = new WeakReference(detailsFragment);
                    return detailsFragment;
                case 1:
                    SignUpNameAvatarFragment avatarFragment = new SignUpNameAvatarFragment();
                    if (SignUpPagerActivity.this.mVineLogin != null) {
                        avatarFragment.setArguments(SignUpPagerActivity.this.getIntent().getExtras());
                    }
                    SignUpPagerActivity.this.mAvatarFrag = new WeakReference(avatarFragment);
                    return avatarFragment;
                default:
                    throw new IllegalArgumentException("no pager found for " + i);
            }
        }

        @Override // android.support.v4.view.PagerAdapter
        public int getCount() {
            return 2;
        }
    }

    private final class SignUpListener extends AuthenticationActionListener {
        private SignUpListener() {
        }

        @Override // co.vine.android.service.components.authentication.AuthenticationActionListener
        public void digitVerificationSuccess() {
            SignUpPagerActivity.this.toNuxFromSignUp();
        }

        @Override // co.vine.android.service.components.authentication.AuthenticationActionListener
        public void digitVerificationFailure() {
            SignUpPagerActivity.this.toNuxFromSignUp();
        }

        @Override // co.vine.android.service.components.authentication.AuthenticationActionListener
        public void onSignUpComplete(String reqId, int statusCode, String reasonPhrase, VineLogin vineLogin, String accountName, String password, int resultStatus) {
            Util.fetchClientFlags(SignUpPagerActivity.this.getApplication(), false, true);
            if (statusCode != 200 || vineLogin == null || vineLogin.userId <= 0) {
                SignUpPagerActivity.this.dismissDialog();
                if (!TextUtils.isEmpty(reasonPhrase)) {
                    Util.showCenteredToast(SignUpPagerActivity.this, reasonPhrase);
                    return;
                } else {
                    Util.showCenteredToast(SignUpPagerActivity.this, R.string.error_sign_up);
                    return;
                }
            }
            switch (vineLogin.loginType) {
                case 1:
                    if (resultStatus == 0) {
                        SignUpPagerActivity.this.dismissDialog();
                        if (!SignUpPagerActivity.this.mFinish) {
                            if (!TextUtils.isEmpty(SignUpPagerActivity.this.mPhone)) {
                                SignUpPagerActivity.this.initPhoneVerification();
                                break;
                            } else {
                                SignUpPagerActivity.this.toNuxFromSignUp();
                                break;
                            }
                        } else {
                            SignUpPagerActivity.this.setResult(-1);
                            SignUpPagerActivity.this.finish();
                            break;
                        }
                    } else {
                        SignUpPagerActivity.this.dismissDialog();
                        Util.showCenteredToast(SignUpPagerActivity.this, R.string.failed_to_add_account_on_device);
                        SignUpPagerActivity.this.finish();
                        break;
                    }
                case 2:
                    if (SignUpPagerActivity.this.mVineLogin != null) {
                        FlurryUtils.trackNuxAccountCreated("twitter");
                        ProgressDialog dialog = new ProgressDialog(SignUpPagerActivity.this, R.style.ProgressDialogTheme);
                        SignUpPagerActivity.this.mProgressDialog = dialog;
                        dialog.setProgressStyle(0);
                        dialog.setMessage(SignUpPagerActivity.this.getString(R.string.signing_up));
                        DialogUtils.showDialogUnsafe(dialog);
                        Components.authComponent().loginCheckTwitter(SignUpPagerActivity.this.mAppController, SignUpPagerActivity.this.mVineLogin, false);
                        break;
                    } else {
                        FlurryUtils.onSignupFailure(true, -1, "mVineLogin is null.", -1);
                        SignUpPagerActivity.this.finish();
                        break;
                    }
            }
        }

        @Override // co.vine.android.service.components.authentication.AuthenticationActionListener
        public void onLoginSuccess() {
            SignUpPagerActivity.this.dismissDialog();
            Util.showCenteredToast(SignUpPagerActivity.this, SignUpPagerActivity.this.getString(R.string.login_success));
            if (!SignUpPagerActivity.this.mFinish) {
                if (!TextUtils.isEmpty(SignUpPagerActivity.this.mPhone)) {
                    SignUpPagerActivity.this.initPhoneVerification();
                    return;
                } else {
                    SignUpPagerActivity.this.toNuxFromSignUp();
                    SignUpPagerActivity.this.mAppController.getEditions();
                    return;
                }
            }
            SignUpPagerActivity.this.setResult(-1);
            SignUpPagerActivity.this.finish();
        }

        @Override // co.vine.android.service.components.authentication.AuthenticationActionListener
        public void onLoginFailed(Context context, String reasonPhrase) {
            super.onLoginFailed(context, reasonPhrase);
            SignUpPagerActivity.this.dismissDialog();
        }

        @Override // co.vine.android.service.components.authentication.AuthenticationActionListener
        public void onCheckTwitterLoginSuccess(VineLogin login) {
            SignUpPagerActivity.this.dismissDialog();
            if (!SignUpPagerActivity.this.mFinish) {
                if (!TextUtils.isEmpty(SignUpPagerActivity.this.mPhone)) {
                    SignUpPagerActivity.this.initPhoneVerification();
                    return;
                } else {
                    SignUpPagerActivity.this.toNuxFromSignUp();
                    return;
                }
            }
            SignUpPagerActivity.this.setResult(-1);
            SignUpPagerActivity.this.finish();
        }

        @Override // co.vine.android.service.components.authentication.AuthenticationActionListener
        public void onCheckTwitterLoginBadCredentials(VineLogin login) {
            SignUpPagerActivity.this.dismissDialog();
            Bundle extras = new Bundle();
            extras.putParcelable(PropertyConfiguration.USER, login);
            SignUpPagerActivity.this.startActivity(SignUpPagerActivity.getIntent(SignUpPagerActivity.this, extras));
        }

        @Override // co.vine.android.service.components.authentication.AuthenticationActionListener
        public void onCheckTwitterLoginDeactivated(VineLogin login) {
            SignUpPagerActivity.this.dismissDialog();
        }

        @Override // co.vine.android.service.components.authentication.AuthenticationActionListener
        public void onCheckTwitterLoginUnknownError(ServiceNotification notification) {
            SignUpPagerActivity.this.dismissDialog();
            super.onCheckTwitterLoginUnknownError(notification);
        }

        @Override // co.vine.android.service.components.authentication.AuthenticationActionListener
        public void onCheckTwitterLoginFailedToCreateLocalAccount(Context context) {
            SignUpPagerActivity.this.dismissDialog();
            super.onCheckTwitterLoginFailedToCreateLocalAccount(context);
            SignUpPagerActivity.this.finish();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initPhoneVerification() {
        PhoneConfirmationUtil.confirmPhoneNumber(this, new AuthCallback() { // from class: co.vine.android.nux.SignUpPagerActivity.1
            @Override // com.digits.sdk.android.AuthCallback
            public void success(DigitsSession digitsSession, String s) {
                SignUpPagerActivity.this.verifyDigitsSuccess(digitsSession);
            }

            @Override // com.digits.sdk.android.AuthCallback
            public void failure(DigitsException e) {
                SignUpPagerActivity.this.toNuxFromSignUp();
            }
        }, this.mPhone, 1653);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean verifyDigitsSuccess(DigitsSession digitsSession) {
        AuthToken token = null;
        if (digitsSession != null) {
            token = digitsSession.getAuthToken();
        } else {
            DigitsSession session = (DigitsSession) Digits.getSessionManager().getActiveSession();
            if (session != null) {
                token = session.getAuthToken();
            }
        }
        if (token instanceof TwitterAuthToken) {
            Components.authComponent().verifyDigits(this.mAppController, this.mAppController.getActiveSession(), (TwitterAuthToken) token, new TwitterAuthConfig(TwitterVineApp.API_KEY, TwitterVineApp.API_SECRET));
            Digits.getSessionManager().clearActiveSession();
            return true;
        }
        return false;
    }

    public static Intent getIntent(Context context, Bundle extras) {
        Intent intent = new Intent(context, (Class<?>) SignUpPagerActivity.class);
        if (extras != null) {
            intent.putExtras(extras);
        }
        return intent;
    }
}
