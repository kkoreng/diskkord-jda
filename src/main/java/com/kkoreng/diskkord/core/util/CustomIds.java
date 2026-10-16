package com.kkoreng.diskkord.core.util;

import com.kkoreng.diskkord.api.interaction.Args;

import java.util.Objects;
import java.util.regex.Pattern;

public final class CustomIds {
    private static final char SEPARATOR = ':';
    private static final int MAX_LENGTH = 100;

    private CustomIds() {}

    public static String build(String id, Object... args) {
        validateId(id);
        StringBuilder sb = new StringBuilder(id);
        for (Object arg : args) {
            Objects.requireNonNull(arg, "Argument must not be null");
            String value = arg instanceof Enum<?> e ? e.name() : arg.toString();
            if (value.indexOf(SEPARATOR) >= 0) {
                throw new IllegalArgumentException("Argument must not contain '" + SEPARATOR + "': " + value);
            }
            sb.append(SEPARATOR).append(value);
        }
        if (sb.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Custom ID exceeds " + MAX_LENGTH + " chars: " + sb);
        }
        return sb.toString();
    }

    public static Parsed parse(String customId) {
        int sep = customId.indexOf(SEPARATOR);
        if (sep < 0) return new Parsed(customId, Args.empty());
        return new Parsed(
                customId.substring(0, sep),
                Args.of(customId.substring(sep + 1).split(Pattern.quote(String.valueOf(SEPARATOR)), -1))
        );
    }

    public static void validateId(String id) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("ID must not be blank");
        if (id.indexOf(SEPARATOR) >= 0) throw new IllegalArgumentException("ID must not contain '" + SEPARATOR + "': " + id);
    }

    public record Parsed(String id, Args args) {}
}