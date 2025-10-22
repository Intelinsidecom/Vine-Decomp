package co.vine.android.util.ClientFlags;

import android.content.Context;
import co.vine.android.PersistentPreference;
import co.vine.android.util.CrossConstants;
import java.util.Date;

/* loaded from: classes.dex */
public class ClientFlag<T> implements Flag<T> {
    private final Date mCreatedAt;
    private final T mDefaultValue;
    private final String mDescription;
    private final FeatureStatus mFeatureStatus;
    private final String mKey;
    private final FlagKeyValueStore<T> mOverrideStore;
    private final FlagKeyValueStore<T> mServerStore;

    public ClientFlag(String key, T defaultValue, Date createdAt, String description, FeatureStatus featureStatus) {
        this(new ClientFlagPersistedKeyValueStore(PersistentPreference.DEFAULT), new ClientFlagPersistedKeyValueStore(PersistentPreference.CLIENT_FLAGS_OVERRIDE), key, defaultValue, createdAt, description, featureStatus);
    }

    public ClientFlag(FlagKeyValueStore<T> serverStore, FlagKeyValueStore<T> overrideStore, String key, T defaultValue, Date createdAt, String description, FeatureStatus featureStatus) {
        this.mServerStore = serverStore;
        this.mOverrideStore = overrideStore;
        this.mKey = key;
        this.mCreatedAt = createdAt;
        this.mDefaultValue = defaultValue;
        this.mDescription = description;
        this.mFeatureStatus = featureStatus;
    }

    public String getKey() {
        return this.mKey;
    }

    @Override // co.vine.android.util.ClientFlags.Flag
    public String getOverrideKey() {
        return "override_" + getKey();
    }

    public FeatureStatus getFeatureStatus() {
        return this.mFeatureStatus;
    }

    public T getDefaultValue() {
        return this.mDefaultValue;
    }

    @Override // co.vine.android.util.ClientFlags.Flag
    public T getValue(Context context) {
        T overrideValue = getOverrideValue(context);
        if (overrideValue != null) {
            return overrideValue;
        }
        T serverValue = getServerValue(context);
        switch (getFeatureStatus()) {
            case READY_FOR_TESTING:
                if (serverValue != null && !CrossConstants.IS_RELEASE_BUILD) {
                    return serverValue;
                }
                break;
            case READY_FOR_PRODUCTION:
                if (serverValue != null) {
                    return serverValue;
                }
                break;
        }
        return getDefaultValue();
    }

    public T getServerValue(Context context) {
        return this.mServerStore.getValue(context, getKey(), getDefaultValue());
    }

    @Override // co.vine.android.util.ClientFlags.Flag
    public void setServerValue(Context context, T serverValue) {
        this.mServerStore.setValue(context, getKey(), serverValue);
    }

    public T getOverrideValue(Context context) {
        return this.mOverrideStore.getValue(context, getOverrideKey(), getDefaultValue());
    }
}
