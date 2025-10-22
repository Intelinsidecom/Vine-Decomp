package co.vine.android.feedadapter.viewmanager;

import android.app.Activity;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import co.vine.android.R;
import co.vine.android.api.VineFeed;
import co.vine.android.client.AppController;
import co.vine.android.feedadapter.TimelineClickListenerFactory;
import co.vine.android.feedadapter.v2.ViewType;
import co.vine.android.feedadapter.viewholder.FeedFooterViewHolder;

/* loaded from: classes.dex */
public class FeedFooterViewManager implements ViewManager {
    private final AppController mAppController;
    private final TimelineClickListenerFactory.Callback mCallback;
    private final Activity mContext;
    private final PostDescriptionViewManager mDescriptionManager = new PostDescriptionViewManager();
    private final PostActionButtonViewManager mMoreButtonManager = new PostActionButtonViewManager(ViewType.MORE_OPTIONS);
    private final PostActionButtonViewManager mShareChannelManager = new PostActionButtonViewManager(ViewType.SHARE_CHANNEL_BUTTON);

    public FeedFooterViewManager(Activity context, AppController appController, TimelineClickListenerFactory.Callback callback) {
        this.mContext = context;
        this.mAppController = appController;
        this.mCallback = callback;
    }

    @Override // co.vine.android.feedadapter.viewmanager.ViewManager
    public ViewType getType() {
        return ViewType.FEED_FOOTER;
    }

    public void bind(FeedFooterViewHolder h, VineFeed data, int profileColor) {
        if (data != null) {
            if (!TextUtils.isEmpty(data.description)) {
                SpannableString commentUser = new SpannableString(this.mContext.getString(R.string.feed_share_comment, new Object[]{data.user.username}) + '\n');
                commentUser.setSpan(new ForegroundColorSpan(this.mContext.getResources().getColor(R.color.discover_entity_color)), 0, commentUser.length(), 0);
                SpannableString comment = new SpannableString(data.description);
                comment.setSpan(new ForegroundColorSpan(this.mContext.getResources().getColor(R.color.black_eighty_five_percent)), 0, comment.length(), 0);
                SpannableStringBuilder ssb = new SpannableStringBuilder(commentUser);
                ssb.append((CharSequence) comment);
                this.mDescriptionManager.bind(h.getDescriptionHolder(), ssb);
            } else {
                h.getDescriptionHolder().text.setVisibility(8);
            }
            this.mMoreButtonManager.bind(h.getMoreButtonHolder(), true, false, profileColor);
            this.mMoreButtonManager.bindClickListener(h.getMoreButtonHolder(), TimelineClickListenerFactory.newMoreButtonClickListener(this.mCallback, data));
            this.mShareChannelManager.bindClickListener(h.getShareChannelHolder(), TimelineClickListenerFactory.newShareFeedButtonClickedListener(this.mCallback, data));
        }
    }
}
