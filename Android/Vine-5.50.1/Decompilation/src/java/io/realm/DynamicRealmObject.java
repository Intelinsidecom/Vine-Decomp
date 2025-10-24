package io.realm;

import io.realm.internal.Table;
import java.util.Arrays;

/* loaded from: classes.dex */
public final class DynamicRealmObject extends RealmObject {
    private String className;

    DynamicRealmObject() {
    }

    public String[] getFieldNames() {
        String[] keys = new String[(int) this.row.getColumnCount()];
        for (int i = 0; i < keys.length; i++) {
            keys[i] = this.row.getColumnName(i);
        }
        return keys;
    }

    public int hashCode() {
        String realmName = this.realm.getPath();
        String tableName = this.row.getTable().getName();
        long rowIndex = this.row.getIndex();
        int result = (realmName != null ? realmName.hashCode() : 0) + 527;
        return (((result * 31) + (tableName != null ? tableName.hashCode() : 0)) * 31) + ((int) ((rowIndex >>> 32) ^ rowIndex));
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DynamicRealmObject other = (DynamicRealmObject) o;
        String path = this.realm.getPath();
        String otherPath = other.realm.getPath();
        if (path != null) {
            if (!path.equals(otherPath)) {
                return false;
            }
        } else if (otherPath != null) {
            return false;
        }
        String tableName = this.row.getTable().getName();
        String otherTableName = other.row.getTable().getName();
        if (tableName != null) {
            if (!tableName.equals(otherTableName)) {
                return false;
            }
        } else if (otherTableName != null) {
            return false;
        }
        return this.row.getIndex() == other.row.getIndex();
    }

    public String toString() {
        if (this.row == null || !this.row.isAttached()) {
            return "Invalid object";
        }
        StringBuilder sb = new StringBuilder(this.row.getTable().getName() + " = [");
        String[] fields = getFieldNames();
        for (String field : fields) {
            long columnIndex = this.row.getColumnIndex(field);
            RealmFieldType type = this.row.getColumnType(columnIndex);
            sb.append("{");
            switch (type) {
                case BOOLEAN:
                    sb.append(field).append(": ").append(this.row.getBoolean(columnIndex));
                    break;
                case INTEGER:
                    sb.append(field).append(": ").append(this.row.getLong(columnIndex));
                    break;
                case FLOAT:
                    sb.append(field).append(": ").append(this.row.getFloat(columnIndex));
                    break;
                case DOUBLE:
                    sb.append(field).append(": ").append(this.row.getDouble(columnIndex));
                    break;
                case STRING:
                    sb.append(field).append(": ").append(this.row.getString(columnIndex));
                    break;
                case BINARY:
                    sb.append(field).append(": ").append(Arrays.toString(this.row.getBinaryByteArray(columnIndex)));
                    break;
                case DATE:
                    sb.append(field).append(": ").append(this.row.getDate(columnIndex));
                    break;
                case OBJECT:
                    if (this.row.isNullLink(columnIndex)) {
                        sb.append("null");
                        break;
                    } else {
                        sb.append(field).append(": ").append(this.row.getTable().getLinkTarget(columnIndex).getName());
                        break;
                    }
                case LIST:
                    String targetType = this.row.getTable().getLinkTarget(columnIndex).getName();
                    sb.append(String.format("%s: RealmList<%s>[%s]", field, targetType, Long.valueOf(this.row.getLinkList(columnIndex).size())));
                    break;
                default:
                    sb.append(field).append(": ?");
                    break;
            }
            sb.append("}, ");
        }
        sb.replace(sb.length() - 2, sb.length(), "");
        sb.append("]");
        return sb.toString();
    }

    @Override // io.realm.RealmObject
    protected Table getTable() {
        return this.className != null ? this.realm.schema.getTable(this.className) : super.getTable();
    }
}
