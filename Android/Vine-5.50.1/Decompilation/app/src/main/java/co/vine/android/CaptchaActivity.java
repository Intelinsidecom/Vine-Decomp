package co.vine.android;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import co.vine.android.network.NetworkOperation;

/* loaded from: classes.dex */
public class CaptchaActivity extends BaseControllerActionBarActivity {
    private String mReqId;

    @Override // co.vine.android.BaseActionBarActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityDonut, android.app.Activity
    public void onCreate(Bundle bun) {
        super.onCreate(bun, R.layout.webview_activity, false);
        Bundle b = getIntent().getExtras();
        WebView webView = (WebView) findViewById(R.id.webview);
        this.mReqId = b.getString("rid");
        String captchaUrl = b.getString("captcha_url");
        WebSettings settings = webView.getSettings();
        settings.setUserAgentString(NetworkOperation.USER_AGENT_STRING);
        settings.setSavePassword(false);
        settings.setSaveFormData(false);
        webView.loadUrl(captchaUrl);
        webView.setWebViewClient(new WebViewClient() { // from class: co.vine.android.CaptchaActivity.1
            /* JADX WARN: Type inference failed for: r4v6, types: [co.vine.android.CaptchaActivity$1$1] */
            @Override // android.webkit.WebViewClient
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Uri parsedUri = Uri.parse(url);
                if (!parsedUri.getScheme().equals("vine") && !parsedUri.getScheme().equals("vine-dev")) {
                    return false;
                }
                String host = parsedUri.getHost();
                if ("_captcha_complete".equals(host)) {
                    Intent i = new Intent();
                    i.putExtra("rid", CaptchaActivity.this.mReqId);
                    CaptchaActivity.this.setResult(-1, i);
                    new AsyncTask<Void, Void, Void>() { // from class: co.vine.android.CaptchaActivity.1.1
                        /* JADX INFO: Access modifiers changed from: protected */
                        @Override // android.os.AsyncTask
                        public Void doInBackground(Void... voids) {
                            AppImpl.getInstance().clearUploadCaptchas(CaptchaActivity.this.getApplicationContext());
                            return null;
                        }
                    }.execute(new Void[0]);
                    CaptchaActivity.this.finish();
                }
                return true;
            }

            @Override // android.webkit.WebViewClient
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                view.setVisibility(8);
            }
        });
    }

    public static Intent getIntent(Context context, String captchaUrl, String reqId) {
        Intent i = new Intent(context, (Class<?>) CaptchaActivity.class);
        i.putExtra("captcha_url", captchaUrl);
        i.putExtra("rid", reqId);
        return i;
    }

    @Override // co.vine.android.BaseActionBarActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onBackPressed() {
        cancelCaptcha();
    }

    @Override // co.vine.android.BaseActionBarActivity, android.app.Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != 16908332) {
            return false;
        }
        cancelCaptcha();
        return true;
    }

    private void cancelCaptcha() {
        Intent i = new Intent();
        i.putExtra("rid", this.mReqId);
        setResult(2, i);
        finish();
    }
}
