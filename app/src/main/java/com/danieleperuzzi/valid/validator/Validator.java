package com.danieleperuzzi.valid.validator;

public interface Validator {

    void validate(Validable<?> value, ValidatorOptions options, Callback callback);

    interface Callback {
        void status(Validable<?> value, ValidatorResult result);
    }
}
