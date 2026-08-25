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

package org.lidiuma.math.vector;

import jdk.internal.vm.annotation.NullRestricted;
import org.lidiuma.math.api.traits.vector.FloatingVector2Ops;
import org.lidiuma.math.api.tuple.UnaryTuple2;
import org.lidiuma.math.api.vector.Vector2;
import org.lidiuma.math.numerics.FloatNumeric;
import org.lidiuma.math.processor.AliasExclude;
import org.lidiuma.math.processor.FactoryAlias;
import org.lidiuma.math.processor.FieldAlias;
import org.lidiuma.math.processor.NamedAlias;
import java.util.function.UnaryOperator;
import static org.lidiuma.math.internal.AnnotationConst.*;

@FactoryAlias(methodName = VEC2_FACTORY, outputClass = VECTOR_OUT)
public value record Vec2F32(
        @Override @NullRestricted Float x,
        @Override @NullRestricted Float y
) implements Vector2<Float> {

    @FieldAlias(outputClass = VECTOR_OUT)
    public static final Ops OPS = new Ops();

    /// A constructor creating a specialized vector from a generic vector.
    @NamedAlias(methodName = VEC2_FACTORY + F32)
    public Vec2F32(UnaryTuple2<Float> vec) {
        this(vec.x(), vec.y());
    }

    public static final value class Ops implements FloatingVector2Ops<Vec2F32, Float> {

        private Ops() {}

        @Override
        @AliasExclude
        public Vec2F32 of(Float x, Float y) {
            return new Vec2F32(x, y);
        }

        @Override
        @NamedAlias(methodName = ZERO_FACTORY + UPPER_VEC2_FACTORY + F32)
        public Vec2F32 zero() {
            return of(0f, 0f);
        }

        @Override
        @NamedAlias(methodName = ONE_FACTORY + UPPER_VEC2_FACTORY + F32)
        public Vec2F32 one() {
            return of(1f, 1f);
        }

        @Override
        @AliasExclude
        public FloatNumeric scalarOps() {
            return FloatNumeric.OPS;
        }

        /*
        Handwritten to remove GC collections when used polymorphically (different generic parameters).
        Speed is more or less the same, but without a rare case of the JIT failing giving x10 less performance.
        This unfortunately creates code duplication, and I'm sure some methods are fine as-is,
        but writing them anyway is faster than making sure with benchmarking.
        */

        @Override
        public Vec2F32 sqrt(Vec2F32 operand) {
            return of(
                    (float) Math.sqrt(operand.x()),
                    (float) Math.sqrt(operand.y())
            );
        }

        @Override
        public Vec2F32 ceil(Vec2F32 operand) {
            return of(
                    (float) Math.ceil(operand.x()),
                    (float) Math.ceil(operand.y())
            );
        }

        @Override
        public Vec2F32 floor(Vec2F32 operand) {
            return of(
                    (float) Math.floor(operand.x()),
                    (float) Math.floor(operand.y())
            );
        }

        @Override
        public boolean epsilonEquals(Vec2F32 v1, Vec2F32 v2, Float epsilon) {
            final var vec = abs(subtract(v1, v2));
            if (vec.x() > epsilon) return false;
            return vec.y() <= epsilon;
        }

        @Override
        public Vec2F32 signum(Vec2F32 vector) {
            return of(
                    Math.signum(vector.x()),
                    Math.signum(vector.y())
            );
        }

        @Override
        public Float distance(Vec2F32 v1, Vec2F32 v2) {
            return (float) Math.sqrt(distanceSquared(v1, v2));
        }

        @Override
        public Float length(Vec2F32 vector) {
            return (float) Math.sqrt(lengthSquared(vector));
        }

        @Override
        public Vec2F32 withLength(Vec2F32 vector, Float length) {
            return withMagnitude(vector, length, length(vector));
        }

        @Override
        public Vec2F32 withLimit(Vec2F32 vector, Float limit) {
            final var length = length(vector);
            if (length <= limit) return vector;
            return withMagnitude(vector, limit, length);
        }

        @Override
        public Vec2F32 normalize(Vec2F32 vector) {
            return withLength(vector, 1f);
        }

        @Override
        public Vec2F32 normalizeOrElse(Vec2F32 vector, Float epsilon, Vec2F32 fallback) {
            if (epsilonEquals(vector, zero(), epsilon)) return fallback;
            return normalize(vector);
        }

        @Override
        public Vec2F32 abs(Vec2F32 vector) {
            return of(
                    Math.abs(vector.x()),
                    Math.abs(vector.y())
            );
        }

        @Override
        public Vec2F32 interpolate(Vec2F32 start, Vec2F32 end, Float alpha, UnaryOperator<Float> easing) {
            final var eased = easing.apply(alpha);
            final var invAlpha = 1f - eased;
            final var invStart = multiply(start, invAlpha);
            final var invEnd = multiply(end, eased);
            return add(invStart, invEnd);
        }

        @Override
        public Vec2F32 lerp(Vec2F32 start, Vec2F32 end, Float alpha) {
            return interpolate(start, end, alpha, UnaryOperator.identity());
        }

        @Override
        public Float cross(Vec2F32 v1, Vec2F32 v2) {
            return v1.x() * v2.y() - v1.y() * v2.x();
        }

        @Override
        public Float sum(Vec2F32 vector) {
            return vector.x() + vector.y();
        }

        @Override
        public Vec2F32 multiply(Vec2F32 vector, Float scalar) {
            return multiply(vector, of(scalar, scalar));
        }

        @Override
        public Vec2F32 clamp(Vec2F32 vector, Float min, Float max) {
            return clamp(vector, of(min, min), of(max, max));
        }

        @Override
        public Vec2F32 clamp(Vec2F32 value, Vec2F32 min, Vec2F32 max) {
            return of(
                    Math.clamp(value.x(), min.x(), max.x()),
                    Math.clamp(value.y(), min.y(), max.y())
            );
        }

        @Override
        public Vec2F32 add(Vec2F32 op1, Vec2F32 op2) {
            return of(
                    op1.x() + op2.x(),
                    op1.y() + op2.y()
            );
        }

        @Override
        public Vec2F32 multiply(Vec2F32 op1, Vec2F32 op2) {
            return of(
                    op1.x() * op2.x(),
                    op1.y() * op2.y()
            );
        }

        @Override
        public Vec2F32 divide(Vec2F32 op1, Vec2F32 op2) {
            return of(
                    op1.x() / op2.x(),
                    op1.y() / op2.y()
            );
        }

        @Override
        public Vec2F32 remainder(Vec2F32 op1, Vec2F32 op2) {
            return of(
                    op1.x() % op2.x(),
                    op1.y() % op2.y()
            );
        }

        @Override
        public Vec2F32 negated(Vec2F32 operand) {
            return of(
                    -operand.x(),
                    -operand.y()
            );
        }

        @Override
        public Float distanceSquared(Vec2F32 a, Vec2F32 b) {
            final var sub = subtract(a, b);
            return sum(multiply(sub, sub));
        }

        @Override
        public Float lengthSquared(Vec2F32 vector) {
            return dot(vector, vector);
        }

        @Override
        public Float dot(Vec2F32 v1, Vec2F32 v2) {
            return sum(multiply(v1, v2));
        }

        @Override
        public Vec2F32 subtract(Vec2F32 op1, Vec2F32 op2) {
            return of(
                    op1.x() - op2.x(),
                    op1.y() - op2.y()
            );
        }

        private Vec2F32 withMagnitude(Vec2F32 vector, float wanted, float current) {
            return multiply(vector, wanted / current);
        }
    }
}
