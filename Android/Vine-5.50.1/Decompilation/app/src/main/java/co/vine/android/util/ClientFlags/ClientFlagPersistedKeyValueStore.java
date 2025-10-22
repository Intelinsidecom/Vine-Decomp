package co.vine.android.util.ClientFlags;

import android.content.Context;
import co.vine.android.PersistentPreference;

/* loaded from: classes.dex */
public class ClientFlagPersistedKeyValueStore<T> implements FlagKeyValueStore<T> {
    private final PersistentPreference mPersistentPreference;

    public ClientFlagPersistedKeyValueStore(PersistentPreference persistentPreference) {
        this.mPersistentPreference = persistentPreference;
    }

    @Override // co.vine.android.util.ClientFlags.FlagKeyValueStore
    public T getValue(Context context, String str, T t) {
        if (this.mPersistentPreference.getPref(context).contains(str)) {
            return (T) this.mPersistentPreference.getPrefValue(context, str, t);
        }
        return null;
    }

    @Override // co.vine.android.util.ClientFlags.FlagKeyValueStore
    public void setValue(Context context, String key, T value) {
        if (value != null) {
            this.mPersistentPreference.setPrefValue(context, key, value);
        }
    }
}
