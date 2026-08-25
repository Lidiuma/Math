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
import org.lidiuma.math.api.traits.vector.Vector4Ops;
import org.lidiuma.math.api.tuple.UnaryTuple4;
import org.lidiuma.math.api.vector.Vector4;
import org.lidiuma.math.numerics.IntegerNumeric;
import org.lidiuma.math.processor.AliasExclude;
import org.lidiuma.math.processor.FactoryAlias;
import org.lidiuma.math.processor.FieldAlias;
import org.lidiuma.math.processor.NamedAlias;
import static org.lidiuma.math.internal.AnnotationConst.*;

@FactoryAlias(methodName = VEC4_FACTORY, outputClass = VECTOR_OUT)
public value record Vec4I32(
        @Override @NullRestricted Integer x,
        @Override @NullRestricted Integer y,
        @Override @NullRestricted Integer z,
        @Override @NullRestricted Integer w
) implements Vector4<Integer> {

    @FieldAlias(outputClass = VECTOR_OUT)
    public static final Ops OPS = new Ops();

    /// A constructor creating a specialized vector from a generic tuple.
    @AliasExclude // This method can be a performance sink if used inappropriately, so I exclude it from the alias.
    public Vec4I32(UnaryTuple4<Integer> vec) {
        this(vec.x(), vec.y(), vec.z(), vec.w());
    }

    public static final value class Ops implements Vector4Ops<Vec4I32, Integer> {

        private Ops() {}

        @Override
        @AliasExclude
        public Vec4I32 of(Integer x, Integer y, Integer z, Integer w) {
            return new Vec4I32(x, y, z, w);
        }

        @Override
        public Vec4I32 signum(Vec4I32 vector) {
            return of(
                    Integer.signum(vector.x()),
                    Integer.signum(vector.y()),
                    Integer.signum(vector.z()),
                    Integer.signum(vector.w())
            );
        }

        @Override
        @NamedAlias(methodName = ZERO_FACTORY + UPPER_VEC4_FACTORY + I32)
        public Vec4I32 zero() {
            return of(0, 0, 0, 0);
        }

        @Override
        @NamedAlias(methodName = ONE_FACTORY + UPPER_VEC4_FACTORY + I32)
        public Vec4I32 one() {
            return of(1, 1, 1, 1);
        }

        @Override
        @AliasExclude
        public IntegerNumeric scalarOps() {
            return IntegerNumeric.OPS;
        }

        /*
        Handwritten to remove GC collections when used polymorphically (different generic parameters).
        Speed is more or less the same, but without a rare case of the JIT failing giving x10 less performance.
        This unfortunately creates code duplication, and I'm sure some methods are fine as-is,
        but writing them anyway is faster than making sure with benchmarking.
        */

        @Override
        public Vec4I32 abs(Vec4I32 vector) {
            return of(
                    Math.abs(vector.x()),
                    Math.abs(vector.y()),
                    Math.abs(vector.z()),
                    Math.abs(vector.w())
            );
        }

        @Override
        public Integer sum(Vec4I32 vector) {
            return vector.x() + vector.y() + vector.z() + vector.w();
        }

        @Override
        public Vec4I32 multiply(Vec4I32 vector, Integer scalar) {
            return multiply(vector, of(scalar, scalar, scalar, scalar));
        }

        @Override
        public Vec4I32 clamp(Vec4I32 vector, Integer min, Integer max) {
            return clamp(vector, of(min, min, min, min), of(max, max, max, max));
        }

        @Override
        public Vec4I32 clamp(Vec4I32 value, Vec4I32 min, Vec4I32 max) {
            return of(
                    Math.clamp(value.x(), min.x(), max.x()),
                    Math.clamp(value.y(), min.y(), max.y()),
                    Math.clamp(value.z(), min.z(), max.z()),
                    Math.clamp(value.w(), min.w(), max.w())
            );
        }

        @Override
        public Vec4I32 add(Vec4I32 op1, Vec4I32 op2) {
            return of(
                    op1.x() + op2.x(),
                    op1.y() + op2.y(),
                    op1.z() + op2.z(),
                    op1.w() + op2.w()
            );
        }

        @Override
        public Vec4I32 multiply(Vec4I32 op1, Vec4I32 op2) {
            return of(
                    op1.x() * op2.x(),
                    op1.y() * op2.y(),
                    op1.z() * op2.z(),
                    op1.w() * op2.w()
            );
        }

        @Override
        public Vec4I32 divide(Vec4I32 op1, Vec4I32 op2) {
            return of(
                    op1.x() / op2.x(),
                    op1.y() / op2.y(),
                    op1.z() / op2.z(),
                    op1.w() / op2.w()
            );
        }

        @Override
        public Vec4I32 remainder(Vec4I32 op1, Vec4I32 op2) {
            return of(
                    op1.x() % op2.x(),
                    op1.y() % op2.y(),
                    op1.z() % op2.z(),
                    op1.w() % op2.w()
            );
        }

        @Override
        public Vec4I32 negated(Vec4I32 operand) {
            return of(
                    -operand.x(),
                    -operand.y(),
                    -operand.z(),
                    -operand.w()
            );
        }

        @Override
        public Integer distanceSquared(Vec4I32 a, Vec4I32 b) {
            final var sub = subtract(a, b);
            return sum(multiply(sub, sub));
        }

        @Override
        public Integer lengthSquared(Vec4I32 vector) {
            return dot(vector, vector);
        }

        @Override
        public Integer dot(Vec4I32 v1, Vec4I32 v2) {
            return sum(multiply(v1, v2));
        }

        @Override
        public Vec4I32 subtract(Vec4I32 op1, Vec4I32 op2) {
            return of(
                    op1.x() - op2.x(),
                    op1.y() - op2.y(),
                    op1.z() - op2.z(),
                    op1.w() - op2.w()
            );
        }
    }
}
