package dev.keii.dungeons.item.api.component.trait;

public class TraitSharpness implements ItemTrait, PassiveTrait {
    @Override
    public String key() {
        return "sharpness";
    }

    @Override
    public String name() {
        return "Sharpness";
    }

    @Override
    public double damageFraction() {
        return 2;
    }
}
