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

import com.danieleperuzzi.valid.core.Validable;
import com.danieleperuzzi.valid.core.Validator;

import java.util.Set;
import java.util.TreeSet;

/**
 * Every {@link Validable} Object must match all the provided {@link Constraint}
 * in order to be declared valid.
 *
 * <p>This class simply exposes a set of priority ordered constraints
 * that must be associated to a validable object.
 * The order on which we evaluate all the constraints is specified
 * in the constraint itself, we just order them so the {@link Validator}
 * can process them one by one</p>
 */
public class SortedConstraintSet {

    private Set<Constraint<?, ?>> constraints;

    /**
     * @return  the priority ordered {@link Constraint} set
     */
    public Set<Constraint<?, ?>> getConstraints() {
        return constraints;
    }

    private SortedConstraintSet() {
        constraints = new TreeSet<>(new ConstraintComparator());
    }


    public static class Builder {

        private SortedConstraintSet instance;

        public Builder() {
            this.instance = new SortedConstraintSet();
        }

        public Builder addConstraint(Constraint<?, ?> constraint) {
            instance.constraints.add(constraint);
            return this;
        }

        public SortedConstraintSet build() {
            return instance;
        }
    }
}