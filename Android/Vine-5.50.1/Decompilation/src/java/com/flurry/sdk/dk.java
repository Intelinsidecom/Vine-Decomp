package com.flurry.sdk;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;

/* loaded from: classes.dex */
public class dk {
    private static final String a = dk.class.getSimpleName();
    private static dk b;

    public static synchronized dk a() {
        if (b == null) {
            b = new dk();
        }
        return b;
    }

    public int b() {
        int iIntValue = ((Integer) dm.a().a("AgentVersion")).intValue();
        el.a(4, a, "getAgentVersion() = " + iIntValue);
        return iIntValue;
    }

    int c() {
        return ((Integer) dm.a().a("ReleaseMajorVersion")).intValue();
    }

    int d() {
        return ((Integer) dm.a().a("ReleaseMinorVersion")).intValue();
    }

    int e() {
        return ((Integer) dm.a().a("ReleasePatchVersion")).intValue();
    }

    String f() {
        return (String) dm.a().a("ReleaseBetaVersion");
    }

    public String g() {
        String str;
        if (f().length() > 0) {
            str = ".";
        } else {
            str = "";
        }
        return String.format(Locale.getDefault(), "Flurry_Android_%d_%d.%d.%d%s%s", Integer.valueOf(b()), Integer.valueOf(c()), Integer.valueOf(d()), Integer.valueOf(e()), str, f());
    }

    public String h() {
        dg dgVarC = di.a().c();
        if (dgVarC == null) {
            return null;
        }
        return dgVarC.j();
    }

    public String i() {
        dg dgVarC = di.a().c();
        if (dgVarC == null) {
            return null;
        }
        return dgVarC.k();
    }

    public String j() {
        dg dgVarC = di.a().c();
        if (dgVarC == null) {
            return null;
        }
        return dgVarC.l();
    }

    public boolean k() {
        dg dgVarC = di.a().c();
        if (dgVarC != null) {
            return dgVarC.o();
        }
        return true;
    }

    public Map<Cdo, ByteBuffer> l() {
        dg dgVarC = di.a().c();
        return dgVarC != null ? dgVarC.p() : Collections.emptyMap();
    }
}
