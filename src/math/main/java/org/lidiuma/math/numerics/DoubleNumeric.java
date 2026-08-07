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

import org.lidiuma.math.api.traits.numeric.OrderableFloatingNumerical;

public class DoubleNumeric implements OrderableFloatingNumerical<Double> {

    public static final DoubleNumeric WITNESS = new DoubleNumeric();

    private DoubleNumeric() {}

    @Override
    public Double zero() {
        return 0d;
    }

    @Override
    public Double one() {
        return 1d;
    }

    @Override
    public Double signum(Double operant) {
        return Math.signum(operant);
    }

    @Override
    public Double sqrt(Double operant) {
        return Math.sqrt(operant);
    }

    @Override
    public Double ceil(Double operant) {
        return Math.ceil(operant);
    }

    @Override
    public Double floor(Double operant) {
        return Math.floor(operant);
    }

    @Override
    public Double add(Double op1, Double op2) {
        return op1 + op2;
    }

    @Override
    public Double multiply(Double op1, Double op2) {
        return op1 * op2;
    }

    @Override
    public Double divide(Double op1, Double op2) {
        return op1 / op2;
    }

    @Override
    public Double remainder(Double op1, Double op2) {
        return op1 % op2;
    }

    @Override
    public Double negated(Double operant) {
        return -operant;
    }

    @Override
    public boolean lessThan(Double op1, Double op2) {
        return op1 < op2;
    }
}

