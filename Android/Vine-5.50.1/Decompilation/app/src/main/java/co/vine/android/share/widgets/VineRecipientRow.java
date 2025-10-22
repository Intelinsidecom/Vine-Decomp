package co.vine.android.share.widgets;

import android.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import co.vine.android.api.VineRecipient;
import co.vine.android.cache.image.ImageKey;
import co.vine.android.cache.image.UrlImage;
import co.vine.android.client.AppController;
import co.vine.android.drawable.RecyclableBitmapDrawable;
import co.vine.android.util.ColorUtils;
import co.vine.android.util.Util;
import java.util.HashMap;

/* loaded from: classes.dex */
public final class VineRecipientRow extends VineToggleRow {
    private final int mColorBlack;
    private final int mColorVineGreen;
    private VineRecipient mRecipient;
    private ImageKey mWaitingOnAvatarImageKey;

    public VineRecipientRow(Context context) {
        this(context, null, 0);
    }

    public VineRecipientRow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VineRecipientRow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mColorBlack = getResources().getColor(R.color.black);
        this.mColorVineGreen = getResources().getColor(co.vine.android.R.color.vine_green);
    }

    public void bind(VineRecipient recipient, String name, ImageKey avatarImageKey, boolean checked) {
        this.mRecipient = recipient;
        int recipientColor = ColorUtils.ensureNotBlack((-16777216) | recipient.color, this.mColorVineGreen);
        if (avatarImageKey != null) {
            Bitmap bitmap = AppController.getInstance(getContext()).getPhotoBitmap(avatarImageKey);
            this.mIcon.clearColorFilter();
            if (bitmap != null) {
                this.mIcon.setImageDrawable(new RecyclableBitmapDrawable(getResources(), bitmap));
            } else {
                this.mWaitingOnAvatarImageKey = avatarImageKey;
                Util.safeSetDefaultAvatar(this.mIcon, Util.ProfileImageSize.MEDIUM, recipientColor);
            }
        } else {
            Util.safeSetDefaultAvatar(this.mIcon, Util.ProfileImageSize.MEDIUM, recipientColor);
        }
        this.mLabel.setText(name);
        setLabelTextColors(this.mColorBlack, recipientColor);
        setTextStyle(isEnabled(), checked);
        setToggleButtonColor(recipientColor);
        setCheckedWithoutEvent(checked);
    }

    public VineRecipient getRecipient() {
        return this.mRecipient;
    }

    public void updateAvatarIfNeeded(HashMap<ImageKey, UrlImage> images) {
        ImageKey avatarImageKey = this.mWaitingOnAvatarImageKey;
        if (avatarImageKey != null && images.containsKey(avatarImageKey)) {
            this.mWaitingOnAvatarImageKey = null;
            Bitmap bitmap = images.get(avatarImageKey).bitmap;
            if (bitmap != null) {
                this.mIcon.clearColorFilter();
                this.mIcon.setImageDrawable(new RecyclableBitmapDrawable(getResources(), bitmap));
            } else {
                Util.safeSetDefaultAvatar(this.mIcon, Util.ProfileImageSize.MEDIUM, this.mColorVineGreen);
            }
        }
    }
}
