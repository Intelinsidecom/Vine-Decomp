package com.flurry.sdk;

import android.text.TextUtils;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class dn {
    private Map<String, Object> a = new HashMap();
    private Map<String, List<a>> b = new HashMap();

    public interface a {
        void a(String str, Object obj);
    }

    public synchronized void a(String str, Object obj) {
        if (!TextUtils.isEmpty(str) && !a(obj, this.a.get(str))) {
            if (obj == null) {
                this.a.remove(str);
            } else {
                this.a.put(str, obj);
            }
            b(str, obj);
        }
    }

    private boolean a(Object obj, Object obj2) {
        return obj == obj2 || (obj != null && obj.equals(obj2));
    }

    private void b(String str, Object obj) {
        if (this.b.get(str) != null) {
            Iterator<a> it = this.b.get(str).iterator();
            while (it.hasNext()) {
                it.next().a(str, obj);
            }
        }
    }

    public synchronized Object a(String str) {
        return this.a.get(str);
    }

    public synchronized void a(String str, a aVar) {
        if (!TextUtils.isEmpty(str) && aVar != null) {
            List<a> linkedList = this.b.get(str);
            if (linkedList == null) {
                linkedList = new LinkedList<>();
            }
            linkedList.add(aVar);
            this.b.put(str, linkedList);
        }
    }

    public synchronized boolean b(String str, a aVar) {
        List<a> list;
        boolean zRemove;
        zRemove = (TextUtils.isEmpty(str) || aVar == null || (list = this.b.get(str)) == null) ? false : list.remove(aVar);
        return zRemove;
    }
}
