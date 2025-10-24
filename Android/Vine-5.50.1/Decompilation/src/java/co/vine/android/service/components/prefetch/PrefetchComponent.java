package co.vine.android.service.components.prefetch;

import android.content.Context;
import android.content.Intent;
import co.vine.android.service.VineService;
import co.vine.android.service.VineServiceActionMapProvider;
import co.vine.android.service.components.VineServiceComponent;

/* loaded from: classes.dex */
public final class PrefetchComponent extends VineServiceComponent {
    public Intent getPrefetchIntent(Context context, boolean manual) {
        Intent intent = new Intent(context, (Class<?>) VineService.class);
        intent.setAction("co.vine.android.service.prefetch");
        intent.putExtra("manual", manual);
        return intent;
    }

    @Override // co.vine.android.service.components.VineServiceComponent
    public void registerActions(VineServiceActionMapProvider.Builder builder) {
        registerAsActionString(builder, "co.vine.android.service.prefetch", new PrefetchAction());
    }
}
