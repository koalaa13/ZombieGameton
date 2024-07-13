package org.example.attack.damage.impl;

import org.example.attack.Utils;
import org.example.attack.damage.NotDyingAfterAttackZombieDamageCalculator;
import org.example.model.MyBaseBlock;
import org.example.model.Zombie;
import org.example.model.response.UnitsResponse;

import java.util.List;

public class JuggernautZombieDamageCalculator extends NotDyingAfterAttackZombieDamageCalculator {
    @Override
    public Zombie.Type getType() {
        return Zombie.Type.juggernaut;
    }

    @Override
    protected List<MyBaseBlock> getDamageDealtBlocks(Zombie zombie, UnitsResponse unitsResponse) {
        return unitsResponse.base.stream()
                .filter(b -> Utils.canReachPoint(zombie, b))
                .sorted((p1, p2) -> {
                    if (zombie.direction == Zombie.Direction.up) {
                        return -Long.compare(p1.y, p2.y);
                    }
                    if (zombie.direction == Zombie.Direction.down) {
                        return Long.compare(p1.y, p2.y);
                    }
                    if (zombie.direction == Zombie.Direction.left) {
                        return -Long.compare(p1.x, p2.x);
                    }
                    if (zombie.direction == Zombie.Direction.right) {
                        return Long.compare(p1.x, p2.x);
                    }
                    throw new RuntimeException("zombie with bad direction " + zombie.direction);
                })
                .toList();
    }
}
