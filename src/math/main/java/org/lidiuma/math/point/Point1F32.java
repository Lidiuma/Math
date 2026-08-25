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
import org.lidiuma.math.api.traits.point.FloatingPoint1Ops;
import org.lidiuma.math.api.tuple.UnaryTuple1;
import org.lidiuma.math.processor.AliasExclude;
import org.lidiuma.math.processor.FactoryAlias;
import org.lidiuma.math.processor.FieldAlias;
import org.lidiuma.math.processor.NamedAlias;
import org.lidiuma.math.vector.Vec1F32;
import java.util.function.UnaryOperator;
import static org.lidiuma.math.internal.AnnotationConst.*;

@LooselyConsistentValue
@FactoryAlias(methodName = POINT1_FACTORY, outputClass = POINT_OUT)
public value record Point1F32(@NullRestricted Float x) implements Point1<Float> {

    @FieldAlias(outputClass = POINT_OUT)
    public static final Ops OPS = new Ops();

    @NamedAlias(methodName = POINT1_FACTORY + F32)
    public Point1F32(UnaryTuple1<Float> tuple) {
        this(tuple.x());
    }

    public static final value class Ops implements FloatingPoint1Ops<Point1F32, Vec1F32, Float> {

        private Ops() {}

        @Override
        @AliasExclude
        public Point1F32 of(Float x) {
            return new Point1F32(x);
        }

        @Override
        @AliasExclude
        public Vec1F32.Ops vectorOps() {
            return Vec1F32.OPS;
        }

        /*
        Handwritten to remove GC collections when used polymorphically (different generic parameters).
        Speed is more or less the same, but without a rare case of the JIT failing giving x10 less performance.
        This unfortunately creates code duplication, and I'm sure some methods are fine as-is,
        but writing them anyway is faster than making sure with benchmarking.
        */

        @Override
        public Float distance(Point1F32 first, Point1F32 second) {
            return vectorOps().distance(vec(first), vec(second));
        }

        @Override
        public Point1F32 interpolate(Point1F32 start, Point1F32 end, Float alpha, UnaryOperator<Float> easing) {
            return point(vectorOps().interpolate(vec(start), vec(end), alpha, easing));
        }

        @Override
        public Point1F32 lerp(Point1F32 start, Point1F32 end, Float alpha) {
            return interpolate(start, end, alpha, UnaryOperator.identity());
        }

        @Override
        public Point1F32 add(Point1F32 point, Vec1F32 vector) {
            return point(vectorOps().add(vec(point), vector));
        }

        @Override
        public Vec1F32 subtract(Point1F32 minuend, Point1F32 subtrahend) {
            return vectorOps().subtract(vec(minuend), vec(subtrahend));
        }

        @Override
        public Float distanceSquared(Point1F32 first, Point1F32 second) {
            return vectorOps().distanceSquared(vec(first), vec(second));
        }

        @Override
        public Point1F32 clamp(Point1F32 point, Float min, Float max) {
            return point(vectorOps().clamp(vec(point), min, max));
        }

        private Vec1F32 vec(Point1F32 point) {
            return new Vec1F32(point.x());
        }

        private Point1F32 point(Vec1F32 vec) {
            return new Point1F32(vec.x());
        }
    }
}
