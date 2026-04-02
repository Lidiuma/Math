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

package org.lidiuma.math.rotation;

import jdk.internal.vm.annotation.NullRestricted;
import org.lidiuma.math.api.rotation.Angle;

public value record AngleF(
        @Override @NullRestricted Float radian
) implements Angle<Float> {

    public static AngleF radians(float radians) {
        return new AngleF(radians);
    }

    public static AngleF degrees(float degrees) {
        return new AngleF((float) Math.toRadians(degrees));
    }

    public static AngleF turns(float turns) {
        return new AngleF((float) (turns * Math.TAU));
    }

    @Override
    public Float degree() {
        return (float) Math.toDegrees(radian);
    }

    @Override
    public Float turn() {
        return (float) (radian / Math.TAU);
    }
}
