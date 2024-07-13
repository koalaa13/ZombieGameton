package org.example.attack.damage.impl;

import org.example.model.Zombie;

public class FastZombieDamageCalculator extends NormalZombieDamageCalculator {
    @Override
    public Zombie.Type getType() {
        return Zombie.Type.fast;
    }
}
