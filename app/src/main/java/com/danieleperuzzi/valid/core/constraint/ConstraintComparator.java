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

package com.danieleperuzzi.valid.core.constraint;

import android.support.annotation.NonNull;

import java.util.Comparator;

/**
 * Simple comparator used by {@link SortedConstraintSet} to order the {@link Constraint}
 */
public final class ConstraintComparator implements Comparator<Constraint<?, ?>> {

    @Override
    public final int compare(@NonNull Constraint firstConstraint, @NonNull Constraint secondConstraint) {
        int result = 0;

        if (firstConstraint.getEvaluationPriority() < secondConstraint.getEvaluationPriority()) {
            result = -1;
        }

        if (firstConstraint.getEvaluationPriority() == secondConstraint.getEvaluationPriority()) {
            result = 0;
        }

        if (firstConstraint.getEvaluationPriority() > secondConstraint.getEvaluationPriority()) {
            result = 1;
        }

        return result;
    }
}