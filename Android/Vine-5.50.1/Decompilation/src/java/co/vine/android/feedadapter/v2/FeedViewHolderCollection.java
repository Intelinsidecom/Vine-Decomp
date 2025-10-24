package co.vine.android.feedadapter.v2;

import co.vine.android.feedadapter.viewholder.CardViewHolder;
import com.google.common.android.base.android.Optional;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public class FeedViewHolderCollection {
    private final ArrayList<WeakReference<? extends CardViewHolder>> mViewHolders = new ArrayList<>();

    public ArrayList<CardViewHolder> getViewHolders() {
        ArrayList<CardViewHolder> holders = new ArrayList<>();
        ArrayList<WeakReference<? extends CardViewHolder>> toRemove = new ArrayList<>();
        Iterator<WeakReference<? extends CardViewHolder>> it = this.mViewHolders.iterator();
        while (it.hasNext()) {
            WeakReference<? extends CardViewHolder> ref = it.next();
            CardViewHolder holder = ref.get();
            if (holder == null) {
                toRemove.add(ref);
            } else {
                holders.add(holder);
            }
        }
        this.mViewHolders.removeAll(toRemove);
        return holders;
    }

    public void invalidateAllViewHolderPositions() {
        ArrayList<CardViewHolder> holders = getViewHolders();
        Iterator<CardViewHolder> it = holders.iterator();
        while (it.hasNext()) {
            CardViewHolder holder = it.next();
            holder.post = Optional.absent();
            holder.position = -1;
        }
    }

    public void add(WeakReference<? extends CardViewHolder> item) {
        this.mViewHolders.add(item);
    }

    public CardViewHolder getViewHolderForPostId(long postId) {
        ArrayList<CardViewHolder> holders = getViewHolders();
        Iterator<CardViewHolder> it = holders.iterator();
        while (it.hasNext()) {
            CardViewHolder holder = it.next();
            if (holder.post.isPresent() && holder.post.get().postId == postId) {
                return holder;
            }
        }
        return null;
    }

    public CardViewHolder getViewHolderForPosition(int position) {
        ArrayList<CardViewHolder> holders = getViewHolders();
        Iterator<CardViewHolder> it = holders.iterator();
        while (it.hasNext()) {
            CardViewHolder holder = it.next();
            if (holder.position == position) {
                return holder;
            }
        }
        return null;
    }
}
