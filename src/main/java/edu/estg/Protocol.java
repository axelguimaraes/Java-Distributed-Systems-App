package edu.estg;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import edu.estg.userInterface.Server;
import edu.estg.utils.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Collectors;

import static edu.estg.userInterface.Server.PASSENGER_GROUP_IP;

public class Protocol {
    private final JsonFileHelper jsonFileHelper;
    private final Gson jsonHelper;
    private final ArrayListSync<ClientHandler> clientHandlers;
    private final ClientHandler clientHandler;
    protected Server server;

    public Protocol(ClientHandler clientHandler, ArrayListSync<ClientHandler> clientHandlers, Server server) throws IOException {
        this.clientHandler = clientHandler;
        this.server = server;
        this.clientHandlers = clientHandlers;
        this.jsonHelper = new Gson();
        this.jsonFileHelper = new JsonFileHelper("files/");
    }

    protected synchronized String processMessage(String requestMessage) {
        RequestType requestType;
        ArrayList<LocalNode> currentLocalNodes;
        ArrayList<Passenger> currentPassengers;

        try {
            requestType = this.jsonHelper.fromJson(requestMessage, Request.class).getType();
            currentLocalNodes = this.jsonFileHelper.getLocalNodes();
            currentPassengers = this.jsonFileHelper.getPassengers();
        } catch (IOException e) {
            return this.jsonHelper.toJson(new Response<>(ResponseStatus.NOT_OK, "Unexpected error while registering!"));
        }

        switch (requestType) {
            case LOCAL_NODE_LOGIN:
                return localNodeLoginHandler(requestMessage, currentLocalNodes, currentPassengers);
            case LOCAL_NODE_REGISTER:
                return localNodeRegisterHandler(requestMessage, currentLocalNodes, currentPassengers);
            case PASSENGER_LOGIN:
                return passengerLoginHandler(requestMessage, currentPassengers, currentLocalNodes);
            case PASSENGER_REGISTER:
                return passengerRegisterHandler(requestMessage, currentPassengers, currentLocalNodes);
            case ADD_TRAIN_LINE:
                return addTrainLineHandler(requestMessage, currentLocalNodes);
            case GET_CURRENT_LOCAL_NODES:
                return getCurrentLocalNodesHandler(currentLocalNodes);
            case PASSENGER_MESSAGE_TO_NODE:
                return sendPassengerMessageHelper(requestMessage, currentPassengers, currentLocalNodes);
            default:
                return this.jsonHelper.toJson(new Response<>(ResponseStatus.NOT_OK, "Unsupported request!"));
        }
    }

    private String localNodeLoginHandler(String requestMessage, ArrayList<LocalNode> currentLocalNodes, ArrayList<Passenger> currentPassengers) {
        try {
            Login login = this.jsonHelper.<Request<Login>>fromJson(requestMessage, new TypeToken<Request<Login>>() {
            }.getType()).getData();

            LocalNode localNodeDB = currentLocalNodes.stream().filter(localNode -> localNode.getPassword().equals(login.getPassword()) && localNode.getUsername().equals(login.getUsername())).findFirst().orElse(null);

            if (localNodeDB == null) {
                return this.jsonHelper.toJson(new Response<>(ResponseStatus.NOT_OK, "Invalid credentials!"));
            }

            ArrayList<String> ipsToJoin = new ArrayList<>();
            ipsToJoin.add(0, Server.MAIN_GROUP_IP);
            ipsToJoin.add(1, Server.LOCAL_NODE_GROUP_IP);

            LocalNodeLogin localNodeLogin = new LocalNodeLogin(localNodeDB, ipsToJoin);
            this.clientHandler.username = localNodeDB.getUsername();
            return this.jsonHelper.toJson(new Response<>(ResponseStatus.OK, RequestType.LOCAL_NODE_LOGIN, "Login successfully!", localNodeLogin));

        } catch (RuntimeException e) {
            return this.jsonHelper.toJson(new Response<>(ResponseStatus.NOT_OK, "Invalid login request!"));
        }
    }

    private String localNodeRegisterHandler(String requestMessage, ArrayList<LocalNode> currentLocalNodes, ArrayList<Passenger> currentPassengers) {
        try {
            LocalNodeRegister localNodeRegister = this.jsonHelper.<Request<LocalNodeRegister>>fromJson(requestMessage, new TypeToken<Request<LocalNodeRegister>>() {
            }.getType()).getData();

            boolean localNodeExists = currentLocalNodes.contains(localNodeRegister.getLocalNode());

            if (localNodeExists)
                return this.jsonHelper.toJson(new Response<>(ResponseStatus.NOT_OK, RequestType.LOCAL_NODE_REGISTER, "Username already in use!"));

            currentLocalNodes.add(localNodeRegister.getLocalNode());
            jsonFileHelper.updateLocalNodes(currentLocalNodes);
            return this.jsonHelper.toJson(new Response<>(ResponseStatus.OK, RequestType.LOCAL_NODE_REGISTER, "Local node registered successfully"));
        } catch (IOException e) {
            return this.jsonHelper.toJson(new Response<>(ResponseStatus.NOT_OK, RequestType.LOCAL_NODE_REGISTER, "Unexpected error while saving changes!"));
        }
    }

    private String passengerLoginHandler(String requestMessage, ArrayList<Passenger> currentPassengers, ArrayList<LocalNode> currentLocalNodes) {
        try {
            Login login = this.jsonHelper.<Request<Login>>fromJson(requestMessage, new TypeToken<Request<Login>>() {
            }.getType()).getData();

            Passenger passengerDB = currentPassengers.stream().filter(passenger -> passenger.getPassword().equals(login.getPassword()) && passenger.getUsername().equals(login.getUsername())).findFirst().orElse(null);

            if (passengerDB == null) {
                return this.jsonHelper.toJson(new Response<>(ResponseStatus.NOT_OK, "Invalid credentials!"));
            }

            ArrayList<String> localNodesNotAdded = currentLocalNodes.stream().map(LocalNode::getUsername).collect(Collectors.toCollection(ArrayList::new));
            localNodesNotAdded.remove(passengerDB.getAddedTrainLines());

            ArrayList<String> ipsToJoin = new ArrayList<>();
            ipsToJoin.add(0, Server.MAIN_GROUP_IP);
            ipsToJoin.add(1, PASSENGER_GROUP_IP);

            PassengerLogin passengerLogin = new PassengerLogin(passengerDB, ipsToJoin, localNodesNotAdded);
            this.clientHandler.username = passengerDB.getUsername();
            return this.jsonHelper.toJson(new Response<>(ResponseStatus.OK, RequestType.PASSENGER_LOGIN, "Login successfully!", passengerLogin));

        } catch (RuntimeException e) {
            return this.jsonHelper.toJson(new Response<>(ResponseStatus.NOT_OK, "Invalid login request!"));
        }
    }

    private String passengerRegisterHandler(String requestMessage, ArrayList<Passenger> currentPassengers, ArrayList<LocalNode> currentLocalNodes) { // TODO: add passengers to local nodes
        try {
            PassengerRegister passengerRegister = this.jsonHelper.<Request<PassengerRegister>>fromJson(requestMessage, new TypeToken<Request<PassengerRegister>>() {
            }.getType()).getData();

            boolean passengerExists = false;
            for (int i = 0; i < currentPassengers.size(); i++) {
                if (currentPassengers.get(i).equals(passengerRegister.getPassenger())) {
                    passengerExists = true;
                    break;
                }
            }

            if (passengerExists)
                return this.jsonHelper.toJson(new Response<>(ResponseStatus.NOT_OK, RequestType.PASSENGER_REGISTER, "Username already in use!"));


            for (int i = 0; i < currentLocalNodes.size(); i++) {
                for (int j = 0; j < currentLocalNodes.get(i).getTrainLines().size(); j++) {
                    for (int k = 0; k < passengerRegister.getPassenger().getAddedTrainLines().size(); k++) {
                        if (currentLocalNodes.get(i).getTrainLines().get(j).equals(passengerRegister.getPassenger().getAddedTrainLines().get(k))) {
                            if (!currentLocalNodes.get(i).getPassengers().contains(passengerRegister.getPassenger())) {
                                currentLocalNodes.get(i).addPassenger(passengerRegister.getPassenger());
                                break;
                            }
                        }
                    }
                }
            }
            jsonFileHelper.updateLocalNodes(currentLocalNodes);


            currentPassengers.add(new Passenger(passengerRegister.getPassenger().getName(), passengerRegister.getPassenger()
                    .getUsername(), passengerRegister.getPassenger().getPassword(), passengerRegister.getPassenger().getAddedTrainLines()));

            jsonFileHelper.updatePassengers(currentPassengers);
            return this.jsonHelper.toJson(new Response<>(ResponseStatus.OK, RequestType.PASSENGER_REGISTER, "Passenger registered successfully"));
        } catch (IOException e) {
            return this.jsonHelper.toJson(new Response<>(ResponseStatus.NOT_OK, RequestType.PASSENGER_REGISTER, "Unexpected error while saving changes!"));
        }
    }

    private String addTrainLineHandler(String requestMessage, ArrayList<LocalNode> currentLocalNodes) {
        AddTrainLinesToLocalNodeHelper trainLineToAdd = this.jsonHelper.<Request<AddTrainLinesToLocalNodeHelper>>fromJson(requestMessage, new TypeToken<Request<AddTrainLinesToLocalNodeHelper>>() {
        }.getType()).getData();

        LocalNode localNodeToAdd = currentLocalNodes.stream().filter(localNode -> localNode.getName().equals(trainLineToAdd.getLocalNode())).findFirst().orElse(null);

        if (localNodeToAdd == null) {
            return this.jsonHelper.toJson(new Response<>(ResponseStatus.NOT_OK, "Failed to add train line to local node!"));
        }

        localNodeToAdd.addTrainLine(trainLineToAdd.getBeginning(), trainLineToAdd.getEnd());
        try {
            this.jsonFileHelper.updateLocalNodes(currentLocalNodes);
        } catch (IOException e) {
            return this.jsonHelper.toJson(new Response<>(ResponseStatus.NOT_OK, "Unexpected error saving changes!"));
        }
        return this.jsonHelper.toJson(new Response<>(ResponseStatus.OK, RequestType.FEEDBACK_ADD_TRAIN_LINE, "Train line added!", trainLineToAdd));
    }

    private String getCurrentLocalNodesHandler(ArrayList<LocalNode> currentLocalNodes) {
        return this.jsonHelper.toJson(new Response<>(ResponseStatus.OK, RequestType.GET_CURRENT_LOCAL_NODES, currentLocalNodes));
    }

    private String sendPassengerMessageHelper(String requestMessage, ArrayList<Passenger> currentPassengers, ArrayList<LocalNode> currentLocalNodes) {
        PassengerMessage passengerMessage = this.jsonHelper.<Request<PassengerMessage>>fromJson(requestMessage, new TypeToken<Request<PassengerMessage>>() {
        }.getType()).getData();

        server.sendMulticastPassengerMessage(this.jsonHelper.toJson(new Response<>(ResponseStatus.OK, RequestType.PASSENGER_MESSAGE_FROM_NODE, passengerMessage)));

        return this.jsonHelper.toJson(new Response<>(ResponseStatus.OK, RequestType.SOMETHING, "Message sent!"));
    }
}
