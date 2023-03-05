package ru.clevertec.parser;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.parser.util.JsonReader;
import ru.clevertec.test_entities.*;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class JsonParserTest {
    private Gson gson;
    private JsonParser jsonParser;

    private ComplexClass complexClass;
    private ComplexClass cc;
    private ComplexClass cc1;
    private ClassWithArrays classWithArrays;

    @BeforeEach
    void setUp() {
        gson = new Gson();
        jsonParser = new JsonParser();
        cc = ComplexClass.builder()
                .character('1')
                .ints(new int[][]{{133, 11}, {111, 99, 9123}})
                .intWrapper(Integer.valueOf(999999))
                .build();
        cc1 = ComplexClass.builder()
                .character('&')
                .ints(new int[][]{{112, 11}, {222, 99, 888}})
                .intWrapper(Integer.valueOf(4))
                .build();
        complexClass = ComplexClass.builder()
                .list(List.of(cc))
                .build();
        initializeClassWithArrays();
    }

    private void initializeClassWithArrays() {
        classWithArrays = ClassWithArrays.builder()
                .ints(new int[][][]{
                        {{11, 11}, {222, 99, 888}},
                        {{123, 11}, {222, 99, 888}},
                        {{55, 11}, {11, 99, 888}}})
                .booleans(new boolean[][]{
                        {true, true, false},
                        {true, true, false}})
                .chars(new char[][]{
                        {'1', '*', '.'},
                        {'@', '#', '^'}
                })
                .doubles(new double[][] {
                        {19.0113, 123.123},
                        {778.144, 632712.304289}
                })
                .shorts(new short[][][]{
                        {{11, 22}, {22, 33}},
                        {{33, 99}, {77, 44}},
                        {{11, 1010}, {11, 55}}
                })
                .longs(new long[][]{
                        {123L, 88L},
                        {77L, 987654321L}
                })
                .bytes(new byte[][]{
                        {123, 88, 9}
                })
                .floats(new float[][]{
                        {19.0113f, 123.123f},
                        {778.144f, 632712.304289f}
                })
                .integers(new Integer[][][]{
                        {{11, 11}, {222, 99, 888}},
                        {{123, 11}, {222, 99, 888}},
                        {{55, 11}, {11, 99, 888}}}
                )
                .strings(new String[][]{
                        {"123", "world", "hello"},
                        {"999", "university", "bye"}
                })
                .build();
    }

    // TODO : make parametrized tests
    @Test
    void checkToJsonShouldParseObjectWithPrimitiveFields() throws IllegalAccessException {
        Product product = new Product("bam bam", 89.1, 123);
        assertThat(jsonParser.toJson(product)).isEqualTo(gson.toJson(product));
    }

    @Test
    void checkToJsonShouldParseListWithObjects() throws IllegalAccessException {
        Product product = new Product("bam bam", 89.1, 123);
        Product product1 = new Product("one two", 111.111, 999);
        List<Product> list = List.of(product, product1);
        assertThat(jsonParser.toJson(list)).isEqualTo(gson.toJson(list));
    }

    @Test
    void checkToJsonShouldParseObjectWithListOfObjects() throws IllegalAccessException {
        Product product = new Product("bam bam", 89.1, 123);
        Shop shop = new Shop("Clevertec Shop", List.of(product));
        assertThat(jsonParser.toJson(shop)).isEqualTo(gson.toJson(shop));
    }

    @Test
    void checkToJsonShouldParseMapOfPrimitives() throws IllegalAccessException {
        Map<String, Integer> map = new HashMap<>();
        map.put("Hello", 123);
        map.put("Bye", 111);
        map.put("aasdfasdf", 999990);
        assertThat(jsonParser.toJson(map)).isEqualTo(gson.toJson(map));
    }

    @Test
    void checkToJsonShouldParseList() throws IllegalAccessException {
        List<Product> list = List.of(
                new Product("a", 12.1, 123),
                new Product("b", 11.1, 99));
        assertThat(jsonParser.toJson(list)).isEqualTo(gson.toJson(list));
    }

    @Test
    void checkToJsonShouldParseArrayOfObjects() throws IllegalAccessException {
        ComplexClass[] complexClasses = {complexClass, cc, cc1};
        assertThat(jsonParser.toJson(complexClasses)).isEqualTo(gson.toJson(complexClasses));
    }

    @Test
    void checkToJsonShouldParseComplexClass() throws IllegalAccessException {
        assertThat(jsonParser.toJson(complexClass)).isEqualTo(gson.toJson(complexClass));
    }

    @Test
    void checkToJsonShouldParseAllSymbols() throws IllegalAccessException {
        int size = (int) Math.pow(2, 16);
        char[] charArray = new char[size];
        for(int i = 0; i < size; i++) {
            charArray[i] = (char)i;
        }
        String myJson = jsonParser.toJson(charArray);
        String[] mySymbols = myJson.substring(1, myJson.length() - 1).split(",");
        String googleJson = gson.toJson(charArray);
        String[] googleSymbols = googleJson.substring(1, googleJson.length() - 1).split(",");
        assertThat(myJson).isEqualTo(googleJson);
        for(int i = 0; i < mySymbols.length; i++) {
            assertThat(mySymbols[i]).isEqualTo(googleSymbols[i]);
        }
    }

    @Test
    void checkToJsonShouldParseClassWithArray() throws IllegalAccessException {
        assertThat(jsonParser.toJson(classWithArrays))
                .isEqualTo(gson.toJson(classWithArrays));
    }

    @Test
    void checkToJsonShouldParseClassWithEnum() throws IllegalAccessException {
        ClassWithEnum object = new ClassWithEnum(Days.MONDAY);
        assertThat(jsonParser.toJson(object))
                .isEqualTo(gson.toJson(object));
    }

    @Test
    void checkToJsonMultipleTimes() throws IllegalAccessException {
        ClassWithEnum object = new ClassWithEnum(Days.MONDAY);
        for(int i = 0; i < 3; i++) {
            assertThat(jsonParser.toJson(object))
                    .isEqualTo(gson.toJson(object));
        }
    }

    @Test
    void checkToJsonExtendedClass() throws IllegalAccessException {
        ExtendedClass object = new ExtendedClass();
        assertThat(jsonParser.toJson(object)).isEqualTo(gson.toJson(object));
    }
    @Test
    void checkFromJsonProduct() throws Exception {
        String json = gson.toJson(new Product("name", 123.01, 132));
        assertThat(jsonParser.fromJson(json, Product.class)).isEqualTo(gson.fromJson(json, Product.class));
    }
}