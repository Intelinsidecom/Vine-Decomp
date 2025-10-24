package co.vine.android;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import co.vine.android.client.VineAPI;
import co.vine.android.provider.SettingsManager;
import co.vine.android.scribe.model.AppNavigation;
import co.vine.android.search.SearchActivity;
import co.vine.android.util.BuildUtil;
import co.vine.android.util.CrossConstants;
import co.vine.android.util.LinkDispatcher;
import co.vine.android.util.Util;
import co.vine.android.views.WebViewContainerLayout;
import co.vine.android.widget.OnTabChangedListener;
import co.vine.android.widget.ScrollAwayFrameLayout;
import co.vine.android.widget.ScrollDeltaScrollView;
import com.edisonwang.android.slog.SLog;
import com.twitter.android.widget.TopScrollable;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

/* loaded from: classes.dex */
public class ExploreFragment extends BaseControllerFragment implements OnTabChangedListener, ScrollDeltaScrollView.ScrollDeltaListener, TopScrollable {
    private BroadcastReceiver mConnectivityReceiver = new BroadcastReceiver() { // from class: co.vine.android.ExploreFragment.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            NetworkInfo ni;
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
            if (connectivityManager != null) {
                try {
                    ni = connectivityManager.getActiveNetworkInfo();
                } catch (SecurityException e) {
                    return;
                }
            } else {
                ni = null;
            }
            if (ni != null && ni.isConnected()) {
                ExploreFragment.this.reloadWebView();
            }
        }
    };
    private View mEmpty;
    private TextView mEmptyText;
    private String mExploreUrl;
    private Handler mHandler;
    private SharedPreferences mPrefs;
    private RelativeLayout mProgressContainer;
    private ImageView mSadface;
    private View mSadfaceContainer;
    private ScrollAwayFrameLayout mSearchBarContainer;
    private WebViewContainerLayout mWebViewContainer;

    @Override // co.vine.android.BaseControllerFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mHandler = new Handler(Looper.getMainLooper());
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.explore, container, false);
        this.mEmpty = v.findViewById(R.id.empty);
        this.mEmptyText = (TextView) v.findViewById(R.id.empty_text);
        this.mProgressContainer = (RelativeLayout) v.findViewById(R.id.progress_container);
        this.mSadfaceContainer = v.findViewById(R.id.real_sadface);
        this.mSadface = (ImageView) v.findViewById(R.id.sadface);
        this.mWebViewContainer = (WebViewContainerLayout) v.findViewById(R.id.webview_container);
        this.mWebViewContainer.setInitializationListener(new WebViewInitializedListener());
        ((ScrollDeltaScrollView) v.findViewById(R.id.scrollview)).setScrollDeltaListener(this);
        this.mPrefs = Util.getDefaultSharedPrefs(getActivity());
        setupSearch(v);
        return v;
    }

    private void setupSearch(View rootView) {
        this.mSearchBarContainer = (ScrollAwayFrameLayout) rootView.findViewById(R.id.search_bar_container);
        this.mSearchBarContainer.findViewById(R.id.search_bar).setVisibility(0);
        this.mSearchBarContainer.findViewById(R.id.background_rect).setVisibility(0);
        rootView.findViewById(R.id.spacer).getLayoutParams().height = getResources().getDimensionPixelSize(R.dimen.tabbar_height) * 2;
        this.mSearchBarContainer.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.ExploreFragment.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                ExploreFragment.this.startSectionedSearchActivity();
            }
        });
        this.mSearchBarContainer.setVisibility(0);
        View searchButton = rootView.findViewById(R.id.search);
        searchButton.setVisibility(8);
    }

    @Override // android.support.v4.app.Fragment
    public void onViewCreated(View view, Bundle savedInstanceState) {
        this.mWebViewContainer.onViewCreated();
    }

    @Override // android.support.v4.app.Fragment
    public void onActivityCreated(Bundle savedInstanceState) throws Resources.NotFoundException {
        super.onActivityCreated(savedInstanceState);
        this.mExploreUrl = VineAPI.getInstance(getActivity()).getExploreUrl();
        if (getArguments().getBoolean("take_focus", false)) {
            HomeTabActivity callback = (HomeTabActivity) getActivity();
            callback.showPage(2, this);
            if (BuildUtil.isLogsOn()) {
                Log.d("ExploreFragment", "Explore tab took focus.");
            }
        }
        reloadWebView();
    }

    @Override // co.vine.android.BaseControllerFragment, co.vine.android.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        if (this.mWebViewContainer != null) {
            this.mWebViewContainer.onResume();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("abort_all_requests");
        getActivity().registerReceiver(this.mConnectivityReceiver, filter, CrossConstants.BROADCAST_PERMISSION, null);
    }

    @Override // co.vine.android.BaseControllerFragment, android.support.v4.app.Fragment
    public void onPause() {
        super.onPause();
        if (this.mWebViewContainer != null) {
            this.mWebViewContainer.onPause();
        }
        getActivity().unregisterReceiver(this.mConnectivityReceiver);
    }

    public void reloadWebView() {
        WebView webView = getWebView();
        if (webView != null) {
            webView.setVisibility(0);
            this.mSadfaceContainer.setVisibility(8);
            webView.setWebViewClient(new VineVideoListWebViewClient());
            WebSettings s = webView.getSettings();
            s.setCacheMode(2);
            Map<String, String> headers = new HashMap<>(1);
            String language = Util.getLocale();
            if (!TextUtils.isEmpty(language)) {
                headers.put("Accept-Language", language);
            }
            VineAPI api = VineAPI.getInstance(getActivity());
            headers.put("X-Vine-Client", api.getVineClientHeader());
            if (!BuildUtil.isProduction()) {
                headers.put("X-Vine-Auth", api.getAuthHeaderSecret());
            }
            String exploreUrl = getExploreUrl();
            if (BuildUtil.isLogsOn()) {
                Log.d("ExploreFragment", "Accessing explore url: " + exploreUrl);
            }
            webView.loadUrl(exploreUrl, headers);
        }
    }

    private String getExploreUrl() {
        String versionName;
        try {
            versionName = Util.getVersionName(getActivity());
        } catch (PackageManager.NameNotFoundException e) {
            versionName = "undefined";
        }
        String model = String.format(Locale.US, "%s_%s", Build.MANUFACTURER, Build.MODEL);
        ArrayList<BasicNameValuePair> data = new ArrayList<>();
        data.add(new BasicNameValuePair("model", model.replaceAll("\\s+", "-")));
        data.add(new BasicNameValuePair("v", versionName));
        data.add(new BasicNameValuePair("os", "android"));
        data.add(new BasicNameValuePair("ed", SettingsManager.getEdition(getActivity())));
        return this.mExploreUrl + "?" + URLEncodedUtils.format(data, "UTF-8");
    }

    @Override // co.vine.android.widget.OnTabChangedListener
    public void onMoveAway(int newPosition) {
    }

    @Override // co.vine.android.widget.OnTabChangedListener
    public void onMoveTo(int oldPosition) {
        long lastTime = this.mPrefs.getLong("last_explore", 0L);
        SharedPreferences.Editor edit = this.mPrefs.edit();
        long now = System.currentTimeMillis();
        if (edit != null) {
            if (lastTime > 0 && 900000 + lastTime < now) {
                reloadWebView();
                edit.putLong("last_explore", now).apply();
            } else if (lastTime <= 0) {
                edit.putLong("last_explore", now).apply();
            }
        }
    }

    private WebView getWebView() {
        if (this.mWebViewContainer != null) {
            return this.mWebViewContainer.getWebView();
        }
        return null;
    }

    @Override // com.twitter.android.widget.TopScrollable
    public boolean scrollTop() {
        WebView webView = getWebView();
        if (webView == null) {
            return false;
        }
        webView.scrollTo(0, 0);
        return true;
    }

    @Override // co.vine.android.widget.ScrollDeltaScrollView.ScrollDeltaListener
    public void onScroll(int delta) {
        ((HomeTabActivity) getActivity()).onScroll(delta);
        if (this.mSearchBarContainer != null) {
            this.mSearchBarContainer.onScroll(delta);
        }
    }

    @Override // android.support.v4.app.Fragment
    public void onDestroy() {
        if (this.mWebViewContainer != null) {
            this.mWebViewContainer.onDestroy();
        }
        super.onDestroy();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startSectionedSearchActivity() {
        Intent intent = new Intent(getActivity(), (Class<?>) SearchActivity.class);
        intent.putExtra("enter_anim", R.anim.activity_open_enter_slide);
        startActivity(intent);
    }

    @Override // co.vine.android.BaseFragment
    protected AppNavigation.Views getAppNavigationView() {
        return AppNavigation.Views.EXPLORE_WEBVIEW;
    }

    private static class WebViewInitializedListener implements WebViewContainerLayout.OnWebViewInitializedListener {
        private WebViewInitializedListener() {
        }

        @Override // co.vine.android.views.WebViewContainerLayout.OnWebViewInitializedListener
        @SuppressLint({"SetJavaScriptEnabled"})
        public void onInitialized(WebView webView) {
            WebSettings settings = webView.getSettings();
            settings.setSavePassword(false);
            settings.setSaveFormData(false);
            try {
                settings.setJavaScriptEnabled(true);
            } catch (Exception e) {
            }
            webView.setHorizontalScrollBarEnabled(false);
        }
    }

    class VineVideoListWebViewClient extends WebViewClient {
        VineVideoListWebViewClient() {
        }

        @Override // android.webkit.WebViewClient
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            if (!Build.MODEL.contains("GT-I9500") || !"5.0.1".equals(Build.VERSION.RELEASE) || !request.getUrl().getLastPathSegment().contains(".gif")) {
                return null;
            }
            InputStream is = new ByteArrayInputStream(new byte[0]);
            return new WebResourceResponse("text/html", "UTF-8", is);
        }

        @Override // android.webkit.WebViewClient
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Uri parsedUri = Uri.parse(url);
            if (!parsedUri.getScheme().equals("vine")) {
                return false;
            }
            LinkDispatcher.dispatch(parsedUri, ExploreFragment.this.getActivity());
            return true;
        }

        @Override // android.webkit.WebViewClient
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            String message;
            SLog.dWithTag("ExploreFragment", "WebViewClient errorCode=" + errorCode);
            view.setVisibility(8);
            if (ExploreFragment.this.mProgressContainer != null) {
                ExploreFragment.this.mProgressContainer.setVisibility(8);
            }
            if (ExploreFragment.this.mEmpty != null) {
                ExploreFragment.this.mEmpty.setVisibility(0);
            }
            if (ExploreFragment.this.mSadface != null) {
                ExploreFragment.this.mSadfaceContainer.setVisibility(0);
            }
            if (ExploreFragment.this.mEmptyText != null) {
                ExploreFragment.this.mEmptyText.setVisibility(0);
            }
            FragmentActivity activity = ExploreFragment.this.getActivity();
            if (activity != null) {
                if (errorCode == -2) {
                    message = activity.getString(R.string.explore_empty_no_connectivity);
                } else {
                    message = activity.getString(R.string.explore_empty_generic);
                }
                ExploreFragment.this.mEmptyText.setText(message);
            }
        }

        @Override // android.webkit.WebViewClient
        public void onPageFinished(WebView view, String url) {
            ExploreFragment.this.mEmpty.setVisibility(8);
            view.scrollTo(0, 0);
        }
    }
}
