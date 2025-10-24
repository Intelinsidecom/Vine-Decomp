package com.flurry.sdk;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.IOException;

/* loaded from: classes.dex */
public final class cx {
    private int a;
    private long b;
    private String c;
    private String d;
    private String e;
    private Throwable f;

    public cx(int i, long j, String str, String str2, String str3, Throwable th) {
        this.a = i;
        this.b = j;
        this.c = str;
        this.d = str2;
        this.e = str3;
        this.f = th;
    }

    public int a() {
        return b().length;
    }

    public byte[] b() throws Throwable {
        Throwable th;
        DataOutputStream dataOutputStream;
        byte[] byteArray;
        ByteArrayOutputStream byteArrayOutputStream;
        try {
            try {
                byteArrayOutputStream = new ByteArrayOutputStream();
                dataOutputStream = new DataOutputStream(byteArrayOutputStream);
            } catch (IOException e) {
                dataOutputStream = null;
            } catch (Throwable th2) {
                th = th2;
                fb.a((Closeable) null);
                throw th;
            }
            try {
                dataOutputStream.writeShort(this.a);
                dataOutputStream.writeLong(this.b);
                dataOutputStream.writeUTF(this.c);
                dataOutputStream.writeUTF(this.d);
                dataOutputStream.writeUTF(this.e);
                if (this.f != null) {
                    if (this.c == "uncaught") {
                        dataOutputStream.writeByte(3);
                    } else {
                        dataOutputStream.writeByte(2);
                    }
                    dataOutputStream.writeByte(2);
                    StringBuilder sb = new StringBuilder("");
                    String property = System.getProperty("line.separator");
                    for (StackTraceElement stackTraceElement : this.f.getStackTrace()) {
                        sb.append(stackTraceElement);
                        sb.append(property);
                    }
                    if (this.f.getCause() != null) {
                        sb.append(property);
                        sb.append("Caused by: ");
                        for (StackTraceElement stackTraceElement2 : this.f.getCause().getStackTrace()) {
                            sb.append(stackTraceElement2);
                            sb.append(property);
                        }
                    }
                    byte[] bytes = sb.toString().getBytes();
                    dataOutputStream.writeInt(bytes.length);
                    dataOutputStream.write(bytes);
                } else {
                    dataOutputStream.writeByte(1);
                    dataOutputStream.writeByte(0);
                }
                dataOutputStream.flush();
                byteArray = byteArrayOutputStream.toByteArray();
                fb.a(dataOutputStream);
            } catch (IOException e2) {
                byteArray = new byte[0];
                fb.a(dataOutputStream);
                return byteArray;
            }
            return byteArray;
        } catch (Throwable th3) {
            th = th3;
            fb.a((Closeable) null);
            throw th;
        }
    }

    public String c() {
        return this.c;
    }
}
