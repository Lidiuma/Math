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

import org.lidiuma.math.matrix.Matrix4F32;
import org.lidiuma.math.point.p3.Point3;
import org.lidiuma.math.rotation.Radians;
import org.lidiuma.math.vector.Vector;
import org.lidiuma.math.vector.v2.Vector2F32;
import org.lidiuma.math.vector.v4.Vector4F32;
import jdk.internal.vm.annotation.LooselyConsistentValue;
import jdk.internal.vm.annotation.NullRestricted;
import java.util.function.UnaryOperator;
import static org.lidiuma.math.vector.Vectors.v3;

@LooselyConsistentValue
public value record Vector3F32(@NullRestricted Float x,
                               @NullRestricted Float y,
                               @NullRestricted Float z
) implements Vector3<Vector3F32, Float>, Vector.Real<Vector3F32, Float>, Point3.F32 {

    @Override
    public Vector3I32 asInt() {
        return v3((int) (float) x(), (int) (float) y(), (int) (float) z());
    }

    public Vector3F64 asF64() {
        return new Vector3F64((double) x(), (double) y(), (double) z());
    }

    @Override
    public Vector3F32 ceil() {
        final float x = (float) Math.ceil(x());
        final float y = (float) Math.ceil(y());
        final float z = (float) Math.ceil(z());
        return v3(x, y, z);
    }

    @Override
    public Vector3F32 floor() {
        final float x = (float) Math.floor(x());
        final float y = (float) Math.floor(y());
        final float z = (float) Math.floor(z());
        return v3(x, y, z);
    }

    @Override
    public Float length() {
        return (float) Math.sqrt(length2());
    }

    @Override
    public Vector3F32 withLength(Float length) {
        final float len = length();
        if (len == 0) return v3(0f, 0f, 0f);
        return mul(length / len);
    }

    @Override
    public Vector3F32 withLength2(Float length2) {
        return withLength((float) Math.sqrt(length2));
    }

    @Override
    public Vector3F32 limit(Float limit) {
        return limit2(limit * limit);
    }

    @Override
    public Vector3F32 limit2(Float limit2) {
        final float len2 = length2();
        if (len2 == 0 || len2 <= limit2) return this;
        return mul((float) Math.sqrt(limit2 / len2));
    }

    @Override
    public Vector3F32 normalize() {
        return withLength(1f);
    }

    @Override
    public Float distance(Vector3F32 vector) {
        return (float) Math.sqrt(distance2(vector));
    }

    @Override
    public Vector3F32 lerp(Vector3F32 target, Float alpha) {
        final float x = x() + (target.x() - x()) * alpha;
        final float y = y() + (target.y() - y()) * alpha;
        final float z = z() + (target.z() - z()) * alpha;
        return v3(x, y, z);
    }

    @Override
    public Vector3F32 interpolate(Vector3F32 target, Float alpha, UnaryOperator<Float> interpolator) {
        return lerp(target, interpolator.apply(alpha));
    }

    @Override
    public boolean isUnit(Float margin) {
        return Math.abs(length2() - 1) < margin * margin;
    }

    @Override
    public boolean isCollinear(Vector3F32 vector, Float epsilon) {
        return cross(vector).length() <= epsilon * length() * vector.length();
    }

    @Override
    public boolean isPerpendicular(Vector3F32 vector, Float epsilon) {
        return Math.abs(dot(vector)) <= epsilon * length() * vector.length();
    }

    @Override
    public boolean epsilonEquals(Vector3F32 vector, Float epsilon) {
        return vector.sub(this).abs()
                .ltEq(v3(epsilon, epsilon, epsilon));
    }

    @Override
    public boolean isZero(Float epsilon) {
        return epsilonEquals(v3(0f, 0f, 0f), epsilon);
    }

    @Override
    public Vector2F32 asV2() {
        return new Vector2F32(x(), y());
    }

    @Override
    public Vector4F32 asV4(Float w) {
        return new Vector4F32(x(), y(), z(), w);
    }

    @Override
    public Vector3F32 cross(Vector3F32 vector) {
        final float x = y() * vector.z() - z() * vector.y();
        final float y = z() * vector.x() - x() * vector.z();
        final float z = x() * vector.y() - y() * vector.x();
        return v3(x, y, z);
    }

    @Override
    public Vector3F32 rotate(Vector3F32 axis, Radians angle) {
        return Matrix4F32.fromAxisAngle(axis, angle)
                .asMatrix3()
                .transform(this);
    }

    @Override
    public Float component(int index) throws IndexOutOfBoundsException {
        return switch (index) {
            case 0 -> x();
            case 1 -> y();
            case 2 -> z();
            default -> throw new IndexOutOfBoundsException("There's no component for index " + index + ".");
        };
    }

    @Override
    public Float sum() {
        return x() + y() + z();
    }

    @Override
    public Vector3F32 add(Vector3F32 other) {
        final float x = x() + other.x();
        final float y = y() + other.y();
        final float z = z() + other.z();
        return v3(x, y, z);
    }

    @Override
    public Vector3F32 sub(Vector3F32 other) {
        final float x = x() - other.x();
        final float y = y() - other.y();
        final float z = z() - other.z();
        return v3(x, y, z);
    }

    @Override
    public Vector3F32 mul(Vector3F32 other) {
        final float x = x() * other.x();
        final float y = y() * other.y();
        final float z = z() * other.z();
        return v3(x, y, z);
    }

    @Override
    public Vector3F32 mul(Float scalar) {
        return mul(v3(scalar, scalar, scalar));
    }

    @Override
    public Vector3F32 div(Vector3F32 other) {
        final float x = x() / other.x();
        final float y = y() / other.y();
        final float z = z() / other.z();
        return v3(x, y, z);
    }

    @Override
    public boolean lt(Vector3F32 other) {
        return x() < other.x() && y() < other.y() && z() < other.z();
    }

    @Override
    public boolean ltEq(Vector3F32 other) {
        return x() <= other.x() && y() <= other.y() && z() <= other.z();
    }

    @Override
    public boolean gt(Vector3F32 other) {
        return x() > other.x() && y() > other.y() && z() > other.z();
    }

    @Override
    public boolean gtEq(Vector3F32 other) {
        return x() >= other.x() && y() >= other.y() && z() >= other.z();
    }

    @Override
    public Vector3F32 abs() {
        return v3(Math.abs(x()), Math.abs(y()), Math.abs(z()));
    }

    @Override
    public Vector3F32 max(Vector3F32 other) {
        return v3(Math.max(x(), other.x()), Math.max(y(), other.y()), Math.max(z(), other.z()));
    }

    @Override
    public Vector3F32 min(Vector3F32 other) {
        return v3(Math.min(x(), other.x()), Math.min(y(), other.y()), Math.min(z(), other.z()));
    }

    @Override
    public Float distance2(Vector3F32 vector) {
        final var delta = sub(vector);
        final var delta2 = delta.mul(delta);
        return delta2.sum();
    }

    @Override
    public Float length2() {
        return mul(this).sum();
    }

    @Override
    public Float dot(Vector3F32 vector) {
        return mul(vector).sum();
    }

    @Override
    public Vector3F32 clamp(Float min, Float max) {
        final float x = Math.clamp(x(), min, max);
        final float y = Math.clamp(y(), min, max);
        final float z = Math.clamp(z(), min, max);
        return v3(x, y, z);
    }

    @Override
    public boolean hasSameDirection(Vector3F32 vector) {
        return dot(vector) > 0;
    }

    @Override
    public boolean hasOppositeDirection(Vector3F32 vector) {
        return dot(vector) < 0;
    }
}
