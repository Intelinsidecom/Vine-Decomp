package co.vine.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/* loaded from: classes.dex */
public class ImageActivity extends BaseControllerActionBarActivity {
    @Override // co.vine.android.BaseActionBarActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityDonut, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.fragment_layout, true);
        setRequestedOrientation(1);
        Intent intent = getIntent();
        int titleResId = intent.getIntExtra("title", 0);
        if (titleResId == 0) {
            throw new IllegalArgumentException("An ActionBar title resourceId must be provided when starting this Activity.");
        }
        setupActionBar((Boolean) true, (Boolean) true, titleResId, (Boolean) true, (Boolean) true);
        if (savedInstanceState == null) {
            ImageFragment fragment = new ImageFragment();
            intent.putExtra("take_focus", true);
            fragment.setArguments(BaseCursorListFragment.prepareArguments(intent, false));
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment, "image_fragment").commit();
        }
    }

    public static void start(Context context, String imageUrl, int titleResId) {
        Intent intent = new Intent(context, (Class<?>) ImageActivity.class);
        intent.putExtra("image_url", imageUrl);
        intent.putExtra("title", titleResId);
        context.startActivity(intent);
    }
}
