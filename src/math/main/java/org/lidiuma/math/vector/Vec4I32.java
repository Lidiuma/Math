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

    /// A constructor creating a specialized vector from a generic vector.
    @NamedAlias(methodName = VEC4_FACTORY + I32)
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
            return Vector4Ops.super.zero();
        }

        @Override
        @NamedAlias(methodName = ONE_FACTORY + UPPER_VEC4_FACTORY + I32)
        public Vec4I32 one() {
            return Vector4Ops.super.one();
        }

        @Override
        @AliasExclude
        public IntegerNumeric scalarOps() {
            return IntegerNumeric.OPS;
        }
    }
}
