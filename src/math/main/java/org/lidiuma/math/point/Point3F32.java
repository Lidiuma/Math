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
import org.lidiuma.math.api.traits.point.FloatingPoint3Ops;
import org.lidiuma.math.api.tuple.UnaryTuple3;
import org.lidiuma.math.processor.AliasExclude;
import org.lidiuma.math.processor.FactoryAlias;
import org.lidiuma.math.processor.FieldAlias;
import org.lidiuma.math.processor.NamedAlias;
import org.lidiuma.math.vector.Vec3F32;
import static org.lidiuma.math.internal.AnnotationConst.*;

@LooselyConsistentValue
@FactoryAlias(methodName = POINT3_FACTORY, outputClass = POINT_OUT)
public value record Point3F32(
        @NullRestricted Float x,
        @NullRestricted Float y,
        @NullRestricted Float z
) implements Point3<Float> {

    @FieldAlias(outputClass = POINT_OUT)
    public static final Ops OPS = new Ops();

    @NamedAlias(methodName = POINT3_FACTORY + F32)
    public Point3F32(UnaryTuple3<Float> tuple) {
        this(tuple.x(), tuple.y(), tuple.z());
    }

    public static final class Ops implements FloatingPoint3Ops<Point3F32, Vec3F32, Float> {

        @Override
        @AliasExclude
        public Point3F32 of(Float x, Float y, Float z) {
            return new Point3F32(x, y, z);
        }

        @Override
        @AliasExclude
        public Vec3F32.Ops vectorOps() {
            return Vec3F32.OPS;
        }
    }
}
