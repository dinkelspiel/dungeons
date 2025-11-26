package dev.keii.dungeons.item.api.component.trait;

import dev.keii.dungeons.actor.component.StatusEffectComponent;
import dev.keii.dungeons.actor.component.statuseffect.StatusEffectBurning;
import dev.keii.dungeons.item.context.ItemAttackContext;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class TraitFireAspect implements ItemTrait, PassiveTrait {
    @Override
    public String key() {
        return "fireaspect";
    }

    @Override
    public String name() {
        return "Fire Aspect";
    }

    @Override
    public Component description() {
        return Component.text("Applies fire to enemies.", NamedTextColor.GRAY);
    }

    @Override
    public void onAttackEntity(ItemAttackContext ctx) {
        StatusEffectComponent statusEffectComponent = ctx.getVictim().getComponent(StatusEffectComponent.class);
        if (statusEffectComponent == null)
            return;

        statusEffectComponent.add(new StatusEffectBurning(ctx.actor()), 3);
    }
}
