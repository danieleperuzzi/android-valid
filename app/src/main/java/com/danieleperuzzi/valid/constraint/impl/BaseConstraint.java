package com.danieleperuzzi.valid.constraint.impl;

import android.support.annotation.NonNull;

import com.danieleperuzzi.valid.constraint.Constraint;
import com.danieleperuzzi.valid.constraint.ConstraintErrorMap;
import com.danieleperuzzi.valid.validator.Validable;
import com.danieleperuzzi.valid.validator.ValidatorResult;

import java.util.List;
import java.util.Map;

public abstract class BaseConstraint<V, C> implements Constraint {

    private C validationConstraint;
    private int evaluationPriority;
    private ConstraintErrorMap errorMap;

    public C getValidationConstraint() {
        return validationConstraint;
    }

    public int getEvaluationPriority() {
        return evaluationPriority;
    }

    protected ConstraintErrorMap getErrorMap() {
        return errorMap;
    }

    public BaseConstraint(C validationConstraint, int evaluationPriority, ConstraintErrorMap errorMap) throws Exception {
        this.validationConstraint = validationConstraint;
        this.evaluationPriority = evaluationPriority;
        this.errorMap = errorMap;

        init();
    }

    public abstract Class<?> getValidableConcreteClassType(Validable<?> value);

    private void checkType(Validable<?> value) throws ClassCastException {
        Class<?> validableConcreteClassType = getValidableConcreteClassType(value);

        if (validableConcreteClassType != null && !validableConcreteClassType.isInstance(value.getValue())) {
            String className = value.getValue().getClass().getSimpleName();
            throw new ClassCastException("The value provided must be an instance of " + className);
        }
    }

    public boolean shouldBreakValidateChain(Validable<?> value) throws ClassCastException {
        checkType(value);
        V concreteValue = (V) value.getValue();
        return shouldBreakValidateChain(concreteValue);
    }

    public abstract boolean shouldBreakValidateChain(V value);

    @Override
    public int compareTo(@NonNull Constraint constraint) {
        int result = 0;

        if (evaluationPriority < constraint.getEvaluationPriority()) {
            result = -1;
        }

        if (evaluationPriority == constraint.getEvaluationPriority()) {
            result = 0;
        }

        if (evaluationPriority > constraint.getEvaluationPriority()) {
            result = 1;
        }

        return result;
    }


    public ValidatorResult evaluate(Validable<?> value) throws ClassCastException {
        checkType(value);
        V concreteValue = (V) value.getValue();
        return evaluate(concreteValue);
    }

    public abstract ValidatorResult evaluate(V value);

    protected abstract List<String> getErrorKeyList();

    private void init() throws Exception {
        List<String> errorKeyList = getErrorKeyList();
        int count = 0;

        for (Map.Entry<String, String> entry : errorMap.getErrorMap().entrySet()) {
            if (errorKeyList.contains(entry.getKey()) && entry.getValue() != null) {
                count++;
            }
        }

        if (errorKeyList.size() != count) {
            throw new Exception("Must implement all the error messages");
        }
    }
}
