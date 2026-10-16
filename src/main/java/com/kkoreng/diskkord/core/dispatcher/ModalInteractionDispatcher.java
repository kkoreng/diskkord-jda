package com.kkoreng.diskkord.core.dispatcher;

import com.kkoreng.diskkord.api.interaction.ModalInteractionHandler;
import com.kkoreng.diskkord.core.registry.ModalInteractionRegistry;
import com.kkoreng.diskkord.core.util.CustomIds;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ModalInteractionDispatcher extends ListenerAdapter {

    private final ModalInteractionRegistry registry;

    public ModalInteractionDispatcher(ModalInteractionRegistry registry) {
        this.registry = registry;
    }

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        CustomIds.Parsed parsed = CustomIds.parse(event.getModalId());
        ModalInteractionHandler handler = registry.get(parsed.id());

        if (handler == null) {
            return;
        }

        handler.handle(event, parsed.args());
    }
}
