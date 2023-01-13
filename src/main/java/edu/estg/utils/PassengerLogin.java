package edu.estg.utils;

import java.util.ArrayList;

public class PassengerLogin {
    private Passenger passenger;
    private ArrayList<String> listIpsToJoin;
    private ArrayList<String> localNodesToAdd;

    public PassengerLogin(Passenger passenger, ArrayList<String> listIpsToJoin, ArrayList<String> localNodesToAdd) {
        this.passenger = passenger;
        this.listIpsToJoin = listIpsToJoin;
        this.localNodesToAdd = localNodesToAdd;
    }

    public Passenger getPassenger() {
        return this.passenger;
    }

    public ArrayList<String> getListIpsToJoin() {
        return this.listIpsToJoin;
    }

    public ArrayList<String> getLocalNodesToAdd() {
        return this.localNodesToAdd;
    }
}
