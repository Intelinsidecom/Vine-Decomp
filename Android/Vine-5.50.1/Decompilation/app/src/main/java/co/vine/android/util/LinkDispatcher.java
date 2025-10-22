package co.vine.android.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import co.vine.android.R;
import java.util.List;
import twitter4j.conf.PropertyConfiguration;

/* loaded from: classes.dex */
public class LinkDispatcher {
    public static void dispatch(String link, Context context) {
        dispatch(Uri.parse(link), context);
    }

    public static String trimSegment(String segment) {
        return segment.replaceAll("\\s+", "").replace("\\", "");
    }

    public static void dispatch(Uri linkUri, Context context) {
        dispatch(linkUri, context, (LinkSuppressor) null);
    }

    public static void dispatch(String linkUri, Context context, LinkSuppressor suppressor) throws PackageManager.NameNotFoundException {
        dispatch(Uri.parse(linkUri), context, suppressor);
    }

    public static void dispatch(Uri linkUri, Context context, LinkSuppressor suppressor) throws PackageManager.NameNotFoundException {
        if (linkUri != null) {
            String scheme = linkUri.getScheme();
            try {
                if ("vine".equals(scheme) || "vine-dev".equals(scheme)) {
                    dispatchVineScheme(linkUri, context, suppressor);
                } else if ("http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme)) {
                    dispatchHttpScheme(linkUri, context);
                }
            } catch (NumberFormatException e) {
                Util.showCenteredToast(context, R.string.invalid_link);
            }
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    /* JADX WARN: Removed duplicated region for block: B:19:0x0068  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static void dispatchHttpScheme(android.net.Uri r11, android.content.Context r12) throws android.content.pm.PackageManager.NameNotFoundException, java.lang.NumberFormatException {
        /*
            r9 = 1
            r7 = 0
            java.lang.String r1 = r11.getHost()
            java.util.ArrayList r5 = new java.util.ArrayList
            java.util.List r8 = r11.getPathSegments()
            r5.<init>(r8)
            java.lang.String r8 = "twitter.com"
            boolean r8 = r1.contains(r8)
            if (r8 == 0) goto L48
            co.vine.android.scribe.ScribeLogger r7 = co.vine.android.scribe.ScribeLoggerSingleton.getInstance(r12)
            co.vine.android.scribe.AppStateProvider r8 = co.vine.android.AppStateProviderSingleton.getInstance(r12)
            co.vine.android.scribe.AppNavigationProvider r9 = co.vine.android.scribe.AppNavigationProviderSingleton.getInstance()
            co.vine.android.scribe.UIEventScribeLogger.twitterLinkTap(r7, r8, r9)
            r2 = 0
            android.content.pm.PackageManager r7 = r12.getPackageManager()     // Catch: java.lang.Exception -> L43
            java.lang.String r8 = "com.twitter.android"
            r9 = 0
            r7.getPackageInfo(r8, r9)     // Catch: java.lang.Exception -> L43
            android.content.Intent r3 = new android.content.Intent     // Catch: java.lang.Exception -> L43
            java.lang.String r7 = "android.intent.action.VIEW"
            r3.<init>(r7, r11)     // Catch: java.lang.Exception -> L43
            r7 = 268435456(0x10000000, float:2.524355E-29)
            r3.addFlags(r7)     // Catch: java.lang.Exception -> Ld8
            r12.startActivity(r3)     // Catch: java.lang.Exception -> Ld8
            r2 = r3
        L42:
            return
        L43:
            r0 = move-exception
        L44:
            co.vine.android.WebViewActivity.start(r12, r11)
            goto L42
        L48:
            java.lang.String r8 = "vine.co"
            boolean r8 = r1.contains(r8)
            if (r8 == 0) goto L57
            boolean r8 = r5.isEmpty()
            if (r8 == 0) goto L5a
        L57:
            co.vine.android.WebViewActivity.start(r12, r11)
        L5a:
            java.lang.Object r4 = r5.get(r7)
            java.lang.String r4 = (java.lang.String) r4
            r8 = -1
            int r10 = r4.hashCode()
            switch(r10) {
                case 117: goto L79;
                case 118: goto L84;
                case 3552281: goto L99;
                case 1432626128: goto L70;
                case 1690566114: goto L8f;
                default: goto L68;
            }
        L68:
            r7 = r8
        L69:
            switch(r7) {
                case 0: goto La3;
                case 1: goto Lad;
                case 2: goto Lbb;
                case 3: goto Lca;
                case 4: goto Lcf;
                default: goto L6c;
            }
        L6c:
            co.vine.android.WebViewActivity.start(r12, r11)
            goto L42
        L70:
            java.lang.String r10 = "channels"
            boolean r10 = r4.equals(r10)
            if (r10 == 0) goto L68
            goto L69
        L79:
            java.lang.String r7 = "u"
            boolean r7 = r4.equals(r7)
            if (r7 == 0) goto L68
            r7 = r9
            goto L69
        L84:
            java.lang.String r7 = "v"
            boolean r7 = r4.equals(r7)
            if (r7 == 0) goto L68
            r7 = 2
            goto L69
        L8f:
            java.lang.String r7 = "popular-now"
            boolean r7 = r4.equals(r7)
            if (r7 == 0) goto L68
            r7 = 3
            goto L69
        L99:
            java.lang.String r7 = "tags"
            boolean r7 = r4.equals(r7)
            if (r7 == 0) goto L68
            r7 = 4
            goto L69
        La3:
            int r7 = r5.size()
            if (r7 <= 0) goto L42
            co.vine.android.ChannelActivity.startExploreChannel(r12, r11)
            goto L42
        Lad:
            java.lang.String r7 = r11.getLastPathSegment()
            long r8 = java.lang.Long.parseLong(r7)
            java.lang.String r7 = "LinkDispatcher"
            co.vine.android.ChannelActivity.startProfile(r12, r8, r7)
            goto L42
        Lbb:
            java.lang.Object r7 = r5.get(r9)
            java.lang.String r7 = (java.lang.String) r7
            java.lang.String r6 = trimSegment(r7)
            co.vine.android.SingleActivity.start(r12, r6)
            goto L42
        Lca:
            co.vine.android.ExploreVideoListActivity.start(r12, r11)
            goto L42
        Lcf:
            java.lang.String r7 = r11.getLastPathSegment()
            co.vine.android.TabbedFeedActivityFactory.startTabbedTagsActivity(r12, r7)
            goto L42
        Ld8:
            r0 = move-exception
            r2 = r3
            goto L44
        */
        throw new UnsupportedOperationException("Method not decompiled: co.vine.android.util.LinkDispatcher.dispatchHttpScheme(android.net.Uri, android.content.Context):void");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:109:0x0248  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static void dispatchVineScheme(android.net.Uri r22, android.content.Context r23, co.vine.android.util.LinkSuppressor r24) throws java.lang.NumberFormatException {
        /*
            Method dump skipped, instructions count: 836
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: co.vine.android.util.LinkDispatcher.dispatchVineScheme(android.net.Uri, android.content.Context, co.vine.android.util.LinkSuppressor):void");
    }

    private static String getLinkType(Uri linkUri, List<String> pathSegments) {
        String host = linkUri.getHost();
        if (PropertyConfiguration.USER.equals(host) && "settings".equals(linkUri.getLastPathSegment())) {
            return "settings";
        }
        if ("timelines".equals(host) && pathSegments != null && pathSegments.size() > 0) {
            String firstSegment = pathSegments.get(0);
            if ("audio".equals(firstSegment) || "remixes".equals(firstSegment)) {
                return firstSegment;
            }
        }
        return host;
    }
}
