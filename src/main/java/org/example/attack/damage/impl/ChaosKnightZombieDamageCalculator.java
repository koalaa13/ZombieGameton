package org.example.attack.damage.impl;

import org.example.attack.damage.DyingAfterAttackZombieDamageCalculator;
import org.example.model.MyBaseBlock;
import org.example.model.Zombie;
import org.example.model.response.UnitsResponse;

import java.util.Collections;
import java.util.List;

public class ChaosKnightZombieDamageCalculator extends DyingAfterAttackZombieDamageCalculator {
    @Override
    public Zombie.Type getType() {
        return Zombie.Type.chaos_knight;
    }

    // TODO ???????
    @Override
    protected List<MyBaseBlock> getDamageDealtBlocks(Zombie zombie, UnitsResponse unitsResponse) {
        return Collections.emptyList();
    }
}
