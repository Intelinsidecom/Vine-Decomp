package com.googlecode.javacpp;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/* loaded from: classes.dex */
public class Pointer {
    private static final ReferenceQueue<Pointer> referenceQueue = new ReferenceQueue<>();
    protected long address;
    protected int capacity;
    private Deallocator deallocator;
    protected int limit;
    protected int position;

    /* JADX INFO: Access modifiers changed from: protected */
    public interface Deallocator {
        void deallocate();
    }

    private native void allocate(Buffer buffer);

    private native ByteBuffer asDirectBuffer();

    public static native Pointer memchr(Pointer pointer, int i, long j);

    public static native int memcmp(Pointer pointer, Pointer pointer2, long j);

    public static native Pointer memcpy(Pointer pointer, Pointer pointer2, long j);

    public static native Pointer memmove(Pointer pointer, Pointer pointer2, long j);

    public static native Pointer memset(Pointer pointer, int i, long j);

    public Pointer() {
        this.address = 0L;
        this.position = 0;
        this.limit = 0;
        this.capacity = 0;
        this.deallocator = null;
    }

    public Pointer(final Pointer p) {
        this.address = 0L;
        this.position = 0;
        this.limit = 0;
        this.capacity = 0;
        this.deallocator = null;
        if (p != null) {
            this.address = p.address;
            this.position = p.position;
            this.limit = p.limit;
            this.capacity = p.capacity;
            if (p.deallocator != null) {
                this.deallocator = new Deallocator() { // from class: com.googlecode.javacpp.Pointer.1
                    @Override // com.googlecode.javacpp.Pointer.Deallocator
                    public void deallocate() {
                        p.deallocate();
                    }
                };
            }
        }
    }

    public Pointer(final Buffer b) {
        this.address = 0L;
        this.position = 0;
        this.limit = 0;
        this.capacity = 0;
        this.deallocator = null;
        if (b != null) {
            allocate(b);
        }
        if (!isNull()) {
            this.position = b.position();
            this.limit = b.limit();
            this.capacity = b.capacity();
            this.deallocator = new Deallocator() { // from class: com.googlecode.javacpp.Pointer.2
                Buffer bb;

                {
                    this.bb = b;
                }

                @Override // com.googlecode.javacpp.Pointer.Deallocator
                public void deallocate() {
                    this.bb = null;
                }
            };
        }
    }

    void init(long allocatedAddress, int allocatedCapacity, long deallocatorAddress) {
        this.address = allocatedAddress;
        this.position = 0;
        this.limit = allocatedCapacity;
        this.capacity = allocatedCapacity;
        deallocator(new NativeDeallocator(this, deallocatorAddress));
    }

    protected static <P extends Pointer> P withDeallocator(P p) {
        return (P) p.deallocator(new CustomDeallocator(p));
    }

    protected static class CustomDeallocator extends DeallocatorReference implements Deallocator {
        Method method;
        Pointer pointer;

        public CustomDeallocator(Pointer p) throws NoSuchMethodException, SecurityException {
            super(p, null);
            this.pointer = null;
            this.method = null;
            this.deallocator = this;
            Class<?> cls = p.getClass();
            Method[] arr$ = cls.getDeclaredMethods();
            int len$ = arr$.length;
            int i$ = 0;
            while (true) {
                if (i$ >= len$) {
                    break;
                }
                Method m = arr$[i$];
                Class[] parameters = m.getParameterTypes();
                if (!Modifier.isStatic(m.getModifiers()) || !m.getReturnType().equals(Void.TYPE) || !m.getName().equals("deallocate") || parameters.length != 1 || !Pointer.class.isAssignableFrom(parameters[0])) {
                    i$++;
                } else {
                    m.setAccessible(true);
                    this.method = m;
                    break;
                }
            }
            if (this.method == null) {
                throw new RuntimeException(new NoSuchMethodException("static void " + cls.getCanonicalName() + ".deallocate(" + Pointer.class.getCanonicalName() + ")"));
            }
            try {
                Constructor<?> constructor = cls.getConstructor(Pointer.class);
                constructor.setAccessible(true);
                this.pointer = (Pointer) constructor.newInstance(p);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }

        @Override // com.googlecode.javacpp.Pointer.Deallocator
        public void deallocate() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            try {
                this.method.invoke(null, this.pointer);
                this.pointer.setNull();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    protected static class NativeDeallocator extends DeallocatorReference implements Deallocator {
        private long allocatedAddress;
        private long deallocatorAddress;

        private native void deallocate(long j, long j2);

        NativeDeallocator(Pointer p, long deallocatorAddress) {
            super(p, null);
            this.deallocator = this;
            this.allocatedAddress = p.address;
            this.deallocatorAddress = deallocatorAddress;
        }

        @Override // com.googlecode.javacpp.Pointer.Deallocator
        public void deallocate() {
            if (this.allocatedAddress != 0 && this.deallocatorAddress != 0) {
                deallocate(this.allocatedAddress, this.deallocatorAddress);
                this.deallocatorAddress = 0L;
                this.allocatedAddress = 0L;
            }
        }
    }

    private static class DeallocatorReference extends PhantomReference<Pointer> {
        static DeallocatorReference head = null;
        Deallocator deallocator;
        DeallocatorReference next;
        DeallocatorReference prev;

        DeallocatorReference(Pointer p, Deallocator deallocator) {
            super(p, Pointer.referenceQueue);
            this.prev = null;
            this.next = null;
            this.deallocator = deallocator;
        }

        final void add() {
            synchronized (DeallocatorReference.class) {
                if (head == null) {
                    head = this;
                } else {
                    this.next = head;
                    DeallocatorReference deallocatorReference = this.next;
                    head = this;
                    deallocatorReference.prev = this;
                }
            }
        }

        final void remove() {
            synchronized (DeallocatorReference.class) {
                if (this.prev != this || this.next != this) {
                    if (this.prev == null) {
                        head = this.next;
                    } else {
                        this.prev.next = this.next;
                    }
                    if (this.next != null) {
                        this.next.prev = this.prev;
                    }
                    this.next = this;
                    this.prev = this;
                }
            }
        }

        @Override // java.lang.ref.Reference
        public void clear() {
            super.clear();
            this.deallocator.deallocate();
        }
    }

    public static void deallocateReferences() {
        while (true) {
            DeallocatorReference r = (DeallocatorReference) referenceQueue.poll();
            if (r != null) {
                r.clear();
                r.remove();
            } else {
                return;
            }
        }
    }

    public boolean isNull() {
        return this.address == 0;
    }

    public void setNull() {
        this.address = 0L;
    }

    public int position() {
        return this.position;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <P extends Pointer> P position(int position) {
        this.position = position;
        return this;
    }

    public int limit() {
        return this.limit;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <P extends Pointer> P limit(int limit) {
        this.limit = limit;
        return this;
    }

    public int capacity() {
        return this.capacity;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <P extends Pointer> P capacity(int capacity) {
        this.limit = capacity;
        this.capacity = capacity;
        return this;
    }

    protected Deallocator deallocator() {
        return this.deallocator;
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected <P extends Pointer> P deallocator(Deallocator deallocator) {
        if (this.deallocator != null) {
            this.deallocator.deallocate();
            this.deallocator = null;
        }
        deallocateReferences();
        if (deallocator != 0 && !deallocator.equals(null)) {
            this.deallocator = deallocator;
            DeallocatorReference r = deallocator instanceof DeallocatorReference ? (DeallocatorReference) deallocator : new DeallocatorReference(this, deallocator);
            r.add();
        }
        return this;
    }

    public void deallocate() {
        this.deallocator.deallocate();
        this.address = 0L;
    }

    public int offsetof(String member) {
        try {
            Class<?> cls = getClass();
            if (cls == Pointer.class) {
                return -1;
            }
            int offset = Loader.offsetof(cls, member);
            return offset;
        } catch (NullPointerException e) {
            return -1;
        }
    }

    public int sizeof() {
        Class c = getClass();
        if (c == Pointer.class || c == BytePointer.class) {
            return 1;
        }
        return offsetof("sizeof");
    }

    public ByteBuffer asByteBuffer() {
        if (isNull()) {
            return null;
        }
        if (this.limit > 0 && this.limit < this.position) {
            throw new IllegalArgumentException("limit < position: (" + this.limit + " < " + this.position + ")");
        }
        int valueSize = sizeof();
        int arrayPosition = this.position;
        int arrayLimit = this.limit;
        this.position = valueSize * arrayPosition;
        this.limit = (arrayLimit <= 0 ? arrayPosition + 1 : arrayLimit) * valueSize;
        ByteBuffer byteBufferOrder = asDirectBuffer().order(ByteOrder.nativeOrder());
        this.position = arrayPosition;
        this.limit = arrayLimit;
        return byteBufferOrder;
    }

    public Buffer asBuffer() {
        return asByteBuffer();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <P extends Pointer> P put(Pointer p) {
        if (p.limit > 0 && p.limit < p.position) {
            throw new IllegalArgumentException("limit < position: (" + p.limit + " < " + p.position + ")");
        }
        int size = sizeof();
        int psize = p.sizeof();
        int length = psize * (p.limit <= 0 ? 1 : p.limit - p.position);
        this.position *= size;
        p.position *= psize;
        memcpy(this, p, length);
        this.position /= size;
        p.position /= psize;
        return this;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <P extends Pointer> P fill(int b) {
        if (this.limit > 0 && this.limit < this.position) {
            throw new IllegalArgumentException("limit < position: (" + this.limit + " < " + this.position + ")");
        }
        int size = sizeof();
        int length = size * (this.limit <= 0 ? 1 : this.limit - this.position);
        this.position *= size;
        memset(this, b, length);
        this.position /= size;
        return this;
    }

    public <P extends Pointer> P zero() {
        return (P) fill(0);
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null) {
            return isNull();
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        Pointer other = (Pointer) obj;
        return this.address == other.address && this.position == other.position;
    }

    public int hashCode() {
        return (int) this.address;
    }

    public String toString() {
        return getClass().getName() + "[address=0x" + Long.toHexString(this.address) + ",position=" + this.position + ",limit=" + this.limit + ",capacity=" + this.capacity + ",deallocator=" + this.deallocator + "]";
    }
}
