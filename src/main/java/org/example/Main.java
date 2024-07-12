package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.api.ApiController;
import org.example.model.Spot;
import org.example.model.response.UnitsResponse;

import java.util.List;

public class Main {
    public static void main(String[] args) throws JsonProcessingException {
        ApiController apiController = ApiController.getTestInstance();
        apiController.register();
        List<Spot> spots = apiController.getZpots();
        for (Spot s : spots) {
            System.out.println(s);
        }
        UnitsResponse response = apiController.getUnitsInfo();
    }
}