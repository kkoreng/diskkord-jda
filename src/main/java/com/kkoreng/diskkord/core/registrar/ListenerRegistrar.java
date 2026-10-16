package com.kkoreng.diskkord.core.registrar;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.EventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class ListenerRegistrar {

    private final List<EventListener> listeners = new ArrayList<>();
    private boolean installed;

    public void register(EventListener... listeners) {
        if (installed) {
            throw new IllegalStateException(
                    "Listeners have already been installed"
            );
        }

        for (EventListener listener : listeners) {
            this.listeners.add(Objects.requireNonNull(listener));
        }
    }

    public void install(JDABuilder builder) {
        Objects.requireNonNull(builder);

        if (installed) {
            throw new IllegalStateException(
                    "Listeners have already been installed"
            );
        }

        builder.addEventListeners(listeners.toArray());
        installed = true;
    }
}