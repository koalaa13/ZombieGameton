package org.example.model.request;

import org.example.model.Point;

public class AttackRequest {
    public String blockId;
    public Point target;

    public AttackRequest() {}

    public AttackRequest(String blockId, Point target) {
        this.blockId = blockId;
        this.target = target;
    }
}
