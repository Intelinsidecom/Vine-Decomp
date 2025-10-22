package com.tune.crosspromo;

/* loaded from: classes.dex */
class TuneAdViewSet {
    public boolean showView1;
    public TuneAdView view1;
    public TuneAdView view2;

    protected TuneAdView getPreviousView() {
        return this.showView1 ? this.view2 : this.view1;
    }
}
