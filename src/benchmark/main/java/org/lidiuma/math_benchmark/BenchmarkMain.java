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
import org.lidiuma.math.rotation.AngleF64;
import org.lidiuma.math.rotation.Rotations;
import org.lidiuma.math.vector.Vec3F32;
import org.lidiuma.math.vector.Vectors;
import org.openjdk.jmh.Main;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import java.util.Arrays;
import java.util.Random;
import static org.lidiuma.math.matrix.Matrices.fromRotation;
import static org.lidiuma.math.matrix.Matrices.identityAffine3F32;
import static org.lidiuma.math.rotation.Rotations.fromAxisAngle;
import static org.lidiuma.math.rotation.Rotations.radians;
import static org.lidiuma.math.vector.Vectors.vec3;
import static org.lidiuma.math.vector.Vectors.zeroVec3F32;

@NullMarked
@State(Scope.Thread)
@SuppressWarnings("unused")
public class BenchmarkMain {

    static void main(String... args) throws Exception {
        System.out.println(Arrays.toString(args));
        Main.main(args);
    }

    private AngleF32 angleF32 = radians(0f); // Default value, not actually used.
    private AngleF64 angleF64 = radians(0d); // Default value, not actually used.
    private float xf;
    private float yf;
    private float zf;
    private double xd;
    private double yd;
    private double zd;
    private Affine3F32 matrix = identityAffine3F32(); // Default value, not actually used.
    private Vec3F32 vector = zeroVec3F32(); // Default value, not actually used.

    @Setup
    public void setup() {

        final var random = new Random();
        angleF32 = radians(random.nextFloat());
        angleF64 = radians(random.nextDouble());

        xf = random.nextFloat(); yf = random.nextFloat(); zf = random.nextFloat();
        xd = random.nextDouble(); yd = random.nextDouble(); zd = random.nextDouble();

        final var quat = fromAxisAngle(vec3(0f, 1f, 0f), angleF32);
        matrix = fromRotation(quat);
        vector = vec3(xf, yf, zf);
    }

    @Benchmark
    @Warmup(iterations = 1,  time = 20)
    @Measurement(iterations = 1,  time = 20)
    @Fork(1)
    public void rotationCached(Blackhole hole) {

        final var r = Matrices.multiply(matrix, vector);

        // Feeding the vector directly will force it to go onto the heap, killing performance.
        hole.consume(r.x());
        hole.consume(r.y());
        hole.consume(r.z());
    }

    @Benchmark
    @Warmup(iterations = 1,  time = 20)
    @Measurement(iterations = 1,  time = 30)
    @Fork(1)
    public void rotationF32(Blackhole hole) {

        final var yAxis = Vectors.vec3(0f, 1f, 0f);
        final var v3 = Vectors.vec3(xf, yf, zf);
        final var quat = Rotations.fromAxisAngle(yAxis, angleF32);
        final var r = Rotations.rotate(quat, v3); // I rotate v3 on the yAxis by angle.

        // Feeding the vector directly will force it to go onto the heap, killing performance.
        hole.consume(r.x());
        hole.consume(r.y());
        hole.consume(r.z());
    }

    @Benchmark
    @Warmup(iterations = 1,  time = 20)
    @Measurement(iterations = 1,  time = 30)
    @Fork(1)
    public void rotationF64(Blackhole hole) {

        final var yAxis = Vectors.vec3(0d, 1d, 0d);
        final var v3 = Vectors.vec3(xd, yd, zd);
        final var quat = Rotations.fromAxisAngle(yAxis, angleF64);
        final var r = Rotations.rotate(quat, v3); // I rotate v3 on the yAxis by angle.

        // Feeding the vector directly will force it to go onto the heap, killing performance.
        hole.consume(r.x());
        hole.consume(r.y());
        hole.consume(r.z());
    }

    @Benchmark
    @Warmup(iterations = 1,  time = 20)
    @Measurement(iterations = 1,  time = 20)
    @Fork(1)
    public void operations(Blackhole hole) {

        final var r1 = Vectors.add(vec3(xf, yf, zf), vec3(zf, yf, zf));
        final var r2 = Vectors.multiply(r1, zf);
        final var r3 = Vectors.divide(r2, vec3(1f, xf + 1, 1f));
        final var r = Vectors.subtract(r3, vec3(yf - 2, zf, xf));

        // Feeding the vector directly will force it to go onto the heap, killing performance.
        hole.consume(r.x());
        hole.consume(r.y());
        hole.consume(r.z());
    }
}
