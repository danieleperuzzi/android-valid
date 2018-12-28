package com.danieleperuzzi.valid.validator;

public interface Validable<V> {
    V getValue();
    void setValue(V value);
}
