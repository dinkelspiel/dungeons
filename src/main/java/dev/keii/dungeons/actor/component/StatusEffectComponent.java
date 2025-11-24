package dev.keii.dungeons.actor.component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import dev.keii.dungeons.actor.Actor;
import dev.keii.dungeons.actor.ActorComponent;
import dev.keii.dungeons.actor.ActorContext;
import dev.keii.dungeons.actor.component.statuseffect.StatusEffect;
import dev.keii.dungeons.actor.component.statuseffect.StatusEffectContext;
import dev.keii.dungeons.actor.component.statuseffect.StatusEffectInstance;

public class StatusEffectComponent implements ActorComponent {
    private Actor ctx;

    public StatusEffectComponent(Actor ctx) {
        this.ctx = ctx;
    }

    private final List<StatusEffectInstance> active = new ArrayList<>();

    public void add(StatusEffect effect, double durationSeconds) {
        if (!effect.isStacking() && has(effect.getClass())) {
            StatusEffectInstance instance = get(effect.getClass());
            instance.setRemaining(durationSeconds);
            return;
        }

        // handle stacking / refreshing
        var inst = new StatusEffectInstance(effect, ctx, durationSeconds);
        active.add(inst);
        effect.onApply(inst.ctx());
    }

    public boolean has(Class<? extends StatusEffect> type) {
        return active.stream().anyMatch(i -> type.isInstance(i.effect()));
    }

    public StatusEffectInstance get(Class<? extends StatusEffect> type) {
        Optional<StatusEffectInstance> effect = active.stream().filter(i -> type.isInstance(i.effect())).findFirst();
        if (effect.isEmpty()) {
            return null;
        }
        return effect.get();
    }

    @Override
    public void onTick(ActorContext ctx, long time) {
        var it = active.iterator();
        while (it.hasNext()) {

            var inst = it.next();
            inst.tick();

            inst.effect().onTick(inst.ctx());

            if (inst.isExpired()) {
                inst.effect().onRemove(inst.ctx());
                it.remove();
            }
        }
    }

    public double modifyIncomingDamage(StatusEffectContext ctx, StatusEffectContext src, double amount) {
        double out = amount;
        for (var inst : active) {
            out = inst.effect().onIncomingDamage(inst.ctx(), src, out);
        }
        return out;
    }
}
