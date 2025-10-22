package co.vine.android;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.StatFs;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import co.vine.android.cache.video.VideoKey;
import co.vine.android.client.AppSessionListener;
import co.vine.android.util.Util;
import co.vine.android.util.VideoSaver;

/* loaded from: classes.dex */
public class DownloadVineActivity extends BaseControllerActionBarActivity {
    private Button mAddButton;
    private boolean mAllSelected;
    private long mAvaibleSpace;
    private TextView mEstimatedSpace;
    private DownloadPostsFragment mFragment;
    private Handler mHandler;
    private ProgressDialog mProgressDialog;
    private TextView mSelectAllButton;
    private VideoSavedListener mVideoSavedListener;
    private VideoSaver mVideoSaver;

    @Override // co.vine.android.BaseActionBarActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityDonut, android.app.Activity
    @SuppressLint({"MissingSuperCall"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_import_video_tab, true);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.mAddButton = (Button) findViewById(R.id.import_add);
        this.mSelectAllButton = (TextView) findViewById(R.id.select_all);
        this.mEstimatedSpace = (TextView) findViewById(R.id.estimated_space);
        this.mAvaibleSpace = getAvailableStorage();
        this.mEstimatedSpace.setText(getString(R.string.estimate_space, new Object[]{0, Long.valueOf(this.mAvaibleSpace)}));
        this.mProgressDialog = new ProgressDialog(this);
        this.mProgressDialog.setProgressStyle(1);
        this.mProgressDialog.setCancelable(true);
        this.mProgressDialog.setIndeterminate(false);
        this.mVideoSavedListener = new VideoSavedListener();
        this.mVideoSaver = new VideoSaver(this, this.mAppController, this.mVideoSavedListener);
        this.mHandler = new Handler(Looper.getMainLooper());
        this.mAddButton.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.DownloadVineActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (DownloadVineActivity.this.mFragment.mSelectedVines.size() != 0) {
                    DownloadVineActivity.this.mProgressDialog.setMax(DownloadVineActivity.this.mFragment.mSelectedVines.size());
                    DownloadVineActivity.this.mProgressDialog.show();
                    DownloadVineActivity.this.mProgressDialog.onStart();
                    for (VideoKey video : DownloadVineActivity.this.mFragment.mSelectedVines) {
                        DownloadVineActivity.this.mVideoSaver.saveVideo(video.url);
                    }
                }
            }
        });
        this.mSelectAllButton.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.DownloadVineActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                DownloadVineActivity.this.mAllSelected = !DownloadVineActivity.this.mAllSelected;
                if (DownloadVineActivity.this.mAllSelected) {
                    DownloadVineActivity.this.mFragment.selectAll();
                    DownloadVineActivity.this.mSelectAllButton.setText(DownloadVineActivity.this.getResources().getString(R.string.deselect_all));
                } else {
                    DownloadVineActivity.this.mFragment.unSelectAll();
                    DownloadVineActivity.this.mSelectAllButton.setText(DownloadVineActivity.this.getResources().getString(R.string.select_all));
                }
            }
        });
        Bundle bundle = new Bundle();
        bundle.putInt("columns", 3);
        this.mFragment = DownloadPostsFragment.newInstance(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, this.mFragment, "POSTS").commit();
        this.mAppSessionListener = new RequestEmailDownloadListener();
    }

    private class RequestEmailDownloadListener extends AppSessionListener {
        private RequestEmailDownloadListener() {
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onRequestEmailDownload(String reqId, int statusCode, String reasonPhrase) {
            if (statusCode == 200) {
                DownloadVineActivity.this.showEmailConfirm();
            } else {
                DownloadVineActivity.this.showError();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showError() {
        new AlertDialog.Builder(this).setMessage(R.string.error_requesting).setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() { // from class: co.vine.android.DownloadVineActivity.3
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        }).show();
    }

    private class VideoSavedListener implements VideoSaver.VideoSavedListener {
        private int saved;

        private VideoSavedListener() {
            this.saved = 0;
        }

        @Override // co.vine.android.util.VideoSaver.VideoSavedListener
        public void onVideoSaved(String videoUrl) {
            this.saved++;
            DownloadVineActivity.this.mFragment.mDownloadedVines.add(videoUrl);
            DownloadVineActivity.this.mFragment.mSelectedVines.remove(new VideoKey(videoUrl));
            DownloadVineActivity.this.mProgressDialog.setProgress(this.saved);
            if (DownloadVineActivity.this.mFragment.mSelectedVines.size() == 0) {
                DownloadVineActivity.this.mProgressDialog.dismiss();
                this.saved = 0;
                DownloadVineActivity.this.mHandler.post(new Runnable() { // from class: co.vine.android.DownloadVineActivity.VideoSavedListener.1
                    @Override // java.lang.Runnable
                    public void run() {
                        DownloadVineActivity.this.mFragment.updateSelection();
                        DownloadVineActivity.this.mFragment.mAdapter.notifyDataSetChanged();
                        DownloadVineActivity.this.showDoneDialog();
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showDoneDialog() {
        new AlertDialog.Builder(this).setTitle(getString(R.string.done_dialog)).setMessage(getString(R.string.done_dialog_msg)).setNegativeButton(getString(R.string.ok), new DialogInterface.OnClickListener() { // from class: co.vine.android.DownloadVineActivity.4
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        }).show();
    }

    @Override // android.app.Activity
    public boolean onPrepareOptionsMenu(Menu menu) {
        return false;
    }

    public void updateSelection() {
        int selectedSize = this.mFragment.mSelectedVines.size() * 2;
        this.mEstimatedSpace.setText(getString(R.string.estimate_space, new Object[]{Integer.valueOf(selectedSize), Long.valueOf(this.mAvaibleSpace)}));
        if (selectedSize >= this.mAvaibleSpace) {
            new AlertDialog.Builder(this).setMessage(R.string.not_enough_space).setPositiveButton(R.string.email_instead, new DialogInterface.OnClickListener() { // from class: co.vine.android.DownloadVineActivity.6
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    DownloadVineActivity.this.toEmail();
                }
            }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() { // from class: co.vine.android.DownloadVineActivity.5
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                }
            }).show();
        }
    }

    public void toEmail() {
        final String savedEmail = Util.getDefaultSharedPrefs(this).getString("settings_profile_email", "");
        String message = TextUtils.isEmpty(savedEmail) ? getString(R.string.email_dialog_title_no_email) : getString(R.string.email_dialog_title, new Object[]{savedEmail});
        new AlertDialog.Builder(this).setMessage(message).setPositiveButton(getString(R.string.confirm_email), new DialogInterface.OnClickListener() { // from class: co.vine.android.DownloadVineActivity.8
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int whichButton) {
                String email = savedEmail;
                DownloadVineActivity.this.mAppController.requestDownloadEmail(email);
            }
        }).setNegativeButton(R.string.go_to_settings, new DialogInterface.OnClickListener() { // from class: co.vine.android.DownloadVineActivity.7
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int whichButton) {
                Intent settingIntent = new Intent(this, (Class<?>) SettingsActivity.class);
                DownloadVineActivity.this.startActivity(settingIntent);
            }
        }).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showEmailConfirm() {
        new AlertDialog.Builder(this).setMessage(R.string.email_confirm).setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() { // from class: co.vine.android.DownloadVineActivity.9
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        }).show();
    }

    public long getAvailableStorage() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        long bytesAvailable = stat.getBlockSize() * stat.getAvailableBlocks();
        return bytesAvailable / 1048576;
    }
}
