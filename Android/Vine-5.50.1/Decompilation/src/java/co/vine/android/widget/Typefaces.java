package co.vine.android.widget;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;

/* loaded from: classes.dex */
public class Typefaces {
    private static Typefaces sInstance;
    public final Typeface boldContent;
    public final Typeface boldContentBold;
    public final Typeface boldContentBoldItalic;
    public final Typeface boldContentDetail;
    public final Typeface boldContentItalic;
    public final Typeface lightContent;
    public final Typeface lightContentBold;
    public final Typeface lightContentBoldItalic;
    public final Typeface lightContentDetail;
    public final Typeface lightContentItalic;
    public final Typeface mediumContent;
    public final Typeface mediumContentBold;
    public final Typeface mediumContentBoldItalic;
    public final Typeface mediumContentDetail;
    public final Typeface mediumContentItalic;
    public final Typeface regularContent;
    public final Typeface regularContentBold;
    public final Typeface regularContentBoldItalic;
    public final Typeface regularContentDetail;
    public final Typeface regularContentItalic;

    private Typefaces(Context context) {
        AssetManager mgr = context.getAssets();
        Typeface regular = Typeface.createFromAsset(mgr, "fonts/Roboto-Regular.ttf");
        this.regularContent = regular;
        this.regularContentItalic = Typeface.createFromAsset(mgr, "fonts/Roboto-Italic.ttf");
        this.regularContentBold = Typeface.createFromAsset(mgr, "fonts/Roboto-Bold.ttf");
        this.regularContentBoldItalic = Typeface.create(this.regularContentBold, 2);
        this.regularContentDetail = regular;
        Typeface bold = Typeface.createFromAsset(mgr, "fonts/Roboto-Bold.ttf");
        this.boldContent = bold;
        this.boldContentItalic = Typeface.createFromAsset(mgr, "fonts/Roboto-BoldItalic.ttf");
        this.boldContentBold = bold;
        this.boldContentBoldItalic = Typeface.create(bold, 2);
        this.boldContentDetail = bold;
        Typeface medium = Typeface.createFromAsset(mgr, "fonts/Roboto-Medium.ttf");
        this.mediumContent = medium;
        this.mediumContentItalic = Typeface.createFromAsset(mgr, "fonts/Roboto-MediumItalic.ttf");
        this.mediumContentBold = this.boldContent;
        this.mediumContentBoldItalic = Typeface.create(this.mediumContentBold, 2);
        this.mediumContentDetail = medium;
        Typeface light = Typeface.createFromAsset(mgr, "fonts/Roboto-Light.ttf");
        this.lightContent = light;
        this.lightContentItalic = Typeface.createFromAsset(mgr, "fonts/Roboto-LightItalic.ttf");
        this.lightContentBold = this.regularContent;
        this.lightContentBoldItalic = Typeface.create(this.lightContentBold, 2);
        this.lightContentDetail = light;
    }

    public Typeface getContentTypeface(int style, int weight) {
        switch (weight) {
            case 1:
                if ((style & 1) != 0 && (style & 2) != 0) {
                    return this.lightContentBoldItalic;
                }
                if ((style & 1) != 0) {
                    return this.lightContentBold;
                }
                if ((style & 2) != 0) {
                    return this.lightContentItalic;
                }
                return this.lightContent;
            case 2:
            default:
                if ((style & 1) != 0 && (style & 2) != 0) {
                    return this.regularContentBoldItalic;
                }
                if ((style & 1) != 0) {
                    return this.regularContentBold;
                }
                if ((style & 2) != 0) {
                    return this.regularContentItalic;
                }
                return this.regularContent;
            case 3:
                if ((style & 1) != 0 && (style & 2) != 0) {
                    return this.mediumContentBoldItalic;
                }
                if ((style & 1) != 0) {
                    return this.mediumContentBold;
                }
                if ((style & 2) != 0) {
                    return this.mediumContentItalic;
                }
                return this.mediumContent;
            case 4:
                if ((style & 1) != 0 && (style & 2) != 0) {
                    return this.boldContentBoldItalic;
                }
                if ((style & 1) != 0) {
                    return this.boldContentBold;
                }
                if ((style & 2) != 0) {
                    return this.boldContentItalic;
                }
                return this.boldContent;
        }
    }

    public static synchronized Typefaces get(Context context) {
        if (sInstance == null) {
            sInstance = new Typefaces(context);
        }
        return sInstance;
    }
}
