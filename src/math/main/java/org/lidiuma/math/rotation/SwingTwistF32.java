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

import jdk.internal.vm.annotation.NullRestricted;
import org.lidiuma.math.api.rotation.SwingTwist;
import org.lidiuma.math.processor.NamedAlias;

@NamedAlias(methodName = "swingTwist", outputClass = "Rotations")
public value record SwingTwistF32(
        @Override @NullRestricted QuaternionF32 swing,
        @Override @NullRestricted QuaternionF32 twist
) implements SwingTwist<QuaternionF32, Float> {
}
