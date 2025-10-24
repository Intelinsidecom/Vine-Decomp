package co.vine.android;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import co.vine.android.client.AppController;
import co.vine.android.util.ImageUtils;
import co.vine.android.widget.CroppableImageView;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import twitter4j.internal.http.HttpResponseCode;

/* loaded from: classes.dex */
public class CropActivity extends BaseControllerActionBarActivity implements DialogInterface.OnCancelListener {
    int mBitmapHeight;
    int mBitmapHeightOffset;
    protected boolean mBitmapLoaded;
    int mBitmapWidth;
    int mBitmapWidthOffset;
    protected CropImageTask mCropTask;
    private int mCurrDegrees;
    int mFileToViewScale;
    protected Uri mUri;
    protected CroppableImageView mView;

    protected void onCreate(Bundle savedInstanceState, int layout) {
        super.onCreate(savedInstanceState, layout, false);
        this.mUri = (Uri) getIntent().getParcelableExtra("uri");
        this.mView = (CroppableImageView) findViewById(R.id.image);
        this.mView.setVisibility(4);
        if (savedInstanceState != null) {
            this.mCurrDegrees = savedInstanceState.getInt("degrees", 0);
        } else {
            this.mCurrDegrees = 0;
        }
        this.mCropTask = (CropImageTask) getLastCustomNonConfigurationInstance();
        if (this.mCropTask == null) {
            new LoadImageTask(this).execute(new Void[0]);
        } else {
            this.mCropTask.attachActivity(this);
        }
    }

    @Override // co.vine.android.BaseActionBarActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("degrees", this.mCurrDegrees);
    }

    @Override // android.support.v4.app.FragmentActivity
    public Object onRetainCustomNonConfigurationInstance() {
        if (this.mCropTask != null) {
            this.mCropTask.detachActivity();
        }
        return this.mCropTask;
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public boolean onSearchRequested() {
        return false;
    }

    protected void doCrop() {
        if (this.mBitmapLoaded) {
            if (this.mCropTask != null) {
                this.mCropTask.detachActivity();
            }
            this.mCropTask = new CropImageTask(this);
            this.mCropTask.execute(new Void[0]);
        }
    }

    public void onClickHandler(View view) {
        int id = view.getId();
        if (id == R.id.save_button) {
            doCrop();
        } else if (id == R.id.cancel_button) {
            setResult(0);
            finish();
        }
    }

    @Override // android.app.Activity
    protected Dialog onCreateDialog(int id) {
        ProgressDialog dialog = new ProgressDialog(this, R.style.ProgressDialogTheme);
        dialog.setProgressStyle(0);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnCancelListener(this);
        switch (id) {
            case 0:
                dialog.setMessage(getString(R.string.cropping_image));
                return dialog;
            case 1:
                dialog.setMessage(getString(R.string.loading_image));
                return dialog;
            default:
                return super.onCreateDialog(id);
        }
    }

    protected void onBitmapProcessingDone(Bitmap bmp) {
        this.mView.setVisibility(0);
        this.mView.setImageBitmap(bmp);
    }

    @Override // co.vine.android.BaseControllerActionBarActivity, co.vine.android.BaseActionBarActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onPause() {
        super.onPause();
        removeDialog(0);
        removeDialog(1);
    }

    class LoadImageTask extends AsyncTask<Void, Void, Bitmap> {
        private final WeakReference<CropActivity> mActivity;

        LoadImageTask(CropActivity activity) {
            this.mActivity = new WeakReference<>(activity);
            CropActivity.this.mBitmapLoaded = false;
        }

        @Override // android.os.AsyncTask
        protected void onPreExecute() {
            CropActivity.this.showDialog(1);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public Bitmap doInBackground(Void... voids) throws IOException {
            DisplayMetrics metrics = CropActivity.this.getResources().getDisplayMetrics();
            ImageUtils.BitmapInfo bmpInfo = ImageUtils.safeDecode(CropActivity.this, CropActivity.this.mUri, metrics.widthPixels, metrics.heightPixels, true);
            if (bmpInfo == null) {
                return null;
            }
            int scale = bmpInfo.scale;
            CropActivity.this.mBitmapWidth = bmpInfo.origWidth;
            CropActivity.this.mBitmapHeight = bmpInfo.origHeight;
            CropActivity.this.mBitmapWidthOffset = bmpInfo.origWidth % scale;
            CropActivity.this.mBitmapHeightOffset = bmpInfo.origHeight % scale;
            CropActivity.this.mFileToViewScale = scale;
            return bmpInfo.bitmap;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(Bitmap bmp) {
            CropActivity activity = this.mActivity.get();
            if (activity != null) {
                if (bmp != null) {
                    activity.mBitmapLoaded = true;
                    activity.onBitmapProcessingDone(bmp);
                } else {
                    Toast.makeText(CropActivity.this, R.string.load_image_failure, 1).show();
                    CropActivity.this.removeDialog(1);
                    CropActivity.this.setResult(0);
                    CropActivity.this.finish();
                }
                CropActivity.this.removeDialog(1);
            }
        }
    }

    public void onCropDone(boolean success, Intent intent) {
        if (success) {
            setResult(-1, intent);
        } else {
            Toast.makeText(this, R.string.cropping_image_failure, 1).show();
            setResult(0);
        }
        finish();
    }

    private static class CropImageTask extends AsyncTask<Void, Void, Boolean> {
        CropActivity mActivity;
        final int mBitmapHeight;
        final int mBitmapHeightOffset;
        final int mBitmapWidth;
        final int mBitmapWidthOffset;
        final Context mContext;
        final Rect mCropRect;
        final int mFileToViewScale;
        final Intent mIntent = new Intent();
        final Uri mUri;
        ImageView mView;
        Bitmap mViewBitmap;

        CropImageTask(CropActivity activity) {
            this.mActivity = activity;
            this.mContext = activity.getApplicationContext();
            this.mFileToViewScale = activity.mFileToViewScale;
            this.mBitmapWidthOffset = activity.mBitmapWidthOffset;
            this.mBitmapHeightOffset = activity.mBitmapHeightOffset;
            this.mBitmapHeight = activity.mBitmapHeight;
            this.mBitmapWidth = activity.mBitmapWidth;
            this.mUri = activity.mUri;
            this.mView = activity.mView;
            RectF rectF = ((CroppableImageView) this.mView).getCropRect();
            this.mCropRect = new Rect((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom);
        }

        public void attachActivity(CropActivity activity) {
            this.mActivity = activity;
        }

        public void detachActivity() {
            this.mActivity = null;
            this.mView = null;
        }

        @Override // android.os.AsyncTask
        protected void onPreExecute() {
            ImageView view = this.mView;
            if (this.mActivity != null && view != null && view.getDrawable() != null) {
                this.mViewBitmap = ((BitmapDrawable) view.getDrawable()).getBitmap();
                this.mActivity.showDialog(0);
            }
        }

        private Boolean basicCrop() throws Throwable {
            Bitmap cropped;
            Context context = this.mContext;
            Rect rect = this.mCropRect;
            Bitmap bmp = this.mViewBitmap;
            if (bmp == null) {
                return Boolean.FALSE;
            }
            Rect bitmapRect = new Rect(0, 0, bmp.getWidth(), bmp.getHeight());
            int height = rect.height();
            int width = rect.width();
            if (width <= 1 || height <= 1) {
                return Boolean.FALSE;
            }
            if (bitmapRect.contains(rect) && (cropped = ImageUtils.createBitmap(context, bmp, rect.left, rect.top, rect.width(), rect.height())) != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                cropped.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                ImageUtils.BitmapInfo bi = ImageUtils.resizeBitmap(context, baos.toByteArray(), HttpResponseCode.OK, HttpResponseCode.OK);
                if (bi != null && bi.bitmap != null) {
                    long ownerId = AppController.getInstance(context).getActiveId();
                    Uri uri = ImageUtils.writePicToFile(context, bi.bitmap, ownerId);
                    if (uri != null) {
                        int scale = this.mFileToViewScale;
                        rect.set(rect.left * scale, rect.top * scale, (rect.right - this.mBitmapWidthOffset) * scale, (rect.bottom - this.mBitmapHeightOffset) * scale);
                        this.mIntent.putExtra("cropped_rect", rect);
                        this.mIntent.putExtra("uri", uri);
                        return Boolean.TRUE;
                    }
                }
            }
            return Boolean.FALSE;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public Boolean doInBackground(Void... voids) {
            Context context = this.mContext;
            return basicCrop();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(Boolean success) {
            if (this.mActivity != null) {
                this.mActivity.removeDialog(0);
                this.mActivity.onCropDone(success.booleanValue(), this.mIntent);
            }
        }
    }

    @Override // android.content.DialogInterface.OnCancelListener
    public void onCancel(DialogInterface dialog) {
        setResult(0);
        finish();
    }
}
