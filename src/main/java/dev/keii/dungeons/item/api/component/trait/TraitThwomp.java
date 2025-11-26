package dev.keii.dungeons.item.api.component.trait;

import dev.keii.dungeons.actor.component.StatusEffectComponent;
import dev.keii.dungeons.actor.component.statuseffect.StatusEffectThwomp;
import dev.keii.dungeons.item.context.ItemUseContext;
import dev.keii.dungeons.util.MessageFormatter;
import dev.keii.dungeons.util.SoundUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.sound.SoundEvent;

public class TraitThwomp extends ActiveTrait {

    @Override
    public String key() {
        return "thwomp";
    }

    @Override
    public String name() {
        return "Thwomp";
    }

    @Override
    public Component description() {
        return Component.text("Slams the ground, dealing damage and stunning nearby enemies.", NamedTextColor.GRAY);
    }

    @Override
    public double cooldownSeconds() {
        return 4.0;
    }

    @Override
    public void onUse(ItemUseContext ctx) {
        if (ctx.entity().isOnGround()) {
            SoundUtil.playInRadius(ctx.entity().getInstance(), ctx.entity().getPosition(),
                    SoundEvent.BLOCK_NOTE_BLOCK_BASS);
            ctx.audience().sendMessage(MessageFormatter.error("You must use this ability in the air"));
            return;
        }

        ctx.entity().setVelocity(ctx.entity().getVelocity().mul(0.1));
        StatusEffectComponent statusEffectComponent = ctx.actor().getComponent(StatusEffectComponent.class);
        if (statusEffectComponent == null)
            return;

        statusEffectComponent.addIndefinete(new StatusEffectThwomp());
    }

}
