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

package org.lidiuma.math.vector;

import jdk.internal.vm.annotation.NullRestricted;
import org.lidiuma.math.api.rotation.Angle;
import org.lidiuma.math.api.tuple.UnaryTuple3;
import org.lidiuma.math.api.vector.Vector3;
import org.lidiuma.math.tuple.Tuples;

import java.util.function.UnaryOperator;
import static org.lidiuma.math.FloatingUtil.EPSILON_F32;

public value record Vector3F(
        @Override @NullRestricted Float x,
        @Override @NullRestricted Float y,
        @Override @NullRestricted Float z
) implements Vector3<Float> {

    public Vector3F(Vector3<Float> vec) {
        this(vec.x(), vec.y(), vec.z());
    }

    @Override
    public Vector3F add(Vector3<Float> other) {
        return new Vector3F(
                x() + other.x(),
                y() + other.y(),
                z() + other.z()
        );
    }

    @Override
    public Vector3F subtract(Vector3<Float> other) {
        return new Vector3F(
                x() - other.x(),
                y() - other.y(),
                z() - other.z()
        );
    }

    @Override
    public Vector3F multiply(Vector3<Float> other) {
        return new Vector3F(
                x() * other.x(),
                y() * other.y(),
                z() * other.z()
        );
    }

    @Override
    public Vector3F multiply(Float scalar) {
        return new Vector3F(
                x() * scalar,
                y() * scalar,
                z() * scalar
        );
    }

    @Override
    public Vector3F divide(Vector3<Float> other) {
        return new Vector3F(
                x() / other.x(),
                y() / other.y(),
                z() / other.z()
        );
    }

    @Override
    public Vector3F negated() {
        return new Vector3F(-x(), -y(), -z());
    }

    @Override
    public boolean lessThan(Vector3<Float> other) {
        return x() < other.x()
            && y() < other.y()
            && z() < other.z();
    }

    @Override
    public boolean lessThanEqual(Vector3<Float> other) {
        return x() <= other.x()
            && y() <= other.y()
            && z() <= other.z();
    }

    @Override
    public boolean greaterThan(Vector3<Float> other) {
        return x() > other.x()
            && y() > other.y()
            && z() > other.z();
    }

    @Override
    public boolean greaterThanEqual(Vector3<Float> other) {
        return x() >= other.x()
            && y() >= other.y()
            && z() >= other.z();
    }

    @Override
    public Vector3F abs() {
        return new Vector3F(
                Math.abs(x()),
                Math.abs(y()),
                Math.abs(z())
        );
    }

    @Override
    public Vector3F signum() {
        return new Vector3F(
                Math.signum(x()),
                Math.signum(y()),
                Math.signum(z())
        );
    }

    @Override
    public Vector3F max(Vector3<Float> other) {
        return new Vector3F(
                Math.max(x(), other.x()),
                Math.max(y(), other.y()),
                Math.max(z(), other.z())
        );
    }

    @Override
    public Vector3F min(Vector3<Float> other) {
        return new Vector3F(
                Math.min(x(), other.x()),
                Math.min(y(), other.y()),
                Math.min(z(), other.z())
        );
    }

    @Override
    public Vector3F clamp(Float min, Float max) {
        return new Vector3F(
                Math.clamp(x(), min, max),
                Math.clamp(y(), min, max),
                Math.clamp(z(), min, max)
        );
    }

    @Override
    public Vector3F clamp(UnaryTuple3<Float> min, UnaryTuple3<Float> max) {
        return new Vector3F(
                Math.clamp(x(), min.x(), max.x()),
                Math.clamp(y(), min.y(), max.y()),
                Math.clamp(z(), min.z(), max.z())
        );
    }

    @Override
    public Vector3F ceil() {
        return new Vector3F(
                (float) Math.ceil(x()),
                (float) Math.ceil(y()),
                (float) Math.ceil(z())
        );
    }

    @Override
    public Vector3F floor() {
        return new Vector3F(
                (float) Math.floor(x()),
                (float) Math.floor(y()),
                (float) Math.floor(z())
        );
    }

    @Override
    public Float distance(Vector3<Float> other) {
        return (float) Math.sqrt(distanceSquared(other));
    }

    @Override
    public Float distanceSquared(Vector3<Float> other) {
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
    public Vector3F withLength(Float length) {
        final float current = length();
        if (current == 0 || current == length) return this;
        return withMagnitude(length, current);
    }

    @Override
    public Vector3F withLengthSquared(Float lengthSquared) {
        final float current = lengthSquared();
        if (current == 0 || current == lengthSquared) return this;
        return withMagnitudeSquared(lengthSquared, current);
    }

    @Override
    public Vector3F withLimit(Float limit) {
        final float current = length();
        if (current == 0 || current <= limit) return this;
        return withMagnitude(limit, current);
    }

    @Override
    public Vector3F withLimitSquared(Float limitSquared) {
        final float current = lengthSquared();
        if (current == 0 || current <= limitSquared) return this;
        return withMagnitudeSquared(limitSquared, current);
    }

    @Override
    public Float dot(Vector3<Float> other) {
        return multiply(other).sum();
    }

    @Override
    public Vector3F cross(Vector3<Float> other) {
        final float x = y() * other.z() - z() * other.y();
        final float y = z() * other.x() - x() * other.z();
        final float z = x() * other.y() - y() * other.x();
        return new Vector3F(x, y, z);
    }

    @Override
    public Vector3F normalized() {
        return withLength(1f);
    }

    @Override
    public Vector3F normalized(Vector3<Float> orElse) {
        final float current = lengthSquared();
        if (current <= EPSILON_F32 * EPSILON_F32) return new Vector3F(orElse);
        return withMagnitudeSquared(1f, current);
    }

    @Override
    public Vector3F rotate(Vector3<Float> axis, Angle<Float> angle) {
        // TODO Use an Affine3 matrix to re-use the math.
        return null;
    }

    @Override
    public Vector3F interpolate(Vector3<Float> target, Float alpha, UnaryOperator<Float> easing) {

        final float eased = easing.apply(alpha);
        final float invAlpha = 1f - eased;

        final float x = x() * invAlpha + target.x() * eased;
        final float y = y() * invAlpha + target.y() * eased;
        final float z = z() * invAlpha + target.z() * eased;
        return new Vector3F(x, y, z);
    }

    @Override
    public Vector2F withoutZ() {
        return new Vector2F(x(), y());
    }

    @Override
    public boolean equals(UnaryTuple3<Float> other, Float epsilon) {
        return Tuples.epsilonEquals(x(), other.x(), epsilon) &&
               Tuples.epsilonEquals(y(), other.y(), epsilon) &&
               Tuples.epsilonEquals(z(), other.z(), epsilon);
    }

    @Override
    public boolean componentEquals(Float value, Float epsilon) {
        final var vec = new Vector3F(value, value, value);
        return equals(vec, epsilon);
    }

    /// @return all this vector components added together.
    private float sum() {
        return x() + y() + z();
    }

    private Vector3F withMagnitudeSquared(float wanted, float current) {
        final float scalar = (float) Math.sqrt(wanted / current);
        return multiply(scalar);
    }

    private Vector3F withMagnitude(float wanted, float current) {
        final float scalar = wanted / current;
        return multiply(scalar);
    }
}
