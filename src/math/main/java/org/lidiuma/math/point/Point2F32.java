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

import org.lidiuma.math.api.point.Point2;
import org.lidiuma.math.api.traits.point.FloatingPoint2Ops;
import org.lidiuma.math.api.tuple.UnaryTuple2;
import org.lidiuma.math.processor.AliasExclude;
import org.lidiuma.math.processor.FactoryAlias;
import org.lidiuma.math.processor.FieldAlias;
import org.lidiuma.math.vector.Vec2F32;
import java.util.function.UnaryOperator;
import static org.lidiuma.math.internal.AnnotationConst.POINT2_FACTORY;
import static org.lidiuma.math.internal.AnnotationConst.POINT_OUT;

@FactoryAlias(methodName = POINT2_FACTORY, outputClass = POINT_OUT)
public value record Point2F32(
        @Override Float x,
        @Override Float y
) implements Point2<Float> {

    @FieldAlias(outputClass = POINT_OUT)
    public static final Ops OPS = new Ops();

    /// A constructor creating a specialized point from a generic tuple.
    @AliasExclude // This method can be a performance sink if used inappropriately, so I exclude it from the alias.
    public Point2F32(UnaryTuple2<Float> tuple) {
        this(tuple.x(), tuple.y());
    }

    public static final value class Ops implements FloatingPoint2Ops<Point2F32, Vec2F32, Float> {

        private Ops() {}

        @Override
        @AliasExclude
        public Point2F32 of(Float x, Float y) {
            return new Point2F32(x, y);
        }

        @Override
        @AliasExclude
        public Vec2F32.Ops vectorOps() {
            return Vec2F32.OPS;
        }
        
        /*
        Handwritten to remove GC collections when used polymorphically (different generic parameters).
        Speed is more or less the same, but without a rare case of the JIT failing giving x10 less performance.
        This unfortunately creates code duplication, and I'm sure some methods are fine as-is,
        but writing them anyway is faster than making sure with benchmarking.
        */

        @Override
        public Float distance(Point2F32 first, Point2F32 second) {
            return vectorOps().distance(vec(first), vec(second));
        }

        @Override
        public Point2F32 interpolate(Point2F32 start, Point2F32 end, Float alpha, UnaryOperator<Float> easing) {
            return point(vectorOps().interpolate(vec(start), vec(end), alpha, easing));
        }

        @Override
        public Point2F32 lerp(Point2F32 start, Point2F32 end, Float alpha) {
            return interpolate(start, end, alpha, UnaryOperator.identity());
        }

        @Override
        public Point2F32 add(Point2F32 point, Vec2F32 vector) {
            return point(vectorOps().add(vec(point), vector));
        }

        @Override
        public Vec2F32 subtract(Point2F32 minuend, Point2F32 subtrahend) {
            return vectorOps().subtract(vec(minuend), vec(subtrahend));
        }

        @Override
        public Float distanceSquared(Point2F32 first, Point2F32 second) {
            return vectorOps().distanceSquared(vec(first), vec(second));
        }

        @Override
        public Point2F32 clamp(Point2F32 point, Float min, Float max) {
            return point(vectorOps().clamp(vec(point), min, max));
        }

        private Vec2F32 vec(Point2F32 point) {
            return new Vec2F32(point.x(), point.y());
        }

        private Point2F32 point(Vec2F32 vec) {
            return new Point2F32(vec.x(), vec.y());
        }
    }
}
