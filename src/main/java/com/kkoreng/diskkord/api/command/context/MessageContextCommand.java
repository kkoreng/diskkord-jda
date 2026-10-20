package com.kkoreng.diskkord.api.command.context;

import com.kkoreng.diskkord.api.command.Command;
import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

/**
 * A command shown when right-clicking a message (Apps menu).
 * The clicked message is available from {@link MessageContextInteractionEvent#getTarget()}.
 */
public interface MessageContextCommand extends Command<MessageContextInteractionEvent> {

    /**
     * The name shown in the menu. Up to 32 characters; spaces and uppercase are allowed.
     */
    String name();

    /**
     * The command data sent to Discord. Override to set permissions or localizations.
     */
    default CommandData data() {
        return Commands.message(name());
    }
}
