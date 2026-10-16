package com.kkoreng.diskkord.core.registry;

import com.kkoreng.diskkord.api.interaction.ModalInteractionHandler;

import java.util.Locale;

public class ModalInteractionRegistry extends AbstractRegistry<String, ModalInteractionHandler> {

    public void register(ModalInteractionHandler handler) {
        super.register(handler.id(), handler);
    }

    @Override
    protected String normalizeKey(String key) {
        return key.toLowerCase(Locale.ROOT);
    }
}
