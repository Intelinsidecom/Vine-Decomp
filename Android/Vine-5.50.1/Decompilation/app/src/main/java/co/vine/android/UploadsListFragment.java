package co.vine.android;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import co.vine.android.BaseAdapterFragment;
import co.vine.android.api.VineUpload;
import co.vine.android.provider.VineUploads;
import co.vine.android.provider.VineUploadsDatabaseSQL;
import co.vine.android.recorder.ProgressView;
import co.vine.android.recorder.RecordSessionVersion;
import co.vine.android.service.VineUploadService;
import co.vine.android.util.Util;
import co.vine.android.widgets.PromptDialogSupportFragment;
import com.edisonwang.android.slog.SLog;
import java.io.IOException;

/* loaded from: classes.dex */
public class UploadsListFragment extends BaseCursorListFragment implements View.OnClickListener, AdapterView.OnItemLongClickListener {
    private boolean mBound;
    private ServiceConnection mConnection;
    private Messenger mIncomingMessenger;
    private String mPath;
    private ProgressView mProgressView;
    private int mProgressViewWidth = 0;
    private String mReference;
    private ImageView mRefreshIcon;
    private Messenger mServiceMessenger;
    private TextView mStatusMessage;
    private RecordSessionVersion[] mVersions;

    @Override // co.vine.android.BaseCursorListFragment, co.vine.android.BaseAdapterFragment, co.vine.android.BaseControllerFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mIncomingMessenger = new Messenger(new UploadProgressHandler());
        this.mBound = false;
        this.mConnection = new ServiceConnection() { // from class: co.vine.android.UploadsListFragment.1
            @Override // android.content.ServiceConnection
            public void onServiceConnected(ComponentName name, IBinder service) throws RemoteException {
                SLog.dWithTag("UploadsListFragment", "Bind VineUploadService successful!");
                UploadsListFragment.this.mServiceMessenger = new Messenger(service);
                UploadsListFragment.this.mBound = true;
                Message subscribe = Message.obtain((Handler) null, 1);
                subscribe.replyTo = UploadsListFragment.this.mIncomingMessenger;
                UploadsListFragment.this.sendMessage(subscribe);
            }

            @Override // android.content.ServiceConnection
            public void onServiceDisconnected(ComponentName name) {
                SLog.dWithTag("UploadsListFragment", "Connection to VineUploadService lost unexpectedly!");
                UploadsListFragment.this.mServiceMessenger = null;
                UploadsListFragment.this.mBound = false;
            }
        };
    }

    private class UploadProgressHandler extends Handler {
        private UploadProgressHandler() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            int what = msg.what;
            Bundle data = (Bundle) msg.obj;
            SLog.dWithTag("UploadsListFragment", "Message received, what=" + what);
            switch (what) {
                case 6:
                    if (UploadsListFragment.this.mStatusMessage != null && UploadsListFragment.this.mProgressView != null) {
                        double p = data.getDouble("upload_progress");
                        SLog.dWithTag("UploadsListFragment", "Upload progress changed to p=" + p);
                        if (UploadsListFragment.this.mProgressViewWidth == 0) {
                            UploadsListFragment.this.mProgressViewWidth = UploadsListFragment.this.mProgressView.getMeasuredWidth();
                        }
                        UploadsListFragment.this.mProgressView.setProgressRatio((float) ((0.98d * p) / 100.0d));
                        UploadsListFragment.this.mStatusMessage.setText(UploadsListFragment.this.getString(R.string.upload_status_uploading));
                        break;
                    }
                    break;
                case 7:
                    boolean success = data.getBoolean("success");
                    SLog.dWithTag("UploadsListFragment", "Post completed, success=" + success);
                    sendMessage(Message.obtain((Handler) null, 2));
                    if (success) {
                        UploadsListFragment.this.mStatusMessage.setText(UploadsListFragment.this.getString(R.string.upload_status_succeeded));
                        break;
                    } else {
                        UploadsListFragment.this.resetFailedUpload();
                        break;
                    }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetFailedUpload() {
        if (this.mStatusMessage != null && this.mRefreshIcon != null && this.mProgressView != null) {
            this.mStatusMessage.setText(getString(R.string.upload_status_failed));
            this.mRefreshIcon.setVisibility(0);
            this.mProgressView.setProgressRatio(0.0f);
        }
    }

    @Override // android.support.v4.app.Fragment
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentActivity activity = getActivity();
        if (this.mCursorAdapter == null) {
            this.mCursorAdapter = new UploadsAdapter(activity, this.mAppController, 0);
        }
        try {
            this.mVersions = RecordSessionVersion.getValuesWithManagers(activity);
        } catch (IOException e) {
            Util.showCenteredToast(activity, R.string.storage_not_ready);
            activity.finish();
        }
        View header = LayoutInflater.from(activity).inflate(R.layout.upload_header_view, (ViewGroup) this.mListView, false);
        TextView headerTitle = (TextView) header.findViewById(R.id.title);
        headerTitle.setText(R.string.upload_header_title);
        ListView listview = this.mListView;
        listview.addHeaderView(header, null, false);
        listview.setBackgroundColor(getResources().getColor(R.color.uploads_list_bg));
        listview.setDividerHeight(0);
        listview.setOnItemLongClickListener(this);
        listview.setAdapter((ListAdapter) this.mCursorAdapter);
    }

    @Override // co.vine.android.BaseCursorListFragment, co.vine.android.BaseAdapterFragment, co.vine.android.BaseControllerFragment, co.vine.android.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        if (this.mCursorAdapter.getCursor() == null) {
            initLoader();
        }
        if (!this.mBound) {
            SLog.dWithTag("UploadsListFragment", "Will bind to VineUploadService now");
            Intent intent = new Intent(getActivity(), (Class<?>) VineUploadService.class);
            getActivity().bindService(intent, this.mConnection, 1);
        }
    }

    private class UploadListPendingRequestHelper extends BaseAdapterFragment.BasePendingRequestHelper {
        private UploadListPendingRequestHelper() {
            super();
        }

        @Override // co.vine.android.PendingRequestHelper
        public void handleFailedCaptchaRequest(PendingCaptchaRequest pendingCaptchaRequest) {
            View currentRow;
            if (UploadsListFragment.this.mListView != null && UploadsListFragment.this.mListView.getChildCount() > UploadsListFragment.this.mListView.getHeaderViewsCount() && (currentRow = UploadsListFragment.this.mListView.getChildAt(UploadsListFragment.this.mListView.getHeaderViewsCount())) != null) {
                ProgressBar loading = (ProgressBar) currentRow.findViewById(R.id.loading);
                if (loading != null) {
                    loading.setVisibility(8);
                }
                ImageView retry = (ImageView) currentRow.findViewById(R.id.retry);
                if (retry != null) {
                    retry.setVisibility(0);
                }
            }
        }

        @Override // co.vine.android.PendingRequestHelper
        public void handleRetryCaptchaRequest(PendingCaptchaRequest pendingCaptchaRequest) {
            Cursor c = UploadsListFragment.this.mCursorAdapter.getCursor();
            if (c.moveToFirst()) {
                UploadsListFragment.this.handleRetryPost(c, true);
            }
        }
    }

    @Override // co.vine.android.BaseAdapterFragment
    protected PendingRequestHelper createPendingRequestHelper() {
        return new UploadListPendingRequestHelper();
    }

    @Override // co.vine.android.BaseCursorListFragment, co.vine.android.BaseAdapterFragment, co.vine.android.BaseControllerFragment, android.support.v4.app.Fragment
    public void onPause() throws RemoteException {
        super.onPause();
        if (this.mBound) {
            SLog.dWithTag("UploadsListFragment", "Will unbind from VineUploadService now");
            sendMessage(Message.obtain((Handler) null, 2));
            getActivity().unbindService(this.mConnection);
            this.mBound = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendMessage(Message msg) throws RemoteException {
        try {
            this.mServiceMessenger.send(msg);
        } catch (RemoteException e) {
        }
    }

    @Override // co.vine.android.BaseCursorAdapterFragment, android.support.v4.app.LoaderManager.LoaderCallbacks
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] selArgs = {String.valueOf(2), "0"};
        return new CursorLoader(getActivity(), VineUploads.Uploads.CONTENT_URI, VineUploadsDatabaseSQL.UploadsQuery.PROJECTION, "status=? AND is_private=?", selArgs, "_id ASC");
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // co.vine.android.BaseCursorAdapterFragment, android.support.v4.app.LoaderManager.LoaderCallbacks
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        super.onLoadFinished(loader, cursor);
        hideProgress(3);
        if (isEmpty()) {
            showSadface(true, false);
            getActivity().startService(VineUploadService.getClearNotificationsIntent(getActivity()));
        } else {
            showSadface(false);
        }
    }

    @Override // co.vine.android.BaseCursorListFragment
    public void onListItemClick(ListView l, View v, int position, long id) {
        Cursor cursor = this.mCursorAdapter.getCursor();
        int uploadPosition = position - l.getHeaderViewsCount();
        if (uploadPosition == 0 && cursor.moveToFirst()) {
            this.mPath = cursor.getString(1);
            this.mProgressView = (ProgressView) v.findViewById(R.id.progress_view);
            ImageView retry = (ImageView) v.findViewById(R.id.retry);
            retry.setVisibility(8);
            this.mRefreshIcon = retry;
            TextView statusMessage = (TextView) v.findViewById(R.id.status_message);
            statusMessage.setText(getString(R.string.uploading).toUpperCase());
            this.mStatusMessage = statusMessage;
            handleRetryPost(cursor, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleRetryPost(Cursor cursor, boolean ignoreCaptcha) {
        this.mPath = cursor.getString(1);
        int status = cursor.getInt(3);
        VineUpload upload = VineUpload.fromCursor(this.mPath, cursor);
        if (status == 0) {
            Intent intent = VineUploadService.getUploadIntent(getActivity(), upload.path, upload.thumbnailPath, upload.reference, false, -1L);
            getActivity().startService(intent);
        } else if (TextUtils.isEmpty(upload.captchaUrl) || ignoreCaptcha) {
            Intent intent2 = VineUploadService.getPostIntent(getActivity(), upload, true);
            getActivity().startService(intent2);
        } else {
            this.mPendingRequestHelper.onCaptchaRequired(getActivity(), this.mPath, 0, null, upload.captchaUrl);
        }
    }

    @Override // android.widget.AdapterView.OnItemLongClickListener
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Cursor cursor = this.mCursorAdapter.getCursor();
        if (!cursor.moveToPosition(position - ((ListView) parent).getHeaderViewsCount())) {
            return false;
        }
        this.mPath = cursor.getString(1);
        this.mReference = cursor.getString(11);
        PromptDialogSupportFragment promptDialogFragment = PromptDialogSupportFragment.newInstance(1);
        promptDialogFragment.setListener(new PromptDialogSupportFragment.OnDialogDoneListener() { // from class: co.vine.android.UploadsListFragment.2
            @Override // co.vine.android.widgets.PromptDialogFragment.OnDialogDoneListener
            public void onDialogDone(DialogInterface dialog, int id2, int which) {
                switch (which) {
                    case -1:
                        Context context = UploadsListFragment.this.getActivity().getApplicationContext();
                        try {
                            if (!TextUtils.isEmpty(UploadsListFragment.this.mReference)) {
                                RecordSessionVersion.deleteSessionWithName(context, UploadsListFragment.this.mReference);
                            }
                        } catch (IOException e) {
                            SLog.e("Failed to delete session.");
                        }
                        UploadsListFragment.this.getActivity().startService(VineUploadService.getDiscardIntent(context, UploadsListFragment.this.mPath));
                        break;
                }
            }
        });
        promptDialogFragment.setMessage(R.string.delete_confirm_pending);
        promptDialogFragment.setPositiveButton(R.string.yes);
        promptDialogFragment.setNegativeButton(R.string.cancel);
        promptDialogFragment.show(getChildFragmentManager());
        return true;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
    }

    @Override // co.vine.android.BaseCursorListFragment, android.support.v4.app.Fragment
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!this.mPendingRequestHelper.onActivityResult(requestCode, resultCode, data) && resultCode == 0) {
            resetFailedUpload();
        }
    }
}
