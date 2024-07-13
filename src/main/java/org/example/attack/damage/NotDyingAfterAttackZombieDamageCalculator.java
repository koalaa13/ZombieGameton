package org.example.attack.damage;

import org.example.attack.Utils;
import org.example.model.MyBaseBlock;
import org.example.model.Zombie;
import org.example.model.response.UnitsResponse;

import java.util.List;

public abstract class NotDyingAfterAttackZombieDamageCalculator extends ZombieDamageCalculator {
    @Override
    public long getDamage(Zombie zombie, UnitsResponse unitsResponse, long stepsCount) {
        if (zombie.type != getType()) {
            throw new RuntimeException("can't calculate damage for zombie type " + zombie.type + " using " + this.getClass().getName());
        }
        List<MyBaseBlock> damageDealtBlocks = getDamageDealtBlocks(zombie, unitsResponse);
        long damageDealt = 0;
        long turnsLeft = stepsCount;
        for (MyBaseBlock block : damageDealtBlocks) {
            long toBreak = Utils.turnsToBreak(zombie, block);
            if (turnsLeft <= toBreak) {
                damageDealt += zombie.attack * turnsLeft;
                turnsLeft = 0;
                break;
            }
            damageDealt += block.health;
            turnsLeft -= toBreak;
        }
        return damageDealt;
    }
}
