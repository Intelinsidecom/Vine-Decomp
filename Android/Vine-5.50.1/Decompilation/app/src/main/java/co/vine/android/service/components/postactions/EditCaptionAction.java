package co.vine.android.service.components.postactions;

import android.os.Bundle;
import co.vine.android.api.VineEntity;
import co.vine.android.api.VineParserReader;
import co.vine.android.client.VineAPI;
import co.vine.android.network.NetworkOperation;
import co.vine.android.service.VineServiceAction;
import co.vine.android.service.VineServiceActionResult;
import co.vine.android.util.CrashUtil;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class EditCaptionAction extends VineServiceAction {
    @Override // co.vine.android.service.VineServiceAction
    public VineServiceActionResult doAction(VineServiceAction.Request request) {
        Bundle b = request.b;
        long postId = b.getLong("post_id");
        String description = b.getString("desc");
        ArrayList<VineEntity> entities = b.getParcelableArrayList("entities");
        return putDescriptionUpdate(request, postId, description, entities);
    }

    private VineServiceActionResult putDescriptionUpdate(VineServiceAction.Request request, long postId, String description, ArrayList<VineEntity> entities) throws JSONException {
        StringBuilder url = VineAPI.buildUponUrl(request.api.getBaseUrl(), "posts", Long.valueOf(postId), "description");
        JSONObject body = new JSONObject();
        try {
            body.put("description", description);
            JSONArray jsonEntities = new JSONArray();
            if (entities != null) {
                Iterator<VineEntity> it = entities.iterator();
                while (it.hasNext()) {
                    VineEntity entity = it.next();
                    entity.generateEntityLinkForComment();
                    jsonEntities.put(entity.toJsonObject());
                }
                if (jsonEntities.length() > 0) {
                    body.put("entities", jsonEntities);
                }
            }
        } catch (JSONException ex) {
            CrashUtil.logOrThrowInDebug(ex);
        }
        VineParserReader vp = VineParserReader.createParserReader(1);
        NetworkOperation op = request.networkFactory.createBasicAuthJsonPutRequest(request.context, url, request.api, body, vp).execute();
        return new VineServiceActionResult(vp, op);
    }
}
