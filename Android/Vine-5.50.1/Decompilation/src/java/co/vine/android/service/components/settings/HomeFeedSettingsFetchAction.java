package co.vine.android.service.components.settings;

import android.os.Bundle;
import co.vine.android.api.VineParserReader;
import co.vine.android.api.response.VineHomeFeedSettingsResponse;
import co.vine.android.client.VineAPI;
import co.vine.android.network.NetworkOperation;
import co.vine.android.service.VineServiceAction;
import co.vine.android.service.VineServiceActionResult;
import org.parceler.Parcels;

/* loaded from: classes.dex */
public final class HomeFeedSettingsFetchAction extends VineServiceAction {
    @Override // co.vine.android.service.VineServiceAction
    public VineServiceActionResult doAction(VineServiceAction.Request request) {
        Bundle b = request.b;
        StringBuilder url = VineAPI.buildUponUrl(request.api.getBaseUrl(), "users", "me", "timelines", "home", "settings");
        VineParserReader vp = VineParserReader.createParserReader(37);
        NetworkOperation op = request.networkFactory.createBasicAuthGetRequest(request.context, url, request.api, vp).execute();
        if (op.isOK()) {
            VineHomeFeedSettingsResponse.Data homeFeedSettings = (VineHomeFeedSettingsResponse.Data) vp.getParsedObject();
            b.putParcelable("notificationSettings", Parcels.wrap(homeFeedSettings.items));
        }
        return new VineServiceActionResult(vp, op);
    }
}
