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
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
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

    /// @return the unique non-abstract methods from the static field provided.
    public SequencedSet<ExecutableElement> fieldMethods(VariableElement element) {

        if (!element.getModifiers().contains(Modifier.STATIC)) throw new IllegalArgumentException("The field \"" + element.getSimpleName() + "\" must be static to generate aliases");

        final TypeElement type = findInitializerType(element);

        final TypeElement objectElement = processingEnv.getElementUtils().getTypeElement(Object.class.getName());
        final Elements elements = processingEnv.getElementUtils();

        // Normal order goes from the bottom of the hierarchy to the top, top to bottom gives a better method ordering.
        final var methods = ElementFilter.methodsIn(elements.getAllMembers(type)).reversed();
        final var ignored = new HashSet<String>();

        final var result = new LinkedHashSet<ExecutableElement>();
        for (var method : methods) {

            // I remove methods from Object.
            if (method.getEnclosingElement().equals(objectElement)) continue;
            if (!method.getModifiers().contains(Modifier.PUBLIC)) continue;
            // Default are not considered as ABSTRACT.
            if (method.getModifiers().contains(Modifier.ABSTRACT)) continue;

            final var signature = methodSignature((DeclaredType) element.asType(), method);
            if (method.getAnnotation(AliasExclude.class) != null) {
                // This way even in case of default methods, the method is gone for good.
                ignored.add(signature);
                continue;
            }

            final boolean present = ignored.contains(signature);
            ignored.add(signature); // I now ignore it since already added.
            if (present) continue;
            result.add(method);
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
