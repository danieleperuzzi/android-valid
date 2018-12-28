package com.danieleperuzzi.valid.validator.impl;

import android.support.annotation.MainThread;

import com.danieleperuzzi.valid.validator.Validable;
import com.danieleperuzzi.valid.validator.ValidatorOptions;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PoolThreadValidator extends BaseValidator {

    private Executor executor;

    public PoolThreadValidator() {
        int cpuNum = Runtime.getRuntime().availableProcessors();
        executor = Executors.newFixedThreadPool(cpuNum);
    }

    @Override
    @MainThread
    public void startValidation(Validable<?> value, ValidatorOptions options, Callback callback) {
        Runnable runnable = () -> {
            doValidation(value, options, callback);
        };

        executor.execute(runnable);
    }
}
