package com.kkoreng.diskkord.core.registry;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractRegistry<K, V> {

    private final Map<K, V> entries = new HashMap<>();

    protected K normalizeKey(K key) {
        return key;
    }

    public void register(K key, V value) {
        K normalized = normalizeKey(key);

        if (entries.putIfAbsent(normalized, value) != null) {
            throw new IllegalStateException("IllegalStateException, key: " + key);
        }
    }

    public V get(K key) {
        return entries.get(normalizeKey(key));
    }

    public boolean contains(K key) {
        return entries.containsKey(normalizeKey(key));
    }

    public V unregister(K key) {
        return entries.remove(normalizeKey(key));
    }

    public Collection<V> getAll() {
        return Collections.unmodifiableCollection(entries.values());
    }

    public void clear() {
        entries.clear();
    }
}
