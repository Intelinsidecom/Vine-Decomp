package com.flurry.sdk;

import android.content.Context;

/* loaded from: classes.dex */
public class ep implements eq {
    private final eq a;

    public ep(eq eqVar) {
        this.a = eqVar;
    }

    @Override // com.flurry.sdk.eq
    public void a(dg dgVar, Context context) {
        if (this.a != null) {
            this.a.a(dgVar, context);
        }
    }

    @Override // com.flurry.sdk.eq
    public void b(dg dgVar, Context context) {
        if (this.a != null) {
            this.a.b(dgVar, context);
        }
    }

    @Override // com.flurry.sdk.eq
    public void c(dg dgVar, Context context) {
        if (this.a != null) {
            this.a.c(dgVar, context);
        }
    }

    @Override // com.flurry.sdk.eq
    public void a(dg dgVar) {
        if (this.a != null) {
            this.a.a(dgVar);
        }
    }
}
