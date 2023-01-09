package edu.estg.utils;

public class Passenger {
    private String name;
    private String username;
    private String password;

    public Passenger(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
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
}
