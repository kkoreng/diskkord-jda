package com.kkoreng.diskkord.core.registry;

import com.kkoreng.diskkord.api.command.SlashCommand;

import java.util.Locale;

public final class SlashCommandRegistry extends AbstractRegistry<String, SlashCommand> {

    public void register(SlashCommand command) {
        super.register(command.data().getName(), command);
    }

    @Override
    protected String normalizeKey(String key) {
        return key.toLowerCase(Locale.ROOT);
    }
}
