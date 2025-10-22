package android.support.v8.renderscript;

/* loaded from: classes.dex */
public class BaseObj {
    private boolean mDestroyed;
    private int mID;
    RenderScript mRS;

    BaseObj(int id, RenderScript rs) {
        rs.validate();
        this.mRS = rs;
        this.mID = id;
        this.mDestroyed = false;
    }

    void setID(int id) {
        if (this.mID != 0) {
            throw new RSRuntimeException("Internal Error, reset of object ID.");
        }
        this.mID = id;
    }

    int getID(RenderScript rs) {
        this.mRS.validate();
        if (RenderScript.isNative) {
            if (getNObj() != null) {
                return getNObj().hashCode();
            }
        }
        if (this.mDestroyed) {
            throw new RSInvalidStateException("using a destroyed object.");
        }
        if (this.mID == 0) {
            throw new RSRuntimeException("Internal error: Object id 0.");
        }
        if (rs != null && rs != this.mRS) {
            throw new RSInvalidStateException("using object with mismatched context.");
        }
        return this.mID;
    }

    android.renderscript.BaseObj getNObj() {
        return null;
    }

    void checkValid() {
        if (this.mID == 0 && getNObj() == null) {
            throw new RSIllegalArgumentException("Invalid object.");
        }
    }

    protected void finalize() throws Throwable {
        if (!this.mDestroyed) {
            if (this.mID != 0 && this.mRS.isAlive()) {
                this.mRS.nObjDestroy(this.mID);
            }
            this.mRS = null;
            this.mID = 0;
            this.mDestroyed = true;
        }
        super.finalize();
    }

    public synchronized void destroy() {
        if (this.mDestroyed) {
            throw new RSInvalidStateException("Object already destroyed.");
        }
        this.mDestroyed = true;
        this.mRS.nObjDestroy(this.mID);
    }

    public int hashCode() {
        return this.mID;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        BaseObj b = (BaseObj) obj;
        return this.mID == b.mID;
    }
}
