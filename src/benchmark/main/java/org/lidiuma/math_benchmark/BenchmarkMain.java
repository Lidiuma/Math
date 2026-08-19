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

package org.lidiuma.math_benchmark;

import org.jspecify.annotations.NullMarked;
import org.lidiuma.math.matrix.Affine3F32;
import org.lidiuma.math.matrix.Matrices;
import org.lidiuma.math.rotation.AngleF32;
import org.lidiuma.math.rotation.Rotations;
import org.lidiuma.math.vector.Vec3F32;
import org.openjdk.jmh.Main;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import java.util.Arrays;
import java.util.Random;
import static org.lidiuma.math.matrix.Matrices.fromRotation;
import static org.lidiuma.math.matrix.Matrices.identityAffine3F32;
import static org.lidiuma.math.rotation.Rotations.fromAxisAngle;
import static org.lidiuma.math.rotation.Rotations.radians;
import static org.lidiuma.math.vector.Vectors.*;

@NullMarked
@State(Scope.Thread)
@SuppressWarnings("unused")
public class BenchmarkMain {

    static void main(String... args) throws Exception {
        System.out.println(Arrays.toString(args));
        Main.main(args);
    }

    private AngleF32 angle = radians(0); // Default value, not actually used.
    private float x;
    private float y;
    private float z;
    private Affine3F32 matrix = identityAffine3F32(); // Default value, not actually used.
    private Vec3F32 vector = zeroVec3F32(); // Default value, not actually used.

    @Setup
    public void setup() {
        final var random = new Random();
        angle = radians(random.nextFloat());
        x = random.nextFloat();
        y = random.nextFloat();
        z = random.nextFloat();
        final var quat = fromAxisAngle(vec3(0f, 1f, 0f), angle);
        matrix = fromRotation(quat);
        vector = vec3(x, y, z);
    }

    @Benchmark
    @Warmup(iterations = 6,  time = 1)
    @Measurement(iterations = 2,  time = 1)
    @Fork(1)
    public void rotationCached(Blackhole hole) {

        final var r = Matrices.multiply(matrix, vector);

        // Feeding the vector directly will force it to go onto the heap, killing performance.
        hole.consume(r.x());
        hole.consume(r.y());
        hole.consume(r.z());
    }

    @Benchmark
    @Warmup(iterations = 6,  time = 1)
    @Measurement(iterations = 2,  time = 1)
    @Fork(1)
    public void rotation(Blackhole hole) {

        final var yAxis = vec3(0f, 1f, 0f);
        final var v3 = vec3(x, y, z);
        final var r = Rotations.rotate(fromAxisAngle(yAxis, angle), v3); // I rotate v3 on the yAxis by angle.

        // Feeding the vector directly will force it to go onto the heap, killing performance.
        hole.consume(r.x());
        hole.consume(r.y());
        hole.consume(r.z());
    }

    @Benchmark
    @Warmup(iterations = 6,  time = 1)
    @Measurement(iterations = 2,  time = 1)
    @Fork(1)
    public void operations(Blackhole hole) {

        final var r1 = add(vec3(x, y, z), vec3(z, y, z));
        final var r2 = multiply(r1, z);
        final var r3 = divide(r2, vec3(1f, x + 1, 1f));
        final var r = subtract(r3, vec3(y - 2, z, x));

        // Feeding the vector directly will force it to go onto the heap, killing performance.
        hole.consume(r.x());
        hole.consume(r.y());
        hole.consume(r.z());
    }
}
