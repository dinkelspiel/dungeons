package dev.keii.dungeons.actor.component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import dev.keii.dungeons.actor.Actor;
import dev.keii.dungeons.actor.ActorComponent;
import dev.keii.dungeons.actor.component.stats.ActorStats;
import dev.keii.dungeons.item.api.Item;
import dev.keii.dungeons.item.api.component.TraitComponent;
import dev.keii.dungeons.item.api.component.trait.ItemTrait;
import dev.keii.dungeons.item.api.component.trait.PassiveTrait;
import dev.keii.dungeons.item.model.GameItemInstance;
import lombok.Getter;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import net.minestom.server.utils.MathUtils;

public class ActorStatsComponent implements ActorComponent {
    @Getter
    private Integer[] itemSlots;

    public ActorStatsComponent(Integer[] itemSlots) {
        this.itemSlots = itemSlots;
    }

    public ActorStats calculate(Actor actor) {
        if (!(actor.getEntity() instanceof Player player))
            return new ActorStats();

        ActorStats actorStats = new ActorStats();

        List<ItemStack> itemStacks = Arrays.asList(itemSlots).stream()
                .map(slot -> player.getInventory().getItemStack(slot)).filter(stack -> stack != null).toList();
        itemStacks = new ArrayList<>(itemStacks);
        itemStacks.add(player.getItemInMainHand());

        for (ItemStack stack : itemStacks) {
            Optional<GameItemInstance> inst = actor.getItem(stack);

            if (inst.isEmpty())
                continue;

            Item item = inst.get().getItem();
            TraitComponent traits = item.getComponent(TraitComponent.class);
            if (traits == null)
                continue;

            for (ItemTrait t : traits.getTraits()) {
                try {
                    if (t instanceof PassiveTrait passiveTrait) {
                        if (passiveTrait.getClass().getMethod("cooldownReductionFraction")
                                .getDeclaringClass() == passiveTrait
                                        .getClass()) {
                            actorStats.cooldownReduction += passiveTrait.cooldownReductionFraction();
                        }

                        if (passiveTrait.getClass().getMethod("meleeLifestealFraction")
                                .getDeclaringClass() == passiveTrait
                                        .getClass()) {
                            actorStats.meleeLifesteal += passiveTrait.meleeLifestealFraction();
                        }
                    }
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }

        actorStats.cooldownReduction = MathUtils.clamp(actorStats.cooldownReduction, 0, 1);
        actorStats.meleeLifesteal = actorStats.meleeLifesteal < 0 ? 0 : actorStats.meleeLifesteal;

        return actorStats;
    }
}
