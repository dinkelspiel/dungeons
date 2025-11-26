package dev.keii.dungeons.item.api.component.trait;

public class TraitMeleeLiftsteal implements ItemTrait, PassiveTrait {

    @Override
    public String key() {
        return "melee_lifesteal";
    }

    @Override
    public String name() {
        return "Melee Lifesteal";
    }

    @Override
    public double meleeLifestealFraction() {
        return 0.2;
    }

}
