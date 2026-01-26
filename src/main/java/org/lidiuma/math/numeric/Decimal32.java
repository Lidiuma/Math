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

package org.lidiuma.math.numeric;

/// Fixed-point decimal class using 32 bits.\
/// The class provides 3 decimal places.
public value class Decimal32 extends Decimal {

    private int internal;

    public Decimal32(int internal) {
        this.internal = internal;
    }

    /// A double is enough to represent the whole Decimal32 range accurately, making it safe to use.
    public Decimal32(double value) {
        final long scale = scaleOfDecimal((byte) 3);
        this.internal = (int) (value * scale);
    }

    @Override
    public long scale() {
        return scaleOfDecimal((byte) 3); // This is always constant-folded.
    }

    public double asDouble() {
        return internal / (double) scale();
    }

    public Decimal32 add(Decimal32 other) {
        return new Decimal32(internal + other.internal);
    }

    public Decimal32 sub(Decimal32 other) {
        return new Decimal32(internal - other.internal);
    }

    public Decimal32 mul(Decimal32 other) {
        final long multiplied = ((long) internal * other.internal) / scale();
        return new Decimal32((int) multiplied);
    }

    public Decimal32 div(Decimal32 other) {
        final long divided = ((long) internal * scale()) / other.internal;
        return new Decimal32((int) divided);
    }

    public Decimal32 sqrt() {
        final int result = (int) (Math.sqrt(asDouble()) * scale());
        return new Decimal32(result);
    }

    @Override
    public String toString() {
        final long integer = internal / scale();
        final long decimal = internal % scale();
        return String.format("%d.%03d", integer, decimal);
    }
}
