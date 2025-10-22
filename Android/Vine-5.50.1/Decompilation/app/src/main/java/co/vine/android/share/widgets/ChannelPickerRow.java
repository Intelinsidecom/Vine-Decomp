package co.vine.android.share.widgets;

import android.content.Context;
import android.graphics.PorterDuff;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import co.vine.android.R;
import co.vine.android.api.VineChannel;
import co.vine.android.cache.image.ImageKey;
import co.vine.android.client.AppController;
import co.vine.android.drawable.RecyclableBitmapDrawable;
import co.vine.android.util.ViewUtil;
import co.vine.android.util.analytics.FlurryUtils;
import co.vine.android.widget.TypefacesTextView;

/* loaded from: classes.dex */
public class ChannelPickerRow extends RelativeLayout {
    private VineChannel mChannel;
    private final ImageView mHamburger;
    private final TypefacesTextView mSelectChannelLabel;
    private final ViewGroup mSelectedChannelContainer;
    private final ImageView mSelectedChannelIcon;
    private final TypefacesTextView mSelectedChannelLabel;

    public ChannelPickerRow(Context context) {
        this(context, null);
    }

    public ChannelPickerRow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChannelPickerRow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.channel_picker, this);
        this.mSelectChannelLabel = (TypefacesTextView) findViewById(R.id.select_channel_label);
        this.mSelectedChannelContainer = (ViewGroup) findViewById(R.id.selected_channel_container);
        this.mSelectedChannelIcon = (ImageView) findViewById(R.id.channel_icon);
        this.mSelectedChannelLabel = (TypefacesTextView) findViewById(R.id.channel_name);
        this.mHamburger = (ImageView) findViewById(R.id.channel_hamburger);
        clearSelectedChannel();
        ensure();
    }

    public VineChannel getSelectedChannel() {
        return this.mChannel;
    }

    public long getSelectedChannelId() {
        if (this.mChannel == null) {
            return -1L;
        }
        return this.mChannel.channelId;
    }

    public void setSelectedChannel(VineChannel channel) {
        if (channel == null) {
            clearSelectedChannel();
            return;
        }
        this.mChannel = channel;
        this.mSelectedChannelLabel.setText(channel.channel);
        ensure();
        this.mHamburger.setColorFilter(channel.colorHex, PorterDuff.Mode.SRC_IN);
        FlurryUtils.trackChannelChange(channel.channel + " (id=" + channel.channelId + ")");
    }

    public void clearSelectedChannel() {
        this.mChannel = null;
        this.mSelectedChannelLabel.setText((CharSequence) null);
        this.mHamburger.setColorFilter(getResources().getColor(R.color.black_thirty_five_percent));
        ensure();
        FlurryUtils.trackChannelChange("Channel removed");
    }

    private void ensure() {
        if (this.mChannel == null) {
            ViewUtil.disableAndHide(this.mSelectedChannelContainer);
            ViewUtil.enableAndShow(this.mSelectChannelLabel);
        } else {
            this.mSelectedChannelIcon.setImageDrawable(!TextUtils.isEmpty(this.mChannel.iconFullUrl) ? new RecyclableBitmapDrawable(getResources(), AppController.getInstance(getContext()).getPhotoBitmap(new ImageKey(this.mChannel.iconFullUrl))) : null);
            ViewUtil.disableAndHide(this.mSelectChannelLabel);
            ViewUtil.enableAndShow(this.mSelectedChannelContainer);
        }
    }
}
