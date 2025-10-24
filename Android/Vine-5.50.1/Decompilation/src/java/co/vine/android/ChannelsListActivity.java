package co.vine.android;

import android.content.Intent;
import android.os.Bundle;

/* loaded from: classes.dex */
public class ChannelsListActivity extends BaseControllerActionBarActivity {
    @Override // co.vine.android.BaseActionBarActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityDonut, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.fragment_layout, true);
        setupActionBar((Boolean) true, (Boolean) true, R.string.select_channel_title, (Boolean) true, (Boolean) true);
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            ChannelsListFragment fragment = new ChannelsListFragment();
            long selectedChannel = intent.getLongExtra("selected_channel", -1L);
            intent.putExtra("refresh", false);
            intent.putExtra("take_focus", true);
            intent.putExtra("selected_channel", selectedChannel);
            intent.putExtra("is_nux", false);
            fragment.setArguments(BaseCursorListFragment.prepareArguments(intent, false));
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }
}
