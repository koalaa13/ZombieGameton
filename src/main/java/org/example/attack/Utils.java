package org.example.attack;

import org.example.attack.damage.ZombieDamageCalculator;
import org.example.attack.damage.impl.*;
import org.example.model.MyBaseBlock;
import org.example.model.Point;
import org.example.model.Zombie;
import org.example.model.response.UnitsResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {
    /**
     * Какая клетка базы может атаковать какого зомби
     * @param unitsResponse информация обо всем
     * @return маппинг кто кого может атаковать
     */
    public static Map<MyBaseBlock, List<Zombie>> attackMapping(UnitsResponse unitsResponse) {
        Map<MyBaseBlock, List<Zombie>> res = new HashMap<>();
        List<Zombie> zombies = unitsResponse.zombies;
        for (MyBaseBlock block : unitsResponse.base) {
            res.put(block, new ArrayList<>());
            for (Zombie zombie : zombies) {
                if (canBaseBlockAttackZombie(block, zombie)) {
                    res.get(block).add(zombie);
                }
            }
        }
        return res;
    }

    public static boolean canBaseBlockAttackZombie(MyBaseBlock block, Zombie zombie) {
        long dist = dist(block, zombie);
        long can = block.range * block.range;
        return dist <= can;
    }

    public static long dist(Point p1, Point p2) {
        long xDiff = Math.abs(p1.x - p2.x);
        long yDiff = Math.abs(p1.y - p2.y);
        return xDiff * xDiff + yDiff * yDiff;
    }
    
    public static long turnsToReachBase(Zombie zombie, UnitsResponse unitsResponse) {
        // Этот долбоеб рандомно ходит, на него похуй
        if (zombie.type == Zombie.Type.chaos_knight) {
            return Long.MAX_VALUE;
        }
        
        return unitsResponse.base.stream()
                .filter(bp -> canReachPoint(zombie, bp))
                .mapToLong(bp -> turnsCountToReachPoint(zombie, bp) + zombie.waitTurns)
                .min()
                .orElse(Long.MAX_VALUE);
    }

    public static MyBaseBlock nearestBaseBlock(Zombie zombie, UnitsResponse unitsResponse) {
        // Этот долбоеб рандомно ходит, на него похуй
        if (zombie.type == Zombie.Type.chaos_knight) {
            return null;
        }

        return unitsResponse.base.stream()
                .filter(bp -> canReachPoint(zombie, bp))
                .min((bp1, bp2) -> {
                    long dst1 = turnsCountToReachPoint(zombie, bp1);
                    long dst2 = turnsCountToReachPoint(zombie, bp2);
                    return Long.compare(dst1, dst2);
                })
                .orElse(null);
    }
    
    public static boolean canReachPoint(Zombie zombie, Point point) {
        if (zombie.direction == Zombie.Direction.up) {
            return point.x == zombie.x && point.y < zombie.y;
        }
        if (zombie.direction == Zombie.Direction.down) {
            return point.x == zombie.x && point.y > zombie.y;
        }
        if (zombie.direction == Zombie.Direction.left) {
            return point.y == zombie.y && point.x < zombie.x;
        }
        if (zombie.direction == Zombie.Direction.right) {
            return point.y == zombie.y && point.x > zombie.x;
        }
        return false;
    }
    
    private static long turnsCountToReachPoint(Zombie zombie, Point point) {
        long diff = zombie.direction == Zombie.Direction.up || zombie.direction == Zombie.Direction.down ?
                Math.abs(point.y - zombie.y) :
                Math.abs(point.x - zombie.x);
        return diff / zombie.speed + (diff % zombie.speed == 0 ? 0 : 1);
    }

    public static ZombieDamageCalculator getCorrectCalculator(Zombie zombie) {
        if (zombie.type == Zombie.Type.normal) {
            return new NormalZombieDamageCalculator();
        }
        if (zombie.type == Zombie.Type.liner) {
            return new LinerZombieDamageCalculator();
        }
        if (zombie.type == Zombie.Type.juggernaut) {
            return new JuggernautZombieDamageCalculator();
        }
        if (zombie.type == Zombie.Type.fast) {
            return new FastZombieDamageCalculator();
        }
        if (zombie.type == Zombie.Type.chaos_knight) {
            return new ChaosKnightZombieDamageCalculator();
        }
        if (zombie.type == Zombie.Type.bomber) {
            return new BomberZombieDamageCalculator();
        }
        throw new RuntimeException("no calculator for such zombie type " + zombie.type);
    }

    public static long getDamage(Zombie zombie, UnitsResponse unitsResponse) {
        return getCorrectCalculator(zombie).getDamage(zombie, unitsResponse);
    }

    public static long getDamage(Zombie zombie, UnitsResponse unitsResponse, long stepsCount) {
        return getCorrectCalculator(zombie).getDamage(zombie, unitsResponse, stepsCount);
    }

    public static long turnsToBreak(Zombie zombie, MyBaseBlock block) {
        return block.health / zombie.attack + (block.health % zombie.attack == 0 ? 0 : 1);
    }
}
