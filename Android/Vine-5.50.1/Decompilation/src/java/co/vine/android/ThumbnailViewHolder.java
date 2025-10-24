package co.vine.android;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import co.vine.android.views.SdkProgressBar;

/* loaded from: classes.dex */
public class ThumbnailViewHolder extends RecyclerView.ViewHolder {
    private View mDownloaded;
    private TextView mDurationText;
    private ImageView mImageView;
    private TextView mOrder;
    private RelativeLayout mRemixDisabledOverlay;
    private SdkProgressBar mSdkProgressBar;
    private View mSelected;
    private View mSelectedOverlay;

    public ThumbnailViewHolder(View v) {
        super(v);
        this.mSdkProgressBar = (SdkProgressBar) v.findViewById(R.id.thumbnail_load_image);
        this.mImageView = (ImageView) v.findViewById(R.id.thumbnail);
        this.mDurationText = (TextView) v.findViewById(R.id.duration);
        this.mSelected = v.findViewById(R.id.selected);
        this.mOrder = (TextView) v.findViewById(R.id.order);
        this.mRemixDisabledOverlay = (RelativeLayout) v.findViewById(R.id.remix_disabled_overlay);
        this.mDownloaded = v.findViewById(R.id.downloaded);
        this.mSelectedOverlay = v.findViewById(R.id.selected_overlay);
    }

    public ImageView getImageView() {
        return this.mImageView;
    }

    public void setSelectedOrder(int videoOrder) {
        if (videoOrder != -1) {
            this.mSelected.setVisibility(0);
            this.mSelectedOverlay.setVisibility(0);
            this.mOrder.setVisibility(8);
        } else {
            this.mSelected.setVisibility(4);
            this.mSelectedOverlay.setVisibility(4);
            this.mOrder.setVisibility(8);
        }
    }

    public void setSelected(boolean selected) {
        if (selected) {
            this.mSelected.setVisibility(0);
            this.mSelectedOverlay.setVisibility(0);
            this.mDownloaded.setVisibility(8);
        } else {
            this.mSelected.setVisibility(8);
            this.mSelectedOverlay.setVisibility(8);
            this.mDownloaded.setVisibility(8);
        }
    }

    public void setDownloaded(boolean downloaded) {
        if (downloaded) {
            this.mDownloaded.setVisibility(0);
            this.mSelectedOverlay.setVisibility(8);
            this.mSelected.setVisibility(8);
        }
    }

    public void setDuration(String videoDuration) {
        this.mDurationText.setText(videoDuration);
    }

    public RelativeLayout getRemixDisabledOverlay() {
        return this.mRemixDisabledOverlay;
    }
}
