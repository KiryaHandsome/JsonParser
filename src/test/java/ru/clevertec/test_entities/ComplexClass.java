package ru.clevertec.test_entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComplexClass {
    private List<ComplexClass> list;
    private Integer intWrapper;
    private char character;
    private int[][] ints;
}
