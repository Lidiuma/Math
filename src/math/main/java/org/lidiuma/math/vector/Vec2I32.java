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
import org.lidiuma.math.api.traits.vector.Vector2Ops;
import org.lidiuma.math.api.vector.Vector2;
import org.lidiuma.math.numerics.IntegerNumeric;
import org.lidiuma.math.processor.Alias;
import org.lidiuma.math.processor.AliasExclude;
import org.lidiuma.math.processor.FactoryAlias;
import static org.lidiuma.math.internal.AnnotationConst.VEC2_FACTORY;
import static org.lidiuma.math.internal.AnnotationConst.VECTOR_OUT;

@FactoryAlias(methodName = VEC2_FACTORY, outputClass = VECTOR_OUT)
public value record Vec2I32(
        @Override @NullRestricted Integer x,
        @Override @NullRestricted Integer y
) implements Vector2<Integer> {

    @Alias(outputClass = VECTOR_OUT)
    public static final Ops OPS = new Ops();

    @AliasExclude
    public Vec2I32(Vector2<Integer> vec) {
        this(vec.x(), vec.y());
    }

    public static final class Ops implements Vector2Ops<Vec2I32, Integer> {

        @Override
        @AliasExclude
        public Vec2I32 of(Integer x, Integer y) {
            return new Vec2I32(x, y);
        }

        @Override
        public Vec2I32 signum(Vec2I32 vector) {
            return of(
                    Integer.signum(vector.x()),
                    Integer.signum(vector.y())
            );
        }

        @Override
        @AliasExclude
        public IntegerNumeric scalarOps() {
            return IntegerNumeric.OPS;
        }

        @Override
        @AliasExclude
        public Vec2I32 zero() {
            return Vector2Ops.super.zero();
        }

        @Override
        @AliasExclude
        public Vec2I32 one() {
            return Vector2Ops.super.one();
        }
    }
}
