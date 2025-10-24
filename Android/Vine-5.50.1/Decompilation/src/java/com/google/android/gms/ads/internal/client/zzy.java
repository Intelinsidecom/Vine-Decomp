package com.google.android.gms.ads.internal.client;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import com.google.android.gms.ads.mediation.MediationAdapter;
import com.google.android.gms.ads.search.SearchAdRequest;
import com.google.android.gms.internal.zzha;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@zzha
/* loaded from: classes.dex */
public final class zzy {
    public static final String DEVICE_ID_EMULATOR = zzl.zzcN().zzaE("emulator");
    private final Date zzaW;
    private final Set<String> zzaY;
    private final Location zzba;
    private final boolean zzpt;
    private final int zztH;
    private final int zztK;
    private final String zztL;
    private final String zztN;
    private final Bundle zztP;
    private final String zztR;
    private final boolean zztT;
    private final Bundle zzul;
    private final Map<Class<? extends Object>, Object> zzum;
    private final SearchAdRequest zzun;
    private final Set<String> zzuo;
    private final Set<String> zzup;

    public static final class zza {
        private Date zzaW;
        private Location zzba;
        private String zztL;
        private String zztN;
        private String zztR;
        private boolean zztT;
        private final HashSet<String> zzuq = new HashSet<>();
        private final Bundle zzul = new Bundle();
        private final HashMap<Class<? extends Object>, Object> zzur = new HashMap<>();
        private final HashSet<String> zzus = new HashSet<>();
        private final Bundle zztP = new Bundle();
        private final HashSet<String> zzut = new HashSet<>();
        private int zztH = -1;
        private boolean zzpt = false;
        private int zztK = -1;

        public void zzF(String str) {
            this.zzuq.add(str);
        }

        public void zzG(String str) {
            this.zzus.add(str);
        }

        public void zzH(String str) {
            this.zzus.remove(str);
        }

        public void zza(Class<? extends MediationAdapter> cls, Bundle bundle) {
            this.zzul.putBundle(cls.getName(), bundle);
        }

        public void zza(Date date) {
            this.zzaW = date;
        }

        public void zzb(Location location) {
            this.zzba = location;
        }

        public void zzk(boolean z) {
            this.zztK = z ? 1 : 0;
        }

        public void zzl(boolean z) {
            this.zztT = z;
        }

        public void zzn(int i) {
            this.zztH = i;
        }
    }

    public zzy(zza zzaVar) {
        this(zzaVar, null);
    }

    public zzy(zza zzaVar, SearchAdRequest searchAdRequest) {
        this.zzaW = zzaVar.zzaW;
        this.zztN = zzaVar.zztN;
        this.zztH = zzaVar.zztH;
        this.zzaY = Collections.unmodifiableSet(zzaVar.zzuq);
        this.zzba = zzaVar.zzba;
        this.zzpt = zzaVar.zzpt;
        this.zzul = zzaVar.zzul;
        this.zzum = Collections.unmodifiableMap(zzaVar.zzur);
        this.zztL = zzaVar.zztL;
        this.zztR = zzaVar.zztR;
        this.zzun = searchAdRequest;
        this.zztK = zzaVar.zztK;
        this.zzuo = Collections.unmodifiableSet(zzaVar.zzus);
        this.zztP = zzaVar.zztP;
        this.zzup = Collections.unmodifiableSet(zzaVar.zzut);
        this.zztT = zzaVar.zztT;
    }

    public Date getBirthday() {
        return this.zzaW;
    }

    public String getContentUrl() {
        return this.zztN;
    }

    public Bundle getCustomTargeting() {
        return this.zztP;
    }

    public int getGender() {
        return this.zztH;
    }

    public Set<String> getKeywords() {
        return this.zzaY;
    }

    public Location getLocation() {
        return this.zzba;
    }

    public boolean getManualImpressionsEnabled() {
        return this.zzpt;
    }

    public Bundle getNetworkExtrasBundle(Class<? extends MediationAdapter> adapterClass) {
        return this.zzul.getBundle(adapterClass.getName());
    }

    public String getPublisherProvidedId() {
        return this.zztL;
    }

    public boolean isDesignedForFamilies() {
        return this.zztT;
    }

    public boolean isTestDevice(Context context) {
        return this.zzuo.contains(zzl.zzcN().zzS(context));
    }

    public String zzcT() {
        return this.zztR;
    }

    public SearchAdRequest zzcU() {
        return this.zzun;
    }

    public Map<Class<? extends Object>, Object> zzcV() {
        return this.zzum;
    }

    public Bundle zzcW() {
        return this.zzul;
    }

    public int zzcX() {
        return this.zztK;
    }

    public Set<String> zzcY() {
        return this.zzup;
    }
}
