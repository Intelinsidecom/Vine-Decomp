package com.bluelinelabs.logansquare;

import com.bluelinelabs.logansquare.typeconverters.DefaultCalendarConverter;
import com.bluelinelabs.logansquare.typeconverters.DefaultDateConverter;
import com.bluelinelabs.logansquare.typeconverters.TypeConverter;
import com.fasterxml.jackson.core.JsonFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: classes.dex */
public class LoganSquare {
    public static final JsonFactory JSON_FACTORY;
    private static final Map<Class, JsonMapper> OBJECT_MAPPERS = new ConcurrentHashMap();
    private static final Map<Class, TypeConverter> TYPE_CONVERTERS = new HashMap();

    static {
        registerTypeConverter(Date.class, new DefaultDateConverter());
        registerTypeConverter(Calendar.class, new DefaultCalendarConverter());
        JSON_FACTORY = new JsonFactory();
    }

    public static <E> E parse(InputStream inputStream, Class<E> cls) throws IOException {
        return (E) mapperFor(cls).parse(inputStream);
    }

    public static <E> E parse(String str, Class<E> cls) throws IOException {
        return (E) mapperFor(cls).parse(str);
    }

    public static <E> List<E> parseList(InputStream is, Class<E> jsonObjectClass) throws IOException {
        return mapperFor(jsonObjectClass).parseList(is);
    }

    public static <E> List<E> parseList(String jsonString, Class<E> jsonObjectClass) throws IOException {
        return mapperFor(jsonObjectClass).parseList(jsonString);
    }

    public static <E> Map<String, E> parseMap(InputStream is, Class<E> jsonObjectClass) throws IOException {
        return mapperFor(jsonObjectClass).parseMap(is);
    }

    public static <E> Map<String, E> parseMap(String jsonString, Class<E> jsonObjectClass) throws IOException {
        return mapperFor(jsonObjectClass).parseMap(jsonString);
    }

    public static <E> String serialize(E object) throws IOException {
        return mapperFor(object.getClass()).serialize((JsonMapper) object);
    }

    public static <E> void serialize(E object, OutputStream os) throws IOException {
        mapperFor(object.getClass()).serialize((JsonMapper) object, os);
    }

    public static <E> String serialize(List<E> list, Class<E> jsonObjectClass) throws IOException {
        return mapperFor(jsonObjectClass).serialize((List) list);
    }

    public static <E> void serialize(List<E> list, OutputStream os, Class<E> jsonObjectClass) throws IOException {
        mapperFor(jsonObjectClass).serialize((List) list, os);
    }

    public static <E> String serialize(Map<String, E> map, Class<E> jsonObjectClass) throws IOException {
        return mapperFor(jsonObjectClass).serialize((Map) map);
    }

    public static <E> void serialize(Map<String, E> map, OutputStream os, Class<E> jsonObjectClass) throws IOException {
        mapperFor(jsonObjectClass).serialize((Map) map, os);
    }

    public static <E> JsonMapper<E> mapperFor(Class<E> cls) throws ClassNotFoundException, NoSuchMapperException {
        JsonMapper<E> mapper = OBJECT_MAPPERS.get(cls);
        if (mapper == null) {
            try {
                Class<?> mapperClass = Class.forName(cls.getName() + Constants.MAPPER_CLASS_SUFFIX);
                JsonMapper<E> mapper2 = (JsonMapper) mapperClass.newInstance();
                OBJECT_MAPPERS.put(cls, mapper2);
                return mapper2;
            } catch (Exception e) {
                throw new NoSuchMapperException(cls, e);
            }
        }
        return mapper;
    }

    public static <E> TypeConverter<E> typeConverterFor(Class<E> cls) throws NoSuchTypeConverterException {
        TypeConverter<E> typeConverter = TYPE_CONVERTERS.get(cls);
        if (typeConverter == null) {
            throw new NoSuchTypeConverterException(cls);
        }
        return typeConverter;
    }

    public static <E> void registerTypeConverter(Class<E> cls, TypeConverter<E> converter) {
        TYPE_CONVERTERS.put(cls, converter);
    }
}
