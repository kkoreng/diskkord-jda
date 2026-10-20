package com.kkoreng.diskkord.api.command.context;

import com.kkoreng.diskkord.api.command.Command;
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

/**
 * A command shown when right-clicking a user (Apps menu).
 * The clicked user is available from {@link UserContextInteractionEvent#getTarget()}.
 */
public interface UserContextCommand extends Command<UserContextInteractionEvent> {

    /**
     * The name shown in the menu. Up to 32 characters; spaces and uppercase are allowed.
     */
    String name();

    /**
     * The command data sent to Discord. Override to set permissions or localizations.
     */
    default CommandData data() {
        return Commands.user(name());
    }
}
