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

package org.lidiuma.math.vector.v4;

import jdk.internal.vm.annotation.NullRestricted;
import org.lidiuma.math.api.tuple.UnaryTuple4;
import org.lidiuma.math.api.vector.Vector4;
import org.lidiuma.math.tuple.Tuples;
import org.lidiuma.math.vector.v3.Vector3D;
import java.util.function.UnaryOperator;
import static org.lidiuma.math.FloatingUtil.EPSILON_F32;

public value record Vector4D(
        @Override @NullRestricted Double x,
        @Override @NullRestricted Double y,
        @Override @NullRestricted Double z,
        @Override @NullRestricted Double w
) implements Vector4<Double> {

    public Vector4D(Vector4<Double> vec) {
        this(vec.x(), vec.y(), vec.z(), vec.w());
    }

    @Override
    public Vector4D add(Vector4<Double> other) {
        return new Vector4D(
                x() + other.x(),
                y() + other.y(),
                z() + other.z(),
                w() + other.w()
        );
    }

    @Override
    public Vector4D subtract(Vector4<Double> other) {
        return new Vector4D(
                x() - other.x(),
                y() - other.y(),
                z() - other.z(),
                w() - other.w()
        );
    }

    @Override
    public Vector4D multiply(Vector4<Double> other) {
        return new Vector4D(
                x() * other.x(),
                y() * other.y(),
                z() * other.z(),
                w() * other.w()
        );
    }

    @Override
    public Vector4D multiply(Double scalar) {
        return new Vector4D(
                x() * scalar,
                y() * scalar,
                z() * scalar,
                w() * scalar
        );
    }

    @Override
    public Vector4D divide(Vector4<Double> other) {
        return new Vector4D(
                x() / other.x(),
                y() / other.y(),
                z() / other.z(),
                w() / other.w()
        );
    }

    @Override
    public Vector4D negated() {
        return new Vector4D(-x(), -y(), -z(), -w());
    }

    @Override
    public boolean lessThan(Vector4<Double> other) {
        return x() < other.x()
            && y() < other.y()
            && z() < other.z()
            && w() < other.w();
    }

    @Override
    public boolean lessThanEqual(Vector4<Double> other) {
        return x() <= other.x()
            && y() <= other.y()
            && z() <= other.z()
            && w() <= other.w();
    }

    @Override
    public boolean greaterThan(Vector4<Double> other) {
        return x() > other.x()
            && y() > other.y()
            && z() > other.z()
            && w() > other.w();
    }

    @Override
    public boolean greaterThanEqual(Vector4<Double> other) {
        return x() >= other.x()
            && y() >= other.y()
            && z() >= other.z()
            && w() >= other.w();
    }

    @Override
    public Vector4D abs() {
        return new Vector4D(
                Math.abs(x()),
                Math.abs(y()),
                Math.abs(z()),
                Math.abs(w())
        );
    }

    @Override
    public Vector4D signum() {
        return new Vector4D(
                Math.signum(x()),
                Math.signum(y()),
                Math.signum(z()),
                Math.signum(w())
        );
    }

    @Override
    public Vector4D max(Vector4<Double> other) {
        return new Vector4D(
                Math.max(x(), other.x()),
                Math.max(y(), other.y()),
                Math.max(z(), other.z()),
                Math.max(w(), other.w())
        );
    }

    @Override
    public Vector4D min(Vector4<Double> other) {
        return new Vector4D(
                Math.min(x(), other.x()),
                Math.min(y(), other.y()),
                Math.min(z(), other.z()),
                Math.min(w(), other.w())
        );
    }

    @Override
    public Vector4D clamp(Double min, Double max) {
        return new Vector4D(
                Math.clamp(x(), min, max),
                Math.clamp(y(), min, max),
                Math.clamp(z(), min, max),
                Math.clamp(w(), min, max)
        );
    }

    @Override
    public Vector4D clamp(UnaryTuple4<Double> min, UnaryTuple4<Double> max) {
        return new Vector4D(
                Math.clamp(x(), min.x(), max.x()),
                Math.clamp(y(), min.y(), max.y()),
                Math.clamp(z(), min.z(), max.z()),
                Math.clamp(w(), min.w(), max.w())
        );
    }

    @Override
    public Vector4D ceil() {
        return new Vector4D(
                Math.ceil(x()),
                Math.ceil(y()),
                Math.ceil(z()),
                Math.ceil(w())
        );
    }

    @Override
    public Vector4D floor() {
        return new Vector4D(
                Math.floor(x()),
                Math.floor(y()),
                Math.floor(z()),
                Math.floor(w())
        );
    }

    @Override
    public Double distance(Vector4<Double> other) {
        return Math.sqrt(distanceSquared(other));
    }

    @Override
    public Double distanceSquared(Vector4<Double> other) {
        final var delta = subtract(other);
        final var squared = delta.multiply(delta);
        return squared.sum();
    }

    @Override
    public Double length() {
        return Math.sqrt(lengthSquared());
    }

    @Override
    public Double lengthSquared() {
        return multiply(this).sum();
    }

    private Vector4D withMagnitudeSquared(double wanted, double current) {
        final double scalar = Math.sqrt(wanted / current);
        return multiply(scalar);
    }

    private Vector4D withMagnitude(double wanted, double current) {
        final double scalar = wanted / current;
        return multiply(scalar);
    }

    @Override
    public Vector4D withLength(Double length) {
        final double current = length();
        if (current == 0 || current == length) return this;
        return withMagnitude(length, current);
    }

    @Override
    public Vector4D withLengthSquared(Double lengthSquared) {
        final double current = lengthSquared();
        if (current == 0 || current == lengthSquared) return this;
        return withMagnitudeSquared(lengthSquared, current);
    }

    @Override
    public Vector4D withLimit(Double limit) {
        final double current = length();
        if (current == 0 || current <= limit) return this;
        return withMagnitude(limit, current);
    }

    @Override
    public Vector4D withLimitSquared(Double limitSquared) {
        final double current = lengthSquared();
        if (current == 0 || current <= limitSquared) return this;
        return withMagnitudeSquared(limitSquared, current);
    }

    @Override
    public Double dot(Vector4<Double> other) {
        return multiply(other).sum();
    }

    @Override
    public Vector4D normalized() {
        return withLength(1d);
    }

    @Override
    public Vector4D normalized(Vector4<Double> orElse) {
        final double current = lengthSquared();
        if (current <= EPSILON_F32 * EPSILON_F32) return new Vector4D(orElse);
        return withMagnitudeSquared(1d, current);
    }

    @Override
    public Vector4D interpolate(Vector4<Double> target, Double alpha, UnaryOperator<Double> easing) {

        final double eased = easing.apply(alpha);
        final double invAlpha = 1d - eased;

        final double x = x() * invAlpha + target.x() * eased;
        final double y = y() * invAlpha + target.y() * eased;
        final double z = z() * invAlpha + target.z() * eased;
        final double w = w() * invAlpha + target.w() * eased;
        return new Vector4D(x, y, z, w);
    }

    @Override
    public Vector3D withoutW() {
        return new Vector3D(x(), y(), z());
    }

    @Override
    public boolean equals(UnaryTuple4<Double> other, Double epsilon) {
        return Tuples.epsilonEquals(x(), other.x(), epsilon) &&
               Tuples.epsilonEquals(y(), other.y(), epsilon) &&
               Tuples.epsilonEquals(z(), other.z(), epsilon) &&
               Tuples.epsilonEquals(w(), other.w(), epsilon);
    }

    @Override
    public boolean componentEquals(Double value, Double epsilon) {
        final var vec = new Vector4D(value, value, value, value);
        return equals(vec, epsilon);
    }

    /// @return all this vector components added together.
    private double sum() {
        return x() + y() + z() + w();
    }
}
