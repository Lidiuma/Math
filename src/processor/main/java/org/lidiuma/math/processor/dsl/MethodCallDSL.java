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

package org.lidiuma.math.processor.dsl;

import java.util.ArrayList;
import java.util.Objects;

public final class MethodCallDSL {

    private String type = "";
    private String field = "";
    private String name = "";
    private final ArrayList<String> parameters = new ArrayList<>(6);

    public MethodCallDSL type(String type) {
        this.type = Objects.requireNonNull(type).trim();
        return this;
    }

    /// The instance to where to call the method from.
    public MethodCallDSL instance(String field) {
        this.field = Objects.requireNonNull(field).trim();
        return this;
    }

    public MethodCallDSL name(String name) {
        this.name = Objects.requireNonNull(name).trim();
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public MethodCallDSL addParameter(String name) {
        parameters.add(Objects.requireNonNull(name).trim());
        return this;
    }

    @Override
    public String toString() {

        final boolean isConstructor = field.isEmpty() && name.isEmpty();
        if (isConstructor && type.isEmpty()) throw new IllegalArgumentException("Type is required for constructor call.");
        if (!isConstructor && name.isEmpty()) throw new IllegalArgumentException("Name is required for method call.");

        final String typeStr = type.isEmpty() ? "" : type + (isConstructor ? "" : ".");
        final String fieldStr = field.isEmpty() ? "" : (field + ".");

        final StringBuilder sb = new StringBuilder()
                .append(isConstructor ? "new " : "")
                .append(typeStr)
                .append(fieldStr)
                .append(name).append("(");
        for (var name : parameters) {
            sb.append(name).append(", ");
        }
        if (!parameters.isEmpty()) sb.delete(sb.length() - 2, sb.length());
        return sb.append(");").toString();
    }
}
