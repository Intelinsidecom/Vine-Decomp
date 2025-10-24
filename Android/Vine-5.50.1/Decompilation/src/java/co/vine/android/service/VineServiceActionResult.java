package co.vine.android.service;

import android.os.Bundle;
import co.vine.android.api.VineParserReader;
import co.vine.android.network.NetworkOperation;

/* loaded from: classes.dex */
public final class VineServiceActionResult {
    public final NetworkOperation op;
    public final Bundle ret;
    public final VineParserReader vp;

    public VineServiceActionResult(VineParserReader vp, NetworkOperation op) {
        this.vp = vp;
        this.op = op;
        this.ret = null;
    }

    public VineServiceActionResult(Bundle ret) {
        this.ret = ret;
        this.vp = null;
        this.op = null;
    }
}
