package com.kkoreng.diskkord.core.registrar;

import com.kkoreng.diskkord.api.command.SlashCommand;
import com.kkoreng.diskkord.api.command.TextCommand;
import com.kkoreng.diskkord.api.command.context.MessageContextCommand;
import com.kkoreng.diskkord.api.command.context.UserContextCommand;
import com.kkoreng.diskkord.core.registry.MessageContextCommandRegistry;
import com.kkoreng.diskkord.core.registry.SlashCommandRegistry;
import com.kkoreng.diskkord.core.registry.TextCommandRegistry;
import com.kkoreng.diskkord.core.registry.UserContextCommandRegistry;
import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
public final class CommandRegistrar {

    private static final Logger log = LoggerFactory.getLogger(CommandRegistrar.class);

    private Set<Long> guildIds = Set.of();

    private final SlashCommandRegistry slashCommands =
            new SlashCommandRegistry();

    private final TextCommandRegistry textCommands =
            new TextCommandRegistry();

    private final MessageContextCommandRegistry messageContextCommands =
            new MessageContextCommandRegistry();

    private final UserContextCommandRegistry userContextCommands =
            new UserContextCommandRegistry();

    public void register(SlashCommand command) {
        slashCommands.register(command);
    }

    public void register(TextCommand command) {
        textCommands.register(command);
    }

    public void register(MessageContextCommand command) {
        messageContextCommands.register(command);
    }

    public void register(UserContextCommand command) {
        userContextCommands.register(command);
    }

    /**
     * Registers commands in the given servers instead of globally. An empty set means global.
     */
    public void useGuilds(Set<Long> guildIds) {
        this.guildIds = Set.copyOf(guildIds);
    }

    /**
     * Registers every slash and context menu command with Discord, either globally or in each
     * configured server. Must run after JDA is ready, since server commands need the server cache.
     * <p>
     * With sharding this runs once per shard. Global commands belong to the application, so only
     * shard 0 updates them, and each shard only updates the servers it owns.
     */
    public void sync(JDA jda) {
        JDA.ShardInfo shard = jda.getShardInfo();
        boolean primary = shard.getShardId() == 0;

        List<CommandData> data = new ArrayList<>();
        data.addAll(slashCommands.getAllData());
        data.addAll(messageContextCommands.getAllData());
        data.addAll(userContextCommands.getAllData());

        if (guildIds.isEmpty()) {
            if (primary) {
                jda.updateCommands().addCommands(data).queue();
            }
            return;
        }

        // Clear global commands so servers do not show both the old global and the new server version.
        if (primary) {
            jda.updateCommands().queue();
        }

        for (long guildId : guildIds) {
            // Discord assigns a server to shard (guildId >> 22) % shardTotal.
            if ((guildId >> 22) % shard.getShardTotal() != shard.getShardId()) {
                continue;
            }

            Guild guild = jda.getGuildById(guildId);

            if (guild == null) {
                log.warn("Skipping commands for guild {}: the bot is not in that server", guildId);
                continue;
            }

            guild.updateCommands().addCommands(data).queue();
        }
    }

}
