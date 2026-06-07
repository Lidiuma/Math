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

package org.lidiuma.math.vector;

import jdk.internal.vm.annotation.NullRestricted;
import org.lidiuma.math.api.vector.FloatingVector4Ops;
import org.lidiuma.math.api.vector.Vector4;
import org.lidiuma.math.numerics.DoubleNumeric;
import org.lidiuma.math.rotation.AngleF64;

public value record Vec4F64(
        @Override @NullRestricted Double x,
        @Override @NullRestricted Double y,
        @Override @NullRestricted Double z,
        @Override @NullRestricted Double w
) implements Vector4<Double> {

    public static final FloatingVector4Ops<Vec4F64, AngleF64, Double> WITNESS = new FloatingVector4Ops<>() {

        @Override
        public Vec4F64 of(Double x, Double y, Double z, Double w) {
            return new Vec4F64(x, y, z, w);
        }

        @Override
        public AngleF64 angle(Vec4F64 v1, Vec4F64 v2) {
            final double dot = dot(v1, v2);
            final double length1 = lengthSquared(v1);
            final double length2 = lengthSquared(v2);
            final double theta = dot / Math.sqrt(length1 * length2);
            return AngleF64.radians(Math.acos(theta));
        }

        @Override
        public DoubleNumeric scalarOps() {
            return DoubleNumeric.WITNESS;
        }
    };

    public Vec4F64(Vector4<Double> vec) {
        this(vec.x(), vec.y(), vec.z(), vec.w());
    }

    @Override
    public Vec3F64 withoutW() {
        return new Vec3F64(x(), y(), z());
    }
}
