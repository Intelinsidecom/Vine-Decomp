package co.vine.android;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

/* loaded from: classes.dex */
public class TabbedFeedActivityFactory {
    public static void startTabbedAudioTrackActivity(Context context, Uri data) {
        Intent intent = new Intent(context, (Class<?>) TabbedFeedActivity.class);
        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme(data.getScheme());
        uriBuilder.authority(data.getAuthority());
        uriBuilder.path(data.getPath());
        Uri data2 = uriBuilder.build();
        Bundle topPosts = prepareArguments("remix-recent", null, data2);
        intent.putExtra("tab_bundle_1", topPosts);
        Bundle recentPosts = prepareArguments("remix-top", null, data2);
        intent.putExtra("tab_bundle_2", recentPosts);
        intent.putExtra("title", " ");
        intent.putExtra("show_subtitle", false);
        context.startActivity(intent);
    }

    public static void startTabbedRemixActivity(Context context, Uri data) {
        Intent intent = new Intent(context, (Class<?>) TabbedFeedActivity.class);
        Bundle topPosts = prepareArguments("remix-recent", null, data);
        intent.putExtra("tab_bundle_1", topPosts);
        Bundle recentPosts = prepareArguments("remix-top", null, data);
        intent.putExtra("tab_bundle_2", recentPosts);
        intent.putExtra("title", context.getString(R.string.remixes));
        intent.putExtra("show_subtitle", false);
        context.startActivity(intent);
    }

    public static void startTabbedPostsActivity(Context context, String queryString) {
        Intent intent = new Intent(context, (Class<?>) TabbedFeedActivity.class);
        Bundle topPosts = prepareArguments("post-search-top", queryString, null);
        intent.putExtra("tab_bundle_1", topPosts);
        Bundle recentPosts = prepareArguments("post-search-recent", queryString, null);
        intent.putExtra("tab_bundle_2", recentPosts);
        intent.putExtra("title", queryString);
        intent.putExtra("show_subtitle", true);
        context.startActivity(intent);
    }

    public static void startTabbedTagsActivity(Context context, String queryString) {
        Intent intent = new Intent(context, (Class<?>) TabbedFeedActivity.class);
        Bundle topTags = prepareArguments("tag", queryString, null);
        intent.putExtra("tab_bundle_1", topTags);
        Bundle recentTags = prepareArguments("tag-recent", queryString, null);
        intent.putExtra("tab_bundle_2", recentTags);
        intent.putExtra("title", "#" + queryString);
        intent.putExtra("show_subtitle", true);
        context.startActivity(intent);
    }

    private static Bundle prepareArguments(String category, String query, Uri data) {
        Bundle tags = ExploreTimelineFragment.prepareArguments(new Intent(), true, category, query, data);
        tags.putInt("empty_desc", R.string.failed_to_load_posts);
        return tags;
    }
}
