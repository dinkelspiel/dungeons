package dev.keii.dungeons.actor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import dev.keii.dungeons.actor.component.HealthComponent;
import dev.keii.dungeons.common.ComponentContainer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.entity.Entity;

public final class Actor implements ComponentContainer<ActorComponent> {
    private final UUID id;
    private final Entity entity;
    private final Map<Class<? extends ActorComponent>, ActorComponent> components = new HashMap<>();
    private final List<ActorComponent> componentList = new ArrayList<>();

    public Actor(Entity entity) {
        this.id = entity.getUuid();
        this.entity = entity;
    }

    public UUID getId() {
        return id;
    }

    public Entity getEntity() {
        return entity;
    }

    public <T extends ActorComponent> T getComponent(Class<T> type) {
        ActorComponent comp = components.get(type);
        if (comp == null) {
            return null;
        }
        return type.cast(comp);
    }

    public void addComponent(ActorComponent component) {
        components.put(component.getClass(), component);
        componentList.add(component);
    }

    public List<ActorComponent> components() {
        return Collections.unmodifiableList(componentList);
    }

    public Component getActionBar() {
        HealthComponent healthComponent = getComponent(HealthComponent.class);
        TextComponent.Builder builder = Component.text();

        if (healthComponent != null) {
            builder.append(Component.text(
                    String.format("♥ %s/%s", (int) healthComponent.getHealth(), (int) healthComponent.getMaxHealth()),
                    NamedTextColor.RED));
        }

        return builder.build();
    }

    public void tick(long time) {
        for (ActorComponent component : components()) {
            component.onTick(new ActorContext(this), time);
        }
    }

    @Override
    public boolean hasComponent(Class<? extends ActorComponent> type) {
        return getComponent(type) != null;
    }
}