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
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.*;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.SequencedSet;

public final class AliasClassGenerator {

    private static final String CLASS_SOURCE = """
            package %s;
            %s
            
            public final class %s {
            
                private %s() {}%s
            }
            """; // The methods are near the constructor to have the correct new line.
    private static final String METHOD_SOURCE = """
            \n
                public static %s %s(%s) {
                    %s
                }\
            """;
    private static final String METHOD_CALL = "%s%s.%s.%s(%s);";
    private DeclaredType declared;
    private Utility util;
    // Class generation information.
    private final SequencedSet<String> imports = new LinkedHashSet<>();
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

        this.util = Objects.requireNonNull(util);
        this.declared = (DeclaredType) Objects.requireNonNull(annotated).asType();
        if (package_ == null) throw new IllegalArgumentException("The package name is required.");
        if (class_ == null) throw new IllegalArgumentException("The class name is required.");

        final StringBuilder methods = new StringBuilder();
        for (var method : this.methods) {
            methods.append(createMethod(annotated, method));
        }

        final String fullName = package_ + "." + class_;
        final JavaFileObject sourceFile = util.processingEnv().getFiler().createSourceFile(fullName);

        try (var writer = sourceFile.openWriter()) {
            final var source = String.format(CLASS_SOURCE,
                    package_,
                    "\n" + String.join("\n", this.imports),
                    class_,
                    class_,
                    methods
            );
            writer.write(source);
        }
    }

    private String createMethod(VariableElement annotated, ExecutableElement method) {

        final var returnType = method.getReturnType();
        final var arguments = method.getParameters();

        final StringBuilder argumentsLong = new StringBuilder();
        final StringBuilder argumentsShort = new StringBuilder();
        for (var argument : arguments) {
            final String type = retrieveClass(argument.asType());
            argumentsLong.append(String.format("%s %s, ", type, argument.getSimpleName()));
            argumentsShort.append(argument.getSimpleName()).append(", ");
        }
        if (!arguments.isEmpty()) {
            argumentsLong.delete(argumentsLong.length() - 2, argumentsLong.length());
            argumentsShort.delete(argumentsShort.length() - 2, argumentsShort.length());
        }

        final String methodCall = String.format(METHOD_CALL,
                returnType.getKind() == TypeKind.VOID ? "" : "return ",
                // Full package name not needed, since the generate class is created within the package of the annotated one.
                annotated.getEnclosingElement().getSimpleName(),
                annotated.getSimpleName(),
                method.getSimpleName(),
                argumentsShort);

        return String.format(METHOD_SOURCE,
                retrieveClass(returnType),
                method.getSimpleName(),
                argumentsLong,
                methodCall);
    }

    /// Imports the necessary classes and returns the class name.
    private void verifyImports(TypeMirror type) {

        if (type.getKind() != TypeKind.DECLARED) return;

        final var elements = util.processingEnv().getElementUtils();
        final var element = (TypeElement) ((DeclaredType) type).asElement();
        final String elementPackage = elements.getPackageOf(element).toString();

        // Same package, I don't need the full qualified name.
        if (elementPackage.equals(package_)) return;
        // Java lang classes are always imported.
        if (elementPackage.equals("java.lang")) return;

        final String import_ = "import " + element + ";";
        // I import the missing class.
        if (elementPackage.startsWith("java")) imports.addFirst(import_);
        else imports.addLast(import_);
    }

    private String retrieveClass(TypeMirror type) {
        final TypeMirror specialized = util.specializedOfGeneric(declared, type);
        return switch (specialized.getKind()) {
            case DECLARED -> {

                verifyImports(specialized);

                final var declared = (DeclaredType) specialized;
                final var name = declared.asElement().getSimpleName().toString();
                final var sb = new StringBuilder(name);

                final var args = declared.getTypeArguments();
                if (!args.isEmpty()) {
                    sb.append("<");
                    for (int i = 0; i < args.size(); i++) {
                        if (i > 0) sb.append(", ");
                        sb.append(retrieveClass(args.get(i)));
                    }
                    sb.append(">");
                }
                yield sb.toString();
            }
            case ARRAY -> retrieveClass(((ArrayType) specialized).getComponentType()) + "[]";
            case WILDCARD -> {
                final var wildcard = (WildcardType) specialized;
                if (wildcard.getExtendsBound() != null) yield "? extends " + retrieveClass(wildcard.getExtendsBound());
                if (wildcard.getSuperBound() != null) yield "? super " + retrieveClass(wildcard.getSuperBound());
                yield "?";
            }
            default -> specialized.toString();
        };
    }
}
