package com.kkoreng.diskkord.core.registrar;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

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
        install(builder::addEventListeners);
    }

    public void install(DefaultShardManagerBuilder builder) {
        Objects.requireNonNull(builder);
        install(builder::addEventListeners);
    }

    private void install(Consumer<Object[]> adder) {
        if (installed) {
            throw new IllegalStateException(
                    "Listeners have already been installed"
            );
        }

        adder.accept(listeners.toArray());
        installed = true;
    }
}