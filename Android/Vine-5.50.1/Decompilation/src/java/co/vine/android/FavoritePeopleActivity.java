package co.vine.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/* loaded from: classes.dex */
public class FavoritePeopleActivity extends BaseControllerActionBarActivity {
    @Override // co.vine.android.BaseActionBarActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityDonut, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.user_list, true);
        Intent intent = getIntent();
        setupActionBar((Boolean) true, (Boolean) true, R.string.favorite_people, (Boolean) true, (Boolean) false);
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.getInt("color", 0) != 0) {
            setActionBarColor(extras.getInt("color"));
        }
        if (savedInstanceState == null) {
            FavoritePeopleFragment fragment = new FavoritePeopleFragment();
            Bundle b = BaseArrayListFragment.prepareArguments(intent, false);
            b.putBoolean("refresh", true);
            fragment.setArguments(b);
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }

    public static void start(Context context, int actionBarColor) {
        Intent intent = new Intent(context, (Class<?>) FavoritePeopleActivity.class);
        intent.putExtra("color", actionBarColor);
        context.startActivity(intent);
    }
}
