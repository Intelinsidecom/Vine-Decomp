package com.google.ads.mediation;

import android.location.Location;
import com.google.ads.AdRequest;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

@Deprecated
/* loaded from: classes.dex */
public class MediationAdRequest {
    private final Date zzaW;
    private final AdRequest.Gender zzaX;
    private final Set<String> zzaY;
    private final boolean zzaZ;
    private final Location zzba;

    public MediationAdRequest(Date birthday, AdRequest.Gender gender, Set<String> keywords, boolean isTesting, Location location) {
        this.zzaW = birthday;
        this.zzaX = gender;
        this.zzaY = keywords;
        this.zzaZ = isTesting;
        this.zzba = location;
    }

    public Integer getAgeInYears() {
        if (this.zzaW == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar.setTime(this.zzaW);
        Integer numValueOf = Integer.valueOf(calendar2.get(1) - calendar.get(1));
        return (calendar2.get(2) < calendar.get(2) || (calendar2.get(2) == calendar.get(2) && calendar2.get(5) < calendar.get(5))) ? Integer.valueOf(numValueOf.intValue() - 1) : numValueOf;
    }

    public Date getBirthday() {
        return this.zzaW;
    }

    public AdRequest.Gender getGender() {
        return this.zzaX;
    }

    public Set<String> getKeywords() {
        return this.zzaY;
    }

    public Location getLocation() {
        return this.zzba;
    }

    public boolean isTesting() {
        return this.zzaZ;
    }
}
