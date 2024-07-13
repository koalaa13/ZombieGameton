package org.example.attack.damage.impl;

import org.example.attack.Utils;
import org.example.attack.damage.DyingAfterAttackZombieDamageCalculator;
import org.example.attack.damage.ZombieDamageCalculator;
import org.example.model.MyBaseBlock;
import org.example.model.Zombie;
import org.example.model.response.UnitsResponse;

import java.util.Collections;
import java.util.List;

public class NormalZombieDamageCalculator extends DyingAfterAttackZombieDamageCalculator {
    @Override
    public Zombie.Type getType() {
        return Zombie.Type.normal;
    }

    @Override
    public List<MyBaseBlock> getDamageDealtBlocks(Zombie zombie, UnitsResponse unitsResponse) {
        MyBaseBlock nearest = Utils.nearestBaseBlock(zombie, unitsResponse);
        return nearest == null ? Collections.emptyList() : List.of(nearest);
    }
}
