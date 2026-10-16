package com.kkoreng.diskkord;

import com.kkoreng.diskkord.api.command.SlashCommand;
import com.kkoreng.diskkord.api.command.TextCommand;
import com.kkoreng.diskkord.api.interaction.ButtonInteractionHandler;
import com.kkoreng.diskkord.api.interaction.EntitySelectInteractionHandler;
import com.kkoreng.diskkord.api.interaction.ModalInteractionHandler;
import com.kkoreng.diskkord.api.interaction.StringSelectInteractionHandler;
import com.kkoreng.diskkord.core.dispatcher.*;
import com.kkoreng.diskkord.core.loader.ComponentLoader;
import com.kkoreng.diskkord.core.registrar.CommandRegistrar;
import com.kkoreng.diskkord.core.registrar.InteractionRegistrar;
import com.kkoreng.diskkord.core.registrar.ListenerRegistrar;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.EventListener;

import java.util.Objects;

public final class Diskkord {

    private final CommandRegistrar commands = new CommandRegistrar();
    private final InteractionRegistrar interactions = new InteractionRegistrar();
    private final ListenerRegistrar listeners = new ListenerRegistrar();

    private JDABuilder builder;
    private JDA jda;
    private boolean started;

    /**
     * Installs Diskkord into the given JDABuilder.
     *
     * @param builder the JDABuilder to install Diskkord into
     */
    public void install(JDABuilder builder) {
        ensureNotStarted();

        if (this.builder != null) {
            throw new IllegalStateException("Diskkord is already installed");
        }

        this.builder = Objects.requireNonNull(builder);
    }

    /**
     * Scans the given package and registers every class annotated with
     * {@link com.kkoreng.diskkord.api.annotation.RegisterCommand} or
     * {@link com.kkoreng.diskkord.api.annotation.RegisterListener}.
     * Must be called before {@link #start()}.
     *
     * @param basePackage the package to scan, including subpackages
     */
    public void scan(String basePackage) {
        ensureNotStarted();
        new ComponentLoader().load(Objects.requireNonNull(basePackage), this);
    }

    /**
     * Starts the bot and synchronizes application commands.
     *
     * @return the JDA instance
     */
    public JDA start() {
        if (builder == null) {
            throw new IllegalStateException("Diskkord is not installed");
        }

        ensureNotStarted();
        started = true;

        listeners.register(
                new SlashCommandDispatcher(commands.getSlashCommands()),
                new TextCommandDispatcher(commands.getTextCommands()),
                new ButtonInteractionDispatcher(interactions.getButtons()),
                new ModalInteractionDispatcher(interactions.getModals()),
                new StringSelectInteractionDispatcher(interactions.getStringSelects()),
                new EntitySelectInteractionDispatcher(interactions.getEntitySelects())
        );

        try {
            listeners.install(builder);
            jda = builder.build();
            commands.sync(jda);

            return jda;
        } catch (RuntimeException e) {
            // Do not attempt to reuse a potentially consumed JDABuilder.
            throw e;
        }
    }

    /**
     * Registers a slash command.
     * Must be called before {@link #start()}.
     *
     * @param command the slash command to register
     */
    public void register(SlashCommand command) {
        ensureNotStarted();
        commands.register(command);
    }

    /**
     * Registers a text command.
     * Must be called before {@link #start()}.
     *
     * @param command the text command to register
     */
    public void register(TextCommand command) {
        ensureNotStarted();
        commands.register(command);
    }

    /**
     * Registers a button interaction handler.
     * Must be called before {@link #start()}.
     *
     * @param handler the button interaction handler to register
     */
    public void register(ButtonInteractionHandler handler) {
        ensureNotStarted();
        interactions.register(handler);
    }

    /**
     * Registers a modal interaction handler.
     * Must be called before {@link #start()}.
     *
     * @param handler the modal interaction handler to register
     */
    public void register(ModalInteractionHandler handler) {
        ensureNotStarted();
        interactions.register(handler);
    }

    /**
     * Registers a string select interaction handler.
     * Must be called before {@link #start()}.
     *
     * @param handler the string select interaction handler to register
     */
    public void register(StringSelectInteractionHandler handler) {
        ensureNotStarted();
        interactions.register(handler);
    }

    /**
     * Registers an entity select interaction handler.
     * Must be called before {@link #start()}.
     *
     * @param handler the entity select interaction handler to register
     */
    public void register(EntitySelectInteractionHandler handler) {
        ensureNotStarted();
        interactions.register(handler);
    }

    /**
     * Registers a JDA event listener.
     * Must be called before {@link #start()}.
     *
     * @param listener the event listener to register
     */
    public void register(EventListener listener) {
        ensureNotStarted();
        listeners.register(listener);
    }

    /**
     * Returns the JDA instance.
     *
     * @return the JDA instance, or null if not started
     */
    public JDA getJDA() {
        return jda;
    }

    private void ensureNotStarted() {
        if (started) {
            throw new IllegalStateException("Diskkord is already started");
        }
    }
}