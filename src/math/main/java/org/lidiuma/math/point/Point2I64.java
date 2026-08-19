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

package org.lidiuma.math.point;

import jdk.internal.vm.annotation.LooselyConsistentValue;
import jdk.internal.vm.annotation.NullRestricted;
import org.lidiuma.math.api.point.Point2;
import org.lidiuma.math.api.traits.point.Point2Ops;
import org.lidiuma.math.api.tuple.UnaryTuple2;
import org.lidiuma.math.processor.AliasExclude;
import org.lidiuma.math.processor.FactoryAlias;
import org.lidiuma.math.processor.FieldAlias;
import org.lidiuma.math.processor.NamedAlias;
import org.lidiuma.math.vector.Vec2I64;
import static org.lidiuma.math.internal.AnnotationConst.*;

@LooselyConsistentValue
@FactoryAlias(methodName = POINT2_FACTORY, outputClass = POINT_OUT)
public value record Point2I64(
        @NullRestricted Long x,
        @NullRestricted Long y
) implements Point2<Long> {

    @FieldAlias(outputClass = POINT_OUT)
    public static final Ops OPS = new Ops();

    @NamedAlias(methodName = POINT2_FACTORY + I64)
    public Point2I64(UnaryTuple2<Long> tuple) {
        this(tuple.x(), tuple.y());
    }

    public static final value class Ops implements Point2Ops<Point2I64, Vec2I64, Long> {

        private Ops() {}

        @Override
        @AliasExclude
        public Point2I64 of(Long x, Long y) {
            return new Point2I64(x, y);
        }

        @Override
        @AliasExclude
        public Vec2I64.Ops vectorOps() {
            return Vec2I64.OPS;
        }
    }
}
