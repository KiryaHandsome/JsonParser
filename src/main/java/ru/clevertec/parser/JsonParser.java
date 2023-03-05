package ru.clevertec.parser;

import ru.clevertec.parser.util.JsonReader;
import ru.clevertec.parser.util.JsonWriter;


public class JsonParser {
    private JsonWriter writer;
    private JsonReader reader;

    public String toJson(Object object) throws IllegalAccessException {
        writer = new JsonWriter();
        return writer.writeObject(object);
    }

    public <T> T fromJson(String json, Class<T> type) throws Exception {
        reader = new JsonReader();
        return reader.readObject(json, type);
    }
}

