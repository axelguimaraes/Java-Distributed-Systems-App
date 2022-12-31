package edu.estg.models;

import java.util.ArrayList;

public class LocalNode {
    private String name;
    private String username;
    private String password;
    private ArrayList<String> passengers;
    private ArrayList<String> trainLines;

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

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean addPassenger(String passenger) {
        return this.passengers.add(passenger);
    }

    public boolean addTrainLine(String trainLine) {
        return this.trainLines.add(trainLine);
    }
}
