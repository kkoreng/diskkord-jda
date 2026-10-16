package com.kkoreng.diskkord.core.dispatcher;

import com.kkoreng.diskkord.api.interaction.ButtonInteractionHandler;
import com.kkoreng.diskkord.core.registry.ButtonInteractionRegistry;
import com.kkoreng.diskkord.core.util.CustomIds;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ButtonInteractionDispatcher extends ListenerAdapter {

    private final ButtonInteractionRegistry registry;

    public ButtonInteractionDispatcher(ButtonInteractionRegistry registry) {
        this.registry = registry;
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        CustomIds.Parsed parsed = CustomIds.parse(event.getComponentId());
        ButtonInteractionHandler handler = registry.get(parsed.id());

        if (handler == null) {
            return;
        }

        handler.handle(event, parsed.args());
    }
}