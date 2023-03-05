package ru.clevertec.parser.util;

import org.apache.commons.lang3.ArrayUtils;

import java.util.HashMap;
import java.util.Map;

public class PrimitiveUtil {
    public static final char OPENING_BRACE = '{';
    public static final char CLOSING_BRACE = '}';
    public static final char OPENING_SQUARE_BRACKET = '[';
    public static final char CLOSING_SQUARE_BRACKET = ']';
    public static final char COMMA = ',';
    private static Map<Character, String> charRepresentation = new HashMap<>();

    static {
        for (int i = 0; i <= 0x1f; i++) {
            charRepresentation.put((char)i, String.format("\\u%04x", i));
        }
        charRepresentation.put('"',"\\\"");
        charRepresentation.put('\\', "\\\\");
        charRepresentation.put('\t', "\\t");
        charRepresentation.put('\b', "\\b");
        charRepresentation.put('\f', "\\f");
        charRepresentation.put('\n', "\\n");
        charRepresentation.put('\r', "\\r");
        charRepresentation.put('\u2028', "\\u2028");
        charRepresentation.put('\u2029', "\\u2029");
        //HTML safe replaces
        charRepresentation.put('<', "\\u003c");
        charRepresentation.put('>', "\\u003e");
        charRepresentation.put('&', "\\u0026");
        charRepresentation.put('=', "\\u003d");
        charRepresentation.put('\'',  "\\u0027");
    }
    public static boolean isWrapperType(Class<?> type) {
        return type == Integer.class
                || type == Float.class
                || type == Byte.class
                || type == Double.class
                || type == Long.class
                || type == Character.class
                || type == Boolean.class
                || type == Short.class
                || type == Void.class;
    }
    
    public static Object[] toObjectArray(Object object) {
        Class<?> clazz = object.getClass();
        if (clazz == int[].class) {
            return ArrayUtils.toObject((int[]) object);
        } else if (clazz == double[].class) {
            return ArrayUtils.toObject((double[]) object);
        } else if (clazz == float[].class) {
            return ArrayUtils.toObject((float[]) object);
        } else if (clazz == char[].class) {
            return ArrayUtils.toObject((char[]) object);
        } else if (clazz == boolean[].class) {
            return ArrayUtils.toObject((boolean[]) object);
        } else if (clazz == long[].class) {
            return ArrayUtils.toObject((long[]) object);
        } else if (clazz == short[].class) {
            return ArrayUtils.toObject((short[]) object);
        } else if (clazz == byte[].class) {
            return ArrayUtils.toObject((byte[]) object);
        } else {
            return (Object[]) object;
        }
    }

    public static String getReplacementChar(char ch) {
        if(charRepresentation.containsKey(ch)) {
            return charRepresentation.get(ch);
        }
        return String.valueOf(ch);
    }
}
