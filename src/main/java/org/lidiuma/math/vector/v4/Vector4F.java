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
import org.lidiuma.math.vector.v3.Vector3F;
import java.util.function.UnaryOperator;
import static org.lidiuma.math.FloatingUtil.EPSILON_F32;

public value record Vector4F(
        @Override @NullRestricted Float x,
        @Override @NullRestricted Float y,
        @Override @NullRestricted Float z,
        @Override @NullRestricted Float w
) implements Vector4<Float> {

    public Vector4F(Vector4<Float> vec) {
        this(vec.x(), vec.y(), vec.z(), vec.w());
    }

    @Override
    public Vector4F add(Vector4<Float> other) {
        return new Vector4F(
                x() + other.x(),
                y() + other.y(),
                z() + other.z(),
                w() + other.w()
        );
    }

    @Override
    public Vector4F subtract(Vector4<Float> other) {
        return new Vector4F(
                x() - other.x(),
                y() - other.y(),
                z() - other.z(),
                w() - other.w()
        );
    }

    @Override
    public Vector4F multiply(Vector4<Float> other) {
        return new Vector4F(
                x() * other.x(),
                y() * other.y(),
                z() * other.z(),
                w() * other.w()
        );
    }

    @Override
    public Vector4F multiply(Float scalar) {
        return new Vector4F(
                x() * scalar,
                y() * scalar,
                z() * scalar,
                w() * scalar
        );
    }

    @Override
    public Vector4F divide(Vector4<Float> other) {
        return new Vector4F(
                x() / other.x(),
                y() / other.y(),
                z() / other.z(),
                w() / other.w()
        );
    }

    @Override
    public Vector4F negated() {
        return new Vector4F(-x(), -y(), -z(), -w());
    }

    @Override
    public boolean lessThan(Vector4<Float> other) {
        return x() < other.x()
            && y() < other.y()
            && z() < other.z()
            && w() < other.w();
    }

    @Override
    public boolean lessThanEqual(Vector4<Float> other) {
        return x() <= other.x()
            && y() <= other.y()
            && z() <= other.z()
            && w() <= other.w();
    }

    @Override
    public boolean greaterThan(Vector4<Float> other) {
        return x() > other.x()
            && y() > other.y()
            && z() > other.z()
            && w() > other.w();
    }

    @Override
    public boolean greaterThanEqual(Vector4<Float> other) {
        return x() >= other.x()
            && y() >= other.y()
            && z() >= other.z()
            && w() >= other.w();
    }

    @Override
    public Vector4F abs() {
        return new Vector4F(
                Math.abs(x()),
                Math.abs(y()),
                Math.abs(z()),
                Math.abs(w())
        );
    }

    @Override
    public Vector4F signum() {
        return new Vector4F(
                Math.signum(x()),
                Math.signum(y()),
                Math.signum(z()),
                Math.signum(w())
        );
    }

    @Override
    public Vector4F max(Vector4<Float> other) {
        return new Vector4F(
                Math.max(x(), other.x()),
                Math.max(y(), other.y()),
                Math.max(z(), other.z()),
                Math.max(w(), other.w())
        );
    }

    @Override
    public Vector4F min(Vector4<Float> other) {
        return new Vector4F(
                Math.min(x(), other.x()),
                Math.min(y(), other.y()),
                Math.min(z(), other.z()),
                Math.min(w(), other.w())
        );
    }

    @Override
    public Vector4F clamp(Float min, Float max) {
        return new Vector4F(
                Math.clamp(x(), min, max),
                Math.clamp(y(), min, max),
                Math.clamp(z(), min, max),
                Math.clamp(w(), min, max)
        );
    }

    @Override
    public Vector4F clamp(UnaryTuple4<Float> min, UnaryTuple4<Float> max) {
        return new Vector4F(
                Math.clamp(x(), min.x(), max.x()),
                Math.clamp(y(), min.y(), max.y()),
                Math.clamp(z(), min.z(), max.z()),
                Math.clamp(w(), min.w(), max.w())
        );
    }

    @Override
    public Vector4F ceil() {
        return new Vector4F(
                (float) Math.ceil(x()),
                (float) Math.ceil(y()),
                (float) Math.ceil(z()),
                (float) Math.ceil(w())
        );
    }

    @Override
    public Vector4F floor() {
        return new Vector4F(
                (float) Math.floor(x()),
                (float) Math.floor(y()),
                (float) Math.floor(z()),
                (float) Math.floor(w())
        );
    }

    @Override
    public Float distance(Vector4<Float> other) {
        return (float) Math.sqrt(distanceSquared(other));
    }

    @Override
    public Float distanceSquared(Vector4<Float> other) {
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

    private Vector4F withMagnitudeSquared(float wanted, float current) {
        final float scalar = (float) Math.sqrt(wanted / current);
        return multiply(scalar);
    }

    private Vector4F withMagnitude(float wanted, float current) {
        final float scalar = wanted / current;
        return multiply(scalar);
    }

    @Override
    public Vector4F withLength(Float length) {
        final float current = length();
        if (current == 0 || current == length) return this;
        return withMagnitude(length, current);
    }

    @Override
    public Vector4F withLengthSquared(Float lengthSquared) {
        final float current = lengthSquared();
        if (current == 0 || current == lengthSquared) return this;
        return withMagnitudeSquared(lengthSquared, current);
    }

    @Override
    public Vector4F withLimit(Float limit) {
        final float current = length();
        if (current == 0 || current <= limit) return this;
        return withMagnitude(limit, current);
    }

    @Override
    public Vector4F withLimitSquared(Float limitSquared) {
        final float current = lengthSquared();
        if (current == 0 || current <= limitSquared) return this;
        return withMagnitudeSquared(limitSquared, current);
    }

    @Override
    public Float dot(Vector4<Float> other) {
        return multiply(other).sum();
    }

    @Override
    public Vector4F normalized() {
        return withLength(1f);
    }

    @Override
    public Vector4F normalized(Vector4<Float> orElse) {
        final float current = lengthSquared();
        if (current <= EPSILON_F32 * EPSILON_F32) return new Vector4F(orElse);
        return withMagnitudeSquared(1f, current);
    }

    @Override
    public Vector4F interpolate(Vector4<Float> target, Float alpha, UnaryOperator<Float> easing) {

        final float eased = easing.apply(alpha);
        final float invAlpha = 1f - eased;

        final float x = x() * invAlpha + target.x() * eased;
        final float y = y() * invAlpha + target.y() * eased;
        final float z = z() * invAlpha + target.z() * eased;
        final float w = w() * invAlpha + target.w() * eased;
        return new Vector4F(x, y, z, w);
    }

    @Override
    public Vector3F withoutW() {
        return new Vector3F(x(), y(), z());
    }

    @Override
    public boolean equals(UnaryTuple4<Float> other, Float epsilon) {
        return Tuples.epsilonEquals(x(), other.x(), epsilon) &&
               Tuples.epsilonEquals(y(), other.y(), epsilon) &&
               Tuples.epsilonEquals(z(), other.z(), epsilon) &&
               Tuples.epsilonEquals(w(), other.w(), epsilon);
    }

    @Override
    public boolean componentEquals(Float value, Float epsilon) {
        final var vec = new Vector4F(value, value, value, value);
        return equals(vec, epsilon);
    }

    /// @return all this vector components added together.
    private float sum() {
        return x() + y() + z() + w();
    }
}
