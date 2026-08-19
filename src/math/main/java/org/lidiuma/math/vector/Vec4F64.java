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
            return FloatingVector4Ops.super.zero();
        }

        @Override
        @NamedAlias(methodName = ONE_FACTORY + UPPER_VEC4_FACTORY + F64)
        public Vec4F64 one() {
            return FloatingVector4Ops.super.one();
        }

        @Override
        @AliasExclude
        public DoubleNumeric scalarOps() {
            return DoubleNumeric.OPS;
        }
    }
}
