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

public value record Vector1F(
        @Override @NullRestricted Float x
) implements Vector1<Float> {

    /// A constructor creating a specialized vector from a generic vector.
    public Vector1F(Vector1<Float> vec) {
        this(vec.x());
    }

    @Override
    public Vector1F add(Vector1<Float> other) {
        return new Vector1F(x() + other.x());
    }

    @Override
    public Vector1F subtract(Vector1<Float> other) {
        return new Vector1F(x() - other.x());
    }

    @Override
    public Vector1F multiply(Vector1<Float> other) {
        return new Vector1F(x() * other.x());
    }

    @Override
    public Vector1F multiply(Float scalar) {
        return new Vector1F(x() * scalar);
    }

    @Override
    public Vector1F divide(Vector1<Float> other) {
        return new Vector1F(x() / other.x());
    }

    @Override
    public Vector1F negated() {
        return new Vector1F(-x());
    }

    @Override
    public boolean lessThan(Vector1<Float> other) {
        return x() < other.x();
    }

    @Override
    public boolean lessThanEqual(Vector1<Float> other) {
        return x() <= other.x();
    }

    @Override
    public boolean greaterThan(Vector1<Float> other) {
        return x() > other.x();
    }

    @Override
    public boolean greaterThanEqual(Vector1<Float> other) {
        return x() >= other.x();
    }

    @Override
    public Vector1F abs() {
        return new Vector1F(Math.abs(x()));
    }

    @Override
    public Vector1F signum() {
        return new Vector1F(Math.signum(x()));
    }

    @Override
    public Vector1F max(Vector1<Float> other) {
        return new Vector1F(Math.max(x(), other.x()));
    }

    @Override
    public Vector1F min(Vector1<Float> other) {
        return new Vector1F(Math.min(x(), other.x()));
    }

    @Override
    public Vector1F clamp(Float min, Float max) {
        return new Vector1F(Math.clamp(x(), min, max));
    }

    @Override
    public Vector1F clamp(UnaryTuple1<Float> min, UnaryTuple1<Float> max) {
        return new Vector1F(Math.clamp(x(), min.x(), max.x()));
    }

    @Override
    public Vector1F ceil() {
        return new Vector1F((float) Math.ceil(x()));
    }

    @Override
    public Vector1F floor() {
        return new Vector1F((float) Math.floor(x()));
    }

    @Override
    public Float distance(Vector1<Float> vector) {
        return Math.abs(x() - vector.x());
    }

    @Override
    public Float distanceSquared(Vector1<Float> other) {
        final float distance = x() - other.x();
        return distance * distance;
    }

    @Override
    public Float length() {
        return Math.abs(x());
    }

    @Override
    public Float lengthSquared() {
        return x() * x();
    }

    @Override
    public Vector1F withLength(Float length) {
        final float current = length();
        if (current == 0 || current == length) return this;
        return signum().multiply(length);
    }

    @Override
    public Vector1F withLengthSquared(Float lengthSquared) {
        final float current = lengthSquared();
        if (current == 0 || current == lengthSquared) return this;
        return signum().multiply((float) Math.sqrt(lengthSquared));
    }

    @Override
    public Vector1F withLimit(Float limit) {
        final float current = length();
        if (current == 0 || current <= limit) return this;
        return signum().multiply(limit);
    }

    @Override
    public Vector1F withLimitSquared(Float limitSquared) {
        final float current = lengthSquared();
        if (current == 0 || current <= limitSquared) return this;
        return signum().multiply((float) Math.sqrt(limitSquared));
    }

    @Override
    public Float dot(Vector1<Float> other) {
        return x() * other.x();
    }

    @Override
    public Vector1F normalized() {
        return signum();
    }

    @Override
    public Vector1F normalized(Vector1<Float> orElse) {
        final float current = lengthSquared();
        if (current <= EPSILON_F32 * EPSILON_F32) return new Vector1F(orElse);
        return signum();
    }

    @Override
    public Vector1F interpolate(Vector1<Float> target, Float alpha, UnaryOperator<Float> easing) {
        final float eased = easing.apply(alpha);
        final float x = x() * (1f - eased) + target.x() * eased;
        return new Vector1F(x);
    }

    @Override
    public boolean equals(UnaryTuple1<Float> other, Float epsilon) {
        return Tuples.epsilonEquals(x(), other.x(), epsilon);
    }

    @Override
    public boolean componentEquals(Float value, Float epsilon) {
        final var vec1 = new Vector1F(value);
        return equals(vec1, epsilon);
    }
}
