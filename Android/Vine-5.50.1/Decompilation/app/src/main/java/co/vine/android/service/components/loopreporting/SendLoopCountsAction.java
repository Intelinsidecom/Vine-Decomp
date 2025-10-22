package co.vine.android.service.components.loopreporting;

import co.vine.android.api.VineParserReader;
import co.vine.android.api.response.VineLoopSubmissionResponse;
import co.vine.android.client.VineAPI;
import co.vine.android.network.NetworkOperation;
import co.vine.android.network.NetworkOperationReader;
import co.vine.android.service.VineServiceAction;
import co.vine.android.service.VineServiceActionResult;
import co.vine.android.util.ConsoleLoggers;
import co.vine.android.util.CrashUtil;
import co.vine.android.util.LoopManager;
import com.edisonwang.android.slog.SLogger;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

/* loaded from: classes.dex */
public class SendLoopCountsAction extends VineServiceAction {
    @Override // co.vine.android.service.VineServiceAction
    public VineServiceActionResult doAction(VineServiceAction.Request request) throws JSONException {
        List<LoopManager.Record> records = request.b.getParcelableArrayList("loops");
        StringBuilder url = VineAPI.buildUponUrl(request.api.getBaseUrl(), "loops");
        SLogger logger = ConsoleLoggers.LOOP_REPORTING.get();
        JSONArray loopsList = new JSONArray();
        JSONObject postBody = new JSONObject();
        try {
            for (LoopManager.Record r : records) {
                JSONObject loopBody = new JSONObject();
                loopBody.put("postId", r.postId);
                loopBody.put("count", r.loopCount);
                loopBody.put("ts", r.timeStamp / 1000.0d);
                loopsList.put(loopBody);
                if (logger.isActive()) {
                    logger.v("Sent " + r.postId + " " + r.loopCount);
                }
            }
            postBody.put("loops", loopsList);
        } catch (JSONException e) {
            CrashUtil.logOrThrowInDebug(e);
        }
        VineParserReader vp = VineParserReader.createParserReader(27);
        NetworkOperation op = request.networkFactory.createBasicAuthJsonPostRequest(request.context, url, (StringBuilder) request.api, postBody, (NetworkOperationReader) vp).execute();
        if (op.isOK()) {
            VineLoopSubmissionResponse response = (VineLoopSubmissionResponse) vp.getParsedObject();
            request.b.putParcelable("loop_submission", Parcels.wrap(response));
        }
        return new VineServiceActionResult(vp, op);
    }
}
