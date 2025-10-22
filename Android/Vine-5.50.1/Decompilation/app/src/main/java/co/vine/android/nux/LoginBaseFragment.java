package co.vine.android.nux;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import co.vine.android.BaseFragment;
import co.vine.android.R;
import co.vine.android.client.AppController;
import co.vine.android.util.analytics.FlurryUtils;
import com.twitter.android.sdk.Twitter;

/* loaded from: classes.dex */
public abstract class LoginBaseFragment extends BaseFragment implements View.OnClickListener {
    boolean mLoginRequest;
    private Twitter mTwitter;

    abstract String getEmailButtonText();

    abstract String getTwitterButtonText();

    abstract void onEmailButtonClicked();

    abstract void putExtra(Intent intent);

    @Override // android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            this.mLoginRequest = args.getBoolean("login_request", true);
        }
        this.mTwitter = AppController.getInstance(getActivity()).getTwitter();
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.splash_tabs, container, false);
        Button emailButton = (Button) v.findViewById(R.id.email_button);
        RelativeLayout twitterButton = (RelativeLayout) v.findViewById(R.id.twitter_button);
        TextView twitterButtonText = (TextView) v.findViewById(R.id.twitter_button_text);
        emailButton.setText(getEmailButtonText());
        twitterButtonText.setText(getTwitterButtonText());
        emailButton.setOnClickListener(this);
        twitterButton.setOnClickListener(this);
        return v;
    }

    void onTwitterButtonClicked() {
        try {
            if (!this.mTwitter.startAuthActivityForResult(getActivity(), 1)) {
                if (this.mLoginRequest) {
                    startActivity(LoginTwitterActivity.getIntentWithFinish(getActivity()));
                } else {
                    Intent loginIntent = LoginTwitterActivity.getIntent(getActivity());
                    putExtra(loginIntent);
                    startActivity(loginIntent);
                }
            } else {
                FlurryUtils.trackNuxStarted("twitter-sso");
            }
        } catch (SecurityException e) {
            if (this.mLoginRequest) {
                startActivity(LoginTwitterActivity.getIntentWithFinish(getActivity()));
                return;
            }
            Intent loginIntent2 = LoginTwitterActivity.getIntent(getActivity());
            putExtra(loginIntent2);
            startActivity(loginIntent2);
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.email_button) {
            onEmailButtonClicked();
        } else if (id == R.id.twitter_button) {
            onTwitterButtonClicked();
        }
    }
}
