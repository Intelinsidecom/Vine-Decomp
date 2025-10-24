package co.vine.android.feedadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import co.vine.android.R;
import co.vine.android.RemixAttributions;
import co.vine.android.api.VineAudioMetadata;
import co.vine.android.api.VineSource;
import co.vine.android.client.AppController;
import co.vine.android.util.LinkDispatcher;
import co.vine.android.util.ResourceLoader;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class AttributionAdapter extends RecyclerView.Adapter<ViewHolder> {
    private AppController mAppController;
    private Context mContext;
    private ArrayList<RemixAttributions> mList = new ArrayList<>();
    private boolean delayEnterAnimation = true;
    private int lastAnimatedPosition = -1;
    private boolean animationsLocked = false;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView author;
        public ImageView thumbnail;
        public TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            this.thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            this.title = (TextView) itemView.findViewById(R.id.title);
            this.author = (TextView) itemView.findViewById(R.id.author);
        }

        public ImageView getThumbnail() {
            return this.thumbnail;
        }

        public TextView getTitle() {
            return this.title;
        }

        public TextView getAuthor() {
            return this.author;
        }
    }

    public AttributionAdapter(Context context, ArrayList<RemixAttributions> list) {
        this.mContext = context;
        this.mList.addAll(list);
        this.mAppController = AppController.getInstance(context);
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View attributionView = inflater.inflate(R.layout.attribution_row_item, parent, false);
        return new ViewHolder(attributionView);
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public void onBindViewHolder(ViewHolder holder, int position) {
        final RemixAttributions attribution = this.mList.get(position);
        switch (attribution.getType()) {
            case 0:
                VineSource source = attribution.getVineSource();
                ResourceLoader bitmapLoader = new ResourceLoader(this.mContext, this.mAppController);
                if (source.getContentType() == 1) {
                    holder.getTitle().setText(R.string.remixed_audio);
                    bitmapLoader.setImageWhenLoaded(new ResourceLoader.ImageViewImageSetter(holder.getThumbnail()), source.getThumbnailUrl(), false, 6, 100);
                } else if (source.getContentType() == 2) {
                    holder.getTitle().setText(R.string.remixed_video);
                    bitmapLoader.setImageWhenLoaded(new ResourceLoader.ImageViewImageSetter(holder.getThumbnail()), source.getThumbnailUrl(), false, 6, 100);
                } else if (source.getContentType() == 3) {
                    holder.getTitle().setText(R.string.sound_board);
                    holder.getThumbnail().setImageResource(R.drawable.attribution_thumb_soundboard);
                }
                holder.getAuthor().setText(source.getUsername());
                break;
            case 1:
                VineAudioMetadata audioMetadata = attribution.getAudioMetadata();
                holder.getThumbnail().setImageResource(R.drawable.attribution_thumb_music);
                holder.getTitle().setText(audioMetadata.getTrackName());
                holder.getAuthor().setText(audioMetadata.getArtistName());
                break;
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.feedadapter.AttributionAdapter.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (attribution.getType() == 0) {
                    LinkDispatcher.dispatch("vine://timelines/remixes/post/" + attribution.getVineSource().getPostId(), AttributionAdapter.this.mContext);
                } else if (attribution.getType() == 1) {
                    LinkDispatcher.dispatch("vine://timelines/audio/" + attribution.getAudioMetadata().getTrackId(), AttributionAdapter.this.mContext);
                }
            }
        });
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mList.size();
    }
}
