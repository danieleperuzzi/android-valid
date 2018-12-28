package com.danieleperuzzi.valid.constraint.impl;

import com.danieleperuzzi.valid.constraint.ConstraintErrorMap;
import com.danieleperuzzi.valid.validator.Validable;
import com.danieleperuzzi.valid.validator.ValidatorResult;
import com.danieleperuzzi.valid.validator.ValidatorStatus;

import java.util.ArrayList;
import java.util.List;

public class MandatoryTextConstraint extends BaseConstraint<String, Boolean> {

    public MandatoryTextConstraint(Boolean mandatory, int evaluationPriority, ConstraintErrorMap errorMap) throws Exception {
        super(mandatory, evaluationPriority, errorMap);
    }

    @Override
    public Class<?> getValidableConcreteClassType(Validable<?> value) {
        if (value.getValue() == null) {
            return null;
        }
        return value.getValue().getClass();
    }

    @Override
    public boolean shouldBreakValidateChain(String text) {
        if (text == null || text.isEmpty()) {
            return true;
        }
        return false;
    }

    @Override
    public ValidatorResult evaluate(String text) {
        ValidatorResult result = new ValidatorResult();

        if ((text == null || text.isEmpty()) && getValidationConstraint()) {
            result.status = ValidatorStatus.NOT_VALIDATED;
            result.validatorError = getErrorMap().getErrorByKey("MANDATORY_FIELD");
        } else {
            result.status = ValidatorStatus.VALIDATED;
            result.validatorError = null;
        }

        return result;
    }

    @Override
    protected List<String> getErrorKeyList() {
        List<String> errorKeyList = new ArrayList<>();
        errorKeyList.add("MANDATORY_FIELD");

        return errorKeyList;
    }
}
