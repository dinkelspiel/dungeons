package dev.keii.dungeons.item.api.component;

import java.util.ArrayList;
import java.util.List;

import dev.keii.dungeons.item.api.ItemComponent;
import dev.keii.dungeons.item.api.component.modifier.ItemModifier;
import dev.keii.dungeons.item.api.component.modifier.WeaponModifier;
import dev.keii.dungeons.item.context.ItemAttackContext;
import dev.keii.dungeons.item.context.ItemBuildLoreContext;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class ModifierComponent implements ItemComponent {
    @Getter
    private List<ItemModifier> modifiers;

    public ModifierComponent(List<ItemModifier> modifiers) {
        this.modifiers = new ArrayList<>(modifiers);
    }

    @Override
    public void onBuildLore(ItemBuildLoreContext ctx, List<Component> lore) {
        lore.add(Component.text(String.join(", ", modifiers.stream().map(mod -> mod.name()).toList()),
                NamedTextColor.BLUE));
    }

    @Override
    public void onAttack(ItemAttackContext ctx) {
        for (ItemModifier modifier : modifiers) {
            if (modifier instanceof WeaponModifier weaponModifier) {
                weaponModifier.onAttack(ctx);
            }
        }
    }
}
