package org.example.model.response;

import org.example.model.request.AllRequestWithBaseMove;

import java.util.List;

public class CommandResponse {
    public AllRequestWithBaseMove acceptedCommands;
    public List<String> errors;
}
