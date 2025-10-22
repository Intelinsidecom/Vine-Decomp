package com.fasterxml.jackson.core;

import com.fasterxml.jackson.core.util.RequestPayload;

/* loaded from: classes2.dex */
public class JsonParseException extends JsonProcessingException {
    private static final long serialVersionUID = 2;
    protected transient JsonParser _processor;
    protected RequestPayload _requestPayload;

    public JsonParseException(JsonParser p, String msg) {
        super(msg, p == null ? null : p.getCurrentLocation());
        this._processor = p;
    }

    public JsonParseException(JsonParser p, String msg, Throwable root) {
        super(msg, p == null ? null : p.getCurrentLocation(), root);
        this._processor = p;
    }

    public JsonParseException withRequestPayload(RequestPayload p) {
        this._requestPayload = p;
        return this;
    }

    @Override // com.fasterxml.jackson.core.JsonProcessingException, java.lang.Throwable
    public String getMessage() {
        String msg = super.getMessage();
        if (this._requestPayload != null) {
            return msg + "\nRequest payload : " + this._requestPayload.toString();
        }
        return msg;
    }
}
