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
import org.lidiuma.math.api.point.Point1;
import org.lidiuma.math.api.traits.point.FloatingPointOps;
import org.lidiuma.math.processor.Alias;
import org.lidiuma.math.processor.FactoryAlias;
import org.lidiuma.math.vector.Vec1F32;
import java.util.function.UnaryOperator;

@LooselyConsistentValue
@FactoryAlias(methodName = "point1", outputClass = "Points")
public value record Point1F32(@NullRestricted Float x) implements Point1<Float> {

    @Alias(outputClass = "Points")
    public static final Ops WITNESS = new Ops();

    // To avoid re-defining the same calculation twice,
    // I re-use the Vector math but with the constraint of the vector used starting from the origin.
    public static final class Ops implements FloatingPointOps<Point1F32, Vec1F32, Float> {

        private static Vec1F32 v(Point1F32 point) {
            return new Vec1F32(point.x());
        }

        private static Point1F32 p(Vec1F32 vec) {
            return new Point1F32(vec.x());
        }

        private static Vec1F32.Ops vw() {
            return Vec1F32.WITNESS;
        }

        @Override
        public Float distance(Point1F32 first, Point1F32 second) {
            return vw().distance(v(first), v(second));
        }

        @Override
        public Point1F32 add(Point1F32 point, Vec1F32 vector) {
            return p(vw().add(v(point), vector));
        }

        @Override
        public Vec1F32 subtract(Point1F32 minuend, Point1F32 subtrahend) {
            return vw().subtract(v(minuend), v(subtrahend));
        }

        @Override
        public Float distanceSquared(Point1F32 first, Point1F32 second) {
            return vw().distanceSquared(v(first), v(second));
        }

        @Override
        public Point1F32 clamp(Point1F32 point, Float min, Float max) {
            return p(vw().clamp(v(point), min, max));
        }

        @Override
        public Point1F32 interpolate(Point1F32 start, Point1F32 end, Float alpha, UnaryOperator<Float> easing) {
            return p(vw().interpolate(v(start), v(end), alpha, easing));
        }
    }
}
