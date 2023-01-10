package edu.estg.userInterface;

import com.google.gson.Gson;
import edu.estg.userInterface.Client;
import edu.estg.utils.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.InputMismatchException;
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
        try {
            int opt = -1;
            Scanner scanner = new Scanner(System.in);
            while (opt != 0) {
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
            }

        } catch (InputMismatchException e) {
            System.out.println("Invalid option!");
        }
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

                    localNodeDashboard(); // TODO: validate login

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

    private static void localNodeDashboard() {
        int opt = -1;
        Scanner scanner = new Scanner(System.in);
        while (opt != 0) {
            System.out.println("=== LOCAL NODE DASHBOARD ===\n\n\n" +
                    "1. List associated passengers\n" +
                    "2. List associated lines\n" +
                    "0. Exit\n\n" +
                    "Option:");

            opt = scanner.nextInt();

            switch (opt) {
                case 1:
                    System.out.println("ASSOCIATED PASSENGERS");
                    break;
                case 2:
                    System.out.println("ASSOCIATED LINES");
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    private static void passengerDashboard() {
        int opt = -1;
        Scanner scanner = new Scanner(System.in);
        while (opt != 0) {
            System.out.println("=== PASSENGER DASHBOARD ===\n\n\n" +
                    "1. List associated lines\n " +
                    "0. Exit\n\n" +
                    "Option:");

            opt = scanner.nextInt();

            switch (opt) {
                case 1:
                    System.out.println("ASSOCIATED LINES");
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }
}
