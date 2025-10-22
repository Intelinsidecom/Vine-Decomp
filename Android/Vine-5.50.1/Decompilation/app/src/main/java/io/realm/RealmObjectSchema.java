package io.realm;

import io.realm.internal.ImplicitTransaction;
import io.realm.internal.Table;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/* loaded from: classes.dex */
public final class RealmObjectSchema {
    private static final Map<Class<?>, FieldMetaData> SUPPORTED_LINKED_FIELDS;
    private static final Map<Class<?>, FieldMetaData> SUPPORTED_SIMPLE_FIELDS = new HashMap();
    private final Map<String, Long> columnIndices;
    private final BaseRealm realm;
    final Table table;
    private final ImplicitTransaction transaction;

    static {
        SUPPORTED_SIMPLE_FIELDS.put(String.class, new FieldMetaData(RealmFieldType.STRING, true));
        SUPPORTED_SIMPLE_FIELDS.put(Short.TYPE, new FieldMetaData(RealmFieldType.INTEGER, false));
        SUPPORTED_SIMPLE_FIELDS.put(Short.class, new FieldMetaData(RealmFieldType.INTEGER, true));
        SUPPORTED_SIMPLE_FIELDS.put(Integer.TYPE, new FieldMetaData(RealmFieldType.INTEGER, false));
        SUPPORTED_SIMPLE_FIELDS.put(Integer.class, new FieldMetaData(RealmFieldType.INTEGER, true));
        SUPPORTED_SIMPLE_FIELDS.put(Long.TYPE, new FieldMetaData(RealmFieldType.INTEGER, false));
        SUPPORTED_SIMPLE_FIELDS.put(Long.class, new FieldMetaData(RealmFieldType.INTEGER, true));
        SUPPORTED_SIMPLE_FIELDS.put(Float.TYPE, new FieldMetaData(RealmFieldType.FLOAT, false));
        SUPPORTED_SIMPLE_FIELDS.put(Float.class, new FieldMetaData(RealmFieldType.FLOAT, true));
        SUPPORTED_SIMPLE_FIELDS.put(Double.TYPE, new FieldMetaData(RealmFieldType.DOUBLE, false));
        SUPPORTED_SIMPLE_FIELDS.put(Double.class, new FieldMetaData(RealmFieldType.DOUBLE, true));
        SUPPORTED_SIMPLE_FIELDS.put(Boolean.TYPE, new FieldMetaData(RealmFieldType.BOOLEAN, false));
        SUPPORTED_SIMPLE_FIELDS.put(Boolean.class, new FieldMetaData(RealmFieldType.BOOLEAN, true));
        SUPPORTED_SIMPLE_FIELDS.put(Byte.TYPE, new FieldMetaData(RealmFieldType.INTEGER, false));
        SUPPORTED_SIMPLE_FIELDS.put(Byte.class, new FieldMetaData(RealmFieldType.INTEGER, true));
        SUPPORTED_SIMPLE_FIELDS.put(byte[].class, new FieldMetaData(RealmFieldType.BINARY, true));
        SUPPORTED_SIMPLE_FIELDS.put(Date.class, new FieldMetaData(RealmFieldType.DATE, true));
        SUPPORTED_LINKED_FIELDS = new HashMap();
        SUPPORTED_LINKED_FIELDS.put(RealmObject.class, new FieldMetaData(RealmFieldType.OBJECT, false));
        SUPPORTED_LINKED_FIELDS.put(RealmList.class, new FieldMetaData(RealmFieldType.LIST, false));
    }

    RealmObjectSchema(BaseRealm realm, Table table, Map<String, Long> columnIndices) {
        this.realm = realm;
        this.transaction = realm.sharedGroupManager.getTransaction();
        this.table = table;
        this.columnIndices = columnIndices;
    }

    public String getClassName() {
        return this.table.getName().substring(Table.TABLE_PREFIX.length());
    }

    public RealmObjectSchema addField(String fieldName, Class<?> fieldType, FieldAttribute... attributes) throws Exception {
        FieldMetaData metadata = SUPPORTED_SIMPLE_FIELDS.get(fieldType);
        if (metadata == null) {
            if (SUPPORTED_LINKED_FIELDS.containsKey(fieldType)) {
                throw new IllegalArgumentException("Use addLinkField() instead to add fields that link to other RealmObjects: " + fieldName);
            }
            throw new IllegalArgumentException(String.format("Realm doesn't support this field type: %s(%s)", fieldName, fieldType));
        }
        checkNewFieldName(fieldName);
        boolean nullable = metadata.defaultNullable;
        if (containsAttribute(attributes, FieldAttribute.REQUIRED) || containsAttribute(attributes, FieldAttribute.PRIMARY_KEY)) {
            nullable = false;
        }
        long columnIndex = this.table.addColumn(metadata.realmType, fieldName, nullable);
        try {
            addModifiers(fieldName, attributes);
            return this;
        } catch (Exception e) {
            this.table.removeColumn(columnIndex);
            throw e;
        }
    }

    public RealmObjectSchema addIndex(String fieldName) {
        checkLegalName(fieldName);
        checkFieldExists(fieldName);
        long columnIndex = getColumnIndex(fieldName);
        if (this.table.hasSearchIndex(columnIndex)) {
            throw new IllegalStateException(fieldName + " already has an index.");
        }
        this.table.addSearchIndex(columnIndex);
        return this;
    }

    public RealmObjectSchema addPrimaryKey(String fieldName) {
        checkLegalName(fieldName);
        checkFieldExists(fieldName);
        if (this.table.hasPrimaryKey()) {
            throw new IllegalStateException("A primary key is already defined");
        }
        this.table.setPrimaryKey(fieldName);
        return this;
    }

    private void addModifiers(String fieldName, FieldAttribute[] attributes) throws Exception {
        boolean indexAdded = false;
        if (attributes != null) {
            try {
                if (attributes.length > 0) {
                    if (containsAttribute(attributes, FieldAttribute.INDEXED)) {
                        addIndex(fieldName);
                    }
                    if (containsAttribute(attributes, FieldAttribute.PRIMARY_KEY)) {
                        addIndex(fieldName);
                        indexAdded = true;
                        addPrimaryKey(fieldName);
                    }
                }
            } catch (Exception e) {
                long columnIndex = getColumnIndex(fieldName);
                if (indexAdded) {
                    this.table.removeSearchIndex(columnIndex);
                }
                throw e;
            }
        }
    }

    private boolean containsAttribute(FieldAttribute[] attributeList, FieldAttribute attribute) {
        if (attributeList == null || attributeList.length == 0) {
            return false;
        }
        for (FieldAttribute fieldAttribute : attributeList) {
            if (fieldAttribute == attribute) {
                return true;
            }
        }
        return false;
    }

    private void checkNewFieldName(String fieldName) {
        checkLegalName(fieldName);
        checkFieldNameIsAvailable(fieldName);
    }

    private void checkLegalName(String fieldName) {
        if (fieldName == null || fieldName.isEmpty()) {
            throw new IllegalArgumentException("Field name can not be null or empty");
        }
        if (fieldName.contains(".")) {
            throw new IllegalArgumentException("Field name can not contain '.'");
        }
    }

    private void checkFieldNameIsAvailable(String fieldName) {
        if (this.table.getColumnIndex(fieldName) != -1) {
            throw new IllegalArgumentException("Field already exists in '" + getClassName() + "': " + fieldName);
        }
    }

    private void checkFieldExists(String fieldName) {
        if (this.table.getColumnIndex(fieldName) == -1) {
            throw new IllegalArgumentException("Field name doesn't exist on object '" + getClassName() + "': " + fieldName);
        }
    }

    private long getColumnIndex(String fieldName) {
        long columnIndex = this.table.getColumnIndex(fieldName);
        if (columnIndex == -1) {
            throw new IllegalArgumentException(String.format("Field name '%s' does not exist on schema for '%s", fieldName, getClassName()));
        }
        return columnIndex;
    }

    long[] getColumnIndices(String fieldDescription, RealmFieldType... validColumnTypes) {
        if (fieldDescription == null || fieldDescription.equals("")) {
            throw new IllegalArgumentException("Non-empty fieldname must be provided");
        }
        if (fieldDescription.startsWith(".") || fieldDescription.endsWith(".")) {
            throw new IllegalArgumentException("Illegal field name. It cannot start or end with a '.': " + fieldDescription);
        }
        Table table = this.table;
        boolean checkColumnType = validColumnTypes != null && validColumnTypes.length > 0;
        if (fieldDescription.contains(".")) {
            String[] names = fieldDescription.split("\\.");
            long[] columnIndices = new long[names.length];
            for (int i = 0; i < names.length - 1; i++) {
                long index = table.getColumnIndex(names[i]);
                if (index < 0) {
                    throw new IllegalArgumentException("Invalid query: " + names[i] + " does not refer to a class.");
                }
                RealmFieldType type = table.getColumnType(index);
                if (type == RealmFieldType.OBJECT || type == RealmFieldType.LIST) {
                    table = table.getLinkTarget(index);
                    columnIndices[i] = index;
                } else {
                    throw new IllegalArgumentException("Invalid query: " + names[i] + " does not refer to a class.");
                }
            }
            String columnName = names[names.length - 1];
            long columnIndex = table.getColumnIndex(columnName);
            columnIndices[names.length - 1] = columnIndex;
            if (columnIndex < 0) {
                throw new IllegalArgumentException(columnName + " is not a field name in class " + table.getName());
            }
            if (!checkColumnType || isValidType(table.getColumnType(columnIndex), validColumnTypes)) {
                return columnIndices;
            }
            throw new IllegalArgumentException(String.format("Field '%s': type mismatch.", names[names.length - 1]));
        }
        if (getFieldIndex(fieldDescription) == null) {
            throw new IllegalArgumentException(String.format("Field '%s' does not exist.", fieldDescription));
        }
        RealmFieldType tableColumnType = table.getColumnType(getFieldIndex(fieldDescription).longValue());
        if (checkColumnType && !isValidType(tableColumnType, validColumnTypes)) {
            throw new IllegalArgumentException(String.format("Field '%s': type mismatch. Was %s, expected %s.", fieldDescription, tableColumnType, Arrays.toString(validColumnTypes)));
        }
        return new long[]{getFieldIndex(fieldDescription).longValue()};
    }

    private boolean isValidType(RealmFieldType columnType, RealmFieldType[] validColumnTypes) {
        for (RealmFieldType realmFieldType : validColumnTypes) {
            if (realmFieldType == columnType) {
                return true;
            }
        }
        return false;
    }

    Long getFieldIndex(String fieldName) {
        return this.columnIndices.get(fieldName);
    }

    private static class FieldMetaData {
        public final boolean defaultNullable;
        public final RealmFieldType realmType;

        public FieldMetaData(RealmFieldType realmType, boolean defaultNullable) {
            this.realmType = realmType;
            this.defaultNullable = defaultNullable;
        }
    }

    static class DynamicColumnMap implements Map<String, Long> {
        private final Table table;

        public DynamicColumnMap(Table table) {
            this.table = table;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Map
        public Long get(Object key) {
            return Long.valueOf(this.table.getColumnIndex((String) key));
        }

        @Override // java.util.Map
        public void clear() {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Map
        public boolean containsKey(Object key) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Map
        public boolean containsValue(Object value) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Map
        public Set<Map.Entry<String, Long>> entrySet() {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Map
        public boolean isEmpty() {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Map
        public Set<String> keySet() {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Map
        public Long put(String key, Long value) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Map
        public void putAll(Map<? extends String, ? extends Long> map) {
            throw new UnsupportedOperationException();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Map
        public Long remove(Object key) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Map
        public int size() {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Map
        public Collection<Long> values() {
            throw new UnsupportedOperationException();
        }
    }
}
