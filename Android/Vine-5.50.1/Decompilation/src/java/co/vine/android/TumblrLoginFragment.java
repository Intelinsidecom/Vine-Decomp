package co.vine.android;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import co.vine.android.client.AppSessionListener;
import co.vine.android.client.VineAccountHelper;
import co.vine.android.util.Util;

/* loaded from: classes.dex */
public class TumblrLoginFragment extends BaseControllerFragment {
    private EditText mPassword;
    private EditText mUsername;

    @Override // co.vine.android.BaseControllerFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAppSessionListener(new TumblrLoginSessionListener());
        setHasOptionsMenu(true);
    }

    @Override // android.support.v4.app.Fragment
    public void onViewCreated(View view, Bundle savedInstanceState) {
        this.mUsername = (EditText) view.findViewById(R.id.username);
        this.mPassword = (EditText) view.findViewById(R.id.password);
        this.mPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: co.vine.android.TumblrLoginFragment.1
            @Override // android.widget.TextView.OnEditorActionListener
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && event.getKeyCode() == 66) || (actionId == 6 && TumblrLoginFragment.this.validateAndDisplayToastIfCredentialsInvalid())) {
                    TumblrLoginFragment.this.login();
                    return false;
                }
                return false;
            }
        });
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tumblr_xauth, container, false);
    }

    @Override // android.support.v4.app.Fragment
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Util.setSoftKeyboardVisibility(getActivity(), this.mUsername, true);
    }

    @Override // android.support.v4.app.Fragment
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.login, menu);
    }

    @Override // android.support.v4.app.Fragment
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.sign_in) {
            if (validateAndDisplayToastIfCredentialsInvalid()) {
                login();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void login() {
        showProgressDialog(R.string.login_logging_in);
        this.mAppController.tumblrXauthLogin(this.mUsername.getText().toString(), this.mPassword.getText().toString());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean validateAndDisplayToastIfCredentialsInvalid() {
        if (this.mUsername.getText().toString().isEmpty()) {
            Util.showCenteredToast(getActivity(), R.string.login_tumblr_empty_username_email);
            return false;
        }
        if (this.mPassword.getText().toString().isEmpty()) {
            Util.showCenteredToast(getActivity(), R.string.login_empty_password);
            return false;
        }
        return true;
    }

    private class TumblrLoginSessionListener extends AppSessionListener {
        private TumblrLoginSessionListener() {
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onTumblrLoginComplete(String reqId, int statusCode, String reasonPhrase, String oauthToken, String oauthTokenSecret) {
            TumblrLoginFragment.this.dismissProgressDialog();
            if (TumblrLoginFragment.this.getActivity() != null) {
                if (statusCode == 200) {
                    if (TextUtils.isEmpty(oauthToken)) {
                        Util.showCenteredToast(TumblrLoginFragment.this.getActivity(), R.string.tumblr_signin_failed);
                        TumblrLoginFragment.this.getActivity().setResult(0);
                    } else {
                        VineAccountHelper.saveTumblrInfo(TumblrLoginFragment.this.getActivity(), oauthToken, oauthTokenSecret);
                        TumblrLoginFragment.this.getActivity().setResult(-1);
                    }
                    TumblrLoginFragment.this.getActivity().finish();
                    return;
                }
                if (statusCode == 401) {
                    Util.showCenteredToast(TumblrLoginFragment.this.getActivity(), R.string.API_ERROR_BAD_CREDENTIALS);
                } else if (!TextUtils.isEmpty(reasonPhrase)) {
                    Util.showCenteredToast(TumblrLoginFragment.this.getActivity(), reasonPhrase);
                } else {
                    Util.showCenteredToast(TumblrLoginFragment.this.getActivity(), R.string.API_ERROR_UNKNOWN_ERROR);
                }
            }
        }
    }
}
