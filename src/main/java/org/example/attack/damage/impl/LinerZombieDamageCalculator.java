package org.example.attack.damage.impl;

import org.example.attack.Utils;
import org.example.attack.damage.DyingAfterAttackZombieDamageCalculator;
import org.example.attack.damage.ZombieDamageCalculator;
import org.example.model.MyBaseBlock;
import org.example.model.Point;
import org.example.model.Zombie;
import org.example.model.response.UnitsResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class LinerZombieDamageCalculator extends DyingAfterAttackZombieDamageCalculator {
    @Override
    public Zombie.Type getType() {
        return Zombie.Type.liner;
    }

    @Override
    protected List<MyBaseBlock> getDamageDealtBlocks(Zombie zombie, UnitsResponse unitsResponse) {
        MyBaseBlock nearest = Utils.nearestBaseBlock(zombie, unitsResponse);
        if (nearest == null) {
            return Collections.emptyList();
        }
        int xDiff = zombie.direction.deltaX();
        int yDiff = zombie.direction.deltaY();
        List<MyBaseBlock> res = new ArrayList<>();
        res.add(nearest);
        Point point = new Point(nearest.x + xDiff, nearest.y + yDiff);
        while (true) {
            Optional<MyBaseBlock> basePoint = unitsResponse.base.stream()
                    .filter(p -> p.x == point.x && p.y == point.y)
                    .findAny();
            if (basePoint.isEmpty()) {
                break;
            }
            res.add(basePoint.get());
            point.x += xDiff;
            point.y += yDiff;
        }
        return res;
    }
}
