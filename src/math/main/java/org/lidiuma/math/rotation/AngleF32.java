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

import jdk.internal.vm.annotation.LooselyConsistentValue;
import org.lidiuma.math.api.rotation.Angle;
import org.lidiuma.math.api.traits.rotation.AngleOps;
import org.lidiuma.math.internal.Epsilon;
import org.lidiuma.math.processor.FieldAlias;
import org.lidiuma.math.processor.NamedAlias;
import org.lidiuma.math.vector.Vec2F32;
import java.util.function.UnaryOperator;
import static org.lidiuma.math.internal.AnnotationConst.ROTATION_OUT;

@LooselyConsistentValue
public value class AngleF32 implements Angle<Float> {

    @FieldAlias(outputClass = ROTATION_OUT)
    public static final Ops OPS = new Ops();

    // Hidden representation since this class represent a generic angle and not what it's made of.
    private float radians;

    private AngleF32(float radians) {
        this.radians = radians;
    }

    /// A constructor creating a specialized angle from a generic angle.
    // This method can be a performance sink if used inappropriately, so I exclude it from the alias.
    public AngleF32(Angle<Float> angle) {
        this(angle.radians());
    }

    @Override
    public Float radians() {
        return radians;
    }

    @Override
    public Float degrees() {
        return (float) Math.toDegrees(radians());
    }

    @Override
    public Float turns() {
        return (float) (radians() / Math.TAU);
    }

    @Override
    public Vec2F32 vector() {
        return new Vec2F32(
                Rotations.cos(this),
                Rotations.sin(this)
        );
    }

    public static final value class Ops implements AngleOps<AngleF32, Vec2F32, Float> {

        @Override
        @NamedAlias(methodName = "radians")
        public AngleF32 fromRadians(Float radians) {
            return new AngleF32(radians);
        }

        @Override
        @NamedAlias(methodName = "degrees")
        public AngleF32 fromDegrees(Float degrees) {
            return fromRadians((float) Math.toRadians(degrees));
        }

        @Override
        @NamedAlias(methodName = "turns")
        public AngleF32 fromTurns(Float turns) {
            return fromRadians((float) (turns * Math.TAU));
        }

        @Override
        @NamedAlias(methodName = "vectorAngle")
        public AngleF32 fromVector(Vec2F32 vector) {
            final var angle = (float) Math.atan2(vector.y(), vector.x());
            return fromRadians(angle < 0f ? (float) (angle + Math.TAU) : angle);
        }

        @Override
        public Float cos(AngleF32 angle) {
            // If I cast after the cos(), the cast loses precision and goes bellow epsilon.
            return Epsilon.clamp((float) Math.cos(angle.radians()));
        }

        @Override
        public Float sin(AngleF32 angle) {
            // If I cast after the sin(), the cast loses precision and goes bellow epsilon.
            return Epsilon.clamp((float) Math.sin(angle.radians()));
        }

        @Override
        public Float tan(AngleF32 angle) {
            return (float) Math.tan(angle.radians());
        }

        @Override
        public AngleF32 normalize(AngleF32 angle) {
            final float normalized = (float) (angle.radians() % Math.TAU);
            return fromRadians((float) (normalized + (normalized < 0f ? Math.TAU : 0f)));
        }

        @Override
        public AngleF32 add(AngleF32 op1, AngleF32 op2) {
            return fromRadians(op1.radians() + op2.radians());
        }

        @Override
        public AngleF32 subtract(AngleF32 op1, AngleF32 op2) {
            return fromRadians(op1.radians() - op2.radians());
        }

        @Override
        public AngleF32 multiply(AngleF32 op1, Float scalar) {
            return fromRadians(op1.radians() * scalar);
        }

        @Override
        public AngleF32 divide(AngleF32 op1, Float scalar) {
            return fromRadians(op1.radians() / scalar);
        }

        @Override
        public AngleF32 negated(AngleF32 operand) {
            return normalize(fromRadians(-operand.radians()));
        }

        @Override
        public AngleF32 interpolate(AngleF32 start, AngleF32 end, Float alpha, UnaryOperator<Float> easing) {

            final float easedAlpha = easing.apply(alpha);
            final float startRadian = start.radians();

            // Compute the shortest angular difference
            final float delta = end.radians() - startRadian;
            final float shortestDelta = (float) (delta - Math.TAU * Math.floor((delta + Math.PI) / Math.TAU));

            return normalize(fromRadians(startRadian + shortestDelta * easedAlpha));
        }
    }
}
