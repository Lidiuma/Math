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
import org.lidiuma.math.vector.v1.Vector1F32;
import org.lidiuma.math.vector.v3.Vector3F32;
import jdk.internal.vm.annotation.LooselyConsistentValue;
import jdk.internal.vm.annotation.NullRestricted;
import java.util.function.UnaryOperator;
import static org.lidiuma.math.vector.Vectors.v2;

@LooselyConsistentValue
public value record Vector2F32(@NullRestricted Float x,
                               @NullRestricted Float y
) implements Vector2<Vector2F32, Float>, Vector2.Real<Vector2F32, Float>, Point2.F32 {

    @Override
    public Vector2I32 asInt() {
        return v2((int) (float) x(), (int) (float) y());
    }

    public Vector2F64 asF64() {
        return new Vector2F64((double) x(), (double) y());
    }

    @Override
    public Vector1F32 asV1() {
        return new Vector1F32(x());
    }

    @Override
    public Vector3F32 asV3(Float z) {
        return new Vector3F32(x(), y(), z);
    }

    @Override
    public Vector2F32 rotate(Radians radians) {

        final float cos = (float) Math.cos(radians.value());
        final float sin = (float) Math.sin(radians.value());

        final float newX = x() * cos - y() * sin;
        final float newY = x() * sin + y() * cos;

        return v2(newX, newY);
    }

    @Override
    public Radians angle() {
        return Radians.radians(Math.atan2(y(), x()));
    }

    @Override
    public Float cross(Vector2F32 vector) {
        return x() * vector.y() - y() * vector.x();
    }

    @Override
    public Float component(int index) throws IndexOutOfBoundsException {
        return switch (index) {
            case 0 -> x();
            case 1 -> y();
            default -> throw new IndexOutOfBoundsException("There's no component for index " + index + ".");
        };
    }

    @Override
    public Float sum() {
        return x() + y();
    }

    @Override
    public Vector2F32 add(Vector2F32 other) {
        return v2(x() + other.x(), y() + other.y());
    }

    @Override
    public Vector2F32 sub(Vector2F32 other) {
        return v2(x() - other.x(), y() - other.y());
    }

    @Override
    public Vector2F32 mul(Vector2F32 other) {
        return v2(x() * other.x(), y() * other.y());
    }

    @Override
    public Vector2F32 mul(Float scalar) {
        return mul(v2(scalar, scalar));
    }

    @Override
    public Vector2F32 div(Vector2F32 other) {
        return v2(x() / other.x(), y() / other.y());
    }

    @Override
    public boolean lt(Vector2F32 other) {
        return x() < other.x() && y() < other.y();
    }

    @Override
    public boolean ltEq(Vector2F32 other) {
        return x() <= other.x() && y() <= other.y();
    }

    @Override
    public boolean gt(Vector2F32 other) {
        return x() > other.x() && y() > other.y();
    }

    @Override
    public boolean gtEq(Vector2F32 other) {
        return x() >= other.x() && y() >= other.y();
    }

    @Override
    public Vector2F32 abs() {
        return v2(Math.abs(x()), Math.abs(y()));
    }

    @Override
    public Vector2F32 max(Vector2F32 other) {
        return v2(Math.max(x(), other.x()), Math.max(y(), other.y()));
    }

    @Override
    public Vector2F32 min(Vector2F32 other) {
        return v2(Math.min(x(), other.x()), Math.min(y(), other.y()));
    }

    @Override
    public Float distance2(Vector2F32 vector) {
        final var delta = sub(vector);
        final var delta2 = delta.mul(delta);
        return delta2.sum();
    }

    @Override
    public Float length2() {
        return mul(this).sum();
    }

    @Override
    public Float dot(Vector2F32 vector) {
        return mul(vector).sum();
    }

    @Override
    public Vector2F32 clamp(Float min, Float max) {
        final float x = Math.clamp(x(), min, max);
        final float y = Math.clamp(y(), min, max);
        return v2(x, y);
    }

    @Override
    public boolean hasSameDirection(Vector2F32 vector) {
        return dot(vector) > 0;
    }

    @Override
    public boolean hasOppositeDirection(Vector2F32 vector) {
        return dot(vector) < 0;
    }

    @Override
    public Vector2F32 ceil() {
        final float x = (float) Math.ceil(x());
        final float y = (float) Math.ceil(y());
        return v2(x, y);
    }

    @Override
    public Vector2F32 floor() {
        final float x = (float) Math.floor(x());
        final float y = (float) Math.floor(y());
        return v2(x, y);
    }

    @Override
    public Float length() {
        return (float) Math.sqrt(length2());
    }

    @Override
    public Vector2F32 withLength(Float length) {
        final float len = length();
        if (len == 0) return v2(0f, 0f);
        return mul(length / len);
    }

    @Override
    public Vector2F32 withLength2(Float length2) {
        return withLength((float) Math.sqrt(length2));
    }

    @Override
    public Vector2F32 limit(Float limit) {
        return limit2(limit * limit);
    }

    @Override
    public Vector2F32 limit2(Float limit2) {
        final float len2 = length2();
        if (len2 == 0 || len2 <= limit2) return this;
        return mul((float) Math.sqrt(limit2 / len2));
    }

    @Override
    public Vector2F32 normalize() {
        return withLength(1f);
    }

    @Override
    public Float distance(Vector2F32 vector) {
        return (float) Math.sqrt(distance2(vector));
    }

    @Override
    public Vector2F32 lerp(Vector2F32 target, Float alpha) {
        final float x = x() + (target.x() - x()) * alpha;
        final float y = y() + (target.y() - y()) * alpha;
        return v2(x, y);
    }

    @Override
    public Vector2F32 interpolate(Vector2F32 target, Float alpha, UnaryOperator<Float> interpolator) {
        return lerp(target, interpolator.apply(alpha));
    }

    @Override
    public boolean isUnit(Float margin) {
        return Math.abs(length2() - 1) < margin * margin;
    }

    @Override
    public boolean isCollinear(Vector2F32 vector, Float epsilon) {
        return Math.abs(cross(vector)) <= epsilon * length() * vector.length();
    }

    @Override
    public boolean isPerpendicular(Vector2F32 vector, Float epsilon) {
        return Math.abs(dot(vector)) <= epsilon * length() * vector.length();
    }

    @Override
    public boolean epsilonEquals(Vector2F32 vector, Float epsilon) {
        return vector.sub(this).abs()
                .ltEq(v2(epsilon, epsilon));
    }

    @Override
    public boolean isZero(Float epsilon) {
        return epsilonEquals(v2(0f, 0f), epsilon);
    }
}
