/*
 * Copyright (c) 2026 Xasmedy
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

package org.lidiuma.math.numerics;

import org.lidiuma.math.api.traits.numeric.OrderableNumerical;

public final class IntegerNumeric implements OrderableNumerical<Integer> {

    public static final IntegerNumeric OPS = new IntegerNumeric();

    private IntegerNumeric() {}

    @Override
    public Integer zero() {
        return 0;
    }

    @Override
    public Integer one() {
        return 1;
    }

    @Override
    public Integer add(Integer op1, Integer op2) {
        return op1 + op2;
    }

    @Override
    public Integer multiply(Integer op1, Integer op2) {
        return op1 * op2;
    }

    @Override
    public Integer divide(Integer op1, Integer op2) {
        return op1 / op2;
    }

    @Override
    public Integer remainder(Integer op1, Integer op2) {
        return op1 % op2;
    }

    @Override
    public Integer negated(Integer operant) {
        return -operant;
    }

    @Override
    public boolean lessThan(Integer op1, Integer op2) {
        return op1 < op2;
    }
}
