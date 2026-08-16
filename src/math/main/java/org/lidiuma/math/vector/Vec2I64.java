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
import org.lidiuma.math.api.tuple.UnaryTuple2;
import org.lidiuma.math.api.vector.Vector2;
import org.lidiuma.math.numerics.LongNumeric;
import org.lidiuma.math.processor.AliasExclude;
import org.lidiuma.math.processor.FactoryAlias;
import org.lidiuma.math.processor.FieldAlias;
import org.lidiuma.math.processor.NamedAlias;
import static org.lidiuma.math.internal.AnnotationConst.*;

@FactoryAlias(methodName = VEC2_FACTORY, outputClass = VECTOR_OUT)
public value record Vec2I64(
        @Override @NullRestricted Long x,
        @Override @NullRestricted Long y
) implements Vector2<Long> {

    @FieldAlias(outputClass = VECTOR_OUT)
    public static final Ops OPS = new Ops();

    /// A constructor creating a specialized vector from a generic vector.
    @NamedAlias(methodName = VEC2_FACTORY + I64)
    public Vec2I64(UnaryTuple2<Long> vec) {
        this(vec.x(), vec.y());
    }

    public static final class Ops implements Vector2Ops<Vec2I64, Long> {

        @Override
        @AliasExclude
        public Vec2I64 of(Long x, Long y) {
            return new Vec2I64(x, y);
        }

        @Override
        public Vec2I64 signum(Vec2I64 vector) {
            return of(
                    (long) Long.signum(vector.x()),
                    (long) Long.signum(vector.y())
            );
        }

        @Override
        @AliasExclude
        public LongNumeric scalarOps() {
            return LongNumeric.OPS;
        }

        @Override
        @AliasExclude
        public Vec2I64 zero() {
            return Vector2Ops.super.zero();
        }

        @Override
        @AliasExclude
        public Vec2I64 one() {
            return Vector2Ops.super.one();
        }
    }
}
