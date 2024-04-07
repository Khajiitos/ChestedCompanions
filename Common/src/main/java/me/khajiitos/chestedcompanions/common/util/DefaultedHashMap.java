package me.khajiitos.chestedcompanions.common.util;

import java.util.HashMap;

public class DefaultedHashMap<K,V> extends HashMap<K, V> {
    private final V defaultValue;

    public DefaultedHashMap(V defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public V get(Object key) {
        return this.getOrDefault(key, this.defaultValue);
    }
}
