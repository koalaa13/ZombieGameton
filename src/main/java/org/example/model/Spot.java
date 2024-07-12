package org.example.model;

public class Spot extends Point {
    public Type type;

    public Spot() {
    }

    public Spot(long x, long y, Type type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public void setType(String type) {
        if ("default".equals(type)) {
            this.type = Type.DEFAULT;
        } else if ("wall".equals(type)) {
            this.type = Type.WALL;
        }
    }

    public enum Type {
        DEFAULT,
        WALL
    }

    @Override
    public String toString() {
        return "Spot{" +
                "x=" + x +
                ", y=" + y +
                ", type=" + type +
                '}';
    }
}
