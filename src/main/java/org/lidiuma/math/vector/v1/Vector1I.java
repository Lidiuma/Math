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

public value record Vector1I(
        @Override @NullRestricted Integer x
) implements Vector1<Integer> {

    /// A constructor creating a specialized vector from a generic vector.
    public Vector1I(Vector1<Integer> vector1) {
        this(vector1.x());
    }

    @Override
    public Vector1I add(Vector1<Integer> other) {
        return new Vector1I(x() + other.x());
    }

    @Override
    public Vector1I subtract(Vector1<Integer> other) {
        return new Vector1I(x() - other.x());
    }

    @Override
    public Vector1I multiply(Vector1<Integer> other) {
        return new Vector1I(x() * other.x());
    }

    @Override
    public Vector1I multiply(Integer scalar) {
        return new Vector1I(x() * scalar);
    }

    @Override
    public Vector1I divide(Vector1<Integer> other) {
        return new Vector1I(x() / other.x());
    }

    @Override
    public Vector1I negated() {
        return new Vector1I(-x());
    }

    @Override
    public boolean lessThan(Vector1<Integer> other) {
        return x() < other.x();
    }

    @Override
    public boolean lessThanEqual(Vector1<Integer> other) {
        return x() <= other.x();
    }

    @Override
    public boolean greaterThan(Vector1<Integer> other) {
        return x() > other.x();
    }

    @Override
    public boolean greaterThanEqual(Vector1<Integer> other) {
        return x() >= other.x();
    }

    @Override
    public Vector1I abs() {
        return new Vector1I(Math.abs(x()));
    }

    @Override
    public Vector1I max(Vector1<Integer> other) {
        return new Vector1I(Math.max(x(), other.x()));
    }

    @Override
    public Vector1I min(Vector1<Integer> other) {
        return new Vector1I(Math.min(x(), other.x()));
    }

    @Override
    public Vector1I clamp(Integer min, Integer max) {
        return new Vector1I(Math.clamp(x(), min, max));
    }

    @Override
    public Vector1I signum() {
        return new Vector1I(Integer.signum(x()));
    }

    @Override
    public Vector1I clamp(UnaryTuple1<Integer> min, UnaryTuple1<Integer> max) {
        return new Vector1I(Math.clamp(x(), min.x(), max.x()));
    }

    @Override
    public Integer distance(Vector1<Integer> vector) {
        return Math.abs(x() - vector.x());
    }

    @Override
    public Integer distanceSquared(Vector1<Integer> other) {
        final int distance = x() - other.x();
        return distance * distance;
    }

    @Override
    public Integer length() {
        return Math.abs(x());
    }

    @Override
    public Integer lengthSquared() {
        return x() * x();
    }

    private Vector1I withMagnitudeSquared(int wanted, int current) {
        // TODO Need to check that the length is actually the one wanted after the round.
        final float scalar = (float) Math.sqrt((float) wanted / current);
        return multiply(Math.round(scalar));
    }

    private Vector1I withMagnitude(int wanted, int current) {
        // TODO Need to check that the length is actually the one wanted after the round.
        final float scalar = (float) wanted / current;
        return multiply(Math.round(scalar));
    }

    @Override
    public Vector1I withLength(Integer length) {
        final int current = length();
        if (current == 0 || current == length) return this;
        return withMagnitude(length, current);
    }

    @Override
    public Vector1I withLengthSquared(Integer lengthSquared) {
        final int current = lengthSquared();
        if (current == 0 || current == lengthSquared) return this;
        return withMagnitudeSquared(lengthSquared, current);
    }

    @Override
    public Vector1I withLimit(Integer limit) {
        final int current = length();
        if (current == 0 || current <= limit) return this;
        return withMagnitude(limit, current);
    }

    @Override
    public Vector1I withLimitSquared(Integer limitSquared) {
        final int current = lengthSquared();
        if (current == 0 || current <= limitSquared) return this;
        return withMagnitudeSquared(limitSquared, current);
    }

    @Override
    public Integer dot(Vector1<Integer> other) {
        return x() * other.x();
    }

    @Override
    public Vector1I normalized() {
        return withMagnitude(1, length());
    }

    @Override
    public Vector1I normalized(Vector1<Integer> orElse) {
        final int current = lengthSquared();
        if (current == 0) return new Vector1I(orElse);
        return withMagnitudeSquared(1, current);
    }

    @Override
    public Vector1I ceil() {
        return this;
    }

    @Override
    public Vector1I floor() {
        return this;
    }

    /// Interpolates `this` and the `other` vector with a range `[0,maxAlpha]`.
    /// @apiNote Alpha is not clamped and can be used above and bellow the maximum and minimum values.
    public Vector1I interpolate(Vector1<Integer> target, int alpha, int maxAlpha, UnaryOperator<Integer> easing) {
        final int eased = easing.apply(alpha);
        final int invAlpha = maxAlpha - eased;
        final int x = (x() * invAlpha + target.x() * eased) / maxAlpha;
        return new Vector1I(x);
    }

    /// Interpolates `this` and the `other` vector with a range `[0,100]`.
    ///
    /// To have more control over the max alpha [#interpolate(Vector1, int, int, UnaryOperator)] can be used.
    /// @param target the vector to interpolate towards.
    /// @param alpha the progress of the interpolation.
    /// @param easing a function to adjust the interpolation curve ([identity][UnaryOperator#identity()] for linear).
    /// @return the interpolated vector.
    /// @apiNote Alpha is not clamped and can be used above and bellow the maximum and minimum values.
    @Override
    public Vector1I interpolate(Vector1<Integer> target, Integer alpha, UnaryOperator<Integer> easing) {
        return interpolate(target, alpha, 100, easing);
    }

    /// Linearly interpolates `this` and the `other` with a range `[0,maxAlpha]`.
    /// @param target the vector to interpolate towards.
    /// @param alpha the progress of the interpolation.
    /// @return the interpolated vector.
    /// @apiNote Alpha is not clamped and can be used above and bellow the maximum and minimum values.
    public Vector1I lerp(Vector1<Integer> target, int alpha, int maxAlpha) {
        return interpolate(target, alpha, maxAlpha, UnaryOperator.identity());
    }

    @Override
    public boolean equals(UnaryTuple1<Integer> other, Integer epsilon) {
        return Tuples.epsilonEquals(x(), other.x(), epsilon);
    }

    @Override
    public boolean componentEquals(Integer value, Integer epsilon) {
        final var vec1 = new Vector1I(value);
        return equals(vec1, epsilon);
    }
}
