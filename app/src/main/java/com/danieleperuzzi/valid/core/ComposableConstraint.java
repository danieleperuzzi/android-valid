package com.danieleperuzzi.valid.core;

import java.util.Map;

/**
 * When a {@link Constraint} can exist in conjunction to other ones inside a {@link ValidatorOptions}
 * then this Class should be used as opposed to {@link SingleConstraint}
 *
 * @param <V>   the {@link Validable} type
 * @param <C>   the Object that holds information against which the value is going
 *              to be validated
 */
public abstract class ComposableConstraint<V, C> extends Constraint<V, C> {

    protected ComposableConstraint(C constraint, int evaluationPriority, String error) {
        super(constraint, evaluationPriority, error);
    }

    protected ComposableConstraint(C constraint, int evaluationPriority, Map<String, String> errorMap) {
        super(constraint, evaluationPriority, errorMap);
    }

    protected abstract ConstraintResult evaluate(V value);

    protected abstract boolean shouldStopValidation(V value);

    /**
     * Declare this {@link Constraint} not unique
     *
     * @return      false
     */
    protected final boolean isUnique() {
        return false;
    }
}
