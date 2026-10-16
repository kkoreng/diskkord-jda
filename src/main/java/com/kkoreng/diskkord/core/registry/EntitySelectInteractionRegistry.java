package com.kkoreng.diskkord.core.registry;

import com.kkoreng.diskkord.api.interaction.EntitySelectInteractionHandler;

import java.util.Locale;

public class EntitySelectInteractionRegistry extends AbstractRegistry<String, EntitySelectInteractionHandler> {

    public void register(EntitySelectInteractionHandler handler) {
        super.register(handler.id(), handler);
    }

    @Override
    protected String normalizeKey(String key) {
        return key.toLowerCase(Locale.ROOT);
    }
}
