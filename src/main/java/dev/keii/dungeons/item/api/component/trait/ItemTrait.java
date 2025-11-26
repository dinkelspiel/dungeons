package dev.keii.dungeons.item.api.component.trait;

import dev.keii.dungeons.item.api.ItemComponent;
import net.kyori.adventure.text.Component;

public interface ItemTrait extends ItemComponent {
    String key();

    String name();

    default Component description() {
        return null;
    }
}
