package edu.estg.centralNode;

import com.google.gson.Gson;
import edu.estg.models.ArrayListSync;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;

public class CentralServer {
    private final ServerSocket serverSocket;
    private final DatagramSocket datagramSocket;

    private final ArrayListSync<ClientHandler> clientHandlers;
    private final Gson jsonHelper;

    public CentralServer(ServerSocket serverSocket, DatagramSocket datagramSocket) {
        this.clientHandlers = new ArrayListSync<>();
        this.jsonHelper = new Gson();
        this.serverSocket = serverSocket;
        this.datagramSocket = datagramSocket;
    }

    public void startServer() {
        try {
            while (true) {
                System.out.println("Central node is online!");
                Socket socket = serverSocket.accept();
                new Thread(new ClientHandler(clientHandlers, socket, this)).start();
            }
        } catch (IOException e) {
            closeServerSocket();
        }
    }

    private void closeServerSocket() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        DatagramSocket datagramSocket = new DatagramSocket(4445);
        CentralServer centralServer = new CentralServer(new ServerSocket(2048), datagramSocket);
        centralServer.startServer();
    }
}
