package org.example.attack;

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
                if (canHit(block, zombie)) {
                    res.get(block).add(zombie);
                }
            }
        }
        return res;
    }

    public static boolean canHit(MyBaseBlock block, Zombie zombie) {
        long dist = dist(block, zombie);
        long can = block.range * block.range;
        return dist <= can;
    }

    public static long dist(Point p1, Point p2) {
        long xDiff = Math.abs(p1.x - p2.x);
        long yDiff = Math.abs(p1.y - p2.y);
        return xDiff * xDiff + yDiff * yDiff;
    }
}
