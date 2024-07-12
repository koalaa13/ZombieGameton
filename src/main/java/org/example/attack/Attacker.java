package org.example.attack;

import org.example.model.request.AttackRequest;
import org.example.model.response.UnitsResponse;

import java.util.List;

public interface Attacker {
    List<AttackRequest> makeAttacks(UnitsResponse unitsResponse);
}
