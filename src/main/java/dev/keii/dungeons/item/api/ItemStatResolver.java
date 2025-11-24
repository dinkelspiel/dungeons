package dev.keii.dungeons.item.api;

import dev.keii.dungeons.actor.Actor;
import dev.keii.dungeons.item.api.component.ModifierComponent;
import dev.keii.dungeons.item.api.component.WeaponComponent;
import dev.keii.dungeons.item.api.component.modifier.ItemModifier;
import dev.keii.dungeons.item.api.component.modifier.WeaponModifier;
import dev.keii.dungeons.item.context.ItemAttackContext;

public final class ItemStatResolver {
    public ItemStats resolve(Item item, Actor actor, Actor victim) {
        ItemStats stats = new ItemStats();

        WeaponComponent weapon = item.getComponent(WeaponComponent.class);
        ModifierComponent modifiers = item.getComponent(ModifierComponent.class);

        if (weapon != null) {
            stats.setDamage(weapon.damage());
        }

        if (modifiers != null) {
            for (ItemModifier modifier : modifiers.getModifiers()) {
                if (!(modifier instanceof WeaponModifier weaponModifier)) {
                    continue;
                }

                stats.setDamage(
                        weaponModifier.modifyDamage(new ItemAttackContext(item, actor, victim), stats.getDamage()));
            }
        }

        return stats;
    }
}
