package com.kkoreng.diskkord.api.command.context;

import com.kkoreng.diskkord.api.command.Command;
import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent;

public interface MessageContextCommand extends Command<MessageContextInteractionEvent> {
    String name();
}
