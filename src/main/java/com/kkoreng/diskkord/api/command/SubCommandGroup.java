package com.kkoreng.diskkord.api.command;

import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;

import java.util.List;

public interface SubCommandGroup {

    String name();

    String description();

    List<SubCommand> subcommands();

    default SubcommandGroupData data() {
        SubcommandGroupData group = new SubcommandGroupData(name(), description());

        for (SubCommand command : subcommands()) {
            group.addSubcommands(command.data());
        }

        return group;
    }
}