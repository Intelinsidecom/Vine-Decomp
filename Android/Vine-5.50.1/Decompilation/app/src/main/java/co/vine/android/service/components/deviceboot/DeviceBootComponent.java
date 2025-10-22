package co.vine.android.service.components.deviceboot;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import co.vine.android.service.VineService;
import co.vine.android.service.VineServiceActionMapProvider;
import co.vine.android.service.components.VineServiceComponent;

/* loaded from: classes.dex */
public final class DeviceBootComponent extends VineServiceComponent {
    public Intent getDeviceBootIntent(Context context) {
        return new Intent(context, (Class<?>) VineService.class).setAction("co.vine.android.device.boot").putExtras(Bundle.EMPTY);
    }

    @Override // co.vine.android.service.components.VineServiceComponent
    public void registerActions(VineServiceActionMapProvider.Builder builder) {
        registerAsActionString(builder, "co.vine.android.device.boot", new DeviceBootAction());
    }
}
