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
import org.lidiuma.math.api.traits.point.FloatingPointOps;
import org.lidiuma.math.processor.Alias;
import org.lidiuma.math.processor.FactoryAlias;
import org.lidiuma.math.vector.Vec2F64;
import java.util.function.UnaryOperator;

@LooselyConsistentValue
@FactoryAlias(methodName = "point2", outputClass = "Points")
public value record Point2F64(
        @NullRestricted Double x,
        @NullRestricted Double y
) implements Point2<Double> {

    @Alias(outputClass = "Points")
    public static final Ops WITNESS = new Ops();

    // To avoid re-defining the same calculation twice,
    // I re-use the Vector math but with the constraint of the vector used starting from the origin.
    public static final class Ops implements FloatingPointOps<Point2F64, Vec2F64, Double> {

        private static Vec2F64 v(Point2F64 point) {
            return new Vec2F64(point.x(), point.y());
        }

        private static Point2F64 p(Vec2F64 vec) {
            return new Point2F64(vec.x(), vec.y());
        }

        private static Vec2F64.Ops vw() {
            return Vec2F64.WITNESS;
        }

        @Override
        public Double distance(Point2F64 first, Point2F64 second) {
            return vw().distance(v(first), v(second));
        }

        @Override
        public Point2F64 add(Point2F64 point, Vec2F64 vector) {
            return p(vw().add(v(point), vector));
        }

        @Override
        public Vec2F64 subtract(Point2F64 minuend, Point2F64 subtrahend) {
            return vw().subtract(v(minuend), v(subtrahend));
        }

        @Override
        public Double distanceSquared(Point2F64 first, Point2F64 second) {
            return vw().distanceSquared(v(first), v(second));
        }

        @Override
        public Point2F64 clamp(Point2F64 point, Double min, Double max) {
            return p(vw().clamp(v(point), min, max));
        }

        @Override
        public Point2F64 interpolate(Point2F64 start, Point2F64 end, Double alpha, UnaryOperator<Double> easing) {
            return p(vw().interpolate(v(start), v(end), alpha, easing));
        }
    }
}
