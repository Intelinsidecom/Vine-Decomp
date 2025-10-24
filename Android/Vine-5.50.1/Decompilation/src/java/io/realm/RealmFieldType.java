package io.realm;

import io.realm.internal.Keep;
import io.realm.internal.Mixed;
import java.nio.ByteBuffer;
import java.util.Date;

@Keep
/* loaded from: classes.dex */
public enum RealmFieldType {
    INTEGER(0),
    BOOLEAN(1),
    STRING(2),
    BINARY(4),
    UNSUPPORTED_TABLE(5),
    UNSUPPORTED_MIXED(6),
    DATE(7),
    FLOAT(9),
    DOUBLE(10),
    OBJECT(12),
    LIST(13);

    private static RealmFieldType[] typeList = new RealmFieldType[15];
    private final int nativeValue;

    static {
        RealmFieldType[] columnTypes = values();
        for (int i = 0; i < columnTypes.length; i++) {
            int v = columnTypes[i].nativeValue;
            typeList[v] = columnTypes[i];
        }
    }

    RealmFieldType(int nativeValue) {
        this.nativeValue = nativeValue;
    }

    public int getNativeValue() {
        return this.nativeValue;
    }

    public boolean isValid(Object obj) {
        switch (this.nativeValue) {
            case 0:
                return (obj instanceof Long) || (obj instanceof Integer) || (obj instanceof Short) || (obj instanceof Byte);
            case 1:
                return obj instanceof Boolean;
            case 2:
                return obj instanceof String;
            case 3:
            case 8:
            case 11:
            default:
                throw new RuntimeException("Unsupported Realm type:  " + this);
            case 4:
                return (obj instanceof byte[]) || (obj instanceof ByteBuffer);
            case 5:
                return obj == null || (obj instanceof Object[][]);
            case 6:
                return (obj instanceof Mixed) || (obj instanceof Long) || (obj instanceof Integer) || (obj instanceof Short) || (obj instanceof Byte) || (obj instanceof Boolean) || (obj instanceof Float) || (obj instanceof Double) || (obj instanceof String) || (obj instanceof byte[]) || (obj instanceof ByteBuffer) || obj == null || (obj instanceof Object[][]) || (obj instanceof Date);
            case 7:
                return obj instanceof Date;
            case 9:
                return obj instanceof Float;
            case 10:
                return obj instanceof Double;
            case 12:
            case 13:
            case 14:
                return false;
        }
    }

    public static RealmFieldType fromNativeValue(int value) {
        RealmFieldType e;
        if (value >= 0 && value < typeList.length && (e = typeList[value]) != null) {
            return e;
        }
        throw new IllegalArgumentException("Invalid native Realm type: " + value);
    }
}
