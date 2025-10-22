package com.google.ads.mediation.customevent;

import android.support.v4.BuildConfig;
import com.google.ads.mediation.MediationServerParameters;

/* loaded from: classes.dex */
public final class CustomEventServerParameters extends MediationServerParameters {

    @MediationServerParameters.Parameter(name = "class_name", required = true)
    public String className;

    @MediationServerParameters.Parameter(name = "label", required = true)
    public String label;

    @MediationServerParameters.Parameter(name = "parameter", required = BuildConfig.DEBUG)
    public String parameter = null;
}
