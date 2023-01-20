package edu.estg.server;

import com.google.gson.Gson;
import edu.estg.utils.LocalNodeStatistics;
import edu.estg.utils.RequestType;
import edu.estg.utils.Response;
import edu.estg.utils.ResponseStatus;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Server {
    public static final String MAIN_GROUP_IP = "224.0.0.2";
    public static final String PASSENGER_GROUP_IP = "224.0.1.0";
    public static final String LOCAL_NODE_GROUP_IP = "224.0.2.0";
    private final ServerSocket serverSocket;
    private final DatagramSocket datagramSocket;
    private final ArrayListSync<ClientHandler> clientHandlers;
    protected ArrayList<LocalNodeStatistics> localNodeStatistics;
    protected final Semaphore semaphore;

    public Server(ServerSocket serverSocket, DatagramSocket datagramSocket) {
        this.clientHandlers = new ArrayListSync<>();
        this.serverSocket = serverSocket;
        this.datagramSocket = datagramSocket;
        this.localNodeStatistics = new ArrayList<>();
        this.semaphore = new Semaphore(1);
    }

    public void startServer() {
        System.out.println("Central node is online!");
        try {
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected! -> " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort());

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

    public void sendMulticastPassengerMessage(String messageToSend) {
        new Thread(() -> {

            byte[] buf = messageToSend.getBytes();
            try {
                datagramSocket.send(new DatagramPacket(buf, buf.length, InetAddress.getByName(PASSENGER_GROUP_IP), 4446));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }).start();
    }

    public void sendMulticastLocalNodeMessage(String messageToSend) {
        new Thread(() -> {

            byte[] buf = messageToSend.getBytes();
            try {
                datagramSocket.send(new DatagramPacket(buf, buf.length, InetAddress.getByName(LOCAL_NODE_GROUP_IP), 4446));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }).start();
    }

    private void getStatistics() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(30000); // Sleep 30 seconds

                    System.out.println("STATISTICS");
                    Response<Object> response = new Response<>(ResponseStatus.OK, RequestType.STATISTICS_REQUEST);
                    sendMulticastLocalNodeMessage(new Gson().toJson(response));


                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                if (!this.localNodeStatistics.isEmpty()) {
                    for (LocalNodeStatistics localNodeStatistic : this.localNodeStatistics) {
                        System.out.println("\nStatistics:\n" + localNodeStatistic + "\n");
                    }
                } else {
                    System.out.println("\nStatistics:\nNothing to show.\n");
                }
            }

        }).start();
    }

    public static void main(String[] args) throws IOException {
        DatagramSocket datagramSocket = new DatagramSocket(4445);
        Server server = new Server(new ServerSocket(2048), datagramSocket);
        server.getStatistics();
        server.startServer();
    }

}
