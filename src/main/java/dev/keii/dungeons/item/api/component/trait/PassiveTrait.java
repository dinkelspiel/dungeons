package dev.keii.dungeons.item.api.component.trait;

public interface PassiveTrait {
    default double cooldownReductionFraction() {
        return 0;
    }

    default double damageFraction() {
        return 1;
    }

    default double attackSpeedFraction() {
        return 1;
    }

    default double meleeLifestealFraction() {
        return 0;
    }
}
