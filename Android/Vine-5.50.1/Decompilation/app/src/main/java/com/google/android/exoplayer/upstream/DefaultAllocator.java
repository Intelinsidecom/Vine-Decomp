package com.google.android.exoplayer.upstream;

import com.google.android.exoplayer.util.Assertions;
import com.google.android.exoplayer.util.Util;
import java.util.Arrays;

/* loaded from: classes.dex */
public final class DefaultAllocator implements Allocator {
    private int allocatedCount;
    private Allocation[] availableAllocations;
    private int availableCount;
    private final int individualAllocationSize;
    private final byte[] initialAllocationBlock;

    public DefaultAllocator(int individualAllocationSize) {
        this(individualAllocationSize, 0);
    }

    public DefaultAllocator(int individualAllocationSize, int initialAllocationCount) {
        Assertions.checkArgument(individualAllocationSize > 0);
        Assertions.checkArgument(initialAllocationCount >= 0);
        this.individualAllocationSize = individualAllocationSize;
        this.availableCount = initialAllocationCount;
        this.availableAllocations = new Allocation[initialAllocationCount + 100];
        if (initialAllocationCount > 0) {
            this.initialAllocationBlock = new byte[initialAllocationCount * individualAllocationSize];
            for (int i = 0; i < initialAllocationCount; i++) {
                int allocationOffset = i * individualAllocationSize;
                this.availableAllocations[i] = new Allocation(this.initialAllocationBlock, allocationOffset);
            }
            return;
        }
        this.initialAllocationBlock = null;
    }

    @Override // com.google.android.exoplayer.upstream.Allocator
    public synchronized Allocation allocate() {
        Allocation allocation;
        this.allocatedCount++;
        if (this.availableCount > 0) {
            Allocation[] allocationArr = this.availableAllocations;
            int i = this.availableCount - 1;
            this.availableCount = i;
            allocation = allocationArr[i];
            this.availableAllocations[this.availableCount] = null;
        } else {
            allocation = new Allocation(new byte[this.individualAllocationSize], 0);
        }
        return allocation;
    }

    @Override // com.google.android.exoplayer.upstream.Allocator
    public synchronized void release(Allocation allocation) {
        Assertions.checkArgument(allocation.data == this.initialAllocationBlock || allocation.data.length == this.individualAllocationSize);
        this.allocatedCount--;
        if (this.availableCount == this.availableAllocations.length) {
            this.availableAllocations = (Allocation[]) Arrays.copyOf(this.availableAllocations, this.availableAllocations.length * 2);
        }
        Allocation[] allocationArr = this.availableAllocations;
        int i = this.availableCount;
        this.availableCount = i + 1;
        allocationArr[i] = allocation;
        notifyAll();
    }

    @Override // com.google.android.exoplayer.upstream.Allocator
    public synchronized void trim(int targetSize) {
        int lowIndex;
        int highIndex;
        int targetAllocationCount = Util.ceilDivide(targetSize, this.individualAllocationSize);
        int targetAvailableCount = Math.max(0, targetAllocationCount - this.allocatedCount);
        if (targetAvailableCount < this.availableCount) {
            if (this.initialAllocationBlock != null) {
                int highIndex2 = this.availableCount - 1;
                int highIndex3 = highIndex2;
                int lowIndex2 = 0;
                while (lowIndex2 <= highIndex3) {
                    Allocation lowAllocation = this.availableAllocations[lowIndex2];
                    if (lowAllocation.data == this.initialAllocationBlock) {
                        lowIndex = lowIndex2 + 1;
                        highIndex = highIndex3;
                    } else {
                        Allocation highAllocation = this.availableAllocations[lowIndex2];
                        if (highAllocation.data != this.initialAllocationBlock) {
                            highIndex = highIndex3 - 1;
                            lowIndex = lowIndex2;
                        } else {
                            lowIndex = lowIndex2 + 1;
                            this.availableAllocations[lowIndex2] = highAllocation;
                            highIndex = highIndex3 - 1;
                            this.availableAllocations[highIndex3] = lowAllocation;
                        }
                    }
                    highIndex3 = highIndex;
                    lowIndex2 = lowIndex;
                }
                targetAvailableCount = Math.max(targetAvailableCount, lowIndex2);
                if (targetAvailableCount < this.availableCount) {
                }
            }
            Arrays.fill(this.availableAllocations, targetAvailableCount, this.availableCount, (Object) null);
            this.availableCount = targetAvailableCount;
        }
    }

    @Override // com.google.android.exoplayer.upstream.Allocator
    public synchronized int getTotalBytesAllocated() {
        return this.allocatedCount * this.individualAllocationSize;
    }

    @Override // com.google.android.exoplayer.upstream.Allocator
    public synchronized void blockWhileTotalBytesAllocatedExceeds(int limit) throws InterruptedException {
        while (getTotalBytesAllocated() > limit) {
            wait();
        }
    }

    @Override // com.google.android.exoplayer.upstream.Allocator
    public int getIndividualAllocationLength() {
        return this.individualAllocationSize;
    }
}
