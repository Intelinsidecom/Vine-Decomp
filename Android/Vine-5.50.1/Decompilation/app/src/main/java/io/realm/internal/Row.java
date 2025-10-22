package io.realm.internal;

import io.realm.RealmFieldType;
import java.util.Date;

/* loaded from: classes.dex */
public interface Row {
    public static final Row EMPTY_ROW = new Row() { // from class: io.realm.internal.Row.1
        @Override // io.realm.internal.Row
        public long getColumnCount() {
            throw new IllegalStateException("Can't access a row that hasn't been loaded, make sure the instance is loaded by calling RealmObject.isLoaded");
        }

        @Override // io.realm.internal.Row
        public String getColumnName(long columnIndex) {
            throw new IllegalStateException("Can't access a row that hasn't been loaded, make sure the instance is loaded by calling RealmObject.isLoaded");
        }

        @Override // io.realm.internal.Row
        public long getColumnIndex(String columnName) {
            throw new IllegalStateException("Can't access a row that hasn't been loaded, make sure the instance is loaded by calling RealmObject.isLoaded");
        }

        @Override // io.realm.internal.Row
        public RealmFieldType getColumnType(long columnIndex) {
            throw new IllegalStateException("Can't access a row that hasn't been loaded, make sure the instance is loaded by calling RealmObject.isLoaded");
        }

        @Override // io.realm.internal.Row
        public Table getTable() {
            return null;
        }

        @Override // io.realm.internal.Row
        public long getIndex() {
            throw new IllegalStateException("Can't access a row that hasn't been loaded, make sure the instance is loaded by calling RealmObject.isLoaded");
        }

        @Override // io.realm.internal.Row
        public long getLong(long columnIndex) {
            throw new IllegalStateException("Can't access a row that hasn't been loaded, make sure the instance is loaded by calling RealmObject.isLoaded");
        }

        @Override // io.realm.internal.Row
        public boolean getBoolean(long columnIndex) {
            throw new IllegalStateException("Can't access a row that hasn't been loaded, make sure the instance is loaded by calling RealmObject.isLoaded");
        }

        @Override // io.realm.internal.Row
        public float getFloat(long columnIndex) {
            throw new IllegalStateException("Can't access a row that hasn't been loaded, make sure the instance is loaded by calling RealmObject.isLoaded");
        }

        @Override // io.realm.internal.Row
        public double getDouble(long columnIndex) {
            throw new IllegalStateException("Can't access a row that hasn't been loaded, make sure the instance is loaded by calling RealmObject.isLoaded");
        }

        @Override // io.realm.internal.Row
        public Date getDate(long columnIndex) {
            throw new IllegalStateException("Can't access a row that hasn't been loaded, make sure the instance is loaded by calling RealmObject.isLoaded");
        }

        @Override // io.realm.internal.Row
        public String getString(long columnIndex) {
            throw new IllegalStateException("Can't access a row that hasn't been loaded, make sure the instance is loaded by calling RealmObject.isLoaded");
        }

        @Override // io.realm.internal.Row
        public byte[] getBinaryByteArray(long columnIndex) {
            throw new IllegalStateException("Can't access a row that hasn't been loaded, make sure the instance is loaded by calling RealmObject.isLoaded");
        }

        @Override // io.realm.internal.Row
        public boolean isNullLink(long columnIndex) {
            throw new IllegalStateException("Can't access a row that hasn't been loaded, make sure the instance is loaded by calling RealmObject.isLoaded");
        }

        @Override // io.realm.internal.Row
        public void setNull(long columnIndex) {
            throw new IllegalStateException("Can't access a row that hasn't been loaded, make sure the instance is loaded by calling RealmObject.isLoaded");
        }

        @Override // io.realm.internal.Row
        public LinkView getLinkList(long columnIndex) {
            throw new IllegalStateException("Can't access a row that hasn't been loaded, make sure the instance is loaded by calling RealmObject.isLoaded");
        }

        @Override // io.realm.internal.Row
        public void setLong(long columnIndex, long value) {
            throw new IllegalStateException("Can't access a row that hasn't been loaded, make sure the instance is loaded by calling RealmObject.isLoaded");
        }

        @Override // io.realm.internal.Row
        public void setBoolean(long columnIndex, boolean value) {
            throw new IllegalStateException("Can't access a row that hasn't been loaded, make sure the instance is loaded by calling RealmObject.isLoaded");
        }

        @Override // io.realm.internal.Row
        public void setString(long columnIndex, String value) {
            throw new IllegalStateException("Can't access a row that hasn't been loaded, make sure the instance is loaded by calling RealmObject.isLoaded");
        }

        @Override // io.realm.internal.Row
        public boolean isAttached() {
            return false;
        }
    };

    byte[] getBinaryByteArray(long j);

    boolean getBoolean(long j);

    long getColumnCount();

    long getColumnIndex(String str);

    String getColumnName(long j);

    RealmFieldType getColumnType(long j);

    Date getDate(long j);

    double getDouble(long j);

    float getFloat(long j);

    long getIndex();

    LinkView getLinkList(long j);

    long getLong(long j);

    String getString(long j);

    Table getTable();

    boolean isAttached();

    boolean isNullLink(long j);

    void setBoolean(long j, boolean z);

    void setLong(long j, long j2);

    void setNull(long j);

    void setString(long j, String str);
}
