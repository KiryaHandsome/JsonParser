package ru.clevertec.test_entities;

import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode
public class Shop {
    private String name;
    private List<Product> products;

    public Shop(String name, List<Product> products) {
        this.name = name;
        this.products = products;
    }
}
