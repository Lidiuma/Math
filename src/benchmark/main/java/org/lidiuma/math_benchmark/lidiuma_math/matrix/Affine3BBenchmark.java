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
import org.lidiuma.math.rotation.AngleF32;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import java.util.concurrent.TimeUnit;
import java.util.random.RandomGeneratorFactory;
import static org.lidiuma.math.matrix.Matrices.*;
import static org.lidiuma.math.rotation.Rotations.fromAxisAngle;
import static org.lidiuma.math.rotation.Rotations.radians;
import static org.lidiuma.math.vector.Vectors.vec3;
import static org.lidiuma.math_benchmark.BenchmarkMain.consume;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)

@Warmup(iterations = 2, time = 3, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 2, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@Threads(1)
@OperationsPerInvocation(5)
public final class Affine3BBenchmark {

	private static final AngleF32 ROTATION = radians(32f);
	private Affine3F32 matrix;

	@Setup(Level.Iteration)
	public void setupMatrix() {
		final var translation = vec3(32F, 0.5F, 1F);
		final var rotation = fromAxisAngle(vec3(0.25F, 2F, 1F), ROTATION);
		final var scale = vec3(0F, 1F, 0F);
		matrix = fromTRS(translation, rotation, scale);
	}

	@Benchmark
	public void testCreation(Blackhole hole) {
		consume(hole, identityAffine3F32());
	}

	@Benchmark
	public void testStandardOperation(Blackhole hole) {
		final var translation = vec3(32F, 0.5F, 1F);
		final var rotation = fromAxisAngle(vec3(0.25F, 2F, 1F), ROTATION);
		final var scale = vec3(0F, 1F, 0F);
		consume(hole, fromTRS(translation, rotation, scale));
	}
	
	@Benchmark
	public void testMatrixTransform(Blackhole hole) {
		// TODO In a future release use Point3F32.
		consume(hole, multiply(matrix, vec3(1f, 3f, 6f)));
	}
	
	@Benchmark
	@OperationsPerInvocation(100)
	public void testBoneAnimation(Blackhole hole, AnimationContainer container) {
		final var processed = container.animation.process();
		for (Affine3F32 a : processed) consume(hole, a);
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
