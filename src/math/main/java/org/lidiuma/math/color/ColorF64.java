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

package org.lidiuma.math.color;

import jdk.internal.vm.annotation.NullRestricted;
import org.lidiuma.math.api.color.Color;
import org.lidiuma.math.api.traits.color.ColorOps;
import org.lidiuma.math.api.tuple.UnaryTuple4;
import org.lidiuma.math.processor.FactoryAlias;
import org.lidiuma.math.processor.FieldAlias;
import org.lidiuma.math.processor.MethodAlias;
import org.lidiuma.math.processor.NamedAlias;
import org.lidiuma.math.rotation.AngleF64;
import org.lidiuma.math.vector.Vec4F64;
import java.util.function.UnaryOperator;
import static org.lidiuma.math.internal.AnnotationConst.*;

@FactoryAlias(methodName = COLOR_FACTORY, outputClass = COLOR_OUT)
public value record ColorF64(
        @Override @NullRestricted Double red,
        @Override @NullRestricted Double green,
        @Override @NullRestricted Double blue,
        @Override @NullRestricted Double alpha
) implements Color<Double> {

    @FieldAlias(outputClass = COLOR_OUT)
    public static final ColorF64.Ops OPS = new ColorF64.Ops();

    @NamedAlias(methodName = COLOR_FACTORY + F64)
    public ColorF64(UnaryTuple4<Double> tuple) {
        this(tuple.x(), tuple.y(), tuple.z(), tuple.w());
    }

    /// Extracts the `R`, `G`, `B`, and `A` channels from a packed `0xRRGGBBAA` integer.
    /// @return the extracted colors.
    @MethodAlias(outputClass = COLOR_OUT)
    @NamedAlias(methodName = COLOR_FACTORY + F64 + "Rgba")
    public static ColorF64 colorRgba(int rgba) {
        return colorRgba(
                rgba >>> 24,
                rgba >>> 16 & 0xFF,
                rgba >>> 8 & 0xFF,
                rgba & 0xFF
        );
    }

    /// Converts the color channels from an int value in the range `[0,255]` to a double value in the range `[0,1]`.\
    /// Providing integers above `255` is allowed, but the resulting value will be above `1`.
    /// @return the color with the channels normalized by dividing each value by `255`.
    @MethodAlias(outputClass = COLOR_OUT)
    @NamedAlias(methodName = COLOR_FACTORY + F64 + "Rgba")
    public static ColorF64 colorRgba(int red, int green, int blue, int alpha) {
        return new ColorF64(
                red / 255d,
                green / 255d,
                blue / 255d,
                alpha / 255d
        );
    }

    /// Converts the color channels from a double value in the range `[0,255]` to a double value in the range `[0,1]`.\
    /// Providing doubles above `255` is allowed, but the resulting value will be above `1`.
    /// @return the color with the channels normalized by dividing each value by `255`.
    @MethodAlias(outputClass = COLOR_OUT)
    @NamedAlias(methodName = COLOR_FACTORY + F64 + "Rgba") // No collision, but consistency.
    public static ColorF64 colorRgba(double red, double green, double blue, double alpha) {
        return new ColorF64(
                red / 255d,
                green / 255d,
                blue / 255d,
                alpha / 255d
        );
    }

    /// Converts a hex string to a color.\
    /// The hex can contain `#`, which will simply be ignored,
    ///  and it must provide either `RRGGBB` or `RRGGBBAA` in hexadecimal format.
    @MethodAlias(outputClass = COLOR_OUT)
    @NamedAlias(methodName = COLOR_FACTORY + F64 + "Hex")
    public static ColorF64 colorHex(String hexColor) {
        final String hex = hexColor.startsWith("#") ? hexColor.substring(1) : hexColor;
        final int length = hex.length();
        if (length != 6 && length != 8) throw new IllegalArgumentException("The hex length can either be 6 or 8, provided: " + hex + ".");
        return colorRgba(Integer.parseUnsignedInt(hex + (length == 6 ? "FF" : ""), 16));
    }

    /// Converts the HSV to a color.
    /// @param hue the angle of the hue, any angle is allowed.
    /// @param saturation the saturation in the range [0,1].
    /// @param value the value in the range [0,1].
    /// @return the converted color.
    @MethodAlias(outputClass = COLOR_OUT)
    @NamedAlias(methodName = COLOR_FACTORY + F64 + "Hsv")
    public static ColorF64 colorHsv(AngleF64 hue, double saturation, double value) {

        if (value == 0d) return new ColorF64(0d, 0d, 0d, 1d); // Black.
        if (saturation == 0d) return new ColorF64(value, value, value, 1d); // Grayscale.

        // Normalizes hue into one of 6 zones.
        final double x = AngleF64.normalize(hue).radian() / (Math.TAU / 6d);
        final int zone = (int) x;
        final double remainder = x - zone;

        final double p = value * (1d - saturation);
        final double q = value * (1d - saturation * remainder);
        final double t = value * (1d - saturation * (1d - remainder));

        return switch (zone) {
            case 0 -> new ColorF64(value, t, p, 1d); // Red.
            case 1 -> new ColorF64(q, value, p, 1d); // Yellow.
            case 2 -> new ColorF64(p, value, t, 1d); // Green.
            case 3 -> new ColorF64(p, q, value, 1d); // Cyan.
            case 4 -> new ColorF64(t, p, value, 1d); // Blue.
            case 5 -> new ColorF64(value, p, q, 1d); // Magenta.
            default -> throw new AssertionError("Illegal zone.");
        };
    }

    /// Converts the HSL to a color.
    /// @param hue the angle of the hue, any angle is allowed.
    /// @param saturation the saturation in the range [0,1].
    /// @param lightness the lightness in the range [0,1].
    /// @return the converted color.
    @MethodAlias(outputClass = COLOR_OUT)
    @NamedAlias(methodName = COLOR_FACTORY + F64 + "Hsl")
    public static ColorF64 colorHsl(AngleF64 hue, double saturation, double lightness) {

        if (lightness == 0d) return new ColorF64(0d, 0d, 0d, 1d); // Black.
        if (saturation == 0d) return new ColorF64(lightness, lightness, lightness, 1d); // Grayscale.

        // Normalizes hue into one of 6 zones.
        final double x = AngleF64.normalize(hue).radian() / (Math.TAU / 6d);
        final int zone = (int) x;
        final double remainder = x - zone;

        final double chroma = (1d - Math.abs(2d * lightness - 1d)) * saturation;
        final double min = lightness - chroma / 2d;
        final double max = lightness + chroma / 2d;
        final double q = max - chroma * remainder;
        final double t = min + chroma * remainder;

        return switch (zone) {
            case 0 -> new ColorF64(max, t, min, 1d); // Red.
            case 1 -> new ColorF64(q, max, min, 1d); // Yellow.
            case 2 -> new ColorF64(min, max, t, 1d); // Green.
            case 3 -> new ColorF64(min, q, max, 1d); // Cyan.
            case 4 -> new ColorF64(t, min, max, 1d); // Blue.
            case 5 -> new ColorF64(max, min, q, 1d); // Magenta.
            default -> throw new AssertionError("Illegal zone.");
        };
    }

    public static final value class Ops implements ColorOps<ColorF64, Double> {

        // I use Vec4.Ops to avoid re-doing the math, which is error-prone.

        private Ops() {}

        private Vec4F64 v(ColorF64 c) {
            return new Vec4F64(c.red(), c.green(), c.blue(), c.alpha());
        }

        private ColorF64 c(Vec4F64 v) {
            return new ColorF64(v.x(), v.y(), v.z(), v.w());
        }

        @Override
        public ColorF64 clamp(ColorF64 value, ColorF64 min, ColorF64 max) {
            return c(Vec4F64.OPS.clamp(v(value), v(min), v(max)));
        }

        @Override
        public ColorF64 interpolate(ColorF64 start, ColorF64 end, Double alpha, UnaryOperator<Double> easing) {
            return c(Vec4F64.OPS.interpolate(v(start), v(end), alpha, easing));
        }
    }
}
