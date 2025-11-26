package dev.keii.dungeons.actor.component.stats;

public class ActorStats {
    public double cooldownReduction;
    public double meleeLifesteal;

    public ActorStats(double cooldownReduction, double meleeLifesteal) {
        this.cooldownReduction = cooldownReduction;
        this.meleeLifesteal = meleeLifesteal;
    }

    public ActorStats() {
        this(0.0, 0.0);
    }
}
