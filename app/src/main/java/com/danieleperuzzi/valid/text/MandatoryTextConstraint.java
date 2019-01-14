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
import com.danieleperuzzi.valid.core.ConstraintResult;
import com.danieleperuzzi.valid.core.ValidableStatus;

public class MandatoryTextConstraint extends Constraint<String, Boolean> {

    public MandatoryTextConstraint(Boolean mandatory, int evaluationPriority, String error) {
        super(mandatory, evaluationPriority, error);
    }

    @Override
    protected boolean shouldStopValidation(String text) {
        return text == null || text.isEmpty();
    }

    @Override
    protected ConstraintResult evaluate(String text) {
        ValidableStatus status;
        String error;

        if ((text == null || text.isEmpty()) && getConstraint()) {
            status = ValidableStatus.NOT_VALID;
            error = getError();
        } else {
            status = ValidableStatus.VALID;
            error = null;
        }

        return new ConstraintResult(status, error);
    }
}
