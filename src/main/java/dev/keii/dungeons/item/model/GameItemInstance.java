package dev.keii.dungeons.item.model;

import java.util.UUID;

import dev.keii.dungeons.Dungeons;
import dev.keii.dungeons.item.api.Item;
import lombok.Getter;
import lombok.Setter;

public class GameItemInstance {
    @Getter
    @Setter
    private UUID id;

    @Getter
    @Setter
    private String key;

    public GameItemInstance(String key) {
        this.key = key;
    }

    public Item getItem() {
        return Dungeons.getItemRegistry().get(getKey());
    }
}
