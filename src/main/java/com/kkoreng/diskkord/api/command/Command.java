package com.kkoreng.diskkord.api.command;

import net.dv8tion.jda.api.events.GenericEvent;

public interface Command <E extends GenericEvent> {
    void execute(E event);
}