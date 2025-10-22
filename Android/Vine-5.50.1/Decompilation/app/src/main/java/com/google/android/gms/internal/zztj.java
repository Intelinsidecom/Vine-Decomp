package com.google.android.gms.internal;

import java.io.IOException;

/* loaded from: classes2.dex */
public class zztj extends IOException {
    public zztj(String str) {
        super(str);
    }

    static zztj zzHC() {
        return new zztj("While parsing a protocol message, the input ended unexpectedly in the middle of a field.  This could mean either than the input has been truncated or that an embedded message misreported its own length.");
    }

    static zztj zzHD() {
        return new zztj("CodedInputStream encountered an embedded string or message which claimed to have negative size.");
    }

    static zztj zzHE() {
        return new zztj("CodedInputStream encountered a malformed varint.");
    }

    static zztj zzHF() {
        return new zztj("Protocol message contained an invalid tag (zero).");
    }

    static zztj zzHG() {
        return new zztj("Protocol message end-group tag did not match expected tag.");
    }

    static zztj zzHH() {
        return new zztj("Protocol message tag had invalid wire type.");
    }

    static zztj zzHI() {
        return new zztj("Protocol message had too many levels of nesting.  May be malicious.  Use CodedInputStream.setRecursionLimit() to increase the depth limit.");
    }
}
