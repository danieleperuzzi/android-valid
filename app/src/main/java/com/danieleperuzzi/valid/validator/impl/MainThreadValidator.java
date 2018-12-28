package com.danieleperuzzi.valid.validator.impl;

import android.support.annotation.MainThread;

import com.danieleperuzzi.valid.validator.Validable;
import com.danieleperuzzi.valid.validator.ValidatorOptions;

public class MainThreadValidator extends BaseValidator {

    @Override
    @MainThread
    public void startValidation(Validable<?> value, ValidatorOptions options, Callback callback) {
        doValidation(value, options, callback);
    }
}
