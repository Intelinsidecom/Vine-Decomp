package com.flurry.sdk;

import android.annotation.SuppressLint;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.Display;
import android.view.WindowManager;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* loaded from: classes.dex */
public class ez {
    @SuppressLint({"NewApi"})
    public static Point a() {
        Display defaultDisplay = ((WindowManager) dl.a().b().getSystemService("window")).getDefaultDisplay();
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= 17) {
            defaultDisplay.getRealSize(point);
        } else if (Build.VERSION.SDK_INT >= 14) {
            try {
                Method method = Display.class.getMethod("getRawHeight", new Class[0]);
                point.x = ((Integer) Display.class.getMethod("getRawWidth", new Class[0]).invoke(defaultDisplay, new Object[0])).intValue();
                point.y = ((Integer) method.invoke(defaultDisplay, new Object[0])).intValue();
            } catch (Throwable th) {
                defaultDisplay.getSize(point);
            }
        } else if (Build.VERSION.SDK_INT >= 13) {
            defaultDisplay.getSize(point);
        } else {
            point.x = defaultDisplay.getWidth();
            point.y = defaultDisplay.getHeight();
        }
        return point;
    }

    public static DisplayMetrics b() {
        Display defaultDisplay = ((WindowManager) dl.a().b().getSystemService("window")).getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        defaultDisplay.getMetrics(displayMetrics);
        return displayMetrics;
    }

    @SuppressLint({"NewApi"})
    public static DisplayMetrics c() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Display defaultDisplay = ((WindowManager) dl.a().b().getSystemService("window")).getDefaultDisplay();
        if (Build.VERSION.SDK_INT >= 17) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            defaultDisplay.getRealMetrics(displayMetrics);
            return displayMetrics;
        }
        if (Build.VERSION.SDK_INT >= 14) {
            try {
                DisplayMetrics displayMetrics2 = new DisplayMetrics();
                Display.class.getMethod("getRealMetrics", new Class[0]).invoke(defaultDisplay, displayMetrics2);
                return displayMetrics2;
            } catch (Exception e) {
                return b();
            }
        }
        return b();
    }

    public static float d() {
        return c().density;
    }

    public static int a(int i) {
        return Math.round(i / c().density);
    }

    public static int b(int i) {
        return Math.round(c().density * i);
    }

    public static int e() {
        return a().x;
    }

    public static int f() {
        return a().y;
    }

    public static int g() {
        return a(e());
    }

    public static int h() {
        return a(f());
    }

    public static int i() {
        Point pointA = a();
        if (pointA.x == pointA.y) {
            return 3;
        }
        if (pointA.x < pointA.y) {
            return 1;
        }
        return 2;
    }

    public static Pair<Integer, Integer> j() {
        return Pair.create(Integer.valueOf(g()), Integer.valueOf(h()));
    }

    public static Pair<Integer, Integer> c(int i) {
        int iG = g();
        int iH = h();
        switch (i) {
            case 2:
                return Pair.create(Integer.valueOf(iH), Integer.valueOf(iG));
            default:
                return Pair.create(Integer.valueOf(iG), Integer.valueOf(iH));
        }
    }
}
