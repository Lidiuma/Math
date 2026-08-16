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
import org.lidiuma.math.api.traits.vector.Vector3Ops;
import org.lidiuma.math.api.tuple.UnaryTuple3;
import org.lidiuma.math.api.vector.Vector3;
import org.lidiuma.math.numerics.IntegerNumeric;
import org.lidiuma.math.processor.AliasExclude;
import org.lidiuma.math.processor.FactoryAlias;
import org.lidiuma.math.processor.FieldAlias;
import org.lidiuma.math.processor.NamedAlias;
import static org.lidiuma.math.internal.AnnotationConst.*;

@FactoryAlias(methodName = VEC3_FACTORY, outputClass = VECTOR_OUT)
public value record Vec3I32(
        @Override @NullRestricted Integer x,
        @Override @NullRestricted Integer y,
        @Override @NullRestricted Integer z
) implements Vector3<Integer> {

    @FieldAlias(outputClass = VECTOR_OUT)
    public static final Ops OPS = new Ops();

    /// A constructor creating a specialized vector from a generic vector.
    @NamedAlias(methodName = VEC3_FACTORY + I32)
    public Vec3I32(UnaryTuple3<Integer> vec) {
        this(vec.x(), vec.y(), vec.z());
    }

    public static final class Ops implements Vector3Ops<Vec3I32, Integer> {

        @Override
        @AliasExclude
        public Vec3I32 of(Integer x, Integer y, Integer z) {
            return new Vec3I32(x, y, z);
        }

        @Override
        public Vec3I32 signum(Vec3I32 vector) {
            return of(
                    Integer.signum(vector.x()),
                    Integer.signum(vector.y()),
                    Integer.signum(vector.z())
            );
        }

        @Override
        @NamedAlias(methodName = ZERO_FACTORY + UPPER_VEC3_FACTORY + I32)
        public Vec3I32 zero() {
            return Vector3Ops.super.zero();
        }

        @Override
        @NamedAlias(methodName = ONE_FACTORY + UPPER_VEC3_FACTORY + I32)
        public Vec3I32 one() {
            return Vector3Ops.super.one();
        }

        @Override
        @AliasExclude
        public IntegerNumeric scalarOps() {
            return IntegerNumeric.OPS;
        }
    }
}
