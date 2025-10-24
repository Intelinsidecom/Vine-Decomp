package android.support.v4.graphics.drawable;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableWrapperDonut;

/* loaded from: classes2.dex */
class DrawableWrapperKitKat extends DrawableWrapperHoneycomb {
    DrawableWrapperKitKat(Drawable drawable) {
        super(drawable);
    }

    DrawableWrapperKitKat(DrawableWrapperDonut.DrawableWrapperState state, Resources resources) {
        super(state, resources);
    }

    @Override // android.graphics.drawable.Drawable
    public void setAutoMirrored(boolean mirrored) {
        this.mDrawable.setAutoMirrored(mirrored);
    }

    @Override // android.graphics.drawable.Drawable
    public boolean isAutoMirrored() {
        return this.mDrawable.isAutoMirrored();
    }

    @Override // android.support.v4.graphics.drawable.DrawableWrapperHoneycomb, android.support.v4.graphics.drawable.DrawableWrapperDonut
    DrawableWrapperDonut.DrawableWrapperState mutateConstantState() {
        return new DrawableWrapperStateKitKat(this.mState, null);
    }

    private static class DrawableWrapperStateKitKat extends DrawableWrapperDonut.DrawableWrapperState {
        DrawableWrapperStateKitKat(DrawableWrapperDonut.DrawableWrapperState orig, Resources res) {
            super(orig, res);
        }

        @Override // android.support.v4.graphics.drawable.DrawableWrapperDonut.DrawableWrapperState, android.graphics.drawable.Drawable.ConstantState
        public Drawable newDrawable(Resources res) {
            return new DrawableWrapperKitKat(this, res);
        }
    }
}
