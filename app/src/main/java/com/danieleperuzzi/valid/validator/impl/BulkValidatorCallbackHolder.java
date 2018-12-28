package com.danieleperuzzi.valid.validator.impl;

import android.support.annotation.MainThread;

import com.danieleperuzzi.valid.validator.BulkValidator;
import com.danieleperuzzi.valid.validator.BulkValidatorStatus;
import com.danieleperuzzi.valid.validator.Validable;
import com.danieleperuzzi.valid.validator.Validator;
import com.danieleperuzzi.valid.validator.ValidatorResult;
import com.danieleperuzzi.valid.validator.ValidatorStatus;

import java.util.HashMap;
import java.util.Map;

public class BulkValidatorCallbackHolder implements Validator.Callback {

    private int validableInstances;
    private int decrementableValidableInstances;
    private BulkValidator.Callback callback;

    private int validatedValidable = 0;
    private int notValidatedValidable = 0;

    private Map<Validable<?>, ValidatorResult> validableResults = new HashMap<>();

    public BulkValidatorCallbackHolder(int validableInstances, BulkValidator.Callback callback) {
        this.validableInstances = this.decrementableValidableInstances = validableInstances;
        this.callback = callback;
    }

    @MainThread
    public void status(Validable<?> value, ValidatorResult result) {
        decrementableValidableInstances--;

        if (result.status == ValidatorStatus.VALIDATED) {
            validatedValidable++;
        } else {
            notValidatedValidable++;
        }

        validableResults.put(value, result);

        if ((validatedValidable + notValidatedValidable) == validableInstances) {
            if (validatedValidable == validableInstances) {
                triggerListener(validableResults, BulkValidatorStatus.ALL_VALIDATED);
                return;
            }

            if (validatedValidable < validableInstances) {
                triggerListener(validableResults, BulkValidatorStatus.AT_LEAST_ONE_NOT_VALIDATED);
                return;
            }
        }
    }

    private void triggerListener(Map<Validable<?>, ValidatorResult> validableResults, BulkValidatorStatus status) {
        if (callback != null) {
            callback.status(validableResults, status);
        }
    }
}
