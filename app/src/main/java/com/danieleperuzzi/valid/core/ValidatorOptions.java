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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Every {@link Validable} Object must match all the provided {@link Constraint}
 * in order to be declared validated.
 *
 * <p>This class simply holds a list of priority ordered constraints
 * that must be associated to a validable object.
 * The order on which we evaluate all the constraints is specified
 * in the constraint itself, we just order them so the {@link Validator}
 * can process them one by one</p>
 */
public final class ValidatorOptions {

    private List<Constraint<?, ?>> constraints = new ArrayList<>();

    List<Constraint<?, ?>> getConstraints() {
        return constraints;
    }

    private ValidatorOptions() {
    }


    public final static class Builder {

        private final String TAG = getClass().getSimpleName();

        private ValidatorOptions instance;

        public Builder() {
            this.instance = new ValidatorOptions();
        }

        public Builder addConstraint(Constraint<?, ?> constraint) {
            if (!constraint.isUnique() || (constraint.isUnique() && instance.constraints.size() == 0)) {
                instance.constraints.add(constraint);
            } else {
                throw new RuntimeException(TAG, new Exception(constraint.getClass().getSimpleName() +
                        " must be the only one inside the validator options"));
            }

            return this;
        }


        /**
         * When we finally construct the ValidatorOptions Object we also
         * order the {@link Constraint} by priority, from lower to higher
         * @return
         */
        public ValidatorOptions build() {
            Collections.sort(instance.constraints);
            return instance;
        }
    }
}
