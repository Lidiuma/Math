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

import org.lidiuma.math.api.rotation.Angle;
import org.lidiuma.math.api.traits.rotation.AngleOps;
import org.lidiuma.math.internal.Epsilon;
import org.lidiuma.math.processor.FieldAlias;
import org.lidiuma.math.processor.NamedAlias;
import org.lidiuma.math.vector.Vec2F64;
import java.util.function.UnaryOperator;
import static org.lidiuma.math.internal.AnnotationConst.ROTATION_OUT;

public class AngleF64 implements Angle<Double> {

    @FieldAlias(outputClass = ROTATION_OUT)
    public static final Ops OPS = new Ops();

    // Hidden representation since this class represent a generic angle and not what it's made of.
    private double radians;

    private AngleF64(double radians) {
        this.radians = radians;
    }

    /// A constructor creating a specialized angle from a generic angle.
    // This method can be a performance sink if used inappropriately, so I exclude it from the alias.
    public AngleF64(Angle<Double> angle) {
        this(angle.radians());
    }

    @Override
    public Double radians() {
        return radians;
    }

    @Override
    public Double degrees() {
        return Math.toDegrees(radians());
    }

    @Override
    public Double turns() {
        return radians() / Math.TAU;
    }

    @Override
    public Vec2F64 vector() {
        return new Vec2F64(
                Rotations.cos(this),
                Rotations.sin(this)
        );
    }

    public static final class Ops implements AngleOps<AngleF64, Vec2F64, Double> {

        @Override
        @NamedAlias(methodName = "radians")
        public AngleF64 fromRadians(Double radians) {
            return new AngleF64(radians);
        }

        @Override
        @NamedAlias(methodName = "degrees")
        public AngleF64 fromDegrees(Double degrees) {
            return fromRadians(Math.toRadians(degrees));
        }

        @Override
        @NamedAlias(methodName = "turns")
        public AngleF64 fromTurns(Double turns) {
            return fromRadians((turns * Math.TAU));
        }

        @Override
        @NamedAlias(methodName = "vectorAngle")
        public AngleF64 fromVector(Vec2F64 vector) {
            final var angle = Math.atan2(vector.y(), vector.x());
            return fromRadians(angle < 0d ? (angle + Math.TAU) : angle);
        }

        @Override
        public Double cos(AngleF64 angle) {
            return Epsilon.clamp(Math.cos(angle.radians()));
        }

        @Override
        public Double sin(AngleF64 angle) {
            return Epsilon.clamp(Math.sin(angle.radians()));
        }

        @Override
        public Double tan(AngleF64 angle) {
            return Math.tan(angle.radians());
        }

        @Override
        public AngleF64 normalize(AngleF64 angle) {
            final double normalized = angle.radians() % Math.TAU;
            return fromRadians(normalized + (normalized < 0d ? Math.TAU : 0d));
        }

        @Override
        public AngleF64 add(AngleF64 op1, AngleF64 op2) {
            return fromRadians(op1.radians() + op2.radians());
        }

        @Override
        public AngleF64 subtract(AngleF64 op1, AngleF64 op2) {
            return fromRadians(op1.radians() - op2.radians());
        }

        @Override
        public AngleF64 multiply(AngleF64 op1, Double scalar) {
            return fromRadians(op1.radians() * scalar);
        }

        @Override
        public AngleF64 divide(AngleF64 op1, Double scalar) {
            return fromRadians(op1.radians() / scalar);
        }

        @Override
        public AngleF64 negated(AngleF64 operand) {
            return normalize(fromRadians(-operand.radians()));
        }

        @Override
        public AngleF64 interpolate(AngleF64 start, AngleF64 end, Double alpha, UnaryOperator<Double> easing) {

            final double easedAlpha = easing.apply(alpha);
            final double startRadian = start.radians();

            // Compute the shortest angular difference
            final double delta = end.radians() - startRadian;
            final double shortestDelta = delta - Math.TAU * Math.floor((delta + Math.PI) / Math.TAU);

            return normalize(fromRadians(startRadian + shortestDelta * easedAlpha));
        }
    }
}
