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
    CUP("cup"),
    FLUID_OUNCE("fl oz"),
    PINT("pint"),
    QUART("quart"),
    GALLON("gallon"),

    PIECE("piece"),
    PINCH("pinch"),
    TO_TASTE("to taste");

    private final String selectedUnit;

    Unit(String selectedUnit) {
        this.selectedUnit = selectedUnit;
    }

    public String getSelectedUnit() {
        return selectedUnit;
    }
}