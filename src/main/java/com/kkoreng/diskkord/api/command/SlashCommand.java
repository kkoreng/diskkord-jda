package com.kkoreng.diskkord.api.command;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.util.List;

public interface SlashCommand extends Command<SlashCommandInteractionEvent> {
    SlashCommandData data();

    /**
     * Runs when the command is used without a matching subcommand.
     * Commands that only use {@link #subcommands()} or {@link #groups()} do not need to override this.
     */
    @Override
    default void execute(SlashCommandInteractionEvent event) {
        throw new IllegalStateException(
                getClass().getName() + " has no subcommand for " + event.getFullCommandName()
                        + " and does not override execute()"
        );
    }

    default List<SubCommand> subcommands() {
        return List.of();
    }

    default List<SubCommandGroup> groups() {
        return List.of();
    }

    default void autocomplete(CommandAutoCompleteInteractionEvent event) {
        event.replyChoices().queue();
    }
}