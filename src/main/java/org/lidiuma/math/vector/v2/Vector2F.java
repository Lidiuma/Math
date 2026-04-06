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

package org.lidiuma.math.vector.v2;

import jdk.internal.vm.annotation.NullRestricted;
import org.lidiuma.math.api.rotation.Angle;
import org.lidiuma.math.api.tuple.UnaryTuple2;
import org.lidiuma.math.api.vector.Vector2;
import org.lidiuma.math.rotation.AngleF;
import org.lidiuma.math.tuple.Tuples;
import org.lidiuma.math.vector.v1.Vector1F;
import java.util.function.UnaryOperator;
import static org.lidiuma.math.FloatingUtil.EPSILON_F32;

public value record Vector2F(
        @Override @NullRestricted Float x,
        @Override @NullRestricted Float y
) implements Vector2<Float> {

    public Vector2F(Vector2<Float> vec) {
        this(vec.x(), vec.y());
    }

    @Override
    public Vector2F add(Vector2<Float> other) {
        return new Vector2F(
                x() + other.x(),
                y() + other.y()
        );
    }

    @Override
    public Vector2F subtract(Vector2<Float> other) {
        return new Vector2F(
                x() - other.x(),
                y() - other.y()
        );
    }

    @Override
    public Vector2F multiply(Vector2<Float> other) {
        return new Vector2F(
                x() * other.x(),
                y() * other.y()
        );
    }

    @Override
    public Vector2F multiply(Float scalar) {
        return new Vector2F(
                x() * scalar,
                y() * scalar
        );
    }

    @Override
    public Vector2F divide(Vector2<Float> other) {
        return new Vector2F(
                x() / other.x(),
                y() / other.y()
        );
    }

    @Override
    public Vector2F negated() {
        return new Vector2F(-x(), -y());
    }

    @Override
    public boolean lessThan(Vector2<Float> other) {
        return x() < other.x() && y() < other.y();
    }

    @Override
    public boolean lessThanEqual(Vector2<Float> other) {
        return x() <= other.x() && y() <= other.y();
    }

    @Override
    public boolean greaterThan(Vector2<Float> other) {
        return x() > other.x() && y() > other.y();
    }

    @Override
    public boolean greaterThanEqual(Vector2<Float> other) {
        return x() >= other.x() && y() >= other.y();
    }

    @Override
    public Vector2F abs() {
        return new Vector2F(
                Math.abs(x()),
                Math.abs(y())
        );
    }

    @Override
    public Vector2F signum() {
        return new Vector2F(
                Math.signum(x()),
                Math.signum(y())
        );
    }

    @Override
    public Vector2F max(Vector2<Float> other) {
        return new Vector2F(
                Math.max(x(), other.x()),
                Math.max(y(), other.y())
        );
    }

    @Override
    public Vector2F min(Vector2<Float> other) {
        return new Vector2F(
                Math.min(x(), other.x()),
                Math.min(y(), other.y())
        );
    }

    @Override
    public Vector2F clamp(Float min, Float max) {
        return new Vector2F(
                Math.clamp(x(), min, max),
                Math.clamp(y(), min, max)
        );
    }

    @Override
    public Vector2F clamp(UnaryTuple2<Float> min, UnaryTuple2<Float> max) {
        return new Vector2F(
                Math.clamp(x(), min.x(), max.x()),
                Math.clamp(y(), min.y(), max.y())
        );
    }

    @Override
    public Vector2F ceil() {
        return new Vector2F(
                (float) Math.ceil(x()),
                (float) Math.ceil(y())
        );
    }

    @Override
    public Vector2F floor() {
        return new Vector2F(
                (float) Math.floor(x()),
                (float) Math.floor(y())
        );
    }

    @Override
    public Float distance(Vector2<Float> other) {
        return (float) Math.sqrt(distanceSquared(other));
    }

    @Override
    public Float distanceSquared(Vector2<Float> other) {
        final var delta = subtract(other);
        final var squared = delta.multiply(delta);
        return squared.sum();
    }

    @Override
    public Float length() {
        return (float) Math.sqrt(lengthSquared());
    }

    @Override
    public Float lengthSquared() {
        return multiply(this).sum();
    }

    @Override
    public Vector2F withLength(Float length) {
        final float current = length();
        if (current == 0 || current == length) return this;
        return withMagnitude(length, current);
    }

    @Override
    public Vector2F withLengthSquared(Float lengthSquared) {
        final float current = lengthSquared();
        if (current == 0 || current == lengthSquared) return this;
        return withMagnitudeSquared(lengthSquared, current);
    }

    @Override
    public Vector2F withLimit(Float limit) {
        final float current = length();
        if (current == 0 || current <= limit) return this;
        return withMagnitude(limit, current);
    }

    @Override
    public Vector2F withLimitSquared(Float limitSquared) {
        final float current = lengthSquared();
        if (current == 0 || current <= limitSquared) return this;
        return withMagnitudeSquared(limitSquared, current);
    }

    @Override
    public Float dot(Vector2<Float> other) {
        return multiply(other).sum();
    }

    @Override
    public Float cross(Vector2<Float> other) {
        return x() * other.y() - y() * other.x();
    }

    @Override
    public Vector2F normalized() {
        return withLength(1f);
    }

    @Override
    public Vector2F normalized(Vector2<Float> orElse) {
        final float current = lengthSquared();
        if (current <= EPSILON_F32 * EPSILON_F32) return new Vector2F(orElse);
        return withMagnitudeSquared(1f, current);
    }

    @Override
    public Vector2F rotate(Angle<Float> angle) {

        // TODO Use an Affine2 matrix to re-use the math.

        final float radian = angle.radian();
        final float cos = (float) Math.cos(radian);
        final float sin = (float) Math.sin(radian);

        final float newX = x() * cos - y() * sin;
        final float newY = x() * sin + y() * cos;

        return new Vector2F(newX, newY);
    }

    @Override
    public AngleF angle() {
        final float angle = (float) Math.atan2(y(), x());
        return AngleF.radians(angle);
    }

    @Override
    public Vector2F interpolate(Vector2<Float> target, Float alpha, UnaryOperator<Float> easing) {

        final float eased = easing.apply(alpha);
        final float invAlpha = 1f - eased;

        final float x = x() * invAlpha + target.x() * eased;
        final float y = y() * invAlpha + target.y() * eased;
        return new Vector2F(x, y);
    }

    @Override
    public Vector1F withoutY() {
        return new Vector1F(x());
    }

    @Override
    public boolean equals(UnaryTuple2<Float> other, Float epsilon) {
        return Tuples.epsilonEquals(x(), other.x(), epsilon) &&
               Tuples.epsilonEquals(y(), other.y(), epsilon);
    }

    @Override
    public boolean componentEquals(Float value, Float epsilon) {
        final var vector2 = new Vector2F(value, value);
        return equals(vector2, epsilon);
    }

    /// @return all this vector components added together.
    private float sum() {
        return x() + y();
    }

    private Vector2F withMagnitudeSquared(float wanted, float current) {
        final float scalar = (float) Math.sqrt(wanted / current);
        return multiply(scalar);
    }

    private Vector2F withMagnitude(float wanted, float current) {
        final float scalar = wanted / current;
        return multiply(scalar);
    }
}
