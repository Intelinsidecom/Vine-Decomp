package com.fasterxml.jackson.core.io;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.util.BufferRecycler;
import com.fasterxml.jackson.core.util.TextBuffer;

/* loaded from: classes2.dex */
public class IOContext {
    protected final BufferRecycler _bufferRecycler;
    protected char[] _concatCBuffer;
    protected JsonEncoding _encoding;
    protected final boolean _managedResource;
    protected char[] _nameCopyBuffer;
    protected byte[] _readIOBuffer;
    protected final Object _sourceRef;
    protected char[] _tokenCBuffer;
    protected byte[] _writeEncodingBuffer;

    public IOContext(BufferRecycler br, Object sourceRef, boolean managedResource) {
        this._bufferRecycler = br;
        this._sourceRef = sourceRef;
        this._managedResource = managedResource;
    }

    public void setEncoding(JsonEncoding enc) {
        this._encoding = enc;
    }

    public Object getSourceReference() {
        return this._sourceRef;
    }

    public JsonEncoding getEncoding() {
        return this._encoding;
    }

    public boolean isResourceManaged() {
        return this._managedResource;
    }

    public TextBuffer constructTextBuffer() {
        return new TextBuffer(this._bufferRecycler);
    }

    public byte[] allocReadIOBuffer() {
        _verifyAlloc(this._readIOBuffer);
        byte[] bArrAllocByteBuffer = this._bufferRecycler.allocByteBuffer(0);
        this._readIOBuffer = bArrAllocByteBuffer;
        return bArrAllocByteBuffer;
    }

    public byte[] allocWriteEncodingBuffer() {
        _verifyAlloc(this._writeEncodingBuffer);
        byte[] bArrAllocByteBuffer = this._bufferRecycler.allocByteBuffer(1);
        this._writeEncodingBuffer = bArrAllocByteBuffer;
        return bArrAllocByteBuffer;
    }

    public char[] allocTokenBuffer() {
        _verifyAlloc(this._tokenCBuffer);
        char[] cArrAllocCharBuffer = this._bufferRecycler.allocCharBuffer(0);
        this._tokenCBuffer = cArrAllocCharBuffer;
        return cArrAllocCharBuffer;
    }

    public char[] allocTokenBuffer(int minSize) {
        _verifyAlloc(this._tokenCBuffer);
        char[] cArrAllocCharBuffer = this._bufferRecycler.allocCharBuffer(0, minSize);
        this._tokenCBuffer = cArrAllocCharBuffer;
        return cArrAllocCharBuffer;
    }

    public char[] allocConcatBuffer() {
        _verifyAlloc(this._concatCBuffer);
        char[] cArrAllocCharBuffer = this._bufferRecycler.allocCharBuffer(1);
        this._concatCBuffer = cArrAllocCharBuffer;
        return cArrAllocCharBuffer;
    }

    public void releaseReadIOBuffer(byte[] buf) {
        if (buf != null) {
            _verifyRelease(buf, this._readIOBuffer);
            this._readIOBuffer = null;
            this._bufferRecycler.releaseByteBuffer(0, buf);
        }
    }

    public void releaseWriteEncodingBuffer(byte[] buf) {
        if (buf != null) {
            _verifyRelease(buf, this._writeEncodingBuffer);
            this._writeEncodingBuffer = null;
            this._bufferRecycler.releaseByteBuffer(1, buf);
        }
    }

    public void releaseTokenBuffer(char[] buf) {
        if (buf != null) {
            _verifyRelease(buf, this._tokenCBuffer);
            this._tokenCBuffer = null;
            this._bufferRecycler.releaseCharBuffer(0, buf);
        }
    }

    public void releaseConcatBuffer(char[] buf) {
        if (buf != null) {
            _verifyRelease(buf, this._concatCBuffer);
            this._concatCBuffer = null;
            this._bufferRecycler.releaseCharBuffer(1, buf);
        }
    }

    public void releaseNameCopyBuffer(char[] buf) {
        if (buf != null) {
            _verifyRelease(buf, this._nameCopyBuffer);
            this._nameCopyBuffer = null;
            this._bufferRecycler.releaseCharBuffer(3, buf);
        }
    }

    protected final void _verifyAlloc(Object buffer) {
        if (buffer != null) {
            throw new IllegalStateException("Trying to call same allocXxx() method second time");
        }
    }

    protected final void _verifyRelease(byte[] toRelease, byte[] src) {
        if (toRelease != src && toRelease.length < src.length) {
            throw wrongBuf();
        }
    }

    protected final void _verifyRelease(char[] toRelease, char[] src) {
        if (toRelease != src && toRelease.length < src.length) {
            throw wrongBuf();
        }
    }

    private IllegalArgumentException wrongBuf() {
        return new IllegalArgumentException("Trying to release buffer smaller than original");
    }
}
