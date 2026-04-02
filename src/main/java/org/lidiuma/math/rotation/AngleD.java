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

public value record AngleD(
        @Override @NullRestricted Double radian
) implements Angle<Double> {

    public static AngleD radians(double radians) {
        return new AngleD(radians);
    }

    public static AngleD degrees(double degrees) {
        return new AngleD(Math.toRadians(degrees));
    }

    public static AngleD turns(double turns) {
        return new AngleD(turns * Math.TAU);
    }

    @Override
    public Double degree() {
        return Math.toDegrees(radian);
    }

    @Override
    public Double turn() {
        return radian / Math.TAU;
    }
}
