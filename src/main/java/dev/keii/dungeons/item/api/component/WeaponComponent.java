package dev.keii.dungeons.item.api.component;

import java.util.List;

import dev.keii.dungeons.item.api.ItemComponent;
import dev.keii.dungeons.item.context.ItemBuildLoreContext;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public record WeaponComponent(float damage) implements ItemComponent {
    @Override
    public void onBuildLore(ItemBuildLoreContext ctx, List<Component> lore) {
        lore.add(Component.text("Damage: ", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
                .append(Component.text(damage, NamedTextColor.YELLOW)));
    }

}
