package edu.estg.utils;

import java.util.ArrayList;

public class Passenger {
    private final String name;
    private final String username;
    private final String password;
    private ArrayList<String> addedTrainLines;

    public Passenger(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.addedTrainLines = new ArrayList<>();
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

    public ArrayList<String> getAddedLocalNodes() {
        return this.addedTrainLines;
    }

    public void addTrainLine(String trainLine) {
        this.addedTrainLines.add(trainLine);
    }
}
