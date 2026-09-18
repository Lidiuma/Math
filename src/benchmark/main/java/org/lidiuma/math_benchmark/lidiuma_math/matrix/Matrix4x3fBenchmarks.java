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

package org.lidiuma.math_benchmark.lidiuma_math.matrix;

import org.lidiuma.math.matrix.Affine3F32;
import org.lidiuma.math.matrix.Matrices;
import org.lidiuma.math.rotation.QuaternionF32;
import org.lidiuma.math.vector.Vec3F32;
import org.lidiuma.math_benchmark.base.matrix.Matrix4x3fData;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import java.util.concurrent.TimeUnit;
import java.util.random.RandomGeneratorFactory;
import static org.lidiuma.math.matrix.Matrices.*;
import static org.lidiuma.math.rotation.Rotations.fromAxisAngle;
import static org.lidiuma.math.rotation.Rotations.radians;
import static org.lidiuma.math.vector.Vectors.vec3;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)

@Warmup(iterations = 2, time = 3, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 2, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@Threads(1)
@OperationsPerInvocation(5)
public class Matrix4x3fBenchmarks extends Matrix4x3fData {
	private Affine3F32 matrix;

	@Setup(Level.Iteration)
	public void setupMatrix() {
		setupMatrixData();
		matrix = fromTRS(vec3(tx, ty, tz), new QuaternionF32(qx, qy, qz, qw), vec3(sx, sy, sz));
	}

	@Benchmark
	public Affine3F32 testCreation() {
		return identityAffine3F32();
	}

	@Benchmark
	public Affine3F32 testStandardOperation(Blackhole hole) {
		final var translation = fromTranslation(vec3(tx, ty, tz));
		final var rotation = fromRotation(fromAxisAngle(vec3(ax, ay, az), radians(angle)));
		final var scale = fromScale(vec3(sx, sy, sz));
		return multiply(translation, multiply(rotation, scale));
	}

	@Benchmark
	public Affine3F32 testStandardOperationOptimized(Blackhole hole) {
		return Matrices.fromTRS(vec3(tx, ty, tz), fromRotation(fromAxisAngle(vec3(ax, ay, az), radians(angle))), vec3(sx, sy, sz));
	}

	@Benchmark
	public Affine3F32 testComposeTRS() {
		return fromTRS(vec3(tx, ty, tz), new QuaternionF32(qx, qy, qz, qw), vec3(sx, sy, sz));
	}

	@Benchmark
	public Vec3F32 testMatrixTransform() {
		return multiply(matrix, vec3(px, py, pz));
	}
	
	@Benchmark
	@OperationsPerInvocation(100)
	public Affine3F32[] testBoneAnimation(AnimationContainer container) {
		return container.animation.process();
	}
	
	@State(Scope.Benchmark)
	public static class AnimationContainer {
		private BoneAnimation animation;
		
		@Param("100")
		public int operationMultiplier;
		
		@Setup(Level.Iteration)
		public void setupContainer() {
			animation = new BoneAnimation(operationMultiplier, RandomGeneratorFactory.getDefault().create(32231212134522L));
		}
	}
}