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

import java.util.Map;

/**
 * Extend this class when the {@link Constraint} is unique inside the {@link ValidatorOptions}
 *
 * @param <V>   the {@link Validable} type
 * @param <C>   the Object that holds information against which the value is going
 *              to be validated
 */
public abstract class UniqueConstraint<V, C> extends Constraint<V, C> {

    protected UniqueConstraint(C constraint, int evaluationPriority, String error) {
        super(constraint, evaluationPriority, error);
    }

    protected UniqueConstraint(C constraint, int evaluationPriority, Map<String, String> errorMap) {
        super(constraint, evaluationPriority, errorMap);
    }

    protected abstract ConstraintResult evaluate(V value);

    /**
     * Because this {@link Constraint} is unique there's no need to go on the
     * validation.
     *
     * @param value the concrete value that has been validated, we need it
     *              to take decision about the validation process
     * @return      tell the Validator if it should go on or stop
     */
    protected final boolean shouldStopValidation(V value) {
        return true;
    }

    /**
     * Declare this {@link Constraint} unique
     *
     * @return      true
     */
    protected final boolean isUnique() {
        return true;
    }
}
