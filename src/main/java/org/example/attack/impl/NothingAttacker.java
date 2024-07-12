package org.example.attack.impl;

import org.example.attack.Attacker;
import org.example.model.request.AttackRequest;
import org.example.model.response.UnitsResponse;

import java.util.Collections;
import java.util.List;

public class NothingAttacker implements Attacker {
    @Override
    public List<AttackRequest> makeAttacks(UnitsResponse unitsResponse) {
        return Collections.emptyList();
    }
}
