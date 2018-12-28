package com.danieleperuzzi.valid.validator.impl;

import com.danieleperuzzi.valid.validator.Validator;
import com.danieleperuzzi.valid.validator.ValidatorStatus;

public class ValidableMetaDataHolder {

    private ValidatorStatus currentStatus;
    private boolean shouldContributeToValidatorResultObserver;
    private Validator.Callback originalCallback;


    public ValidatorStatus getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(ValidatorStatus currentStatus) {
        this.currentStatus = currentStatus;
    }

    public boolean shouldContributeToValidatorResultObserver() {
        return shouldContributeToValidatorResultObserver;
    }

    public Validator.Callback getOriginalCallback() {
        return originalCallback;
    }

    public ValidableMetaDataHolder(ValidatorStatus currentStatus, boolean shouldContributeToValidatorResultObserver, Validator.Callback originalCallback) {
        this.currentStatus = currentStatus;
        this.shouldContributeToValidatorResultObserver = shouldContributeToValidatorResultObserver;
        this.originalCallback = originalCallback;
    }
}
