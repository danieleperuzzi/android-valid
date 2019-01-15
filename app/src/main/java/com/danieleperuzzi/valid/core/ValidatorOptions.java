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

import java.util.List;

/**
 * Every {@link Validable} Object must match all the provided {@link Constraint}
 * in order to be declared valid.
 *
 * <p>This interface simply exposes a list of priority ordered constraints
 * that must be associated to a validable object.
 * The order on which we evaluate all the constraints is specified
 * in the constraint itself, we just order them so the {@link Validator}
 * can process them one by one</p>
 */
public interface ValidatorOptions {

    /**
     * Classes that implements this method should take care of ordering the
     * {@link Constraint} by priority, from the lowest to the highest
     *
     * @return      the priority ordered {@link Constraint}
     */
    List<Constraint<?, ?>> getConstraints();
}