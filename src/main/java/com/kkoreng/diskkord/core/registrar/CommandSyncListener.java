package com.kkoreng.diskkord.core.registrar;

import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * Syncs application commands once JDA is ready, when the server cache is available for guild commands.
 */
public final class CommandSyncListener extends ListenerAdapter {

    private final CommandRegistrar commands;

    public CommandSyncListener(CommandRegistrar commands) {
        this.commands = commands;
    }

    @Override
    public void onReady(ReadyEvent event) {
        commands.sync(event.getJDA());
    }
}
