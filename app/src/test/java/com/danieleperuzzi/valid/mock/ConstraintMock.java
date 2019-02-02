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

package com.danieleperuzzi.valid.mock;

import com.danieleperuzzi.valid.core.Validable;
import com.danieleperuzzi.valid.core.constraint.Constraint;
import com.danieleperuzzi.valid.core.constraint.ConstraintResult;

import java.util.Map;

public class ConstraintMock<V, C> extends Constraint<V, C> {

    public ConstraintMock(C constraint, int evaluationPriority, String error) {
        super(constraint, evaluationPriority, error);
    }

    public ConstraintMock(C constraint, int evaluationPriority, Map<String, String> errorMap) {
        super(constraint, evaluationPriority, errorMap);
    }

    @Override
    public C getConstraint() {
        return super.getConstraint();
    }

    @Override
    public int getEvaluationPriority() {
        return super.getEvaluationPriority();
    }

    @Override
    public String getError() {
        return super.getError();
    }

    @Override
    public Map<String, String> getErrorMap() {
        return super.getErrorMap();
    }

    @Override
    public ConstraintResult evaluate(Validable<?> value) {
        return super.evaluate(value);
    }

    @Override
    public ConstraintResult evaluate(V value) {
        return null;
    }

    @Override
    public boolean shouldStopValidation(Validable<?> value) {
        return super.shouldStopValidation(value);
    }

    @Override
    public boolean shouldStopValidation(V value) {
        return false;
    }
}
