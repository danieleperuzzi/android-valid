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

package com.danieleperuzzi.valid.constraint;

import android.support.annotation.NonNull;

import com.danieleperuzzi.valid.validator.Validable;
import com.danieleperuzzi.valid.validator.ValidatorResult;

public interface Constraint extends Comparable<Constraint> {
    boolean shouldBreakValidateChain(Validable<?> value) throws ClassCastException;
    ValidatorResult evaluate(Validable<?> value) throws ClassCastException;
    int getEvaluationPriority();
    int compareTo(@NonNull Constraint validator);
}
