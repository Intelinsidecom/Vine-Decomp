package com.facebook.buck.android.support.exopackage;

import android.annotation.TargetApi;
import dalvik.system.BaseDexClassLoader;
import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;
import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.List;

/* loaded from: classes.dex */
class SystemClassLoaderAdder {
    private SystemClassLoaderAdder() {
    }

    static void installDexJars(ClassLoader appClassLoader, File optimizedDirectory, List<File> dexJars) throws ArrayIndexOutOfBoundsException, IllegalArgumentException, NegativeArraySizeException {
        SystemClassLoaderAdder classLoaderAdder = new SystemClassLoaderAdder();
        for (File dexJar : dexJars) {
            DexClassLoader newClassLoader = new DexClassLoader(dexJar.getAbsolutePath(), optimizedDirectory.getAbsolutePath(), null, appClassLoader);
            classLoaderAdder.addPathsOfClassLoaderToSystemClassLoader(newClassLoader, (PathClassLoader) appClassLoader);
        }
    }

    private void addPathsOfClassLoaderToSystemClassLoader(DexClassLoader newClassLoader, PathClassLoader systemClassLoader) throws ArrayIndexOutOfBoundsException, IllegalArgumentException, NegativeArraySizeException {
        try {
            if (existsBaseDexClassLoader()) {
                addNewClassLoaderToSystemClassLoaderWithBaseDex(newClassLoader, systemClassLoader);
            } else {
                addNewClassLoaderToSystemClassLoaderPreBaseDex(newClassLoader, systemClassLoader);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e2) {
            throw new RuntimeException(e2);
        }
    }

    private boolean existsBaseDexClassLoader() throws ClassNotFoundException {
        try {
            Class.forName("dalvik.system.BaseDexClassLoader");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    private void addNewClassLoaderToSystemClassLoaderWithBaseDex(DexClassLoader newClassLoader, PathClassLoader systemClassLoader) throws IllegalAccessException, NoSuchFieldException, ArrayIndexOutOfBoundsException, IllegalArgumentException, NegativeArraySizeException {
        Object currentElementsArray = getDexElementsArray(getDexPathList(systemClassLoader));
        Object newElementsArray = getDexElementsArray(getDexPathList(newClassLoader));
        Object mergedElementsArray = mergeArrays(currentElementsArray, newElementsArray);
        setDexElementsArray(getDexPathList(systemClassLoader), mergedElementsArray);
    }

    private void addNewClassLoaderToSystemClassLoaderPreBaseDex(DexClassLoader newClassLoader, PathClassLoader systemClassLoader) throws IllegalAccessException, NoSuchFieldException, IllegalArgumentException {
        try {
            newClassLoader.loadClass("foo");
        } catch (ClassNotFoundException e) {
        }
        setField(systemClassLoader, PathClassLoader.class, "mPaths", mergeArrayAndScalar(getField(systemClassLoader, PathClassLoader.class, "mPaths"), getField(newClassLoader, DexClassLoader.class, "mRawDexPath")));
        setField(systemClassLoader, PathClassLoader.class, "mFiles", mergeArrays(getField(systemClassLoader, PathClassLoader.class, "mFiles"), getField(newClassLoader, DexClassLoader.class, "mFiles")));
        setField(systemClassLoader, PathClassLoader.class, "mZips", mergeArrays(getField(systemClassLoader, PathClassLoader.class, "mZips"), getField(newClassLoader, DexClassLoader.class, "mZips")));
        setField(systemClassLoader, PathClassLoader.class, "mDexs", mergeArrays(getField(systemClassLoader, PathClassLoader.class, "mDexs"), getField(newClassLoader, DexClassLoader.class, "mDexs")));
    }

    @TargetApi(14)
    private Object getDexPathList(BaseDexClassLoader classLoader) throws IllegalAccessException, NoSuchFieldException {
        return getField(classLoader, BaseDexClassLoader.class, "pathList");
    }

    private Object getDexElementsArray(Object dexPathList) throws IllegalAccessException, NoSuchFieldException {
        return getField(dexPathList, dexPathList.getClass(), "dexElements");
    }

    private void setDexElementsArray(Object dexPathList, Object newElementArray) throws IllegalAccessException, NoSuchFieldException, IllegalArgumentException {
        setField(dexPathList, dexPathList.getClass(), "dexElements", newElementArray);
    }

    private Object mergeArrays(Object array1, Object array2) throws ArrayIndexOutOfBoundsException, IllegalArgumentException, NegativeArraySizeException {
        Class<?> arrayClass = array1.getClass();
        Class<?> itemClass = arrayClass.getComponentType();
        int array1Size = Array.getLength(array1);
        int array2Size = Array.getLength(array2);
        int newSize = array1Size + array2Size;
        Object newArray = Array.newInstance(itemClass, newSize);
        for (int i = 0; i < newSize; i++) {
            if (i < array1Size) {
                Array.set(newArray, i, Array.get(array1, i));
            } else {
                Array.set(newArray, i, Array.get(array2, i - array1Size));
            }
        }
        return newArray;
    }

    private Object mergeArrayAndScalar(Object array, Object scalar) throws ArrayIndexOutOfBoundsException, IllegalArgumentException, NegativeArraySizeException {
        Class<?> arrayClass = array.getClass();
        Class<?> itemClass = arrayClass.getComponentType();
        int array1Size = Array.getLength(array);
        int newSize = array1Size + 1;
        Object newArray = Array.newInstance(itemClass, newSize);
        for (int i = 0; i < newSize; i++) {
            if (i < array1Size) {
                Array.set(newArray, i, Array.get(array, i));
            } else {
                Array.set(newArray, i, scalar);
            }
        }
        return newArray;
    }

    private Object getField(Object object, Class<?> clazz, String fieldName) throws IllegalAccessException, NoSuchFieldException {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(object);
    }

    private void setField(Object object, Class<?> clazz, String fieldName, Object fieldValue) throws IllegalAccessException, NoSuchFieldException, IllegalArgumentException {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(object, fieldValue);
    }
}
