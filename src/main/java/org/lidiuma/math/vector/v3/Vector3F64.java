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

import org.lidiuma.math.matrix.Matrix4F64;
import org.lidiuma.math.point.p3.Point3;
import org.lidiuma.math.rotation.Radians;
import org.lidiuma.math.vector.v2.Vector2F64;
import org.lidiuma.math.vector.v4.Vector4F64;
import jdk.internal.vm.annotation.LooselyConsistentValue;
import jdk.internal.vm.annotation.NullRestricted;
import java.util.function.UnaryOperator;
import static org.lidiuma.math.vector.Vectors.v3;

@LooselyConsistentValue
public value record Vector3F64(@NullRestricted Double x,
                               @NullRestricted Double y,
                               @NullRestricted Double z
) implements Vector3<Vector3F64, Double>, Vector3.Real<Vector3F64, Double>, Point3.F64 {

    @Override
    public Vector3I64 asInt() {
        return v3((long) (double) x(), (long) (double) y(), (long) (double) z());
    }

    public Vector3F32 asF32() {
        return new Vector3F32((float) (double) x(), (float) (double) y(), (float) (double) z());
    }

    @Override
    public Vector3F64 ceil() {
        final double x = Math.ceil(x());
        final double y = Math.ceil(y());
        final double z = Math.ceil(z());
        return v3(x, y, z);
    }

    @Override
    public Vector3F64 floor() {
        final double x = Math.floor(x());
        final double y = Math.floor(y());
        final double z = Math.floor(z());
        return v3(x, y, z);
    }

    @Override
    public Double length() {
        return Math.sqrt(length2());
    }

    @Override
    public Vector3F64 withLength(Double length) {
        final double len = length();
        if (len == 0) return v3(0d, 0d, 0d);
        return mul(length / len);
    }

    @Override
    public Vector3F64 withLength2(Double length2) {
        return withLength(Math.sqrt(length2));
    }

    @Override
    public Vector3F64 limit(Double limit) {
        return limit2(limit * limit);
    }

    @Override
    public Vector3F64 limit2(Double limit2) {
        final double len2 = length2();
        if (len2 == 0 || len2 <= limit2) return this;
        return mul(Math.sqrt(limit2 / len2));
    }

    @Override
    public Vector3F64 normalize() {
        return withLength(1d);
    }

    @Override
    public Double distance(Vector3F64 vector) {
        return Math.sqrt(distance2(vector));
    }

    @Override
    public Vector3F64 lerp(Vector3F64 target, Double alpha) {
        final double x = x() + (target.x() - x()) * alpha;
        final double y = y() + (target.y() - y()) * alpha;
        final double z = z() + (target.z() - z()) * alpha;
        return v3(x, y, z);
    }

    @Override
    public Vector3F64 interpolate(Vector3F64 target, Double alpha, UnaryOperator<Double> interpolator) {
        return lerp(target, interpolator.apply(alpha));
    }

    @Override
    public boolean isUnit(Double margin) {
        return Math.abs(length2() - 1) < margin * margin;
    }

    @Override
    public boolean isCollinear(Vector3F64 vector, Double epsilon) {
        return cross(vector).length() <= epsilon * length() * vector.length();
    }

    @Override
    public boolean isPerpendicular(Vector3F64 vector, Double epsilon) {
        return Math.abs(dot(vector)) <= epsilon * length() * vector.length();
    }

    @Override
    public boolean epsilonEquals(Vector3F64 vector, Double epsilon) {
        return vector.sub(this).abs()
                .ltEq(v3(epsilon, epsilon, epsilon));
    }

    @Override
    public boolean isZero(Double epsilon) {
        return epsilonEquals(v3(0d, 0d, 0d), epsilon);
    }

    @Override
    public Vector2F64 asV2() {
        return new Vector2F64(x(), y());
    }

    @Override
    public Vector4F64 asV4(Double w) {
        return new Vector4F64(x(), y(), z(), w);
    }

    @Override
    public Vector3F64 cross(Vector3F64 vector) {
        final double x = y() * vector.z() - z() * vector.y();
        final double y = z() * vector.x() - x() * vector.z();
        final double z = x() * vector.y() - y() * vector.x();
        return v3(x, y, z);
    }

    @Override
    public Vector3F64 rotate(Vector3F64 axis, Radians angle) {
        return Matrix4F64.fromAxisAngle(axis, angle)
                .asMatrix3()
                .transform(this);
    }

    @Override
    public Double component(int index) throws IndexOutOfBoundsException {
        return switch (index) {
            case 0 -> x();
            case 1 -> y();
            case 2 -> z();
            default -> throw new IndexOutOfBoundsException("There's no component for index " + index + ".");
        };
    }

    @Override
    public Double sum() {
        return x() + y() + z();
    }

    @Override
    public Vector3F64 add(Vector3F64 other) {
        final double x = x() + other.x();
        final double y = y() + other.y();
        final double z = z() + other.z();
        return v3(x, y, z);
    }

    @Override
    public Vector3F64 sub(Vector3F64 other) {
        final double x = x() - other.x();
        final double y = y() - other.y();
        final double z = z() - other.z();
        return v3(x, y, z);
    }

    @Override
    public Vector3F64 mul(Vector3F64 other) {
        final double x = x() * other.x();
        final double y = y() * other.y();
        final double z = z() * other.z();
        return v3(x, y, z);
    }

    @Override
    public Vector3F64 mul(Double scalar) {
        return mul(v3(scalar, scalar, scalar));
    }

    @Override
    public Vector3F64 div(Vector3F64 other) {
        final double x = x() / other.x();
        final double y = y() / other.y();
        final double z = z() / other.z();
        return v3(x, y, z);
    }

    @Override
    public boolean lt(Vector3F64 other) {
        return x() < other.x() && y() < other.y() && z() < other.z();
    }

    @Override
    public boolean ltEq(Vector3F64 other) {
        return x() <= other.x() && y() <= other.y() && z() <= other.z();
    }

    @Override
    public boolean gt(Vector3F64 other) {
        return x() > other.x() && y() > other.y() && z() > other.z();
    }

    @Override
    public boolean gtEq(Vector3F64 other) {
        return x() >= other.x() && y() >= other.y() && z() >= other.z();
    }

    @Override
    public Vector3F64 abs() {
        return v3(Math.abs(x()), Math.abs(y()), Math.abs(z()));
    }

    @Override
    public Vector3F64 max(Vector3F64 other) {
        return v3(Math.max(x(), other.x()), Math.max(y(), other.y()), Math.max(z(), other.z()));
    }

    @Override
    public Vector3F64 min(Vector3F64 other) {
        return v3(Math.min(x(), other.x()), Math.min(y(), other.y()), Math.min(z(), other.z()));
    }

    @Override
    public Double distance2(Vector3F64 vector) {
        final var delta = sub(vector);
        final var delta2 = delta.mul(delta);
        return delta2.sum();
    }

    @Override
    public Double length2() {
        return mul(this).sum();
    }

    @Override
    public Double dot(Vector3F64 vector) {
        return mul(vector).sum();
    }

    @Override
    public Vector3F64 clamp(Double min, Double max) {
        final double x = Math.clamp(x(), min, max);
        final double y = Math.clamp(y(), min, max);
        final double z = Math.clamp(z(), min, max);
        return v3(x, y, z);
    }

    @Override
    public boolean hasSameDirection(Vector3F64 vector) {
        return dot(vector) > 0;
    }

    @Override
    public boolean hasOppositeDirection(Vector3F64 vector) {
        return dot(vector) < 0;
    }
}
