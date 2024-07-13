package org.example.attack.damage;

import org.example.attack.Utils;
import org.example.model.MyBaseBlock;
import org.example.model.Zombie;
import org.example.model.response.UnitsResponse;

import java.util.List;

public abstract class ZombieDamageCalculator {
    /**
     * Для какого типа зомби предназначен калькулятор
     *
     * @return тип зомби
     */
    public abstract Zombie.Type getType();

    public long getDamage(Zombie zombie, UnitsResponse unitsResponse) {
        return getDamage(zombie, unitsResponse, Long.MAX_VALUE);
    }

    public abstract long getDamage(Zombie zombie, UnitsResponse unitsResponse, long stepsCount);

    /**
     * Какие клетки базы разнесет и в каком порядке, если ничего с ним не делать
     * @param zombie зомби
     * @param unitsResponse общая инфа по базе
     * @return какие именно клетки он заденет своей атакой
     */
    protected abstract List<MyBaseBlock> getDamageDealtBlocks(Zombie zombie, UnitsResponse unitsResponse);
}
