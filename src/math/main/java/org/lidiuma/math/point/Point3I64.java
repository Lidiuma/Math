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
import org.lidiuma.math.api.point.Point3;
import org.lidiuma.math.api.traits.point.Point3Ops;
import org.lidiuma.math.api.tuple.UnaryTuple3;
import org.lidiuma.math.processor.Alias;
import org.lidiuma.math.processor.AliasExclude;
import org.lidiuma.math.processor.FactoryAlias;
import org.lidiuma.math.processor.NamedAlias;
import org.lidiuma.math.vector.Vec3I64;
import static org.lidiuma.math.internal.AnnotationConst.*;

@LooselyConsistentValue
@FactoryAlias(methodName = POINT3_FACTORY, outputClass = POINT_OUT)
public value record Point3I64(
        @NullRestricted Long x,
        @NullRestricted Long y,
        @NullRestricted Long z
) implements Point3<Long> {

    @Alias(outputClass = POINT_OUT)
    public static final Ops OPS = new Ops();

    @NamedAlias(methodName = POINT3_FACTORY + I64)
    public Point3I64(UnaryTuple3<Long> tuple) {
        this(tuple.x(), tuple.y(), tuple.z());
    }

    public static final class Ops implements Point3Ops<Point3I64, Vec3I64, Long> {

        @Override
        @AliasExclude
        public Point3I64 of(Long x, Long y, Long z) {
            return new Point3I64(x, y, z);
        }

        @Override
        @AliasExclude
        public Vec3I64.Ops vectorOps() {
            return Vec3I64.OPS;
        }
    }
}
