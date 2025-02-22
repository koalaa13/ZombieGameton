package org.example.api;

import org.example.model.*;
import org.example.model.request.AllRequest;
import org.example.model.response.CommandResponse;
import org.example.model.response.RegisterResponse;
import org.example.model.response.UnitsResponse;
import org.example.model.response.ZpotsResponse;

import java.util.Collections;
import java.util.List;

public class ApiControllerStub implements Controller {

    @Override
    public RegisterResponse register() {
        return null;
    }

    @Override
    public ZpotsResponse getZpots() {
        ZpotsResponse zpotsResponse = new ZpotsResponse();
        zpotsResponse.zpots = List.of(
                new Spot(-20, 5, Spot.Type.DEFAULT),
                new Spot(20, 30, Spot.Type.DEFAULT),
                new Spot(25, -3, Spot.Type.DEFAULT),
                new Spot(-7, -12, Spot.Type.DEFAULT),
                new Spot(1, 1, Spot.Type.DEFAULT),
                new Spot(-1, 1, Spot.Type.WALL),
                new Spot(-1, 0, Spot.Type.WALL),
                new Spot(-1, -1, Spot.Type.WALL)
        );
        return zpotsResponse;
    }

    private Zombie getZombie() {
        Zombie zombie = new Zombie();
        zombie.attack = 10;
        zombie.direction = Zombie.Direction.up;
        zombie.health = 100;
        zombie.id = "f47ac10b-58cc-4372-a567-0e02b2c3d479";
        zombie.speed = 10;
        zombie.type = Zombie.Type.juggernaut;
        zombie.waitTurns = 1;
        zombie.x = 5;
        zombie.y = 5;
        return zombie;
    }

    private Zombie getZombie2() {
        Zombie zombie = getZombie();
        zombie.type = Zombie.Type.normal;
        zombie.x = 5;
        zombie.y = 7;
        zombie.direction = Zombie.Direction.left;
        return zombie;
    }

    private MyBaseBlock getMyBaseBlock() {
        MyBaseBlock myBaseBlock = new MyBaseBlock();
        myBaseBlock.id = "f47ac10b-58cc-4372-a567-0e02b2c3d479";
        myBaseBlock.attack = 10;
        myBaseBlock.health = 100;
        myBaseBlock.setHead(true);
        myBaseBlock.lastAttack = new Point(2, 2);
        myBaseBlock.range = 5;
        myBaseBlock.x = 3;
        myBaseBlock.y = 1;
        return myBaseBlock;
    }

    private MyBaseBlock getMyBaseBlock2(int x, int y) {
        MyBaseBlock block = getMyBaseBlock();
        block.setHead(false);
        block.x = x;
        block.y = y;
        return block;
    }

    private EnemyBaseBlock getEnemyBaseBlock() {
        EnemyBaseBlock enemyBaseBlock = new EnemyBaseBlock();
        enemyBaseBlock.name = "player-test";
        enemyBaseBlock.attack = 10;
        enemyBaseBlock.health = 100;
        enemyBaseBlock.setHead(true);
        enemyBaseBlock.lastAttack = new Point(2, 2);
        enemyBaseBlock.x = 1;
        enemyBaseBlock.y = 3;
        return enemyBaseBlock;
    }

    private EnemyBaseBlock getEnemyBaseBlock2(int x, int y) {
        EnemyBaseBlock block = getEnemyBaseBlock();
        block.setHead(false);
        block.x = x;
        block.y = y;
        return block;
    }

    private long turn = 0L;

    @Override
    public UnitsResponse getUnitsInfo() {
        UnitsResponse unitsResponse = new UnitsResponse();

        unitsResponse.player = new Player();
        unitsResponse.player.enemyBlockKills = 100;
        unitsResponse.player.gold = 2;
        unitsResponse.player.points = 100;
        unitsResponse.player.zombieKills = 100;
        unitsResponse.turn = 1;

        unitsResponse.zombies = List.of(
                getZombie(), getZombie2()
        );

        unitsResponse.base = List.of(
                getMyBaseBlock(), getMyBaseBlock2(4, 1),
                getMyBaseBlock2(3, 0), getMyBaseBlock2(15, 3)
        );

        unitsResponse.enemyBlocks = List.of(
                getEnemyBaseBlock(), getEnemyBaseBlock2(1, 4)
        );

        unitsResponse.turn = turn++;

        return unitsResponse;
    }

    @Override
    public CommandResponse command(AllRequest allRequest) {
        CommandResponse commandResponse = new CommandResponse();
        commandResponse.errors = Collections.emptyList();
        return commandResponse;
    }
}
