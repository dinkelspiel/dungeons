package dev.keii.dungeons.actor.component.statuseffect;

public interface StatusEffect {
    /**
     * If a StatusEffect on an Actor exists of this type already should they stack
     * or should the duration be refreshed on the existing one
     * 
     * @return
     */
    boolean isStacking();

    default void onApply(StatusEffectContext ctx) {
    }

    default void onTick(StatusEffectContext ctx) {
    }

    default void onRemove(StatusEffectContext ctx) {
    }

    default double onIncomingDamage(StatusEffectContext ctx, StatusEffectContext src, double amount) {
        return amount;
    }

    default double onOutgoingDamage(StatusEffectContext ctx, StatusEffectContext target, double amount) {
        return amount;
    }
}
