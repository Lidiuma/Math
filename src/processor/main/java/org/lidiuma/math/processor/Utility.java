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

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.*;
import java.util.*;

public record Utility(ProcessingEnvironment processingEnv) {


    /// @return All interfaces, direct or not, of the provided type.
    public SequencedSet<TypeElement> interfacesOfType(TypeElement typeElement) {

        final var result = new LinkedHashSet<TypeElement>();
        final var next = new LinkedHashSet<TypeElement>();
        next.add(typeElement);

        while (!next.isEmpty()) {

            final var node = next.removeFirst();
            final var interfaces = node.getInterfaces();
            for (var i : interfaces) {
                final var iType = (TypeElement) processingEnv.getTypeUtils().asElement(i);
                next.add(iType);
                result.add(iType);
            }
        }
        return result;
    }

    public SequencedSet<ExecutableElement> methodsOfTypes(DeclaredType declaredType, Collection<TypeElement> elements, Set<Modifier> modifiers) {

        final var result = new LinkedHashSet<ExecutableElement>();
        final var visited = new HashSet<String>();

        for (var element : elements) {

            final var methods = element.getEnclosedElements();
            for (var method : methods) {
                if (method.getKind() != ElementKind.METHOD) continue;
                // In case there are no modifiers that fit.
                if (method.getModifiers().stream().noneMatch(modifiers::contains)) continue;

                final var executable = (ExecutableElement) method;
                final var signature = methodSignature(declaredType, executable);
                if (visited.contains(signature)) continue;
                result.add(executable);
                visited.add(signature);
            }
        }
        return result;
    }

    /// @param declaredType original type to figure out generics.
    public String methodSignature(DeclaredType declaredType, ExecutableElement method) {
        final var builder = new StringBuilder(method.getSimpleName()).append("(");
        final var parameters = method.getParameters();
        for (var parameter : parameters) {
            final var type = specializedOfGeneric(declaredType, parameter.asType());
            builder.append(type).append(",");
        }
        if (!parameters.isEmpty()) builder.delete(builder.length() - 1, builder.length());
        builder.append(")");
        return builder.toString();
    }

    /// @param declaredType the type with the implementation of the generic type.
    /// @param typeMirror the generic type to discover the specialized type.
    /// @return the actual type used of the generic parameter.
    /// @apiNote if not generics, the normal type will be returned.
    public String specializedOfGeneric(DeclaredType declaredType, TypeMirror typeMirror) {
        return switch (typeMirror.getKind()) {
            case TYPEVAR -> {
                // Get the type variable name (e.g., "N", "V", "T")
                TypeVariable tv = (TypeVariable) typeMirror;
                String varName = tv.asElement().getSimpleName().toString();

                // Get concrete type via asMemberOf
                TypeMirror concrete = processingEnv.getTypeUtils().asMemberOf(declaredType, tv.asElement());
                yield concrete.toString();
            }
            case DECLARED -> {
                DeclaredType declared = (DeclaredType) typeMirror;
                TypeElement element = (TypeElement) declared.asElement();
                String baseName = element.getQualifiedName().toString();

                List<? extends TypeMirror> args = declared.getTypeArguments();
                if (args.isEmpty()) {
                    yield baseName;
                }

                // Recursively resolve each type argument
                StringBuilder sb = new StringBuilder(baseName).append("<");
                for (int i = 0; i < args.size(); i++) {
                    if (i > 0) sb.append(", ");
                    sb.append(specializedOfGeneric(declaredType, args.get(i)));
                }
                sb.append(">");
                yield sb.toString();
            }
            case ARRAY -> {
                ArrayType arr = (ArrayType) typeMirror;
                yield specializedOfGeneric(declaredType, arr.getComponentType()) + "[]";
            }
            case WILDCARD -> {
                WildcardType wildcard = (WildcardType) typeMirror;
                TypeMirror extendsBound = wildcard.getExtendsBound();
                if (extendsBound != null) {
                    yield "? extends " + specializedOfGeneric(declaredType, extendsBound);
                }
                TypeMirror superBound = wildcard.getSuperBound();
                if (superBound != null) {
                    yield "? super " + specializedOfGeneric(declaredType, superBound);
                }
                yield "?";
            }
            default -> typeMirror.toString();
        };
    }
}
