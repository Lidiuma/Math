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

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.SequencedSet;

public final class ClassDSL {

    private String package_ = null;
    private final SequencedSet<String> imports = new LinkedHashSet<>();
    private AccessModifier access = AccessModifier.DEFAULT;
    private String name = null;
    private final SequencedSet<String> methods = new LinkedHashSet<>();

    public ClassDSL package_(String package_) {
        this.package_ = Objects.requireNonNull(package_).trim();
        return this;
    }

    public ClassDSL addImport(String import_, boolean isStatic) {
        imports.add((isStatic ? "static " : "") + Objects.requireNonNull(import_).trim());
        return this;
    }

    public ClassDSL access(AccessModifier access) {
        this.access = Objects.requireNonNull(access);
        if (access == AccessModifier.PRIVATE) throw new IllegalArgumentException("Cannot make a private class.");
        return this;
    }

    public ClassDSL name(String name) {
        this.name = Objects.requireNonNull(name).trim();
        return this;
    }

    public ClassDSL addMethod(String method) {
        methods.add(Objects.requireNonNull(method).trim());
        return this;
    }

    public String syntax() {

        Objects.requireNonNull(package_, "Package is not optional.");
        Objects.requireNonNull(name, "Name is not optional.");

        final var builder = new StringBuilder()
                .append("package ").append(package_).append(";\n");

        for (var import_ : imports) {
            builder.append("\n").append("import ").append(import_).append(";");
        }

        final var constructor = ("\nprivate " + name + "() {}").indent(JavaDSL.INDENT);
        builder.append("\n\n").append(access).append(" final class ").append(name).append(" {\n")
                .append(constructor);

        for (var method : methods) {
            builder.append("\n").append(method.indent(JavaDSL.INDENT));
        }
        builder.append("}\n");
        return builder.toString();
    }
}
