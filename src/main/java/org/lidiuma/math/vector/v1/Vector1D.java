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
import static org.lidiuma.math.FloatingUtil.EPSILON_F32;

public value record Vector1D(
        @Override @NullRestricted Double x
) implements Vector1<Double> {

    /// A constructor creating a specialized vector from a generic vector.
    public Vector1D(Vector1<Double> vec) {
        this(vec.x());
    }

    @Override
    public Vector1D add(Vector1<Double> other) {
        return new Vector1D(x() + other.x());
    }

    @Override
    public Vector1D subtract(Vector1<Double> other) {
        return new Vector1D(x() - other.x());
    }

    @Override
    public Vector1D multiply(Vector1<Double> other) {
        return new Vector1D(x() * other.x());
    }

    @Override
    public Vector1D multiply(Double scalar) {
        return new Vector1D(x() * scalar);
    }

    @Override
    public Vector1D divide(Vector1<Double> other) {
        return new Vector1D(x() / other.x());
    }

    @Override
    public Vector1D negated() {
        return new Vector1D(-x());
    }

    @Override
    public boolean lessThan(Vector1<Double> other) {
        return x() < other.x();
    }

    @Override
    public boolean lessThanEqual(Vector1<Double> other) {
        return x() <= other.x();
    }

    @Override
    public boolean greaterThan(Vector1<Double> other) {
        return x() > other.x();
    }

    @Override
    public boolean greaterThanEqual(Vector1<Double> other) {
        return x() >= other.x();
    }

    @Override
    public Vector1D abs() {
        return new Vector1D(Math.abs(x()));
    }

    @Override
    public Vector1D signum() {
        return new Vector1D(Math.signum(x()));
    }

    @Override
    public Vector1D max(Vector1<Double> other) {
        return new Vector1D(Math.max(x(), other.x()));
    }

    @Override
    public Vector1D min(Vector1<Double> other) {
        return new Vector1D(Math.min(x(), other.x()));
    }

    @Override
    public Vector1D clamp(Double min, Double max) {
        return new Vector1D(Math.clamp(x(), min, max));
    }

    @Override
    public Vector1D clamp(UnaryTuple1<Double> min, UnaryTuple1<Double> max) {
        return new Vector1D(Math.clamp(x(), min.x(), max.x()));
    }

    @Override
    public Vector1D ceil() {
        return new Vector1D(Math.ceil(x()));
    }

    @Override
    public Vector1D floor() {
        return new Vector1D(Math.floor(x()));
    }

    @Override
    public Double distance(Vector1<Double> vector) {
        return Math.abs(x() - vector.x());
    }

    @Override
    public Double distanceSquared(Vector1<Double> other) {
        final double distance = x() - other.x();
        return distance * distance;
    }

    @Override
    public Double length() {
        return Math.abs(x());
    }

    @Override
    public Double lengthSquared() {
        return x() * x();
    }

    @Override
    public Vector1D withLength(Double length) {
        final double current = length();
        if (current == 0 || current == length) return this;
        return signum().multiply(length);
    }

    @Override
    public Vector1D withLengthSquared(Double lengthSquared) {
        final double current = lengthSquared();
        if (current == 0 || current == lengthSquared) return this;
        return signum().multiply(Math.sqrt(lengthSquared));
    }

    @Override
    public Vector1D withLimit(Double limit) {
        final double current = length();
        if (current == 0 || current <= limit) return this;
        return signum().multiply(limit);
    }

    @Override
    public Vector1D withLimitSquared(Double limitSquared) {
        final double current = lengthSquared();
        if (current == 0 || current <= limitSquared) return this;
        return signum().multiply(Math.sqrt(limitSquared));
    }

    @Override
    public Double dot(Vector1<Double> other) {
        return x() * other.x();
    }

    @Override
    public Vector1D normalized() {
        return signum();
    }

    @Override
    public Vector1D normalized(Vector1<Double> orElse) {
        final double current = lengthSquared();
        if (current <= EPSILON_F32 * EPSILON_F32) return new Vector1D(orElse);
        return signum();
    }

    @Override
    public Vector1D interpolate(Vector1<Double> target, Double alpha, UnaryOperator<Double> easing) {
        final double eased = easing.apply(alpha);
        final double x = x() * (1d - eased) + target.x() * eased;
        return new Vector1D(x);
    }

    @Override
    public boolean equals(UnaryTuple1<Double> other, Double epsilon) {
        return Tuples.epsilonEquals(x(), other.x(), epsilon);
    }

    @Override
    public boolean componentEquals(Double value, Double epsilon) {
        final var vec1 = new Vector1D(value);
        return equals(vec1, epsilon);
    }
}
