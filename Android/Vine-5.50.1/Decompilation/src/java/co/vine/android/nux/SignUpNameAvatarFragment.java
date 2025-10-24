package co.vine.android.nux;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.ColorFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.StyleSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import co.vine.android.BaseControllerFragment;
import co.vine.android.EditProfileCropActivity;
import co.vine.android.ImagePicker;
import co.vine.android.R;
import co.vine.android.SetThumbnailTask;
import co.vine.android.api.TwitterUser;
import co.vine.android.api.VineLogin;
import co.vine.android.cache.image.ImageKey;
import co.vine.android.cache.image.UrlImage;
import co.vine.android.client.AppController;
import co.vine.android.client.AppSessionListener;
import co.vine.android.drawable.RecyclableBitmapDrawable;
import co.vine.android.scribe.AppNavigationProviderSingleton;
import co.vine.android.scribe.model.AppNavigation;
import co.vine.android.util.AuthenticationUtils;
import co.vine.android.util.ContactsHelper;
import co.vine.android.util.CrashUtil;
import co.vine.android.util.ImageUtils;
import co.vine.android.util.Util;
import co.vine.android.widgets.PromptDialogSupportFragment;
import com.googlecode.javacv.cpp.opencv_core;
import java.io.IOException;
import java.util.HashMap;
import twitter4j.conf.PropertyConfiguration;

/* loaded from: classes.dex */
public class SignUpNameAvatarFragment extends BaseControllerFragment implements TextWatcher, View.OnClickListener, ImagePicker.Listener, SetThumbnailTask.SetThumbnailListener, ContactsHelper.ContactHelperListener, PromptDialogSupportFragment.OnDialogDoneListener {
    private MenuItem mDoneButton;
    private CheckBox mFollowVineOnTwitterCheckBox;
    private Handler mHandler;
    private boolean mHasPromptedForPhoto;
    private ImagePicker mImagePicker;
    private VineLogin mLogin;
    private EditText mName;
    private EditText mPhoneNumber;
    private ViewGroup mPhoneNumberContainer;
    private boolean mPhotoAttached;
    private ImageView mProfileImage;
    private ImageView mProfileImageAction;
    private Uri mProfileUri;
    private Dialog mProgress;
    private TwitterUser mTwitterUser;

    @Override // co.vine.android.BaseControllerFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mAppController = AppController.getInstance(getActivity());
        if (savedInstanceState != null && savedInstanceState.containsKey("state_pi")) {
            this.mProfileUri = (Uri) savedInstanceState.getParcelable("state_pi");
            if (this.mProfileUri != null) {
                new SetThumbnailTask(this).execute(this.mProfileUri);
            }
        }
        setHasOptionsMenu(true);
        ((SignUpPagerActivity) getActivity()).setBarTitle(R.string.login_sign_up);
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(PropertyConfiguration.USER)) {
            this.mLogin = (VineLogin) bundle.getParcelable(PropertyConfiguration.USER);
        }
        setAppSessionListener(new SignUpListener());
        this.mHasPromptedForPhoto = false;
        this.mHandler = new Handler();
        this.mImagePicker = new ImagePicker(getActivity(), this, this.mAppController.getActiveId());
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup root, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sign_up_flow_name, root, false);
        TextView mTos = (TextView) view.findViewById(R.id.tos_line_2);
        StyleSpan[] boldStyle = {new StyleSpan(1), new StyleSpan(1)};
        Spanned spanned = Util.getSpannedText(boldStyle, getString(R.string.sign_up_tos_line_2), '\"');
        mTos.setText(spanned);
        mTos.setMovementMethod(LinkMovementMethod.getInstance());
        Spannable clickSpannable = (Spannable) mTos.getText();
        StyleSpan[] spans = (StyleSpan[]) spanned.getSpans(0, spanned.length(), StyleSpan.class);
        NuxClickableSpanFactory clickableSpanFactory = new NuxClickableSpanFactory(getResources().getColor(R.color.text_fineprint));
        int start = spanned.getSpanStart(spans[0]);
        int end = spanned.getSpanEnd(spans[0]);
        Util.safeSetSpan(clickSpannable, clickableSpanFactory.newVinePrivacyPolicyClickableSpan(), start, end, 33);
        int start2 = spanned.getSpanStart(spans[1]);
        int end2 = spanned.getSpanEnd(spans[1]);
        Util.safeSetSpan(clickSpannable, clickableSpanFactory.newVineTermsOfServiceClickableSpan(), start2, end2, 33);
        return view;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean onEditorActionClicked(int actionId, View v) {
        FragmentActivity activity = getActivity();
        if (activity != null && actionId == 6 && validate()) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService("input_method");
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            onNextClicked(activity);
            return true;
        }
        return true;
    }

    @Override // android.support.v4.app.Fragment
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mName = (EditText) view.findViewById(R.id.sign_up_username);
        this.mName.addTextChangedListener(this);
        this.mName.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: co.vine.android.nux.SignUpNameAvatarFragment.1
            @Override // android.widget.TextView.OnEditorActionListener
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return SignUpNameAvatarFragment.this.onEditorActionClicked(actionId, SignUpNameAvatarFragment.this.mName);
            }
        });
        this.mPhoneNumberContainer = (ViewGroup) view.findViewById(R.id.phone_tos_container);
        this.mPhoneNumber = (EditText) view.findViewById(R.id.signup_name_phone);
        this.mPhoneNumber.addTextChangedListener(this);
        this.mPhoneNumber.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: co.vine.android.nux.SignUpNameAvatarFragment.2
            @Override // android.widget.TextView.OnEditorActionListener
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return SignUpNameAvatarFragment.this.onEditorActionClicked(actionId, SignUpNameAvatarFragment.this.mPhoneNumber);
            }
        });
        View userImageRoot = view.findViewById(R.id.user_image_container);
        this.mProfileImageAction = (ImageView) userImageRoot.findViewById(R.id.user_image_action);
        this.mProfileImageAction.setVisibility(0);
        userImageRoot.setOnClickListener(this);
        this.mProfileImage = (ImageView) userImageRoot.findViewById(R.id.user_image);
        this.mFollowVineOnTwitterCheckBox = (CheckBox) view.findViewById(R.id.twitter_vine_follow_enabled);
        this.mFollowVineOnTwitterCheckBox.setChecked(true);
        this.mFollowVineOnTwitterCheckBox.setEnabled(true);
        LinearLayout followVineLayout = (LinearLayout) view.findViewById(R.id.follow_vine_on_twitter_selection);
        followVineLayout.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.nux.SignUpNameAvatarFragment.3
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SignUpNameAvatarFragment.this.mFollowVineOnTwitterCheckBox.toggle();
            }
        });
        if (this.mLogin != null) {
            ProgressDialog dialog = new ProgressDialog(getActivity(), R.style.ProgressDialogTheme);
            dialog.setMessage(getString(R.string.loading));
            dialog.setProgress(0);
            dialog.show();
            this.mProgress = dialog;
            this.mAppController.fetchTwitterUser(this.mLogin);
        }
        ContactsHelper.loadContacts(this, this);
    }

    @Override // android.support.v4.app.Fragment
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.done, menu);
        this.mDoneButton = menu.findItem(R.id.done);
        if (this.mDoneButton != null) {
            this.mDoneButton.setEnabled(validate());
        }
    }

    private boolean showProfilePhotoPrompt() {
        if (this.mProfileUri != null || this.mHasPromptedForPhoto || this.mPhotoAttached) {
            return false;
        }
        PromptDialogSupportFragment promptDialogSupportFragment = PromptDialogSupportFragment.newInstance(1).setMessage(R.string.profile_photo_prompt).setPositiveButton(R.string.set_photo).setNegativeButton(R.string.skip);
        promptDialogSupportFragment.setListener(this);
        try {
            promptDialogSupportFragment.show(getActivity().getSupportFragmentManager());
            this.mHasPromptedForPhoto = true;
            return true;
        } catch (IllegalStateException e) {
            return false;
        }
    }

    private void nextClicked() {
        SignUpPagerActivity activity = (SignUpPagerActivity) getActivity();
        activity.setName(this.mName.getText().toString());
        String phoneNumber = this.mPhoneNumber.getText().toString();
        if (!TextUtils.isEmpty(phoneNumber)) {
            activity.setPhone(phoneNumber);
        }
        if (this.mPhotoAttached) {
            activity.setProfile(this.mProfileUri);
        }
        if (this.mTwitterUser != null) {
            this.mTwitterUser.name = this.mName.getText().toString();
            activity.setTwitterUser(this.mTwitterUser);
            boolean followVineOnTwitter = this.mFollowVineOnTwitterCheckBox.isChecked();
            activity.setFollowVineOnTwitter(followVineOnTwitter);
        }
        activity.toNextStep();
    }

    @Override // android.support.v4.app.Fragment
    public boolean onOptionsItemSelected(MenuItem item) {
        return item.getItemId() == R.id.done ? onNextClicked(getActivity()) : super.onOptionsItemSelected(item);
    }

    private boolean onNextClicked(Activity a) {
        if (!showProfilePhotoPrompt()) {
            SignUpPagerActivity activity = (SignUpPagerActivity) a;
            activity.setName(this.mName.getText().toString());
            String phoneNumber = this.mPhoneNumber.getText().toString();
            if (!TextUtils.isEmpty(phoneNumber)) {
                activity.setPhone(phoneNumber);
            }
            activity.setProfile(this.mImagePicker.getSavedImageUri());
            if (this.mTwitterUser != null) {
                this.mTwitterUser.name = this.mName.getText().toString();
                activity.setTwitterUser(this.mTwitterUser);
            }
            nextClicked();
        }
        Util.setSoftKeyboardVisibility(getActivity(), this.mName, false);
        return true;
    }

    @Override // android.text.TextWatcher
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    @Override // android.text.TextWatcher
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    @Override // android.text.TextWatcher
    public void afterTextChanged(Editable editable) {
        if (this.mDoneButton != null) {
            this.mDoneButton.setEnabled(validate());
        }
    }

    @Override // co.vine.android.util.ContactsHelper.ContactHelperListener
    public void onNameLoaded(String nameText) {
        EditText name = this.mName;
        if (TextUtils.isEmpty(name.getText()) && !TextUtils.isEmpty(nameText)) {
            name.setText(nameText);
            name.setSelection(nameText.length());
        }
    }

    @Override // co.vine.android.util.ContactsHelper.ContactHelperListener
    public void onPhoneNumberLoaded(String phoneNumber) {
        if (this.mPhoneNumber.getVisibility() == 0 && TextUtils.isEmpty(this.mPhoneNumber.getText()) && !TextUtils.isEmpty(phoneNumber)) {
            this.mPhoneNumber.setText(phoneNumber);
        }
    }

    @Override // co.vine.android.util.ContactsHelper.ContactHelperListener
    public void onEmailLoaded(String email) {
    }

    private boolean validate() {
        return AuthenticationUtils.isUsernameValid(this.mName.getText().toString());
    }

    private void addPhoto() {
        if (this.mPhotoAttached) {
            PromptDialogSupportFragment p = PromptDialogSupportFragment.newInstance(3);
            p.setTargetFragment(this, 0);
            p.setTitle(R.string.sign_up_profile_photo).setPositiveButton(R.string.remove_photo).setNeutralButton(R.string.take_photo).setNegativeButton(R.string.choose_photo).setButtonPlacementVertical(true).show(getActivity().getSupportFragmentManager());
        } else {
            PromptDialogSupportFragment p2 = PromptDialogSupportFragment.newInstance(2);
            p2.setTargetFragment(this, 0);
            try {
                p2.setTitle(R.string.sign_up_profile_photo).setPositiveButton(R.string.take_photo).setNeutralButton(R.string.choose_photo).show(getActivity().getSupportFragmentManager());
            } catch (IllegalStateException e) {
                CrashUtil.logException(e);
            }
        }
    }

    @Override // android.support.v4.app.Fragment
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setAvatar(null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setAvatar(Bitmap bitmap) {
        if (bitmap != null) {
            this.mProfileImage.setImageDrawable(new RecyclableBitmapDrawable(this.mProfileImage.getResources(), bitmap));
            this.mProfileImage.setColorFilter((ColorFilter) null);
            this.mProfileImageAction.setImageResource(R.drawable.avatar_edit);
            this.mPhotoAttached = true;
            return;
        }
        this.mProfileImage.setImageResource(R.drawable.blank_avatar_nux);
        this.mProfileImageAction.setImageResource(R.drawable.avatar_add);
        this.mPhotoAttached = false;
    }

    @Override // android.support.v4.app.Fragment
    public void onActivityResult(int requestCode, int resultCode, Intent extras) {
        Uri uri;
        super.onActivityResult(requestCode, resultCode, extras);
        switch (requestCode) {
            case 1:
            case 3:
                if (resultCode == -1) {
                    if (requestCode == 1) {
                        uri = this.mProfileUri;
                    } else {
                        uri = extras.getData();
                    }
                    Intent intent = new Intent(getActivity(), (Class<?>) EditProfileCropActivity.class).putExtra("uri", uri);
                    getActivity().startActivityForResult(intent, 2);
                    break;
                } else {
                    this.mProfileUri = null;
                    break;
                }
            case 2:
                if (resultCode == -1 && extras != null) {
                    Uri croppedUri = (Uri) extras.getParcelableExtra("uri");
                    if (croppedUri != null) {
                        ImageUtils.deleteTempPic(this.mProfileUri);
                        this.mProfileUri = croppedUri;
                        new SetThumbnailTask(this).execute(croppedUri);
                        break;
                    }
                } else {
                    this.mProfileUri = null;
                    break;
                }
                break;
        }
    }

    @Override // co.vine.android.ImagePicker.Listener
    public void setAvatarUrl(Uri url) {
        this.mProfileUri = url;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getId() == R.id.user_image_container) {
            addPhoto();
        }
    }

    @Override // co.vine.android.widgets.PromptDialogFragment.OnDialogDoneListener
    public void onDialogDone(DialogInterface dialog, int id, int which) {
        switch (id) {
            case 1:
                switch (which) {
                    case -2:
                        nextClicked();
                        break;
                    case -1:
                        addPhoto();
                        break;
                }
            case 2:
                switch (which) {
                    case opencv_core.CV_StsInternal /* -3 */:
                        this.mImagePicker.chooseImage(3);
                        break;
                    case -1:
                        this.mImagePicker.captureImage(1);
                        break;
                }
            case 3:
                switch (which) {
                    case opencv_core.CV_StsInternal /* -3 */:
                        this.mImagePicker.captureImage(1);
                        break;
                    case -2:
                        this.mImagePicker.chooseImage(3);
                        break;
                    case -1:
                        setThumbnailImage(null);
                        break;
                }
        }
    }

    @Override // android.support.v4.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("state_pi", this.mProfileUri);
    }

    @Override // co.vine.android.SetThumbnailTask.SetThumbnailListener
    public void setThumbnailImage(Bitmap bm) {
        setAvatar(bm);
        if (this.mPhotoAttached) {
            this.mHasPromptedForPhoto = true;
        }
    }

    class SignUpListener extends AppSessionListener {
        SignUpListener() {
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onGetTwitterUserComplete(String reqId, int statusCode, String reasonPhrase, TwitterUser user, VineLogin login) throws Resources.NotFoundException, IOException {
            if (statusCode == 200 || user == null) {
                if (SignUpNameAvatarFragment.this.mProgress != null) {
                    SignUpNameAvatarFragment.this.mProgress.dismiss();
                    return;
                }
                return;
            }
            SignUpNameAvatarFragment.this.mName.setText(user.name);
            SignUpNameAvatarFragment.this.mPhoneNumberContainer.setVisibility(0);
            SignUpNameAvatarFragment.this.mProfileUri = null;
            if (user.defaultProfileImage) {
                if (SignUpNameAvatarFragment.this.mProgress != null) {
                    SignUpNameAvatarFragment.this.mProgress.dismiss();
                }
            } else {
                int w = SignUpNameAvatarFragment.this.getResources().getDimensionPixelSize(R.dimen.user_image_size_large);
                ImageKey key = new ImageKey(user.profileUrl, w, w, true);
                Bitmap bm = SignUpNameAvatarFragment.this.mAppController.getPhotoBitmap(key);
                SignUpNameAvatarFragment.this.setAvatar(bm);
                if (SignUpNameAvatarFragment.this.mPhotoAttached) {
                    SignUpNameAvatarFragment.this.mProfileImage.setTag(null);
                    SignUpNameAvatarFragment.this.mImagePicker.saveProfileImage(bm);
                    if (SignUpNameAvatarFragment.this.mProgress != null) {
                        SignUpNameAvatarFragment.this.mProgress.dismiss();
                    }
                } else {
                    SignUpNameAvatarFragment.this.mProfileImage.setTag(key);
                }
            }
            SignUpNameAvatarFragment.this.mTwitterUser = user;
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onPhotoImageLoaded(HashMap<ImageKey, UrlImage> images) throws IOException {
            ImageKey key = (ImageKey) SignUpNameAvatarFragment.this.mProfileImage.getTag();
            UrlImage image = images.get(key);
            if (image != null && image.isValid()) {
                SignUpNameAvatarFragment.this.setAvatar(image.bitmap);
                SignUpNameAvatarFragment.this.mImagePicker.saveProfileImage(image.bitmap);
                if (SignUpNameAvatarFragment.this.mProgress != null) {
                    SignUpNameAvatarFragment.this.mProgress.dismiss();
                }
            }
        }
    }

    @Override // co.vine.android.BaseFragment
    protected void updateAppNavigationProvider() {
    }

    @Override // co.vine.android.BaseFragment, android.support.v4.app.Fragment
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            AppNavigationProviderSingleton.getInstance().setViewAndSubview(getAppNavigationView(), getAppNavigationSubview());
        }
    }

    @Override // co.vine.android.BaseFragment
    protected AppNavigation.Views getAppNavigationView() {
        if (this.mLogin == null) {
            return AppNavigation.Views.SIGN_UP_EMAIL_1;
        }
        return this.mLogin.loginType == 1 ? AppNavigation.Views.SIGN_UP_EMAIL_1 : AppNavigation.Views.SIGNUP_TWITTER;
    }

    public void onMoveTo() {
        if (this.mHandler != null) {
            this.mHandler.postDelayed(new Runnable() { // from class: co.vine.android.nux.SignUpNameAvatarFragment.4
                @Override // java.lang.Runnable
                public void run() {
                    FragmentActivity activity = SignUpNameAvatarFragment.this.getActivity();
                    if (activity != null) {
                        Util.setSoftKeyboardVisibility(activity, SignUpNameAvatarFragment.this.mName, true);
                    }
                }
            }, 300L);
        }
    }
}
