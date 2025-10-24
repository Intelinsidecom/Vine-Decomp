package io.realm.internal;

import io.realm.RealmFieldType;
import java.util.ArrayList;
import java.util.List;

@Keep
/* loaded from: classes.dex */
public class TableSpec {
    private List<ColumnInfo> columnInfos = new ArrayList();

    public static class ColumnInfo {
        protected final String name;
        protected final TableSpec tableSpec;
        protected final RealmFieldType type;

        public ColumnInfo(RealmFieldType type, String name) {
            this.name = name;
            this.type = type;
            this.tableSpec = type == RealmFieldType.UNSUPPORTED_TABLE ? new TableSpec() : null;
        }

        public int hashCode() {
            int result = (this.name == null ? 0 : this.name.hashCode()) + 31;
            return (((result * 31) + (this.tableSpec == null ? 0 : this.tableSpec.hashCode())) * 31) + (this.type != null ? this.type.hashCode() : 0);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj != null && getClass() == obj.getClass()) {
                ColumnInfo other = (ColumnInfo) obj;
                if (this.name == null) {
                    if (other.name != null) {
                        return false;
                    }
                } else if (!this.name.equals(other.name)) {
                    return false;
                }
                if (this.tableSpec == null) {
                    if (other.tableSpec != null) {
                        return false;
                    }
                } else if (!this.tableSpec.equals(other.tableSpec)) {
                    return false;
                }
                return this.type == other.type;
            }
            return false;
        }
    }

    public void addColumn(RealmFieldType type, String name) {
        if (name.length() > 63) {
            throw new IllegalArgumentException("Column names are currently limited to max 63 characters.");
        }
        this.columnInfos.add(new ColumnInfo(type, name));
    }

    protected void addColumn(int colTypeIndex, String name) {
        addColumn(RealmFieldType.fromNativeValue(colTypeIndex), name);
    }

    public TableSpec addSubtableColumn(String name) {
        if (name.length() > 63) {
            throw new IllegalArgumentException("Column names are currently limited to max 63 characters.");
        }
        ColumnInfo columnInfo = new ColumnInfo(RealmFieldType.UNSUPPORTED_TABLE, name);
        this.columnInfos.add(columnInfo);
        return columnInfo.tableSpec;
    }

    public TableSpec getSubtableSpec(long columnIndex) {
        return this.columnInfos.get((int) columnIndex).tableSpec;
    }

    public long getColumnCount() {
        return this.columnInfos.size();
    }

    public RealmFieldType getColumnType(long columnIndex) {
        return this.columnInfos.get((int) columnIndex).type;
    }

    public String getColumnName(long columnIndex) {
        return this.columnInfos.get((int) columnIndex).name;
    }

    public long getColumnIndex(String name) {
        for (int i = 0; i < this.columnInfos.size(); i++) {
            ColumnInfo columnInfo = this.columnInfos.get(i);
            if (columnInfo.name.equals(name)) {
                return i;
            }
        }
        return -1L;
    }

    public int hashCode() {
        int result = (this.columnInfos == null ? 0 : this.columnInfos.hashCode()) + 31;
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && getClass() == obj.getClass()) {
            TableSpec other = (TableSpec) obj;
            return this.columnInfos == null ? other.columnInfos == null : this.columnInfos.equals(other.columnInfos);
        }
        return false;
    }
}
