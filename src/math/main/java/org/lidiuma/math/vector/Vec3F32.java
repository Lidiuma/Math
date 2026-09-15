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

import org.lidiuma.math.api.traits.vector.FloatingVector3Ops;
import org.lidiuma.math.api.tuple.UnaryTuple3;
import org.lidiuma.math.api.vector.Vector3;
import org.lidiuma.math.numerics.FloatNumeric;
import org.lidiuma.math.processor.AliasExclude;
import org.lidiuma.math.processor.FactoryAlias;
import org.lidiuma.math.processor.FieldAlias;
import org.lidiuma.math.processor.NamedAlias;
import java.util.function.UnaryOperator;
import static java.lang.Math.fma;
import static org.lidiuma.math.internal.AnnotationConst.*;

@FactoryAlias(methodName = VEC3_FACTORY, outputClass = VECTOR_OUT)
public value record Vec3F32(
        @Override Float x,
        @Override Float y,
        @Override Float z
) implements Vector3<Float> {

    @FieldAlias(outputClass = VECTOR_OUT)
    public static final Ops OPS = new Ops();

    /// A constructor creating a specialized vector from a generic tuple.
    @AliasExclude // This method can be a performance sink if used inappropriately, so I exclude it from the alias.
    public Vec3F32(UnaryTuple3<Float> vec) {
        this(vec.x(), vec.y(), vec.z());
    }

    public static final value class Ops implements FloatingVector3Ops<Vec3F32, Float> {

        private Ops() {}

        @Override
        @AliasExclude
        public Vec3F32 of(Float x, Float y, Float z) {
            return new Vec3F32(x, y, z);
        }

        @Override
        @NamedAlias(methodName = ZERO_FACTORY + UPPER_VEC3_FACTORY + F32)
        public Vec3F32 zero() {
            return of(0f, 0f, 0f);
        }

        @Override
        @NamedAlias(methodName = ONE_FACTORY + UPPER_VEC3_FACTORY + F32)
        public Vec3F32 one() {
            return of(1f, 1f, 1f);
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
        public Vec3F32 sqrt(Vec3F32 operand) {
            return of(
                    (float) Math.sqrt(operand.x()),
                    (float) Math.sqrt(operand.y()),
                    (float) Math.sqrt(operand.z())
            );
        }

        @Override
        public Vec3F32 ceil(Vec3F32 operand) {
            return of(
                    (float) Math.ceil(operand.x()),
                    (float) Math.ceil(operand.y()),
                    (float) Math.ceil(operand.z())
            );
        }

        @Override
        public Vec3F32 floor(Vec3F32 operand) {
            return of(
                    (float) Math.floor(operand.x()),
                    (float) Math.floor(operand.y()),
                    (float) Math.floor(operand.z())
            );
        }

        @Override
        public boolean epsilonEquals(Vec3F32 v1, Vec3F32 v2, Float epsilon) {
            final var vec = abs(subtract(v1, v2));
            if (vec.x() > epsilon) return false;
            if (vec.y() > epsilon) return false;
            return vec.z() <= epsilon;
        }

        @Override
        public Vec3F32 signum(Vec3F32 vector) {
            return of(
                    Math.signum(vector.x()),
                    Math.signum(vector.y()),
                    Math.signum(vector.z())
            );
        }

        @Override
        public Float distance(Vec3F32 v1, Vec3F32 v2) {
            return (float) Math.sqrt(distanceSquared(v1, v2));
        }

        @Override
        public Float length(Vec3F32 vector) {
            return (float) Math.sqrt(lengthSquared(vector));
        }

        @Override
        public Vec3F32 withLength(Vec3F32 vector, Float length) {
            return withMagnitude(vector, length, length(vector));
        }

        @Override
        public Vec3F32 withLimit(Vec3F32 vector, Float limit) {
            final var length = length(vector);
            if (length <= limit) return vector;
            return withMagnitude(vector, limit, length);
        }

        @Override
        public Vec3F32 normalize(Vec3F32 vector) {
            return withLength(vector, 1f);
        }

        @Override
        public Vec3F32 normalizeOrElse(Vec3F32 vector, Float epsilon, Vec3F32 fallback) {
            if (epsilonEquals(vector, zero(), epsilon)) return fallback;
            return normalize(vector);
        }

        @Override
        public Vec3F32 abs(Vec3F32 vector) {
            return of(
                    Math.abs(vector.x()),
                    Math.abs(vector.y()),
                    Math.abs(vector.z())
            );
        }

        @Override
        public Vec3F32 interpolate(Vec3F32 start, Vec3F32 end, Float alpha, UnaryOperator<Float> easing) {
            final float eased = easing.apply(alpha);
            final float inv = 1f - eased;
            return of(
                    fma(start.x(), inv, end.x() * eased),
                    fma(start.y(), inv, end.y() * eased),
                    fma(start.z(), inv, end.z() * eased)
            );
        }

        @Override
        public Vec3F32 lerp(Vec3F32 start, Vec3F32 end, Float alpha) {
            return interpolate(start, end, alpha, UnaryOperator.identity());
        }

        @Override
        public Vec3F32 cross(Vec3F32 v1, Vec3F32 v2) {
            return of(
                    v1.y() * v2.z() - v1.z() * v2.y(),
                    v1.z() * v2.x() - v1.x() * v2.z(),
                    v1.x() * v2.y() - v1.y() * v2.x()
            );
        }

        @Override
        public Float sum(Vec3F32 vector) {
            return vector.x() + vector.y() + vector.z();
        }

        @Override
        public Vec3F32 multiply(Vec3F32 vector, Float scalar) {
            return multiply(vector, of(scalar, scalar, scalar));
        }

        @Override
        public Vec3F32 clamp(Vec3F32 vector, Float min, Float max) {
            return clamp(vector, of(min, min, min), of(max, max, max));
        }

        @Override
        public Vec3F32 clamp(Vec3F32 value, Vec3F32 min, Vec3F32 max) {
            return of(
                    Math.clamp(value.x(), min.x(), max.x()),
                    Math.clamp(value.y(), min.y(), max.y()),
                    Math.clamp(value.z(), min.z(), max.z())
            );
        }

        @Override
        public Vec3F32 add(Vec3F32 op1, Vec3F32 op2) {
            return of(
                    op1.x() + op2.x(),
                    op1.y() + op2.y(),
                    op1.z() + op2.z()
            );
        }

        @Override
        public Vec3F32 multiply(Vec3F32 op1, Vec3F32 op2) {
            return of(
                    op1.x() * op2.x(),
                    op1.y() * op2.y(),
                    op1.z() * op2.z()
            );
        }

        @Override
        public Vec3F32 divide(Vec3F32 op1, Vec3F32 op2) {
            return of(
                    op1.x() / op2.x(),
                    op1.y() / op2.y(),
                    op1.z() / op2.z()
            );
        }

        @Override
        public Vec3F32 remainder(Vec3F32 op1, Vec3F32 op2) {
            return of(
                    op1.x() % op2.x(),
                    op1.y() % op2.y(),
                    op1.z() % op2.z()
            );
        }

        @Override
        public Vec3F32 negated(Vec3F32 operand) {
            return of(
                    -operand.x(),
                    -operand.y(),
                    -operand.z()
            );
        }

        @Override
        public Float distanceSquared(Vec3F32 a, Vec3F32 b) {
            final var sub = subtract(a, b);
            return sum(multiply(sub, sub));
        }

        @Override
        public Float lengthSquared(Vec3F32 vector) {
            return dot(vector, vector);
        }

        @Override
        public Float dot(Vec3F32 v1, Vec3F32 v2) {
            return sum(multiply(v1, v2));
        }

        @Override
        public Vec3F32 subtract(Vec3F32 op1, Vec3F32 op2) {
            return of(
                    op1.x() - op2.x(),
                    op1.y() - op2.y(),
                    op1.z() - op2.z()
            );
        }

        private Vec3F32 withMagnitude(Vec3F32 vector, float wanted, float current) {
            return multiply(vector, wanted / current);
        }
    }
}
