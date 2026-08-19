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
import org.lidiuma.math.api.traits.vector.FloatingVector3Ops;
import org.lidiuma.math.api.tuple.UnaryTuple3;
import org.lidiuma.math.api.vector.Vector3;
import org.lidiuma.math.numerics.FloatNumeric;
import org.lidiuma.math.processor.AliasExclude;
import org.lidiuma.math.processor.FactoryAlias;
import org.lidiuma.math.processor.FieldAlias;
import org.lidiuma.math.processor.NamedAlias;
import static org.lidiuma.math.internal.AnnotationConst.*;

@FactoryAlias(methodName = VEC3_FACTORY, outputClass = VECTOR_OUT)
@LooselyConsistentValue
public value record Vec3F32(
        @Override @NullRestricted Float x,
        @Override @NullRestricted Float y,
        @Override @NullRestricted Float z
) implements Vector3<Float> {

    @FieldAlias(outputClass = VECTOR_OUT)
    public static final Ops OPS = new Ops();

    /// A constructor creating a specialized vector from a generic vector.
    @NamedAlias(methodName = VEC3_FACTORY + F32)
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
            return FloatingVector3Ops.super.zero();
        }

        @Override
        @NamedAlias(methodName = ONE_FACTORY + UPPER_VEC3_FACTORY + F32)
        public Vec3F32 one() {
            return FloatingVector3Ops.super.one();
        }

        @Override
        @AliasExclude
        public FloatNumeric scalarOps() {
            return FloatNumeric.OPS;
        }
    }
}
