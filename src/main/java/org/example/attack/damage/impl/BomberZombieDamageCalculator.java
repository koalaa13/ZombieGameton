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

public class BomberZombieDamageCalculator extends DyingAfterAttackZombieDamageCalculator {
    @Override
    public Zombie.Type getType() {
        return Zombie.Type.bomber;
    }

    private List<Point> getAllDamaged(Point basePoint) {
        List<Point> bomberHitPoints = new ArrayList<>();
        for (int i = -1; i <= 1; ++i) {
            for (int j = -1; j <= 1; ++j) {
                bomberHitPoints.add(new Point(basePoint.x + i, basePoint.y + j));
            }
        }
        return bomberHitPoints;
    }

    @Override
    protected List<MyBaseBlock> getDamageDealtBlocks(Zombie zombie, UnitsResponse unitsResponse) {
        MyBaseBlock nearest = Utils.nearestBaseBlock(zombie, unitsResponse);
        if (nearest == null) {
            return Collections.emptyList();
        }
        List<Point> damagedPoints = getAllDamaged(nearest);
        return unitsResponse.base.stream()
                .filter(b -> damagedPoints.stream().anyMatch(dp -> dp.x == b.x && dp.y == b.y))
                .toList();
    }
}
