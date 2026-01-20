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

import org.lidiuma.math.point.p1.Point1;
import org.lidiuma.math.vector.v2.Vector2F64;
import jdk.internal.vm.annotation.LooselyConsistentValue;
import jdk.internal.vm.annotation.NullRestricted;
import java.util.function.UnaryOperator;
import static org.lidiuma.math.vector.Vectors.v1;

@LooselyConsistentValue
public value record Vector1F64(@NullRestricted Double x
) implements Vector1<Vector1F64, Double>, Vector1.Real<Vector1F64, Double>, Point1.F64 {

    @Override
    public Vector1I64 asInt() {
        return v1((long) (double) x());
    }

    public Vector1F32 asF32() {
        return new Vector1F32((float) (double) x());
    }

    @Override
    public Vector1F64 ceil() {
        return v1(Math.ceil(x()));
    }

    @Override
    public Vector1F64 floor() {
        return v1(Math.floor(x()));
    }

    @Override
    public Double length() {
        return abs().x();
    }

    @Override
    public Vector1F64 withLength(Double length) {
        final double len = length();
        if (len == 0 || len == length) return this;
        return v1(x() * length / len);
    }

    @Override
    public Vector1F64 withLength2(Double length2) {
        return withLength(Math.sqrt(length2));
    }

    @Override
    public Vector1F64 limit(Double limit) {
        return limit2(limit * limit);
    }

    @Override
    public Vector1F64 limit2(Double limit2) {
        final double len2 = length2();
        if (len2 == 0 || len2 <= limit2) return this;
        return v1(x() * Math.sqrt(limit2 / len2));
    }

    @Override
    public Vector1F64 normalize() {
        return withLength(1d);
    }

    @Override
    public Double distance(Vector1F64 vector) {
        final double delta = x() - vector.x();
        return Math.abs(delta);
    }

    @Override
    public Vector1F64 lerp(Vector1F64 target, Double alpha) {
        final double invAlpha = 1 - alpha;
        return v1(x() * invAlpha + target.x() * alpha);
    }

    @Override
    public Vector1F64 interpolate(Vector1F64 target, Double alpha, UnaryOperator<Double> interpolator) {
        return lerp(target, interpolator.apply(alpha));
    }

    @Override
    public boolean isUnit(Double margin) {
        return Math.abs(length2() - 1) < margin;
    }

    @Override
    public boolean isCollinear(Vector1F64 vector, Double epsilon) {
        return x() != 0 && vector.x() != 0;
    }

    @Override
    public boolean isPerpendicular(Vector1F64 vector, Double epsilon) {
        return Math.abs(dot(vector)) <= epsilon;
    }

    @Override
    public boolean epsilonEquals(Vector1F64 vector, Double epsilon) {
        return Math.abs(x() - vector.x()) <= epsilon;
    }

    @Override
    public boolean isZero(Double epsilon) {
        return epsilonEquals(v1(0d), epsilon);
    }

    @Override
    public Vector2F64 asV2(Double y) {
        return new Vector2F64(x(), y);
    }

    @Override
    public Double component(int index) throws IndexOutOfBoundsException {
        if (index != 0) throw new IndexOutOfBoundsException("There's no component for index " + index + ".");
        return x();
    }

    @Override
    public Double sum() {
        return x();
    }

    @Override
    public Vector1F64 add(Vector1F64 other) {
        return v1(x() + other.x());
    }

    @Override
    public Vector1F64 sub(Vector1F64 other) {
        return v1(x() - other.x());
    }

    @Override
    public Vector1F64 mul(Vector1F64 other) {
        return v1(x() * other.x());
    }

    @Override
    public Vector1F64 mul(Double scalar) {
        return v1(x() * scalar);
    }

    @Override
    public Vector1F64 div(Vector1F64 other) {
        return v1(x() / other.x());
    }

    @Override
    public boolean lt(Vector1F64 other) {
        return x() < other.x();
    }

    @Override
    public boolean ltEq(Vector1F64 other) {
        return x() <= other.x();
    }

    @Override
    public boolean gt(Vector1F64 other) {
        return x() > other.x();
    }

    @Override
    public boolean gtEq(Vector1F64 other) {
        return x() >= other.x();
    }

    @Override
    public Vector1F64 abs() {
        return v1(Math.abs(x()));
    }

    @Override
    public Vector1F64 max(Vector1F64 other) {
        return v1(Math.max(x(), other.x()));
    }

    @Override
    public Vector1F64 min(Vector1F64 other) {
        return v1(Math.min(x(), other.x()));
    }

    @Override
    public Double distance2(Vector1F64 vector) {
        final double delta = x() - vector.x();
        return delta * delta;
    }

    @Override
    public Double length2() {
        return x() * x();
    }

    @Override
    public Double dot(Vector1F64 vector) {
        return x() * vector.x();
    }

    @Override
    public Vector1F64 clamp(Double min, Double max) {
        return v1(Math.clamp(x(), min, max));
    }

    @Override
    public boolean hasSameDirection(Vector1F64 vector) {
        return dot(vector) > 0;
    }

    @Override
    public boolean hasOppositeDirection(Vector1F64 vector) {
        return dot(vector) < 0;
    }
}
