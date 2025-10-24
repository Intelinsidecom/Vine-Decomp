package co.vine.android.service.components;

import android.os.Looper;
import java.lang.Enum;
import java.util.ArrayList;

/* loaded from: classes.dex */
public abstract class NotifiableComponent<E extends Enum<E>, K> extends ActionCodeComponent<E> {
    protected final ArrayList<K> mListeners = new ArrayList<>();

    public void addListener(K listener) {
        checkLooper();
        if (!this.mListeners.contains(listener)) {
            this.mListeners.add(listener);
        }
    }

    public void removeListener(K listener) {
        checkLooper();
        this.mListeners.remove(listener);
    }

    private void checkLooper() {
        if (Looper.getMainLooper() != Looper.myLooper()) {
            throw new IllegalAccessError("You have to run this method on the main thread.");
        }
    }
}
