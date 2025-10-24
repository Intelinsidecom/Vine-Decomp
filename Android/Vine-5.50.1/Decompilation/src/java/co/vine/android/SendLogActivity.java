package co.vine.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import com.edisonwang.android.slog.MessageFormatter;
import com.edisonwang.android.slog.SLog;
import com.google.ads.AdRequest;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
public class SendLogActivity extends Activity {
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private String mAdditionalInfo;
    private CollectLogTask mCollectLogTask;
    private String mFormat;
    private AlertDialog mMainDialog;
    private ProgressDialog mProgressDialog;
    private Intent mSendIntent;

    @Override // android.app.Activity
    public void onCreate(Bundle savedInstanceState) throws PackageManager.NameNotFoundException {
        super.onCreate(savedInstanceState);
        this.mSendIntent = null;
        PackageInfo packagInfo = null;
        try {
            packagInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
        }
        this.mSendIntent = new Intent("android.intent.action.SEND");
        this.mSendIntent.putExtra("android.intent.extra.EMAIL", new String[]{"vine-bugs-android@twitter.com"});
        Intent intent = this.mSendIntent;
        Object[] objArr = new Object[3];
        objArr[0] = packagInfo == null ? "Unknown Version" : packagInfo.versionName + " (" + packagInfo.versionCode + ")";
        objArr[1] = packagInfo == null ? AdRequest.VERSION : packagInfo.versionName;
        objArr[2] = packagInfo == null ? "0" : Integer.valueOf(packagInfo.versionCode);
        intent.putExtra("android.intent.extra.SUBJECT", MessageFormatter.format("#summary=\"[BUG REPORT] {}\" #affectsVersions={} #build={} #labels=created-via-email", objArr).getMessage());
        this.mSendIntent.setType("text/plain");
        this.mAdditionalInfo = MessageFormatter.format("Thanks for filing a bug!\nSummary:\nSteps to reproduce:\nExpected results:\nActual results:\nModel: {}, \n Release: {}, \n Kernel: {}, \n Display: {}. \n", new Object[]{Build.MODEL, Build.VERSION.RELEASE, getFormattedKernelVersion(), Build.DISPLAY}).getMessage();
        this.mFormat = "time";
        collectAndSendLog();
    }

    void collectAndSendLog() {
        ArrayList<String> list = new ArrayList<>();
        if (this.mFormat != null) {
            list.add("-v");
            list.add(this.mFormat);
        }
        this.mCollectLogTask = (CollectLogTask) new CollectLogTask().execute(list);
    }

    private class CollectLogTask extends AsyncTask<ArrayList<String>, Void, File> {
        private CollectLogTask() {
        }

        @Override // android.os.AsyncTask
        protected void onPreExecute() {
            SendLogActivity.this.showProgressDialog("Getting the log");
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public File doInBackground(ArrayList<String>... params) throws IOException {
            File file = new File(SendLogActivity.this.getExternalCacheDir(), "vine_log.txt");
            try {
                OutputStreamWriter stream = new OutputStreamWriter(new FileOutputStream(file, true));
                ArrayList<String> commandLine = new ArrayList<>();
                commandLine.add("logcat");
                commandLine.add("-d");
                ArrayList<String> arguments = (params == null || params.length <= 0) ? null : params[0];
                if (arguments != null) {
                    commandLine.addAll(arguments);
                }
                Process process = Runtime.getRuntime().exec((String[]) commandLine.toArray(new String[commandLine.size()]));
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                while (true) {
                    String line = bufferedReader.readLine();
                    if (line != null) {
                        stream.write(line);
                        stream.write(SendLogActivity.LINE_SEPARATOR);
                    } else {
                        stream.flush();
                        stream.close();
                        return file;
                    }
                }
            } catch (IOException e) {
                SLog.e("CollectLogTask.doInBackground failed.", (Throwable) e);
                return null;
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(File file) {
            if (file == null) {
                SendLogActivity.this.dismissProgressDialog();
                SendLogActivity.this.showErrorDialog("Error getting the log");
                return;
            }
            StringBuilder log = new StringBuilder();
            if (SendLogActivity.this.mAdditionalInfo != null) {
                log.insert(0, SendLogActivity.LINE_SEPARATOR);
                log.insert(0, SendLogActivity.this.mAdditionalInfo);
            }
            SendLogActivity.this.mSendIntent.putExtra("android.intent.extra.STREAM", Uri.fromFile(file));
            SendLogActivity.this.mSendIntent.putExtra("android.intent.extra.TEXT", log.toString());
            SendLogActivity.this.startActivity(Intent.createChooser(SendLogActivity.this.mSendIntent, "Send"));
            SendLogActivity.this.dismissProgressDialog();
            SendLogActivity.this.dismissMainDialog();
            SendLogActivity.this.finish();
        }
    }

    void showErrorDialog(String errorMessage) {
        new AlertDialog.Builder(this).setTitle("Vine").setMessage(errorMessage).setIcon(android.R.drawable.ic_dialog_alert).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { // from class: co.vine.android.SendLogActivity.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int whichButton) {
                SendLogActivity.this.finish();
            }
        }).show();
    }

    void dismissMainDialog() {
        if (this.mMainDialog != null && this.mMainDialog.isShowing()) {
            this.mMainDialog.dismiss();
            this.mMainDialog = null;
        }
    }

    void showProgressDialog(String message) {
        this.mProgressDialog = new ProgressDialog(this);
        this.mProgressDialog.setIndeterminate(true);
        this.mProgressDialog.setMessage(message);
        this.mProgressDialog.setCancelable(true);
        this.mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: co.vine.android.SendLogActivity.2
            @Override // android.content.DialogInterface.OnCancelListener
            public void onCancel(DialogInterface dialog) {
                SendLogActivity.this.cancellCollectTask();
                SendLogActivity.this.finish();
            }
        });
        this.mProgressDialog.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dismissProgressDialog() {
        if (this.mProgressDialog != null && this.mProgressDialog.isShowing()) {
            this.mProgressDialog.dismiss();
            this.mProgressDialog = null;
        }
    }

    void cancellCollectTask() {
        if (this.mCollectLogTask != null && this.mCollectLogTask.getStatus() == AsyncTask.Status.RUNNING) {
            this.mCollectLogTask.cancel(true);
            this.mCollectLogTask = null;
        }
    }

    @Override // android.app.Activity
    protected void onPause() {
        cancellCollectTask();
        dismissProgressDialog();
        dismissMainDialog();
        super.onPause();
    }

    private String getFormattedKernelVersion() throws IOException {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("/proc/version"), 256);
            try {
                String procVersionStr = reader.readLine();
                reader.close();
                Pattern p = Pattern.compile("\\w+\\s+\\w+\\s+([^\\s]+)\\s+\\(([^\\s@]+(?:@[^\\s.]+)?)[^)]*\\)\\s+\\([^)]+\\)\\s+([^\\s]+)\\s+(?:PREEMPT\\s+)?(.+)");
                Matcher m = p.matcher(procVersionStr);
                if (!m.matches()) {
                    SLog.e("Regex did not match on /proc/version: " + procVersionStr);
                    return "Unavailable";
                }
                if (m.groupCount() < 4) {
                    SLog.e("Regex match on /proc/version only returned " + m.groupCount() + " groups");
                    return "Unavailable";
                }
                return m.group(1) + "\n" + m.group(2) + " " + m.group(3) + "\n" + m.group(4);
            } catch (Throwable th) {
                reader.close();
                throw th;
            }
        } catch (IOException e) {
            SLog.e("IO Exception when getting kernel version for Device Info screen", (Throwable) e);
            return "Unavailable";
        }
    }
}
