package org.example.model;

public class BaseBlock extends Point {
    public long attack;
    public long health;
    public boolean isHead;
    public Point lastAttack;
    public long range;

    public boolean isHead() {
        return isHead || attack == 40L;
    }

    public void setHead(boolean head) {
        this.isHead = head;
    }
}
