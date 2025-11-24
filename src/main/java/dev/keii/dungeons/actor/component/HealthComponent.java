package dev.keii.dungeons.actor.component;

import dev.keii.dungeons.actor.ActorComponent;
import lombok.Getter;
import lombok.Setter;

public class HealthComponent implements ActorComponent {
    @Getter
    @Setter
    private float health;

    @Getter
    @Setter
    private float maxHealth;

    public HealthComponent(float health, float maxHealth) {
        this.health = health;
        this.maxHealth = maxHealth;
    }
}
