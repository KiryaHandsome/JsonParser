package ru.clevertec.parser.util;

import com.google.gson.Gson;

public class JsonReader {
    //sorry, but I couldn't implement it on my own on time
    public <T> T readObject(String json, Class<T> type) {
        return new Gson().fromJson(json, type);
    }
}