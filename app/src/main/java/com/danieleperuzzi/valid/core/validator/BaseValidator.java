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

package com.danieleperuzzi.valid.core.validator;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.AnyThread;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.danieleperuzzi.valid.core.constraint.Constraint;
import com.danieleperuzzi.valid.core.constraint.SortedConstraintSet;
import com.danieleperuzzi.valid.core.Validable;
import com.danieleperuzzi.valid.core.Validator;

import java.util.concurrent.Executor;

/**
 * This is the base Class implementing {@link Validator} interface
 * providing the main structure of the validation algorithm.
 *
 * <p>it is possible to instantiate it directly just passing an executor
 * implementation or just use the provided subclasses:</p>
 *
 * <ul>
 *     <li>{@link com.danieleperuzzi.valid.core.validator.impl.MainThreadValidator}</li>
 *     <li>{@link com.danieleperuzzi.valid.core.validator.impl.SingleThreadValidator}</li>
 *     <li>{@link com.danieleperuzzi.valid.core.validator.impl.PoolThreadValidator}</li>
 * </ul>
 *
 * <p>It also notifies an optional {@link ValidatorObserver} about
 * the result of the validation of a particular {@link Validable}</p>
 */
public class BaseValidator implements Validator {

    private Executor executor;
    private ValidatorAlgorithmFactory factory;
    private Looper mainThreadLooper;
    private Handler mainThreadHandler;

    /**
     * @param executor              the Executor used to run the validation algorithm
     * @param factory               the factory that provides validator algorithms
     * @param mainThreadHandler     the Handler of the main Thread
     */
    public BaseValidator(Executor executor, ValidatorAlgorithmFactory factory, Handler mainThreadHandler) {
        this.executor = executor;
        this.factory = factory;
        this.mainThreadHandler = mainThreadHandler;

        mainThreadLooper = mainThreadHandler.getLooper();
    }

    /**
     * This method simply invokes the one below with the {@link ValidatorObserver}
     * at null in case is not provided.
     *
     * @param value      the {@link Validable} Object that is going to be validated
     * @param constraint the {@link Constraint} used to check the validable
     * @param callback   {@link Callback} used to post the validation result
     */
    @MainThread
    public final void validate(Validable<?> value, Constraint<?, ?> constraint, Callback callback) {
        startValidation(value, constraint, null, null, callback);
    }

    /**
     * Convenience method used when the {@link Validable} is checked against only one
     * constraint as opposed to {@link #validate(Validable, SortedConstraintSet, ValidatorObserver, Callback)}
     *
     * @param value      the {@link Validable} Object that is going to be validated
     * @param constraint the {@link Constraint} used to check the validable
     * @param observer   the optional {@link ValidatorObserver}
     * @param callback   {@link Callback} used to post the validation result
     */
    @MainThread
    public final void validate(Validable<?> value, Constraint<?, ?> constraint, @Nullable ValidatorObserver observer, Callback callback) {
        startValidation(value, constraint, null, observer, callback);
    }

    /**
     * This method simply invokes the one below with the {@link ValidatorObserver}
     * at null in case is not provided.
     *
     * @param value             the {@link Validable} Object that is going to be validated
     * @param constraintSet     the {@link SortedConstraintSet} that the value should all match
     *                          to be positive validated
     * @param callback          {@link Callback} used to post the validation result
     */
    @MainThread
    public final void validate(Validable<?> value, SortedConstraintSet constraintSet, Callback callback) {
        startValidation(value, null, constraintSet, null, callback);
    }

    /**
     * Invokes the validation.
     *
     * @param value             the {@link Validable} Object that is going to be validated
     * @param constraintSet     the {@link SortedConstraintSet} that the value should all match
     *                          to be positive validated
     * @param observer          the optional {@link ValidatorObserver}
     * @param callback          {@link Callback} used to post the validation result
     */
    @MainThread
    public final void validate(Validable<?> value, SortedConstraintSet constraintSet, @Nullable ValidatorObserver observer, Validator.Callback callback) {
        startValidation(value, null, constraintSet, observer, callback);
    }

    /**
     * This method is the main entry point of the validation process, it takes care
     * of the fact that the validation must start on the main thread otherwise
     * it throws a {@link RuntimeException}.
     *
     * <p>First it uses a {@link ValidatorAlgorithmFactory} to retrieve a new algorithm
     * based on the input data.</p>
     *
     * <p>See:</p>
     *
     * <ul>
     *     <li>{@link com.danieleperuzzi.valid.core.validator.impl.ValidatorAlgorithm}</li>
     *     <li>{@link com.danieleperuzzi.valid.core.validator.impl.SimpleValidatorAlgorithm}</li>
     * </ul>
     *
     * <p>It uses an executor to run the validation algorithm.</p>
     *
     * @param value             the {@link Validable} Object that is going to be validated
     * @param constraint        the {@link Constraint} that the value should match
     * @param constraintSet     the {@link SortedConstraintSet} that the value should all match
     *                          to be positive validated
     * @param observer          the optional {@link ValidatorObserver}
     * @param callback          {@link Callback} used to post the validation result
     */
    @MainThread
    private void startValidation(Validable<?> value, Constraint<?, ?> constraint, SortedConstraintSet constraintSet, @Nullable ValidatorObserver observer, Validator.Callback callback) {
        ValidatorAlgorithmData data = new ValidatorAlgorithmData(value, constraint, constraintSet, observer, callback);
        BaseValidatorAlgorithm validatorAlgorithm = factory.createValidatorAlgorithm(data, this::postResult);

        if (currentThreadIsMainThread() && validatorAlgorithm != null) {
            executor.execute(validatorAlgorithm);

        } else if (validatorAlgorithm == null) {
            throw new RuntimeException("unable to find any suitable validator algorithm");
        } else {
            throw new RuntimeException("validation must start on the main thread");
        }
    }

    /**
     * This method is invoked when any validator algorithm terminates its computation to post the result
     *
     * <p>It has simply logic to determine if the validation process has been run on the main thread
     * or on another thread because the callback is always invoked on the main thread</p>
     *
     * @param result    the result of the computation done by any validator algorithm
     */
    private void postResult(ValidatorAlgorithmResult result) {
        if (currentThreadIsMainThread()) {
            triggerListener(result);
        } else {
            runOnMainThread(result);
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
            return mainThreadLooper.isCurrentThread();
        }
        return Thread.currentThread().equals(mainThreadLooper.getThread());
    }

    /**
     * If the validation process has been run on a worker thread than we need to invoke
     * the callback on the main thread and this is what this method actually does.
     *
     * @param result    the result of the computation done by any validator algorithm
     */
    @WorkerThread
    private void runOnMainThread(ValidatorAlgorithmResult result) {
        Runnable callbackRunnable = () -> {
            triggerListener(result);
        };

        mainThreadHandler.post(callbackRunnable);
    }

    /**
     * The method that eventually invokes the callback and post the result of the validation
     *
     * <p>If a {@link ValidatorObserver} is provided it notifies to it the validation result</p>
     *
     * @param result    the result of the computation done by any validator algorithm
     */
    @MainThread
    private void triggerListener(ValidatorAlgorithmResult result) {
        if (result != null) {
            Validator.Callback callback = result.callback;
            ValidatorObserver observer = result.observer;

            if (callback != null) {
                callback.status(result.value, result.result);
            }

            if (observer != null) {
                observer.notify(result.value, result.result);
            }
        }
    }
}