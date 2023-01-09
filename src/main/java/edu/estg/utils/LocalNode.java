package edu.estg.utils;

import java.util.ArrayList;

public class LocalNode {
    private String name;
    private String username;
    private String password;
    private ArrayList<Passenger> passengers;
    private ArrayList<TrainLine> trainLines;

    public LocalNode(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.passengers = new ArrayList<>();
        this.trainLines = new ArrayList<>();
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

    public ArrayList<Passenger> getPassengers() {
        return passengers;
    }

    public ArrayList<TrainLine> getTrainLines() {
        return trainLines;
    }
}