package com.kkoreng.diskkord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import java.util.Objects;

/**
 * A running Diskkord bot. Create one with {@link #builder(JDABuilder)}.
 */
public final class Diskkord {

    private final JDA jda;

    Diskkord(JDA jda) {
        this.jda = Objects.requireNonNull(jda);
    }

    /**
     * Creates a builder that installs Diskkord into the given {@link JDABuilder}.
     *
     * @param jdaBuilder the JDABuilder to build the bot with
     * @return a new builder
     */
    public static DiskkordBuilder builder(JDABuilder jdaBuilder) {
        return new DiskkordBuilder(jdaBuilder);
    }

    /**
     * Returns the JDA instance.
     *
     * @return the JDA instance
     */
    public JDA getJDA() {
        return jda;
    }
}
