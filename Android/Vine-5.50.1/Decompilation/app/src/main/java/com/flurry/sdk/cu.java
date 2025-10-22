package com.flurry.sdk;

import com.flurry.android.FlurryAgent;
import com.flurry.sdk.dn;
import java.lang.Thread;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class cu implements dn.a, Thread.UncaughtExceptionHandler {
    private static final String a = cu.class.getSimpleName();
    private static cu b;
    private final HashMap<String, Map<String, String>> c = new HashMap<>();
    private boolean d;

    public static class a {
        public int a;
    }

    private cu() {
        dz.a().a(this);
        d();
    }

    private void d() {
        dn dnVarA = dm.a();
        this.d = ((Boolean) dnVarA.a("CaptureUncaughtExceptions")).booleanValue();
        dnVarA.a("CaptureUncaughtExceptions", (dn.a) this);
        el.a(4, a, "initSettings, CrashReportingEnabled = " + this.d);
        String str = (String) dnVarA.a("VersionName");
        dnVarA.a("VersionName", (dn.a) this);
        dy.a(str);
        el.a(4, a, "initSettings, VersionName = " + str);
    }

    @Override // com.flurry.sdk.dn.a
    public void a(String str, Object obj) {
        if (str.equals("CaptureUncaughtExceptions")) {
            this.d = ((Boolean) obj).booleanValue();
            el.a(4, a, "onSettingUpdate, CrashReportingEnabled = " + this.d);
        } else {
            if (str.equals("VersionName")) {
                String str2 = (String) obj;
                dy.a(str2);
                el.a(4, a, "onSettingUpdate, VersionName = " + str2);
                return;
            }
            el.a(6, a, "onSettingUpdate internal error!");
        }
    }

    public static synchronized cu a() {
        if (b == null) {
            b = new cu();
        }
        return b;
    }

    public void a(String str, String str2, Map<String, String> map) {
        if (map == null) {
            map = new HashMap<>();
        }
        if (map.size() >= 10) {
            el.d(a, "MaxOriginParams exceeded: " + map.size());
            return;
        }
        map.put("flurryOriginVersion", str2);
        synchronized (this.c) {
            if (this.c.size() >= 10 && !this.c.containsKey(str)) {
                el.d(a, "MaxOrigins exceeded: " + this.c.size());
            } else {
                this.c.put(str, map);
            }
        }
    }

    public HashMap<String, Map<String, String>> b() {
        HashMap<String, Map<String, String>> map;
        synchronized (this.c) {
            map = new HashMap<>(this.c);
        }
        return map;
    }

    public void a(String str) {
        dg dgVarC = di.a().c();
        if (dgVarC != null) {
            dgVarC.a(str, null, false);
        }
    }

    public void a(String str, Map<String, String> map) {
        dg dgVarC = di.a().c();
        if (dgVarC != null) {
            dgVarC.a(str, map, false);
        }
    }

    public void a(String str, boolean z) {
        dg dgVarC = di.a().c();
        if (dgVarC != null) {
            dgVarC.a(str, null, z);
        }
    }

    public void a(String str, Map<String, String> map, boolean z) {
        dg dgVarC = di.a().c();
        if (dgVarC != null) {
            dgVarC.a(str, map, z);
        }
    }

    public void b(String str) {
        dg dgVarC = di.a().c();
        if (dgVarC != null) {
            dgVarC.a(str, (Map<String, String>) null);
        }
    }

    public void b(String str, Map<String, String> map) {
        dg dgVarC = di.a().c();
        if (dgVarC != null) {
            dgVarC.a(str, map);
        }
    }

    @Deprecated
    public void a(String str, String str2, String str3) {
        StackTraceElement[] stackTraceElementArr;
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (stackTrace == null || stackTrace.length <= 2) {
            stackTraceElementArr = stackTrace;
        } else {
            stackTraceElementArr = new StackTraceElement[stackTrace.length - 2];
            System.arraycopy(stackTrace, 2, stackTraceElementArr, 0, stackTraceElementArr.length);
        }
        Throwable th = new Throwable(str2);
        th.setStackTrace(stackTraceElementArr);
        dg dgVarC = di.a().c();
        if (dgVarC != null) {
            dgVarC.a(str, str2, str3, th);
        }
    }

    public void a(String str, String str2, Throwable th) {
        dg dgVarC = di.a().c();
        if (dgVarC != null) {
            dgVarC.a(str, str2, th.getClass().getName(), th);
        }
    }

    public void c(String str) {
        dg dgVarC = di.a().c();
        if (dgVarC != null) {
            dgVarC.a(str, null, false);
        }
    }

    public void c(String str, Map<String, String> map) {
        dg dgVarC = di.a().c();
        if (dgVarC != null) {
            dgVarC.a(str, map, false);
        }
    }

    public void c() {
        dg dgVarC = di.a().c();
        if (dgVarC != null) {
            dgVarC.e();
        }
    }

    @Override // java.lang.Thread.UncaughtExceptionHandler
    public void uncaughtException(Thread thread, Throwable th) {
        th.printStackTrace();
        if (this.d) {
            String message = "";
            StackTraceElement[] stackTrace = th.getStackTrace();
            if (stackTrace != null && stackTrace.length > 0) {
                StringBuilder sb = new StringBuilder();
                if (th.getMessage() != null) {
                    sb.append(" (" + th.getMessage() + ")\n");
                }
                message = sb.toString();
            } else if (th.getMessage() != null) {
                message = th.getMessage();
            }
            FlurryAgent.onError("uncaught", message, th);
        }
        di.a().d();
        dw.a().g();
    }

    public void a(boolean z) {
        el.a(z);
    }
}
