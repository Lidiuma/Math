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
import org.lidiuma.math.vector.Vec2I64;
import static org.lidiuma.math.internal.AnnotationConst.POINT2_FACTORY;
import static org.lidiuma.math.internal.AnnotationConst.POINT_OUT;

@LooselyConsistentValue
@FactoryAlias(methodName = POINT2_FACTORY, outputClass = POINT_OUT)
public value record Point2I64(
        @NullRestricted Long x,
        @NullRestricted Long y
) implements Point2<Long> {

    @FieldAlias(outputClass = POINT_OUT)
    public static final Ops OPS = new Ops();

    /// A constructor creating a specialized point from a generic tuple.
    @AliasExclude // This method can be a performance sink if used inappropriately, so I exclude it from the alias.
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
                        
        /*
        Handwritten to remove GC collections when used polymorphically (different generic parameters).
        Speed is more or less the same, but without a rare case of the JIT failing giving x10 less performance.
        This unfortunately creates code duplication, and I'm sure some methods are fine as-is,
        but writing them anyway is faster than making sure with benchmarking.
        */

        @Override
        public Point2I64 add(Point2I64 point, Vec2I64 vector) {
            return point(vectorOps().add(vec(point), vector));
        }

        @Override
        public Vec2I64 subtract(Point2I64 minuend, Point2I64 subtrahend) {
            return vectorOps().subtract(vec(minuend), vec(subtrahend));
        }

        @Override
        public Long distanceSquared(Point2I64 first, Point2I64 second) {
            return vectorOps().distanceSquared(vec(first), vec(second));
        }

        @Override
        public Point2I64 clamp(Point2I64 point, Long min, Long max) {
            return point(vectorOps().clamp(vec(point), min, max));
        }

        private Vec2I64 vec(Point2I64 point) {
            return new Vec2I64(point.x(), point.y());
        }

        private Point2I64 point(Vec2I64 vec) {
            return new Point2I64(vec.x(), vec.y());
        }
    }
}
