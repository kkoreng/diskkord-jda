package com.kkoreng.diskkord.core.dispatcher;

import com.kkoreng.diskkord.api.command.context.UserContextCommand;
import com.kkoreng.diskkord.core.registry.UserContextCommandRegistry;
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class UserContextCommandDispatcher extends ListenerAdapter {

    private final UserContextCommandRegistry registry;

    public UserContextCommandDispatcher(UserContextCommandRegistry registry) {
        this.registry = registry;
    }

    @Override
    public void onUserContextInteraction(UserContextInteractionEvent event) {
        UserContextCommand command = registry.get(event.getName());

        if (command == null) {
            return;
        }

        command.execute(event);
    }
}
