package com.kkoreng.diskkord.core.registry;

import com.kkoreng.diskkord.api.interaction.ButtonInteractionHandler;

import java.util.Locale;

public class ButtonInteractionRegistry extends AbstractRegistry<String, ButtonInteractionHandler> {

    public void register(ButtonInteractionHandler handler) {
        super.register(handler.id(), handler);
    }

    @Override
    protected String normalizeKey(String key) {
        return key.toLowerCase(Locale.ROOT);
    }
}
