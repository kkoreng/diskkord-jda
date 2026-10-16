package com.kkoreng.diskkord.api.command.context;

import com.kkoreng.diskkord.api.command.Command;
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent;

public interface UserContextCommand extends Command<UserContextInteractionEvent> {
    String name();
}