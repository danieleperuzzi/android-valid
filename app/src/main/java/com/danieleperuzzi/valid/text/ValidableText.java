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

import android.support.annotation.Nullable;

import com.danieleperuzzi.valid.core.Validable;

public class ValidableText implements Validable<String> {

    private String text;

    @Nullable
    private String tag;

    @Override
    public String getValue() {
        return text;
    }

    @Override
    public String getTag() {
        return tag;
    }

    @Override
    public void setValue(String text, @Nullable String tag) {
        this.text = text;
        this.tag = tag;
    }

    public ValidableText(String text, @Nullable String tag) {
        this.text = text;
        this.tag = tag;
    }

    public ValidableText() {
    }
}
