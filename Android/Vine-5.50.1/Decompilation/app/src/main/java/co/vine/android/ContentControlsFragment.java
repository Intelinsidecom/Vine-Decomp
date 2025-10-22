package co.vine.android;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import co.vine.android.client.AppSessionListener;
import co.vine.android.util.BuildUtil;
import co.vine.android.util.Util;
import co.vine.android.util.ViewUtil;
import co.vine.android.views.SwitchInterface;
import co.vine.android.widgets.PromptDialogSupportFragment;

/* loaded from: classes.dex */
public class ContentControlsFragment extends BaseControllerFragment implements CompoundButton.OnCheckedChangeListener {
    private boolean mExplicit;
    private ImageView mExplicitIcon;
    private SwitchInterface mExplicitSwitch;
    private boolean mPrivate;
    private ImageView mPrivateIcon;
    private SwitchInterface mPrivateSwitch;
    private ProgressDialog mProgress;
    private View mView;
    private final PromptDialogSupportFragment.OnDialogDoneListener mProtectPostDialogDoneListner = new PromptDialogSupportFragment.OnDialogDoneListener() { // from class: co.vine.android.ContentControlsFragment.1
        @Override // co.vine.android.widgets.PromptDialogFragment.OnDialogDoneListener
        public void onDialogDone(DialogInterface dialog, int id, int which) {
            switch (which) {
                case -1:
                    ContentControlsFragment.this.mPrivate = true;
                    ContentControlsFragment.this.mProgress.show();
                    ContentControlsFragment.this.mAppController.updatePrivate(ContentControlsFragment.this.mAppController.getActiveSession(), true);
                    break;
                default:
                    ContentControlsFragment.this.mPrivate = false;
                    ContentControlsFragment.this.mPrivateIcon.setImageResource(R.drawable.ic_settings_private_off);
                    ContentControlsFragment.this.mPrivateSwitch.setOnCheckedChangeListener(null);
                    ContentControlsFragment.this.mPrivateSwitch.setChecked(false);
                    ContentControlsFragment.this.mPrivateSwitch.setOnCheckedChangeListener(ContentControlsFragment.this);
                    break;
            }
        }
    };
    private final PromptDialogSupportFragment.OnDialogDoneListener mUnProtectPostDialogDoneListner = new PromptDialogSupportFragment.OnDialogDoneListener() { // from class: co.vine.android.ContentControlsFragment.2
        @Override // co.vine.android.widgets.PromptDialogFragment.OnDialogDoneListener
        public void onDialogDone(DialogInterface dialog, int id, int which) {
            switch (which) {
                case -1:
                    ContentControlsFragment.this.mPrivate = false;
                    ContentControlsFragment.this.mProgress.show();
                    ContentControlsFragment.this.mAppController.updatePrivate(ContentControlsFragment.this.mAppController.getActiveSession(), false);
                    break;
                default:
                    ContentControlsFragment.this.mPrivate = false;
                    ContentControlsFragment.this.mPrivateIcon.setImageResource(R.drawable.ic_settings_private_on);
                    ContentControlsFragment.this.mPrivateSwitch.setOnCheckedChangeListener(null);
                    ContentControlsFragment.this.mPrivateSwitch.setChecked(true);
                    ContentControlsFragment.this.mPrivateSwitch.setOnCheckedChangeListener(ContentControlsFragment.this);
                    break;
            }
        }
    };
    private final PromptDialogSupportFragment.OnDialogDoneListener mExplictOnDoneListner = new PromptDialogSupportFragment.OnDialogDoneListener() { // from class: co.vine.android.ContentControlsFragment.3
        @Override // co.vine.android.widgets.PromptDialogFragment.OnDialogDoneListener
        public void onDialogDone(DialogInterface dialog, int id, int which) {
            switch (which) {
                case -1:
                    ContentControlsFragment.this.mExplicit = true;
                    ContentControlsFragment.this.mProgress.show();
                    ContentControlsFragment.this.mAppController.updateExplicit(ContentControlsFragment.this.mAppController.getActiveSession(), true);
                    break;
                default:
                    ContentControlsFragment.this.mExplicit = false;
                    ContentControlsFragment.this.mExplicitIcon.setImageResource(R.drawable.ic_settings_explicit_off);
                    ContentControlsFragment.this.mExplicitSwitch.setOnCheckedChangeListener(null);
                    ContentControlsFragment.this.mExplicitSwitch.setChecked(false);
                    ContentControlsFragment.this.mExplicitSwitch.setOnCheckedChangeListener(ContentControlsFragment.this);
                    break;
            }
        }
    };
    private final PromptDialogSupportFragment.OnDialogDoneListener mExplictOffDoneListner = new PromptDialogSupportFragment.OnDialogDoneListener() { // from class: co.vine.android.ContentControlsFragment.4
        @Override // co.vine.android.widgets.PromptDialogFragment.OnDialogDoneListener
        public void onDialogDone(DialogInterface dialog, int id, int which) {
            switch (which) {
                case -1:
                    ContentControlsFragment.this.mExplicit = false;
                    ContentControlsFragment.this.mProgress.show();
                    ContentControlsFragment.this.mAppController.updateExplicit(ContentControlsFragment.this.mAppController.getActiveSession(), false);
                    break;
                default:
                    ContentControlsFragment.this.mExplicit = true;
                    ContentControlsFragment.this.mExplicitIcon.setImageResource(R.drawable.ic_settings_explicit_on);
                    ContentControlsFragment.this.mExplicitSwitch.setOnCheckedChangeListener(null);
                    ContentControlsFragment.this.mExplicitSwitch.setChecked(true);
                    ContentControlsFragment.this.mExplicitSwitch.setOnCheckedChangeListener(ContentControlsFragment.this);
                    break;
            }
        }
    };

    @Override // co.vine.android.BaseControllerFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAppSessionListener(new ContentControlsListener());
        if (savedInstanceState != null) {
            this.mPrivate = savedInstanceState.getBoolean("state_private", false);
            this.mExplicit = savedInstanceState.getBoolean("state_explicit", false);
        } else {
            SharedPreferences prefs = Util.getDefaultSharedPrefs(getActivity());
            this.mPrivate = prefs.getBoolean("settings_private", false);
            this.mExplicit = prefs.getBoolean("settings_explicit", false);
        }
        ProgressDialog prog = new ProgressDialog(getActivity(), R.style.ProgressDialogTheme);
        prog.setMessage(getString(R.string.content_controls_updating));
        prog.setProgressStyle(0);
        this.mProgress = prog;
    }

    @Override // android.support.v4.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("state_private", this.mPrivate);
        outState.putBoolean("state_explicit", this.mExplicit);
    }

    @Override // android.support.v4.app.Fragment
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ViewUtil.reduceTextSizeViaFontScaleIfNeeded(getActivity(), 1.1f, 10.0f, (TextView) this.mView.findViewById(R.id.text1), (TextView) this.mView.findViewById(R.id.text2));
        ViewUtil.reduceTextSizeViaFontScaleIfNeeded(getActivity(), 1.1f, 5.0f, (TextView) this.mView.findViewById(R.id.text3), (TextView) this.mView.findViewById(R.id.explicit_description_settings));
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mView = inflater.inflate(R.layout.content_controls, container, false);
        return this.mView;
    }

    @Override // android.support.v4.app.Fragment
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SwitchInterface pSwitch = (SwitchInterface) view.findViewById(R.id.private_switch);
        SwitchInterface eSwitch = (SwitchInterface) view.findViewById(R.id.explicit_switch);
        this.mPrivateIcon = (ImageView) view.findViewById(R.id.private_icon);
        if (this.mPrivate) {
            this.mPrivateIcon.setImageResource(R.drawable.ic_settings_private_on);
            pSwitch.setChecked(true);
        } else {
            this.mPrivateIcon.setImageResource(R.drawable.ic_settings_private_off);
        }
        if (BuildUtil.isExplore()) {
            view.findViewById(R.id.explicit_row_container_settings).setVisibility(8);
            view.findViewById(R.id.explicit_description_settings).setVisibility(8);
        } else {
            this.mExplicitIcon = (ImageView) view.findViewById(R.id.explicit_icon);
            if (this.mExplicit) {
                this.mExplicitIcon.setImageResource(R.drawable.ic_settings_explicit_on);
                eSwitch.setChecked(true);
            } else {
                this.mExplicitIcon.setImageResource(R.drawable.ic_settings_explicit_off);
            }
        }
        pSwitch.setOnCheckedChangeListener(this);
        eSwitch.setOnCheckedChangeListener(this);
        this.mPrivateSwitch = pSwitch;
        this.mExplicitSwitch = eSwitch;
    }

    @Override // android.widget.CompoundButton.OnCheckedChangeListener
    public void onCheckedChanged(CompoundButton compoundButton, boolean on) {
        int id = compoundButton.getId();
        if (id == R.id.private_switch) {
            if (on) {
                this.mPrivateIcon.setImageResource(R.drawable.ic_settings_private_on);
                PromptDialogSupportFragment p = PromptDialogSupportFragment.newInstance(1);
                p.setTitle(R.string.content_controls_protect_my_posts);
                p.setMessage(R.string.content_controls_protect_my_posts_message);
                p.setNegativeButton(R.string.cancel);
                p.setPositiveButton(R.string.content_controls_protect);
                p.setListener(this.mProtectPostDialogDoneListner);
                p.show(getActivity().getSupportFragmentManager());
                return;
            }
            this.mPrivateIcon.setImageResource(R.drawable.ic_settings_private_off);
            PromptDialogSupportFragment p2 = PromptDialogSupportFragment.newInstance(2);
            p2.setTitle(R.string.content_controls_unprotect_my_posts);
            p2.setMessage(R.string.content_controls_unprotect_my_posts_message);
            p2.setNegativeButton(R.string.cancel);
            p2.setPositiveButton(R.string.content_controls_unprotect);
            p2.setListener(this.mUnProtectPostDialogDoneListner);
            p2.show(getActivity().getSupportFragmentManager());
            return;
        }
        if (id == R.id.explicit_switch) {
            if (on) {
                this.mExplicitIcon.setImageResource(R.drawable.ic_settings_explicit_on);
                PromptDialogSupportFragment p3 = PromptDialogSupportFragment.newInstance(3);
                p3.setTitle(R.string.content_controls_explicit_my_posts);
                p3.setMessage(R.string.content_controls_explicit_my_posts_message);
                p3.setNegativeButton(R.string.cancel);
                p3.setPositiveButton(R.string.content_controls_make_explicit);
                p3.setListener(this.mExplictOnDoneListner);
                p3.show(getActivity().getSupportFragmentManager());
                return;
            }
            this.mExplicitIcon.setImageResource(R.drawable.ic_settings_explicit_off);
            PromptDialogSupportFragment p4 = PromptDialogSupportFragment.newInstance(4);
            p4.setTitle(R.string.content_controls_unexplicit_my_posts);
            p4.setMessage(R.string.content_controls_unexplicit_my_posts_message);
            p4.setNegativeButton(R.string.cancel);
            p4.setPositiveButton(R.string.content_controls_unmake_explicit);
            p4.setListener(this.mExplictOffDoneListner);
            p4.show(getFragmentManager());
        }
    }

    class ContentControlsListener extends AppSessionListener {
        ContentControlsListener() {
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onUpdatePrivateComplete(String reqId, int statusCode, String reasonPhrase, boolean priv) {
            if (ContentControlsFragment.this.mProgress != null) {
                ContentControlsFragment.this.mProgress.dismiss();
            }
            Activity activity = ContentControlsFragment.this.getActivity();
            if (statusCode == 200) {
                SharedPreferences.Editor editor = Util.getDefaultSharedPrefs(activity).edit();
                editor.putBoolean("settings_private", priv);
                editor.apply();
            } else if (!TextUtils.isEmpty(reasonPhrase)) {
                Util.showCenteredToast(activity, reasonPhrase);
            } else {
                Util.showCenteredToast(activity, R.string.content_controls_update_fail);
            }
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onUpdateExplicitComplete(String reqId, int statusCode, String reasonPhrase, boolean explicit) {
            if (ContentControlsFragment.this.mProgress != null) {
                ContentControlsFragment.this.mProgress.dismiss();
            }
            Activity activity = ContentControlsFragment.this.getActivity();
            if (statusCode == 200) {
                SharedPreferences.Editor editor = Util.getDefaultSharedPrefs(ContentControlsFragment.this.getActivity()).edit();
                editor.putBoolean("settings_explicit", explicit);
                editor.apply();
            } else if (!TextUtils.isEmpty(reasonPhrase)) {
                Util.showCenteredToast(activity, reasonPhrase);
            } else {
                Util.showCenteredToast(activity, R.string.content_controls_update_fail);
            }
        }
    }
}
