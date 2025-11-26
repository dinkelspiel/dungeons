package dev.keii.dungeons.item.api.component;

import java.util.ArrayList;
import java.util.List;

import dev.keii.dungeons.item.api.ItemComponent;
import dev.keii.dungeons.item.api.component.trait.ActiveTrait;
import dev.keii.dungeons.item.api.component.trait.ItemTrait;
import dev.keii.dungeons.item.api.component.trait.PassiveTrait;
import dev.keii.dungeons.item.context.ItemAttackContext;
import dev.keii.dungeons.item.context.ItemBuildLoreContext;
import dev.keii.dungeons.item.context.ItemContext;
import dev.keii.dungeons.item.context.ItemUseContext;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class TraitComponent implements ItemComponent {
    @Getter
    private List<ItemTrait> traits;

    public TraitComponent(List<ItemTrait> traits) {
        this.traits = new ArrayList<>(traits);
    }

    @Override
    public void onBuildLore(ItemBuildLoreContext ctx, List<Component> lore) {
        traits.stream()
                .forEach(trait -> {
                    if (!(trait instanceof PassiveTrait))
                        return;

                    lore.add(Component.text("= " + trait.name(), NamedTextColor.GRAY)
                            .decoration(TextDecoration.ITALIC, false));
                    if (trait.description() != null) {
                        lore.add(trait.description());
                    }

                    try {
                        // These lines are to check if the method is overridden in the implementing
                        // class eg. if they have changed the default value
                        // They are guarded against so all passive traits don't spam "0% cooldown
                        // reduction" for example
                        // passiveTrait.getClass().getMethod("damageFraction").getDeclaringClass() ==
                        // passiveTrait
                        // .getClass()

                        if (trait instanceof PassiveTrait passiveTrait) {
                            if (passiveTrait.getClass().getMethod("damageFraction").getDeclaringClass() == passiveTrait
                                    .getClass()) {
                                lore.add(Component
                                        .text("+" + (int) (passiveTrait.damageFraction() * 100) + "%",
                                                NamedTextColor.WHITE)
                                        .decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.BOLD, true)
                                        .append(Component.text(" Weapon Damage", NamedTextColor.GRAY)
                                                .decoration(TextDecoration.ITALIC, false)
                                                .decoration(TextDecoration.BOLD, false)));
                            }

                            if (passiveTrait.getClass().getMethod("cooldownReductionFraction")
                                    .getDeclaringClass() == passiveTrait
                                            .getClass()) {
                                lore.add(Component
                                        .text("+" + (int) (passiveTrait.cooldownReductionFraction() * 100),
                                                NamedTextColor.WHITE)
                                        .decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.BOLD, true)
                                        .append(Component.text("% Ability Cooldown Reduction", NamedTextColor.GRAY)
                                                .decoration(TextDecoration.ITALIC, false)
                                                .decoration(TextDecoration.BOLD, false)));
                            }

                            if (passiveTrait.getClass().getMethod("meleeLifestealFraction")
                                    .getDeclaringClass() == passiveTrait
                                            .getClass()) {
                                lore.add(Component
                                        .text("+" + (int) (passiveTrait.meleeLifestealFraction() * 100),
                                                NamedTextColor.WHITE)
                                        .decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.BOLD, true)
                                        .append(Component.text("% Melee Lifesteal", NamedTextColor.GRAY)
                                                .decoration(TextDecoration.ITALIC, false)
                                                .decoration(TextDecoration.BOLD, false)));
                            }
                        }
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                });

        lore.add(Component.text(""));

        traits.stream()
                .forEach(trait -> {
                    if (!(trait instanceof ActiveTrait activeTrait))
                        return;

                    lore.add(Component.text("On Right Click: ", NamedTextColor.GRAY)
                            .decoration(TextDecoration.ITALIC, false)
                            .append(Component.text(trait.name(), NamedTextColor.YELLOW)
                                    .decoration(TextDecoration.ITALIC, false)));
                    lore.add(Component
                            .text((int) (activeTrait.cooldownSeconds()) + "s", NamedTextColor.WHITE)
                            .decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.BOLD, true)
                            .append(Component.text(" Cooldown", NamedTextColor.GRAY)
                                    .decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.BOLD, false)));
                });

    }

    @Override
    public void onAttackEntity(ItemAttackContext ctx) {
        for (ItemTrait trait : traits) {
            trait.onAttackEntity(ctx);
        }
    }

    @Override
    public void onAttack(ItemContext ctx) {
        for (ItemTrait trait : traits) {
            trait.onAttack(ctx);
        }
    }

    @Override
    public void onUse(ItemUseContext ctx) {
        for (ItemTrait trait : traits) {
            trait.onUse(ctx);
        }
    }

    @Override
    public void onHolderLand(ItemContext ctx) {
        for (ItemTrait trait : traits) {
            trait.onHolderLand(ctx);
        }
    }
}
