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
import jdk.internal.vm.annotation.NullRestricted;
import org.lidiuma.math.api.rotation.Quaternion;
import org.lidiuma.math.api.traits.rotation.QuaternionOps;
import org.lidiuma.math.api.tuple.UnaryTuple4;
import org.lidiuma.math.internal.Strict;
import org.lidiuma.math.numerics.DoubleNumeric;
import org.lidiuma.math.processor.AliasExclude;
import org.lidiuma.math.processor.FactoryAlias;
import org.lidiuma.math.processor.FieldAlias;
import org.lidiuma.math.processor.NamedAlias;
import org.lidiuma.math.vector.Vec3F64;
import static org.lidiuma.math.internal.AnnotationConst.*;

@FactoryAlias(methodName = QUATERNION_FACTORY, outputClass = ROTATION_OUT)
@LooselyConsistentValue
public value record QuaternionF64(
        @Override @NullRestricted Double x,
        @Override @NullRestricted Double y,
        @Override @NullRestricted Double z,
        @Override @NullRestricted Double w
) implements Quaternion<Double> {

    @FieldAlias(outputClass = ROTATION_OUT)
    public static final Ops OPS = new Ops();

    @NamedAlias(methodName = QUATERNION_FACTORY + F64)
    public QuaternionF64(UnaryTuple4<Double> v4) {
        this(v4.x(), v4.y(), v4.z(), v4.w());
    }

    public static final value class Ops implements QuaternionOps<QuaternionF64, Vec3F64, AngleF64, Double> {

        private Ops() {}

        @Override
        @AliasExclude
        public QuaternionF64 of(Double x, Double y, Double z, Double w) {
            return new QuaternionF64(x, y, z, w);
        }

        @Override
        public QuaternionF64 fromAxisAngle(Vec3F64 axis, AngleF64 angle) {
            final double half = angle.radian() * .5d;
            final double sin = Strict.sin(half);
            final double cos = Strict.cos(half);
            return of(
                    (axis.x() * sin),
                    (axis.y() * sin),
                    (axis.z() * sin),
                    cos
            );
        }

        @Override
        public QuaternionF64 fromEulerAngle(AngleF64 yaw, AngleF64 pitch, AngleF64 roll) {
            final double hr = roll.radian() * 0.5d;
            final double shr = Strict.sin(hr);
            final double chr = Strict.cos(hr);

            final double hp = pitch.radian() * 0.5d;
            final double shp = Strict.sin(hp);
            final double chp = Strict.cos(hp);

            final double hy = yaw.radian() * 0.5d;
            final double shy = Strict.sin(hy);
            final double chy = Strict.cos(hy);

            final double chyShp = chy * shp;
            final double shyChp = shy * chp;
            final double chyChp = chy * chp;
            final double shyShp = shy * shp;

            final double newX = (chyShp * chr) + (shyChp * shr); // cos(yaw/2) * sin(pitch/2) * cos(roll/2) + sin(yaw/2) * cos(pitch/2) * sin(roll/2)
            final double newY = (shyChp * chr) - (chyShp * shr); // sin(yaw/2) * cos(pitch/2) * cos(roll/2) - cos(yaw/2) * sin(pitch/2) * sin(roll/2)
            final double newZ = (chyChp * shr) - (shyShp * chr); // cos(yaw/2) * cos(pitch/2) * sin(roll/2) - sin(yaw/2) * sin(pitch/2) * cos(roll/2)
            final double newW = (chyChp * chr) + (shyShp * shr); // cos(yaw/2) * cos(pitch/2) * cos(roll/2) + sin(yaw/2) * sin(pitch/2) * sin(roll/2)
            return new QuaternionF64(newX, newY, newZ, newW);
        }

        @Override
        public QuaternionF64 fromRotationBetween(Vec3F64 vector1, Vec3F64 vector2) {

            final var vOps = Vec3F64.OPS;
            final double dot = Math.clamp(vOps.dot(vector1, vector2), -1d, 1d);

            // When the vectors are parallel.
            if (dot >= 1d) return identity();

            // If the vectors are antiparallel (dot == -1), rotate 180 degrees around an arbitrary perpendicular axis.
            if (dot <= -1d) {
                final var perpendicular = Math.abs(vector1.x()) < .9d ?
                        new Vec3F64(1d, 0d, 0d) :
                        new Vec3F64(0d, 1d, 0d);
                final var axis = vOps.cross(perpendicular, vector1);
                // xyz = axis * (sin(pi / 2) = 1), w = cos(pi / 2) = 0
                return of(axis.x(), axis.y(), axis.z(), 0d);
            }

            final Vec3F64 cross = vOps.cross(vector1, vector2);
            final double scale = Math.sqrt(2d * (1d + dot));
            return of(
                    cross.x() / scale,
                    cross.y() / scale,
                    cross.z() / scale,
                    (1d + dot) / scale
            );
        }

        @Override
        public QuaternionF64 exp(QuaternionF64 quaternion) {

            final var witness = Vec3F64.OPS;
            final var vectorQuat = new Vec3F64(quaternion.x(), quaternion.y(), quaternion.z());
            final double angle = witness.length(vectorQuat); // The math is the same.

            if (angle < Strict.EPSILON_F64) return identity();

            final double sin = Strict.sin(angle);
            final double cos = Strict.cos(angle);

            final double k = sin / angle;
            return of(
                    quaternion.x() * k,
                    quaternion.y() * k,
                    quaternion.z() * k,
                    cos
            );
        }

        @Override
        public QuaternionF64 log(QuaternionF64 quaternion) {

            final double w = Math.clamp(quaternion.w(), -1d, 1d);
            final double angle = Math.acos(w);
            final double sin = Math.sqrt(Math.max(0d, 1d - w * w));

            if (sin < Strict.EPSILON_F64) return of(quaternion.x(), quaternion.y(), quaternion.z(), 0d);

            final double k = angle / sin;
            return of(
                    quaternion.x() * k,
                    quaternion.y() * k,
                    quaternion.z() * k,
                    0d
            );
        }

        @Override
        public QuaternionF64 pow(QuaternionF64 quaternion, Double exponent) {
            return exp(multiply(log(quaternion), exponent));
        }

        @Override
        public QuaternionF64 slerp(QuaternionF64 start, QuaternionF64 end, Double alpha) {

            final double dot = dot(start, end);
            final double absDot = Math.abs(dot);
            final double sign = dot < 0d ? -1d : 1d;

            // To avoid numerical instability at low angles, I use nlerp.
            if (absDot > 0.9995f) return normalize(lerp(start, multiply(end, sign), alpha));

            final double angle = Math.acos(absDot);
            final double invSinTheta = 1d / Strict.sin(angle);

            final double scale0 = Strict.sin((1d - alpha) * angle) * invSinTheta;
            final double scale1 = Strict.sin((alpha * angle)) * invSinTheta;
            return add(multiply(start, scale0), multiply(end, sign * scale1));
        }

        @Override
        public Vec3F64 rotate(QuaternionF64 quaternion, Vec3F64 vector) {
            final var conjugate = conjugate(quaternion);
            final var vectorQuat = of(vector.x(), vector.y(), vector.z(), 0d);
            final var rotated = multiply(multiply(quaternion, vectorQuat), conjugate);
            return new Vec3F64(rotated.x(), rotated.y(), rotated.z());
        }

        @Override
        public Vec3F64 unrotate(QuaternionF64 quaternion, Vec3F64 vector) {
            final var conjugate = conjugate(quaternion);
            final var vectorQuat = of(vector.x(), vector.y(), vector.z(), 0d);
            final var unrotated = multiply(multiply(conjugate, vectorQuat), quaternion);
            return new Vec3F64(unrotated.x(), unrotated.y(), unrotated.z());
        }

        @Override
        public AxisAngleF64 axisAngle(QuaternionF64 quaternion, Double epsilon) {

            final double sqrt = Math.sqrt(1d - quaternion.w() * quaternion.w());
            final var angle = angle(quaternion);

            // I avoid dividing by 0 if the sqrt is small enough.
            if (sqrt < epsilon) {
                // I re-normalize because without w the length might no longer be 1.
                final var ws = Vec3F64.OPS;
                final var axis = ws.normalize(new Vec3F64(quaternion.x(), quaternion.y(), quaternion.z()));
                return new AxisAngleF64(axis, angle);
            }

            final var axis = new Vec3F64(
                    quaternion.x() / sqrt,
                    quaternion.y() / sqrt,
                    quaternion.z() / sqrt
            );
            return new AxisAngleF64(axis, angle);
        }

        @Override
        public AngleF64 angle(QuaternionF64 quaternion) {
            final double w = Math.clamp(quaternion.w(), -1d, 1d);
            return AngleF64.radians(2d * Math.acos(w));
        }

        @Override
        public SwingTwistF64 swingTwist(QuaternionF64 quaternion, Vec3F64 axis) {
            final var twist = twist(quaternion, axis);
            final var swing = multiply(quaternion, conjugate(twist));
            return new SwingTwistF64(swing, twist);
        }

        @Override
        public AngleF64 angleAround(QuaternionF64 quaternion, Vec3F64 axis) {
            return angle(twist(quaternion, axis));
        }

        private QuaternionF64 twist(QuaternionF64 quaternion, Vec3F64 axis) {

            final var witness = Vec3F64.OPS;
            final var vectorQuat = new Vec3F64(quaternion.x(), quaternion.y(), quaternion.z());
            final double dot = witness.dot(vectorQuat, axis);

            return normalize(of(
                    axis.x() * dot,
                    axis.y() * dot,
                    axis.z() * dot,
                    quaternion.w())
            );
        }

        @Override
        @NamedAlias(methodName = ZERO_FACTORY + F64)
        public QuaternionF64 zero() {
            return QuaternionOps.super.zero();
        }

        @Override
        @NamedAlias(methodName = ONE_FACTORY + F64)
        public QuaternionF64 one() {
            return QuaternionOps.super.one();
        }

        @Override
        @NamedAlias(methodName = IDENTITY_FACTORY + F64)
        public QuaternionF64 identity() {
            return QuaternionOps.super.identity();
        }

        @Override
        @AliasExclude
        public DoubleNumeric scalarOps() {
            return DoubleNumeric.OPS;
        }
    }
}
