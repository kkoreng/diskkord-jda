package com.kkoreng.diskkord.core.loader;

import com.kkoreng.diskkord.api.annotation.RegisterCommand;
import com.kkoreng.diskkord.api.annotation.RegisterListener;
import com.kkoreng.diskkord.api.command.SlashCommand;
import com.kkoreng.diskkord.api.command.TextCommand;
import com.kkoreng.diskkord.api.command.context.MessageContextCommand;
import com.kkoreng.diskkord.api.command.context.UserContextCommand;
import com.kkoreng.diskkord.api.interaction.InteractionHandler;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import net.dv8tion.jda.api.hooks.EventListener;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.function.Consumer;

/**
 * Scans a package for annotated classes, creates them and passes each instance to a callback.
 * <ul>
 *     <li>{@link RegisterCommand}: slash, text and context menu commands</li>
 *     <li>{@link RegisterListener}: event listeners and interaction handlers</li>
 * </ul>
 * Annotated classes are created with their no-args constructor.
 */
public final class ComponentLoader {

    public void load(String basePackage, Consumer<Object> registrar) {
        try (ScanResult scan = new ClassGraph()
                .enableAnnotationInfo()
                // Include package-private classes; ClassGraph skips them by default.
                .ignoreClassVisibility()
                .acceptPackages(basePackage)
                .scan()) {

            for (ClassInfo info : scan.getClassesWithAnnotation(RegisterCommand.class)) {
                Object instance = instantiate(info.loadClass());

                if (!(instance instanceof SlashCommand
                        || instance instanceof TextCommand
                        || instance instanceof MessageContextCommand
                        || instance instanceof UserContextCommand)) {
                    throw new IllegalStateException(
                            "@RegisterCommand is not supported on " + info.getName()
                    );
                }

                registrar.accept(instance);
            }

            for (ClassInfo info : scan.getClassesWithAnnotation(RegisterListener.class)) {
                Object instance = instantiate(info.loadClass());

                if (!(instance instanceof InteractionHandler<?> || instance instanceof EventListener)) {
                    throw new IllegalStateException(
                            "@RegisterListener is not supported on " + info.getName()
                    );
                }

                registrar.accept(instance);
            }
        }
    }

    private static Object instantiate(Class<?> type) {
        if (type.isInterface() || Modifier.isAbstract(type.getModifiers())) {
            throw new IllegalStateException(type.getName() + " must be a concrete class");
        }

        if (type.isMemberClass() && !Modifier.isStatic(type.getModifiers())) {
            throw new IllegalStateException(type.getName() + " must be a static nested class");
        }

        try {
            Constructor<?> constructor = type.getDeclaredConstructor();
            // Allow package-private classes and constructors.
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException(
                    type.getName() + " needs a no-args constructor to be scanned. "
                            + "Register it with DiskkordBuilder#register instead.", e
            );
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to instantiate " + type.getName(), e);
        }
    }
}
