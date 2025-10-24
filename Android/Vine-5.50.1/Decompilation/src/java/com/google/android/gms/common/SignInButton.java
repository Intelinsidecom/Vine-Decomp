package com.google.android.gms.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import com.google.android.gms.R;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.zzab;
import com.google.android.gms.common.internal.zzac;
import com.google.android.gms.dynamic.zzg;

/* loaded from: classes2.dex */
public final class SignInButton extends FrameLayout implements View.OnClickListener {
    private int mColor;
    private int mSize;
    private Scope[] zzaem;
    private View zzaen;
    private View.OnClickListener zzaeo;

    public SignInButton(Context context) {
        this(context, null);
    }

    public SignInButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SignInButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.zzaeo = null;
        zza(context, attrs);
        setStyle(this.mSize, this.mColor, this.zzaem);
    }

    private static Button zza(Context context, int i, int i2, Scope[] scopeArr) {
        zzac zzacVar = new zzac(context);
        zzacVar.zza(context.getResources(), i, i2, scopeArr);
        return zzacVar;
    }

    private void zza(Context context, AttributeSet attributeSet) {
        TypedArray typedArrayObtainStyledAttributes = context.getTheme().obtainStyledAttributes(attributeSet, R.styleable.SignInButton, 0, 0);
        try {
            this.mSize = typedArrayObtainStyledAttributes.getInt(R.styleable.SignInButton_buttonSize, 0);
            this.mColor = typedArrayObtainStyledAttributes.getInt(R.styleable.SignInButton_colorScheme, 2);
            String string = typedArrayObtainStyledAttributes.getString(R.styleable.SignInButton_scopeUris);
            if (string == null) {
                this.zzaem = null;
            } else {
                String[] strArrSplit = string.trim().split("\\s+");
                this.zzaem = new Scope[strArrSplit.length];
                for (int i = 0; i < strArrSplit.length; i++) {
                    this.zzaem[i] = new Scope(strArrSplit[i].toString());
                }
            }
        } finally {
            typedArrayObtainStyledAttributes.recycle();
        }
    }

    private void zzaq(Context context) {
        if (this.zzaen != null) {
            removeView(this.zzaen);
        }
        try {
            this.zzaen = zzab.zzb(context, this.mSize, this.mColor, this.zzaem);
        } catch (zzg.zza e) {
            Log.w("SignInButton", "Sign in button not found, using placeholder instead");
            this.zzaen = zza(context, this.mSize, this.mColor, this.zzaem);
        }
        addView(this.zzaen);
        this.zzaen.setEnabled(isEnabled());
        this.zzaen.setOnClickListener(this);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (this.zzaeo == null || view != this.zzaen) {
            return;
        }
        this.zzaeo.onClick(this);
    }

    public void setColorScheme(int colorScheme) {
        setStyle(this.mSize, colorScheme, this.zzaem);
    }

    @Override // android.view.View
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        this.zzaen.setEnabled(enabled);
    }

    @Override // android.view.View
    public void setOnClickListener(View.OnClickListener listener) {
        this.zzaeo = listener;
        if (this.zzaen != null) {
            this.zzaen.setOnClickListener(this);
        }
    }

    public void setScopes(Scope[] scopes) {
        setStyle(this.mSize, this.mColor, scopes);
    }

    public void setSize(int buttonSize) {
        setStyle(buttonSize, this.mColor, this.zzaem);
    }

    public void setStyle(int buttonSize, int colorScheme, Scope[] scopes) {
        this.mSize = buttonSize;
        this.mColor = colorScheme;
        this.zzaem = scopes;
        zzaq(getContext());
    }
}
