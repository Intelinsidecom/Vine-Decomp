package org.parceler;

import java.util.Map;

/* loaded from: classes.dex */
public interface Repository<T> {
    Map<Class, T> get();
}
