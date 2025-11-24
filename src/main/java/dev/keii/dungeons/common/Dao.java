package dev.keii.dungeons.common;

import java.util.List;
import java.util.UUID;

public interface Dao<T> {
    T findById(UUID id);

    List<T> findAll();

    UUID insert(T item);

    void update(T item);

    T save(T item);

    void delete(UUID id);
}