package org.example.model.response;

import org.example.model.Spot;

import java.util.List;

public class ZpotsResponse {
    public List<Spot> zpots;

    @Override
    public String toString() {
        return "ZpotsResponse{" +
                "zpots=" + zpots +
                '}';
    }
}
