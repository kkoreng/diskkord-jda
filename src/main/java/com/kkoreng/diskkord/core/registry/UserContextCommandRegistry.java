package com.kkoreng.diskkord.core.registry;

import com.kkoreng.diskkord.api.command.context.UserContextCommand;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores user context commands by name. Names are matched exactly, since Discord allows uppercase and spaces.
 */
public final class UserContextCommandRegistry extends AbstractRegistry<String, UserContextCommand> {

    private final List<CommandData> data = new ArrayList<>();

    public void register(UserContextCommand command) {
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
