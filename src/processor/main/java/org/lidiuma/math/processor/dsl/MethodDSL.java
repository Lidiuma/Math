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

import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.SequencedMap;
import java.util.stream.Collectors;

public final class MethodDSL {

    private String doc = "";
    private AccessModifier access = AccessModifier.PRIVATE;
    private boolean isStatic = true;
    private String returnType = "";
    private String name = null;
    // name -> type, since the name is the one that is unique, while the type can be used multiple times.
    private final SequencedMap<String, String> parameters = new LinkedHashMap<>();
    private String body = "";

    public MethodDSL documentation(String documentation) {
        this.doc = Objects.requireNonNull(documentation)
                .trim()
                .lines()
                .map(line -> "/// " + line)
                .collect(Collectors.joining("\n"));
        return this;
    }

    public MethodDSL access(AccessModifier access) {
        this.access = Objects.requireNonNull(access);
        return this;
    }

    public MethodDSL static_(boolean isStatic) {
        this.isStatic = isStatic;
        return this;
    }

    public MethodDSL return_(String type) {
        this.returnType = Objects.requireNonNull(type).trim();
        return this;
    }

    public MethodDSL name(String name) {
        this.name = Objects.requireNonNull(name).trim();
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public MethodDSL addParameter(String type, String name) {
        if (Objects.requireNonNull(type).isBlank()) throw new IllegalArgumentException("The parameter type cannot be empty.");
        if (Objects.requireNonNull(name).isBlank()) throw new IllegalArgumentException("The parameter value cannot be empty.");
        parameters.put(name.trim(), type.trim());
        return this;
    }

    public MethodDSL body(String body) {
        this.body = Objects.requireNonNull(body).trim();
        return this;
    }

    public String syntax() {

        Objects.requireNonNull(name, "Name is not optional.");
        if (body.isBlank() && !returnType.isEmpty()) throw new IllegalArgumentException("Empty body when return type expected.");

        final String accessStr = access.toString() + (access.toString().isBlank() ? "" : " ");
        final String staticStr = isStatic ? "static " : "";
        final String returnStr = (returnType.isBlank() ? "void" : returnType) + " ";

        final StringBuilder result = new StringBuilder()
                .append(doc).append("\n")
                .append(accessStr)
                .append(staticStr)
                .append(returnStr)
                .append(name).append("(");

        for (var entry : parameters.entrySet()) {
            // No it's not inverted, type is indeed the value.
            final String type = entry.getValue();
            final String name = entry.getKey();
            result.append(type).append(" ").append(name).append(", ");
        }
        // Removes the last ", "
        if (!parameters.isEmpty()) result.delete(result.length() - 2, result.length());

        return result.append(") {\n")
                .append(body.indent(4))
                .append("}")
                .toString();
    }
}
