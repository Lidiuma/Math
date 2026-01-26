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

package org.lidiuma.math.numeric;

import java.util.List;

public abstract value class Decimal {

    @SuppressWarnings("preview")
    // TODO This becomes `LazyConstant` in second preview.
    private static final List<Long> SCALES = StableValue.list(18, i -> (long) Math.pow(10, i)); // 18 is the max power of 10 that can fit within a long.

    public static long scaleOfDecimal(byte decimals) {
        return SCALES.get(decimals);
    }

    public abstract long scale();
}
