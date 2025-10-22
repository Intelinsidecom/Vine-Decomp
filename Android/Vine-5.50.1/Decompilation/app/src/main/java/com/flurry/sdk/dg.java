package com.flurry.sdk;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.SystemClock;
import com.flurry.android.impl.analytics.FlurryAnalyticsModule;
import com.flurry.sdk.cu;
import com.flurry.sdk.dj;
import com.flurry.sdk.dn;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: classes.dex */
public class dg implements dj.b, dn.a {
    private Location D;
    private boolean E;
    private String F;
    private byte G;
    private long H;
    private long I;
    private boolean L;
    private int M;
    private int O;
    private int P;
    private Map<String, List<String>> R;
    private dj S;
    private int T;
    private final String k;
    private String l;
    private WeakReference<Context> m;
    private List<de> o;
    private boolean q;
    private long r;
    private AdvertisingIdClient.Info t;
    private byte[] u;
    private String v;
    private long w;
    private long x;
    private long y;
    private long z;
    private static final String g = dg.class.getSimpleName();
    static int a = 100;
    static int b = 10;
    static int c = 1000;
    static int d = 160000;
    static int e = 50;
    static int f = 20;
    private final AtomicInteger h = new AtomicInteger(0);
    private final AtomicInteger i = new AtomicInteger(0);
    private final AtomicInteger j = new AtomicInteger(0);
    private File n = null;
    private final Map<Cdo, ByteBuffer> p = new HashMap();
    private List<de> s = new ArrayList();
    private String A = "";
    private String B = "";
    private byte C = -1;
    private final Map<String, cu.a> J = new HashMap();
    private final List<cy> K = new ArrayList();
    private final List<cx> N = new ArrayList();
    private final cv Q = new cv();
    private boolean U = false;

    Map<String, List<String>> a() {
        return this.R;
    }

    public void a(fc fcVar) {
        dl.a().c(fcVar);
    }

    public void b() {
        this.q = true;
    }

    public dg(String str) {
        el.a(4, g, "Creating new Flurry session");
        this.k = str;
        this.m = new WeakReference<>(null);
    }

    private void u() {
        dn dnVarA = dm.a();
        this.G = ((Byte) dnVarA.a("Gender")).byteValue();
        dnVarA.a("Gender", (dn.a) this);
        el.a(4, g, "initSettings, Gender = " + ((int) this.G));
        this.F = (String) dnVarA.a("UserId");
        dnVarA.a("UserId", (dn.a) this);
        el.a(4, g, "initSettings, UserId = " + this.F);
        this.E = ((Boolean) dnVarA.a("LogEvents")).booleanValue();
        dnVarA.a("LogEvents", (dn.a) this);
        el.a(4, g, "initSettings, LogEvents = " + this.E);
        this.H = ((Long) dnVarA.a("Age")).longValue();
        dnVarA.a("Age", (dn.a) this);
        el.a(4, g, "initSettings, BirthDate = " + this.H);
        this.I = ((Long) dnVarA.a("ContinueSessionMillis")).longValue();
        dnVarA.a("ContinueSessionMillis", (dn.a) this);
        el.a(4, g, "initSettings, ContinueSessionMillis = " + this.I);
    }

    @Override // com.flurry.sdk.dn.a
    public void a(String str, Object obj) {
        if (str.equals("Gender")) {
            this.G = ((Byte) obj).byteValue();
            el.a(4, g, "onSettingUpdate, Gender = " + ((int) this.G));
            return;
        }
        if (str.equals("UserId")) {
            this.F = (String) obj;
            el.a(4, g, "onSettingUpdate, UserId = " + this.F);
            return;
        }
        if (str.equals("LogEvents")) {
            this.E = ((Boolean) obj).booleanValue();
            el.a(4, g, "onSettingUpdate, LogEvents = " + this.E);
        } else if (str.equals("Age")) {
            this.H = ((Long) obj).longValue();
            el.a(4, g, "onSettingUpdate, Birthdate = " + this.H);
        } else if (str.equals("ContinueSessionMillis")) {
            this.I = ((Long) obj).longValue();
            el.a(4, g, "onSettingUpdate, ContinueSessionMillis = " + this.I);
        } else {
            el.a(6, g, "onSettingUpdate internal error!");
        }
    }

    public synchronized void a(Context context) {
        el.a(4, g, "Initializing new Flurry session with context:" + context);
        this.m = new WeakReference<>(context);
        this.S = new dj(this);
        this.n = context.getFileStreamPath(F());
        this.l = dy.a();
        this.y = -1L;
        this.O = 0;
        this.B = TimeZone.getDefault().getID();
        this.A = Locale.getDefault().getLanguage() + "_" + Locale.getDefault().getCountry();
        this.L = true;
        this.M = 0;
        this.P = 0;
        this.R = d(context);
        u();
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        this.w = System.currentTimeMillis();
        this.x = jElapsedRealtime;
        a(new fc() { // from class: com.flurry.sdk.dg.1
            @Override // com.flurry.sdk.fc
            public void a() {
                dg.this.t = dt.a();
            }
        });
        a(new fc() { // from class: com.flurry.sdk.dg.2
            @Override // com.flurry.sdk.fc
            public void a() {
                dg.this.u = dv.a();
            }
        });
        a(new fc() { // from class: com.flurry.sdk.dg.3
            @Override // com.flurry.sdk.fc
            public void a() {
                dg.this.v = dx.a();
            }
        });
        a(new fc() { // from class: com.flurry.sdk.dg.4
            @Override // com.flurry.sdk.fc
            public void a() {
                dg.this.C();
            }
        });
        a(new fc() { // from class: com.flurry.sdk.dg.5
            @Override // com.flurry.sdk.fc
            public void a() {
                dg.this.v();
            }
        });
        em.a().a(this, context);
        this.U = true;
    }

    public synchronized void b(Context context) {
        if (this.U) {
            el.d(g, "Start session with context: " + context + " count:" + g());
            this.m = new WeakReference<>(context);
            if (this.S.b()) {
                this.S.a();
            }
            D();
            dw.a().c();
            this.D = dw.a().f();
            em.a().b(this, context);
        }
    }

    public synchronized void c(Context context) {
        if (this.U) {
            em.a().c(this, context);
            this.D = dw.a().f();
            z();
            E();
            el.d(g, "End session with context: " + context + " count:" + g());
            this.y = SystemClock.elapsedRealtime() - this.x;
            a(this.y);
            w();
            if (g() == 0) {
                this.S.a(this.I);
            }
        }
    }

    public synchronized void c() {
        if (this.U) {
            el.d(g, "Finalize session");
            if (this.S.b()) {
                this.S.a();
            }
            if (g() != 0) {
                el.a(6, g, "Session with apiKey = " + j() + " was ended while getSessionCount() is not 0");
            }
            this.T = 0;
            x();
            em.a().a(this);
            a(new fc() { // from class: com.flurry.sdk.dg.6
                @Override // com.flurry.sdk.fc
                public void a() {
                    di.a().a(dg.this.j());
                }
            });
            dm.a().b("Gender", (dn.a) this);
            dm.a().b("UserId", (dn.a) this);
            dm.a().b("Age", (dn.a) this);
            dm.a().b("LogEvents", (dn.a) this);
            dm.a().b("ContinueSessionMillis", (dn.a) this);
        }
    }

    private Map<String, List<String>> d(Context context) {
        Bundle extras;
        if (!(context instanceof Activity) || (extras = ((Activity) context).getIntent().getExtras()) == null) {
            return null;
        }
        el.a(3, g, "Launch Options Bundle is present " + extras.toString());
        HashMap map = new HashMap();
        for (String str : extras.keySet()) {
            if (str != null) {
                Object obj = extras.get(str);
                String string = obj != null ? obj.toString() : "null";
                map.put(str, new ArrayList(Arrays.asList(string)));
                el.a(3, g, "Launch options Key: " + str + ". Its value: " + string);
            }
        }
        return map;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void v() {
        try {
            if (this.v != null) {
                el.a(3, g, "Fetched phone id");
                a(Cdo.PhoneId, ByteBuffer.wrap(this.v.getBytes()));
            }
            if (this.u != null) {
                el.a(3, g, "Fetched hashed IMEI");
                a(Cdo.Sha1Imei, ByteBuffer.wrap(this.u));
            }
            if (this.t != null) {
                el.a(3, g, "Fetched advertising id");
                a(Cdo.AndroidAdvertisingId, ByteBuffer.wrap(this.t.getId().getBytes()));
            }
            A();
        } catch (Throwable th) {
            el.a(6, g, "", th);
        }
    }

    private synchronized void a(long j) {
        for (cy cyVar : this.K) {
            if (cyVar.a() && !cyVar.b()) {
                cyVar.a(j);
            }
        }
    }

    private void w() {
        a(new fc() { // from class: com.flurry.sdk.dg.7
            @Override // com.flurry.sdk.fc
            public void a() {
                de deVarD = dg.this.d();
                dg.this.s.clear();
                dg.this.s.add(deVarD);
                dg.this.B();
            }
        });
    }

    private void x() {
        a(new fc() { // from class: com.flurry.sdk.dg.8
            @Override // com.flurry.sdk.fc
            public void a() {
                dg.this.y();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void y() {
        boolean z;
        try {
            synchronized (this) {
                z = this.s.size() > 0;
            }
            if (z) {
                A();
            }
        } catch (Throwable th) {
            el.a(6, g, "", th);
        }
    }

    private void z() {
        a(new fc() { // from class: com.flurry.sdk.dg.9
            @Override // com.flurry.sdk.fc
            public void a() {
                dw.a().d();
            }
        });
    }

    synchronized de d() {
        de deVar;
        df dfVar = new df();
        dfVar.a(this.l);
        dfVar.a(this.w);
        dfVar.b(this.y);
        dfVar.c(this.z);
        dfVar.b(k());
        dfVar.c(l());
        dfVar.a((int) this.C);
        dfVar.d(h());
        dfVar.a(this.D);
        dfVar.b(f());
        dfVar.a(this.G);
        dfVar.a(Long.valueOf(this.H));
        dfVar.a(t());
        dfVar.a(r());
        dfVar.a(this.L);
        dfVar.b(s());
        dfVar.c(this.O);
        try {
            deVar = new de(dfVar);
        } catch (IOException e2) {
            e2.printStackTrace();
            deVar = null;
        }
        if (deVar == null) {
            el.d(g, "New session report wasn't created");
        }
        return deVar;
    }

    public synchronized void e() {
        this.P++;
    }

    int f() {
        return this.P;
    }

    public synchronized void a(String str, Map<String, String> map, boolean z) {
        long jElapsedRealtime = SystemClock.elapsedRealtime() - this.x;
        String strA = fb.a(str);
        if (strA.length() != 0) {
            cu.a aVar = this.J.get(strA);
            if (aVar == null) {
                if (this.J.size() < a) {
                    cu.a aVar2 = new cu.a();
                    aVar2.a = 1;
                    this.J.put(strA, aVar2);
                    el.d(g, "Event count started: " + strA);
                } else {
                    el.d(g, "Too many different events. Event not counted: " + strA);
                }
            } else {
                aVar.a++;
                el.d(g, "Event count incremented: " + strA);
            }
            if (this.E && this.K.size() < c && this.M < d) {
                Map<String, String> mapEmptyMap = map == null ? Collections.emptyMap() : map;
                if (mapEmptyMap.size() > b) {
                    el.d(g, "MaxEventParams exceeded: " + mapEmptyMap.size());
                } else {
                    cy cyVar = new cy(G(), strA, mapEmptyMap, jElapsedRealtime, z);
                    if (cyVar.d() + this.M <= d) {
                        this.K.add(cyVar);
                        this.M = cyVar.d() + this.M;
                    } else {
                        this.M = d;
                        this.L = false;
                        el.d(g, "Event Log size exceeded. No more event details logged.");
                    }
                }
            } else {
                this.L = false;
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x0026, code lost:
    
        if (r9.size() <= 0) goto L19;
     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x002c, code lost:
    
        if (r7.M >= com.flurry.sdk.dg.d) goto L19;
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x002e, code lost:
    
        r1 = r7.M - r0.d();
        r4 = new java.util.HashMap(r0.c());
        r0.a(r9);
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x0048, code lost:
    
        if ((r0.d() + r1) > com.flurry.sdk.dg.d) goto L27;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x0054, code lost:
    
        if (r0.c().size() <= com.flurry.sdk.dg.b) goto L22;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0056, code lost:
    
        com.flurry.sdk.el.d(com.flurry.sdk.dg.g, "MaxEventParams exceeded on endEvent: " + r0.c().size());
        r0.b(r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0079, code lost:
    
        r0.a(r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x007e, code lost:
    
        r7.M = r1 + r0.d();
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x0089, code lost:
    
        r0.b(r4);
        r7.L = false;
        r7.M = com.flurry.sdk.dg.d;
        com.flurry.sdk.el.d(com.flurry.sdk.dg.g, "Event Log size exceeded. No more event details logged.");
     */
    /* JADX WARN: Code restructure failed: missing block: B:8:0x0019, code lost:
    
        r2 = android.os.SystemClock.elapsedRealtime() - r7.x;
     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x0020, code lost:
    
        if (r9 == null) goto L19;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public synchronized void a(java.lang.String r8, java.util.Map<java.lang.String, java.lang.String> r9) {
        /*
            r7 = this;
            monitor-enter(r7)
            java.util.List<com.flurry.sdk.cy> r0 = r7.K     // Catch: java.lang.Throwable -> L86
            java.util.Iterator r1 = r0.iterator()     // Catch: java.lang.Throwable -> L86
        L7:
            boolean r0 = r1.hasNext()     // Catch: java.lang.Throwable -> L86
            if (r0 == 0) goto L7c
            java.lang.Object r0 = r1.next()     // Catch: java.lang.Throwable -> L86
            com.flurry.sdk.cy r0 = (com.flurry.sdk.cy) r0     // Catch: java.lang.Throwable -> L86
            boolean r2 = r0.a(r8)     // Catch: java.lang.Throwable -> L86
            if (r2 == 0) goto L7
            long r2 = android.os.SystemClock.elapsedRealtime()     // Catch: java.lang.Throwable -> L86
            long r4 = r7.x     // Catch: java.lang.Throwable -> L86
            long r2 = r2 - r4
            if (r9 == 0) goto L79
            int r1 = r9.size()     // Catch: java.lang.Throwable -> L86
            if (r1 <= 0) goto L79
            int r1 = r7.M     // Catch: java.lang.Throwable -> L86
            int r4 = com.flurry.sdk.dg.d     // Catch: java.lang.Throwable -> L86
            if (r1 >= r4) goto L79
            int r1 = r7.M     // Catch: java.lang.Throwable -> L86
            int r4 = r0.d()     // Catch: java.lang.Throwable -> L86
            int r1 = r1 - r4
            java.util.HashMap r4 = new java.util.HashMap     // Catch: java.lang.Throwable -> L86
            java.util.Map r5 = r0.c()     // Catch: java.lang.Throwable -> L86
            r4.<init>(r5)     // Catch: java.lang.Throwable -> L86
            r0.a(r9)     // Catch: java.lang.Throwable -> L86
            int r5 = r0.d()     // Catch: java.lang.Throwable -> L86
            int r5 = r5 + r1
            int r6 = com.flurry.sdk.dg.d     // Catch: java.lang.Throwable -> L86
            if (r5 > r6) goto L89
            java.util.Map r5 = r0.c()     // Catch: java.lang.Throwable -> L86
            int r5 = r5.size()     // Catch: java.lang.Throwable -> L86
            int r6 = com.flurry.sdk.dg.b     // Catch: java.lang.Throwable -> L86
            if (r5 <= r6) goto L7e
            java.lang.String r1 = com.flurry.sdk.dg.g     // Catch: java.lang.Throwable -> L86
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L86
            r5.<init>()     // Catch: java.lang.Throwable -> L86
            java.lang.String r6 = "MaxEventParams exceeded on endEvent: "
            java.lang.StringBuilder r5 = r5.append(r6)     // Catch: java.lang.Throwable -> L86
            java.util.Map r6 = r0.c()     // Catch: java.lang.Throwable -> L86
            int r6 = r6.size()     // Catch: java.lang.Throwable -> L86
            java.lang.StringBuilder r5 = r5.append(r6)     // Catch: java.lang.Throwable -> L86
            java.lang.String r5 = r5.toString()     // Catch: java.lang.Throwable -> L86
            com.flurry.sdk.el.d(r1, r5)     // Catch: java.lang.Throwable -> L86
            r0.b(r4)     // Catch: java.lang.Throwable -> L86
        L79:
            r0.a(r2)     // Catch: java.lang.Throwable -> L86
        L7c:
            monitor-exit(r7)
            return
        L7e:
            int r4 = r0.d()     // Catch: java.lang.Throwable -> L86
            int r1 = r1 + r4
            r7.M = r1     // Catch: java.lang.Throwable -> L86
            goto L79
        L86:
            r0 = move-exception
            monitor-exit(r7)
            throw r0
        L89:
            r0.b(r4)     // Catch: java.lang.Throwable -> L86
            r1 = 0
            r7.L = r1     // Catch: java.lang.Throwable -> L86
            int r1 = com.flurry.sdk.dg.d     // Catch: java.lang.Throwable -> L86
            r7.M = r1     // Catch: java.lang.Throwable -> L86
            java.lang.String r1 = com.flurry.sdk.dg.g     // Catch: java.lang.Throwable -> L86
            java.lang.String r4 = "Event Log size exceeded. No more event details logged."
            com.flurry.sdk.el.d(r1, r4)     // Catch: java.lang.Throwable -> L86
            goto L79
        */
        throw new UnsupportedOperationException("Method not decompiled: com.flurry.sdk.dg.a(java.lang.String, java.util.Map):void");
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x005a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public synchronized void a(java.lang.String r10, java.lang.String r11, java.lang.String r12, java.lang.Throwable r13) {
        /*
            r9 = this;
            r0 = 0
            monitor-enter(r9)
            if (r10 == 0) goto L5a
            java.lang.String r1 = "uncaught"
            boolean r1 = r1.equals(r10)     // Catch: java.lang.Throwable -> La1
            if (r1 == 0) goto L5a
            r1 = 1
        Le:
            int r2 = r9.O     // Catch: java.lang.Throwable -> La1
            int r2 = r2 + 1
            r9.O = r2     // Catch: java.lang.Throwable -> La1
            java.util.List<com.flurry.sdk.cx> r2 = r9.N     // Catch: java.lang.Throwable -> La1
            int r2 = r2.size()     // Catch: java.lang.Throwable -> La1
            int r3 = com.flurry.sdk.dg.e     // Catch: java.lang.Throwable -> La1
            if (r2 >= r3) goto L5c
            long r0 = java.lang.System.currentTimeMillis()     // Catch: java.lang.Throwable -> La1
            java.lang.Long r2 = java.lang.Long.valueOf(r0)     // Catch: java.lang.Throwable -> La1
            com.flurry.sdk.cx r0 = new com.flurry.sdk.cx     // Catch: java.lang.Throwable -> La1
            int r1 = r9.H()     // Catch: java.lang.Throwable -> La1
            long r2 = r2.longValue()     // Catch: java.lang.Throwable -> La1
            r4 = r10
            r5 = r11
            r6 = r12
            r7 = r13
            r0.<init>(r1, r2, r4, r5, r6, r7)     // Catch: java.lang.Throwable -> La1
            java.util.List<com.flurry.sdk.cx> r1 = r9.N     // Catch: java.lang.Throwable -> La1
            r1.add(r0)     // Catch: java.lang.Throwable -> La1
            java.lang.String r1 = com.flurry.sdk.dg.g     // Catch: java.lang.Throwable -> La1
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> La1
            r2.<init>()     // Catch: java.lang.Throwable -> La1
            java.lang.String r3 = "Error logged: "
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch: java.lang.Throwable -> La1
            java.lang.String r0 = r0.c()     // Catch: java.lang.Throwable -> La1
            java.lang.StringBuilder r0 = r2.append(r0)     // Catch: java.lang.Throwable -> La1
            java.lang.String r0 = r0.toString()     // Catch: java.lang.Throwable -> La1
            com.flurry.sdk.el.d(r1, r0)     // Catch: java.lang.Throwable -> La1
        L58:
            monitor-exit(r9)
            return
        L5a:
            r1 = r0
            goto Le
        L5c:
            if (r1 == 0) goto La8
            r8 = r0
        L5f:
            java.util.List<com.flurry.sdk.cx> r0 = r9.N     // Catch: java.lang.Throwable -> La1
            int r0 = r0.size()     // Catch: java.lang.Throwable -> La1
            if (r8 >= r0) goto L58
            java.util.List<com.flurry.sdk.cx> r0 = r9.N     // Catch: java.lang.Throwable -> La1
            java.lang.Object r0 = r0.get(r8)     // Catch: java.lang.Throwable -> La1
            com.flurry.sdk.cx r0 = (com.flurry.sdk.cx) r0     // Catch: java.lang.Throwable -> La1
            java.lang.String r1 = r0.c()     // Catch: java.lang.Throwable -> La1
            if (r1 == 0) goto La4
            java.lang.String r1 = "uncaught"
            java.lang.String r0 = r0.c()     // Catch: java.lang.Throwable -> La1
            boolean r0 = r1.equals(r0)     // Catch: java.lang.Throwable -> La1
            if (r0 != 0) goto La4
            long r0 = java.lang.System.currentTimeMillis()     // Catch: java.lang.Throwable -> La1
            java.lang.Long r2 = java.lang.Long.valueOf(r0)     // Catch: java.lang.Throwable -> La1
            com.flurry.sdk.cx r0 = new com.flurry.sdk.cx     // Catch: java.lang.Throwable -> La1
            int r1 = r9.H()     // Catch: java.lang.Throwable -> La1
            long r2 = r2.longValue()     // Catch: java.lang.Throwable -> La1
            r4 = r10
            r5 = r11
            r6 = r12
            r7 = r13
            r0.<init>(r1, r2, r4, r5, r6, r7)     // Catch: java.lang.Throwable -> La1
            java.util.List<com.flurry.sdk.cx> r1 = r9.N     // Catch: java.lang.Throwable -> La1
            r1.set(r8, r0)     // Catch: java.lang.Throwable -> La1
            goto L58
        La1:
            r0 = move-exception
            monitor-exit(r9)
            throw r0
        La4:
            int r0 = r8 + 1
            r8 = r0
            goto L5f
        La8:
            java.lang.String r0 = com.flurry.sdk.dg.g     // Catch: java.lang.Throwable -> La1
            java.lang.String r1 = "Max errors logged. No more errors logged."
            com.flurry.sdk.el.d(r0, r1)     // Catch: java.lang.Throwable -> La1
            goto L58
        */
        throw new UnsupportedOperationException("Method not decompiled: com.flurry.sdk.dg.a(java.lang.String, java.lang.String, java.lang.String, java.lang.Throwable):void");
    }

    private void A() {
        try {
            el.a(3, g, "generating agent report");
            cz czVar = new cz(this.k, this.l, this.q, o(), this.r, this.w, this.s, p(), this.Q.a(false), a(), cu.a().b(), System.currentTimeMillis());
            this.o = new ArrayList(this.s);
            if (czVar != null && czVar.a() != null) {
                el.a(3, g, "generated report of size " + czVar.a().length + " with " + this.s.size() + " reports.");
                a(czVar.a());
                this.s.removeAll(this.o);
                this.o = null;
                B();
            } else {
                el.d(g, "Error generating report");
            }
        } catch (Throwable th) {
            el.a(6, g, "", th);
        }
    }

    private void a(byte[] bArr) {
        FlurryAnalyticsModule.getInstance().a().b(bArr, this.k, "" + dk.a().b());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void B() {
        if (!fa.a(this.n)) {
            el.d(g, "Error persisting report: could not create directory");
        } else {
            try {
                DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(this.n));
                dh dhVar = new dh();
                dhVar.a(this.q);
                dhVar.a(this.r);
                dhVar.a(this.s);
                dhVar.a(dataOutputStream, this.k, i());
            } catch (Exception e2) {
                el.b(g, "Error saving persistent data", e2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:13:0x005b A[Catch: all -> 0x008e, TryCatch #7 {, blocks: (B:5:0x0004, B:11:0x0053, B:13:0x005b, B:15:0x006a, B:24:0x0087, B:25:0x008d, B:21:0x007c, B:30:0x0092), top: B:44:0x0004 }] */
    /* JADX WARN: Removed duplicated region for block: B:15:0x006a A[Catch: all -> 0x008e, TRY_LEAVE, TryCatch #7 {, blocks: (B:5:0x0004, B:11:0x0053, B:13:0x005b, B:15:0x006a, B:24:0x0087, B:25:0x008d, B:21:0x007c, B:30:0x0092), top: B:44:0x0004 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public synchronized void C() {
        /*
            r8 = this;
            r2 = 0
            r4 = 0
            monitor-enter(r8)
            r0 = 4
            java.lang.String r1 = com.flurry.sdk.dg.g     // Catch: java.lang.Throwable -> L8e
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L8e
            r3.<init>()     // Catch: java.lang.Throwable -> L8e
            java.lang.String r5 = "Loading persistent data: "
            java.lang.StringBuilder r3 = r3.append(r5)     // Catch: java.lang.Throwable -> L8e
            java.io.File r5 = r8.n     // Catch: java.lang.Throwable -> L8e
            java.lang.String r5 = r5.getAbsolutePath()     // Catch: java.lang.Throwable -> L8e
            java.lang.StringBuilder r3 = r3.append(r5)     // Catch: java.lang.Throwable -> L8e
            java.lang.String r3 = r3.toString()     // Catch: java.lang.Throwable -> L8e
            com.flurry.sdk.el.a(r0, r1, r3)     // Catch: java.lang.Throwable -> L8e
            java.io.File r0 = r8.n     // Catch: java.lang.Throwable -> L8e
            boolean r0 = r0.exists()     // Catch: java.lang.Throwable -> L8e
            if (r0 == 0) goto L91
            java.io.FileInputStream r3 = new java.io.FileInputStream     // Catch: java.lang.Exception -> L73 java.lang.Throwable -> L84
            java.io.File r0 = r8.n     // Catch: java.lang.Exception -> L73 java.lang.Throwable -> L84
            r3.<init>(r0)     // Catch: java.lang.Exception -> L73 java.lang.Throwable -> L84
            java.io.DataInputStream r1 = new java.io.DataInputStream     // Catch: java.lang.Throwable -> L9b java.lang.Exception -> La3
            r1.<init>(r3)     // Catch: java.lang.Throwable -> L9b java.lang.Exception -> La3
            com.flurry.sdk.dh r0 = new com.flurry.sdk.dh     // Catch: java.lang.Throwable -> L9e java.lang.Exception -> La7
            r0.<init>()     // Catch: java.lang.Throwable -> L9e java.lang.Exception -> La7
            java.lang.String r2 = r8.k     // Catch: java.lang.Throwable -> L9e java.lang.Exception -> La7
            r0.a(r1, r2)     // Catch: java.lang.Throwable -> L9e java.lang.Exception -> La7
            boolean r2 = r0.a()     // Catch: java.lang.Throwable -> L9e java.lang.Exception -> La7
            r8.q = r2     // Catch: java.lang.Throwable -> L9e java.lang.Exception -> La7
            long r6 = r0.c()     // Catch: java.lang.Throwable -> L9e java.lang.Exception -> La7
            r8.r = r6     // Catch: java.lang.Throwable -> L9e java.lang.Exception -> La7
            java.util.List r0 = r0.b()     // Catch: java.lang.Throwable -> L9e java.lang.Exception -> La7
            r8.s = r0     // Catch: java.lang.Throwable -> L9e java.lang.Exception -> La7
            r0 = 1
            com.flurry.sdk.fb.a(r1)     // Catch: java.lang.Throwable -> L8e
            com.flurry.sdk.fb.a(r3)     // Catch: java.lang.Throwable -> L8e
        L59:
            if (r0 != 0) goto L68
            r1 = 3
            java.lang.String r2 = com.flurry.sdk.dg.g     // Catch: java.lang.Throwable -> L8e
            java.lang.String r3 = "Deleting agent cache file"
            com.flurry.sdk.el.a(r1, r2, r3)     // Catch: java.lang.Throwable -> L8e
            java.io.File r1 = r8.n     // Catch: java.lang.Throwable -> L8e
            r1.delete()     // Catch: java.lang.Throwable -> L8e
        L68:
            if (r0 != 0) goto L71
            r0 = 0
            r8.q = r0     // Catch: java.lang.Throwable -> L8e
            long r0 = r8.w     // Catch: java.lang.Throwable -> L8e
            r8.r = r0     // Catch: java.lang.Throwable -> L8e
        L71:
            monitor-exit(r8)
            return
        L73:
            r0 = move-exception
            r1 = r2
        L75:
            java.lang.String r3 = com.flurry.sdk.dg.g     // Catch: java.lang.Throwable -> La0
            java.lang.String r5 = "Error loading persistent data"
            com.flurry.sdk.el.b(r3, r5, r0)     // Catch: java.lang.Throwable -> La0
            com.flurry.sdk.fb.a(r1)     // Catch: java.lang.Throwable -> L8e
            com.flurry.sdk.fb.a(r2)     // Catch: java.lang.Throwable -> L8e
            r0 = r4
            goto L59
        L84:
            r0 = move-exception
            r1 = r2
            r3 = r2
        L87:
            com.flurry.sdk.fb.a(r1)     // Catch: java.lang.Throwable -> L8e
            com.flurry.sdk.fb.a(r3)     // Catch: java.lang.Throwable -> L8e
            throw r0     // Catch: java.lang.Throwable -> L8e
        L8e:
            r0 = move-exception
            monitor-exit(r8)
            throw r0
        L91:
            r0 = 4
            java.lang.String r1 = com.flurry.sdk.dg.g     // Catch: java.lang.Throwable -> L8e
            java.lang.String r2 = "Agent cache file doesn't exist."
            com.flurry.sdk.el.a(r0, r1, r2)     // Catch: java.lang.Throwable -> L8e
            r0 = r4
            goto L68
        L9b:
            r0 = move-exception
            r1 = r2
            goto L87
        L9e:
            r0 = move-exception
            goto L87
        La0:
            r0 = move-exception
            r3 = r2
            goto L87
        La3:
            r0 = move-exception
            r1 = r2
            r2 = r3
            goto L75
        La7:
            r0 = move-exception
            r2 = r3
            goto L75
        */
        throw new UnsupportedOperationException("Method not decompiled: com.flurry.sdk.dg.C():void");
    }

    private void D() {
        this.T++;
    }

    private void E() {
        if (this.T > 0) {
            this.T--;
        }
    }

    int g() {
        return this.T;
    }

    private String F() {
        return ".flurryagent." + Integer.toString(this.k.hashCode(), 16);
    }

    private int G() {
        return this.h.incrementAndGet();
    }

    private int H() {
        return this.i.incrementAndGet();
    }

    String h() {
        return this.F == null ? "" : this.F;
    }

    public String i() {
        return this.v;
    }

    public String j() {
        return this.k;
    }

    public String k() {
        return this.A;
    }

    public String l() {
        return this.B;
    }

    public long m() {
        return this.w;
    }

    public long n() {
        return this.x;
    }

    public boolean o() {
        return this.t == null || !this.t.isLimitAdTrackingEnabled();
    }

    private void a(Cdo cdo, ByteBuffer byteBuffer) {
        synchronized (this.p) {
            this.p.put(cdo, byteBuffer);
        }
    }

    public Map<Cdo, ByteBuffer> p() {
        return new HashMap(this.p);
    }

    @Override // com.flurry.sdk.dj.b
    public void q() {
        c();
    }

    List<cy> r() {
        return this.K;
    }

    List<cx> s() {
        return this.N;
    }

    Map<String, cu.a> t() {
        return this.J;
    }
}
