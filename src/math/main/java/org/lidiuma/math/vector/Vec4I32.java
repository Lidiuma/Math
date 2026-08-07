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
import org.lidiuma.math.api.vector.Vector4;
import org.lidiuma.math.numerics.IntegerNumeric;
import org.lidiuma.math.processor.AliasExclude;
import org.lidiuma.math.processor.Alias;
import org.lidiuma.math.processor.FactoryAlias;

@FactoryAlias(methodName = "vec4", outputClass = "Vectors")
public value record Vec4I32(
        @Override @NullRestricted Integer x,
        @Override @NullRestricted Integer y,
        @Override @NullRestricted Integer z,
        @Override @NullRestricted Integer w
) implements Vector4<Integer> {

    @Alias(outputClass = "Vectors")
    public static final Ops OPS = new Ops();

    @AliasExclude
    public Vec4I32(Vector4<Integer> vec) {
        this(vec.x(), vec.y(), vec.z(), vec.w());
    }

    public static final class Ops implements Vector4Ops<Vec4I32, Integer> {

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
        @AliasExclude
        public IntegerNumeric scalarOps() {
            return IntegerNumeric.OPS;
        }

        @Override
        @AliasExclude
        public Vec4I32 zero() {
            return Vector4Ops.super.zero();
        }

        @Override
        @AliasExclude
        public Vec4I32 one() {
            return Vector4Ops.super.one();
        }
    }
}
