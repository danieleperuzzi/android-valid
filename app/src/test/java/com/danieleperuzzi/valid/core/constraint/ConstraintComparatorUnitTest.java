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

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ConstraintComparatorUnitTest {

    private ConstraintMock<String, String> firstConstraint = new ConstraintMock<>("", 0, "");
    private ConstraintMock<String, String> samePriorityOfFirstConstraint = new ConstraintMock<>("constraint", 0, "");
    private ConstraintMockSubclass<String, String> firstConstraintSubclass = new ConstraintMockSubclass<>("", 0, "");
    private ConstraintMock<String, String> firstConstraintClone = new ConstraintMock<>("", 0, "");
    private ConstraintMock<String, String> firstConstraintCloneClone = new ConstraintMock<>("", 0, "");
    private ConstraintMock<String, String> secondConstraint = new ConstraintMock<>("", 1, "");
    private ConstraintMock<String, String> thirdConstraint = new ConstraintMock<>("", 2, "");
    private ConstraintMock<String, String> fourthConstraint = new ConstraintMock<>("", 2, "");
    private ConstraintMock<String, String> fifthConstraint = new ConstraintMock<>("", -1, "");


    private ConstraintComparator comparator = new ConstraintComparator();


    @Test
    public void checkLesser() {
        int comparison = comparator.compare(firstConstraint, secondConstraint);

        assertThat(comparison, lessThan(0));
    }

    @Test
    public void checkGreater() {
        int comparison = comparator.compare(secondConstraint, firstConstraint);

        assertThat(comparison, greaterThan(0));
    }

    @Test
    public void checkEquals() {
        int comparison = comparator.compare(firstConstraint, firstConstraintClone);

        assertThat(comparison, equalTo(0));
    }

    @Test
    public void checkEqualEvaluationPriority() {
        int comparison = comparator.compare(firstConstraint, samePriorityOfFirstConstraint);

        assertThat(comparison, greaterThan(0));
    }

    @Test
    public void checkSubclassNotEqual() {
        int comparison = comparator.compare(firstConstraint, firstConstraintSubclass);
        int secondComparison = comparator.compare(firstConstraintSubclass, firstConstraint);

        assertThat(comparison, greaterThan(0));
        assertThat(secondComparison, greaterThan(0));
    }

    @Test
    public void checkSignum() {
        int firstComparison = comparator.compare(firstConstraint, secondConstraint);
        int secondComparison = comparator.compare(secondConstraint, firstConstraint);

        secondComparison *= -1;

        assertThat(firstComparison, equalTo(secondComparison));
    }

    @Test
    public void checkSignumWithEquals() {
        int firstComparison = comparator.compare(firstConstraint, firstConstraintClone);
        int secondComparison = comparator.compare(firstConstraintClone, firstConstraint);

        secondComparison *= -1;

        assertThat(firstComparison, equalTo(secondComparison));
    }

    @Test
    public void checkSignumWithAny() {
        int firstComparison = comparator.compare(firstConstraint, firstConstraintClone);
        int secondComparison = comparator.compare(firstConstraint, thirdConstraint);
        int thirdComparison = comparator.compare(firstConstraintClone, thirdConstraint);

        assertThat(firstComparison, equalTo(0));
        assertThat(secondComparison, equalTo(thirdComparison));
    }

    @Test
    public void isTransitive() {
        int firstComparison = comparator.compare(firstConstraint, secondConstraint);
        int secondComparison = comparator.compare(secondConstraint, thirdConstraint);
        int thirdComparison = comparator.compare(firstConstraint, thirdConstraint);

        assertThat(firstComparison, lessThan(0));
        assertThat(secondComparison, lessThan(0));
        assertThat(thirdComparison, lessThan(0));
    }

    @Test
    public void isTransitiveWithEquals() {
        int firstComparison = comparator.compare(firstConstraint, firstConstraintClone);
        int secondComparison = comparator.compare(firstConstraintClone, firstConstraintCloneClone);
        int thirdComparison = comparator.compare(firstConstraint, firstConstraintCloneClone);

        assertThat(firstComparison, equalTo(0));
        assertThat(secondComparison, equalTo(0));
        assertThat(thirdComparison, equalTo(0));
    }

    @Test
    public void orderConstraint() {
        List<Constraint<?, ?>> constraintList = new ArrayList<>();

        constraintList.add(secondConstraint);
        constraintList.add(fourthConstraint);
        constraintList.add(firstConstraint);
        constraintList.add(samePriorityOfFirstConstraint);
        constraintList.add(firstConstraintClone);
        constraintList.add(firstConstraintCloneClone);
        constraintList.add(firstConstraintSubclass);
        constraintList.add(thirdConstraint);
        constraintList.add(fifthConstraint);

        Collections.sort(constraintList, comparator);

        assertThat(constraintList, contains(fifthConstraint, firstConstraint, samePriorityOfFirstConstraint,
                firstConstraintClone, firstConstraintCloneClone, firstConstraintSubclass,
                secondConstraint, fourthConstraint, thirdConstraint));
    }
}
