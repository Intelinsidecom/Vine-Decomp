package co.vine.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import co.vine.android.client.AppSessionListener;
import co.vine.android.util.Util;

/* loaded from: classes.dex */
public class ConfirmationFlowVerificationFragment extends BaseControllerFragment implements View.OnClickListener {
    private EditText mConfirmationBox;
    private MenuItem mDone;
    private TextView mErrorMessage;
    private Animation mFadeInAnimation;
    private Animation mFadeOutAnimation;
    private View mFadingIn;
    private View mFadingOut;
    private boolean mFromSignup;
    private Handler mHandler;
    private TextView mMessage;
    private String mPhone;
    private ProgressBar mProgressBar;
    private View mSendAgain;
    private boolean mSentAgain;
    private Runnable mShowMessageRunnable = new Runnable() { // from class: co.vine.android.ConfirmationFlowVerificationFragment.2
        @Override // java.lang.Runnable
        public void run() {
            ConfirmationFlowVerificationFragment.this.toggleErrorMessage(false);
        }
    };
    private Runnable mSendAgainRunnable = new Runnable() { // from class: co.vine.android.ConfirmationFlowVerificationFragment.3
        @Override // java.lang.Runnable
        public void run() {
            ConfirmationFlowVerificationFragment.this.mSendAgain.setOnClickListener(ConfirmationFlowVerificationFragment.this);
        }
    };
    private Animation.AnimationListener mFadeListener = new Animation.AnimationListener() { // from class: co.vine.android.ConfirmationFlowVerificationFragment.4
        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationStart(Animation animation) {
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationEnd(Animation animation) {
            if (ConfirmationFlowVerificationFragment.this.mFadingIn != null) {
                ConfirmationFlowVerificationFragment.this.mFadingIn.setVisibility(0);
            }
            if (ConfirmationFlowVerificationFragment.this.mFadingOut != null) {
                ConfirmationFlowVerificationFragment.this.mFadingOut.setVisibility(4);
            }
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationRepeat(Animation animation) {
        }
    };

    @Override // co.vine.android.BaseControllerFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAppSessionListener(new ConfirmationSessionListener());
        setHasOptionsMenu(true);
        this.mHandler = new Handler(Looper.getMainLooper());
        this.mFadeOutAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out_fast);
        this.mFadeOutAnimation.setAnimationListener(this.mFadeListener);
        this.mFadeInAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in_fast);
        this.mFadeInAnimation.setAnimationListener(this.mFadeListener);
        Bundle args = getArguments();
        if (args != null) {
            this.mPhone = args.getString("phone_number", "");
            this.mFromSignup = args.getBoolean("from_sign_up", false);
        }
    }

    @Override // android.support.v4.app.Fragment
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!this.mFromSignup && !TextUtils.isEmpty(this.mPhone)) {
            ConfirmationFlowActivity.requestPhoneVerification(this.mAppController, this.mPhone);
        }
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.confirm_phone_verify, container, false);
        if (v == null) {
            return null;
        }
        this.mMessage = (TextView) v.findViewById(R.id.confirm_phone_message);
        this.mMessage.setText(getString(R.string.confirm_phone_message, this.mPhone));
        if (this.mFromSignup) {
            View skip = v.findViewById(R.id.skip);
            skip.setVisibility(0);
            skip.setOnClickListener(this);
        }
        final EditText confirmationBox = (EditText) v.findViewById(R.id.phone_number_box);
        confirmationBox.addTextChangedListener(new TextWatcher() { // from class: co.vine.android.ConfirmationFlowVerificationFragment.1
            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(confirmationBox.getText())) {
                    ConfirmationFlowVerificationFragment.this.toggleDoneAction(true);
                } else {
                    ConfirmationFlowVerificationFragment.this.toggleDoneAction(false);
                }
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable s) {
            }
        });
        this.mConfirmationBox = confirmationBox;
        this.mErrorMessage = (TextView) v.findViewById(R.id.incorrect_code_message);
        this.mSendAgain = v.findViewById(R.id.confirm_send_again);
        this.mProgressBar = (ProgressBar) v.findViewById(R.id.submit_code_in_progress);
        return v;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void toggleDoneAction(boolean enabled) {
        if (this.mDone != null) {
            this.mDone.setEnabled(enabled);
        }
    }

    @Override // android.support.v4.app.Fragment
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.done, menu);
        this.mDone = menu.findItem(R.id.done);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override // android.support.v4.app.Fragment
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.done) {
            Editable e = this.mConfirmationBox.getText();
            if (e == null) {
                return false;
            }
            showProgressBar(true);
            item.setEnabled(false);
            this.mAppController.verifyPhoneNumber(this.mAppController.getActiveSession(), e.toString());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showProgressBar(boolean show) {
        if (this.mProgressBar != null) {
            this.mProgressBar.setVisibility(show ? 0 : 8);
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.confirm_send_again) {
            this.mSentAgain = true;
            ConfirmationFlowActivity.requestPhoneVerification(this.mAppController, this.mPhone);
            this.mHandler.postDelayed(this.mShowMessageRunnable, 500L);
            this.mSendAgain.setOnClickListener(null);
            return;
        }
        if (id == R.id.skip) {
            startActivity(new Intent(getActivity(), (Class<?>) ChannelsListNUXActivity.class));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void toggleErrorMessage(boolean show) {
        if (this.mMessage != null && this.mErrorMessage != null) {
            clearTextViewAnimations();
            if (show && this.mErrorMessage.getVisibility() != 0) {
                this.mMessage.startAnimation(this.mFadeOutAnimation);
                this.mErrorMessage.startAnimation(this.mFadeInAnimation);
                this.mFadingIn = this.mErrorMessage;
                this.mFadingOut = this.mMessage;
                return;
            }
            if (!show && this.mMessage.getVisibility() != 0) {
                this.mMessage.startAnimation(this.mFadeInAnimation);
                this.mErrorMessage.startAnimation(this.mFadeOutAnimation);
                this.mFadingIn = this.mMessage;
                this.mFadingOut = this.mErrorMessage;
            }
        }
    }

    private void clearTextViewAnimations() {
        this.mMessage.clearAnimation();
        this.mErrorMessage.clearAnimation();
    }

    @Override // co.vine.android.BaseControllerFragment, co.vine.android.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        this.mSendAgain.setOnClickListener(this);
    }

    @Override // co.vine.android.BaseControllerFragment, android.support.v4.app.Fragment
    public void onPause() {
        super.onPause();
        this.mHandler.removeCallbacks(this.mShowMessageRunnable);
        this.mHandler.removeCallbacks(this.mSendAgainRunnable);
        Util.setSoftKeyboardVisibility(getActivity(), this.mConfirmationBox, false);
    }

    private class ConfirmationSessionListener extends AppSessionListener {
        private ConfirmationSessionListener() {
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onRequestPhoneVerificationComplete(String reqId, int statusCode, String reasonPhrase, String phone) {
            Activity activity = ConfirmationFlowVerificationFragment.this.getActivity();
            ConfirmationFlowVerificationFragment.this.mHandler.postDelayed(ConfirmationFlowVerificationFragment.this.mSendAgainRunnable, 10000L);
            if (statusCode != 200 || !ConfirmationFlowVerificationFragment.this.mSentAgain) {
                if (!TextUtils.isEmpty(reasonPhrase)) {
                    Util.showCenteredToast(activity, reasonPhrase);
                    return;
                }
                return;
            }
            Util.showCenteredToast(activity, ConfirmationFlowVerificationFragment.this.getString(R.string.confirm_phone_again, phone));
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onVerifyPhoneNumberComplete(String reqId, int statusCode, String reasonPhrase) {
            Activity activity = ConfirmationFlowVerificationFragment.this.getActivity();
            ConfirmationFlowVerificationFragment.this.showProgressBar(false);
            ConfirmationFlowVerificationFragment.this.toggleDoneAction(true);
            if (statusCode != 200) {
                ConfirmationFlowVerificationFragment.this.toggleErrorMessage(true);
                if (!TextUtils.isEmpty(reasonPhrase)) {
                    ConfirmationFlowVerificationFragment.this.mErrorMessage.setText(reasonPhrase);
                    return;
                } else {
                    ConfirmationFlowVerificationFragment.this.mErrorMessage.setText(R.string.confirm_phone_error);
                    return;
                }
            }
            Util.showCenteredToast(activity, R.string.confirm_phone_success);
            ConfirmationFlowVerificationFragment.this.getActivity().setResult(1527);
            ConfirmationFlowVerificationFragment.this.getActivity().finish();
        }
    }
}
