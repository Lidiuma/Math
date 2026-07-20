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

package org.lidiuma.math.processor;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.*;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.util.*;

public final class Processor extends AbstractProcessor {

    private Utility util;

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Set.of(GenerateAlias.class.getName());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        util = new Utility(processingEnv);
        if (roundEnv.processingOver()) return false;
        roundEnv.getElementsAnnotatedWith(GenerateAlias.class).forEach(this::hangleGenerateAlis);
        return true;
    }

    private void hangleGenerateAlis(Element element) {

        final String className = Objects.requireNonNull(element.getAnnotation(GenerateAlias.class)).className();
        if (className.isBlank()) throw new IllegalArgumentException("Class name cannot be empty. (GenerateAlias: " + className + ")");

        if (!(element instanceof VariableElement ve)) return;

        if (!(processingEnv.getTypeUtils().asElement(ve.asType()) instanceof TypeElement te)) return;

        final var types = util.interfacesOfType(te);
        types.add(te); // I also get the methods of the top class.

        final var annotatedDeclared = (DeclaredType) element.asType();

        final PackageElement packageElem = processingEnv.getElementUtils().getPackageOf(element);
        final String packageName = packageElem.getQualifiedName().toString();

        StringBuilder generated = new StringBuilder()
                .append("package ").append(packageName).append(";\n\n")
                .append("public value class ").append(className).append(" {");

        var methods = util.methodsOfTypes(annotatedDeclared, types, Set.of(Modifier.PUBLIC));
        methods.forEach(method -> {
            generated.append("\n\n    ").append(buildAliasMethod(element, method));
        });
        generated.append("\n}\n");

        JavaFileObject sourceFile;
        try {
             sourceFile = processingEnv.getFiler().createSourceFile(
                    packageName + "." + className
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (var writer = sourceFile.openWriter()) {
            writer.write(generated.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String buildAliasMethod(Element annotated, ExecutableElement method) {

        final var annotatedDeclared = (DeclaredType) annotated.asType();

        final var returnType = method.getReturnType();
        final String realReturn = util.specializedOfGeneric(annotatedDeclared, returnType);

        final StringBuilder codeArguments = new StringBuilder();
        final var arguments = method.getParameters();
        for (var argument : arguments) {

            final String real = util.specializedOfGeneric(annotatedDeclared, argument.asType());
            codeArguments.append(real)
                    .append(" ")
                    .append(argument.getSimpleName())
                    .append(", ");
        }
        if (!codeArguments.isEmpty()) codeArguments.delete(codeArguments.length() - 2, codeArguments.length());

        final StringBuilder methodCall = new StringBuilder(returnType.getKind() == TypeKind.VOID ? "    " : "    return ")
                .append(annotated.getEnclosingElement().toString())
                .append(".")
                .append(annotated.getSimpleName())
                .append(".")
                .append(method.getSimpleName())
                .append("(");
        for (var argument : arguments) {
            methodCall.append(argument.getSimpleName()).append(", ");
        }
        if (!arguments.isEmpty()) methodCall.delete(methodCall.length() - 2, methodCall.length());

        methodCall.append(");");

        return "public static " + realReturn +
                " " +
                method.getSimpleName() +
                "(" +
                codeArguments +
                ") {\n" +
                "    " +
                methodCall +
                "\n    }";
    }
}
