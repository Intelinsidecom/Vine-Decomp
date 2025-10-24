package com.flurry.sdk;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import com.flurry.sdk.dn;

/* loaded from: classes.dex */
public class dw implements dn.a {
    private static final String c = dw.class.getSimpleName();
    private static dw o;
    boolean b;
    private LocationManager g;
    private Criteria h;
    private Location i;
    private String k;
    private volatile Location n;
    private final int d = 3;
    private final long e = 10000;
    private final long f = 0;
    boolean a = false;
    private int l = 0;
    private int m = 0;
    private a j = new a();

    static /* synthetic */ int a(dw dwVar) {
        int i = dwVar.m + 1;
        dwVar.m = i;
        return i;
    }

    private dw() {
        dn dnVarA = dm.a();
        this.h = (Criteria) dnVarA.a("LocationCriteria");
        dnVarA.a("LocationCriteria", (dn.a) this);
        el.a(4, c, "initSettings, LocationCriteria = " + this.h);
        this.b = ((Boolean) dnVarA.a("ReportLocation")).booleanValue();
        dnVarA.a("ReportLocation", (dn.a) this);
        el.a(4, c, "initSettings, ReportLocation = " + this.b);
    }

    public static synchronized dw a() {
        if (o == null) {
            o = new dw();
        }
        return o;
    }

    public synchronized void b() {
        if (this.g == null) {
            this.g = (LocationManager) dl.a().b().getSystemService("location");
        }
    }

    public synchronized void c() {
        el.a(4, c, "Location provider subscribed");
        this.l++;
        if (!this.a && this.m < 3) {
            j();
        }
    }

    public synchronized void d() {
        el.a(4, c, "Location provider unsubscribed");
        if (this.l <= 0) {
            el.a(6, c, "Error! Unsubscribed too many times!");
        } else {
            this.l--;
            if (this.l == 0) {
                i();
            }
        }
    }

    public void a(float f, float f2) {
        this.n = new Location("Explicit");
        this.n.setLatitude(f);
        this.n.setLongitude(f2);
    }

    public void e() {
        this.n = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void i() {
        this.g.removeUpdates(this.j);
        this.a = false;
        this.m = 0;
        el.a(4, c, "LocationProvider stopped");
    }

    private void j() {
        if (this.b && this.n == null) {
            Context contextB = dl.a().b();
            if (contextB.checkCallingOrSelfPermission("android.permission.ACCESS_FINE_LOCATION") == 0 || contextB.checkCallingOrSelfPermission("android.permission.ACCESS_COARSE_LOCATION") == 0) {
                i();
                String strK = k();
                a(strK);
                this.i = b(strK);
                this.a = true;
                el.a(4, c, "LocationProvider started");
            }
        }
    }

    private String k() {
        String bestProvider;
        Criteria criteria = this.h;
        if (criteria == null) {
            criteria = new Criteria();
        }
        if (TextUtils.isEmpty(this.k)) {
            bestProvider = this.g.getBestProvider(criteria, true);
        } else {
            bestProvider = this.k;
        }
        el.a(4, c, "provider = " + bestProvider);
        return bestProvider;
    }

    private void a(String str) {
        if (!TextUtils.isEmpty(str)) {
            this.g.requestLocationUpdates(str, 10000L, 0.0f, this.j, Looper.getMainLooper());
        }
    }

    private Location b(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return this.g.getLastKnownLocation(str);
    }

    public Location f() {
        Location location = null;
        if (this.n != null) {
            return this.n;
        }
        if (this.b) {
            Location locationB = b(k());
            if (locationB != null) {
                this.i = locationB;
            }
            location = this.i;
        }
        el.a(4, c, "getLocation() = " + location);
        return location;
    }

    public void g() {
        this.l = 0;
        i();
    }

    class a implements LocationListener {
        public a() {
        }

        @Override // android.location.LocationListener
        public void onStatusChanged(String str, int i, Bundle bundle) {
        }

        @Override // android.location.LocationListener
        public void onProviderEnabled(String str) {
        }

        @Override // android.location.LocationListener
        public void onProviderDisabled(String str) {
        }

        @Override // android.location.LocationListener
        public void onLocationChanged(Location location) {
            if (location != null) {
                dw.this.i = location;
            }
            if (dw.a(dw.this) >= 3) {
                el.a(4, dw.c, "Max location reports reached, stopping");
                dw.this.i();
            }
        }
    }

    @Override // com.flurry.sdk.dn.a
    public void a(String str, Object obj) {
        if (str.equals("LocationCriteria")) {
            this.h = (Criteria) obj;
            el.a(4, c, "onSettingUpdate, LocationCriteria = " + this.h);
            if (this.a) {
                j();
                return;
            }
            return;
        }
        if (str.equals("ReportLocation")) {
            this.b = ((Boolean) obj).booleanValue();
            el.a(4, c, "onSettingUpdate, ReportLocation = " + this.b);
            if (this.b) {
                if (!this.a && this.l > 0) {
                    j();
                    return;
                }
                return;
            }
            i();
            return;
        }
        el.a(6, c, "LocationProvider internal error! Had to be LocationCriteria or ReportLocation key.");
    }
}
