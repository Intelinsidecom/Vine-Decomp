package com.flurry.sdk;

import android.os.Looper;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

/* loaded from: classes.dex */
public class dd {
    String b;
    LinkedHashMap<String, List<String>> c;
    private static final String d = dd.class.getSimpleName();
    static final Integer a = 50;

    public dd(String str) {
        a(str);
    }

    void a(String str) {
        this.c = new LinkedHashMap<>();
        this.b = str + "Main";
        List<String> listE = e(this.b);
        if (listE != null) {
            for (String str2 : listE) {
                List<String> listE2 = e(str2);
                if (listE2 != null) {
                    this.c.put(str2, listE2);
                }
            }
        }
    }

    private synchronized void c() {
        LinkedList linkedList = new LinkedList(this.c.keySet());
        b();
        if (!linkedList.isEmpty()) {
            a(this.b, linkedList);
        }
    }

    public synchronized void a(dc dcVar, String str) {
        List<String> linkedList;
        boolean z = false;
        synchronized (this) {
            el.a(4, d, "addBlockInfo");
            String strA = dcVar.a();
            List<String> list = this.c.get(str);
            if (list == null) {
                el.a(4, d, "New Data Key");
                linkedList = new LinkedList();
                z = true;
            } else {
                linkedList = list;
            }
            linkedList.add(strA);
            if (linkedList.size() > a.intValue()) {
                b(linkedList.get(0));
                linkedList.remove(0);
            }
            this.c.put(str, linkedList);
            a(str, linkedList);
            if (z) {
                c();
            }
        }
    }

    boolean b(String str) {
        return new dc(str).c();
    }

    public boolean a(String str, String str2) {
        List<String> list = this.c.get(str2);
        boolean zRemove = false;
        if (list != null) {
            b(str);
            zRemove = list.remove(str);
        }
        if (list != null && !list.isEmpty()) {
            this.c.put(str2, list);
            a(str2, list);
        } else {
            d(str2);
        }
        return zRemove;
    }

    public List<String> a() {
        return new ArrayList(this.c.keySet());
    }

    public List<String> c(String str) {
        return this.c.get(str);
    }

    public synchronized boolean d(String str) {
        boolean zA;
        if (Looper.myLooper() == Looper.getMainLooper()) {
            el.a(6, d, "discardOutdatedBlocksForDataKey(ID) running on the MAIN thread!");
        }
        File fileStreamPath = dl.a().b().getFileStreamPath(".FlurrySenderIndex.info." + str);
        List<String> listC = c(str);
        if (listC != null) {
            el.a(4, d, "discardOutdatedBlocksForDataKey: notSentBlocks = " + listC.size());
            for (int i = 0; i < listC.size(); i++) {
                String str2 = listC.get(i);
                b(str2);
                el.a(4, d, "discardOutdatedBlocksForDataKey: removed block = " + str2);
            }
        }
        this.c.remove(str);
        zA = a(fileStreamPath);
        c();
        return zA;
    }

    private synchronized boolean a(File file) {
        boolean zDelete;
        zDelete = false;
        if (file != null) {
            if (file.exists()) {
                el.a(4, d, "Trying to delete persistence file : " + file.getAbsolutePath());
                zDelete = file.delete();
                if (zDelete) {
                    el.a(4, d, "Deleted persistence file");
                } else {
                    el.a(6, d, "Cannot delete persistence file");
                }
            }
        }
        return zDelete;
    }

    void b() {
        a(dl.a().b().getFileStreamPath(".FlurrySenderIndex.info." + this.b));
    }

    private synchronized List<String> e(String str) {
        DataInputStream dataInputStream;
        ArrayList arrayList;
        Throwable th;
        int unsignedShort;
        ArrayList arrayList2 = null;
        synchronized (this) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                el.a(6, d, "readFromFile(byte[], ID) running on the MAIN thread!");
            }
            File fileStreamPath = dl.a().b().getFileStreamPath(".FlurrySenderIndex.info." + str);
            if (fileStreamPath.exists()) {
                try {
                    dataInputStream = new DataInputStream(new FileInputStream(fileStreamPath));
                    try {
                        try {
                            unsignedShort = dataInputStream.readUnsignedShort();
                        } catch (Throwable th2) {
                            th = th2;
                            fb.a(dataInputStream);
                            throw th;
                        }
                    } catch (Throwable th3) {
                        arrayList = null;
                        th = th3;
                    }
                } catch (Throwable th4) {
                    th = th4;
                    dataInputStream = null;
                    fb.a(dataInputStream);
                    throw th;
                }
                if (unsignedShort == 0) {
                    fb.a(dataInputStream);
                } else {
                    arrayList = new ArrayList(unsignedShort);
                    for (int i = 0; i < unsignedShort; i++) {
                        try {
                            int unsignedShort2 = dataInputStream.readUnsignedShort();
                            el.a(4, d, "read iter " + i + " dataLength = " + unsignedShort2);
                            byte[] bArr = new byte[unsignedShort2];
                            dataInputStream.readFully(bArr);
                            arrayList.add(new String(bArr));
                        } catch (Throwable th5) {
                            th = th5;
                            el.a(6, d, "Error when loading persistent file", th);
                            fb.a(dataInputStream);
                            arrayList2 = arrayList;
                            return arrayList2;
                        }
                    }
                    if (dataInputStream.readUnsignedShort() == 0) {
                    }
                    fb.a(dataInputStream);
                }
            } else {
                el.a(5, d, "Agent cache file doesn't exist.");
                arrayList = null;
            }
            arrayList2 = arrayList;
        }
        return arrayList2;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v3, types: [android.content.Context] */
    /* JADX WARN: Type inference failed for: r2v4, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r2v6 */
    /* JADX WARN: Type inference failed for: r2v7, types: [java.io.Closeable] */
    private synchronized boolean a(String str, List<String> list) {
        DataOutputStream dataOutputStream;
        boolean z;
        boolean z2 = false;
        synchronized (this) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                el.a(6, d, "saveToFile(byte[], ID) running on the MAIN thread!");
            }
            ?? r2 = ".FlurrySenderIndex.info." + str;
            File fileStreamPath = dl.a().b().getFileStreamPath(r2);
            try {
                try {
                } catch (Throwable th) {
                    th = th;
                    dataOutputStream = null;
                }
                if (fa.a(fileStreamPath)) {
                    dataOutputStream = new DataOutputStream(new FileOutputStream(fileStreamPath));
                    try {
                        dataOutputStream.writeShort(list.size());
                        for (int i = 0; i < list.size(); i++) {
                            byte[] bytes = list.get(i).getBytes();
                            int length = bytes.length;
                            el.a(4, d, "write iter " + i + " dataLength = " + length);
                            dataOutputStream.writeShort(length);
                            dataOutputStream.write(bytes);
                        }
                        dataOutputStream.writeShort(0);
                        z = true;
                        fb.a(dataOutputStream);
                    } catch (Throwable th2) {
                        th = th2;
                        el.a(6, d, "", th);
                        fb.a(dataOutputStream);
                        z = false;
                        z2 = z;
                        return z2;
                    }
                    z2 = z;
                } else {
                    fb.a((Closeable) null);
                }
            } catch (Throwable th3) {
                th = th3;
                fb.a((Closeable) r2);
                throw th;
            }
        }
        return z2;
    }
}
