package dev.keii.dungeons.common;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public abstract class Registry<T> {
    private Map<String, Supplier<T>> registry = new HashMap<>();

    public void register(String key, Supplier<T> factory) {
        registry.put(key, factory);
    }

    public abstract void register(Supplier<T> factory);

    public String[] getKeys() {
        return registry.keySet().toArray(String[]::new);
    }

    public T get(String key) {
        return registry.get(key).get();
    }
}
