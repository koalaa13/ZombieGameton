package org.example.attack.damage;

import org.example.attack.Utils;
import org.example.model.Zombie;
import org.example.model.response.UnitsResponse;

public abstract class DyingAfterAttackZombieDamageCalculator extends ZombieDamageCalculator {
    @Override
    public long getDamage(Zombie zombie, UnitsResponse unitsResponse, long stepsCount) {
        if (zombie.type != getType()) {
            throw new RuntimeException("can't calculate damage for zombie type " + zombie.type + " using " + this.getClass().getName());
        }
        if (Utils.turnsToReachBase(zombie, unitsResponse) > stepsCount) {
            return 0;
        }
        return getDamageDealtBlocks(zombie, unitsResponse).stream()
                .mapToLong(b -> Math.min(b.health, zombie.attack))
                .sum();
    }
}
