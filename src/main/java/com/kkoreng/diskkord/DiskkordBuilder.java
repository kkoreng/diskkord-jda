package com.kkoreng.diskkord;

import com.kkoreng.diskkord.api.command.SlashCommand;
import com.kkoreng.diskkord.api.command.TextCommand;
import com.kkoreng.diskkord.api.command.context.MessageContextCommand;
import com.kkoreng.diskkord.api.command.context.UserContextCommand;
import com.kkoreng.diskkord.api.interaction.ButtonInteractionHandler;
import com.kkoreng.diskkord.api.interaction.EntitySelectInteractionHandler;
import com.kkoreng.diskkord.api.interaction.ModalInteractionHandler;
import com.kkoreng.diskkord.api.interaction.StringSelectInteractionHandler;
import com.kkoreng.diskkord.core.dispatcher.ButtonInteractionDispatcher;
import com.kkoreng.diskkord.core.dispatcher.EntitySelectInteractionDispatcher;
import com.kkoreng.diskkord.core.dispatcher.MessageContextCommandDispatcher;
import com.kkoreng.diskkord.core.dispatcher.ModalInteractionDispatcher;
import com.kkoreng.diskkord.core.dispatcher.SlashCommandDispatcher;
import com.kkoreng.diskkord.core.dispatcher.StringSelectInteractionDispatcher;
import com.kkoreng.diskkord.core.dispatcher.TextCommandDispatcher;
import com.kkoreng.diskkord.core.dispatcher.UserContextCommandDispatcher;
import com.kkoreng.diskkord.core.loader.ComponentLoader;
import com.kkoreng.diskkord.core.registrar.CommandRegistrar;
import com.kkoreng.diskkord.core.registrar.CommandSyncListener;
import com.kkoreng.diskkord.core.registrar.InteractionRegistrar;
import com.kkoreng.diskkord.core.registrar.ListenerRegistrar;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.EventListener;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Collects commands, handlers and listeners, then builds a running {@link Diskkord}.
 * <p>
 * Scanned classes are created with their no-args constructor. Classes that need
 * constructor arguments are created by the caller and passed to {@code register}.
 *
 * <pre>{@code
 * Diskkord diskkord = Diskkord.builder(JDABuilder.createDefault(token))
 *         .scan("com.example.bot")
 *         .register(new HelloCommand(config))
 *         .build();
 * }</pre>
 */
public final class DiskkordBuilder {

    private final JDABuilder jdaBuilder;

    private final CommandRegistrar commands = new CommandRegistrar();
    private final InteractionRegistrar interactions = new InteractionRegistrar();
    private final ListenerRegistrar listeners = new ListenerRegistrar();

    private final List<String> scanPackages = new ArrayList<>();

    private boolean built;

    DiskkordBuilder(JDABuilder jdaBuilder) {
        this.jdaBuilder = Objects.requireNonNull(jdaBuilder);
    }

    /**
     * Registers every class annotated with
     * {@link com.kkoreng.diskkord.api.annotation.RegisterCommand} or
     * {@link com.kkoreng.diskkord.api.annotation.RegisterListener}
     * in the given package and its subpackages when {@link #build()} is called.
     *
     * @param basePackage the package to scan
     * @return this builder
     */
    public DiskkordBuilder scan(String basePackage) {
        ensureNotBuilt();
        scanPackages.add(Objects.requireNonNull(basePackage));
        return this;
    }

    /**
     * Registers every slash and context menu command in the given servers instead of globally.
     * Server commands update immediately, which is handy while developing.
     * Calling this again replaces the previous servers; calling it with no IDs means global.
     *
     * @param guildIds the server IDs
     * @return this builder
     */
    public DiskkordBuilder guilds(long... guildIds) {
        ensureNotBuilt();
        Set<Long> ids = new LinkedHashSet<>();
        for (long id : guildIds) {
            ids.add(id);
        }
        commands.useGuilds(ids);
        return this;
    }

    /**
     * Registers a slash command.
     *
     * @param command the slash command to register
     * @return this builder
     */
    public DiskkordBuilder register(SlashCommand command) {
        ensureNotBuilt();
        commands.register(command);
        return this;
    }

    /**
     * Registers a text command.
     *
     * @param command the text command to register
     * @return this builder
     */
    public DiskkordBuilder register(TextCommand command) {
        ensureNotBuilt();
        commands.register(command);
        return this;
    }

    /**
     * Registers a message context menu command.
     *
     * @param command the message context command to register
     * @return this builder
     */
    public DiskkordBuilder register(MessageContextCommand command) {
        ensureNotBuilt();
        commands.register(command);
        return this;
    }

    /**
     * Registers a user context menu command.
     *
     * @param command the user context command to register
     * @return this builder
     */
    public DiskkordBuilder register(UserContextCommand command) {
        ensureNotBuilt();
        commands.register(command);
        return this;
    }

    /**
     * Registers a button interaction handler.
     *
     * @param handler the button interaction handler to register
     * @return this builder
     */
    public DiskkordBuilder register(ButtonInteractionHandler handler) {
        ensureNotBuilt();
        interactions.register(handler);
        return this;
    }

    /**
     * Registers a modal interaction handler.
     *
     * @param handler the modal interaction handler to register
     * @return this builder
     */
    public DiskkordBuilder register(ModalInteractionHandler handler) {
        ensureNotBuilt();
        interactions.register(handler);
        return this;
    }

    /**
     * Registers a string select interaction handler.
     *
     * @param handler the string select interaction handler to register
     * @return this builder
     */
    public DiskkordBuilder register(StringSelectInteractionHandler handler) {
        ensureNotBuilt();
        interactions.register(handler);
        return this;
    }

    /**
     * Registers an entity select interaction handler.
     *
     * @param handler the entity select interaction handler to register
     * @return this builder
     */
    public DiskkordBuilder register(EntitySelectInteractionHandler handler) {
        ensureNotBuilt();
        interactions.register(handler);
        return this;
    }

    /**
     * Registers a JDA event listener.
     *
     * @param listener the event listener to register
     * @return this builder
     */
    public DiskkordBuilder register(EventListener listener) {
        ensureNotBuilt();
        listeners.register(listener);
        return this;
    }

    /**
     * Creates scanned classes, installs listeners and builds JDA.
     * Application commands are synchronized with Discord once JDA is ready.
     * The builder cannot be used after this call.
     *
     * @return the running Diskkord instance
     */
    public Diskkord build() {
        ensureNotBuilt();
        built = true;

        ComponentLoader loader = new ComponentLoader();
        for (String basePackage : scanPackages) {
            loader.load(basePackage, this::registerScanned);
        }

        listeners.register(
                new SlashCommandDispatcher(commands.getSlashCommands()),
                new TextCommandDispatcher(commands.getTextCommands()),
                new MessageContextCommandDispatcher(commands.getMessageContextCommands()),
                new UserContextCommandDispatcher(commands.getUserContextCommands()),
                new ButtonInteractionDispatcher(interactions.getButtons()),
                new ModalInteractionDispatcher(interactions.getModals()),
                new StringSelectInteractionDispatcher(interactions.getStringSelects()),
                new EntitySelectInteractionDispatcher(interactions.getEntitySelects()),
                new CommandSyncListener(commands)
        );
        listeners.install(jdaBuilder);

        JDA jda = jdaBuilder.build();

        return new Diskkord(jda);
    }

    private void registerScanned(Object component) {
        switch (component) {
            case SlashCommand command -> commands.register(command);
            case TextCommand command -> commands.register(command);
            case MessageContextCommand command -> commands.register(command);
            case UserContextCommand command -> commands.register(command);
            case ButtonInteractionHandler handler -> interactions.register(handler);
            case ModalInteractionHandler handler -> interactions.register(handler);
            case StringSelectInteractionHandler handler -> interactions.register(handler);
            case EntitySelectInteractionHandler handler -> interactions.register(handler);
            case EventListener listener -> listeners.register(listener);
            default -> throw new IllegalStateException(
                    "Unsupported component: " + component.getClass().getName()
            );
        }
    }

    private void ensureNotBuilt() {
        if (built) {
            throw new IllegalStateException("DiskkordBuilder has already been built");
        }
    }
}
