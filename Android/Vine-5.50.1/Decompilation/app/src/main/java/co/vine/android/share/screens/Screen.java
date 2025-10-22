package co.vine.android.share.screens;

import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import co.vine.android.client.AppController;
import co.vine.android.share.widgets.FakeActionBar;

/* loaded from: classes.dex */
public abstract class Screen extends LinearLayout {
    abstract AnimatorSet getHideAnimatorSet();

    abstract AnimatorSet getShowAnimatorSet();

    abstract void onBindFakeActionBar(FakeActionBar fakeActionBar);

    abstract void onInitialize(ScreenManager screenManager, AppController appController, Bundle bundle);

    public Screen(Context context) {
        super(context);
    }

    public Screen(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Screen(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    boolean onBack() {
        return false;
    }

    void onShow(Bundle previousResult) {
    }

    void onSaveInstanceState(Bundle savedInstanceState) {
    }

    void onRestoreInstanceState(Bundle savedInstanceState) {
    }

    void onHide() {
    }

    boolean onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        return false;
    }

    void onResume() {
    }
}
