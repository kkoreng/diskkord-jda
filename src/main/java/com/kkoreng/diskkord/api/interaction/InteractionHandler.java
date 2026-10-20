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

    /**
     * Builds a custom ID for the handler with the given ID, without needing a handler instance.
     * For example, a command can create a button for a handler it does not hold:
     * <pre>{@code
     * Button.primary(InteractionHandler.idOf(CounterButtonHandler.ID, 3), "+1");
     * }</pre>
     *
     * @param id   the handler ID
     * @param args the arguments to append
     * @return the custom ID, e.g. {@code counter:3}
     */
    static String idOf(String id, Object... args) {
        return CustomIds.build(id, args);
    }
}