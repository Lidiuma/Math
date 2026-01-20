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
import org.lidiuma.math.vector.v2.Vector2F32;
import jdk.internal.vm.annotation.LooselyConsistentValue;
import jdk.internal.vm.annotation.NullRestricted;
import java.util.function.UnaryOperator;
import static org.lidiuma.math.vector.Vectors.v1;

@LooselyConsistentValue
public value record Vector1F32(@NullRestricted Float x
) implements Vector1<Vector1F32, Float>, Vector1.Real<Vector1F32, Float>, Point1.F32 {

    @Override
    public Vector1I32 asInt() {
        return v1((int) (float) x());
    }

    public Vector1F64 asF64() {
        return new Vector1F64((double) x());
    }

    @Override
    public Vector1F32 ceil() {
        return v1((float) Math.ceil(x()));
    }

    @Override
    public Vector1F32 floor() {
        return v1((float) Math.floor(x()));
    }

    @Override
    public Float length() {
        return abs().x();
    }

    @Override
    public Vector1F32 withLength(Float length) {
        final float len = length();
        if (len == 0 || len == length) return this;
        return v1(x() * length / len);
    }

    @Override
    public Vector1F32 withLength2(Float length2) {
        return withLength((float) Math.sqrt(length2));
    }

    @Override
    public Vector1F32 limit(Float limit) {
        return limit2(limit * limit);
    }

    @Override
    public Vector1F32 limit2(Float limit2) {
        final float len2 = length2();
        if (len2 == 0 || len2 <= limit2) return this;
        return v1(x() * (float) Math.sqrt(limit2 / len2));
    }

    @Override
    public Vector1F32 normalize() {
        return withLength(1f);
    }

    @Override
    public Float distance(Vector1F32 vector) {
        final float delta = x() - vector.x();
        return Math.abs(delta);
    }

    @Override
    public Vector1F32 lerp(Vector1F32 target, Float alpha) {
        final float invAlpha = 1 - alpha;
        return v1(x() * invAlpha + target.x() * alpha);
    }

    @Override
    public Vector1F32 interpolate(Vector1F32 target, Float alpha, UnaryOperator<Float> interpolator) {
        return lerp(target, interpolator.apply(alpha));
    }

    @Override
    public boolean isUnit(Float margin) {
        return Math.abs(length2() - 1) < margin;
    }

    @Override
    public boolean isCollinear(Vector1F32 vector, Float epsilon) {
        return x() != 0 && vector.x() != 0;
    }

    @Override
    public boolean isPerpendicular(Vector1F32 vector, Float epsilon) {
        return Math.abs(dot(vector)) <= epsilon;
    }

    @Override
    public boolean epsilonEquals(Vector1F32 vector, Float epsilon) {
        return Math.abs(x() - vector.x()) <= epsilon;
    }

    @Override
    public boolean isZero(Float epsilon) {
        return epsilonEquals(v1(0f), epsilon);
    }

    @Override
    public Vector2F32 asV2(Float y) {
        return new Vector2F32(x(), y);
    }

    @Override
    public Float component(int index) throws IndexOutOfBoundsException {
        if (index != 0) throw new IndexOutOfBoundsException("There's no component for index " + index + ".");
        return x();
    }

    @Override
    public Float sum() {
        return x();
    }

    @Override
    public Vector1F32 add(Vector1F32 other) {
        return v1(x() + other.x());
    }

    @Override
    public Vector1F32 sub(Vector1F32 other) {
        return v1(x() - other.x());
    }

    @Override
    public Vector1F32 mul(Vector1F32 other) {
        return v1(x() * other.x());
    }

    @Override
    public Vector1F32 mul(Float scalar) {
        return v1(x() * scalar);
    }

    @Override
    public Vector1F32 div(Vector1F32 other) {
        return v1(x() / other.x());
    }

    @Override
    public boolean lt(Vector1F32 other) {
        return x() < other.x();
    }

    @Override
    public boolean ltEq(Vector1F32 other) {
        return x() <= other.x();
    }

    @Override
    public boolean gt(Vector1F32 other) {
        return x() > other.x();
    }

    @Override
    public boolean gtEq(Vector1F32 other) {
        return x() >= other.x();
    }

    @Override
    public Vector1F32 abs() {
        return v1(Math.abs(x()));
    }

    @Override
    public Vector1F32 max(Vector1F32 other) {
        return v1(Math.max(x(), other.x()));
    }

    @Override
    public Vector1F32 min(Vector1F32 other) {
        return v1(Math.min(x(), other.x()));
    }

    @Override
    public Float distance2(Vector1F32 vector) {
        final float delta = x() - vector.x();
        return delta * delta;
    }

    @Override
    public Float length2() {
        return x() * x();
    }

    @Override
    public Float dot(Vector1F32 vector) {
        return x() * vector.x();
    }

    @Override
    public Vector1F32 clamp(Float min, Float max) {
        return v1(Math.clamp(x(), min, max));
    }

    @Override
    public boolean hasSameDirection(Vector1F32 vector) {
        return dot(vector) > 0;
    }

    @Override
    public boolean hasOppositeDirection(Vector1F32 vector) {
        return dot(vector) < 0;
    }
}
