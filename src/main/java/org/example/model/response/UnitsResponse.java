package org.example.model.response;

import org.example.model.EnemyBaseBlock;
import org.example.model.MyBaseBlock;
import org.example.model.Player;
import org.example.model.Zombie;

import java.util.List;

public class UnitsResponse {
    public List<MyBaseBlock> base;
    public List<EnemyBaseBlock> enemyBlocks;
    public Player player;
    public List<Zombie> zombies;
}
