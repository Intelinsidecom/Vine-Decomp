package android.support.v4.content.res;

import android.content.res.Resources;
import android.os.Build;

/* loaded from: classes.dex */
public final class ConfigurationHelper {
    private static final ConfigurationHelperImpl IMPL;

    private interface ConfigurationHelperImpl {
        int getDensityDpi(Resources resources);

        int getScreenHeightDp(Resources resources);

        int getScreenWidthDp(Resources resources);

        int getSmallestScreenWidthDp(Resources resources);
    }

    static {
        int sdk = Build.VERSION.SDK_INT;
        if (sdk >= 17) {
            IMPL = new JellybeanMr1Impl();
        } else if (sdk >= 13) {
            IMPL = new HoneycombMr2Impl();
        } else {
            IMPL = new DonutImpl();
        }
    }

    private ConfigurationHelper() {
    }

    private static class DonutImpl implements ConfigurationHelperImpl {
        private DonutImpl() {
        }

        @Override // android.support.v4.content.res.ConfigurationHelper.ConfigurationHelperImpl
        public int getScreenHeightDp(Resources resources) {
            return ConfigurationHelperDonut.getScreenHeightDp(resources);
        }

        @Override // android.support.v4.content.res.ConfigurationHelper.ConfigurationHelperImpl
        public int getScreenWidthDp(Resources resources) {
            return ConfigurationHelperDonut.getScreenWidthDp(resources);
        }

        @Override // android.support.v4.content.res.ConfigurationHelper.ConfigurationHelperImpl
        public int getSmallestScreenWidthDp(Resources resources) {
            return ConfigurationHelperDonut.getSmallestScreenWidthDp(resources);
        }

        @Override // android.support.v4.content.res.ConfigurationHelper.ConfigurationHelperImpl
        public int getDensityDpi(Resources resources) {
            return ConfigurationHelperDonut.getDensityDpi(resources);
        }
    }

    private static class HoneycombMr2Impl extends DonutImpl {
        private HoneycombMr2Impl() {
            super();
        }

        @Override // android.support.v4.content.res.ConfigurationHelper.DonutImpl, android.support.v4.content.res.ConfigurationHelper.ConfigurationHelperImpl
        public int getScreenHeightDp(Resources resources) {
            return ConfigurationHelperHoneycombMr2.getScreenHeightDp(resources);
        }

        @Override // android.support.v4.content.res.ConfigurationHelper.DonutImpl, android.support.v4.content.res.ConfigurationHelper.ConfigurationHelperImpl
        public int getScreenWidthDp(Resources resources) {
            return ConfigurationHelperHoneycombMr2.getScreenWidthDp(resources);
        }

        @Override // android.support.v4.content.res.ConfigurationHelper.DonutImpl, android.support.v4.content.res.ConfigurationHelper.ConfigurationHelperImpl
        public int getSmallestScreenWidthDp(Resources resources) {
            return ConfigurationHelperHoneycombMr2.getSmallestScreenWidthDp(resources);
        }
    }

    private static class JellybeanMr1Impl extends HoneycombMr2Impl {
        private JellybeanMr1Impl() {
            super();
        }

        @Override // android.support.v4.content.res.ConfigurationHelper.DonutImpl, android.support.v4.content.res.ConfigurationHelper.ConfigurationHelperImpl
        public int getDensityDpi(Resources resources) {
            return ConfigurationHelperJellybeanMr1.getDensityDpi(resources);
        }
    }

    public static int getScreenHeightDp(Resources resources) {
        return IMPL.getScreenHeightDp(resources);
    }

    public static int getScreenWidthDp(Resources resources) {
        return IMPL.getScreenWidthDp(resources);
    }

    public static int getSmallestScreenWidthDp(Resources resources) {
        return IMPL.getSmallestScreenWidthDp(resources);
    }

    public static int getDensityDpi(Resources resources) {
        return IMPL.getDensityDpi(resources);
    }
}
