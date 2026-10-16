package com.kkoreng.diskkord.core.registrar;

import com.kkoreng.diskkord.api.command.SlashCommand;
import com.kkoreng.diskkord.api.command.TextCommand;
import com.kkoreng.diskkord.core.registry.SlashCommandRegistry;
import com.kkoreng.diskkord.core.registry.TextCommandRegistry;
import lombok.Getter;
import net.dv8tion.jda.api.JDA;

@Getter
public final class CommandRegistrar {

    private final SlashCommandRegistry slashCommands =
            new SlashCommandRegistry();

    private final TextCommandRegistry textCommands =
            new TextCommandRegistry();

    public void register(SlashCommand command) {
        slashCommands.register(command);
    }

    public void register(TextCommand command) {
        textCommands.register(command);
    }

    public void sync(JDA jda) {
        jda.updateCommands()
                .addCommands(
                        slashCommands.getAll().stream()
                                .map(SlashCommand::data)
                                .toList()
                )
                .queue();
    }

}