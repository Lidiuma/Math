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

import org.lidiuma.math.processor.AliasExclude;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.*;
import javax.lang.model.util.Types;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public record Utility(ProcessingEnvironment processingEnv) {

    /// @return All interfaces, direct or not, of the provided type.
    public SequencedSet<TypeElement> typeHierarchy(TypeElement typeElement) {

        final var result = new LinkedHashSet<TypeElement>();
        final var next = new LinkedHashSet<TypeElement>();
        result.add(typeElement);
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

    /// Collects the methods from the types provided.
    /// @param types the types to retrieve the methods from.
    /// @param filter the predicate, returns true to not put the method into the final result.
    /// @return the filtered methods.
    public SequencedMap<TypeElement, SequencedSet<ExecutableElement>> methods(SequencedSet<TypeElement> types, BiPredicate<TypeElement, ExecutableElement> filter) {

        final var result = new LinkedHashMap<TypeElement, SequencedSet<ExecutableElement>>();
        for (var type : types) {

            final var methods = type.getEnclosedElements();
            for (var method : methods) {
                if (method.getKind() != ElementKind.METHOD) continue;
                final var executable = (ExecutableElement) method;

                if (filter.test(type, executable)) continue;
                result.computeIfAbsent(type, _ -> new LinkedHashSet<>()).add(executable);
            }
        }
        return result.reversed();
    }

    /// @return all the methods of the types.
    @SuppressWarnings("unused")
    public SequencedMap<TypeElement, SequencedSet<ExecutableElement>> allMethods(SequencedSet<TypeElement> types) {
        return methods(types, (_, _) -> false);
    }

    /// @return all the base methods, the overwritten methods are removed.
    public SequencedMap<TypeElement, SequencedSet<ExecutableElement>> baseMethods(TypeElement typeElement) {

        final var visited = new LinkedHashSet<String>();
        final var types = typeHierarchy(typeElement).reversed();

        return methods(types, (_, method) -> {
            final var signature = methodSignature((DeclaredType) typeElement.asType(), method);
            if (visited.contains(signature)) return true; // I filter it.
            visited.add(signature);
            return false;
        });
    }

    /// @return the most specialized methods if it overrides a base method, otherwise the base method.
    public SequencedMap<TypeElement, SequencedSet<ExecutableElement>> specializedMethods(TypeElement typeElement) {

        final var visited = new LinkedHashSet<String>();
        final var types = typeHierarchy(typeElement);

        return methods(types, (_, method) -> {
            final var signature = methodSignature((DeclaredType) typeElement.asType(), method);
            if (visited.contains(signature)) return true; // I filter it.
            visited.add(signature);
            return false;
        }).reversed();
    }

    public SequencedSet<ExecutableElement> methodsOfField(VariableElement element) {

        if (!element.getModifiers().contains(Modifier.STATIC)) throw new IllegalArgumentException("The field \"" + element.getSimpleName() + "\" must be static to generate aliases");
        final TypeElement type = Objects.requireNonNull(findInitializerType(element));
        final DeclaredType declared = (DeclaredType) type.asType();

        final Predicate<ExecutableElement> filter = method -> {
            if (!method.getModifiers().contains(Modifier.PUBLIC)) return false;
            return method.getAnnotation(AliasExclude.class) == null;
        };

        final var order = baseMethods(type)
                .values()
                .stream()
                .flatMap(Collection::stream)
                .filter(filter)
                .map(method -> methodSignature(declared, method))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        final var methods = specializedMethods(type)
                .values()
                .stream()
                .flatMap(Collection::stream)
                .filter(filter)
                .collect(Collectors.toMap(method -> methodSignature(declared, method), method -> method));

        final var result = new LinkedHashSet<ExecutableElement>();
        for (var signature : order) {

            final var method = methods.get(signature);
            // Some methods might have gotten filtered, so I check if the association exist.
            if (method == null) continue;
            result.add(method);
        }
        return result;
    }

    /// @return the method signature while retaining as much information as the compiler has.
    @SuppressWarnings("unused")
    public String erasedMethodSignature(ExecutableElement method) {

        final Types types = processingEnv.getTypeUtils();
        final String parameters = method.getParameters()
                .stream()
                .map(p -> types.erasure(p.asType()).toString())
                .collect(Collectors.joining(","));

        return method.getSimpleName() + "(" + parameters + ")";
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
                final var tv = (TypeVariable) typeMirror;
                yield processingEnv.getTypeUtils().asMemberOf(declaredType, tv.asElement()).toString();
            }
            case DECLARED -> {

                final var declared = (DeclaredType) typeMirror;
                final String baseName = ((TypeElement) declared.asElement()).getQualifiedName().toString();

                final List<? extends TypeMirror> args = declared.getTypeArguments();
                if (args.isEmpty()) yield baseName;

                // Recursively resolve each type argument
                StringBuilder sb = new StringBuilder(baseName).append("<");
                for (int i = 0; i < args.size(); i++) {
                    if (i > 0) sb.append(", ");
                    sb.append(specializedOfGeneric(declaredType, args.get(i)));
                }
                sb.append(">");
                yield sb.toString();
            }
            case ARRAY -> specializedOfGeneric(declaredType, ((ArrayType) typeMirror).getComponentType()) + "[]";
            case WILDCARD -> {

                final var wildcard = (WildcardType) typeMirror;
                final TypeMirror extendsBound = wildcard.getExtendsBound();
                if (extendsBound != null) yield "? extends " + specializedOfGeneric(declaredType, extendsBound);

                final TypeMirror superBound = wildcard.getSuperBound();
                yield superBound == null ? "?" : "? super " + specializedOfGeneric(declaredType, superBound);
            }
            default -> typeMirror.toString();
        };
    }

    public TypeElement findInitializerType(VariableElement element) {
        final var owner = (TypeElement) element.getEnclosingElement();
        for (Element e : owner.getEnclosedElements()) {
            if (e.getKind() == ElementKind.CLASS || e.getKind() == ElementKind.RECORD) return (TypeElement) e;
        }
        return null;
    }
}
