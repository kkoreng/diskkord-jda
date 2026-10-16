package com.kkoreng.diskkord.api.interaction;

import java.util.List;
import java.util.function.Function;

/**
 * Arguments attached to an interaction custom ID.
 * <p>
 * For the custom ID {@code ticket.close:42:open}, the arguments are {@code ["42", "open"]}.
 * Values are stored as strings and converted on access.
 */
public final class Args {

    private static final Args EMPTY = new Args(List.of());

    private final List<String> values;

    private Args(List<String> values) {
        this.values = values;
    }

    /**
     * Creates arguments from the given values.
     *
     * @param values the argument values
     * @return the arguments
     */
    public static Args of(List<String> values) {
        return values.isEmpty() ? EMPTY : new Args(List.copyOf(values));
    }

    /**
     * Creates arguments from the given values.
     *
     * @param values the argument values
     * @return the arguments
     */
    public static Args of(String... values) {
        return of(List.of(values));
    }

    /**
     * Returns empty arguments.
     *
     * @return empty arguments
     */
    public static Args empty() {
        return EMPTY;
    }

    public int size() {
        return values.size();
    }

    public boolean isEmpty() {
        return values.isEmpty();
    }

    /**
     * Returns all arguments as an unmodifiable list.
     *
     * @return the argument values
     */
    public List<String> asList() {
        return values;
    }

    public String getString(int index) {
        return at(index);
    }

    public int getInt(int index) {
        return parse(index, Integer::parseInt, "int");
    }

    public long getLong(int index) {
        return parse(index, Long::parseLong, "long");
    }

    /**
     * Returns the argument as a boolean. Only {@code "true"} and {@code "false"} are accepted, ignoring case.
     */
    public boolean getBoolean(int index) {
        String value = at(index);

        if (value.equalsIgnoreCase("true")) return true;
        if (value.equalsIgnoreCase("false")) return false;

        throw new IllegalArgumentException("Argument " + index + " is not a boolean: " + value);
    }

    /**
     * Returns the argument as an enum constant, matched by {@link Enum#name()}.
     */
    public <T extends Enum<T>> T getEnum(int index, Class<T> type) {
        return parse(index, value -> Enum.valueOf(type, value), type.getSimpleName());
    }

    private String at(int index) {
        if (index < 0 || index >= values.size()) {
            throw new IllegalArgumentException("No argument at index " + index + " (size " + values.size() + ")");
        }

        return values.get(index);
    }

    private <T> T parse(int index, Function<String, T> parser, String type) {
        String value = at(index);

        try {
            return parser.apply(value);
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("Argument " + index + " is not a " + type + ": " + value, e);
        }
    }

    @Override
    public String toString() {
        return values.toString();
    }
}
