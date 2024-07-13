package org.example.attack.impl;

import org.example.attack.Attacker;
import org.example.attack.Utils;
import org.example.model.MyBaseBlock;
import org.example.model.Point;
import org.example.model.Zombie;
import org.example.model.request.AttackRequest;
import org.example.model.response.UnitsResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DangerousZombiesAttacker implements Attacker {
    @Override
    public List<AttackRequest> makeAttacks(UnitsResponse unitsResponse) {
        List<Zombie> dangZombies = new ArrayList<>(unitsResponse.zombies).stream()
                .filter(z -> Utils.getDamage(z, unitsResponse) > 0)
                // Мб нужен другой компаратор, этот прям тупенький
                .sorted(Comparator.comparingLong(z -> -Utils.getDamage(z, unitsResponse)))
                .toList();
        if (dangZombies.isEmpty()) {
            return Collections.emptyList();
        }
        List<AttackRequest> attackRequests = new ArrayList<>();
        for (MyBaseBlock block : unitsResponse.base) {
            for (Zombie zombie : dangZombies) {
                if (zombie.health <= 0) {
                    continue;
                }
                if (Utils.canBaseBlockAttackZombie(block, zombie)) {
                    attackRequests.add(new AttackRequest(block.id, new Point(zombie.x, zombie.y)));
                    break;
                }
            }
        }
        return attackRequests;
    }
}
