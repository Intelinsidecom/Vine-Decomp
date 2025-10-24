package co.vine.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import co.vine.android.client.AppController;
import co.vine.android.util.analytics.FlurryUtils;

/* loaded from: classes.dex */
public class ConfirmationFlowActivity extends BaseActionBarActivity {
    public static Intent getIntent(Context c, String phone, boolean fromSignup) {
        Intent i = new Intent(c, (Class<?>) ConfirmationFlowActivity.class);
        i.putExtra("phone_number", phone);
        i.putExtra("from_sign_up", fromSignup);
        return i;
    }

    public static void requestPhoneVerification(AppController appController, String phone) {
        appController.requestPhoneVerification(appController.getActiveSession(), phone, appController.getActiveId());
    }

    @Override // co.vine.android.BaseActionBarActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityDonut, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        Fragment frag;
        super.onCreate(savedInstanceState, R.layout.fragment_full_layout, true, true);
        Intent intent = getIntent();
        boolean fromSignup = intent.getBooleanExtra("from_sign_up", false);
        FlurryUtils.trackNuxScreenDisplayed("verify_phone");
        if (fromSignup) {
            setupActionBar((Boolean) false, (Boolean) true, R.string.login_sign_up, (Boolean) false, (Boolean) false);
        } else {
            setupActionBar((Boolean) true, (Boolean) true, R.string.confirm_phone_title, (Boolean) true, (Boolean) false);
        }
        if (savedInstanceState == null) {
            if (fromSignup) {
                frag = new ConfirmationFlowVerificationFragment();
            } else {
                frag = new ConfirmationFlowEntryFragment();
            }
            Bundle args = BaseFragment.prepareArguments(intent);
            args.putString("phone_number", intent.getStringExtra("phone_number"));
            frag.setArguments(args);
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, frag, "confirmationFlow").commit();
        }
    }
}
