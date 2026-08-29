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
import org.lidiuma.math.vector.Vec4F32;
import java.util.function.UnaryOperator;
import static org.lidiuma.math.internal.AnnotationConst.POINT4_FACTORY;
import static org.lidiuma.math.internal.AnnotationConst.POINT_OUT;

@FactoryAlias(methodName = POINT4_FACTORY, outputClass = POINT_OUT)
public value record Point4F32(
        @Override Float x,
        @Override Float y,
        @Override Float z,
        @Override Float w
) implements Point4<Float> {

    @FieldAlias(outputClass = POINT_OUT)
    public static final Ops OPS = new Ops();

    /// A constructor creating a specialized point from a generic tuple.
    @AliasExclude // This method can be a performance sink if used inappropriately, so I exclude it from the alias.
    public Point4F32(UnaryTuple4<Float> tuple) {
        this(tuple.x(), tuple.y(), tuple.z(), tuple.w());
    }

    public static final value class Ops implements FloatingPoint4Ops<Point4F32, Vec4F32, Float> {

        private Ops() {}

        @Override
        @AliasExclude
        public Point4F32 of(Float x, Float y, Float z, Float w) {
            return new Point4F32(x, y, z, w);
        }

        @Override
        @AliasExclude
        public Vec4F32.Ops vectorOps() {
            return Vec4F32.OPS;
        }
                        
        /*
        Handwritten to remove GC collections when used polymorphically (different generic parameters).
        Speed is more or less the same, but without a rare case of the JIT failing giving x10 less performance.
        This unfortunately creates code duplication, and I'm sure some methods are fine as-is,
        but writing them anyway is faster than making sure with benchmarking.
        */

        @Override
        public Float distance(Point4F32 first, Point4F32 second) {
            return vectorOps().distance(vec(first), vec(second));
        }

        @Override
        public Point4F32 interpolate(Point4F32 start, Point4F32 end, Float alpha, UnaryOperator<Float> easing) {
            return point(vectorOps().interpolate(vec(start), vec(end), alpha, easing));
        }

        @Override
        public Point4F32 lerp(Point4F32 start, Point4F32 end, Float alpha) {
            return interpolate(start, end, alpha, UnaryOperator.identity());
        }

        @Override
        public Point4F32 add(Point4F32 point, Vec4F32 vector) {
            return point(vectorOps().add(vec(point), vector));
        }

        @Override
        public Vec4F32 subtract(Point4F32 minuend, Point4F32 subtrahend) {
            return vectorOps().subtract(vec(minuend), vec(subtrahend));
        }

        @Override
        public Float distanceSquared(Point4F32 first, Point4F32 second) {
            return vectorOps().distanceSquared(vec(first), vec(second));
        }

        @Override
        public Point4F32 clamp(Point4F32 point, Float min, Float max) {
            return point(vectorOps().clamp(vec(point), min, max));
        }

        private Vec4F32 vec(Point4F32 point) {
            return new Vec4F32(point.x(), point.y(), point.z(), point.w());
        }

        private Point4F32 point(Vec4F32 vec) {
            return new Point4F32(vec.x(), vec.y(), vec.z(), vec.w());
        }
    }
}
