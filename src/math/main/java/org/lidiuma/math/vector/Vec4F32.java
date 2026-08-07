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
import org.lidiuma.math.api.vector.Vector4;
import org.lidiuma.math.numerics.FloatNumeric;
import org.lidiuma.math.processor.AliasExclude;
import org.lidiuma.math.processor.Alias;
import org.lidiuma.math.processor.FactoryAlias;
import org.lidiuma.math.rotation.AngleF32;

@FactoryAlias(methodName = "vec4", outputClass = "Vectors")
public value record Vec4F32(
        @Override @NullRestricted Float x,
        @Override @NullRestricted Float y,
        @Override @NullRestricted Float z,
        @Override @NullRestricted Float w
) implements Vector4<Float> {

    @Alias(outputClass = "Vectors")
    public static final Ops OPS = new Ops();

    @AliasExclude
    public Vec4F32(Vector4<Float> vec) {
        this(vec.x(), vec.y(), vec.z(), vec.w());
    }

    public static final class Ops implements FloatingVector4Ops<Vec4F32, AngleF32, Float> {

        @Override
        @AliasExclude
        public Vec4F32 of(Float x, Float y, Float z, Float w) {
            return new Vec4F32(x, y, z, w);
        }

        @Override
        public AngleF32 angle(Vec4F32 v1, Vec4F32 v2) {
            final float dot = dot(v1, v2);
            final float length1 = lengthSquared(v1);
            final float length2 = lengthSquared(v2);
            final float theta = (float) (dot / Math.sqrt(length1 * length2));
            return AngleF32.radians((float) Math.acos(theta));
        }

        @Override
        @AliasExclude
        public FloatNumeric scalarOps() {
            return FloatNumeric.WITNESS;
        }

        @Override
        @AliasExclude
        public Vec4F32 zero() {
            return FloatingVector4Ops.super.zero();
        }

        @Override
        @AliasExclude
        public Vec4F32 one() {
            return FloatingVector4Ops.super.one();
        }
    }
}
