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

package org.lidiuma.math.vector.v3;

import jdk.internal.vm.annotation.NullRestricted;
import org.lidiuma.math.api.rotation.Angle;
import org.lidiuma.math.api.tuple.UnaryTuple3;
import org.lidiuma.math.api.vector.Vector3;
import org.lidiuma.math.tuple.Tuples;
import org.lidiuma.math.vector.v2.Vector2D;
import java.util.function.UnaryOperator;
import static org.lidiuma.math.FloatingUtil.EPSILON_F32;

public value record Vector3D(
        @Override @NullRestricted Double x,
        @Override @NullRestricted Double y,
        @Override @NullRestricted Double z
) implements Vector3<Double> {

    public Vector3D(Vector3<Double> vec) {
        this(vec.x(), vec.y(), vec.z());
    }

    @Override
    public Vector3D add(Vector3<Double> other) {
        return new Vector3D(
                x() + other.x(),
                y() + other.y(),
                z() + other.z()
        );
    }

    @Override
    public Vector3D subtract(Vector3<Double> other) {
        return new Vector3D(
                x() - other.x(),
                y() - other.y(),
                z() - other.z()
        );
    }

    @Override
    public Vector3D multiply(Vector3<Double> other) {
        return new Vector3D(
                x() * other.x(),
                y() * other.y(),
                z() * other.z()
        );
    }

    @Override
    public Vector3D multiply(Double scalar) {
        return new Vector3D(
                x() * scalar,
                y() * scalar,
                z() * scalar
        );
    }

    @Override
    public Vector3D divide(Vector3<Double> other) {
        return new Vector3D(
                x() / other.x(),
                y() / other.y(),
                z() / other.z()
        );
    }

    @Override
    public Vector3D negated() {
        return new Vector3D(-x(), -y(), -z());
    }

    @Override
    public boolean lessThan(Vector3<Double> other) {
        return x() < other.x()
            && y() < other.y()
            && z() < other.z();
    }

    @Override
    public boolean lessThanEqual(Vector3<Double> other) {
        return x() <= other.x()
            && y() <= other.y()
            && z() <= other.z();
    }

    @Override
    public boolean greaterThan(Vector3<Double> other) {
        return x() > other.x()
            && y() > other.y()
            && z() > other.z();
    }

    @Override
    public boolean greaterThanEqual(Vector3<Double> other) {
        return x() >= other.x()
            && y() >= other.y()
            && z() >= other.z();
    }

    @Override
    public Vector3D abs() {
        return new Vector3D(
                Math.abs(x()),
                Math.abs(y()),
                Math.abs(z())
        );
    }

    @Override
    public Vector3D signum() {
        return new Vector3D(
                Math.signum(x()),
                Math.signum(y()),
                Math.signum(z())
        );
    }

    @Override
    public Vector3D max(Vector3<Double> other) {
        return new Vector3D(
                Math.max(x(), other.x()),
                Math.max(y(), other.y()),
                Math.max(z(), other.z())
        );
    }

    @Override
    public Vector3D min(Vector3<Double> other) {
        return new Vector3D(
                Math.min(x(), other.x()),
                Math.min(y(), other.y()),
                Math.min(z(), other.z())
        );
    }

    @Override
    public Vector3D clamp(Double min, Double max) {
        return new Vector3D(
                Math.clamp(x(), min, max),
                Math.clamp(y(), min, max),
                Math.clamp(z(), min, max)
        );
    }

    @Override
    public Vector3D clamp(UnaryTuple3<Double> min, UnaryTuple3<Double> max) {
        return new Vector3D(
                Math.clamp(x(), min.x(), max.x()),
                Math.clamp(y(), min.y(), max.y()),
                Math.clamp(z(), min.z(), max.z())
        );
    }

    @Override
    public Vector3D ceil() {
        return new Vector3D(
                Math.ceil(x()),
                Math.ceil(y()),
                Math.ceil(z())
        );
    }

    @Override
    public Vector3D floor() {
        return new Vector3D(
                Math.floor(x()),
                Math.floor(y()),
                Math.floor(z())
        );
    }

    @Override
    public Double distance(Vector3<Double> other) {
        return Math.sqrt(distanceSquared(other));
    }

    @Override
    public Double distanceSquared(Vector3<Double> other) {
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

    private Vector3D withMagnitudeSquared(double wanted, double current) {
        final double scalar = Math.sqrt(wanted / current);
        return multiply(scalar);
    }

    private Vector3D withMagnitude(double wanted, double current) {
        final double scalar = wanted / current;
        return multiply(scalar);
    }

    @Override
    public Vector3D withLength(Double length) {
        final double current = length();
        if (current == 0 || current == length) return this;
        return withMagnitude(length, current);
    }

    @Override
    public Vector3D withLengthSquared(Double lengthSquared) {
        final double current = lengthSquared();
        if (current == 0 || current == lengthSquared) return this;
        return withMagnitudeSquared(lengthSquared, current);
    }

    @Override
    public Vector3D withLimit(Double limit) {
        final double current = length();
        if (current == 0 || current <= limit) return this;
        return withMagnitude(limit, current);
    }

    @Override
    public Vector3D withLimitSquared(Double limitSquared) {
        final double current = lengthSquared();
        if (current == 0 || current <= limitSquared) return this;
        return withMagnitudeSquared(limitSquared, current);
    }

    @Override
    public Double dot(Vector3<Double> other) {
        return multiply(other).sum();
    }

    @Override
    public Vector3D cross(Vector3<Double> other) {
        final double x = y() * other.z() - z() * other.y();
        final double y = z() * other.x() - x() * other.z();
        final double z = x() * other.y() - y() * other.x();
        return new Vector3D(x, y, z);
    }

    @Override
    public Vector3D normalized() {
        return withLength(1d);
    }

    @Override
    public Vector3D normalized(Vector3<Double> orElse) {
        final double current = lengthSquared();
        if (current <= EPSILON_F32 * EPSILON_F32) return new Vector3D(orElse);
        return withMagnitudeSquared(1d, current);
    }

    @Override
    public Vector3D rotate(Vector3<Double> axis, Angle<Double> angle) {
        // TODO Use an Affine3 matrix to re-use the math.
        return null;
    }

    @Override
    public Vector3D interpolate(Vector3<Double> target, Double alpha, UnaryOperator<Double> easing) {

        final double eased = easing.apply(alpha);
        final double invAlpha = 1d - eased;

        final double x = x() * invAlpha + target.x() * eased;
        final double y = y() * invAlpha + target.y() * eased;
        final double z = z() * invAlpha + target.z() * eased;
        return new Vector3D(x, y, z);
    }

    @Override
    public Vector2D withoutZ() {
        return new Vector2D(x(), y());
    }

    @Override
    public boolean equals(UnaryTuple3<Double> other, Double epsilon) {
        return Tuples.epsilonEquals(x(), other.x(), epsilon) &&
               Tuples.epsilonEquals(y(), other.y(), epsilon) &&
               Tuples.epsilonEquals(z(), other.z(), epsilon);
    }

    @Override
    public boolean componentEquals(Double value, Double epsilon) {
        final var vec = new Vector3D(value, value, value);
        return equals(vec, epsilon);
    }

    /// @return all this vector components added together.
    private double sum() {
        return x() + y() + z();
    }
}
