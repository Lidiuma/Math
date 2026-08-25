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
import org.lidiuma.math.vector.Vec3F64;
import java.util.function.UnaryOperator;
import static org.lidiuma.math.internal.AnnotationConst.POINT3_FACTORY;
import static org.lidiuma.math.internal.AnnotationConst.POINT_OUT;

@LooselyConsistentValue
@FactoryAlias(methodName = POINT3_FACTORY, outputClass = POINT_OUT)
public value record Point3F64(
        @NullRestricted Double x,
        @NullRestricted Double y,
        @NullRestricted Double z
) implements Point3<Double> {

    @FieldAlias(outputClass = POINT_OUT)
    public static final Ops OPS = new Ops();

    /// A constructor creating a specialized point from a generic tuple.
    @AliasExclude // This method can be a performance sink if used inappropriately, so I exclude it from the alias.
    public Point3F64(UnaryTuple3<Double> tuple) {
        this(tuple.x(), tuple.y(), tuple.z());
    }

    public static final value class Ops implements FloatingPoint3Ops<Point3F64, Vec3F64, Double> {

        private Ops() {}

        @Override
        @AliasExclude
        public Point3F64 of(Double x, Double y, Double z) {
            return new Point3F64(x, y, z);
        }

        @Override
        @AliasExclude
        public Vec3F64.Ops vectorOps() {
            return Vec3F64.OPS;
        }
                        
        /*
        Handwritten to remove GC collections when used polymorphically (different generic parameters).
        Speed is more or less the same, but without a rare case of the JIT failing giving x10 less performance.
        This unfortunately creates code duplication, and I'm sure some methods are fine as-is,
        but writing them anyway is faster than making sure with benchmarking.
        */

        @Override
        public Double distance(Point3F64 first, Point3F64 second) {
            return vectorOps().distance(vec(first), vec(second));
        }

        @Override
        public Point3F64 interpolate(Point3F64 start, Point3F64 end, Double alpha, UnaryOperator<Double> easing) {
            return point(vectorOps().interpolate(vec(start), vec(end), alpha, easing));
        }

        @Override
        public Point3F64 lerp(Point3F64 start, Point3F64 end, Double alpha) {
            return interpolate(start, end, alpha, UnaryOperator.identity());
        }

        @Override
        public Point3F64 add(Point3F64 point, Vec3F64 vector) {
            return point(vectorOps().add(vec(point), vector));
        }

        @Override
        public Vec3F64 subtract(Point3F64 minuend, Point3F64 subtrahend) {
            return vectorOps().subtract(vec(minuend), vec(subtrahend));
        }

        @Override
        public Double distanceSquared(Point3F64 first, Point3F64 second) {
            return vectorOps().distanceSquared(vec(first), vec(second));
        }

        @Override
        public Point3F64 clamp(Point3F64 point, Double min, Double max) {
            return point(vectorOps().clamp(vec(point), min, max));
        }

        private Vec3F64 vec(Point3F64 point) {
            return new Vec3F64(point.x(), point.y(), point.z());
        }

        private Point3F64 point(Vec3F64 vec) {
            return new Point3F64(vec.x(), vec.y(), vec.z());
        }
    }
}
