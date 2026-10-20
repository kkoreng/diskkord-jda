package com.kkoreng.diskkord.api.command;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface TextCommand extends Command<MessageReceivedEvent> {
    String name();

    /**
     * Other names that run this command. Like {@link #name()}, aliases are matched
     * case-insensitively against the first word of the message and must not contain spaces.
     */
    default String[] aliases() {
        return new String[0];
    }

}