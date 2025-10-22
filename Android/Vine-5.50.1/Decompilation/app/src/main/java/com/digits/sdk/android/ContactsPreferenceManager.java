package com.digits.sdk.android;

import android.annotation.SuppressLint;
import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.services.persistence.PreferenceStore;
import io.fabric.sdk.android.services.persistence.PreferenceStoreImpl;

/* loaded from: classes.dex */
class ContactsPreferenceManager {
    private final PreferenceStore prefStore = new PreferenceStoreImpl(Fabric.getKit(Digits.class));

    ContactsPreferenceManager() {
    }

    @SuppressLint({"CommitPrefEdits"})
    protected void setContactImportPermissionGranted() {
        this.prefStore.save(this.prefStore.edit().putBoolean("CONTACTS_IMPORT_PERMISSION", true));
    }

    @SuppressLint({"CommitPrefEdits"})
    protected void setContactsReadTimestamp(long timestamp) {
        this.prefStore.save(this.prefStore.edit().putLong("CONTACTS_READ_TIMESTAMP", timestamp));
    }

    @SuppressLint({"CommitPrefEdits"})
    protected void setContactsUploaded(int count) {
        this.prefStore.save(this.prefStore.edit().putInt("CONTACTS_CONTACTS_UPLOADED", count));
    }
}
