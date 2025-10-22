package co.vine.android.service.components;

import android.os.Bundle;
import co.vine.android.client.AppController;
import co.vine.android.client.ListenerNotifier;
import co.vine.android.service.VineServiceAction;
import co.vine.android.service.VineServiceActionMapProvider;
import java.lang.Enum;
import java.util.TreeMap;

/* loaded from: classes.dex */
public abstract class ActionCodeComponent<E extends Enum<E>> extends VineServiceComponent {
    private final TreeMap<E, Integer> mActionCodeMap = new TreeMap<>();

    protected int getActionCode(E action) {
        return this.mActionCodeMap.get(action).intValue();
    }

    protected void registerAsActionCode(VineServiceActionMapProvider.Builder builder, E action, VineServiceAction serviceAction) {
        registerAsActionCode(builder, action, serviceAction, null);
    }

    protected void registerAsActionCode(VineServiceActionMapProvider.Builder builder, E action, VineServiceAction serviceAction, ListenerNotifier listenerNotifier) {
        int actionCode = builder.registerAsActionCode(serviceAction);
        this.mActionCodeMap.put(action, Integer.valueOf(actionCode));
        if (listenerNotifier != null) {
            AppController.register(actionCode, listenerNotifier);
        }
    }

    protected String executeServiceAction(AppController appController, E action, Bundle b) {
        return appController.executeComponentServiceAction(getActionCode(action), b);
    }
}
