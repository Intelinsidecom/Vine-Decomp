package co.vine.android.nux;

import android.content.Intent;
import co.vine.android.R;

/* loaded from: classes.dex */
public class LoginFragment extends LoginBaseFragment {
    @Override // co.vine.android.nux.LoginBaseFragment
    void onEmailButtonClicked() {
        if (this.mLoginRequest) {
            startActivity(LoginActivity.getIntentWithFinish(getActivity()));
        } else {
            startActivity(LoginActivity.getIntent(getActivity()));
        }
    }

    @Override // co.vine.android.nux.LoginBaseFragment
    void putExtra(Intent intent) {
        intent.putExtra("login_mode", 3);
    }

    @Override // co.vine.android.nux.LoginBaseFragment
    String getEmailButtonText() {
        return getString(R.string.login_with_email);
    }

    @Override // co.vine.android.nux.LoginBaseFragment
    String getTwitterButtonText() {
        return getString(R.string.login_with_twitter);
    }
}
