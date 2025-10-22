package co.vine.android.feedadapter.viewholder;

import android.view.View;
import co.vine.android.api.VineFeed;
import co.vine.android.api.VineMosaic;
import co.vine.android.api.VinePost;
import co.vine.android.api.VineSolicitor;
import co.vine.android.api.VineUrlAction;
import co.vine.android.feedadapter.ViewGroupHelper;
import com.google.common.android.base.android.Optional;

/* loaded from: classes.dex */
public abstract class CardViewHolder implements ViewGroupHelper.ViewChildViewHolder, ViewHolder {
    public int position;
    public View view;
    public Optional<VinePost> post = Optional.absent();
    public Optional<VineMosaic> mosaic = Optional.absent();
    public Optional<VineUrlAction> urlAction = Optional.absent();
    public Optional<VineSolicitor> solicitor = Optional.absent();
    public Optional<VineFeed> feed = Optional.absent();

    public CardViewHolder(View view) {
        this.view = view;
    }

    @Override // co.vine.android.feedadapter.ViewGroupHelper.ViewChildViewHolder
    public int getPosition() {
        return this.position;
    }
}
