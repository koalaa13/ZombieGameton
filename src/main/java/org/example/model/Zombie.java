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
        up, down, left, right;

        public int deltaX() {
            if (this == left) {
                return -1;
            } else if (this == right) {
                return 1;
            } else {
                return 0;
            }
        }

        public int deltaY() {
            if (this == up) {
                return -1;
            } else if (this == down) {
                return 1;
            } else {
                return 0;
            }
        }
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
