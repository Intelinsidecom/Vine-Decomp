package co.vine.android;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;
import co.vine.android.service.components.Components;
import co.vine.android.service.components.userinteraction.UserInteractionsListener;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class ChannelsFollowActivity extends BaseControllerActionBarActivity {
    private ArrayList<String> mChannelIdsToFollow = new ArrayList<>();
    private ArrayList<String> mChannelIdsToUnfollow = new ArrayList<>();

    @Override // co.vine.android.BaseActionBarActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityDonut, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.fragment_layout, true, true);
        setupActionBar((Boolean) true, (Boolean) true, R.string.show_me, (Boolean) true, (Boolean) false);
        if (savedInstanceState == null) {
            Intent intent = new Intent();
            ChannelsListFragment fragment = new ChannelsListFragment();
            intent.putExtra("take_focus", true);
            intent.putExtra("show_channel_follow", true);
            fragment.setArguments(BaseCursorListFragment.prepareArguments(intent, false));
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment, "channels_list").commit();
        }
        setUpCallbacks();
    }

    @Override // co.vine.android.BaseActionBarActivity, android.app.Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.done) {
            ChannelsListFragment fragment = getFragment();
            if (fragment != null) {
                followChannelsAndFinish(fragment);
            } else {
                finish();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
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
        this.mChannelIdsToUnfollow.addAll(fragment.getChannelIdsToUnfollow());
        Components.userInteractionsComponent().bulkUnfollowChannels(this.mAppController, this.mChannelIdsToUnfollow, false);
        this.mChannelIdsToFollow.addAll(fragment.getSelectedChannelIds());
        Components.userInteractionsComponent().bulkFollowChannels(this.mAppController, this.mChannelIdsToFollow, true);
    }

    private ChannelsListFragment getFragment() {
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentByTag("channels_list");
        if (fragment instanceof ChannelsListFragment) {
            return (ChannelsListFragment) fragment;
        }
        return null;
    }

    private void setUpCallbacks() {
        Components.userInteractionsComponent().addListener(new UserInteractionsListener() { // from class: co.vine.android.ChannelsFollowActivity.1
            @Override // co.vine.android.service.components.userinteraction.UserInteractionsListener
            public void onBulkFollowChannelsComplete(String reqId, int statusCode, String reasonPhrase) {
                ChannelsFollowActivity.this.dismissDialog();
                ChannelsFollowActivity.this.finish();
            }
        });
    }
}
