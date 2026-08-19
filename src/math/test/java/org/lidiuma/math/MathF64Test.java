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

package org.lidiuma.math;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.lidiuma.math.matrix.Matrices;
import org.lidiuma.math.rotation.AngleF64;
import org.lidiuma.math.rotation.Rotations;
import org.lidiuma.math.vector.Vec3F64;
import org.lidiuma.math.vector.Vectors;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public value class MathF64Test {

    public static final Vec3F64 X_AXIS = Vectors.vec3(1d, 0d, 0d);
    public static final Vec3F64 Y_AXIS = Vectors.vec3(0d, 1d, 0d);
    public static final Vec3F64 Z_AXIS = Vectors.vec3(0d, 0d, 1d);
    public static final AngleF64 DEG_180 = Rotations.degrees(180d);

    @Test
    void rotation2D() {

        final var start = Vectors.vec2(10d, 10d);
        final var rot180 = Matrices.fromRotation(DEG_180);

        final var halfRotation = Matrices.multiply(rot180, start);
        final var fullRotation = Matrices.multiply(rot180, halfRotation);

        // The opposite direction of a vector is its negated version, equivalent to a 180-degree turn.
        Assertions.assertEquals(Vectors.negated(start), halfRotation);
        Assertions.assertEquals(start, fullRotation);
    }

    @Test
    void rotation3D() {

        // I could rotate the vector directly with the quaternion, but I convert it to a matrix to have a wider testing area.
        final var start = Vectors.vec3(10d, 10d, 10d);
        final var rotX180 = Matrices.fromRotation(Rotations.fromAxisAngle(X_AXIS, DEG_180));
        final var rotY180 = Matrices.fromRotation(Rotations.fromAxisAngle(Y_AXIS, DEG_180));
        final var rotZ180 = Matrices.fromRotation(Rotations.fromAxisAngle(Z_AXIS, DEG_180));

        final var halfX = Matrices.multiply(rotX180, start);
        final var halfY = Matrices.multiply(rotY180, start);
        final var halfZ = Matrices.multiply(rotZ180, start);
        final var fullY = Matrices.multiply(rotY180, halfY);

        Assertions.assertEquals(Vectors.vec3(10d, -10d, -10d), halfX);
        Assertions.assertEquals(Vectors.vec3(-10d, 10d, -10d), halfY);
        Assertions.assertEquals(Vectors.vec3(-10d, -10d, 10d), halfZ);
        Assertions.assertEquals(Vectors.vec3(-10d, -10d, 10d), halfZ);
        Assertions.assertEquals(Vectors.vec3(-10d, -10d, 10d), halfZ);
        Assertions.assertEquals(start, fullY);
    }
}
