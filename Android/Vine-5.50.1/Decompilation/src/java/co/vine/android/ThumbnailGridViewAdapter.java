package co.vine.android;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/* loaded from: classes.dex */
public abstract class ThumbnailGridViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    @Override // android.support.v7.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == 1) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.grid_item, viewGroup, false);
            return new ThumbnailViewHolder(v);
        }
        if (viewType == 0) {
            View v2 = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.progress_item, viewGroup, false);
            return new ProgressBarViewHolder(v2);
        }
        return null;
    }
}
