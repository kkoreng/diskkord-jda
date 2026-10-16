package com.kkoreng.diskkord.core.registry;

import com.kkoreng.diskkord.api.interaction.StringSelectInteractionHandler;

import java.util.Locale;

public class StringSelectInteractionRegistry extends AbstractRegistry<String, StringSelectInteractionHandler> {

    public void register(StringSelectInteractionHandler handler) {
        super.register(handler.id(), handler);
    }

    @Override
    protected String normalizeKey(String key) {
        return key.toLowerCase(Locale.ROOT);
    }
}
