package co.vine.android.util.ClientFlags;

import android.content.Context;

/* loaded from: classes.dex */
public interface FlagKeyValueStore<T> {
    T getValue(Context context, String str, T t);

    void setValue(Context context, String str, T t);
}
