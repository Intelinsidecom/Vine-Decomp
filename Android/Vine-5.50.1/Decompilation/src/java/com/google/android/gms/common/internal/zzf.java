package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.content.Context;
import android.view.View;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.internal.zzsd;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/* loaded from: classes2.dex */
public final class zzf {
    private final Account zzSo;
    private final String zzTz;
    private final Set<Scope> zzaeA;
    private final int zzaeC;
    private final View zzaeD;
    private final String zzaeE;
    private final zzsd zzaeP;
    private final Set<Scope> zzajr;
    private final Map<Api<?>, zza> zzajs;
    private Integer zzajt;

    public static final class zza {
        public final Set<Scope> zzVH;
        public final boolean zzaju;
    }

    public zzf(Account account, Set<Scope> set, Map<Api<?>, zza> map, int i, View view, String str, String str2, zzsd zzsdVar) {
        this.zzSo = account;
        this.zzaeA = set == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(set);
        this.zzajs = map == null ? Collections.EMPTY_MAP : map;
        this.zzaeD = view;
        this.zzaeC = i;
        this.zzTz = str;
        this.zzaeE = str2;
        this.zzaeP = zzsdVar;
        HashSet hashSet = new HashSet(this.zzaeA);
        Iterator<zza> it = this.zzajs.values().iterator();
        while (it.hasNext()) {
            hashSet.addAll(it.next().zzVH);
        }
        this.zzajr = Collections.unmodifiableSet(hashSet);
    }

    public static zzf zzas(Context context) {
        return new GoogleApiClient.Builder(context).zzoH();
    }

    public Account getAccount() {
        return this.zzSo;
    }

    public void zza(Integer num) {
        this.zzajt = num;
    }

    public Account zzpY() {
        return this.zzSo != null ? this.zzSo : new Account("<<default account>>", "com.google");
    }

    public Set<Scope> zzqa() {
        return this.zzaeA;
    }

    public Set<Scope> zzqb() {
        return this.zzajr;
    }

    public Map<Api<?>, zza> zzqc() {
        return this.zzajs;
    }

    public String zzqd() {
        return this.zzTz;
    }

    public String zzqe() {
        return this.zzaeE;
    }

    public zzsd zzqg() {
        return this.zzaeP;
    }

    public Integer zzqh() {
        return this.zzajt;
    }
}
