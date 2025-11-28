package com.example.demo.entities;

public enum Unit {
    GRAM("g"),
    KILOGRAM("kg"),
    OUNCE("oz"),
    POUND("lb"),
    MILLILITER("ml"),
    LITER("l"),
    DECILITER("dl"),
    TEASPOON("tsp"),
    TABLESPOON("tbsp"),
    GALLON("gallon");

    private final String selectedUnit;

    Unit(String selectedUnit) {
        this.selectedUnit = selectedUnit;
    }

    public String getSelectedUnit() {
        return selectedUnit;
    }
}