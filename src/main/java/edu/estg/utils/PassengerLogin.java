package edu.estg.utils;

import java.util.ArrayList;

public class PassengerLogin {
    private Passenger passenger;
    private ArrayList<String> listIpsToJoin;

    public PassengerLogin(Passenger passenger, ArrayList<String> listIpsToJoin) {
        this.passenger = passenger;
        this.listIpsToJoin = listIpsToJoin;
    }

    public Passenger getPassenger() {
        return this.passenger;
    }

    public ArrayList<String> getListIpsToJoin() {
        return this.listIpsToJoin;
    }
}
