package com.kkoreng.diskkord.api.interaction;

import com.kkoreng.diskkord.core.util.CustomIds;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;

/**
 * Handles a specific type of JDA interaction event.
 *
 * @param <E> the type of interaction event to handle
 */
public interface InteractionHandler<E extends GenericInteractionCreateEvent> {
    String id();
    void handle(E event, Args args);

    default String idWith(Object... args) {
        return CustomIds.build(id(), args);
    }
}