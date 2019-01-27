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

package com.danieleperuzzi.valid.core.validator.impl;

import android.support.annotation.AnyThread;
import android.support.annotation.Nullable;

import com.danieleperuzzi.valid.core.Validable;
import com.danieleperuzzi.valid.core.Validator;
import com.danieleperuzzi.valid.core.constraint.Constraint;
import com.danieleperuzzi.valid.core.constraint.ConstraintResult;
import com.danieleperuzzi.valid.core.validator.BaseValidator;
import com.danieleperuzzi.valid.core.validator.BaseValidatorAlgorithm;
import com.danieleperuzzi.valid.core.validator.ValidatorAlgorithmCallback;
import com.danieleperuzzi.valid.core.validator.ValidatorAlgorithmData;
import com.danieleperuzzi.valid.core.validator.ValidatorAlgorithmResult;
import com.danieleperuzzi.valid.core.validator.ValidatorObserver;
import com.danieleperuzzi.valid.core.validator.ValidatorResult;

/**
 * Defines a simplified version of the {@link ValidatorAlgorithm} used when
 * comparing a {@link Validable} to only one {@link Constraint}
 */
public final class SimpleValidatorAlgorithm extends BaseValidatorAlgorithm {

    private Validable<?> value;
    private Constraint<?, ?> constraint;
    @Nullable private ValidatorObserver observer;
    private Validator.Callback callback;

    /**
     * @param data          the {@link ValidatorAlgorithmData} used to fed the validator algorithm
     * @param callback      callback to post the validation result to the {@link BaseValidator}
     */
    public SimpleValidatorAlgorithm(ValidatorAlgorithmData data, ValidatorAlgorithmCallback callback) {
        super(data, callback);

        this.value = data.value;
        this.constraint = data.constraint;
        this.observer = data.observer;
        this.callback = data.callback;
    }

    /**
     * Basic validation algorithm used when comparing a {@link Validable} to only one {@link Constraint}
     */
    @AnyThread
    public void run() {
        ConstraintResult constraintResult = constraint.evaluate(value);
        postResult(new ValidatorAlgorithmResult(value, new ValidatorResult(constraintResult), observer, callback));
    }
}
