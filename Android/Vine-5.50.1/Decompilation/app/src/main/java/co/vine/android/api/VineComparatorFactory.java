package co.vine.android.api;

import co.vine.android.util.Util;
import java.util.Comparator;

/* loaded from: classes.dex */
public class VineComparatorFactory {

    public interface VineComparator<VinePost> extends Comparator<VinePost> {
        long getOrderId(VinePost vinepost);
    }

    public static VineComparator<VinePost> get(int type) {
        if (Util.isPopularTimeline(type)) {
            return new VinePopularComparator();
        }
        switch (type) {
            case 1:
                return new HomeTimelineComparator();
            case 2:
                return new ProfileTimelineComparator();
            default:
                return new DefaultComparator();
        }
    }

    private static class HomeTimelineComparator implements VineComparator<VinePost> {
        private HomeTimelineComparator() {
        }

        @Override // java.util.Comparator
        public int compare(VinePost vinePost, VinePost vinePost2) {
            return Long.valueOf(getOrderId(vinePost2)).compareTo(Long.valueOf(getOrderId(vinePost)));
        }

        @Override // co.vine.android.api.VineComparatorFactory.VineComparator
        public long getOrderId(VinePost post) {
            if (post.user != null && post.user.isFollowing()) {
                return post.postId;
            }
            if (post.repost != null) {
                return post.repost.repostId;
            }
            return post.postId;
        }
    }

    private static class ProfileTimelineComparator implements VineComparator<VinePost> {
        private ProfileTimelineComparator() {
        }

        @Override // co.vine.android.api.VineComparatorFactory.VineComparator
        public long getOrderId(VinePost vinePost) {
            return vinePost.repost != null ? vinePost.repost.repostId : vinePost.postId;
        }

        @Override // java.util.Comparator
        public int compare(VinePost vp, VinePost vp2) {
            return Long.valueOf(getOrderId(vp2)).compareTo(Long.valueOf(getOrderId(vp)));
        }
    }

    private static class VinePopularComparator implements VineComparator<VinePost> {
        private VinePopularComparator() {
        }

        @Override // java.util.Comparator
        public int compare(VinePost vinePost, VinePost vinePost2) {
            return Long.valueOf(getOrderId(vinePost)).compareTo(Long.valueOf(getOrderId(vinePost2)));
        }

        @Override // co.vine.android.api.VineComparatorFactory.VineComparator
        public long getOrderId(VinePost post) {
            return Long.valueOf(post.orderId).longValue();
        }
    }

    private static class DefaultComparator implements VineComparator<VinePost> {
        private DefaultComparator() {
        }

        @Override // co.vine.android.api.VineComparatorFactory.VineComparator
        public long getOrderId(VinePost post) {
            return post.postId;
        }

        @Override // java.util.Comparator
        public int compare(VinePost vp, VinePost vp2) {
            return Long.valueOf(getOrderId(vp2)).compareTo(Long.valueOf(getOrderId(vp)));
        }
    }
}
