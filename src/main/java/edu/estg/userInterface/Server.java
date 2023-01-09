package edu.estg.userInterface;

import com.google.gson.Gson;
import edu.estg.*;
import edu.estg.utils.RequestType;
import edu.estg.utils.Response;
import edu.estg.utils.ResponseStatus;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class Server {
    public static final String MAIN_GROUP_IP = "224.0.0.2";
    private final ServerSocket serverSocket;
    private final DatagramSocket datagramSocket;
    private final ArrayListSync<ClientHandler> clientHandlers;
    private final Gson jsonHelper;

    public Server(ServerSocket serverSocket, DatagramSocket datagramSocket) {
        this.clientHandlers = new ArrayListSync<>();
        this.jsonHelper = new Gson();
        this.serverSocket = serverSocket;
        this.datagramSocket = datagramSocket;
    }

    public void startServer() {
        try {
            while (true) {
                Socket socket = serverSocket.accept();

                new Thread(new ClientHandler(clientHandlers, socket, this)).start();
            }
        } catch (IOException e) {
            closeServerSocket();
        }
    }

    public void closeServerSocket() {
        try {
            if (serverSocket != null)
                serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendBroadcastMessage() {
        new Thread(() -> {
            while (true) {
                System.out.print("Broadcast message: ");
                String message = this.jsonHelper.toJson(new Response<>(ResponseStatus.OK, RequestType.BROADCAST_MESSAGE, new Scanner(System.in).nextLine()));

                byte[] buf = message.getBytes();
                try {
                    datagramSocket.send(new DatagramPacket(buf, buf.length, InetAddress.getByName(MAIN_GROUP_IP), 4446));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void main(String[] args) throws IOException {
        DatagramSocket datagramSocket = new DatagramSocket(4445);
        Server server = new Server(new ServerSocket(2048), datagramSocket);
        server.sendBroadcastMessage();
        server.startServer();
    }

}