package com.kkoreng.diskkord.api.command;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.util.List;

public interface SlashCommand extends Command<SlashCommandInteractionEvent> {
    SlashCommandData data();

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