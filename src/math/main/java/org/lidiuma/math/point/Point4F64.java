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
import org.lidiuma.math.api.traits.point.FloatingPointOps;
import org.lidiuma.math.processor.Alias;
import org.lidiuma.math.processor.FactoryAlias;
import org.lidiuma.math.vector.Vec4F64;
import java.util.function.UnaryOperator;

@LooselyConsistentValue
@FactoryAlias(methodName = "point4", outputClass = "Points")
public value record Point4F64(
        @NullRestricted Double x,
        @NullRestricted Double y,
        @NullRestricted Double z,
        @NullRestricted Double w
) implements Point4<Double> {

    @Alias(outputClass = "Points")
    public static final Ops WITNESS = new Ops();

    // To avoid re-defining the same calculation twice,
    // I re-use the Vector math but with the constraint of the vector used starting from the origin.
    public static final class Ops implements FloatingPointOps<Point4F64, Vec4F64, Double> {

        private static Vec4F64 v(Point4F64 point) {
            return new Vec4F64(point.x(), point.y(), point.z(), point.w());
        }

        private static Point4F64 p(Vec4F64 vec) {
            return new Point4F64(vec.x(), vec.y(), vec.z(), vec.w());
        }

        private static Vec4F64.Ops vw() {
            return Vec4F64.WITNESS;
        }

        @Override
        public Double distance(Point4F64 first, Point4F64 second) {
            return vw().distance(v(first), v(second));
        }

        @Override
        public Point4F64 add(Point4F64 point, Vec4F64 vector) {
            return p(vw().add(v(point), vector));
        }

        @Override
        public Vec4F64 subtract(Point4F64 minuend, Point4F64 subtrahend) {
            return vw().subtract(v(minuend), v(subtrahend));
        }

        @Override
        public Double distanceSquared(Point4F64 first, Point4F64 second) {
            return vw().distanceSquared(v(first), v(second));
        }

        @Override
        public Point4F64 clamp(Point4F64 point, Double min, Double max) {
            return p(vw().clamp(v(point), min, max));
        }

        @Override
        public Point4F64 interpolate(Point4F64 start, Point4F64 end, Double alpha, UnaryOperator<Double> easing) {
            return p(vw().interpolate(v(start), v(end), alpha, easing));
        }
    }
}
