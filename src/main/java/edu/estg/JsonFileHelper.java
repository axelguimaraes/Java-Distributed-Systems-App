package edu.estg;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.estg.utils.LocalNode;
import edu.estg.utils.Passenger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class JsonFileHelper {
    private final String diretoryName;
    private final Gson jsonHelper;

    public JsonFileHelper(String directoryName) throws IOException {
        Files.createDirectories(Paths.get(directoryName));
        this.jsonHelper = configGson();
        this.diretoryName = directoryName;
    }

    private Gson configGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.serializeNulls();

        return gsonBuilder.create();
    }

    public synchronized <T> void serialize(String filename, T obj) throws IOException {
        try (Writer writer = new FileWriter(diretoryName + filename + ".json")) {
            writer.write(jsonHelper.toJson(obj));
        }
    }

    public synchronized <T> List<T> deserializeArray(String filename, Class<T[]> type) throws IOException {
        if (!new File(diretoryName + filename + ".json").exists()) return new ArrayList<>();

        String jsonString = new String(Files.readAllBytes(Paths.get(diretoryName + filename + ".json")));

        if (jsonString.isEmpty())
            return new ArrayList<>();

        return Arrays.asList(jsonHelper.fromJson(jsonString, type));
    }

    public synchronized boolean addLocalNode(LocalNode localNode) throws IOException {
        Set<LocalNode> localNodes = new HashSet<>(getLocalNodes());

        if (!localNodes.add(localNode)) return false;

        serialize("localNodes", localNode);
        return true;
    }

    public synchronized boolean updateLocalNode(LocalNode localNode) throws IOException {
        Set<LocalNode> localNodes = new HashSet<>(getLocalNodes());

        localNodes.remove(localNode);
        localNodes.add(localNode);

        serialize("localNodes", localNodes);
        return true;
    }

    public synchronized void updateLocalNodes(ArrayList<LocalNode> localNodes) throws IOException {
        serialize("localNodes", new HashSet<>(localNodes));
    }

    public synchronized LocalNode getLocalNode(String username) throws IOException {
        ArrayList<LocalNode> localNodes = new ArrayList<>(getLocalNodes());
        return localNodes.stream().filter(localNode -> localNode.getUsername().equals(username)).findFirst().orElse(null);
    }

    public synchronized ArrayList<LocalNode> getLocalNodes() throws IOException {
        return new ArrayList<>(deserializeArray("localNodes", LocalNode[].class));
    }

    public synchronized boolean addPassenger(Passenger passenger) throws IOException {
        Set<Passenger> passengers = new HashSet<>(getPassengers());

        if (!passengers.add(passenger)) return false;

        serialize("passengers", passengers);
        return true;
    }

    public synchronized boolean updatePassenger(Passenger passenger) throws IOException {
        Set<Passenger> passengers = new HashSet<>(getPassengers());

        passengers.remove(passenger);
        passengers.add(passenger);

        serialize("passengers", passengers);
        return true;
    }

    public synchronized void updatePassengers(ArrayList<Passenger> passengers) throws IOException {
        serialize("passengers", new HashSet<>(passengers));
    }

    public synchronized Passenger getPassenger(String username) throws IOException {
        ArrayList<Passenger> passengers = new ArrayList<>(getPassengers());
        return passengers.stream().filter(passenger -> passenger.getUsername().equals(username)).findFirst().orElse(null);
    }

    public synchronized ArrayList<Passenger> getPassengers() throws IOException {
        return new ArrayList<>(deserializeArray("passengers", Passenger[].class));
    }

}
