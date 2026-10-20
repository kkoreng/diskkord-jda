package com.kkoreng.diskkord.core.dispatcher;

import com.kkoreng.diskkord.api.command.SlashCommand;
import com.kkoreng.diskkord.api.command.SubCommand;
import com.kkoreng.diskkord.core.registry.SlashCommandRegistry;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * Routes slash commands and autocomplete to the matching subcommand,
 * or to the command itself when no registered subcommand matches.
 */
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

        SubCommand subcommand = registry.getSubCommand(
                event.getName(), event.getSubcommandGroup(), event.getSubcommandName()
        );

        if (subcommand != null) {
            subcommand.execute(event);
        } else {
            command.execute(event);
        }
    }

    @Override
    public void onCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent event) {
        SlashCommand command = registry.get(event.getName());

        if (command == null) {
            return;
        }

        SubCommand subcommand = registry.getSubCommand(
                event.getName(), event.getSubcommandGroup(), event.getSubcommandName()
        );

        if (subcommand != null) {
            subcommand.autocomplete(event);
        } else {
            command.autocomplete(event);
        }
    }
}
