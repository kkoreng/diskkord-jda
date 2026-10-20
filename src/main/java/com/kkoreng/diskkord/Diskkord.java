package com.kkoreng.diskkord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

import java.util.Objects;

/**
 * A running Diskkord bot. Create one with {@link #builder(JDABuilder)}
 * or {@link #builder(DefaultShardManagerBuilder)}.
 */
public final class Diskkord {

    private final JDA jda;
    private final ShardManager shardManager;

    Diskkord(JDA jda) {
        this.jda = Objects.requireNonNull(jda);
        this.shardManager = null;
    }

    Diskkord(ShardManager shardManager) {
        this.jda = null;
        this.shardManager = Objects.requireNonNull(shardManager);
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
     * Creates a builder that installs Diskkord into every shard of the given
     * {@link DefaultShardManagerBuilder}.
     *
     * @param shardManagerBuilder the DefaultShardManagerBuilder to build the bot with
     * @return a new builder
     */
    public static DiskkordBuilder builder(DefaultShardManagerBuilder shardManagerBuilder) {
        return new DiskkordBuilder(shardManagerBuilder);
    }

    /**
     * Returns the JDA instance.
     *
     * @return the JDA instance
     * @throws IllegalStateException if the bot was built with a shard manager
     */
    public JDA getJDA() {
        if (jda == null) {
            throw new IllegalStateException("Diskkord was built with a shard manager; use getShardManager()");
        }
        return jda;
    }

    /**
     * Returns the shard manager.
     *
     * @return the shard manager
     * @throws IllegalStateException if the bot was built with a JDABuilder
     */
    public ShardManager getShardManager() {
        if (shardManager == null) {
            throw new IllegalStateException("Diskkord was built with a JDABuilder; use getJDA()");
        }
        return shardManager;
    }
}
