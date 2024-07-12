package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.api.ApiControllerStub;
import org.example.api.Controller;
import org.example.attack.Attacker;
import org.example.attack.impl.NothingAttacker;
import org.example.model.Point;
import org.example.model.request.AllRequest;
import org.example.model.request.AllRequestWithBaseMove;
import org.example.model.response.CommandResponse;
import org.example.model.response.UnitsResponse;
import org.example.visual.Visualizer;

public class Main {
    public static void main(String[] args) throws JsonProcessingException, InterruptedException {
        Controller apiController = new ApiControllerStub();
        Visualizer visualizer = new Visualizer(apiController.getZpots());
        Attacker attacker = new NothingAttacker();

        int it = 0;
        long lastTurn = -1;
        while (true) {
            System.out.println("Iteration #" + it++);
            UnitsResponse unitsResponse = apiController.getUnitsInfo();
            if (unitsResponse.turn == lastTurn) {
                Thread.sleep(200);
                continue;
            }
            lastTurn = unitsResponse.turn;

            visualizer.setGame(unitsResponse);

            Point futureBase = visualizer.getFutureBase();
            boolean moveBase = futureBase != null;

            AllRequest allRequest = moveBase ? new AllRequestWithBaseMove() : new AllRequest();
            allRequest.build = visualizer.getFutureBlocks();

            if (moveBase) {
                ((AllRequestWithBaseMove) allRequest).moveBase = futureBase;
            }

            allRequest.attack = attacker.makeAttacks(unitsResponse);

            CommandResponse commandResponse = apiController.command(allRequest);
            if (commandResponse.errors.isEmpty()) {
                System.out.println("all commands were accepted!!!");
            } else {
                System.out.println("some errors happened:");
                for (String s : commandResponse.errors) {
                    System.out.println(s);
                }
            }

            System.out.println("----------------------------------------------");
            Thread.sleep(5000);
        }
    }
}