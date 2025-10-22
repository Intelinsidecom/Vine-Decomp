package co.vine.android;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import co.vine.android.util.ViewUtil;

/* loaded from: classes.dex */
public class EditProfileCropActivity extends CropActivity {
    private Button mSaveButton;

    @Override // co.vine.android.BaseActionBarActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityDonut, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.crop_photo);
        this.mSaveButton = (Button) findViewById(R.id.save_button);
        this.mSaveButton.setEnabled(false);
        this.mView.setCropType(2);
        ViewUtil.reduceTextSizeViaFontScaleIfNeeded(this, 1.1f, 12.0f, (TextView) findViewById(R.id.save_button), (TextView) findViewById(R.id.cancel_button));
        setupActionBar((Boolean) null, (Boolean) true, R.string.crop_photo_title, (Boolean) null, (Boolean) false);
    }

    @Override // co.vine.android.CropActivity
    protected void onBitmapProcessingDone(Bitmap bmp) {
        super.onBitmapProcessingDone(bmp);
        this.mSaveButton.setEnabled(true);
    }
}
