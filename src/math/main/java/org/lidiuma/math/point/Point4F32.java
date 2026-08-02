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
import org.lidiuma.math.vector.Vec4F32;
import java.util.function.UnaryOperator;

@LooselyConsistentValue
@FactoryAlias(methodName = "point4", outputClass = "Points")
public value record Point4F32(
        @NullRestricted Float x,
        @NullRestricted Float y,
        @NullRestricted Float z,
        @NullRestricted Float w
) implements Point4<Float> {

    @Alias(outputClass = "Points")
    public static final Ops WITNESS = new Ops();

    // To avoid re-defining the same calculation twice,
    // I re-use the Vector math but with the constraint of the vector used starting from the origin.
    public static final class Ops implements FloatingPointOps<Point4F32, Vec4F32, Float> {

        private static Vec4F32 v(Point4F32 point) {
            return new Vec4F32(point.x(), point.y(), point.z(), point.w());
        }

        private static Point4F32 p(Vec4F32 vec) {
            return new Point4F32(vec.x(), vec.y(), vec.z(), vec.w());
        }

        private static Vec4F32.Ops vw() {
            return Vec4F32.WITNESS;
        }

        @Override
        public Float distance(Point4F32 first, Point4F32 second) {
            return vw().distance(v(first), v(second));
        }

        @Override
        public Point4F32 add(Point4F32 point, Vec4F32 vector) {
            return p(vw().add(v(point), vector));
        }

        @Override
        public Vec4F32 subtract(Point4F32 minuend, Point4F32 subtrahend) {
            return vw().subtract(v(minuend), v(subtrahend));
        }

        @Override
        public Float distanceSquared(Point4F32 first, Point4F32 second) {
            return vw().distanceSquared(v(first), v(second));
        }

        @Override
        public Point4F32 clamp(Point4F32 point, Float min, Float max) {
            return p(vw().clamp(v(point), min, max));
        }

        @Override
        public Point4F32 interpolate(Point4F32 start, Point4F32 end, Float alpha, UnaryOperator<Float> easing) {
            return p(vw().interpolate(v(start), v(end), alpha, easing));
        }
    }
}
