package com.kkoreng.diskkord.core.dispatcher;

import com.kkoreng.diskkord.api.interaction.StringSelectInteractionHandler;
import com.kkoreng.diskkord.core.registry.StringSelectInteractionRegistry;
import com.kkoreng.diskkord.core.util.CustomIds;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class StringSelectInteractionDispatcher extends ListenerAdapter {

    private final StringSelectInteractionRegistry registry;

    public StringSelectInteractionDispatcher(StringSelectInteractionRegistry registry) {
        this.registry = registry;
    }

    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
        CustomIds.Parsed parsed = CustomIds.parse(event.getComponentId());
        StringSelectInteractionHandler handler = registry.get(parsed.id());

        if (handler == null) {
            return;
        }

        handler.handle(event, parsed.args());
    }
}