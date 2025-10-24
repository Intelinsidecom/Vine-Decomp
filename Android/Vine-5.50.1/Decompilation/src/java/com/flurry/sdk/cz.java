package com.flurry.sdk;

import android.os.Build;
import com.flurry.android.FlurryAgent;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.nio.ByteBuffer;
import java.security.DigestOutputStream;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class cz {
    private static final String a = cz.class.getSimpleName();
    private byte[] b;

    public cz(String str, String str2, boolean z, boolean z2, long j, long j2, List<de> list, Map<Cdo, ByteBuffer> map, Map<String, List<String>> map2, Map<String, List<String>> map3, Map<String, Map<String, String>> map4, long j3) throws Throwable {
        DataOutputStream dataOutputStream;
        byte[] byteArray;
        ea eaVar;
        ByteArrayOutputStream byteArrayOutputStream;
        DigestOutputStream digestOutputStream;
        this.b = null;
        DataOutputStream dataOutputStream2 = null;
        try {
            eaVar = new ea();
            byteArrayOutputStream = new ByteArrayOutputStream();
            digestOutputStream = new DigestOutputStream(byteArrayOutputStream, eaVar);
            dataOutputStream = new DataOutputStream(digestOutputStream);
        } catch (Throwable th) {
            th = th;
        }
        try {
            dataOutputStream.writeShort(29);
            dataOutputStream.writeShort(0);
            dataOutputStream.writeLong(0L);
            dataOutputStream.writeShort(0);
            dataOutputStream.writeShort(3);
            dataOutputStream.writeShort(FlurryAgent.getAgentVersion());
            dataOutputStream.writeLong(j3);
            dataOutputStream.writeUTF(str);
            dataOutputStream.writeUTF(str2);
            dataOutputStream.writeShort(map.size());
            for (Map.Entry<Cdo, ByteBuffer> entry : map.entrySet()) {
                dataOutputStream.writeShort(entry.getKey().d);
                byte[] bArrArray = entry.getValue().array();
                dataOutputStream.writeShort(bArrArray.length);
                dataOutputStream.write(bArrArray);
            }
            dataOutputStream.writeByte(0);
            dataOutputStream.writeBoolean(z);
            dataOutputStream.writeBoolean(z2);
            dataOutputStream.writeLong(j);
            dataOutputStream.writeLong(j2);
            dataOutputStream.writeShort(6);
            dataOutputStream.writeUTF("device.model");
            dataOutputStream.writeUTF(Build.MODEL);
            dataOutputStream.writeByte(0);
            dataOutputStream.writeUTF("build.brand");
            dataOutputStream.writeUTF(Build.BRAND);
            dataOutputStream.writeByte(0);
            dataOutputStream.writeUTF("build.id");
            dataOutputStream.writeUTF(Build.ID);
            dataOutputStream.writeByte(0);
            dataOutputStream.writeUTF("version.release");
            dataOutputStream.writeUTF(Build.VERSION.RELEASE);
            dataOutputStream.writeByte(0);
            dataOutputStream.writeUTF("build.device");
            dataOutputStream.writeUTF(Build.DEVICE);
            dataOutputStream.writeByte(0);
            dataOutputStream.writeUTF("build.product");
            dataOutputStream.writeUTF(Build.PRODUCT);
            dataOutputStream.writeByte(0);
            dataOutputStream.writeShort(map2 != null ? map2.keySet().size() : 0);
            if (map2 != null) {
                el.a(3, a, "sending referrer values because it exists");
                for (Map.Entry<String, List<String>> entry2 : map2.entrySet()) {
                    el.a(3, a, "Referrer Entry:  " + entry2.getKey() + "=" + entry2.getValue());
                    dataOutputStream.writeUTF(entry2.getKey());
                    el.a(3, a, "referrer key is :" + entry2.getKey());
                    dataOutputStream.writeShort(entry2.getValue().size());
                    for (String str3 : entry2.getValue()) {
                        dataOutputStream.writeUTF(str3);
                        el.a(3, a, "referrer value is :" + str3);
                    }
                }
            }
            dataOutputStream.writeBoolean(false);
            int size = map3 != null ? map3.keySet().size() : 0;
            el.a(3, a, "optionsMapSize is:  " + size);
            dataOutputStream.writeShort(size);
            if (map3 != null) {
                el.a(3, a, "sending launch options");
                for (Map.Entry<String, List<String>> entry3 : map3.entrySet()) {
                    el.a(3, a, "Launch Options Key:  " + entry3.getKey());
                    dataOutputStream.writeUTF(entry3.getKey());
                    dataOutputStream.writeShort(entry3.getValue().size());
                    for (String str4 : entry3.getValue()) {
                        dataOutputStream.writeUTF(str4);
                        el.a(3, a, "Launch Options value is :" + str4);
                    }
                }
            }
            int size2 = map4 != null ? map4.keySet().size() : 0;
            el.a(3, a, "numOriginAttributions is:  " + size);
            dataOutputStream.writeShort(size2);
            if (map4 != null) {
                for (Map.Entry<String, Map<String, String>> entry4 : map4.entrySet()) {
                    el.a(3, a, "Origin Atttribute Key:  " + entry4.getKey());
                    dataOutputStream.writeUTF(entry4.getKey());
                    dataOutputStream.writeShort(entry4.getValue().size());
                    el.a(3, a, "Origin Attribute Map Size for " + entry4.getKey() + ":  " + entry4.getValue().size());
                    for (Map.Entry<String, String> entry5 : entry4.getValue().entrySet()) {
                        el.a(3, a, "Origin Atttribute for " + entry4.getKey() + ":  " + entry5.getKey() + ":" + entry5.getValue());
                        dataOutputStream.writeUTF(entry5.getKey() != null ? entry5.getKey() : "");
                        dataOutputStream.writeUTF(entry5.getValue() != null ? entry5.getValue() : "");
                    }
                }
            }
            int size3 = list.size();
            dataOutputStream.writeShort(size3);
            for (int i = 0; i < size3; i++) {
                dataOutputStream.write(list.get(i).a());
            }
            dataOutputStream.writeShort(0);
            digestOutputStream.on(false);
            dataOutputStream.write(eaVar.a());
            dataOutputStream.close();
            byteArray = byteArrayOutputStream.toByteArray();
            fb.a(dataOutputStream);
        } catch (Throwable th2) {
            th = th2;
            fb.a(dataOutputStream);
            throw th;
        }
        this.b = byteArray;
    }

    public byte[] a() {
        return this.b;
    }
}
