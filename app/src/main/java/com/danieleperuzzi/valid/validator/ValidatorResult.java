package com.danieleperuzzi.valid.validator;

import android.support.annotation.Nullable;

public class ValidatorResult {

    public ValidatorStatus status;

    @Nullable
    public String validatorError;

    public ValidatorResult() {
    }
}
