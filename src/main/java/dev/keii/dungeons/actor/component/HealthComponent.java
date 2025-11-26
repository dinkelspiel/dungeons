package dev.keii.dungeons.actor.component;

import dev.keii.dungeons.actor.ActorComponent;
import dev.keii.dungeons.actor.ActorContext;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;

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

    @Override
    public void onBuildTitle(ActorContext ctx, TextComponent.Builder title) {
        title.append(Component.text(
                String.format("♥ %s/%s", (int) getHealth() >= 0 ? (int) getHealth() : 0, (int) getMaxHealth()),
                NamedTextColor.RED));
    }
}
