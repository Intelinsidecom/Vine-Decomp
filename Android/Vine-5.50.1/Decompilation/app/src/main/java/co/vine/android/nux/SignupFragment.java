package co.vine.android.nux;

import android.content.Intent;
import android.os.Bundle;
import co.vine.android.R;
import co.vine.android.util.analytics.FlurryUtils;

/* loaded from: classes.dex */
public class SignupFragment extends LoginBaseFragment {
    @Override // co.vine.android.nux.LoginBaseFragment
    void onEmailButtonClicked() {
        Bundle extras = new Bundle();
        if (this.mLoginRequest) {
            extras.putBoolean("finish", true);
        }
        startActivity(SignUpPagerActivity.getIntent(getActivity(), extras));
        FlurryUtils.trackNuxStarted("email");
    }

    @Override // co.vine.android.nux.LoginBaseFragment
    void putExtra(Intent intent) {
        intent.putExtra("login_mode", 2);
    }

    @Override // co.vine.android.nux.LoginBaseFragment
    String getEmailButtonText() {
        return getString(R.string.signup_with_email);
    }

    @Override // co.vine.android.nux.LoginBaseFragment
    String getTwitterButtonText() {
        return getString(R.string.signup_with_twitter);
    }
}
