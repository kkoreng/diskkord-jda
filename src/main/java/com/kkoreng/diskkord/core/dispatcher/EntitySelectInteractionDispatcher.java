package com.kkoreng.diskkord.core.dispatcher;

import com.kkoreng.diskkord.api.interaction.EntitySelectInteractionHandler;
import com.kkoreng.diskkord.core.registry.EntitySelectInteractionRegistry;
import com.kkoreng.diskkord.core.util.CustomIds;
import net.dv8tion.jda.api.events.interaction.component.EntitySelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class EntitySelectInteractionDispatcher extends ListenerAdapter {

    private final EntitySelectInteractionRegistry registry;

    public EntitySelectInteractionDispatcher(EntitySelectInteractionRegistry registry) {
        this.registry = registry;
    }

    @Override
    public void onEntitySelectInteraction(EntitySelectInteractionEvent event) {
        CustomIds.Parsed parsed = CustomIds.parse(event.getComponentId());
        EntitySelectInteractionHandler handler = registry.get(parsed.id());

        if (handler == null) {
            return;
        }

        handler.handle(event, parsed.args());
    }
}