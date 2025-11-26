package dev.keii.dungeons.item.api.component.trait;

public class TraitTranscendentCooldown implements ItemTrait, PassiveTrait {

    @Override
    public String key() {
        return "transcendent_cooldown";
    }

    @Override
    public String name() {
        return "Transcendent Cooldown";
    }

    @Override
    public double cooldownReductionFraction() {
        return 0.2;
    }
}
