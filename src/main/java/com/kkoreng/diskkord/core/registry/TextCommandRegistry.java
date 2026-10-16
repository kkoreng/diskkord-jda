package com.kkoreng.diskkord.core.registry;

import com.kkoreng.diskkord.api.command.TextCommand;

import java.util.Locale;

public class TextCommandRegistry extends AbstractRegistry<String, TextCommand> {

    public void register(TextCommand command) {
        super.register(command.name(), command);
    }

    @Override
    protected String normalizeKey(String key) {
        return key.toLowerCase(Locale.ROOT);
    }
}
