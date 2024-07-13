package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.api.ApiController;
import org.example.api.ApiControllerStub;
import org.example.api.Controller;
import org.example.attack.Attacker;
import org.example.attack.impl.NothingAttacker;
import org.example.attack.impl.PriorityAttacker;
import org.example.model.Point;
import org.example.model.request.AllRequest;
import org.example.model.request.AllRequestWithBaseMove;
import org.example.model.response.CommandResponse;
import org.example.model.response.RegisterResponse;
import org.example.model.response.UnitsResponse;
import org.example.model.response.ZpotsResponse;
import org.example.visual.Visualizer;

public class Main {
    public static void main(String[] args) throws JsonProcessingException, InterruptedException {
        Controller apiController = ApiController.getInstance();
//        apiController = new ApiControllerStub();
        RegisterResponse registerResponse = apiController.register();
        if (registerResponse == null) {
            System.err.println("error happened while registering");
        } else {
            while (registerResponse != null && registerResponse.startsInSec > 0) {
                System.err.println("round haven't started yet, starts in " + registerResponse.startsInSec);
                Thread.sleep(1000);
                registerResponse = apiController.register();
            }
        }

        ZpotsResponse zpotsResponse = apiController.getZpots();

        Visualizer visualizer = new Visualizer(zpotsResponse);
        Attacker attacker = new PriorityAttacker();

        int it = 0;
        long lastTurn = -1;
        while (true) {
            System.out.println("Iteration #" + it++);
            UnitsResponse unitsResponse = apiController.getUnitsInfo();
            if (unitsResponse.turn == lastTurn) {
                Thread.sleep(200);
                continue;
            }
            if (unitsResponse.turn % 10 == 0) {
                zpotsResponse = apiController.getZpots();
            }
            lastTurn = unitsResponse.turn;

            visualizer.setGame(unitsResponse, zpotsResponse);

            visualizer.setFreeze(false);
            Thread.sleep(1000);
            visualizer.setFreeze(true);

            Point futureBase = visualizer.getFutureBase();
            boolean moveBase = futureBase != null;

            AllRequest allRequest = moveBase ? new AllRequestWithBaseMove() : new AllRequest();
            allRequest.build = visualizer.getFutureBlocks();

            if (moveBase) {
                ((AllRequestWithBaseMove) allRequest).moveBase = futureBase;
            }

            allRequest.attack = attacker.makeAttacks(unitsResponse);

            CommandResponse commandResponse = apiController.command(allRequest);
            if (commandResponse.errors == null || commandResponse.errors.isEmpty()) {
                System.out.println("all commands were accepted!!!");
            } else {
                System.out.println("some errors happened:");
                for (String s : commandResponse.errors) {
                    System.out.println(s);
                }
            }

            System.out.println("----------------------------------------------");
        }
    }
}