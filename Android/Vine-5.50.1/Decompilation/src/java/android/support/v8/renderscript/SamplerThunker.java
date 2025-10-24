package android.support.v8.renderscript;

import android.renderscript.Sampler;
import android.support.v8.renderscript.Sampler;

/* loaded from: classes2.dex */
class SamplerThunker extends Sampler {
    android.renderscript.Sampler mN;

    protected SamplerThunker(int id, RenderScript rs) {
        super(id, rs);
    }

    @Override // android.support.v8.renderscript.BaseObj
    android.renderscript.BaseObj getNObj() {
        return this.mN;
    }

    static Sampler.Value convertValue(Sampler.Value v) {
        switch (v) {
            case NEAREST:
                return Sampler.Value.NEAREST;
            case LINEAR:
                return Sampler.Value.LINEAR;
            case LINEAR_MIP_LINEAR:
                return Sampler.Value.LINEAR_MIP_LINEAR;
            case LINEAR_MIP_NEAREST:
                return Sampler.Value.LINEAR_MIP_NEAREST;
            case WRAP:
                return Sampler.Value.WRAP;
            case CLAMP:
                return Sampler.Value.CLAMP;
            case MIRRORED_REPEAT:
                return Sampler.Value.MIRRORED_REPEAT;
            default:
                return null;
        }
    }

    public static class Builder {
        float mAniso;
        RenderScriptThunker mRS;
        Sampler.Value mMin = Sampler.Value.NEAREST;
        Sampler.Value mMag = Sampler.Value.NEAREST;
        Sampler.Value mWrapS = Sampler.Value.WRAP;
        Sampler.Value mWrapT = Sampler.Value.WRAP;
        Sampler.Value mWrapR = Sampler.Value.WRAP;

        public Builder(RenderScriptThunker rs) {
            this.mRS = rs;
        }

        public void setMinification(Sampler.Value v) {
            if (v == Sampler.Value.NEAREST || v == Sampler.Value.LINEAR || v == Sampler.Value.LINEAR_MIP_LINEAR || v == Sampler.Value.LINEAR_MIP_NEAREST) {
                this.mMin = v;
                return;
            }
            throw new IllegalArgumentException("Invalid value");
        }

        public void setMagnification(Sampler.Value v) {
            if (v == Sampler.Value.NEAREST || v == Sampler.Value.LINEAR) {
                this.mMag = v;
                return;
            }
            throw new IllegalArgumentException("Invalid value");
        }

        public void setWrapS(Sampler.Value v) {
            if (v == Sampler.Value.WRAP || v == Sampler.Value.CLAMP || v == Sampler.Value.MIRRORED_REPEAT) {
                this.mWrapS = v;
                return;
            }
            throw new IllegalArgumentException("Invalid value");
        }

        public void setWrapT(Sampler.Value v) {
            if (v == Sampler.Value.WRAP || v == Sampler.Value.CLAMP || v == Sampler.Value.MIRRORED_REPEAT) {
                this.mWrapT = v;
                return;
            }
            throw new IllegalArgumentException("Invalid value");
        }

        public void setAnisotropy(float v) {
            if (v >= 0.0f) {
                this.mAniso = v;
                return;
            }
            throw new IllegalArgumentException("Invalid value");
        }

        public Sampler create() {
            this.mRS.validate();
            Sampler.Builder b = new Sampler.Builder(this.mRS.mN);
            b.setMinification(SamplerThunker.convertValue(this.mMin));
            b.setMagnification(SamplerThunker.convertValue(this.mMag));
            b.setWrapS(SamplerThunker.convertValue(this.mWrapS));
            b.setWrapT(SamplerThunker.convertValue(this.mWrapT));
            b.setAnisotropy(this.mAniso);
            android.renderscript.Sampler s = b.create();
            SamplerThunker sampler = new SamplerThunker(0, this.mRS);
            sampler.mMin = this.mMin;
            sampler.mMag = this.mMag;
            sampler.mWrapS = this.mWrapS;
            sampler.mWrapT = this.mWrapT;
            sampler.mWrapR = this.mWrapR;
            sampler.mAniso = this.mAniso;
            sampler.mN = s;
            return sampler;
        }
    }
}
