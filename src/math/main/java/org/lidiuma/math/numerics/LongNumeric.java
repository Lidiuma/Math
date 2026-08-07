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

public final class LongNumeric implements OrderableNumerical<Long> {

    public static final LongNumeric OPS = new LongNumeric();

    private LongNumeric() {}

    @Override
    public Long zero() {
        return 0L;
    }

    @Override
    public Long one() {
        return 1L;
    }

    @Override
    public Long add(Long op1, Long op2) {
        return op1 + op2;
    }

    @Override
    public Long multiply(Long op1, Long op2) {
        return op1 * op2;
    }

    @Override
    public Long divide(Long op1, Long op2) {
        return op1 / op2;
    }

    @Override
    public Long remainder(Long op1, Long op2) {
        return op1 % op2;
    }

    @Override
    public Long negated(Long operant) {
        return -operant;
    }

    @Override
    public boolean lessThan(Long op1, Long op2) {
        return op1 < op2;
    }
}
