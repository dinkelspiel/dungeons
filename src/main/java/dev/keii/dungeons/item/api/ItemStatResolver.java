package dev.keii.dungeons.item.api;

import dev.keii.dungeons.actor.Actor;
import dev.keii.dungeons.item.api.component.TraitComponent;
import dev.keii.dungeons.item.api.component.WeaponComponent;
import dev.keii.dungeons.item.api.component.trait.ItemTrait;
import dev.keii.dungeons.item.api.component.trait.PassiveTrait;

public final class ItemStatResolver {
    public ItemStats resolve(Item item, Actor actor, Actor victim) {
        ItemStats stats = new ItemStats();

        WeaponComponent weapon = item.getComponent(WeaponComponent.class);
        TraitComponent modifiers = item.getComponent(TraitComponent.class);

        if (weapon != null) {
            stats.setDamage(weapon.damage());
        }

        if (modifiers != null) {
            for (ItemTrait trait : modifiers.getTraits()) {
                if (trait instanceof PassiveTrait passiveTrait) {
                    stats.setDamage(
                            stats.getDamage()
                                    * passiveTrait.damageFraction());
                }
            }
        }

        return stats;
    }
}
