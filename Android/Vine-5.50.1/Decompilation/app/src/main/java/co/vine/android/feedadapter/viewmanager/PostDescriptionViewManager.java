package co.vine.android.feedadapter.viewmanager;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import co.vine.android.api.VineEntity;
import co.vine.android.feedadapter.v2.ViewType;
import co.vine.android.feedadapter.viewholder.TextViewHolder;
import co.vine.android.util.CrashUtil;
import co.vine.android.util.LinkSuppressor;
import co.vine.android.util.Util;
import co.vine.android.widget.EntityLinkClickableSpan;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public class PostDescriptionViewManager implements ViewManager {
    @Override // co.vine.android.feedadapter.viewmanager.ViewManager
    public ViewType getType() {
        return ViewType.DESCRIPTION;
    }

    public void bind(TextViewHolder h, Description description, boolean hasEntities, Boolean isRTL, int color, int weight, LinkSuppressor suppressor) {
        if (h.text != null) {
            h.text.setVisibility(0);
            if (isRTL.booleanValue()) {
                h.text.setGravity(5);
            } else {
                h.text.setGravity(3);
            }
            if (hasEntities) {
                h.text.setMovementMethod(LinkMovementMethod.getInstance());
            }
            if (weight <= 0) {
                weight = 2;
            }
            h.text.setText(description.getBuiltDescription(color, weight, suppressor));
        }
    }

    public void bind(TextViewHolder h, SpannableStringBuilder description) {
        if (h.text != null) {
            h.text.setVisibility(0);
            h.text.setText(description);
        }
    }

    public static final class Description {
        private final Context mContext;
        private final ArrayList<VineEntity> mEntities;
        private final CharSequence mRaw;
        private CharSequence mReady;

        Description(Context context, CharSequence description) {
            this.mContext = context;
            this.mReady = description;
            this.mRaw = null;
            this.mEntities = null;
        }

        Description(Context context, String raw, ArrayList<VineEntity> entities) {
            this.mContext = context;
            this.mRaw = raw;
            this.mEntities = entities;
            this.mReady = null;
        }

        public CharSequence getBuiltDescription(int color, int weight, LinkSuppressor suppressor) {
            if (this.mReady != null) {
                return this.mReady;
            }
            if (this.mEntities == null) {
                this.mReady = this.mRaw;
                return this.mReady;
            }
            SpannableStringBuilder sb = new SpannableStringBuilder(this.mRaw);
            try {
                Util.adjustEntities(this.mEntities, sb, 0, false);
                Iterator<VineEntity> it = this.mEntities.iterator();
                while (it.hasNext()) {
                    VineEntity e = it.next();
                    EntityLinkClickableSpan clickableSpan = new EntityLinkClickableSpan(this.mContext, color, weight, e.link, suppressor);
                    Util.safeSetSpan(sb, clickableSpan, e.start, e.end, 33);
                }
                this.mReady = sb;
            } catch (Exception e2) {
                CrashUtil.logException(e2);
                this.mReady = this.mRaw;
            }
            return this.mReady;
        }
    }
}
