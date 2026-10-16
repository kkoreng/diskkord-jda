package com.kkoreng.diskkord.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a class to be registered automatically by {@link com.kkoreng.diskkord.Diskkord#scan(String)}.
 * <p>
 * Use this for anything that reacts when something happens:
 * <ul>
 *     <li>{@link com.kkoreng.diskkord.api.interaction.ButtonInteractionHandler}</li>
 *     <li>{@link com.kkoreng.diskkord.api.interaction.ModalInteractionHandler}</li>
 *     <li>{@link com.kkoreng.diskkord.api.interaction.StringSelectInteractionHandler}</li>
 *     <li>{@link com.kkoreng.diskkord.api.interaction.EntitySelectInteractionHandler}</li>
 *     <li>{@link net.dv8tion.jda.api.hooks.EventListener}, including {@link net.dv8tion.jda.api.hooks.ListenerAdapter}</li>
 * </ul>
 * The class must be concrete, top-level or static nested, and have a no-args constructor.
 * Classes that need constructor arguments should be registered manually with
 * {@link com.kkoreng.diskkord.Diskkord#register} instead.
 *
 * @see RegisterCommand
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RegisterListener {
}