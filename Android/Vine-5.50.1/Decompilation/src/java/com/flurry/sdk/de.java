package com.flurry.sdk;

import com.flurry.sdk.cu;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class de {
    private static final String b = de.class.getSimpleName();
    byte[] a;

    public de(byte[] bArr) {
        this.a = bArr;
    }

    public de(df dfVar) throws Throwable {
        DataOutputStream dataOutputStream;
        int i;
        DataOutputStream dataOutputStream2 = null;
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            dataOutputStream = new DataOutputStream(byteArrayOutputStream);
            try {
                dataOutputStream.writeShort(3);
                dataOutputStream.writeUTF(dfVar.a());
                dataOutputStream.writeLong(dfVar.b());
                dataOutputStream.writeLong(dfVar.c());
                dataOutputStream.writeLong(dfVar.d());
                Map<String, String> mapE = dfVar.e();
                if (mapE == null) {
                    dataOutputStream.writeShort(0);
                } else {
                    dataOutputStream.writeShort(mapE.size());
                    for (Map.Entry<String, String> entry : mapE.entrySet()) {
                        dataOutputStream.writeUTF(entry.getKey());
                        dataOutputStream.writeUTF(entry.getValue());
                        dataOutputStream.writeByte(0);
                    }
                }
                dataOutputStream.writeUTF(dfVar.f());
                dataOutputStream.writeUTF(dfVar.g());
                dataOutputStream.writeByte(dfVar.h());
                dataOutputStream.writeUTF(dfVar.i());
                if (dfVar.j() == null) {
                    dataOutputStream.writeBoolean(false);
                } else {
                    dataOutputStream.writeBoolean(true);
                    dataOutputStream.writeDouble(a(dfVar.j().getLatitude()));
                    dataOutputStream.writeDouble(a(dfVar.j().getLongitude()));
                    dataOutputStream.writeFloat(dfVar.j().getAccuracy());
                }
                dataOutputStream.writeInt(dfVar.k());
                dataOutputStream.writeByte(-1);
                dataOutputStream.writeByte(-1);
                dataOutputStream.writeByte(dfVar.l());
                if (dfVar.m() == null) {
                    dataOutputStream.writeBoolean(false);
                } else {
                    dataOutputStream.writeBoolean(true);
                    dataOutputStream.writeLong(dfVar.m().longValue());
                }
                Map<String, cu.a> mapN = dfVar.n();
                if (mapN == null) {
                    dataOutputStream.writeShort(0);
                } else {
                    dataOutputStream.writeShort(mapN.size());
                    for (Map.Entry<String, cu.a> entry2 : mapN.entrySet()) {
                        dataOutputStream.writeUTF(entry2.getKey());
                        dataOutputStream.writeInt(entry2.getValue().a);
                    }
                }
                List<cy> listO = dfVar.o();
                if (listO == null) {
                    dataOutputStream.writeShort(0);
                } else {
                    dataOutputStream.writeShort(listO.size());
                    Iterator<cy> it = listO.iterator();
                    while (it.hasNext()) {
                        dataOutputStream.write(it.next().e());
                    }
                }
                dataOutputStream.writeBoolean(dfVar.p());
                List<cx> listR = dfVar.r();
                if (listR != null) {
                    int i2 = 0;
                    int iA = 0;
                    int i3 = 0;
                    while (true) {
                        if (i2 >= listR.size()) {
                            i = i3;
                            break;
                        }
                        iA += listR.get(i2).a();
                        if (iA > 160000) {
                            el.a(5, b, "Error Log size exceeded. No more event details logged.");
                            i = i3;
                            break;
                        } else {
                            i3++;
                            i2++;
                        }
                    }
                } else {
                    i = 0;
                }
                dataOutputStream.writeInt(dfVar.q());
                dataOutputStream.writeShort(i);
                for (int i4 = 0; i4 < i; i4++) {
                    dataOutputStream.write(listR.get(i4).b());
                }
                dataOutputStream.writeInt(-1);
                dataOutputStream.writeShort(0);
                dataOutputStream.writeShort(0);
                dataOutputStream.writeShort(0);
                this.a = byteArrayOutputStream.toByteArray();
                fb.a(dataOutputStream);
            } catch (IOException e) {
                e = e;
                dataOutputStream2 = dataOutputStream;
                try {
                    el.a(6, b, "", e);
                    throw e;
                } catch (Throwable th) {
                    th = th;
                    dataOutputStream = dataOutputStream2;
                    fb.a(dataOutputStream);
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                fb.a(dataOutputStream);
                throw th;
            }
        } catch (IOException e2) {
            e = e2;
        } catch (Throwable th3) {
            th = th3;
            dataOutputStream = null;
        }
    }

    public byte[] a() {
        return this.a;
    }

    double a(double d) {
        return Math.round(d * 1000.0d) / 1000.0d;
    }
}
