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
import org.lidiuma.math.internal.Math28;
import org.lidiuma.math.numerics.DoubleNumeric;
import org.lidiuma.math.processor.AliasExclude;
import org.lidiuma.math.processor.FactoryAlias;
import org.lidiuma.math.processor.FieldAlias;
import org.lidiuma.math.processor.NamedAlias;
import java.util.function.UnaryOperator;
import static org.lidiuma.math.internal.AnnotationConst.*;

@FactoryAlias(methodName = VEC3_FACTORY, outputClass = VECTOR_OUT)
public record Vec3F64(
        @Override Double x,
        @Override Double y,
        @Override Double z
) implements Vector3<Double> {

    @FieldAlias(outputClass = VECTOR_OUT)
    public static final Ops OPS = new Ops();

    /// A constructor creating a specialized vector from a generic tuple.
    @AliasExclude // This method can be a performance sink if used inappropriately, so I exclude it from the alias.
    public Vec3F64(UnaryTuple3<Double> vec) {
        this(vec.x(), vec.y(), vec.z());
    }

    public static final class Ops implements FloatingVector3Ops<Vec3F64, Double> {

        private Ops() {}

        @Override
        @AliasExclude
        public Vec3F64 of(Double x, Double y, Double z) {
            return new Vec3F64(x, y, z);
        }

        @Override
        @NamedAlias(methodName = ZERO_FACTORY + UPPER_VEC3_FACTORY + F64)
        public Vec3F64 zero() {
            return of(0d, 0d, 0d);
        }

        @Override
        @NamedAlias(methodName = ONE_FACTORY + UPPER_VEC3_FACTORY + F64)
        public Vec3F64 one() {
            return of(1d, 1d, 1d);
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
        public Vec3F64 sqrt(Vec3F64 operand) {
            return of(
                    Math.sqrt(operand.x()),
                    Math.sqrt(operand.y()),
                    Math.sqrt(operand.z())
            );
        }

        @Override
        public Vec3F64 ceil(Vec3F64 operand) {
            return of(
                    Math.ceil(operand.x()),
                    Math.ceil(operand.y()),
                    Math.ceil(operand.z())
            );
        }

        @Override
        public Vec3F64 floor(Vec3F64 operand) {
            return of(
                    Math.floor(operand.x()),
                    Math.floor(operand.y()),
                    Math.floor(operand.z())
            );
        }

        @Override
        public boolean epsilonEquals(Vec3F64 v1, Vec3F64 v2, Double epsilon) {
            final var vec = abs(subtract(v1, v2));
            if (vec.x() > epsilon) return false;
            if (vec.y() > epsilon) return false;
            return vec.z() <= epsilon;
        }

        @Override
        public Vec3F64 signum(Vec3F64 vector) {
            return of(
                    Math.signum(vector.x()),
                    Math.signum(vector.y()),
                    Math.signum(vector.z())
            );
        }

        @Override
        public Double distance(Vec3F64 v1, Vec3F64 v2) {
            return Math.sqrt(distanceSquared(v1, v2));
        }

        @Override
        public Double length(Vec3F64 vector) {
            return Math.sqrt(lengthSquared(vector));
        }

        @Override
        public Vec3F64 withLength(Vec3F64 vector, Double length) {
            return withMagnitude(vector, length, length(vector));
        }

        @Override
        public Vec3F64 withLimit(Vec3F64 vector, Double limit) {
            final var length = length(vector);
            if (length <= limit) return vector;
            return withMagnitude(vector, limit, length);
        }

        @Override
        public Vec3F64 normalize(Vec3F64 vector) {
            return withLength(vector, 1d);
        }

        @Override
        public Vec3F64 normalizeOrElse(Vec3F64 vector, Double epsilon, Vec3F64 fallback) {
            if (epsilonEquals(vector, zero(), epsilon)) return fallback;
            return normalize(vector);
        }

        @Override
        public Vec3F64 abs(Vec3F64 vector) {
            return of(
                    Math.abs(vector.x()),
                    Math.abs(vector.y()),
                    Math.abs(vector.z())
            );
        }

        @Override
        public Vec3F64 interpolate(Vec3F64 start, Vec3F64 end, Double alpha, UnaryOperator<Double> easing) {
            final var eased = easing.apply(alpha);
            final var invAlpha = 1d - eased;
            final var invStart = multiply(start, invAlpha);
            final var invEnd = multiply(end, eased);
            return add(invStart, invEnd);
        }

        @Override
        public Vec3F64 lerp(Vec3F64 start, Vec3F64 end, Double alpha) {
            return interpolate(start, end, alpha, UnaryOperator.identity());
        }

        @Override
        public Vec3F64 cross(Vec3F64 v1, Vec3F64 v2) {
            return of(
                    v1.y() * v2.z() - v1.z() * v2.y(),
                    v1.z() * v2.x() - v1.x() * v2.z(),
                    v1.x() * v2.y() - v1.y() * v2.x()
            );
        }

        @Override
        public Double sum(Vec3F64 vector) {
            return vector.x() + vector.y() + vector.z();
        }

        @Override
        public Vec3F64 multiply(Vec3F64 vector, Double scalar) {
            return multiply(vector, of(scalar, scalar, scalar));
        }

        @Override
        public Vec3F64 clamp(Vec3F64 vector, Double min, Double max) {
            return clamp(vector, of(min, min, min), of(max, max, max));
        }

        @Override
        public Vec3F64 clamp(Vec3F64 value, Vec3F64 min, Vec3F64 max) {
            return of(
                    Math28.clamp(value.x(), min.x(), max.x()),
                    Math28.clamp(value.y(), min.y(), max.y()),
                    Math28.clamp(value.z(), min.z(), max.z())
            );
        }

        @Override
        public Vec3F64 add(Vec3F64 op1, Vec3F64 op2) {
            return of(
                    op1.x() + op2.x(),
                    op1.y() + op2.y(),
                    op1.z() + op2.z()
            );
        }

        @Override
        public Vec3F64 multiply(Vec3F64 op1, Vec3F64 op2) {
            return of(
                    op1.x() * op2.x(),
                    op1.y() * op2.y(),
                    op1.z() * op2.z()
            );
        }

        @Override
        public Vec3F64 divide(Vec3F64 op1, Vec3F64 op2) {
            return of(
                    op1.x() / op2.x(),
                    op1.y() / op2.y(),
                    op1.z() / op2.z()
            );
        }

        @Override
        public Vec3F64 remainder(Vec3F64 op1, Vec3F64 op2) {
            return of(
                    op1.x() % op2.x(),
                    op1.y() % op2.y(),
                    op1.z() % op2.z()
            );
        }

        @Override
        public Vec3F64 negated(Vec3F64 operand) {
            return of(
                    -operand.x(),
                    -operand.y(),
                    -operand.z()
            );
        }

        @Override
        public Double distanceSquared(Vec3F64 a, Vec3F64 b) {
            final var sub = subtract(a, b);
            return sum(multiply(sub, sub));
        }

        @Override
        public Double lengthSquared(Vec3F64 vector) {
            return dot(vector, vector);
        }

        @Override
        public Double dot(Vec3F64 v1, Vec3F64 v2) {
            return sum(multiply(v1, v2));
        }

        @Override
        public Vec3F64 subtract(Vec3F64 op1, Vec3F64 op2) {
            return of(
                    op1.x() - op2.x(),
                    op1.y() - op2.y(),
                    op1.z() - op2.z()
            );
        }

        private Vec3F64 withMagnitude(Vec3F64 vector, double wanted, double current) {
            return multiply(vector, wanted / current);
        }
    }
}
