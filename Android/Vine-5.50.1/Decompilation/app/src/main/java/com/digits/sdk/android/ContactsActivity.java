package com.digits.sdk.android;

import android.app.Activity;
import android.os.Bundle;

/* loaded from: classes.dex */
public class ContactsActivity extends Activity {
    ContactsActivityDelegateImpl delegate;

    @Override // android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        setTheme(getIntent().getIntExtra("THEME_RESOURCE_ID", R.style.Digits_default));
        super.onCreate(savedInstanceState);
        this.delegate = new ContactsActivityDelegateImpl(this);
        this.delegate.init();
    }
}
