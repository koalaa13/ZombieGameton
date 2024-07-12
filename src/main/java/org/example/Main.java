package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.api.ApiController;
import org.example.api.ApiControllerStub;
import org.example.api.Controller;
import org.example.visual.Visualizer;

public class Main {
    public static void main(String[] args) throws JsonProcessingException, InterruptedException {
        Controller apiController = new ApiControllerStub();
        Visualizer visualizer = new Visualizer(apiController.getZpots());
        int it = 0;
        while (true) {
            visualizer.setGame(apiController.getUnitsInfo());
            System.out.println("Iteration #" + it++);
            Thread.sleep(5000);
        }
    }
}