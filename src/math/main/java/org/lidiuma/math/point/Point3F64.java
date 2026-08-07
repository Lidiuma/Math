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
import org.lidiuma.math.processor.Alias;
import org.lidiuma.math.processor.AliasExclude;
import org.lidiuma.math.processor.FactoryAlias;
import org.lidiuma.math.vector.Vec3F64;

@LooselyConsistentValue
@FactoryAlias(methodName = "point3", outputClass = "Points")
public value record Point3F64(
        @NullRestricted Double x,
        @NullRestricted Double y,
        @NullRestricted Double z
) implements Point3<Double> {

    @Alias(outputClass = "Points")
    public static final Ops WITNESS = new Ops();

    public static final class Ops implements FloatingPoint3Ops<Point3F64, Vec3F64, Double> {

        @Override
        @AliasExclude
        public Point3F64 of(Double x, Double y, Double z) {
            return new Point3F64(x, y, z);
        }

        @Override
        @AliasExclude
        public Vec3F64.Ops vectorOps() {
            return Vec3F64.WITNESS;
        }
    }
}
