package ru.clevertec.test_entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassWithArrays {
    private int[][][] ints;
    private char[][] chars;
    private boolean[][] booleans;
    private byte[][] bytes;
    private transient long[][] longs;
    private transient double[][] doubles;
    private float[][] floats;
    private short[][][] shorts;
    private Integer[][][] integers;
    private String[][] strings;
}
