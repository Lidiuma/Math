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

import org.lidiuma.math.api.point.Point4;
import org.lidiuma.math.api.traits.point.Point4Ops;
import org.lidiuma.math.api.tuple.UnaryTuple4;
import org.lidiuma.math.processor.AliasExclude;
import org.lidiuma.math.processor.FactoryAlias;
import org.lidiuma.math.processor.FieldAlias;
import org.lidiuma.math.vector.Vec4I32;
import static org.lidiuma.math.internal.AnnotationConst.POINT4_FACTORY;
import static org.lidiuma.math.internal.AnnotationConst.POINT_OUT;

@FactoryAlias(methodName = POINT4_FACTORY, outputClass = POINT_OUT)
public record Point4I32(
        @Override Integer x,
        @Override Integer y,
        @Override Integer z,
        @Override Integer w
) implements Point4<Integer> {

    @FieldAlias(outputClass = POINT_OUT)
    public static final Ops OPS = new Ops();

    /// A constructor creating a specialized point from a generic tuple.
    @AliasExclude // This method can be a performance sink if used inappropriately, so I exclude it from the alias.
    public Point4I32(UnaryTuple4<Integer> tuple) {
        this(tuple.x(), tuple.y(), tuple.z(), tuple.w());
    }

    public static final class Ops implements Point4Ops<Point4I32, Vec4I32, Integer> {

        private Ops() {}

        @Override
        @AliasExclude
        public Point4I32 of(Integer x, Integer y, Integer z, Integer w) {
            return new Point4I32(x, y, z, w);
        }

        @Override
        @AliasExclude
        public Vec4I32.Ops vectorOps() {
            return Vec4I32.OPS;
        }
                                
        /*
        Handwritten to remove GC collections when used polymorphically (different generic parameters).
        Speed is more or less the same, but without a rare case of the JIT failing giving x10 less performance.
        This unfortunately creates code duplication, and I'm sure some methods are fine as-is,
        but writing them anyway is faster than making sure with benchmarking.
        */

        @Override
        public Point4I32 add(Point4I32 point, Vec4I32 vector) {
            return point(vectorOps().add(vec(point), vector));
        }

        @Override
        public Vec4I32 subtract(Point4I32 minuend, Point4I32 subtrahend) {
            return vectorOps().subtract(vec(minuend), vec(subtrahend));
        }

        @Override
        public Integer distanceSquared(Point4I32 first, Point4I32 second) {
            return vectorOps().distanceSquared(vec(first), vec(second));
        }

        @Override
        public Point4I32 clamp(Point4I32 point, Integer min, Integer max) {
            return point(vectorOps().clamp(vec(point), min, max));
        }

        private Vec4I32 vec(Point4I32 point) {
            return new Vec4I32(point.x(), point.y(), point.z(), point.w());
        }

        private Point4I32 point(Vec4I32 vec) {
            return new Point4I32(vec.x(), vec.y(), vec.z(), vec.w());
        }
    }
}
