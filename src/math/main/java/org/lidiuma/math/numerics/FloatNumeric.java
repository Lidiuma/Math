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

public final value class FloatNumeric implements OrderableFloatingNumerical<Float> {

    public static final FloatNumeric OPS = new FloatNumeric();

    private FloatNumeric() {}

    @Override
    public Float zero() {
        return 0f;
    }

    @Override
    public Float one() {
        return 1f;
    }

    @Override
    public Float signum(Float operant) {
        return Math.signum(operant);
    }

    @Override
    public Float sqrt(Float operant) {
        return (float) Math.sqrt(operant);
    }

    @Override
    public Float ceil(Float operant) {
        return (float) Math.ceil(operant);
    }

    @Override
    public Float floor(Float operant) {
        return (float) Math.floor(operant);
    }

    @Override
    public Float add(Float op1, Float op2) {
        return op1 + op2;
    }

    @Override
    public Float multiply(Float op1, Float op2) {
        return op1 * op2;
    }

    @Override
    public Float divide(Float op1, Float op2) {
        return op1 / op2;
    }

    @Override
    public Float remainder(Float op1, Float op2) {
        return op1 % op2;
    }

    @Override
    public Float negated(Float operant) {
        return -operant;
    }

    @Override
    public boolean lessThan(Float op1, Float op2) {
        return op1 < op2;
    }
}
