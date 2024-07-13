package org.example.attack.impl;

import org.example.attack.Attacker;
import org.example.model.EnemyBaseBlock;
import org.example.model.MyBaseBlock;
import org.example.model.Point;
import org.example.model.Zombie;
import org.example.model.request.AttackRequest;
import org.example.model.response.UnitsResponse;

import java.util.*;

import static org.example.attack.Utils.*;

public class PriorityAttacker implements Attacker {

    private static final Map<Zombie.Type, Integer> Priority = Map.of(
            Zombie.Type.juggernaut, 5000,
            Zombie.Type.bomber, 4500,
            Zombie.Type.liner, 7500,
            Zombie.Type.chaos_knight, 4000,
            Zombie.Type.fast, 3500,
            Zombie.Type.normal, 2000
    );

    private static final Integer BasePriority = 1800;

    private long getPriority(Point target, UnitsResponse unitsResponse) {
        if (target instanceof Zombie zombie) {
            long turns = turnsToReachBase(zombie, unitsResponse);
            if (turns < Long.MAX_VALUE) {
                return Priority.get(zombie.type) / turns;
            } else {
                return -zombie.health;
            }
        } else if (target instanceof EnemyBaseBlock block) {
            if (block.isHead()) {
                return BasePriority * 2;
            } else {
                return BasePriority - block.health;
            }
        } else {
            return Long.MIN_VALUE;
        }
    }

    @Override
    public List<AttackRequest> makeAttacks(UnitsResponse unitsResponse) {
        List<Point> targets = new ArrayList<>();
        targets.addAll(unitsResponse.zombies);
        if (unitsResponse.enemyBlocks != null) {
            targets.addAll(unitsResponse.enemyBlocks);
        }

        targets.sort(Comparator.comparingLong(p -> -getPriority(p, unitsResponse)));

        List<AttackRequest> res = new ArrayList<>();
        Set<MyBaseBlock> used = new HashSet<>();
        for (Point target : targets) {
            long health = 0;
            if (target instanceof Zombie zombie) {
                health = zombie.health;
            } else if (target instanceof EnemyBaseBlock block) {
                health = block.health;
            }

            for (MyBaseBlock block : unitsResponse.base) {
                if (!canBaseBlockAttackZombie(block, target) || used.contains(block)) {
                    continue;
                }
                used.add(block);
                res.add(new AttackRequest(block.id, target));
                health -= block.attack;
                if (health <= 0) break;
            }
        }
        return res;
    }
}
