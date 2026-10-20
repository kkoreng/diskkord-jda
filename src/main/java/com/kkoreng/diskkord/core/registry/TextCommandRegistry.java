package com.kkoreng.diskkord.core.registry;

import com.kkoreng.diskkord.api.command.TextCommand;

import java.util.Locale;

/**
 * Stores text commands by name and by each of their aliases.
 */
public class TextCommandRegistry extends AbstractRegistry<String, TextCommand> {

    public void register(TextCommand command) {
        super.register(validate(command.name(), command), command);

        for (String alias : command.aliases()) {
            super.register(validate(alias, command), command);
        }
    }

    @Override
    protected String normalizeKey(String key) {
        return key.toLowerCase(Locale.ROOT);
    }

    private static String validate(String key, TextCommand command) {
        // The dispatcher matches the first whitespace-separated word, so a key with spaces could never match.
        if (key == null || key.isBlank() || key.chars().anyMatch(Character::isWhitespace)) {
            throw new IllegalArgumentException(
                    "Invalid text command name or alias '" + key + "' in " + command.getClass().getName()
            );
        }

        return key;
    }
}
