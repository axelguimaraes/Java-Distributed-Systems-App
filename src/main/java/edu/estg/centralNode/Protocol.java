package edu.estg.centralNode;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import edu.estg.models.ArrayListSync;
import edu.estg.models.LocalNode;
import edu.estg.models.schemas.*;

import java.io.IOException;
import java.util.ArrayList;

public class Protocol {
    private final JsonFileHelper jsonFileHelper;
    private final Gson jsonHelper;
    private final ArrayListSync<ClientHandler> clientHandlers;
    private final ClientHandler clientHandler;
    private Server server;

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

        try {
            requestType = this.jsonHelper.fromJson(requestMessage, Request.class).getType();
            currentLocalNodes = this.jsonFileHelper.getLocalNodes();
        } catch (IOException e) {
            return this.jsonHelper.toJson(new Response<>(ResponseStatus.NOT_OK, "Unexpected error during register!"));
        }

        switch (requestType) {
            case LOGIN:
                return loginHandler(requestMessage, currentLocalNodes);
            case REGISTER:
                return registerHandler(requestMessage, currentLocalNodes);
            case BROADCAST_EVENT_REQUEST:
                return broadcastEventHandler(requestMessage, currentLocalNodes);
            case PASSENGER_REPORT:
                return passengerReportHandler(requestMessage, currentLocalNodes);
            default:
                return this.jsonHelper.toJson(new Response<>(ResponseStatus.NOT_OK, "Invalid request!"));
        }
    }

    private String loginHandler(String requestMessage, ArrayList<LocalNode> currentLocalNodes) {
        try {
            Login login = this.jsonHelper.<Request<Login>>fromJson(requestMessage, new TypeToken<Request<Login>>() {
            }.getType()).getData();

            LocalNode localNodeDB = currentLocalNodes.stream().filter(localNode -> localNode.getPassword().equals(login.getPassword()) && localNode.getUsername().equals(login.getUsername())).findFirst().orElse(null);

            if (localNodeDB == null) {
                return this.jsonHelper.toJson(new Response<>(ResponseStatus.NOT_OK, "Invalid credentials!"));
            }

            this.clientHandler.setLocalNodeUsername(localNodeDB.getUsername());
            return this.jsonHelper.toJson(new Response<>(ResponseStatus.OK, RequestType.LOGIN, "Login successfully!"));
        } catch (ClassCastException e) {
            return this.jsonHelper.toJson(new Response<>(ResponseStatus.NOT_OK, "Invalid login request!"));
        }
    }

    private String registerHandler(String requestMessage, ArrayList<LocalNode> currentLocalNodes) {
        try {
            LocalNodeRegister localNodeRegister = this.jsonHelper.<Request<LocalNodeRegister>>fromJson(requestMessage, new TypeToken<Request<LocalNodeRegister>>() {
            }.getType()).getData();

            boolean localNodeExists = currentLocalNodes.contains(localNodeRegister.getLocalNode());

            if (localNodeExists) {
                return this.jsonHelper.toJson(new Response<>(ResponseStatus.NOT_OK, RequestType.REGISTER, "The username \"" + localNodeRegister.getLocalNode().getUsername() + "\" already exists!"));
            }

            currentLocalNodes.add(localNodeRegister.getLocalNode());
            jsonFileHelper.updateLocalNodes(currentLocalNodes);
            return this.jsonHelper.toJson(new Response<>(ResponseStatus.OK, RequestType.REGISTER, "Register sucessfully!"));
        } catch (IOException e) {
            return this.jsonHelper.toJson(new Response<>(ResponseStatus.NOT_OK, RequestType.REGISTER, "Unexpected error when saving changes!"));
        }
    }

    private String broadcastEventHandler(String requestMessage, ArrayList<LocalNode> currentLocalNodes) {
        return "";
    }

    private String passengerReportHandler(String requestMessage, ArrayList<LocalNode> currentLocalNodes) {
        return "";
    }
}
