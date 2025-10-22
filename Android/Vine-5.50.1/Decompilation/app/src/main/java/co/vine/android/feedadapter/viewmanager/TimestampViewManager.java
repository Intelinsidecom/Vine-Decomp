package co.vine.android.feedadapter.viewmanager;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import co.vine.android.ExploreVideoListActivity;
import co.vine.android.R;
import co.vine.android.api.VineVenue;
import co.vine.android.feedadapter.v2.ViewType;
import co.vine.android.feedadapter.viewholder.TextViewHolder;
import co.vine.android.util.LinkSuppressor;

/* loaded from: classes.dex */
public class TimestampViewManager implements ViewManager {
    private final Context mContext;

    public TimestampViewManager(Context context) {
        this.mContext = context;
    }

    @Override // co.vine.android.feedadapter.viewmanager.ViewManager
    public ViewType getType() {
        return ViewType.TIMESTAMP;
    }

    public void bind(TextViewHolder h, String timestamp, boolean hasVenue) {
        if (h.text != null && timestamp != null) {
            h.text.setCompoundDrawablesWithIntrinsicBounds((Drawable) null, (Drawable) null, hasVenue ? this.mContext.getResources().getDrawable(R.drawable.ic_feed_map_pin) : null, (Drawable) null);
            h.text.setText(timestamp);
        }
    }

    public void bindClickListener(TextViewHolder h, final LinkSuppressor suppressor, final String url, final VineVenue venue) {
        if (venue != null) {
            h.text.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.feedadapter.viewmanager.TimestampViewManager.1
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    if ((suppressor == null || !suppressor.shouldSuppressVenueLink(url)) && !TextUtils.isEmpty(url)) {
                        Intent i = ExploreVideoListActivity.getIntent(TimestampViewManager.this.mContext, "venue", url + "?title=" + Uri.encode(venue.venueName));
                        TimestampViewManager.this.mContext.startActivity(i);
                    }
                }
            });
        } else {
            h.text.setOnClickListener(null);
        }
    }
}
