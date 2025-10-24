package org.aspectj.lang;

/* loaded from: classes2.dex */
public class NoAspectBoundException extends RuntimeException {
    Throwable cause;

    public NoAspectBoundException(String aspectName, Throwable inner) {
        super(inner != null ? new StringBuffer().append("Exception while initializing ").append(aspectName).append(": ").append(inner).toString() : aspectName);
        this.cause = inner;
    }

    public NoAspectBoundException() {
    }

    @Override // java.lang.Throwable
    public Throwable getCause() {
        return this.cause;
    }
}
