package com.danieleperuzzi.valid.core;

import java.util.Map;

/**
 * Extend this class when the {@link Constraint} is unique inside the {@link ValidatorOptions}
 *
 * @param <V>   the {@link Validable} type
 * @param <C>   the Object that holds information against which the value is going
 *              to be validated
 */
public abstract class SingleConstraint<V, C> extends Constraint<V, C> {

    protected SingleConstraint(C constraint, int evaluationPriority, String error) {
        super(constraint, evaluationPriority, error);
    }

    protected SingleConstraint(C constraint, int evaluationPriority, Map<String, String> errorMap) {
        super(constraint, evaluationPriority, errorMap);
    }

    protected abstract ConstraintResult evaluate(V value);

    /**
     * Because this {@link Constraint} is unique there's no need to go on the
     * validation.
     *
     * @param value the concrete value that has been validated, we need it
     *              to take decision about the validation process
     * @return      tell the Validator if it should go on or stop
     */
    protected final boolean shouldStopValidation(V value) {
        return true;
    }

    /**
     * Declare this {@link Constraint} unique
     *
     * @return      true
     */
    protected final boolean isUnique() {
        return true;
    }
}
