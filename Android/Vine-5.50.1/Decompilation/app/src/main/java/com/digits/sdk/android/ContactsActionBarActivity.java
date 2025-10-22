package com.digits.sdk.android;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

/* loaded from: classes.dex */
public class ContactsActionBarActivity extends ActionBarActivity {
    ContactsActivityDelegateImpl delegate;

    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityDonut, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        setTheme(getIntent().getIntExtra("THEME_RESOURCE_ID", R.style.Theme_AppCompat_Light));
        super.onCreate(savedInstanceState);
        this.delegate = new ContactsActivityDelegateImpl(this);
        this.delegate.init();
    }
}
