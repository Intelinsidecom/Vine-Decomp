package io.fabric.sdk.android.services.common;

import android.content.Context;
import io.fabric.sdk.android.Fabric;
import java.lang.reflect.Method;

/* loaded from: classes.dex */
class AdvertisingInfoReflectionStrategy implements AdvertisingInfoStrategy {
    private final Context context;

    public AdvertisingInfoReflectionStrategy(Context context) {
        this.context = context.getApplicationContext();
    }

    boolean isGooglePlayServiceAvailable(Context context) throws NoSuchMethodException, SecurityException {
        try {
            Method method = Class.forName("com.google.android.gms.common.GooglePlayServicesUtil").getMethod("isGooglePlayServicesAvailable", Context.class);
            Integer result = (Integer) method.invoke(null, context);
            return result.intValue() == 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Override // io.fabric.sdk.android.services.common.AdvertisingInfoStrategy
    public AdvertisingInfo getAdvertisingInfo() {
        if (isGooglePlayServiceAvailable(this.context)) {
            return new AdvertisingInfo(getAdvertisingId(), isLimitAdTrackingEnabled());
        }
        return null;
    }

    private String getAdvertisingId() throws NoSuchMethodException, SecurityException {
        try {
            Method method = Class.forName("com.google.android.gms.ads.identifier.AdvertisingIdClient$Info").getMethod("getId", new Class[0]);
            return (String) method.invoke(getInfo(), new Object[0]);
        } catch (Exception e) {
            Fabric.getLogger().w("Fabric", "Could not call getId on com.google.android.gms.ads.identifier.AdvertisingIdClient$Info");
            return null;
        }
    }

    private boolean isLimitAdTrackingEnabled() throws NoSuchMethodException, SecurityException {
        try {
            Method method = Class.forName("com.google.android.gms.ads.identifier.AdvertisingIdClient$Info").getMethod("isLimitAdTrackingEnabled", new Class[0]);
            return ((Boolean) method.invoke(getInfo(), new Object[0])).booleanValue();
        } catch (Exception e) {
            Fabric.getLogger().w("Fabric", "Could not call isLimitAdTrackingEnabled on com.google.android.gms.ads.identifier.AdvertisingIdClient$Info");
            return false;
        }
    }

    private Object getInfo() throws NoSuchMethodException, SecurityException {
        try {
            Method method = Class.forName("com.google.android.gms.ads.identifier.AdvertisingIdClient").getMethod("getAdvertisingIdInfo", Context.class);
            return method.invoke(null, this.context);
        } catch (Exception e) {
            Fabric.getLogger().w("Fabric", "Could not call getAdvertisingIdInfo on com.google.android.gms.ads.identifier.AdvertisingIdClient");
            return null;
        }
    }
}
