package com.danieleperuzzi.valid.validator.impl;

import com.danieleperuzzi.valid.validator.Validable;

public class ValidableText implements Validable<String> {

    private String text;

    @Override
    public String getValue() {
        return text;
    }

    @Override
    public void setValue(String text) {
        this.text = text;
    }
}
