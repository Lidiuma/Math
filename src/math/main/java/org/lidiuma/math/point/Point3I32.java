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
import org.lidiuma.math.api.traits.point.Point3Ops;
import org.lidiuma.math.api.tuple.UnaryTuple3;
import org.lidiuma.math.processor.AliasExclude;
import org.lidiuma.math.processor.FactoryAlias;
import org.lidiuma.math.processor.FieldAlias;
import org.lidiuma.math.vector.Vec3I32;
import static org.lidiuma.math.internal.AnnotationConst.POINT3_FACTORY;
import static org.lidiuma.math.internal.AnnotationConst.POINT_OUT;

@FactoryAlias(methodName = POINT3_FACTORY, outputClass = POINT_OUT)
@LooselyConsistentValue
public value record Point3I32(
        @Override @NullRestricted Integer x,
        @Override @NullRestricted Integer y,
        @Override @NullRestricted Integer z
) implements Point3<Integer> {

    @FieldAlias(outputClass = POINT_OUT)
    public static final Ops OPS = new Ops();

    /// A constructor creating a specialized point from a generic tuple.
    @AliasExclude // This method can be a performance sink if used inappropriately, so I exclude it from the alias.
    public Point3I32(UnaryTuple3<Integer> tuple) {
        this(tuple.x(), tuple.y(), tuple.z());
    }

    public static final value class Ops implements Point3Ops<Point3I32, Vec3I32, Integer> {

        private Ops() {}

        @Override
        @AliasExclude
        public Point3I32 of(Integer x, Integer y, Integer z) {
            return new Point3I32(x, y, z);
        }

        @Override
        @AliasExclude
        public Vec3I32.Ops vectorOps() {
            return Vec3I32.OPS;
        }
                        
        /*
        Handwritten to remove GC collections when used polymorphically (different generic parameters).
        Speed is more or less the same, but without a rare case of the JIT failing giving x10 less performance.
        This unfortunately creates code duplication, and I'm sure some methods are fine as-is,
        but writing them anyway is faster than making sure with benchmarking.
        */

        @Override
        public Point3I32 add(Point3I32 point, Vec3I32 vector) {
            return point(vectorOps().add(vec(point), vector));
        }

        @Override
        public Vec3I32 subtract(Point3I32 minuend, Point3I32 subtrahend) {
            return vectorOps().subtract(vec(minuend), vec(subtrahend));
        }

        @Override
        public Integer distanceSquared(Point3I32 first, Point3I32 second) {
            return vectorOps().distanceSquared(vec(first), vec(second));
        }

        @Override
        public Point3I32 clamp(Point3I32 point, Integer min, Integer max) {
            return point(vectorOps().clamp(vec(point), min, max));
        }

        private Vec3I32 vec(Point3I32 point) {
            return new Vec3I32(point.x(), point.y(), point.z());
        }

        private Point3I32 point(Vec3I32 vec) {
            return new Point3I32(vec.x(), vec.y(), vec.z());
        }
    }
}
