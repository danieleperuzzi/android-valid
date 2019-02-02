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

package com.danieleperuzzi.valid.core.constraint;

import com.danieleperuzzi.valid.core.Validable;
import com.danieleperuzzi.valid.mock.ConstraintMock;
import com.danieleperuzzi.valid.mock.ConstraintMockSubclass;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ConstraintUnitTest {

    private ConstraintMock<String, String> firstConstraint = new ConstraintMock<>("", 0, "");
    private ConstraintMock<String, String> samePriorityOfFirstConstraint = new ConstraintMock<>("constraint", 0, "");
    private ConstraintMockSubclass<String, String> firstConstraintSubclass = new ConstraintMockSubclass<>("", 0, "");
    private ConstraintMock<String, String> firstConstraintClone = new ConstraintMock<>("", 0, "");
    private ConstraintMock<String, String> secondConstraint = new ConstraintMock<>("", 1, "");

    private ConstraintMock<String, String> spiedFirstConstraint = Mockito.spy(firstConstraint);

    private ConstraintResult result = mock(ConstraintResult.class);

    @SuppressWarnings("unchecked")
    private Validable<String> correctValidable = mock(Validable.class);

    @SuppressWarnings("unchecked")
    private Validable<Integer> wrongValidable = mock(Validable.class);


    @Before
    public void prepareConstraint() {
        Mockito.when(correctValidable.getValue()).thenReturn("value");
        Mockito.when(wrongValidable.getValue()).thenReturn(0);

        Mockito.doReturn(result).when(spiedFirstConstraint).evaluate("value");
        Mockito.doReturn(true).when(spiedFirstConstraint).shouldStopValidation("value");
    }

    @Test
    public void passCorrectValidableToEvaluate() {
        ConstraintResult firstResult = spiedFirstConstraint.evaluate(correctValidable);

        assertThat(firstResult, sameInstance(result));
    }

    @Test
    public void passCorrectValidableToShouldStopValidation() {
        boolean shouldStopValidation = spiedFirstConstraint.shouldStopValidation(correctValidable);

        assertThat(shouldStopValidation, is(true));
    }

    @Test
    public void passWrongValidableToEvaluate() {
        try {
            ConstraintResult firstResult = spiedFirstConstraint.evaluate(wrongValidable);
        } catch (Exception e) {
            assertThat(e.toString(), equalTo("java.lang.ClassCastException: java.lang.Integer cannot be cast to java.lang.String"));
            assertThat(e, instanceOf(ClassCastException.class));
        }
    }

    @Test
    public void passWrongValidableToShouldStopValidation() {
        try {
            boolean shouldStopValidation = spiedFirstConstraint.shouldStopValidation(wrongValidable);
        } catch (Exception e) {
            assertThat(e.toString(), equalTo("java.lang.ClassCastException: java.lang.Integer cannot be cast to java.lang.String"));
            assertThat(e, instanceOf(ClassCastException.class));
        }
    }

    @Test
    public void checkEquals() {
        assertThat(firstConstraint.equals(firstConstraint), is(true));
        assertThat(firstConstraint.equals(secondConstraint), is(false));
        assertThat(firstConstraint.equals(samePriorityOfFirstConstraint), is(false));
        assertThat(firstConstraint.equals(firstConstraintClone), is(true));
        assertThat(firstConstraint.equals(firstConstraintSubclass), is(false));
    }
}
