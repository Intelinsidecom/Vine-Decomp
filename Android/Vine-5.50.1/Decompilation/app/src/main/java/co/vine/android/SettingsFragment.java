package co.vine.android;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import co.vine.android.ImagePicker;
import co.vine.android.SetThumbnailTask;
import co.vine.android.api.VineUser;
import co.vine.android.api.response.VineEditions;
import co.vine.android.cache.image.ImageKey;
import co.vine.android.cache.image.UrlImage;
import co.vine.android.client.AppController;
import co.vine.android.client.AppSessionListener;
import co.vine.android.client.Session;
import co.vine.android.client.TwitterVineApp;
import co.vine.android.client.VineAccountHelper;
import co.vine.android.drawable.RecyclableBitmapDrawable;
import co.vine.android.network.UrlCachePolicy;
import co.vine.android.prefetch.PrefetchManager;
import co.vine.android.provider.Vine;
import co.vine.android.provider.VineDatabaseSQL;
import co.vine.android.scribe.model.AppNavigation;
import co.vine.android.service.GCMRegistrationService;
import co.vine.android.service.components.Components;
import co.vine.android.service.components.authentication.AuthenticationActionListener;
import co.vine.android.social.FacebookHelper;
import co.vine.android.social.TumblrHelper;
import co.vine.android.util.BuildUtil;
import co.vine.android.util.ClientFlagsHelper;
import co.vine.android.util.CrashUtil;
import co.vine.android.util.CrossConstants;
import co.vine.android.util.ImageUtils;
import co.vine.android.util.PhoneConfirmationUtil;
import co.vine.android.util.ShareUtils;
import co.vine.android.util.Util;
import co.vine.android.util.analytics.FlurryUtils;
import co.vine.android.views.SimpleTextWatcher;
import co.vine.android.widget.ColorPicker;
import co.vine.android.widget.TypingEditText;
import co.vine.android.widgets.PromptDialogSupportFragment;
import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.edisonwang.android.slog.SLog;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.googlecode.javacv.cpp.opencv_core;
import com.twitter.android.sdk.Twitter;
import com.twitter.sdk.android.core.AuthToken;
import com.twitter.sdk.android.core.SessionManager;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import java.util.HashMap;

/* loaded from: classes.dex */
public class SettingsFragment extends BaseControllerFragment implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener, CompoundButton.OnCheckedChangeListener, ImagePicker.Listener, SetThumbnailTask.SetThumbnailListener, ColorPicker.ColorClickListener, TypingEditText.TypingListener, PromptDialogSupportFragment.OnDialogDoneListener {
    private ImageView mAlertIcon;
    private AppController mAppController;
    private ImageView mAvatarActionView;
    private ImageView mAvatarImageView;
    private ImageKey mAvatarKey;
    private Uri mAvatarUri;
    private int mBackgroundColor;
    private TextView mClearCacheValue;
    private int mColorIndex;
    private String mDescription;
    private TextView mEditTextEmail;
    private TextView mEditTextPhone;
    private String mEdition;
    private Spinner mEditionsSpinner;
    private SharedPreferences.Editor mEditor;
    private String mEmail;
    private ImageView mEmailVerified;
    private int mErrorCode;
    private TextView mFacebookValue;
    private ImagePicker mImagePicker;
    private Intent mInviteSmsIntent;
    private boolean mIsEmailVerified;
    private boolean mIsPhoneVerified;
    private String mLocation;
    private String mName;
    private ViewGroup mNotifications;
    private String mOriginalAvatarUrl;
    private int mOriginalBackgroundColor;
    private int mOriginalColorIndex;
    private String mOriginalDescription;
    private String mOriginalEdition;
    private String mOriginalEmail;
    private boolean mOriginalEmailVerified;
    private String mOriginalLocation;
    private String mOriginalName;
    private String mOriginalPhone;
    private boolean mOriginalPhoneVerified;
    private String mPhone;
    private ImageView mPhoneVerified;
    private boolean mPhotoChanged;
    private SharedPreferences mPrefs;
    private View mProfileBackground;
    private ProgressDialog mProgress;
    private ScrollView mScrollView;
    private EditionsSpinnerAdapter mSpinnerAdapter;
    private Bitmap mThumbnail;
    private TextView mTumblrValue;
    private boolean mTwitterConnected;
    private boolean mTwitterLogin;
    private int mVersionTapCount;
    private boolean mEditionsFetched = false;
    private final AuthenticationActionListener mAuthListener = new AuthenticationActionListener() { // from class: co.vine.android.SettingsFragment.1
        @Override // co.vine.android.service.components.authentication.AuthenticationActionListener
        public void digitVerificationSuccess() {
            SettingsFragment.this.successPhoneVerification();
            SLog.d("Digit success...now verified.");
        }

        @Override // co.vine.android.service.components.authentication.AuthenticationActionListener
        public void digitVerificationFailure() {
            SettingsFragment.this.failurePhoneVerification();
        }

        @Override // co.vine.android.service.components.authentication.AuthenticationActionListener
        public void onLogoutComplete(String reqId) {
            if (SettingsFragment.this.getActivity() != null && !SettingsFragment.this.getActivity().isFinishing()) {
                if (SettingsFragment.this.mProgress != null) {
                    SettingsFragment.this.mProgress.dismiss();
                }
                StartActivity.toStart(SettingsFragment.this.getActivity());
            }
        }
    };

    private void resetPrefs() {
        SharedPreferences.Editor editor = this.mEditor;
        editor.putString("settings_profile_name", this.mOriginalName);
        editor.putString("settings_profile_description", this.mOriginalDescription);
        editor.putString("settings_profile_location", this.mOriginalLocation);
        editor.putString("settings_profile_email", this.mOriginalEmail);
        editor.putString("settings_profile_phone", this.mOriginalPhone);
        editor.putString("settings_profile_avatar_url", this.mOriginalAvatarUrl);
        editor.putString("settings_edition", this.mOriginalEdition);
        editor.putInt("color_index", this.mOriginalColorIndex);
        editor.putInt("profile_background", this.mOriginalBackgroundColor);
        editor.commit();
    }

    private boolean isDirty() {
        return (this.mOriginalName.equals(this.mName) && this.mOriginalEmail.equals(this.mEmail) && this.mOriginalDescription.equals(this.mDescription) && this.mOriginalLocation.equals(this.mLocation) && this.mOriginalPhone.equals(this.mPhone) && this.mOriginalEdition.equals(this.mEdition) && this.mOriginalColorIndex == this.mColorIndex && !this.mPhotoChanged) ? false : true;
    }

    @Override // co.vine.android.BaseControllerFragment, android.support.v4.app.Fragment
    public void onPause() {
        super.onPause();
        if (this.mProgress != null) {
            this.mProgress.dismiss();
        }
        Components.authComponent().removeListener(this.mAuthListener);
    }

    @Override // android.support.v4.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("state_name", this.mName);
        outState.putString("state_desc", this.mDescription);
        outState.putString("state_loc", this.mLocation);
        outState.putString("state_phone", this.mPhone);
        outState.putString("state_email", this.mEmail);
        outState.putString("state_edition", this.mEdition);
        outState.putBoolean("state_editions_fetched", this.mEditionsFetched);
        outState.putParcelable("state_avatar_url", this.mAvatarUri);
        outState.putInt("state_background_color", this.mBackgroundColor);
        outState.putInt("state_color_index", this.mColorIndex);
    }

    private void setOriginalPreferenceValues() {
        SharedPreferences prefs = this.mPrefs;
        this.mOriginalName = prefs.getString("settings_profile_name", "");
        this.mName = this.mOriginalName;
        this.mOriginalDescription = prefs.getString("settings_profile_description", "");
        this.mDescription = this.mOriginalDescription;
        this.mOriginalLocation = prefs.getString("settings_profile_location", "");
        this.mLocation = this.mOriginalLocation;
        this.mOriginalEmail = prefs.getString("settings_profile_email", "");
        this.mEmail = this.mOriginalEmail;
        this.mOriginalPhone = prefs.getString("settings_profile_phone", "");
        this.mPhone = this.mOriginalPhone;
        this.mOriginalEdition = prefs.getString("settings_edition", "");
        this.mEdition = this.mOriginalEdition;
        this.mTwitterConnected = prefs.getBoolean("settings_twitter_connected", false);
        this.mAvatarUri = Uri.parse(prefs.getString("settings_profile_avatar_url", ""));
        this.mOriginalAvatarUrl = this.mAvatarUri.toString();
        this.mBackgroundColor = prefs.getInt("profile_background", Settings.DEFAULT_PROFILE_COLOR);
        this.mOriginalBackgroundColor = this.mBackgroundColor;
        int i = 0;
        while (true) {
            if (i >= Settings.PROFILE_BACKGROUND_COLORS.length) {
                break;
            }
            if (Settings.PROFILE_BACKGROUND_COLORS[i] != this.mBackgroundColor) {
                i++;
            } else {
                this.mOriginalColorIndex = i;
                break;
            }
        }
        this.mColorIndex = this.mOriginalColorIndex;
        this.mOriginalEmailVerified = prefs.getBoolean("profile_email_verified", false);
        this.mIsEmailVerified = this.mOriginalEmailVerified;
        this.mOriginalPhoneVerified = prefs.getBoolean("profile_phone_verified", false);
        this.mIsPhoneVerified = this.mOriginalPhoneVerified;
    }

    @Override // co.vine.android.BaseControllerFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        this.mPrefs = Util.getDefaultSharedPrefs(getActivity());
        setAppSessionListener(new SettingsListener());
        this.mAppController = AppController.getInstance(getActivity());
        this.mImagePicker = new ImagePicker(getActivity(), this, this.mAppController.getActiveId());
        this.mEditor = this.mPrefs.edit();
        this.mAppController.fetchUsersMe(this.mAppController.getActiveId(), UrlCachePolicy.FORCE_REFRESH);
    }

    @Override // co.vine.android.BaseControllerFragment, co.vine.android.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        if (this.mSpinnerAdapter.getCursor() == null) {
            getLoaderManager().initLoader(0, null, this);
        } else if (this.mSpinnerAdapter.isEmpty() && !this.mEditionsFetched) {
            this.mEditionsFetched = true;
            this.mAppController.getEditions();
        }
        if (!PrefetchManager.getInstance(getActivity()).isClientPrefetchEnabled()) {
            View view = getView();
            view.findViewById(R.id.sync).setVisibility(8);
            view.findViewById(R.id.sync_divider).setVisibility(8);
        }
        Components.authComponent().addListener(this.mAuthListener);
        checkDigitsSuccess();
        updateVerificationIcons();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkDigitsSuccess() {
        SessionManager<DigitsSession> dsm = Digits.getSessionManager();
        DigitsSession das = (DigitsSession) dsm.getActiveSession();
        if (das != null) {
            AuthToken token = das.getAuthToken();
            if (token instanceof TwitterAuthToken) {
                SLog.d("Digit success...now verify it.");
                Components.authComponent().verifyDigits(this.mAppController, this.mAppController.getActiveSession(), (TwitterAuthToken) token, new TwitterAuthConfig(TwitterVineApp.API_KEY, TwitterVineApp.API_SECRET));
                dsm.clearActiveSession();
            }
        }
    }

    private class GetCacheSizeAsyncTask extends AsyncTask<Void, Void, String> {
        private GetCacheSizeAsyncTask() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(String size) {
            if (size != null) {
                SettingsFragment.this.mClearCacheValue.setText(size);
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public String doInBackground(Void... params) {
            try {
                FragmentActivity activity = SettingsFragment.this.getActivity();
                if (activity != null) {
                    return Util.formatFileSize(SettingsFragment.this.getResources(), Util.getCacheSize(activity));
                }
            } catch (Exception e) {
                CrashUtil.logException(e);
            }
            return null;
        }
    }

    @Override // android.support.v4.app.Fragment
    @SuppressLint({"NewApi"})
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) throws Resources.NotFoundException, PackageManager.NameNotFoundException {
        View v = inflater.inflate(R.layout.settings, container, false);
        Resources res = getResources();
        setOriginalPreferenceValues();
        if (savedInstanceState != null) {
            this.mName = savedInstanceState.getString("state_name");
            this.mDescription = savedInstanceState.getString("state_desc");
            this.mLocation = savedInstanceState.getString("state_loc");
            this.mPhone = savedInstanceState.getString("state_phone");
            this.mEmail = savedInstanceState.getString("state_email");
            this.mEditionsFetched = savedInstanceState.getBoolean("state_editions_fetched");
            this.mEdition = savedInstanceState.getString("state_edition");
            this.mAvatarUri = (Uri) savedInstanceState.getParcelable("state_avatar_url");
            this.mBackgroundColor = savedInstanceState.getInt("state_background_color");
            this.mColorIndex = savedInstanceState.getInt("state_color_index");
        }
        this.mScrollView = (ScrollView) v.findViewById(R.id.scrollview);
        this.mEmailVerified = (ImageView) v.findViewById(R.id.email_verified);
        this.mEmailVerified.setOnClickListener(this);
        this.mPhoneVerified = (ImageView) v.findViewById(R.id.phone_verified);
        this.mPhoneVerified.setOnClickListener(this);
        ColorPicker colorPicker = (ColorPicker) v.findViewById(R.id.color_picker);
        colorPicker.setOnColorClickListener(this);
        colorPicker.setColorIndex(this.mColorIndex);
        ViewGroup profileBackground = (ViewGroup) v.findViewById(R.id.user_image_container);
        profileBackground.setBackgroundColor(this.mBackgroundColor | ViewCompat.MEASURED_STATE_MASK);
        this.mProfileBackground = profileBackground;
        setActionBarColor(this.mBackgroundColor);
        TextView privacyControls = (TextView) v.findViewById(R.id.privacy_controls);
        privacyControls.setOnClickListener(this);
        TextView syncControls = (TextView) v.findViewById(R.id.sync);
        syncControls.setOnClickListener(this);
        this.mAvatarImageView = (ImageView) v.findViewById(R.id.user_image);
        this.mAvatarActionView = (ImageView) v.findViewById(R.id.user_image_action);
        this.mAvatarActionView.setVisibility(0);
        String avatarUrl = this.mAvatarUri == null ? "" : this.mAvatarUri.toString();
        this.mAvatarImageView.setOnClickListener(this);
        if (this.mThumbnail != null) {
            setImage(this.mThumbnail, false);
        } else if (TextUtils.isEmpty(avatarUrl) || Util.isDefaultAvatarUrl(avatarUrl)) {
            setImage(null, false);
        } else {
            int dimen = res.getDimensionPixelOffset(R.dimen.user_image_size_large);
            this.mAvatarKey = new ImageKey(avatarUrl, dimen, dimen, true);
            Bitmap bmp = this.mAppController.getPhotoBitmap(this.mAvatarKey);
            setImage(bmp, false);
        }
        EditText editTextName = (EditText) v.findViewById(R.id.name);
        editTextName.setText(this.mName);
        editTextName.addTextChangedListener(new SimpleTextWatcher() { // from class: co.vine.android.SettingsFragment.2
            @Override // co.vine.android.views.SimpleTextWatcher, android.text.TextWatcher
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SettingsFragment.this.mName = s.toString();
            }
        });
        EditText editTextDescription = (EditText) v.findViewById(R.id.description);
        editTextDescription.setText(this.mDescription);
        editTextDescription.setHint(R.string.settings_description);
        editTextDescription.addTextChangedListener(new SimpleTextWatcher() { // from class: co.vine.android.SettingsFragment.3
            @Override // co.vine.android.views.SimpleTextWatcher, android.text.TextWatcher
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SettingsFragment.this.mDescription = s.toString();
            }
        });
        EditText editTextLocation = (EditText) v.findViewById(R.id.location);
        editTextLocation.setText(this.mLocation);
        editTextLocation.setHint(R.string.settings_location);
        editTextLocation.addTextChangedListener(new SimpleTextWatcher() { // from class: co.vine.android.SettingsFragment.4
            @Override // co.vine.android.views.SimpleTextWatcher, android.text.TextWatcher
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SettingsFragment.this.mLocation = s.toString();
            }
        });
        TypingEditText editTextEmail = (TypingEditText) v.findViewById(R.id.settings_email);
        editTextEmail.setText(this.mEmail);
        editTextEmail.setInputType(32);
        editTextEmail.setTypingListener(this);
        this.mEditTextEmail = editTextEmail;
        editTextEmail.addTextChangedListener(new SimpleTextWatcher() { // from class: co.vine.android.SettingsFragment.5
            @Override // co.vine.android.views.SimpleTextWatcher, android.text.TextWatcher
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SettingsFragment.this.mEmail = s.toString();
                SettingsFragment.this.mIsEmailVerified = (SettingsFragment.this.mOriginalEmail == null || SettingsFragment.this.mOriginalPhone.equals(SettingsFragment.this.mEmail)) && SettingsFragment.this.mOriginalEmailVerified;
            }
        });
        TypingEditText editTextPhone = (TypingEditText) v.findViewById(R.id.settings_phone);
        editTextPhone.setText(this.mPhone);
        editTextPhone.setInputType(3);
        editTextPhone.addTextChangedListener(new SimpleTextWatcher() { // from class: co.vine.android.SettingsFragment.6
            @Override // co.vine.android.views.SimpleTextWatcher, android.text.TextWatcher
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SettingsFragment.this.mPhone = s.toString();
                SettingsFragment.this.mIsPhoneVerified = (SettingsFragment.this.mOriginalPhone == null || SettingsFragment.this.mOriginalPhone.equals(SettingsFragment.this.mPhone)) && SettingsFragment.this.mOriginalPhoneVerified;
            }
        });
        editTextPhone.setTypingListener(this);
        this.mEditTextPhone = editTextPhone;
        this.mEditionsSpinner = (Spinner) v.findViewById(R.id.edition);
        this.mEditionsSpinner.setFocusable(false);
        this.mEditionsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // from class: co.vine.android.SettingsFragment.7
            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SettingsFragment.this.mEdition = SettingsFragment.this.mSpinnerAdapter.getEditionCode(i);
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        v.findViewById(R.id.edition_container).setOnClickListener(this);
        v.findViewById(R.id.edition_label).setOnClickListener(this);
        this.mSpinnerAdapter = new EditionsSpinnerAdapter();
        this.mEditionsSpinner.setAdapter((SpinnerAdapter) this.mSpinnerAdapter);
        TextView password = (TextView) v.findViewById(R.id.settings_password);
        password.setOnClickListener(this);
        TextView content = (TextView) v.findViewById(R.id.content_controls);
        content.setOnClickListener(this);
        TextView solicitInput = (TextView) v.findViewById(R.id.tell_us_what_you_like);
        solicitInput.setOnClickListener(this);
        FragmentActivity activity = getActivity();
        if (activity != null && !ClientFlagsHelper.solicitorEnabled(activity)) {
            solicitInput.setVisibility(8);
            View divider = v.findViewById(R.id.tell_us_what_you_like_divider);
            divider.setVisibility(8);
        }
        this.mNotifications = (ViewGroup) v.findViewById(R.id.notification_settings);
        if (BuildUtil.isGoogle()) {
            this.mNotifications.setVisibility(0);
            this.mAlertIcon = (ImageView) this.mNotifications.findViewById(R.id.alert_icon);
            this.mErrorCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
            if (this.mErrorCode != 0) {
                this.mAlertIcon.setVisibility(0);
                this.mAlertIcon.setOnClickListener(this);
            } else {
                this.mAlertIcon.setVisibility(8);
            }
            this.mNotifications.setOnClickListener(this);
        } else {
            this.mNotifications.setVisibility(8);
        }
        TextView findFriends = (TextView) v.findViewById(R.id.find_friends);
        findFriends.setOnClickListener(this);
        TextView inviteViaText = (TextView) v.findViewById(R.id.invite_via_text);
        inviteViaText.setOnClickListener(this);
        TextView inviteViaEmail = (TextView) v.findViewById(R.id.invite_via_email);
        inviteViaEmail.setOnClickListener(this);
        RelativeLayout twitterConnect = (RelativeLayout) v.findViewById(R.id.twitter_connect);
        twitterConnect.setBackgroundResource(R.drawable.bg_selectable_item);
        TextView twitterConnectedTextView = (TextView) twitterConnect.findViewById(R.id.twitter_connect_value);
        AccountManager am = AccountManager.get(getActivity());
        Session session = this.mAppController.getActiveSession();
        Account acc = VineAccountHelper.getAccount(getActivity(), session.getUserId(), session.getUsername());
        if (acc != null && VineAccountHelper.getLoginType(am, acc).intValue() == 2) {
            twitterConnectedTextView.setText('@' + VineAccountHelper.getTwitterUsername(am, acc));
            this.mTwitterLogin = true;
        } else if (this.mTwitterConnected) {
            twitterConnectedTextView.setText("");
            this.mTwitterLogin = true;
        } else {
            twitterConnectedTextView.setText(R.string.settings_connect_to_twitter_summary);
            this.mTwitterLogin = false;
        }
        twitterConnect.setOnClickListener(this);
        RelativeLayout facebookConnect = (RelativeLayout) v.findViewById(R.id.facebook_connect);
        facebookConnect.setOnClickListener(this);
        this.mFacebookValue = (TextView) v.findViewById(R.id.facebook_value);
        invalidateFacebookSessionUI();
        RelativeLayout tumblrConnect = (RelativeLayout) v.findViewById(R.id.tumblr_connect);
        tumblrConnect.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.SettingsFragment.8
            @Override // android.view.View.OnClickListener
            public void onClick(View v2) {
                FragmentActivity activity2 = SettingsFragment.this.getActivity();
                if (TumblrHelper.isTumblrConnected(activity2)) {
                    PromptDialogSupportFragment dialog = PromptDialogSupportFragment.newInstance(12);
                    dialog.setTargetFragment(SettingsFragment.this, 0);
                    dialog.setTitle(R.string.settings_disconnect_dialog).setPositiveButton(R.string.yes).setNegativeButton(R.string.cancel);
                    dialog.show(activity2.getSupportFragmentManager());
                    return;
                }
                SettingsFragment.this.startActivityForResult(new Intent(activity2, (Class<?>) TumblrLoginActivity.class), 8);
            }
        });
        this.mTumblrValue = (TextView) v.findViewById(R.id.tumblr_value);
        updateTumblrConnectValue();
        RelativeLayout clearCache = (RelativeLayout) v.findViewById(R.id.clear_cache);
        clearCache.setOnClickListener(this);
        TextView clearCacheLabel = (TextView) v.findViewById(R.id.clear_cache_label);
        clearCacheLabel.setText(getString(R.string.settings_cache_size));
        this.mClearCacheValue = (TextView) v.findViewById(R.id.clear_cache_value);
        new GetCacheSizeAsyncTask().execute(new Void[0]);
        TextView help = (TextView) v.findViewById(R.id.settings_help);
        help.setOnClickListener(this);
        TextView vineRules = (TextView) v.findViewById(R.id.vine_rules);
        vineRules.setOnClickListener(this);
        TextView termsOfService = (TextView) v.findViewById(R.id.terms_of_service);
        termsOfService.setOnClickListener(this);
        TextView privacyPolicy = (TextView) v.findViewById(R.id.privacy_policy);
        privacyPolicy.setOnClickListener(this);
        TextView attribution = (TextView) v.findViewById(R.id.attribution);
        attribution.setOnClickListener(this);
        TextView deactivateAccount = (TextView) v.findViewById(R.id.deactivate_account);
        deactivateAccount.setOnClickListener(this);
        Button logout = (Button) v.findViewById(R.id.logout);
        logout.setOnClickListener(this);
        LinearLayout version = (LinearLayout) v.findViewById(R.id.version);
        TextView tv = (TextView) v.findViewById(R.id.version_number);
        try {
            PackageInfo pi = getActivity().getPackageManager().getPackageInfo("co.vine.android", 0);
            tv.setText(getString(R.string.settings_about_summary, pi.versionName));
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("SettingsFragment", "Error retrieving package info:", e);
        }
        version.setOnClickListener(this);
        if (BuildUtil.isI18nOn()) {
            TextView locale = (TextView) v.findViewById(R.id.locale);
            locale.setVisibility(0);
            locale.setOnClickListener(this);
        }
        TextView favoritePeople = (TextView) v.findViewById(R.id.favorite_people);
        favoritePeople.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.SettingsFragment.9
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                FavoritePeopleActivity.start(SettingsFragment.this.getActivity(), SettingsFragment.this.mBackgroundColor);
            }
        });
        return v;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void invalidateFacebookSessionUI() {
        this.mFacebookValue.setText(FacebookHelper.isFacebookConnected(getActivity()) ? R.string.settings_connected : R.string.settings_connect_to_twitter_summary);
    }

    @Override // android.widget.CompoundButton.OnCheckedChangeListener
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    }

    public void onBackPressed() {
        FragmentActivity activity = getActivity();
        if (isDirty() && activity != null) {
            PromptDialogSupportFragment p = PromptDialogSupportFragment.newInstance(3);
            p.setMessage(R.string.settings_unsaved_changes);
            p.setNeutralButton(R.string.settings_continue_editing);
            p.setPositiveButton(R.string.settings_discard);
            p.setTargetFragment(this, 0);
            p.show(activity.getSupportFragmentManager());
            return;
        }
        if (this.mProgress != null) {
            this.mProgress.dismiss();
        }
        if (activity != null) {
            activity.finish();
        }
    }

    @Override // android.support.v4.app.Fragment
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.save, menu);
        MenuItem menuItem = menu.findItem(R.id.done);
        menuItem.setEnabled(true);
        menu.findItem(R.id.done).setEnabled(true);
    }

    @Override // android.support.v4.app.LoaderManager.LoaderCallbacks
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(getActivity(), Vine.Editions.CONTENT_URI, VineDatabaseSQL.EditionsQuery.PROJECTION, null, null, null);
    }

    @Override // android.support.v4.app.LoaderManager.LoaderCallbacks
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        if (!this.mEditionsFetched) {
            this.mAppController.getEditions();
            this.mEditionsFetched = true;
        }
        this.mSpinnerAdapter.changeCursor(cursor);
        this.mSpinnerAdapter.setSelectionToUserEdition();
    }

    @Override // android.support.v4.app.LoaderManager.LoaderCallbacks
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
    }

    @Override // co.vine.android.widget.TypingEditText.TypingListener
    public void onTypingTimeout(View v) {
        int id = v.getId();
        if (id == R.id.email || id == R.id.settings_phone) {
            updateVerificationIcons();
        }
    }

    @Override // co.vine.android.widget.ColorPicker.ColorClickListener
    @TargetApi(11)
    public void onColorClick(int colorIndex) {
        int origColor;
        if (this.mColorIndex >= 0) {
            origColor = Settings.PROFILE_BACKGROUND_COLORS[this.mColorIndex];
        } else {
            origColor = Settings.DEFAULT_PROFILE_COLOR;
        }
        this.mColorIndex = colorIndex;
        this.mBackgroundColor = Settings.PROFILE_BACKGROUND_COLORS[this.mColorIndex];
        final ValueAnimator animator = ValueAnimator.ofObject(new ArgbEvaluator(), Integer.valueOf(origColor | ViewCompat.MEASURED_STATE_MASK), Integer.valueOf(this.mBackgroundColor | ViewCompat.MEASURED_STATE_MASK));
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: co.vine.android.SettingsFragment.10
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator animation) {
                SettingsFragment.this.mProfileBackground.setBackgroundColor(((Integer) animator.getAnimatedValue()).intValue());
                SettingsFragment.this.setActionBarColor(((Integer) animator.getAnimatedValue()).intValue());
            }
        });
        animator.setDuration(100L);
        animator.start();
        Intent intent = new Intent("co.vine.android.profileColor");
        getActivity().sendBroadcast(intent, CrossConstants.BROADCAST_PERMISSION);
    }

    class SettingsListener extends AppSessionListener {
        SettingsListener() {
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onPhotoImageLoaded(HashMap<ImageKey, UrlImage> images) {
            UrlImage urlImage = images.get(SettingsFragment.this.mAvatarKey);
            if (urlImage != null && urlImage.isValid()) {
                SettingsFragment.this.mAvatarImageView.setImageDrawable(new RecyclableBitmapDrawable(SettingsFragment.this.mAvatarImageView.getResources(), urlImage.bitmap));
            }
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onSendFacebookTokenComplete(String reqId, int statusCode, String reasonPhrase) {
            if (SettingsFragment.this.mProgress != null) {
                SettingsFragment.this.mProgress.dismiss();
            }
            SettingsFragment.this.invalidateFacebookSessionUI();
            if (statusCode != 200) {
                Util.showCenteredToast(SettingsFragment.this.getActivity(), R.string.error_facebook_send_token);
            }
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onUpdateProfileComplete(String reqId, int statusCode, String reasonPhrase, String avatarUrl) {
            if (SettingsFragment.this.mProgress != null) {
                SettingsFragment.this.mProgress.dismiss();
            }
            FragmentActivity activity = SettingsFragment.this.getActivity();
            if (statusCode == 200) {
                SharedPreferences.Editor editor = SettingsFragment.this.mEditor;
                if (!TextUtils.isEmpty(avatarUrl)) {
                    SettingsFragment.this.mEditor.putString("settings_profile_avatar_url", avatarUrl);
                }
                editor.putString("settings_profile_name", SettingsFragment.this.mName);
                editor.putString("settings_profile_description", SettingsFragment.this.mDescription);
                editor.putString("settings_profile_location", SettingsFragment.this.mLocation);
                editor.putString("settings_profile_email", SettingsFragment.this.mEmail);
                editor.putString("settings_profile_phone", SettingsFragment.this.mPhone);
                if (SettingsFragment.this.mColorIndex < 0 || SettingsFragment.this.mColorIndex >= Settings.PROFILE_BACKGROUND_COLORS.length) {
                    SettingsFragment.this.mColorIndex = 0;
                }
                editor.putInt("profile_background", Settings.PROFILE_BACKGROUND_COLORS[SettingsFragment.this.mColorIndex]);
                editor.putInt("color_index", SettingsFragment.this.mColorIndex);
                editor.apply();
                if (activity != null) {
                    activity.sendBroadcast(new Intent("co.vine.android.profileColor"), CrossConstants.BROADCAST_PERMISSION);
                    activity.finish();
                    return;
                }
                return;
            }
            if (statusCode == 401) {
                Util.showCenteredToast(activity, R.string.update_profile_error_retry);
            } else if (TextUtils.isEmpty(reasonPhrase)) {
                Util.showCenteredToast(activity, R.string.update_profile_error);
            } else {
                Util.showCenteredToast(activity, reasonPhrase);
            }
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onUpdateEditionComplete(String reqId, int statusCode, String reasonPhrase, String edition) {
            if (SettingsFragment.this.mProgress != null) {
                SettingsFragment.this.mProgress.dismiss();
            }
            if (statusCode == 200) {
                SettingsFragment.this.mEdition = edition;
                SettingsFragment.this.mOriginalEdition = edition;
            }
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onClearCacheComplete(String reqId, int statusCode, String reasonPhrase) {
            if (SettingsFragment.this.mProgress != null) {
                SettingsFragment.this.mProgress.dismiss();
            }
            try {
                SettingsFragment.this.mClearCacheValue.setText(Util.formatFileSize(SettingsFragment.this.getResources(), Util.getCacheSize(SettingsFragment.this.getActivity())));
            } catch (VineLoggingException e) {
                CrashUtil.logException(e);
            }
            SettingsFragment.this.getActivity().setResult(1);
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onConnectTwitterComplete(String reqId, int statusCode, String reasonPhrase, String username, String token, String secret, long userId) throws Resources.NotFoundException {
            if (SettingsFragment.this.mProgress != null) {
                SettingsFragment.this.mProgress.dismiss();
            }
            TextView valueTextView = (TextView) SettingsFragment.this.getView().findViewById(R.id.twitter_connect_value);
            TextView label = (TextView) SettingsFragment.this.getView().findViewById(R.id.twitter_connect_label);
            if (statusCode == 200 && userId > 0 && !TextUtils.isEmpty(token) && !TextUtils.isEmpty(secret)) {
                label.setText(R.string.settings_connected_to_twitter);
                valueTextView.setText('@' + username);
                Session session = SettingsFragment.this.mAppController.getActiveSession();
                VineAccountHelper.saveTwitterInfo(SettingsFragment.this.getActivity(), session.getUserId(), session.getUsername(), username, token, secret, userId);
                SettingsFragment.this.mTwitterLogin = true;
                SettingsFragment.this.mPrefs.edit().putBoolean("settings_twitter_connected", true).apply();
                return;
            }
            label.setText(R.string.settings_connect_to_twitter);
            valueTextView.setText(R.string.settings_connect_to_twitter_summary);
            SettingsFragment.this.mTwitterLogin = false;
            if (TextUtils.isEmpty(reasonPhrase)) {
                reasonPhrase = SettingsFragment.this.getResources().getString(R.string.settings_error_connect_to_twitter);
            }
            Util.showCenteredToast(SettingsFragment.this.getActivity(), reasonPhrase);
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onDisconnectTwitterComplete(String reqId, int statusCode, String reasonPhrase) throws Resources.NotFoundException {
            TextView valueTextView = (TextView) SettingsFragment.this.getView().findViewById(R.id.twitter_connect_value);
            TextView label = (TextView) SettingsFragment.this.getView().findViewById(R.id.twitter_connect_label);
            if (statusCode == 200) {
                label.setText(R.string.settings_connect_to_twitter);
                valueTextView.setText(R.string.settings_connect_to_twitter_summary);
                Session session = SettingsFragment.this.mAppController.getActiveSession();
                VineAccountHelper.removeTwitterInfo(SettingsFragment.this.getActivity(), session.getUserId(), session.getUsername());
                SettingsFragment.this.mTwitterLogin = false;
                SettingsFragment.this.mEditor.putBoolean("settings_twitter_connected", false).apply();
                return;
            }
            if (TextUtils.isEmpty(reasonPhrase)) {
                reasonPhrase = SettingsFragment.this.getResources().getString(R.string.settings_error_disconnect_from_twitter);
            }
            Util.showCenteredToast(SettingsFragment.this.getActivity(), reasonPhrase);
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onDeactivateAccountComplete(String reqId, int statusCode, String reasonPhrase, boolean success) {
            int messageId;
            if (SettingsFragment.this.mProgress != null) {
                SettingsFragment.this.mProgress.dismiss();
            }
            if (success) {
                Components.authComponent().logout(SettingsFragment.this.mAppController);
                messageId = R.string.settings_account_deactivation_success;
                SLog.d("User account successfully deactivated");
            } else {
                messageId = R.string.settings_account_deactivation_fail;
                SLog.d("User account deactivation failed");
            }
            Util.showCenteredToast(SettingsFragment.this.getActivity(), messageId);
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onGetUsersMeComplete(String reqId, int statusCode, String reasonPhrase, long sessionOwnerId, VineUser meUser, UrlCachePolicy policy) {
            if (meUser != null) {
                SettingsFragment.this.mIsEmailVerified = meUser.isEmailVerified();
                SettingsFragment.this.mIsPhoneVerified = meUser.isPhoneVerified();
                SettingsFragment.this.updateVerificationIcons();
            }
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onGetEditionsComplete(int statusCode, VineEditions editions) {
            if (statusCode == 200) {
                SettingsFragment.this.mEditionsFetched = true;
            }
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onRequestEmailVerificationComplete(String reqId, int statusCode, String reasonPhrase, String email) {
            if (statusCode == 200) {
                PromptDialogSupportFragment p = PromptDialogSupportFragment.newInstance(10);
                p.setTargetFragment(SettingsFragment.this, 0);
                p.setNeutralButton(android.R.string.ok).setMessage(R.string.confirm_email_sent).show(SettingsFragment.this.getActivity().getSupportFragmentManager());
            } else {
                String reason = reasonPhrase != null ? reasonPhrase : SettingsFragment.this.getString(R.string.generic_error);
                Util.showCenteredToast(SettingsFragment.this.getActivity(), reason);
            }
        }
    }

    @Override // co.vine.android.SetThumbnailTask.SetThumbnailListener
    public void setThumbnailImage(Bitmap bitmap) {
        setImage(bitmap, true);
    }

    private void setImage(Bitmap bmp, boolean edited) {
        this.mThumbnail = bmp;
        if (bmp == null) {
            this.mAvatarImageView.setImageResource(R.drawable.avatar_large);
            this.mAvatarActionView.setImageResource(R.drawable.avatar_add);
        } else {
            this.mAvatarActionView.setImageResource(edited ? R.drawable.avatar_check : R.drawable.avatar_edit);
            this.mAvatarImageView.setImageDrawable(new RecyclableBitmapDrawable(this.mAvatarImageView.getResources(), bmp));
        }
    }

    @Override // android.support.v4.app.Fragment
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            String action = args.getString("action");
            if (HomeTabActivity.ACTION_VERIFICATION_COMPLETE.equals(action)) {
                final ScrollView scrollView = this.mScrollView;
                final View verified = scrollView.findViewById(R.id.settings_phone_label);
                scrollView.post(new Runnable() { // from class: co.vine.android.SettingsFragment.11
                    @Override // java.lang.Runnable
                    public void run() {
                        scrollView.smoothScrollTo(0, verified.getBottom());
                    }
                });
            }
        }
        this.mInviteSmsIntent = new Intent("android.intent.action.VIEW");
        this.mInviteSmsIntent.setType("vnd.android-dir/mms-sms");
        this.mInviteSmsIntent.putExtra("sms_body", ShareUtils.getSmsMessage(getActivity()));
        if (this.mInviteSmsIntent.resolveActivity(getActivity().getPackageManager()) == null) {
            this.mScrollView.findViewById(R.id.invite_via_text).setVisibility(8);
            this.mScrollView.findViewById(R.id.invite_via_text_divider).setVisibility(8);
        }
    }

    @Override // android.support.v4.app.Fragment
    public void onActivityResult(int requestCode, int resultCode, Intent extras) throws PackageManager.NameNotFoundException {
        Uri uri;
        super.onActivityResult(requestCode, resultCode, extras);
        switch (requestCode) {
            case 1:
                if (resultCode == -1) {
                    String username = extras.getStringExtra("screen_name");
                    String token = extras.getStringExtra("tk");
                    String secret = extras.getStringExtra("ts");
                    long userId = extras.getLongExtra("user_id", 0L);
                    ProgressDialog d = new ProgressDialog(getActivity(), R.style.ProgressDialogTheme);
                    d.setMessage(getString(R.string.sign_up_authorizing));
                    d.setProgress(0);
                    d.show();
                    this.mProgress = d;
                    this.mAppController.connectTwitter(this.mAppController.getActiveSession(), username, token, secret, userId);
                    break;
                } else {
                    Util.showCenteredToast(getActivity(), R.string.error_twitter_sdk);
                    break;
                }
            case 2:
                if (resultCode == -1) {
                    String username2 = extras.getStringExtra("screen_name");
                    String token2 = extras.getStringExtra("token");
                    String secret2 = extras.getStringExtra("secret");
                    long userId2 = extras.getLongExtra("user_id", 0L);
                    ProgressDialog d2 = new ProgressDialog(getActivity(), R.style.ProgressDialogTheme);
                    d2.setMessage(getString(R.string.sign_up_authorizing));
                    d2.setProgress(0);
                    d2.show();
                    this.mProgress = d2;
                    this.mAppController.connectTwitter(this.mAppController.getActiveSession(), username2, token2, secret2, userId2);
                    break;
                } else if (resultCode != 0) {
                    Util.showCenteredToast(getActivity(), R.string.error_xauth);
                    break;
                }
                break;
            case 3:
            case 5:
                if (resultCode == -1) {
                    if (requestCode == 3) {
                        uri = this.mAvatarUri;
                    } else {
                        uri = extras.getData();
                    }
                    Intent intent = new Intent(getActivity(), (Class<?>) EditProfileCropActivity.class).putExtra("uri", uri);
                    startActivityForResult(intent, 4);
                    break;
                }
                break;
            case 4:
                if (resultCode == -1 && extras != null) {
                    Uri croppedUri = (Uri) extras.getParcelableExtra("uri");
                    if (croppedUri != null) {
                        ImageUtils.deleteTempPic(this.mAvatarUri);
                        this.mAvatarUri = croppedUri;
                        new SetThumbnailTask(this).execute(croppedUri);
                    }
                    this.mPhotoChanged = true;
                    break;
                }
                break;
            case 6:
                Activity activity = getActivity();
                int errorCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
                if (errorCode == 0) {
                    if (this.mNotifications != null) {
                        this.mNotifications.setOnClickListener(this);
                    }
                    if (this.mAlertIcon != null) {
                        this.mAlertIcon.setVisibility(8);
                    }
                    getActivity().startService(GCMRegistrationService.getRegisterIntent(activity, this.mAppController.getActiveId()));
                    break;
                } else {
                    Util.showCenteredToast(getActivity(), R.string.toast_gms_resolution_failed);
                    break;
                }
            case 7:
                switch (resultCode) {
                    case 1527:
                        successPhoneVerification();
                        break;
                    case 1528:
                        failurePhoneVerification();
                        break;
                }
            case 8:
                updateTumblrConnectValue();
                break;
            default:
                SLog.d("Facebook auth came back: {}", Integer.valueOf(requestCode));
                FacebookHelper.onActivityResult(getActivity(), requestCode, resultCode, extras);
                invalidateFacebookSessionUI();
                break;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void failurePhoneVerification() {
        this.mIsPhoneVerified = false;
        this.mPrefs.edit().putBoolean("profile_phone_verified", false).apply();
        updateVerificationIcons();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void successPhoneVerification() {
        this.mPrefs.edit().putBoolean("profile_phone_verified", true).apply();
        this.mIsPhoneVerified = true;
        updateVerificationIcons();
    }

    @Override // co.vine.android.ImagePicker.Listener
    public void setAvatarUrl(Uri url) {
        this.mAvatarUri = url;
    }

    @Override // android.support.v4.app.Fragment
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.done) {
            if (isDirty()) {
                updateProfile(this.mAvatarUri);
            } else {
                if (this.mProgress != null) {
                    this.mProgress.dismiss();
                }
                if (getActivity() != null) {
                    getActivity().finish();
                }
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override // co.vine.android.widgets.PromptDialogFragment.OnDialogDoneListener
    public void onDialogDone(DialogInterface dialog, int id, int which) {
        switch (id) {
            case 1:
                switch (which) {
                    case opencv_core.CV_StsInternal /* -3 */:
                        this.mImagePicker.chooseImage(5);
                        break;
                    case -1:
                        this.mImagePicker.captureImage(3);
                        break;
                }
            case 2:
                switch (which) {
                    case opencv_core.CV_StsInternal /* -3 */:
                        this.mImagePicker.captureImage(3);
                        break;
                    case -2:
                        this.mImagePicker.chooseImage(5);
                        break;
                    case -1:
                        setThumbnailImage(null);
                        this.mPhotoChanged = true;
                        this.mAvatarUri = Uri.parse("");
                        break;
                }
            case 3:
                switch (which) {
                    case -1:
                        resetPrefs();
                        FragmentActivity activity = getActivity();
                        if (activity != null) {
                            activity.finish();
                            break;
                        }
                        break;
                }
            case 4:
                switch (which) {
                    case -1:
                        FlurryUtils.trackLogout();
                        closeFbSessionAndInvalidate();
                        ProgressDialog d = new ProgressDialog(getActivity(), R.style.ProgressDialogTheme);
                        d.setMessage(getString(R.string.logging_out));
                        d.setProgress(0);
                        d.show();
                        this.mProgress = d;
                        Components.authComponent().logout(this.mAppController);
                        break;
                }
            case 5:
                switch (which) {
                    case -1:
                        this.mAppController.disconnectTwitter(this.mAppController.getActiveSession());
                        break;
                }
            case 6:
                switch (which) {
                    case -1:
                        ProgressDialog d2 = new ProgressDialog(getActivity(), R.style.ProgressDialogTheme);
                        d2.setMessage(getString(R.string.settings_clearing_cache));
                        d2.setProgress(0);
                        d2.show();
                        this.mProgress = d2;
                        this.mAppController.clearDbCache(true);
                        break;
                }
            case 7:
                switch (which) {
                    case -1:
                        ProgressDialog d3 = new ProgressDialog(getActivity(), R.style.ProgressDialogTheme);
                        d3.setMessage(getString(R.string.settings_deactivating_account));
                        d3.setProgress(0);
                        d3.show();
                        this.mProgress = d3;
                        this.mAppController.deactivateAccount();
                        break;
                }
            case 8:
                switch (which) {
                    case -1:
                        CharSequence email = this.mEditTextEmail.getText();
                        if (!TextUtils.isEmpty(email)) {
                            this.mAppController.requestEmailVerification(this.mAppController.getActiveSession(), email.toString(), this.mAppController.getActiveId());
                            break;
                        }
                        break;
                }
            case 9:
                switch (which) {
                    case -1:
                        CharSequence phone = this.mEditTextPhone.getText();
                        if (!TextUtils.isEmpty(phone)) {
                            this.mAppController.requestPhoneVerification(this.mAppController.getActiveSession(), phone.toString(), this.mAppController.getActiveId());
                            break;
                        }
                        break;
                }
            case 11:
                switch (which) {
                    case -1:
                        closeFbSessionAndInvalidate();
                        break;
                }
            case 12:
                switch (which) {
                    case -1:
                        VineAccountHelper.removeTumblrTokens(getActivity());
                        updateTumblrConnectValue();
                        break;
                }
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) throws PackageManager.NameNotFoundException {
        int id = view.getId();
        if (id == R.id.facebook_connect) {
            if (FacebookHelper.isFacebookConnected(getActivity())) {
                PromptDialogSupportFragment p = PromptDialogSupportFragment.newInstance(11);
                p.setTargetFragment(this, 0);
                p.setTitle(R.string.settings_disconnect_dialog).setMessage(R.string.settings_disconnect_facebook).setPositiveButton(R.string.yes).setNegativeButton(R.string.cancel);
                try {
                    p.show(getActivity().getSupportFragmentManager());
                    return;
                } catch (Exception e) {
                    CrashUtil.logException(e);
                    return;
                }
            }
            FacebookHelper.connectToFacebookProfile(getActivity());
            return;
        }
        if (id == R.id.user_image) {
            if (this.mAvatarUri != null && !Util.isDefaultAvatarUrl(this.mAvatarUri.toString())) {
                PromptDialogSupportFragment p2 = PromptDialogSupportFragment.newInstance(2);
                p2.setTargetFragment(this, 0);
                p2.setTitle(R.string.sign_up_profile_photo).setPositiveButton(R.string.remove_photo).setNeutralButton(R.string.take_photo).setNegativeButton(R.string.choose_photo).setButtonPlacementVertical(true);
                try {
                    p2.show(getActivity().getSupportFragmentManager());
                    return;
                } catch (Exception e2) {
                    CrashUtil.logException(e2);
                    return;
                }
            }
            PromptDialogSupportFragment p3 = PromptDialogSupportFragment.newInstance(1);
            p3.setTargetFragment(this, 0);
            p3.setTitle(R.string.sign_up_profile_photo).setPositiveButton(R.string.take_photo).setNeutralButton(R.string.choose_photo);
            try {
                p3.show(getActivity().getSupportFragmentManager());
                return;
            } catch (Exception e3) {
                CrashUtil.logException(e3);
                return;
            }
        }
        if (id == R.id.twitter_connect) {
            if (this.mTwitterLogin) {
                PromptDialogSupportFragment p4 = PromptDialogSupportFragment.newInstance(5);
                p4.setMessage(R.string.settings_disconnect_twitter);
                p4.setPositiveButton(R.string.settings_disconnect_dialog);
                p4.setNegativeButton(R.string.cancel);
                p4.setTargetFragment(this, 0);
                try {
                    p4.show(getActivity().getSupportFragmentManager());
                    return;
                } catch (Exception e4) {
                    CrashUtil.logException(e4);
                    return;
                }
            }
            Twitter twitter = this.mAppController.getTwitter();
            Activity activity = getActivity();
            AppController.startTwitterAuthWithFinish(twitter, activity);
            return;
        }
        if (id == R.id.logout) {
            PromptDialogSupportFragment p5 = PromptDialogSupportFragment.newInstance(4);
            p5.setMessage(R.string.logout_confirm);
            p5.setPositiveButton(R.string.logout);
            p5.setNegativeButton(R.string.cancel);
            p5.setTargetFragment(this, 0);
            try {
                p5.show(getActivity().getSupportFragmentManager());
                return;
            } catch (Exception e5) {
                CrashUtil.logException(e5);
                return;
            }
        }
        if (id == R.id.settings_password) {
            Intent i = new Intent(getActivity(), (Class<?>) ResetPasswordActivity.class);
            if (!TextUtils.isEmpty(this.mEmail)) {
                i.putExtra("email", this.mEmail);
                i.putExtra("color", (-16777216) | this.mBackgroundColor);
            }
            startActivity(i);
            return;
        }
        if (id == R.id.content_controls) {
            FlurryUtils.trackContentControls();
            Intent intent = new Intent(getActivity(), (Class<?>) ContentControlsActivity.class);
            intent.putExtra("color", this.mBackgroundColor);
            startActivity(intent);
            return;
        }
        if (id == R.id.tell_us_what_you_like) {
            SolicitorActivity.start(getActivity());
            return;
        }
        if (id == R.id.privacy_controls) {
            Intent intent2 = new Intent(getActivity(), (Class<?>) PrivacyControlsActivity.class);
            intent2.putExtra("color", this.mBackgroundColor);
            startActivity(intent2);
            return;
        }
        if (id == R.id.sync) {
            Intent intent3 = new Intent(getActivity(), (Class<?>) SyncControlsActivity.class);
            intent3.putExtra("color", this.mBackgroundColor);
            startActivity(intent3);
            return;
        }
        if (id == R.id.find_friends) {
            FlurryUtils.trackVisitFindFriends("Settings");
            startActivity(new Intent(getActivity(), (Class<?>) FindFriendsActivity.class));
            return;
        }
        if (id == R.id.invite_via_email) {
            FlurryUtils.trackInvite("email", "Settings");
            Intent intent4 = new Intent("android.intent.action.SENDTO", Uri.fromParts("mailto", "", null));
            intent4.putExtra("android.intent.extra.SUBJECT", getString(R.string.find_friends_invite_email_subject));
            intent4.putExtra("android.intent.extra.TEXT", ShareUtils.getEmailMessage(getActivity()));
            startActivity(Intent.createChooser(intent4, getString(R.string.find_friends_email_choose_title)));
            return;
        }
        if (id == R.id.invite_via_text) {
            FlurryUtils.trackInvite("sms", "Settings");
            startActivity(Intent.createChooser(this.mInviteSmsIntent, getString(R.string.send)));
            return;
        }
        if (id == R.id.clear_cache) {
            PromptDialogSupportFragment p6 = PromptDialogSupportFragment.newInstance(6);
            p6.setMessage(R.string.settings_clear_cache_confirm_dialog);
            p6.setNegativeButton(R.string.cancel);
            p6.setPositiveButton(R.string.settings_clear_cache_confirm_button);
            p6.setTargetFragment(this, 0);
            p6.show(getActivity().getSupportFragmentManager());
            return;
        }
        if (id == R.id.privacy_policy) {
            FlurryUtils.trackPrivacyPolicy();
            Intent i2 = new Intent(getActivity(), (Class<?>) WebViewActivity.class);
            i2.putExtra("type", 1);
            startActivity(i2);
            return;
        }
        if (id == R.id.settings_help) {
            Intent i3 = new Intent(getActivity(), (Class<?>) WebViewActivity.class);
            i3.putExtra("type", 6);
            startActivity(i3);
            return;
        }
        if (id == R.id.vine_rules) {
            Intent i4 = new Intent(getActivity(), (Class<?>) WebViewActivity.class);
            i4.putExtra("type", 7);
            startActivity(i4);
            return;
        }
        if (id == R.id.terms_of_service) {
            FlurryUtils.trackTos();
            Intent i5 = new Intent(getActivity(), (Class<?>) WebViewActivity.class);
            i5.putExtra("type", 2);
            startActivity(i5);
            return;
        }
        if (id == R.id.attribution) {
            FlurryUtils.trackAttribution();
            Intent i6 = new Intent(getActivity(), (Class<?>) WebViewActivity.class);
            i6.putExtra("type", 5);
            startActivity(i6);
            return;
        }
        if (id == R.id.deactivate_account) {
            FlurryUtils.trackDeactivateAccount();
            PromptDialogSupportFragment p7 = PromptDialogSupportFragment.newInstance(7);
            p7.setMessage(R.string.settings_deactivate_account_dialog);
            p7.setTitle(R.string.settings_deactivate_account_title);
            p7.setNegativeButton(R.string.cancel);
            p7.setPositiveButton(R.string.settings_deactivate_account_confirm);
            p7.setTargetFragment(this, 0);
            try {
                p7.show(getActivity().getSupportFragmentManager());
                return;
            } catch (Exception e6) {
                CrashUtil.logException(e6);
                return;
            }
        }
        if (id == R.id.version) {
            this.mVersionTapCount++;
            if (this.mVersionTapCount >= 6) {
                this.mVersionTapCount = 0;
                startActivity(new Intent(getActivity(), (Class<?>) DebugHomeActivity.class));
                return;
            } else {
                if (this.mVersionTapCount == 3) {
                    try {
                        PackageInfo pi = getActivity().getPackageManager().getPackageInfo("co.vine.android", 0);
                        Util.showShortCenteredToast(getActivity(), "Version Code: " + pi.versionCode);
                        return;
                    } catch (Exception e7) {
                        SLog.e("Failed to show version code.", (Throwable) e7);
                        return;
                    }
                }
                return;
            }
        }
        if (id == R.id.locale) {
            LocaleDialog ldf = LocaleDialog.newInstance();
            ldf.show(getActivity().getSupportFragmentManager(), "locale");
            return;
        }
        if (id == R.id.edition_container || id == R.id.edition_label) {
            this.mEditionsSpinner.performClick();
            return;
        }
        if (id == R.id.alert_icon || id == R.id.notification_settings) {
            if (this.mErrorCode != 0) {
                Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(this.mErrorCode, getActivity(), 6);
                errorDialog.show();
                return;
            } else {
                FlurryUtils.trackNotificationSettings();
                Intent intent5 = new Intent(getActivity(), (Class<?>) NotificationSettingsActivity.class);
                intent5.putExtra("color", this.mBackgroundColor);
                startActivity(intent5);
                return;
            }
        }
        if (id == R.id.email_verified) {
            if (!this.mIsEmailVerified) {
                PromptDialogSupportFragment p8 = PromptDialogSupportFragment.newInstance(8);
                p8.setTargetFragment(this, 0);
                p8.setPositiveButton(R.string.confirm_email_yes).setMessage(getString(R.string.confirm_email_prompt, this.mEmail)).setNeutralButton(android.R.string.cancel);
                try {
                    p8.show(getActivity().getSupportFragmentManager());
                    return;
                } catch (Exception e8) {
                    CrashUtil.logException(e8);
                    return;
                }
            }
            return;
        }
        if (id == R.id.phone_verified && !this.mIsPhoneVerified) {
            PhoneConfirmationUtil.confirmPhoneNumber(getActivity(), new AuthCallback() { // from class: co.vine.android.SettingsFragment.12
                @Override // com.digits.sdk.android.AuthCallback
                public void success(DigitsSession digitsSession, String s) {
                    SettingsFragment.this.checkDigitsSuccess();
                }

                @Override // com.digits.sdk.android.AuthCallback
                public void failure(DigitsException e9) {
                    SettingsFragment.this.failurePhoneVerification();
                }
            }, this.mPhone, 7);
        }
    }

    private boolean validate() {
        FragmentActivity activity = getActivity();
        if (this.mName.isEmpty()) {
            if (activity == null) {
                return false;
            }
            Util.showCenteredToast(activity, R.string.name_length_toast);
            return false;
        }
        if (this.mLocation.length() > 32) {
            if (activity == null) {
                return false;
            }
            Util.showCenteredToast(activity, R.string.API_ERROR_LOCATION_INVALID_LENGTH);
            return false;
        }
        if (this.mDescription.length() > 140) {
            if (activity == null) {
                return false;
            }
            Util.showCenteredToast(activity, R.string.API_ERROR_DESCRIPTION_INVALID_LENGTH);
            return false;
        }
        if (activity != null) {
            ProgressDialog progressDialog = new ProgressDialog(activity, R.style.ProgressDialogTheme);
            progressDialog.setMessage(getString(R.string.settings_updating));
            progressDialog.show();
            this.mProgress = progressDialog;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateVerificationIcons() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            Resources res = activity.getResources();
            if (this.mEditTextEmail != null && TextUtils.isEmpty(this.mEditTextEmail.getText().toString())) {
                this.mEmailVerified.setVisibility(8);
            } else if (res != null) {
                this.mEmailVerified.setVisibility(0);
                if (this.mIsEmailVerified) {
                    this.mEmailVerified.setImageDrawable(res.getDrawable(R.drawable.ic_settings_check));
                } else {
                    this.mEmailVerified.setImageDrawable(res.getDrawable(R.drawable.ic_settings_alert));
                }
            }
            if (this.mEditTextPhone != null && TextUtils.isEmpty(this.mEditTextPhone.getText().toString())) {
                this.mPhoneVerified.setVisibility(8);
                return;
            }
            this.mPhoneVerified.setVisibility(0);
            if (res != null) {
                if (this.mIsPhoneVerified) {
                    this.mPhoneVerified.setImageDrawable(res.getDrawable(R.drawable.ic_settings_check));
                } else {
                    this.mPhoneVerified.setImageDrawable(res.getDrawable(R.drawable.ic_settings_alert));
                }
            }
        }
    }

    private void updateProfile(Uri avatarUri) {
        if (validate()) {
            if (!this.mOriginalName.equals(this.mName)) {
                FlurryUtils.trackChangedName();
            }
            if (!this.mOriginalDescription.equals(this.mDescription)) {
                FlurryUtils.trackChangedDescription();
            }
            if (!this.mOriginalLocation.equals(this.mLocation)) {
                FlurryUtils.trackChangedLocation();
            }
            if (!this.mOriginalEmail.equals(this.mEmail)) {
                FlurryUtils.trackChangedEmail();
            }
            if (!this.mOriginalEdition.equals(this.mEdition)) {
                FlurryUtils.trackChangedEdition();
            }
            this.mAppController.updateProfile(this.mAppController.getActiveSession(), this.mName, this.mDescription, this.mLocation, this.mEmail, this.mPhone, this.mPhotoChanged ? avatarUri : null, this.mBackgroundColor);
            this.mAppController.updateEdition(this.mAppController.getActiveSession(), this.mEdition);
        }
    }

    class EditionsSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {
        private Cursor mCursor;

        EditionsSpinnerAdapter() {
        }

        @Override // android.widget.Adapter
        public int getCount() {
            if (this.mCursor != null) {
                return this.mCursor.getCount();
            }
            return 0;
        }

        public void changeCursor(Cursor c) {
            this.mCursor = c;
            notifyDataSetChanged();
        }

        @Override // android.widget.Adapter
        public Object getItem(int i) {
            return null;
        }

        @Override // android.widget.Adapter
        public long getItemId(int i) {
            return i;
        }

        @Override // android.widget.Adapter
        public View getView(int i, View view, ViewGroup viewGroup) {
            View res;
            if (this.mCursor == null || i >= this.mCursor.getCount()) {
                return view;
            }
            if (view != null) {
                res = view;
            } else {
                res = LayoutInflater.from(SettingsFragment.this.getActivity()).inflate(R.layout.settings_spinner_row_item, (ViewGroup) null);
            }
            TextView t = (TextView) res.findViewById(R.id.title);
            t.setText(getEditionName(i));
            t.setSingleLine(true);
            t.setEllipsize(TextUtils.TruncateAt.END);
            return res;
        }

        @Override // android.widget.BaseAdapter, android.widget.SpinnerAdapter
        public View getDropDownView(int i, View view, ViewGroup viewGroup) {
            View res;
            if (this.mCursor == null || i >= this.mCursor.getCount()) {
                return view;
            }
            if (view != null) {
                res = view;
            } else {
                res = LayoutInflater.from(SettingsFragment.this.getActivity()).inflate(R.layout.settings_spinner_row_item, (ViewGroup) null);
            }
            TextView t = (TextView) res.findViewById(R.id.title);
            t.setText(getEditionName(i));
            t.setSingleLine(false);
            res.invalidate();
            return res;
        }

        public void setSelectionToUserEdition() {
            if (this.mCursor != null) {
                this.mCursor.moveToPosition(-1);
                while (this.mCursor.moveToNext()) {
                    String editionCode = this.mCursor.getString(1);
                    if (editionCode.equals(SettingsFragment.this.mEdition)) {
                        SettingsFragment.this.mEditionsSpinner.setSelection(this.mCursor.getPosition());
                        return;
                    }
                }
            }
        }

        public String getEditionName(int index) {
            if (this.mCursor == null || index >= this.mCursor.getCount()) {
                return "";
            }
            this.mCursor.moveToPosition(index);
            return this.mCursor.getString(2);
        }

        public String getEditionCode(int index) {
            if (this.mCursor == null || index >= this.mCursor.getCount()) {
                return "";
            }
            this.mCursor.moveToPosition(index);
            return this.mCursor.getString(1);
        }

        public Object getCursor() {
            return this.mCursor;
        }
    }

    private void closeFbSessionAndInvalidate() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            FacebookHelper.clearFacebookToken(activity);
            invalidateFacebookSessionUI();
        }
    }

    private void updateTumblrConnectValue() {
        this.mTumblrValue.setText(TumblrHelper.isTumblrConnected(getActivity()) ? R.string.settings_connected : R.string.settings_connect_to_twitter_summary);
    }

    @Override // co.vine.android.BaseFragment
    public AppNavigation.Views getAppNavigationView() {
        return AppNavigation.Views.SETTINGS;
    }
}
