package com.digits.sdk.android;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

/* loaded from: classes.dex */
public class FailureActionBarActivity extends ActionBarActivity {
    FailureActivityDelegateImpl delegate;

    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityDonut, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        setTheme(Digits.getInstance().getTheme());
        super.onCreate(savedInstanceState);
        this.delegate = new FailureActivityDelegateImpl(this);
        this.delegate.init();
    }
}
