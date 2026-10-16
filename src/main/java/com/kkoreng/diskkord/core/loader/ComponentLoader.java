package com.kkoreng.diskkord.core.loader;

import com.kkoreng.diskkord.Diskkord;
import com.kkoreng.diskkord.api.annotation.RegisterCommand;
import com.kkoreng.diskkord.api.annotation.RegisterListener;
import com.kkoreng.diskkord.api.command.SlashCommand;
import com.kkoreng.diskkord.api.command.TextCommand;
import com.kkoreng.diskkord.api.interaction.ButtonInteractionHandler;
import com.kkoreng.diskkord.api.interaction.EntitySelectInteractionHandler;
import com.kkoreng.diskkord.api.interaction.ModalInteractionHandler;
import com.kkoreng.diskkord.api.interaction.StringSelectInteractionHandler;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import net.dv8tion.jda.api.hooks.EventListener;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

/**
 * Scans a package for annotated classes and registers them.
 * <ul>
 *     <li>{@link RegisterCommand}: slash and text commands</li>
 *     <li>{@link RegisterListener}: event listeners and interaction handlers</li>
 * </ul>
 * Annotated classes must be concrete and have a no-args constructor.
 */
public final class ComponentLoader {

    public void load(String basePackage, Diskkord diskkord) {
        try (ScanResult scan = scan(basePackage)) {
            for (ClassInfo info : scan.getClassesWithAnnotation(RegisterCommand.class)) {
                registerCommand(instantiate(info.loadClass()), diskkord);
            }

            for (ClassInfo info : scan.getClassesWithAnnotation(RegisterListener.class)) {
                registerListener(instantiate(info.loadClass()), diskkord);
            }
        }
    }

    private static void registerCommand(Object instance, Diskkord diskkord) {
        if (instance instanceof SlashCommand command) {
            diskkord.register(command);
        } else if (instance instanceof TextCommand command) {
            diskkord.register(command);
        } else {
            throw new IllegalStateException(
                    "@RegisterCommand is not supported on " + instance.getClass().getName()
            );
        }
    }

    private static void registerListener(Object instance, Diskkord diskkord) {
        if (instance instanceof ButtonInteractionHandler handler) {
            diskkord.register(handler);
        } else if (instance instanceof ModalInteractionHandler handler) {
            diskkord.register(handler);
        } else if (instance instanceof StringSelectInteractionHandler handler) {
            diskkord.register(handler);
        } else if (instance instanceof EntitySelectInteractionHandler handler) {
            diskkord.register(handler);
        } else if (instance instanceof EventListener listener) {
            diskkord.register(listener);
        } else {
            throw new IllegalStateException(
                    "@RegisterListener is not supported on " + instance.getClass().getName()
            );
        }
    }

    private static ScanResult scan(String basePackage) {
        return new ClassGraph()
                .enableAnnotationInfo()
                // Include package-private classes; ClassGraph skips them by default.
                .ignoreClassVisibility()
                .acceptPackages(basePackage)
                .scan();
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
            throw new IllegalStateException(type.getName() + " needs a no-args constructor", e);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to instantiate " + type.getName(), e);
        }
    }
}
