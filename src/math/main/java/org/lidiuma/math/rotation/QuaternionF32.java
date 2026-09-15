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

import org.lidiuma.math.api.rotation.Quaternion;
import org.lidiuma.math.api.traits.rotation.QuaternionOps;
import org.lidiuma.math.api.tuple.UnaryTuple4;
import org.lidiuma.math.internal.Epsilon;
import org.lidiuma.math.numerics.FloatNumeric;
import org.lidiuma.math.processor.AliasExclude;
import org.lidiuma.math.processor.FactoryAlias;
import org.lidiuma.math.processor.FieldAlias;
import org.lidiuma.math.processor.NamedAlias;
import org.lidiuma.math.vector.Vec3F32;
import java.util.function.UnaryOperator;
import static java.lang.Math.fma;
import static org.lidiuma.math.internal.AnnotationConst.*;

@FactoryAlias(methodName = QUATERNION_FACTORY, outputClass = ROTATION_OUT)
public value record QuaternionF32(
        @Override Float x,
        @Override Float y,
        @Override Float z,
        @Override Float w
) implements Quaternion<Float> {

    @FieldAlias(outputClass = ROTATION_OUT)
    public static final Ops OPS = new Ops();

    /// A constructor creating a specialized quaternion from a generic quaternion.
    @AliasExclude // This method can be a performance sink if used inappropriately, so I exclude it from the alias.
    public QuaternionF32(UnaryTuple4<Float> v4) {
        this(v4.x(), v4.y(), v4.z(), v4.w());
    }

    public static final value class Ops implements QuaternionOps<QuaternionF32, Vec3F32, AngleF32, Float> {

        private Ops() {}

        @Override
        @AliasExclude
        public QuaternionF32 of(Float x, Float y, Float z, Float w) {
            return new QuaternionF32(x, y, z, w);
        }

        @Override
        public QuaternionF32 fromAxisAngle(Vec3F32 axis, AngleF32 angle) {
            final AngleF32 half = Rotations.multiply(angle, .5f);
            final float sin = Rotations.sin(half);
            return of(
                    (axis.x() * sin),
                    (axis.y() * sin),
                    (axis.z() * sin),
                    Rotations.cos(half)
            );
        }

        @Override
        public QuaternionF32 fromEulerAngle(AngleF32 yaw, AngleF32 pitch, AngleF32 roll) {

            final AngleF32 hr = Rotations.multiply(roll, .5f);
            final float shr = Rotations.sin(hr);
            final float chr = Rotations.cos(hr);

            final AngleF32 hp = Rotations.multiply(pitch, .5f);
            final float shp = Rotations.sin(hp);
            final float chp = Rotations.cos(hp);

            final AngleF32 hy = Rotations.multiply(yaw, .5f);
            final float shy = Rotations.sin(hy);
            final float chy = Rotations.cos(hy);

            final float chyShp = chy * shp;
            final float shyChp = shy * chp;
            final float chyChp = chy * chp;
            final float shyShp = shy * shp;

            final float newX = (chyShp * chr) + (shyChp * shr); // cos(yaw/2) * sin(pitch/2) * cos(roll/2) + sin(yaw/2) * cos(pitch/2) * sin(roll/2)
            final float newY = (shyChp * chr) - (chyShp * shr); // sin(yaw/2) * cos(pitch/2) * cos(roll/2) - cos(yaw/2) * sin(pitch/2) * sin(roll/2)
            final float newZ = (chyChp * shr) - (shyShp * chr); // cos(yaw/2) * cos(pitch/2) * sin(roll/2) - sin(yaw/2) * sin(pitch/2) * cos(roll/2)
            final float newW = (chyChp * chr) + (shyShp * shr); // cos(yaw/2) * cos(pitch/2) * cos(roll/2) + sin(yaw/2) * sin(pitch/2) * sin(roll/2)
            return new QuaternionF32(newX, newY, newZ, newW);
        }

        @Override
        public QuaternionF32 fromRotationBetween(Vec3F32 vector1, Vec3F32 vector2) {

            final var vOps = Vec3F32.OPS;
            final float dot = Math.clamp(vOps.dot(vector1, vector2), -1f, 1f);

            // When the vectors are parallel.
            if (dot >= 1f) return identity();

            // If the vectors are antiparallel (dot == -1), rotate 180 degrees around an arbitrary perpendicular axis.
            if (dot <= -1f) {
                final var perpendicular = Math.abs(vector1.x()) < .9f ?
                        new Vec3F32(1f, 0f, 0f) :
                        new Vec3F32(0f, 1f, 0f);
                final var axis = vOps.cross(perpendicular, vector1);
                // xyz = axis * (sin(pi / 2) = 1), w = cos(pi / 2) = 0
                return of(axis.x(), axis.y(), axis.z(), 0f);
            }

            final Vec3F32 cross = vOps.cross(vector1, vector2);
            final float scale = (float) Math.sqrt(2f * (1f + dot));
            return of(
                    cross.x() / scale,
                    cross.y() / scale,
                    cross.z() / scale,
                    (1f + dot) / scale
            );
        }

        @Override
        public QuaternionF32 exp(QuaternionF32 quaternion) {

            final var witness = Vec3F32.OPS;
            final var vectorQuat = new Vec3F32(quaternion.x(), quaternion.y(), quaternion.z());
            final float radians = witness.length(vectorQuat); // The math is the same.

            if (radians < Epsilon.EPSILON_F32) return identity();

            final AngleF32 angle = Rotations.radians(radians);
            final float sin = Rotations.sin(angle);
            final float cos = Rotations.cos(angle);

            final float k = sin / radians;
            return of(
                    quaternion.x() * k,
                    quaternion.y() * k,
                    quaternion.z() * k,
                    cos
            );
        }

        @Override
        public QuaternionF32 log(QuaternionF32 quaternion) {

            final float w = Math.clamp(quaternion.w(), -1f, 1f);
            final float radians = (float) Math.acos(w);
            final float sin = (float) Math.sqrt(Math.max(0f, 1f - w * w));

            if (sin < Epsilon.EPSILON_F32) return of(quaternion.x(), quaternion.y(), quaternion.z(), 0f);

            final float k = radians / sin;
            return of(
                    quaternion.x() * k,
                    quaternion.y() * k,
                    quaternion.z() * k,
                    0f
            );
        }

        @Override
        public QuaternionF32 pow(QuaternionF32 quaternion, Float exponent) {
            return exp(multiply(log(quaternion), exponent));
        }

        @Override
        public QuaternionF32 slerp(QuaternionF32 start, QuaternionF32 end, Float alpha) {

            final float dot = dot(start, end);
            final float absDot = Math.abs(dot);
            final float sign = dot < 0f ? -1f : 1f;

            // To avoid numerical instability at low angles, I use nlerp.
            if (absDot > 0.9995f) return normalize(lerp(start, multiply(end, sign), alpha));

            final AngleF32 angle = Rotations.radians((float) Math.acos(absDot));
            final float invSinTheta = 1f / Rotations.sin(angle);

            final float scale0 = Rotations.sin(Rotations.multiply(angle, 1f - alpha)) * invSinTheta;
            final float scale1 = Rotations.sin(Rotations.multiply(angle, alpha)) * invSinTheta;
            return add(multiply(start, scale0), multiply(end, sign * scale1));
        }

        @Override
        public Vec3F32 rotate(QuaternionF32 quaternion, Vec3F32 vector) {
            final var conjugate = conjugate(quaternion);
            final var vectorQuat = of(vector.x(), vector.y(), vector.z(), 0f);
            final var rotated = multiply(multiply(quaternion, vectorQuat), conjugate);
            return new Vec3F32(rotated.x(), rotated.y(), rotated.z());
        }

        @Override
        public Vec3F32 unrotate(QuaternionF32 quaternion, Vec3F32 vector) {
            final var conjugate = conjugate(quaternion);
            final var vectorQuat = of(vector.x(), vector.y(), vector.z(), 0f);
            final var unrotated = multiply(multiply(conjugate, vectorQuat), quaternion);
            return new Vec3F32(unrotated.x(), unrotated.y(), unrotated.z());
        }

        @Override
        public AxisAngleF32 axisAngle(QuaternionF32 quaternion, Float epsilon) {

            final float sqrt = (float) Math.sqrt(1f - quaternion.w() * quaternion.w());
            final var angle = angle(quaternion);

            // I avoid dividing by 0 if the sqrt is small enough.
            if (sqrt < epsilon) {
                // I re-normalize because without w the length might no longer be 1.
                final var ws = Vec3F32.OPS;
                final var axis = ws.normalize(new Vec3F32(quaternion.x(), quaternion.y(), quaternion.z()));
                return new AxisAngleF32(axis, angle);
            }

            final var axis = new Vec3F32(
                    quaternion.x() / sqrt,
                    quaternion.y() / sqrt,
                    quaternion.z() / sqrt
            );
            return new AxisAngleF32(axis, angle);
        }

        @Override
        public AngleF32 angle(QuaternionF32 quaternion) {
            final float w = Math.clamp(quaternion.w(), -1f, 1f);
            return Rotations.radians((float) (2f * Math.acos(w)));
        }

        @Override
        public SwingTwistF32 swingTwist(QuaternionF32 quaternion, Vec3F32 axis) {
            final var twist = twist(quaternion, axis);
            final var swing = multiply(quaternion, conjugate(twist));
            return new SwingTwistF32(swing, twist);
        }

        @Override
        public AngleF32 angleAround(QuaternionF32 quaternion, Vec3F32 axis) {
            return angle(twist(quaternion, axis));
        }

        private QuaternionF32 twist(QuaternionF32 quaternion, Vec3F32 axis) {

            final var witness = Vec3F32.OPS;
            final var vectorQuat = new Vec3F32(quaternion.x(), quaternion.y(), quaternion.z());
            final float dot = witness.dot(vectorQuat, axis);

            return normalize(of(
                    axis.x() * dot,
                    axis.y() * dot,
                    axis.z() * dot,
                    quaternion.w())
            );
        }

        @Override
        @NamedAlias(methodName = ZERO_FACTORY + F32)
        public QuaternionF32 zero() {
            return QuaternionOps.super.zero();
        }

        @Override
        @NamedAlias(methodName = ONE_FACTORY + F32)
        public QuaternionF32 one() {
            return QuaternionOps.super.one();
        }

        @Override
        @NamedAlias(methodName = IDENTITY_FACTORY + F32)
        public QuaternionF32 identity() {
            return QuaternionOps.super.identity();
        }

        @Override
        @AliasExclude
        public FloatNumeric scalarOps() {
            return FloatNumeric.OPS;
        }

        @Override
        public Float sum(QuaternionF32 quaternion) {
            return quaternion.x() + quaternion.y() + quaternion.z() + quaternion.w();
        }

        @Override
        public QuaternionF32 conjugate(QuaternionF32 quaternion) {
            return of(
                    -quaternion.x(),
                    -quaternion.y(),
                    -quaternion.z(),
                    quaternion.w()
            );
        }

        @Override
        public QuaternionF32 invert(QuaternionF32 quaternion) {
            return multiply(conjugate(quaternion), 1f / lengthSquared(quaternion));
        }

        @Override
        public Float length(QuaternionF32 quaternion) {
            return (float) Math.sqrt(lengthSquared(quaternion));
        }

        @Override
        public Float lengthSquared(QuaternionF32 quaternion) {
            return dot(quaternion, quaternion);
        }

        @Override
        public QuaternionF32 withLength(QuaternionF32 quaternion, Float length) {
            return withMagnitude(quaternion, length, length(quaternion));
        }

        @Override
        public QuaternionF32 withLimit(QuaternionF32 quaternion, Float limit) {
            final float length = length(quaternion);
            return length <= limit ? quaternion : withMagnitude(quaternion, limit, length);
        }

        @Override
        public Float dot(QuaternionF32 q1, QuaternionF32 q2) {
            return fma(q1.x(), q2.x(), fma(q1.y(), q2.y(), fma(q1.z(), q2.z(), q1.w() * q2.w())));
        }

        @Override
        public QuaternionF32 normalize(QuaternionF32 quaternion) {
            return withLength(quaternion, 1f);
        }

        @Override
        public QuaternionF32 normalize(QuaternionF32 quaternion, Float epsilon, QuaternionF32 fallback) {
            return epsilonEquals(quaternion, zero(), epsilon) ? fallback : normalize(quaternion);
        }

        @Override
        public QuaternionF32 nlerp(QuaternionF32 start, QuaternionF32 end, Float alpha) {
            return normalize(lerp(
                    start,
                    dot(start, end) < 0f ? negated(end) : end,
                    alpha
            ));
        }

        @Override
        public QuaternionF32 interpolate(QuaternionF32 start, QuaternionF32 end, Float alpha, UnaryOperator<Float> easing) {
            final float eased = easing.apply(alpha);
            final float inv = 1f - eased;
            return new QuaternionF32(
                    fma(start.x(), inv, end.x() * eased),
                    fma(start.y(), inv, end.y() * eased),
                    fma(start.z(), inv, end.z() * eased),
                    fma(start.w(), inv, end.w() * eased)
            );
        }

        @Override
        public boolean epsilonEquals(QuaternionF32 q1, QuaternionF32 q2, Float epsilon) {
            final var vec = abs(subtract(q1, q2));
            if (vec.x() > epsilon) return false;
            if (vec.y() > epsilon) return false;
            if (vec.z() > epsilon) return false;
            return vec.w() <= epsilon;
        }

        @Override
        public QuaternionF32 multiply(QuaternionF32 quaternion, Float scalar) {
            return multiplyHadamard(quaternion, of(scalar, scalar, scalar, scalar));
        }

        @Override
        public QuaternionF32 multiplyHadamard(QuaternionF32 op1, QuaternionF32 op2) {
            return of(
                    op1.x() * op2.x(),
                    op1.y() * op2.y(),
                    op1.z() * op2.z(),
                    op1.w() * op2.w()
            );
        }

        @Override
        public QuaternionF32 divideHadamard(QuaternionF32 op1, QuaternionF32 op2) {
            return of(
                    op1.x() / op2.x(),
                    op1.y() / op2.y(),
                    op1.z() / op2.z(),
                    op1.w() / op2.w()
            );
        }

        @Override
        public QuaternionF32 add(QuaternionF32 op1, QuaternionF32 op2) {
            return of(
                    op1.x() + op2.x(),
                    op1.y() + op2.y(),
                    op1.z() + op2.z(),
                    op1.w() + op2.w()
            );
        }

        @Override
        public QuaternionF32 subtract(QuaternionF32 op1, QuaternionF32 op2) {
            return of(
                    op1.x() - op2.x(),
                    op1.y() - op2.y(),
                    op1.z() - op2.z(),
                    op1.w() - op2.w()
            );
        }

        @Override
        public QuaternionF32 multiply(QuaternionF32 op1, QuaternionF32 op2) {
            return of(
                    fma(op1.w(), op2.x(), fma(op1.x(), op2.w(), fma(op1.y(), op2.z(), -op1.z() * op2.y()))),
                    fma(op1.w(), op2.y(), fma(op1.y(), op2.w(), fma(op1.z(), op2.x(), -op1.x() * op2.z()))),
                    fma(op1.w(), op2.z(), fma(op1.z(), op2.w(), fma(op1.x(), op2.y(), -op1.y() * op2.x()))),
                    fma(op1.w(), op2.w(),-fma(op1.x(), op2.x(),-fma(op1.y(), op2.y(),  op1.z() * op2.z())))
            );
        }

        @Override
        public QuaternionF32 divide(QuaternionF32 op1, QuaternionF32 op2) {
            return multiply(op1, invert(op2));
        }

        @Override
        public QuaternionF32 remainder(QuaternionF32 op1, QuaternionF32 op2) {
            return of(
                    op1.x() % op2.x(),
                    op1.y() % op2.y(),
                    op1.z() % op2.z(),
                    op1.w() % op2.w()
            );
        }

        @Override
        public QuaternionF32 negated(QuaternionF32 quaternion) {
            return of(
                    -quaternion.x(),
                    -quaternion.y(),
                    -quaternion.z(),
                    -quaternion.w()
            );
        }

        @Override
        public QuaternionF32 signum(QuaternionF32 quaternion) {
            return of(
                    Math.signum(quaternion.x()),
                    Math.signum(quaternion.y()),
                    Math.signum(quaternion.z()),
                    Math.signum(quaternion.w())
            );
        }

        @Override
        public QuaternionF32 sqrt(QuaternionF32 quaternion) {
            return of(
                    (float) Math.sqrt(quaternion.x()),
                    (float) Math.sqrt(quaternion.y()),
                    (float) Math.sqrt(quaternion.z()),
                    (float) Math.sqrt(quaternion.w())
            );
        }

        @Override
        public QuaternionF32 ceil(QuaternionF32 quaternion) {
            return of(
                    (float) Math.ceil(quaternion.x()),
                    (float) Math.ceil(quaternion.y()),
                    (float) Math.ceil(quaternion.z()),
                    (float) Math.ceil(quaternion.w())
            );
        }

        @Override
        public QuaternionF32 floor(QuaternionF32 quaternion) {
            return of(
                    (float) Math.floor(quaternion.x()),
                    (float) Math.floor(quaternion.y()),
                    (float) Math.floor(quaternion.z()),
                    (float) Math.floor(quaternion.w())
            );
        }

        @Override
        public QuaternionF32 lerp(QuaternionF32 start, QuaternionF32 end, Float alpha) {
            return interpolate(start, end, alpha, UnaryOperator.identity());
        }

        @Override
        public QuaternionF32 abs(QuaternionF32 quaternion) {
            return of(
                    Math.abs(quaternion.x()),
                    Math.abs(quaternion.y()),
                    Math.abs(quaternion.z()),
                    Math.abs(quaternion.w())
            );
        }

        private QuaternionF32 withMagnitude(QuaternionF32 quat, float wanted, float current) {
            return multiply(quat, wanted / current);
        }
    }
}
