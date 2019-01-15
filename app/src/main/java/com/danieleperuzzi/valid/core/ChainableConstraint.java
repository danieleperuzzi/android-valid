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
 * When a {@link Constraint} can exist in conjunction to other ones inside a {@link ValidatorOptions}
 * then this Class should be used as opposed to {@link UniqueConstraint}
 *
 * @param <V>   the {@link Validable} type
 * @param <C>   the Object that holds information against which the value is going
 *              to be validated
 */
public abstract class ChainableConstraint<V, C> extends Constraint<V, C> {

    protected ChainableConstraint(C constraint, int evaluationPriority, String error) {
        super(constraint, evaluationPriority, error);
    }

    protected ChainableConstraint(C constraint, int evaluationPriority, Map<String, String> errorMap) {
        super(constraint, evaluationPriority, errorMap);
    }

    protected abstract ConstraintResult evaluate(V value);

    protected abstract boolean shouldStopValidation(V value);

    /**
     * Declare this {@link Constraint} not unique
     *
     * @return      false
     */
    protected final boolean isUnique() {
        return false;
    }
}
