package com.kkoreng.diskkord.core.registrar;

import com.kkoreng.diskkord.api.interaction.*;
import com.kkoreng.diskkord.core.registry.*;
import com.kkoreng.diskkord.core.util.CustomIds;
import lombok.Getter;

import java.util.Objects;

@Getter
public final class InteractionRegistrar {

    private final ButtonInteractionRegistry buttons = new ButtonInteractionRegistry();

    private final ModalInteractionRegistry modals = new ModalInteractionRegistry();

    private final StringSelectInteractionRegistry stringSelects = new StringSelectInteractionRegistry();

    private final EntitySelectInteractionRegistry entitySelects = new EntitySelectInteractionRegistry();

    public void register(ButtonInteractionHandler handler) {
        Objects.requireNonNull(handler);
        CustomIds.validateId(handler.id());
        buttons.register(handler);
    }

    public void register(ModalInteractionHandler handler) {
        Objects.requireNonNull(handler);
        CustomIds.validateId(handler.id());
        modals.register(handler);
    }

    public void register(StringSelectInteractionHandler handler) {
        Objects.requireNonNull(handler);
        CustomIds.validateId(handler.id());
        stringSelects.register(handler);
    }

    public void register(EntitySelectInteractionHandler handler) {
        Objects.requireNonNull(handler);
        CustomIds.validateId(handler.id());
        entitySelects.register(handler);
    }
}