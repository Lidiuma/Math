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

package org.lidiuma.math.vector.v1;

import jdk.internal.vm.annotation.NullRestricted;
import org.lidiuma.math.api.tuple.UnaryTuple1;
import org.lidiuma.math.api.vector.Vector1;
import org.lidiuma.math.tuple.Tuples;
import java.util.function.UnaryOperator;

public value record Vector1L(
        @Override @NullRestricted Long x
) implements Vector1<Long> {

    /// A constructor creating a specialized vector from a generic vector.
    public Vector1L(Vector1<Long> vector1) {
        this(vector1.x());
    }

    @Override
    public Vector1L add(Vector1<Long> other) {
        return new Vector1L(x() + other.x());
    }

    @Override
    public Vector1L subtract(Vector1<Long> other) {
        return new Vector1L(x() - other.x());
    }

    @Override
    public Vector1L multiply(Vector1<Long> other) {
        return new Vector1L(x() * other.x());
    }

    @Override
    public Vector1L multiply(Long scalar) {
        return new Vector1L(x() * scalar);
    }

    @Override
    public Vector1L divide(Vector1<Long> other) {
        return new Vector1L(x() / other.x());
    }

    @Override
    public Vector1L negated() {
        return new Vector1L(-x());
    }

    @Override
    public boolean lessThan(Vector1<Long> other) {
        return x() < other.x();
    }

    @Override
    public boolean lessThanEqual(Vector1<Long> other) {
        return x() <= other.x();
    }

    @Override
    public boolean greaterThan(Vector1<Long> other) {
        return x() > other.x();
    }

    @Override
    public boolean greaterThanEqual(Vector1<Long> other) {
        return x() >= other.x();
    }

    @Override
    public Vector1L abs() {
        return new Vector1L(Math.abs(x()));
    }

    @Override
    public Vector1L signum() {
        return new Vector1L((long) Long.signum(x()));
    }

    @Override
    public Vector1L max(Vector1<Long> other) {
        return new Vector1L(Math.max(x(), other.x()));
    }

    @Override
    public Vector1L min(Vector1<Long> other) {
        return new Vector1L(Math.min(x(), other.x()));
    }

    @Override
    public Vector1L clamp(Long min, Long max) {
        return new Vector1L(Math.clamp(x(), min, max));
    }

    @Override
    public Vector1L clamp(UnaryTuple1<Long> min, UnaryTuple1<Long> max) {
        return new Vector1L(Math.clamp(x(), min.x(), max.x()));
    }

    @Override
    public Vector1L ceil() {
        return this;
    }

    @Override
    public Vector1L floor() {
        return this;
    }

    @Override
    public Long distance(Vector1<Long> vector) {
        return Math.abs(x() - vector.x());
    }

    @Override
    public Long distanceSquared(Vector1<Long> other) {
        final long distance = x() - other.x();
        return distance * distance;
    }

    @Override
    public Long length() {
        return Math.abs(x());
    }

    @Override
    public Long lengthSquared() {
        return x() * x();
    }

    @Override
    public Vector1L withLength(Long length) {
        final long current = length();
        if (current == 0 || current == length) return this;
        return signum().multiply(length);
    }

    /// This operation is lossy, use [#withLengthSquaredD(long)] instead for full precision.
    @Override
    public Vector1L withLengthSquared(Long lengthSquared) {
        final long current = lengthSquared();
        if (current == 0 || current == lengthSquared) return this;
        return signum().multiply((long) Math.sqrt(lengthSquared));
    }

    public Vector1D withLengthSquaredD(long lengthSquared) {

        final long current = lengthSquared();
        final double x = Double.valueOf(x());

        if (current == 0 || current == lengthSquared) return new Vector1D(x);
        return new Vector1D(x)
                .signum()
                .multiply(Math.sqrt(lengthSquared));
    }

    @Override
    public Vector1L withLimit(Long limit) {
        final long current = length();
        if (current == 0 || current <= limit) return this;
        return signum().multiply(limit);
    }

    /// This operation is lossy, use [#withLimitSquaredD(long)] instead for full precision.
    @Override
    public Vector1L withLimitSquared(Long limitSquared) {
        final long current = lengthSquared();
        if (current == 0 || current <= limitSquared) return this;
        return signum().multiply((long) Math.sqrt(limitSquared));
    }

    public Vector1D withLimitSquaredD(long limitSquared) {

        final long current = lengthSquared();
        final double x = Double.valueOf(x());

        if (current == 0 || current <= limitSquared) return new Vector1D(x);
        return new Vector1D(x)
                .signum()
                .multiply(Math.sqrt(limitSquared));
    }

    @Override
    public Long dot(Vector1<Long> other) {
        return x() * other.x();
    }

    @Override
    public Vector1L normalized() {
        return signum();
    }

    @Override
    public Vector1L normalized(Vector1<Long> orElse) {
        final long current = lengthSquared();
        if (current == 0) return new Vector1L(orElse);
        return signum();
    }

    /// Interpolates `this` and the `other` vector with a range `[0,maxAlpha]`.
    /// @apiNote Alpha is not clamped and can be used above and bellow the maximum and minimum values.
    public Vector1L interpolate(Vector1<Long> target, long alpha, long maxAlpha, UnaryOperator<Long> easing) {
        final long eased = easing.apply(alpha);
        final long invAlpha = maxAlpha - eased;
        final long x = (x() * invAlpha + target.x() * eased) / maxAlpha;
        return new Vector1L(x);
    }

    /// Interpolates `this` and the `other` vector with a range `[0,100]`.
    ///
    /// To have more control over the max alpha [#longerpolate(Vector1, long, long, UnaryOperator)] can be used.
    /// @param target the vector to longerpolate towards.
    /// @param alpha the progress of the longerpolation.
    /// @param easing a function to adjust the longerpolation curve ([identity][UnaryOperator#identity()] for linear).
    /// @return the longerpolated vector.
    /// @apiNote Alpha is not clamped and can be used above and bellow the maximum and minimum values.
    @Override
    public Vector1L interpolate(Vector1<Long> target, Long alpha, UnaryOperator<Long> easing) {
        return interpolate(target, alpha, 100, easing);
    }

    /// Linearly longerpolates `this` and the `other` with a range `[0,maxAlpha]`.
    /// @param target the vector to longerpolate towards.
    /// @param alpha the progress of the longerpolation.
    /// @return the longerpolated vector.
    /// @apiNote Alpha is not clamped and can be used above and bellow the maximum and minimum values.
    public Vector1L lerp(Vector1<Long> target, long alpha, long maxAlpha) {
        return interpolate(target, alpha, maxAlpha, UnaryOperator.identity());
    }

    @Override
    public boolean equals(UnaryTuple1<Long> other, Long epsilon) {
        return Tuples.epsilonEquals(x(), other.x(), epsilon);
    }

    @Override
    public boolean componentEquals(Long value, Long epsilon) {
        final var vec1 = new Vector1L(value);
        return equals(vec1, epsilon);
    }
}
