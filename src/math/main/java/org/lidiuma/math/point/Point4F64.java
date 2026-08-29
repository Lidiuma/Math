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
import org.lidiuma.math.api.traits.point.FloatingPoint4Ops;
import org.lidiuma.math.api.tuple.UnaryTuple4;
import org.lidiuma.math.processor.AliasExclude;
import org.lidiuma.math.processor.FactoryAlias;
import org.lidiuma.math.processor.FieldAlias;
import org.lidiuma.math.vector.Vec4F64;
import java.util.function.UnaryOperator;
import static org.lidiuma.math.internal.AnnotationConst.POINT4_FACTORY;
import static org.lidiuma.math.internal.AnnotationConst.POINT_OUT;

@FactoryAlias(methodName = POINT4_FACTORY, outputClass = POINT_OUT)
public value record Point4F64(
        @Override Double x,
        @Override Double y,
        @Override Double z,
        @Override Double w
) implements Point4<Double> {

    @FieldAlias(outputClass = POINT_OUT)
    public static final Ops OPS = new Ops();

    /// A constructor creating a specialized point from a generic tuple.
    @AliasExclude // This method can be a performance sink if used inappropriately, so I exclude it from the alias.
    public Point4F64(UnaryTuple4<Double> tuple) {
        this(tuple.x(), tuple.y(), tuple.z(), tuple.w());
    }

    public static final value class Ops implements FloatingPoint4Ops<Point4F64, Vec4F64, Double> {

        private Ops() {}

        @Override
        @AliasExclude
        public Point4F64 of(Double x, Double y, Double z, Double w) {
            return new Point4F64(x, y, z, w);
        }

        @Override
        @AliasExclude
        public Vec4F64.Ops vectorOps() {
            return Vec4F64.OPS;
        }

        /*
        Handwritten to remove GC collections when used polymorphically (different generic parameters).
        Speed is more or less the same, but without a rare case of the JIT failing giving x10 less performance.
        This unfortunately creates code duplication, and I'm sure some methods are fine as-is,
        but writing them anyway is faster than making sure with benchmarking.
        */

        @Override
        public Double distance(Point4F64 first, Point4F64 second) {
            return vectorOps().distance(vec(first), vec(second));
        }

        @Override
        public Point4F64 interpolate(Point4F64 start, Point4F64 end, Double alpha, UnaryOperator<Double> easing) {
            return point(vectorOps().interpolate(vec(start), vec(end), alpha, easing));
        }

        @Override
        public Point4F64 lerp(Point4F64 start, Point4F64 end, Double alpha) {
            return interpolate(start, end, alpha, UnaryOperator.identity());
        }

        @Override
        public Point4F64 add(Point4F64 point, Vec4F64 vector) {
            return point(vectorOps().add(vec(point), vector));
        }

        @Override
        public Vec4F64 subtract(Point4F64 minuend, Point4F64 subtrahend) {
            return vectorOps().subtract(vec(minuend), vec(subtrahend));
        }

        @Override
        public Double distanceSquared(Point4F64 first, Point4F64 second) {
            return vectorOps().distanceSquared(vec(first), vec(second));
        }

        @Override
        public Point4F64 clamp(Point4F64 point, Double min, Double max) {
            return point(vectorOps().clamp(vec(point), min, max));
        }

        private Vec4F64 vec(Point4F64 point) {
            return new Vec4F64(point.x(), point.y(), point.z(), point.w());
        }

        private Point4F64 point(Vec4F64 vec) {
            return new Point4F64(vec.x(), vec.y(), vec.z(), vec.w());
        }
    }
}
