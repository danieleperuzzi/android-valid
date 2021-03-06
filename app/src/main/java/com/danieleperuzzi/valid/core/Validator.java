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

import android.support.annotation.Nullable;

import com.danieleperuzzi.valid.core.constraint.Constraint;
import com.danieleperuzzi.valid.core.constraint.SortedConstraintSet;
import com.danieleperuzzi.valid.core.validator.ValidatorObserver;
import com.danieleperuzzi.valid.core.validator.ValidatorResult;

/**
 * This interface represents the entry point of the whole validation process.
 */
public interface Validator {

    /**
     * This method starts the validation on a {@link Validable} Object comparing it
     * to the only {@link Constraint} provided and post the result using
     * the supplied {@link Callback}.
     *
     * <p>The only difference between this method and the {@link #validate(Validable, SortedConstraintSet, Callback)}
     * is that the latter can check the validable against multiple {@link Constraint}</p>
     *
     * @param value         the {@link Validable} Object that is going to be validated
     * @param constraint    the {@link Constraint} used to check the validable
     * @param callback      {@link Callback} used to post the validation result
     */
    void validate(Validable<?> value, Constraint<?, ?> constraint, Callback callback);

    /**
     * This method accepts one more parameter respect the one above and it is an
     * optional {@link ValidatorObserver} that is constantly informed about the
     * validation output of a certain {@link Validable} in order to know the
     * global state of a set of {@link Validable}.
     *
     * <p>The only difference between this method and the {@link #validate(Validable, SortedConstraintSet, ValidatorObserver, Callback)}
     * is that the latter can check the validable against multiple {@link Constraint}</p>
     *
     * @param value         the {@link Validable} Object that is going to be validated
     * @param constraint    the {@link Constraint} used to check the validable
     * @param observer      the optional {@link ValidatorObserver}
     * @param callback      {@link Callback} used to post the validation result
     */
    void validate(Validable<?> value, Constraint<?, ?> constraint, @Nullable ValidatorObserver observer, Callback callback);

    /**
     * This method starts the validation on a {@link Validable} Object accordingly
     * to the {@link SortedConstraintSet} provided and post the result using
     * the supplied {@link Callback}.
     *
     * @param value             the {@link Validable} Object that is going to be validated
     * @param constraintSet     the {@link SortedConstraintSet} that the value should all match
     *                          to be positive validated
     * @param callback          {@link Callback} used to post the validation result
     */
    void validate(Validable<?> value, SortedConstraintSet constraintSet, Callback callback);

    /**
     * This method accepts one more parameter respect the one above and it is an
     * optional {@link ValidatorObserver} that is constantly informed about the
     * validation output of a certain {@link Validable} in order to know the
     * global state of a set of {@link Validable}.
     *
     * @param value             the {@link Validable} Object that is going to be validated
     * @param constraintSet     the {@link SortedConstraintSet} that the value should all match
     *                          to be positive validated
     * @param observer          the optional {@link ValidatorObserver}
     * @param callback          {@link Callback} used to post the validation result
     */
    void validate(Validable<?> value, SortedConstraintSet constraintSet, @Nullable ValidatorObserver observer, Callback callback);

    /**
     * This interface is the callback itself, invoked by the Validator
     * when the validation process ends.
     */
    interface Callback {

        /**
         * The class that implements this method receives a reference to
         * the {@link Validable} Object that has been validated
         * and the {@link ValidatorResult} of the validation.
         *
         * @param value     the {@link Validable} Object that has been validated
         * @param result    the {@link ValidatorResult}
         */
        void status(Validable<?> value, ValidatorResult result);
    }
}
