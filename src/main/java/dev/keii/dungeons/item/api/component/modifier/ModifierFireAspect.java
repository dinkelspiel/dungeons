package dev.keii.dungeons.item.api.component.modifier;

import dev.keii.dungeons.actor.component.StatusEffectComponent;
import dev.keii.dungeons.actor.component.statuseffect.StatusEffectBurning;
import dev.keii.dungeons.item.context.ItemAttackContext;

public class ModifierFireAspect implements ItemModifier, WeaponModifier {
    @Override
    public String key() {
        return "fireaspect";
    }

    @Override
    public String name() {
        return "Fire Aspect";
    }

    @Override
    public void onAttack(ItemAttackContext ctx) {
        StatusEffectComponent statusEffectComponent = ctx.getVictim().getComponent(StatusEffectComponent.class);
        if (statusEffectComponent == null)
            return;

        statusEffectComponent.add(new StatusEffectBurning(), 3);
    }
}
