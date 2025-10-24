package co.vine.android;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;
import co.vine.android.nux.NuxResolver;
import co.vine.android.scribe.AppNavigationProviderSingleton;
import co.vine.android.scribe.model.AppNavigation;
import co.vine.android.service.components.Components;
import co.vine.android.service.components.userinteraction.UserInteractionsListener;
import co.vine.android.util.analytics.FlurryUtils;
import co.vine.android.widgets.PromptDialogSupportFragment;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class ChannelsListNUXActivity extends BaseControllerActionBarActivity {
    private ArrayList<String> mChannelIdsToFollow = new ArrayList<>();

    @Override // co.vine.android.BaseActionBarActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityDonut, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.fragment_layout, true, true);
        setupActionBar((Boolean) false, (Boolean) true, R.string.show_me, (Boolean) false, (Boolean) false);
        if (savedInstanceState == null) {
            Intent intent = new Intent();
            ChannelsListFragment fragment = new ChannelsListFragment();
            intent.putExtra("take_focus", true);
            intent.putExtra("show_channel_follow", true);
            intent.putExtra("is_nux", true);
            fragment.setArguments(BaseCursorListFragment.prepareArguments(intent, false));
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment, "channels_list").commit();
        }
        setUpCallbacks();
        FlurryUtils.trackNuxScreenDisplayed("channel_follow");
    }

    @Override // co.vine.android.BaseControllerActionBarActivity, co.vine.android.BaseActionBarActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        AppNavigationProviderSingleton.getInstance().setViewAndSubview(AppNavigation.Views.CHANNEL_FOLLOW, null);
    }

    @Override // co.vine.android.BaseActionBarActivity, android.app.Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.next) {
            ChannelsListFragment fragment = getFragment();
            if (fragment != null) {
                if (fragment.getSelectedChannelIds().isEmpty()) {
                    showSkipConfirmation();
                    return true;
                }
                followChannelsAndFinish(fragment);
                return true;
            }
            toNextStep();
            return true;
        }
        return true;
    }

    private void showSkipConfirmation() {
        PromptDialogSupportFragment dialog = PromptDialogSupportFragment.newInstance(1).setMessage(R.string.NUX_are_you_sure_you_want_to_skip).setNegativeButton(R.string.NUX_yes_im_sure).setPositiveButton(R.string.NUX_go_back);
        dialog.setListener(new PromptDialogSupportFragment.OnDialogDoneListener() { // from class: co.vine.android.ChannelsListNUXActivity.1
            @Override // co.vine.android.widgets.PromptDialogFragment.OnDialogDoneListener
            public void onDialogDone(DialogInterface dialog2, int id, int which) {
                switch (which) {
                    case -2:
                        ChannelsListNUXActivity.this.toNextStep();
                        break;
                    case -1:
                        dialog2.dismiss();
                        break;
                }
            }
        });
        try {
            dialog.show(getSupportFragmentManager());
        } catch (Exception e) {
        }
    }

    private void followChannelsAndFinish(ChannelsListFragment fragment) {
        ProgressDialog dialog = new ProgressDialog(this, R.style.ProgressDialogTheme);
        this.mProgressDialog = dialog;
        dialog.setProgressStyle(0);
        dialog.setMessage(getString(R.string.following_channels));
        try {
            dialog.show();
        } catch (Exception e) {
        }
        this.mChannelIdsToFollow.addAll(fragment.getSelectedChannelIds());
        Components.userInteractionsComponent().bulkFollowChannels(this.mAppController, this.mChannelIdsToFollow, true);
    }

    private void setUpCallbacks() {
        Components.userInteractionsComponent().addListener(new UserInteractionsListener() { // from class: co.vine.android.ChannelsListNUXActivity.2
            @Override // co.vine.android.service.components.userinteraction.UserInteractionsListener
            public void onBulkFollowChannelsComplete(String reqId, int statusCode, String reasonPhrase) {
                ChannelsListNUXActivity.this.dismissDialog();
                ChannelsListNUXActivity.this.toNextStep();
            }
        });
    }

    @Override // co.vine.android.BaseActionBarActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onBackPressed() {
    }

    private ChannelsListFragment getFragment() {
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentByTag("channels_list");
        if (fragment instanceof ChannelsListFragment) {
            return (ChannelsListFragment) fragment;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void toNextStep() {
        FlurryUtils.trackNuxChannelFollows(this.mChannelIdsToFollow);
        NuxResolver.toNuxFromChannelList(this);
        finish();
    }
}
