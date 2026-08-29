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

import org.lidiuma.math.api.traits.vector.FloatingVector1Ops;
import org.lidiuma.math.api.tuple.UnaryTuple1;
import org.lidiuma.math.api.vector.Vector1;
import org.lidiuma.math.internal.Math28;
import org.lidiuma.math.numerics.FloatNumeric;
import org.lidiuma.math.processor.AliasExclude;
import org.lidiuma.math.processor.FactoryAlias;
import org.lidiuma.math.processor.FieldAlias;
import org.lidiuma.math.processor.NamedAlias;
import java.util.function.UnaryOperator;
import static org.lidiuma.math.internal.AnnotationConst.*;

@FactoryAlias(methodName = VEC1_FACTORY, outputClass = VECTOR_OUT)
public record Vec1F32(@Override Float x) implements Vector1<Float> {

    @FieldAlias(outputClass = VECTOR_OUT)
    public static final Ops OPS = new Ops();

    /// A constructor creating a specialized vector from a generic tuple.
    @AliasExclude // This method can be a performance sink if used inappropriately, so I exclude it from the alias.
    public Vec1F32(UnaryTuple1<Float> vec) {
        this(vec.x());
    }

    public static final class Ops implements FloatingVector1Ops<Vec1F32, Float> {

        private Ops() {}

        @Override
        @AliasExclude
        public Vec1F32 of(Float x) {
            return new Vec1F32(x);
        }

        @Override
        @NamedAlias(methodName = ZERO_FACTORY + UPPER_VEC1_FACTORY + F32)
        public Vec1F32 zero() {
            return of(0f);
        }

        @Override
        @NamedAlias(methodName = ONE_FACTORY + UPPER_VEC1_FACTORY + F32)
        public Vec1F32 one() {
            return of(1f);
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
        public Vec1F32 sqrt(Vec1F32 operand) {
            return of((float) Math.sqrt(operand.x()));
        }

        @Override
        public Vec1F32 ceil(Vec1F32 operand) {
            return of((float) Math.ceil(operand.x()));
        }

        @Override
        public Vec1F32 floor(Vec1F32 operand) {
            return of((float) Math.floor(operand.x()));
        }

        @Override
        public boolean epsilonEquals(Vec1F32 v1, Vec1F32 v2, Float epsilon) {
            return abs(subtract(v1, v2)).x() <= epsilon;
        }

        @Override
        public Vec1F32 signum(Vec1F32 vector) {
            return of(Math.signum(vector.x()));
        }

        @Override
        public Float distance(Vec1F32 v1, Vec1F32 v2) {
            return abs(subtract(v1, v2)).x();
        }

        @Override
        public Float length(Vec1F32 vector) {
            return abs(vector).x();
        }

        @Override
        public Vec1F32 withLength(Vec1F32 vector, Float length) {
            return of(length);
        }

        @Override
        public Vec1F32 withLimit(Vec1F32 vector, Float limit) {
            final var length = length(vector);
            if (length <= limit) return vector;
            return of(limit);
        }

        @Override
        public Vec1F32 normalize(Vec1F32 vector) {
            return withLength(vector, 1f);
        }

        @Override
        public Vec1F32 normalizeOrElse(Vec1F32 vector, Float epsilon, Vec1F32 fallback) {
            if (epsilonEquals(vector, zero(), epsilon)) return fallback;
            return normalize(vector);
        }

        @Override
        public Vec1F32 abs(Vec1F32 vector) {
            return of(Math.abs(vector.x()));
        }

        @Override
        public Vec1F32 interpolate(Vec1F32 start, Vec1F32 end, Float alpha, UnaryOperator<Float> easing) {
            final var eased = easing.apply(alpha);
            final var invAlpha = 1f - eased;
            final var invStart = multiply(start, invAlpha);
            final var invEnd = multiply(end, eased);
            return add(invStart, invEnd);
        }

        @Override
        public Vec1F32 lerp(Vec1F32 start, Vec1F32 end, Float alpha) {
            return interpolate(start, end, alpha, UnaryOperator.identity());
        }

        @Override
        public Float sum(Vec1F32 vector) {
            return vector.x();
        }

        @Override
        public Vec1F32 multiply(Vec1F32 vector, Float scalar) {
            return multiply(vector, of(scalar));
        }

        @Override
        public Vec1F32 clamp(Vec1F32 vector, Float min, Float max) {
            return clamp(vector, of(min), of(max));
        }

        @Override
        public Vec1F32 clamp(Vec1F32 value, Vec1F32 min, Vec1F32 max) {
            return of(Math28.clamp(value.x(), min.x(), max.x()));
        }

        @Override
        public Vec1F32 add(Vec1F32 op1, Vec1F32 op2) {
            return of(op1.x() + op2.x());
        }

        @Override
        public Vec1F32 multiply(Vec1F32 op1, Vec1F32 op2) {
            return of(op1.x() * op2.x());
        }

        @Override
        public Vec1F32 divide(Vec1F32 op1, Vec1F32 op2) {
            return of(op1.x() / op2.x());
        }

        @Override
        public Vec1F32 remainder(Vec1F32 op1, Vec1F32 op2) {
            return of(op1.x() % op2.x());
        }

        @Override
        public Vec1F32 negated(Vec1F32 operand) {
            return of(-operand.x());
        }

        @Override
        public Float distanceSquared(Vec1F32 a, Vec1F32 b) {
            final var sub = subtract(a, b);
            return sum(multiply(sub, sub));
        }

        @Override
        public Float lengthSquared(Vec1F32 vector) {
            return dot(vector, vector);
        }

        @Override
        public Float dot(Vec1F32 v1, Vec1F32 v2) {
            return sum(multiply(v1, v2));
        }

        @Override
        public Vec1F32 subtract(Vec1F32 op1, Vec1F32 op2) {
            return of(op1.x() - op2.x());
        }
    }
}
