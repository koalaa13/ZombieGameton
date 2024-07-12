package org.example.api;

import org.example.model.request.AllRequest;
import org.example.model.response.CommandResponse;
import org.example.model.response.RegisterResponse;
import org.example.model.response.UnitsResponse;
import org.example.model.response.ZpotsResponse;

public interface Controller {
    RegisterResponse register();
    ZpotsResponse getZpots();
    UnitsResponse getUnitsInfo();
    CommandResponse command(AllRequest allRequest);
}
