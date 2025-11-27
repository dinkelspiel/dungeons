package dev.keii.dungeons.common;

import lombok.Getter;

// TODO: It might be worth to split this into a DamageSource and DamageType class since a Magic item might want to apply knockback and so on
public enum DamageType {
    MELEE(true),
    MAGIC(false);

    @Getter
    private boolean applyKnockback;

    private DamageType(boolean applyKnockback) {
        this.applyKnockback = applyKnockback;
    }
}
