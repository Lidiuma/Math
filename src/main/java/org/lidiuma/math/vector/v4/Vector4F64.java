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

import org.lidiuma.math.point.p4.Point4;
import org.lidiuma.math.vector.v3.Vector3F64;
import jdk.internal.vm.annotation.LooselyConsistentValue;
import jdk.internal.vm.annotation.NullRestricted;
import java.util.function.UnaryOperator;
import static org.lidiuma.math.vector.Vectors.v4;

@LooselyConsistentValue
public value record Vector4F64(@NullRestricted Double x,
                               @NullRestricted Double y,
                               @NullRestricted Double z,
                               @NullRestricted Double w
) implements Vector4<Vector4F64, Double>, Vector4.Real<Vector4F64, Double>, Point4.F64 {

    @Override
    public Vector4I64 asInt() {
        return v4(
                (long) (double) x(),
                (long) (double) y(),
                (long) (double) z(),
                (long) (double) w()
        );
    }

    public Vector4F32 asF32() {
        return new Vector4F32((float) (double) x(), (float) (double) y(), (float) (double) z(), (float) (double) w());
    }

    @Override
    public Vector4F64 ceil() {
        final double x = Math.ceil(x());
        final double y = Math.ceil(y());
        final double z = Math.ceil(z());
        final double w = Math.ceil(w());
        return v4(x, y, z, w);
    }

    @Override
    public Vector4F64 floor() {
        final double x = Math.floor(x());
        final double y = Math.floor(y());
        final double z = Math.floor(z());
        final double w = Math.floor(w());
        return v4(x, y, z, w);
    }

    @Override
    public Double length() {
        return Math.sqrt(length2());
    }

    @Override
    public Vector4F64 withLength(Double length) {
        final double len = length();
        if (len == 0) return v4(0d, 0d, 0d, 0d);
        return mul(length / len);
    }

    @Override
    public Vector4F64 withLength2(Double length2) {
        return withLength(Math.sqrt(length2));
    }

    @Override
    public Vector4F64 limit(Double limit) {
        return limit2(limit * limit);
    }

    @Override
    public Vector4F64 limit2(Double limit2) {
        final double len2 = length2();
        if (len2 == 0 || len2 <= limit2) return this;
        return mul(Math.sqrt(limit2 / len2));
    }

    @Override
    public Vector4F64 normalize() {
        return withLength(1d);
    }

    @Override
    public Double distance(Vector4F64 vector) {
        return Math.sqrt(distance2(vector));
    }

    @Override
    public Vector4F64 lerp(Vector4F64 target, Double alpha) {
        final double x = x() + (target.x() - x()) * alpha;
        final double y = y() + (target.y() - y()) * alpha;
        final double z = z() + (target.z() - z()) * alpha;
        final double w = w() + (target.w() - w()) * alpha;
        return v4(x, y, z, w);
    }

    @Override
    public Vector4F64 interpolate(Vector4F64 target, Double alpha, UnaryOperator<Double> interpolator) {
        return lerp(target, interpolator.apply(alpha));
    }

    @Override
    public boolean isUnit(Double margin) {
        return Math.abs(length2() - 1) < margin * margin;
    }

    @Override
    public boolean isCollinear(Vector4F64 vector, Double epsilon) {
        final double len = length();
        final double vLen = vector.length();
        if (len == 0 || vLen == 0) return false;

        final double cosTheta = Math.abs(dot(vector) / (len * vLen));
        return Math.abs(cosTheta - 1) <= epsilon;
    }

    @Override
    public boolean isPerpendicular(Vector4F64 vector, Double epsilon) {
        return Math.abs(dot(vector)) <= epsilon * length() * vector.length();
    }

    @Override
    public boolean epsilonEquals(Vector4F64 vector, Double epsilon) {
        return vector.sub(this).abs()
                .ltEq(v4(epsilon, epsilon, epsilon, epsilon));
    }

    @Override
    public boolean isZero(Double epsilon) {
        return epsilonEquals(v4(0d, 0d, 0d, 0d), epsilon);
    }

    @Override
    public Vector3F64 asV3() {
        return new Vector3F64(x(), y(), z());
    }

    @Override
    public Double component(int index) throws IndexOutOfBoundsException {
        return switch (index) {
            case 0 -> x();
            case 1 -> y();
            case 2 -> z();
            case 3 -> w();
            default -> throw new IndexOutOfBoundsException("There's no component for index " + index + ".");
        };
    }

    @Override
    public Double sum() {
        return x() + y() + z() + w();
    }

    @Override
    public Vector4F64 add(Vector4F64 other) {
        final double x = x() + other.x();
        final double y = y() + other.y();
        final double z = z() + other.z();
        final double w = w() + other.w();
        return v4(x, y, z, w);
    }

    @Override
    public Vector4F64 sub(Vector4F64 other) {
        final double x = x() - other.x();
        final double y = y() - other.y();
        final double z = z() - other.z();
        final double w = w() - other.w();
        return v4(x, y, z, w);
    }

    @Override
    public Vector4F64 mul(Vector4F64 other) {
        final double x = x() * other.x();
        final double y = y() * other.y();
        final double z = z() * other.z();
        final double w = w() * other.w();
        return v4(x, y, z, w);
    }

    @Override
    public Vector4F64 mul(Double scalar) {
        return mul(v4(scalar, scalar, scalar, scalar));
    }

    @Override
    public Vector4F64 div(Vector4F64 other) {
        final double x = x() / other.x();
        final double y = y() / other.y();
        final double z = z() / other.z();
        final double w = w() / other.w();
        return v4(x, y, z, w);
    }

    @Override
    public boolean lt(Vector4F64 other) {
        return x() < other.x() && y() < other.y() &&
                z() < other.z() && w() < other.w();
    }

    @Override
    public boolean ltEq(Vector4F64 other) {
        return x() <= other.x() && y() <= other.y() &&
                z() <= other.z() && w() <= other.w();
    }

    @Override
    public boolean gt(Vector4F64 other) {
        return x() > other.x() && y() > other.y() &&
                z() > other.z() && w() > other.w();
    }

    @Override
    public boolean gtEq(Vector4F64 other) {
        return x() >= other.x() && y() >= other.y() &&
                z() >= other.z() && w() >= other.w();
    }

    @Override
    public Vector4F64 abs() {
        final double x = Math.abs(x());
        final double y = Math.abs(y());
        final double z = Math.abs(z());
        final double w = Math.abs(w());
        return v4(x, y, z, w);
    }

    @Override
    public Vector4F64 max(Vector4F64 other) {
        final double x = Math.max(x(), other.x());
        final double y = Math.max(y(), other.y());
        final double z = Math.max(z(), other.z());
        final double w = Math.max(w(), other.w());
        return v4(x, y, z, w);
    }

    @Override
    public Vector4F64 min(Vector4F64 other) {
        final double x = Math.min(x(), other.x());
        final double y = Math.min(y(), other.y());
        final double z = Math.min(z(), other.z());
        final double w = Math.min(w(), other.w());
        return v4(x, y, z, w);
    }

    @Override
    public Double distance2(Vector4F64 vector) {
        final var delta = sub(vector);
        final var delta2 = delta.mul(delta);
        return delta2.sum();
    }

    @Override
    public Double length2() {
        return mul(this).sum();
    }

    @Override
    public Double dot(Vector4F64 vector) {
        return mul(vector).sum();
    }

    @Override
    public Vector4F64 clamp(Double min, Double max) {
        final double x = Math.clamp(x(), min, max);
        final double y = Math.clamp(y(), min, max);
        final double z = Math.clamp(z(), min, max);
        final double w = Math.clamp(w(), min, max);
        return v4(x, y, z, w);
    }

    @Override
    public boolean hasSameDirection(Vector4F64 vector) {
        return dot(vector) > 0;
    }

    @Override
    public boolean hasOppositeDirection(Vector4F64 vector) {
        return dot(vector) < 0;
    }
}
