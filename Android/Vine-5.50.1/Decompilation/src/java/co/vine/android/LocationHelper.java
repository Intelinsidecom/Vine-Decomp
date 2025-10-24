package co.vine.android;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.edisonwang.android.slog.SLog;

/* loaded from: classes.dex */
final class LocationHelper implements LocationListener {
    private final Context mContext;
    private final Handler mHandler;
    private Location mLocation;
    private LocationManager mLocationManager;
    private final long mUpdateDistanceMeters;
    private final long mUpdateIntervalSeconds;

    public LocationHelper(Context context, Handler handler) {
        this(context, handler, 20, 20);
    }

    public LocationHelper(Context context, Handler handler, int updateIntervalSeconds, int updateDistanceMeters) {
        this.mContext = context;
        this.mHandler = handler;
        this.mUpdateIntervalSeconds = updateIntervalSeconds;
        this.mUpdateDistanceMeters = updateDistanceMeters;
        getLocation();
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:13:0x0030 -> B:18:0x001a). Please report as a decompilation issue!!! */
    public Location getLocation() {
        Location location;
        this.mLocationManager = (LocationManager) this.mContext.getSystemService("location");
        try {
            this.mLocation = getLocationFromProvider("gps");
        } catch (IllegalArgumentException ex) {
            SLog.e("Illegal arguments when requesting location updates", (Throwable) ex);
        } catch (SecurityException ex2) {
            SLog.e("No suitable permission for requesting location updates", (Throwable) ex2);
        } catch (RuntimeException ex3) {
            SLog.e("Location requesting thread has no looper", (Throwable) ex3);
        }
        if (this.mLocation != null) {
            location = this.mLocation;
        } else {
            this.mLocation = getLocationFromProvider("network");
            if (this.mLocation != null) {
                location = this.mLocation;
            } else {
                location = this.mLocation;
            }
        }
        return location;
    }

    private Location getLocationFromProvider(String provider) {
        if (this.mLocationManager == null) {
            return null;
        }
        this.mLocationManager.requestLocationUpdates(provider, this.mUpdateIntervalSeconds, this.mUpdateDistanceMeters, this);
        return this.mLocationManager.getLastKnownLocation(provider);
    }

    public void disable() {
        if (this.mLocationManager != null) {
            this.mLocationManager.removeUpdates(this);
        }
    }

    public boolean isLocationEnabled() {
        if (this.mLocationManager == null) {
            return false;
        }
        return this.mLocationManager.isProviderEnabled("gps") || this.mLocationManager.isProviderEnabled("network");
    }

    @Override // android.location.LocationListener
    public void onLocationChanged(Location location) {
        if (location != null) {
            this.mLocation = location;
            Message message = Message.obtain(this.mHandler, 0);
            this.mHandler.sendMessage(message);
        }
    }

    @Override // android.location.LocationListener
    public void onProviderEnabled(String provider) {
    }

    @Override // android.location.LocationListener
    public void onProviderDisabled(String provider) {
    }

    @Override // android.location.LocationListener
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
}
