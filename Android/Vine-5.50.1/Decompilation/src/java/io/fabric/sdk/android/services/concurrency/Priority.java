package io.fabric.sdk.android.services.concurrency;

/* loaded from: classes.dex */
public enum Priority {
    LOW,
    NORMAL,
    HIGH,
    IMMEDIATE;

    static <Y> int compareTo(PriorityProvider self, Y other) {
        Priority otherPriority;
        if (other instanceof PriorityProvider) {
            otherPriority = ((PriorityProvider) other).getPriority();
        } else {
            otherPriority = NORMAL;
        }
        return otherPriority.ordinal() - self.getPriority().ordinal();
    }
}
