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

import org.lidiuma.math.point.p2.Point2;
import org.lidiuma.math.rotation.Radians;
import org.lidiuma.math.vector.v1.Vector1F64;
import org.lidiuma.math.vector.v3.Vector3F64;
import jdk.internal.vm.annotation.LooselyConsistentValue;
import jdk.internal.vm.annotation.NullRestricted;
import java.util.function.UnaryOperator;
import static org.lidiuma.math.vector.Vectors.v2;

@LooselyConsistentValue
public value record Vector2F64(@NullRestricted Double x,
                               @NullRestricted Double y
) implements Vector2<Vector2F64, Double>, Vector2.Real<Vector2F64, Double>, Point2.F64 {

    @Override
    public Vector2I64 asInt() {
        return v2((long) (double) x(), (long) (double) y());
    }

    public Vector2F32 asF32() {
        return new Vector2F32((float) (double) x(), (float) (double) y());
    }

    @Override
    public Vector1F64 asV1() {
        return new Vector1F64(x());
    }

    @Override
    public Vector3F64 asV3(Double z) {
        return new Vector3F64(x(), y(), z);
    }

    @Override
    public Vector2F64 rotate(Radians radians) {

        final double cos = Math.cos(radians.value());
        final double sin = Math.sin(radians.value());

        final double newX = x() * cos - y() * sin;
        final double newY = x() * sin + y() * cos;

        return v2(newX, newY);
    }

    @Override
    public Radians angle() {
        return Radians.radians(Math.atan2(y(), x()));
    }

    @Override
    public Double cross(Vector2F64 vector) {
        return x() * vector.y() - y() * vector.x();
    }

    @Override
    public Double component(int index) throws IndexOutOfBoundsException {
        return switch (index) {
            case 0 -> x();
            case 1 -> y();
            default -> throw new IndexOutOfBoundsException("There's no component for index " + index + ".");
        };
    }

    @Override
    public Double sum() {
        return x() + y();
    }

    @Override
    public Vector2F64 add(Vector2F64 other) {
        return v2(x() + other.x(), y() + other.y());
    }

    @Override
    public Vector2F64 sub(Vector2F64 other) {
        return v2(x() - other.x(), y() - other.y());
    }

    @Override
    public Vector2F64 mul(Vector2F64 other) {
        return v2(x() * other.x(), y() * other.y());
    }

    @Override
    public Vector2F64 mul(Double scalar) {
        return mul(v2(scalar, scalar));
    }

    @Override
    public Vector2F64 div(Vector2F64 other) {
        return v2(x() / other.x(), y() / other.y());
    }

    @Override
    public boolean lt(Vector2F64 other) {
        return x() < other.x() && y() < other.y();
    }

    @Override
    public boolean ltEq(Vector2F64 other) {
        return x() <= other.x() && y() <= other.y();
    }

    @Override
    public boolean gt(Vector2F64 other) {
        return x() > other.x() && y() > other.y();
    }

    @Override
    public boolean gtEq(Vector2F64 other) {
        return x() >= other.x() && y() >= other.y();
    }

    @Override
    public Vector2F64 abs() {
        return v2(Math.abs(x()), Math.abs(y()));
    }

    @Override
    public Vector2F64 max(Vector2F64 other) {
        return v2(Math.max(x(), other.x()), Math.max(y(), other.y()));
    }

    @Override
    public Vector2F64 min(Vector2F64 other) {
        return v2(Math.min(x(), other.x()), Math.min(y(), other.y()));
    }

    @Override
    public Double distance2(Vector2F64 vector) {
        final var delta = sub(vector);
        final var delta2 = delta.mul(delta);
        return delta2.sum();
    }

    @Override
    public Double length2() {
        return mul(this).sum();
    }

    @Override
    public Double dot(Vector2F64 vector) {
        return mul(vector).sum();
    }

    @Override
    public Vector2F64 clamp(Double min, Double max) {
        final double x = Math.clamp(x(), min, max);
        final double y = Math.clamp(y(), min, max);
        return v2(x, y);
    }

    @Override
    public boolean hasSameDirection(Vector2F64 vector) {
        return dot(vector) > 0;
    }

    @Override
    public boolean hasOppositeDirection(Vector2F64 vector) {
        return dot(vector) < 0;
    }

    @Override
    public Vector2F64 ceil() {
        final double x = Math.ceil(x());
        final double y = Math.ceil(y());
        return v2(x, y);
    }

    @Override
    public Vector2F64 floor() {
        final double x = Math.floor(x());
        final double y = Math.floor(y());
        return v2(x, y);
    }

    @Override
    public Double length() {
        return Math.sqrt(length2());
    }

    @Override
    public Vector2F64 withLength(Double length) {
        final double len = length();
        if (len == 0) return v2(0d, 0d);
        return mul(length / len);
    }

    @Override
    public Vector2F64 withLength2(Double length2) {
        return withLength(Math.sqrt(length2));
    }

    @Override
    public Vector2F64 limit(Double limit) {
        return limit2(limit * limit);
    }

    @Override
    public Vector2F64 limit2(Double limit2) {
        final double len2 = length2();
        if (len2 == 0 || len2 <= limit2) return this;
        return mul(Math.sqrt(limit2 / len2));
    }

    @Override
    public Vector2F64 normalize() {
        return withLength(1d);
    }

    @Override
    public Double distance(Vector2F64 vector) {
        return Math.sqrt(distance2(vector));
    }

    @Override
    public Vector2F64 lerp(Vector2F64 target, Double alpha) {
        final double x = x() + (target.x() - x()) * alpha;
        final double y = y() + (target.y() - y()) * alpha;
        return v2(x, y);
    }

    @Override
    public Vector2F64 interpolate(Vector2F64 target, Double alpha, UnaryOperator<Double> interpolator) {
        return lerp(target, interpolator.apply(alpha));
    }

    @Override
    public boolean isUnit(Double margin) {
        return Math.abs(length2() - 1) < margin * margin;
    }

    @Override
    public boolean isCollinear(Vector2F64 vector, Double epsilon) {
        return Math.abs(cross(vector)) <= epsilon * length() * vector.length();
    }

    @Override
    public boolean isPerpendicular(Vector2F64 vector, Double epsilon) {
        return Math.abs(dot(vector)) <= epsilon * length() * vector.length();
    }

    @Override
    public boolean epsilonEquals(Vector2F64 vector, Double epsilon) {
        return vector.sub(this).abs()
                .ltEq(v2(epsilon, epsilon));
    }

    @Override
    public boolean isZero(Double epsilon) {
        return epsilonEquals(v2(0d, 0d), epsilon);
    }
}
