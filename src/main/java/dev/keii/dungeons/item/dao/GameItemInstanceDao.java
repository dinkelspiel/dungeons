package dev.keii.dungeons.item.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import dev.keii.dungeons.common.Dao;
import dev.keii.dungeons.item.model.GameItemInstance;
import lombok.Getter;

public class GameItemInstanceDao implements Dao<GameItemInstance> {
    @Getter
    private static Map<UUID, GameItemInstance> data = new HashMap<>();

    @Override
    public GameItemInstance findById(UUID id) {
        return data.get(id);
    }

    @Override
    public List<GameItemInstance> findAll() {
        return data.values().stream().toList();
    }

    @Override
    public UUID insert(GameItemInstance item) {
        UUID id = UUID.randomUUID();
        data.put(id, item);
        return id;
    }

    @Override
    public void update(GameItemInstance item) {
        data.put(item.getId(), item);
    }

    @Override
    public GameItemInstance save(GameItemInstance item) {
        if (item.getId() == null) {
            UUID id = insert(item);
            item.setId(id);
            return item;
        } else {
            update(item);
            return item;
        }
    }

    @Override
    public void delete(UUID id) {
        data.remove(id);
    }
}
