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
import org.lidiuma.math.processor.NamedAlias;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.*;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.*;

public final class Processor extends AbstractProcessor {

    private Utility util;

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Set.of(Alias.class.getName(), NamedAlias.class.getName());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        util = new Utility(processingEnv);
        if (roundEnv.processingOver()) return false;

        try { handleAlias(roundEnv);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    private void handleAlias(RoundEnvironment roundEnv) throws IOException {

        final var elements = util.processingEnv().getElementUtils();
        final Map<String, AliasGenerator> aliases = new HashMap<>();

        roundEnv.getElementsAnnotatedWith(NamedAlias.class).forEach(element -> {

            final NamedAlias annotation = Objects.requireNonNull(element.getAnnotation(NamedAlias.class));
            final String package_ = elements.getPackageOf(element).toString();
            final String class_ = annotation.outputClass();
            final String signature = package_ + "." + class_;

            aliases.computeIfAbsent(signature, _ -> new AliasGenerator(util, package_, class_))
                    .addMethods(annotation, element);
        });

        roundEnv.getElementsAnnotatedWith(Alias.class).forEach(element -> {

            final var annotation = Objects.requireNonNull(element.getAnnotation(Alias.class));
            if (isAliasInvalid(element, annotation.outputClass())) return;

            final String package_ = elements.getPackageOf(element).toString();
            final String class_ = annotation.outputClass();
            final String signature = package_ + "." + class_;

            aliases.computeIfAbsent(signature, _ -> new AliasGenerator(util, package_, class_))
                    .addMethods(annotation, element);
        });

        aliases.values().forEach(gen -> {
            try { gen.build();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        });
    }

    private boolean isAliasInvalid(Element element, String className) {

        final String annotationName = Alias.class.getSimpleName();
        final var messager = processingEnv.getMessager();
        if (className.isBlank()) {
            messager.printError(annotationName + "'s class name cannot be empty.", element);
            return true;
        }

        final var modifiers = element.getModifiers();
        if (modifiers.contains(Modifier.PRIVATE)) { // TODO Allow the method generator to use anything other than public.
            messager.printError("Cannot generate alias since '" + element.getSimpleName() + "' is private.", element);
            return true;
        }

        if (!modifiers.contains(Modifier.STATIC)) {
            messager.printError("Cannot generate alias since '" + element.getSimpleName() + "' is not static.", element);
            return true;
        }

        if (!modifiers.contains(Modifier.FINAL)) {
            messager.printError("Cannot generate alias since '" + element.getSimpleName() + "' is not final.", element);
            return true;
        }
        return false;
    }
}
