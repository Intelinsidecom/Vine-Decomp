package com.digits.sdk.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

/* loaded from: classes.dex */
public abstract class DigitsActionBarActivity extends ActionBarActivity {
    DigitsActivityDelegate delegate;

    abstract DigitsActivityDelegate getActivityDelegate();

    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityDonut, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        setTheme(Digits.getInstance().getTheme());
        super.onCreate(savedInstanceState);
        this.delegate = getActivityDelegate();
        Bundle bundle = getIntent().getExtras();
        if (this.delegate.isValid(bundle)) {
            setContentView(this.delegate.getLayoutId());
            this.delegate.init(this, bundle);
        } else {
            finish();
            throw new IllegalAccessError("This activity can only be started from Digits");
        }
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        this.delegate.onResume();
    }

    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        this.delegate.onDestroy();
        super.onDestroy();
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.delegate.onActivityResult(requestCode, resultCode, this);
        if (resultCode == 200 && requestCode == 140) {
            finish();
        }
    }
}
