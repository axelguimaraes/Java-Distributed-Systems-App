package edu.estg;

import com.google.gson.Gson;
import edu.estg.userInterface.Client;
import edu.estg.utils.*;

import java.util.Scanner;

public class Menu {
    private final Gson jsonHelper = new Gson();
    private final Client client;

    public Menu(Client client) {
        LocalNode associatedLocalNode = null;
        Passenger associatedPassenger = null;
        this.client = client;
        startMenu();
    }

    public void startMenu() {
        int opt = -1;
        Scanner scanner = new Scanner(System.in);
        do {
            System.out.println("Who are you?\n" +
                    "1. Local Node\n" +
                    "2. Passenger\n\n" +
                    "0. Exit\n" +
                    "Option:");

            opt = scanner.nextInt();
            switch (opt) {
                case 1:
                    localNodeMenu();
                    break;
                case 2:
                    passengerMenu();
                    break;
                case 0:
                    System.exit(0);
                default:
                    System.out.println("Invalid option!");
            }

        } while (opt != 0);
    }

    private void localNodeMenu() {
        int opt = -1;
        Scanner scanner = new Scanner(System.in);
        do {
            System.out.println("::: LOCAL NODE MENU :::\n" +
                    "1. Login\n" +
                    "2. Register\n" +
                    "Option:");

            opt = scanner.nextInt();

            switch (opt) {
                case 1:
                    System.out.println("Username:");
                    String username = scanner.next();

                    System.out.println("Password:");
                    String password = scanner.next();

                    Login login = new Login(username, password);
                    Request<Login> loginRequest = new Request<>(RequestType.LOCAL_NODE_LOGIN, login);
                    this.client.sendMessage(jsonHelper.toJson(loginRequest));
                    break;

                case 2:
                    System.out.println("Name:");
                    String name = scanner.next();

                    System.out.println("Username:");
                    username = scanner.next();

                    System.out.println("Password:");
                    password = scanner.next();

                    LocalNode localNode = new LocalNode(name, username, password);
                    LocalNodeRegister localNodeRegister = new LocalNodeRegister(localNode);
                    Request<LocalNodeRegister> registerRequest = new Request<>(RequestType.LOCAL_NODE_REGISTER, localNodeRegister);
                    this.client.sendMessage(jsonHelper.toJson(registerRequest));
                    break;

                case 0:
                    break;
                default:
                    System.out.println("Invalid option!");
            }

        } while (opt != 0);
    }

    private void passengerMenu() {
        int opt = -1;
        Scanner scanner = new Scanner(System.in);

        do {
            System.out.println("::: PASSENGER MENU :::\n" +
                    "1. Login\n" +
                    "2. Register\n" +
                    "0. Exit\n\n" +
                    "Option:");

            opt = scanner.nextInt();

            switch (opt) {
                case 1:
                    System.out.println("Username:");
                    String username = scanner.next();

                    System.out.println("Password:");
                    String password = scanner.next();

                    Login login = new Login(username, password);
                    Request<Login> loginRequest = new Request<>(RequestType.PASSENGER_LOGIN, login);
                    this.client.sendMessage(jsonHelper.toJson(loginRequest));
                    break;

                case 2:
                    System.out.println("Name:");
                    String name = scanner.next();

                    System.out.println("Username:");
                    username = scanner.next();

                    System.out.println("Password:");
                    password = scanner.next();

                    Passenger passenger = new Passenger(name, username, password);
                    PassengerRegister passengerRegister = new PassengerRegister(passenger);
                    Request<PassengerRegister> registerRequest = new Request<>(RequestType.PASSENGER_REGISTER, passengerRegister);
                    this.client.sendMessage(jsonHelper.toJson(registerRequest));
                    break;

                case 0:
                    break;
                default:
                    System.out.println("Invalid option!");
            }
        } while (opt != 0);
    }
}
