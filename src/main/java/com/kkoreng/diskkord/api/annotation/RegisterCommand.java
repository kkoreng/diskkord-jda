package com.kkoreng.diskkord.api.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

/**
 * Marks a class to be registered automatically by {@link com.kkoreng.diskkord.DiskkordBuilder#scan(String)}.
 * <p>
 * Use this for anything a user invokes:
 * <ul>
 *     <li>{@link com.kkoreng.diskkord.api.command.SlashCommand}</li>
 *     <li>{@link com.kkoreng.diskkord.api.command.TextCommand}</li>
 *     <li>{@link com.kkoreng.diskkord.api.command.context.MessageContextCommand}</li>
 *     <li>{@link com.kkoreng.diskkord.api.command.context.UserContextCommand}</li>
 * </ul>
 * The class must be concrete, top-level or static nested, and have a no-args constructor.
 * Classes that need constructor arguments should be created by the caller and registered with
 * {@link com.kkoreng.diskkord.DiskkordBuilder#register} instead.
 *
 * @see RegisterListener
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(TYPE)
public @interface RegisterCommand {
}