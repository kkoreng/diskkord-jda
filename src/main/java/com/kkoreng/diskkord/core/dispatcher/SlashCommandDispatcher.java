package com.kkoreng.diskkord.core.dispatcher;

import com.kkoreng.diskkord.api.command.SlashCommand;
import com.kkoreng.diskkord.core.registry.SlashCommandRegistry;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class SlashCommandDispatcher extends ListenerAdapter {

    private final SlashCommandRegistry registry;

    public SlashCommandDispatcher(SlashCommandRegistry registry) {
        this.registry = registry;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        SlashCommand command = registry.get(event.getName());

        if (command == null) {
            return;
        }

        command.execute(event);
    }

    @Override
    public void onCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent event) {
        SlashCommand command = registry.get(event.getName());

        if (command == null) {
            return;
        }

        command.autocomplete(event);
    }
}