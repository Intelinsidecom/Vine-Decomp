package co.vine.android;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;
import co.vine.android.util.CommonUtil;
import co.vine.android.util.ImageUtils;
import com.edisonwang.android.slog.SLog;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/* loaded from: classes.dex */
public class ImagePicker {
    private long mActiveUserId;
    private Activity mActivity;
    private Listener mListener;
    private Uri mSavedImageUri;

    public interface Listener {
        void setAvatarUrl(Uri uri);
    }

    public ImagePicker(Activity activity, Listener listener, long activeUserId) {
        this.mActivity = activity;
        this.mActiveUserId = activeUserId;
        this.mListener = listener;
    }

    public void captureImage(int requestCode) {
        File profileFile = ImageUtils.getTempPic(this.mActivity, false, this.mActiveUserId);
        if (profileFile == null || !ImageUtils.hasEnoughExternalStorageForNewPhoto(this.mActivity)) {
            Toast.makeText(this.mActivity, R.string.camera_photo_error, 0).show();
            return;
        }
        String profile = this.mActivity.getString(R.string.profile_title);
        ContentValues values = new ContentValues();
        values.put("title", profile);
        values.put("description", profile);
        Uri uri = Uri.fromFile(profileFile);
        this.mListener.setAvatarUrl(uri);
        Intent i = new Intent("android.media.action.IMAGE_CAPTURE").putExtra("output", uri);
        try {
            this.mActivity.startActivityForResult(i, requestCode);
        } catch (ActivityNotFoundException e) {
            CommonUtil.showCenteredToast(this.mActivity, R.string.unsupported_feature);
            ImageUtils.deleteTempPic(uri);
            this.mListener.setAvatarUrl(null);
        }
    }

    public void chooseImage(int requestCode) {
        Intent intent = new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        try {
            this.mActivity.startActivityForResult(intent, requestCode);
        } catch (ActivityNotFoundException e) {
            CommonUtil.showCenteredToast(this.mActivity, R.string.unsupported_feature);
        }
    }

    public Uri getSavedImageUri() {
        return this.mSavedImageUri;
    }

    public void saveProfileImage(Bitmap bm) throws IOException {
        File f = new File(CommonUtil.getExternalCacheDir(this.mActivity), "twitter_profile.jpg");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] compressed = bos.toByteArray();
        try {
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(compressed);
            fos.close();
            this.mSavedImageUri = Uri.fromFile(f);
            SLog.d("Pending Uri for profile photo is {}.", this.mSavedImageUri);
            this.mListener.setAvatarUrl(this.mSavedImageUri);
        } catch (Exception e) {
            if (this.mActivity != null) {
                this.mActivity.runOnUiThread(new Runnable() { // from class: co.vine.android.ImagePicker.1
                    @Override // java.lang.Runnable
                    public void run() {
                        Toast.makeText(ImagePicker.this.mActivity, R.string.profile_image_save_error, 0).show();
                    }
                });
            }
            this.mListener.setAvatarUrl(null);
        }
    }
}
