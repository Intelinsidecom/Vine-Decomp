package co.vine.android;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import co.vine.android.client.VineAPI;
import co.vine.android.util.BuildUtil;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/* loaded from: classes.dex */
public class WebViewActivity extends BaseControllerActionBarActivity {
    @Override // co.vine.android.BaseActionBarActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityDonut, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.webview_activity, false);
        WebView webView = (WebView) findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient() { // from class: co.vine.android.WebViewActivity.1
            @Override // android.webkit.WebViewClient
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url == null || url.startsWith("https://vine.co") || url.startsWith("https://twitter.com")) {
                    return false;
                }
                view.getContext().startActivity(new Intent("android.intent.action.VIEW", Uri.parse(url)));
                return true;
            }
        });
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSavePassword(false);
        settings.setSaveFormData(false);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            Map<String, String> headers = new HashMap<>(1);
            String language = Locale.getDefault().getLanguage();
            if (!TextUtils.isEmpty(language)) {
                headers.put("Accept-Language", language);
                headers.put("X-Vine-Client", VineAPI.getInstance(this).getVineClientHeader());
            }
            switch (b.getInt("type")) {
                case 1:
                    webView.loadUrl(getString(R.string.privacy_policy_url), headers);
                    break;
                case 2:
                    webView.loadUrl(getString(R.string.tos_url), headers);
                    break;
                case 3:
                    webView.loadUrl(getString(R.string.privacy_policy_twitter_url), headers);
                    break;
                case 4:
                    webView.loadUrl(getString(R.string.tos_twitter_url), headers);
                    break;
                case 5:
                    if (BuildUtil.isAStar()) {
                        webView.loadUrl("file:///android_asset/astar_attribution.html");
                        break;
                    } else {
                        webView.loadUrl(getString(R.string.attribution_url), headers);
                        break;
                    }
                case 6:
                    webView.loadUrl(getString(R.string.vine_help_url), headers);
                    break;
                case 7:
                    webView.loadUrl(getString(R.string.vine_rules_url), headers);
                    break;
            }
        } else {
            Uri data = getIntent().getData();
            if (data != null) {
                webView.loadUrl(data.toString());
            }
        }
        setupActionBar((Boolean) true, (Boolean) null, (String) null, (Boolean) null, (Boolean) true);
    }

    @Override // co.vine.android.BaseActionBarActivity, android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.done, menu);
        menu.findItem(R.id.done).setEnabled(true);
        return true;
    }

    @Override // co.vine.android.BaseActionBarActivity, android.app.Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != R.id.done) {
            return super.onOptionsItemSelected(item);
        }
        finish();
        return true;
    }

    public static void start(Context context, Uri data) {
        context.startActivity(new Intent(context, (Class<?>) WebViewActivity.class).setData(data));
    }
}
