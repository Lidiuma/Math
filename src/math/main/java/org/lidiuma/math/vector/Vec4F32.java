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

import jdk.internal.vm.annotation.LooselyConsistentValue;
import jdk.internal.vm.annotation.NullRestricted;
import org.lidiuma.math.api.traits.vector.FloatingVector4Ops;
import org.lidiuma.math.api.tuple.UnaryTuple4;
import org.lidiuma.math.api.vector.Vector4;
import org.lidiuma.math.numerics.FloatNumeric;
import org.lidiuma.math.processor.AliasExclude;
import org.lidiuma.math.processor.FactoryAlias;
import org.lidiuma.math.processor.FieldAlias;
import org.lidiuma.math.processor.NamedAlias;
import java.util.function.UnaryOperator;
import static org.lidiuma.math.internal.AnnotationConst.*;

@FactoryAlias(methodName = VEC4_FACTORY, outputClass = VECTOR_OUT)
@LooselyConsistentValue
public value record Vec4F32(
        @Override @NullRestricted Float x,
        @Override @NullRestricted Float y,
        @Override @NullRestricted Float z,
        @Override @NullRestricted Float w
) implements Vector4<Float> {

    @FieldAlias(outputClass = VECTOR_OUT)
    public static final Ops OPS = new Ops();

    /// A constructor creating a specialized vector from a generic tuple.
    @AliasExclude // This method can be a performance sink if used inappropriately, so I exclude it from the alias.
    public Vec4F32(UnaryTuple4<Float> vec) {
        this(vec.x(), vec.y(), vec.z(), vec.w());
    }

    public static final value class Ops implements FloatingVector4Ops<Vec4F32, Float> {

        private Ops() {}

        @Override
        @AliasExclude
        public Vec4F32 of(Float x, Float y, Float z, Float w) {
            return new Vec4F32(x, y, z, w);
        }

        @Override
        @NamedAlias(methodName = ZERO_FACTORY + UPPER_VEC4_FACTORY + F32)
        public Vec4F32 zero() {
            return of(0f, 0f, 0f, 0f);
        }

        @Override
        @NamedAlias(methodName = ONE_FACTORY + UPPER_VEC4_FACTORY + F32)
        public Vec4F32 one() {
            return of(1f, 1f, 1f, 1f);
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
        public Vec4F32 sqrt(Vec4F32 operand) {
            return of(
                    (float) Math.sqrt(operand.x()),
                    (float) Math.sqrt(operand.y()),
                    (float) Math.sqrt(operand.z()),
                    (float) Math.sqrt(operand.w())
            );
        }

        @Override
        public Vec4F32 ceil(Vec4F32 operand) {
            return of(
                    (float) Math.ceil(operand.x()),
                    (float) Math.ceil(operand.y()),
                    (float) Math.ceil(operand.z()),
                    (float) Math.ceil(operand.w())
            );
        }

        @Override
        public Vec4F32 floor(Vec4F32 operand) {
            return of(
                    (float) Math.floor(operand.x()),
                    (float) Math.floor(operand.y()),
                    (float) Math.floor(operand.z()),
                    (float) Math.floor(operand.w())
            );
        }

        @Override
        public boolean epsilonEquals(Vec4F32 v1, Vec4F32 v2, Float epsilon) {
            final var vec = abs(subtract(v1, v2));
            if (vec.x() > epsilon) return false;
            if (vec.y() > epsilon) return false;
            if (vec.z() > epsilon) return false;
            return vec.w() <= epsilon;
        }

        @Override
        public Vec4F32 signum(Vec4F32 vector) {
            return of(
                    Math.signum(vector.x()),
                    Math.signum(vector.y()),
                    Math.signum(vector.z()),
                    Math.signum(vector.w())
            );
        }

        @Override
        public Float distance(Vec4F32 v1, Vec4F32 v2) {
            return (float) Math.sqrt(distanceSquared(v1, v2));
        }

        @Override
        public Float length(Vec4F32 vector) {
            return (float) Math.sqrt(lengthSquared(vector));
        }

        @Override
        public Vec4F32 withLength(Vec4F32 vector, Float length) {
            return withMagnitude(vector, length, length(vector));
        }

        @Override
        public Vec4F32 withLimit(Vec4F32 vector, Float limit) {
            final var length = length(vector);
            if (length <= limit) return vector;
            return withMagnitude(vector, limit, length);
        }

        @Override
        public Vec4F32 normalize(Vec4F32 vector) {
            return withLength(vector, 1f);
        }

        @Override
        public Vec4F32 normalizeOrElse(Vec4F32 vector, Float epsilon, Vec4F32 fallback) {
            if (epsilonEquals(vector, zero(), epsilon)) return fallback;
            return normalize(vector);
        }

        @Override
        public Vec4F32 abs(Vec4F32 vector) {
            return of(
                    Math.abs(vector.x()),
                    Math.abs(vector.y()),
                    Math.abs(vector.z()),
                    Math.abs(vector.w())
            );
        }

        @Override
        public Vec4F32 interpolate(Vec4F32 start, Vec4F32 end, Float alpha, UnaryOperator<Float> easing) {
            final var eased = easing.apply(alpha);
            final var invAlpha = 1f - eased;
            final var invStart = multiply(start, invAlpha);
            final var invEnd = multiply(end, eased);
            return add(invStart, invEnd);
        }

        @Override
        public Vec4F32 lerp(Vec4F32 start, Vec4F32 end, Float alpha) {
            return interpolate(start, end, alpha, UnaryOperator.identity());
        }

        @Override
        public Float sum(Vec4F32 vector) {
            return vector.x() + vector.y() + vector.z() + vector.w();
        }

        @Override
        public Vec4F32 multiply(Vec4F32 vector, Float scalar) {
            return multiply(vector, of(scalar, scalar, scalar, scalar));
        }

        @Override
        public Vec4F32 clamp(Vec4F32 vector, Float min, Float max) {
            return clamp(vector, of(min, min, min, min), of(max, max, max, max));
        }

        @Override
        public Vec4F32 clamp(Vec4F32 value, Vec4F32 min, Vec4F32 max) {
            return of(
                    Math.clamp(value.x(), min.x(), max.x()),
                    Math.clamp(value.y(), min.y(), max.y()),
                    Math.clamp(value.z(), min.z(), max.z()),
                    Math.clamp(value.w(), min.w(), max.w())
            );
        }

        @Override
        public Vec4F32 add(Vec4F32 op1, Vec4F32 op2) {
            return of(
                    op1.x() + op2.x(),
                    op1.y() + op2.y(),
                    op1.z() + op2.z(),
                    op1.w() + op2.w()
            );
        }

        @Override
        public Vec4F32 multiply(Vec4F32 op1, Vec4F32 op2) {
            return of(
                    op1.x() * op2.x(),
                    op1.y() * op2.y(),
                    op1.z() * op2.z(),
                    op1.w() * op2.w()
            );
        }

        @Override
        public Vec4F32 divide(Vec4F32 op1, Vec4F32 op2) {
            return of(
                    op1.x() / op2.x(),
                    op1.y() / op2.y(),
                    op1.z() / op2.z(),
                    op1.w() / op2.w()
            );
        }

        @Override
        public Vec4F32 remainder(Vec4F32 op1, Vec4F32 op2) {
            return of(
                    op1.x() % op2.x(),
                    op1.y() % op2.y(),
                    op1.z() % op2.z(),
                    op1.w() % op2.w()
            );
        }

        @Override
        public Vec4F32 negated(Vec4F32 operand) {
            return of(
                    -operand.x(),
                    -operand.y(),
                    -operand.z(),
                    -operand.w()
            );
        }

        @Override
        public Float distanceSquared(Vec4F32 a, Vec4F32 b) {
            final var sub = subtract(a, b);
            return sum(multiply(sub, sub));
        }

        @Override
        public Float lengthSquared(Vec4F32 vector) {
            return dot(vector, vector);
        }

        @Override
        public Float dot(Vec4F32 v1, Vec4F32 v2) {
            return sum(multiply(v1, v2));
        }

        @Override
        public Vec4F32 subtract(Vec4F32 op1, Vec4F32 op2) {
            return of(
                    op1.x() - op2.x(),
                    op1.y() - op2.y(),
                    op1.z() - op2.z(),
                    op1.w() - op2.w()
            );
        }

        private Vec4F32 withMagnitude(Vec4F32 vector, float wanted, float current) {
            return multiply(vector, wanted / current);
        }
    }
}
