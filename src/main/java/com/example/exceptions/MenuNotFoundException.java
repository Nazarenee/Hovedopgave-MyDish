package com.example.exceptions;

public class MenuNotFoundException extends RuntimeException {
    public MenuNotFoundException(Long id) {
        super("Menu not found with id: " + id);
    }
}