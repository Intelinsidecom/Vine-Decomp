package org.aspectj.runtime.reflect;

import java.lang.ref.SoftReference;
import java.util.StringTokenizer;
import org.aspectj.lang.Signature;

/* loaded from: classes2.dex */
abstract class SignatureImpl implements Signature {
    Class declaringType;
    String declaringTypeName;
    ClassLoader lookupClassLoader = null;
    int modifiers;
    String name;
    Cache stringCache;
    private String stringRep;
    private static boolean useCache = true;
    static String[] EMPTY_STRING_ARRAY = new String[0];
    static Class[] EMPTY_CLASS_ARRAY = new Class[0];

    private interface Cache {
        String get(int i);

        void set(int i, String str);
    }

    protected abstract String createToString(StringMaker stringMaker);

    SignatureImpl(int modifiers, String name, Class declaringType) {
        this.modifiers = -1;
        this.modifiers = modifiers;
        this.name = name;
        this.declaringType = declaringType;
    }

    String toString(StringMaker sm) {
        String result = null;
        if (useCache) {
            if (this.stringCache == null) {
                try {
                    this.stringCache = new CacheImpl();
                } catch (Throwable th) {
                    useCache = false;
                }
            } else {
                result = this.stringCache.get(sm.cacheOffset);
            }
        }
        if (result == null) {
            result = createToString(sm);
        }
        if (useCache) {
            this.stringCache.set(sm.cacheOffset, result);
        }
        return result;
    }

    public final String toString() {
        return toString(StringMaker.middleStringMaker);
    }

    public int getModifiers() {
        if (this.modifiers == -1) {
            this.modifiers = extractInt(0);
        }
        return this.modifiers;
    }

    public String getName() {
        if (this.name == null) {
            this.name = extractString(1);
        }
        return this.name;
    }

    public Class getDeclaringType() {
        if (this.declaringType == null) {
            this.declaringType = extractType(2);
        }
        return this.declaringType;
    }

    public String getDeclaringTypeName() {
        if (this.declaringTypeName == null) {
            this.declaringTypeName = getDeclaringType().getName();
        }
        return this.declaringTypeName;
    }

    private ClassLoader getLookupClassLoader() {
        if (this.lookupClassLoader == null) {
            this.lookupClassLoader = getClass().getClassLoader();
        }
        return this.lookupClassLoader;
    }

    String extractString(int n) {
        int startIndex = 0;
        int endIndex = this.stringRep.indexOf(45);
        while (true) {
            int n2 = n;
            n = n2 - 1;
            if (n2 <= 0) {
                break;
            }
            startIndex = endIndex + 1;
            endIndex = this.stringRep.indexOf(45, startIndex);
        }
        if (endIndex == -1) {
            endIndex = this.stringRep.length();
        }
        return this.stringRep.substring(startIndex, endIndex);
    }

    int extractInt(int n) {
        String s = extractString(n);
        return Integer.parseInt(s, 16);
    }

    Class extractType(int n) {
        String s = extractString(n);
        return Factory.makeClass(s, getLookupClassLoader());
    }

    Class[] extractTypes(int n) {
        String s = extractString(n);
        StringTokenizer st = new StringTokenizer(s, ":");
        int N = st.countTokens();
        Class[] ret = new Class[N];
        for (int i = 0; i < N; i++) {
            ret[i] = Factory.makeClass(st.nextToken(), getLookupClassLoader());
        }
        return ret;
    }

    private static final class CacheImpl implements Cache {
        private SoftReference toStringCacheRef;

        public CacheImpl() {
            makeCache();
        }

        @Override // org.aspectj.runtime.reflect.SignatureImpl.Cache
        public String get(int cacheOffset) {
            String[] cachedArray = array();
            if (cachedArray == null) {
                return null;
            }
            return cachedArray[cacheOffset];
        }

        @Override // org.aspectj.runtime.reflect.SignatureImpl.Cache
        public void set(int cacheOffset, String result) {
            String[] cachedArray = array();
            if (cachedArray == null) {
                cachedArray = makeCache();
            }
            cachedArray[cacheOffset] = result;
        }

        private String[] array() {
            return (String[]) this.toStringCacheRef.get();
        }

        private String[] makeCache() {
            String[] array = new String[3];
            this.toStringCacheRef = new SoftReference(array);
            return array;
        }
    }
}
