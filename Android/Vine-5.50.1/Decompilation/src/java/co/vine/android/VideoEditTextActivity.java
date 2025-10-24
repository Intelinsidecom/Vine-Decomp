package co.vine.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import co.vine.android.api.VinePost;

/* loaded from: classes.dex */
public class VideoEditTextActivity extends BaseControllerActionBarActivity {
    @Override // co.vine.android.BaseActionBarActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityDonut, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(1);
        super.onCreate(savedInstanceState, R.layout.fragment_layout, true);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().hide();
        }
        Intent i = getIntent();
        VinePost post = (VinePost) i.getParcelableExtra("post");
        String videoPath = i.getStringExtra("video_path");
        int mode = i.getIntExtra("mode", 0);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, VideoEditTextFragment.newInstance(videoPath, post, mode)).commit();
        }
    }

    public static Intent getIntent(Context context, String videoPath, VinePost post, int mode) {
        Intent i = new Intent(context, (Class<?>) VideoEditTextActivity.class);
        i.putExtra("post", post);
        i.putExtra("video_path", videoPath);
        i.putExtra("mode", mode);
        return i;
    }

    @Override // android.app.Activity
    public boolean onPrepareOptionsMenu(Menu menu) {
        return false;
    }
}
