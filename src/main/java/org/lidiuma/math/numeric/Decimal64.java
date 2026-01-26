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

public value class Decimal64 extends Decimal {

    private long internal;

    public Decimal64(long internal) {
        this.internal = internal;
    }

    @Override
    public long scale() {
        return scaleOfDecimal((byte) 6);
    }

    /// Useful to avoid overflowing.
    private long longInternal() {
        return internal;
    }

    public Decimal64 add(Decimal64 other) {
        return new Decimal64(internal + other.internal);
    }

    public Decimal64 sub(Decimal64 other) {
        return new Decimal64(internal - other.internal);
    }

    public Decimal64 mul(Decimal64 other) {
        // TODO Int128?
        final long multiplied = (longInternal() * other.internal) / scale();
        return new Decimal64((int) multiplied);
    }

    public Decimal64 div(Decimal64 other) {
        // TODO Int128?
        final long divided = (longInternal() * scale()) / other.internal;
        return new Decimal64((int) divided);
    }

    public Decimal64 sqrt() {

        // TODO Int128?
        long scaledX = (long) internal * scale();
        long ans = 0;
        long bit = 1L << 62; // start with highest possible bit for 64-bit long

        // Align bit with scaledX
        while (bit > scaledX) {
            bit >>= 2;
        }

        while (bit != 0) {

            final long delta = scaledX - (ans + bit);
            final long mask = ~(delta >> 63); // 0xFFFFFFFFFFFFFFFF if delta >= 0, 0 otherwise

            scaledX -= (ans + bit) & mask;
            ans = (ans >> 1) + (bit & mask);

            bit >>= 2;
        }

        return new Decimal64((int) ans);
    }

    @Override
    public String toString() {
        final long integer = internal / scale();
        final long decimal = internal % scale();
        return String.format("%d.%06d", integer, decimal);
    }
}
