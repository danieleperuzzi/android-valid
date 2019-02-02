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

import com.danieleperuzzi.valid.mock.ConstraintMock;
import com.danieleperuzzi.valid.mock.ConstraintMockSubclass;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

public class SortedConstraintSetUnitTest {

    private ConstraintMock<String, String> firstConstraint = new ConstraintMock<>("", 0, "");
    private ConstraintMock<String, String> samePriorityOfFirstConstraint = new ConstraintMock<>("constraint", 0, "");
    private ConstraintMockSubclass<String, String> firstConstraintSubclass = new ConstraintMockSubclass<>("", 0, "");
    private ConstraintMock<String, String> firstConstraintClone = new ConstraintMock<>("", 0, "");
    private ConstraintMock<String, String> firstConstraintCloneClone = new ConstraintMock<>("", 0, "");
    private ConstraintMock<String, String> secondConstraint = new ConstraintMock<>("", 1, "");
    private ConstraintMock<String, String> thirdConstraint = new ConstraintMock<>("", 2, "");
    private ConstraintMock<String, String> fourthConstraint = new ConstraintMock<>("", 2, "");
    private ConstraintMock<String, String> fifthConstraint = new ConstraintMock<>("", -1, "");


    @Test
    public void addConstraint() {
        SortedConstraintSet constraintSet = new SortedConstraintSet.Builder()
                .addConstraint(firstConstraint)
                .build();

        assertThat(constraintSet.getConstraints().size(), equalTo(1));
        assertThat(constraintSet.getConstraints(), contains(firstConstraint));
    }

    @Test
    public void addTwoConstraintsAndCheckOrder() {
        SortedConstraintSet constraintSet = new SortedConstraintSet.Builder()
                .addConstraint(secondConstraint)
                .addConstraint(firstConstraint)
                .build();

        assertThat(constraintSet.getConstraints().size(), equalTo(2));
        assertThat(constraintSet.getConstraints(), contains(firstConstraint, secondConstraint));
    }

    @Test
    public void addThreeConstraintsAndCheckOrder() {
        SortedConstraintSet constraintSet = new SortedConstraintSet.Builder()
                .addConstraint(secondConstraint)
                .addConstraint(thirdConstraint)
                .addConstraint(firstConstraint)
                .build();

        assertThat(constraintSet.getConstraints().size(), equalTo(3));
        assertThat(constraintSet.getConstraints(), contains(firstConstraint, secondConstraint, thirdConstraint));
    }

    @Test
    public void addThreeConstraintsWithSamePriorityAndCheckOrder() {
        SortedConstraintSet constraintSet = new SortedConstraintSet.Builder()
                .addConstraint(firstConstraint)
                .addConstraint(secondConstraint)
                .addConstraint(samePriorityOfFirstConstraint)
                .build();

        assertThat(constraintSet.getConstraints().size(), equalTo(3));
        assertThat(constraintSet.getConstraints(), contains(firstConstraint, samePriorityOfFirstConstraint, secondConstraint));
    }

    @Test
    public void addThreeConstraintsWithEqualsAndCheckOrder() {
        SortedConstraintSet constraintSet = new SortedConstraintSet.Builder()
                .addConstraint(firstConstraint)
                .addConstraint(secondConstraint)
                .addConstraint(firstConstraintClone)
                .build();

        assertThat(constraintSet.getConstraints().size(), equalTo(2));
        assertThat(constraintSet.getConstraints(), contains(firstConstraint, secondConstraint));
    }

    @Test
    public void addThreeConstraintsWithSubclassAndCheckOrder() {
        SortedConstraintSet constraintSet = new SortedConstraintSet.Builder()
                .addConstraint(firstConstraint)
                .addConstraint(secondConstraint)
                .addConstraint(firstConstraintSubclass)
                .build();

        assertThat(constraintSet.getConstraints().size(), equalTo(3));
        assertThat(constraintSet.getConstraints(), contains(firstConstraint, firstConstraintSubclass, secondConstraint));
    }

    @Test
    public void addNineConstraintsAndCheckOrder() {
        SortedConstraintSet constraintSet = new SortedConstraintSet.Builder()
                .addConstraint(secondConstraint)
                .addConstraint(fourthConstraint)
                .addConstraint(firstConstraint)
                .addConstraint(samePriorityOfFirstConstraint)
                .addConstraint(firstConstraintClone)
                .addConstraint(firstConstraintCloneClone)
                .addConstraint(firstConstraintSubclass)
                .addConstraint(thirdConstraint)
                .addConstraint(fifthConstraint)
                .build();

        assertThat(constraintSet.getConstraints().size(), equalTo(6));
        assertThat(constraintSet.getConstraints(), contains(fifthConstraint, firstConstraint, samePriorityOfFirstConstraint,
                firstConstraintSubclass, secondConstraint, fourthConstraint));
    }
}
