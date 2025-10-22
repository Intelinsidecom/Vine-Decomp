package co.vine.android;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import co.vine.android.api.VineUser;
import co.vine.android.cache.image.ImageKey;
import co.vine.android.cache.image.UrlImage;
import co.vine.android.client.AppSessionListener;
import co.vine.android.util.ImageUtils;
import co.vine.android.util.analytics.FlurryUtils;
import java.util.ArrayList;
import java.util.HashMap;

/* loaded from: classes.dex */
public class FavoritePeopleOnboardActivity extends BaseControllerActionBarActivity implements View.OnClickListener {
    private ViewGroup mFaceCarouselLayout;
    private int mImagesToShow;
    private ProgressBar mProgressBar;
    private int mReadyFaces;
    private int mStrokeWidth;
    private ArrayList<VineUser> mUsers;
    private HashMap<Long, ImageKey> mImageKeys = new HashMap<>();
    private ArrayList<ImageView> mImageViews = new ArrayList<>();
    private int[] mProfileColorIds = {R.color.face_overlay_1, R.color.face_overlay_2, R.color.face_overlay_3, R.color.face_overlay_4, R.color.face_overlay_5, R.color.face_overlay_6};
    private View.OnClickListener faceTapListener = new View.OnClickListener() { // from class: co.vine.android.FavoritePeopleOnboardActivity.1
        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            FavoritePeopleOnboardActivity.this.pickNowAndDismiss();
        }
    };

    @Override // co.vine.android.BaseActionBarActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityDonut, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) throws Resources.NotFoundException {
        Bitmap bm;
        super.onCreate(savedInstanceState, R.layout.favorites_onboard, true, true);
        hideActionBar();
        this.mAppSessionListener = new FavoritePeopleOnboardListener();
        this.mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        this.mFaceCarouselLayout = (ViewGroup) findViewById(R.id.face_carousel_layout);
        Intent intent = getIntent();
        if (intent != null) {
            this.mUsers = intent.getParcelableArrayListExtra("users");
            if (this.mUsers != null) {
                this.mStrokeWidth = getResources().getDimensionPixelSize(R.dimen.favorite_face_stroke_width);
                int width = getResources().getDimensionPixelSize(R.dimen.favorite_face_width);
                int[] imageViewIds = {R.id.face_1, R.id.face_2, R.id.face_3, R.id.face_4, R.id.face_5, R.id.face_6};
                this.mImagesToShow = Math.min(this.mUsers.size(), imageViewIds.length);
                this.mReadyFaces = 0;
                for (int i = 0; i < this.mImagesToShow; i++) {
                    this.mImageViews.add((ImageView) findViewById(imageViewIds[i]));
                    VineUser user = this.mUsers.get(i);
                    ImageKey key = new ImageKey(user.avatarUrl, width, width, true);
                    if (!TextUtils.isEmpty(user.avatarUrl) && (bm = this.mAppController.getPhotoBitmap(key)) != null) {
                        setImage(i, bm);
                    }
                    this.mImageKeys.put(Long.valueOf(user.userId), key);
                }
                if (this.mReadyFaces == 0) {
                    this.mProgressBar.setVisibility(0);
                    this.mFaceCarouselLayout.setVisibility(8);
                } else {
                    this.mProgressBar.setVisibility(8);
                    this.mFaceCarouselLayout.setVisibility(0);
                }
                FlurryUtils.trackOnboardingShown("favs");
            }
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.pick_now) {
            pickNowAndDismiss();
        } else if (id == R.id.no_thanks) {
            FlurryUtils.trackOnboardingDismissed("no_thanks");
            finish();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setImage(int index, Bitmap bm) throws Resources.NotFoundException {
        ImageView imageView = this.mImageViews.get(index);
        int colorId = this.mProfileColorIds[index % this.mProfileColorIds.length];
        int color = getResources().getColor(colorId);
        Bitmap image = ImageUtils.desaturateWithColorFilter(bm.copy(bm.getConfig(), true), 0.0f, color);
        imageView.setImageBitmap(ImageUtils.addStroke(-1, this.mStrokeWidth, image));
        imageView.setVisibility(0);
        imageView.setOnClickListener(this.faceTapListener);
        this.mReadyFaces++;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void pickNowAndDismiss() {
        FavoritePeopleActivity.start(this, 0);
        FlurryUtils.trackOnboardingDismissed("pick_now");
        finish();
    }

    public static void start(Context context, ArrayList<VineUser> users) {
        Intent intent = new Intent(context, (Class<?>) FavoritePeopleOnboardActivity.class);
        intent.putParcelableArrayListExtra("users", users);
        context.startActivity(intent);
    }

    private class FavoritePeopleOnboardListener extends AppSessionListener {
        private FavoritePeopleOnboardListener() {
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onPhotoImageLoaded(HashMap<ImageKey, UrlImage> images) throws Resources.NotFoundException {
            for (int i = 0; i < FavoritePeopleOnboardActivity.this.mImagesToShow; i++) {
                UrlImage image = images.get(FavoritePeopleOnboardActivity.this.mImageKeys.get(Long.valueOf(((VineUser) FavoritePeopleOnboardActivity.this.mUsers.get(i)).userId)));
                if (image != null && image.isValid()) {
                    FavoritePeopleOnboardActivity.this.setImage(i, image.bitmap);
                }
            }
            if (FavoritePeopleOnboardActivity.this.mReadyFaces > 0 && FavoritePeopleOnboardActivity.this.mProgressBar != null && FavoritePeopleOnboardActivity.this.mFaceCarouselLayout != null) {
                FavoritePeopleOnboardActivity.this.mProgressBar.setVisibility(8);
                FavoritePeopleOnboardActivity.this.mFaceCarouselLayout.setVisibility(0);
            }
        }
    }

    @Override // android.app.Activity
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_from_bottom, R.anim.slide_out_to_bottom);
    }
}
