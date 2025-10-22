package co.vine.android.service.components.suggestions;

import android.os.Bundle;
import co.vine.android.client.ListenerNotifier;
import co.vine.android.client.ServiceNotification;
import co.vine.android.model.VineTag;
import co.vine.android.model.impl.VineModelFactory;
import java.util.ArrayList;

/* loaded from: classes.dex */
public final class FetchTagsTypeaheadActionNotifier implements ListenerNotifier {
    FetchTagsTypeaheadActionNotifier() {
    }

    @Override // co.vine.android.client.ListenerNotifier
    public void notifyListeners(ServiceNotification notification) {
        if (notification.statusCode == 200) {
            Bundle b = notification.b;
            String query = b.getString("q");
            ArrayList<VineTag> tags = b.getParcelableArrayList("tags");
            VineModelFactory.getMutableModelInstance().getMutableTagModel().addTagsForQuery(query, tags);
        }
    }
}
