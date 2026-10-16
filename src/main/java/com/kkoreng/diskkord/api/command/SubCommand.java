package com.kkoreng.diskkord.api.command;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public interface SubCommand extends Command<SlashCommandInteractionEvent> {
    SubcommandData data();

    default void autocomplete(CommandAutoCompleteInteractionEvent event) {
        event.replyChoices().queue();
    }
}
