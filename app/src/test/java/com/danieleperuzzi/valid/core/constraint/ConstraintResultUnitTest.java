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

import com.danieleperuzzi.valid.core.ValidableStatus;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

public class ConstraintResultUnitTest {

    private ConstraintResult firstResult = new ConstraintResult(ValidableStatus.VALID, null);
    private ConstraintResult secondResult = new ConstraintResult(ValidableStatus.VALID, "This error should not be there");
    private ConstraintResult thirdResult = new ConstraintResult(ValidableStatus.NOT_VALID, null);
    private ConstraintResult fourthResult = new ConstraintResult(ValidableStatus.NOT_VALID, "This error should be there");

    @Test
    public void checkValues() {
        assertThat(firstResult.status, equalTo(ValidableStatus.VALID));
        assertThat(firstResult.constraintError, equalTo(null));

        assertThat(secondResult.status, equalTo(ValidableStatus.VALID));
        assertThat(secondResult.constraintError, equalTo(null));

        assertThat(thirdResult.status, equalTo(ValidableStatus.NOT_VALID));
        assertThat(thirdResult.constraintError, equalTo(null));

        assertThat(fourthResult.status, equalTo(ValidableStatus.NOT_VALID));
        assertThat(fourthResult.constraintError, equalTo("This error should be there"));
    }
}
