package com.fasterxml.jackson.core;

import java.io.IOException;

/* loaded from: classes2.dex */
public class JsonProcessingException extends IOException {
    static final long serialVersionUID = 123;
    protected JsonLocation _location;

    protected JsonProcessingException(String msg, JsonLocation loc, Throwable rootCause) {
        super(msg);
        if (rootCause != null) {
            initCause(rootCause);
        }
        this._location = loc;
    }

    protected JsonProcessingException(String msg, JsonLocation loc) {
        this(msg, loc, null);
    }

    public JsonLocation getLocation() {
        return this._location;
    }

    protected String getMessageSuffix() {
        return null;
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        String msg = super.getMessage();
        if (msg == null) {
            msg = "N/A";
        }
        JsonLocation loc = getLocation();
        String suffix = getMessageSuffix();
        if (loc != null || suffix != null) {
            StringBuilder sb = new StringBuilder(100);
            sb.append(msg);
            if (suffix != null) {
                sb.append(suffix);
            }
            if (loc != null) {
                sb.append('\n');
                sb.append(" at ");
                sb.append(loc.toString());
            }
            return sb.toString();
        }
        return msg;
    }

    @Override // java.lang.Throwable
    public String toString() {
        return getClass().getName() + ": " + getMessage();
    }
}
