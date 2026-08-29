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

import org.lidiuma.math.api.point.Point1;
import org.lidiuma.math.api.traits.point.Point1Ops;
import org.lidiuma.math.api.tuple.UnaryTuple1;
import org.lidiuma.math.processor.AliasExclude;
import org.lidiuma.math.processor.FactoryAlias;
import org.lidiuma.math.processor.FieldAlias;
import org.lidiuma.math.vector.Vec1I64;
import static org.lidiuma.math.internal.AnnotationConst.POINT1_FACTORY;
import static org.lidiuma.math.internal.AnnotationConst.POINT_OUT;

@FactoryAlias(methodName = POINT1_FACTORY, outputClass = POINT_OUT)
public record Point1I64(@Override Long x) implements Point1<Long> {

    @FieldAlias(outputClass = POINT_OUT)
    public static final Ops OPS = new Ops();

    /// A constructor creating a specialized point from a generic tuple.
    @AliasExclude // This method can be a performance sink if used inappropriately, so I exclude it from the alias.
    public Point1I64(UnaryTuple1<Long> tuple) {
        this(tuple.x());
    }

    public static final class Ops implements Point1Ops<Point1I64, Vec1I64, Long> {

        private Ops() {}

        @Override
        @AliasExclude
        public Point1I64 of(Long x) {
            return new Point1I64(x);
        }

        @Override
        @AliasExclude
        public Vec1I64.Ops vectorOps() {
            return Vec1I64.OPS;
        }
                
        /*
        Handwritten to remove GC collections when used polymorphically (different generic parameters).
        Speed is more or less the same, but without a rare case of the JIT failing giving x10 less performance.
        This unfortunately creates code duplication, and I'm sure some methods are fine as-is,
        but writing them anyway is faster than making sure with benchmarking.
        */

        @Override
        public Long distance(Point1I64 first, Point1I64 second) {
            return vectorOps().distance(vec(first), vec(second));
        }

        @Override
        public Point1I64 add(Point1I64 point, Vec1I64 vector) {
            return point(vectorOps().add(vec(point), vector));
        }

        @Override
        public Vec1I64 subtract(Point1I64 minuend, Point1I64 subtrahend) {
            return vectorOps().subtract(vec(minuend), vec(subtrahend));
        }

        @Override
        public Long distanceSquared(Point1I64 first, Point1I64 second) {
            return vectorOps().distanceSquared(vec(first), vec(second));
        }

        @Override
        public Point1I64 clamp(Point1I64 point, Long min, Long max) {
            return point(vectorOps().clamp(vec(point), min, max));
        }

        private Vec1I64 vec(Point1I64 point) {
            return new Vec1I64(point.x());
        }

        private Point1I64 point(Vec1I64 vec) {
            return new Point1I64(vec.x());
        }
    }
}
