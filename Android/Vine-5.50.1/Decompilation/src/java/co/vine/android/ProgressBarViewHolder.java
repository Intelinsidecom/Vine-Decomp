package co.vine.android;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

/* loaded from: classes.dex */
public class ProgressBarViewHolder extends RecyclerView.ViewHolder {
    public ProgressBar mProgressBar;

    public ProgressBarViewHolder(View v) {
        super(v);
        this.mProgressBar = (ProgressBar) v.findViewById(R.id.progress_bar);
    }

    public ProgressBar getProgress() {
        return this.mProgressBar;
    }
}
