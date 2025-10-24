package android.support.v4.print;

import android.content.Context;
import android.print.PrintAttributes;

/* loaded from: classes2.dex */
class PrintHelperApi23 extends PrintHelperApi20 {
    @Override // android.support.v4.print.PrintHelperKitkat
    protected PrintAttributes.Builder copyAttributes(PrintAttributes other) {
        PrintAttributes.Builder b = super.copyAttributes(other);
        if (other.getDuplexMode() != 0) {
            b.setDuplexMode(other.getDuplexMode());
        }
        return b;
    }

    PrintHelperApi23(Context context) {
        super(context);
        this.mIsMinMarginsHandlingCorrect = false;
    }
}
