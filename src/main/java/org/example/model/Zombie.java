package org.example.model;

public class Zombie extends Point {
    public long attack;
    public Direction direction;
    public long health;
    public String id;
    public long speed;
    public Type type;
    public long waitTurns;

    public enum Direction {
        up, down, left, right
    }

    public enum Type {
        normal,
        fast,
        bomber,
        liner,
        juggernaut,
        chaos_knight
    }
}
