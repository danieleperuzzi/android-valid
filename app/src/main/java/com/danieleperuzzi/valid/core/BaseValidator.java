/*
 * Copyright 2019 Daniele Peruzzi. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.danieleperuzzi.valid.core;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.AnyThread;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import java.util.List;

/**
 * This is the main Class implementing {@link Validator} interface,
 * it is never instantiated directly instead it must be subclassed and
 * then instantiated.
 *
 * <p>It provides the main structure of the validation algorithm</p>
 *
 * <p>It also notifies an optional {@link ValidatorObserver} about
 * the result of the validation of a particular {@link Validable}</p>
 */
public abstract class BaseValidator implements Validator {

    private final Looper mainLooper = Looper.getMainLooper();
    private final Handler mainThreadHandler = new Handler(mainLooper);

    protected BaseValidator() {
    }

    /**
     * This method simply invokes the one below with the {@link ValidatorObserver}
     * at null in case is not provided
     *
     * @param value    the {@link Validable} Object that is going to be validated
     * @param options  the {@link ValidatorOptions} that the value should all match
     *                 to be positive validated
     * @param callback {@link Callback} used to post the validation result
     */
    @MainThread
    public final void validate(Validable<?> value, ValidatorOptions options, Callback callback) {
        validate(value, options, null, callback);
    }

    /**
     * This method is the main entry point of the validation process, it takes care
     * of the fact that the validation must start on the main thread otherwise
     * it throws a {@link RuntimeException}
     *
     * @param value     the {@link Validable} Object that is going to be validated
     * @param options   the {@link ValidatorOptions} that the value should all match
     *                  to be positive validated
     * @param observer  the optional {@link ValidatorObserver}
     * @param callback  {@link Callback} used to post the validation result
     */
    @MainThread
    public final void validate(Validable<?> value, ValidatorOptions options, @Nullable ValidatorObserver observer, Validator.Callback callback) {
        if (currentThreadIsMainThread()) {
            startValidation(value, options, observer, callback);
        } else {
            throw new RuntimeException("validation must start on the main thread");
        }
    }

    /**
     * The main purpose of this method is to give the possibility to implement custom validation
     * logic such as threading logic
     *
     * @param value     the {@link Validable} Object that is going to be validated
     * @param options   the {@link ValidatorOptions} that the value should all match
     *                  to be positive validated
     * @param observer  the optional {@link ValidatorObserver}
     * @param callback  {@link Callback} used to post the validation result
     */
    @MainThread
    protected abstract void startValidation(Validable<?> value, ValidatorOptions options, @Nullable ValidatorObserver observer, Validator.Callback callback);

    /**
     * The main validation algorithm. We get all the constraints that the {@link Validable} must
     * be compliant to one by one as the {@link ValidatorOptions} provides us following a
     * priority order declared by the {@link Constraint} itself.
     *
     * <p>The first time a {@link Constraint} is not satisfied we declare validation failed</p>
     *
     * <p>Only if the {@link Validable} is compliant to all the {@link Constraint} we can
     * declare it validated</p>
     *
     * <p>There is also a special case, when a {@link Validable} satisfies a {@link Constraint}
     * but it's not the last one. In this case every {@link Constraint} gives us the chance
     * to determine if it is enough to declare the entire {@link Validable} Object validated.</p>
     *
     * @param value     the {@link Validable} Object that is going to be validated
     * @param options   the {@link ValidatorOptions} that the value should all match
     *                  to be positive validated
     * @param observer  the optional {@link ValidatorObserver}
     * @param callback  {@link Callback} used to post the validation result
     */
    @AnyThread
    protected final void doValidation(Validable<?> value, ValidatorOptions options, @Nullable ValidatorObserver observer, Validator.Callback callback) {
        List<Constraint<?, ?>> constraints = options.getConstraints();
        ConstraintResult constraintResult = null;

        for (Constraint<?, ?> constraint : constraints) {
            constraintResult = constraint.evaluate(value);

            if (constraintResult.status == ValidableStatus.NOT_VALID || (constraintResult.status == ValidableStatus.VALID && constraint.shouldBreakValidationChain(value))) {
                postResult(value, new ValidatorResult(constraintResult), observer, callback);
                return;
            }
        }

        //if we reach this point we can say that all the constraints had evaluated to true
        postResult(value, new ValidatorResult(constraintResult), observer, callback);
    }

    /**
     * This method is only used by {@link #doValidation(Validable, ValidatorOptions, ValidatorObserver, Callback)} method
     * to post the result of the validation.
     *
     * <p>It has simply logic to determine if the validation process has been run on the main thread
     * or on another thread because the callback is always invoked on the main thread</p>
     *
     * @param value    the {@link Validable} Object that has been validated
     * @param result   the {@link ValidatorResult} of the validation
     * @param observer  the optional {@link ValidatorObserver}
     * @param callback {@link Callback} used to post the validation result
     */
    private void postResult(Validable<?> value, ValidatorResult result, @Nullable ValidatorObserver observer, Validator.Callback callback) {
        if (currentThreadIsMainThread()) {
            triggerListener(value, result, observer, callback);
        } else {
            runOnMainThread(value, result, observer, callback);
        }
    }

    /**
     * Convenient method to know if I am on the main thread or on another thread
     *
     * @return  true if I am on the main thread otherwise false
     */
    @AnyThread
    private boolean currentThreadIsMainThread() {
        if (Build.VERSION.SDK_INT >=  Build.VERSION_CODES.M) {
            return mainLooper.isCurrentThread();
        }
        return Thread.currentThread().equals(mainLooper.getThread());
    }

    /**
     * If the validation process has been run on a worker thread than we need to invoke
     * the callback on the main thread and this is what this method actually does.
     *
     * @param value    the {@link Validable} Object that has been validated
     * @param result   the {@link ValidatorResult} of the validation
     * @param observer  the optional {@link ValidatorObserver}
     * @param callback {@link Callback} used to post the validation result
     */
    @WorkerThread
    private void runOnMainThread(Validable<?> value, ValidatorResult result, @Nullable ValidatorObserver observer, Validator.Callback callback) {
        Runnable callbackRunnable = () -> {
            triggerListener(value, result, observer, callback);
        };

        mainThreadHandler.post(callbackRunnable);
    }

    /**
     * The method that eventually invokes the callback and post the result of the validation
     *
     * <p>If a {@link ValidatorObserver} is provided it notifies to it the validation result</p>
     *
     * @param value    the {@link Validable} Object that has been validated
     * @param result   the {@link ValidatorResult} of the validation
     * @param observer  the optional {@link ValidatorObserver}
     * @param callback {@link Callback} used to post the validation result
     */
    @MainThread
    private void triggerListener(Validable<?> value, ValidatorResult result, @Nullable ValidatorObserver observer, Validator.Callback callback) {
        if (callback != null) {
            callback.status(value, result);
        }

        if (observer != null) {
            observer.notify(value, result);
        }
    }
}
