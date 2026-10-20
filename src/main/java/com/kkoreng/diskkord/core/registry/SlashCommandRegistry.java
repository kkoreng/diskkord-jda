package com.kkoreng.diskkord.core.registry;

import com.kkoreng.diskkord.api.command.SlashCommand;
import com.kkoreng.diskkord.api.command.SubCommand;
import com.kkoreng.diskkord.api.command.SubCommandGroup;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Stores slash commands by name, together with their subcommand routes
 * and the command data assembled from {@link SlashCommand#subcommands()} and {@link SlashCommand#groups()}.
 */
public final class SlashCommandRegistry extends AbstractRegistry<String, SlashCommand> {

    private final List<SlashCommandData> data = new ArrayList<>();

    // command name -> ("sub" or "group/sub") -> subcommand
    private final Map<String, Map<String, SubCommand>> routes = new HashMap<>();

    public void register(SlashCommand command) {
        SlashCommandData commandData = command.data();
        String name = commandData.getName();

        super.register(name, command);

        Map<String, SubCommand> commandRoutes = new HashMap<>();

        for (SubCommand subcommand : command.subcommands()) {
            SubcommandData subcommandData = subcommand.data();
            commandData.addSubcommands(subcommandData);
            addRoute(commandRoutes, name, route(null, subcommandData.getName()), subcommand);
        }

        for (SubCommandGroup group : command.groups()) {
            commandData.addSubcommandGroups(group.data());

            for (SubCommand subcommand : group.subcommands()) {
                addRoute(commandRoutes, name, route(group.name(), subcommand.data().getName()), subcommand);
            }
        }

        data.add(commandData);
        routes.put(normalizeKey(name), commandRoutes);
    }

    /**
     * Returns the subcommand for the given path, or {@code null} if the command has no matching subcommand.
     *
     * @param command    the command name
     * @param group      the subcommand group name, or {@code null}
     * @param subcommand the subcommand name, or {@code null}
     * @return the subcommand, or {@code null}
     */
    public SubCommand getSubCommand(String command, String group, String subcommand) {
        if (subcommand == null) {
            return null;
        }

        return routes.getOrDefault(normalizeKey(command), Map.of()).get(route(group, subcommand));
    }

    /**
     * Returns the command data to send to Discord, including subcommands and groups.
     *
     * @return the command data of every registered command
     */
    public List<SlashCommandData> getAllData() {
        return List.copyOf(data);
    }

    @Override
    protected String normalizeKey(String key) {
        return key.toLowerCase(Locale.ROOT);
    }

    private static String route(String group, String subcommand) {
        // '/' is not allowed in Discord command names, so it cannot collide with a real name.
        return group == null ? subcommand : group + "/" + subcommand;
    }

    private static void addRoute(Map<String, SubCommand> routes, String command, String route, SubCommand subcommand) {
        if (routes.putIfAbsent(route, subcommand) != null) {
            throw new IllegalStateException("Duplicate subcommand: /" + command + " " + route.replace('/', ' '));
        }
    }
}
