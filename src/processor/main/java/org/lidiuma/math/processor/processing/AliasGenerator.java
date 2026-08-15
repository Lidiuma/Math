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

import org.lidiuma.math.processor.Alias;
import org.lidiuma.math.processor.FactoryAlias;
import org.lidiuma.math.processor.NamedAlias;
import org.lidiuma.math.processor.dsl.AccessModifier;
import org.lidiuma.math.processor.dsl.JavaDSL;
import javax.lang.model.element.*;
import javax.lang.model.type.*;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.util.*;

public final class AliasGenerator {

    // Class generation information.
    private final SequencedSet<String> imports = new LinkedHashSet<>();
    private final SequencedMap<AliasType, SequencedSet<ExecutableElement>> methods = new LinkedHashMap<>();
    private final Utility util;
    private final String package_;
    private final String class_;

    public AliasGenerator(Utility util, String package_, String class_) {
        this.util = Objects.requireNonNull(util);
        this.package_ = Objects.requireNonNull(package_);
        this.class_ = Objects.requireNonNull(class_);
    }

    private static boolean isNonAccessible(TypeElement ignored, ExecutableElement element) {
        return !element.getModifiers().contains(Modifier.PUBLIC);
    }

    public void addMethods(FactoryAlias factory, Element element) {

        if (!(element instanceof TypeElement typeElement)) throw new IllegalArgumentException("Invalid element.");

        final var alias = new AliasType.Constructor(typeElement, factory.methodName());
        addMethodsAlias(alias, util.constructorsOfType(typeElement));
    }

    public void addMethods(Alias ignored, Element element) { // Ignored since I only use it as a description of "element must be field".
        if (!(element instanceof VariableElement varElement)) throw new IllegalArgumentException("Invalid @GenerateAlias element.");
        final var type = new AliasType.Field(varElement);
        addMethodsAlias(type, util.methodsOfField(varElement));
    }

    public void build() throws IOException {

        final String fullName = package_ + "." + class_;
        final JavaFileObject sourceFile = util.processingEnv().getFiler().createSourceFile(fullName);

        try (var writer = sourceFile.openWriter()) {

            final var dsl = JavaDSL.class_().package_(package_).access(AccessModifier.PUBLIC);

            // Methods first to discover the imports.
            for (var entry : methods.entrySet()) {

                final var annotated = entry.getKey();
                final var methods = entry.getValue();
                for (var method : methods) {
                    dsl.addMethod(createMethod(annotated, method));
                }
            }

            for (var import_ : imports) dsl.addImport(import_, false);
            dsl.name(class_);
            writer.write(dsl.toString());
        }
    }

    /// @param aliasType used for the method resolution.
    /// @param methods the methods to compile.
    @SuppressWarnings("UnusedReturnValue")
    private AliasGenerator addMethodsAlias(AliasType aliasType, SequencedSet<ExecutableElement> methods) {
        Objects.requireNonNull(aliasType);
        Objects.requireNonNull(methods);
        this.methods.put(aliasType, methods);
        return this;
    }

    private String createMethod(AliasType aliasType, ExecutableElement method) {

        final var arguments = method.getParameters();
        final var declared = (DeclaredType) aliasType.annotated().asType();
        final TypeMirror returnType = switch (aliasType) {
            case AliasType.Constructor(var annotated, _) -> annotated.asType();
            case AliasType.Field _ -> method.getReturnType();
        };
        final NamedAlias named = method.getAnnotation(NamedAlias.class);
        final String methodName = switch (aliasType) {
            // In case the annotation is present, it overrides the default name.
            case AliasType.Constructor _, AliasType.Field _ when named != null -> named.methodName();
            case AliasType.Constructor(_, var name) -> name;
            case AliasType.Field _ -> method.getSimpleName().toString();
        };

        // Constructors method names are <init>, so I fix it for the correct Javadoc format.
        final String signature = util.erasedMethodSignature(method).replaceAll("<init>", declared.asElement().getSimpleName().toString());
        final var methodDsl = JavaDSL.method()
                .documentation("Generated alias of [" + declared.asElement() + "#" + signature + "].")
                .access(AccessModifier.PUBLIC)
                .static_(true)
                .return_(retrieveClass(declared, returnType))
                .name(methodName);

        final var callDsl = JavaDSL.methodCall();
        switch (aliasType) {
            // Full package name not needed, since the generate class is created within the package of the annotated one.
            case AliasType.Field(var annotated) -> callDsl.type(annotated.getEnclosingElement().getSimpleName().toString())
                    .instance(annotated.getSimpleName().toString())
                    .name(method.getSimpleName().toString());
            case AliasType.Constructor(var annotated, _) -> callDsl.type(annotated.getSimpleName().toString());
        }

        for (var argument : arguments) {
            final String type = retrieveClass(declared, argument.asType());
            final String name = argument.getSimpleName().toString();
            methodDsl.addParameter(type, name);
            callDsl.addParameter(name);
        }

        final String returnStr = returnType.getKind() == TypeKind.VOID ? "" : "return ";
        return methodDsl.body(returnStr + callDsl).toString();
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

        // I import the missing class.
        if (elementPackage.startsWith("java")) imports.addFirst(element.toString());
        else imports.addLast(element.toString());
    }

    private String retrieveClass(DeclaredType declaredType, TypeMirror type) {
        final TypeMirror specialized = util.specializedOfGeneric(declaredType, type);
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
                        sb.append(retrieveClass(declaredType, args.get(i)));
                    }
                    sb.append(">");
                }
                yield sb.toString();
            }
            case ARRAY -> retrieveClass(declaredType, ((ArrayType) specialized).getComponentType()) + "[]";
            case WILDCARD -> {
                final var wildcard = (WildcardType) specialized;
                if (wildcard.getExtendsBound() != null) yield "? extends " + retrieveClass(declaredType, wildcard.getExtendsBound());
                if (wildcard.getSuperBound() != null) yield "? super " + retrieveClass(declaredType, wildcard.getSuperBound());
                yield "?";
            }
            default -> specialized.toString();
        };
    }

    private sealed interface AliasType {

        Element annotated();

        record Field(VariableElement annotated) implements AliasType {}

        record Constructor(TypeElement annotated, String methodName) implements AliasType {}
    }
}
