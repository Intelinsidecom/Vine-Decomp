package io.realm.internal;

/* loaded from: classes.dex */
public interface TableOrView {
    void clear();

    void remove(long j);

    long size();

    long sync();

    TableQuery where();
}
