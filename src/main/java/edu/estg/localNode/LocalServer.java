package edu.estg.localNode;

import edu.estg.localNode.UI.StartFrame;

import java.io.*;
import java.net.MulticastSocket;
import java.net.Socket;
import java.nio.channels.NotYetConnectedException;

import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.showMessageDialog;

public class LocalServer {
    private MulticastSocket multicastSocket;
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private final StartFrame startFrame;

    public LocalServer(MulticastSocket multicastSocket, String host, int port) throws IOException {
        try {
            this.socket = new Socket(host, port);
            this.multicastSocket = multicastSocket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            this.startFrame = new StartFrame(this);
            startFrame.configFrame();
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
            throw new NotYetConnectedException();
        }
    }

    public void sendMessage(String message) {
        new Thread(() -> {
            if (message != null) {
                try {
                    bufferedWriter.write(message);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                } catch (IOException e) {
                    closeEverything(socket, bufferedReader, bufferedWriter);
                }
            }
        }).start();
    }

    public void receiveMessage() {
        new Thread(() -> {
            while (true) {
                try {
                    String responseMessage = bufferedReader.readLine();

                    if (responseMessage == null) closeEverything(socket, bufferedReader, bufferedWriter);

                    System.out.println(responseMessage);

                    this.startFrame.processResponse(responseMessage);

                    System.out.println(responseMessage);
                } catch (IOException e) {
                    closeEverything(socket, bufferedReader, bufferedWriter);
                    break;
                }
            }
        }).start();
    }

    private void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try {
            if (bufferedReader != null) bufferedReader.close();
            if (bufferedWriter != null) bufferedWriter.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            MulticastSocket multicastSocket = new MulticastSocket(4446);
            LocalServer localServer = new LocalServer(multicastSocket, "localhost", 2048);
            localServer.receiveMessage();
        } catch (NotYetConnectedException | IOException e) {
            showMessageDialog(null, "Not connected", "", ERROR_MESSAGE);
        }
    }
}
