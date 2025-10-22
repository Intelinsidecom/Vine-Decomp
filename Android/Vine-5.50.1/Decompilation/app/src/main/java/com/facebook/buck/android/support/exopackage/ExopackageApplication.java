package com.facebook.buck.android.support.exopackage;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import com.facebook.buck.android.support.exopackage.ApplicationLike;

/* loaded from: classes.dex */
public abstract class ExopackageApplication<T extends ApplicationLike> extends Application {
    private T delegate;
    private final String delegateClassName;
    private final boolean isExopackage;

    protected ExopackageApplication(boolean isExopackage) {
        this(DefaultApplicationLike.class.getName(), isExopackage);
    }

    protected ExopackageApplication(String delegateClassName, boolean isExopackage) {
        this.delegateClassName = delegateClassName;
        this.isExopackage = isExopackage;
    }

    private T createDelegate() throws ArrayIndexOutOfBoundsException, IllegalArgumentException, NegativeArraySizeException {
        if (this.isExopackage) {
            ExopackageDexLoader.loadExopackageJars(this);
        }
        try {
            return (T) Class.forName(this.delegateClassName).getConstructor(Application.class).newInstance(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private synchronized void ensureDelegate() {
        if (this.delegate == null) {
            this.delegate = (T) createDelegate();
        }
    }

    protected void onBaseContextAttached() {
    }

    public final T getDelegateIfPresent() {
        return this.delegate;
    }

    @Override // android.content.ContextWrapper
    protected final void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        onBaseContextAttached();
        ensureDelegate();
    }

    @Override // android.app.Application
    public final void onCreate() {
        super.onCreate();
        ensureDelegate();
        this.delegate.onCreate();
    }

    @Override // android.app.Application
    public final void onTerminate() {
        super.onTerminate();
        if (this.delegate != null) {
            this.delegate.onTerminate();
        }
    }

    @Override // android.app.Application, android.content.ComponentCallbacks
    public final void onLowMemory() {
        super.onLowMemory();
        if (this.delegate != null) {
            this.delegate.onLowMemory();
        }
    }

    @Override // android.app.Application, android.content.ComponentCallbacks2
    @TargetApi(14)
    public final void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (this.delegate != null) {
            this.delegate.onTrimMemory(level);
        }
    }

    @Override // android.app.Application, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (this.delegate != null) {
            this.delegate.onConfigurationChanged(newConfig);
        }
    }
}
