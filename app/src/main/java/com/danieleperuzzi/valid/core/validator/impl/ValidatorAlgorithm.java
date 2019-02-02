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
import com.danieleperuzzi.valid.core.ValidableStatus;
import com.danieleperuzzi.valid.core.Validator;
import com.danieleperuzzi.valid.core.constraint.Constraint;
import com.danieleperuzzi.valid.core.constraint.ConstraintResult;
import com.danieleperuzzi.valid.core.constraint.SortedConstraintSet;
import com.danieleperuzzi.valid.core.validator.BaseValidator;
import com.danieleperuzzi.valid.core.validator.BaseValidatorAlgorithm;
import com.danieleperuzzi.valid.core.validator.ValidatorAlgorithmCallback;
import com.danieleperuzzi.valid.core.validator.ValidatorAlgorithmData;
import com.danieleperuzzi.valid.core.validator.ValidatorAlgorithmResult;
import com.danieleperuzzi.valid.core.validator.ValidatorObserver;
import com.danieleperuzzi.valid.core.validator.ValidatorResult;

import java.util.Set;

/**
 * Defines the main validator algorithm.
 */
public class ValidatorAlgorithm extends BaseValidatorAlgorithm {

    private Validable<?> value;
    private SortedConstraintSet constraintSet;
    @Nullable private ValidatorObserver observer;
    private Validator.Callback callback;

    /**
     * @param data          the {@link ValidatorAlgorithmData} used to fed the validator algorithm
     * @param callback      callback to post the validation result to the {@link BaseValidator}
     */
    public ValidatorAlgorithm(ValidatorAlgorithmData data, ValidatorAlgorithmCallback callback) {
        super(data, callback);

        this.value = data.value;
        this.constraintSet = data.constraintSet;
        this.observer = data.observer;
        this.callback = data.callback;
    }

    /**
     * The main validation algorithm. We get all the constraints that the {@link Validable} must
     * be compliant to one by one as the {@link SortedConstraintSet} provides us following a
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
     */
    @AnyThread
    public void run() {
        Set<Constraint<?, ?>> constraints = constraintSet.getConstraints();
        ConstraintResult constraintResult = null;

        for (Constraint<?, ?> constraint : constraints) {
            constraintResult = constraint.evaluate(value);

            if (constraintResult.status == ValidableStatus.NOT_VALID || (constraintResult.status == ValidableStatus.VALID && constraint.shouldStopValidation(value))) {
                postResult(new ValidatorAlgorithmResult(value, new ValidatorResult(constraintResult), observer, callback));
            }
        }

        //if we reach this point we can say that all the constraints had evaluated to true
        postResult(new ValidatorAlgorithmResult(value, new ValidatorResult(constraintResult), observer, callback));
    }
}
