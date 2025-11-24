package dev.keii.dungeons.item.api.component.modifier;

import dev.keii.dungeons.item.context.ItemAttackContext;

public interface WeaponModifier {
    default double modifyDamage(ItemAttackContext ctx, double baseDamage) {
        return baseDamage;
    }

    default double modifyAttackSpeed(ItemAttackContext ctx, double baseSpeed) {
        return baseSpeed;
    }

    default void onAttack(ItemAttackContext ctx) {
    }
}
