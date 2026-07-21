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

package org.lidiuma.math.processor.processing;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.SequencedSet;

public final class AliasClassGenerator {

    private static final String CLASS_SOURCE = """
            package %s;
            
            public final class %s {
            
                private %s() {}
                %s
            }
            """;
    private static final String METHOD_SOURCE = """
                public static %s %s(%s) {
                    %s
                }
            """;
    private static final String METHOD_CALL = "%s%s.%s.%s(%s);";
    private String package_;
    private String class_;
    private SequencedSet<ExecutableElement> methods = new LinkedHashSet<>();

    private AliasClassGenerator() {}

    public static AliasClassGenerator new_() {
        return new AliasClassGenerator();
    }

    public AliasClassGenerator packageName(String packageName) {
        this.package_ = packageName;
        return this;
    }

    public AliasClassGenerator className(String className) {
        this.class_ = className;
        return this;
    }

    public AliasClassGenerator methods(SequencedSet<ExecutableElement> methods) {
        this.methods = methods != null ? methods : new LinkedHashSet<>();
        return this;
    }

    public void build(VariableElement annotated, Utility util) throws IOException {

        Objects.requireNonNull(annotated);
        Objects.requireNonNull(util);
        if (package_ == null) throw new IllegalArgumentException("The package name is required.");
        if (class_ == null) throw new IllegalArgumentException("The class name is required.");

        final StringBuilder methods = new StringBuilder();
        for (var method : this.methods) {
            methods.append(createMethod(util, annotated, method));
        }

        final String fullName = package_ + "." + class_;
        final JavaFileObject sourceFile = util.processingEnv().getFiler().createSourceFile(fullName);

        try (var writer = sourceFile.openWriter()) {
            final var source = String.format(CLASS_SOURCE, package_, class_, class_, methods);
            writer.write(source);
        }
    }

    private String createMethod(Utility util, VariableElement annotated, ExecutableElement method) {

        final var annotatedDeclared = (DeclaredType) annotated.asType();

        final var returnType = method.getReturnType();
        final var arguments = method.getParameters();

        final StringBuilder argumentsLong = new StringBuilder();
        final StringBuilder argumentsShort = new StringBuilder();
        for (var argument : arguments) {
            final String type = util.specializedOfGeneric(annotatedDeclared, argument.asType());
            argumentsLong.append(String.format("%s %s, ", type, argument.getSimpleName()));
            argumentsShort.append(argument.getSimpleName()).append(", ");
        }
        if (!arguments.isEmpty()) {
            argumentsLong.delete(argumentsLong.length() - 2, argumentsLong.length());
            argumentsShort.delete(argumentsShort.length() - 2, argumentsShort.length());
        }

        final String methodCall = String.format(METHOD_CALL,
                returnType.getKind() == TypeKind.VOID ? "" : "return ",
                annotated.getEnclosingElement().toString(),
                annotated.getSimpleName(),
                method.getSimpleName(),
                argumentsShort);

        return String.format(METHOD_SOURCE,
                util.specializedOfGeneric(annotatedDeclared, returnType),
                method.getSimpleName(),
                argumentsLong,
                methodCall);
    }
}
