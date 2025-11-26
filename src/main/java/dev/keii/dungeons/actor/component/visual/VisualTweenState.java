package dev.keii.dungeons.actor.component.visual;

import net.minestom.server.coordinate.Vec;

public class VisualTweenState {
    public Vec fromScale;
    public Vec toScale;

    public VisualTweenState(Vec fromScale, Vec toScale) {
        this.fromScale = fromScale;
        this.toScale = toScale;
    }
}
