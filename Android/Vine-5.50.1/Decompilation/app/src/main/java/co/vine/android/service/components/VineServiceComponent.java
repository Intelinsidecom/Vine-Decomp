package co.vine.android.service.components;

import co.vine.android.service.VineServiceAction;
import co.vine.android.service.VineServiceActionMapProvider;

/* loaded from: classes.dex */
public abstract class VineServiceComponent {
    public abstract void registerActions(VineServiceActionMapProvider.Builder builder);

    protected VineServiceComponent() {
    }

    protected void registerAsActionString(VineServiceActionMapProvider.Builder builder, String actionString, VineServiceAction serviceAction) {
        builder.registerAsActionString(actionString, serviceAction);
    }
}
