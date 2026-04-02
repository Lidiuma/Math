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

package org.lidiuma.math.vector.nv2;

import jdk.internal.vm.annotation.NullRestricted;
import org.lidiuma.math.api.rotation.Angle;
import org.lidiuma.math.api.tuple.UnaryTuple2;
import org.lidiuma.math.api.vector.Vector2;
import org.lidiuma.math.rotation.AngleD;
import org.lidiuma.math.tuple.Tuples;
import org.lidiuma.math.vector.v1.Vector1D;
import java.util.function.UnaryOperator;
import static org.lidiuma.math.FloatingUtil.EPSILON_F32;

public value record Vector2D(
        @Override @NullRestricted Double x,
        @Override @NullRestricted Double y
) implements Vector2<Double> {

    public Vector2D(Vector2<Double> vector2) {
        this(vector2.x(), vector2.y());
    }

    @Override
    public Vector2D add(Vector2<Double> other) {
        return new Vector2D(
                x() + other.x(),
                y() + other.y()
        );
    }

    @Override
    public Vector2D subtract(Vector2<Double> other) {
        return new Vector2D(
                x() - other.x(),
                y() - other.y()
        );
    }

    @Override
    public Vector2D multiply(Vector2<Double> other) {
        return new Vector2D(
                x() * other.x(),
                y() * other.y()
        );
    }

    @Override
    public Vector2D multiply(Double scalar) {
        return new Vector2D(
                x() * scalar,
                y() * scalar
        );
    }

    @Override
    public Vector2D divide(Vector2<Double> other) {
        return new Vector2D(
                x() / other.x(),
                y() / other.y()
        );
    }

    @Override
    public Vector2D negated() {
        return new Vector2D(-x(), -y());
    }

    @Override
    public boolean lessThan(Vector2<Double> other) {
        return x() < other.x() && y() < other.y();
    }

    @Override
    public boolean lessThanEqual(Vector2<Double> other) {
        return x() <= other.x() && y() <= other.y();
    }

    @Override
    public boolean greaterThan(Vector2<Double> other) {
        return x() > other.x() && y() > other.y();
    }

    @Override
    public boolean greaterThanEqual(Vector2<Double> other) {
        return x() >= other.x() && y() >= other.y();
    }

    @Override
    public Vector2D abs() {
        return new Vector2D(
                Math.abs(x()),
                Math.abs(y())
        );
    }

    @Override
    public Vector2D signum() {
        return new Vector2D(
                Math.signum(x()),
                Math.signum(y())
        );
    }

    @Override
    public Vector2D max(Vector2<Double> other) {
        return new Vector2D(
                Math.max(x(), other.x()),
                Math.max(y(), other.y())
        );
    }

    @Override
    public Vector2D min(Vector2<Double> other) {
        return new Vector2D(
                Math.min(x(), other.x()),
                Math.min(y(), other.y())
        );
    }

    @Override
    public Vector2D clamp(Double min, Double max) {
        return new Vector2D(
                Math.clamp(x(), min, max),
                Math.clamp(y(), min, max)
        );
    }

    @Override
    public Vector2D clamp(UnaryTuple2<Double> min, UnaryTuple2<Double> max) {
        return new Vector2D(
                Math.clamp(x(), min.x(), max.x()),
                Math.clamp(y(), min.y(), max.y())
        );
    }

    @Override
    public Vector2D ceil() {
        return new Vector2D(
                Math.ceil(x()),
                Math.ceil(y())
        );
    }

    @Override
    public Vector2D floor() {
        return new Vector2D(
                Math.floor(x()),
                Math.floor(y())
        );
    }

    /// @return all this vector components added together.
    private double sum() {
        return x() + y();
    }

    @Override
    public Double distance(Vector2<Double> other) {
        return Math.sqrt(distanceSquared(other));
    }

    @Override
    public Double distanceSquared(Vector2<Double> other) {
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

    private Vector2D withMagnitudeSquared(double wanted, double current) {
        final double scalar = Math.sqrt(wanted / current);
        return multiply(scalar);
    }

    private Vector2D withMagnitude(double wanted, double current) {
        final double scalar = wanted / current;
        return multiply(scalar);
    }

    @Override
    public Vector2D withLength(Double length) {
        final double current = length();
        if (current == 0 || current == length) return this;
        return withMagnitude(length, current);
    }

    @Override
    public Vector2D withLengthSquared(Double lengthSquared) {
        final double current = lengthSquared();
        if (current == 0 || current == lengthSquared) return this;
        return withMagnitudeSquared(lengthSquared, current);
    }

    @Override
    public Vector2D withLimit(Double limit) {
        final double current = length();
        if (current == 0 || current <= limit) return this;
        return withMagnitude(limit, current);
    }

    @Override
    public Vector2D withLimitSquared(Double limitSquared) {
        final double current = lengthSquared();
        if (current == 0 || current <= limitSquared) return this;
        return withMagnitudeSquared(limitSquared, current);
    }

    @Override
    public Double dot(Vector2<Double> other) {
        return multiply(other).sum();
    }

    @Override
    public Double cross(Vector2<Double> other) {
        return x() * other.y() - y() * other.x();
    }

    @Override
    public Vector2D normalized() {
        return withLength(1d);
    }

    @Override
    public Vector2D normalized(Vector2<Double> orElse) {
        final double current = lengthSquared();
        if (current <= EPSILON_F32 * EPSILON_F32) return new Vector2D(orElse);
        return withMagnitudeSquared(1d, current);
    }

    @Override
    public Vector2D rotate(Angle<Double> angle) {

        // TODO Use an Affine2 matrix to re-use the math.

        final double radian = angle.radian();
        final double cos = Math.cos(radian);
        final double sin = Math.sin(radian);

        final double newX = x() * cos - y() * sin;
        final double newY = x() * sin + y() * cos;

        return new Vector2D(newX, newY);
    }

    @Override
    public AngleD angle() {
        final double angle = Math.atan2(y(), x());
        return AngleD.radians(angle);
    }

    @Override
    public Vector2D interpolate(Vector2<Double> target, Double alpha, UnaryOperator<Double> easing) {

        final double eased = easing.apply(alpha);
        final double invAlpha = 1d - eased;

        final double x = x() * invAlpha + target.x() * eased;
        final double y = y() * invAlpha + target.y() * eased;
        return new Vector2D(x, y);
    }

    @Override
    public Vector1D withoutY() {
        return new Vector1D(x());
    }

    @Override
    public boolean equals(UnaryTuple2<Double> other, Double epsilon) {
        return Tuples.epsilonEquals(x(), other.x(), epsilon) &&
                Tuples.epsilonEquals(y(), other.y(), epsilon);
    }

    @Override
    public boolean componentEquals(Double value, Double epsilon) {
        final var vector2 = new Vector2D(value, value);
        return equals(vector2, epsilon);
    }
}
