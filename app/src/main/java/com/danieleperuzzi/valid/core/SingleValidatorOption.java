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
import java.util.List;

/**
 * Use this Class when it is needed only one constraint.
 *
 * <p>It is suggested to use directly {@link Validator#validate(Validable, Constraint, Validator.Callback)}
 * because that method instantiates this Class internally</p>
 */
public final class SingleValidatorOption implements ValidatorOptions {

    private List<Constraint<?, ?>> constraints = new ArrayList<>();

    @Override
    public List<Constraint<?, ?>> getConstraints() {
        return constraints;
    }

    public SingleValidatorOption(Constraint<?, ?> constraint) {
        constraints.add(constraint);
    }
}
