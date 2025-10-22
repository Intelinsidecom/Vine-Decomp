package android.support.v7.internal.widget;

import android.graphics.Rect;
import android.os.Build;

/* loaded from: classes.dex */
public class DrawableUtils {
    public static final Rect INSETS_NONE = new Rect();
    private static Class<?> sInsetsClazz;

    static {
        if (Build.VERSION.SDK_INT >= 18) {
            try {
                sInsetsClazz = Class.forName("android.graphics.Insets");
            } catch (ClassNotFoundException e) {
            }
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:24:0x006b A[Catch: Exception -> 0x0072, TRY_LEAVE, TryCatch #0 {Exception -> 0x0072, blocks: (B:4:0x0005, B:6:0x001f, B:8:0x002e, B:9:0x0039, B:10:0x003c, B:11:0x003f, B:24:0x006b, B:30:0x007d, B:31:0x0084, B:32:0x008b, B:12:0x0042, B:15:0x004c, B:18:0x0057, B:21:0x0061), top: B:34:0x0005 }] */
    /* JADX WARN: Removed duplicated region for block: B:30:0x007d A[Catch: Exception -> 0x0072, TRY_ENTER, TryCatch #0 {Exception -> 0x0072, blocks: (B:4:0x0005, B:6:0x001f, B:8:0x002e, B:9:0x0039, B:10:0x003c, B:11:0x003f, B:24:0x006b, B:30:0x007d, B:31:0x0084, B:32:0x008b, B:12:0x0042, B:15:0x004c, B:18:0x0057, B:21:0x0061), top: B:34:0x0005 }] */
    /* JADX WARN: Removed duplicated region for block: B:31:0x0084 A[Catch: Exception -> 0x0072, TryCatch #0 {Exception -> 0x0072, blocks: (B:4:0x0005, B:6:0x001f, B:8:0x002e, B:9:0x0039, B:10:0x003c, B:11:0x003f, B:24:0x006b, B:30:0x007d, B:31:0x0084, B:32:0x008b, B:12:0x0042, B:15:0x004c, B:18:0x0057, B:21:0x0061), top: B:34:0x0005 }] */
    /* JADX WARN: Removed duplicated region for block: B:32:0x008b A[Catch: Exception -> 0x0072, TRY_LEAVE, TryCatch #0 {Exception -> 0x0072, blocks: (B:4:0x0005, B:6:0x001f, B:8:0x002e, B:9:0x0039, B:10:0x003c, B:11:0x003f, B:24:0x006b, B:30:0x007d, B:31:0x0084, B:32:0x008b, B:12:0x0042, B:15:0x004c, B:18:0x0057, B:21:0x0061), top: B:34:0x0005 }] */
    /* JADX WARN: Removed duplicated region for block: B:37:0x003f A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static android.graphics.Rect getOpticalBounds(android.graphics.drawable.Drawable r12) throws java.lang.IllegalAccessException, java.lang.NoSuchMethodException, java.lang.SecurityException, java.lang.IllegalArgumentException, java.lang.reflect.InvocationTargetException {
        /*
            r9 = 0
            java.lang.Class<?> r8 = android.support.v7.internal.widget.DrawableUtils.sInsetsClazz
            if (r8 == 0) goto L7a
            android.graphics.drawable.Drawable r12 = android.support.v4.graphics.drawable.DrawableCompat.unwrap(r12)     // Catch: java.lang.Exception -> L72
            java.lang.Class r8 = r12.getClass()     // Catch: java.lang.Exception -> L72
            java.lang.String r10 = "getOpticalInsets"
            r11 = 0
            java.lang.Class[] r11 = new java.lang.Class[r11]     // Catch: java.lang.Exception -> L72
            java.lang.reflect.Method r3 = r8.getMethod(r10, r11)     // Catch: java.lang.Exception -> L72
            r8 = 0
            java.lang.Object[] r8 = new java.lang.Object[r8]     // Catch: java.lang.Exception -> L72
            java.lang.Object r5 = r3.invoke(r12, r8)     // Catch: java.lang.Exception -> L72
            if (r5 == 0) goto L7a
            android.graphics.Rect r7 = new android.graphics.Rect     // Catch: java.lang.Exception -> L72
            r7.<init>()     // Catch: java.lang.Exception -> L72
            java.lang.Class<?> r8 = android.support.v7.internal.widget.DrawableUtils.sInsetsClazz     // Catch: java.lang.Exception -> L72
            java.lang.reflect.Field[] r0 = r8.getFields()     // Catch: java.lang.Exception -> L72
            int r6 = r0.length     // Catch: java.lang.Exception -> L72
            r4 = 0
        L2c:
            if (r4 >= r6) goto L7c
            r2 = r0[r4]     // Catch: java.lang.Exception -> L72
            java.lang.String r10 = r2.getName()     // Catch: java.lang.Exception -> L72
            r8 = -1
            int r11 = r10.hashCode()     // Catch: java.lang.Exception -> L72
            switch(r11) {
                case -1383228885: goto L61;
                case 115029: goto L4c;
                case 3317767: goto L42;
                case 108511772: goto L57;
                default: goto L3c;
            }     // Catch: java.lang.Exception -> L72
        L3c:
            switch(r8) {
                case 0: goto L6b;
                case 1: goto L7d;
                case 2: goto L84;
                case 3: goto L8b;
                default: goto L3f;
            }     // Catch: java.lang.Exception -> L72
        L3f:
            int r4 = r4 + 1
            goto L2c
        L42:
            java.lang.String r11 = "left"
            boolean r10 = r10.equals(r11)     // Catch: java.lang.Exception -> L72
            if (r10 == 0) goto L3c
            r8 = r9
            goto L3c
        L4c:
            java.lang.String r11 = "top"
            boolean r10 = r10.equals(r11)     // Catch: java.lang.Exception -> L72
            if (r10 == 0) goto L3c
            r8 = 1
            goto L3c
        L57:
            java.lang.String r11 = "right"
            boolean r10 = r10.equals(r11)     // Catch: java.lang.Exception -> L72
            if (r10 == 0) goto L3c
            r8 = 2
            goto L3c
        L61:
            java.lang.String r11 = "bottom"
            boolean r10 = r10.equals(r11)     // Catch: java.lang.Exception -> L72
            if (r10 == 0) goto L3c
            r8 = 3
            goto L3c
        L6b:
            int r8 = r2.getInt(r5)     // Catch: java.lang.Exception -> L72
            r7.left = r8     // Catch: java.lang.Exception -> L72
            goto L3f
        L72:
            r1 = move-exception
            java.lang.String r8 = "DrawableUtils"
            java.lang.String r9 = "Couldn't obtain the optical insets. Ignoring."
            android.util.Log.e(r8, r9)
        L7a:
            android.graphics.Rect r7 = android.support.v7.internal.widget.DrawableUtils.INSETS_NONE
        L7c:
            return r7
        L7d:
            int r8 = r2.getInt(r5)     // Catch: java.lang.Exception -> L72
            r7.top = r8     // Catch: java.lang.Exception -> L72
            goto L3f
        L84:
            int r8 = r2.getInt(r5)     // Catch: java.lang.Exception -> L72
            r7.right = r8     // Catch: java.lang.Exception -> L72
            goto L3f
        L8b:
            int r8 = r2.getInt(r5)     // Catch: java.lang.Exception -> L72
            r7.bottom = r8     // Catch: java.lang.Exception -> L72
            goto L3f
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v7.internal.widget.DrawableUtils.getOpticalBounds(android.graphics.drawable.Drawable):android.graphics.Rect");
    }
}
