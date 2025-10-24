package co.vine.android.service.components.timelinefetch;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import co.vine.android.api.TimelineItem;
import co.vine.android.api.TimelineItemType;
import co.vine.android.api.TimelineItemWrapper;
import co.vine.android.api.VinePagedData;
import co.vine.android.api.VineParserReader;
import co.vine.android.api.VinePost;
import co.vine.android.client.VineAPI;
import co.vine.android.model.impl.TimelineDetails;
import co.vine.android.network.NetworkOperation;
import co.vine.android.network.NetworkOperationFactory;
import co.vine.android.service.VineDatabaseHelperInterface;
import co.vine.android.service.VineServiceAction;
import co.vine.android.service.VineServiceActionResult;
import co.vine.android.service.VineServicePagedAction;
import co.vine.android.service.components.VineServiceActionHelper;
import co.vine.android.util.BuildUtil;
import co.vine.android.util.Util;
import com.mobileapptracker.MATEvent;
import java.util.Iterator;
import org.parceler.Parcels;

/* loaded from: classes.dex */
public final class TimelineFetchAction extends VineServicePagedAction {
    private VineAPI mApi;
    private Context mContext;
    private String mMarker;
    NetworkOperationFactory<VineAPI> mNetOpFactory;
    NetworkOperation op = null;
    VineParserReader vp = null;

    @Override // co.vine.android.service.VineServicePagedAction
    public VineServiceActionResult doPagedAction(VineServiceAction.Request request) {
        return executeFetchPost(request);
    }

    @Override // co.vine.android.service.VineServicePagedAction
    protected String getUniqueMarker(VineServiceAction.Request request) {
        Bundle b = request.b;
        TimelineDetails timelineDetails = new TimelineDetails(b.getInt("type"), Long.valueOf(b.getLong("profile_id")), b.getString("sort"));
        this.mMarker = timelineDetails.getUniqueMarker();
        return this.mMarker;
    }

    private VineServiceActionResult executeFetchPost(VineServiceAction.Request request) {
        Bundle b = request.b;
        int type = b.getInt("type", -1);
        this.mNetOpFactory = request.networkFactory;
        this.mContext = request.context;
        this.mApi = request.api;
        StringBuilder url = generatePostFetchUrl(type, b.getLong("profile_id"), b);
        int fetchType = b.getInt("fetch_type");
        this.mMarker = getUniqueMarker(request);
        if (fetchType == 1) {
            addPagingInfoToRequest(url, getUniqueMarker(request));
        }
        this.op = getPosts(url, type, b.getInt("size", 20), request.dbHelper, b.getString("anchor"), b.getString("back_anchor"), b, false);
        return new VineServiceActionResult(this.vp, this.op);
    }

    private StringBuilder generatePostFetchUrl(int type, long userId, Bundle b) {
        String base = this.mApi.getBaseUrl();
        switch (type) {
            case 1:
                StringBuilder url = VineAPI.buildUponUrl(base, "timelines", "graph");
                return url;
            case 2:
                StringBuilder url2 = VineAPI.buildUponUrl(base, "timelines", "users", Long.valueOf(userId));
                VineAPI.addParam(url2, "reposts", 1);
                VineAPI.addParam(url2, "sort", b.getString("sort", "recent"));
                VineAPI.addParam(url2, "c_overflow", "trunc");
                return url2;
            case 3:
                StringBuilder url3 = VineAPI.buildUponUrl(base, "timelines", "users", Long.valueOf(userId), "likes");
                VineAPI.addParam(url3, "sort", b.getString("sort", "recent"));
                return url3;
            case 4:
                StringBuilder url4 = VineAPI.buildUponUrl(base, "timelines", "users", "trending");
                return url4;
            case 5:
                StringBuilder url5 = VineAPI.buildUponUrl(base, "timelines", "popular");
                return url5;
            case 6:
            case 16:
                String tag = b.getString("tag_name");
                StringBuilder url6 = VineAPI.buildUponUrl(base, "timelines", "tags", tag);
                VineAPI.addParam(url6, "sort", type == 16 ? "recent" : "top");
                return url6;
            case 7:
            case 12:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 32:
            case 33:
            case 34:
            case 35:
            case 37:
            case 38:
            case 39:
            default:
                throw new IllegalArgumentException("Tried to fetch timeline with unsupported type " + type);
            case 8:
            case 9:
                String channelId = b.getString("tag_name");
                String sort = b.getString("sort");
                StringBuilder url7 = VineAPI.buildUponUrl(base, "timelines", "channels", channelId, sort);
                return url7;
            case 10:
                StringBuilder url8 = VineAPI.buildUponUrl(base, "timelines", "users", Long.valueOf(userId));
                VineAPI.addParam(url8, "sort", b.getString("sort", "recent"));
                VineAPI.addParam(url8, "reposts", 0);
                return url8;
            case 11:
                StringBuilder url9 = VineAPI.buildUponUrl(base, "timelines");
                Uri data = (Uri) b.getParcelable("data");
                for (String pathSegment : data.getPathSegments()) {
                    url9 = VineAPI.buildUponUrl(url9.toString(), pathSegment);
                }
                return url9;
            case 13:
                StringBuilder url10 = VineAPI.buildUponUrl(base, "timelines");
                StringBuilder url11 = VineAPI.buildUponUrl(url10.toString(), "venues");
                Uri data2 = (Uri) b.getParcelable("data");
                for (String pathSegment2 : data2.getPathSegments()) {
                    url11 = VineAPI.buildUponUrl(url11.toString(), pathSegment2);
                }
                return url11;
            case 14:
            case 15:
                String query = b.getString("tag_name");
                StringBuilder url12 = VineAPI.buildUponUrl(this.mApi.getBaseUrl(), MATEvent.SEARCH, "posts");
                VineAPI.addParam(url12, "q", query);
                VineAPI.addParam(url12, "sort", type == 15 ? "recent" : "top");
                return url12;
            case 17:
            case 18:
                StringBuilder url13 = VineAPI.buildUponUrl(base, "timelines");
                Uri data3 = (Uri) b.getParcelable("data");
                for (String pathSegment3 : data3.getPathSegments()) {
                    url13 = VineAPI.buildUponUrl(url13.toString(), pathSegment3);
                }
                for (String param : data3.getQueryParameterNames()) {
                    VineAPI.addParam(url13, param, data3.getQueryParameter(param));
                }
                VineAPI.addParam(url13, "sort", type == 18 ? "recent" : "top");
                return url13;
            case 30:
                StringBuilder url14 = VineAPI.buildUponUrl(base, "timelines", "welcome");
                return url14;
            case 31:
                StringBuilder url15 = VineAPI.buildUponUrl(base, "timelines", "solicitor");
                return url15;
            case 36:
                String channelId2 = b.getString("tag_name");
                StringBuilder url16 = VineAPI.buildUponUrl(base, "timelines", "genius", "me", "channels", channelId2);
                return url16;
            case 40:
                StringBuilder url17 = VineAPI.buildUponUrl(base, "timelines", "users", Long.valueOf(userId));
                VineAPI.addParam(url17, "sort", b.getString("sort", "recent"));
                VineAPI.addParam(url17, "reposts", 0);
                VineAPI.addParam(url17, "feeds", 0);
                return url17;
        }
    }

    private NetworkOperation getPosts(StringBuilder url, int type, int size, VineDatabaseHelperInterface dbHelper, String anchor, String backAnchor, Bundle b, boolean prefetch) {
        VinePagedData<TimelineItem> pagedData;
        VineAPI.addAnchor(url, anchor);
        VineAPI.addBackAnchor(url, backAnchor);
        VineAPI.addParam(url, "size", size);
        limitCommentAndLikesIfNeeded(url);
        this.vp = VineParserReader.createParserReader(36);
        this.op = this.mNetOpFactory.createBasicAuthGetRequest(this.mContext, url, this.mApi, this.vp);
        VineServiceActionHelper.assignPollingHeader(this.op, b);
        this.op.execute();
        NetworkOperation.NetworkOperationResult result = this.op.getLastExecuteResult();
        if (this.op.isOK() && (pagedData = (VinePagedData) this.vp.getParsedObject()) != null) {
            if (type >= 0) {
                if (Util.isPopularTimeline(type)) {
                    int count = 1;
                    Cursor c = dbHelper.getOldestSortId(type);
                    if (c != null) {
                        if (c.moveToFirst()) {
                            count = c.getInt(0) + 1;
                        }
                        c.close();
                    }
                    Iterator<TimelineItem> it = pagedData.items.iterator();
                    while (it.hasNext()) {
                        TimelineItem item = it.next();
                        if (item.getType() == TimelineItemType.POST) {
                            ((VinePost) item).orderId = String.valueOf(count);
                            count++;
                        }
                    }
                }
                b.putInt("count", pagedData.items == null ? 0 : pagedData.items.size());
                b.putInt("size", pagedData.count);
                TimelineItemWrapper.bundleTimelineItemList(b, pagedData.items, "timeline_items");
                b.putBoolean("in_memory", true);
                b.putBoolean("network", result == NetworkOperation.NetworkOperationResult.NETWORK);
                b.putInt("next_page", pagedData.nextPage);
                b.putInt("previous_page", pagedData.previousPage);
                b.putString("anchor", pagedData.anchor);
                b.putString("back_anchor", pagedData.backAnchor);
                b.putString("title", pagedData.title);
                if (type == 8 || type == 9) {
                    b.putParcelable("channels", Parcels.wrap(pagedData.channel));
                }
                if (type == 36) {
                    b.putBoolean("for_you", true);
                }
            } else {
                TimelineItemWrapper.bundleTimelineItemList(b, pagedData.items, "timeline_items");
            }
            updatePagingInfoFromResult(pagedData.nextPage, pagedData.anchor, this.mMarker);
        }
        return this.op;
    }

    public void limitCommentAndLikesIfNeeded(StringBuilder url) {
        if (BuildUtil.isOldDeviceOrLowEndDevice(this.mContext)) {
            VineAPI.addParam(url, "c_max", 0);
            VineAPI.addParam(url, "l_max", 0);
        }
    }
}
