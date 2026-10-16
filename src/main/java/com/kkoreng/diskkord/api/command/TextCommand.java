package com.kkoreng.diskkord.api.command;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface TextCommand extends Command<MessageReceivedEvent> {
    String name();

    default String[] aliases() {
        return new String[0];
    }

}