package io.realm;

import android.content.Context;
import io.realm.exceptions.RealmException;
import io.realm.internal.RealmProxyMediator;
import io.realm.internal.SharedGroup;
import io.realm.internal.modules.CompositeMediator;
import io.realm.internal.modules.FilterableMediator;
import io.realm.rx.RealmObservableFactory;
import io.realm.rx.RxObservableFactory;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/* loaded from: classes.dex */
public class RealmConfiguration {
    private static final Object DEFAULT_MODULE = Realm.getDefaultModule();
    private static final RealmProxyMediator DEFAULT_MODULE_MEDIATOR;
    private final String canonicalPath;
    private final boolean deleteRealmIfMigrationNeeded;
    private final SharedGroup.Durability durability;
    private final byte[] key;
    private final RealmMigration migration;
    private final String realmFileName;
    private final File realmFolder;
    private final RxObservableFactory rxObservableFactory;
    private final RealmProxyMediator schemaMediator;
    private final long schemaVersion;

    static {
        if (DEFAULT_MODULE != null) {
            DEFAULT_MODULE_MEDIATOR = getModuleMediator(DEFAULT_MODULE.getClass().getCanonicalName());
        } else {
            DEFAULT_MODULE_MEDIATOR = null;
        }
    }

    private RealmConfiguration(Builder builder) {
        this.realmFolder = builder.folder;
        this.realmFileName = builder.fileName;
        this.canonicalPath = Realm.getCanonicalPath(new File(this.realmFolder, this.realmFileName));
        this.key = builder.key;
        this.schemaVersion = builder.schemaVersion;
        this.deleteRealmIfMigrationNeeded = builder.deleteRealmIfMigrationNeeded;
        this.migration = builder.migration;
        this.durability = builder.durability;
        this.schemaMediator = createSchemaMediator(builder);
        this.rxObservableFactory = builder.rxFactory;
    }

    public File getRealmFolder() {
        return this.realmFolder;
    }

    public String getRealmFileName() {
        return this.realmFileName;
    }

    public byte[] getEncryptionKey() {
        if (this.key == null) {
            return null;
        }
        return Arrays.copyOf(this.key, this.key.length);
    }

    public long getSchemaVersion() {
        return this.schemaVersion;
    }

    public RealmMigration getMigration() {
        return this.migration;
    }

    public boolean shouldDeleteRealmIfMigrationNeeded() {
        return this.deleteRealmIfMigrationNeeded;
    }

    public SharedGroup.Durability getDurability() {
        return this.durability;
    }

    @Deprecated
    public RealmProxyMediator getSchemaMediator() {
        return this.schemaMediator;
    }

    public String getPath() {
        return this.canonicalPath;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        RealmConfiguration that = (RealmConfiguration) obj;
        if (this.schemaVersion != that.schemaVersion || this.deleteRealmIfMigrationNeeded != that.deleteRealmIfMigrationNeeded || !this.realmFolder.equals(that.realmFolder) || !this.realmFileName.equals(that.realmFileName) || !this.canonicalPath.equals(that.canonicalPath) || !Arrays.equals(this.key, that.key) || !this.durability.equals(that.durability)) {
            return false;
        }
        if (this.migration != null) {
            if (!this.migration.equals(that.migration)) {
                return false;
            }
        } else if (that.migration != null) {
            return false;
        }
        if (this.rxObservableFactory.equals(that.rxObservableFactory)) {
            return this.schemaMediator.equals(that.schemaMediator);
        }
        return false;
    }

    public int hashCode() {
        int result = this.realmFolder.hashCode();
        return (((((((((((((((result * 31) + this.realmFileName.hashCode()) * 31) + this.canonicalPath.hashCode()) * 31) + (this.key != null ? Arrays.hashCode(this.key) : 0)) * 31) + ((int) this.schemaVersion)) * 31) + (this.migration != null ? this.migration.hashCode() : 0)) * 31) + (this.deleteRealmIfMigrationNeeded ? 1 : 0)) * 31) + this.schemaMediator.hashCode()) * 31) + this.durability.hashCode();
    }

    private RealmProxyMediator createSchemaMediator(Builder builder) {
        Set<Object> modules = builder.modules;
        Set<Class<? extends RealmObject>> debugSchema = builder.debugSchema;
        if (debugSchema.size() > 0) {
            return new FilterableMediator(DEFAULT_MODULE_MEDIATOR, debugSchema);
        }
        if (modules.size() == 1) {
            return getModuleMediator(modules.iterator().next().getClass().getCanonicalName());
        }
        RealmProxyMediator[] mediators = new RealmProxyMediator[modules.size()];
        int i = 0;
        for (Object module : modules) {
            mediators[i] = getModuleMediator(module.getClass().getCanonicalName());
            i++;
        }
        return new CompositeMediator(mediators);
    }

    private static RealmProxyMediator getModuleMediator(String fullyQualifiedModuleClassName) throws ClassNotFoundException {
        String[] moduleNameParts = fullyQualifiedModuleClassName.split("\\.");
        String moduleSimpleName = moduleNameParts[moduleNameParts.length - 1];
        String mediatorName = String.format("io.realm.%s%s", moduleSimpleName, "Mediator");
        try {
            Class<?> clazz = Class.forName(mediatorName);
            Constructor<?> constructor = clazz.getDeclaredConstructors()[0];
            constructor.setAccessible(true);
            return (RealmProxyMediator) constructor.newInstance(new Object[0]);
        } catch (ClassNotFoundException e) {
            throw new RealmException("Could not find " + mediatorName, e);
        } catch (IllegalAccessException e2) {
            throw new RealmException("Could not create an instance of " + mediatorName, e2);
        } catch (InstantiationException e3) {
            throw new RealmException("Could not create an instance of " + mediatorName, e3);
        } catch (InvocationTargetException e4) {
            throw new RealmException("Could not create an instance of " + mediatorName, e4);
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("");
        stringBuilder.append("realmFolder: ");
        stringBuilder.append(this.realmFolder.toString());
        stringBuilder.append("\n");
        stringBuilder.append("realmFileName : ");
        stringBuilder.append(this.realmFileName);
        stringBuilder.append("\n");
        stringBuilder.append("canonicalPath: ");
        stringBuilder.append(this.canonicalPath);
        stringBuilder.append("\n");
        stringBuilder.append("key: ");
        stringBuilder.append("[length: " + Integer.toString(this.key == null ? 0 : 64) + "]");
        stringBuilder.append("\n");
        stringBuilder.append("schemaVersion: ");
        stringBuilder.append(Long.toString(this.schemaVersion));
        stringBuilder.append("\n");
        stringBuilder.append("migration: ");
        stringBuilder.append(this.migration);
        stringBuilder.append("\n");
        stringBuilder.append("deleteRealmIfMigrationNeeded: ");
        stringBuilder.append(this.deleteRealmIfMigrationNeeded);
        stringBuilder.append("\n");
        stringBuilder.append("durability: ");
        stringBuilder.append(this.durability);
        stringBuilder.append("\n");
        stringBuilder.append("schemaMediator: ");
        stringBuilder.append(this.schemaMediator);
        return stringBuilder.toString();
    }

    public static class Builder {
        private boolean deleteRealmIfMigrationNeeded;
        private SharedGroup.Durability durability;
        private String fileName;
        private File folder;
        private byte[] key;
        private RealmMigration migration;
        private long schemaVersion;
        private HashSet<Object> modules = new HashSet<>();
        private HashSet<Class<? extends RealmObject>> debugSchema = new HashSet<>();
        private RxObservableFactory rxFactory = new RealmObservableFactory();

        public Builder(Context context) {
            if (context == null) {
                throw new IllegalArgumentException("A non-null Context must be provided");
            }
            initializeBuilder(context.getFilesDir());
        }

        private void initializeBuilder(File folder) {
            if (folder == null || !folder.isDirectory()) {
                throw new IllegalArgumentException("An existing folder must be provided. Yours was " + (folder != null ? folder.getAbsolutePath() : "null"));
            }
            if (!folder.canWrite()) {
                throw new IllegalArgumentException("Folder is not writable: " + folder.getAbsolutePath());
            }
            this.folder = folder;
            this.fileName = "default.realm";
            this.key = null;
            this.schemaVersion = 0L;
            this.migration = null;
            this.deleteRealmIfMigrationNeeded = false;
            this.durability = SharedGroup.Durability.FULL;
            if (RealmConfiguration.DEFAULT_MODULE != null) {
                this.modules.add(RealmConfiguration.DEFAULT_MODULE);
            }
        }

        public Builder schemaVersion(long schemaVersion) {
            if (schemaVersion < 0) {
                throw new IllegalArgumentException("Realm schema version numbers must be 0 (zero) or higher. Yours was: " + schemaVersion);
            }
            this.schemaVersion = schemaVersion;
            return this;
        }

        public Builder migration(RealmMigration migration) {
            if (migration == null) {
                throw new IllegalArgumentException("A non-null migration must be provided");
            }
            this.migration = migration;
            return this;
        }

        public RealmConfiguration build() {
            return new RealmConfiguration(this);
        }
    }
}
