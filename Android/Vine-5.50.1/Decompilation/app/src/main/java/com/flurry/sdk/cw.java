package com.flurry.sdk;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class cw {
    private static final String a = cw.class.getSimpleName();

    public Map<String, List<String>> a(String str) {
        el.a(3, a, "Parsing referrer map");
        if (str == null) {
            return Collections.emptyMap();
        }
        HashMap map = new HashMap();
        String[] strArrSplit = str.split("&");
        int length = strArrSplit.length;
        for (int i = 0; i < length; i++) {
            String[] strArrSplit2 = strArrSplit[i].split("=");
            if (strArrSplit2.length != 2) {
                el.a(5, a, "Invalid referrer Element: " + strArrSplit[i] + " in referrer tag " + str);
            } else {
                String strDecode = URLDecoder.decode(strArrSplit2[0]);
                String strDecode2 = URLDecoder.decode(strArrSplit2[1]);
                if (map.get(strDecode) == null) {
                    map.put(strDecode, new ArrayList());
                }
                ((List) map.get(strDecode)).add(strDecode2);
            }
        }
        for (Map.Entry entry : map.entrySet()) {
            el.a(3, a, "entry: " + ((String) entry.getKey()) + "=" + entry.getValue());
        }
        StringBuilder sb = new StringBuilder();
        if (map.get("utm_source") == null) {
            sb.append("Campaign Source is missing.\n");
        }
        if (map.get("utm_medium") == null) {
            sb.append("Campaign Medium is missing.\n");
        }
        if (map.get("utm_campaign") == null) {
            sb.append("Campaign Name is missing.\n");
        }
        if (sb.length() > 0) {
            el.a(5, a, "Detected missing referrer keys : " + sb.toString());
        }
        return map;
    }
}
