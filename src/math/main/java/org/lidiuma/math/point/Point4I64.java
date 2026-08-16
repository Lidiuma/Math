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
import org.lidiuma.math.api.point.Point4;
import org.lidiuma.math.api.traits.point.Point4Ops;
import org.lidiuma.math.api.tuple.UnaryTuple4;
import org.lidiuma.math.processor.AliasExclude;
import org.lidiuma.math.processor.FactoryAlias;
import org.lidiuma.math.processor.FieldAlias;
import org.lidiuma.math.processor.NamedAlias;
import org.lidiuma.math.vector.Vec4I64;
import static org.lidiuma.math.internal.AnnotationConst.*;

@LooselyConsistentValue
@FactoryAlias(methodName = POINT4_FACTORY, outputClass = POINT_OUT)
public value record Point4I64(
        @NullRestricted Long x,
        @NullRestricted Long y,
        @NullRestricted Long z,
        @NullRestricted Long w
) implements Point4<Long> {

    @FieldAlias(outputClass = POINT_OUT)
    public static final Ops OPS = new Ops();

    @NamedAlias(methodName = POINT4_FACTORY + I64)
    public Point4I64(UnaryTuple4<Long> tuple) {
        this(tuple.x(), tuple.y(), tuple.z(), tuple.w());
    }

    public static final class Ops implements Point4Ops<Point4I64, Vec4I64, Long> {

        @Override
        @AliasExclude
        public Point4I64 of(Long x, Long y, Long z, Long w) {
            return new Point4I64(x, y, z, w);
        }

        @Override
        @AliasExclude
        public Vec4I64.Ops vectorOps() {
            return Vec4I64.OPS;
        }
    }
}
