package co.vine.android.feedadapter.v2;

import android.support.v4.util.ArrayMap;
import java.util.Map;

/* loaded from: classes.dex */
public enum ViewType {
    ERROR,
    POST,
    SIMILAR_USER,
    SUGGESTED_FEED,
    SUGGESTED_USERS,
    URL_ACTION,
    SOLICITOR,
    FEED,
    ACTION_BUTTON,
    AVATAR,
    AVATAR_FOLLOW,
    BACKGROUND_IMAGE,
    BACKGROUND_VIDEO,
    BYLINE,
    CLOSE_BUTTON,
    COMMENT_BUTTON,
    COMMENTS_LABEL,
    CONSUMPTION_BUTTON,
    DESCRIPTION,
    FOLLOW_TOGGLE,
    INLINE_COMMENTS,
    LIKE_BUTTON,
    LIKES_LABEL,
    LOOP_COUNTER,
    MORE_OPTIONS,
    POST_FOOTER,
    POST_HEADER,
    POST_MOSAIC,
    POST_VIDEO,
    TIMELINEITEM_VIDEO,
    FEED_HEADER,
    FEED_FOOTER,
    SHARE_CHANNEL_BUTTON,
    SIMILAR_VINES_PAGER,
    SOLICITOR_PROMPT_BUTTON,
    PREVIEW,
    SHARE_BUTTON,
    SHARES_LABEL,
    PLAYLIST_BUTTON,
    SIMILAR_BUTTON,
    TIMESTAMP,
    TITLE,
    USER_MOSAIC,
    USERNAME,
    VIEW_ALL,
    VIDEO_GRID,
    IMMERSIVE_DESCRIPTION,
    POST_OVERLAY,
    DAY;

    private static final Map<ViewType, Integer> sMap = new ArrayMap();

    static {
        int i = 0;
        ViewType[] viewTypeArrValues = values();
        int length = viewTypeArrValues.length;
        int i2 = -1;
        while (i < length) {
            ViewType type = viewTypeArrValues[i];
            sMap.put(type, Integer.valueOf(i2));
            i++;
            i2++;
        }
    }

    public static int getValue(ViewType type) {
        return !sMap.containsKey(type) ? sMap.get(ERROR).intValue() : sMap.get(type).intValue();
    }
}
