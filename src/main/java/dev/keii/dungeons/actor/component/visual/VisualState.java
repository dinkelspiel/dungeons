package dev.keii.dungeons.actor.component.visual;

import lombok.Getter;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.block.Block;

public class VisualState {
    @Getter
    Block block;

    @Getter
    Vec scale;

    public VisualState(Block block, Vec scale) {
        this.block = block;
        this.scale = scale;
    }
}
