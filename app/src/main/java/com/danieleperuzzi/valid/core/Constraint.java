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

import android.support.annotation.NonNull;

import java.util.Map;

/**
 * This Class exposes methods that are used by the Validator to perform essential
 * operation on every constraint given to it.
 *
 * <p>You must extend this Class and implement few methods to be fully operative.</p>
 *
 * <p>The Class expects that you are going to validate a {@link Validable} Object type
 * that the concrete implementation is able to process, otherwise it throws a
 * ClassCastException at runtime</p>
 *
 * @param <V>   the {@link Validable} type
 * @param <C>   the Object that holds information against which the value is going
 *              to be validated
 */
public abstract class Constraint<V, C> implements Comparable<Constraint> {

    private C constraint;

    /**
     * Every Constraint declares its priority used by the ValidatorOptions
     * to order them. This is necessary to let the Validator be able
     * to check the constraints in the correct order.
     *
     * <p>the lower number the higher priority is or, if you prefer, the lower
     * number the first to be checked</p>
     */
    private int evaluationPriority;

    private String error;
    private Map<String, String> errorMap;

    protected final C getConstraint() {
        return constraint;
    }

    protected final String getError() {
        return error;
    }

    protected final Map<String, String> getErrorMap() {
        return errorMap;
    }

    /**
     * @param constraint            the Object that holds information against
     *                              which the value is going to be validated
     * @param evaluationPriority    the priority of this Constraint
     * @param error                 the error thrown if evaluation fails
     */
    protected Constraint(C constraint, int evaluationPriority, String error) {
        this(constraint, evaluationPriority);

        this.error = error;
    }

    /**
     * @param constraint            the Object that holds information against
     *                              which the value is going to be validated
     * @param evaluationPriority    the priority of this Constraint
     * @param errorMap              a map is used if this constraint can throw
     *                              multiple errors
     */
    protected Constraint(C constraint, int evaluationPriority, Map<String, String> errorMap) {
        this(constraint, evaluationPriority);

        this.errorMap = errorMap;
    }

    private Constraint(C constraint, int evaluationPriority) {
        this.constraint = constraint;
        this.evaluationPriority = evaluationPriority;
    }

    private Constraint() {
    }

    /**
     * Used to inspect the {@link Validable} type, if we cannot perform a cast
     * a ClassCastException is thrown with a custom message, otherwise the
     * concrete {@link Validable} type is returned
     *
     * @param value                 the {@link Validable} Object to be inspected
     * @return                      the concrete Object held by the {@link Validable}
     * @throws ClassCastException   the exception thrown if the type this Constraint
     *                              can validate and the provided {@link Validable} Object type
     *                              does not match
     */
    private V tryToGetConcreteValidableValue(Validable<?> value) throws ClassCastException {
        try {
            return (V) value.getValue();

        } catch (ClassCastException e) {
            throw new ClassCastException("This Constraint cannot valid a " + value.getValue().getClass().getSimpleName() +
                    " Object");
        }
    }

    /**
     * This method is exposed to the Validator, his only scope is to extract the {@link Validable}
     * concrete value and pass it to the method that performs validation implemented by
     * the Class that extends this one.
     *
     * <p>The only thing we must do is to check if the value held by the {@link Validable} Object
     * is of the same type of the one that the Constraint can process</p>
     *
     * @param value                 the current value to be validated, every Constraint
     *                              implementation knows how to evaluate it
     * @return                      the result of the operation that is a status
     *                              and an optional error message
     */
     final ConstraintResult evaluate(Validable<?> value) {
        V concreteValue = tryToGetConcreteValidableValue(value);
        return evaluate(concreteValue);
    }

    /**
     * Every constraints validates a single portion of a {@link Validable} Object
     * as opposite to the Validator that validates them all.
     *
     * <p>This method performs a single validation and return the result
     * to the Validator that collects all the informations and take
     * decisions.</p>
     *
     * @param value the concrete value that is going to be validated
     * @return      the result of the operation that is a status
     *              and an optional error message
     */
    protected abstract ConstraintResult evaluate(V value);

    /**
     * This method is exposed to the Validator, his only scope is to extract the {@link Validable}
     * concrete value and pass it to the method that takes decisions about the validation
     * process, implemented by the Class that extends this one.
     *
     * <p>The only thing we must do is to check if the value held by the {@link Validable} Object
     * is of the same type of the one that the Constraint can process</p>
     *
     * @param value the current {@link Validable} that has been validated,
     *              we need it to take decision about the validation process
     * @return      tell the Validator if it should go on or stop
     */
     final boolean shouldStopValidation(Validable<?> value) {
        V concreteValue = tryToGetConcreteValidableValue(value);
        return shouldStopValidation(concreteValue);
    }

    /**
     * Based on the concrete value that has been validated, this method decides if
     * we should stop the validation after the value has been evaluated
     * against the contraint to true.
     *
     * <p>In other words this means that a positive validation is sufficient enough
     * to declare the entire {@link Validable} Object validated,
     * regardless the remaining constraints that haven't been validated yet.</p>
     *
     * @param value the concrete value that has been validated, we need it
     *              to take decision about the validation process
     * @return      tell the Validator if it should go on or stop
     */
    protected abstract boolean shouldStopValidation(V value);

    /**
     * Used to tell the {@link ValidatorOptions} that this constraint
     * should be the only one in the constraint list
     *
     * @return      true if this constraint is unique otherwise false
     */
    protected abstract boolean isUnique();

    /**
     * The logic used to compare the constraint
     *
     * @param constraint    the constraint against which compare
     * @return              an integer as declared in the {@link Comparable} interface
     */
    @Override
    public final int compareTo(@NonNull Constraint constraint) {
        int result = 0;

        if (this.evaluationPriority < constraint.evaluationPriority) {
            result = -1;
        }

        if (this.evaluationPriority == constraint.evaluationPriority) {
            result = 0;
        }

        if (this.evaluationPriority > constraint.evaluationPriority) {
            result = 1;
        }

        return result;
    }
}
