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
 * Use this Class when {@link Constraint} are more than one.
 */
public final class MultipleValidatorOptions implements ValidatorOptions {

    private List<Constraint<?, ?>> constraints = new ArrayList<>();

    @Override
    public List<Constraint<?, ?>> getConstraints() {
        return constraints;
    }

    private MultipleValidatorOptions() {
    }


    public final static class Builder {

        private final String TAG = getClass().getSimpleName();

        private MultipleValidatorOptions instance;

        public Builder() {
            this.instance = new MultipleValidatorOptions();
        }

        public Builder addConstraint(Constraint<?, ?> constraint) {
            instance.constraints.add(constraint);
            return this;
        }


        /**
         * When we finally construct the MultipleValidatorOptions Object we also
         * order the {@link Constraint} by priority, from the lowest to the highest
         *
         * @return      the {@link MultipleValidatorOptions} Object
         */
        public MultipleValidatorOptions build() {
            Collections.sort(instance.constraints);
            return instance;
        }
    }
}
