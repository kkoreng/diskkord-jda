package com.kkoreng.diskkord.core.registry;

import com.kkoreng.diskkord.api.command.context.MessageContextCommand;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores message context commands by name. Names are matched exactly, since Discord allows uppercase and spaces.
 */
public final class MessageContextCommandRegistry extends AbstractRegistry<String, MessageContextCommand> {

    private final List<CommandData> data = new ArrayList<>();

    public void register(MessageContextCommand command) {
        CommandData commandData = command.data();
        super.register(commandData.getName(), command);
        data.add(commandData);
    }

    /**
     * Returns the command data to send to Discord.
     *
     * @return the command data of every registered command
     */
    public List<CommandData> getAllData() {
        return List.copyOf(data);
    }
}
