package co.vine.android.share.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import co.vine.android.R;
import co.vine.android.api.VineChannel;
import co.vine.android.cache.image.ImageKey;
import co.vine.android.cache.image.UrlImage;
import co.vine.android.client.AppController;
import co.vine.android.drawable.RecyclableBitmapDrawable;
import co.vine.android.util.ColorUtils;
import co.vine.android.widget.TypefacesTextView;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public final class ChannelsAdapter extends BaseAdapter {
    private final Context mContext;
    private final LayoutInflater mInflater;
    private final List<VineChannel> mChannels = new ArrayList();
    private final List<WeakReference<ViewGroup>> mViews = new ArrayList();

    public ChannelsAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.mChannels.size();
    }

    @Override // android.widget.Adapter
    public Object getItem(int position) {
        return this.mChannels.get(position);
    }

    @Override // android.widget.Adapter
    public long getItemId(int position) {
        return position;
    }

    @Override // android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) throws Resources.NotFoundException {
        ViewGroup view;
        if (convertView == null) {
            view = (ViewGroup) this.mInflater.inflate(R.layout.channel_row_standalone, parent, false);
            this.mViews.add(new WeakReference<>(view));
            VineChannelRowHolder row = new VineChannelRowHolder(view);
            view.setTag(row);
        } else {
            view = (ViewGroup) convertView;
        }
        VineChannel channel = (VineChannel) getItem(position);
        VineChannelRowHolder row2 = (VineChannelRowHolder) view.getTag();
        row2.bind(channel);
        return view;
    }

    public void replaceData(List<VineChannel> channels) {
        this.mChannels.clear();
        this.mChannels.addAll(channels);
        notifyDataSetChanged();
    }

    public void updateImages(Map<ImageKey, UrlImage> images) throws Resources.NotFoundException {
        List<WeakReference<ViewGroup>> viewReferencesToRemove = new ArrayList<>();
        for (WeakReference<ViewGroup> viewReference : this.mViews) {
            ViewGroup view = viewReference.get();
            if (view == null) {
                viewReferencesToRemove.add(viewReference);
            } else {
                VineChannelRowHolder row = (VineChannelRowHolder) view.getTag();
                if (row != null) {
                    row.updateIconIfNeeded(images);
                }
            }
        }
        this.mViews.removeAll(viewReferencesToRemove);
    }

    private final class VineChannelRowHolder {
        private VineChannel mChannel;
        private final ImageView mIcon;
        private final TypefacesTextView mLabel;
        private ImageKey mWaitingOnIconImageKey;

        private VineChannelRowHolder(ViewGroup rootView) {
            this.mIcon = (ImageView) rootView.findViewById(R.id.channel_icon);
            this.mLabel = (TypefacesTextView) rootView.findViewById(R.id.channel_label);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void bind(VineChannel channel) throws Resources.NotFoundException {
            ImageKey iconImageKey;
            this.mChannel = channel;
            Bitmap iconBitmap = null;
            if (!TextUtils.isEmpty(channel.iconFullUrl) && (iconBitmap = AppController.getInstance(ChannelsAdapter.this.mContext).getPhotoBitmap((iconImageKey = new ImageKey(channel.iconFullUrl)))) == null) {
                this.mWaitingOnIconImageKey = iconImageKey;
            }
            int vineGreen = ChannelsAdapter.this.mContext.getResources().getColor(R.color.vine_green);
            int channelColorHex = ColorUtils.parseColorHex(channel.backgroundColor, vineGreen);
            setIcon(iconBitmap, channelColorHex);
            this.mLabel.setText(channel.channel);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateIconIfNeeded(Map<ImageKey, UrlImage> images) throws Resources.NotFoundException {
            int channelColorHex;
            ImageKey iconImageKey = this.mWaitingOnIconImageKey;
            if (iconImageKey != null && images.containsKey(iconImageKey)) {
                this.mWaitingOnIconImageKey = null;
                Bitmap bitmap = images.get(iconImageKey).bitmap;
                VineChannel channel = this.mChannel;
                int vineGreen = ChannelsAdapter.this.mContext.getResources().getColor(R.color.vine_green);
                if (channel != null) {
                    channelColorHex = ColorUtils.parseColorHex(this.mChannel.backgroundColor, vineGreen);
                } else {
                    channelColorHex = vineGreen;
                }
                setIcon(bitmap, channelColorHex);
            }
        }

        private void setIcon(Bitmap bitmap, int color) {
            if (bitmap != null) {
                this.mIcon.setImageDrawable(new RecyclableBitmapDrawable(ChannelsAdapter.this.mContext.getResources(), bitmap));
                if (color != 0) {
                    this.mIcon.setBackground(new ColorDrawable(color));
                    return;
                }
                return;
            }
            this.mIcon.setImageBitmap(null);
            this.mIcon.setBackgroundColor(ChannelsAdapter.this.mContext.getResources().getColor(R.color.solid_light_gray));
        }
    }
}
