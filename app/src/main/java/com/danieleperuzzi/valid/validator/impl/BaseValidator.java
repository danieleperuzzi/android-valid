package com.danieleperuzzi.valid.validator.impl;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.AnyThread;
import android.support.annotation.MainThread;
import android.support.annotation.WorkerThread;

import com.danieleperuzzi.valid.constraint.Constraint;
import com.danieleperuzzi.valid.validator.Validable;
import com.danieleperuzzi.valid.validator.Validator;
import com.danieleperuzzi.valid.validator.ValidatorOptions;
import com.danieleperuzzi.valid.validator.ValidatorResult;
import com.danieleperuzzi.valid.validator.ValidatorStatus;

import java.util.List;

public abstract class BaseValidator implements Validator {

    private Looper mainLooper = Looper.getMainLooper();
    private Handler mainThreadHandler;

    public BaseValidator() {
        mainThreadHandler = new Handler(mainLooper);
    }

    @MainThread
    public void validate(Validable<?> value, ValidatorOptions options, Validator.Callback callback) {
        if (currentThreadIsMainThread()) {
            startValidation(value, options, callback);
        } else {
            throw new RuntimeException("validation must start on the main thread");
        }
    }

    @MainThread
    public abstract void startValidation(Validable<?> value, ValidatorOptions options, Validator.Callback callback);

    @AnyThread
    public void doValidation(Validable<?> value, ValidatorOptions options, Validator.Callback callback) {
        List<Constraint> constraints = options.getConstraints();
        ValidatorResult result = null;

        for (Constraint constraint : constraints) {
            result = constraint.evaluate(value);

            if (result.status == ValidatorStatus.NOT_VALIDATED || (result.status == ValidatorStatus.VALIDATED && constraint.shouldBreakValidateChain(value))) {
                postResult(value, result, callback);
                return;
            }
        }

        //if we reach this point we can say that all the constraints had evaluated to true
        postResult(value, result, callback);
    }


    private void postResult(Validable<?> value, ValidatorResult result, Validator.Callback callback) {
        if (currentThreadIsMainThread()) {
            triggerListener(value, result, callback);
        } else {
            runOnMainThread(value, result, callback);
        }
    }

    @AnyThread
    private boolean currentThreadIsMainThread() {
        if (Build.VERSION.SDK_INT >=  Build.VERSION_CODES.M) {
            return mainLooper.isCurrentThread();
        }
        return Thread.currentThread().equals(mainLooper.getThread());
    }

    @WorkerThread
    private void runOnMainThread(Validable<?> value, ValidatorResult result, Validator.Callback callback) {
        Runnable callbackRunnable = () -> {
            triggerListener(value, result, callback);
        };

        mainThreadHandler.post(callbackRunnable);
    }

    @MainThread
    private void triggerListener(Validable<?> value, ValidatorResult result, Validator.Callback callback) {
        if (callback != null) {
            callback.status(value, result);
        }
    }
}
