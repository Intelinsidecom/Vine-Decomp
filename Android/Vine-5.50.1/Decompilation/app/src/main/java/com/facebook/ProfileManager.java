package com.facebook;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import com.facebook.internal.Utility;
import com.facebook.internal.Validate;

/* loaded from: classes2.dex */
final class ProfileManager {
    private static volatile ProfileManager instance;
    private Profile currentProfile;
    private final LocalBroadcastManager localBroadcastManager;
    private final ProfileCache profileCache;

    ProfileManager(LocalBroadcastManager localBroadcastManager, ProfileCache profileCache) {
        Validate.notNull(localBroadcastManager, "localBroadcastManager");
        Validate.notNull(profileCache, "profileCache");
        this.localBroadcastManager = localBroadcastManager;
        this.profileCache = profileCache;
    }

    static ProfileManager getInstance() {
        if (instance == null) {
            synchronized (ProfileManager.class) {
                if (instance == null) {
                    Context applicationContext = FacebookSdk.getApplicationContext();
                    LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(applicationContext);
                    instance = new ProfileManager(localBroadcastManager, new ProfileCache());
                }
            }
        }
        return instance;
    }

    Profile getCurrentProfile() {
        return this.currentProfile;
    }

    boolean loadCurrentProfile() {
        Profile profile = this.profileCache.load();
        if (profile == null) {
            return false;
        }
        setCurrentProfile(profile, false);
        return true;
    }

    void setCurrentProfile(Profile currentProfile) {
        setCurrentProfile(currentProfile, true);
    }

    private void setCurrentProfile(Profile currentProfile, boolean writeToCache) {
        Profile oldProfile = this.currentProfile;
        this.currentProfile = currentProfile;
        if (writeToCache) {
            if (currentProfile != null) {
                this.profileCache.save(currentProfile);
            } else {
                this.profileCache.clear();
            }
        }
        if (!Utility.areObjectsEqual(oldProfile, currentProfile)) {
            sendCurrentProfileChangedBroadcast(oldProfile, currentProfile);
        }
    }

    private void sendCurrentProfileChangedBroadcast(Profile oldProfile, Profile currentProfile) {
        Intent intent = new Intent("com.facebook.sdk.ACTION_CURRENT_PROFILE_CHANGED");
        intent.putExtra("com.facebook.sdk.EXTRA_OLD_PROFILE", oldProfile);
        intent.putExtra("com.facebook.sdk.EXTRA_NEW_PROFILE", currentProfile);
        this.localBroadcastManager.sendBroadcast(intent);
    }
}
