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
import org.lidiuma.math.vector.v3.Vector3F32;
import jdk.internal.vm.annotation.LooselyConsistentValue;
import jdk.internal.vm.annotation.NullRestricted;
import java.util.function.UnaryOperator;
import static org.lidiuma.math.vector.Vectors.v4;

@LooselyConsistentValue
public value record Vector4F32(@NullRestricted Float x,
                               @NullRestricted Float y,
                               @NullRestricted Float z,
                               @NullRestricted Float w
) implements Vector4<Vector4F32, Float>, Vector4.Real<Vector4F32, Float>, Point4.F32 {

    @Override
    public Vector4I32 asInt() {
        return v4(
                (int) (float) x(),
                (int) (float) y(),
                (int) (float) z(),
                (int) (float) w()
        );
    }

    public Vector4F64 asF64() {
        return new Vector4F64((double) x(), (double) y(), (double) z(), (double) w());
    }

    @Override
    public Vector4F32 ceil() {
        final float x = (float) Math.ceil(x());
        final float y = (float) Math.ceil(y());
        final float z = (float) Math.ceil(z());
        final float w = (float) Math.ceil(w());
        return v4(x, y, z, w);
    }

    @Override
    public Vector4F32 floor() {
        final float x = (float) Math.floor(x());
        final float y = (float) Math.floor(y());
        final float z = (float) Math.floor(z());
        final float w = (float) Math.floor(w());
        return v4(x, y, z, w);
    }

    @Override
    public Float length() {
        return (float) Math.sqrt(length2());
    }

    @Override
    public Vector4F32 withLength(Float length) {
        final float len = length();
        if (len == 0) return v4(0f, 0f, 0f, 0f);
        return mul(length / len);
    }

    @Override
    public Vector4F32 withLength2(Float length2) {
        return withLength((float) Math.sqrt(length2));
    }

    @Override
    public Vector4F32 limit(Float limit) {
        return limit2(limit * limit);
    }

    @Override
    public Vector4F32 limit2(Float limit2) {
        final float len2 = length2();
        if (len2 == 0 || len2 <= limit2) return this;
        return mul((float) Math.sqrt(limit2 / len2));
    }

    @Override
    public Vector4F32 normalize() {
        return withLength(1f);
    }

    @Override
    public Float distance(Vector4F32 vector) {
        return (float) Math.sqrt(distance2(vector));
    }

    @Override
    public Vector4F32 lerp(Vector4F32 target, Float alpha) {
        final float x = x() + (target.x() - x()) * alpha;
        final float y = y() + (target.y() - y()) * alpha;
        final float z = z() + (target.z() - z()) * alpha;
        final float w = w() + (target.w() - w()) * alpha;
        return v4(x, y, z, w);
    }

    @Override
    public Vector4F32 interpolate(Vector4F32 target, Float alpha, UnaryOperator<Float> interpolator) {
        return lerp(target, interpolator.apply(alpha));
    }

    @Override
    public boolean isUnit(Float margin) {
        return Math.abs(length2() - 1) < margin * margin;
    }

    @Override
    public boolean isCollinear(Vector4F32 vector, Float epsilon) {
        final float len = length();
        final float vLen = vector.length();
        if (len == 0 || vLen == 0) return false;

        final float cosTheta = Math.abs(dot(vector) / (len * vLen));
        return Math.abs(cosTheta - 1) <= epsilon;
    }

    @Override
    public boolean isPerpendicular(Vector4F32 vector, Float epsilon) {
        return Math.abs(dot(vector)) <= epsilon * length() * vector.length();
    }

    @Override
    public boolean epsilonEquals(Vector4F32 vector, Float epsilon) {
        return vector.sub(this).abs()
                .ltEq(v4(epsilon, epsilon, epsilon, epsilon));
    }

    @Override
    public boolean isZero(Float epsilon) {
        return epsilonEquals(v4(0f, 0f, 0f, 0f), epsilon);
    }

    @Override
    public Vector3F32 asV3() {
        return new Vector3F32(x(), y(), z());
    }

    @Override
    public Float component(int index) throws IndexOutOfBoundsException {
        return switch (index) {
            case 0 -> x();
            case 1 -> y();
            case 2 -> z();
            case 3 -> w();
            default -> throw new IndexOutOfBoundsException("There's no component for index " + index + ".");
        };
    }

    @Override
    public Float sum() {
        return x() + y() + z() + w();
    }

    @Override
    public Vector4F32 add(Vector4F32 other) {
        final float x = x() + other.x();
        final float y = y() + other.y();
        final float z = z() + other.z();
        final float w = w() + other.w();
        return v4(x, y, z, w);
    }

    @Override
    public Vector4F32 sub(Vector4F32 other) {
        final float x = x() - other.x();
        final float y = y() - other.y();
        final float z = z() - other.z();
        final float w = w() - other.w();
        return v4(x, y, z, w);
    }

    @Override
    public Vector4F32 mul(Vector4F32 other) {
        final float x = x() * other.x();
        final float y = y() * other.y();
        final float z = z() * other.z();
        final float w = w() * other.w();
        return v4(x, y, z, w);
    }

    @Override
    public Vector4F32 mul(Float scalar) {
        return mul(v4(scalar, scalar, scalar, scalar));
    }

    @Override
    public Vector4F32 div(Vector4F32 other) {
        final float x = x() / other.x();
        final float y = y() / other.y();
        final float z = z() / other.z();
        final float w = w() / other.w();
        return v4(x, y, z, w);
    }

    @Override
    public boolean lt(Vector4F32 other) {
        return x() < other.x() && y() < other.y() &&
                z() < other.z() && w() < other.w();
    }

    @Override
    public boolean ltEq(Vector4F32 other) {
        return x() <= other.x() && y() <= other.y() &&
                z() <= other.z() && w() <= other.w();
    }

    @Override
    public boolean gt(Vector4F32 other) {
        return x() > other.x() && y() > other.y() &&
                z() > other.z() && w() > other.w();
    }

    @Override
    public boolean gtEq(Vector4F32 other) {
        return x() >= other.x() && y() >= other.y() &&
                z() >= other.z() && w() >= other.w();
    }

    @Override
    public Vector4F32 abs() {
        final float x = Math.abs(x());
        final float y = Math.abs(y());
        final float z = Math.abs(z());
        final float w = Math.abs(w());
        return v4(x, y, z, w);
    }

    @Override
    public Vector4F32 max(Vector4F32 other) {
        final float x = Math.max(x(), other.x());
        final float y = Math.max(y(), other.y());
        final float z = Math.max(z(), other.z());
        final float w = Math.max(w(), other.w());
        return v4(x, y, z, w);
    }

    @Override
    public Vector4F32 min(Vector4F32 other) {
        final float x = Math.min(x(), other.x());
        final float y = Math.min(y(), other.y());
        final float z = Math.min(z(), other.z());
        final float w = Math.min(w(), other.w());
        return v4(x, y, z, w);
    }

    @Override
    public Float distance2(Vector4F32 vector) {
        final var delta = sub(vector);
        final var delta2 = delta.mul(delta);
        return delta2.sum();
    }

    @Override
    public Float length2() {
        return mul(this).sum();
    }

    @Override
    public Float dot(Vector4F32 vector) {
        return mul(vector).sum();
    }

    @Override
    public Vector4F32 clamp(Float min, Float max) {
        final float x = Math.clamp(x(), min, max);
        final float y = Math.clamp(y(), min, max);
        final float z = Math.clamp(z(), min, max);
        final float w = Math.clamp(w(), min, max);
        return v4(x, y, z, w);
    }

    @Override
    public boolean hasSameDirection(Vector4F32 vector) {
        return dot(vector) > 0;
    }

    @Override
    public boolean hasOppositeDirection(Vector4F32 vector) {
        return dot(vector) < 0;
    }
}
