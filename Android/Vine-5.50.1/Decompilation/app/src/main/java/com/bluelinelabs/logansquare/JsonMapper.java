package com.bluelinelabs.logansquare;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public abstract class JsonMapper<T> {
    public abstract T parse(JsonParser jsonParser) throws IOException;

    public abstract void serialize(T t, JsonGenerator jsonGenerator, boolean z) throws IOException;

    public T parse(InputStream is) throws IOException {
        JsonParser jsonParser = LoganSquare.JSON_FACTORY.createParser(is);
        jsonParser.nextToken();
        return parse(jsonParser);
    }

    public T parse(byte[] byteArray) throws IOException {
        JsonParser jsonParser = LoganSquare.JSON_FACTORY.createParser(byteArray);
        jsonParser.nextToken();
        return parse(jsonParser);
    }

    public T parse(char[] charArray) throws IOException {
        JsonParser jsonParser = LoganSquare.JSON_FACTORY.createParser(charArray);
        jsonParser.nextToken();
        return parse(jsonParser);
    }

    public T parse(String jsonString) throws IOException {
        JsonParser jsonParser = LoganSquare.JSON_FACTORY.createParser(jsonString);
        jsonParser.nextToken();
        return parse(jsonParser);
    }

    public List<T> parseList(InputStream is) throws IOException {
        JsonParser jsonParser = LoganSquare.JSON_FACTORY.createParser(is);
        jsonParser.nextToken();
        return parseList(jsonParser);
    }

    public List<T> parseList(byte[] byteArray) throws IOException {
        JsonParser jsonParser = LoganSquare.JSON_FACTORY.createParser(byteArray);
        jsonParser.nextToken();
        return parseList(jsonParser);
    }

    public List<T> parseList(char[] charArray) throws IOException {
        JsonParser jsonParser = LoganSquare.JSON_FACTORY.createParser(charArray);
        jsonParser.nextToken();
        return parseList(jsonParser);
    }

    public List<T> parseList(String jsonString) throws IOException {
        JsonParser jsonParser = LoganSquare.JSON_FACTORY.createParser(jsonString);
        jsonParser.nextToken();
        return parseList(jsonParser);
    }

    public List<T> parseList(JsonParser jsonParser) throws IOException {
        List<T> list = new ArrayList<>();
        if (jsonParser.getCurrentToken() == JsonToken.START_ARRAY) {
            while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                list.add(parse(jsonParser));
            }
        }
        return list;
    }

    public Map<String, T> parseMap(InputStream is) throws IOException {
        JsonParser jsonParser = LoganSquare.JSON_FACTORY.createParser(is);
        jsonParser.nextToken();
        return parseMap(jsonParser);
    }

    public Map<String, T> parseMap(byte[] byteArray) throws IOException {
        JsonParser jsonParser = LoganSquare.JSON_FACTORY.createParser(byteArray);
        jsonParser.nextToken();
        return parseMap(jsonParser);
    }

    public Map<String, T> parseMap(char[] charArray) throws IOException {
        JsonParser jsonParser = LoganSquare.JSON_FACTORY.createParser(charArray);
        jsonParser.nextToken();
        return parseMap(jsonParser);
    }

    public Map<String, T> parseMap(String jsonString) throws IOException {
        JsonParser jsonParser = LoganSquare.JSON_FACTORY.createParser(jsonString);
        jsonParser.nextToken();
        return parseMap(jsonParser);
    }

    public Map<String, T> parseMap(JsonParser jsonParser) throws IOException {
        HashMap<String, T> map = new HashMap<>();
        while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
            String key = jsonParser.getText();
            jsonParser.nextToken();
            if (jsonParser.getCurrentToken() == JsonToken.VALUE_NULL) {
                map.put(key, null);
            } else {
                map.put(key, parse(jsonParser));
            }
        }
        return map;
    }

    public String serialize(T object) throws IOException {
        StringWriter sw = new StringWriter();
        JsonGenerator jsonGenerator = LoganSquare.JSON_FACTORY.createGenerator(sw);
        serialize(object, jsonGenerator, true);
        jsonGenerator.close();
        return sw.toString();
    }

    public void serialize(T object, OutputStream os) throws IOException {
        JsonGenerator jsonGenerator = LoganSquare.JSON_FACTORY.createGenerator(os);
        serialize(object, jsonGenerator, true);
        jsonGenerator.close();
    }

    public String serialize(List<T> list) throws IOException {
        StringWriter sw = new StringWriter();
        JsonGenerator jsonGenerator = LoganSquare.JSON_FACTORY.createGenerator(sw);
        serialize(list, jsonGenerator);
        jsonGenerator.close();
        return sw.toString();
    }

    public void serialize(List<T> list, OutputStream os) throws IOException {
        JsonGenerator jsonGenerator = LoganSquare.JSON_FACTORY.createGenerator(os);
        serialize(list, jsonGenerator);
        jsonGenerator.close();
    }

    public void serialize(List<T> list, JsonGenerator jsonGenerator) throws IOException {
        jsonGenerator.writeStartArray();
        for (T object : list) {
            if (object != null) {
                serialize(object, jsonGenerator, true);
            } else {
                jsonGenerator.writeNull();
            }
        }
        jsonGenerator.writeEndArray();
    }

    public String serialize(Map<String, T> map) throws IOException {
        StringWriter sw = new StringWriter();
        JsonGenerator jsonGenerator = LoganSquare.JSON_FACTORY.createGenerator(sw);
        serialize(map, jsonGenerator);
        jsonGenerator.close();
        return sw.toString();
    }

    public void serialize(Map<String, T> map, OutputStream os) throws IOException {
        JsonGenerator jsonGenerator = LoganSquare.JSON_FACTORY.createGenerator(os);
        serialize(map, jsonGenerator);
        jsonGenerator.close();
    }

    public void serialize(Map<String, T> map, JsonGenerator jsonGenerator) throws IOException {
        jsonGenerator.writeStartObject();
        for (Map.Entry<String, T> entry : map.entrySet()) {
            jsonGenerator.writeFieldName(entry.getKey());
            if (entry.getValue() == null) {
                jsonGenerator.writeNull();
            } else {
                serialize(entry.getValue(), jsonGenerator, true);
            }
        }
        jsonGenerator.writeEndObject();
    }
}
