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

import android.support.annotation.Nullable;

import com.danieleperuzzi.valid.core.Validable;
import com.danieleperuzzi.valid.core.Validator;
import com.danieleperuzzi.valid.core.constraint.Constraint;
import com.danieleperuzzi.valid.core.constraint.SortedConstraintSet;

/**
 * Simple class to hold the input data for any validator algorithm
 */
public class ValidatorAlgorithmData {

    public Validable<?> value;
    public Constraint<?, ?> constraint;
    public SortedConstraintSet constraintSet;
    @Nullable public ValidatorObserver observer;
    public Validator.Callback callback;

    /**
     * @param value             the {@link Validable} Object that is going to be validated
     * @param constraint        the {@link Constraint} that the value should match to be
     *                          positive validated
     * @param constraintSet     the {@link SortedConstraintSet} that the value should all match
     *                          to be positive validated
     * @param observer          the optional {@link ValidatorObserver}
     * @param callback          {@link Validator.Callback} used to post the validation result
     */
    public ValidatorAlgorithmData(Validable<?> value, Constraint<?, ?> constraint, SortedConstraintSet constraintSet, @Nullable ValidatorObserver observer, Validator.Callback callback) {
        this.value = value;
        this.constraint = constraint;
        this.constraintSet = constraintSet;
        this.observer = observer;
        this.callback = callback;
    }
}
