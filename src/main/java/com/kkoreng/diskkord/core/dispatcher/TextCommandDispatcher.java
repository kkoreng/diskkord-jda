package com.kkoreng.diskkord.core.dispatcher;

import com.kkoreng.diskkord.api.command.TextCommand;
import com.kkoreng.diskkord.core.registry.TextCommandRegistry;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class TextCommandDispatcher extends ListenerAdapter {

    private final TextCommandRegistry registry;

    public TextCommandDispatcher(TextCommandRegistry registry) {
        this.registry = registry;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }

        String content = event.getMessage().getContentRaw().trim();

        if (content.isEmpty()) {
            return;
        }

        String name = content.split("\\s+", 2)[0];

        TextCommand command = registry.get(name);

        if (command == null) {
            return;
        }

        command.execute(event);
    }
}