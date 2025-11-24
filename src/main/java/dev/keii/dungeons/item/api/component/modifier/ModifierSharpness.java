package dev.keii.dungeons.item.api.component.modifier;

import dev.keii.dungeons.item.context.ItemAttackContext;

public class ModifierSharpness implements ItemModifier, WeaponModifier {
    @Override
    public String key() {
        return "sharpness";
    }

    @Override
    public String name() {
        return "Sharpness";
    }

    @Override
    public double modifyDamage(ItemAttackContext ctx, double baseDamage) {
        return baseDamage * 2;
    }
}
