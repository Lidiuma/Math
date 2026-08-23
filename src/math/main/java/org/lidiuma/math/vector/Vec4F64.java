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
import org.lidiuma.math.api.traits.vector.FloatingVector4Ops;
import org.lidiuma.math.api.tuple.UnaryTuple4;
import org.lidiuma.math.api.vector.Vector4;
import org.lidiuma.math.numerics.DoubleNumeric;
import org.lidiuma.math.processor.AliasExclude;
import org.lidiuma.math.processor.FactoryAlias;
import org.lidiuma.math.processor.FieldAlias;
import org.lidiuma.math.processor.NamedAlias;
import java.util.function.UnaryOperator;
import static org.lidiuma.math.internal.AnnotationConst.*;

@FactoryAlias(methodName = VEC4_FACTORY, outputClass = VECTOR_OUT)
public value record Vec4F64(
        @Override @NullRestricted Double x,
        @Override @NullRestricted Double y,
        @Override @NullRestricted Double z,
        @Override @NullRestricted Double w
) implements Vector4<Double> {

    @FieldAlias(outputClass = VECTOR_OUT)
    public static final Ops OPS = new Ops();

    /// A constructor creating a specialized vector from a generic vector.
    @NamedAlias(methodName = VEC4_FACTORY + F64)
    public Vec4F64(UnaryTuple4<Double> vec) {
        this(vec.x(), vec.y(), vec.z(), vec.w());
    }

    public static final value class Ops implements FloatingVector4Ops<Vec4F64, Double> {

        private Ops() {}

        @Override
        @AliasExclude
        public Vec4F64 of(Double x, Double y, Double z, Double w) {
            return new Vec4F64(x, y, z, w);
        }

        @Override
        @NamedAlias(methodName = ZERO_FACTORY + UPPER_VEC4_FACTORY + F64)
        public Vec4F64 zero() {
            return of(0d, 0d, 0d, 0d);
        }

        @Override
        @NamedAlias(methodName = ONE_FACTORY + UPPER_VEC4_FACTORY + F64)
        public Vec4F64 one() {
            return of(1d, 1d, 1d, 1d);
        }

        @Override
        @AliasExclude
        public DoubleNumeric scalarOps() {
            return DoubleNumeric.OPS;
        }

        /*
        Handwritten to remove GC collections when used polymorphically (different generic parameters).
        Speed is more or less the same, but without a rare case of the JIT failing giving x10 less performance.
        This unfortunately creates code duplication, and I'm sure some methods are fine as-is,
        but writing them anyway is faster than making sure with benchmarking.
        */

        @Override
        public Vec4F64 sqrt(Vec4F64 operand) {
            return of(
                    Math.sqrt(operand.x()),
                    Math.sqrt(operand.y()),
                    Math.sqrt(operand.z()),
                    Math.sqrt(operand.w())
            );
        }

        @Override
        public Vec4F64 ceil(Vec4F64 operand) {
            return of(
                    Math.ceil(operand.x()),
                    Math.ceil(operand.y()),
                    Math.ceil(operand.z()),
                    Math.ceil(operand.w())
            );
        }

        @Override
        public Vec4F64 floor(Vec4F64 operand) {
            return of(
                    Math.floor(operand.x()),
                    Math.floor(operand.y()),
                    Math.floor(operand.z()),
                    Math.floor(operand.w())
            );
        }

        @Override
        public boolean epsilonEquals(Vec4F64 v1, Vec4F64 v2, Double epsilon) {
            final var vec = abs(subtract(v1, v2));
            if (vec.x() > epsilon) return false;
            if (vec.y() > epsilon) return false;
            if (vec.z() > epsilon) return false;
            return vec.w() <= epsilon;
        }

        @Override
        public Vec4F64 signum(Vec4F64 vector) {
            return of(
                    Math.signum(vector.x()),
                    Math.signum(vector.y()),
                    Math.signum(vector.z()),
                    Math.signum(vector.w())
            );
        }

        @Override
        public Double distance(Vec4F64 v1, Vec4F64 v2) {
            return Math.sqrt(distanceSquared(v1, v2));
        }

        @Override
        public Double length(Vec4F64 vector) {
            return Math.sqrt(lengthSquared(vector));
        }

        @Override
        public Vec4F64 withLength(Vec4F64 vector, Double length) {
            return withMagnitude(vector, length, length(vector));
        }

        @Override
        public Vec4F64 withLimit(Vec4F64 vector, Double limit) {
            final var length = length(vector);
            if (length <= limit) return vector;
            return withMagnitude(vector, limit, length);
        }

        @Override
        public Vec4F64 normalize(Vec4F64 vector) {
            return withLength(vector, 1d);
        }

        @Override
        public Vec4F64 normalizeOrElse(Vec4F64 vector, Double epsilon, Vec4F64 fallback) {
            if (epsilonEquals(vector, zero(), epsilon)) return fallback;
            return normalize(vector);
        }

        @Override
        public Vec4F64 abs(Vec4F64 vector) {
            return of(
                    Math.abs(vector.x()),
                    Math.abs(vector.y()),
                    Math.abs(vector.z()),
                    Math.abs(vector.w())
            );
        }

        @Override
        public Vec4F64 interpolate(Vec4F64 start, Vec4F64 end, Double alpha, UnaryOperator<Double> easing) {
            final var eased = easing.apply(alpha);
            final var invAlpha = 1d - eased;
            final var invStart = multiply(start, invAlpha);
            final var invEnd = multiply(end, eased);
            return add(invStart, invEnd);
        }

        @Override
        public Double sum(Vec4F64 vector) {
            return vector.x() + vector.y() + vector.z() + vector.w();
        }

        @Override
        public Vec4F64 multiply(Vec4F64 vector, Double scalar) {
            return multiply(vector, of(scalar, scalar, scalar, scalar));
        }

        @Override
        public Vec4F64 clamp(Vec4F64 vector, Double min, Double max) {
            return clamp(vector, of(min, min, min, min), of(max, max, max, max));
        }

        @Override
        public Vec4F64 clamp(Vec4F64 value, Vec4F64 min, Vec4F64 max) {
            return of(
                    Math.clamp(value.x(), min.x(), max.x()),
                    Math.clamp(value.y(), min.y(), max.y()),
                    Math.clamp(value.z(), min.z(), max.z()),
                    Math.clamp(value.w(), min.w(), max.w())
            );
        }

        @Override
        public Vec4F64 add(Vec4F64 op1, Vec4F64 op2) {
            return of(
                    op1.x() + op2.x(),
                    op1.y() + op2.y(),
                    op1.z() + op2.z(),
                    op1.w() + op2.w()
            );
        }

        @Override
        public Vec4F64 multiply(Vec4F64 op1, Vec4F64 op2) {
            return of(
                    op1.x() * op2.x(),
                    op1.y() * op2.y(),
                    op1.z() * op2.z(),
                    op1.w() * op2.w()
            );
        }

        @Override
        public Vec4F64 divide(Vec4F64 op1, Vec4F64 op2) {
            return of(
                    op1.x() / op2.x(),
                    op1.y() / op2.y(),
                    op1.z() / op2.z(),
                    op1.w() / op2.w()
            );
        }

        @Override
        public Vec4F64 remainder(Vec4F64 op1, Vec4F64 op2) {
            return of(
                    op1.x() % op2.x(),
                    op1.y() % op2.y(),
                    op1.z() % op2.z(),
                    op1.w() % op2.w()
            );
        }

        @Override
        public Vec4F64 negated(Vec4F64 operand) {
            return of(
                    -operand.x(),
                    -operand.y(),
                    -operand.z(),
                    -operand.w()
            );
        }

        @Override
        public Double distanceSquared(Vec4F64 a, Vec4F64 b) {
            final var sub = subtract(a, b);
            return sum(multiply(sub, sub));
        }

        @Override
        public Double lengthSquared(Vec4F64 vector) {
            return dot(vector, vector);
        }

        @Override
        public Double dot(Vec4F64 v1, Vec4F64 v2) {
            return sum(multiply(v1, v2));
        }

        @Override
        public Vec4F64 subtract(Vec4F64 op1, Vec4F64 op2) {
            return of(
                    op1.x() - op2.x(),
                    op1.y() - op2.y(),
                    op1.z() - op2.z(),
                    op1.w() - op2.w()
            );
        }

        private Vec4F64 withMagnitude(Vec4F64 vector, Double wanted, Double current) {
            return multiply(vector, wanted / current);
        }
    }
}
