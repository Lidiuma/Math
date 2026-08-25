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

    public static final value class Ops implements Point4Ops<Point4I64, Vec4I64, Long> {

        private Ops() {}

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
                                        
        /*
        Handwritten to remove GC collections when used polymorphically (different generic parameters).
        Speed is more or less the same, but without a rare case of the JIT failing giving x10 less performance.
        This unfortunately creates code duplication, and I'm sure some methods are fine as-is,
        but writing them anyway is faster than making sure with benchmarking.
        */

        @Override
        public Point4I64 add(Point4I64 point, Vec4I64 vector) {
            return point(vectorOps().add(vec(point), vector));
        }

        @Override
        public Vec4I64 subtract(Point4I64 minuend, Point4I64 subtrahend) {
            return vectorOps().subtract(vec(minuend), vec(subtrahend));
        }

        @Override
        public Long distanceSquared(Point4I64 first, Point4I64 second) {
            return vectorOps().distanceSquared(vec(first), vec(second));
        }

        @Override
        public Point4I64 clamp(Point4I64 point, Long min, Long max) {
            return point(vectorOps().clamp(vec(point), min, max));
        }

        private Vec4I64 vec(Point4I64 point) {
            return new Vec4I64(point.x(), point.y(), point.z(), point.w());
        }

        private Point4I64 point(Vec4I64 vec) {
            return new Point4I64(vec.x(), vec.y(), vec.z(), vec.w());
        }
    }
}
