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

package com.danieleperuzzi.valid.text;

import com.danieleperuzzi.valid.core.Constraint;
import com.danieleperuzzi.valid.core.ConstraintErrorMap;
import com.danieleperuzzi.valid.core.ConstraintResult;
import com.danieleperuzzi.valid.core.ValidableStatus;

import java.util.ArrayList;
import java.util.List;

public class MaxLengthTextConstraint extends Constraint<String, Integer> {

    private final boolean shouldBrakeValidateChain = false;

    public MaxLengthTextConstraint(Integer maxLength, int evaluationPriority, ConstraintErrorMap errorMap) throws Exception {
        super(maxLength, evaluationPriority, errorMap);
    }

    @Override
    protected boolean shouldBreakValidationChain(String text) {
        return shouldBrakeValidateChain;
    }

    @Override
    protected ConstraintResult evaluate(String text) {
        ValidableStatus status;
        String error;

        if (text == null) {
            status = ValidableStatus.VALIDATED;
            error = null;
            return new ConstraintResult(status, error);
        }

        if ((text.length() > getValidationConstraint()) && (getValidationConstraint() != -1)) {
            status = ValidableStatus.NOT_VALIDATED;
            error = getErrorMap().getErrorByKey("MAX_LENGTH_EXCEEDED");
        } else {
            status = ValidableStatus.VALIDATED;
            error = null;
        }

        return new ConstraintResult(status, error);
    }

    @Override
    protected List<String> getErrorKeyList() {
        List<String> errorKeyList = new ArrayList<>();
        errorKeyList.add("MAX_LENGTH_EXCEEDED");

        return errorKeyList;
    }
}
