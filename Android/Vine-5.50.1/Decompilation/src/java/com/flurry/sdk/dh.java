package com.flurry.sdk;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class dh {
    private static final String a = dh.class.getSimpleName();
    private boolean b;
    private List<de> c;
    private long d = -1;

    public void a(boolean z) {
        this.b = z;
    }

    public boolean a() {
        return this.b;
    }

    public void a(List<de> list) {
        this.c = list;
    }

    public List<de> b() {
        return this.c;
    }

    public void a(long j) {
        this.d = j;
    }

    public long c() {
        return this.d;
    }

    public void a(DataOutputStream dataOutputStream, String str, String str2) throws IOException {
        try {
            try {
                dataOutputStream.writeShort(46586);
                dataOutputStream.writeShort(2);
                dataOutputStream.writeUTF(str);
                dataOutputStream.writeUTF(str2);
                dataOutputStream.writeBoolean(this.b);
                dataOutputStream.writeLong(this.d);
                int size = this.c.size() - 1;
                while (true) {
                    if (size < 0) {
                        break;
                    }
                    byte[] bArrA = this.c.get(size).a();
                    int length = bArrA.length;
                    if (length + 2 + dataOutputStream.size() > 50000) {
                        el.a(6, a, "discarded sessions: " + size);
                        break;
                    } else {
                        dataOutputStream.writeShort(length);
                        dataOutputStream.write(bArrA);
                        size--;
                    }
                }
                dataOutputStream.writeShort(0);
            } catch (Throwable th) {
                el.a(6, a, "", th);
                throw new IOException(th.getMessage());
            }
        } finally {
            fb.a(dataOutputStream);
        }
    }

    public boolean a(DataInputStream dataInputStream, String str) throws IOException {
        boolean zA = false;
        try {
            try {
                int unsignedShort = dataInputStream.readUnsignedShort();
                el.a(4, a, "Magic: " + unsignedShort);
                if (unsignedShort == 46586) {
                    zA = a(str, dataInputStream);
                } else {
                    el.a(3, a, "Unexpected file type");
                }
                return zA;
            } catch (Throwable th) {
                el.a(6, a, "Error when loading persistent file", th);
                throw new IOException(th.getMessage());
            }
        } finally {
            fb.a(dataInputStream);
        }
    }

    private boolean a(String str, DataInputStream dataInputStream) throws IOException {
        int unsignedShort = dataInputStream.readUnsignedShort();
        el.a(3, a, "File version: " + unsignedShort);
        if (unsignedShort > 2) {
            el.a(6, a, "Unknown agent file version: " + unsignedShort);
            throw new IOException("Unknown agent file version: " + unsignedShort);
        }
        if (unsignedShort >= 2) {
            String utf = dataInputStream.readUTF();
            el.a(3, a, "Loading API key: " + a(str));
            if (utf.equals(str)) {
                ArrayList arrayList = new ArrayList();
                dataInputStream.readUTF();
                boolean z = dataInputStream.readBoolean();
                long j = dataInputStream.readLong();
                el.a(3, a, "Loading session reports");
                int i = 0;
                while (true) {
                    int unsignedShort2 = dataInputStream.readUnsignedShort();
                    if (unsignedShort2 != 0) {
                        byte[] bArr = new byte[unsignedShort2];
                        dataInputStream.readFully(bArr);
                        arrayList.add(0, new de(bArr));
                        i++;
                        el.a(3, a, "Session report added: " + i);
                    } else {
                        el.a(3, a, "Persistent file loaded");
                        a(z);
                        a(j);
                        a(arrayList);
                        return true;
                    }
                }
            } else {
                el.a(3, a, "Api keys do not match, old: " + a(str) + ", new: " + a(utf));
                return false;
            }
        } else {
            el.a(5, a, "Deleting old file version: " + unsignedShort);
            return false;
        }
    }

    private static String a(String str) {
        if (str != null && str.length() > 4) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < str.length() - 4; i++) {
                sb.append('*');
            }
            sb.append(str.substring(str.length() - 4));
            return sb.toString();
        }
        return str;
    }
}
