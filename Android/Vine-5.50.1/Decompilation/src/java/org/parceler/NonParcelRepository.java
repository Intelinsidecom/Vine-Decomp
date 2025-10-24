package org.parceler;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import org.parceler.Parcels;
import org.parceler.converter.ArrayListParcelConverter;
import org.parceler.converter.BooleanArrayParcelConverter;
import org.parceler.converter.CharArrayParcelConverter;
import org.parceler.converter.CollectionParcelConverter;
import org.parceler.converter.HashMapParcelConverter;
import org.parceler.converter.HashSetParcelConverter;
import org.parceler.converter.LinkedHashMapParcelConverter;
import org.parceler.converter.LinkedHashSetParcelConverter;
import org.parceler.converter.LinkedListParcelConverter;
import org.parceler.converter.NullableParcelConverter;
import org.parceler.converter.SparseArrayParcelConverter;
import org.parceler.converter.TreeMapParcelConverter;
import org.parceler.converter.TreeSetParcelConverter;

/* loaded from: classes.dex */
final class NonParcelRepository implements Repository<Parcels.ParcelableFactory> {
    private static final NonParcelRepository INSTANCE = new NonParcelRepository();
    private final Map<Class, Parcels.ParcelableFactory> parcelableCollectionFactories = new HashMap();

    private NonParcelRepository() {
        this.parcelableCollectionFactories.put(Collection.class, new CollectionParcelableFactory());
        this.parcelableCollectionFactories.put(List.class, new ListParcelableFactory());
        this.parcelableCollectionFactories.put(ArrayList.class, new ListParcelableFactory());
        this.parcelableCollectionFactories.put(Set.class, new SetParcelableFactory());
        this.parcelableCollectionFactories.put(HashSet.class, new SetParcelableFactory());
        this.parcelableCollectionFactories.put(TreeSet.class, new TreeSetParcelableFactory());
        this.parcelableCollectionFactories.put(SparseArray.class, new SparseArrayParcelableFactory());
        this.parcelableCollectionFactories.put(Map.class, new MapParcelableFactory());
        this.parcelableCollectionFactories.put(HashMap.class, new MapParcelableFactory());
        this.parcelableCollectionFactories.put(TreeMap.class, new TreeMapParcelableFactory());
        this.parcelableCollectionFactories.put(Integer.class, new IntegerParcelableFactory());
        this.parcelableCollectionFactories.put(Long.class, new LongParcelableFactory());
        this.parcelableCollectionFactories.put(Double.class, new DoubleParcelableFactory());
        this.parcelableCollectionFactories.put(Float.class, new FloatParcelableFactory());
        this.parcelableCollectionFactories.put(Byte.class, new ByteParcelableFactory());
        this.parcelableCollectionFactories.put(String.class, new StringParcelableFactory());
        this.parcelableCollectionFactories.put(Character.class, new CharacterParcelableFactory());
        this.parcelableCollectionFactories.put(Boolean.class, new BooleanParcelableFactory());
        this.parcelableCollectionFactories.put(byte[].class, new ByteArrayParcelableFactory());
        this.parcelableCollectionFactories.put(char[].class, new CharArrayParcelableFactory());
        this.parcelableCollectionFactories.put(boolean[].class, new BooleanArrayParcelableFactory());
        this.parcelableCollectionFactories.put(IBinder.class, new IBinderParcelableFactory());
        this.parcelableCollectionFactories.put(Bundle.class, new BundleParcelableFactory());
        this.parcelableCollectionFactories.put(SparseBooleanArray.class, new SparseBooleanArrayParcelableFactory());
        this.parcelableCollectionFactories.put(LinkedList.class, new LinkedListParcelableFactory());
        this.parcelableCollectionFactories.put(LinkedHashMap.class, new LinkedHashMapParcelableFactory());
        this.parcelableCollectionFactories.put(SortedMap.class, new TreeMapParcelableFactory());
        this.parcelableCollectionFactories.put(SortedSet.class, new TreeSetParcelableFactory());
        this.parcelableCollectionFactories.put(LinkedHashSet.class, new LinkedHashSetParcelableFactory());
    }

    public static NonParcelRepository getInstance() {
        return INSTANCE;
    }

    @Override // org.parceler.Repository
    public Map<Class, Parcels.ParcelableFactory> get() {
        return this.parcelableCollectionFactories;
    }

    private static class ListParcelableFactory implements Parcels.ParcelableFactory<List> {
        private ListParcelableFactory() {
        }

        @Override // org.parceler.Parcels.ParcelableFactory
        public Parcelable buildParcelable(List input) {
            return new ListParcelable(input);
        }
    }

    private static class CharacterParcelableFactory implements Parcels.ParcelableFactory<Character> {
        private CharacterParcelableFactory() {
        }

        @Override // org.parceler.Parcels.ParcelableFactory
        public Parcelable buildParcelable(Character input) {
            return new CharacterParcelable(input);
        }
    }

    private static class BooleanParcelableFactory implements Parcels.ParcelableFactory<Boolean> {
        private BooleanParcelableFactory() {
        }

        @Override // org.parceler.Parcels.ParcelableFactory
        public Parcelable buildParcelable(Boolean input) {
            return new BooleanParcelable(input.booleanValue());
        }
    }

    private static class ByteArrayParcelableFactory implements Parcels.ParcelableFactory<byte[]> {
        private ByteArrayParcelableFactory() {
        }

        @Override // org.parceler.Parcels.ParcelableFactory
        public Parcelable buildParcelable(byte[] input) {
            return new ByteArrayParcelable(input);
        }
    }

    private static class CharArrayParcelableFactory implements Parcels.ParcelableFactory<char[]> {
        private CharArrayParcelableFactory() {
        }

        @Override // org.parceler.Parcels.ParcelableFactory
        public Parcelable buildParcelable(char[] input) {
            return new CharArrayParcelable(input);
        }
    }

    private static class BooleanArrayParcelableFactory implements Parcels.ParcelableFactory<boolean[]> {
        private BooleanArrayParcelableFactory() {
        }

        @Override // org.parceler.Parcels.ParcelableFactory
        public Parcelable buildParcelable(boolean[] input) {
            return new BooleanArrayParcelable(input);
        }
    }

    private static class IBinderParcelableFactory implements Parcels.ParcelableFactory<IBinder> {
        private IBinderParcelableFactory() {
        }

        @Override // org.parceler.Parcels.ParcelableFactory
        public Parcelable buildParcelable(IBinder input) {
            return new IBinderParcelable(input);
        }
    }

    private static class BundleParcelableFactory implements Parcels.ParcelableFactory<Bundle> {
        private BundleParcelableFactory() {
        }

        @Override // org.parceler.Parcels.ParcelableFactory
        public Parcelable buildParcelable(Bundle input) {
            return input;
        }
    }

    private static class SparseBooleanArrayParcelableFactory implements Parcels.ParcelableFactory<SparseBooleanArray> {
        private SparseBooleanArrayParcelableFactory() {
        }

        @Override // org.parceler.Parcels.ParcelableFactory
        public Parcelable buildParcelable(SparseBooleanArray input) {
            return new SparseBooleanArrayParcelable(input);
        }
    }

    private static class LinkedListParcelableFactory implements Parcels.ParcelableFactory<LinkedList> {
        private LinkedListParcelableFactory() {
        }

        @Override // org.parceler.Parcels.ParcelableFactory
        public Parcelable buildParcelable(LinkedList input) {
            return new LinkedListParcelable(input);
        }
    }

    private static class LinkedHashMapParcelableFactory implements Parcels.ParcelableFactory<LinkedHashMap> {
        private LinkedHashMapParcelableFactory() {
        }

        @Override // org.parceler.Parcels.ParcelableFactory
        public Parcelable buildParcelable(LinkedHashMap input) {
            return new LinkedHashMapParcelable(input);
        }
    }

    private static class LinkedHashSetParcelableFactory implements Parcels.ParcelableFactory<LinkedHashSet> {
        private LinkedHashSetParcelableFactory() {
        }

        @Override // org.parceler.Parcels.ParcelableFactory
        public Parcelable buildParcelable(LinkedHashSet input) {
            return new LinkedHashSetParcelable(input);
        }
    }

    private static class SetParcelableFactory implements Parcels.ParcelableFactory<Set> {
        private SetParcelableFactory() {
        }

        @Override // org.parceler.Parcels.ParcelableFactory
        public Parcelable buildParcelable(Set input) {
            return new SetParcelable(input);
        }
    }

    private static class TreeSetParcelableFactory implements Parcels.ParcelableFactory<Set> {
        private TreeSetParcelableFactory() {
        }

        @Override // org.parceler.Parcels.ParcelableFactory
        public Parcelable buildParcelable(Set input) {
            return new TreeSetParcelable(input);
        }
    }

    private static class MapParcelableFactory implements Parcels.ParcelableFactory<Map> {
        private MapParcelableFactory() {
        }

        @Override // org.parceler.Parcels.ParcelableFactory
        public Parcelable buildParcelable(Map input) {
            return new MapParcelable(input);
        }
    }

    private static class TreeMapParcelableFactory implements Parcels.ParcelableFactory<Map> {
        private TreeMapParcelableFactory() {
        }

        @Override // org.parceler.Parcels.ParcelableFactory
        public Parcelable buildParcelable(Map input) {
            return new TreeMapParcelable(input);
        }
    }

    private static class CollectionParcelableFactory implements Parcels.ParcelableFactory<Collection> {
        private CollectionParcelableFactory() {
        }

        @Override // org.parceler.Parcels.ParcelableFactory
        public Parcelable buildParcelable(Collection input) {
            return new CollectionParcelable(input);
        }
    }

    private static class SparseArrayParcelableFactory implements Parcels.ParcelableFactory<SparseArray> {
        private SparseArrayParcelableFactory() {
        }

        @Override // org.parceler.Parcels.ParcelableFactory
        public Parcelable buildParcelable(SparseArray input) {
            return new SparseArrayParcelable(input);
        }
    }

    private static class IntegerParcelableFactory implements Parcels.ParcelableFactory<Integer> {
        private IntegerParcelableFactory() {
        }

        @Override // org.parceler.Parcels.ParcelableFactory
        public Parcelable buildParcelable(Integer input) {
            return new IntegerParcelable(input);
        }
    }

    private static class LongParcelableFactory implements Parcels.ParcelableFactory<Long> {
        private LongParcelableFactory() {
        }

        @Override // org.parceler.Parcels.ParcelableFactory
        public Parcelable buildParcelable(Long input) {
            return new LongParcelable(input);
        }
    }

    private static class DoubleParcelableFactory implements Parcels.ParcelableFactory<Double> {
        private DoubleParcelableFactory() {
        }

        @Override // org.parceler.Parcels.ParcelableFactory
        public Parcelable buildParcelable(Double input) {
            return new DoubleParcelable(input);
        }
    }

    private static class FloatParcelableFactory implements Parcels.ParcelableFactory<Float> {
        private FloatParcelableFactory() {
        }

        @Override // org.parceler.Parcels.ParcelableFactory
        public Parcelable buildParcelable(Float input) {
            return new FloatParcelable(input);
        }
    }

    private static class ByteParcelableFactory implements Parcels.ParcelableFactory<Byte> {
        private ByteParcelableFactory() {
        }

        @Override // org.parceler.Parcels.ParcelableFactory
        public Parcelable buildParcelable(Byte input) {
            return new ByteParcelable(input);
        }
    }

    private static class StringParcelableFactory implements Parcels.ParcelableFactory<String> {
        private StringParcelableFactory() {
        }

        @Override // org.parceler.Parcels.ParcelableFactory
        public Parcelable buildParcelable(String input) {
            return new StringParcelable(input);
        }
    }

    public static final class ListParcelable extends ConverterParcelable<List> {
        private static final ArrayListParcelConverter CONVERTER = new ArrayListParcelConverter() { // from class: org.parceler.NonParcelRepository.ListParcelable.1
            @Override // org.parceler.converter.CollectionParcelConverter
            public Object itemFromParcel(Parcel parcel) {
                return Parcels.unwrap(parcel.readParcelable(ListParcelable.class.getClassLoader()));
            }

            @Override // org.parceler.converter.CollectionParcelConverter
            public void itemToParcel(Object input, Parcel parcel) {
                parcel.writeParcelable(Parcels.wrap(input), 0);
            }
        };
        public static final ListParcelableCreator CREATOR = new ListParcelableCreator();

        @Override // org.parceler.NonParcelRepository.ConverterParcelable, android.os.Parcelable
        public /* bridge */ /* synthetic */ int describeContents() {
            return super.describeContents();
        }

        @Override // org.parceler.NonParcelRepository.ConverterParcelable, android.os.Parcelable
        public /* bridge */ /* synthetic */ void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
        }

        public ListParcelable(Parcel parcel) {
            super(parcel, (TypeRangeParcelConverter) CONVERTER);
        }

        public ListParcelable(List value) {
            super(value, CONVERTER);
        }

        private static final class ListParcelableCreator implements Parcelable.Creator<ListParcelable> {
            private ListParcelableCreator() {
            }

            @Override // android.os.Parcelable.Creator
            public ListParcelable createFromParcel(Parcel parcel) {
                return new ListParcelable(parcel);
            }

            @Override // android.os.Parcelable.Creator
            public ListParcelable[] newArray(int size) {
                return new ListParcelable[size];
            }
        }
    }

    public static final class LinkedListParcelable extends ConverterParcelable<LinkedList> {
        private static final LinkedListParcelConverter CONVERTER = new LinkedListParcelConverter() { // from class: org.parceler.NonParcelRepository.LinkedListParcelable.1
            @Override // org.parceler.converter.CollectionParcelConverter
            public Object itemFromParcel(Parcel parcel) {
                return Parcels.unwrap(parcel.readParcelable(LinkedListParcelable.class.getClassLoader()));
            }

            @Override // org.parceler.converter.CollectionParcelConverter
            public void itemToParcel(Object input, Parcel parcel) {
                parcel.writeParcelable(Parcels.wrap(input), 0);
            }
        };
        public static final LinkedListParcelableCreator CREATOR = new LinkedListParcelableCreator();

        @Override // org.parceler.NonParcelRepository.ConverterParcelable, android.os.Parcelable
        public /* bridge */ /* synthetic */ int describeContents() {
            return super.describeContents();
        }

        @Override // org.parceler.NonParcelRepository.ConverterParcelable, android.os.Parcelable
        public /* bridge */ /* synthetic */ void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
        }

        public LinkedListParcelable(Parcel parcel) {
            super(parcel, (TypeRangeParcelConverter) CONVERTER);
        }

        public LinkedListParcelable(LinkedList value) {
            super(value, CONVERTER);
        }

        private static final class LinkedListParcelableCreator implements Parcelable.Creator<LinkedListParcelable> {
            private LinkedListParcelableCreator() {
            }

            @Override // android.os.Parcelable.Creator
            public LinkedListParcelable createFromParcel(Parcel parcel) {
                return new LinkedListParcelable(parcel);
            }

            @Override // android.os.Parcelable.Creator
            public LinkedListParcelable[] newArray(int size) {
                return new LinkedListParcelable[size];
            }
        }
    }

    public static final class MapParcelable extends ConverterParcelable<Map> {
        private static final HashMapParcelConverter CONVERTER = new HashMapParcelConverter() { // from class: org.parceler.NonParcelRepository.MapParcelable.1
            @Override // org.parceler.converter.MapParcelConverter
            public void mapKeyToParcel(Object key, Parcel parcel) {
                parcel.writeParcelable(Parcels.wrap(key), 0);
            }

            @Override // org.parceler.converter.MapParcelConverter
            public void mapValueToParcel(Object value, Parcel parcel) {
                parcel.writeParcelable(Parcels.wrap(value), 0);
            }

            @Override // org.parceler.converter.MapParcelConverter
            public Object mapKeyFromParcel(Parcel parcel) {
                return Parcels.unwrap(parcel.readParcelable(MapParcelable.class.getClassLoader()));
            }

            @Override // org.parceler.converter.MapParcelConverter
            public Object mapValueFromParcel(Parcel parcel) {
                return Parcels.unwrap(parcel.readParcelable(MapParcelable.class.getClassLoader()));
            }
        };
        public static final MapParcelableCreator CREATOR = new MapParcelableCreator();

        @Override // org.parceler.NonParcelRepository.ConverterParcelable, android.os.Parcelable
        public /* bridge */ /* synthetic */ int describeContents() {
            return super.describeContents();
        }

        @Override // org.parceler.NonParcelRepository.ConverterParcelable, android.os.Parcelable
        public /* bridge */ /* synthetic */ void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
        }

        public MapParcelable(Parcel parcel) {
            super(parcel, (TypeRangeParcelConverter) CONVERTER);
        }

        public MapParcelable(Map value) {
            super(value, CONVERTER);
        }

        private static final class MapParcelableCreator implements Parcelable.Creator<MapParcelable> {
            private MapParcelableCreator() {
            }

            @Override // android.os.Parcelable.Creator
            public MapParcelable createFromParcel(Parcel parcel$$17) {
                return new MapParcelable(parcel$$17);
            }

            @Override // android.os.Parcelable.Creator
            public MapParcelable[] newArray(int size) {
                return new MapParcelable[size];
            }
        }
    }

    public static final class LinkedHashMapParcelable extends ConverterParcelable<LinkedHashMap> {
        private static final LinkedHashMapParcelConverter CONVERTER = new LinkedHashMapParcelConverter() { // from class: org.parceler.NonParcelRepository.LinkedHashMapParcelable.1
            @Override // org.parceler.converter.MapParcelConverter
            public void mapKeyToParcel(Object key, Parcel parcel) {
                parcel.writeParcelable(Parcels.wrap(key), 0);
            }

            @Override // org.parceler.converter.MapParcelConverter
            public void mapValueToParcel(Object value, Parcel parcel) {
                parcel.writeParcelable(Parcels.wrap(value), 0);
            }

            @Override // org.parceler.converter.MapParcelConverter
            public Object mapKeyFromParcel(Parcel parcel) {
                return Parcels.unwrap(parcel.readParcelable(MapParcelable.class.getClassLoader()));
            }

            @Override // org.parceler.converter.MapParcelConverter
            public Object mapValueFromParcel(Parcel parcel) {
                return Parcels.unwrap(parcel.readParcelable(MapParcelable.class.getClassLoader()));
            }
        };
        public static final LinkedHashMapParcelableCreator CREATOR = new LinkedHashMapParcelableCreator();

        @Override // org.parceler.NonParcelRepository.ConverterParcelable, android.os.Parcelable
        public /* bridge */ /* synthetic */ int describeContents() {
            return super.describeContents();
        }

        @Override // org.parceler.NonParcelRepository.ConverterParcelable, android.os.Parcelable
        public /* bridge */ /* synthetic */ void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
        }

        public LinkedHashMapParcelable(Parcel parcel) {
            super(parcel, (TypeRangeParcelConverter) CONVERTER);
        }

        public LinkedHashMapParcelable(LinkedHashMap value) {
            super(value, CONVERTER);
        }

        private static final class LinkedHashMapParcelableCreator implements Parcelable.Creator<LinkedHashMapParcelable> {
            private LinkedHashMapParcelableCreator() {
            }

            @Override // android.os.Parcelable.Creator
            public LinkedHashMapParcelable createFromParcel(Parcel parcel$$17) {
                return new LinkedHashMapParcelable(parcel$$17);
            }

            @Override // android.os.Parcelable.Creator
            public LinkedHashMapParcelable[] newArray(int size) {
                return new LinkedHashMapParcelable[size];
            }
        }
    }

    public static final class TreeMapParcelable extends ConverterParcelable<Map> {
        private static final TreeMapParcelConverter CONVERTER = new TreeMapParcelConverter() { // from class: org.parceler.NonParcelRepository.TreeMapParcelable.1
            @Override // org.parceler.converter.MapParcelConverter
            public void mapKeyToParcel(Object key, Parcel parcel) {
                parcel.writeParcelable(Parcels.wrap(key), 0);
            }

            @Override // org.parceler.converter.MapParcelConverter
            public void mapValueToParcel(Object value, Parcel parcel) {
                parcel.writeParcelable(Parcels.wrap(value), 0);
            }

            @Override // org.parceler.converter.MapParcelConverter
            public Object mapKeyFromParcel(Parcel parcel) {
                return Parcels.unwrap(parcel.readParcelable(MapParcelable.class.getClassLoader()));
            }

            @Override // org.parceler.converter.MapParcelConverter
            public Object mapValueFromParcel(Parcel parcel) {
                return Parcels.unwrap(parcel.readParcelable(MapParcelable.class.getClassLoader()));
            }
        };
        public static final TreeMapParcelableCreator CREATOR = new TreeMapParcelableCreator();

        @Override // org.parceler.NonParcelRepository.ConverterParcelable, android.os.Parcelable
        public /* bridge */ /* synthetic */ int describeContents() {
            return super.describeContents();
        }

        @Override // org.parceler.NonParcelRepository.ConverterParcelable, android.os.Parcelable
        public /* bridge */ /* synthetic */ void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
        }

        public TreeMapParcelable(Parcel parcel) {
            super(parcel, (TypeRangeParcelConverter) CONVERTER);
        }

        public TreeMapParcelable(Map value) {
            super(value, CONVERTER);
        }

        private static final class TreeMapParcelableCreator implements Parcelable.Creator<TreeMapParcelable> {
            private TreeMapParcelableCreator() {
            }

            @Override // android.os.Parcelable.Creator
            public TreeMapParcelable createFromParcel(Parcel parcel$$17) {
                return new TreeMapParcelable(parcel$$17);
            }

            @Override // android.os.Parcelable.Creator
            public TreeMapParcelable[] newArray(int size) {
                return new TreeMapParcelable[size];
            }
        }
    }

    public static final class SetParcelable extends ConverterParcelable<Set> {
        private static final HashSetParcelConverter CONVERTER = new HashSetParcelConverter() { // from class: org.parceler.NonParcelRepository.SetParcelable.1
            @Override // org.parceler.converter.CollectionParcelConverter
            public Object itemFromParcel(Parcel parcel) {
                return Parcels.unwrap(parcel.readParcelable(SetParcelable.class.getClassLoader()));
            }

            @Override // org.parceler.converter.CollectionParcelConverter
            public void itemToParcel(Object input, Parcel parcel) {
                parcel.writeParcelable(Parcels.wrap(input), 0);
            }
        };
        public static final SetParcelableCreator CREATOR = new SetParcelableCreator();

        @Override // org.parceler.NonParcelRepository.ConverterParcelable, android.os.Parcelable
        public /* bridge */ /* synthetic */ int describeContents() {
            return super.describeContents();
        }

        @Override // org.parceler.NonParcelRepository.ConverterParcelable, android.os.Parcelable
        public /* bridge */ /* synthetic */ void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
        }

        public SetParcelable(Parcel parcel) {
            super(parcel, (TypeRangeParcelConverter) CONVERTER);
        }

        public SetParcelable(Set value) {
            super(value, CONVERTER);
        }

        private static final class SetParcelableCreator implements Parcelable.Creator<SetParcelable> {
            private SetParcelableCreator() {
            }

            @Override // android.os.Parcelable.Creator
            public SetParcelable createFromParcel(Parcel parcel) {
                return new SetParcelable(parcel);
            }

            @Override // android.os.Parcelable.Creator
            public SetParcelable[] newArray(int size) {
                return new SetParcelable[size];
            }
        }
    }

    public static final class TreeSetParcelable extends ConverterParcelable<Set> {
        private static final TreeSetParcelConverter CONVERTER = new TreeSetParcelConverter() { // from class: org.parceler.NonParcelRepository.TreeSetParcelable.1
            @Override // org.parceler.converter.CollectionParcelConverter
            public Object itemFromParcel(Parcel parcel) {
                return Parcels.unwrap(parcel.readParcelable(TreeSetParcelable.class.getClassLoader()));
            }

            @Override // org.parceler.converter.CollectionParcelConverter
            public void itemToParcel(Object input, Parcel parcel) {
                parcel.writeParcelable(Parcels.wrap(input), 0);
            }
        };
        public static final TreeSetParcelableCreator CREATOR = new TreeSetParcelableCreator();

        @Override // org.parceler.NonParcelRepository.ConverterParcelable, android.os.Parcelable
        public /* bridge */ /* synthetic */ int describeContents() {
            return super.describeContents();
        }

        @Override // org.parceler.NonParcelRepository.ConverterParcelable, android.os.Parcelable
        public /* bridge */ /* synthetic */ void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
        }

        public TreeSetParcelable(Parcel parcel) {
            super(parcel, (TypeRangeParcelConverter) CONVERTER);
        }

        public TreeSetParcelable(Set value) {
            super(value, CONVERTER);
        }

        private static final class TreeSetParcelableCreator implements Parcelable.Creator<TreeSetParcelable> {
            private TreeSetParcelableCreator() {
            }

            @Override // android.os.Parcelable.Creator
            public TreeSetParcelable createFromParcel(Parcel parcel) {
                return new TreeSetParcelable(parcel);
            }

            @Override // android.os.Parcelable.Creator
            public TreeSetParcelable[] newArray(int size) {
                return new TreeSetParcelable[size];
            }
        }
    }

    public static final class LinkedHashSetParcelable extends ConverterParcelable<LinkedHashSet> {
        private static final LinkedHashSetParcelConverter CONVERTER = new LinkedHashSetParcelConverter() { // from class: org.parceler.NonParcelRepository.LinkedHashSetParcelable.1
            @Override // org.parceler.converter.CollectionParcelConverter
            public Object itemFromParcel(Parcel parcel) {
                return Parcels.unwrap(parcel.readParcelable(LinkedHashSetParcelable.class.getClassLoader()));
            }

            @Override // org.parceler.converter.CollectionParcelConverter
            public void itemToParcel(Object input, Parcel parcel) {
                parcel.writeParcelable(Parcels.wrap(input), 0);
            }
        };
        public static final LinkedHashSetParcelableCreator CREATOR = new LinkedHashSetParcelableCreator();

        @Override // org.parceler.NonParcelRepository.ConverterParcelable, android.os.Parcelable
        public /* bridge */ /* synthetic */ int describeContents() {
            return super.describeContents();
        }

        @Override // org.parceler.NonParcelRepository.ConverterParcelable, android.os.Parcelable
        public /* bridge */ /* synthetic */ void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
        }

        public LinkedHashSetParcelable(Parcel parcel) {
            super(parcel, (TypeRangeParcelConverter) CONVERTER);
        }

        public LinkedHashSetParcelable(LinkedHashSet value) {
            super(value, CONVERTER);
        }

        private static final class LinkedHashSetParcelableCreator implements Parcelable.Creator<LinkedHashSetParcelable> {
            private LinkedHashSetParcelableCreator() {
            }

            @Override // android.os.Parcelable.Creator
            public LinkedHashSetParcelable createFromParcel(Parcel parcel) {
                return new LinkedHashSetParcelable(parcel);
            }

            @Override // android.os.Parcelable.Creator
            public LinkedHashSetParcelable[] newArray(int size) {
                return new LinkedHashSetParcelable[size];
            }
        }
    }

    public static final class CollectionParcelable extends ConverterParcelable<Collection> {
        private static final CollectionParcelConverter CONVERTER = new ArrayListParcelConverter() { // from class: org.parceler.NonParcelRepository.CollectionParcelable.1
            @Override // org.parceler.converter.CollectionParcelConverter
            public Object itemFromParcel(Parcel parcel) {
                return Parcels.unwrap(parcel.readParcelable(CollectionParcelable.class.getClassLoader()));
            }

            @Override // org.parceler.converter.CollectionParcelConverter
            public void itemToParcel(Object input, Parcel parcel) {
                parcel.writeParcelable(Parcels.wrap(input), 0);
            }
        };
        public static final CollectionParcelableCreator CREATOR = new CollectionParcelableCreator();

        @Override // org.parceler.NonParcelRepository.ConverterParcelable, android.os.Parcelable
        public /* bridge */ /* synthetic */ int describeContents() {
            return super.describeContents();
        }

        @Override // org.parceler.NonParcelRepository.ConverterParcelable, android.os.Parcelable
        public /* bridge */ /* synthetic */ void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
        }

        public CollectionParcelable(Parcel parcel) {
            super(parcel, (TypeRangeParcelConverter) CONVERTER);
        }

        public CollectionParcelable(Collection value) {
            super(value, CONVERTER);
        }

        private static final class CollectionParcelableCreator implements Parcelable.Creator<CollectionParcelable> {
            private CollectionParcelableCreator() {
            }

            @Override // android.os.Parcelable.Creator
            public CollectionParcelable createFromParcel(Parcel parcel) {
                return new CollectionParcelable(parcel);
            }

            @Override // android.os.Parcelable.Creator
            public CollectionParcelable[] newArray(int size) {
                return new CollectionParcelable[size];
            }
        }
    }

    public static final class SparseArrayParcelable extends ConverterParcelable<SparseArray> {
        private static final SparseArrayParcelConverter CONVERTER = new SparseArrayParcelConverter() { // from class: org.parceler.NonParcelRepository.SparseArrayParcelable.1
            @Override // org.parceler.converter.SparseArrayParcelConverter
            public Object itemFromParcel(Parcel parcel) {
                return Parcels.unwrap(parcel.readParcelable(SparseArrayParcelable.class.getClassLoader()));
            }

            @Override // org.parceler.converter.SparseArrayParcelConverter
            public void itemToParcel(Object input, Parcel parcel) {
                parcel.writeParcelable(Parcels.wrap(input), 0);
            }
        };
        public static final SparseArrayCreator CREATOR = new SparseArrayCreator();

        @Override // org.parceler.NonParcelRepository.ConverterParcelable, android.os.Parcelable
        public /* bridge */ /* synthetic */ int describeContents() {
            return super.describeContents();
        }

        @Override // org.parceler.NonParcelRepository.ConverterParcelable, android.os.Parcelable
        public /* bridge */ /* synthetic */ void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
        }

        public SparseArrayParcelable(Parcel parcel) {
            super(parcel, (TypeRangeParcelConverter) CONVERTER);
        }

        public SparseArrayParcelable(SparseArray value) {
            super(value, CONVERTER);
        }

        private static final class SparseArrayCreator implements Parcelable.Creator<SparseArrayParcelable> {
            private SparseArrayCreator() {
            }

            @Override // android.os.Parcelable.Creator
            public SparseArrayParcelable createFromParcel(Parcel parcel) {
                return new SparseArrayParcelable(parcel);
            }

            @Override // android.os.Parcelable.Creator
            public SparseArrayParcelable[] newArray(int size) {
                return new SparseArrayParcelable[size];
            }
        }
    }

    public static final class SparseBooleanArrayParcelable extends ConverterParcelable<SparseBooleanArray> {
        private static final NullableParcelConverter<SparseBooleanArray> CONVERTER = new NullableParcelConverter<SparseBooleanArray>() { // from class: org.parceler.NonParcelRepository.SparseBooleanArrayParcelable.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.parceler.converter.NullableParcelConverter
            public SparseBooleanArray nullSafeFromParcel(Parcel parcel) {
                return parcel.readSparseBooleanArray();
            }

            @Override // org.parceler.converter.NullableParcelConverter
            public void nullSafeToParcel(SparseBooleanArray input, Parcel parcel) {
                parcel.writeSparseBooleanArray(input);
            }
        };
        public static final SparseBooleanArrayCreator CREATOR = new SparseBooleanArrayCreator();

        @Override // org.parceler.NonParcelRepository.ConverterParcelable, android.os.Parcelable
        public /* bridge */ /* synthetic */ int describeContents() {
            return super.describeContents();
        }

        @Override // org.parceler.NonParcelRepository.ConverterParcelable, android.os.Parcelable
        public /* bridge */ /* synthetic */ void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
        }

        public SparseBooleanArrayParcelable(Parcel parcel) {
            super(parcel, (TypeRangeParcelConverter) CONVERTER);
        }

        public SparseBooleanArrayParcelable(SparseBooleanArray value) {
            super(value, CONVERTER);
        }

        private static final class SparseBooleanArrayCreator implements Parcelable.Creator<SparseBooleanArrayParcelable> {
            private SparseBooleanArrayCreator() {
            }

            @Override // android.os.Parcelable.Creator
            public SparseBooleanArrayParcelable createFromParcel(Parcel parcel) {
                return new SparseBooleanArrayParcelable(parcel);
            }

            @Override // android.os.Parcelable.Creator
            public SparseBooleanArrayParcelable[] newArray(int size) {
                return new SparseBooleanArrayParcelable[size];
            }
        }
    }

    public static final class IntegerParcelable extends ConverterParcelable<Integer> {
        private static final NullableParcelConverter<Integer> CONVERTER = new NullableParcelConverter<Integer>() { // from class: org.parceler.NonParcelRepository.IntegerParcelable.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.parceler.converter.NullableParcelConverter
            public Integer nullSafeFromParcel(Parcel parcel) {
                return Integer.valueOf(parcel.readInt());
            }

            @Override // org.parceler.converter.NullableParcelConverter
            public void nullSafeToParcel(Integer input, Parcel parcel) {
                parcel.writeInt(input.intValue());
            }
        };
        public static final IntegerParcelableCreator CREATOR = new IntegerParcelableCreator();

        @Override // org.parceler.NonParcelRepository.ConverterParcelable, android.os.Parcelable
        public /* bridge */ /* synthetic */ int describeContents() {
            return super.describeContents();
        }

        @Override // org.parceler.NonParcelRepository.ConverterParcelable, android.os.Parcelable
        public /* bridge */ /* synthetic */ void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
        }

        public IntegerParcelable(Parcel parcel) {
            super(parcel, (TypeRangeParcelConverter) CONVERTER);
        }

        public IntegerParcelable(Integer value) {
            super(value, CONVERTER);
        }

        private static final class IntegerParcelableCreator implements Parcelable.Creator<IntegerParcelable> {
            private IntegerParcelableCreator() {
            }

            @Override // android.os.Parcelable.Creator
            public IntegerParcelable createFromParcel(Parcel parcel) {
                return new IntegerParcelable(parcel);
            }

            @Override // android.os.Parcelable.Creator
            public IntegerParcelable[] newArray(int size) {
                return new IntegerParcelable[size];
            }
        }
    }

    public static final class LongParcelable extends ConverterParcelable<Long> {
        private static final NullableParcelConverter<Long> CONVERTER = new NullableParcelConverter<Long>() { // from class: org.parceler.NonParcelRepository.LongParcelable.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.parceler.converter.NullableParcelConverter
            public Long nullSafeFromParcel(Parcel parcel) {
                return Long.valueOf(parcel.readLong());
            }

            @Override // org.parceler.converter.NullableParcelConverter
            public void nullSafeToParcel(Long input, Parcel parcel) {
                parcel.writeLong(input.longValue());
            }
        };
        public static final LongParcelableCreator CREATOR = new LongParcelableCreator();

        @Override // org.parceler.NonParcelRepository.ConverterParcelable, android.os.Parcelable
        public /* bridge */ /* synthetic */ int describeContents() {
            return super.describeContents();
        }

        @Override // org.parceler.NonParcelRepository.ConverterParcelable, android.os.Parcelable
        public /* bridge */ /* synthetic */ void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
        }

        public LongParcelable(Parcel parcel) {
            super(parcel, (TypeRangeParcelConverter) CONVERTER);
        }

        public LongParcelable(Long value) {
            super(value, CONVERTER);
        }

        private static final class LongParcelableCreator implements Parcelable.Creator<LongParcelable> {
            private LongParcelableCreator() {
            }

            @Override // android.os.Parcelable.Creator
            public LongParcelable createFromParcel(Parcel parcel) {
                return new LongParcelable(parcel);
            }

            @Override // android.os.Parcelable.Creator
            public LongParcelable[] newArray(int size) {
                return new LongParcelable[size];
            }
        }
    }

    public static final class DoubleParcelable extends ConverterParcelable<Double> {
        private static final NullableParcelConverter<Double> CONVERTER = new NullableParcelConverter<Double>() { // from class: org.parceler.NonParcelRepository.DoubleParcelable.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.parceler.converter.NullableParcelConverter
            public Double nullSafeFromParcel(Parcel parcel) {
                return Double.valueOf(parcel.readDouble());
            }

            @Override // org.parceler.converter.NullableParcelConverter
            public void nullSafeToParcel(Double input, Parcel parcel) {
                parcel.writeDouble(input.doubleValue());
            }
        };
        public static final DoubleParcelableCreator CREATOR = new DoubleParcelableCreator();

        @Override // org.parceler.NonParcelRepository.ConverterParcelable, android.os.Parcelable
        public /* bridge */ /* synthetic */ int describeContents() {
            return super.describeContents();
        }

        @Override // org.parceler.NonParcelRepository.ConverterParcelable, android.os.Parcelable
        public /* bridge */ /* synthetic */ void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
        }

        public DoubleParcelable(Parcel parcel) {
            super(parcel, (TypeRangeParcelConverter) CONVERTER);
        }

        public DoubleParcelable(Double value) {
            super(value, CONVERTER);
        }

        private static final class DoubleParcelableCreator implements Parcelable.Creator<DoubleParcelable> {
            private DoubleParcelableCreator() {
            }

            @Override // android.os.Parcelable.Creator
            public DoubleParcelable createFromParcel(Parcel parcel) {
                return new DoubleParcelable(parcel);
            }

            @Override // android.os.Parcelable.Creator
            public DoubleParcelable[] newArray(int size) {
                return new DoubleParcelable[size];
            }
        }
    }

    public static final class FloatParcelable extends ConverterParcelable<Float> {
        private static final NullableParcelConverter<Float> CONVERTER = new NullableParcelConverter<Float>() { // from class: org.parceler.NonParcelRepository.FloatParcelable.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.parceler.converter.NullableParcelConverter
            public Float nullSafeFromParcel(Parcel parcel) {
                return Float.valueOf(parcel.readFloat());
            }

            @Override // org.parceler.converter.NullableParcelConverter
            public void nullSafeToParcel(Float input, Parcel parcel) {
                parcel.writeFloat(input.floatValue());
            }
        };
        public static final FloatParcelableCreator CREATOR = new FloatParcelableCreator();

        @Override // org.parceler.NonParcelRepository.ConverterParcelable, android.os.Parcelable
        public /* bridge */ /* synthetic */ int describeContents() {
            return super.describeContents();
        }

        @Override // org.parceler.NonParcelRepository.ConverterParcelable, android.os.Parcelable
        public /* bridge */ /* synthetic */ void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
        }

        public FloatParcelable(Parcel parcel) {
            super(parcel, (TypeRangeParcelConverter) CONVERTER);
        }

        public FloatParcelable(Float value) {
            super(value, CONVERTER);
        }

        private static final class FloatParcelableCreator implements Parcelable.Creator<FloatParcelable> {
            private FloatParcelableCreator() {
            }

            @Override // android.os.Parcelable.Creator
            public FloatParcelable createFromParcel(Parcel parcel) {
                return new FloatParcelable(parcel);
            }

            @Override // android.os.Parcelable.Creator
            public FloatParcelable[] newArray(int size) {
                return new FloatParcelable[size];
            }
        }
    }

    public static final class ByteParcelable extends ConverterParcelable<Byte> {
        private static final NullableParcelConverter<Byte> CONVERTER = new NullableParcelConverter<Byte>() { // from class: org.parceler.NonParcelRepository.ByteParcelable.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.parceler.converter.NullableParcelConverter
            public Byte nullSafeFromParcel(Parcel parcel) {
                return Byte.valueOf(parcel.readByte());
            }

            @Override // org.parceler.converter.NullableParcelConverter
            public void nullSafeToParcel(Byte input, Parcel parcel) {
                parcel.writeByte(input.byteValue());
            }
        };
        public static final ByteParcelableCreator CREATOR = new ByteParcelableCreator();

        @Override // org.parceler.NonParcelRepository.ConverterParcelable, android.os.Parcelable
        public /* bridge */ /* synthetic */ int describeContents() {
            return super.describeContents();
        }

        @Override // org.parceler.NonParcelRepository.ConverterParcelable, android.os.Parcelable
        public /* bridge */ /* synthetic */ void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
        }

        public ByteParcelable(Parcel parcel) {
            super(parcel, (TypeRangeParcelConverter) CONVERTER);
        }

        public ByteParcelable(Byte value) {
            super(value, CONVERTER);
        }

        private static final class ByteParcelableCreator implements Parcelable.Creator<ByteParcelable> {
            private ByteParcelableCreator() {
            }

            @Override // android.os.Parcelable.Creator
            public ByteParcelable createFromParcel(Parcel parcel) {
                return new ByteParcelable(parcel);
            }

            @Override // android.os.Parcelable.Creator
            public ByteParcelable[] newArray(int size) {
                return new ByteParcelable[size];
            }
        }
    }

    public static final class IBinderParcelable extends ConverterParcelable<IBinder> {
        private static final NullableParcelConverter<IBinder> CONVERTER = new NullableParcelConverter<IBinder>() { // from class: org.parceler.NonParcelRepository.IBinderParcelable.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.parceler.converter.NullableParcelConverter
            public IBinder nullSafeFromParcel(Parcel parcel) {
                return parcel.readStrongBinder();
            }

            @Override // org.parceler.converter.NullableParcelConverter
            public void nullSafeToParcel(IBinder input, Parcel parcel) {
                parcel.writeStrongBinder(input);
            }
        };
        public static final IBinderParcelableCreator CREATOR = new IBinderParcelableCreator();

        @Override // org.parceler.NonParcelRepository.ConverterParcelable, android.os.Parcelable
        public /* bridge */ /* synthetic */ int describeContents() {
            return super.describeContents();
        }

        @Override // org.parceler.NonParcelRepository.ConverterParcelable, android.os.Parcelable
        public /* bridge */ /* synthetic */ void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
        }

        public IBinderParcelable(Parcel parcel) {
            super(parcel, (TypeRangeParcelConverter) CONVERTER);
        }

        public IBinderParcelable(IBinder value) {
            super(value, CONVERTER);
        }

        private static final class IBinderParcelableCreator implements Parcelable.Creator<IBinderParcelable> {
            private IBinderParcelableCreator() {
            }

            @Override // android.os.Parcelable.Creator
            public IBinderParcelable createFromParcel(Parcel parcel) {
                return new IBinderParcelable(parcel);
            }

            @Override // android.os.Parcelable.Creator
            public IBinderParcelable[] newArray(int size) {
                return new IBinderParcelable[size];
            }
        }
    }

    public static final class ByteArrayParcelable extends ConverterParcelable<byte[]> {
        private static final NullableParcelConverter<byte[]> CONVERTER = new NullableParcelConverter<byte[]>() { // from class: org.parceler.NonParcelRepository.ByteArrayParcelable.1
            @Override // org.parceler.converter.NullableParcelConverter
            public byte[] nullSafeFromParcel(Parcel parcel) {
                return parcel.createByteArray();
            }

            @Override // org.parceler.converter.NullableParcelConverter
            public void nullSafeToParcel(byte[] input, Parcel parcel) {
                parcel.writeByteArray(input);
            }
        };
        public static final ByteArrayParcelableCreator CREATOR = new ByteArrayParcelableCreator();

        @Override // org.parceler.NonParcelRepository.ConverterParcelable, android.os.Parcelable
        public /* bridge */ /* synthetic */ int describeContents() {
            return super.describeContents();
        }

        @Override // org.parceler.NonParcelRepository.ConverterParcelable, android.os.Parcelable
        public /* bridge */ /* synthetic */ void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
        }

        public ByteArrayParcelable(Parcel parcel) {
            super(parcel, (TypeRangeParcelConverter) CONVERTER);
        }

        public ByteArrayParcelable(byte[] value) {
            super(value, CONVERTER);
        }

        private static final class ByteArrayParcelableCreator implements Parcelable.Creator<ByteArrayParcelable> {
            private ByteArrayParcelableCreator() {
            }

            @Override // android.os.Parcelable.Creator
            public ByteArrayParcelable createFromParcel(Parcel parcel) {
                return new ByteArrayParcelable(parcel);
            }

            @Override // android.os.Parcelable.Creator
            public ByteArrayParcelable[] newArray(int size) {
                return new ByteArrayParcelable[size];
            }
        }
    }

    public static final class BooleanArrayParcelable extends ConverterParcelable<boolean[]> {
        private static final BooleanArrayParcelConverter CONVERTER = new BooleanArrayParcelConverter();
        public static final BooleanArrayParcelableCreator CREATOR = new BooleanArrayParcelableCreator();

        @Override // org.parceler.NonParcelRepository.ConverterParcelable, android.os.Parcelable
        public /* bridge */ /* synthetic */ int describeContents() {
            return super.describeContents();
        }

        @Override // org.parceler.NonParcelRepository.ConverterParcelable, android.os.Parcelable
        public /* bridge */ /* synthetic */ void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
        }

        public BooleanArrayParcelable(Parcel parcel) {
            super(parcel, (TypeRangeParcelConverter) CONVERTER);
        }

        public BooleanArrayParcelable(boolean[] value) {
            super(value, CONVERTER);
        }

        private static final class BooleanArrayParcelableCreator implements Parcelable.Creator<BooleanArrayParcelable> {
            private BooleanArrayParcelableCreator() {
            }

            @Override // android.os.Parcelable.Creator
            public BooleanArrayParcelable createFromParcel(Parcel parcel) {
                return new BooleanArrayParcelable(parcel);
            }

            @Override // android.os.Parcelable.Creator
            public BooleanArrayParcelable[] newArray(int size) {
                return new BooleanArrayParcelable[size];
            }
        }
    }

    public static final class BooleanParcelable extends ConverterParcelable<Boolean> {
        private static final NullableParcelConverter<Boolean> CONVERTER = new NullableParcelConverter<Boolean>() { // from class: org.parceler.NonParcelRepository.BooleanParcelable.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.parceler.converter.NullableParcelConverter
            public Boolean nullSafeFromParcel(Parcel parcel) {
                return Boolean.valueOf(parcel.createBooleanArray()[0]);
            }

            @Override // org.parceler.converter.NullableParcelConverter
            public void nullSafeToParcel(Boolean input, Parcel parcel) {
                parcel.writeBooleanArray(new boolean[]{input.booleanValue()});
            }
        };
        public static final BooleanParcelableCreator CREATOR = new BooleanParcelableCreator();

        @Override // org.parceler.NonParcelRepository.ConverterParcelable, android.os.Parcelable
        public /* bridge */ /* synthetic */ int describeContents() {
            return super.describeContents();
        }

        @Override // org.parceler.NonParcelRepository.ConverterParcelable, android.os.Parcelable
        public /* bridge */ /* synthetic */ void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
        }

        public BooleanParcelable(Parcel parcel) {
            super(parcel, (TypeRangeParcelConverter) CONVERTER);
        }

        public BooleanParcelable(boolean value) {
            super(Boolean.valueOf(value), CONVERTER);
        }

        private static final class BooleanParcelableCreator implements Parcelable.Creator<BooleanParcelable> {
            private BooleanParcelableCreator() {
            }

            @Override // android.os.Parcelable.Creator
            public BooleanParcelable createFromParcel(Parcel parcel) {
                return new BooleanParcelable(parcel);
            }

            @Override // android.os.Parcelable.Creator
            public BooleanParcelable[] newArray(int size) {
                return new BooleanParcelable[size];
            }
        }
    }

    public static final class CharArrayParcelable extends ConverterParcelable<char[]> {
        private static final CharArrayParcelConverter CONVERTER = new CharArrayParcelConverter();
        public static final CharArrayParcelableCreator CREATOR = new CharArrayParcelableCreator();

        @Override // org.parceler.NonParcelRepository.ConverterParcelable, android.os.Parcelable
        public /* bridge */ /* synthetic */ int describeContents() {
            return super.describeContents();
        }

        @Override // org.parceler.NonParcelRepository.ConverterParcelable, android.os.Parcelable
        public /* bridge */ /* synthetic */ void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
        }

        public CharArrayParcelable(Parcel parcel) {
            super(parcel, (TypeRangeParcelConverter) CONVERTER);
        }

        public CharArrayParcelable(char[] value) {
            super(value, CONVERTER);
        }

        private static final class CharArrayParcelableCreator implements Parcelable.Creator<CharArrayParcelable> {
            private CharArrayParcelableCreator() {
            }

            @Override // android.os.Parcelable.Creator
            public CharArrayParcelable createFromParcel(Parcel parcel) {
                return new CharArrayParcelable(parcel);
            }

            @Override // android.os.Parcelable.Creator
            public CharArrayParcelable[] newArray(int size) {
                return new CharArrayParcelable[size];
            }
        }
    }

    public static final class CharacterParcelable extends ConverterParcelable<Character> {
        private static final NullableParcelConverter<Character> CONVERTER = new NullableParcelConverter<Character>() { // from class: org.parceler.NonParcelRepository.CharacterParcelable.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.parceler.converter.NullableParcelConverter
            public Character nullSafeFromParcel(Parcel parcel) {
                return Character.valueOf(parcel.createCharArray()[0]);
            }

            @Override // org.parceler.converter.NullableParcelConverter
            public void nullSafeToParcel(Character input, Parcel parcel) {
                parcel.writeCharArray(new char[]{input.charValue()});
            }
        };
        public static final CharacterParcelableCreator CREATOR = new CharacterParcelableCreator();

        @Override // org.parceler.NonParcelRepository.ConverterParcelable, android.os.Parcelable
        public /* bridge */ /* synthetic */ int describeContents() {
            return super.describeContents();
        }

        @Override // org.parceler.NonParcelRepository.ConverterParcelable, android.os.Parcelable
        public /* bridge */ /* synthetic */ void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
        }

        public CharacterParcelable(Parcel parcel) {
            super(parcel, (TypeRangeParcelConverter) CONVERTER);
        }

        public CharacterParcelable(Character value) {
            super(value, CONVERTER);
        }

        private static final class CharacterParcelableCreator implements Parcelable.Creator<CharacterParcelable> {
            private CharacterParcelableCreator() {
            }

            @Override // android.os.Parcelable.Creator
            public CharacterParcelable createFromParcel(Parcel parcel) {
                return new CharacterParcelable(parcel);
            }

            @Override // android.os.Parcelable.Creator
            public CharacterParcelable[] newArray(int size) {
                return new CharacterParcelable[size];
            }
        }
    }

    public static final class StringParcelable implements Parcelable, ParcelWrapper<String> {
        public static final StringParcelableCreator CREATOR = new StringParcelableCreator();
        private String contents;

        private StringParcelable(Parcel parcel) {
            this.contents = parcel.readString();
        }

        private StringParcelable(String contents) {
            this.contents = contents;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int flags) {
            parcel.writeString(this.contents);
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // org.parceler.ParcelWrapper
        public String getParcel() {
            return this.contents;
        }

        private static final class StringParcelableCreator implements Parcelable.Creator<StringParcelable> {
            private StringParcelableCreator() {
            }

            @Override // android.os.Parcelable.Creator
            public StringParcelable createFromParcel(Parcel parcel) {
                return new StringParcelable(parcel);
            }

            @Override // android.os.Parcelable.Creator
            public StringParcelable[] newArray(int size) {
                return new StringParcelable[size];
            }
        }
    }

    private static class ConverterParcelable<T> implements Parcelable, ParcelWrapper<T> {
        private final TypeRangeParcelConverter<T, T> converter;
        private final T value;

        private ConverterParcelable(Parcel parcel, TypeRangeParcelConverter<T, T> converter) {
            this(converter.fromParcel(parcel), converter);
        }

        private ConverterParcelable(T value, TypeRangeParcelConverter<T, T> converter) {
            this.converter = converter;
            this.value = value;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int flags) {
            this.converter.toParcel(this.value, parcel);
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // org.parceler.ParcelWrapper
        public T getParcel() {
            return this.value;
        }
    }
}
