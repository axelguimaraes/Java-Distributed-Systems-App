package edu.estg.utils;

import java.util.ArrayList;

public class Passenger {
    private final String name;
    private final String username;
    private final String password;
    private ArrayList<String> addedTrainLines;

    public Passenger(String name, String username, String password, ArrayList<String> addedTrainLines) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.addedTrainLines = addedTrainLines;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public ArrayList<String> getAddedTrainLines() {
        return this.addedTrainLines;
    }
}
