package android.support.v8.renderscript;

import android.content.Context;
import android.renderscript.RenderScript;
import android.support.v8.renderscript.RenderScript;

/* loaded from: classes2.dex */
class RenderScriptThunker extends RenderScript {
    android.renderscript.RenderScript mN;

    @Override // android.support.v8.renderscript.RenderScript
    void validate() {
        if (this.mN == null) {
            throw new RSInvalidStateException("Calling RS with no Context active.");
        }
    }

    @Override // android.support.v8.renderscript.RenderScript
    public void setPriority(RenderScript.Priority p) {
        if (p == RenderScript.Priority.LOW) {
            this.mN.setPriority(RenderScript.Priority.LOW);
        }
        if (p == RenderScript.Priority.NORMAL) {
            this.mN.setPriority(RenderScript.Priority.NORMAL);
        }
    }

    RenderScriptThunker(Context ctx) {
        super(ctx);
        isNative = true;
    }

    public static RenderScript create(Context ctx, int sdkVersion) {
        RenderScriptThunker rs = new RenderScriptThunker(ctx);
        rs.mN = android.renderscript.RenderScript.create(ctx, sdkVersion);
        return rs;
    }

    @Override // android.support.v8.renderscript.RenderScript
    public void contextDump() {
        this.mN.contextDump();
    }

    @Override // android.support.v8.renderscript.RenderScript
    public void finish() {
        this.mN.finish();
    }

    @Override // android.support.v8.renderscript.RenderScript
    public void destroy() {
        this.mN.destroy();
        this.mN = null;
    }
}
