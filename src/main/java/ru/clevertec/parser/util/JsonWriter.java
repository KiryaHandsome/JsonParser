package ru.clevertec.parser.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

import static ru.clevertec.parser.util.PrimitiveUtil.*;

public class JsonWriter {

    private StringBuilder json;


    public String writeObject(Object object) throws IllegalAccessException {
        json = new StringBuilder();
        return serialize(object);
    }

    private String serialize(Object object) throws IllegalAccessException {
        Class<?> clazz = object.getClass();
        object.getClass();
        if (isWrapperType(clazz) && clazz != Character.class) {
            json.append(object);
        } else if (clazz == String.class || clazz.isEnum()) {
            json.append("\"" + object + "\"");
        } else if (clazz == Character.class) {
            json.append("\"" + getReplacementChar((char)object) + "\"");
        } else {
            serializeReference(object);
        }
        return json.toString();
    }

    private void serializeReference(Object object) throws IllegalAccessException {
        Class<?> clazz = object.getClass();
        if (Collection.class.isAssignableFrom(clazz)) {
            serializeCollection(object);
        } else if(Map.class.isAssignableFrom(clazz)) {
            serializeMap(object);
        } else if(clazz.isArray()) {
            serializeArray(object);
        } else {
            serializeObject(object);
        }
    }

    private void serializeObject(Object object) throws IllegalAccessException {
        Class<?> clazz = object.getClass();
        List<Field> allFields = new ArrayList<>(Arrays.stream(clazz.getDeclaredFields()).toList());
        Class<?> currentClazz = clazz.getSuperclass();
        while(currentClazz != Object.class) {
            allFields.addAll(List.of(currentClazz.getDeclaredFields()));
            currentClazz = currentClazz.getSuperclass();
        }
        allFields.forEach(f -> f.setAccessible(true));
        json.append(OPENING_BRACE);
        for (Field field : allFields) {
            if (field.get(object) != null && !Modifier.isTransient(field.getModifiers())) {
                json.append("\"" + field.getName() + "\":");
                serialize(field.get(object));
                json.append(COMMA);
            }
        }
        removeLastComma();
        json.append(CLOSING_BRACE);
    }

    private void serializeCollection(Object object) throws IllegalAccessException {
        json.append(OPENING_SQUARE_BRACKET);
        Collection<?> collection = (Collection<?>) object;
        for (Object el : collection) {
            serialize(el);
            json.append(COMMA);
        }
        removeLastComma();
        json.append(CLOSING_SQUARE_BRACKET);
    }

    private void serializeMap(Object object) throws IllegalAccessException {
        json.append(OPENING_BRACE);
        Map<?, ?> map = (Map<?, ?>) object;
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            serialize(entry.getKey());
            json.append(":");
            serialize(entry.getValue());
            json.append(COMMA);
        }
        removeLastComma();
        json.append(CLOSING_BRACE);
    }

    private void serializeArray(Object object) throws IllegalAccessException {
        json.append(OPENING_SQUARE_BRACKET);
        Object[] array = toObjectArray(object);
        for (Object el : array) {
            serialize(el);
            json.append(COMMA);
        }
        removeLastComma();
        json.append(CLOSING_SQUARE_BRACKET);
    }

    private void removeLastComma() {
        if(json.charAt(json.length() - 1) == COMMA) {
            json.setLength(json.length() - 1);
        }
    }
}
