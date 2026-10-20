package com.kkoreng.diskkord.core.dispatcher;

import com.kkoreng.diskkord.api.command.context.MessageContextCommand;
import com.kkoreng.diskkord.core.registry.MessageContextCommandRegistry;
import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageContextCommandDispatcher extends ListenerAdapter {

    private final MessageContextCommandRegistry registry;

    public MessageContextCommandDispatcher(MessageContextCommandRegistry registry) {
        this.registry = registry;
    }

    @Override
    public void onMessageContextInteraction(MessageContextInteractionEvent event) {
        MessageContextCommand command = registry.get(event.getName());

        if (command == null) {
            return;
        }

        command.execute(event);
    }
}
