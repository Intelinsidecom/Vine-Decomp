package com.google.android.exoplayer.upstream;

/* loaded from: classes.dex */
public interface Allocator {
    Allocation allocate();

    void blockWhileTotalBytesAllocatedExceeds(int i) throws InterruptedException;

    int getIndividualAllocationLength();

    int getTotalBytesAllocated();

    void release(Allocation allocation);

    void trim(int i);
}
