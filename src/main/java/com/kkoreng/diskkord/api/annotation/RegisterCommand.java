package com.kkoreng.diskkord.api.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

/**
 * Marks a class to be registered automatically by {@link com.kkoreng.diskkord.Diskkord#scan(String)}.
 * <p>
 * Use this for anything a user invokes:
 * <ul>
 *     <li>{@link com.kkoreng.diskkord.api.command.SlashCommand}</li>
 *     <li>{@link com.kkoreng.diskkord.api.command.TextCommand}</li>
 * </ul>
 * The class must be concrete, top-level or static nested, and have a no-args constructor.
 * Classes that need constructor arguments should be registered manually with
 * {@link com.kkoreng.diskkord.Diskkord#register} instead.
 *
 * @see RegisterListener
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(TYPE)
public @interface RegisterCommand {
}