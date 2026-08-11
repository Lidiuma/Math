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

package org.lidiuma.math.color;

import jdk.internal.vm.annotation.NullRestricted;
import org.lidiuma.math.api.color.Color;
import org.lidiuma.math.api.traits.color.ColorOps;
import org.lidiuma.math.vector.Vec4F64;
import java.util.function.UnaryOperator;

public value record ColorF64(
        @Override @NullRestricted Double red,
        @Override @NullRestricted Double green,
        @Override @NullRestricted Double blue,
        @Override @NullRestricted Double alpha
) implements Color<Double> {

    public static final ColorF64.Ops OPS = new ColorF64.Ops();

    public static final class Ops implements ColorOps<ColorF64, Double> {

        // I use Vec4.Ops to avoid re-doing the math, which is error-prone.

        private Vec4F64 v(ColorF64 c) {
            return new Vec4F64(c.red(), c.green(), c.blue(), c.alpha());
        }

        private ColorF64 c(Vec4F64 v) {
            return new ColorF64(v.x(), v.y(), v.z(), v.w());
        }

        @Override
        public ColorF64 clamp(ColorF64 value, ColorF64 min, ColorF64 max) {
            return c(Vec4F64.OPS.clamp(v(value), v(min), v(max)));
        }

        @Override
        public ColorF64 interpolate(ColorF64 start, ColorF64 end, Double alpha, UnaryOperator<Double> easing) {
            return c(Vec4F64.OPS.interpolate(v(start), v(end), alpha, easing));
        }
    }
}
