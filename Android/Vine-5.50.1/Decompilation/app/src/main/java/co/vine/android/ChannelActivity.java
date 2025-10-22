package co.vine.android;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import co.vine.android.api.VineChannel;
import java.util.List;

/* loaded from: classes.dex */
public class ChannelActivity extends BaseTimelineActivity {
    private String mFragmentTag;

    private enum ChannelType {
        EXPLORE,
        PROFILE
    }

    @Override // co.vine.android.BaseActionBarActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityDonut, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) throws Resources.NotFoundException {
        int navRGB;
        int titleRGB;
        TextView title;
        super.onCreate(savedInstanceState, R.layout.fragment_layout, false);
        setRequestedOrientation(1);
        Intent intent = getIntent();
        if (intent == null) {
            throw new IllegalAccessError("Intent is null. You must provide a URI in order to view a channel.");
        }
        ChannelType type = (ChannelType) intent.getSerializableExtra("type");
        if (type == null) {
            type = ChannelType.PROFILE;
        }
        if (type == ChannelType.PROFILE) {
            this.mFragmentTag = "profile";
            setupActionBar((Boolean) true, (Boolean) true, R.string.profile_screen_title, (Boolean) true, (Boolean) false);
            setActionBarColor(getResources().getColor(R.color.bg_profile_header));
            if (savedInstanceState == null) {
                ProfileFragment fragment = new ProfileFragment();
                intent.putExtra("refresh", true);
                intent.putExtra("take_focus", true);
                intent.putExtra("hide_user_name", true);
                fragment.setArguments(BaseArrayListFragment.prepareArguments(intent, true));
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment, this.mFragmentTag).commit();
                return;
            }
            return;
        }
        if (type == ChannelType.EXPLORE) {
            this.mFragmentTag = "explore_channel";
            VineChannel channel = (VineChannel) intent.getParcelableExtra("channel");
            String backgroundColor = channel.backgroundColor;
            if (TextUtils.isEmpty(backgroundColor)) {
                navRGB = getResources().getColor(R.color.vine_green);
            } else {
                try {
                    navRGB = Color.parseColor(backgroundColor);
                } catch (IllegalArgumentException e) {
                    navRGB = getResources().getColor(R.color.vine_green);
                }
            }
            String fontColor = channel.fontColor;
            if (TextUtils.isEmpty(fontColor)) {
                titleRGB = getResources().getColor(R.color.solid_white);
            } else {
                try {
                    titleRGB = Color.parseColor(fontColor);
                } catch (IllegalArgumentException e2) {
                    titleRGB = getResources().getColor(R.color.solid_white);
                }
            }
            setupActionBar((Boolean) true, (Boolean) true, channel.channel, (Boolean) true, (Boolean) false);
            setActionBarColor(navRGB);
            int actionBarTitleId = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
            if (actionBarTitleId > 0 && (title = (TextView) findViewById(actionBarTitleId)) != null) {
                title.setTextColor(titleRGB);
            }
            if (savedInstanceState == null) {
                ChannelFragment fragment2 = new ChannelFragment();
                intent.putExtra("refresh", true);
                intent.putExtra("take_focus", true);
                fragment2.setArguments(BaseArrayListFragment.prepareArguments(intent, true));
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment2, this.mFragmentTag).commit();
            }
        }
    }

    @Override // co.vine.android.BaseTimelineActivity
    protected BaseTimelineFragment getCurrentTimeLineFragment() {
        return (BaseTimelineFragment) getSupportFragmentManager().findFragmentByTag(this.mFragmentTag);
    }

    @Override // co.vine.android.BaseTimelineActivity, co.vine.android.BaseActionBarActivity, android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        this.mShowCaptureIcon = false;
        return super.onCreateOptionsMenu(menu);
    }

    @Override // co.vine.android.BaseActionBarActivity, android.app.Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.feed) {
            BaseTimelineFragment timeline = getCurrentTimeLineFragment();
            if (timeline instanceof ProfileFragment) {
                ((ProfileFragment) getCurrentTimeLineFragment()).toTheaterMode(this);
            } else if (timeline instanceof ChannelFragment) {
                ((ChannelFragment) getCurrentTimeLineFragment()).toTheaterMode(this);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public static void startProfile(Context context, long userId, String followEventSource) {
        startProfile(context, userId, followEventSource, true, false);
    }

    public static void startProfileWatchMode(Context context, long userId, String followEventSource) {
        startProfile(context, userId, followEventSource, true, true);
    }

    public static void startProfile(Context context, long userId, String followEventSource, boolean showProfileActions) {
        startProfile(context, userId, followEventSource, showProfileActions, false);
    }

    public static void startProfile(Context context, long userId, String followEventSource, boolean showProfileActions, boolean watchMode) {
        Intent i = new Intent(context, (Class<?>) ChannelActivity.class);
        i.putExtra("type", ChannelType.PROFILE);
        i.putExtra("user_id", userId);
        i.putExtra("event_source", followEventSource);
        i.putExtra("show_profile_actions", showProfileActions);
        i.putExtra("watch_mode", watchMode);
        context.startActivity(i);
    }

    public static void startExploreChannel(Context context, Uri data) {
        if (data == null) {
            throw new IllegalAccessError("Intent data is null. You must provide a URI in order to view a channel.");
        }
        VineChannel channel = new VineChannel();
        String navRGBParam = data.getQueryParameter("navRGB");
        if (navRGBParam != null && !navRGBParam.startsWith("#")) {
            channel.backgroundColor = "#" + navRGBParam;
        }
        channel.fontColor = data.getQueryParameter("titleRGB");
        List<String> pathSegments = data.getPathSegments();
        channel.channelId = Long.parseLong(pathSegments.get(0));
        boolean watchMode = false;
        if (pathSegments.size() > 1 && "watch".equals(pathSegments.get(1))) {
            watchMode = true;
        }
        channel.retinaIconFullUrl = data.getQueryParameter("icon");
        channel.channel = data.getQueryParameter("name");
        if (channel.channel != null) {
            channel.channel = channel.channel.replace('+', ' ');
        }
        channel.showRecent = Boolean.valueOf(data.getBooleanQueryParameter("showRecent", false));
        startExploreChannel(context, channel, watchMode);
    }

    public static void startExploreChannel(Context context, VineChannel channel, boolean watchMode) {
        Intent intent = new Intent(context, (Class<?>) ChannelActivity.class);
        intent.putExtra("type", ChannelType.EXPLORE);
        intent.putExtra("channel", channel);
        intent.putExtra("watch_mode", watchMode);
        context.startActivity(intent);
    }

    @Override // co.vine.android.BaseActionBarActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 3) {
            getCurrentTimeLineFragment().onActivityResult(requestCode, resultCode, data);
            return;
        }
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(this.mFragmentTag);
        if (fragment instanceof BaseTimelineFragment) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void updateActionBarWithUsername(String username, String twitterScreenname) {
        if (!TextUtils.isEmpty(username)) {
            ActionBar ab = getSupportActionBar();
            ab.setDisplayShowCustomEnabled(true);
            ab.setDisplayShowTitleEnabled(false);
            View v = getLayoutInflater().inflate(R.layout.profile_actionbar, (ViewGroup) null);
            TextView usernameTextview = (TextView) v.findViewById(R.id.username);
            TextView twitterScreennameTextview = (TextView) v.findViewById(R.id.twitterScreenname);
            usernameTextview.setText(username);
            if (twitterScreenname != null) {
                twitterScreennameTextview.setText("@" + twitterScreenname);
                twitterScreennameTextview.setVisibility(0);
            } else {
                twitterScreennameTextview.setVisibility(8);
            }
            ab.setCustomView(v);
            v.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.ChannelActivity.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    Fragment fragment = ChannelActivity.this.getSupportFragmentManager().findFragmentByTag(ChannelActivity.this.mFragmentTag);
                    ((ProfileFragment) fragment).togglerTwitterTooltip();
                }
            });
        }
    }
}
