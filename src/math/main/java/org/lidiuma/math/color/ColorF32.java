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
import org.lidiuma.math.rotation.AngleF32;
import org.lidiuma.math.vector.Vec4F32;
import java.util.function.UnaryOperator;
import static org.lidiuma.math.internal.AnnotationConst.*;

@FactoryAlias(methodName = COLOR_FACTORY, outputClass = COLOR_OUT)
public value record ColorF32(
        @Override @NullRestricted Float red,
        @Override @NullRestricted Float green,
        @Override @NullRestricted Float blue,
        @Override @NullRestricted Float alpha
) implements Color<Float> {

    @FieldAlias(outputClass = COLOR_OUT)
    public static final Ops OPS = new ColorF32.Ops();

    @NamedAlias(methodName = COLOR_FACTORY + F32)
    public ColorF32(UnaryTuple4<Float> tuple) {
        this(tuple.x(), tuple.y(), tuple.z(), tuple.w());
    }

    /// Extracts the `R`, `G`, `B`, and `A` channels from a packed `0xRRGGBBAA` integer.
    /// @return the extracted colors.
    @MethodAlias(outputClass = COLOR_OUT)
    @NamedAlias(methodName = COLOR_FACTORY + F32 + "Rgba")
    public static ColorF32 colorRgba(int rgba) {
        return colorRgba(
                rgba >>> 24,
                rgba >>> 16 & 0xFF,
                rgba >>> 8 & 0xFF,
                rgba & 0xFF
        );
    }

    /// Converts the color channels from an int value in the range `[0,255]` to a float value in the range `[0,1]`.\
    /// Providing integers above `255` is allowed, but the resulting value will be above `1`.
    /// @return the color with the channels normalized by dividing each value by `255`.
    @MethodAlias(outputClass = COLOR_OUT)
    @NamedAlias(methodName = COLOR_FACTORY + F32 + "Rgba")
    public static ColorF32 colorRgba(int red, int green, int blue, int alpha) {
        return new ColorF32(
                  red / 255f,
                green / 255f,
                 blue / 255f,
                alpha / 255f
        );
    }

    /// Converts the color channels from a float value in the range `[0,255]` to a float value in the range `[0,1]`.\
    /// Providing floats above `255` is allowed, but the resulting value will be above `1`.
    /// @return the color with the channels normalized by dividing each value by `255`.
    @MethodAlias(outputClass = COLOR_OUT)
    @NamedAlias(methodName = COLOR_FACTORY + F32 + "Rgba") // No collision, but consistency.
    public static ColorF32 colorRgba(float red, float green, float blue, float alpha) {
        return new ColorF32(
                red / 255f,
                green / 255f,
                blue / 255f,
                alpha / 255f
        );
    }

    /// Converts a hex string to a color.\
    /// The hex can contain `#`, which will simply be ignored,
    ///  and it must provide either `RRGGBB` or `RRGGBBAA` in hexadecimal format.
    @MethodAlias(outputClass = COLOR_OUT)
    @NamedAlias(methodName = COLOR_FACTORY + F32 + "Hex")
    public static ColorF32 colorHex(String hexColor) {
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
    @NamedAlias(methodName = COLOR_FACTORY + F32 + "Hsv")
    public static ColorF32 colorHsv(AngleF32 hue, float saturation, float value) {

        if (value == 0f) return new ColorF32(0f, 0f, 0f, 1f); // Black.
        if (saturation == 0f) return new ColorF32(value, value, value, 1f); // Grayscale.

        // Normalizes hue into one of 6 zones.
        final float x = (float) (AngleF32.normalize(hue).radian() / (Math.TAU / 6f));
        final int zone = (int) x;
        final float remainder = x - zone;

        final float p = value * (1f - saturation);
        final float q = value * (1f - saturation * remainder);
        final float t = value * (1f - saturation * (1f - remainder));

        return switch (zone) {
            case 0 -> new ColorF32(value, t, p, 1f); // Red.
            case 1 -> new ColorF32(q, value, p, 1f); // Yellow.
            case 2 -> new ColorF32(p, value, t, 1f); // Green.
            case 3 -> new ColorF32(p, q, value, 1f); // Cyan.
            case 4 -> new ColorF32(t, p, value, 1f); // Blue.
            case 5 -> new ColorF32(value, p, q, 1f); // Magenta.
            default -> throw new AssertionError("Illegal zone.");
        };
    }

    /// Converts the HSL to a color.
    /// @param hue the angle of the hue, any angle is allowed.
    /// @param saturation the saturation in the range [0,1].
    /// @param lightness the lightness in the range [0,1].
    /// @return the converted color.
    @MethodAlias(outputClass = COLOR_OUT)
    @NamedAlias(methodName = COLOR_FACTORY + F32 + "Hsl")
    public static ColorF32 colorHsl(AngleF32 hue, float saturation, float lightness) {

        if (lightness == 0f) return new ColorF32(0f, 0f, 0f, 1f); // Black.
        if (saturation == 0f) return new ColorF32(lightness, lightness, lightness, 1f); // Grayscale.

        // Normalizes hue into one of 6 zones.
        final float x = (float) (AngleF32.normalize(hue).radian() / (Math.TAU / 6f));
        final int zone = (int) x;
        final float remainder = x - zone;

        final float chroma = (1f - Math.abs(2f * lightness - 1f)) * saturation;
        final float min = lightness - chroma / 2f;
        final float max = lightness + chroma / 2f;
        final float q = max - chroma * remainder;
        final float t = min + chroma * remainder;

        return switch (zone) {
            case 0 -> new ColorF32(max, t, min, 1f); // Red.
            case 1 -> new ColorF32(q, max, min, 1f); // Yellow.
            case 2 -> new ColorF32(min, max, t, 1f); // Green.
            case 3 -> new ColorF32(min, q, max, 1f); // Cyan.
            case 4 -> new ColorF32(t, min, max, 1f); // Blue.
            case 5 -> new ColorF32(max, min, q, 1f); // Magenta.
            default -> throw new AssertionError("Illegal zone.");
        };
    }

    public static final value class Ops implements ColorOps<ColorF32, Float> {

        // I use Vec4.Ops to avoid re-doing the math, which is error-prone.

        private Ops() {}

        private Vec4F32 v(ColorF32 c) {
            return new Vec4F32(c.red(), c.green(), c.blue(), c.alpha());
        }

        private ColorF32 c(Vec4F32 v) {
            return new ColorF32(v.x(), v.y(), v.z(), v.w());
        }

        @Override
        public ColorF32 clamp(ColorF32 value, ColorF32 min, ColorF32 max) {
            return c(Vec4F32.OPS.clamp(v(value), v(min), v(max)));
        }

        @Override
        public ColorF32 interpolate(ColorF32 start, ColorF32 end, Float alpha, UnaryOperator<Float> easing) {
            return c(Vec4F32.OPS.interpolate(v(start), v(end), alpha, easing));
        }
    }
}
